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
package org.arakhne.afc.math.geometry.d2;

import java.io.Serializable;
import java.util.Objects;

import org.eclipse.xtext.xbase.lib.Pure;

/** 2D shape.
 * 
 * @param <ST> is the type of the general implementation.
 * @param <IT> is the type of the implementation of this shape.
 * @param <I> is the type of the iterator used to obtain the elements of the path.
 * @param <P> is the type of the points.
 * @param <B> is the type of the bounding boxes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface Shape2D<
		ST extends Shape2D<?, ?, I, P, B>,
		IT extends Shape2D<?, ?, I, P, B>,
		I extends PathIterator2D<?>,
		P extends Point2D,
		B extends Shape2D<?, ?, I, P, B>>
		extends Cloneable, Serializable {

	/** Replies if this shape is empty.
	 * The semantic associated to the state "empty"
	 * depends on the implemented shape. See the
	 * subclasses for details.
	 * 
	 * @return <code>true</code> if the shape is empty;
	 * <code>false</code> otherwise.
	 */
	@Pure
	boolean isEmpty();

	/** Clone this shape.
	 * 
	 * @return the clone.
	 */
	@Pure
	IT clone();

	/** Replies this shape as the same path iterator as the given one.
	 * <p>
	 * The equality test does not flatten the paths. It means that
	 * is function has is functionnality equivalent to: <pre><code>
	 * PathIterator2D it = this.getPathIterator();
	 * while (it.hasNext() && pathIterator.hasNext()) {
	 *   PathElement2D e1 = it.next();
	 *   PathElement2D e2 = it.next();
	 *   if (!e1.equals(e2)) return false;
	 * }
	 * return !it.hasNext() && !pathIterator.hasNext();
	 * </code></pre>
	 * 
	 * @param pathIterator the path iterator to compare to the one of this shape.
	 * @return <code>true</code> if the path iterator of this shape replies the same
	 * elements as the given path iterator.
	 */
	@Pure
	default boolean equalsToPathIterator(PathIterator2D<?> pathIterator) {
		I localIterator = getPathIterator();
		if (pathIterator == null) {
			return false;
		}
		PathElement2D element1;
		PathElement2D element2;
		while (localIterator.hasNext() && pathIterator.hasNext()) {
			element1 = localIterator.next();
			element2 = pathIterator.next();
			if (!Objects.equals(element1, element2)) {
				return false;
			}
		}
		return !localIterator.hasNext() && !pathIterator.hasNext();
	}

	/** Replies this shape is equal to the given shape.
	 * 
	 * @param shape the shape to compare to.
	 * @return <code>true</code> if this shape is equal is equal to the given path.
	 */
	@Pure
	boolean equalsToShape(IT shape);

	/** Reset this shape to be equivalent to
	 * an just-created instance of this shape type. 
	 */
	void clear();

	/** Replies if the given point is inside this shape.
	 * 
	 * @param point
	 * @return <code>true</code> if the given shape is intersecting this
	 * shape, otherwise <code>false</code>.
	 */
	@Pure
	boolean contains(Point2D point);
	
	/** Replies the point on the shape that is closest to the given point.
	 * 
	 * @param point
	 * @return the closest point on the shape; or the point itself
	 * if it is inside the shape.
	 */
	@Pure
	P getClosestPointTo(Point2D point);
	
	/** Replies the point on the shape that is farthest the given point.
	 * 
	 * @param point
	 * @return the farthest point on the shape.
	 */
	@Pure
	P getFarthestPointTo(Point2D point);

	/** Replies the minimal distance from this shape to the given point.
	 * 
	 * @param point
	 * @return the minimal distance between this shape and the point.
	 */
	@Pure
	default double getDistance(Point2D point) {
		assert (point != null) : "Point must not be null"; //$NON-NLS-1$
		return Math.sqrt(getDistanceSquared(point));
	}

	/** Replies the squared value of the minimal distance from this shape to the given point.
	 * 
	 * @param point
	 * @return squared value of the minimal distance between this shape and the point.
	 */
	@Pure
	double getDistanceSquared(Point2D point);

	/**
	 * Computes the L-1 (Manhattan) distance between this shape and
	 * point p1.  The L-1 distance is equal to abs(x1-x2) + abs(y1-y2).
	 * @param point the point
	 * @return the distance.
	 */
	@Pure
	double getDistanceL1(Point2D point);

	/**
	 * Computes the L-infinite distance between this shape and
	 * point p1.  The L-infinite distance is equal to 
	 * MAX[abs(x1-x2), abs(y1-y2)]. 
	 * @param point the point
	 * @return the distance.
	 */
	@Pure
	double getDistanceLinf(Point2D point);

	/** Set this shape with the attributes of the given shape.
	 * 
	 * @param s
	 */
	void set(IT s);

	/** Replies an iterator on the path elements.
	 * <p>
	 * The iterator for this class is not multi-threaded safe.
	 * 
	 * @return an iterator on the path elements.
	 */
	@Pure
	default I getPathIterator() {
		return getPathIterator(null);
	}

	/** Replies the elements of the paths.
	 * 
	 * @param transform is the transformation to apply to the path.
	 * @return the elements of the path.
	 */
	@Pure
	I getPathIterator(Transform2D transform);

	/** Apply the transformation to the shape and reply the result.
	 * This function does not change the current shape.
	 * 
	 * @param transform is the transformation to apply to the shape.
	 * @return the result of the transformation.
	 */
	@Pure
	ST createTransformedShape(Transform2D transform);

	/** Translate the shape.
	 * 
	 * @param vector
	 */
	void translate(Vector2D vector); 

	/** Replies the bounding box of this shape.
	 * 
	 * @return the bounding box of this shape.
	 */
	@Pure
	B toBoundingBox();
	
	/** Replies the bounds of the shape.
	 * 
	 * @param box is set with the bounds of the shape.
	 */
	void toBoundingBox(B box);

}