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

package org.arakhne.afc.math.geometry.d2.i;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.ai.Segment2ai;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** A 2D segment/line with 2 integer numbers.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Segment2i extends AbstractShape2i<Segment2i>
		implements Segment2ai<Shape2i<?>, Segment2i, PathElement2i, Point2i, Vector2i, Rectangle2i> {

	private static final long serialVersionUID = 4069080422632034507L;

	private int ax;

	private int ay;

	private int bx;

	private int by;

	/** Construct an empty segment.
	 */
	public Segment2i() {
		//
	}

	/** Construct a segment with the two given points.
	 * @param p1 first point.
	 * @param p2 second point.
	 */
	public Segment2i(Point2D<?, ?> p1, Point2D<?, ?> p2) {
		this(p1.ix(), p1.iy(), p2.ix(), p2.iy());
	}

	/** Constructor by copy.
	 * @param segment the segment to copy.
	 */
	public Segment2i(Segment2ai<?, ?, ?, ?, ?, ?> segment) {
		this(segment.getX1(), segment.getY1(), segment.getX2(), segment.getY2());
	}

	/** Construct a segment with the two given points.
	 * @param x1 x coordinate of the first point.
	 * @param y1 y coordinate of the first point.
	 * @param x2 x coordinate of the second point.
	 * @param y2 y coordinate of the second point.
	 */
	public Segment2i(int x1, int y1, int x2, int y2) {
		set(x1, y1, x2, y2);
	}

	@Pure
	@Override
	public int hashCode() {
		int bits = 1;
		bits = 31 * bits + Integer.hashCode(this.ax);
		bits = 31 * bits + Integer.hashCode(this.ay);
		bits = 31 * bits + Integer.hashCode(this.bx);
		bits = 31 * bits + Integer.hashCode(this.by);
		return bits ^ (bits >> 31);
	}

	@Pure
	@Override
	public Shape2i<?> createTransformedShape(Transform2D transform) {
		assert transform != null : AssertMessages.notNullParameter();
		final Point2i point = getGeomFactory().newPoint(getX1(), getY1());
		transform.transform(point);
		final int x1 = point.ix();
		final int y1 = point.iy();
		point.set(getX2(), getY2());
		transform.transform(point);
		return getGeomFactory().newSegment(x1, y1, point.ix(), point.iy());
	}

	@Override
	public void set(int x1, int y1, int x2, int y2) {
		if (this.ax != x1 || this.ay != y1 || this.bx != x2 || this.by != y2) {
			this.ax = x1;
			this.ay = y1;
			this.bx = x2;
			this.by = y2;
			fireGeometryChange();
		}
	}

	@Override
	public void setX1(int x) {
		if (this.ax != x) {
			this.ax = x;
			fireGeometryChange();
		}
	}

	@Override
	public void setY1(int y) {
		if (this.ay != y) {
			this.ay = y;
			fireGeometryChange();
		}
	}

	@Override
	public void setX2(int x) {
		if (this.bx != x) {
			this.bx = x;
			fireGeometryChange();
		}
	}

	@Override
	public void setY2(int y) {
		if (this.by != y) {
			this.by = y;
			fireGeometryChange();
		}
	}

	@Pure
	@Override
	public int getX1() {
		return this.ax;
	}

	@Pure
	@Override
	public int getY1() {
		return this.ay;
	}

	@Pure
	@Override
	public int getX2() {
		return this.bx;
	}

	@Pure
	@Override
	public int getY2() {
		return this.by;
	}

	@Override
	public Point2i getP1() {
		return getGeomFactory().newPoint(this.ax, this.ay);
	}

	@Override
	public Point2i getP2() {
		return getGeomFactory().newPoint(this.bx, this.by);
	}

}
