/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

package org.arakhne.afc.math.geometry.d2.afp;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.Unefficient;
import org.arakhne.afc.math.geometry.CrossingComputationType;
import org.arakhne.afc.math.geometry.GeomConstants;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Shape2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.ai.Rectangle2ai;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** 2D shape with 2D floating coordinates.
 *
 * @param <ST> is the type of the general implementation.
 * @param <IT> is the type of the implementation of this shape.
 * @param <IE> is the type of the path elements.
 * @param <P> is the type of the points.
 * @param <V> is the type of the vectors.
 * @param <B> is the type of the bounding boxes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface Shape2afp<
		ST extends Shape2afp<?, ?, IE, P, V, B>,
		IT extends Shape2afp<?, ?, IE, P, V, B>,
		IE extends PathElement2afp,
		P extends Point2D<? super P, ? super V>,
		V extends Vector2D<? super V, ? super P>,
		B extends Rectangle2afp<?, ?, IE, P, V, B>>
		extends Shape2D<ST, IT, PathIterator2afp<IE>, P, V, B> {

	@Pure
	@Override
	default boolean contains(Point2D<?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		return contains(pt.getX(), pt.getY());
	}

	@Pure
	@Unefficient
	@Override
	default boolean contains(Shape2D<?, ?, ?, ?, ?, ?> shape) {
		assert shape != null : AssertMessages.notNullParameter();
		if (isEmpty()) {
			return false;
		}
		if (shape instanceof Rectangle2afp) {
			return contains((Rectangle2afp<?, ?, ?, ?, ?, ?>) shape);
		}
		final PathIterator2afp<?> iterator = getPathIterator();
		final int crossings;
		if (shape instanceof Circle2afp) {
			final Circle2afp<?, ?, ?, ?, ?, ?> circle = (Circle2afp<?, ?, ?, ?, ?, ?>) shape;
			crossings = Path2afp.calculatesCrossingsPathIteratorCircleShadow(
					0, iterator,
					circle.getCenterX(), circle.getCenterY(), circle.getRadius(),
					CrossingComputationType.STANDARD);
		} else if (shape instanceof Ellipse2afp) {
			final Ellipse2afp<?, ?, ?, ?, ?, ?> ellipse = (Ellipse2afp<?, ?, ?, ?, ?, ?>) shape;
			crossings = Path2afp.calculatesCrossingsPathIteratorEllipseShadow(
					0, iterator,
					ellipse.getMinX(), ellipse.getMinY(), ellipse.getWidth(), ellipse.getHeight(),
					CrossingComputationType.STANDARD);
		} else if (shape instanceof RoundRectangle2afp) {
			final RoundRectangle2afp<?, ?, ?, ?, ?, ?> roundRectangle = (RoundRectangle2afp<?, ?, ?, ?, ?, ?>) shape;
			crossings = Path2afp.calculatesCrossingsPathIteratorRoundRectangleShadow(
					0, iterator,
					roundRectangle.getMinX(), roundRectangle.getMinY(),
					roundRectangle.getMaxX(), roundRectangle.getMaxY(),
					roundRectangle.getArcWidth(), roundRectangle.getArcHeight(),
					CrossingComputationType.STANDARD);
		} else if (shape instanceof Segment2afp) {
			final Segment2afp<?, ?, ?, ?, ?, ?> segment = (Segment2afp<?, ?, ?, ?, ?, ?>) shape;
			crossings = Path2afp.calculatesCrossingsPathIteratorSegmentShadow(
					0, iterator,
					segment.getX1(), segment.getY1(),
					segment.getX2(), segment.getY2(),
					CrossingComputationType.STANDARD);
		} else if (shape instanceof Triangle2afp) {
			final Triangle2afp<?, ?, ?, ?, ?, ?> triangle = (Triangle2afp<?, ?, ?, ?, ?, ?>) shape;
			crossings = Path2afp.calculatesCrossingsPathIteratorTriangleShadow(
					0, iterator,
					triangle.getX1(), triangle.getY1(),
					triangle.getX2(), triangle.getY2(),
					triangle.getX3(), triangle.getY3(),
					CrossingComputationType.STANDARD);
		} else if (!iterator.isPolygon()) {
		    // Only a polygon can contain another shape.
		    return false;
		} else {
            final double minX;
			final double minY;
			final double maxX;
			final double maxY;
			final Shape2D<?, ?, ?, ?, ?, ?> originalBounds = shape.toBoundingBox();
			if (originalBounds instanceof Rectangle2afp) {
			    final Rectangle2afp<?, ?, ?, ?, ?, ?> rect = (Rectangle2afp<?, ?, ?, ?, ?, ?>) originalBounds;
			    minX = rect.getMinX();
                minY = rect.getMinY();
                maxX = rect.getMaxX();
                maxY = rect.getMaxY();
			} else {
			    assert originalBounds instanceof Rectangle2ai;
                final Rectangle2ai<?, ?, ?, ?, ?, ?> rect = (Rectangle2ai<?, ?, ?, ?, ?, ?>) originalBounds;
                minX = rect.getMinX();
                minY = rect.getMinY();
                maxX = rect.getMaxX();
                maxY = rect.getMaxY();
			}
			final PathIterator2afp<?> shapePathIterator = iterator.getGeomFactory().convert(shape.getPathIterator());
			crossings = Path2afp.calculatesCrossingsPathIteratorPathShadow(
					0, iterator,
					new BasicPathShadow2afp(shapePathIterator, minX, minY, maxX, maxY),
					CrossingComputationType.STANDARD);
		}

		final int mask = iterator.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
		return crossings != GeomConstants.SHAPE_INTERSECTS
				&& (crossings & mask) != 0;
	}

	/** Replies if the given rectangle is inside this shape.
	 *
	 * @param rectangle the rectangle.
	 * @return {@code true} if the given rectangle is inside this
	 *     shape, otherwise {@code false}.
	 */
	@Pure
	boolean contains(Rectangle2afp<?, ?, ?, ?, ?, ?> rectangle);

	/** Replies if the given point is inside this shape.
	 *
	 * @param x x coordinate of the point to test.
	 * @param y y coordinate of the point to test.
	 * @return {@code true} if the given point is inside this
	 *     shape, otherwise {@code false}.
	 */
	@Pure
	boolean contains(double x, double y);

	/** Translate the shape.
	 *
	 * @param dx x translation.
	 * @param dy y translation.
	 */
	void translate(double dx, double dy);

	@Pure
	@Override
	default void translate(Vector2D<?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		translate(vector.getX(), vector.getY());
	}

	@Pure
	@Unefficient
	@Override
	default boolean intersects(Shape2D<?, ?, ?, ?, ?, ?> shape) {
		// CAUTION:
		// It is important to test several types in the reverse order than the inheritance hierarchy.
		if (shape instanceof Rectangle2afp) {
			return intersects((Rectangle2afp<?, ?, ?, ?, ?, ?>) shape);
		}
		if (shape instanceof OrientedRectangle2afp) {
			return intersects((OrientedRectangle2afp<?, ?, ?, ?, ?, ?>) shape);
		}
		if (shape instanceof Parallelogram2afp) {
			return intersects((Parallelogram2afp<?, ?, ?, ?, ?, ?>) shape);
		}
		//
		if (shape instanceof Circle2afp) {
			return intersects((Circle2afp<?, ?, ?, ?, ?, ?>) shape);
		}
		if (shape instanceof Ellipse2afp) {
			return intersects((Ellipse2afp<?, ?, ?, ?, ?, ?>) shape);
		}
		if (shape instanceof MultiShape2afp) {
			return intersects((MultiShape2afp<?, ?, ?, ?, ?, ?, ?>) shape);
		}
		if (shape instanceof Path2afp) {
			return intersects((Path2afp<?, ?, ?, ?, ?, ?>) shape);
		}
		if (shape instanceof PathIterator2afp) {
			return intersects((PathIterator2afp<?>) shape);
		}
		if (shape instanceof RoundRectangle2afp) {
			return intersects((RoundRectangle2afp<?, ?, ?, ?, ?, ?>) shape);
		}
		if (shape instanceof Segment2afp) {
			return intersects((Segment2afp<?, ?, ?, ?, ?, ?>) shape);
		}
		if (shape instanceof Triangle2afp) {
			return intersects((Triangle2afp<?, ?, ?, ?, ?, ?>) shape);
		}
		return intersects(getPathIterator());
	}

	/** Replies if this shape is intersecting the given ellipse.
	 *
	 * @param ellipse the ellipse.
	 * @return {@code true} if this shape is intersecting the given shape;
	 * {@code false} if there is no intersection.
	 */
	@Pure
	boolean intersects(Ellipse2afp<?, ?, ?, ?, ?, ?> ellipse);

	/** Replies if this shape is intersecting the given circle.
	 *
	 * @param circle the circle.
	 * @return {@code true} if this shape is intersecting the given shape;
	 * {@code false} if there is no intersection.
	 */
	@Pure
	boolean intersects(Circle2afp<?, ?, ?, ?, ?, ?> circle);

	/** Replies if this shape is intersecting the given rectangle.
	 *
	 * @param rectangle the rectangle.
	 * @return {@code true} if this shape is intersecting the given shape;
	 * {@code false} if there is no intersection.
	 */
	@Pure
	boolean intersects(Rectangle2afp<?, ?, ?, ?, ?, ?> rectangle);

	/** Replies if this shape is intersecting the given line.
	 *
	 * @param segment the segment.
	 * @return {@code true} if this shape is intersecting the given shape;
	 * {@code false} if there is no intersection.
	 */
	@Pure
	boolean intersects(Segment2afp<?, ?, ?, ?, ?, ?> segment);

	/** Replies if this shape is intersecting the given triangle.
	 *
	 * @param triangle the triangle.
	 * @return {@code true} if this shape is intersecting the given shape;
	 * {@code false} if there is no intersection.
	 */
	@Pure
	boolean intersects(Triangle2afp<?, ?, ?, ?, ?, ?> triangle);

	/** Replies if this shape is intersecting the given path.
	 *
	 * @param path the other path.
	 * @return {@code true} if this shape is intersecting the given path;
	 * {@code false} if there is no intersection.
	 */
	@Pure
	default boolean intersects(Path2afp<?, ?, ?, ?, ?, ?> path) {
		assert path != null : AssertMessages.notNullParameter();
		return intersects(path.getPathIterator());
	}

	/** Replies if this shape is intersecting the shape representing the given path iterator.
	 *
	 * @param iterator the path iterator.
	 * @return {@code true} if this shape is intersecting the given shape;
	 * {@code false} if there is no intersection.
	 */
	@Pure
	boolean intersects(PathIterator2afp<?> iterator);

	/** Replies if this shape is intersecting the given rectangle.
	 *
	 * @param orientedRectangle the oriented rectangle.
	 * @return {@code true} if this shape is intersecting the given shape;
	 * {@code false} if there is no intersection.
	 */
	@Pure
	boolean intersects(OrientedRectangle2afp<?, ?, ?, ?, ?, ?> orientedRectangle);

	/** Replies if this shape is intersecting the given parallelogram.
	 *
	 * @param parallelogram the parallelogram.
	 * @return {@code true} if this shape is intersecting the given shape;
	 * {@code false} if there is no intersection.
	 */
	@Pure
	boolean intersects(Parallelogram2afp<?, ?, ?, ?, ?, ?> parallelogram);

	/** Replies if this shape is intersecting the given rectangle.
	 *
	 * @param roundRectangle the round rectangle.
	 * @return {@code true} if this shape is intersecting the given shape;
	 * {@code false} if there is no intersection.
	 */
	@Pure
	boolean intersects(RoundRectangle2afp<?, ?, ?, ?, ?, ?> roundRectangle);

	/** Replies if this shape is intersecting the given multishape.
	 *
	 * @param multishape the multishape.
	 * @return {@code true} if this shape is intersecting the given shape;
	 * {@code false} if there is no intersection.
	 */
	@Pure
	boolean intersects(MultiShape2afp<?, ?, ?, ?, ?, ?, ?> multishape);

	@Pure
	@Unefficient
	@Override
	default double getDistanceSquared(Shape2D<?, ?, ?, ?, ?, ?> shape) {
		// CAUTION:
		// It is important to test several types in the reverse order than the inheritance hierarchy.
		if (shape instanceof Rectangle2afp) {
			return getDistanceSquared((Rectangle2afp<?, ?, ?, ?, ?, ?>) shape);
		}
		if (shape instanceof OrientedRectangle2afp) {
			return getDistanceSquared((OrientedRectangle2afp<?, ?, ?, ?, ?, ?>) shape);
		}
		if (shape instanceof Parallelogram2afp) {
			return getDistanceSquared((Parallelogram2afp<?, ?, ?, ?, ?, ?>) shape);
		}
		//
		if (shape instanceof Circle2afp) {
			return getDistanceSquared((Circle2afp<?, ?, ?, ?, ?, ?>) shape);
		}
		if (shape instanceof Ellipse2afp) {
			return getDistanceSquared((Ellipse2afp<?, ?, ?, ?, ?, ?>) shape);
		}
		if (shape instanceof MultiShape2afp) {
			return getDistanceSquared((MultiShape2afp<?, ?, ?, ?, ?, ?, ?>) shape);
		}
		if (shape instanceof Path2afp) {
			return getDistanceSquared((Path2afp<?, ?, ?, ?, ?, ?>) shape);
		}
		if (shape instanceof RoundRectangle2afp) {
			return getDistanceSquared((RoundRectangle2afp<?, ?, ?, ?, ?, ?>) shape);
		}
		if (shape instanceof Segment2afp) {
			return getDistanceSquared((Segment2afp<?, ?, ?, ?, ?, ?>) shape);
		}
		if (shape instanceof Triangle2afp) {
			return getDistanceSquared((Triangle2afp<?, ?, ?, ?, ?, ?>) shape);
		}
		throw new IllegalArgumentException();
	}

	/** Replies the minimum distance between this shape and the given ellipse.
	 *
	 * @param ellipse the ellipse.
	 * @return the minimum distance between the two shapes.
	 */
	@Pure
	default double getDistanceSquared(Ellipse2afp<?, ?, ?, ?, ?, ?> ellipse) {
		assert ellipse != null : AssertMessages.notNullParameter();
		return ellipse.getDistanceSquared(getClosestPointTo(ellipse));
	}

	/** Replies the minimum distance between this shape and the given circle.
	 *
	 * @param circle the circle.
	 * @return the minimum distance between the two shapes.
	 */
	@Pure
	default double getDistanceSquared(Circle2afp<?, ?, ?, ?, ?, ?> circle) {
		assert circle != null : AssertMessages.notNullParameter();
		return circle.getDistanceSquared(getClosestPointTo(circle));
	}

	/** Replies the minimum distance between this shape and the given rectangle.
	 *
	 * @param rectangle the rectangle.
	 * @return the minimum distance between the two shapes.
	 */
	@Pure
	default double getDistanceSquared(Rectangle2afp<?, ?, ?, ?, ?, ?> rectangle) {
		assert rectangle != null : AssertMessages.notNullParameter();
		return rectangle.getDistanceSquared(getClosestPointTo(rectangle));
	}

	/** Replies the minimum distance between this shape and the given segment.
	 *
	 * @param segment the segment.
	 * @return the minimum distance between the two shapes.
	 */
	@Pure
	default double getDistanceSquared(Segment2afp<?, ?, ?, ?, ?, ?> segment) {
		assert segment != null : AssertMessages.notNullParameter();
		return segment.getDistanceSquared(getClosestPointTo(segment));
	}

	/** Replies the minimum distance between this shape and the given triangle.
	 *
	 * @param triangle the triangle.
	 * @return the minimum distance between the two shapes.
	 */
	@Pure
	default double getDistanceSquared(Triangle2afp<?, ?, ?, ?, ?, ?> triangle) {
		assert triangle != null : AssertMessages.notNullParameter();
		return triangle.getDistanceSquared(getClosestPointTo(triangle));
	}

	/** Replies the minimum distance between this shape and the given path.
	 *
	 * @param path the path.
	 * @return the minimum distance between the two shapes.
	 */
	@Pure
	default double getDistanceSquared(Path2afp<?, ?, ?, ?, ?, ?> path) {
		assert path != null : AssertMessages.notNullParameter();
		return path.getDistanceSquared(getClosestPointTo(path));
	}

	/** Replies the minimum distance between this shape and the given oriented rectangle.
	 *
	 * @param orientedRectangle the oriented rectangle.
	 * @return the minimum distance between the two shapes.
	 */
	@Pure
	default double getDistanceSquared(OrientedRectangle2afp<?, ?, ?, ?, ?, ?> orientedRectangle) {
		assert orientedRectangle != null : AssertMessages.notNullParameter();
		return orientedRectangle.getDistanceSquared(getClosestPointTo(orientedRectangle));
	}

	/** Replies the minimum distance between this shape and the given parallelogram.
	 *
	 * @param parallelogram the parallelogram.
	 * @return the minimum distance between the two shapes.
	 */
	@Pure
	default double getDistanceSquared(Parallelogram2afp<?, ?, ?, ?, ?, ?> parallelogram) {
		assert parallelogram != null : AssertMessages.notNullParameter();
		return parallelogram.getDistanceSquared(getClosestPointTo(parallelogram));
	}

	/** Replies the minimum distance between this shape and the given round rectangle.
	 *
	 * @param roundRectangle the round rectangle.
	 * @return the minimum distance between the two shapes.
	 */
	@Pure
	default double getDistanceSquared(RoundRectangle2afp<?, ?, ?, ?, ?, ?> roundRectangle) {
		assert roundRectangle != null : AssertMessages.notNullParameter();
		return roundRectangle.getDistanceSquared(getClosestPointTo(roundRectangle));
	}

	/** Replies the minimum distance between this shape and the given multishape.
	 *
	 * @param multishape the multishape.
	 * @return the minimum distance between the two shapes.
	 */
	@Pure
	default double getDistanceSquared(MultiShape2afp<?, ?, ?, ?, ?, ?, ?> multishape) {
	    assert multishape != null : AssertMessages.notNullParameter();
        double minDist = Double.POSITIVE_INFINITY;
        double dist;
        for (final Shape2afp<?, ?, ?, ?, ?, ?> shape : multishape) {
            dist = getDistanceSquared(shape);
            if (dist < minDist) {
                minDist = dist;
            }
        }
        return minDist;
	}

	@Pure
	@Unefficient
	@Override
	default P getClosestPointTo(Shape2D<?, ?, ?, ?, ?, ?> shape) {
		// CAUTION:
		// It is important to test several types in the reverse order than the inheritance hierarchy.
		if (shape instanceof Rectangle2afp) {
			return getClosestPointTo((Rectangle2afp<?, ?, ?, ?, ?, ?>) shape);
		}
		if (shape instanceof OrientedRectangle2afp) {
			return getClosestPointTo((OrientedRectangle2afp<?, ?, ?, ?, ?, ?>) shape);
		}
		if (shape instanceof Parallelogram2afp) {
			return getClosestPointTo((Parallelogram2afp<?, ?, ?, ?, ?, ?>) shape);
		}
		//
		if (shape instanceof Circle2afp) {
			return getClosestPointTo((Circle2afp<?, ?, ?, ?, ?, ?>) shape);
		}
		if (shape instanceof Ellipse2afp) {
			return getClosestPointTo((Ellipse2afp<?, ?, ?, ?, ?, ?>) shape);
		}
		if (shape instanceof MultiShape2afp) {
			return getClosestPointTo((MultiShape2afp<?, ?, ?, ?, ?, ?, ?>) shape);
		}
		if (shape instanceof Path2afp) {
			return getClosestPointTo((Path2afp<?, ?, ?, ?, ?, ?>) shape);
		}
		if (shape instanceof RoundRectangle2afp) {
			return getClosestPointTo((RoundRectangle2afp<?, ?, ?, ?, ?, ?>) shape);
		}
		if (shape instanceof Segment2afp) {
			return getClosestPointTo((Segment2afp<?, ?, ?, ?, ?, ?>) shape);
		}
		if (shape instanceof Triangle2afp) {
			return getClosestPointTo((Triangle2afp<?, ?, ?, ?, ?, ?>) shape);
		}
		throw new IllegalArgumentException();
	}

	/** Replies the closest point on this shape to the given ellipse.
	 *
	 * <p>If the two shapes are intersecting, the replied point is always at the intersection
	 * of the two shapes. This function does not enforce the meaning of the replied point
	 * in the case of shape intersection. In other words, this function is warranting that
	 * the reply point is the either the penetration point, nor a perimeter point, nor any point
	 * with a specific meaning.
	 *
	 * @param ellipse the ellipse.
	 * @return the closest point on the shape; or the point itself
	 *     if it is inside the shape.
	 */
	@Pure
	P getClosestPointTo(Ellipse2afp<?, ?, ?, ?, ?, ?> ellipse);

	/** Replies the closest point on this shape to the given circle.
	 *
	 * <p>If the two shapes are intersecting, the replied point is always at the intersection
	 * of the two shapes. This function does not enforce the meaning of the replied point
	 * in the case of shape intersection. In other words, this function is warranting that
	 * the reply point is the either the penetration point, nor a perimeter point, nor any point
	 * with a specific meaning.
	 *
	 * @param circle the circle.
	 * @return the closest point on the shape; or the point itself
	 *     if it is inside the shape.
	 */
	@Pure
	@Unefficient
	P getClosestPointTo(Circle2afp<?, ?, ?, ?, ?, ?> circle);

	/** Replies the closest point on this shape to the given rectangle.
	 *
	 * <p>If the two shapes are intersecting, the replied point is always at the intersection
	 * of the two shapes. This function does not enforce the meaning of the replied point
	 * in the case of shape intersection. In other words, this function is warranting that
	 * the reply point is the either the penetration point, nor a perimeter point, nor any point
	 * with a specific meaning.
	 *
	 * @param rectangle the rectangle.
	 * @return the closest point on the shape; or the point itself
	 *     if it is inside the shape.
	 */
	@Pure
	P getClosestPointTo(Rectangle2afp<?, ?, ?, ?, ?, ?> rectangle);

	/** Replies the closest point on this shape to the given segment.
	 *
	 * <p>If the two shapes are intersecting, the replied point is always at the intersection
	 * of the two shapes. This function does not enforce the meaning of the replied point
	 * in the case of shape intersection. In other words, this function is warranting that
	 * the reply point is the either the penetration point, nor a perimeter point, nor any point
	 * with a specific meaning.
	 *
	 * @param segment the segment.
	 * @return the closest point on the shape; or the point itself
	 *     if it is inside the shape.
	 */
	@Pure
	P getClosestPointTo(Segment2afp<?, ?, ?, ?, ?, ?> segment);

	/** Replies the closest point on this shape to the given triangle.
	 *
	 * <p>If the two shapes are intersecting, the replied point is always at the intersection
	 * of the two shapes. This function does not enforce the meaning of the replied point
	 * in the case of shape intersection. In other words, this function is warranting that
	 * the reply point is the either the penetration point, nor a perimeter point, nor any point
	 * with a specific meaning.
	 *
	 * @param triangle the triangle.
	 * @return the closest point on the shape; or the point itself
	 *     if it is inside the shape.
	 */
	@Pure
	P getClosestPointTo(Triangle2afp<?, ?, ?, ?, ?, ?> triangle);

	/** Replies the closest point on this shape to the given oriented rectangle.
	 *
	 * <p>If the two shapes are intersecting, the replied point is always at the intersection
	 * of the two shapes. This function does not enforce the meaning of the replied point
	 * in the case of shape intersection. In other words, this function is warranting that
	 * the reply point is the either the penetration point, nor a perimeter point, nor any point
	 * with a specific meaning.
	 *
	 * @param orientedRectangle the oriented rectangle.
	 * @return the closest point on the shape; or the point itself
	 *     if it is inside the shape.
	 */
	@Pure
	P getClosestPointTo(OrientedRectangle2afp<?, ?, ?, ?, ?, ?> orientedRectangle);

	/** Replies the closest point on this shape to the given parallelogram.
	 *
	 * <p>If the two shapes are intersecting, the replied point is always at the intersection
	 * of the two shapes. This function does not enforce the meaning of the replied point
	 * in the case of shape intersection. In other words, this function is warranting that
	 * the reply point is the either the penetration point, nor a perimeter point, nor any point
	 * with a specific meaning.
	 *
	 * @param parallelogram the parallelogram.
	 * @return the closest point on the shape; or the point itself
	 *     if it is inside the shape.
	 */
	@Pure
	P getClosestPointTo(Parallelogram2afp<?, ?, ?, ?, ?, ?> parallelogram);

	/** Replies the closest point on this shape to the given round rectangle.
	 *
	 * <p>If the two shapes are intersecting, the replied point is always at the intersection
	 * of the two shapes. This function does not enforce the meaning of the replied point
	 * in the case of shape intersection. In other words, this function is warranting that
	 * the reply point is the either the penetration point, nor a perimeter point, nor any point
	 * with a specific meaning.
	 *
	 * @param roundRectangle the round rectangle.
	 * @return the closest point on the shape; or the point itself
	 *     if it is inside the shape.
	 */
	@Pure
	P getClosestPointTo(RoundRectangle2afp<?, ?, ?, ?, ?, ?> roundRectangle);

	/** Replies the closest point on this shape to the given path.
	 *
	 * <p>If the two shapes are intersecting, the replied point is always at the intersection
	 * of the two shapes. This function does not enforce the meaning of the replied point
	 * in the case of shape intersection. In other words, this function is warranting that
	 * the reply point is the either the penetration point, nor a perimeter point, nor any point
	 * with a specific meaning.
	 *
	 * @param path the path.
	 * @return the closest point on the shape; or the point itself
	 *     if it is inside the shape.
	 */
	@Pure
	P getClosestPointTo(Path2afp<?, ?, ?, ?, ?, ?> path);

	/** Replies the closest point on this shape to the given multishape.
	 *
	 * <p>If the two shapes are intersecting, the replied point is always at the intersection
	 * of the two shapes. This function does not enforce the meaning of the replied point
	 * in the case of shape intersection. In other words, this function is warranting that
	 * the reply point is the either the penetration point, nor a perimeter point, nor any point
	 * with a specific meaning.
	 *
	 * @param multishape the multishape.
	 * @return the closest point on the shape; or the point itself
	 *     if it is inside the shape.
	 */
	@Pure
	default P getClosestPointTo(MultiShape2afp<?, ?, ?, ?, ?, ?, ?> multishape) {
		assert multishape != null : AssertMessages.notNullParameter();
		Shape2afp<?, ?, ?, ?, ?, ?> closest = null;
		double minDist = Double.POSITIVE_INFINITY;
		double dist;
		for (final Shape2afp<?, ?, ?, ?, ?, ?> shape : multishape) {
			dist = getDistanceSquared(shape);
			if (dist < minDist) {
				minDist = dist;
				closest = shape;
			}
		}
		if (closest == null) {
			return getGeomFactory().newPoint();
		}
		return getClosestPointTo(closest);
	}

	@Override
	GeomFactory2afp<IE, P, V, B> getGeomFactory();

	@Pure
	@SuppressWarnings("unchecked")
	@Override
	default ST createTransformedShape(Transform2D transform) {
		if (transform == null || transform.isIdentity()) {
			return (ST) clone();
		}
		final PathIterator2afp<?> pi = getPathIterator(transform);
		final GeomFactory2afp<IE, P, V, B> factory = getGeomFactory();
		final Path2afp<?, ?, ?, P, V, ?> newPath = factory.newPath(pi.getWindingRule());
		while (pi.hasNext()) {
			final PathElement2afp e = pi.next();
			switch (e.getType()) {
			case MOVE_TO:
				newPath.moveTo(e.getToX(), e.getToY());
				break;
			case LINE_TO:
				newPath.lineTo(e.getToX(), e.getToY());
				break;
			case QUAD_TO:
				newPath.quadTo(e.getCtrlX1(), e.getCtrlY1(), e.getToX(), e.getToY());
				break;
			case CURVE_TO:
				newPath.curveTo(e.getCtrlX1(), e.getCtrlY1(), e.getCtrlX2(), e.getCtrlY2(), e.getToX(), e.getToY());
				break;
			case ARC_TO:
				newPath.arcTo(e.getToX(), e.getToY(), e.getRadiusX(), e.getRadiusY(), e.getRotationX(),
						e.getLargeArcFlag(), e.getSweepFlag());
				break;
			case CLOSE:
				newPath.closePath();
				break;
			default:
			}
		}
		return (ST) newPath;
	}

	@Override
	default B toBoundingBox() {
		final B box = getGeomFactory().newBox();
		toBoundingBox(box);
		return box;
	}

}
