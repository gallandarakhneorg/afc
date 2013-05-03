/* 
 * $Id$
 * 
 * Copyright (C) 2010-2013 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
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
package org.arakhne.afc.math.discrete.object2d;

import java.util.Iterator;

import org.arakhne.afc.math.generic.Point2D;
import org.arakhne.afc.math.generic.Shape2D;
import org.arakhne.afc.math.matrix.Transform2D;

/** 2D shape with integer  points.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Shape2i extends Shape2D {

	/** Replies the bounding box of this shape.
	 * 
	 * @return the bounding box of this shape.
	 */
	public abstract Rectangle2i toBoundingBox();
	
	/** {@inheritDoc}
	 */
	@Override
	public Shape2i clone();
	
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

	/** Replies if this shape is intersecting the given rectangle.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	public boolean intersects(Rectangle2i s);

	/** Replies if this shape is intersecting the given circle.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	public boolean intersects(Circle2i s);

	/** Replies if this shape is intersecting the given segment.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	public boolean intersects(Segment2i s);

	/** Replies the elements of the paths.
	 * 
	 * @param transform is the transformation to apply to the path.
	 * @return the elements of the path.
	 */
	public PathIterator2i getPathIterator(Transform2D transform);

	/** Replies the elements of the paths.
	 * 
	 * @return the elements of the path.
	 */
	public PathIterator2i getPathIterator();

	/** Replies an iterator on the points covered by this shape.
	 * <p>
	 * The implementation of the iterator depends on the shape type.
	 * There is no warranty about the order of the points.
	 * 
	 * @return an iterator on the points.
	 */
	public Iterator<Point2i> getPointIterator();
	
	/** Apply the transformation to the shape and reply the result.
	 * This function does not change the current shape.
	 * 
	 * @param transform is the transformation to apply to the shape.
	 * @return the result of the transformation.
	 */
	public Shape2i createTransformedShape(Transform2D transform);
	
	/** Translate the shape.
	 * 
	 * @param dx
	 * @param dy
	 */
	public void translate(int dx, int dy); 

	/** Replies if the given point is inside this shape.
	 * 
	 * @param x
	 * @param y
	 * @return <code>true</code> if the given point is inside this
	 * shape, otherwise <code>false</code>.
	 */
	public boolean contains(int x, int y);
	
	/** Replies if the given rectangle is inside this shape.
	 * 
	 * @param r
	 * @return <code>true</code> if the given rectangle is inside this
	 * shape, otherwise <code>false</code>.
	 */
	public boolean contains(Rectangle2i r);

}