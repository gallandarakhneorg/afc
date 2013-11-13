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

/**
 * This interface describes an object that permits to classify intersection
 * between objects.
 * 
 * @param <IC> is the type of the object.
 * @param <P> is the type of the points.
 * @author $Author: cbohrhauer$d
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface IntersectionClassifier<IC extends IntersectionClassifier<IC,P>, P> {

	/**
	 * Classify a box with respect to the classifier.
	 * 
	 * @param box
	 * @return the value {@link IntersectionType#INSIDE} if the <var>box</var> is inside this classifier;
	 * {@link IntersectionType#OUTSIDE} if the <var>box</var> is outside this classifier;
	 * {@link IntersectionType#ENCLOSING} if the <var>box</var> is enclosing this classifier;
	 * {@link IntersectionType#SPANNING} otherwhise.
	 */
	public IntersectionType classifies(IC box);

	/**
	 * Replies if the specified box intersects this classifier.
	 * 
	 * @param box
	 * @return <code>true</code> if the box is intersecting this classifier,
	 * otherwise <code>false</code>
	 */
	public boolean intersects(IC box);

	/**
	 * Classify a point.
	 * 
	 * @param p is a point
	 * @return the value {@link IntersectionType#INSIDE} if the point is inside this classifier;
	 * {@link IntersectionType#OUTSIDE} if the point is outside this classifier;
	 * {@link IntersectionType#SPANNING} if the point is on the classifier.
	 */
	public IntersectionType classifies(P p);

	/**
	 * Replies if the specified point intersects this classifier.
	 * 
	 * @param p is a point
	 * @return <code>true</code> if an intersection exists, otherwise <code>false</code>
	 */
	public boolean intersects(P p);

	/**
	 * Classify an area/box. 
	 *
	 * @param l is the lower point of the box
	 * @param u is the upper point of the box
	 * @return the value {@link IntersectionType#INSIDE} if the box is inside this classifier;
	 * {@link IntersectionType#OUTSIDE} if the box is outside this classifier;
	 * {@link IntersectionType#ENCLOSING} if the box is enclosing this classifier;
	 * {@link IntersectionType#SPANNING} otherwhise.
	 */
	public IntersectionType classifies(P l, P u);

	/**
	 * Replies if the specified area intersects this classifier.
	 *
	 * @param l is the lower point of the box
	 * @param u is the upper point of the box
	 * @return the value {@link IntersectionType#INSIDE} if the box is inside this classifier;
	 * {@link IntersectionType#OUTSIDE} if the box is outside this classifier;
	 * {@link IntersectionType#ENCLOSING} if the box is enclosing this classifier;
	 * {@link IntersectionType#SPANNING} otherwhise.
	 */
	public boolean intersects(P l, P u);

}
