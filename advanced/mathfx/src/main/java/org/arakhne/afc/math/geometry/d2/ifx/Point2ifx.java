/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

import javafx.beans.property.IntegerProperty;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.GeomFactory2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.UnmodifiablePoint2D;
import org.arakhne.afc.vmutil.json.JsonBuffer;

/** 2D Point with 2 integer FX properties.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Point2ifx extends Tuple2ifx<Point2ifx> implements Point2D<Point2ifx, Vector2ifx> {

	private static final long serialVersionUID = -1421615416984636660L;

	/** Construct a zero point.
	 */
	public Point2ifx() {
		//
	}

	/** Construct a point with the given properties for its coordinates.
	 * @param x property for the x coordinate.
	 * @param y property for the y coordinate.
	 */
	public Point2ifx(IntegerProperty x, IntegerProperty y) {
		super(x, y);
	}

	/** Constructor by copy.
	 * @param tuple is the tuple to copy.
	 */
	public Point2ifx(Tuple2D<?> tuple) {
		super(tuple);
	}

	/** Constructor by copy.
	 * @param tuple is the tuple to copy.
	 */
	public Point2ifx(int[] tuple) {
		super(tuple);
	}

	/** Constructor by copy.
	 * @param tuple is the tuple to copy.
	 */
	public Point2ifx(double[] tuple) {
		super(tuple);
	}

	/** Construct a point with the given coordinates.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Point2ifx(int x, int y) {
		super(x, y);
	}

	/** Construct a point with the given coordinates.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Point2ifx(float x, float y) {
		super(x, y);
	}

	/** Construct a point with the given coordinates.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Point2ifx(double x, double y) {
		super(x, y);
	}

	/** Construct a point with the given coordinates.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Point2ifx(long x, long y) {
		super(x, y);
	}

	/** Convert the given tuple to a real Point2ifx.
	 *
	 * <p>If the given tuple is already a Point2ifx, it is replied.
	 *
	 * @param tuple the tuple.
	 * @return the Point2ifx.
	 * @since 14.0
	 */
	public static Point2ifx convert(Tuple2D<?> tuple) {
		if (tuple instanceof Point2ifx) {
			return (Point2ifx) tuple;
		}
		return new Point2ifx(tuple.getX(), tuple.getY());
	}

	@Override
	public GeomFactory2ifx getGeomFactory() {
		return GeomFactory2ifx.SINGLETON;
	}

	@Pure
	@Override
	public UnmodifiablePoint2D<Point2ifx, Vector2ifx> toUnmodifiable() {
		return new UnmodifiablePoint2D<>() {

			private static final long serialVersionUID = -8264299960804312720L;

			@Override
			public GeomFactory2D<Vector2ifx, Point2ifx> getGeomFactory() {
				return Point2ifx.this.getGeomFactory();
			}

			@Override
			@SuppressWarnings("checkstyle:superclone")
			public Point2ifx clone() {
				return Point2ifx.this.getGeomFactory().newPoint(
						Point2ifx.this.ix(),
						Point2ifx.this.iy());
			}

			@Override
			public double getX() {
				return Point2ifx.this.getX();
			}

			@Override
			public int ix() {
				return Point2ifx.this.ix();
			}

			@Override
			public double getY() {
				return Point2ifx.this.getY();
			}

			@Override
			public int iy() {
				return Point2ifx.this.iy();
			}

			@Override
			public String toString() {
				return Point2ifx.this.toString();
			}

			@Override
			public void toJson(JsonBuffer buffer) {
				Point2ifx.this.toJson(buffer);
			}

		};
	}

}
