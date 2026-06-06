/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2026 The original authors and other contributors.
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

import org.arakhne.afc.math.Unefficient;
import org.arakhne.afc.math.geometry.base.CrossingComputationType;
import org.arakhne.afc.math.geometry.base.GeomConstants;
import org.arakhne.afc.math.geometry.base.PathWindingRule;
import org.arakhne.afc.math.geometry.base.d2.Point2D;
import org.arakhne.afc.math.geometry.base.d2.Shape2D;
import org.arakhne.afc.math.geometry.base.d2.Transform2D;
import org.arakhne.afc.math.geometry.base.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.ai.Rectangle2ai;
import org.arakhne.afc.math.geometry.d2.general.Shape2DType;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.eclipse.xtext.xbase.lib.Pure;

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

	/** Replies the type of the shape as an enumeration.
	 *
	 * @param <TY> the type of the enumeration.
	 * @param enumerationType the type of the enumeration.
	 * @return the shape type.
	 * @since 18.0
	 */
	@Pure
	@Override
	default <TY extends Enum<TY>> TY getType(Class<TY> enumerationType) {
		assert enumerationType != null && enumerationType.equals(Shape2DType.class) : AssertMessages.invalidValue(0);
		return enumerationType.cast(getType());
	}

	/** Replies the type of the shape as an enumeration.
	 *
	 * @return the shape type.
	 * @since 18.0
	 */
	@Pure
	Shape2DType getType();

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
		final var type = shape.getType(Shape2DType.class);
		assert type != null;
		if (type == Shape2DType.RECTANGLE) {
			return contains((Rectangle2afp<?, ?, ?, ?, ?, ?>) shape);
		}
		final var iterator = getPathIterator();
		var crossings = 0;
		switch (type) {
		case CIRCLE:
			final var circle = (Circle2afp<?, ?, ?, ?, ?, ?>) shape;
			crossings = Path2afp.calculatesCrossingsPathIteratorCircleShadow(
					0, iterator,
					circle.getCenterX(), circle.getCenterY(), circle.getRadius(),
					CrossingComputationType.STANDARD);
			break;
		case ELLIPSE:
			final var ellipse = (Ellipse2afp<?, ?, ?, ?, ?, ?>) shape;
			crossings = Path2afp.calculatesCrossingsPathIteratorEllipseShadow(
					0, iterator,
					ellipse.getMinX(), ellipse.getMinY(), ellipse.getWidth(), ellipse.getHeight(),
					CrossingComputationType.STANDARD);
			break;
		case ROUND_RECTANGLE:
			final var roundRectangle = (RoundRectangle2afp<?, ?, ?, ?, ?, ?>) shape;
			crossings = Path2afp.calculatesCrossingsPathIteratorRoundRectangleShadow(
					0, iterator,
					roundRectangle.getMinX(), roundRectangle.getMinY(),
					roundRectangle.getMaxX(), roundRectangle.getMaxY(),
					roundRectangle.getArcWidth(), roundRectangle.getArcHeight(),
					CrossingComputationType.STANDARD);
			break;
		case SEGMENT:
			final var segment = (Segment2afp<?, ?, ?, ?, ?, ?>) shape;
			crossings = Path2afp.calculatesCrossingsPathIteratorSegmentShadow(
					0, iterator,
					segment.getX1(), segment.getY1(),
					segment.getX2(), segment.getY2(),
					CrossingComputationType.STANDARD);
			break;
		case TRIANGLE:
			final var triangle = (Triangle2afp<?, ?, ?, ?, ?, ?>) shape;
			crossings = Path2afp.calculatesCrossingsPathIteratorTriangleShadow(
					0, iterator,
					triangle.getX1(), triangle.getY1(),
					triangle.getX2(), triangle.getY2(),
					triangle.getX3(), triangle.getY3(),
					CrossingComputationType.STANDARD);
			break;
		case RECTANGLE:
		case ORIENTED_RECTANGLE:
		case PARALLELOGRAM:
		case PATH:
		case MULTISHAPE:
		default:
			if (!iterator.isPolygon()) {
			    // Only a polygon can contain another shape.
			    return false;
			}
			final double minX;
			final double minY;
			final double maxX;
			final double maxY;
			final var originalBounds = shape.toBoundingBox();
			if (originalBounds instanceof Rectangle2afp rect) {
			    minX = rect.getMinX();
                minY = rect.getMinY();
                maxX = rect.getMaxX();
                maxY = rect.getMaxY();
			} else {
			    assert originalBounds instanceof Rectangle2ai;
                final var rect = (Rectangle2ai<?, ?, ?, ?, ?, ?>) originalBounds;
                minX = rect.getMinX();
                minY = rect.getMinY();
                maxX = rect.getMaxX();
                maxY = rect.getMaxY();
			}
			final var shapePathIterator = iterator.getGeomFactory().convert(shape.getPathIterator());
			crossings = Path2afp.calculatesCrossingsPathIteratorPathShadow(
					0, iterator,
					new BasicPathShadow2afp(shapePathIterator, minX, minY, maxX, maxY),
					CrossingComputationType.STANDARD);
			break;
		}
		final var mask = iterator.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
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
		assert shape != null : AssertMessages.notNullParameter();
		final var type = shape.getType(Shape2DType.class);
		assert type != null;
    	assert type.getPreferredContinuousShapeType() != null;
    	assert type.getPreferredContinuousShapeType().isInstance(shape);
		switch (type) {
		case CIRCLE:
			return intersects((Circle2afp<?, ?, ?, ?, ?, ?>) shape);
		case ELLIPSE:
			return intersects((Ellipse2afp<?, ?, ?, ?, ?, ?>) shape);
		case MULTISHAPE:
			return intersects((MultiShape2afp<?, ?, ?, ?, ?, ?, ?>) shape);
		case ORIENTED_RECTANGLE:
			return intersects((OrientedRectangle2afp<?, ?, ?, ?, ?, ?>) shape);
		case PARALLELOGRAM:
			return intersects((Parallelogram2afp<?, ?, ?, ?, ?, ?>) shape);
		case PATH:
			return intersects((Path2afp<?, ?, ?, ?, ?, ?>) shape);
		case RECTANGLE:
			return intersects((Rectangle2afp<?, ?, ?, ?, ?, ?>) shape);
		case ROUND_RECTANGLE:
			return intersects((RoundRectangle2afp<?, ?, ?, ?, ?, ?>) shape);
		case SEGMENT:
			return intersects((Segment2afp<?, ?, ?, ?, ?, ?>) shape);
		case TRIANGLE:
			return intersects((Triangle2afp<?, ?, ?, ?, ?, ?>) shape);
		default:
			break;
		}
		throw new IllegalStateException();
	}

	/** Replies if this shape is intersecting the given ellipse.
	 *
	 * @param ellipse the ellipse.
	 * @return {@code true} if this shape is intersecting the given shape;
	 *     {@code false} if there is no intersection.
	 */
	@Pure
	boolean intersects(Ellipse2afp<?, ?, ?, ?, ?, ?> ellipse);

	/** Replies if this shape is intersecting the given circle.
	 *
	 * @param circle the circle.
	 * @return {@code true} if this shape is intersecting the given shape;
	 *     {@code false} if there is no intersection.
	 */
	@Pure
	boolean intersects(Circle2afp<?, ?, ?, ?, ?, ?> circle);

	/** Replies if this shape is intersecting the given rectangle.
	 *
	 * @param rectangle the rectangle.
	 * @return {@code true} if this shape is intersecting the given shape;
	 *     {@code false} if there is no intersection.
	 */
	@Pure
	boolean intersects(Rectangle2afp<?, ?, ?, ?, ?, ?> rectangle);

	/** Replies if this shape is intersecting the given line.
	 *
	 * @param segment the segment.
	 * @return {@code true} if this shape is intersecting the given shape;
	 *     {@code false} if there is no intersection.
	 */
	@Pure
	boolean intersects(Segment2afp<?, ?, ?, ?, ?, ?> segment);

	/** Replies if this shape is intersecting the given triangle.
	 *
	 * @param triangle the triangle.
	 * @return {@code true} if this shape is intersecting the given shape;
	 *     {@code false} if there is no intersection.
	 */
	@Pure
	boolean intersects(Triangle2afp<?, ?, ?, ?, ?, ?> triangle);

	/** Replies if this shape is intersecting the given path.
	 *
	 * @param path the other path.
	 * @return {@code true} if this shape is intersecting the given path;
	 *     {@code false} if there is no intersection.
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
	 *     {@code false} if there is no intersection.
	 */
	@Pure
	boolean intersects(PathIterator2afp<?> iterator);

	/** Replies if this shape is intersecting the given rectangle.
	 *
	 * @param orientedRectangle the oriented rectangle.
	 * @return {@code true} if this shape is intersecting the given shape;
	 *     {@code false} if there is no intersection.
	 */
	@Pure
	boolean intersects(OrientedRectangle2afp<?, ?, ?, ?, ?, ?> orientedRectangle);

	/** Replies if this shape is intersecting the given parallelogram.
	 *
	 * @param parallelogram the parallelogram.
	 * @return {@code true} if this shape is intersecting the given shape;
	 *     {@code false} if there is no intersection.
	 */
	@Pure
	boolean intersects(Parallelogram2afp<?, ?, ?, ?, ?, ?> parallelogram);

	/** Replies if this shape is intersecting the given rectangle.
	 *
	 * @param roundRectangle the round rectangle.
	 * @return {@code true} if this shape is intersecting the given shape;
	 *     {@code false} if there is no intersection.
	 */
	@Pure
	boolean intersects(RoundRectangle2afp<?, ?, ?, ?, ?, ?> roundRectangle);

	/** Replies if this shape is intersecting the given multishape.
	 *
	 * @param multishape the multishape.
	 * @return {@code true} if this shape is intersecting the given shape;
	 *     {@code false} if there is no intersection.
	 */
	@Pure
	boolean intersects(MultiShape2afp<?, ?, ?, ?, ?, ?, ?> multishape);

	@Pure
	@Unefficient
	@Override
	default double getDistanceSquared(Shape2D<?, ?, ?, ?, ?, ?> shape) {
		assert shape != null : AssertMessages.notNullParameter();
		final var type = shape.getType(Shape2DType.class);
		assert type != null;
    	assert type.getPreferredContinuousShapeType() != null;
    	assert type.getPreferredContinuousShapeType().isInstance(shape);
		switch (type) {
		case CIRCLE:
			return getDistanceSquared((Circle2afp<?, ?, ?, ?, ?, ?>) shape);
		case ELLIPSE:
			return getDistanceSquared((Ellipse2afp<?, ?, ?, ?, ?, ?>) shape);
		case MULTISHAPE:
			return getDistanceSquared((MultiShape2afp<?, ?, ?, ?, ?, ?, ?>) shape);
		case ORIENTED_RECTANGLE:
			return getDistanceSquared((OrientedRectangle2afp<?, ?, ?, ?, ?, ?>) shape);
		case PARALLELOGRAM:
			return getDistanceSquared((Parallelogram2afp<?, ?, ?, ?, ?, ?>) shape);
		case PATH:
			return getDistanceSquared((Path2afp<?, ?, ?, ?, ?, ?>) shape);
		case RECTANGLE:
			return getDistanceSquared((Rectangle2afp<?, ?, ?, ?, ?, ?>) shape);
		case ROUND_RECTANGLE:
			return getDistanceSquared((RoundRectangle2afp<?, ?, ?, ?, ?, ?>) shape);
		case SEGMENT:
			return getDistanceSquared((Segment2afp<?, ?, ?, ?, ?, ?>) shape);
		case TRIANGLE:
			return getDistanceSquared((Triangle2afp<?, ?, ?, ?, ?, ?>) shape);
		default:
			break;
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
	    var minDist = Double.POSITIVE_INFINITY;
        for (final var shape : multishape) {
            final var dist = getDistanceSquared(shape);
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
		assert shape != null : AssertMessages.notNullParameter();
		final var type = shape.getType(Shape2DType.class);
		assert type != null;
    	assert type.getPreferredContinuousShapeType() != null;
    	assert type.getPreferredContinuousShapeType().isInstance(shape);
		switch (type) {
		case CIRCLE:
			return getClosestPointTo((Circle2afp<?, ?, ?, ?, ?, ?>) shape);
		case ELLIPSE:
			return getClosestPointTo((Ellipse2afp<?, ?, ?, ?, ?, ?>) shape);
		case MULTISHAPE:
			return getClosestPointTo((MultiShape2afp<?, ?, ?, ?, ?, ?, ?>) shape);
		case ORIENTED_RECTANGLE:
			return getClosestPointTo((OrientedRectangle2afp<?, ?, ?, ?, ?, ?>) shape);
		case PARALLELOGRAM:
			return getClosestPointTo((Parallelogram2afp<?, ?, ?, ?, ?, ?>) shape);
		case PATH:
			return getClosestPointTo((Path2afp<?, ?, ?, ?, ?, ?>) shape);
		case RECTANGLE:
			return getClosestPointTo((Rectangle2afp<?, ?, ?, ?, ?, ?>) shape);
		case ROUND_RECTANGLE:
			return getClosestPointTo((RoundRectangle2afp<?, ?, ?, ?, ?, ?>) shape);
		case SEGMENT:
			return getClosestPointTo((Segment2afp<?, ?, ?, ?, ?, ?>) shape);
		case TRIANGLE:
			return getClosestPointTo((Triangle2afp<?, ?, ?, ?, ?, ?>) shape);
		default:
			break;
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
		var minDist = Double.POSITIVE_INFINITY;
		for (final var shape : multishape) {
			final var dist = getDistanceSquared(shape);
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
		final var pi = getPathIterator(transform);
		final var factory = getGeomFactory();
		final var newPath = factory.newPath(pi.getWindingRule());
		while (pi.hasNext()) {
			final var e = pi.next();
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
		final var box = getGeomFactory().newBox();
		toBoundingBox(box);
		return box;
	}

}
