/* 
 * $Id$
 * 
 * Copyright (C) 2013 Stephane GALLAND.
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

import java.util.Collection;

import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.eclipse.xtext.xbase.lib.Pure;

/** 2D Path.
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
public interface Path2D<
		ST extends Shape2D<?, ?, I, P, B>,
		IT extends Path2D<?, ?, I, P, B>,
		I extends PathIterator2D<?>,
		P extends Point2D,
		B extends Shape2D<?, ?, I, P, B>>
		extends Shape2D<ST, IT, I, P, B> {

	/** Replies the winding rule for the path.
	 * 
	 * @return the winding rule for the path.
	 */
	@Pure
	PathWindingRule getWindingRule();
	
	/** Set the winding rule for the path.
	 * 
	 * @param rule is the winding rule for the path.
	 */
	void setWindingRule(PathWindingRule rule);

	/** Replies the path is composed only by
	 * one <code>MOVE_TO</code>, and a sequence of <code>LINE_TO</code>
	 * primitives.
	 * 
	 * @return <code>true</code> if the path does not
	 * contain curve primitives, <code>false</code>
	 * otherwise.
	 */
	@Pure
	boolean isPolyline();

	/** Replies the path contains a curve..
	 * 
	 * @return <code>true</code> if the path does not
	 * contain curve primitives, <code>false</code>
	 * otherwise.
	 */
	@Pure
	boolean isCurved();

	/** Replies the path has multiple parts, i.e. multiple <code>MOVE_TO</code> are inside.
	 * primitives.
	 * 
	 * @return <code>true</code> if the path has multiple move-to primitive, <code>false</code>
	 * otherwise.
	 */
	@Pure
	boolean isMultiParts();

	/** Replies the path is composed only by
	 * one <code>MOVE_TO</code>, a sequence of <code>LINE_TO</code>
	 * or <code>QUAD_TO</code> or <code>CURVE_TO</code>, and a
	 * single <code>CLOSE</code> primitives.
	 * 
	 * @return <code>true</code> if the path does not
	 * contain curve primitives, <code>false</code>
	 * otherwise.
	 */
	@Pure
	boolean isPolygon();

	/** Replies an iterator on the path elements.
	 * <p>
	 * Only {@link PathElementType#MOVE_TO},
	 * {@link PathElementType#LINE_TO}, and 
	 * {@link PathElementType#CLOSE} types are returned by the iterator.
	 * <p>
	 * The amount of subdivision of the curved segments is controlled by the 
	 * flatness parameter, which specifies the maximum distance that any point 
	 * on the unflattened transformed curve can deviate from the returned
	 * flattened path segments. Note that a limit on the accuracy of the
	 * flattened path might be silently imposed, causing very small flattening
	 * parameters to be treated as larger values. This limit, if there is one,
	 * is defined by the particular implementation that is used.
	 * <p>
	 * The iterator for this class is not multi-threaded safe.
	 * 
	 * @param flatness is the maximum distance that the line segments used to approximate
	 * the curved segments are allowed to deviate from any point on the original curve.
	 * @return an iterator on the path elements.
	 */
	@Pure
	I getPathIterator(double flatness);	

	/**
	 * Adds a point to the path by moving to the specified
	 * coordinates specified in double precision.
	 *
	 * @param position the new position.
	 */
	void moveTo(Point2D position);

	/**
	 * Adds a point to the path by drawing a straight line from the
	 * current coordinates to the new specified coordinates
	 * specified in double precision.
	 *
	 * @param to the end point
	 */
	void lineTo(Point2D to);

	/**
	 * Adds a curved segment, defined by two new points, to the path by
	 * drawing a Quadratic curve that intersects both the current
	 * coordinates and the specified coordinates {@code (x2,y2)},
	 * using the specified point {@code (x1,y1)} as a quadratic
	 * parametric control point.
	 * All coordinates are specified in double precision.
	 *
	 * @param ctrl the quadratic control point
	 * @param to the final end point
	 */
	void quadTo(Point2D ctrl, Point2D to);

	/**
	 * Adds a curved segment, defined by three new points, to the path by
	 * drawing a B&eacute;zier curve that intersects both the current
	 * coordinates and the specified coordinates {@code (x3,y3)},
	 * using the specified points {@code (x1,y1)} and {@code (x2,y2)} as
	 * B&eacute;zier control points.
	 * All coordinates are specified in double precision.
	 *
	 * @param ctrl1 the first B&eacute;zier control point
	 * @param ctrl2 the second B&eacute;zier control point
	 * @param to the final end point
	 */
	void curveTo(Point2D ctrl1, Point2D ctrl2, Point2D to);

	/**
	 * Closes the current subpath by drawing a straight line back to
	 * the coordinates of the last {@code moveTo}.  If the path is already
	 * closed or if the previous coordinates are for a {@code moveTo}
	 * then this method has no effect.
	 */
	void closePath();

	/** Replies the bounding box of all the points added in this path.
	 * <p>
	 * The replied bounding box includes the (invisible) control points.
	 * 
	 * @return the bounding box with the control points.
	 * @see #toBoundingBox()
	 */
	B toBoundingBoxWithCtrlPoints();

	/** Compute the bounding box of all the points added in this path.
	 * <p>
	 * The replied bounding box includes the (invisible) control points.
	 * 
	 * @param box is the rectangle to set with the bounds.
	 * @see #toBoundingBox()
	 */
	void toBoundingBoxWithCtrlPoints(B box);

	/** Replies the coordinates of this path in an array of
	 * integer numbers.
	 * 
	 * @return the coordinates.
	 */
	@Pure
	default int[] toIntArray() {
		return toIntArray(null);
	}

	/** Replies the coordinates of this path in an array of
	 * integer numbers.
	 * 
	 * @param transform is the transformation to apply to all the coordinates.
	 * @return the coordinates.
	 */
	@Pure
	int[] toIntArray(Transform2D transform);

	/** Replies the coordinates of this path in an array of
	 * single precision floating-point numbers.
	 * 
	 * @return the coordinates.
	 */
	@Pure
	default float[] toFloatArray() {
		return toFloatArray(null);
	}

	/** Replies the coordinates of this path in an array of
	 * single precision floating-point numbers.
	 * 
	 * @param transform is the transformation to apply to all the coordinates.
	 * @return the coordinates.
	 */
	@Pure
	float[] toFloatArray(Transform2D transform);

	/** Replies the coordinates of this path in an array of
	 * double precision floating-point numbers.
	 * 
	 * @return the coordinates.
	 */
	@Pure
	default double[] toDoubleArray() {
		return toDoubleArray(null);
	}

	/** Replies the coordinates of this path in an array of
	 * double precision floating-point numbers.
	 * 
	 * @param transform is the transformation to apply to all the coordinates.
	 * @return the coordinates.
	 */
	@Pure
	double[] toDoubleArray(Transform2D transform);

	/** Replies the points of this path in an array.
	 * 
	 * @return the points.
	 */
	@Pure
	default Point2D[] toPointArray() {
		return toPointArray(null);
	}

	/** Replies the points of this path in an array.
	 * 
	 * @param transform is the transformation to apply to all the points.
	 * @return the points.
	 */
	@Pure
	Point2D[] toPointArray(Transform2D transform);

	/** Replies the collection that is contains all the points of the path.
	 * 
	 * @return the point collection.
	 */
	@Pure
	Collection<P> toCollection();

	/** Replies the point at the given index.
	 * The index is in [0;{@link #size()}).
	 *
	 * @param index
	 * @return the point at the given index.
	 */
	@Pure
	P getPointAt(int index);

	/** Replies the last point in the path.
	 *
	 * @return the last point.
	 */
	@Pure
	P getCurrentPoint();

	/** Replies the number of points in the path.
	 *
	 * @return the number of points in the path.
	 */
	@Pure
	int size();

	/** Replies the number of path elements in this path.
	 *
	 * @return the number of path elements.
	 * @see #getPathElementTypeAt(int)
	 */
	@Pure
	int getPathElementCount();

	/** Replies the type of the path element at the given position in this path.
	 *
	 * @param index the index of the path element.
	 * @return the type of the path element.
	 * @see #getPathElementCount()
	 */
	@Pure
	PathElementType getPathElementTypeAt(int index);

	/** Replies the total length of the path.
	 *
	 * @return the length of the path.
	 */
	@Pure
	default double getLength() {
		return Math.sqrt(getLengthSquared());
	}

	/** Replies the total squared length of the path.
	 *
	 * @return the length of the path.
	 */
	@Pure
	double getLengthSquared();

	/** Remove the last action.
	 */
	void removeLast();

	/** Change the coordinates of the last inserted point.
	 * 
	 * @param point
	 */
	void setLastPoint(Point2D point);

	/** Replies if the given points exists in the coordinates of this path.
	 * 
	 * @param point
	 * @return <code>true</code> if the point is a control point of the path.
	 */
	@Pure
	boolean containsControlPoint(Point2D point);

}