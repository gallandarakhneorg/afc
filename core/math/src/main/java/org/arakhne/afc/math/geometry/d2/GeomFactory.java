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

/** Factory of geometrical primitives.
 * 
 * @param <V> the types of the vectors.
 * @param <P> is the type of the points.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface GeomFactory<V extends Vector2D<? super V, ? super P>, P extends Point2D<? super P, ? super V>> {

	/** Convert the given point if it is not of the right type.
	 *
	 * @param p the point to convert. 
	 * @return <code>p</code> if it is of type <code>P</code>, or a copy of <code>p</code>.
	 */
	P convertToPoint(Point2D<?, ?> p);
	
	/** Convert the given point.
	 *
	 * @param p the point to convert. 
	 * @return the vector.
	 */
	V convertToVector(Point2D<?, ?> p);

	/** Convert the given vector.
	 *
	 * @param v the vector to convert. 
	 * @return the point.
	 */
	P convertToPoint(Vector2D<?, ?> v);
	
	/** Convert the given vector.
	 *
	 * @param v the vector to convert. 
	 * @return the vector.
	 */
	V convertToVector(Vector2D<?, ?> v);

	/** Create a point.
	 *
	 * @return the point.
	 */
	P newPoint();

	/** Create a vector.
	 *
	 * @return the vector.
	 */
	V newVector();

	/** Create a point.
	 *
	 * @param x
	 * @param y 
	 * @return the point.
	 */
	P newPoint(double x, double y);

	/** Create a vector.
	 *
	 * @param x
	 * @param y 
	 * @return the vector.
	 */
	V newVector(double x, double y);

	/** Create a point.
	 *
	 * @param x
	 * @param y 
	 * @return the point.
	 */
	P newPoint(int x, int y);

	/** Create a vector.
	 *
	 * @param x
	 * @param y 
	 * @return the vector.
	 */
	V newVector(int x, int y);

}