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

package org.arakhne.afc.math.bounds.bounds3f;

import org.arakhne.afc.math.bounds.OrientedBounds;
import org.arakhne.afc.math.continous.object3d.Point3f;
import org.arakhne.afc.math.continous.object3d.Vector3f;
import org.arakhne.afc.math.euclide.EuclidianPoint3D;

/** This interface representes an oriented 3D bounds, ie
 * a bounds which could have not its local axis parallel
 * to the global axis.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface OrientedBounds3D extends OrientedBounds<EuclidianPoint3D, Point3f, Vector3f> {

	/**
	 * Replies the R local axis of the oriented bounds.
	 * 
	 * @return the R local
	 */
	public Vector3f getR();
	
	/**
	 * Replies the S local axis of the oriented bounds.
	 * 
	 * @return the S local
	 */
	public Vector3f getS();

	/**
	 * Replies the T local axis of the oriented bounds.
	 * 
	 * @return the T local
	 */
	public Vector3f getT();

	/**
	 * Replies the extent along the R local axis.
	 * 
	 * @return the R extent
	 */
	public float getRExtent();

	/**
	 * Replies the extent along the S local axis.
	 * 
	 * @return the S extent
	 */
	public float getSExtent();

	/**
	 * Replies the extent along the T local axis.
	 * 
	 * @return the T extent
	 */
	public float getTExtent();
	
}