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

package org.arakhne.afc.math.geometry.d2.i;

import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.GeomFactory2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.UnmodifiableVector2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.arakhne.afc.vmutil.json.JsonBuffer;

/** 2D Vector with 2 integer numbers.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Vector2i extends Tuple2i<Vector2i> implements Vector2D<Vector2i, Point2i> {

	private static final long serialVersionUID = -7228108517874845303L;

	/** Construct a zero vector.
	 */
	public Vector2i() {
		//
	}

	/** Constructor by copy.
	 * @param tuple is the tuple to copy.
	 */
	public Vector2i(Tuple2D<?> tuple) {
		super(tuple);
	}

	/** Constructor by copy.
	 * @param tuple is the tuple to copy.
	 */
	public Vector2i(int[] tuple) {
		super(tuple);
	}

	/** Constructor by copy.
	 * @param tuple is the tuple to copy.
	 */
	public Vector2i(double[] tuple) {
		super(tuple);
	}

	/** Constructor a vector with the given coordinates.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Vector2i(int x, int y) {
		super(x, y);
	}

	/** Constructor a vector with the given coordinates.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Vector2i(float x, float y) {
		super(x, y);
	}

	/** Constructor a vector with the given coordinates.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Vector2i(double x, double y) {
		super(x, y);
	}

	/** Constructor a vector with the given coordinates.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Vector2i(long x, long y) {
		super(x, y);
	}

	/** Convert the given tuple to a real Vector2i.
	 *
	 * <p>If the given tuple is already a Vector2i, it is replied.
	 *
	 * @param tuple the tuple.
	 * @return the Vector2i.
	 * @since 14.0
	 */
	public static Vector2i convert(Tuple2D<?> tuple) {
		if (tuple instanceof Vector2i) {
			return (Vector2i) tuple;
		}
		return new Vector2i(tuple.getX(), tuple.getY());
	}


	/** Replies the orientation vector, which is corresponding
	 * to the given angle on a trigonometric circle.
	 *
	 * @param angle is the angle in radians to translate.
	 * @return the orientation vector which is corresponding to the given angle.
	 */
	@Pure
	@Inline(value = "new Vector2i(Math.cos($1), Math.sin($1))", imported = {Vector2i.class, Math.class})
	public static Vector2i toOrientationVector(double angle) {
		return new Vector2i(Math.cos(angle), Math.sin(angle));
	}

	@Pure
	@Override
	public double dot(Vector2D<?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		return this.x * vector.getX() + this.y * vector.getY();
	}

	@Pure
	@Override
	public double perp(Vector2D<?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		return this.x * vector.getY() - vector.getX() * this.y;
	}

	@Pure
	@Override
	public double getLength() {
		return Math.hypot(this.x, this.y);
	}

	@Pure
	@Override
	public double getLengthSquared() {
		return this.x * this.x + this.y * this.y;
	}

	@Override
	public void add(Vector2D<?, ?> vector1, Vector2D<?, ?> vector2) {
		assert vector1 != null : AssertMessages.notNullParameter(0);
		assert vector2 != null : AssertMessages.notNullParameter(1);
		this.x = (int) Math.round(vector1.getX() + vector2.getX());
		this.y = (int) Math.round(vector1.getY() + vector2.getY());
	}

	@Override
	public void add(Vector2D<?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		this.x = (int) Math.round(this.x + vector.getX());
		this.y = (int) Math.round(this.y + vector.getY());
	}

	@Override
	public void scaleAdd(int scale, Vector2D<?, ?> vector1, Vector2D<?, ?> vector2) {
		assert vector1 != null : AssertMessages.notNullParameter(1);
		assert vector2 != null : AssertMessages.notNullParameter(2);
		this.x = (int) Math.round(scale * vector1.getX() + vector2.getX());
		this.y = (int) Math.round(scale * vector1.getY() + vector2.getY());
	}

	@Override
	public void scaleAdd(double scale, Vector2D<?, ?> vector1, Vector2D<?, ?> vector2) {
		assert vector1 != null : AssertMessages.notNullParameter(1);
		assert vector2 != null : AssertMessages.notNullParameter(2);
		this.x = (int) Math.round(scale * vector1.getX() + vector2.getX());
		this.y = (int) Math.round(scale * vector1.getY() + vector2.getY());
	}

	@Override
	public void scaleAdd(int scale, Vector2D<?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter(1);
		this.x = (int) Math.round(scale * this.x + vector.getX());
		this.y = (int) Math.round(scale * this.y + vector.getY());
	}

	@Override
	public void scaleAdd(double scale, Vector2D<?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter(1);
		this.x = (int) Math.round(scale * this.x + vector.getX());
		this.y = (int) Math.round(scale * this.y + vector.getY());
	}

	@Override
	public void sub(Vector2D<?, ?> vector1, Vector2D<?, ?> vector2) {
		assert vector1 != null : AssertMessages.notNullParameter(0);
		assert vector2 != null : AssertMessages.notNullParameter(1);
		this.x = (int) Math.round(vector1.getX() - vector2.getX());
		this.y = (int) Math.round(vector1.getY() - vector2.getY());
	}

	@Override
	public void sub(Point2D<?, ?> point1, Point2D<?, ?> point2) {
		assert point1 != null : AssertMessages.notNullParameter(0);
		assert point2 != null : AssertMessages.notNullParameter(1);
		this.x = (int) Math.round(point1.getX() - point2.getX());
		this.y = (int) Math.round(point1.getY() - point2.getY());
	}

	@Override
	public void sub(Vector2D<?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		this.x = (int) Math.round(this.x - vector.getX());
		this.y = (int) Math.round(this.y - vector.getY());
	}

	@Override
	public void setLength(double newLength) {
		final double l = getLength();
		if (l != 0) {
			final double f = newLength / l;
			this.x = (int) Math.round(this.x * f);
			this.y = (int) Math.round(this.y * f);
		} else {
			this.x = (int) Math.round(newLength);
			this.y = 0;
		}
	}

	@Override
	public GeomFactory2i getGeomFactory() {
		return GeomFactory2i.SINGLETON;
	}

	@Pure
	@Override
	public UnmodifiableVector2D<Vector2i, Point2i> toUnmodifiable() {
		return new UnmodifiableVector2D<Vector2i, Point2i>() {

			private static final long serialVersionUID = 7684988962796497763L;

			@Override
			public GeomFactory2D<Vector2i, Point2i> getGeomFactory() {
				return Vector2i.this.getGeomFactory();
			}

			@Override
			public Vector2i toUnitVector() {
				return Vector2i.this.toUnitVector();
			}

			@Override
			public Vector2i toOrthogonalVector() {
				return Vector2i.this.toOrthogonalVector();
			}

			@Override
			@SuppressWarnings("checkstyle:superclone")
			public Vector2i clone() {
				return Vector2i.this.getGeomFactory().newVector(
						Vector2i.this.ix(),
						Vector2i.this.iy());
			}

			@Override
			public double getX() {
				return Vector2i.this.getX();
			}

			@Override
			public int ix() {
				return Vector2i.this.ix();
			}

			@Override
			public double getY() {
				return Vector2i.this.getY();
			}

			@Override
			public int iy() {
				return Vector2i.this.iy();
			}

			@Override
			public String toString() {
				return Vector2i.this.toString();
			}

			@Override
			public void toJson(JsonBuffer buffer) {
				Vector2i.this.toJson(buffer);
			}

		};
	}

}
