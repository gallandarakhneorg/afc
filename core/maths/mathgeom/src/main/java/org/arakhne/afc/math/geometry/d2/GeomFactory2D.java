/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.arakhne.afc.math.geometry.d2;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.GeomFactoryBase;

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
public interface GeomFactory2D<V extends Vector2D<? super V, ? super P>, P extends Point2D<? super P, ? super V>>
		extends GeomFactoryBase {

	/** Convert the given point if it is not of the right type.
	 *
	 * @param pt the point to convert.
	 * @return <code>p</code> if it is of type <code>P</code>, or a copy of <code>p</code>.
	 */
	@Pure
	P convertToPoint(Point2D<?, ?> pt);

	/** Convert the given vector.
	 *
	 * @param vector the vector to convert.
	 * @return the point.
	 */
	@Pure
	P convertToPoint(Vector2D<?, ?> vector);

	/** Convert the given point.
	 *
	 * @param pt the point to convert.
	 * @return the vector.
	 */
	@Pure
	V convertToVector(Point2D<?, ?> pt);

	/** Convert the given vector.
	 *
	 * @param vector the vector to convert.
	 * @return the vector.
	 */
	@Pure
	V convertToVector(Vector2D<?, ?> vector);

	/** Create a point.
	 *
	 * @return the point.
	 */
	@Pure
	P newPoint();

	/** Create a point.
	 *
	 * @param x x coordinate of the point.
	 * @param y y coordinate of the point.
	 * @return the point.
	 */
	@Pure
	P newPoint(double x, double y);

	/** Create a point.
	 *
	 * @param x x coordinate of the point.
	 * @param y y coordinate of the point.
	 * @return the point.
	 */
	@Pure
	P newPoint(int x, int y);

	/** Create a vector.
	 *
	 * @return the vector.
	 */
	@Pure
	V newVector();

	/** Create a vector.
	 *
	 * @param x x coordinate of the vector.
	 * @param y y coordinate of the vector.
	 * @return the vector.
	 */
	@Pure
	V newVector(double x, double y);

	/** Create a vector.
	 *
	 * @param x x coordinate of the vector.
	 * @param y y coordinate of the vector.
	 * @return the vector.
	 */
	@Pure
	V newVector(int x, int y);

}
