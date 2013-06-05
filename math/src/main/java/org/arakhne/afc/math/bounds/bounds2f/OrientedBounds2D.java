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

package org.arakhne.afc.math.bounds.bounds2f;

import org.arakhne.afc.math.bounds.OrientedBounds;
import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object2d.Vector2f;
import org.arakhne.afc.math.euclide.EuclidianPoint2D;

/** This interface representes an oriented 2D bounds, ie
 * a bounds which could have not its local axis parallel
 * to the global axis.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface OrientedBounds2D extends OrientedBounds<EuclidianPoint2D,Point2f,Vector2f> {

	/**
	 * Replies the R local axis of the oriented bounds.
	 * 
	 * @return the R local
	 */
	public Vector2f getR();
	
	/**
	 * Replies the S local axis of the oriented bounds.
	 * 
	 * @return the S local
	 */
	public Vector2f getS();

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

}