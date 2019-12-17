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

package org.arakhne.afc.math.geometry.d3.i;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.d3.GeomFactory3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.arakhne.afc.math.geometry.d3.UnmodifiablePoint3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.arakhne.afc.vmutil.json.JsonBuffer;

/** 2D Point with 2 integer numbers.
 *
 * @author $Author: sgalland$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Point3i extends Tuple3i<Point3i> implements Point3D<Point3i, Vector3i> {

	private static final long serialVersionUID = -4977158382149954525L;

	/** Construct a zero point.
     */
	public Point3i() {
		//
	}

	/** Constructor.
	 * @param tuple is the tuple to copy.
	 */
	public Point3i(Tuple3D<?> tuple) {
		super(tuple);
	}

	/** Constructor by copy.
	 * @param tuple is the tuple to copy.
	 */
	public Point3i(int[] tuple) {
		super(tuple);
	}

	/** Constructor by copy.
	 * @param tuple is the tuple to copy.
	 */
	public Point3i(double[] tuple) {
		super(tuple);
	}

	/** Construct a point with the given coordinates.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param z z coordinate.
     */
	public Point3i(int x, int y, int z) {
		super(x, y, z);
	}

	/** Construct a point with the given coordinates.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param z z coordinate.
     */
	public Point3i(float x, float y, float z) {
		super(x, y, z);
	}

	/** Construct a point with the given coordinates.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param z z coordinate.
     */
	public Point3i(double x, double y, double z) {
		super(x, y, z);
	}

	/** Construct a point with the given coordinates.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param z z coordinate.
     */
	public Point3i(long x, long y, long z) {
		super(x, y, z);
	}

	/** Convert the given tuple to a real Point2i.
	 *
	 * <p>If the given tuple is already a Point2i, it is replied.
	 *
	 * @param tuple the tuple.
	 * @return the Point2i.
	 * @since 14.0
	 */
	public static Point3i convert(Tuple3D<?> tuple) {
		if (tuple instanceof Point3i) {
			return (Point3i) tuple;
		}
		return new Point3i(tuple.getX(), tuple.getY(), tuple.getZ());
	}

	@Pure
	@Override
	public double getDistanceSquared(Point3D<?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		final double dx = this.x - point.getX();
		final double dy = this.y - point.getY();
		final double dz = this.z - point.getZ();
		return dx * dx + dy * dy + dz * dz;
	}

	@Pure
	@Override
	public double getDistance(Point3D<?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		final double dx = this.x - point.getX();
		final double dy = this.y - point.getY();
		final double dz = this.z - point.getZ();
		return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}

	@Pure
	@Override
	public double getDistanceL1(Point3D<?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		return Math.abs(this.x - point.getX()) + Math.abs(this.y - point.getY()) + Math.abs(this.z - point.getZ());
	}

	@Pure
	@Override
	public double getDistanceLinf(Point3D<?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		return MathUtil.max(Math.abs(this.x - point.getX()), Math.abs(this.y - point.getY()), Math.abs(this.z - point.getZ()));
	}

	@Pure
	@Override
	public int getIdistanceL1(Point3D<?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		return (int) Math.round(getDistanceL1(point));
	}

	@Pure
	@Override
	public int getIdistanceLinf(Point3D<?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		return (int) Math.round(getDistanceLinf(point));
	}

	@Override
	public void add(Point3D<?, ?> point, Vector3D<?, ?> vector) {
		assert point != null : AssertMessages.notNullParameter(0);
		assert vector != null : AssertMessages.notNullParameter(1);
		this.x = (int) Math.round(point.getX() + vector.getX());
		this.y = (int) Math.round(point.getY() + vector.getY());
	}

	@Override
	public void add(Vector3D<?, ?> vector, Point3D<?, ?> point) {
		assert point != null : AssertMessages.notNullParameter(0);
		assert vector != null : AssertMessages.notNullParameter(1);
		this.x = (int) Math.round(vector.getX() + point.getX());
		this.y = (int) Math.round(vector.getY() + point.getY());
		this.z = (int) Math.round(vector.getZ() + point.getZ());
	}

	@Override
	public void add(Vector3D<?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		this.x = (int) Math.round(this.x + vector.getX());
		this.y = (int) Math.round(this.y + vector.getY());
		this.z = (int) Math.round(this.z + vector.getZ());
	}

	@Override
	public void scaleAdd(int scale, Vector3D<?, ?> vector, Point3D<?, ?> point) {
		assert point != null : AssertMessages.notNullParameter(0);
		assert vector != null : AssertMessages.notNullParameter(1);
		this.x = (int) Math.round(scale * vector.getX() + point.getX());
		this.y = (int) Math.round(scale * vector.getY() + point.getY());
		this.z = (int) Math.round(scale * vector.getZ() + point.getZ());
	}

	@Override
	public void scaleAdd(double scale, Vector3D<?, ?> vector, Point3D<?, ?> point) {
		assert point != null : AssertMessages.notNullParameter(0);
		assert vector != null : AssertMessages.notNullParameter(1);
		this.x = (int) Math.round(scale * vector.getX() + point.getX());
		this.y = (int) Math.round(scale * vector.getY() + point.getY());
		this.z = (int) Math.round(scale * vector.getZ() + point.getZ());
	}

	@Override
	public void scaleAdd(int scale, Point3D<?, ?> point, Vector3D<?, ?> vector) {
		assert point != null : AssertMessages.notNullParameter(0);
		assert vector != null : AssertMessages.notNullParameter(1);
		this.x = (int) Math.round(scale * point.getX() + vector.getX());
		this.y = (int) Math.round(scale * point.getY() + vector.getY());
		this.z = (int) Math.round(scale * point.getZ() + vector.getZ());
	}

	@Override
	public void scaleAdd(double scale, Point3D<?, ?> point, Vector3D<?, ?> vector) {
		assert point != null : AssertMessages.notNullParameter(0);
		assert vector != null : AssertMessages.notNullParameter(1);
		this.x = (int) Math.round(scale * point.getX() + vector.getX());
		this.y = (int) Math.round(scale * point.getY() + vector.getY());
		this.z = (int) Math.round(scale * point.getZ() + vector.getZ());
	}

	@Override
	public void scaleAdd(int scale, Vector3D<?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter(1);
		this.x = (int) Math.round(scale * this.x + vector.getX());
		this.y = (int) Math.round(scale * this.y + vector.getY());
		this.z = (int) Math.round(scale * this.z + vector.getZ());
	}

	@Override
	public void scaleAdd(double scale, Vector3D<?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter(1);
		this.x = (int) Math.round(scale * this.x + vector.getX());
		this.y = (int) Math.round(scale * this.y + vector.getY());
		this.z = (int) Math.round(scale * this.z + vector.getZ());
	}

	@Override
	public void sub(Point3D<?, ?> point, Vector3D<?, ?> vector) {
		assert point != null : AssertMessages.notNullParameter(0);
		assert vector != null : AssertMessages.notNullParameter(1);
		this.x = (int) Math.round(point.getX() - vector.getX());
		this.y = (int) Math.round(point.getY() - vector.getY());
		this.z = (int) Math.round(point.getZ() - vector.getZ());
	}

	@Override
	public void sub(Vector3D<?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		this.x = (int) Math.round(this.x - vector.getX());
		this.y = (int) Math.round(this.y - vector.getY());
		this.z = (int) Math.round(this.z - vector.getZ());
	}

	@Override
	public GeomFactory3i getGeomFactory() {
		return GeomFactory3i.SINGLETON;
	}

	@Pure
	@Override
	public UnmodifiablePoint3D<Point3i, Vector3i> toUnmodifiable() {
		return new UnmodifiablePoint3D<Point3i, Vector3i>() {

			private static final long serialVersionUID = -4844158582025788289L;

			@Override
			public GeomFactory3D<Vector3i, Point3i> getGeomFactory() {
				return Point3i.this.getGeomFactory();
			}

			@Override
			@SuppressWarnings("checkstyle:superclone")
			public Point3i clone() {
				return Point3i.this.getGeomFactory().newPoint(
						Point3i.this.ix(),
						Point3i.this.iy(),
						Point3i.this.iz());
			}

			@Override
			public double getX() {
				return Point3i.this.getX();
			}

			@Override
			public int ix() {
				return Point3i.this.ix();
			}

			@Override
			public double getY() {
				return Point3i.this.getY();
			}

			@Override
			public int iy() {
				return Point3i.this.iy();
			}

			@Override
			public double getZ() {
				return Point3i.this.getZ();
			}

			@Override
			public int iz() {
				return Point3i.this.iz();
			}

			@Override
			public String toString() {
				return Point3i.this.toString();
			}

			@Override
			public void toJson(JsonBuffer buffer) {
				Point3i.this.toJson(buffer);
			}

		};
	}

}
