/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2026 The original authors and other contributors.
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

import org.arakhne.afc.math.geometry.base.d2.ImmutableVector2D;
import org.arakhne.afc.math.geometry.base.d2.Point2D;
import org.arakhne.afc.math.geometry.base.d2.Tuple2D;
import org.arakhne.afc.math.geometry.base.d2.UnmodifiableVector2D;
import org.arakhne.afc.math.geometry.base.d2.Vector2D;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

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

	/** Convert the given tuple to a real Vector2d.
	 *
	 * <p>If the given tuple is already a Vector2d, it is replied.
	 *
	 * @param tuple the tuple.
	 * @return the Vector2d.
	 * @since 14.0
	 */
	public static Vector2d convert(Tuple2D<?> tuple) {
		if (tuple instanceof Vector2d vec) {
			return vec;
		}
		return new Vector2d(tuple.getX(), tuple.getY());
	}

	/** Replies the orientation vector, which is corresponding
	 * to the given angle on a trigonometric circle.
	 *
	 * @param angle is the angle in radians to translate.
	 * @return the orientation vector which is corresponding to the given angle.
	 */
	@Pure
	@Inline(value = "new Vector2d(Math.cos($1), Math.sin($1))", imported = {Vector2d.class, Math.class})
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
		this.x = vector1.getX() + vector2.getX();
		this.y = vector1.getY() + vector2.getY();
	}

	@Override
	public void add(Vector2D<?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		this.x = this.x + vector.getX();
		this.y = this.y + vector.getY();
	}

	@Override
	public void scaleAdd(int scale, Vector2D<?, ?> vector1, Vector2D<?, ?> vector2) {
		assert vector1 != null : AssertMessages.notNullParameter(0);
		assert vector2 != null : AssertMessages.notNullParameter(1);
		this.x = scale * vector1.getX() + vector2.getX();
		this.y = scale * vector1.getY() + vector2.getY();
	}

	@Override
	public void scaleAdd(double scale, Vector2D<?, ?> vector1, Vector2D<?, ?> vector2) {
		assert vector1 != null : AssertMessages.notNullParameter(0);
		assert vector2 != null : AssertMessages.notNullParameter(1);
		this.x = scale * vector1.getX() + vector2.getX();
		this.y = scale * vector1.getY() + vector2.getY();
	}

	@Override
	public void scaleAdd(int scale, Vector2D<?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		this.x = scale * this.x + vector.getX();
		this.y = scale * this.y + vector.getY();
	}

	@Override
	public void scaleAdd(double scale, Vector2D<?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		this.x = scale * this.x + vector.getX();
		this.y = scale * this.y + vector.getY();
	}

	@Override
	public void sub(Vector2D<?, ?> vector1, Vector2D<?, ?> vector2) {
		assert vector1 != null : AssertMessages.notNullParameter(0);
		assert vector2 != null : AssertMessages.notNullParameter(1);
		this.x = vector1.getX() - vector2.getX();
		this.y = vector1.getY() - vector2.getY();
	}

	@Override
	public void sub(Point2D<?, ?> point1, Point2D<?, ?> point2) {
		assert point1 != null : AssertMessages.notNullParameter(0);
		assert point2 != null : AssertMessages.notNullParameter(1);
		this.x = point1.getX() - point2.getX();
		this.y = point1.getY() - point2.getY();
	}

	@Override
	public void sub(Vector2D<?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		this.x -= vector.getX();
		this.y -= vector.getY();
	}

	@Override
	public void setLength(double newLength) {
		final var l = getLength();
		if (l != 0) {
			final var f = newLength / l;
			this.x *= f;
			this.y *= f;
		} else {
			this.x = newLength;
			this.y = 0;
		}
	}

	@Pure
	@Override
	public UnmodifiableVector2D<?, ?> toUnmodifiable() {
		return new ImmutableVector2D(getX(), getY());
	}

}
