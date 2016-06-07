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

package org.arakhne.afc.math.geometry.d2.afp;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.CrossingComputationType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Shape2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** A point 2D with two orientation vectors relative to the polyline: the direction and the normal to the point.
 *
 *  <p>The orientation vectors have no physical existence, i.e. they exist only to represent the direction of the
 *  point and its normal when the point is part of a polyline. The normal vector is always perpendicular to the
 *  direction vector..
 *
 * @param <ST> is the type of the general implementation.
 * @param <IT> is the type of the implementation of this shape.
 * @param <IE> is the type of the path elements.
 * @param <P> is the type of the points.
 * @param <V> is the type of the vectors.
 * @param <B> is the type of the bounding boxes.
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("unused")
// TODO : add geometrical length
public interface OrientedPoint2afp<
        ST extends Shape2afp<?, ?, IE, P, V, B>,
        IT extends OrientedPoint2afp<?, ?, IE, P, V, B>,
        IE extends PathElement2afp,
        P extends Point2D<? super P, ? super V>,
        V extends Vector2D<? super V, ? super P>,
        B extends Rectangle2afp<?, ?, IE, P, V, B>> extends Shape2afp<ST, IT, IE, P, V, B> {

    /** Iterator on the elements of the oriented points.
     * It replies : the point and the extremities of the
     * two orientation vectors.
     *
     * @param <T> the type of the path elements.
     * @author $Author: tpiotrow$
     * @version $FullVersion$
     * @mavengroupid $GroupId$
     * @mavenartifactid $ArtifactId$
     */
    class OrientedPointPathIterator<T extends PathElement2afp> implements PathIterator2afp<T> {

        private int index;

        private OrientedPoint2afp<?, ?, T, ?, ?, ?> point;

        private Transform2D transform;

        private double px;

        private double py;

        private double dx;

        private double dy;

        /**
         * @param point the iterated oriented point.
         * @param transform the transformation, or <code>null</code>.
         */
        public OrientedPointPathIterator(OrientedPoint2afp<?, ?, T, ?, ?, ?> point, Transform2D transform) {
            assert point != null : AssertMessages.notNullParameter();
            this.point = point;
            this.transform = transform == null || transform.isIdentity() ? null : transform;
            this.px = point.getX();
            this.py = point.getY();
            this.dx = point.getDirectionX();
            this.dy = point.getDirectionY();
        }

        @Override
        public PathWindingRule getWindingRule() {
            return PathWindingRule.NON_ZERO;
        }

        @Override
        public boolean isPolyline() {
            return false;
        }

        @Override
        public boolean isCurved() {
            return false;
        }

        @Override
        public boolean isMultiParts() {
            return true;
        }

        @Override
        public boolean isPolygon() {
            return false;
        }

        @Override
        public boolean hasNext() {
            return this.index <= 2;
        }

        @Override
        public T next() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public GeomFactory2afp<T, ?, ?, ?> getGeomFactory() {
            return this.point.getGeomFactory();
        }

        @Override
        public PathIterator2afp<T> restartIterations() {
            return new OrientedPointPathIterator<>(this.point, this.transform);
        }

    }

    @Override
    default PathIterator2afp<IE> getPathIterator(Transform2D transform) {
        return new OrientedPointPathIterator<>(this, transform);
    }

    /** Replies the X coordinate of the point.
     *
     * @return the x coordinate of the point.
     */
    @Pure double getX();

    /** Replies the Y coordinate of the point.
     *
     * @return the y coordinate of the point.
     */
    @Pure double getY();

    /** Sets a new value in the X of the point.
     *
     * @param x the new value double x.
     */
    void setX(double x);

    /**  Sets a new value in the Y of the point.
     * @param y the new value double y.
     */
    void setY(double y);

    /** Replies the X coordinate of the direction vector.
     * If this point is not part of a polyline, the direction vector is null.
     *
     * @return the x coordinate of the direction vector.
     */
    @Pure double getDirectionX();

    /** Replies the Y coordinate of the direction vector.
     * If this point is not part of a polyline, the direction vector is null.
     *
     * @return the y coordinate of the direction vector.
     */
    @Pure double getDirectionY();

    /** Sets a new value in the X direction of the point.
     *
     * @param x the new value double x.
     */
    void setDirectionX(double x);

    /**  Sets a new value in the Y direction of the point.
     * @param y the new value double y.
     */
    void setDirectionY(double y);

    /** Replies the X coordinate of the normal vector.
     *  If this point is not part of a polyline, the normal vector is null.
     *
     * @return the x coordinate of the normal vector.
     */
    @Pure default double getNormalX() {
        return -getDirectionY();
    }

    /** Replies the Y coordinate of the normal vector.
     *  If this point is not part of a polyline, the normal vector is null.
     *
     * @return the y coordinate of the normal vector.
     */
    @Pure default double getNormalY() {
        return getDirectionX();
    }

    @Override
    default boolean contains(double x, double y) {
        return x == getX() && y == getY();
    }

    @Override
    default boolean contains(Rectangle2afp<?, ?, ?, ?, ?, ?> rectangle) {
        // A rectangle might be reduced to a point, so that we have to check
        return getX() == rectangle.getMinX() && getX() == rectangle.getMaxX()
            && getY() == rectangle.getMinY() && getY() == rectangle.getMaxY();
    }

    @Override
    default boolean contains(Shape2D<?, ?, ?, ?, ?, ?> shape) {
        return false;
    }

    @Override
    default boolean contains(Point2D<?, ?> pt) {
        return getX() == pt.getX() && getY() == pt.getY();
    }

    @Override
    default boolean equalsToShape(IT shape) {
        if (shape == null) {
            return false;
        }
        if (shape == this) {
            return true;
        }
        // We don't need to check normal because it depends of direction
        return getX() == shape.getX() && getY() == shape.getY()
                && getDirectionX() == shape.getDirectionX()
                && getDirectionY() == shape.getDirectionY();
    }

    /** Replies this point.
     * @return this point
     */
    default P getPoint() {
        return getGeomFactory().newPoint(getX(), getY());
    }

    @Override
    default P getClosestPointTo(Circle2afp<?, ?, ?, ?, ?, ?> circle) {
        return getPoint();
    }

    @Override
    default P getClosestPointTo(Ellipse2afp<?, ?, ?, ?, ?, ?> ellipse) {
        return getPoint();
    }

    @Override
    default P getClosestPointTo(OrientedRectangle2afp<?, ?, ?, ?, ?, ?> orientedRectangle) {
        return getPoint();
    }

    @Override
    default P getClosestPointTo(Parallelogram2afp<?, ?, ?, ?, ?, ?> parallelogram) {
        return getPoint();
    }

    @Override
    default P getClosestPointTo(Path2afp<?, ?, ?, ?, ?, ?> path) {
        return getPoint();
    }

    @Override
    default P getClosestPointTo(Point2D<?, ?> point) {
        return getPoint();
    }

    @Override
    default P getClosestPointTo(Rectangle2afp<?, ?, ?, ?, ?, ?> rectangle) {
        return getPoint();
    }

    @Override
    default P getClosestPointTo(RoundRectangle2afp<?, ?, ?, ?, ?, ?> roundRectangle) {
        return getPoint();
    }

    @Override
    default P getClosestPointTo(Segment2afp<?, ?, ?, ?, ?, ?> segment) {
        return getPoint();
    }

    @Override
    default P getClosestPointTo(Triangle2afp<?, ?, ?, ?, ?, ?> triangle) {
        return getPoint();
    }

    @Override
    default P getClosestPointTo(Shape2D<?, ?, ?, ?, ?, ?> shape) {
        return getPoint();
    }

    @Override
    default double getDistanceL1(Point2D<?, ?> pt) {
        assert pt != null : AssertMessages.notNullParameter();
        return Point2D.getDistanceL1PointPoint(getX(), getY(), pt.getX(), pt.getY());
    }

    @Override
    default double getDistanceLinf(Point2D<?, ?> pt) {
        assert pt != null : AssertMessages.notNullParameter();
        return Point2D.getDistanceLinfPointPoint(getX(), getY(), pt.getX(), pt.getY());
    }

    @Override
    default double getDistanceSquared(Point2D<?, ?> pt) {
        assert pt != null : AssertMessages.notNullParameter();
        return Point2D.getDistanceSquaredPointPoint(getX(), getY(), pt.getX(), pt.getY());
    }

    @Override
    default double getDistance(Point2D<?, ?> pt) {
        assert pt != null : AssertMessages.notNullParameter();
        return Point2D.getDistancePointPoint(getX(), getY(), pt.getX(), pt.getY());
    }

    @Override
    default P getFarthestPointTo(Point2D<?, ?> point) {
        return getPoint();
    }

    @Override
    default boolean intersects(Circle2afp<?, ?, ?, ?, ?, ?> circle) {
        assert circle != null : AssertMessages.notNullParameter();
        return circle.contains(getX(), getY());
    }

    @Override
    default boolean intersects(Ellipse2afp<?, ?, ?, ?, ?, ?> ellipse) {
        assert ellipse != null : AssertMessages.notNullParameter();
        return ellipse.contains(getX(), getY());
    }

    @Override
    default boolean intersects(MultiShape2afp<?, ?, ?, ?, ?, ?, ?> multishape) {
        assert multishape != null : AssertMessages.notNullParameter();
        return multishape.contains(getX(), getY());
    }

    @Override
    default boolean intersects(OrientedRectangle2afp<?, ?, ?, ?, ?, ?> orientedRectangle) {
        assert orientedRectangle != null : AssertMessages.notNullParameter();
        return orientedRectangle.contains(getX(), getY());
    }

    @Override
    default boolean intersects(Parallelogram2afp<?, ?, ?, ?, ?, ?> parallelogram) {
        assert parallelogram != null : AssertMessages.notNullParameter();
        return parallelogram.contains(getX(), getY());
    }

    @Override
    default boolean intersects(PathIterator2afp<?> iterator) {
        assert iterator != null : AssertMessages.notNullParameter();
        final int mask = iterator.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
        final int crossings = Path2afp.computeCrossingsFromPoint(
                0,
                iterator,
                getX(), getY(),
                CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
        return crossings == MathConstants.SHAPE_INTERSECTS
                || (crossings & mask) != 0;
    }

    @Override
    default boolean intersects(Rectangle2afp<?, ?, ?, ?, ?, ?> rectangle) {
        assert rectangle != null : AssertMessages.notNullParameter();
        return rectangle.contains(getX(), getY());
    }

    @Override
    default boolean intersects(RoundRectangle2afp<?, ?, ?, ?, ?, ?> roundRectangle) {
        assert roundRectangle != null : AssertMessages.notNullParameter();
        return roundRectangle.contains(getX(), getY());
    }

    @Override
    default boolean intersects(Segment2afp<?, ?, ?, ?, ?, ?> segment) {
        assert segment != null : AssertMessages.notNullParameter();
        return segment.contains(getX(), getY());
    }

    @Override
    default boolean intersects(Triangle2afp<?, ?, ?, ?, ?, ?> triangle) {
        assert triangle != null : AssertMessages.notNullParameter();
        return triangle.contains(getX(), getY());
    }

    @Override
    default boolean isEmpty() {
        return false;
    }

    /** Change the point.
     *
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     */
    // No default implementation to ensure atomic change
    void set(double x, double y);

    /** Change the point and its orientation vector.
     *
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @param dirX x coordinate of the vector.
     * @param dirY y coordinate of the vector.
     */
    // No default implementation to ensure atomic change
    void set(double x, double y, double dirX, double dirY);

    @Override
    default void set(IT shape) {
        assert shape != null : AssertMessages.notNullParameter();
        set(shape.getX(), shape.getY(), shape.getDirectionX(), shape.getDirectionY());
    }

    @Override
    default void toBoundingBox(B box) {
        assert box != null : AssertMessages.notNullParameter();
        final double x1 = MathUtil.min(getX(), getDirectionX(), getNormalX());
        final double y1 = MathUtil.min(getY(), getDirectionY(), getNormalY());
        final double x2 = MathUtil.max(getX(), getDirectionX(), getNormalX());
        final double y2 = MathUtil.max(getY(), getDirectionY(), getNormalY());
        box.setFromCorners(x1, y1, x2, y2);
    }

    /** Transform the current point.
     * This function changes the current point.
     *
     * @param transform is the affine transformation to apply.
     * @see #createTransformedShape
     */
    default void transforn(Transform2D transform) {
        assert transform != null : AssertMessages.notNullParameter();
        final Point2D<?, ?> p = new InnerComputationPoint2afp(getX(), getY());
        transform.transform(p);
        final double x1 = p.getX();
        final double y1 = p.getY();
        p.set(getDirectionX(), getDirectionY());
        transform.transform(p);
        set(x1, y1, p.getX(), p.getY());
    }

    @Override
    default void translate(double dx, double dy) {
        set(getX() + dx, getY() + dy, getDirectionX() + dx, getDirectionY() + dy);
    }

    @Override
    default void clear() {
        set(0, 0, 0, 0);
    }
}
