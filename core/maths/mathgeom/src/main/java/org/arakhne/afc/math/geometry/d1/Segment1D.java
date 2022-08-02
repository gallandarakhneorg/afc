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

package org.arakhne.afc.math.geometry.d1;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;

/**
 * This class represents a segment is a 1.5D coordinate space.
 *
 * <p>A 1.5D point is defined by its curviline position on a segment, and
 * by a jutting/shifting distance. The jutting distance is positive or
 * negative according to the side vector of the current {@link CoordinateSystem2D}.
 *
 * <p>A segment could be implemented by a line, a spline or a polyline.
 *
 * @param <RP> is the type of point that can be returned by this tuple.
 * @param <RV> is the type of vector that can be returned by this tuple.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public interface Segment1D<RP extends Point2D<?, ?>, RV extends Vector2D<?, ?>> {

	/** Replies the length of the segment.
	 *
	 * @return the length of the segment.
	 */
	@Pure
	double getLength();

	/** Replies the 2D cooordinate of the first point of the segment.
	 *
	 * @return the 2D coordinate or <code>null</code> if 2D mapping is impossible.
	 */
	@Pure
	RP getFirstPoint();

	/** Replies the 2D cooordinates of the last point of the segment.
	 *
	 * @return the 2D coordinates or <code>null</code> if 2D mapping is impossible.
	 */
	@Pure
	RP getLastPoint();

	/** Replies the 2D tangent at the position on the segment.
	 *
	 * @param positionOnSegment is the position on the segment (in <code>0..length</code>).
	 * @return the 2D tangent at the position on the segment.
	 */
	@Pure
	RV getTangentAt(double positionOnSegment);

	/** Replies the 2D position and the 2D tangent at the position on the segment.
	 *
	 * @param positionOnSegment is the position on the segment (in <code>0..length</code>).
	 * @param position is the position to fill.
	 * @param tangent is the tangent to the segment at the given position.
	 */
	void projectsOnPlane(double positionOnSegment, Point2D<?, ?> position, Vector2D<?, ?> tangent);

	/** Replies the 2D position and the 2D tangent at the position on the segment.
	 *
	 * @param positionOnSegment is the position on the segment (in <code>0..length</code>).
	 * @param shiftDistance is the distance that permits to shift the position from the segment.
	 * @param position is the position to fill.
	 * @param tangent is the tangent to the segment at the given position.
	 */
	void projectsOnPlane(double positionOnSegment, double shiftDistance, Point2D<?, ?> position,
			Vector2D<?, ?> tangent);

	/** Replies if this segment is connected to the specified segment by its first point.
	 *
	 * @param otherSegment is the segment to match.
	 * @return <code>true</code> if this segment is connected to the given one,
	 *     otherwise <code>false</code>.
	 */
	@Pure
	boolean isFirstPointConnectedTo(Segment1D<?, ?> otherSegment);

	/** Replies if this segment is connected to the specified segment by its last point.
	 *
	 * @param otherSegment is the segment to match.
	 * @return <code>true</code> if this segment is connected to the given one,
	 *     otherwise <code>false</code>.
	 */
	@Pure
	boolean isLastPointConnectedTo(Segment1D<?, ?> otherSegment);

}

