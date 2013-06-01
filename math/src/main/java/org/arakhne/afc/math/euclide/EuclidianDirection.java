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
package org.arakhne.afc.math.euclide;

import java.io.Serializable;

import org.arakhne.afc.math.PseudoHamelDimension;
import org.arakhne.afc.math.continous.object2d.Tuple2f;
import org.arakhne.afc.math.continous.object3d.Tuple3f;

/**
 * This interface represents a direction in an euclidian line, area or space.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface EuclidianDirection extends Cloneable, Serializable {

	/** Replies the dimension of the euclidian space where this point lies.
	 * 
	 * @return the euclidian dimension.
	 */
	public PseudoHamelDimension getMathematicalDimension();
	
	/** Compares one of the coordinates of this point with the specified value 
	 * for order.  Returns a negative integer, zero, or a positive 
	 * integer as this point coordinate is less
     * than, equal to, or greater than the specified value.
     * <p>
     * This function uses {@link float#compare(float, float)}
     * to perform the coordinate comparison.
     *
     * @param coordinateIndex is the index of the coordinate to compare to.
     * This index is between {@code 0} and the coordinate count given by
     * {@link #getMathematicalDimension()}.
     * @param value the value to be compared.
     * @return a negative integer, zero, or a positive integer as this point
     * coordinate is less than, equal to, or greater than the specified value.
     * @throws IndexOutOfBoundsException is the given coordinate index does
     * not corresponds to a point's coordinate value.
     */
	public int compareCoordinateTo(int coordinateIndex, float value);
	
	/** Compares one of the coordinates of this point with the specified value 
	 * for order.  Returns a negative integer, zero, or a positive 
	 * integer as this point coordinate is less
     * than, equal to, or greater than the specified value.
     * <p>
     * This function uses {@link GeometryUtil#epsilonCompareToDistance(float, float)}
     * to perform the coordinate comparison.
     *
     * @param coordinateIndex is the index of the coordinate to compare to.
     * This index is between {@code 0} and the coordinate count given by
     * {@link #getMathematicalDimension()}.
     * @param value the value to be compared.
     * @return a negative integer, zero, or a positive integer as this point
     * coordinate is less than, equal to, or greater than the specified value.
     * @throws IndexOutOfBoundsException is the given coordinate index does
     * not corresponds to a point's coordinate value.
     * @see GeometryUtil#epsilonCompareToDistance(float, float)
     */
	public int compareCoordinateToEpsilon(int coordinateIndex, float value);

	/** Replies the value of the coordinate at the given index.
	 * 
     * @param coordinateIndex is the index of the coordinate to compare to.
     * This index is between {@code 0} and the coordinate count given by
     * {@link #getMathematicalDimension()}.
	 * @return the coordinate value or
	 */
	public float getCoordinate(int coordinateIndex);
	
	/** Replies a Tuple2f which is containing the coordinates
	 * of this euclidian point. If the dimension of this euclidian
	 * point is not enough large to set all the fields of the tuple,
	 * they are set to {@link float#NaN}.
	 * 
	 * @param tuple is the 2-dimensional tuple to set.
	 */
	public void toTuple2f(Tuple2f<?> tuple);

	/** Replies a Tuple3f which is containing the coordinates
	 * of this euclidian point. If the dimension of this euclidian
	 * point is not enough large to set all the fields of the tuple,
	 * they are set to {@link float#NaN}.
	 * 
	 * @param tuple is the 3-dimensional tuple to set.
	 */
	public void toTuple3f(Tuple3f<?> tuple);

}
