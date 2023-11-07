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

import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * This interface describes an object that permits to classify intersection
 * between objects and planes.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 18.0
 */
public interface PlaneClassifier {

	/**
	 * Classifies a plane with respect to the plane.
	 *
	 * @param otherPlane is the plane to classify
	 * @return the classification
	 */
	@Pure
	PlaneClassification classifies(Plane3D<?, ?, ?, ?, ?> otherPlane);

	/**
	 * Classifies a point with respect to the plane.
	 * 
	 * @param point the point to classify.
	 * @return the classification
	 */
	@Pure
	default PlaneClassification classifies(Point3D<?, ?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		return classifies(point.getX(), point.getY(), point.getZ());
	}

	/**
	 * Classifies a point with respect to the plane.
	 * 
	 * @param x x component of the point.
	 * @param y y component of the point.
	 * @param z z component of the point.
	 * @return the classification.
	 */
	@Pure
	PlaneClassification classifies(double x, double y, double z);

	/**
	 * Classifies a box with respect to the plane.
	 * 
	 * @param lx x component of the minimum point of the box.
	 * @param ly y component of the minimum point of the box.
	 * @param lz z component of the minimum point of the box.
	 * @param ux x component of the maximum point of the box.
	 * @param uy y component of the maximum point of the box.
	 * @param uz z component of the maximum point of the box.
	 * @return the classification.
	 */
	@Pure
	PlaneClassification classifies(double lx, double ly, double lz, double ux, double uy, double uz);

	/**
	 * Classifies a sphere with respect to the plane.
	 * 
	 * @param x x component of the sphere center.
	 * @param y y component of the sphere center.
	 * @param z z component of the sphere center.
	 * @param radius the radius of the sphere.
	 * @return the classification
	 */
	@Pure
	PlaneClassification classifies(double x, double y, double z, double radius);

	/**
	 * Replies if the given plane is intersecting the plane.
	 *
	 * @param otherPlane is the plane to classify
	 * @return {@code true} if intersection, otherwise {@code false}
	 */
	@Pure
	default boolean intersects(Plane3D<?, ?, ?, ?, ?> otherPlane) {
		return classifies(otherPlane) == PlaneClassification.COINCIDENT;
	}

	/**
	 * Replies if the given point is intersecting the plane.
	 * 
	 * @param point the point to test.
	 * @return {@code true} if intersection, otherwise {@code false}
	 */
	@Pure
	default boolean intersects(Point3D<?, ?, ?> point) {
		return classifies(point) == PlaneClassification.COINCIDENT;
	}

	/**
	 * Replies if the given point is intersecting the plane.
	 * 
	 * @param x x component of the point.
	 * @param y y component of the point.
	 * @param z z component of the point.
	 * @return {@code true} if intersection, otherwise {@code false}
	 */
	@Pure
	default boolean intersects(double x, double y, double z) {
		return classifies(x, y, z) == PlaneClassification.COINCIDENT;
	}

	/**
	 * Replies if the given box is intersecting the plane.
	 * 
	 * @param lx x component of the minimum point of the box.
	 * @param ly y component of the minimum point of the box.
	 * @param lz z component of the minimum point of the box.
	 * @param ux x component of the maximum point of the box.
	 * @param uy y component of the maximum point of the box.
	 * @param uz z component of the maximum point of the box.
	 * @return {@code true} if intersection, otherwise {@code false}
	 */
	@Pure
	default boolean intersects(double lx, double ly, double lz, double ux, double uy, double uz) {
		return classifies(lx, ly, lz, ux, uy, uz) == PlaneClassification.COINCIDENT;
	}

	/**
	 * Replies if the given sphere is intersecting the plane.
	 * 
	 * @param x x component of the sphere center.
	 * @param y y component of the sphere center.
	 * @param z z component of the sphere center.
	 * @param radius the radius of the sphere.
	 * @return {@code true} if intersection, otherwise {@code false}
	 */
	@Pure
	default boolean intersects(double x, double y, double z, double radius) {
		return classifies(x, y, z, radius) == PlaneClassification.COINCIDENT;
	}

}
