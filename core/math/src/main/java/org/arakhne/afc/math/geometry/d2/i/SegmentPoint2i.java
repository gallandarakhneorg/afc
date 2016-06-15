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

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.ai.Segment2ai;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** A 2D segment/line encapsulating points with 2 integer numbers.
 *
 *  <p>This segment is defined by its two extremities. It should not differ from
 *  the original Segment2i except from storage type.
 * @author $Author: tpiotrow$
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class SegmentPoint2i extends AbstractShape2i<SegmentPoint2i>
		implements Segment2ai<Shape2i<?>, SegmentPoint2i, PathElement2i, Point2i, Vector2i, Rectangle2i> {

	private static final long serialVersionUID = 4069080422632034507L;

	private Point2i p1 = new Point2i();

	private Point2i p2 = new Point2i();

	/** Construct an empty segment.
	 */
	public SegmentPoint2i() {
		//
	}

	/** Construct a segment with the two given points.
	 * @param p1 first point.
	 * @param p2 second point.
	 */
	public SegmentPoint2i(Point2D<?, ?> p1, Point2D<?, ?> p2) {
	    this(p1.ix(), p1.iy(), p2.ix(), p2.iy());
	}

	/** Construct a segment with the two given points.
	 * @param p1 first point.
	 * @param p2 second point.
	 */
	public SegmentPoint2i(Point2i p1, Point2i p2) {
		this.p1 = p1;
		this.p2 = p2;
	}

	/** Constructor by copy.
	 * @param segment the segment to copy.
	 */
	public SegmentPoint2i(Segment2ai<?, ?, ?, ?, ?, ?> segment) {
	    this(segment.getX1(), segment.getY1(), segment.getX2(), segment.getY2());
	}

	/** Constructor by copy.
	 * @param segment the segment to copy.
	 */
	public SegmentPoint2i(SegmentPoint2i segment) {
	    this.p1 = segment.p1;
	    this.p2 = segment.p2;
	}

	/** Construct a segment with the two given points.
	 * @param x1 x coordinate of the first point.
	 * @param y1 y coordinate of the first point.
	 * @param x2 x coordinate of the second point.
	 * @param y2 y coordinate of the second point.
	 */
	public SegmentPoint2i(int x1, int y1, int x2, int y2) {
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
	public Shape2i<?> createTransformedShape(Transform2D transform) {
		assert transform != null : AssertMessages.notNullParameter();
		final Point2i point = getGeomFactory().newPoint(getX1(), getY1());
		transform.transform(point);
		final int x1 = point.x;
		final int y1 = point.y;
		point.set(getX2(), getY2());
		transform.transform(point);
		return getGeomFactory().newSegment(x1, y1, point.ix(), point.iy());
	}

	@Override
	public void set(int x1, int y1, int x2, int y2) {
		if (this.p1.x != x1 || this.p1.y != y1 || this.p2.x != x2 || this.p2.y != y2) {
			this.p1.x = x1;
			this.p1.y = y1;
			this.p2.x = x2;
			this.p2.y = y2;
			fireGeometryChange();
		}
	}

	@Pure
	@Override
	public int getX1() {
	    return this.p1.x;
	}

	@Override
	public void setX1(int x) {
		if (this.p1.x != x) {
			this.p1.x = x;
			fireGeometryChange();
		}
	}

	@Pure
	@Override
	public int getY1() {
	    return this.p1.y;
	}

	@Override
	public void setY1(int y) {
		if (this.p1.y != y) {
			this.p1.y = y;
			fireGeometryChange();
		}
	}

	@Pure
	@Override
	public int getX2() {
	    return this.p2.x;
	}

	@Override
	public void setX2(int x) {
		if (this.p2.x != x) {
			this.p2.x = x;
			fireGeometryChange();
		}
	}

	@Pure
	@Override
	public int getY2() {
	    return this.p2.y;
	}

	@Override
	public void setY2(int y) {
		if (this.p2.y != y) {
			this.p2.y = y;
			fireGeometryChange();
		}
	}

	@Override
	public Point2i getP1() {
		return this.p1;
	}

	@Override
	public void setP1(int x, int y) {
	    this.p1.x = x;
	    this.p1.y = y;
	    fireGeometryChange();
	}

	/** Set the first point of the segment.
	 * @param p1 the p1 to set
	 */
	public void setP1(Point2i p1) {
	    this.p1 = p1;
	    fireGeometryChange();
	}

	@Override
	public Point2i getP2() {
		return this.p2;
	}

	@Override
	public void setP2(int x, int y) {
	    this.p2.x = x;
	    this.p2.y = y;
	    fireGeometryChange();
	}

	/** Set the second point of the segment.
	 *
	 * @param p2 the p2 to set
	 */
	public void setP2(Point2i p2) {
	    this.p1 = p2;
	    fireGeometryChange();
	}

}
