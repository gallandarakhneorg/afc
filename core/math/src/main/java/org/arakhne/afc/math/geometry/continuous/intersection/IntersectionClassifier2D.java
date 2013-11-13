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
package org.arakhne.afc.math.geometry.continuous.intersection;

import org.arakhne.afc.math.geometry.continuous.object2d.Point2f;
import org.arakhne.afc.math.geometry.continuous.object2d.Vector2f;

/**
 * This interface describes an object that permits to classify intersection
 * between objects in a 2D space.
 * 
 * @param <IC> is the type of the object.
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface IntersectionClassifier2D<IC extends IntersectionClassifier2D<IC>> extends IntersectionClassifier<IC,Point2f> {

	/**
	 * Classify a circle.
	 * 
	 * @param c is the center point of the circle.
	 * @param r is the radius of the circle.
	 * @return the value {@link IntersectionType#INSIDE} if the <var>v</var> is inside this classifier;
	 * {@link IntersectionType#OUTSIDE} if the <var>v</var> is outside this classifier;
	 * {@link IntersectionType#SPANNING} if the <var>v</var> is on this classifier.
	 */
	public IntersectionType classifies(Point2f c, float r);

	/**
	 * Replies if the specified axis-aligned box intersects this classifier.
	 * 
	 * @param c is the center point of the circle.
	 * @param r is the radius of the circle.
	 * @return <code>true</code> if an intersection exists, otherwise <code>false</code>
	 */
	public boolean intersects(Point2f c, float r);

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
	public IntersectionType classifies(Point2f center, Vector2f[] axis, float[] extent);

	/**
	 * Replies if the specified oriented bounding box intersects this classifier.
	 * 
	 * @param center is the center point of the oriented bounding box.
	 * @param axis are the unit vectors for the three axis of the orientied bounding box.
	 * @param extent are the sizes of the oriented bounding box along all the three box's axis.
	 * @return <code>true</code> if an intersection exists, otherwise <code>false</code>
	 */
	public boolean intersects(Point2f center, Vector2f[] axis, float[] extent);

}
