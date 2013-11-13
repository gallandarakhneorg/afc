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

package org.arakhne.afc.math.geometry.continuous.object3d.bounds;

import org.arakhne.afc.math.geometry.Bounds;
import org.arakhne.afc.math.geometry.continuous.euclide.EuclidianPoint3D;
import org.arakhne.afc.math.geometry.continuous.intersection.IntersectionClassifier3D;
import org.arakhne.afc.math.geometry.continuous.object2d.bounds.Bounds2D;
import org.arakhne.afc.math.geometry.continuous.object3d.Point3f;
import org.arakhne.afc.math.geometry.continuous.object3d.Vector3f;
import org.arakhne.afc.math.geometry.continuous.system.CoordinateSystem3D;

/** This interface representes the bounds of an area
 * in a space.
 *
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Bounds3D extends Bounds<EuclidianPoint3D,Point3f,Vector3f>, IntersectionClassifier3D<Bounds3D> {

	/** Replies the type of the bound.
	 * 
	 * @return the type of the bound, or <code>null</code> if the object is not a
	 * primitive bounding volume.
	 * @since 4.0
	 */
	public BoundingPrimitiveType3D getBoundType();

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

	/** Replies the lower point of this bounds.
	 * 
	 * @return the coordinate of the lower point.
	 */
	public float getMinZ();

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

	/** Replies the upper point of this bounds.
	 * 
	 * @return the coordinate of the upper point.
	 */
	public float getMaxZ();

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

	/** Replies the center point of this bounds.
	 * 
	 * @return the coordinate of the lower point.
	 */
	public float getCenterZ();

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

	/**
	 * Replies the size along the Z-axis ofthis bounding object.
	 * 
	 * @return a positive value
	 */
	public float getSizeZ();


	/** Replies a 2D equivalent of this 3D bounds.
	 * <p>
	 * This function uses the coordinate system replied by
	 * {@link CoordinateSystem3D#getDefaultCoordinateSystem()} to
	 * compute the 2D equivalences.
	 * 
	 * @return a 2D bounds.
	 */
	public Bounds2D toBounds2D();

	/** Replies a 2D equivalent of this 3D bounds.
	 * <p>
	 * This function uses the given coordinate system to
	 * compute the 2D equivalences.
	 * 
	 * @param system
	 * @return a 2D bounds.
	 */
	public Bounds2D toBounds2D(CoordinateSystem3D system);

}