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

import org.arakhne.afc.math.continous.object3d.Point3f;
import org.arakhne.afc.math.continous.object3d.Vector3f;

/** This interface representes the bounds of an area
 * in a space that could be translated.
 *
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface TranslatableBounds3D extends Bounds3D {

	/** Translate the bounds.
	 * 
	 * @param v
	 */
	public void translate(Vector3f v);

	/** Set the position of the bounds.
	 * <p>
	 * The position of the bounds depends on the bounding box implementation.
	 * Basically the position corresponds to the bounding box center.
	 * 
	 * @param position
	 * @see #setTranslation(Point3f, boolean)
	 */
	public void setTranslation(Point3f position);

	/** Set the position of the bounds.
	 * <p>
	 * If <var>onGround</var> is <code>true</code>, this function
	 * is assuming that the given position is corresponding
	 * to the lowest face of the bounding box (the face on the ground).
	 * If <var>onGround</var> is <code>false</code> the given position
	 * will correspond to the standard position of the box (see
	 * {@link #setTranslation(Point3f)}.
	 * 
	 * @param position
	 * @param onGround indicates if the given position is corresponding
	 * to the position of the lowest face of the bounding box (if
	 * <var>onGround</var> is <code>true</code>) or to the default
	 * bounding box position (if <var>onGround</var> is <code>false</code>).
	 * @see #setTranslation(Point3f)
	 */
	public void setTranslation(Point3f position, boolean onGround);

}