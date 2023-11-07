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

/** A 3D point with three orientation vectors relative to the polyline: the tangent, the normal and the sway to the point.
 * They have no physical existence.
 *
 * <p>Note: the normal is defined as the orthogonal vector to the tangent's plane,
 * and the sway is the cross product of the tangent and the normal.
 *
 * @param <RP> is the type of point that can be returned by this tuple.
 * @param <RV> is the type of vector that can be returned by this tuple.
 * @param <RQ> is the type of quaternion that can be returned by this tuple.
 * @author $Author: tpiotrow$
 * @author $Author: sgalland$
 * @author $Author: olamotte$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface OrientedPoint3D<RP extends Point3D<? super RP, ? super RV, ? super RQ>, RV extends Vector3D<? super RV, ? super RP, ? super RQ>, RQ extends Quaternion<? super RP, ? super RV, ? super RQ>>
extends Point3D<RP, RV, RQ> {

	/** Replies the X coordinate of the tangent vector.
	 * If this point is not part of a polyline, the tangent vector is null.
	 *
	 * @return the x coordinate of the tangent vector.
	 */
	@Pure
	double getTangentX();

	/** Replies the X coordinate of the tangent vector.
	 * If this point is not part of a polyline, the tangent vector is null.
	 *
	 * @return the x coordinate of the tangent vector.
	 */
	@Pure
	int itx();

	/** Sets a new value in the X tangent of the point.
	 *
	 * @param tanX the new value double x.
	 */
	void setTangentX(int tanX);

	/** Sets a new value in the X tangent of the point.
	 *
	 * @param tanX the new value double x.
	 */
	void setTangentX(double tanX);

	/** Replies the Y coordinate of the tangent vector.
	 * If this point is not part of a polyline, the tangent vector is null.
	 *
	 * @return the y coordinate of the tangent vector.
	 */
	@Pure
	double getTangentY();

	/** Replies the Y coordinate of the tangent vector.
	 * If this point is not part of a polyline, the tangent vector is null.
	 *
	 * @return the y coordinate of the tangent vector.
	 */
	@Pure
	int ity();

	/** Sets a new value in the Y tangent of the point.
	 *
	 * @param tanY the new value double y.
	 */
	void setTangentY(int tanY);

	/**  Sets a new value in the Y tangent of the point.
	 *
	 * @param tanY the new value double y.
	 */
	void setTangentY(double tanY);

	/** Replies the Z coordinate of the tangent vector.
	 * If this point is not part of a polyline, the tangent vector is null.
	 *
	 * @return the z coordinate of the tangent vector.
	 */
	@Pure
	double getTangentZ();

	/** Replies the Z coordinate of the tangent vector.
	 * If this point is not part of a polyline, the tangent vector is null.
	 *
	 * @return the z coordinate of the tangent vector.
	 */
	@Pure
	int itz();

	/** Sets a new value in the Z tangent of the point.
	 *
	 * @param tanZ the new value double z.
	 */
	void setTangentZ(int tanZ);

	/** Sets a new value in the Z tangent of the point.
	 *
	 * @param tanZ the new value double z.
	 */
	void setTangentZ(double tanZ);

	/** Replies the tangent vector
	 *.
	 * @return the tangent vector to this point.
	 */
	@Pure
	RV getTangent();

	/** Sets the given vector as the new tangent to this point.
	 *  The sway vector is automatically recomputed.
	 *
	 * @param tangent the vector to set.
	 */
	default void setTangent(RV tangent) {
		setTangentX(tangent.getX());
		setTangentY(tangent.getY());
		setTangentZ(tangent.getZ());
	}

	/** Sets the given vector as the new tangent to this point.
	 *  The sway vector is automatically recomputed.
	 *
	 * @param x x coordinate of the tangent.
	 * @param y y coordinate of the tangent.
	 * @param z z coordinate of the tangent.
	 * @since 18.0
	 */
	default void setTangent(double x, double y, double z) {
		setTangentX(x);
		setTangentY(y);
		setTangentZ(z);
	}

	/** Replies the X coordinate of the normal vector.
	 *  If this point is not part of a polyline, the normal vector is null.
	 *
	 * @return the x coordinate of the normal vector.
	 */
	@Pure
	double getNormalX();

	/** Replies the X coordinate of the normal vector.
	 *  If this point is not part of a polyline, the normal vector is null.
	 *
	 * @return the x coordinate of the normal vector.
	 */
	@Pure
	int inx();

	/** Sets a new value in the X normal of the point.
	 *
	 * @param norX the new value double x.
	 */
	void setNormalX(int norX);

	/**  Sets a new value in the X normal of the point.
	 *
	 * @param norX the new value double x.
	 */
	void setNormalX(double norX);

	/** Replies the Y coordinate of the normal vector.
	 *  If this point is not part of a polyline, the normal vector is null.
	 *
	 * @return the y coordinate of the normal vector.
	 */
	@Pure
	double getNormalY();

	/** Replies the Y coordinate of the normal vector.
	 *  If this point is not part of a polyline, the normal vector is null.
	 *
	 * @return the y coordinate of the normal vector.
	 */
	@Pure
	int iny();

	/** Sets a new value in the Y normal of the point.
	 *
	 * @param norY the new value double y.
	 */
	void setNormalY(int norY);

	/**  Sets a new value in the Y normal of the point.
	 *
	 * @param norY the new value double y.
	 */
	void setNormalY(double norY);

	/** Replies the Z coordinate of the normal vector.
	 *  If this point is not part of a polyline, the normal vector is null.
	 *
	 * @return the z coordinate of the normal vector.
	 */
	@Pure
	double getNormalZ();

	/** Replies the Z coordinate of the normal vector.
	 *  If this point is not part of a polyline, the normal vector is null.
	 *
	 * @return the z coordinate of the normal vector.
	 */
	@Pure
	int inz();

	/**  Sets a new value in the Z normal of the point.
	 *
	 * @param norZ the new value double z.
	 */
	void setNormalZ(int norZ);

	/**  Sets a new value in the Z normal of the point.
	 *
	 * @param norZ the new value double z.
	 */
	void setNormalZ(double norZ);

	/** Replies the normal vector.
	 * @return the normal vector to the point.
	 */
	@Pure
	RV getNormal();

	/** Sets the given vector as the new normal to this point.
	 *  The sway vector is automatically recomputed.
	 *
	 * @param normal the vector to set.
	 */
	default void setNormal(RV normal) {
		setNormalX(normal.getX());
		setNormalY(normal.getY());
		setNormalZ(normal.getZ());
	}

	/** Sets the given vector as the new normal to this point.
	 *  The sway vector is automatically recomputed.
	 *
	 * @param x x coordinate of the vector to set.
	 * @param y y coordinate of the vector to set.
	 * @param z z coordinate of the vector to set.
	 * @since 18.0
	 */
	default void setNormal(double x, double y, double z) {
		setNormalX(x);
		setNormalY(y);
		setNormalZ(z);
	}

	/** Replies the X coordinate of the sway vector.
	 *  If this point is not part of a polyline, the normal vector is null.
	 *
	 * @return the x coordinate of the sway vector.
	 */
	@Pure
	default double getSwayX() {
		return getTangent().cross(getNormal()).getX();
	}

	/** Replies the X coordinate of the sway vector.
	 *  If this point is not part of a polyline, the normal vector is null.
	 *
	 * @return the x coordinate of the sway vector.
	 */
	@Pure
	default int isx() {
		return getTangent().cross(getNormal()).ix();
	}

	/** Replies the Y coordinate of the sway vector.
	 *  If this point is not part of a polyline, the normal vector is null.
	 *
	 * @return the y coordinate of the sway vector.
	 */
	@Pure
	default double getSwayY() {
		return getTangent().cross(getNormal()).getY();
	}

	/** Replies the Y coordinate of the sway vector.
	 *  If this point is not part of a polyline, the normal vector is null.
	 *
	 * @return the y coordinate of the sway vector.
	 */
	@Pure
	default int isy() {
		return getTangent().cross(getNormal()).iy();
	}

	/** Replies the Z coordinate of the sway vector.
	 *  If this point is not part of a polyline, the normal vector is null.
	 *
	 * @return the z coordinate of the sway vector.
	 */
	@Pure
	default double getSwayZ() {
		return getTangent().cross(getNormal()).getZ();
	}

	/** Replies the Z coordinate of the sway vector.
	 *  If this point is not part of a polyline, the normal vector is null.
	 *
	 * @return the z coordinate of the sway vector.
	 */
	@Pure
	default int isz() {
		return getTangent().cross(getNormal()).iz();
	}

	/** Replies the sway vector.
	 *
	 * @return the sway vector to this point.
	 */
	@Pure
	RV getSway();

	/** Replies this point.
	 * @return this point
	 */
	default RP getPoint() {
		return getGeomFactory().newPoint(getX(), getY(), getZ());
	}

	/** Change the point and its tangent vector.
	 *
	 * @param x x coordinate of the point.
	 * @param y y coordinate of the point.
	 * @param z z coordinate of the point.
	 * @param tanX x coordinate of the tangent vector.
	 * @param tanY y coordinate of the tangent vector.
	 * @param tanZ z coordinate of the tangent vector.
	 */
	default void set(int x, int y, int z, int tanX, int tanY, int tanZ) {
		Point3D.super.set(x, y, z);
		setTangentX(tanX);
		setTangentY(tanY);
		setTangentZ(tanZ);
	}

	/** Change the point and its tangent vector.
	 *
	 * @param x x coordinate of the point.
	 * @param y y coordinate of the point.
	 * @param z z coordinate of the point.
	 * @param tanX x coordinate of the tangent vector.
	 * @param tanY y coordinate of the tangent vector.
	 * @param tanZ z coordinate of the tangent vector.
	 */
	default void set(double x, double y, double z, double tanX, double tanY, double tanZ) {
		Point3D.super.set(x, y, z);
		setTangentX(tanX);
		setTangentY(tanY);
		setTangentZ(tanZ);
	}

	/** Change the point its tangent and normal vectors.
	 *
	 * @param x x coordinate of the point.
	 * @param y y coordinate of the point.
	 * @param z z coordinate of the point.
	 * @param tanX x coordinate of the tangent vector.
	 * @param tanY y coordinate of the tangent vector.
	 * @param tanZ z coordinate of the tangent vector.
	 * @param norX x coordinate of the normal vector.
	 * @param norY y coordinate of the normal vector.
	 * @param norZ z coordinate of the normal vector.
	 */
	default void set(double x, double y, double z, double tanX, double tanY, double tanZ, double norX, double norY, double norZ) {
		Point3D.super.set(x, y, z);
		setTangentX(tanX);
		setTangentY(tanY);
		setTangentZ(tanZ);
		setNormalX(norX);
		setNormalY(norY);
		setNormalZ(norZ);
	}

	/**
	 * Returns true if all of the data members of OrientedPoint3D p1 are
	 * equal to the corresponding data members in this OrientedPoint3D.
	 * @param p1 the vector with which the comparison is made
	 * @return true or false
	 */
	@Pure
	default boolean equals(OrientedPoint3D<?, ?, ?> p1) {
		return Point3D.super.equals(p1) && getTangent().equals(p1.getTangent()) && getNormal().equals(p1.getNormal());
	}

}
