/* 
 * $Id$
 * 
 * Copyright (C) 2010-2013 Christophe BOHRHAUER.
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

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.PseudoHamelDimension;
import org.arakhne.afc.math.geometry.continuous.euclide.EuclidianDirection;
import org.arakhne.afc.math.geometry.continuous.object2d.Tuple2f;
import org.arakhne.afc.math.geometry.continuous.object3d.Tuple3f;

/**
 * This class represents a direction in a 1D or 1.5D coordinate space.
 * <p>
 * A 1D direction is related to a {@link Segment1D}.
 * 
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public enum Direction1D{

	/** Same direction as a {@link Segment1D}.
	 */
	SEGMENT_DIRECTION,
    
	/** Reverted direction then a {@link Segment1D}.
	 */
	REVERTED_DIRECTION,

	/** Both directions of a {@link Segment1D}.
	 */
	BOTH_DIRECTIONS;
	
	/** Replies if the segment direction is allowed.
	 * 
	 * @return <code>true</code> if the segment direction is allowed, otherwise <code>false</code>
	 */
	public boolean isSegmentDirection() {
		return this==SEGMENT_DIRECTION || this==BOTH_DIRECTIONS;
	}
	
	/** Replies if the reverted segment direction is allowed.
	 * 
	 * @return <code>true</code> if the reverted segment direction is allowed, otherwise <code>false</code>
	 */
	public boolean isRevertedSegmentDirection() {
		return this==REVERTED_DIRECTION || this==BOTH_DIRECTIONS;
	}

	/** Replies the direction according to the given integer value.
	 * <p>
	 * The <var>value</var> is mapped to <code>SEGMENT_DIRECTION</code> if positive.
	 * It is mapped to <code>REVERSED_SEGMENT_DIRECTION</code> if negative. And it
	 * is mapped to <code>BOTH_DIRECTIONS</code> if nul.
	 *
	 * @param value
	 * @return the direction which is corresponding to the given <var>value</var>.
	 */
	public static Direction1D fromInteger(int value) {
		if (value<0) return REVERTED_DIRECTION;
		if (value>0) return SEGMENT_DIRECTION;
		return BOTH_DIRECTIONS;
	}

	/** Replies integer value that is corresponding to the direction.
	 * <p>
	 * The returned value is positive if <code>SEGMENT_DIRECTION</code>.
	 * It is negative if <code>REVERSED_DIRECTION</code> if negative. And it
	 * is zero if <code>BOTH_DIRECTIONS</code>.
	 *
	 * @return the direction which is corresponding to the given <var>value</var>.
	 */
	public int toInteger() {
		switch(this) {
		case BOTH_DIRECTIONS:
			return 0;
		case SEGMENT_DIRECTION:
			return 1;
		case REVERTED_DIRECTION:
			return -1;
		default:
			return 0;
		}
	}

}
