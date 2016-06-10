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

package org.arakhne.afc.math.geometry.d2.ai;

import java.util.Iterator;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.Unefficient;
import org.arakhne.afc.math.geometry.CrossingComputationType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Shape2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** 2D shape with 2d floating coordinates.
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
public interface Shape2ai<
        ST extends Shape2ai<?, ?, IE, P, V, B>,
        IT extends Shape2ai<?, ?, IE, P, V, B>,
        IE extends PathElement2ai,
        P extends Point2D<? super P, ? super V>,
        V extends Vector2D<? super V, ? super P>,
        B extends Rectangle2ai<?, ?, IE, P, V, B>>
        extends Shape2D<ST, IT, PathIterator2ai<IE>, P, V, B> {

    /** Replies an iterator on the points covered by the perimeter of this shape.
     *
     * <p>The implementation of the iterator depends on the shape type.
     * There is no warranty about the order of the points.
     *
     * @return an iterator on the points that are located at the perimeter of the shape.
     */
    @Pure
    Iterator<P> getPointIterator();

    @Pure
    @Override
    default boolean contains(Point2D<?, ?> pt) {
        return contains(pt.ix(), pt.iy());
    }

    /** Replies if the given point is inside this shape.
     *
     * @param x x coordinate of the point to test.
     * @param y y coordinate of the point to test.
     * @return <code>true</code> if the given point is inside this
     *     shape, otherwise <code>false</code>.
     */
    @Pure
    boolean contains(int x, int y);

    /** Replies if the given rectangle is inside this shape.
     *
     * @param box the rectangle to test.
     * @return <code>true</code> if the given box is inside the shape.
     */
    @Pure
    boolean contains(Rectangle2ai<?, ?, ?, ?, ?, ?> box);

    @Pure
    @Unefficient
    @Override
    default boolean contains(Shape2D<?, ?, ?, ?, ?, ?> shape) {
        assert shape != null : AssertMessages.notNullParameter();
        if (isEmpty()) {
            return false;
        }
        if (shape instanceof Rectangle2ai) {
            return contains((Rectangle2ai<?, ?, ?, ?, ?, ?>) shape);
        }
        final PathIterator2ai<?> iterator = getPathIterator();
        final int crossings;
        if (shape instanceof Circle2ai) {
            final Circle2ai<?, ?, ?, ?, ?, ?> circle = (Circle2ai<?, ?, ?, ?, ?, ?>) shape;
            crossings = Path2ai.calculatesCrossingsPathIteratorCircleShadow(
                    0, iterator,
                    circle.getX(), circle.getY(), circle.getRadius(),
                    CrossingComputationType.STANDARD);
        } else if (shape instanceof Segment2ai) {
            final Segment2ai<?, ?, ?, ?, ?, ?> segment = (Segment2ai<?, ?, ?, ?, ?, ?>) shape;
            crossings = Path2ai.calculatesCrossingsPathIteratorSegmentShadow(
                    0, iterator,
                    segment.getX1(), segment.getY1(),
                    segment.getX2(), segment.getY2(),
                    CrossingComputationType.STANDARD);
        } else if (!iterator.isPolygon()) {
            // Only a polygon can contain another shape.
            return false;
        } else {
            final int minX;
            final int minY;
            final int maxX;
            final int maxY;
            final Shape2D<?, ?, ?, ?, ?, ?> originalBounds = shape.toBoundingBox();
            if (originalBounds instanceof Rectangle2ai) {
                final Rectangle2ai<?, ?, ?, ?, ?, ?> rect = (Rectangle2ai<?, ?, ?, ?, ?, ?>) originalBounds;
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
            final PathIterator2ai<?> shapePathIterator = iterator.getGeomFactory().convert(shape.getPathIterator());
            crossings = Path2ai.calculatesCrossingsPathIteratorPathShadow(
                    0, iterator,
                    new BasicPathShadow2ai(shapePathIterator, minX, minY, maxX, maxY),
                    CrossingComputationType.STANDARD);
        }

        final int mask = iterator.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
        return crossings != MathConstants.SHAPE_INTERSECTS
                && (crossings & mask) != 0;
    }

    @Pure
    @Override
    default void translate(Vector2D<?, ?> vector) {
        translate(vector.ix(), vector.iy());
    }

    /** Translate the shape.
     *
     * @param dx x translation.
     * @param dy y translation.
     */
    void translate(int dx, int dy);

    @Pure
    @Override
    default B toBoundingBox() {
        final B box = getGeomFactory().newBox();
        toBoundingBox(box);
        return box;
    }

    @Pure
    @Unefficient
    @Override
    default boolean intersects(Shape2D<?, ?, ?, ?, ?, ?> shape) {
        if (shape instanceof Circle2ai) {
            return intersects((Circle2ai<?, ?, ?, ?, ?, ?>) shape);
        }
        if (shape instanceof Path2ai) {
            return intersects((Path2ai<?, ?, ?, ?, ?, ?>) shape);
        }
        if (shape instanceof PathIterator2ai) {
            return intersects((PathIterator2ai<?>) shape);
        }
        if (shape instanceof Rectangle2ai) {
            return intersects((Rectangle2ai<?, ?, ?, ?, ?, ?>) shape);
        }
        if (shape instanceof Segment2ai) {
            return intersects((Segment2ai<?, ?, ?, ?, ?, ?>) shape);
        }
        return intersects(getPathIterator());
    }

    /** Replies if this shape is intersecting the given rectangle.
     *
     * @param rectangle the rectangle.
     * @return <code>true</code> if this shape is intersecting the given shape;
     * <code>false</code> if there is no intersection.
     */
    @Pure
    boolean intersects(Rectangle2ai<?, ?, ?, ?, ?, ?> rectangle);

    /** Replies if this shape is intersecting the given circle.
     *
     * @param circle the circle.
     * @return <code>true</code> if this shape is intersecting the given shape;
     * <code>false</code> if there is no intersection.
     */
    @Pure
    boolean intersects(Circle2ai<?, ?, ?, ?, ?, ?> circle);

    /** Replies if this shape is intersecting the given segment.
     *
     * @param segment the segment.
     * @return <code>true</code> if this shape is intersecting the given shape;
     * <code>false</code> if there is no intersection.
     */
    @Pure
    boolean intersects(Segment2ai<?, ?, ?, ?, ?, ?> segment);

    /** Replies if this shape is intersecting the given multishape.
     *
     * @param multishape the multishape.
     * @return <code>true</code> if this shape is intersecting the given shape;
     * <code>false</code> if there is no intersection.
     */
    @Pure
    boolean intersects(MultiShape2ai<?, ?, ?, ?, ?, ?, ?> multishape);

    /** Replies if this shape is intersecting the given path.
     *
     * @param path the path.
     * @return <code>true</code> if this shape is intersecting the given shape;
     * <code>false</code> if there is no intersection.
     */
    @Pure
    default boolean intersects(Path2ai<?, ?, ?, ?, ?, ?> path) {
        return intersects(path.getPathIterator());
    }

    /** Replies if this shape is intersecting the path described by the given iterator.
     *
     * @param iterator the path iterator.
     * @return <code>true</code> if this shape is intersecting the given shape;
     * <code>false</code> if there is no intersection.
     */
    @Pure
    boolean intersects(PathIterator2ai<?> iterator);

    @Pure
    @Unefficient
    @Override
    default double getDistanceSquared(Shape2D<?, ?, ?, ?, ?, ?> shape) {
        if (shape instanceof Circle2ai) {
            return getDistanceSquared((Circle2ai<?, ?, ?, ?, ?, ?>) shape);
        }
        if (shape instanceof Path2ai) {
            return getDistanceSquared((Path2ai<?, ?, ?, ?, ?, ?>) shape);
        }
        if (shape instanceof Rectangle2ai) {
            return getDistanceSquared((Rectangle2ai<?, ?, ?, ?, ?, ?>) shape);
        }
        if (shape instanceof Segment2ai) {
            return getDistanceSquared((Segment2ai<?, ?, ?, ?, ?, ?>) shape);
        }
        throw new IllegalArgumentException();
    }

    /** Replies the minimum distance between this shape and the given rectangle.
     *
     * @param rectangle the rectangle.
     * @return the minimum distance between the two shapes.
     */
    @Pure
    default double getDistanceSquared(Rectangle2ai<?, ?, ?, ?, ?, ?> rectangle) {
        assert rectangle != null : AssertMessages.notNullParameter();
        return rectangle.getDistanceSquared(getClosestPointTo(rectangle));
    }

    /** Replies the minimum distance between this shape and the given circle.
     *
     * @param circle the circle
     * @return the minimum distance between the two shapes.
     */
    @Pure
    default double getDistanceSquared(Circle2ai<?, ?, ?, ?, ?, ?> circle) {
        assert circle != null : AssertMessages.notNullParameter();
        return circle.getDistanceSquared(getClosestPointTo(circle));
    }

    /** Replies the minimum distance between this shape and the given segment.
     *
     * @param segment the segment.
     * @return the minimum distance between the two shapes.
     */
    @Pure
    default double getDistanceSquared(Segment2ai<?, ?, ?, ?, ?, ?> segment) {
        assert segment != null : AssertMessages.notNullParameter();
        return segment.getDistanceSquared(getClosestPointTo(segment));
    }

    /** Replies the minimum distance between this shape and the given multishape.
     *
     * @param multishape the multishape.
     * @return the minimum distance between the two shapes.
     */
    @Pure
    default double getDistanceSquared(MultiShape2ai<?, ?, ?, ?, ?, ?, ?> multishape) {
        assert multishape != null : AssertMessages.notNullParameter();
        double minDist = Double.POSITIVE_INFINITY;
        double dist;
        for (final Shape2ai<?, ?, ?, ?, ?, ?> shape : multishape) {
            dist = getDistanceSquared(shape);
            if (dist < minDist) {
                minDist = dist;
            }
        }
        return minDist;
    }

    /** Replies the minimum distance between this shape and the given path.
     *
     * @param path the path.
     * @return the minimum distance between the two shapes.
     */
    @Pure
    default double getDistanceSquared(Path2ai<?, ?, ?, ?, ?, ?> path) {
        assert path != null : AssertMessages.notNullParameter();
        return path.getDistanceSquared(getClosestPointTo(path));
    }

    @Pure
    @Unefficient
    @Override
    default P getClosestPointTo(Shape2D<?, ?, ?, ?, ?, ?> shape) {
        if (shape instanceof Circle2ai) {
            return getClosestPointTo((Circle2ai<?, ?, ?, ?, ?, ?>) shape);
        }
        if (shape instanceof MultiShape2ai) {
            return getClosestPointTo((MultiShape2ai<?, ?, ?, ?, ?, ?, ?>) shape);
        }
        if (shape instanceof Path2ai) {
            return getClosestPointTo((Path2ai<?, ?, ?, ?, ?, ?>) shape);
        }
        if (shape instanceof Rectangle2ai) {
            return getClosestPointTo((Rectangle2ai<?, ?, ?, ?, ?, ?>) shape);
        }
        if (shape instanceof Segment2ai) {
            return getClosestPointTo((Segment2ai<?, ?, ?, ?, ?, ?>) shape);
        }
        throw new IllegalArgumentException();
    }

    /** Replies the closest point on this shape to the given rectangle.
     *
     * @param rectangle the rectangle.
     * @return the closest point on this shape to the given shape; or the point
     *     if the point is in this shape.
     */
    @Pure
    P getClosestPointTo(Rectangle2ai<?, ?, ?, ?, ?, ?> rectangle);

    /** Replies the closest point on this shape to the given rectangle.
     *
     * @param circle the circle.
     * @return the closest point on this shape to the given shape; or the point
     *     if the point is in this shape.
     */
    @Pure
    P getClosestPointTo(Circle2ai<?, ?, ?, ?, ?, ?> circle);

    /** Replies the closest point on this shape to the given rectangle.
     *
     * @param segment the segment.
     * @return the closest point on this shape to the given shape; or the point
     *     if the point is in this shape.
     */
    @Pure
    P getClosestPointTo(Segment2ai<?, ?, ?, ?, ?, ?> segment);

    /** Replies the closest point on this shape to the given rectangle.
     *
     * @param multishape the multishape.
     * @return the closest point on this shape to the given shape; or the point
     *     if the point is in this shape.
     */
    @Pure
    default P getClosestPointTo(MultiShape2ai<?, ?, ?, ?, ?, ?, ?> multishape) {
        assert multishape != null : AssertMessages.notNullParameter();
        Shape2ai<?, ?, ?, ?, ?, ?> closest = null;
        double minDist = Double.POSITIVE_INFINITY;
        double dist;
        for (final Shape2ai<?, ?, ?, ?, ?, ?> shape : multishape) {
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

    /** Replies the closest point on this shape to the given rectangle.
     *
     * @param path the path.
     * @return the closest point on this shape to the given shape; or the point
     *     if the point is in this shape.
     */
    @Pure
    P getClosestPointTo(Path2ai<?, ?, ?, ?, ?, ?> path);

    @Override
    GeomFactory2ai<IE, P, V, B> getGeomFactory();

    @Pure
    @SuppressWarnings("unchecked")
    @Override
    default ST createTransformedShape(Transform2D transform) {
        if (transform == null || transform.isIdentity()) {
            return (ST) clone();
        }
        final PathIterator2ai<?> pi = getPathIterator(transform);
        final GeomFactory2ai<IE, P, V, B> factory = getGeomFactory();
        final Path2ai<?, ?, ?, P, V, ?> newPath = factory.newPath(pi.getWindingRule());
        while (pi.hasNext()) {
            final PathElement2ai e = pi.next();
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

}
