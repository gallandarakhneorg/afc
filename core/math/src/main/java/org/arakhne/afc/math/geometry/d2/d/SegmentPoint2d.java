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
public class SegmentPoint2d extends AbstractShape2d<SegmentPoint2d>
		implements Segment2afp<Shape2d<?>, SegmentPoint2d, PathElement2d, Point2d, Vector2d, Rectangle2d> {

	private static final long serialVersionUID = -5667213589442134247L;

	private Point2d p1;

	private Point2d p2;

	/** Construct an empty segment.
	 */
	public SegmentPoint2d() {
		//
	}

	/** Construct a segment from the two given points.
	 * @param p1 first point.
	 * @param p2 second point.
	 */
	public SegmentPoint2d(Point2D<?, ?> p1, Point2D<?, ?> p2) {
		this(p1.getX(), p1.getY(), p2.getX(), p2.getY());
	}

	/** Constructor by copy.
	 * @param segment the segment to copy.
	 */
	public SegmentPoint2d(Segment2afp<?, ?, ?, ?, ?, ?> segment) {
		this(segment.getX1(), segment.getY1(), segment.getX2(), segment.getY2());
	}

	/** Construct a segment from the two given points.
	 * @param x1 x coordinate of the first point.
	 * @param y1 y coordinate of the first point.
	 * @param x2 x coordinate of the second point.
	 * @param y2 y coordinate of the second point.
	 */
	public SegmentPoint2d(double x1, double y1, double x2, double y2) {
		set(x1, y1, x2, y2);
	}

	@Pure
	@Override
	public int hashCode() {
		int bits = 1;
		bits = 31 * bits + this.p1.hashCode();
		bits = 31 * bits + this.p2.hashCode();
        return bits ^ (bits >> 31);
	}

	@Pure
	@Override
	public String toString() {
		final StringBuilder b = new StringBuilder();
		b.append("("); //$NON-NLS-1$
		b.append(getX1());
		b.append(", "); //$NON-NLS-1$
		b.append(getY1());
		b.append(")-("); //$NON-NLS-1$
		b.append(getX2());
		b.append(", "); //$NON-NLS-1$
		b.append(getY2());
		b.append(")"); //$NON-NLS-1$
		return b.toString();
	}

	@Pure
	@Override
	public SegmentPoint2d createTransformedShape(Transform2D transform) {
		if (transform == null || transform.isIdentity()) {
			return clone();
		}
		final Point2d point = getGeomFactory().newPoint(getX1(), getY1());
		transform.transform(point);
		final double x1 = point.x;
		final double y1 = point.y;
		point.set(getX2(), getY2());
		transform.transform(point);
		return new SegmentPoint2d(x1, y1, point.getX(), point.getY());
	}

	@Override
	public void set(double x1, double y1, double x2, double y2) {
		if (this.p1.x != x1 || this.p1.y != y1 || this.p2.x != x2 || this.p2.y != y2) {
			this.p1.x = x1;
			this.p1.y = y1;
			this.p2.x = x2;
			this.p2.y = y2;
			fireGeometryChange();
		}
	}

	@Override
	public void setX1(double x) {
		if (this.p1.x != x) {
			this.p1.x = x;
			fireGeometryChange();
		}
	}

	@Override
	public void setY1(double y) {
		if (this.p1.y != y) {
			this.p1.y = y;
			fireGeometryChange();
		}
	}

	@Override
	public void setX2(double x) {
		if (this.p2.x != x) {
			this.p2.x = x;
			fireGeometryChange();
		}
	}

	@Override
	public void setY2(double y) {
		if (this.p2.y != y) {
			this.p2.y = y;
			fireGeometryChange();
		}
	}

	@Pure
	@Override
	public double getX1() {
		return this.p1.x;
	}

	@Pure
	@Override
	public double getY1() {
		return this.p1.y;
	}

	@Pure
	@Override
	public double getX2() {
		return this.p2.x;
	}

	@Pure
	@Override
	public double getY2() {
		return this.p2.y;
	}

	@Override
	public Point2d getP1() {
		return this.p1;
	}

	@Override
	public Point2d getP2() {
		return this.p2;
	}

}
