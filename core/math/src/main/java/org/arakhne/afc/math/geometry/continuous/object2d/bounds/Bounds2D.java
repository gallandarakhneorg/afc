/* 
 * $Id$
 * 
 * Copyright (c) 2013 Christophe BOHRHAUER
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

package org.arakhne.afc.math.geometry.continuous.object2d.bounds;

import org.arakhne.afc.math.geometry.Bounds;
import org.arakhne.afc.math.geometry.continuous.euclide.EuclidianPoint2D;
import org.arakhne.afc.math.geometry.continuous.intersection.IntersectionClassifier2D;
import org.arakhne.afc.math.geometry.continuous.object2d.Point2f;
import org.arakhne.afc.math.geometry.continuous.object2d.Vector2f;
import org.arakhne.afc.math.geometry.continuous.object3d.bounds.Bounds3D;
import org.arakhne.afc.math.geometry.continuous.system.CoordinateSystem3D;

/** This interface representes the bounds of an area
 * on a plan.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Bounds2D extends Bounds<EuclidianPoint2D,Point2f,Vector2f>, IntersectionClassifier2D<Bounds2D> {

	/** Replies the type of the bound.
	 * 
	 * @return the type of the bound, or <code>null</code> if the object is not a
	 * primitive bounding volume.
	 * @since 4.0
	 */
	public BoundingPrimitiveType2D getBoundType();
	
	/** Replies the lower point of this bounds.
	 * 
	 * @return the coordinate of the lower point.
	 */
	public float getMinX();

	/** Replies the lower point of this bounds.
	 * 
	 * @return the coordinate of the lower point.
	 */
	public float getMinY();

	/** Replies the upper point of this bounds.
	 * 
	 * @return the coordinate of the upper point.
	 */
	public float getMaxX();

	/** Replies the upper point of this bounds.
	 * 
	 * @return the coordinate of the upper point.
	 */
	public float getMaxY();

	/** Replies the center point of this bounds.
	 * 
	 * @return the coordinate of the lower point.
	 */
	public float getCenterX();

	/** Replies the center point of this bounds.
	 * 
	 * @return the coordinate of the lower point.
	 */
	public float getCenterY();

	/**
	 * Replies the size along the X-axis ofthis bounding object.
	 * 
	 * @return a positive value
	 */
	public float getSizeX();

	/**
	 * Replies the size along the Y-axis ofthis bounding object.
	 * 
	 * @return a positive value
	 */
	public float getSizeY();

	/** Replies a 3D equivalent of this 2D bounds.
	 * <p>
	 * This function uses the coordinate system replied by
	 * {@link CoordinateSystem2D#getDefaultCoordinateSystem()} to
	 * compute the 3D equivalences.
	 * 
	 * @return a 3D bounds.
	 */
	public Bounds3D toBounds3D();

	/** Replies a 3D equivalent of this 2D bounds.
	 * <p>
	 * This function uses the coordinate system replied by
	 * {@link CoordinateSystem2D#getDefaultCoordinateSystem()} to
	 * compute the 3D equivalences.
	 *
	 * @param z is the third coordinate of the lowest point of the replied bounds
	 * @param zsize of the size of the bounds along the third axis.
	 * @return a 3D bounds.
	 */
	public Bounds3D toBounds3D(float z, float zsize);

	/** Replies a 3D equivalent of this 2D bounds.
	 * <p>
	 * This function uses the given coordinate system to
	 * compute the 3D equivalences.
	 * 
	 * @param system
	 * @return a 3D bounds.
	 */
	public Bounds3D toBounds3D(CoordinateSystem3D system);

	/** Replies a 3D equivalent of this 2D bounds.
	 * <p>
	 * This function uses the given coordinate system to
	 * compute the 3D equivalences.
	 *
	 * @param z is the third coordinate of the lowest point of the replied bounds
	 * @param zsize of the size of the bounds along the third axis.
	 * @param system
	 * @return a 3D bounds.
	 */
	public Bounds3D toBounds3D(float z, float zsize, CoordinateSystem3D system);

}