/* 
 * $Id$
 * 
 * Copyright (C) 2005-09 Stephane GALLAND.
 * Copyright (C) 2012 Stephane GALLAND.
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

import org.arakhne.afc.math.geometry.PathElementType;
import org.eclipse.xtext.xbase.lib.Pure;

/** An element of the path.
 *
 * @author $Author: galland$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface PathElement2D extends Serializable {
	
	/** Replies the type of the element.
	 * 
	 * @return <code>true</code> if the points are
	 * the same; otherwise <code>false</code>.
	 */
	@Pure
	public PathElementType getType();

	/** Replies if the element is empty, ie. the points are the same.
	 * 
	 * @return <code>true</code> if the points are
	 * the same; otherwise <code>false</code>.
	 */
	@Pure
	public boolean isEmpty();
	
	/** Replies if the element is not empty and its drawable.
	 * Only the path elements that may produce pixels on the screen
	 * must reply <code>true</code> in this function.
	 * 
	 * @return <code>true</code> if the path element
	 * is drawable; otherwise <code>false</code>.
	 */
	@Pure
	public boolean isDrawable();

}