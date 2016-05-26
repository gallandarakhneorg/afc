/* 
 * $Id$
 * 
 * Copyright (C) 2005-09 Stephane GALLAND.
 * Copyright (C) 2012 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUt ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */
package org.arakhne.afc.math.geometry.d3.ai;

import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.d3.PathElement3D;
import org.eclipse.xtext.xbase.lib.Pure;

/** An element of the path.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface PathElement3ai extends PathElement3D {
	
	/** Replies the x coordinate of the starting point.
	 *
	 * @return the x coordinate, or {@link Double#NaN} if the type is {@link PathElementType#MOVE_TO}.
	 */
	@Pure
	int getFromX();
	
	/** Replies the y coordinate of the starting point.
	 *
	 * @return the y coordinate, or {@link Double#NaN} if the type is {@link PathElementType#MOVE_TO}.
	 */
	@Pure
	int getFromY();

	/** Replies the z coordinate of the starting point.
	 *
	 * @return the z coordinate, or {@link Double#NaN} if the type is {@link PathElementType#MOVE_TO}.
	 */
	@Pure
	int getFromZ();

	/** Replies the x coordinate of the first control point.
	 *
	 * @return the x coordinate, or {@link Double#NaN} if the type is {@link PathElementType#MOVE_TO},
	 * {@link PathElementType#LINE_TO}, or {@link PathElementType#CLOSE}.
	 */
	@Pure
	int getCtrlX1();
	
	/** Replies the y coordinate of the first control point.
	 *
	 * @return the y coordinate, or {@link Double#NaN} if the type is {@link PathElementType#MOVE_TO},
	 * {@link PathElementType#LINE_TO}, or {@link PathElementType#CLOSE}.
	 */
	@Pure
	int getCtrlY1();

	/** Replies the z coordinate of the first control point.
	 *
	 * @return the z coordinate, or {@link Double#NaN} if the type is {@link PathElementType#MOVE_TO},
	 * {@link PathElementType#LINE_TO}, or {@link PathElementType#CLOSE}.
	 */
	@Pure
	int getCtrlZ1();

	/** Replies the x coordinate of the second control point.
	 *
	 * @return the x coordinate, or {@link Double#NaN} if the type is {@link PathElementType#MOVE_TO},
	 * {@link PathElementType#LINE_TO}, {@link PathElementType#QUAD_TO}, or {@link PathElementType#CLOSE}.
	 */
	@Pure
	int getCtrlX2();
	
	/** Replies the y coordinate of the second  control point.
	 *
	 * @return the y coordinate, or {@link Double#NaN} if the type is {@link PathElementType#MOVE_TO},
	 * {@link PathElementType#LINE_TO}, {@link PathElementType#QUAD_TO}, or {@link PathElementType#CLOSE}.
	 */
	@Pure
	int getCtrlY2();

	/** Replies the z coordinate of the second  control point.
	 *
	 * @return the z coordinate, or {@link Double#NaN} if the type is {@link PathElementType#MOVE_TO},
	 * {@link PathElementType#LINE_TO}, {@link PathElementType#QUAD_TO}, or {@link PathElementType#CLOSE}.
	 */
	@Pure
	int getCtrlZ2();

	/** Replies the x coordinate of the target point.
	 *
	 * @return the x coordinate.
	 */
	@Pure
	int getToX();
	
	/** Replies the y coordinate of the target point.
	 *
	 * @return the y coordinate.
	 */
	@Pure
	int getToY();

	/** Replies the z coordinate of the target point.
	 *
	 * @return the z coordinate.
	 */
	@Pure
	int getToZ();

	@Pure
	@Override
	PathElementType getType();


	/** Copy the coords into the given array, except the source point.
	 * 
	 * @param array
	 */
	@Pure
	void toArray(int[] array);

	/** Copy the coords into the given array, except the source point.
	 * 
	 * @param array
	 */
	@Pure
	void toArray(double[] array);

}