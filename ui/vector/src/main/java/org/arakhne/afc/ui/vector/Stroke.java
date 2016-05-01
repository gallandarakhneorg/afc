/* 
 * $Id$
 * 
 * Copyright (C) 2012 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
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
package org.arakhne.afc.ui.vector;



/** Interface that is representing a stroke. 
 * See {@link VectorToolkit} to create an instance.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
public interface Stroke {

	/** Default line join.
	 */
	public LineJoin DEFAULT_LINE_JOIN = LineJoin.MITER;
	
	/** Default end cap.
	 */
	public EndCap DEFAULT_END_CAP = EndCap.SQUARE;
	
	/** Default miter limit.
	 */
	public float DEFAULT_MITTER_LIMIT = 10f;

	/**
	 * Returns the array representing the lengths of the dash segments.
	 * Alternate entries in the array represent the user space lengths
	 * of the opaque and transparent segments of the dashes.
	 * As the pen moves along the outline of the <code>Shape</code>
	 * to be stroked, the user space
	 * distance that the pen travels is accumulated.  The distance
	 * value is used to index into the dash array.
	 * The pen is opaque when its current cumulative distance maps
	 * to an even element of the dash array and transparent otherwise.
	 * 
	 * @return the dash array.
	 */
	public float[] getDashArray();

	/**
	 * Returns the current dash phase.
	 * The dash phase is a distance specified in user coordinates that
	 * represents an offset into the dashing pattern. In other words, the dash
	 * phase defines the point in the dashing pattern that will correspond to
	 * the beginning of the stroke.
	 * 
	 * @return the dash phase as a <code>float</code> value.
	 */
	public float getDashPhase();

	/** Replies the width of the line.
	 * 
	 * @return the width of the line.
	 */
	public float getLineWidth();

	/** Replies the type of the line join.
	 * 
	 * @return the type of the line join.
	 */
	public LineJoin getLineJoin();

	/** Replies the type of the end cap for the stroke.
	 * 
	 * @return the type of the end cap for the stroke.
	 */
	public EndCap getEndCap();

	/**
	 * Returns the limit of miter joins.
	 * 
	 * @return the limit of miter joins.
	 */
	public float getMiterLimit();

	/** Define the types of line joins for a stroke. 
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public enum LineJoin {

		/**
		 * Joins path segments by extending their outside edges until
		 * they meet.
		 */
		MITER,

		/**
		 * Joins path segments by rounding off the corner at a radius
		 * of half the line width.
		 */
		ROUND,

		/**
		 * Joins path segments by connecting the outer corners of their
		 * wide outlines with a straight segment.
		 */
		BEVEL;

	}

	/** Define the types of end caps for a stroke. 
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public enum EndCap {

		/**
		 * Ends unclosed subpaths and dash segments with no added
		 * decoration.
		 */
		BUTT,

		/**
		 * Ends unclosed subpaths and dash segments with a round
		 * decoration that has a radius equal to half of the width
		 * of the pen.
		 */
		ROUND,

		/**
		 * Ends unclosed subpaths and dash segments with a square
		 * projection that extends beyond the end of the segment
		 * to a distance equal to half of the line width.
		 */
		SQUARE;

	}

}
