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

package org.arakhne.afc.math.geometry.d3.afp;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.CrossingComputationType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d3.OrientedPoint3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Shape3D;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** Fonctional interface representing a 2D oriented point on a plane.
 *
 * @param <ST> is the type of the general implementation.
 * @param <IT> is the type of the implementation of this shape.
 * @param <IE> is the type of the path elements.
 * @param <P> is the type of the points.
 * @param <V> is the type of the vectors.
 * @param <B> is the type of the bounding boxes.
 * @author $Author: tpiotrow$
 * @author $Author: sgalland$
 * @author $Author: olamotte$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface OrientedPoint3afp<
        ST extends Shape3afp<?, ?, IE, P, V, B>,
        IT extends OrientedPoint3afp<?, ?, IE, P, V, B>,
        IE extends PathElement3afp,
        P extends Point3D<? super P, ? super V>,
        V extends Vector3D<? super V, ? super P>,
        B extends RectangularPrism3afp<?, ?, IE, P, V, B>>
        extends Shape3afp<ST, IT, IE, P, V, B>, OrientedPoint3D<ST, IT, PathIterator3afp<IE>, P, V, B> {

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
    class OrientedPointPathIterator<T extends PathElement3afp> implements PathIterator3afp<T> {

        private int index;

        private OrientedPoint3afp<?, ?, T, ?, ?, ?> point;

        private Transform3D transform;

        private double px;

        private double py;

        private double dx;

        private double dy;

        /**
         * @param point the iterated oriented point.
         * @param transform the transformation, or <code>null</code>.
         */
        public OrientedPointPathIterator(OrientedPoint3afp<?, ?, T, ?, ?, ?> point, Transform3D transform) {
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
        public GeomFactory3afp<T, ?, ?, ?> getGeomFactory() {
            return this.point.getGeomFactory();
        }

        @Override
        public PathIterator3afp<T> restartIterations() {
            return new OrientedPointPathIterator<>(this.point, this.transform);
        }

    }

    @Override
    default PathIterator3afp<IE> getPathIterator(Transform3D transform) {
        return new OrientedPointPathIterator<>(this, transform);
    }

    @Override
    default boolean contains(double x, double y, double z) {
        return x == getX() && y == getY() && z == getZ();
    }

    @Override
    default boolean contains(RectangularPrism3afp<?, ?, ?, ?, ?, ?> rectangle) {
        // A rectangle might be reduced to a point, so that we have to check
        return getX() == rectangle.getMinX() && getX() == rectangle.getMaxX()
            && getY() == rectangle.getMinY() && getY() == rectangle.getMaxY()
            && getZ() == rectangle.getMinZ() && getZ() == rectangle.getMaxZ();
    }

    @Override
    default boolean contains(Shape3D<?, ?, ?, ?, ?, ?> shape) {
        return false;
    }

    @Override
    default boolean contains(Point3D<?, ?> pt) {
        return getX() == pt.getX() && getY() == pt.getY();
    }

    @Override
    default P getClosestPointTo(Sphere3afp<?, ?, ?, ?, ?, ?> circle) {
        return getPoint();
    }

    @Override
    default P getClosestPointTo(Path3afp<?, ?, ?, ?, ?, ?> path) {
        return getPoint();
    }

    @Override
    default P getClosestPointTo(RectangularPrism3afp<?, ?, ?, ?, ?, ?> rectangle) {
        return getPoint();
    }

    @Override
    default P getClosestPointTo(Segment3afp<?, ?, ?, ?, ?, ?> segment) {
        return getPoint();
    }

    @Override
    default P getClosestPointTo(MultiShape3afp<?, ?, ?, ?, ?, ?, ?> multishape) {
        return getPoint();
    }

    @Override
    default P getClosestPointTo(Shape3D<?, ?, ?, ?, ?, ?> shape) {
        return getPoint();
    }

    @Override
    default boolean intersects(Sphere3afp<?, ?, ?, ?, ?, ?> sphere) {
        assert sphere != null : AssertMessages.notNullParameter();
        return sphere.contains(getX(), getY(), getZ());
    }

    @Override
    default boolean intersects(MultiShape3afp<?, ?, ?, ?, ?, ?, ?> multishape) {
        assert multishape != null : AssertMessages.notNullParameter();
        return multishape.contains(getX(), getY(), getZ());
    }

    @Override
    default boolean intersects(PathIterator3afp<?> iterator) {
        assert iterator != null : AssertMessages.notNullParameter();
        final int mask = iterator.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
        final int crossings = Path3afp.computeCrossingsFromPoint(
                0,
                iterator,
                getX(), getY(), getZ(),
                CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
        return crossings == MathConstants.SHAPE_INTERSECTS
                || (crossings & mask) != 0;
    }

    @Override
    default boolean intersects(RectangularPrism3afp<?, ?, ?, ?, ?, ?> rectangle) {
        assert rectangle != null : AssertMessages.notNullParameter();
        return rectangle.contains(getX(), getY(), getZ());
    }

    @Override
    default boolean intersects(Segment3afp<?, ?, ?, ?, ?, ?> segment) {
        assert segment != null : AssertMessages.notNullParameter();
        return segment.contains(getX(), getY(), getZ());
    }

    @Override
    default void toBoundingBox(B box) {
        assert box != null : AssertMessages.notNullParameter();
        final double x1 = MathUtil.min(getX(), getDirectionX(), getNormalX(), getSwayX());
        final double y1 = MathUtil.min(getY(), getDirectionY(), getNormalY(), getSwayY());
        final double z1 = MathUtil.min(getY(), getDirectionZ(), getNormalZ(), getSwayZ());
        final double x2 = MathUtil.max(getX(), getDirectionX(), getNormalX(), getSwayX());
        final double y2 = MathUtil.max(getY(), getDirectionY(), getNormalY(), getSwayY());
        final double z2 = MathUtil.max(getZ(), getDirectionZ(), getNormalZ(), getSwayZ());
        box.setFromCorners(x1, y1, z1, x2, y2, z2);
    }

    /** Transform the current point.
     * This function changes the current point.
     *
     * @param transform is the affine transformation to apply.
     * @see #createTransformedShape
     */
    default void transforn(Transform3D transform) {
        assert transform != null : AssertMessages.notNullParameter();
        final Point3D<?, ?> p = new InnerComputationPoint3afp(getX(), getY(), getZ());
        transform.transform(p);
        final double x1 = p.getX();
        final double y1 = p.getY();
        final double z1 = p.getZ();
        p.set(getDirectionX(), getDirectionY(), getDirectionZ());
        transform.transform(p);
        set(x1, y1, z1, p.getX(), p.getY(), p.getZ());
    }

    @Override
    default void translate(double dx, double dy, double dz) {
        set(getX() + dx, getY() + dy, getZ() + dz, getDirectionX() + dx, getDirectionY() + dy, getDirectionZ() + dz);
    }

}
