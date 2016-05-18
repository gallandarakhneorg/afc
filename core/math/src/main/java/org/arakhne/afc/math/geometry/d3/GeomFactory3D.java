/* 
 * $Id$
 * 
 * Copyright (C) 2010-2016 Stephane GALLAND.
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
package org.arakhne.afc.math.geometry.d3;

/** Factory of geometrical primitives.
 * 
 * @param <V> the types of the vectors.
 * @param <P> is the type of the points.
 * @author $Author: tpiotrow$
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface GeomFactory3D<V extends Vector3D<? super V, ? super P>, P extends Point3D<? super P, ? super V>> {
	
	/** Convert the given point if it is not of the right type.
	 *
	 * @param p the point to convert. 
	 * @return <code>p</code> if it is of type <code>P</code>, or a copy of <code>p</code>.
	 */
	P convertToPoint(Point3D<?, ?> p);
	
	/** Convert the given point.
	 *
	 * @param p the point to convert. 
	 * @return the vector.
	 */
	V convertToVector(Point3D<?, ?> p);

	/** Convert the given vector.
	 *
	 * @param v the vector to convert. 
	 * @return the point.
	 */
	P convertToPoint(Vector3D<?, ?> v);
	
	/** Convert the given vector.
	 *
	 * @param v the vector to convert. 
	 * @return the vector.
	 */
	V convertToVector(Vector3D<?, ?> v);

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
	 * @param z 
	 * @return the point.
	 */
	P newPoint(double x, double y, double z);

	/** Create a vector.
	 *
	 * @param x
	 * @param y 
	 * @param z 
	 * @return the vector.
	 */
	V newVector(double x, double y, double z);

	/** Create a point.
	 *
	 * @param x
	 * @param y 
	 * @param z 
	 * @return the point.
	 */
	P newPoint(int x, int y, int z);

	/** Create a vector.
	 *
	 * @param x
	 * @param y 
	 * @param z 
	 * @return the vector.
	 */
	V newVector(int x, int y, int z);
}
