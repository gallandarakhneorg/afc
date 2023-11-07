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

package org.arakhne.afc.math.geometry.d3.dfx;

import javafx.beans.property.DoubleProperty;

import org.arakhne.afc.math.geometry.d3.GeomFactory3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.arakhne.afc.math.geometry.d3.UnmodifiablePoint3D;
import org.arakhne.afc.vmutil.json.JsonBuffer;

/** 3D Point with 3 double precision floating-point FX properties.
 *
 * @author $Author: sgalland$
 * @author $Author: olamotte$
 * @author $Author: hjaffali$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Point3dfx extends Tuple3dfx<Point3dfx> implements Point3D<Point3dfx, Vector3dfx> {

	private static final long serialVersionUID = 2543935341943572211L;

	/** Construct a zero point.
     */
	public Point3dfx() {
		//
	}

	/** Construct a point with the given properties for its coordinates.
     * @param x property for the x coordinate.
     * @param y property for the y coordinate.
     * @param z property for the z coordinate.
     */
	public Point3dfx(DoubleProperty x, DoubleProperty y, DoubleProperty z) {
		super(x, y, z);
	}

	/** Constructor by copy.
     * @param tuple is the tuple to copy.
     */
	public Point3dfx(Tuple3D<?> tuple) {
		super(tuple);
	}

	/** Constructor by copy.
     * @param tuple is the tuple to copy.
     */
	public Point3dfx(int[] tuple) {
		super(tuple);
	}

	/** Constructor by copy.
     * @param tuple is the tuple to copy.
     */
	public Point3dfx(double[] tuple) {
		super(tuple);
	}

	/** Construct a point with the given coordinates.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param z z coordinate.
     */
	public Point3dfx(int x, int y, int z) {
		super(x, y, z);
	}

	/** Construct a point with the given coordinates.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param z z coordinate.
     */
	public Point3dfx(float x, float y, float z) {
		super(x, y, z);
	}

	/** Construct a point with the given coordinates.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param z z coordinate.
     */
	public Point3dfx(double x, double y, double z) {
		super(x, y, z);
	}

	/** Construct a point with the given coordinates.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param z z coordinate.
     */
	public Point3dfx(long x, long y, long z) {
		super(x, y, z);
	}

	/** Convert the given tuple to a real Point3dfx.
	 *
	 * <p>If the given tuple is already a Point3dfx, it is replied.
	 *
	 * @param tuple the tuple.
	 * @return the Point3dfx.
	 * @since 14.0
	 */
	public static Point3dfx convert(Tuple3D<?> tuple) {
		if (tuple instanceof Point3dfx) {
			return (Point3dfx) tuple;
		}
		return new Point3dfx(tuple.getX(), tuple.getY(), tuple.getZ());
	}

	@Override
	public GeomFactory3dfx getGeomFactory() {
		return GeomFactory3dfx.SINGLETON;
	}

	@Override
	public UnmodifiablePoint3D<Point3dfx, Vector3dfx> toUnmodifiable() {
		return new UnmodifiablePoint3D<>() {

			private static final long serialVersionUID = 5419032367247268556L;

			@Override
			public GeomFactory3D<Vector3dfx, Point3dfx> getGeomFactory() {
				return Point3dfx.this.getGeomFactory();
			}

			@Override
			@SuppressWarnings("checkstyle:superclone")
			public Point3dfx clone() {
				return Point3dfx.this.getGeomFactory().newPoint(
						Point3dfx.this.getX(),
						Point3dfx.this.getY(),
						Point3dfx.this.getZ());
			}

			@Override
			public int iy() {
				return Point3dfx.this.iy();
			}

			@Override
			public int ix() {
				return Point3dfx.this.ix();
			}

			@Override
			public int iz() {
				return Point3dfx.this.iz();
			}

			@Override
			public double getY() {
				return Point3dfx.this.getY();
			}

			@Override
			public double getX() {
				return Point3dfx.this.getX();
			}

			@Override
			public double getZ() {
				return Point3dfx.this.getZ();
			}

			@Override
			public String toString() {
				return Point3dfx.this.toString();
			}

			@Override
			public void toJson(JsonBuffer buffer) {
				Point3dfx.this.toJson(buffer);
			}

		};
	}

}
