/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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

package org.arakhne.afc.math.geometry.d1.d;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.d1.Segment1D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Vector2d;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/**
 * Basic implementation of a 1.5D segment.
 *
 * <p>A 1.5D point is defined by its curviline position on a segment, and
 * by a jutting/shifting distance. The jutting distance is positive or
 * negative according to the side vector of the current {@link CoordinateSystem2D}.
 *
 * <p>A segment could be implemented by a line, a spline or a polyline.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class DefaultSegment1d implements Segment1D<Point2d, Vector2d> {

	private Point2d start;

	private Point2d end;

	private double length = Double.NaN;

	/** Construct a segment.
	 *
	 * @param start the start point.
	 * @param end the end point.
	 */
	public DefaultSegment1d(Point2d start, Point2d end) {
		assert start != null : AssertMessages.notNullParameter(0);
		assert end != null : AssertMessages.notNullParameter(1);
		this.start = start;
		this.end = end;
	}

	@Pure
	@Override
	public double getLength() {
		if (Double.isNaN(this.length)) {
			this.length = this.start.getDistance(this.end);
		}
		return this.length;
	}

	/** Change the length of the segment.
	 *
	 * @param length the new length.
	 */
	public void setLength(double length) {
		this.length = length;
	}

	@Pure
	@Override
	public Point2d getFirstPoint() {
		return this.start;
	}

	@Pure
	@Override
	public Point2d getLastPoint() {
		return this.end;
	}

	@Pure
	@Override
	public Vector2d getTangentAt(double positionOnSegment) {
		final Vector2d v = new Vector2d();
		v.sub(this.end, this.start);
		v.normalize();
		return v;
	}

	@Pure
	@Override
	public void projectsOnPlane(double positionOnSegment, Point2D<?, ?> position, Vector2D<?, ?> tangent) {
		final Vector2D<?, ?> vector;
		if (tangent == null) {
			vector = new Vector2d();
		} else {
			vector = tangent;
		}

		vector.sub(this.end, this.start);
		vector.normalize();

		if (position != null) {
			vector.scale(positionOnSegment);
			position.set(this.start);
			position.add(vector);
			vector.normalize();
		}
	}

	@Pure
	@Override
	public void projectsOnPlane(double positionOnSegment, double shiftDistance, Point2D<?, ?> position,
			Vector2D<?, ?> tangent) {
		final Vector2D<?, ?> vector;
		if (tangent == null) {
			vector = new Vector2d();
		} else {
			vector = tangent;
		}

		vector.sub(this.end, this.start);
		vector.normalize();

		if (position != null) {
			vector.scale(positionOnSegment);
			position.set(this.start);
			position.add(vector);
			vector.normalize();

			vector.makeOrthogonal();
			vector.scale(shiftDistance);

			position.add(vector);
		}
	}

	@Pure
	@Override
	public boolean isFirstPointConnectedTo(Segment1D<?, ?> otherSegment) {
		return this.start.equals(otherSegment.getFirstPoint())
				|| this.start.equals(otherSegment.getLastPoint());
	}

	@Pure
	@Override
	public boolean isLastPointConnectedTo(Segment1D<?, ?> otherSegment) {
		return this.end.equals(otherSegment.getFirstPoint())
				|| this.end.equals(otherSegment.getLastPoint());
	}

}

