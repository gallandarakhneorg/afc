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
package org.arakhne.afc.math.geometry.d2;

import java.io.Serializable;
import java.util.Iterator;

import org.eclipse.xtext.xbase.lib.Pure;

/** 2D shape.
 * 
 * @param <ST> is the type of the shape implementation.
 * @param <I> is the type of the iterator used to obtain the elements of the path.
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Shape2D<
		ST extends Shape2D<? super ST, ? super I>,
		I extends Iterator<? extends PathElement2D>>
		extends Cloneable, Serializable {

	/** Replies if this shape is empty.
	 * The semantic associated to the state "empty"
	 * depends on the implemented shape. See the
	 * subclasses for details.
	 * 
	 * @return <code>true</code> if the shape is empty;
	 * <code>false</code> otherwise.
	 */
	@Pure
	public boolean isEmpty();

	/** Clone this shape.
	 * 
	 * @return the clone.
	 */
	@Pure
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
	@Pure
	public boolean contains(Point2D p);
	
	/** Replies the point on the shape that is closest to the given point.
	 * 
	 * @param p
	 * @return the closest point on the shape; or the point itself
	 * if it is inside the shape.
	 */
	@Pure
	public Point2D getClosestPointTo(Point2D p);
	
	/** Replies the point on the shape that is farthest the given point.
	 * 
	 * @param p
	 * @return the farthest point on the shape.
	 */
	@Pure
	public Point2D getFarthestPointTo(Point2D p);

	/** Set this shape with the attributes of the given shape.
	 * 
	 * @param s
	 */
	public void set(ST s);

	/** Replies an iterator on the path elements.
	 * <p>
	 * The iterator for this class is not multi-threaded safe.
	 * 
	 * @return an iterator on the path elements.
	 */
	@Pure
	public I getPathIterator();

}