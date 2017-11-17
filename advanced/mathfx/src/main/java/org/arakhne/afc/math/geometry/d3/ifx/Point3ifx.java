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

package org.arakhne.afc.math.geometry.d3.ifx;

import javafx.beans.property.IntegerProperty;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d3.GeomFactory3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.arakhne.afc.math.geometry.d3.UnmodifiablePoint3D;
import org.arakhne.afc.vmutil.json.JsonBuffer;

/** 3D Point with 3 integer FX properties.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Point3ifx extends Tuple3ifx<Point3ifx> implements Point3D<Point3ifx, Vector3ifx> {

	private static final long serialVersionUID = -1421615416984636660L;

	/** Construct a zero point.
     */
	public Point3ifx() {
		//
	}

	/** Construct a point with the given properties for its coordinates.
     * @param x property for the x coordinate.
     * @param y property for the y coordinate.
     * @param z property for the z coordinate.
     */
	public Point3ifx(IntegerProperty x, IntegerProperty y, IntegerProperty z) {
		super(x, y, z);
	}

	/** Constructor by copy.
     * @param tuple is the tuple to copy.
     */
	public Point3ifx(Tuple3D<?> tuple) {
		super(tuple);
	}

	/** Constructor by copy.
     * @param tuple is the tuple to copy.
     */
	public Point3ifx(int[] tuple) {
		super(tuple);
	}

	/** Constructor by copy.
     * @param tuple is the tuple to copy.
     */
	public Point3ifx(double[] tuple) {
		super(tuple);
	}

	/** Construct a point with the given coordinates.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param z z coordinate.
     */
	public Point3ifx(int x, int y, int z) {
		super(x, y, z);
	}

	/** Construct a point with the given coordinates.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param z z coordinate.
     */
	public Point3ifx(float x, float y, float z) {
		super(x, y, z);
	}

	/** Construct a point with the given coordinates.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param z z coordinate.
     */
	public Point3ifx(double x, double y, double z) {
		super(x, y, z);
	}

	/** Construct a point with the given coordinates.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param z z coordinate.
     */
	public Point3ifx(long x, long y, long z) {
		super(x, y, z);
	}

	/** Convert the given tuple to a real Point3ifx.
	 *
	 * <p>If the given tuple is already a Point3ifx, it is replied.
	 *
	 * @param tuple the tuple.
	 * @return the Point3ifx.
	 * @since 14.0
	 */
	public static Point3ifx convert(Tuple3D<?> tuple) {
		if (tuple instanceof Point3ifx) {
			return (Point3ifx) tuple;
		}
		return new Point3ifx(tuple.getX(), tuple.getY(), tuple.getZ());
	}

	@Override
	public GeomFactory3ifx getGeomFactory() {
		return GeomFactory3ifx.SINGLETON;
	}

	@Pure
	@Override
	public UnmodifiablePoint3D<Point3ifx, Vector3ifx> toUnmodifiable() {
		return new UnmodifiablePoint3D<Point3ifx, Vector3ifx>() {

			private static final long serialVersionUID = -8264299960804312720L;

			@Override
			public GeomFactory3D<Vector3ifx, Point3ifx> getGeomFactory() {
				return Point3ifx.this.getGeomFactory();
			}

			@Override
			@SuppressWarnings("checkstyle:superclone")
			public Point3ifx clone() {
				return Point3ifx.this.getGeomFactory().newPoint(
						Point3ifx.this.ix(),
						Point3ifx.this.iy(),
						Point3ifx.this.iz());
			}

			@Override
			public double getX() {
				return Point3ifx.this.getX();
			}

			@Override
			public int ix() {
				return Point3ifx.this.ix();
			}

			@Override
			public double getY() {
				return Point3ifx.this.getY();
			}

			@Override
			public int iy() {
				return Point3ifx.this.iy();
			}

			@Override
			public double getZ() {
				return Point3ifx.this.getZ();
			}

			@Override
			public int iz() {
				return Point3ifx.this.iz();
			}

			@Override
			public String toString() {
				return Point3ifx.this.toString();
			}

			@Override
			public void toJson(JsonBuffer buffer) {
				Point3ifx.this.toJson(buffer);
			}

		};
	}

}
