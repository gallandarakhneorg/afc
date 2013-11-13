/* 
 * $Id$
 * 
 * Copyright (C) 2013 Christophe BOHRHAUER.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */
package org.arakhne.afc.math.geometry.continuous.object1d;

import org.arakhne.afc.math.geometry.continuous.object2d.Point2f;
import org.arakhne.afc.math.geometry.continuous.object2d.Vector2f;
import org.arakhne.afc.math.geometry.continuous.system.CoordinateSystem2D;

/**
 * This class represents a segment is a 1D or 1.5D coordinate space.
 * <p>
 * A 1D point is defined by its curviline position on a segment.
 * <p>
 * A 1.5D point is defined by its curviline position on a segment, and
 * by a jutting/shifting distance. The jutting distance is positive or
 * negative according to the side vector of the current {@link fr.utbm.set.geom.system.CoordinateSystem2D}.
 * <p>
 * A segment could be implemented by a line, a spline or a polyline.
 * 
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Segment1D {

    /** Replies the length of the segment.
     * 
     * @return the length of the segment.
     */
	public float getLength();

    /** Replies the 2D cooordinate of the first point of the segment.
     * 
     * @return the 2D coordinate or <code>null</code> if 2D mapping is impossible.
     */
	public Point2f getFirstPoint();

    /** Replies the 2D cooordinates of the last point of the segment.
     * 
     * @return the 2D coordinates or <code>null</code> if 2D mapping is impossible.
     */
	public Point2f getLastPoint();
	
    /** Replies the 2D tangent at the position on the segment.
     *
     * @param positionOnSegment is the position on the segment (in <code>0..length</code>).
     * @return the 2D tangent at the position on the segment.
     */
	public Vector2f getTangentAt(float positionOnSegment);

    /** Replies the 2D position and the 2D tangent at the position on the segment.
    *
    * @param positionOnSegment is the position on the segment (in <code>0..length</code>).
    * @param position is the position to fill.
    * @param tangent is the tangent to the segment at the given position.
    * @param system is the coordinate system to use for convertion. 
    */
	public void projectsOnPlane(float positionOnSegment, Point2f position, Vector2f tangent, CoordinateSystem2D system);

    /** Replies the 2D position and the 2D tangent at the position on the segment.
    *
    * @param positionOnSegment is the position on the segment (in <code>0..length</code>).
    * @param shiftDistance is the distance that permits to shift the position from the segment.
    * @param position is the position to fill.
    * @param tangent is the tangent to the segment at the given position.
    * @param system is the coordinate system to use for convertion. 
    */
	public void projectsOnPlane(float positionOnSegment, float shiftDistance, Point2f position, Vector2f tangent, CoordinateSystem2D system);

	/** Replies if this segment is connected to the specified segment by its first point.
	 * 
	 * @param otherSegment is the segment to match. 
	 * @return <code>true</code> if this segment is connected to the given one,
	 * otherwise <code>false</code>
	 */
	public boolean isFirstPointConnectedTo(Segment1D otherSegment);

	/** Replies if this segment is connected to the specified segment by its last point.
	 * 
	 * @param otherSegment is the segment to match. 
	 * @return <code>true</code> if this segment is connected to the given one,
	 * otherwise <code>false</code>
	 */
	public boolean isLastPointConnectedTo(Segment1D otherSegment);

}

