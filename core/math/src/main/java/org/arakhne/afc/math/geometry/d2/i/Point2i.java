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

package org.arakhne.afc.math.geometry.d2.i;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.GeomFactory2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.UnmodifiablePoint2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** 2D Point with 2 integer numbers.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Point2i extends Tuple2i<Point2i> implements Point2D<Point2i, Vector2i> {

	private static final long serialVersionUID = -4977158382149954525L;

	/** Construct a zero point.
	 */
	public Point2i() {
		//
	}

	/** Constructor by copy.
	 * @param tuple is the tuple to copy.
	 */
	public Point2i(Tuple2D<?> tuple) {
		super(tuple);
	}

	/** Constructor by copy.
	 * @param tuple is the tuple to copy.
	 */
	public Point2i(int[] tuple) {
		super(tuple);
	}

	/** Constructor by copy.
	 * @param tuple is the tuple to copy.
	 */
	public Point2i(double[] tuple) {
		super(tuple);
	}

	/** Construct a point with the given coordinates.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Point2i(int x, int y) {
		super(x, y);
	}

	/** Construct a point with the given coordinates.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Point2i(float x, float y) {
		super(x, y);
	}

	/** Construct a point with the given coordinates.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Point2i(double x, double y) {
		super(x, y);
	}

	/** Construct a point with the given coordinates.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Point2i(long x, long y) {
		super(x, y);
	}

	@Pure
	@Override
	public double getDistanceSquared(Point2D<?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		final double dx = this.x - point.getX();
		final double dy = this.y - point.getY();
		return dx * dx + dy * dy;
	}

	@Pure
	@Override
	public double getDistance(Point2D<?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		final double dx = this.x - point.getX();
		final double dy = this.y - point.getY();
		return Math.sqrt(dx * dx + dy * dy);
	}

	@Pure
	@Override
	public double getDistanceL1(Point2D<?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		return Math.abs(this.x - point.getX()) + Math.abs(this.y - point.getY());
	}

	@Pure
	@Override
	public double getDistanceLinf(Point2D<?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		return Math.max(Math.abs(this.x - point.getX()), Math.abs(this.y - point.getY()));
	}

	@Pure
	@Override
	public int getIdistanceL1(Point2D<?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		return (int) Math.round(getDistanceL1(point));
	}

	@Pure
	@Override
	public int getIdistanceLinf(Point2D<?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		return (int) Math.round(getDistanceLinf(point));
	}

	@Override
	public void add(Point2D<?, ?> point, Vector2D<?, ?> vector) {
		assert point != null : AssertMessages.notNullParameter(0);
		assert vector != null : AssertMessages.notNullParameter(1);
		this.x = (int) Math.round(point.getX() + vector.getX());
		this.y = (int) Math.round(point.getY() + vector.getY());
	}

	@Override
	public void add(Vector2D<?, ?> vector, Point2D<?, ?> point) {
		assert point != null : AssertMessages.notNullParameter(1);
		assert vector != null : AssertMessages.notNullParameter(0);
		this.x = (int) Math.round(vector.getX() + point.getX());
		this.y = (int) Math.round(vector.getY() + point.getY());
	}

	@Override
	public void add(Vector2D<?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		this.x = (int) Math.round(this.x + vector.getX());
		this.y = (int) Math.round(this.y + vector.getY());
	}

	@Override
	public void scaleAdd(int scale, Vector2D<?, ?> vector, Point2D<?, ?> point) {
		assert point != null : AssertMessages.notNullParameter(2);
		assert vector != null : AssertMessages.notNullParameter(1);
		this.x = (int) Math.round(scale * vector.getX() + point.getX());
		this.y = (int) Math.round(scale * vector.getY() + point.getY());
	}

	@Override
	public void scaleAdd(double scale, Vector2D<?, ?> vector, Point2D<?, ?> point) {
		assert point != null : AssertMessages.notNullParameter(2);
		assert vector != null : AssertMessages.notNullParameter(1);
		this.x = (int) Math.round(scale * vector.getX() + point.getX());
		this.y = (int) Math.round(scale * vector.getY() + point.getY());
	}

	@Override
	public void scaleAdd(int scale, Point2D<?, ?> point, Vector2D<?, ?> vector) {
		assert point != null : AssertMessages.notNullParameter(1);
		assert vector != null : AssertMessages.notNullParameter(2);
		this.x = (int) Math.round(scale * point.getX() + vector.getX());
		this.y = (int) Math.round(scale * point.getY() + vector.getY());
	}

	@Override
	public void scaleAdd(double scale, Point2D<?, ?> point, Vector2D<?, ?> vector) {
		assert point != null : AssertMessages.notNullParameter(1);
		assert vector != null : AssertMessages.notNullParameter(2);
		this.x = (int) Math.round(scale * point.getX() + vector.getX());
		this.y = (int) Math.round(scale * point.getY() + vector.getY());
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
	public void sub(Point2D<?, ?> point, Vector2D<?, ?> vector) {
	    assert point != null : AssertMessages.notNullParameter(0);
		assert vector != null : AssertMessages.notNullParameter(1);
		this.x = (int) Math.round(point.getX() - vector.getX());
		this.y = (int) Math.round(point.getY() - vector.getY());
	}

	@Override
	public void sub(Vector2D<?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		this.x = (int) Math.round(this.x - vector.getX());
		this.y = (int) Math.round(this.y - vector.getY());
	}

	@Override
	public GeomFactory2i getGeomFactory() {
		return GeomFactory2i.SINGLETON;
	}

	@Pure
	@Override
	public UnmodifiablePoint2D<Point2i, Vector2i> toUnmodifiable() {
		return new UnmodifiablePoint2D<Point2i, Vector2i>() {

			private static final long serialVersionUID = -4844158582025788289L;

			@Override
			public GeomFactory2D<Vector2i, Point2i> getGeomFactory() {
				return Point2i.this.getGeomFactory();
			}

			@Override
			@SuppressWarnings("checkstyle:superclone")
			public Point2i clone() {
				return Point2i.this.getGeomFactory().newPoint(
						Point2i.this.ix(),
						Point2i.this.iy());
			}

			@Override
			public double getX() {
				return Point2i.this.getX();
			}

			@Override
			public int ix() {
				return Point2i.this.ix();
			}

			@Override
			public double getY() {
				return Point2i.this.getY();
			}

			@Override
			public int iy() {
				return Point2i.this.iy();
			}

		};
	}

}
