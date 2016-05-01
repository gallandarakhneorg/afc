/* 
 * $Id$
 * 
 * Copyright (C) 2010-2013 Stephane GALLAND.
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
package org.arakhne.afc.math.generic;

import java.io.Serializable;

/** 3D shape.
 * 
 * @param <ST> is the type of the shape implementation.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see {@link org.arakhne.afc.math.geometry.d3.Shape3D}
 */
@Deprecated
public interface Shape3D<ST extends Shape3D<? super ST>>
extends Cloneable, Serializable {

	/** Replies if this shape is empty.
	 * The semantic associated to the state "empty"
	 * depends on the implemented shape. See the
	 * subclasses for details.
	 * 
	 * @return <code>true</code> if the shape is empty;
	 * <code>false</code> otherwise.
	 */
	public boolean isEmpty();

	/** Clone this shape.
	 * 
	 * @return the clone.
	 */
	public ST clone();

	/** Reset this shape to be equivalent to
	 * an just-created instance of this shape type. 
	 */
	public void clear();

	/** Replies if the given point is inside this shape.
	 * 
	 * @param p
	 * @return <code>true</code> if the given shape is intersecting this
	 * shape, otherwise <code>false</code>.
	 */
	public boolean contains(Point3D p);
	
	/** Replies the point on the shape that is closest to the given point.
	 * 
	 * @param p
	 * @return the closest point on the shape; or the point itself
	 * if it is inside the shape.
	 */
	public Point3D getClosestPointTo(Point3D p);

	/** Set this shape with the attributes of the given shape.
	 * 
	 * @param s
	 */
	public void set(ST s);

}