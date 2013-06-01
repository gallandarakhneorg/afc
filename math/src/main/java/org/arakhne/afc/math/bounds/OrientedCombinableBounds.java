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

package org.arakhne.afc.math.bounds;

import org.arakhne.afc.math.euclide.EuclidianPoint;

/** This interface representes an oriented bounds which
 * is able to be combined with other bounds.
 *
 * @param <CB> is the implementation type of this bounding box.
 * @param <P> is the type of the points when replied by a function.
 * @param <PP> is the type of the points when passed as parameter.
 * @param <V> is the type of the vectors
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface OrientedCombinableBounds<CB extends Bounds<P,PP,V>, P extends EuclidianPoint,PP,V>
extends OrientedBounds<P,PP,V>, CombinableBounds<CB,P,PP,V> {

	//

}