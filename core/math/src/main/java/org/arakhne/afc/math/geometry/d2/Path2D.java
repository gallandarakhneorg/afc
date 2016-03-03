/* 
 * $Id$
 * 
 * Copyright (C) 2013 Stephane GALLAND.
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

import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.eclipse.xtext.xbase.lib.Pure;

/** 2D Path.
 * 
 * @param <PT> is the type of the path implementation.
 * @param <B> is the type of the bounding box.
 * @param <I> is the type of the iterator used to obtain the elements of the path.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Path2D<
		PT extends Shape2D<? super PT, ? super I>,
		B extends Shape2D<?,?>,
		I extends Iterator<? extends PathElement2D>> extends Shape2D<PT, I> {

	/**
	 * Replies the bounds of this path.
	 * 
	 * @return the bounds
	 */
	public B toBoundingBox();

	/** Replies the winding rule for the path.
	 * 
	 * @return the winding rule for the path.
	 */
	@Pure
	public PathWindingRule getWindingRule();
	
	/** Replies the path is composed only by
	 * <code>MOVE_TO</code>, <code>LINE_TO</code>
	 * or <code>CLOSE</code> primitives (no curve).
	 * 
	 * @return <code>true</code> if the path does not
	 * contain curve primitives, <code>false</code>
	 * otherwise.
	 */
	@Pure
	public boolean isPolyline();

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
	public boolean isPolygon();

	/** Replies an iterator on the path elements.
	 * <p>
	 * Only {@link PathElementType#MOVE_TO},
	 * {@link PathElementType#LINE_TO}, and 
	 * {@link PathElementType#CLOSE} types are returned by the iterator.
	 * <p>
	 * The amount of subdivision of the curved segments is controlled by the 
	 * flatness parameter, which specifies the maximum distance that any point 
	 * on the unflattened transformed curve can deviate from the returned
	 * flattened path segments. Note that a limit on the accuracy of the
	 * flattened path might be silently imposed, causing very small flattening
	 * parameters to be treated as larger values. This limit, if there is one,
	 * is defined by the particular implementation that is used.
	 * <p>
	 * The iterator for this class is not multi-threaded safe.
	 * 
	 * @param flatness is the maximum distance that the line segments used to approximate
	 * the curved segments are allowed to deviate from any point on the original curve.
	 * @return an iterator on the path elements.
	 */
	@Pure
	public I getPathIterator(double flatness);	

}