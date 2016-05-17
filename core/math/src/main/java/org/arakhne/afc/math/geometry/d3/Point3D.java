/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
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

/** 3D Point.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Point3D<RP extends Point3D<? super RP, ? super RV>, RV extends Vector3D<? super RP, ? super RV>> extends Tuple3D<RP> {

	/**
	 * Computes the square of the distance between this point and point p1.
	 * @param p1 the other point
	 * @return the distance.
	 */
	@Pure
	double getDistanceSquared(Point3D p1);

	/**
	 * Computes the distance between this point and point p1.
	 * @param p1 the other point
	 * @return the distance.
	 */
	@Pure
	double getDistance(Point3D p1);

	/**
	 * Computes the L-1 (Manhattan) distance between this point and
	 * point p1.  The L-1 distance is equal to abs(x1-x2) + abs(y1-y2).
	 * @param p1 the other point
	 * @return the distance.
	 */
	@Pure
	double getDistanceL1(Point3D p1);

	/**
	 * Computes the L-infinite distance between this point and
	 * point p1.  The L-infinite distance is equal to
	 * MAX[abs(x1-x2), abs(y1-y2)].
	 * @param p1 the other point
	 * @return the distance.
	 */
	@Pure
	double getDistanceLinf(Point3D p1);

	/**
	 * Computes the square of the distance between this point and point p1.
	 * @param p1 the other point
	 * @return the distance.
	 */
	@Pure
	int distanceSquared(Point3D p1);

	/**
	 * Computes the distance between this point and point p1.
	 * @param p1 the other point
	 * @return the distance.
	 */
	@Pure
	int distance(Point3D p1);

	/**
	 * Computes the L-1 (Manhattan) distance between this point and
	 * point p1.  The L-1 distance is equal to abs(x1-x2) + abs(y1-y2).
	 * @param p1 the other point
	 * @return the distance.
	 */
	@Pure
	int distanceL1(Point3D p1);

	/**
	 * Computes the L-infinite distance between this point and
	 * point p1.  The L-infinite distance is equal to
	 * MAX[abs(x1-x2), abs(y1-y2)].
	 * @param p1 the other point
	 * @return the distance.
	 */
	@Pure
	int distanceLinf(Point3D p1);

	/**
	 * Sets the value of this tuple to the sum of tuples t1 and t2.
	 * @param t1 the first tuple
	 * @param t2 the second tuple
	 */
	void add(Point3D t1, Vector3D t2);

	/**
	 * Sets the value of this tuple to the sum of tuples t1 and t2.
	 * @param t1 the first tuple
	 * @param t2 the second tuple
	 */
	void add(Vector3D t1, Point3D t2);

	/**
	 * Sets the value of this tuple to the sum of itself and t1.
	 * @param t1 the other tuple
	 */
	void add(Vector3D t1);

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of tuple t1 plus tuple t2 (this = s*t1 + t2).
	 * @param scale the scalar value
	 * @param t1 the tuple to be multipled
	 * @param t2 the tuple to be added
	 */
	void scaleAdd(int scale, Vector3D t1, Point3D t2);

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of tuple t1 plus tuple t2 (this = s*t1 + t2).
	 * @param scale the scalar value
	 * @param t1 the tuple to be multipled
	 * @param t2 the tuple to be added
	 */
	void scaleAdd(double scale, Vector3D t1, Point3D t2);

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of tuple t1 plus tuple t2 (this = s*t1 + t2).
	 * @param scale the scalar value
	 * @param t1 the tuple to be multipled
	 * @param t2 the tuple to be added
	 */
	void scaleAdd(int scale, Point3D t1, Vector3D t2);

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of tuple t1 plus tuple t2 (this = s*t1 + t2).
	 * @param scale the scalar value
	 * @param t1 the tuple to be multipled
	 * @param t2 the tuple to be added
	 */
	void scaleAdd(double scale, Point3D t1, Vector3D t2);

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of itself and then adds tuple t1 (this = s*this + t1).
	 * @param scale the scalar value
	 * @param t1 the tuple to be added
	 */
	void scaleAdd(int scale, Vector3D t1);

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of itself and then adds tuple t1 (this = s*this + t1).
	 * @param scale the scalar value
	 * @param t1 the tuple to be added
	 */
	void scaleAdd(double scale, Vector3D t1);


	/**
	 * Sets the value of this tuple to the difference
	 * of tuples t1 and t2 (this = t1 - t2).
	 * @param t1 the first tuple
	 * @param t2 the second tuple
	 */
	void sub(Point3D t1, Vector3D t2);

	/**
	 * Sets the value of this tuple to the difference
	 * of itself and t1 (this = this - t1).
	 * @param t1 the other tuple
	 */
	void sub(Vector3D t1);

	/** Replies an unmodifiable copy of this vector.
	 *
	 * @return an unmodifiable copy.
	 */
	@Pure
	Point3D toUnmodifiable();

}
