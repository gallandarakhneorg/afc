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

package org.arakhne.afc.math.geometry.d3.d;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d3.GeomFactory3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.arakhne.afc.math.geometry.d3.UnmodifiableVector3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.arakhne.afc.vmutil.json.JsonBuffer;

/** 3D Vector with 3 double precision floating-point numbers.
 *
 * @author $Author: sgalland$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Vector3d extends Tuple3d<Vector3d> implements Vector3D<Vector3d, Point3d> {

	private static final long serialVersionUID = 9183440606977893371L;

	/** Construct a zero vector.
     */
	public Vector3d() {
		//
	}

	/** Constructor by copy.
     * @param tuple is the tuple to copy.
     */
	public Vector3d(Tuple3D<?> tuple) {
		super(tuple);
	}

	/** Constructor by copy.
     * @param tuple is the tuple to copy.
     */
	public Vector3d(int[] tuple) {
		super(tuple);
	}

	/** Constructor by copy.
     * @param tuple is the tuple to copy.
     */
	public Vector3d(double[] tuple) {
		super(tuple);
	}

	/** Construct a vector with the given coordinates.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param z z coordinate.
     */
	public Vector3d(int x, int y, int z) {
		super(x, y, z);
	}

	/** Construct a vector with the given coordinates.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param z z coordinate.
     */
	public Vector3d(float x, float y, float z) {
		super(x, y, z);
	}

	/** Construct a vector with the given coordinates.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param z z coordinate.
     */
	public Vector3d(double x, double y, double z) {
		super(x, y, z);
	}

	/** Construct a vector with the given coordinates.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param z z coordinate.
     */
	public Vector3d(long x, long y, long z) {
		super(x, y, z);
	}

	/** Convert the given tuple to a real Vector3d.
	 *
	 * <p>If the given tuple is already a Vector3d, it is replied.
	 *
	 * @param tuple the tuple.
	 * @return the Vector3d.
	 * @since 14.0
	 */
	public static Vector3d convert(Tuple3D<?> tuple) {
		if (tuple instanceof Vector3d) {
			return (Vector3d) tuple;
		}
		return new Vector3d(tuple.getX(), tuple.getY(), tuple.getZ());
	}

	@Override
	public GeomFactory3d getGeomFactory() {
		return GeomFactory3d.SINGLETON;
	}

	@Pure
	@Override
	public double dot(Vector3D<?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		return this.x * vector.getX() + this.y * vector.getY() + this.z * vector.getZ();
	}

	@Pure
	@Override
	public double perp(Vector3D<?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
        return this.x * vector.getY() + this.y * vector.getZ() + this.z * vector.getX() - this.z * vector.getY()
                - this.x * vector.getZ() - this.y * vector.getX();
	}

	@Pure
	@Override
	public double getLength() {
		return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
	}

	@Pure
	@Override
	public double getLengthSquared() {
		return this.x * this.x + this.y * this.y + this.z * this.z;
	}

	@Override
	public void add(Vector3D<?, ?> vector1, Vector3D<?, ?> vector2) {
		assert vector1 != null : AssertMessages.notNullParameter(0);
		assert vector2 != null : AssertMessages.notNullParameter(1);
		this.x = vector1.getX() + vector2.getX();
		this.y = vector1.getY() + vector2.getY();
		this.z = vector1.getZ() + vector2.getZ();
	}

	@Override
	public void add(Vector3D<?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		this.x = this.x + vector.getX();
		this.y = this.y + vector.getY();
		this.z = this.z + vector.getZ();
	}

	@Override
	public void scaleAdd(int scale, Vector3D<?, ?> vector1, Vector3D<?, ?> vector2) {
		assert vector1 != null : AssertMessages.notNullParameter(1);
		assert vector2 != null : AssertMessages.notNullParameter(2);
		this.x = scale * vector1.getX() + vector2.getX();
		this.y = scale * vector1.getY() + vector2.getY();
		this.z = scale * vector1.getZ() + vector2.getZ();
	}

	@Override
	public void scaleAdd(double scale, Vector3D<?, ?> vector1, Vector3D<?, ?> vector2) {
		assert vector1 != null : AssertMessages.notNullParameter(1);
		assert vector2 != null : AssertMessages.notNullParameter(2);
		this.x = scale * vector1.getX() + vector2.getX();
		this.y = scale * vector1.getY() + vector2.getY();
		this.z = scale * vector1.getZ() + vector2.getZ();
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
	public void sub(Vector3D<?, ?> vector1, Vector3D<?, ?> vector2) {
		assert vector1 != null : AssertMessages.notNullParameter(0);
		assert vector2 != null : AssertMessages.notNullParameter(1);
		this.x = vector1.getX() - vector2.getX();
		this.y = vector1.getY() - vector2.getY();
		this.z = vector1.getZ() - vector2.getZ();
	}

	@Override
	public void sub(Point3D<?, ?> point1, Point3D<?, ?> point2) {
		assert point1 != null : AssertMessages.notNullParameter(0);
		assert point2 != null : AssertMessages.notNullParameter(1);
		this.x = point1.getX() - point2.getX();
		this.y = point1.getY() - point2.getY();
		this.z = point1.getZ() - point2.getZ();
	}

	@Override
	public void sub(Vector3D<?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		this.x -= vector.getX();
		this.y -= vector.getY();
		this.z -= vector.getZ();
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

	@Pure
	@Override
	public UnmodifiableVector3D<Vector3d, Point3d> toUnmodifiable() {
		return new UnmodifiableVector3D<Vector3d, Point3d>() {

			private static final long serialVersionUID = 6848610371671516804L;

			@Override
			public GeomFactory3D<Vector3d, Point3d> getGeomFactory() {
				return Vector3d.this.getGeomFactory();
			}

			@Override
			public Vector3d toUnitVector() {
				return Vector3d.this.toUnitVector();
			}

			@Override
			@SuppressWarnings("checkstyle:superclone")
			public Vector3d clone() {
				return Vector3d.this.getGeomFactory().newVector(Vector3d.this.getX(), Vector3d.this.getY(), Vector3d.this.getZ());
			}

			@Override
			public double getX() {
				return Vector3d.this.getX();
			}

			@Override
			public int ix() {
				return Vector3d.this.ix();
			}

			@Override
			public double getY() {
				return Vector3d.this.getY();
			}

			@Override
			public int iy() {
				return Vector3d.this.iy();
			}

			@Override
			public double getZ() {
				return Vector3d.this.getZ();
			}

			@Override
			public int iz() {
				return Vector3d.this.iz();
			}

			@Override
			public String toString() {
				return Vector3d.this.toString();
			}

			@Override
			public void toJson(JsonBuffer buffer) {
				Vector3d.this.toJson(buffer);
			}

		};
	}

}
