/* 
 * $Id$
 * 
 * Copyright (C) 2013 Christophe BOHRHAUER.
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
package org.arakhne.afc.math.geometry.continuous.system;

/**
 * Represents a space referencial and provides the convertion utilities.
 * 
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface CoordinateSystem {
	
	/** Replies the count of dimension in this space referential.
	 * 
	 * @return the count of dimensions.
	 */
	public float getDimensions();
	
	/** Replies if this coordinate system is a right-hand coordinate system.
	 * 
	 * @return <code>true</code> if this coordinate system is right-handed, otherwise <code>false</code>
	 */
	public boolean isRightHanded();

	/** Replies if this coordinate system is a left-hand coordinate system.
	 * 
	 * @return <code>true</code> if this coordinate system is left-handed, otherwise <code>false</code>
	 */
	public boolean isLeftHanded();

	/** Replies the name of the coordinate system.
	 * 
	 * @return the name of the coordinate system.
	 */
	public String name();

	/** Replies the index of this coordinate in the enumeration of the available
	 * coordinate systems of the same type.
	 * 
	 * @return the index of this coordinate system in the enumeration of the
	 * available coordinate systems of the same type. 
	 */
	public int ordinal();

}
