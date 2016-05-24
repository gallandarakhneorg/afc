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

package org.arakhne.afc.math.continous.object2d;

import org.arakhne.afc.math.generic.Point2D;
import org.arakhne.afc.math.generic.Shape2D;
import org.arakhne.afc.math.geometry.d2.d.Shape2d;
import org.arakhne.afc.math.matrix.Transform2D;

/** 2D shape with floating-point points.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see {@link Shape2d}
 */
@Deprecated
@SuppressWarnings("all")
public interface Shape2f extends Shape2D<Shape2f> {

	/** Replies the bounds of the shape.
	 * If the current shape is a Rectangle2f, this function
	 * replies the current shape, NOT A CLONE.
	 * 
	 * @return the bounds of the shape.
	 */
	public Rectangle2f toBoundingBox();
	
	/** Replies the bounds of the shape.
	 * 
	 * @param box is set with the bounds of the shape.
	 */
	public void toBoundingBox(Rectangle2f box);

	/** Replies the minimal distance from this shape to the given point.
	 * 
	 * @param p
	 * @return the minimal distance between this shape and the point.
	 */
	public float distance(Point2D p);

	/** Replies the squared value of the minimal distance from this shape to the given point.
	 * 
	 * @param p
	 * @return squared value of the minimal distance between this shape and the point.
	 */
	public float distanceSquared(Point2D p);

	/**
	 * Computes the L-1 (Manhattan) distance between this shape and
	 * point p1.  The L-1 distance is equal to abs(x1-x2) + abs(y1-y2).
	 * @param p the point
	 * @return the distance.
	 */
	public float distanceL1(Point2D p);

	/**
	 * Computes the L-infinite distance between this shape and
	 * point p1.  The L-infinite distance is equal to 
	 * MAX[abs(x1-x2), abs(y1-y2)]. 
	 * @param p the point
	 * @return the distance.
	 */
	public float distanceLinf(Point2D p);

	/** Translate the shape.
	 * 
	 * @param dx
	 * @param dy
	 */
	public void translate(float dx, float dy); 
	
	/** Replies if the given point is inside this shape.
	 * 
	 * @param x
	 * @param y
	 * @return <code>true</code> if the given point is inside this
	 * shape, otherwise <code>false</code>.
	 */
	public boolean contains(float x, float y);
	
	/** Replies if the given rectangle is inside this shape.
	 * 
	 * @param r
	 * @return <code>true</code> if the given rectangle is inside this
	 * shape, otherwise <code>false</code>.
	 */
	public boolean contains(Rectangle2f r);

	/** Replies the elements of the paths.
	 * 
	 * @param transform is the transformation to apply to the path.
	 * @return the elements of the path.
	 */
	public PathIterator2f getPathIterator(Transform2D transform);

	/** Replies the elements of the paths.
	 * 
	 * @return the elements of the path.
	 */
	public PathIterator2f getPathIterator();

	/** Apply the transformation to the shape and reply the result.
	 * This function does not change the current shape.
	 * 
	 * @param transform is the transformation to apply to the shape.
	 * @return the result of the transformation.
	 */
	public Shape2f createTransformedShape(Transform2D transform);
	
	/** Replies if this shape is intersecting the given rectangle.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	public boolean intersects(Rectangle2f s);

	/** Replies if this shape is intersecting the given ellipse.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	public boolean intersects(Ellipse2f s);

	/** Replies if this shape is intersecting the given circle.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	public boolean intersects(Circle2f s);

	/** Replies if this shape is intersecting the given line.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	public boolean intersects(Segment2f s);

	/** Replies if this shape is intersecting the given path.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given path;
	 * <code>false</code> if there is no intersection.
	 */
	public boolean intersects(Path2f s);

	/** Replies if this shape is intersecting the given path.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given path;
	 * <code>false</code> if there is no intersection.
	 */
	public boolean intersects(PathIterator2f s);

}
