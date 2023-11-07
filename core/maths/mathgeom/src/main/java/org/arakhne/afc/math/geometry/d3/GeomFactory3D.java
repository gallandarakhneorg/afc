/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

package org.arakhne.afc.math.geometry.d3;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.GeomFactoryBase;

/** Factory of geometrical primitives.
 *
 * @param <V> the types of the vectors.
 * @param <P> is the type of the points.
 * @param <Q> is the type of the quaternions.
 * @author $Author: tpiotrow$
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface GeomFactory3D<V extends Vector3D<? super V, ? super P, ? super Q>,
		P extends Point3D<? super P, ? super V, ? super Q>, Q extends Quaternion<? super P, ? super V, ? super Q>>
		extends GeomFactoryBase {

	/** Convert the given point if it is not of the right type.
	 *
	 * @param point the point to convert.
	 * @return {@code p} if it is of type {@code P}, or a copy of {@code p}.
	 */
	@Pure
	P convertToPoint(Point3D<?, ?, ?> point);

	/** Convert the given vector.
	 *
	 * @param vector the vector to convert.
	 * @return the point.
	 */
	@Pure
	P convertToPoint(Vector3D<?, ?, ?> vector);

	/** Convert the given point.
	 *
	 * @param point the point to convert.
	 * @return the vector.
	 */
	@Pure
	V convertToVector(Point3D<?, ?, ?> point);

	/** Convert the given vector.
	 *
	 * @param vector the vector to convert.
	 * @return the vector.
	 */
	@Pure
	V convertToVector(Vector3D<?, ?, ?> vector);

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
	 * @param z z coordinate of the point.
	 * @return the point.
	 */
	@Pure
	P newPoint(double x, double y, double z);

	/** Create a point.
	 *
	 * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @param z z coordinate of the point.
	 * @return the point.
	 */
	@Pure
	P newPoint(int x, int y, int z);

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
     * @param z z coordinate of the vector.
	 * @return the vector.
	 */
	@Pure
	V newVector(double x, double y, double z);

	/** Create a vector.
	 *
	 * @param x x coordinate of the vector.
     * @param y y coordinate of the vector.
     * @param z z coordinate of the vector.
	 * @return the vector.
	 */
	@Pure
	V newVector(int x, int y, int z);

	/** Creates a Quaternion.
	 *
	 * @param x the x component of the quaternion.
	 * @param y the y component of the quaternion.
	 * @param z the z component of the quaternion.
	 * @param w the w component of the quaternion.
	 * @return the quaternion.
	 * @since 18.0
	 */
	@Pure
	Q newQuaternion(double x, double y, double z, double w);

	/** Creates a Quaternion.
	 *
	 * @param x the x component of the quaternion.
	 * @param y the y component of the quaternion.
	 * @param z the z component of the quaternion.
	 * @param w the w component of the quaternion.
	 * @return the quaternion.
	 * @since 18.0
	 */
	@Pure
	Q newQuaternion(int x, int y, int z, int w);

	/** Creates a Quaternion from an axis-angle.
	 *
	 * @param x the x component of the rotation axis.
	 * @param y the y component of the rotation axis.
	 * @param z the z component of the rotation axis.
	 * @param angle the rotation angle around {@code (x, y, z)}.
	 * @return the quaternion.
	 * @since 18.0
	 */
	@Pure
	Q newQuaternionFromAxisAngle(double x, double y, double z, double angle);

	/** Creates a Quaternion from an axis-angle.
	 *
	 * @param x the x component of the rotation axis.
	 * @param y the y component of the rotation axis.
	 * @param z the z component of the rotation axis.
	 * @param angle the rotation angle around {@code (x, y, z)}.
	 * @return the quaternion.
	 * @since 18.0
	 */
	@Pure
	Q newQuaternionFromAxisAngle(int x, int y, int z, int angle);

}
