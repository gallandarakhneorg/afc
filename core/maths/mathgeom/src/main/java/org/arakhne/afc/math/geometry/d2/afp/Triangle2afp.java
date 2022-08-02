/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.Unefficient;
import org.arakhne.afc.math.geometry.CrossingComputationType;
import org.arakhne.afc.math.geometry.GeomConstants;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** Fonctional interface that represented a 2D triangle on a plane.
 *
 * @param <ST> is the type of the general implementation.
 * @param <IT> is the type of the implementation of this shape.
 * @param <IE> is the type of the path elements.
 * @param <P> is the type of the points.
 * @param <V> is the type of the vectors.
 * @param <B> is the type of the bounding boxes.
 * @author $Author: sgalland$
 * @author $Author: fozgul$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
@SuppressWarnings("checkstyle:methodcount")
public interface Triangle2afp<
        ST extends Shape2afp<?, ?, IE, P, V, B>,
        IT extends Triangle2afp<?, ?, IE, P, V, B>,
        IE extends PathElement2afp,
        P extends Point2D<? super P, ? super V>,
        V extends Vector2D<? super V, ? super P>,
        B extends Rectangle2afp<?, ?, IE, P, V, B>>
        extends Shape2afp<ST, IT, IE, P, V, B> {

    /** Replies the closest feature of the triangle to the given point.
     *
     * @param tx1 x coordinate of the first point of the triangle.
     * @param ty1 y coordinate of the first point of the triangle.
     * @param tx2 x coordinate of the second point of the triangle.
     * @param ty2 y coordinate of the second point of the triangle.
     * @param tx3 x coordinate of the third point of the triangle.
     * @param ty3 y coordinate of the third point of the triangle.
     * @param px x coordinate of the reference point.
     * @param py y coordinate of the reference point.
     * @return the closest triangle feature to the reference point.
     */
    @SuppressWarnings({"checkstyle:returncount", "checkstyle:npathcomplexity"})
    static TriangleFeature findsClosestFeatureTrianglePoint(double tx1, double ty1, double tx2, double ty2,
            double tx3, double ty3, double px, double py) {
        final double apx = px - tx1;
        final double apy = py - ty1;
        final double abx = tx2 - tx1;
        final double aby = ty2 - ty1;
        double d1 = Vector2D.dotProduct(abx, aby, apx, apy);
        if (d1 < 0.) {
            final double acx = tx3 - tx1;
            final double acy = ty3 - ty1;
            d1 = Vector2D.dotProduct(acx, acy, apx, apy);
            if (d1 < 0.) {
                return TriangleFeature.FIRST_CORNER;
            }
            final double d2 = Vector2D.dotProduct(acx, acy, acx, acy);
            if (d1 > d2) {
                return TriangleFeature.THIRD_CORNER;
            }
            return TriangleFeature.THIRD_SEGMENT;
        }

        final double bpx = px - tx2;
        final double bpy = py - ty2;
        final double bcx = tx3 - tx2;
        final double bcy = ty3 - ty2;
        final double d2 = Vector2D.dotProduct(bcx, bcy, bpx, bpy);
        if (d2 < 0.) {
            final double d3 = Vector2D.dotProduct(abx, aby, abx, aby);
            if (d1 > d3) {
                return TriangleFeature.SECOND_CORNER;
            }
            return TriangleFeature.FIRST_SEGMENT;
        }

        final double cpx = px - tx3;
        final double cpy = py - ty3;
        final double cax = tx1 - tx1;
        final double cay = ty1 - ty1;
        final double d3 = Vector2D.dotProduct(cax, cay, cpx, cpy);
        if (d3 < 0.) {
            final double d4 = Vector2D.dotProduct(bcx, bcy, bcx, bcy);
            if (d2 > d4) {
                return TriangleFeature.THIRD_CORNER;
            }
            return TriangleFeature.SECOND_SEGMENT;
        }

        double d4 = Vector2D.dotProduct(abx, aby, abx, aby);
        if (d1 > d4) {
            return TriangleFeature.SECOND_SEGMENT;
        }

        d4 = Vector2D.perpProduct(cax, cay, cpx, cpy);
        if (d4 < 0.) {
            return TriangleFeature.THIRD_SEGMENT;
        }

        return TriangleFeature.INSIDE;
    }

    /**
     * Replies if three points of a triangle are defined in a counter-clockwise order.
     *
     * @param x1
     *            is the X coordinate of the first point
     * @param y1
     *            is the Y coordinate of the first point
     * @param x2
     *            is the X coordinate of the second point
     * @param y2
     *            is the Y coordinate of the second point
     * @param x3
     *            is the X coordinate of the third point
     * @param y3
     *            is the Y coordinate of the third point
     * @return <code>true</code> if the three given points are defined in a counter-clockwise order.
     */
    @Pure
    static boolean isCCW(double x1, double y1, double x2, double y2, double x3, double y3) {
        return Vector2D.perpProduct(x2 - x1, y2 - y1, x3 - x1, y3 - y1) >= 0.;
    }

    /** Replies if the points of the triangle are defined in a counter-clockwise order.
     *
     * @return <code>true</code> if the triangle points are defined in a counter-clockwise order.
     */
    boolean isCCW();

    /**
     * Replies if the given point is inside the given triangle.
     *
     * @param tx1 x coordinate of the first point of the triangle.
     * @param ty1 y coordinate of the first point of the triangle.
     * @param tx2 x coordinate of the second point of the triangle.
     * @param ty2 y coordinate of the second point of the triangle.
     * @param tx3 x coordinate of the third point of the triangle.
     * @param ty3 y coordinate of the third point of the triangle.
     * @param px is the point to test.
     * @param py is the point to test.
     * @return <code>true</code> if the point is inside the triangle;
     * <code>false</code> if not.
     */
    @Pure
    static boolean containsTrianglePoint(double tx1, double ty1, double tx2, double ty2,
            double tx3, double ty3, double px, double py) {
        // Barycentric algorithm
        final double ty23 = ty2 - ty3;
        final double tx13 = tx1 - tx3;
        final double tx32 = tx3 - tx2;
        final double ty13 = ty1 - ty3;
        final double denominator = ty23 * tx13 + tx32 * ty13;
        if (denominator == 0.) {
            return false;
        }
        final double px3 = px - tx3;
        final double py3 = py - ty3;
        final double a = (ty23 * px3 + tx32 * py3) / denominator;
        final double b = (-ty13 * px3 + tx13 * py3) / denominator;
        final double c = 1. - a - b;
        return 0. <= a && a <= 1. && 0. <= b && b <= 1. && 0. <= c && c <= 1.;
    }

    /**
     * Replies if the given point is inside the given triangle.
     *
     * @param tx1 x coordinate of the first point of the triangle.
     * @param ty1 y coordinate of the first point of the triangle.
     * @param tx2 x coordinate of the second point of the triangle.
     * @param ty2 y coordinate of the second point of the triangle.
     * @param tx3 x coordinate of the third point of the triangle.
     * @param ty3 y coordinate of the third point of the triangle.
     * @param rx is the x coordinate of the rectangle.
     * @param ry is the y coordinate of the rectangle.
     * @param rwidth the width of the rectangle.
     * @param rheight the height of the rectangle.
     * @return <code>true</code> if the rectangle is inside the triangle;
     * <code>false</code> if not.
     */
    @Pure
    @SuppressWarnings({"checkstyle:parameternumber", "checkstyle:magicnumber"})
    static boolean containsTriangleRectangle(double tx1, double ty1, double tx2, double ty2,
            double tx3, double ty3, double rx, double ry, double rwidth, double rheight) {
        assert rwidth >= 0. : AssertMessages.positiveOrZeroParameter(8);
        assert rheight >= 0. : AssertMessages.positiveOrZeroParameter(9);
        return containsTrianglePoint(tx1, ty1, tx2, ty2, tx3, ty3, rx, ry)
                && containsTrianglePoint(tx1, ty1, tx2, ty2, tx3, ty3, rx + rwidth, ry)
                && containsTrianglePoint(tx1, ty1, tx2, ty2, tx3, ty3, rx + rwidth, ry + rheight)
                && containsTrianglePoint(tx1, ty1, tx2, ty2, tx3, ty3, rx, ry + rheight);
    }

    /**
     * Replies the closest point to the given point inside the given triangle.
     *
     * @param tx1 x coordinate of the first point of the triangle.
     * @param ty1 y coordinate of the first point of the triangle.
     * @param tx2 x coordinate of the second point of the triangle.
     * @param ty2 y coordinate of the second point of the triangle.
     * @param tx3 x coordinate of the third point of the triangle.
     * @param ty3 y coordinate of the third point of the triangle.
     * @param px is the point to test.
     * @param py is the point to test.
     * @param closest the closest point.
     * @param farthest the farthest point.
     */
    @SuppressWarnings({"checkstyle:parameternumber", "checkstyle:magicnumber"})
    static void findsClosestFarthestPointsTrianglePoint(double tx1, double ty1, double tx2, double ty2,
            double tx3, double ty3, double px, double py, Point2D<?, ?> closest, Point2D<?, ?> farthest) {
        assert closest != null || farthest != null : AssertMessages.oneNotNullParameter(8, 9);
        if (closest != null) {
            final double side1 = Vector2D.perpProduct(tx2 - tx1, ty2 - ty1, px - tx1, py - ty1);
            final double side2 = Vector2D.perpProduct(tx3 - tx2, ty3 - ty2, px - tx2, py - ty2);
            final double side3 = Vector2D.perpProduct(tx1 - tx3, ty1 - ty3, px - tx3, py - ty3);
            if (side1 <= 0) {
                if (side2 <= 0) {
                    closest.set(tx2, ty2);
                } else if (side3 <= 0) {
                    closest.set(tx1, ty1);
                } else {
                    Segment2afp.findsClosestPointSegmentPoint(tx1, ty1, tx2, ty2, px, py, closest);
                }
            } else if (side2 <= 0) {
                if (side3 <= 0) {
                    closest.set(tx3, ty3);
                } else {
                    Segment2afp.findsClosestPointSegmentPoint(tx2, ty2, tx3, ty3, px, py, closest);
                }
            } else if (side3 <= 0) {
                Segment2afp.findsClosestPointSegmentPoint(tx3, ty3, tx1, ty1, px, py, closest);
            } else {
                closest.set(px, py);
            }
        }

        if (farthest != null) {
            double dist;
            double x = tx1;
            double y = ty1;
            double max = Math.pow(tx1 - px, 2) + Math.pow(ty1 - py, 2);
            dist = Math.pow(tx2 - px, 2) + Math.pow(ty2 - py, 2);
            if (dist > max) {
                max = dist;
                x = tx2;
                y = ty2;
            }
            dist = Math.pow(tx3 - px, 2) + Math.pow(ty3 - py, 2);
            if (dist > max) {
                x = tx3;
                y = ty3;
            }
            farthest.set(x, y);
        }
    }

    /**
     * Replies the squared distance from the given triangle to the given point.
     *
     * <p>Caution: The points of the triangle must be defined in a CCW order.
     *
     * @param tx1 x coordinate of the first point of the triangle.
     * @param ty1 y coordinate of the first point of the triangle.
     * @param tx2 x coordinate of the second point of the triangle.
     * @param ty2 y coordinate of the second point of the triangle.
     * @param tx3 x coordinate of the third point of the triangle.
     * @param ty3 y coordinate of the third point of the triangle.
     * @param px is the point.
     * @param py is the point.
     * @return the squared distance from the triangle to the point.
     * @see #isCCW(double, double, double, double, double, double)
     */
    @Pure
    @SuppressWarnings("checkstyle:parameternumber")
    static double calculatesSquaredDistanceTrianglePoint(double tx1, double ty1, double tx2, double ty2,
            double tx3, double ty3, double px, double py) {
        final double bx;
        final double by;
        final double cx;
        final double cy;
        if (isCCW(tx1, ty1, tx2, ty2, tx3, ty3)) {
            bx = tx2;
            by = ty2;
            cx = tx3;
            cy = ty3;
        } else {
            bx = tx3;
            by = ty3;
            cx = tx2;
            cy = ty2;
        }
        final double side1 = Vector2D.perpProduct(bx - tx1, by - ty1, px - tx1, py - ty1);
        final double side2 = Vector2D.perpProduct(cx - bx, cy - by, px - bx, py - by);
        final double side3 = Vector2D.perpProduct(tx1 - cx, ty1 - cy, px - cx, py - cy);
        if (side1 < 0) {
            if (side2 < 0) {
                return Point2D.getDistanceSquaredPointPoint(px, py, tx2, ty2);
            }
            if (side3 < 0) {
                return Point2D.getDistanceSquaredPointPoint(px, py, tx1, ty1);
            }
            return Segment2afp.calculatesDistanceSquaredSegmentPoint(tx1, ty1, tx2, ty2, px, py);
        }
        if (side2 < 0) {
            if (side3 < 0) {
                return Point2D.getDistanceSquaredPointPoint(px, py, tx3, ty3);
            }
            return Segment2afp.calculatesDistanceSquaredSegmentPoint(tx2, ty2, tx3, ty3, px, py);
        }
        if (side3 < 0) {
            return Segment2afp.calculatesDistanceSquaredSegmentPoint(tx3, ty3, tx1, ty1, px, py);
        }
        return 0.;
    }

    /** Replies if a triangle and a circle are intersecting.
     *
     * @param tx1 x coordinate of the first point of the triangle.
     * @param ty1 y coordinate of the first point of the triangle.
     * @param tx2 x coordinate of the second point of the triangle.
     * @param ty2 y coordinate of the second point of the triangle.
     * @param tx3 x coordinate of the third point of the triangle.
     * @param ty3 y coordinate of the third point of the triangle.
     * @param cx is the center of the circle
     * @param cy is the center of the circle
     * @param cradius is the radius of the circle
     * @return <code>true</code> if the two shapes are intersecting; otherwise
     * <code>false</code>
     */
    @Pure
    @SuppressWarnings({"checkstyle:parameternumber", "checkstyle:magicnumber"})
    static boolean intersectsTriangleCircle(double tx1, double ty1, double tx2, double ty2,
            double tx3, double ty3, double cx, double cy, double cradius) {
        assert cradius >= 0 : AssertMessages.positiveOrZeroParameter(8);
        final double distance = calculatesSquaredDistanceTrianglePoint(
                tx1, ty1, tx2, ty2, tx3, ty3, cx, cy);
        return distance < cradius * cradius;
    }

    /** Replies if a triangle and an ellipse are intersecting.
     *
     * @param tx1 x coordinate of the first point of the triangle.
     * @param ty1 y coordinate of the first point of the triangle.
     * @param tx2 x coordinate of the second point of the triangle.
     * @param ty2 y coordinate of the second point of the triangle.
     * @param tx3 x coordinate of the third point of the triangle.
     * @param ty3 y coordinate of the third point of the triangle.
     * @param ex is the position of the ellipse
     * @param ey is the position of the ellipse
     * @param ewidth is the width of the ellipse
     * @param eheight is the height of the ellipse
     * @return <code>true</code> if the two shapes are intersecting; otherwise
     * <code>false</code>
     */
    @Pure
    @SuppressWarnings({"checkstyle:parameternumber", "checkstyle:magicnumber"})
    static boolean intersectsTriangleEllipse(double tx1, double ty1, double tx2, double ty2,
            double tx3, double ty3, double ex, double ey, double ewidth, double eheight) {
        assert ewidth >= 0 : AssertMessages.positiveOrZeroParameter(8);
        assert eheight >= 0 : AssertMessages.positiveOrZeroParameter(9);
        final double a = ewidth / 2.;
        final double b = eheight / 2.;
        final double centerX = ex + a;
        final double centerY = ey + b;
        final double x1 = (tx1 - centerX) / a;
        final double y1 = (ty1 - centerY) / b;
        final double x2 = (tx2 - centerX) / a;
        final double y2 = (ty2 - centerY) / b;
        final double x3 = (tx3 - centerX) / a;
        final double y3 = (ty3 - centerY) / b;
        final double distance = calculatesSquaredDistanceTrianglePoint(
                x1, y1, x2, y2, x3, y3, 0, 0);
        return distance < 1.;
    }

    /** Replies if a triangle and a segment are intersecting.
     *
     * @param tx1 x coordinate of the first point of the triangle.
     * @param ty1 y coordinate of the first point of the triangle.
     * @param tx2 x coordinate of the second point of the triangle.
     * @param ty2 y coordinate of the second point of the triangle.
     * @param tx3 x coordinate of the third point of the triangle.
     * @param ty3 y coordinate of the third point of the triangle.
     * @param sx1 x coordinate of the first point of the segment.
     * @param sy1 y coordinate of the first point of the segment.
     * @param sx2 x coordinate of the second point of the segment.
     * @param sy2 y coordinate of the second point of the segment.
     * @return <code>true</code> if the two shapes are intersecting; otherwise
     * <code>false</code>
     */
    @Pure
    @SuppressWarnings({"checkstyle:parameternumber", "checkstyle:npathcomplexity"})
    static boolean intersectsTriangleSegment(double tx1, double ty1, double tx2, double ty2,
            double tx3, double ty3, double sx1, double sy1, double sx2, double sy2) {
        // Separated axis theory on 4 axis (3 directions of the triangle, 1 direction of the segment)
        double vx;
        double vy;
        double min1;
        double max1;
        double min2;
        double max2;
        double a;
        final double[] coordinates = new double[] {
            tx2 - tx1, ty2 - ty1,
            tx3 - tx2, ty3 - ty3,
            tx1 - tx3, ty1 - ty3,
            sx2 - sx1, sy2 - sy1,
        };
        for (int i = 0; i < coordinates.length; i += 2) {
            vx = coordinates[i];
            vy = coordinates[i + 1];
            min1 = Vector2D.perpProduct(vx, vy, tx1, ty1);
            max1 = min1;
            a = Vector2D.perpProduct(vx, vy, tx2, ty2);
            if (a < min1) {
                min1 = a;
            }
            if (a > max1) {
                max1 = a;
            }
            a = Vector2D.perpProduct(vx, vy, tx3, ty3);
            if (a < min1) {
                min1 = a;
            }
            if (a > max1) {
                max1 = a;
            }
            min2 = Vector2D.perpProduct(vx, vy, sx1, sy1);
            max2 = min2;
            a = Vector2D.perpProduct(vx, vy, sx2, sy2);
            if (a < min2) {
                min2 = a;
            }
            if (a > max2) {
                max2 = a;
            }
            if (max1 <= min2 || max2 <= min1) {
                return false;
            }
        }
        return true;
    }

    /** Replies if two triangles are intersecting.
     * The first triangle must be CCW ordered.
     *
     * @param t1x1 x coordinate of the first point of the first triangle.
     * @param t1y1 y coordinate of the first point of the first triangle.
     * @param t1x2 x coordinate of the second point of the first triangle.
     * @param t1y2 y coordinate of the second point of the first triangle.
     * @param t1x3 x coordinate of the third point of the first triangle.
     * @param t1y3 y coordinate of the third point of the first triangle.
     * @param t2x1 x coordinate of the first point of the second triangle.
     * @param t2y1 y coordinate of the first point of the second triangle.
     * @param t2x2 x coordinate of the second point of the second triangle.
     * @param t2y2 y coordinate of the second point of the second triangle.
     * @param t2x3 x coordinate of the third point of the second triangle.
     * @param t2y3 y coordinate of the third point of the second triangle.
     * @return <code>true</code> if the two shapes are intersecting; otherwise
     * <code>false</code>
     */
    @Pure
    @SuppressWarnings({"checkstyle:parameternumber", "checkstyle:magicnumber"})
    static boolean intersectsTriangleTriangle(double t1x1, double t1y1, double t1x2, double t1y2,
            double t1x3, double t1y3, double t2x1, double t2y1, double t2x2, double t2y2,
            double t2x3, double t2y3) {
        assert isCCW(t1x1, t1y1, t1x2, t1y2, t1x3, t1y3) : AssertMessages.ccwParameters(0, 1, 2, 3, 4, 5);

        final double[] coordinates = new double[] {
            t1x2 - t1x1, t1x1, t1y2 - t1y1, t1y1,
            t2x2 - t2x1, t2x1, t2y2 - t2y1, t2y1,
            t1x3 - t1x2, t1x2, t1y3 - t1y2, t1y2,
            t2x3 - t2x2, t2x2, t2y3 - t2y2, t2y2,
            t1x1 - t1x3, t1x3, t1y1 - t1y3, t1y3,
            t2x1 - t2x3, t2x3, t2y1 - t2y3, t2y3,
        };
        for (int i = 0; i < coordinates.length; i += 8) {
            final double a = coordinates[i];
            double ox = coordinates[i + 1];
            final double b = coordinates[i + 2];
            double oy = coordinates[i + 3];
            if ((Vector2D.perpProduct(a, b, t2x1 - ox, t2y1 - oy) <= 0.)
                    && (Vector2D.perpProduct(a, b, t2x2 - ox, t2y2 - oy) <= 0.)
                    && (Vector2D.perpProduct(a, b, t2x3 - ox, t2y3 - oy) <= 0.)) {
                return false;
            }
            final double c = coordinates[i + 4];
            ox = coordinates[i + 5];
            final double d = coordinates[i + 6];
            oy = coordinates[i + 7];
            if ((Vector2D.perpProduct(c, d, t1x1 - ox, t1y1 - oy) <= 0.)
                    && (Vector2D.perpProduct(c, d, t1x2 - ox, t1y2 - oy) <= 0.)
                    && (Vector2D.perpProduct(c, d, t1x3 - ox, t1y3 - oy) <= 0.)) {
                return false;
            }
        }

        return  true;
    }

    /** Replies if a triangle and a rectangle are intersecting.
     *
     * @param tx1 x coordinate of the first point of the triangle.
     * @param ty1 y coordinate of the first point of the triangle.
     * @param tx2 x coordinate of the second point of the triangle.
     * @param ty2 y coordinate of the second point of the triangle.
     * @param tx3 x coordinate of the third point of the triangle.
     * @param ty3 y coordinate of the third point of the triangle.
     * @param rx x coordinate of the minimum corner of the rectangle.
     * @param ry y coordinate of the minimum corner of the rectangle.
     * @param rwidth width of the rectangle.
     * @param rheight height of the rectangle.
     * @return <code>true</code> if the two shapes are intersecting; otherwise
     * <code>false</code>
     */
    @Pure
    @SuppressWarnings({"checkstyle:parameternumber", "checkstyle:magicnumber"})
    static boolean intersectsTriangleRectangle(double tx1, double ty1, double tx2, double ty2,
            double tx3, double ty3, double rx, double ry, double rwidth, double rheight) {
        assert rwidth >= 0. : AssertMessages.positiveOrZeroParameter(8);
        assert rheight >= 0. : AssertMessages.positiveOrZeroParameter(9);
        // Test triangle segment intersection with the rectangle
        final double rx2 = rx + rwidth;
        final double ry2 = ry + rheight;
        if (Rectangle2afp.intersectsRectangleSegment(rx, ry, rx2, ry2, tx1, ty1, tx2, ty2)
                || Rectangle2afp.intersectsRectangleSegment(rx, ry, rx2, ry2, tx2, ty2, tx3, ty3)
                || Rectangle2afp.intersectsRectangleSegment(rx, ry, rx2, ry2, tx3, ty3, tx1, ty1)) {
            return true;
        }
        // Triangle may enclose the rectangle.
        return containsTriangleRectangle(tx1, ty1, tx2, ty2, tx3, ty3, rx, ry, rwidth, rheight);
    }

    @Pure
    @Override
    default boolean equalsToShape(IT shape) {
        if (shape == null) {
            return false;
        }
        if (shape == this) {
            return true;
        }
        return getX1() == shape.getX1()
                && getY1() == shape.getY1()
                && getX2() == shape.getX2()
                && getY2() == shape.getY2()
                && getX3() == shape.getX3()
                && getY3() == shape.getY3();
    }

    /** Replies the first point X.
     *
     * @return the first point x.
     */
    @Pure
    double getX1();

    /** Replies the first point Y.
     *
     * @return the first point y.
     */
    @Pure
    double getY1();

    /** Replies the second point X.
     *
     * @return the second point x.
     */
    @Pure
    double getX2();

    /** Replies the second point Y.
     *
     * @return the second point y.
     */
    @Pure
    double getY2();

    /** Replies the third point X.
     *
     * @return the third point x.
     */
    @Pure
    double getX3();

    /** Replies the third point Y.
     *
     * @return the third point y.
     */
    @Pure
    double getY3();

    /** Replies the first point.
     *
     * @return a copy of the first point.
     */
    @Pure
    default P getP1() {
        return getGeomFactory().newPoint(getX1(), getY1());
    }

    /** Replies the second point.
     *
     * @return a copy of the second point.
     */
    @Pure
    default P getP2() {
        return getGeomFactory().newPoint(getX2(), getY2());
    }

    /** Replies the third point.
     *
     * @return a copy of the third point.
     */
    @Pure
    default P getP3() {
        return getGeomFactory().newPoint(getX3(), getY3());
    }

    /** Change the first point.
     *
     * @param point the point.
     */
    default void setP1(Point2D<?, ?> point) {
        assert point != null : AssertMessages.notNullParameter();
        setX1(point.getX());
        setY1(point.getY());
    }

    /** Change the first point.
     *
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     */
    default void setP1(double x, double y) {
        setX1(x);
        setY1(y);
    }

    /** Change the second point.
     *
     * @param point the point.
     */
    default void setP2(Point2D<?, ?> point) {
        assert point != null : AssertMessages.notNullParameter();
        setX2(point.getX());
        setY2(point.getY());
    }

    /** Change the second point.
     *
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     */
    default void setP2(double x, double y) {
        setX2(x);
        setY2(y);
    }

    /** Change the third point.
     *
     * @param point the point.
     */
    default void setP3(Point2D<?, ?> point) {
        assert point != null : AssertMessages.notNullParameter();
        setX3(point.getX());
        setY3(point.getY());
    }

    /** Change the third point.
     *
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     */
    default void setP3(double x, double y) {
        setX3(x);
        setY3(y);
    }

    /** Change the x coordinate of the first point.
     *
     * @param x x coordinate of the point.
     */
    void setX1(double x);

    /** Change the y coordinate of the first point.
     *
     * @param y y coordinate of the point.
     */
    void setY1(double y);

    /** Change the x coordinate of the second point.
     *
     * @param x x coordinate of the point.
     */
    void setX2(double x);

    /** Change the y coordinate of the second point.
     *
     * @param y y coordinate of the point.
     */
    void setY2(double y);

    /** Change the x coordinate of the third point.
     *
     * @param x x coordinate of the point.
     */
    void setX3(double x);

    /** Change the y coordinate of the third point.
     *
     * @param y y coordinate of the point.
     */
    void setY3(double y);

    /** Change the triangle.
     *
     * @param x1 x coordinate of the first point.
     * @param y1 y coordinate of the first point.
     * @param x2 x coordinate of the second point.
     * @param y2 y coordinate of the second point.
     * @param x3 x coordinate of the third point.
     * @param y3 y coordinate of the third point.
     */
    void set(double x1, double y1, double x2, double y2, double x3, double y3);

    @Override
    default void set(IT shape) {
        assert shape != null : AssertMessages.notNullParameter();
        set(shape.getX1(), shape.getY1(), shape.getX2(), shape.getY2(), shape.getX3(), shape.getY3());
    }

    @Override
    default void clear() {
        set(0, 0, 0, 0, 0, 0);
    }

    @Override
    @SuppressWarnings("checkstyle:npathcomplexity")
    default void toBoundingBox(B box) {
        assert box != null : AssertMessages.notNullParameter();
        double minx = getX1();
        double maxx = minx;
        if (getX2() < minx) {
            minx = getX2();
        }
        if (getX2() > maxx) {
            maxx = getX2();
        }
        double miny = getY1();
        double maxy = miny;
        if (getY2() < miny) {
            miny = getY2();
        }
        if (getY2() > maxy) {
            maxy = getY2();
        }
        if (getX3() < minx) {
            minx = getX3();
        }
        if (getX3() > maxx) {
            maxx = getX3();
        }
        if (getY3() < miny) {
            miny = getY3();
        }
        if (getY3() > maxy) {
            maxy = getY3();
        }
        box.setFromCorners(minx, miny, maxx, maxy);
    }

    @Override
    default boolean isEmpty() {
        final double x = getX1();
        final double y = getY1();
        return x == getX2() && x == getX3() && y == getY2() && y == getY3();
    }

    @Pure
    @Override
    default double getDistanceSquared(Point2D<?, ?> pt) {
        assert pt != null : AssertMessages.notNullParameter();
        return calculatesSquaredDistanceTrianglePoint(getX1(), getY1(), getX2(), getY2(), getX3(), getY3(), pt.getX(), pt.getY());
    }

    @Pure
    @Override
    default double getDistanceL1(Point2D<?, ?> pt) {
        assert pt != null : AssertMessages.notNullParameter();
        final Point2D<?, ?> r = getClosestPointTo(pt);
        return r.getDistanceL1(pt);
    }

    @Pure
    @Override
    default double getDistanceLinf(Point2D<?, ?> pt) {
        assert pt != null : AssertMessages.notNullParameter();
        final Point2D<?, ?> r = getClosestPointTo(pt);
        return r.getDistanceLinf(pt);
    }

    @Pure
    @Override
    default boolean contains(double x, double y) {
        return containsTrianglePoint(getX1(), getY1(), getX2(), getY2(), getX3(), getY3(), x, y);
    }

    @Override
    default boolean contains(Rectangle2afp<?, ?, ?, ?, ?, ?> rectangle) {
        assert rectangle != null : AssertMessages.notNullParameter();
        return containsTriangleRectangle(getX1(), getY1(), getX2(), getY2(), getX3(), getY3(),
                rectangle.getMinX(), rectangle.getMinY(), rectangle.getWidth(), rectangle.getHeight());
    }

    @Override
    default void translate(double dx, double dy) {
        setP1(getX1() + dx, getY1() + dy);
        setP2(getX2() + dx, getY2() + dy);
        setP3(getX3() + dx, getY3() + dy);
    }

    @Pure
    @Override
    default boolean intersects(Rectangle2afp<?, ?, ?, ?, ?, ?> rectangle) {
        assert rectangle != null : AssertMessages.notNullParameter();
        return intersectsTriangleRectangle(getX1(), getY1(), getX2(), getY2(), getX3(), getY3(),
                rectangle.getMinX(), rectangle.getMinY(), rectangle.getWidth(), rectangle.getHeight());
    }

    @Pure
    @Override
    default boolean intersects(Ellipse2afp<?, ?, ?, ?, ?, ?> ellipse) {
        assert ellipse != null : AssertMessages.notNullParameter();
        return intersectsTriangleEllipse(getX1(), getY1(), getX2(), getY2(), getX3(), getY3(),
                ellipse.getMinX(), ellipse.getMinY(), ellipse.getWidth(), ellipse.getHeight());
    }

    @Pure
    @Override
    default boolean intersects(Circle2afp<?, ?, ?, ?, ?, ?> circle) {
        assert circle != null : AssertMessages.notNullParameter();
        return intersectsTriangleCircle(getX1(), getY1(), getX2(), getY2(), getX3(), getY3(),
                circle.getX(), circle.getY(), circle.getRadius());
    }

    @Pure
    @Override
    default boolean intersects(Segment2afp<?, ?, ?, ?, ?, ?> segment) {
        assert segment != null : AssertMessages.notNullParameter();
        return intersectsTriangleSegment(getX1(), getY1(), getX2(), getY2(), getX3(), getY3(),
                segment.getX1(), segment.getY1(), segment.getX2(), segment.getY2());
    }

    @Pure
    @Override
    default boolean intersects(Triangle2afp<?, ?, ?, ?, ?, ?> triangle) {
        assert triangle != null : AssertMessages.notNullParameter();
        if (isCCW()) {
            return intersectsTriangleTriangle(getX1(), getY1(), getX2(), getY2(), getX3(), getY3(),
                    triangle.getX1(), triangle.getY1(), triangle.getX2(), triangle.getY2(), triangle.getX3(), triangle.getY3());
        }
        return intersectsTriangleTriangle(getX1(), getY1(), getX3(), getY3(), getX2(), getY2(),
                triangle.getX1(), triangle.getY1(), triangle.getX2(), triangle.getY2(), triangle.getX3(), triangle.getY3());
    }

    @Pure
    @Override
    default boolean intersects(OrientedRectangle2afp<?, ?, ?, ?, ?, ?> sorientedRectangle) {
        assert sorientedRectangle != null : AssertMessages.notNullParameter();
        return OrientedRectangle2afp.intersectsOrientedRectangleTriangle(
                sorientedRectangle.getCenterX(), sorientedRectangle.getCenterY(),
                sorientedRectangle.getFirstAxisX(), sorientedRectangle.getFirstAxisY(), sorientedRectangle.getFirstAxisExtent(),
                sorientedRectangle.getSecondAxisExtent(),
                getX1(), getY1(), getX2(), getY2(), getX3(), getY3());
    }

    @Pure
    @Override
    default boolean intersects(Parallelogram2afp<?, ?, ?, ?, ?, ?> parallelogram) {
        assert parallelogram != null : AssertMessages.notNullParameter();
        return Parallelogram2afp.intersectsParallelogramTriangle(
                parallelogram.getCenterX(), parallelogram.getCenterY(),
                parallelogram.getFirstAxisX(),
                parallelogram.getFirstAxisY(),
                parallelogram.getFirstAxisExtent(),
                parallelogram.getSecondAxisX(),
                parallelogram.getSecondAxisY(),
                parallelogram.getSecondAxisExtent(),
                getX1(), getY1(),
                getX2(), getY2(),
                getX3(), getY3());
    }

    @Pure
    @Override
    default boolean intersects(PathIterator2afp<?> iterator) {
        assert iterator != null : AssertMessages.notNullParameter();
        final int mask = iterator.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
        final int crossings = Path2afp.calculatesCrossingsPathIteratorTriangleShadow(
                0,
                iterator,
                getX1(), getY1(), getX2(), getY2(), getX3(), getY3(),
                CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
        return crossings == GeomConstants.SHAPE_INTERSECTS
                || (crossings & mask) != 0;
    }

    @Pure
    @Override
    default boolean intersects(RoundRectangle2afp<?, ?, ?, ?, ?, ?> roundRectangle) {
        assert roundRectangle != null : AssertMessages.notNullParameter();
        return roundRectangle.intersects(getPathIterator());
    }

    @Pure
    @Override
    default boolean intersects(MultiShape2afp<?, ?, ?, ?, ?, ?, ?> multishape) {
        assert multishape != null : AssertMessages.notNullParameter();
        return multishape.intersects(this);
    }

    @Pure
    @Override
    default P getClosestPointTo(Point2D<?, ?> pt) {
        assert pt != null : AssertMessages.notNullParameter();
        final P point = getGeomFactory().newPoint();
        findsClosestFarthestPointsTrianglePoint(getX1(), getY1(), getX2(), getY2(), getX3(), getY3(),
                pt.getX(), pt.getY(), point, null);
        return point;
    }

    @Pure
    @Override
    default P getClosestPointTo(Circle2afp<?, ?, ?, ?, ?, ?> circle) {
        return getClosestPointTo(circle.getCenter());
    }

    @Override
    @Unefficient
    default P getClosestPointTo(Ellipse2afp<?, ?, ?, ?, ?, ?> ellipse) {
        assert ellipse != null : AssertMessages.notNullParameter();
        final P point = getGeomFactory().newPoint();
        Path2afp.findsClosestPointPathIteratorPathIterator(getPathIterator(), ellipse.getPathIterator(), point);
        return point;
    }

    @Override
    @Unefficient
    default P getClosestPointTo(Rectangle2afp<?, ?, ?, ?, ?, ?> rectangle) {
        assert rectangle != null : AssertMessages.notNullParameter();
        final P point = getGeomFactory().newPoint();
        Path2afp.findsClosestPointPathIteratorPathIterator(getPathIterator(), rectangle.getPathIterator(), point);
        return point;
    }

    @Override
    @Unefficient
    default P getClosestPointTo(Segment2afp<?, ?, ?, ?, ?, ?> segment) {
        assert segment != null : AssertMessages.notNullParameter();
        final P point = getGeomFactory().newPoint();
        Path2afp.findsClosestPointPathIteratorPathIterator(getPathIterator(), segment.getPathIterator(), point);
        return point;
    }

    @Override
    @Unefficient
    default P getClosestPointTo(Triangle2afp<?, ?, ?, ?, ?, ?> triangle) {
        assert triangle != null : AssertMessages.notNullParameter();
        final P point = getGeomFactory().newPoint();
        Path2afp.findsClosestPointPathIteratorPathIterator(getPathIterator(), triangle.getPathIterator(), point);
        return point;
    }

    @Override
    @Unefficient
    default P getClosestPointTo(OrientedRectangle2afp<?, ?, ?, ?, ?, ?> orientedRectangle) {
        assert orientedRectangle != null : AssertMessages.notNullParameter();
        final P point = getGeomFactory().newPoint();
        Path2afp.findsClosestPointPathIteratorPathIterator(getPathIterator(), orientedRectangle.getPathIterator(), point);
        return point;
    }

    @Override
    @Unefficient
    default P getClosestPointTo(Parallelogram2afp<?, ?, ?, ?, ?, ?> parallelogram) {
        assert parallelogram != null : AssertMessages.notNullParameter();
        final P point = getGeomFactory().newPoint();
        Path2afp.findsClosestPointPathIteratorPathIterator(getPathIterator(), parallelogram.getPathIterator(), point);
        return point;
    }

    @Override
    @Unefficient
    default P getClosestPointTo(RoundRectangle2afp<?, ?, ?, ?, ?, ?> roundRectangle) {
        assert roundRectangle != null : AssertMessages.notNullParameter();
        final P point = getGeomFactory().newPoint();
        Path2afp.findsClosestPointPathIteratorPathIterator(getPathIterator(), roundRectangle.getPathIterator(), point);
        return point;
    }

    @Override
    @Unefficient
    default P getClosestPointTo(Path2afp<?, ?, ?, ?, ?, ?> path) {
        assert path != null : AssertMessages.notNullParameter();
        final P point = getGeomFactory().newPoint();
        Path2afp.findsClosestPointPathIteratorPathIterator(getPathIterator(), path.getPathIterator(), point);
        return point;
    }

    @Pure
    @Override
    default P getFarthestPointTo(Point2D<?, ?> pt) {
        assert pt != null : AssertMessages.notNullParameter();
        final P point = getGeomFactory().newPoint();
        findsClosestFarthestPointsTrianglePoint(getX1(), getY1(), getX2(), getY2(), getX3(), getY3(),
                pt.getX(), pt.getY(), null, point);
        return point;
    }

    @Pure
    @Override
    default PathIterator2afp<IE> getPathIterator(Transform2D transform) {
        if (transform == null || transform.isIdentity()) {
            return new TrianglePathIterator<>(this);
        }
        return new TransformedTrianglePathIterator<>(this, transform);
    }

    /** Abstract iterator on the path elements of the triangle.
     *
     * @param <T> the type of the path elements.
     * @author $Author: sgalland$
     * @version $FullVersion$
     * @mavengroupid $GroupId$
     * @mavenartifactid $ArtifactId$
     */
    abstract class AbstractTrianglePathIterator<T extends PathElement2afp> implements PathIterator2afp<T> {

        /** Number of path elements.
         */
        protected static final int NUMBER_ELEMENTS = 3;

        /** The iterated shape.
         */
        protected final Triangle2afp<?, ?, T, ?, ?, ?> triangle;

        /** Constructor.
         * @param triangle the iterated shape.
         */
        public AbstractTrianglePathIterator(Triangle2afp<?, ?, T, ?, ?, ?> triangle) {
            assert triangle != null : AssertMessages.notNullParameter();
            this.triangle = triangle;
        }

        @Override
        public GeomFactory2afp<T, ?, ?, ?> getGeomFactory() {
            return this.triangle.getGeomFactory();
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

        @Override
        public boolean isCurved() {
            return false;
        }

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

    /** Iterator on the path elements of the triangle.
     *
     * @param <T> the type of the path elements.
     * @author $Author: sgalland$
     * @version $FullVersion$
     * @mavengroupid $GroupId$
     * @mavenartifactid $ArtifactId$
     */
    class TrianglePathIterator<T extends PathElement2afp> extends AbstractTrianglePathIterator<T> {

        private double x1;

        private double y1;

        private double x2;

        private double y2;

        private double x3;

        private double y3;

        private int index;

        private double movex;

        private double movey;

        private double lastx;

        private double lasty;

        /** Constructor.
         * @param triangle the triangle to iterate on.
         */
        public TrianglePathIterator(Triangle2afp<?, ?, T, ?, ?, ?> triangle) {
            super(triangle);
            if (triangle.isEmpty()) {
                this.index = NUMBER_ELEMENTS;
            } else {
                this.x1 = triangle.getX1();
                this.y1 = triangle.getY1();
                this.x2 = triangle.getX2();
                this.y2 = triangle.getY2();
                this.x3 = triangle.getX3();
                this.y3 = triangle.getY3();
                this.index = -1;
            }
        }

        @Override
        public PathIterator2afp<T> restartIterations() {
            return new TrianglePathIterator<>(this.triangle);
        }

        @Pure
        @Override
        public boolean hasNext() {
            return this.index < NUMBER_ELEMENTS;
        }

        @Override
        public T next() {
            if (this.index >= NUMBER_ELEMENTS) {
                throw new NoSuchElementException();
            }
            final int idx = this.index;
            ++this.index;
            if (idx < 0) {
                this.movex = this.x1;
                this.movey = this.y1;
                this.lastx = this.movex;
                this.lasty = this.movey;
                return getGeomFactory().newMovePathElement(
                        this.lastx, this.lasty);
            }
            final double ppx = this.lastx;
            final double ppy = this.lasty;
            switch (idx) {
            case 0:
                this.lastx = this.x2;
                this.lasty = this.y2;
                return getGeomFactory().newLinePathElement(
                        ppx, ppy,
                        this.lastx, this.lasty);
            case 1:
                this.lastx = this.x3;
                this.lasty = this.y3;
                return getGeomFactory().newLinePathElement(
                        ppx, ppy,
                        this.lastx, this.lasty);
            default:
                return getGeomFactory().newClosePathElement(
                        this.lastx, this.lasty,
                        this.movex, this.movey);
            }
        }

    }

    /** Iterator on the path elements of the circle.
     *
     * @param <T> the type of the path elements.
     * @author $Author: sgalland$
     * @version $FullVersion$
     * @mavengroupid $GroupId$
     * @mavenartifactid $ArtifactId$
     */
    class TransformedTrianglePathIterator<T extends PathElement2afp> extends AbstractTrianglePathIterator<T> {

        private final Transform2D transform;

        private final Point2D<?, ?> tmpPoint;

        private double x1;

        private double y1;

        private double x2;

        private double y2;

        private double x3;

        private double y3;

        private int index;

        private double movex;

        private double movey;

        private double lastx;

        private double lasty;

        /** Constructor.
         * @param triangle the iterated triangle.
         * @param transform the transformation to apply.
         */
        public TransformedTrianglePathIterator(Triangle2afp<?, ?, T, ?, ?, ?> triangle, Transform2D transform) {
            super(triangle);
            assert transform != null : AssertMessages.notNullParameter(1);
            this.transform = transform;
            if (triangle.isEmpty()) {
                this.index = NUMBER_ELEMENTS;
                this.tmpPoint = null;
            } else {
                this.tmpPoint = new InnerComputationPoint2afp();
                this.x1 = triangle.getX1();
                this.y1 = triangle.getY1();
                this.x2 = triangle.getX2();
                this.y2 = triangle.getY2();
                this.x3 = triangle.getX3();
                this.y3 = triangle.getY3();
                this.index = -1;
            }
        }

        @Override
        public PathIterator2afp<T> restartIterations() {
            return new TransformedTrianglePathIterator<>(this.triangle, this.transform);
        }

        @Pure
        @Override
        public boolean hasNext() {
            return this.index < NUMBER_ELEMENTS;
        }

        @Override
        public T next() {
            if (this.index >= NUMBER_ELEMENTS) {
                throw new NoSuchElementException();
            }
            final int idx = this.index;
            ++this.index;
            if (idx < 0) {
                this.tmpPoint.set(this.x1, this.y1);
                this.transform.transform(this.tmpPoint);
                this.movex = this.tmpPoint.getX();
                this.movey = this.tmpPoint.getY();
                this.lastx = this.movex;
                this.lasty = this.movey;
                return getGeomFactory().newMovePathElement(
                        this.lastx, this.lasty);
            }
            final double ppx = this.lastx;
            final double ppy = this.lasty;
            switch (idx) {
            case 0:
                this.tmpPoint.set(this.x2, this.y2);
                this.transform.transform(this.tmpPoint);
                this.lastx = this.tmpPoint.getX();
                this.lasty = this.tmpPoint.getY();
                return getGeomFactory().newLinePathElement(
                        ppx, ppy,
                        this.lastx, this.lasty);
            case 1:
                this.tmpPoint.set(this.x3, this.y3);
                this.transform.transform(this.tmpPoint);
                this.lastx = this.tmpPoint.getX();
                this.lasty = this.tmpPoint.getY();
                return getGeomFactory().newLinePathElement(
                        ppx, ppy,
                        this.lastx, this.lasty);
            default:
                return getGeomFactory().newClosePathElement(
                        this.lastx, this.lasty,
                        this.movex, this.movey);
            }
        }

    }

    /** Features of a triangle.
     *
     * @author $Author: sgalland$
     * @version $FullVersion$
     * @mavengroupid $GroupId$
     * @mavenartifactid $ArtifactId$
     */
    enum TriangleFeature {

        /** Inside of the triangle.
         */
        INSIDE,

        /** The first triangle point.
         */
        FIRST_CORNER,

        /** The second triangle point.
         */
        SECOND_CORNER,

        /** The third triangle point.
         */
        THIRD_CORNER,

        /** The first triangle segment.
         */
        FIRST_SEGMENT,

        /** The second triangle segment.
         */
        SECOND_SEGMENT,

        /** The third triangle segment.
         */
        THIRD_SEGMENT;

    }

}
