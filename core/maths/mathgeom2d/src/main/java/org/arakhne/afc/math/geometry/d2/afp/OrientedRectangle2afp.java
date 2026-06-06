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

import java.util.NoSuchElementException;

import org.arakhne.afc.math.GeogebraUtil;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.Unefficient;
import org.arakhne.afc.math.geometry.base.CrossingComputationType;
import org.arakhne.afc.math.geometry.base.GeomConstants;
import org.arakhne.afc.math.geometry.base.PathWindingRule;
import org.arakhne.afc.math.geometry.base.d2.InnerComputationPoint2D;
import org.arakhne.afc.math.geometry.base.d2.InnerComputationVector2D;
import org.arakhne.afc.math.geometry.base.d2.Point2D;
import org.arakhne.afc.math.geometry.base.d2.Transform2D;
import org.arakhne.afc.math.geometry.base.d2.Tuple2D;
import org.arakhne.afc.math.geometry.base.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.general.Shape2DType;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.eclipse.xtext.xbase.lib.Pure;

/** Functional interface that represented a 2D oriented rectangle on a plane.
 * An oriented rectangle is a parallelogram with orthogonal axes.
 *
 * @param <ST> is the type of the general implementation.
 * @param <IT> is the type of the implementation of this shape.
 * @param <IE> is the type of the path elements.
 * @param <P> is the type of the points.
 * @param <V> is the type of the vectors.
 * @param <B> is the type of the bounding boxes.
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("checkstyle:magicnumber")
public interface OrientedRectangle2afp<
        ST extends Shape2afp<?, ?, IE, P, V, B>,
        IT extends OrientedRectangle2afp<?, ?, IE, P, V, B>,
        IE extends PathElement2afp,
        P extends Point2D<? super P, ? super V>,
        V extends Vector2D<? super V, ? super P>,
        B extends Rectangle2afp<?, ?, IE, P, V, B>>
        extends Parallelogram2afp<ST, IT, IE, P, V, B> {

	@Override
	default Shape2DType getType() {
		return Shape2DType.ORIENTED_RECTANGLE;
	}

    /** Project the given vector on the R axis, assuming S axis is orthogonal.
     *
     * <p>This function assumes that axes are orthogonal. For a general projection on the R axis,
     * see {@link Parallelogram2afp#findsVectorProjectionRAxisPoint(double, double, double, double, double, double)}.
     *
     * @param rx the x coordinate of the R axis.
     * @param ry the y coordinate of the R axis.
     * @param x the x coordinate of the vector.
     * @param y the y coordinate of the vector.
     * @return the coordinate of the projection of the vector on R
     * @see Parallelogram2afp#findsVectorProjectionRAxisPoint(double, double, double, double, double, double)
     */
    @Pure
    static double findsVectorProjectionRAxisVector(double rx, double ry, double x,  double y) {
        assert Vector2D.isUnitVector(rx, ry) : AssertMessages.normalizedParameters(0, 1);
        return Vector2D.dotProduct(x, y, rx, ry);
    }

    /** Project the given vector on the S axis, assuming R axis is orthogonal.
     *
     * <p>This function assumes that axes are orthogonal. For a general projection on the S axis,
     * see {@link Parallelogram2afp#findsVectorProjectionSAxisVector(double, double, double, double, double, double)}.
     *
     * @param rx the x coordinate of the R axis (NOT the S axis).
     * @param ry the y coordinate of the R axis (NOT the S axis).
     * @param x the x coordinate of the vector.
     * @param y the y coordinate of the vector.
     * @return the coordinate of the projection of the vector on S
     * @see Parallelogram2afp#findsVectorProjectionSAxisVector(double, double, double, double, double, double)
     */
    @Pure
    static double findsVectorProjectionSAxisVector(double rx, double ry, double x,  double y) {
        assert Vector2D.isUnitVector(rx, ry) : AssertMessages.normalizedParameters(0, 1);
        return Vector2D.dotProduct(x, y, -ry, rx);
    }

    /**
     * Compute the center point and axis extents of an oriented rectangle from a set of points and
     * the oriented rectangle axes.
     *
     * <p>This function assumes orthogonal axes, in opposite to
     * {@link Parallelogram2afp#calculatesCenterPointAxisExtents(Iterable, Vector2D, Vector2D, Point2D, Tuple2D)}, which
     * assumes not constraint on the axes.
     *
     * @param points is the list of the points enclosed by the oriented rectangle.
     * @param raxis is the R axis of the oriented rectangle.
     * @param center is the point which is set with the parallogram's center coordinates.
     * @param extents are the extents of the parallelogram for the R and S axis.
     * @see "MGPCG pages 222-223 (oriented bounding box)"
     * @see Parallelogram2afp#calculatesCenterPointAxisExtents(Iterable, Vector2D, Vector2D, Point2D, Tuple2D)
     */
    static void calculatesCenterPointAxisExtents(
            Iterable<? extends Point2D<?, ?>> points,
                    Vector2D<?, ?> raxis,
                    Point2D<?, ?> center, Tuple2D<?> extents) {
        assert points != null : AssertMessages.notNullParameter(0);
        assert raxis != null : AssertMessages.notNullParameter(1);
        assert raxis.isUnitVector() : AssertMessages.normalizedParameter(1);
        assert center != null : AssertMessages.notNullParameter(2);
        assert extents != null : AssertMessages.notNullParameter(3);

        var minR = Double.POSITIVE_INFINITY;
        var maxR = Double.NEGATIVE_INFINITY;
        var minS = Double.POSITIVE_INFINITY;
        var maxS = Double.NEGATIVE_INFINITY;

        final var ux = raxis.getX();
        final var uy = raxis.getY();
        final var vx = -uy;
        final var vy = ux;

        for (final var tuple : points) {
            final var pdotr = findsVectorProjectionRAxisVector(ux, uy, tuple.getX(), tuple.getY());
            final var pdots = findsVectorProjectionSAxisVector(ux, uy, tuple.getX(), tuple.getY());

            if (pdotr < minR) {
                minR = pdotr;
            }
            if (pdotr > maxR) {
                maxR = pdotr;
            }
            if (pdots < minS) {
                minS = pdots;
            }
            if (pdots > maxS) {
                maxS = pdots;
            }
        }

        final var a = (maxR + minR) / 2.;
        final var b = (maxS + minS) / 2.;

        // Set the center of the oriented rectangle
        center.set(
                a * ux
                + b * vx,

                a * uy
                + b * vy);

        // Compute extents
        extents.set(
                (maxR - minR) / 2.,
                (maxS - minS) / 2.);
    }

    /** Replies if a point is inside in the oriented rectangle.
     *
     * @param px
     *            is the X coordinate of the point to test.
     * @param py
     *            is the Y coordinate of the point to test.
     * @param centerX
     *            is the X coordinate of the oriented rectangle center.
     * @param centerY
     *            is the Y coordinate of the oriented rectangle center.
     * @param axis1X
     *            is the X coordinate of the axis 1 unit vector.
     * @param axis1Y
     *            is the Y coordinate of the axis 1 unit vector.
     * @param axis1Extent
     *            is the extent of the axis 1 of the oriented rectangle.
     * @param axis2Extent
     *            is the extent of the axis 2 of the oriented rectangle.
     * @return {@code true} if the given point is inside the oriented rectangle;
     *     otherwise {@code false}.
     */
    @Pure
    static boolean containsOrientedRectanglePoint(
            double centerX, double centerY,
            double axis1X, double axis1Y,
            double axis1Extent,
            double axis2Extent,
            double px, double py) {
        assert axis1Extent >= 0 : AssertMessages.positiveOrZeroParameter(4);
        assert axis2Extent >= 0 : AssertMessages.positiveOrZeroParameter(5);
        assert Vector2D.isUnitVector(axis1X, axis1Y) : AssertMessages.normalizedParameters(2, 3);
        final var x = px - centerX;
        final var y = py - centerY;
        var coordinate = findsVectorProjectionRAxisVector(axis1X, axis1Y, x, y);
        if (coordinate < -axis1Extent || coordinate > axis1Extent) {
            return false;
        }
        coordinate = findsVectorProjectionSAxisVector(axis1X, axis1Y, x, y);
        return coordinate >= -axis2Extent && coordinate <= axis2Extent;
    }

    /** Replies if a point is inside the oriented rectangle.
     *
     * @param centerX
     *            is the X coordinate of the oriented rectangle center.
     * @param centerY
     *            is the Y coordinate of the oriented rectangle center.
     * @param axis1X
     *            is the X coordinate of the axis 1 unit vector.
     * @param axis1Y
     *            is the Y coordinate of the axis 1 unit vector.
     * @param axis1Extent
     *            is the extent of the axis 1 of the oriented rectangle.
     * @param axis2Extent
     *            is the extent of the axis 2 of the oriented rectangle.
     * @param rx
     *            is the X coordinate of the lower point of the rectangle.
     * @param ry
     *            is the Y coordinate of the lower point of the rectangle.
     * @param rwidth
     *            is the width of the rectangle.
     * @param rheight
     *            is the height of the rectangle.
     * @return {@code true} if the given rectangle is inside the oriented rectangle;
     *     otherwise {@code false}.
     */
    @Pure
    @Unefficient
    @SuppressWarnings({"checkstyle:npathcomplexity", "checkstyle:parameternumber"})
    static boolean containsOrientedRectangleRectangle(
            double centerX, double centerY,
            double axis1X, double axis1Y,
            double axis1Extent,
            double axis2Extent,
            double rx, double ry,
            double rwidth, double rheight) {
        assert axis1Extent >= 0 : AssertMessages.positiveOrZeroParameter(4);
        assert axis2Extent >= 0 : AssertMessages.positiveOrZeroParameter(5);
        assert Vector2D.isUnitVector(axis1X, axis1Y) : AssertMessages.normalizedParameters(2, 3);
        assert rwidth >= 0 : AssertMessages.positiveOrZeroParameter(8);
        assert rheight >= 0 : AssertMessages.positiveOrZeroParameter(9);

        final var basex = rx - centerX;
        final var basey = ry - centerY;

        var x = basex;
        var y = basey;
        var coordinate = findsVectorProjectionRAxisVector(axis1X, axis1Y, x, y);
        if (coordinate < -axis1Extent || coordinate > axis1Extent) {
            return false;
        }
        coordinate = findsVectorProjectionSAxisVector(axis1X, axis1Y, x, y);
        if (coordinate < -axis2Extent || coordinate > axis2Extent) {
            return false;
        }

        x = basex + rwidth;
        coordinate = findsVectorProjectionRAxisVector(axis1X, axis1Y, x, y);
        if (coordinate < -axis1Extent || coordinate > axis1Extent) {
            return false;
        }
        coordinate = findsVectorProjectionSAxisVector(axis1X, axis1Y, x, y);
        if (coordinate < -axis2Extent || coordinate > axis2Extent) {
            return false;
        }

        y = basey + rheight;
        coordinate = findsVectorProjectionRAxisVector(axis1X, axis1Y, x, y);
        if (coordinate < -axis1Extent || coordinate > axis1Extent) {
            return false;
        }
        coordinate = findsVectorProjectionSAxisVector(axis1X, axis1Y, x, y);
        if (coordinate < -axis2Extent || coordinate > axis2Extent) {
            return false;
        }

        x = basex;
        coordinate = findsVectorProjectionRAxisVector(axis1X, axis1Y, x, y);
        if (coordinate < -axis1Extent || coordinate > axis1Extent) {
            return false;
        }
        coordinate = findsVectorProjectionSAxisVector(axis1X, axis1Y, x, y);
        return coordinate >= -axis2Extent && coordinate <= axis2Extent;
    }

    /**
    }
     * Given a point p, this function computes the point q1 on (or in) this oriented rectangle,
     * closest to p; and the point q2 on the oriented rectangle, farthest to p. If there are several
     * points, the function will return one of those. Remember this function may
     * return an approximate result when points remain on oriented rectangle plane of symmetry.
     *
     * @param px
     *            is the X coordinate of the point to test.
     * @param py
     *            is the Y coordinate of the point to test.
     * @param centerX
     *            is the X coordinate of the oriented rectangle center.
     * @param centerY
     *            is the Y coordinate of the oriented rectangle center.
     * @param axis1X
     *            is the X coordinate of the axis 1 vector.
     * @param axis1Y
     *            is the Y coordinate of the axis 1 vector.
     * @param axis1Extent
     *            is the extent of the axis 1 of the oriented rectangle.
     * @param axis2Extent
     *            is the extent of the axis 2 of the oriented rectangle.
     * @param closest the closest point. If {@code null}, the closest point is not computed.
     * @param farthest the farthest point. If {@code null}, the farthest point is not computed.
     */
    @SuppressWarnings({"checkstyle:npathcomplexity", "checkstyle:parameternumber"})
    static void findsClosestFarthestPointsPointOrientedRectangle(
            double px, double py,
            double centerX, double centerY,
            double axis1X, double axis1Y,
            double axis1Extent,
            double axis2Extent,
            Point2D<?, ?> closest, Point2D<?, ?> farthest) {
        assert axis1Extent >= 0. : AssertMessages.positiveOrZeroParameter(6);
        assert axis2Extent >= 0. : AssertMessages.positiveOrZeroParameter(7);
        assert Vector2D.isUnitVector(axis1X, axis1Y) : AssertMessages.normalizedParameters(4, 5);
        assert closest != null || farthest != null : AssertMessages.oneNotNullParameter(8, 9);

        final var dx = px - centerX;
        final var dy = py - centerY;

        // For each axis project d onto that axis to get the distance along
        // the axis of d from the parallelogram center
        var d1 = findsVectorProjectionRAxisVector(axis1X, axis1Y, dx, dy);
        var d2 = findsVectorProjectionSAxisVector(axis1X, axis1Y, dx, dy);

        if (closest != null) {
            final var clampedD1 = MathUtil.clamp(d1, -axis1Extent, axis1Extent);
            final var clampedD2 = MathUtil.clamp(d2, -axis2Extent, axis2Extent);

            // Step that distance along the axis to get world coordinate
            // q1 += dist * this.axis[i]; (If distance farther than the box
            // extents, clamp to the box)
            closest.set(
                    centerX + clampedD1 * axis1X - clampedD2 * axis1Y,
                    centerY + clampedD1 * axis1Y + clampedD2 * axis1X);
        }

        if (farthest != null) {
            // Clamp to the other side of the box
            if (d1 >= 0.) {
                d1 = -axis1Extent;
            } else {
                d1 = axis1Extent;
            }
            if (d2 >= 0.) {
                d2 = -axis2Extent;
            } else {
                d2 = axis2Extent;
            }

            // Step that distance along the axis to get world coordinate
            // q2 += dist * this.axis[i];
            farthest.set(
                    centerX + d1 * axis1X - d2 * axis1Y,
                    centerY + d1 * axis1Y + d2 * axis1X);

        }
    }

    /** Replies if the specified rectangle intersects the specified segment.
     *
     * @param centerX
     *            is the X coordinate of the oriented rectangle center.
     * @param centerY
     *            is the Y coordinate of the oriented rectangle center.
     * @param axis1X
     *            is the X coordinate of the axis 1 unit vector.
     * @param axis1Y
     *            is the Y coordinate of the axis 1 unit vector.
     * @param axis1Extent
     *            is the extent of the axis 1 of the oriented rectangle.
     * @param axis2Extent
     *            is the extent of the axis 2 of the oriented rectangle.
     * @param s1x is the X coordinate of the first point of the segment.
     * @param s1y is the Y coordinate of the first point of the segment.
     * @param s2x is the X coordinate of the second point of the segment.
     * @param s2y is the Y coordinate of the second point of the segment.
     * @return {@code true} if intersecting, otherwise {@code false}
     */
    @Pure
    @SuppressWarnings({"checkstyle:npathcomplexity", "checkstyle:parameternumber"})
    static boolean intersectsOrientedRectangleSegment(
            double centerX, double centerY,
            double axis1X, double axis1Y,
            double axis1Extent,
            double axis2Extent,
            double s1x, double s1y, double s2x, double s2y) {
        assert axis1Extent >= 0. : AssertMessages.positiveOrZeroParameter(4);
        assert axis2Extent >= 0. : AssertMessages.positiveOrZeroParameter(5);
        assert Vector2D.isUnitVector(axis1X, axis1Y) : AssertMessages.normalizedParameters(2, 3);
        //Changing Segment coordinate basis.
        var x = s1x - centerX;
        var y = s1y - centerY;
        final var ax = findsVectorProjectionRAxisVector(axis1X, axis1Y, x, y);
        final var ay = findsVectorProjectionSAxisVector(axis1X, axis1Y, x, y);
        x = s2x - centerX;
        y = s2y - centerY;
        final var bx = findsVectorProjectionRAxisVector(axis1X, axis1Y, x, y);
        final var by = findsVectorProjectionSAxisVector(axis1X, axis1Y, x, y);

        return Rectangle2afp.intersectsRectangleSegment(
                -axis1Extent, -axis2Extent, axis1Extent, axis2Extent,
                ax, ay,  bx,  by);
    }

    /** Replies if the specified rectangle intersects the specified triangle.
     *
     * @param centerX
     *            is the X coordinate of the oriented rectangle center.
     * @param centerY
     *            is the Y coordinate of the oriented rectangle center.
     * @param axis1X
     *            is the X coordinate of the axis 1 unit vector.
     * @param axis1Y
     *            is the Y coordinate of the axis 1 unit vector.
     * @param axis1Extent
     *            is the extent of the axis 1 of the oriented rectangle.
     * @param axis2Extent
     *            is the extent of the axis 2 of the oriented rectangle.
     * @param s1x is the X coordinate of the first point of the triangle.
     * @param s1y is the Y coordinate of the first point of the triangle.
     * @param s2x is the X coordinate of the second point of the triangle.
     * @param s2y is the Y coordinate of the second point of the triangle.
     * @param s3x is the X coordinate of the third point of the triangle.
     * @param s3y is the Y coordinate of the third point of the triangle.
     * @return {@code true} if intersecting, otherwise {@code false}
     */
    @Pure
    @SuppressWarnings({"checkstyle:npathcomplexity", "checkstyle:parameternumber"})
    static boolean intersectsOrientedRectangleTriangle(
            double centerX, double centerY,
            double axis1X, double axis1Y,
            double axis1Extent,
            double axis2Extent,
            double s1x, double s1y, double s2x, double s2y, double s3x, double s3y) {
        assert axis1Extent >= 0. : AssertMessages.positiveOrZeroParameter(4);
        assert axis2Extent >= 0. : AssertMessages.positiveOrZeroParameter(5);
        assert Vector2D.isUnitVector(axis1X, axis1Y) : AssertMessages.normalizedParameters(2, 3);
        //Changing Triangle coordinate basis.
        var x = s1x - centerX;
        var y = s1y - centerY;
        final var ax = findsVectorProjectionRAxisVector(axis1X, axis1Y, x, y);
        final var ay = findsVectorProjectionSAxisVector(axis1X, axis1Y, x, y);
        x = s2x - centerX;
        y = s2y - centerY;
        final var bx = findsVectorProjectionRAxisVector(axis1X, axis1Y, x, y);
        final var by = findsVectorProjectionSAxisVector(axis1X, axis1Y, x, y);
        x = s3x - centerX;
        y = s3y - centerY;
        final var cx = findsVectorProjectionRAxisVector(axis1X, axis1Y, x, y);
        final var cy = findsVectorProjectionSAxisVector(axis1X, axis1Y, x, y);

        return Triangle2afp.intersectsTriangleRectangle(
                ax, ay,  bx,  by, cx, cy,
                -axis1Extent, -axis2Extent, 2. * axis1Extent, 2. * axis2Extent);
    }

    /** Replies if the parallelogram intersects the given ellipse.
     *
     * @param centerX
     *            is the X coordinate of the parallelogram center.
     * @param centerY
     *            is the Y coordinate of the parallelogram center.
     * @param axis1X
     *            is the X coordinate of the axis 1 unit vector.
     * @param axis1Y
     *            is the Y coordinate of the axis 1 unit vector.
     * @param axis1Extent
     *            is the extent of the axis 1 of the parallelogram.
     * @param axis2Extent
     *            is the extent of the axis 2 of the parallelogram.
     * @param ex is the coordinate of the min point of the ellipse rectangle.
     * @param ey is the coordinate of the min point of the ellipse rectangle.
     * @param ewidth is the width of the ellipse.
     * @param eheight is the height of the ellipse.
     * @return {@code true} if intersecting, otherwise {@code false}
     */
    @Pure
    @Unefficient
    @SuppressWarnings({"checkstyle:npathcomplexity", "checkstyle:parameternumber"})
    static boolean intersectsOrientedRectangleEllipse(
            double centerX, double centerY,
            double axis1X, double axis1Y,
            double axis1Extent,
            double axis2Extent,
            double ex, double ey, double ewidth, double eheight) {
        assert axis1Extent >= 0 : AssertMessages.positiveOrZeroParameter(4);
        assert axis2Extent >= 0 : AssertMessages.positiveOrZeroParameter(5);
        assert Vector2D.isUnitVector(axis1X, axis1Y) : AssertMessages.normalizedParameters(2, 3);
        assert ewidth >= 0 : AssertMessages.positiveOrZeroParameter(7);
        assert eheight >= 0 : AssertMessages.positiveOrZeroParameter(8);

        if (ewidth <= 0 || eheight <= 0) {
            return false;
        }

        // Convert the oriented rectangle elements so that the ellipse is transformed to a circle centered at the origin.
        final var a = ewidth / 2.;
        final var b = eheight / 2.;

        final var translateX = ex + a;
        final var translateY = ey + b;

        final var transCenterX = (centerX - translateX) / a;
        final var transCenterY = (centerY - translateY) / b;

        var transAxis1X = axis1Extent * axis1X / a;
        var transAxis1Y = axis1Extent * axis1Y / b;
        final var length1 = Math.hypot(transAxis1X, transAxis1Y);
        transAxis1X /= length1;
        transAxis1Y /= length1;

        var transAxis2X = axis2Extent * -axis1Y / a;
        var transAxis2Y = axis2Extent * axis1X / b;
        final var length2 = Math.hypot(transAxis2X, transAxis2Y);
        transAxis2X /= length2;
        transAxis2Y /= length2;

        return Parallelogram2afp.intersectsParallelogramCircle(
                transCenterX, transCenterY,
                transAxis1X, transAxis1Y, length1,
                transAxis2X, transAxis2Y, length2,
                0, 0, 1);
    }

    /** Replies if the specified parallelogram intersects the specified circle.
     *
     * @param centerX
     *            is the X coordinate of the parallelogram center.
     * @param centerY
     *            is the Y coordinate of the parallelogram center.
     * @param axis1X
     *            is the X coordinate of the axis 1 unit vector.
     * @param axis1Y
     *            is the Y coordinate of the axis 1 unit vector.
     * @param axis1Extent
     *            is the extent of the axis 1 of the parallelogram.
     * @param axis2Extent
     *            is the extent of the axis 2 of the parallelogram.
     * @param circleX is the coordinate of the circle center.
     * @param circleY is the coordinate of the circle center.
     * @param circleRadius is the radius of the circle.
     * @return {@code true} if intersecting, otherwise {@code false}
     */
    @Pure
    @SuppressWarnings({"checkstyle:npathcomplexity", "checkstyle:parameternumber"})
    static boolean intersectsOrientedRectangleCircle(
            double centerX, double centerY,
            double axis1X, double axis1Y,
            double axis1Extent,
            double axis2Extent,
            double circleX, double circleY, double circleRadius) {
        assert axis1Extent >= 0 : AssertMessages.positiveOrZeroParameter(4);
        assert axis2Extent >= 0 : AssertMessages.positiveOrZeroParameter(5);
        assert Vector2D.isUnitVector(axis1X, axis1Y) : AssertMessages.normalizedParameters(2, 3);
        assert circleRadius >= 0 : AssertMessages.positiveOrZeroParameter(8);
        final var closest = new InnerComputationPoint2D();
        findsClosestFarthestPointsPointOrientedRectangle(
                circleX, circleY,
                centerX, centerY,
                axis1X, axis1Y, axis1Extent,
                axis2Extent,
                closest, null);
        // Circle and oriented rectangle intersect if the (squared) distance from sphere
        // center to point p is less than the (squared) sphere radius
        final var squaredRadius = circleRadius * circleRadius;

        return Point2D.getDistanceSquaredPointPoint(
                circleX, circleY,
                closest.getX(), closest.getY()) <= squaredRadius;
    }

    /** Replies if the specified rectangles intersect.
     *
     * <p>The extents are assumed to be positive or zero.
     * The lengths of the given arrays are assumed to be {@code 2}.
     *
     * <p>This function uses the "separating axis theorem" which states that
     * for any two oriented rectangles (AABB is a special case of oriented rectangle)
     * that do not touch, a separating axis can be found.
     *
     * <p>This function uses an general intersection test between two oriented rectangle.
     * If the first box is expected to be an MBR, please use the
     * optimized algorithm given by
     * {@link #intersectsOrientedRectangleRectangle(double, double, double, double, double,
     * double, double, double, double, double)}.
     *
     * @param centerX1
     *            is the X coordinate of the oriented rectangle center.
     * @param centerY1
     *            is the Y coordinate of the oriented rectangle center.
     * @param axis1X1
     *            is the X coordinate of the axis 1 unit vector.
     * @param axis1Y1
     *            is the Y coordinate of the axis 1 unit vector.
     * @param axis1Extent1
     *            is the extent of the axis 1 of the oriented rectangle.
     * @param axis2Extent1
     *            is the extent of the axis 2 of the oriented rectangle.
     * @param centerX2
     *            is the X coordinate of the oriented rectangle center.
     * @param centerY2
     *            is the Y coordinate of the oriented rectangle center.
     * @param axis1X2
     *            is the X coordinate of the axis 1 unit vector.
     * @param axis1Y2
     *            is the Y coordinate of the axis 1 unit vector.
     * @param axis1Extent2
     *            is the extent of the axis 1 of the oriented rectangle.
     * @param axis2Extent2
     *            is the extent of the axis 2 of the oriented rectangle.
     * @return {@code true} if intersecting, otherwise {@code false}
     * @see "RTCD pages 102-105"
     * @see <a href="http://www.jkh.me/files/tutorials/Separating%20Axis%20Theorem%20for%20Oriented%20Bounding%20Boxes.pdf">Intersection between two oriented boudning rectangles</a>
     */
    @Pure
    @SuppressWarnings({"checkstyle:npathcomplexity", "checkstyle:parameternumber"})
    static boolean intersectsOrientedRectangleOrientedRectangle(
            double centerX1, double centerY1,
            double axis1X1, double axis1Y1,
            double axis1Extent1,
            double axis2Extent1,
            double centerX2, double centerY2,
            double axis1X2, double axis1Y2,
            double axis1Extent2,
            double axis2Extent2) {
        assert axis1Extent1 >= 0. : AssertMessages.positiveOrZeroParameter(4);
        assert axis2Extent1 >= 0. : AssertMessages.positiveOrZeroParameter(5);
        assert axis1Extent2 >= 0. : AssertMessages.positiveOrZeroParameter(10);
        assert axis2Extent2 >= 0. : AssertMessages.positiveOrZeroParameter(11);

        final var tx = centerX2 - centerX1;
        final var ty = centerY2 - centerY1;

        // Let A the first box and B the second one.
        // Let Ax and Ay the axes of A.
        // Let WA the extent related to Ax.
        // Let HA the extent related to Ay.
        // Let Bx and By the axes of B.
        // Let WB the extent related to Bx.
        // Let HB the extent related to By.

        // Case 1:
        //   L = Ax
        //   L is a separating axis iff:
        //   | T.Ax | > WA + | ( WB * Bx ) . Ax | + |( HB * By ) . Ax |
        final var absTAx = Math.abs(findsVectorProjectionRAxisVector(axis1X1, axis1Y1, tx, ty));
        final var bxx = axis1Extent2 * axis1X2;
        final var bxy = axis1Extent2 * axis1Y2;
        final var absbxax = Math.abs(findsVectorProjectionRAxisVector(axis1X1, axis1Y1, bxx, bxy));
        final var byx = axis2Extent2 * -axis1Y2;
        final var byy = axis2Extent2 * axis1X2;
        final var absbyax = Math.abs(findsVectorProjectionRAxisVector(axis1X1, axis1Y1, byx, byy));
        if (absTAx > (axis1Extent1 + absbxax + absbyax)) {
            return false;
        }

        // Case 2:
        //   L = Ay
        //   L is a separating axis iff:
        //   | T.Ay | > HA + | ( WB * Bx ) . Ay | + |( HB * By ) . Ay |
        final var absTAy = Math.abs(findsVectorProjectionSAxisVector(axis1X1, axis1Y1, tx, ty));
        final var absBxAy = Math.abs(findsVectorProjectionRAxisVector(-axis1Y1, axis1X1, bxx, bxy));
        final var absByAy = Math.abs(findsVectorProjectionRAxisVector(-axis1Y1, axis1X1, byx, byy));
        if (absTAy > (axis2Extent1 + absBxAy + absByAy)) {
            return false;
        }

        // Case 3:
        //   L = Bx
        //   L is a separating axis iff:
        //   | T . Bx | > | ( WA * Ax ) . Bx | + | ( HA * Ay ) . Bx | + WB
        final var absTBx = Math.abs(findsVectorProjectionRAxisVector(axis1X2, axis1Y2, tx, ty));
        final var axx = axis1Extent1 * axis1X1;
        final var axy = axis1Extent1 * axis1Y1;
        final var absAxBx = Math.abs(findsVectorProjectionRAxisVector(axis1X2, axis1Y2, axx, axy));
        final var ayx = axis2Extent1 * -axis1Y1;
        final var ayy = axis2Extent1 * axis1X1;
        final var absAyBx = Math.abs(findsVectorProjectionRAxisVector(axis1X2, axis1Y2, ayx, ayy));
        if (absTBx > (absAxBx + absAyBx + axis1Extent2)) {
            return false;
        }

        // Case 4:
        //   L = By
        //   L is a separating axis iff:
        //   | T . By | > | ( WA * Ax ) . By | + | ( HA * Ay ) . By | + HB
        final var absTBy = Math.abs(findsVectorProjectionRAxisVector(-axis1Y2, axis1X2, tx, ty));
        final var absAxBy = Math.abs(findsVectorProjectionRAxisVector(-axis1Y2, axis1X2, axx, axy));
        final var absAyBy = Math.abs(findsVectorProjectionRAxisVector(-axis1Y2, axis1X2, ayx, ayy));
        if (absTBy > (absAxBy + absAyBy + axis2Extent2)) {
            return false;
        }

        // No separating axis found, the two boxes are overlapping.
        return true;
    }

    /** Replies if the specified rectangles intersect.
     *
     * <p>This function is assuming that {@code lx1} is lower
     * or equal to {@code ux1}, and {@code ly1} is lower
     * or equal to {@code uy1}.
     * The extents are assumed to be positive or zero.
     * The lengths of the given arrays are assumed to be {@code 2}.
     *
     * <p>This function uses the "separating axis theorem" which states that
     * for any two oriented rectangles (AABB is a special case of oriented rectangle)
     * that do not touch, a separating axis can be found.
     *
     * <p>This function uses an optimized algorithm for AABB as first parameter.
     * The general intersection type between two oriented rectangle is given by
     * {@link #intersectsOrientedRectangleOrientedRectangle}.
     *
     * @param centerX
     *            is the X coordinate of the oriented rectangle center.
     * @param centerY
     *            is the Y coordinate of the oriented rectangle center.
     * @param axis1X
     *            is the X coordinate of the axis 1 unit vector.
     * @param axis1Y
     *            is the Y coordinate of the axis 1 unit vector.
     * @param axis1Extent
     *            is the extent of the axis 1 of the oriented rectangle.
     * @param axis2Extent
     *            is the extent of the axis 2 of the oriented rectangle.
     * @param rx
     *            is the X coordinate of the lower point of the rectangle.
     * @param ry
     *            is the Y coordinate of the lower point of the rectangle.
     * @param rwidth
     *            is the width of the rectangle.
     * @param rheight
     *            is the height of the rectangle.
     * @return {@code true} if intersecting, otherwise {@code false}
     * @see "RTCD pages 102-105"
     * @see <a href="http://www.gamasutra.com/features/19991018/Gomez_5.htm">OBB collision detection on Gamasutra.com</a>
     */
    @Pure
    @SuppressWarnings({"checkstyle:npathcomplexity", "checkstyle:parameternumber"})
    static boolean intersectsOrientedRectangleRectangle(
            double centerX, double centerY,
            double axis1X, double axis1Y,
            double axis1Extent,
            double axis2Extent,
            double rx, double ry,
            double rwidth, double rheight) {
        assert rwidth >= 0. : AssertMessages.positiveOrZeroParameter(8);
        assert rheight >= 0. : AssertMessages.positiveOrZeroParameter(9);
        assert axis1Extent >= 0. : AssertMessages.positiveOrZeroParameter(4);
        assert axis2Extent >= 0. : AssertMessages.positiveOrZeroParameter(5);
        assert Vector2D.isUnitVector(axis1X, axis1Y) : AssertMessages.normalizedParameters(1, 2);
        final var rx2 = rx + rwidth;
        final var ry2 = ry + rheight;
        final var axis2X = -axis1Y;
        final var axis2Y = axis1X;
        // Test border intersections
        final var px1 = centerX + axis1Extent * axis1X + axis2Extent * axis2X;
        final var py1 = centerY + axis1Extent * axis1Y + axis2Extent * axis2Y;
        final var px2 = centerX - axis1Extent * axis1X + axis2Extent * axis2X;
        final var py2 = centerY - axis1Extent * axis1Y + axis2Extent * axis2Y;
        if (Rectangle2afp.intersectsRectangleSegment(rx, ry, rx2, ry2,
                px1, py1, px2, py2)) {
            return true;
        }
        final var px3 = centerX - axis1Extent * axis1X - axis2Extent * axis2X;
        final var py3 = centerY - axis1Extent * axis1Y - axis2Extent * axis2Y;
        if (Rectangle2afp.intersectsRectangleSegment(rx, ry, rx2, ry2,
                px2, py2, px3, py3)) {
            return true;
        }
        final var px4 = centerX + axis1Extent * axis1X - axis2Extent * axis2X;
        final var py4 = centerY + axis1Extent * axis1Y - axis2Extent * axis2Y;
        if (Rectangle2afp.intersectsRectangleSegment(rx, ry, rx2, ry2,
                px3, py3, px4, py4)) {
            return true;
        }
        if (Rectangle2afp.intersectsRectangleSegment(rx, ry, rx2, ry2,
                px4, py4, px1, py1)) {
            return true;
        }

        // The rectangle is entirely outside or entirely inside the parallelogram.

        // Test if one rectangle point is inside the parallelogram.
        // We need to test only one point from the rectangle, since if the first
        // point is not inside, the other three points are not too.
        if (containsOrientedRectanglePoint(
                centerX, centerY, axis1X, axis1Y, axis1Extent, axis2Extent, rx, ry)) {
            return true;
        }

        // Test if one parallelogram point is inside the rectangle
        // We need to test only one point from the rectangle, since if the first
        // point is not inside, the other three points are not too.
        return Rectangle2afp.containsRectanglePoint(rx, ry, rx2, ry2, px1, py1);
    }

    /**  Replies if the oriented rectangle intersects the given rectangle.
     *
     * @param centerX
     *            is the X coordinate of the parallelogram center.
     * @param centerY
     *            is the Y coordinate of the parallelogram center.
     * @param axis1X
     *            is the X coordinate of the axis 1 unit vector.
     * @param axis1Y
     *            is the Y coordinate of the axis 1 unit vector.
     * @param axis1Extent
     *            is the extent of the axis 1 of the parallelogram.
     * @param axis2Extent
     *            is the extent of the axis 2 of the parallelogram.
     * @param rx
     *            is the X coordinate of the lower point of the rectangle.
     * @param ry
     *            is the Y coordinate of the lower point of the rectangle.
     * @param rwidth
     *            is the width of the rectangle.
     * @param rheight
     *            is the height of the rectangle.
     * @param rArcWidth
     *            is the width of the rectangle arcs.
     * @param rArcHeight
     *            is the height of the rectangle arcs.
     * @return {@code true} if intersecting, otherwise {@code false}.
     */
    @Pure
    @Unefficient
    @SuppressWarnings({"checkstyle:npathcomplexity", "checkstyle:parameternumber", "checkstyle:cyclomaticcomplexity"})
    static boolean intersectsOrientedRectangleRoundRectangle(
            double centerX, double centerY,
            double axis1X, double axis1Y,
            double axis1Extent,
            double axis2Extent,
            double rx, double ry,
            double rwidth, double rheight,
            double rArcWidth, double rArcHeight) {
        assert axis1Extent >= 0 : AssertMessages.positiveOrZeroParameter(4);
        assert axis2Extent >= 0 : AssertMessages.positiveOrZeroParameter(5);
        assert Vector2D.isUnitVector(axis1X, axis1Y) : AssertMessages.normalizedParameters(2, 3);
        assert rwidth >= 0 : AssertMessages.positiveOrZeroParameter(8);
        assert rheight >= 0 : AssertMessages.positiveOrZeroParameter(9);
        assert rArcWidth >= 0 : AssertMessages.positiveOrZeroParameter(10);
        assert rArcHeight >= 0 : AssertMessages.positiveOrZeroParameter(11);

        final var rx2 = rx + rwidth;
        final var ry2 = ry + rheight;
        final var rxmin = rx + rArcWidth;
        final var rxmax = rx2 - rArcWidth;
        final var rymin = ry + rArcHeight;
        final var rymax = ry2 - rArcHeight;
        final var ew = rArcWidth * 2;
        final var eh = rArcWidth * 2;
        final var emaxx = rxmax - rArcWidth;
        final var emaxy = rymax - rArcHeight;
        final var axis2X = -axis1Y;
        final var axis2Y = axis1X;
        // Test border intersections
        final var px1 = centerX + axis1Extent * axis1X + axis2Extent * axis2X;
        final var py1 = centerY + axis1Extent * axis1Y + axis2Extent * axis2Y;
        final var px2 = centerX - axis1Extent * axis1X + axis2Extent * axis2X;
        final var py2 = centerY - axis1Extent * axis1Y + axis2Extent * axis2Y;
        if (Rectangle2afp.intersectsRectangleSegment(rxmin, ry, rxmax, ry2,
                px1, py1, px2, py2)
                || Rectangle2afp.intersectsRectangleSegment(rx, rymin, rx2, rymax, px1, py1, px2, py2)
                || Ellipse2afp.intersectsEllipseSegment(rx, ry, ew, eh, px1, py1, px2, py2, false)
                || Ellipse2afp.intersectsEllipseSegment(rx, emaxy, ew, eh, px1, py1, px2, py2, false)
                || Ellipse2afp.intersectsEllipseSegment(emaxx, emaxy, ew, eh, px1, py1, px2, py2, false)
                || Ellipse2afp.intersectsEllipseSegment(emaxx, ry, ew, eh, px1, py1, px2, py2, false)) {
            return true;
        }
        final var px3 = centerX - axis1Extent * axis1X - axis2Extent * axis2X;
        final var py3 = centerY - axis1Extent * axis1Y - axis2Extent * axis2Y;
        if (Rectangle2afp.intersectsRectangleSegment(rxmin, ry, rxmax, ry2,
                px2, py2, px3, py3)
                || Rectangle2afp.intersectsRectangleSegment(rx, rymin, rx2, rymax, px2, py2, px3, py3)
                || Ellipse2afp.intersectsEllipseSegment(rx, ry, ew, eh, px2, py2, px3, py3, false)
                || Ellipse2afp.intersectsEllipseSegment(rx, emaxy, ew, eh, px2, py2, px3, py3, false)
                || Ellipse2afp.intersectsEllipseSegment(emaxx, emaxy, ew, eh, px2, py2, px3, py3, false)
                || Ellipse2afp.intersectsEllipseSegment(emaxx, ry, ew, eh, px2, py2, px3, py3, false)) {
            return true;
        }
        final var px4 = centerX + axis1Extent * axis1X - axis2Extent * axis2X;
        final var py4 = centerY + axis1Extent * axis1Y - axis2Extent * axis2Y;
        if (Rectangle2afp.intersectsRectangleSegment(rxmin, ry, rxmax, ry2,
                px3, py3, px4, py4)
                || Rectangle2afp.intersectsRectangleSegment(rx, rymin, rx2, rymax, px3, py3, px4, py4)
                || Ellipse2afp.intersectsEllipseSegment(rx, ry, ew, eh, px3, py3, px4, py4, false)
                || Ellipse2afp.intersectsEllipseSegment(rx, emaxy, ew, eh, px3, py3, px4, py4, false)
                || Ellipse2afp.intersectsEllipseSegment(emaxx, emaxy, ew, eh, px3, py3, px4, py4, false)
                || Ellipse2afp.intersectsEllipseSegment(emaxx, ry, ew, eh, px3, py3, px4, py4, false)) {
            return true;
        }
        if (Rectangle2afp.intersectsRectangleSegment(rxmin, ry, rxmax, ry2,
                px4, py4, px1, py1)
                || Rectangle2afp.intersectsRectangleSegment(rx, rymin, rx2, rymax, px4, py4, px1, py1)
                || Ellipse2afp.intersectsEllipseSegment(rx, ry, ew, eh, px4, py4, px1, py1, false)
                || Ellipse2afp.intersectsEllipseSegment(rx, emaxy, ew, eh, px4, py4, px1, py1, false)
                || Ellipse2afp.intersectsEllipseSegment(emaxx, emaxy, ew, eh, px4, py4, px1, py1, false)
                || Ellipse2afp.intersectsEllipseSegment(emaxx, ry, ew, eh, px4, py4, px1, py1, false)) {
            return true;
        }

        // The rectangle is entirely outside or entirely inside the oriented rectangle.

        // Test if one rectangle point is inside the oriented rectangle.
        // We need to test only one point from the rectangle, since if the first
        // point is not inside, the other three points are not too.
        if (containsOrientedRectanglePoint(
                centerX, centerY, axis1X, axis1Y, axis1Extent, axis2Extent, rx, ry)) {
            return true;
        }

        // Test if one oriented rectangle point is inside the rectangle
        // We need to test only one point from the rectangle, since if the first
        // point is not inside, the other three points are not too.
        return Rectangle2afp.containsRectanglePoint(rx, ry, rx2, ry2, px1, py1);
    }

    /**
     * Tests if the interior of the specified {@link PathIterator2afp}
     * intersects the interior of a specified set of oriented rectangular
     * coordinates.
     *
     * @param <T> the type of the path elements to iterate on.
     * @param centerX the specified X coordinate of the rectangle center.
     * @param centerY the specified Y coordinate of the rectangle center.
     * @param axis1X the X coordinate of the first axis of the rectangle.
     * @param axis1Y the Y coordinate of the first axis of the rectangle.
     * @param extent1 the extent the rectangle along the first axis.
     * @param extent2 the extent the rectangle along the second axis.
     * @param pathIterator the specified {@link PathIterator2afp}.
     * @return {@code true} if the specified {@link PathIterator2afp} and
     *         the interior of the specified set of rectangular
     *         coordinates intersect each other; {@code false} otherwise.
     */
    static <T extends PathElement2afp> boolean intersectsOrientedRectanglePathIterator(
            double centerX, double centerY, double axis1X, double axis1Y, double extent1, double extent2,
            PathIterator2afp<T> pathIterator) {
        assert pathIterator != null : AssertMessages.notNullParameter(6);
        assert extent1 >= 0. : AssertMessages.positiveOrZeroParameter(4);
        assert extent2 >= 0. : AssertMessages.positiveOrZeroParameter(5);
        assert Vector2D.isUnitVector(axis1X, axis1Y) : AssertMessages.normalizedParameters(2, 3);
        final var mask = pathIterator.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
        final var localIterator = new ProjectionToOrientedRectangleLocalCoordinateSystemPathIterator<>(
                        centerX, centerY, axis1X, axis1Y,
                        pathIterator);
        final var crossings = Path2afp.calculatesCrossingsPathIteratorRectangleShadow(
                0,
                localIterator,
                -extent1, -extent2,
                extent1, extent2,
                CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
        return crossings == GeomConstants.SHAPE_INTERSECTS
                || (crossings & mask) != 0;
    }

    @Pure
    @Override
    @SuppressWarnings("checkstyle:booleanexpressioncomplexity")
    default boolean equalsToShape(IT shape) {
        if (shape == null) {
            return false;
        }
        if (shape == this) {
            return true;
        }
        return getCenterX() == shape.getCenterX()
                && getCenterY() == shape.getCenterY()
                && getFirstAxisX() == shape.getFirstAxisX()
                && getFirstAxisY() == shape.getFirstAxisY()
                && getFirstAxisExtent() == shape.getFirstAxisExtent()
                && getSecondAxisX() == shape.getSecondAxisX()
                && getSecondAxisY() == shape.getSecondAxisY()
                && getSecondAxisExtent() == shape.getSecondAxisExtent();
    }

    @Override
    default void clear() {
        set(0, 0, 1, 0, 0, 0);
    }

    @Override
    default void set(IT rectangle) {
        assert rectangle != null : AssertMessages.notNullParameter();
        set(rectangle.getCenterX(), rectangle.getCenterY(),
                rectangle.getFirstAxisX(), rectangle.getFirstAxisY(), rectangle.getFirstAxisExtent(),
                rectangle.getSecondAxisExtent());
    }

    /** {@inheritDoc}
     *
     * <p>For an oriented rectangle, the coordinates of the second axis are ignored.
     * Indeed, they are automatically computed for being orthogonal to the first axis.
     */
    @Override
    default void set(double centerX, double centerY, double axis1x, double axis1y, double axis1Extent, double axis2x,
            double axis2y, double axis2Extent) {
        set(centerX, centerY, axis1x, axis1y, axis1Extent, axis2Extent);
    }

    /** Set the oriented rectangle.
     * The second axis is automatically computed.
     *
     * @param center is the oriented rectangle center.
     * @param axis1 is the first axis of the oriented rectangle.
     * @param axis1Extent is the extent of the first axis.
     * @param axis2Extent is the extent of the second axis.
     */
    default void set(Point2D<?, ?> center, Vector2D<?, ?> axis1, double axis1Extent, double axis2Extent) {
        assert center != null : AssertMessages.notNullParameter(0);
        assert axis1 != null : AssertMessages.notNullParameter(1);
        set(center.getX(), center.getY(), axis1.getX(), axis1.getY(), axis1Extent, axis2Extent);
    }

    /** Set the oriented rectangle.
     * The second axis is automatically computed.
     *
     * @param centerX is the X coordinate of the oriented rectangle center.
     * @param centerY is the Y coordinate of the oriented rectangle center.
     * @param axis1X is the X coordinate of first axis of the oriented rectangle.
     * @param axis1Y is the Y coordinate of first axis of the oriented rectangle.
     * @param axis1Extent is the extent of the first axis.
     * @param axis2Extent is the extent of the second axis.
     */
    void set(double centerX, double centerY,
            double axis1X, double axis1Y, double axis1Extent,
            double axis2Extent);

    @Override
    default void setFromPointCloud(Iterable<? extends Point2D<?, ?>> pointCloud) {
        assert pointCloud != null : AssertMessages.notNullParameter();
        final var r = new InnerComputationVector2D();
        Parallelogram2afp.calculatesOrthogonalAxes(pointCloud, r, null);
        final var center = new InnerComputationPoint2D();
        final var extents = new InnerComputationVector2D();
        OrientedRectangle2afp.calculatesCenterPointAxisExtents(pointCloud, r, center, extents);
        set(center.getX(), center.getY(),
                r.getX(), r.getY(), extents.getX(),
                extents.getY());
    }

    @Pure
    @Override
    default double getDistanceSquared(Point2D<?, ?> pt) {
        assert pt != null : AssertMessages.notNullParameter();
        final var closest = new InnerComputationPoint2D();
        findsClosestFarthestPointsPointOrientedRectangle(
                pt.getX(), pt.getY(),
                getCenterX(), getCenterY(),
                getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
                getSecondAxisExtent(),
                closest,
                null);
        return closest.getDistanceSquared(pt);
    }

    @Pure
    @Override
    default double getDistanceL1(Point2D<?, ?> pt) {
        assert pt != null : AssertMessages.notNullParameter();
        final var closest = new InnerComputationPoint2D();
        findsClosestFarthestPointsPointOrientedRectangle(
                pt.getX(), pt.getY(),
                getCenterX(), getCenterY(),
                getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
                getSecondAxisExtent(),
                closest,
                null);
        return closest.getDistanceL1(pt);
    }

    @Pure
    @Override
    default double getDistanceLinf(Point2D<?, ?> pt) {
        assert pt != null : AssertMessages.notNullParameter();
        final var closest = new InnerComputationPoint2D();
        findsClosestFarthestPointsPointOrientedRectangle(
                pt.getX(), pt.getY(),
                getCenterX(), getCenterY(),
                getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
                getSecondAxisExtent(),
                closest, null);
        return closest.getDistanceLinf(pt);
    }

    @Override
    default void translate(double dx, double dy) {
        setCenter(getCenterX() + dx, getCenterY() + dy);
    }

    @Pure
    @Override
    default boolean contains(double x, double y) {
        return containsOrientedRectanglePoint(
                getCenterX(), getCenterY(),
                getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
                getSecondAxisExtent(),
                x, y);
    }

    @Pure
    @Override
    default boolean contains(Rectangle2afp<?, ?, ?, ?, ?, ?> rectangle) {
        assert rectangle != null : AssertMessages.notNullParameter();
        return containsOrientedRectangleRectangle(
                getCenterX(), getCenterY(),
                getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
                getSecondAxisExtent(),
                rectangle.getMinX(), rectangle.getMinY(),
                rectangle.getWidth(), rectangle.getHeight());
    }

    @Pure
    @Override
    default boolean intersects(Circle2afp<?, ?, ?, ?, ?, ?> circle) {
        assert circle != null : AssertMessages.notNullParameter();
        return intersectsOrientedRectangleCircle(
                getCenterX(), getCenterY(),
                getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
                getSecondAxisExtent(),
                circle.getX(), circle.getY(), circle.getRadius());
    }

    @Pure
    @Override
    default boolean intersects(Ellipse2afp<?, ?, ?, ?, ?, ?> ellipse) {
        assert ellipse != null : AssertMessages.notNullParameter();
        return intersectsOrientedRectangleEllipse(
                getCenterX(), getCenterY(),
                getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
                getSecondAxisExtent(),
                ellipse.getMinX(), ellipse.getMinY(),
                ellipse.getMaxX() - ellipse.getMinX(),
                ellipse.getMaxY() - ellipse.getMinY());
    }

    @Pure
    @Override
    default boolean intersects(OrientedRectangle2afp<?, ?, ?, ?, ?, ?> orientedRectangle) {
        assert orientedRectangle != null : AssertMessages.notNullParameter();
        return intersectsOrientedRectangleOrientedRectangle(
                getCenterX(), getCenterY(),
                getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
                getSecondAxisExtent(),
                orientedRectangle.getCenterX(), orientedRectangle.getCenterY(),
                orientedRectangle.getFirstAxisX(), orientedRectangle.getFirstAxisY(), orientedRectangle.getFirstAxisExtent(),
                orientedRectangle.getSecondAxisExtent());
    }

    @Pure
    @Override
    default boolean intersects(Parallelogram2afp<?, ?, ?, ?, ?, ?> parallelogram) {
        assert parallelogram != null : AssertMessages.notNullParameter();
        return Parallelogram2afp.intersectsParallelogramParallelogram(
                parallelogram.getCenterX(), parallelogram.getCenterY(),
                parallelogram.getFirstAxisX(), parallelogram.getFirstAxisY(), parallelogram.getFirstAxisExtent(),
                parallelogram.getSecondAxisX(), parallelogram.getSecondAxisY(), parallelogram.getSecondAxisExtent(),
                getCenterX(), getCenterY(),
                getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
                getSecondAxisX(), getSecondAxisY(), getSecondAxisExtent());
    }

    @Pure
    @Override
    default boolean intersects(Rectangle2afp<?, ?, ?, ?, ?, ?> rectangle) {
        assert rectangle != null : AssertMessages.notNullParameter();
        return intersectsOrientedRectangleRectangle(
                getCenterX(), getCenterY(),
                getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
                getSecondAxisExtent(),
                rectangle.getMinX(), rectangle.getMinY(),
                rectangle.getWidth(), rectangle.getHeight());
    }

    @Pure
    @Override
    default boolean intersects(RoundRectangle2afp<?, ?, ?, ?, ?, ?> roundRectangle) {
        assert roundRectangle != null : AssertMessages.notNullParameter();
        return roundRectangle.intersects(this);
    }

    @Pure
    @Override
    default boolean intersects(Segment2afp<?, ?, ?, ?, ?, ?> segment) {
        assert segment != null : AssertMessages.notNullParameter();
        return intersectsOrientedRectangleSegment(
                getCenterX(), getCenterY(),
                getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
                getSecondAxisExtent(),
                segment.getX1(), segment.getY1(), segment.getX2(), segment.getY2());
    }

    @Pure
    @Override
    default boolean intersects(Triangle2afp<?, ?, ?, ?, ?, ?> triangle) {
        assert triangle != null : AssertMessages.notNullParameter();
        return intersectsOrientedRectangleTriangle(
                getCenterX(), getCenterY(),
                getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
                getSecondAxisExtent(),
                triangle.getX1(), triangle.getY1(), triangle.getX2(), triangle.getY2(), triangle.getX3(), triangle.getY3());
    }

    @Pure
    @Override
    default boolean intersects(PathIterator2afp<?> iterator) {
        assert iterator != null : AssertMessages.notNullParameter();
        return intersectsOrientedRectanglePathIterator(
                getCenterX(), getCenterY(),
                getFirstAxisX(), getFirstAxisY(),
                getFirstAxisExtent(), getSecondAxisExtent(),
                iterator);
    }

    @Pure
    @Override
    default boolean intersects(MultiShape2afp<?, ?, ?, ?, ?, ?, ?> multishape) {
        assert multishape != null : AssertMessages.notNullParameter();
        return multishape.intersects(this);
    }

    @Pure
    @Override
    default PathIterator2afp<IE> getPathIterator(Transform2D transform) {
        if (transform == null || transform.isIdentity()) {
            return new OrientedRectanglePathIterator<>(this);
        }
        return new TransformedOrientedRectanglePathIterator<>(this, transform);
    }

    @Pure
    @Override
    default void toBoundingBox(B box) {
        final var minCorner = new InnerComputationPoint2D(getCenterX(), getCenterY());
        final var maxCorner = new InnerComputationPoint2D(getCenterX(), getCenterY());

        final var srx = getFirstAxisX() * getFirstAxisExtent();
        final var sry = getFirstAxisY() * getFirstAxisExtent();
        final var ssx = getSecondAxisX() * getSecondAxisExtent();
        final var ssy = getSecondAxisY() * getSecondAxisExtent();

        if (getFirstAxisX() >= 0.) {
            if (getFirstAxisY() >= 0.) {
                minCorner.add(-srx + ssx, -sry - ssy);
                maxCorner.sub(-srx + ssx, -sry - ssy);
            } else {
                minCorner.add(-srx - ssx, sry - ssy);
                maxCorner.sub(-srx - ssx, sry - ssy);
            }
        } else {
            if (getFirstAxisY() >= 0.) {
                minCorner.add(srx + ssx, -sry + ssy);
                maxCorner.sub(srx + ssx, -sry + ssy);
            } else {
                minCorner.add(srx - ssx, sry + ssy);
                maxCorner.sub(srx - ssx, sry + ssy);
            }
        }
        box.setFromCorners(minCorner, maxCorner);
    }

    @Pure
    @Override
    default P getClosestPointTo(Point2D<?, ?> pt) {
        assert pt != null : AssertMessages.notNullParameter();
        final var point = getGeomFactory().newPoint();
        findsClosestFarthestPointsPointOrientedRectangle(
                pt.getX(), pt.getY(),
                getCenterX(), getCenterY(),
                getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
                getSecondAxisExtent(),
                point, null);
        return point;
    }

    @Override
    default P getClosestPointTo(Circle2afp<?, ?, ?, ?, ?, ?> circle) {
        assert circle != null : AssertMessages.notNullParameter();
        return getClosestPointTo(circle.getCenter());
    }

    @Override
    @Unefficient
    default P getClosestPointTo(Ellipse2afp<?, ?, ?, ?, ?, ?> ellipse) {
        assert ellipse != null : AssertMessages.notNullParameter();
        final var point = getGeomFactory().newPoint();
        Path2afp.findsClosestPointPathIteratorPathIterator(getPathIterator(), ellipse.getPathIterator(), point);
        return point;
    }

    @Override
    @Unefficient
    default P getClosestPointTo(Rectangle2afp<?, ?, ?, ?, ?, ?> rectangle) {
        assert rectangle != null : AssertMessages.notNullParameter();
        final var point = getGeomFactory().newPoint();
        Path2afp.findsClosestPointPathIteratorPathIterator(getPathIterator(), rectangle.getPathIterator(), point);
        return point;
    }

    @Override
    @Unefficient
    default P getClosestPointTo(Segment2afp<?, ?, ?, ?, ?, ?> segment) {
        assert segment != null : AssertMessages.notNullParameter();
        final var point = getGeomFactory().newPoint();
        Path2afp.findsClosestPointPathIteratorPathIterator(getPathIterator(), segment.getPathIterator(), point);
        return point;
    }

    @Override
    @Unefficient
    default P getClosestPointTo(Triangle2afp<?, ?, ?, ?, ?, ?> triangle) {
        assert triangle != null : AssertMessages.notNullParameter();
        final var point = getGeomFactory().newPoint();
        Path2afp.findsClosestPointPathIteratorPathIterator(getPathIterator(), triangle.getPathIterator(), point);
        return point;
    }

    @Override
    @Unefficient
    default P getClosestPointTo(OrientedRectangle2afp<?, ?, ?, ?, ?, ?> orientedRectangle) {
        assert orientedRectangle != null : AssertMessages.notNullParameter();
        final var point = getGeomFactory().newPoint();
        Path2afp.findsClosestPointPathIteratorPathIterator(getPathIterator(), orientedRectangle.getPathIterator(), point);
        return point;
    }

    @Override
    @Unefficient
    default P getClosestPointTo(Parallelogram2afp<?, ?, ?, ?, ?, ?> parallelogram) {
        assert parallelogram != null : AssertMessages.notNullParameter();
        final var point = getGeomFactory().newPoint();
        Path2afp.findsClosestPointPathIteratorPathIterator(getPathIterator(), parallelogram.getPathIterator(), point);
        return point;
    }

    @Override
    @Unefficient
    default P getClosestPointTo(RoundRectangle2afp<?, ?, ?, ?, ?, ?> roundRectangle) {
        assert roundRectangle != null : AssertMessages.notNullParameter();
        final var point = getGeomFactory().newPoint();
        Path2afp.findsClosestPointPathIteratorPathIterator(getPathIterator(), roundRectangle.getPathIterator(), point);
        return point;
    }

    @Override
    @Unefficient
    default P getClosestPointTo(Path2afp<?, ?, ?, ?, ?, ?> path) {
        assert path != null : AssertMessages.notNullParameter();
        final var point = getGeomFactory().newPoint();
        Path2afp.findsClosestPointPathIteratorPathIterator(getPathIterator(), path.getPathIterator(), point);
        return point;
    }

    @Pure
    @Override
    default P getFarthestPointTo(Point2D<?, ?> pt) {
        assert pt != null : AssertMessages.notNullParameter();
        final var point = getGeomFactory().newPoint();
        findsClosestFarthestPointsPointOrientedRectangle(
                pt.getX(), pt.getY(),
                getCenterX(), getCenterY(),
                getFirstAxisX(), getFirstAxisY(), getFirstAxisExtent(),
                getSecondAxisExtent(),
                null, point);
        return point;
    }

    @Override
    default void rotate(double angle) {
        final var axis1 = getFirstAxis();
        final var newAxis = new InnerComputationVector2D();
        newAxis.turn(angle, axis1);
        setFirstAxis(newAxis.getX(), newAxis.getY());
    }

	/** Replies this oriented rectangle with a Geogebra-compatible form.
	 *
	 * @return the Geogebra representation of the oriented rectangle.
	 * @since 18.0
	 */
	@Override
	default String toGeogebra() {
		return GeogebraUtil.toPolygonDefinition(2,
				getCenterX() - getFirstAxisExtent() * getFirstAxisX() - getSecondAxisExtent() * getSecondAxisX(),
				getCenterY() - getFirstAxisExtent() * getFirstAxisY() - getSecondAxisExtent() * getSecondAxisY(),
				getCenterX() + getFirstAxisExtent() * getFirstAxisX() - getSecondAxisExtent() * getSecondAxisX(),
				getCenterY() + getFirstAxisExtent() * getFirstAxisY() - getSecondAxisExtent() * getSecondAxisY(),
				getCenterX() + getFirstAxisExtent() * getFirstAxisX() + getSecondAxisExtent() * getSecondAxisX(),
				getCenterY() + getFirstAxisExtent() * getFirstAxisY() + getSecondAxisExtent() * getSecondAxisY(),
				getCenterX() - getFirstAxisExtent() * getFirstAxisX() + getSecondAxisExtent() * getSecondAxisX(),
				getCenterY() - getFirstAxisExtent() * getFirstAxisY() + getSecondAxisExtent() * getSecondAxisY());
	}

	/** Abstract iterator on the path elements of the oriented rectangle.
	 *
	 * @param <T> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	abstract class AbstractOrientedRectanglePathIterator<T extends PathElement2afp> implements PathIterator2afp<T> {

        /** Number of path elements.
         */
        protected static final int ELEMENT_COUNT = 4;

        /** The iterated shape.
         */
        protected final OrientedRectangle2afp<?, ?, T, ?, ?, ?> rectangle;

        /** Constructor.
         * @param rectangle the iterated rectangle.
         */
        public AbstractOrientedRectanglePathIterator(OrientedRectangle2afp<?, ?, T, ?, ?, ?> rectangle) {
            this.rectangle = rectangle;
        }

        @Override
        public GeomFactory2afp<T, ?, ?, ?> getGeomFactory() {
            return this.rectangle.getGeomFactory();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Pure
        @Override
        public PathWindingRule getWindingRule() {
            return PathWindingRule.NON_ZERO;
        }

        @Pure
        @Override
        public boolean isPolyline() {
            return false;
        }

        @Pure
        @Override
        public boolean isCurved() {
            return false;
        }

        @Pure
        @Override
        public boolean isPolygon() {
            return true;
        }

        @Pure
        @Override
        public boolean isMultiParts() {
            return false;
        }

    }

    /** Iterator on the path elements of an oriented rectangle.
     *
     * @param <T> the type of the path elements.
     * @author $Author: mgrolleau$
     * @author $Author: sgalland$
     * @version $FullVersion$
     * @mavengroupid $GroupId$
     * @mavenartifactid $ArtifactId$
     * @since 13.0
     */
    class OrientedRectanglePathIterator<T extends PathElement2afp> extends AbstractOrientedRectanglePathIterator<T> {

        private double x;

        private double y;

        private Vector2D<?, ?> raxis;

        private Vector2D<?, ?> saxis;

        private int index;

        private double moveX;

        private double moveY;

        private double lastX;

        private double lastY;

        /** Constructor.
         * @param rectangle the oriented rectangle to iterate on.
         */
        public OrientedRectanglePathIterator(OrientedRectangle2afp<?, ?, T, ?, ?, ?> rectangle) {
            super(rectangle);
            if (rectangle.isEmpty()) {
                this.index = ELEMENT_COUNT;
            } else {
                this.raxis = new InnerComputationVector2D(rectangle.getFirstAxisX(), rectangle.getFirstAxisY());
                this.saxis = new InnerComputationVector2D(rectangle.getSecondAxisX(), rectangle.getSecondAxisY());
                this.raxis.scale(rectangle.getFirstAxisExtent());
                this.saxis.scale(rectangle.getSecondAxisExtent());
                this.x = rectangle.getCenterX();
                this.y = rectangle.getCenterY();
                this.index = -1;
            }
        }

        @Override
        public PathIterator2afp<T> restartIterations() {
            return new OrientedRectanglePathIterator<>(this.rectangle);
        }

        @Pure
        @Override
        public boolean hasNext() {
            return this.index < ELEMENT_COUNT;
        }

        @Override
        public T next() {
            final var idx = this.index;
            ++this.index;
            if (idx < 0) {
                this.moveX = this.x + this.raxis.getX() + this.saxis.getX();
                this.moveY = this.y + this.raxis.getY() + this.saxis.getY();
                this.lastX = this.moveX;
                this.lastY = this.moveY;
                return getGeomFactory().newMovePathElement(this.moveX, this.moveY);
            }
            final var lx = this.lastX;
            final var ly = this.lastY;
            switch (idx) {
            case 0:
                this.lastX = this.x - this.raxis.getX() + this.saxis.getX();
                this.lastY = this.y - this.raxis.getY() + this.saxis.getY();
                return getGeomFactory().newLinePathElement(lx, ly, this.lastX, this.lastY);
            case 1:
                this.lastX = this.x - this.raxis.getX() - this.saxis.getX();
                this.lastY = this.y - this.raxis.getY() - this.saxis.getY();
                return getGeomFactory().newLinePathElement(lx, ly, this.lastX, this.lastY);
            case 2:
                this.lastX = this.x + this.raxis.getX() - this.saxis.getX();
                this.lastY = this.y + this.raxis.getY() - this.saxis.getY();
                return getGeomFactory().newLinePathElement(lx, ly, this.lastX, this.lastY);
            case 3:
                return getGeomFactory().newClosePathElement(
                        this.lastX, this.lastY,
                        this.moveX, this.moveY);
            default:
                throw new NoSuchElementException();
            }
        }

    }

    /** Iterator on the path elements of a transformed oriented rectangle.
     *
     * @param <T> the type of the path elements.
     * @author $Author: mgrolleau$
     * @author $Author: sgalland$
     * @version $FullVersion$
     * @mavengroupid $GroupId$
     * @mavenartifactid $ArtifactId$
     */
    class TransformedOrientedRectanglePathIterator<T extends PathElement2afp> extends AbstractOrientedRectanglePathIterator<T> {

        private final Transform2D transform;

        private double x;

        private double y;

        private Vector2D<?, ?> raxis;

        private Vector2D<?, ?> saxis;

        private int index;

        private Point2D<?, ?> move;

        private Point2D<?, ?> last;

        /** Constructor.
         * @param rectangle the oriented rectangle to iterate on.
         * @param transform the transformation to apply.
         */
        public TransformedOrientedRectanglePathIterator(OrientedRectangle2afp<?, ?, T, ?, ?, ?> rectangle,
                Transform2D transform) {
            super(rectangle);
            assert transform != null : AssertMessages.notNullParameter(1);
            this.transform = transform;
            if (rectangle.isEmpty()) {
                this.index = ELEMENT_COUNT;
            } else {
                this.move = getGeomFactory().newPoint();
                this.last = getGeomFactory().newPoint();
                this.raxis = new InnerComputationVector2D(rectangle.getFirstAxisX(), rectangle.getFirstAxisY());
                this.saxis = new InnerComputationVector2D(rectangle.getSecondAxisX(), rectangle.getSecondAxisY());
                this.raxis.scale(rectangle.getFirstAxisExtent());
                this.saxis.scale(rectangle.getSecondAxisExtent());
                this.x = rectangle.getCenterX();
                this.y = rectangle.getCenterY();
                this.index = -1;
            }
        }

        @Override
        public PathIterator2afp<T> restartIterations() {
            return new TransformedOrientedRectanglePathIterator<>(this.rectangle, this.transform);
        }

        @Pure
        @Override
        public boolean hasNext() {
            return this.index < ELEMENT_COUNT;
        }

        @Override
        public T next() {
            final var idx = this.index;
            ++this.index;
            if (idx < 0) {
                this.move.set(
                        this.x + this.raxis.getX() + this.saxis.getX(),
                        this.y + this.raxis.getY() + this.saxis.getY());
                this.transform.transform(this.move);
                this.last.set(this.move);
                return getGeomFactory().newMovePathElement(this.move.getX(), this.move.getY());
            }
            final var lx = this.last.getX();
            final var ly = this.last.getY();
            switch (idx) {
            case 0:
                this.last.set(
                        this.x - this.raxis.getX() + this.saxis.getX(),
                        this.y - this.raxis.getY() + this.saxis.getY());
                this.transform.transform(this.last);
                return getGeomFactory().newLinePathElement(lx, ly, this.last.getX(), this.last.getY());
            case 1:
                this.last.set(
                        this.x - this.raxis.getX() - this.saxis.getX(),
                        this.y - this.raxis.getY() - this.saxis.getY());
                this.transform.transform(this.last);
                return getGeomFactory().newLinePathElement(lx, ly, this.last.getX(), this.last.getY());
            case 2:
                this.last.set(
                        this.x + this.raxis.getX() - this.saxis.getX(),
                        this.y + this.raxis.getY() - this.saxis.getY());
                this.transform.transform(this.last);
                return getGeomFactory().newLinePathElement(lx, ly, this.last.getX(), this.last.getY());
            case 3:
                return getGeomFactory().newClosePathElement(
                        this.last.getX(), this.last.getY(),
                        this.move.getX(), this.move.getY());
            default:
                throw new NoSuchElementException();
            }
        }

    }

    /** An iterator that automatically transform and reply the path elements from the given iterator such that
     * the coordinates of the path elements are projected in the local coordinate system of the given oriented
     * box.
     *
     * @param <T> the type of the path elements.
     * @author $Author: sgalland$
     * @author $Author: mgrolleau$
     * @version $FullVersion$
     * @mavengroupid $GroupId$
     * @mavenartifactid $ArtifactId$
     * @since 13.0
     */
    class ProjectionToOrientedRectangleLocalCoordinateSystemPathIterator<T extends PathElement2afp>
            implements PathIterator2afp<T> {

        private final PathIterator2afp<T> iterator;

        private final double centerX;

        private final double centerY;

        private final double axisX1;

        private final double axisY1;

        /** Constructor.
         * @param x the specified X coordinate of the rectangle center.
         * @param y the specified Y coordinate of the rectangle center.
         * @param axis1X the X coordinate of the first axis of the rectangle.
         * @param axis1Y the Y coordinate of the first axis of the rectangle.
         * @param iterator the iterator to transform.
         */
        public ProjectionToOrientedRectangleLocalCoordinateSystemPathIterator(
                double x, double y, double axis1X, double axis1Y,
                PathIterator2afp<T> iterator) {
            this.iterator = iterator;
            this.centerX = x;
            this.centerY = y;
            this.axisX1 = axis1X;
            this.axisY1 = axis1Y;
        }

        @Override
        public PathIterator2afp<T> restartIterations() {
            return new ProjectionToOrientedRectangleLocalCoordinateSystemPathIterator<>(
                    this.centerX, this.centerY,
                    this.axisX1, this.axisY1,
                    this.iterator.restartIterations());
        }

        @Pure
        @Override
        public boolean hasNext() {
            return this.iterator.hasNext();
        }

        @Override
        public T next() {
            final var elem = this.iterator.next();
            switch (elem.getType()) {
            case CURVE_TO:
                return getCurvToFromNext(elem);
            case ARC_TO:
                return getArcToFromNext(elem);
            case LINE_TO:
                return getLineToFromNext(elem);
            case MOVE_TO:
                return getMoveToFromNext(elem);
            case QUAD_TO:
                return getQuadToFromNext(elem);
            case CLOSE:
                return getCloseFromNext(elem);
            default:
                break;
            }
            return null;
        }

        private T getMoveToFromNext(PathElement2afp elem) {
            return getGeomFactory().newMovePathElement(
                    findsVectorProjectionRAxisVector(this.axisX1, this.axisY1,
                            elem.getToX() - this.centerX, elem.getToY() - this.centerY),
                    findsVectorProjectionSAxisVector(this.axisX1, this.axisY1,
                            elem.getToX() - this.centerX, elem.getToY() - this.centerY));
        }

        private T getCurvToFromNext(PathElement2afp elem) {
            return getGeomFactory().newCurvePathElement(
                    findsVectorProjectionRAxisVector(this.axisX1, this.axisY1,
                            elem.getFromX() - this.centerX, elem.getFromY() - this.centerY),
                    findsVectorProjectionSAxisVector(this.axisX1, this.axisY1,
                            elem.getFromX() - this.centerX, elem.getFromY() - this.centerY),
                    findsVectorProjectionRAxisVector(this.axisX1, this.axisY1,
                            elem.getCtrlX1() - this.centerX, elem.getCtrlY1() - this.centerY),
                    findsVectorProjectionSAxisVector(this.axisX1, this.axisY1,
                            elem.getCtrlX1() - this.centerX, elem.getCtrlY1() - this.centerY),
                    findsVectorProjectionRAxisVector(this.axisX1, this.axisY1,
                            elem.getCtrlX2() - this.centerX, elem.getCtrlY2() - this.centerY),
                    findsVectorProjectionSAxisVector(this.axisX1, this.axisY1,
                            elem.getCtrlX2() - this.centerX, elem.getCtrlY2() - this.centerY),
                    findsVectorProjectionRAxisVector(this.axisX1, this.axisY1,
                            elem.getToX() - this.centerX, elem.getToY() - this.centerY),
                    findsVectorProjectionSAxisVector(this.axisX1, this.axisY1,
                            elem.getToX() - this.centerX, elem.getToY() - this.centerY));
        }

        private T getArcToFromNext(PathElement2afp elem) {
            return getGeomFactory().newArcPathElement(
                    findsVectorProjectionRAxisVector(this.axisX1, this.axisY1,
                            elem.getFromX() - this.centerX, elem.getFromY() - this.centerY),
                    findsVectorProjectionSAxisVector(this.axisX1, this.axisY1,
                            elem.getFromX() - this.centerX, elem.getFromY() - this.centerY),
                    findsVectorProjectionRAxisVector(this.axisX1, this.axisY1,
                            elem.getToX() - this.centerX, elem.getToY() - this.centerY),
                    findsVectorProjectionSAxisVector(this.axisX1, this.axisY1,
                            elem.getToX() - this.centerX, elem.getToY() - this.centerY),
                    elem.getRadiusX(), elem.getRadiusY(), elem.getRotationX(),
                    elem.getLargeArcFlag(), elem.getSweepFlag());
        }

        private T getLineToFromNext(PathElement2afp elem) {
            return getGeomFactory().newLinePathElement(
                    findsVectorProjectionRAxisVector(this.axisX1, this.axisY1,
                            elem.getFromX() - this.centerX, elem.getFromY() - this.centerY),
                    findsVectorProjectionSAxisVector(this.axisX1, this.axisY1,
                            elem.getFromX() - this.centerX, elem.getFromY() - this.centerY),
                    findsVectorProjectionRAxisVector(this.axisX1, this.axisY1,
                            elem.getToX() - this.centerX, elem.getToY() - this.centerY),
                    findsVectorProjectionSAxisVector(this.axisX1, this.axisY1,
                            elem.getToX() - this.centerX, elem.getToY() - this.centerY));
        }

        private T getQuadToFromNext(PathElement2afp elem) {
            return getGeomFactory().newCurvePathElement(
                    findsVectorProjectionRAxisVector(this.axisX1, this.axisY1,
                            elem.getFromX() - this.centerX, elem.getFromY() - this.centerY),
                    findsVectorProjectionSAxisVector(this.axisX1, this.axisY1,
                            elem.getFromX() - this.centerX, elem.getFromY() - this.centerY),
                    findsVectorProjectionRAxisVector(this.axisX1, this.axisY1,
                            elem.getCtrlX1() - this.centerX, elem.getCtrlY1() - this.centerY),
                    findsVectorProjectionSAxisVector(this.axisX1, this.axisY1,
                            elem.getCtrlX1() - this.centerX, elem.getCtrlY1() - this.centerY),
                    findsVectorProjectionRAxisVector(this.axisX1, this.axisY1,
                            elem.getToX() - this.centerX, elem.getToY() - this.centerY),
                    findsVectorProjectionSAxisVector(this.axisX1, this.axisY1,
                            elem.getToX() - this.centerX, elem.getToY() - this.centerY));
        }

        private T getCloseFromNext(PathElement2afp elem) {
            return getGeomFactory().newClosePathElement(
                    findsVectorProjectionRAxisVector(this.axisX1, this.axisY1,
                            elem.getFromX() - this.centerX, elem.getFromY() - this.centerY),
                    findsVectorProjectionSAxisVector(this.axisX1, this.axisY1,
                            elem.getFromX() - this.centerX, elem.getFromY() - this.centerY),
                    findsVectorProjectionRAxisVector(this.axisX1, this.axisY1,
                            elem.getToX() - this.centerX, elem.getToY() - this.centerY),
                    findsVectorProjectionSAxisVector(this.axisX1, this.axisY1,
                            elem.getToX() - this.centerX, elem.getToY() - this.centerY));
        }

        @Override
        public void remove() {
            this.iterator.remove();
        }

        @Pure
        @Override
        public PathWindingRule getWindingRule() {
            return this.iterator.getWindingRule();
        }

        @Pure
        @Override
        public boolean isPolyline() {
            return this.iterator.isPolyline();
        }

        @Pure
        @Override
        public boolean isCurved() {
            return this.iterator.isCurved();
        }

        @Pure
        @Override
        public boolean isPolygon() {
            return this.iterator.isPolygon();
        }

        @Pure
        @Override
        public boolean isMultiParts() {
            return this.iterator.isMultiParts();
        }

        @Override
        public GeomFactory2afp<T, ?, ?, ?> getGeomFactory() {
            return this.iterator.getGeomFactory();
        }

    }

}
