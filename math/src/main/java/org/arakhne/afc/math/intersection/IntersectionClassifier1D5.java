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
package org.arakhne.afc.math.intersection;

import org.arakhne.afc.math.object.Point1D;
import org.arakhne.afc.math.object.Point1D5;

/**
 * This interface describes an object that permits to classify intersection
 * between objects in a 1.5D space.
 * 
 * @param <IC> is the type of the object.
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface IntersectionClassifier1D5<IC extends IntersectionClassifier1D5<IC>> extends IntersectionClassifier<IC,Point1D5> {

	/**
	 * Classify a point.
	 * This function does not take into account the jutting values. 
	 * 
	 * @param p is a point
	 * @return the value {@link IntersectionType#INSIDE} if the point is inside this classifier;
	 * {@link IntersectionType#OUTSIDE} if the point is outside this classifier;
	 * {@link IntersectionType#SPANNING} if the point is on the classifier.
	 */
	public IntersectionType classifies(Point1D p);

	/**
	 * Replies if the specified point intersects this classifier.
	 * This function does not take into account the jutting values. 
	 * 
	 * @param p is a point
	 * @return <code>true</code> if an intersection exists, otherwise <code>false</code>
	 */
	public boolean intersects(Point1D p);

	/**
	 * Classify a segment. This function assumes that the two points
	 * are lying on the same segment.
	 * This function does not take into account the jutting values. 
	 *
	 * @param l is the lower point of the segment
	 * @param u is the upper point of the segment
	 * @return the value {@link IntersectionType#INSIDE} if the segment is inside this classifier;
	 * {@link IntersectionType#OUTSIDE} if the segment is outside this classifier;
	 * {@link IntersectionType#ENCLOSING} if the segment is enclosing this classifier;
	 * {@link IntersectionType#SPANNING} otherwhise.
	 * @throws IllegalArgumentException if the two points are not on the same segment.
	 */
	public IntersectionType classifies(Point1D l, Point1D u);

	/**
	 * Replies if the specified segment intersects this classifier.
	 * This function assumes that the two points
	 * are lying on the same segment. 
	 * This function does not take into account the jutting values. 
	 *
	 * @param l is the lower point of the segment
	 * @param u is the upper point of the segment
	 * @return the value {@link IntersectionType#INSIDE} if the segment is inside this classifier;
	 * {@link IntersectionType#OUTSIDE} if the segment is outside this classifier;
	 * {@link IntersectionType#ENCLOSING} if the segment is enclosing this classifier;
	 * {@link IntersectionType#SPANNING} otherwhise.
	 * @throws IllegalArgumentException if the two points are not on the same segment.
	 */
	public boolean intersects(Point1D l, Point1D u);

}
