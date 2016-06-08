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
import java.util.NoSuchElementException;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.CrossingComputationType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.OrientedPoint2D;
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
public interface OrientedPoint2ai<
        ST extends Shape2ai<?, ?, IE, P, V, B>,
        IT extends OrientedPoint2ai<?, ?, IE, P, V, B>,
        IE extends PathElement2ai,
        P extends Point2D<? super P, ? super V>,
        V extends Vector2D<? super V, ? super P>,
        B extends Rectangle2ai<?, ?, IE, P, V, B>>
        extends Shape2ai<ST, IT, IE, P, V, B>, OrientedPoint2D<ST, IT, PathIterator2ai<IE>, P, V, B> {
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
    // TODO : complete point iterator. The iterator may return only the point or the point and its
    // orientation vectors. As such, it may or may not contain multiple moveto elements.
    class OrientedPointPathIterator<T extends PathElement2ai> implements PathIterator2ai<T> {

        private int index;

        private OrientedPoint2ai<?, ?, T, ?, ?, ?> point;

        private Transform2D transform;

        private int px;

        private int py;

        private int dx;

        private int dy;

        /**
         * @param point the iterated oriented point.
         * @param transform the transformation, or <code>null</code>.
         */
        public OrientedPointPathIterator(OrientedPoint2ai<?, ?, T, ?, ?, ?> point, Transform2D transform) {
            assert point != null : AssertMessages.notNullParameter();
            this.point = point;
            this.transform = transform == null || transform.isIdentity() ? null : transform;
            this.px = point.ix();
            this.py = point.iy();
            this.dx = point.idx();
            this.dy = point.idy();
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
        public GeomFactory2ai<T, ?, ?, ?> getGeomFactory() {
            return this.point.getGeomFactory();
        }

        @Override
        public PathIterator2ai<T> restartIterations() {
            return new OrientedPointPathIterator<>(this.point, this.transform);
        }

    }

    /** Iterator on the points of this oriented point, which replies the point itself
     * and the extremities of the orientation vectors.
     *
     * @param <P> the type of the points.
     * @param <V> the type of the vectors.
     * @author $Author: tpiotrow$
     * @version $FullVersion$
     * @mavengroupid $GroupId$
     * @mavenartifactid $ArtifactId$
     */
    class OrientedPointPointIterator<P extends Point2D<? super P, ? super V>, V extends Vector2D<? super V, ? super P>>
            implements Iterator<P> {
        private int index;

        private OrientedPoint2ai<?, ?, ?, P, V, ?> point;

        /**
         * @param pt the oriented point to iterate on.
         */
        public OrientedPointPointIterator(OrientedPoint2ai<?, ?, ?, P, V, ?> pt) {
            assert pt != null : AssertMessages.notNullParameter();
            this.point = pt;
        }

        @Override
        public boolean hasNext() {
            return this.index <= 2;
        }

        @Override
        public P next() {
            switch (this.index++) {
            case 0:
                return this.point.getGeomFactory().newPoint(this.point.ix(), this.point.iy());
            case 1:
                return this.point.getGeomFactory().newPoint(this.point.idx(), this.point.idy());
            case 2:
                return this.point.getGeomFactory().newPoint(this.point.inx(), this.point.iny());
            default:
                throw new NoSuchElementException();
            }
        }

    }

    @Override
    default PathIterator2ai<IE> getPathIterator(Transform2D transform) {
        return new OrientedPointPathIterator<>(this, transform);
    }

    @Override
    default Iterator<P> getPointIterator() {
        return new OrientedPointPointIterator<>(this);
    }

    @Override
    default boolean contains(int x, int y) {
        return x == getX() && y == getY();
    }

    @Override
    default boolean contains(Rectangle2ai<?, ?, ?, ?, ?, ?> rectangle) {
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
    default P getClosestPointTo(Circle2ai<?, ?, ?, ?, ?, ?> circle) {
        return getPoint();
    }

    @Override
    default P getClosestPointTo(Path2ai<?, ?, ?, ?, ?, ?> path) {
        return getPoint();
    }

    @Override
    default P getClosestPointTo(Rectangle2ai<?, ?, ?, ?, ?, ?> rectangle) {
        return getPoint();
    }

    @Override
    default P getClosestPointTo(Segment2ai<?, ?, ?, ?, ?, ?> segment) {
        return getPoint();
    }

    @Override
    default P getClosestPointTo(Shape2D<?, ?, ?, ?, ?, ?> shape) {
        return getPoint();
    }

    @Override
    default P getClosestPointTo(MultiShape2ai<?, ?, ?, ?, ?, ?, ?> multishape) {
        return getPoint();
    }

    @Override
    default boolean intersects(Circle2ai<?, ?, ?, ?, ?, ?> circle) {
        assert circle != null : AssertMessages.notNullParameter();
        return circle.contains(ix(), iy());
    }

    @Override
    default boolean intersects(MultiShape2ai<?, ?, ?, ?, ?, ?, ?> multishape) {
        assert multishape != null : AssertMessages.notNullParameter();
        return multishape.contains(ix(), iy());
    }

    @Override
    default boolean intersects(PathIterator2ai<?> iterator) {
        assert iterator != null : AssertMessages.notNullParameter();
        final int mask = iterator.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
        final int crossings = Path2ai.computeCrossingsFromPoint(
                0,
                iterator,
                ix(), iy(),
                CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
        return crossings == MathConstants.SHAPE_INTERSECTS
                || (crossings & mask) != 0;
    }

    @Override
    default boolean intersects(Rectangle2ai<?, ?, ?, ?, ?, ?> rectangle) {
        assert rectangle != null : AssertMessages.notNullParameter();
        return rectangle.contains(ix(), iy());
    }

    @Override
    default boolean intersects(Segment2ai<?, ?, ?, ?, ?, ?> segment) {
        assert segment != null : AssertMessages.notNullParameter();
        return segment.contains(ix(), iy());
    }

    @Override
    default void toBoundingBox(B box) {
        assert box != null : AssertMessages.notNullParameter();
        final int x1 = MathUtil.min(ix(), idx(), inx());
        final int y1 = MathUtil.min(iy(), idy(), iny());
        final int x2 = MathUtil.max(ix(), idx(), inx());
        final int y2 = MathUtil.max(iy(), idy(), iny());
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
        final Point2D<?, ?> p = new InnerComputationPoint2ai(ix(), iy());
        transform.transform(p);
        set(p.ix(), p.iy());
    }

    @Override
    default void translate(int dx, int dy) {
        set(ix() + dx, iy() + dy, idx() + dx, idy() + dy);
    }
}
