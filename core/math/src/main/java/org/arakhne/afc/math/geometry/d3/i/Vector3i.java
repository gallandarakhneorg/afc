/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
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

import org.arakhne.afc.math.geometry.d3.GeomFactory3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.arakhne.afc.math.geometry.d3.UnmodifiableVector3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;

/** 2D Vector with 2 integer numbers.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Vector3i extends Tuple3i<Vector3i> implements Vector3D<Vector3i, Point3i> {

	private static final long serialVersionUID = -7228108517874845303L;

	/** Construct a zero vector.
     */
	public Vector3i() {
		//
	}

	/** Constructor by copy.
     * @param tuple is the tuple to copy.
     */
	public Vector3i(Tuple3D<?> tuple) {
		super(tuple);
	}

	/** Constructor by copy.
     * @param tuple is the tuple to copy.
     */
	public Vector3i(int[] tuple) {
		super(tuple);
	}

	/** Constructor by copy.
     * @param tuple is the tuple to copy.
     */
	public Vector3i(double[] tuple) {
		super(tuple);
	}

	/** Constructor a vector with the given coordinates.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param z z coordinate.
     */
	public Vector3i(int x, int y, int z) {
		super(x, y, z);
	}

	/** Constructor a vector with the given coordinates.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param z z coordinate.
     */
	public Vector3i(float x, float y, float z) {
		super(x, y, z);
	}

	/** Constructor a vector with the given coordinates.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param z z coordinate.
     */
	public Vector3i(double x, double y, double z) {
		super(x, y, z);
	}

	/** Constructor a vector with the given coordinates.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param z z coordinate.
     */
	public Vector3i(long x, long y, long z) {
		super(x, y, z);
	}

	@Pure
	@Override
	public double dot(Vector3D<?, ?> vector) {
		assert vector != null : "Vector must not be null";
        return this.x * vector.getX() + this.y * vector.getY() + this.z * vector.getZ();
	}

	@Pure
	@Override
	public double perp(Vector3D<?, ?> vector) {
		assert vector != null : "Vector must not be null";
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
		assert vector1 != null : "First vector must not be null";
		assert vector2 != null : "Second vector must not be null";
		this.x = (int) Math.round(vector1.getX() + vector2.getX());
		this.y = (int) Math.round(vector1.getY() + vector2.getY());
		this.z = (int) Math.round(vector1.getZ() + vector2.getZ());
	}

	@Override
	public void add(Vector3D<?, ?> vector) {
		assert vector != null : "Vector must not be null";
		this.x = (int) Math.round(this.x + vector.getX());
		this.y = (int) Math.round(this.y + vector.getY());
		this.z = (int) Math.round(this.z + vector.getZ());
	}

	@Override
	public void scaleAdd(int scale, Vector3D<?, ?> vector1, Vector3D<?, ?> vector2) {
		assert vector1 != null : "First vector must not be null";
		assert vector2 != null : "Second vector must not be null";
		this.x = (int) Math.round(scale * vector1.getX() + vector2.getX());
		this.y = (int) Math.round(scale * vector1.getY() + vector2.getY());
		this.z = (int) Math.round(scale * vector1.getZ() + vector2.getZ());
	}

	@Override
	public void scaleAdd(double scale, Vector3D<?, ?> vector1, Vector3D<?, ?> vector2) {
		assert vector1 != null : "First vector must not be null";
		assert vector2 != null : "Second vector must not be null";
		this.x = (int) Math.round(scale * vector1.getX() + vector2.getX());
		this.y = (int) Math.round(scale * vector1.getY() + vector2.getY());
		this.z = (int) Math.round(scale * vector1.getZ() + vector2.getZ());
	}

	@Override
	public void scaleAdd(int scale, Vector3D<?, ?> vector) {
		assert vector != null : "Vector must not be null";
		this.x = (int) Math.round(scale * this.x + vector.getX());
		this.y = (int) Math.round(scale * this.y + vector.getY());
		this.z = (int) Math.round(scale * this.z + vector.getZ());
	}

	@Override
	public void scaleAdd(double scale, Vector3D<?, ?> vector) {
		assert vector != null : "Vector must not be null";
		this.x = (int) Math.round(scale * this.x + vector.getX());
		this.y = (int) Math.round(scale * this.y + vector.getY());
		this.z = (int) Math.round(scale * this.z + vector.getZ());
	}

	@Override
	public void sub(Vector3D<?, ?> vector1, Vector3D<?, ?> vector2) {
		assert vector1 != null : "First vector must not be null";
		assert vector2 != null : "Second vector must not be null";
		this.x = (int) Math.round(vector1.getX() - vector2.getX());
		this.y = (int) Math.round(vector1.getY() - vector2.getY());
		this.z = (int) Math.round(vector1.getZ() - vector2.getZ());
	}

	@Override
	public void sub(Point3D<?, ?> point1, Point3D<?, ?> point2) {
		assert point1 != null : "First point must not be null";
		assert point2 != null : "Second point must not be null";
		this.x = (int) Math.round(point1.getX() - point2.getX());
		this.y = (int) Math.round(point1.getY() - point2.getY());
		this.z = (int) Math.round(point1.getZ() - point2.getZ());
	}

	@Override
	public void sub(Vector3D<?, ?> vector) {
		assert vector != null : "Vector must not be null";
		this.x = (int) Math.round(this.x - vector.getX());
		this.y = (int) Math.round(this.y - vector.getY());
		this.z = (int) Math.round(this.z - vector.getZ());
	}

	@Override
	public void setLength(double newLength) {
		final double l = getLength();
		if (l != 0) {
			final double f = newLength / l;
			this.x = (int) Math.round(this.x * f);
			this.y = (int) Math.round(this.y * f);
			this.z = (int) Math.round(this.z * f);
		} else {
			this.x = (int) Math.round(newLength);
			this.y = 0;
		}
	}

	@Override
	public GeomFactory3i getGeomFactory() {
		return GeomFactory3i.SINGLETON;
	}

	@Pure
	@Override
	public UnmodifiableVector3D<Vector3i, Point3i> toUnmodifiable() {
		return new UnmodifiableVector3D<Vector3i, Point3i>() {

			private static final long serialVersionUID = 7684988962796497763L;

			@Override
			public GeomFactory3D<Vector3i, Point3i> getGeomFactory() {
				return Vector3i.this.getGeomFactory();
			}

			@Override
			public Vector3i toUnitVector() {
				return Vector3i.this.toUnitVector();
			}

			@Override
			@SuppressWarnings("checkstyle:superclone")
			public Vector3i clone() {
				return Vector3i.this.getGeomFactory().newVector(
						Vector3i.this.ix(),
						Vector3i.this.iy(),
						Vector3i.this.iz());
			}

			@Override
			public double getX() {
				return Vector3i.this.getX();
			}

			@Override
			public int ix() {
				return Vector3i.this.ix();
			}

			@Override
			public double getY() {
				return Vector3i.this.getY();
			}

			@Override
			public int iy() {
				return Vector3i.this.iy();
			}

			@Override
			public double getZ() {
				return Vector3i.this.getZ();
			}

			@Override
			public int iz() {
				return Vector3i.this.iz();
			}

		};
	}

}
