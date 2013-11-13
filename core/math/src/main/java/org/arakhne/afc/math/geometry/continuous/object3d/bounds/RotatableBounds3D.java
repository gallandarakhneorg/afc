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

/** This interface representes the bounds of an area
 * in a space that could be rotated.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface RotatableBounds3D extends Bounds3D {

	/** Rotate the bounds around its center.
	 * 
	 * @param rotation
	 */
	public void rotate(AxisAngle4f rotation);

	/** Rotate the bounds around its center.
	 * 
	 * @param rotation
	 */
	public void rotate(Quaternion rotation);

	/** Set the rotation of the bounds around its center.
	 * 
	 * @param rotation
	 */
	public void setRotation(AxisAngle4f rotation);

	/** Set the rotation of the bounds around its center.
	 * 
	 * @param rotation
	 */
	public void setRotation(Quaternion rotation);
	
}