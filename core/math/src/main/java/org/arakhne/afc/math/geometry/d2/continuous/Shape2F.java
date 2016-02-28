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
package org.arakhne.afc.math.geometry.d2.continuous;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Shape2D;
import org.eclipse.xtext.xbase.lib.Pure;

/** 2D shape with floating-point points.
 * 
 * @author $Author: galland$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Shape2F extends Shape2D<Shape2F> {

	/** Replies the bounds of the shape.
	 * If the current shape is a Rectangle2f, this function
	 * replies the current shape, NOT A CLONE.
	 * 
	 * @return the bounds of the shape.
	 */
	@Pure
	public AbstractRectangle2F<?> toBoundingBox();
	
	
	/** Replies the bounds of the shape.
	 * 
	 * @param rectangle2f is set with the bounds of the shape.
	 */
	public void toBoundingBox(AbstractRectangle2F<?> rectangle2f);
	

	/** Replies the minimal distance from this shape to the given point.
	 * 
	 * @param p
	 * @return the minimal distance between this shape and the point.
	 */
	@Pure
	public double distance(Point2D p);

	/** Replies the squared value of the minimal distance from this shape to the given point.
	 * 
	 * @param p
	 * @return squared value of the minimal distance between this shape and the point.
	 */
	@Pure
	public double distanceSquared(Point2D p);

	/**
	 * Computes the L-1 (Manhattan) distance between this shape and
	 * point p1.  The L-1 distance is equal to abs(x1-x2) + abs(y1-y2).
	 * @param p the point
	 * @return the distance.
	 */
	@Pure
	public double distanceL1(Point2D p);

	/**
	 * Computes the L-infinite distance between this shape and
	 * point p1.  The L-infinite distance is equal to 
	 * MAX[abs(x1-x2), abs(y1-y2)]. 
	 * @param p the point
	 * @return the distance.
	 */
	@Pure
	public double distanceLinf(Point2D p);

	/** Translate the shape.
	 * 
	 * @param dx
	 * @param dy
	 */
	public void translate(double dx, double dy); 
	
	/** Replies if the given point is inside this shape.
	 * 
	 * @param x
	 * @param y
	 * @return <code>true</code> if the given point is inside this
	 * shape, otherwise <code>false</code>.
	 */
	@Pure
	public boolean contains(double x, double y);
	
	/** Replies if the given rectangle is inside this shape.
	 * 
	 * @param r
	 * @return <code>true</code> if the given rectangle is inside this
	 * shape, otherwise <code>false</code>.
	 */
	@Pure
	public boolean contains(AbstractRectangle2F<?> r);

	/** Replies the elements of the paths.
	 * 
	 * @param transform is the transformation to apply to the path.
	 * @return the elements of the path.
	 */
	@Pure
	public PathIterator2f getPathIterator(Transform2D transform);
	
	/** Replies the elements of the paths.
	 * 
	 * @param transform is the transformation to apply to the path.
	 * @return the elements of the path.
	 */
	@Pure
	public PathIterator2d getPathIteratorProperty(Transform2D transform);
	
	
	/** Replies the elements of the paths.
	 * 
	 * @return the elements of the path.
	 */
	@Pure
	public PathIterator2f getPathIterator();
	
	/** Replies the elements of the paths.
	 * 
	 * @return the elements of the path.
	 */
	@Pure
	public PathIterator2d getPathIteratorProperty();

	/** Apply the transformation to the shape and reply the result.
	 * This function does not change the current shape.
	 * 
	 * @param transform is the transformation to apply to the shape.
	 * @return the result of the transformation.
	 */
	@Pure
	public Shape2F createTransformedShape(Transform2D transform);
	
	/** Replies if this shape is intersecting the given rectangle.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	public boolean intersects(AbstractRectangle2F<?> s);

	/** Replies if this shape is intersecting the given ellipse.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	public boolean intersects(AbstractEllipse2F<?> s);

	/** Replies if this shape is intersecting the given circle.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	public boolean intersects(AbstractCircle2F<?> s);

	/** Replies if this shape is intersecting the given line.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	public boolean intersects(AbstractSegment2F<?> s);

	/** Replies if this shape is intersecting the given path.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given path;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	public boolean intersects(Path2f s);
	
	/** Replies if this shape is intersecting the given path.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given path;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	public boolean intersects(Path2d s);

	/** Replies if this shape is intersecting the given path.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given path;
	 * <code>false</code> if there is no intersection.
	 */
	public boolean intersects(PathIterator2f s);
	
	/** Replies if this shape is intersecting the given path.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given path;
	 * <code>false</code> if there is no intersection.
	 */
	public boolean intersects(PathIterator2d s);
	

	/** Replies if this shape is intersecting the given rectangle.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	public boolean intersects(AbstractOrientedRectangle2F<?> s);


}