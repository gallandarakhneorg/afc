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

import java.util.Iterator;

import org.arakhne.afc.math.geometry.PathWindingRule;
import org.eclipse.xtext.xbase.lib.Pure;


/** This interface describes an interator on path elements.
 *
 * @param <T> the type of the path elements.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface PathIterator2D<T extends PathElement2D> extends Iterator<T> {

	/** Replies the winding rule for the path.
	 * 
	 * @return the winding rule for the path.
	 */
	@Pure
	PathWindingRule getWindingRule();

	/** Replies the path is composed only by
	 * one <code>MOVE_TO</code>, and a sequence of <code>LINE_TO</code>
	 * primitives.
	 * 
	 * @return <code>true</code> if the path does not
	 * contain curve primitives, <code>false</code>
	 * otherwise.
	 */
	@Pure
	boolean isPolyline();

	/** Replies the path contains a curve..
	 * 
	 * @return <code>true</code> if the path does not
	 * contain curve primitives, <code>false</code>
	 * otherwise.
	 */
	@Pure
	boolean isCurved();

	/** Replies the path has multiple parts, i.e. multiple <code>MOVE_TO</code> are inside.
	 * primitives.
	 * 
	 * @return <code>true</code> if the path has multiple move-to primitive, <code>false</code>
	 * otherwise.
	 */
	@Pure
	boolean isMultiParts();

	/** Replies the path is composed only by
	 * one <code>MOVE_TO</code>, a sequence of <code>LINE_TO</code>
	 * or <code>QUAD_TO</code> or <code>CURVE_TO</code>, and a
	 * single <code>CLOSE</code> primitives.
	 * 
	 * @return <code>true</code> if the path does not
	 * contain curve primitives, <code>false</code>
	 * otherwise.
	 */
	@Pure
	boolean isPolygon();

}