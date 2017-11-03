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

package org.arakhne.afc.math.geometry.d3.d;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.d3.GeomFactory3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.arakhne.afc.math.geometry.d3.UnmodifiablePoint3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** 3D Point with 3 double precision floating-point numbers.
 *
 * @author $Author: sgalland$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Point3d extends Tuple3d<Point3d> implements Point3D<Point3d, Vector3d> {

	private static final long serialVersionUID = -8318943957531471869L;

	/** Construct a zero point.
     */
	public Point3d() {
		//
	}

	/** Constructor.
	 * @param tuple is the tuple to copy.
	 */
	public Point3d(Tuple3D<?> tuple) {
		super(tuple);
	}

	/** Constructor.
	 * @param tuple is the tuple to copy.
	 */
	public Point3d(int[] tuple) {
		super(tuple);
	}

	/** Constructor.
	 * @param tuple is the tuple to copy.
	 */
	public Point3d(double[] tuple) {
		super(tuple);
	}

	/** Construct a point with the given coordinates.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param z z coordinate.
     */
	public Point3d(int x, int y, int z) {
		super(x, y, z);
	}

	/** Construct a point with the given coordinates.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param z z coordinate.
     */
	public Point3d(float x, float y, float z) {
		super(x, y, z);
	}

	/** Construct a point with the given coordinates.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param z z coordinate.
     */
	public Point3d(double x, double y, double z) {
		super(x, y, z);
	}

	/** Construct a point with the given coordinates.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param z z coordinate.
     */
	public Point3d(long x, long y, long z) {
		super(x, y, z);
	}

	/** Convert the given tuple to a real Point3d.
	 *
	 * <p>If the given tuple is already a Point3d, it is replied.
	 *
	 * @param tuple the tuple.
	 * @return the Point3d.
	 * @since 14.0
	 */
	public static Point3d convert(Tuple3D<?> tuple) {
		if (tuple instanceof Point3d) {
			return (Point3d) tuple;
		}
		return new Point3d(tuple.getX(), tuple.getY(), tuple.getZ());
	}

	@Override
	public GeomFactory3d getGeomFactory() {
		return GeomFactory3d.SINGLETON;
	}

	@Pure
	@Override
	public double getDistanceSquared(Point3D<?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		final double dx = this.x - pt.getX();
		final double dy = this.y - pt.getY();
		final double dz = this.z - pt.getZ();
        return dx * dx + dy * dy + dz * dz;
	}

	@Pure
	@Override
	public double getDistance(Point3D<?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		final double dx = this.x - pt.getX();
		final double dy = this.y - pt.getY();
		final double dz = this.z - pt.getZ();
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}

	@Pure
	@Override
	public double getDistanceL1(Point3D<?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
        return Math.abs(this.x - pt.getX()) + Math.abs(this.y - pt.getY()) + Math.abs(this.z - pt.getZ());
	}

	@Pure
	@Override
	public double getDistanceLinf(Point3D<?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
        return MathUtil.max(Math.abs(this.x - pt.getX()), Math.abs(this.y - pt.getY()), Math.abs(this.z - pt.getZ()));
	}

	@Pure
	@Override
	public int getIdistanceL1(Point3D<?, ?> pt) {
		return (int) getDistanceL1(pt);
	}

	@Pure
	@Override
	public int getIdistanceLinf(Point3D<?, ?> pt) {
		return (int) getDistanceLinf(pt);
	}

	@Override
	public void add(Point3D<?, ?> point, Vector3D<?, ?> vector) {
		assert point != null : AssertMessages.notNullParameter(0);
		assert vector != null : AssertMessages.notNullParameter(1);
		this.x = point.getX() + vector.getX();
		this.y = point.getY() + vector.getY();
		this.z = point.getZ() + vector.getZ();
	}

	@Override
	public void add(Vector3D<?, ?> vector, Point3D<?, ?> point) {
		assert point != null : AssertMessages.notNullParameter(0);
		assert vector != null : AssertMessages.notNullParameter(1);
		this.x = vector.getX() + point.getX();
		this.y = vector.getY() + point.getY();
		this.z = vector.getZ() + point.getZ();
	}

	@Override
	public void add(Vector3D<?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		this.x = this.x + vector.getX();
		this.y = this.y + vector.getY();
		this.z = this.z + vector.getZ();
	}

	@Override
	public void scaleAdd(int scale, Vector3D<?, ?> vector, Point3D<?, ?> point) {
		assert point != null : AssertMessages.notNullParameter(1);
		assert vector != null : AssertMessages.notNullParameter(2);
		this.x = scale * vector.getX() + point.getX();
		this.y = scale * vector.getY() + point.getY();
		this.z = scale * vector.getZ() + point.getZ();
	}

	@Override
	public void scaleAdd(double scale, Vector3D<?, ?> vector, Point3D<?, ?> point) {
		assert point != null : AssertMessages.notNullParameter(1);
		assert vector != null : AssertMessages.notNullParameter(2);
		this.x = scale * vector.getX() + point.getX();
		this.y = scale * vector.getY() + point.getY();
		this.z = scale * vector.getZ() + point.getZ();
	}

	@Override
	public void scaleAdd(int scale, Point3D<?, ?> point, Vector3D<?, ?> vector) {
		assert point != null : AssertMessages.notNullParameter(1);
		assert vector != null : AssertMessages.notNullParameter(2);
		this.x = scale * point.getX() + vector.getX();
		this.y = scale * point.getY() + vector.getY();
		this.z = scale * point.getZ() + vector.getZ();
	}

	@Override
	public void scaleAdd(double scale, Point3D<?, ?> point, Vector3D<?, ?> vector) {
		assert point != null : AssertMessages.notNullParameter(1);
		assert vector != null : AssertMessages.notNullParameter(2);
		this.x = scale * point.getX() + vector.getX();
		this.y = scale * point.getY() + vector.getY();
		this.z = scale * point.getZ() + vector.getZ();
	}

	@Override
	public void scaleAdd(int scale, Vector3D<?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter(1);
		this.x = scale * this.x + vector.getX();
		this.y = scale * this.y + vector.getY();
		this.z = scale * this.z + vector.getZ();
	}

	@Override
	public void scaleAdd(double scale, Vector3D<?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter(1);
		this.x = scale * this.x + vector.getX();
		this.y = scale * this.y + vector.getY();
		this.z = scale * this.z + vector.getZ();
	}

	@Override
	public void sub(Point3D<?, ?> point, Vector3D<?, ?> vector) {
		assert point != null : AssertMessages.notNullParameter(0);
		assert vector != null : AssertMessages.notNullParameter(1);
		this.x = point.getX() - vector.getX();
		this.y = point.getY() - vector.getY();
		this.z = point.getZ() - vector.getZ();
	}

	@Override
	public void sub(Vector3D<?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		this.x = this.x - vector.getX();
		this.y = this.y - vector.getY();
		this.z = this.z - vector.getZ();
	}

	@Pure
	@Override
	public UnmodifiablePoint3D<Point3d, Vector3d> toUnmodifiable() {
		return new UnmodifiablePoint3D<Point3d, Vector3d>() {

			private static final long serialVersionUID = 3305735799920201356L;

			@Override
			public GeomFactory3D<Vector3d, Point3d> getGeomFactory() {
				return Point3d.this.getGeomFactory();
			}

			@Override
			@SuppressWarnings("checkstyle:superclone")
			public Point3d clone() {
				return Point3d.this.getGeomFactory().newPoint(Point3d.this.getX(), Point3d.this.getY(), Point3d.this.getZ());
			}

			@Override
			public double getX() {
				return Point3d.this.getX();
			}

			@Override
			public int ix() {
				return Point3d.this.ix();
			}

			@Override
			public double getY() {
				return Point3d.this.getY();
			}

			@Override
			public int iy() {
				return Point3d.this.iy();
			}

			@Override
			public double getZ() {
				return Point3d.this.getZ();
			}

			@Override
			public int iz() {
				return Point3d.this.iz();
			}

		};
	}

}
