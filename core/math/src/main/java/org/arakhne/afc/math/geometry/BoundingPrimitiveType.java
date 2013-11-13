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

package org.arakhne.afc.math.geometry;


/** This interface contains all the types of primitive bounding volumes supported by
 * the API.
 * <p>
 * The primitive bounding volumes are the bounding volumes which cannot be decomposed
 * into sub volumes.
 *
 * @param <B> is the type of bounds supported by this interface.
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface BoundingPrimitiveType<B extends Bounds<?,?>> {

	/** Replies the name of the primitive type.
	 * 
	 * @return the name of the primitive type.
	 */
	public String name();
	
	/** Replies the index of this bounding volume type in the enumeration of the available
	 * bounding volume types of the same type.
	 * 
	 * @return the index of this bounding volume type in the enumeration of the
	 * available bounding volume types of the same type. 
	 */
	public int ordinal();

	/** Replies the count of dimension in this space referential.
	 * 
	 * @return the count of dimensions.
	 */
	public float getDimensions();
	
	/** Replies the class associated to the primitive type.
	 * 
	 * @return the class associated to the primitive type.
	 */
	public Class<? extends B> getInstanceType();
	
	/** Replies the new instance of bouding volume of the given primitive type.
	 * 
	 * @return the new instance of bouding volume of the given primitive type.
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public B newInstance() throws InstantiationException, IllegalAccessException;
	
}