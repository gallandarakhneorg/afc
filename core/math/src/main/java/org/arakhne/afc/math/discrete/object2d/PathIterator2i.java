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
package org.arakhne.afc.math.discrete.object2d;

import java.util.Iterator;

import org.arakhne.afc.math.generic.PathWindingRule;


/** This interface describes an interator on path elements.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see {@link org.arakhne.afc.math.geometry.d2.discrete.PathIterator2i}
 */
@Deprecated
public interface PathIterator2i extends Iterator<PathElement2i> {

	/** Replies the winding rule for the path.
	 * 
	 * @return the winding rule for the path.
	 */
	public PathWindingRule getWindingRule();

	/** Replies the iterator may reply only elements of type
	 * <code>MOVE_TO</code>, <code>LINE_TO</code>, or
	 * <code>CLOSE</code> (no curve).
	 * 
	 * @return <code>true</code> if the iterator does not
	 * contain curve primitives, <code>false</code>
	 * otherwise.
	 */
	public boolean isPolyline();

}