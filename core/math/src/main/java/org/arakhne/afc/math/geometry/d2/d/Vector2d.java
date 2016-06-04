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

package org.arakhne.afc.math.geometry.d2.d;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.GeomFactory;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.UnmodifiableVector2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;

/** 2D Vector with 2 double precision floating-point numbers.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Vector2d extends Tuple2d<Vector2d> implements Vector2D<Vector2d, Point2d> {
	private static final long serialVersionUID = 9183440606977893371L;

	/**
	 *Literal constant.
     */
	private static final String SECOND_VECTOR_NOT_NULL = "Second vector must be not null"; 

    /**
     *Literal constant.
     */
    private static final String FIRST_VECTOR_NOT_NULL = "First vector must be not null"; 

	/** Construct a zero vector.
	 */
	public Vector2d() {
		//
	}

	/** Constructor by copy.
	 *
	 * @param tuple is the tuple to copy.
	 */
	public Vector2d(Tuple2D<?> tuple) {
		super(tuple);
	}

	/** Constructor by copy.
	 * @param tuple is the tuple to copy.
	 */
	public Vector2d(int[] tuple) {
		super(tuple);
	}

	/** Constructor by copy.
	 * @param tuple is the tuple to copy.
	 */
	public Vector2d(double[] tuple) {
		super(tuple);
	}

	/** Construct a vector with the given coordinates.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Vector2d(int x, int y) {
		super(x, y);
	}

	/** Construct a vector with the given coordinates.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Vector2d(float x, float y) {
		super(x, y);
	}

	/** Construct a vector with the given coordinates.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Vector2d(double x, double y) {
		super(x, y);
	}

	/** Construct a vector with the given coordinates.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Vector2d(long x, long y) {
		super(x, y);
	}

	/** Replies the orientation vector, which is corresponding
	 * to the given angle on a trigonometric circle.
	 *
	 * @param angle is the angle in radians to translate.
	 * @return the orientation vector which is corresponding to the given angle.
	 */
	@Pure
	public static Vector2d toOrientationVector(double angle) {
		return new Vector2d(Math.cos(angle), Math.sin(angle));
	}

	@Override
	public GeomFactory2d getGeomFactory() {
		return GeomFactory2d.SINGLETON;
	}

	@Pure
	@Override
	public double dot(Vector2D<?, ?> vector) {
		assert vector != null : "Vector must be not null"; 
		return this.x * vector.getX() + this.y * vector.getY();
	}

	@Pure
	@Override
	public double perp(Vector2D<?, ?> vector) {
		assert vector != null : "Vector must be not null"; 
		return this.x * vector.getY() - vector.getX() * this.y;
	}

	@Pure
	@Override
	public double getLength() {
		return Math.sqrt(this.x * this.x + this.y * this.y);
	}

	@Pure
	@Override
	public double getLengthSquared() {
		return this.x * this.x + this.y * this.y;
	}

	@Override
	public void add(Vector2D<?, ?> vector1, Vector2D<?, ?> vector2) {
		assert vector1 != null : FIRST_VECTOR_NOT_NULL;
		assert vector2 != null : SECOND_VECTOR_NOT_NULL;
		this.x = vector1.getX() + vector2.getX();
		this.y = vector1.getY() + vector2.getY();
	}

	@Override
	public void add(Vector2D<?, ?> vector) {
		assert vector != null : "Vector must be not null"; 
		this.x = this.x + vector.getX();
		this.y = this.y + vector.getY();
	}

	@Override
	public void scaleAdd(int scale, Vector2D<?, ?> vector1, Vector2D<?, ?> vector2) {
		assert vector1 != null : FIRST_VECTOR_NOT_NULL;
		assert vector2 != null : SECOND_VECTOR_NOT_NULL;
		this.x = scale * vector1.getX() + vector2.getX();
		this.y = scale * vector1.getY() + vector2.getY();
	}

	@Override
	public void scaleAdd(double scale, Vector2D<?, ?> vector1, Vector2D<?, ?> vector2) {
		assert vector1 != null : FIRST_VECTOR_NOT_NULL;
		assert vector2 != null : SECOND_VECTOR_NOT_NULL;
		this.x = scale * vector1.getX() + vector2.getX();
		this.y = scale * vector1.getY() + vector2.getY();
	}

	@Override
	public void scaleAdd(int scale, Vector2D<?, ?> vector) {
		assert vector != null : "Vector must be not null"; 
		this.x = scale * this.x + vector.getX();
		this.y = scale * this.y + vector.getY();
	}

	@Override
	public void scaleAdd(double scale, Vector2D<?, ?> vector) {
		assert vector != null : "Vector must be not null"; 
		this.x = scale * this.x + vector.getX();
		this.y = scale * this.y + vector.getY();
	}

	@Override
	public void sub(Vector2D<?, ?> vector1, Vector2D<?, ?> vector2) {
		assert vector1 != null : FIRST_VECTOR_NOT_NULL;
		assert vector2 != null : SECOND_VECTOR_NOT_NULL;
		this.x = vector1.getX() - vector2.getX();
		this.y = vector1.getY() - vector2.getY();
	}

	@Override
	public void sub(Point2D<?, ?> point1, Point2D<?, ?> point2) {
		assert point1 != null : "First point must be not null"; 
		assert point2 != null : "Second point must be not null"; 
		this.x = point1.getX() - point2.getX();
		this.y = point1.getY() - point2.getY();
	}

	@Override
	public void sub(Vector2D<?, ?> vector) {
		assert vector != null : "Vector must be not null"; 
		this.x -= vector.getX();
		this.y -= vector.getY();
	}

	@Override
	public void setLength(double newLength) {
		assert newLength >= 0. : "New length must be positive or zero"; 
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

	@Override
	public Vector2d toUnitVector() {
		final double length = getLength();
		if (length == 0.) {
			return getGeomFactory().newVector();
		}
		return getGeomFactory().newVector(getX() / length, getY() / length);
	}

	@Override
	public Vector2d toOrthogonalVector() {
		return getGeomFactory().newVector(-getY(), getX());
	}

	@Pure
	@Override
	public UnmodifiableVector2D<Vector2d, Point2d> toUnmodifiable() {
		return new UnmodifiableVector2D<Vector2d, Point2d>() {

			private static final long serialVersionUID = 6848610371671516804L;

			@Override
			public GeomFactory<Vector2d, Point2d> getGeomFactory() {
				return Vector2d.this.getGeomFactory();
			}

			@Override
			public Vector2d toUnitVector() {
				return Vector2d.this.toUnitVector();
			}

			@Override
			public Vector2d toOrthogonalVector() {
				return Vector2d.this.toOrthogonalVector();
			}

			@Override
			@SuppressWarnings("checkstyle:superclone")
			public Vector2d clone() {
				return Vector2d.this.getGeomFactory().newVector(Vector2d.this.getX(), Vector2d.this.getY());
			}

			@Override
			public double getX() {
				return Vector2d.this.getX();
			}

			@Override
			public int ix() {
				return Vector2d.this.ix();
			}

			@Override
			public double getY() {
				return Vector2d.this.getY();
			}

			@Override
			public int iy() {
				return Vector2d.this.iy();
			}

		};
	}

}
