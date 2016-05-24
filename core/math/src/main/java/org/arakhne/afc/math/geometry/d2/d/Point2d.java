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
import org.arakhne.afc.math.geometry.d2.UnmodifiablePoint2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;

/** 2D Point with 2 double precision floating-point numbers.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Point2d extends Tuple2d<Point2d> implements Point2D<Point2d, Vector2d> {

	private static final long serialVersionUID = -8318943957531471869L;

	/** Construct a zero point.
	 */
	public Point2d() {
		//
	}

	/** Constructor by copy.
	 * @param tuple is the tuple to copy.
	 */
	public Point2d(Tuple2D<?> tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Point2d(int[] tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Point2d(double[] tuple) {
		super(tuple);
	}

	/** Construct a point with the given coordinates.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Point2d(int x, int y) {
		super(x, y);
	}

	/** Construct a point with the given coordinates.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Point2d(float x, float y) {
		super(x, y);
	}

	/** Construct a point with the given coordinates.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Point2d(double x, double y) {
		super(x, y);
	}

	/** Construct a point with the given coordinates.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Point2d(long x, long y) {
		super(x, y);
	}

	@Override
	public GeomFactory2d getGeomFactory() {
		return GeomFactory2d.SINGLETON;
	}

	@Pure
	@Override
	public double getDistanceSquared(Point2D<?, ?> pt) {
		assert pt != null : "Point must be not null"; //$NON-NLS-1$
		final double dx = this.x - pt.getX();
		final double dy = this.y - pt.getY();
		return dx * dx + dy * dy;
	}

	@Pure
	@Override
	public double getDistance(Point2D<?, ?> pt) {
		assert pt != null : "Point must be not null"; //$NON-NLS-1$
		final double dx = this.x - pt.getX();
		final double dy = this.y - pt.getY();
		return Math.sqrt(dx * dx + dy * dy);
	}

	@Pure
	@Override
	public double getDistanceL1(Point2D<?, ?> pt) {
		assert pt != null : "Point must be not null"; //$NON-NLS-1$
		return Math.abs(this.x - pt.getX()) + Math.abs(this.y - pt.getY());
	}

	@Pure
	@Override
	public double getDistanceLinf(Point2D<?, ?> pt) {
		assert pt != null : "Point must be not null"; //$NON-NLS-1$
		return Math.max(Math.abs(this.x - pt.getX()), Math.abs(this.y - pt.getY()));
	}

	@Pure
	@Override
	public int getIdistanceL1(Point2D<?, ?> pt) {
		return (int) getDistanceL1(pt);
	}

	@Pure
	@Override
	public int getIdistanceLinf(Point2D<?, ?> pt) {
		return (int) getDistanceLinf(pt);
	}

	@Override
	public void add(Point2D<?, ?> point, Vector2D<?, ?> vector) {
		assert point != null : "Point must be not null"; //$NON-NLS-1$
		assert vector != null : "Vector must be not null"; //$NON-NLS-1$
		this.x = point.getX() + vector.getX();
		this.y = point.getY() + vector.getY();
	}

	@Override
	public void add(Vector2D<?, ?> vector, Point2D<?, ?> point) {
		assert point != null : "Point must be not null"; //$NON-NLS-1$
		assert vector != null : "Vector must be not null"; //$NON-NLS-1$
		this.x = vector.getX() + point.getX();
		this.y = vector.getY() + point.getY();
	}

	@Override
	public void add(Vector2D<?, ?> vector) {
		assert vector != null : "Vector must be not null"; //$NON-NLS-1$
		this.x = this.x + vector.getX();
		this.y = this.y + vector.getY();
	}

	@Override
	public void scaleAdd(int scale, Vector2D<?, ?> vector, Point2D<?, ?> point) {
		assert point != null : "Point must be not null"; //$NON-NLS-1$
		assert vector != null : "Vector must be not null"; //$NON-NLS-1$
		this.x = scale * vector.getX() + point.getX();
		this.y = scale * vector.getY() + point.getY();
	}

	@Override
	public void scaleAdd(double scale, Vector2D<?, ?> vector, Point2D<?, ?> point) {
		assert point != null : "Point must be not null"; //$NON-NLS-1$
		assert vector != null : "Vector must be not null"; //$NON-NLS-1$
		this.x = scale * vector.getX() + point.getX();
		this.y = scale * vector.getY() + point.getY();
	}

	@Override
	public void scaleAdd(int scale, Point2D<?, ?> point, Vector2D<?, ?> vector) {
		assert point != null : "Point must be not null"; //$NON-NLS-1$
		assert vector != null : "Vector must be not null"; //$NON-NLS-1$
		this.x = scale * point.getX() + vector.getX();
		this.y = scale * point.getY() + vector.getY();
	}

	@Override
	public void scaleAdd(double scale, Point2D<?, ?> point, Vector2D<?, ?> vector) {
		assert point != null : "Point must be not null"; //$NON-NLS-1$
		assert vector != null : "Vector must be not null"; //$NON-NLS-1$
		this.x = scale * point.getX() + vector.getX();
		this.y = scale * point.getY() + vector.getY();
	}

	@Override
	public void scaleAdd(int scale, Vector2D<?, ?> vector) {
		assert vector != null : "Vector must be not null"; //$NON-NLS-1$
		this.x = scale * this.x + vector.getX();
		this.y = scale * this.y + vector.getY();
	}

	@Override
	public void scaleAdd(double scale, Vector2D<?, ?> vector) {
		assert vector != null : "Vector must be not null"; //$NON-NLS-1$
		this.x = scale * this.x + vector.getX();
		this.y = scale * this.y + vector.getY();
	}

	@Override
	public void sub(Point2D<?, ?> point, Vector2D<?, ?> vector) {
		assert point != null : "Point must be not null"; //$NON-NLS-1$
		assert vector != null : "Vector must be not null"; //$NON-NLS-1$
		this.x = point.getX() - vector.getX();
		this.y = point.getY() - vector.getY();
	}

	@Override
	public void sub(Vector2D<?, ?> vector) {
		assert vector != null : "Vector must be not null"; //$NON-NLS-1$
		this.x = this.x - vector.getX();
		this.y = this.y - vector.getY();
	}

	@Pure
	@Override
	public UnmodifiablePoint2D<Point2d, Vector2d> toUnmodifiable() {
		return new UnmodifiablePoint2D<Point2d, Vector2d>() {

			private static final long serialVersionUID = 3305735799920201356L;

			@Override
			public GeomFactory<Vector2d, Point2d> getGeomFactory() {
				return Point2d.this.getGeomFactory();
			}

			@Override
			@SuppressWarnings("checkstyle:superclone")
			public Point2d clone() {
				return Point2d.this.getGeomFactory().newPoint(Point2d.this.getX(), Point2d.this.getY());
			}

			@Override
			public double getX() {
				return Point2d.this.getX();
			}

			@Override
			public int ix() {
				return Point2d.this.ix();
			}

			@Override
			public double getY() {
				return Point2d.this.getY();
			}

			@Override
			public int iy() {
				return Point2d.this.iy();
			}

		};
	}

}
