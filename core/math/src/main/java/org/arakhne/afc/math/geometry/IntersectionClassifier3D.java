/* 
 * $Id$
 * 
 * Copyright (C) 2010-2013 Christophe BOHRHAUER.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
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
package org.arakhne.afc.math.geometry;

import org.arakhne.afc.math.geometry3d.continuous.PlanarClassificationType;
import org.arakhne.afc.math.geometry3d.continuous.Plane;
import org.arakhne.afc.math.geometry3d.continuous.Point3f;
import org.arakhne.afc.math.geometry3d.continuous.Vector3f;

/**
 * This interface describes an object that permits to classify intersection
 * between objects in a 3D space.
 * 
 * @param <IC> is the type of the object.
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface IntersectionClassifier3D<IC extends IntersectionClassifier3D<IC>> extends IntersectionClassifier<IC,Point3f> {

	/**
	 * Classify a sphere.
	 * 
	 * @param c is the center point of the sphere.
	 * @param r is the radius of the sphere.
	 * @return the value {@link IntersectionType#INSIDE} if the <var>v</var> is inside this classifier;
	 * {@link IntersectionType#OUTSIDE} if the <var>v</var> is outside this classifier;
	 * {@link IntersectionType#SPANNING} if the <var>v</var> is on this classifier.
	 */
	public IntersectionType classifies(Point3f c, float r);

	/**
	 * Replies if the specified sphere intersects this classifier.
	 * 
	 * @param c is the center point of the circle.
	 * @param r is the radius of the circle.
	 * @return <code>true</code> if an intersection exists, otherwise <code>false</code>
	 */
	public boolean intersects(Point3f c, float r);

	/**
	 * Classify an plane.
	 *
	 * @param plane is the other plane to classify
	 * @return the value {@link IntersectionType#OUTSIDE} if the <var>plane</var> does not intersecting this classifier;
	 * {@link IntersectionType#SPANNING} if the <var>plane</var> intersecting this classifier.
	 */
	public IntersectionType classifies(Plane plane);

	/**
	 * Classify this bounds according to the specified plane.
	 * 
	 * @param plane is the other plane to classify on
	 * @return the value {@link PlanarClassificationType#IN_FRONT_OF} if this classifier does not intersecting
	 * the plane and is located in the front side of the plane;
	 * {@link PlanarClassificationType#BEHIND} if this classifier does not intersecting
	 * the plane and is located in the behind side of the plane;
	 * {@link PlanarClassificationType#COINCIDENT} if this classifier intersecting the plane.
	 */
	public PlanarClassificationType classifiesAgainst(Plane plane);

	/**
	 * Replies if the specified plane intersects this classifier.
	 * 
	 * @param plane is the other plane to test
	 * @return <code>true</code> if an intersection exists, otherwise <code>false</code>
	 */
	public boolean intersects(Plane plane);

	/**
	 * Classify an oriented bounding box.
	 * 
	 * @param center is the center point of the oriented bounding box.
	 * @param axis are the unit vectors for the three axis of the orientied bounding box.
	 * @param extent are the sizes of the oriented bounding box along all the three box's axis.
	 * @return the value {@link IntersectionType#INSIDE} if the <var>v</var> is inside this classifier;
	 * {@link IntersectionType#OUTSIDE} if the <var>v</var> is outside this classifier;
	 * {@link IntersectionType#SPANNING} if the <var>v</var> is on this classifier.
	 */
	public IntersectionType classifies(Point3f center, Vector3f[] axis, float[] extent);

	/**
	 * Replies if the specified oriented bounding box intersects this classifier.
	 * 
	 * @param center is the center point of the oriented bounding box.
	 * @param axis are the unit vectors for the three axis of the orientied bounding box.
	 * @param extent are the sizes of the oriented bounding box along all the three box's axis.
	 * @return <code>true</code> if an intersection exists, otherwise <code>false</code>
	 */
	public boolean intersects(Point3f center, Vector3f[] axis, float[] extent);

}
