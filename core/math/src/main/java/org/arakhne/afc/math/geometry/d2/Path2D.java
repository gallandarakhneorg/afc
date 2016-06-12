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

package org.arakhne.afc.math.geometry.d2;

import java.util.Collection;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;

/** 2D Path.
 *
 * @param <ST> is the type of the general implementation.
 * @param <IT> is the type of the implementation of this shape.
 * @param <I> is the type of the iterator used to obtain the elements of the path.
 * @param <P> is the type of the points.
 * @param <V> is the type of the vectors.
 * @param <B> is the type of the bounding boxes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface Path2D<
		ST extends Shape2D<?, ?, I, P, V, B>,
		IT extends Path2D<?, ?, I, P, V, B>,
		I extends PathIterator2D<?>,
		P extends Point2D<? super P, ? super V>,
		V extends Vector2D<? super V, ? super P>,
		B extends Shape2D<?, ?, I, P, V, B>>
		extends Shape2D<ST, IT, I, P, V, B> {

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
	 *     contain curve primitives, <code>false</code>
	 *     otherwise.
	 */
	@Pure
	boolean isPolyline();

	/** Replies the path contains a curve..
	 *
	 * @return <code>true</code> if the path does not
	 *     contain curve primitives, <code>false</code>
	 *     otherwise.
	 */
	@Pure
	boolean isCurved();

	/** Replies the path has multiple parts, i.e. multiple <code>MOVE_TO</code> are inside.
	 * primitives.
	 *
	 * @return <code>true</code> if the path has multiple move-to primitive, <code>false</code>
	 *     otherwise.
	 */
	@Pure
	boolean isMultiParts();

	/** Replies the path is composed only by
	 * one <code>MOVE_TO</code>, a sequence of <code>LINE_TO</code>
	 * or <code>QUAD_TO</code> or <code>CURVE_TO</code>,
	 * or <code>ARC_TO</code>, and a
	 * single <code>CLOSE</code> primitives.
	 *
	 * @return <code>true</code> if the path does not
	 *     contain curve primitives, <code>false</code>
	 *     otherwise.
	 */
	@Pure
	boolean isPolygon();

	/** Replies an iterator on the path elements.
	 *
	 * <p>Only {@link PathElementType#MOVE_TO},
	 * {@link PathElementType#LINE_TO}, and
	 * {@link PathElementType#CLOSE} types are returned by the iterator.
	 *
	 * <p>The amount of subdivision of the curved segments is controlled by the
	 * flatness parameter, which specifies the maximum distance that any point
	 * on the unflattened transformed curve can deviate from the returned
	 * flattened path segments. Note that a limit on the accuracy of the
	 * flattened path might be silently imposed, causing very small flattening
	 * parameters to be treated as larger values. This limit, if there is one,
	 * is defined by the particular implementation that is used.
	 *
	 * <p>The iterator for this class is not multi-threaded safe.
	 *
	 * @param flatness is the maximum distance that the line segments used to approximate
	 *     the curved segments are allowed to deviate from any point on the original curve.
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
	void moveTo(Point2D<?, ?> position);

	/**
	 * Adds a point to the path by drawing a straight line from the
	 * current coordinates to the new specified coordinates
	 * specified in double precision.
	 *
	 * @param to the end point
	 */
	void lineTo(Point2D<?, ?> to);

	/**
	 * Adds a curved segment, defined by two new points, to the path by
	 * drawing a Quadratic curve that intersects both the current
	 * coordinates and the specified coordinates {@code (x2, y2)},
	 * using the specified point {@code (x1, y1)} as a quadratic
	 * parametric control point.
	 * All coordinates are specified in double precision.
	 *
	 * @param ctrl the quadratic control point
	 * @param to the final end point
	 */
	void quadTo(Point2D<?, ?> ctrl, Point2D<?, ?> to);

	/**
	 * Adds a curved segment, defined by three new points, to the path by
	 * drawing a B&eacute;zier curve that intersects both the current
	 * coordinates and the specified coordinates {@code (x3, y3)},
	 * using the specified points {@code (x1, y1)} and {@code (x2, y2)} as
	 * B&eacute;zier control points.
	 * All coordinates are specified in double precision.
	 *
	 * @param ctrl1 the first B&eacute;zier control point
	 * @param ctrl2 the second B&eacute;zier control point
	 * @param to the final end point
	 */
	void curveTo(Point2D<?, ?> ctrl1, Point2D<?, ?> ctrl2, Point2D<?, ?> to);

	/**
	 * Adds a section of an shallow ellipse to the current path.
	 * The ellipse from which a quadrant is taken is the ellipse that would be
	 * inscribed in a parallelogram defined by 3 points,
	 * The current point which is considered to be the midpoint of the edge
	 * leading into the corner of the ellipse where the ellipse grazes it,
	 * {@code (ctrlx, ctrly)} which is considered to be the location of the
	 * corner of the parallelogram in which the ellipse is inscribed,
	 * and {@code (tox, toy)} which is considered to be the midpoint of the
	 * edge leading away from the corner of the oval where the oval grazes it.
	 *
	 * <p><img alt="" src="./doc-files/arcto0.png">
	 *
	 * <p>Only the portion of the ellipse from {@code tfrom} to {@code tto}
	 * will be included where {@code 0f} represents the point where the
	 * ellipse grazes the leading edge, {@code 1f} represents the point where
	 * the oval grazes the trailing edge, and {@code 0.5f} represents the
	 * point on the oval closest to the control point.
	 * The two values must satisfy the relation
	 * {@code (0 <= tfrom <= tto <= 1)}.
	 *
	 * <p>If {@code tfrom} is not {@code 0f} then the caller would most likely
	 * want to use one of the arc {@code type} values that inserts a segment
	 * leading to the initial point.
	 * An initial {@link #moveTo(Point2D)} or {@link #lineTo(Point2D)} can be added to direct
	 * the path to the starting point of the ellipse section if
	 * {@link ArcType#MOVE_THEN_ARC} or
	 * {@link ArcType#LINE_THEN_ARC} are
	 * specified by the type argument.
	 * The {@code lineTo} path segment will only be added if the current point
	 * is not already at the indicated location to avoid spurious empty line
	 * segments.
	 * The type can be specified as
	 * {@link ArcType#ARC_ONLY} if the current point
	 * on the path is known to be at the starting point of the ellipse section.
	 *
	 * @param ctrl the control point, i.e. the corner of the parallelogram in which the ellipse is inscribed.
	 * @param to the target point.
	 * @param tfrom the fraction of the ellipse section where the curve should start.
	 * @param tto the fraction of the ellipse section where the curve should end
	 * @param type the specification of what additional path segments should
	 *               be appended to lead the current path to the starting point.
	 */
	void arcTo(Point2D<?, ?> ctrl, Point2D<?, ?> to, double tfrom, double tto, ArcType type);

	/**
	 * Adds a section of an shallow ellipse to the current path.
	 *
	 * <p>This function is equivalent to:<pre><code>
	 * this.arcTo(ctrl, to, 0.0, 1.0, ArcType.ARCONLY);
	 * </code></pre>
	 *
	 * @param ctrl the control point, i.e. the corner of the parallelogram in which the ellipse is inscribed.
	 * @param to the target point.
	 */
	default void arcTo(Point2D<?, ?> ctrl, Point2D<?, ?> to) {
		arcTo(ctrl, to, 0., 1., ArcType.ARC_ONLY);
	}

	/**
	 * Adds a section of an shallow ellipse to the current path.
	 * The ellipse from which the portions are extracted follows the rules:
	 * <ul>
	 * <li>The ellipse will have its X axis tilted from horizontal by the
	 * angle {@code xAxisRotation} specified in radians.</li>
	 * <li>The ellipse will have the X and Y radii (viewed from its tilted
	 * coordinate system) specified by {@code radiusx} and {@code radiusy}
	 * unless that ellipse is too small to bridge the gap from the current
	 * point to the specified destination point in which case a larger
	 * ellipse with the same ratio of dimensions will be substituted instead.</li>
	 * <li>The ellipse may slide perpendicular to the direction from the
	 * current point to the specified destination point so that it just
	 * touches the two points.
	 * The direction it slides (to the "left" or to the "right") will be
	 * chosen to meet the criteria specified by the two boolean flags as
	 * described below.
	 * Only one direction will allow the method to meet both criteria.</li>
	 * <li>If the {@code largeArcFlag} is true, then the ellipse will sweep
	 * the longer way around the ellipse that meets these criteria.</li>
	 * <li>If the {@code sweepFlag} is true, then the ellipse will sweep
	 * clockwise around the ellipse that meets these criteria.</li>
	 * </ul>
	 *
	 * <p><img alt="" src="../doc-files/arcto1.png">
	 *
	 * <p>The method will do nothing if the destination point is the same as
	 * the current point.
	 * The method will draw a simple line segment to the destination point
	 * if either of the two radii are zero.
	 *
	 * @param to the target point.
	 * @param radii the X and Y radii of the tilted ellipse.
	 * @param xAxisRotation the angle of tilt of the ellipse.
	 * @param largeArcFlag <code>true</code> iff the path will sweep the long way around the ellipse.
	 * @param sweepFlag <code>true</code> iff the path will sweep clockwise around the ellipse.
	 * @see "http://www.w3.org/TR/SVG/paths.html#PathDataEllipticalArcCommands"
	 */
	void arcTo(Point2D<?, ?> to, Vector2D<?, ?> radii, double xAxisRotation, boolean largeArcFlag, boolean sweepFlag);

	/**
	 * Closes the current subpath by drawing a straight line back to
	 * the coordinates of the last {@code moveTo}.  If the path is already
	 * closed or if the previous coordinates are for a {@code moveTo}
	 * then this method has no effect.
	 */
	void closePath();

	/** Replies the bounding box of all the points added in this path.
	 *
	 * <p>The replied bounding box includes the (invisible) control points.
	 *
	 * @return the bounding box with the control points.
	 * @see #toBoundingBox()
	 */
	B toBoundingBoxWithCtrlPoints();

	/** Compute the bounding box of all the points added in this path.
	 *
	 * <p>The replied bounding box includes the (invisible) control points.
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
	default P[] toPointArray() {
		return toPointArray(null);
	}

	/** Replies the points of this path in an array.
	 *
	 * @param transform is the transformation to apply to all the points.
	 * @return the points.
	 */
	@Pure
	P[] toPointArray(Transform2D transform);

	/** Replies the collection that is contains all the points of the path.
	 *
	 * @return the point collection.
	 */
	@Pure
	Collection<P> toCollection();

	/** Replies the point at the given index.
	 * The index is in [0;{@link #size()}).
	 *
	 * @param index the index.
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
	 * @param point the point.
	 */
	void setLastPoint(Point2D<?, ?> point);

	/** Replies if the given points exists in the coordinates of this path.
	 *
	 * @param point the point.
	 * @return <code>true</code> if the point is a control point of the path.
	 */
	@Pure
	boolean containsControlPoint(Point2D<?, ?> point);

	/** Type of drawing to used when drawing an arc.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	enum ArcType {
		/** Draw only the ellipse arc.
		 */
		ARC_ONLY,

		/** Move to and draw the ellipse arc.
		 */
		MOVE_THEN_ARC,

		/** Draw a line to and the ellipse arc.
		 */
		LINE_THEN_ARC
	}

}
