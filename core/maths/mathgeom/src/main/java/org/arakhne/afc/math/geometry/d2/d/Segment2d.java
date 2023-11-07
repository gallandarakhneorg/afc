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

package org.arakhne.afc.math.geometry.d2.d;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.afp.Segment2afp;

/** A 2D segment/line with 2 double precision floating-point numbers.
 *
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Segment2d extends AbstractShape2d<Segment2d>
		implements Segment2afp<Shape2d<?>, Segment2d, PathElement2d, Point2d, Vector2d, Rectangle2d> {

	private static final long serialVersionUID = -5667213589442134247L;

	private double ax;

	private double ay;

	private double bx;

	private double by;

	/** Construct an empty segment.
	 */
	public Segment2d() {
		//
	}

	/** Construct a segment from the two given points.
	 * @param p1 first point.
	 * @param p2 second point.
	 */
	public Segment2d(Point2D<?, ?> p1, Point2D<?, ?> p2) {
		this(p1.getX(), p1.getY(), p2.getX(), p2.getY());
	}

	/** Constructor by copy.
	 * @param segment the segment to copy.
	 */
	public Segment2d(Segment2afp<?, ?, ?, ?, ?, ?> segment) {
		this(segment.getX1(), segment.getY1(), segment.getX2(), segment.getY2());
	}

	/** Construct a segment from the two given points.
	 * @param x1 x coordinate of the first point.
	 * @param y1 y coordinate of the first point.
	 * @param x2 x coordinate of the second point.
	 * @param y2 y coordinate of the second point.
	 */
	public Segment2d(double x1, double y1, double x2, double y2) {
		set(x1, y1, x2, y2);
	}

	@Pure
	@Override
	public int hashCode() {
		int bits = 1;
		bits = 31 * bits + Double.hashCode(this.ax);
		bits = 31 * bits + Double.hashCode(this.ay);
		bits = 31 * bits + Double.hashCode(this.bx);
		bits = 31 * bits + Double.hashCode(this.by);
        return bits ^ (bits >> 31);
	}

	@Pure
	@Override
	public Segment2d createTransformedShape(Transform2D transform) {
		if (transform == null || transform.isIdentity()) {
			return clone();
		}
		final Point2d point = getGeomFactory().newPoint(getX1(), getY1());
		transform.transform(point);
		final double x1 = point.getX();
		final double y1 = point.getY();
		point.set(getX2(), getY2());
		transform.transform(point);
		return getGeomFactory().newSegment(x1, y1, point.getX(), point.getY());
	}

	@Override
	public void set(double x1, double y1, double x2, double y2) {
		if (this.ax != x1 || this.ay != y1 || this.bx != x2 || this.by != y2) {
			this.ax = x1;
			this.ay = y1;
			this.bx = x2;
			this.by = y2;
			fireGeometryChange();
		}
	}

	@Override
	public void setX1(double x) {
		if (this.ax != x) {
			this.ax = x;
			fireGeometryChange();
		}
	}

	@Override
	public void setY1(double y) {
		if (this.ay != y) {
			this.ay = y;
			fireGeometryChange();
		}
	}

	@Override
	public void setX2(double x) {
		if (this.bx != x) {
			this.bx = x;
			fireGeometryChange();
		}
	}

	@Override
	public void setY2(double y) {
		if (this.by != y) {
			this.by = y;
			fireGeometryChange();
		}
	}

	@Pure
	@Override
	public double getX1() {
		return this.ax;
	}

	@Pure
	@Override
	public double getY1() {
		return this.ay;
	}

	@Pure
	@Override
	public double getX2() {
		return this.bx;
	}

	@Pure
	@Override
	public double getY2() {
		return this.by;
	}

	@Override
	public Point2d getP1() {
		return getGeomFactory().newPoint(this.ax, this.ay);
	}

	@Override
	public Point2d getP2() {
		return getGeomFactory().newPoint(this.bx, this.by);
	}

}
