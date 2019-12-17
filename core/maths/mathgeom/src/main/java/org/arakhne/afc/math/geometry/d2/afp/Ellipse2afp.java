/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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
import org.arakhne.afc.math.geometry.d2.afp.Circle2afp.AbstractCirclePathIterator;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** Fonctional interface that represented a 2D ellipse on a plane.
 *
 * @param <ST> is the type of the general implementation.
 * @param <IT> is the type of the implementation of this shape.
 * @param <IE> is the type of the path elements.
 * @param <P> is the type of the points.
 * @param <V> is the type of the vectors.
 * @param <B> is the type of the bounding boxes.
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Ellipse2afp<
        ST extends Shape2afp<?, ?, IE, P, V, B>,
        IT extends Ellipse2afp<?, ?, IE, P, V, B>,
        IE extends PathElement2afp,
        P extends Point2D<? super P, ? super V>,
        V extends Vector2D<? super V, ? super P>,
        B extends Rectangle2afp<?, ?, IE, P, V, B>>
        extends RectangularShape2afp<ST, IT, IE, P, V, B> {

    /**
     * Replies if the given point is inside the given ellipse.
     *
     * @param ellx is the min corner of the ellipse.
     * @param elly is the min corner of the ellipse.
     * @param ellw is the width of the ellipse.
     * @param ellh is the height of the ellipse.
     * @param px is the point to test.
     * @param py is the point to test.
     * @return <code>true</code> if the point is inside the ellipse;
     * <code>false</code> if not.
     */
    @Pure
    @SuppressWarnings("checkstyle:magicnumber")
    static boolean containsEllipsePoint(double ellx, double elly, double ellw, double ellh, double px, double py) {
        assert ellw >= 0. : AssertMessages.positiveOrZeroParameter(2);
        assert ellh >= 0. : AssertMessages.positiveOrZeroParameter(3);
        // Copied from AWT Ellipse2D

        // Normalize the coordinates compared to the ellipse
        // having a center at 0, 0 and a radius of 0.5.
        if (ellw <= 0. || ellh <= 0.) {
            return false;
        }
        final double normx = (px - ellx) / ellw - 0.5;
        final double normy = (py - elly) / ellh - 0.5;
        return (normx * normx + normy * normy) <= 0.25;
    }

    /** Replies the closest point from the given point in the solid ellipse.
     * A solid ellipse is an ellipse with a border and an interior area.
     *
     * @param px is the coordinate of the point.
     * @param py is the coordinate of the point.
     * @param ex is the coordinate of the min corner of the ellipse
     * @param ey is the coordinate of the min corner of the ellipse
     * @param ew is the width of the ellipse
     * @param eh is the height of the ellipse
     * @param result the closest point in the ellipse.
     * @see #findsClosestPointShallowEllipsePoint(double, double, double, double, double, double, Point2D)
     */
    @Unefficient
    @SuppressWarnings("checkstyle:magicnumber")
    static void findsClosestPointSolidEllipsePoint(
            double px, double py, double ex, double ey, double ew, double eh,
            Point2D<?, ?> result) {
        assert ew >= 0. : AssertMessages.positiveOrZeroParameter(4);
        assert eh >= 0. : AssertMessages.positiveOrZeroParameter(5);
        assert result != null : AssertMessages.notNullParameter(6);

        // Translate the point in the local ellipse's coordinate system.
        final double e0;
        final double e1;
        final double translateX;
        final double translateY;
        double pointX;
        double pointY;
        final double[] data;

        // The basic algorithm assumes ew >= eh => swap coordinates.
        if (ew >= eh) {
            e0 = ew / 2.;
            e1 = eh / 2.;
            translateX = ex + e0;
            translateY = ey + e1;
            pointX = px - translateX;
            pointY = py - translateY;
        } else {
            e0 = eh / 2;
            e1 = ew / 2;
            translateX = ex + e1;
            translateY = ey + e0;
            pointX = py - translateY;
            pointY = px - translateX;
        }

        // The basic algorithm work only for the positive quadrant => switch the coordinates if necessary.
        if (pointX < 0.) {
            if (pointY < 0.) {
                data = PrivateAPI.computeClosestPointOnSolidEllipseInPositiveQuadrant(
                        -pointX, -pointY,
                        e0, e1, false);
                pointY = -data[1];
            } else {
                data = PrivateAPI.computeClosestPointOnSolidEllipseInPositiveQuadrant(
                        -pointX, pointY,
                        e0, e1, false);
                pointY = data[1];
            }
            pointX = -data[0];
        } else {
            if (pointY < 0.) {
                data = PrivateAPI.computeClosestPointOnSolidEllipseInPositiveQuadrant(
                        pointX, -pointY,
                        e0, e1, false);
                pointY = -data[1];
            } else {
                data = PrivateAPI.computeClosestPointOnSolidEllipseInPositiveQuadrant(
                        pointX, pointY,
                        e0, e1, false);
                pointY = data[1];
            }
            pointX = data[0];
        }

        // Revert translation and swaping of coordinates
        if (ew >= eh) {
            result.set(pointX + translateX, pointY + translateY);
        } else {
            result.set(pointY + translateX, pointX + translateY);
        }
    }

    /** Replies the closest point from the given point in the shallow ellipse.
     * A shallow ellipse is an ellipse with a border and not an interior area.
     *
     * @param px is the coordinate of the point.
     * @param py is the coordinate of the point.
     * @param ex is the coordinate of the min corner of the ellipse
     * @param ey is the coordinate of the min corner of the ellipse
     * @param ew is the width of the ellipse
     * @param eh is the height of the ellipse
     * @param result the closest point in the ellipse.
     * @see #findsClosestPointSolidEllipsePoint(double, double, double, double, double, double, Point2D)
     */
    @Unefficient
    @SuppressWarnings("checkstyle:magicnumber")
    static void findsClosestPointShallowEllipsePoint(double px, double py, double ex, double ey, double ew, double eh,
            Point2D<?, ?> result) {
        assert ew >= 0. : AssertMessages.positiveOrZeroParameter(4);
        assert eh >= 0. : AssertMessages.positiveOrZeroParameter(5);
        assert result != null : AssertMessages.notNullParameter(6);

        // Translate the point in the local ellipse's coordinate system.
        final double e0;
        final double e1;
        final double translateX;
        final double translateY;
        double pointX;
        double pointY;
        final double[] data;

        // The basic algorithm assumes ew >= eh => swap coordinates.
        if (ew >= eh) {
            e0 = ew / 2.;
            e1 = eh / 2.;
            translateX = ex + e0;
            translateY = ey + e1;
            pointX = px - translateX;
            pointY = py - translateY;
        } else {
            e0 = eh / 2.;
            e1 = ew / 2.;
            translateX = ex + e1;
            translateY = ey + e0;
            pointX = py - translateY;
            pointY = px - translateX;
        }

        // The basic algorithm work only for the positive quadrant => switch the coordinates if necessary.
        if (pointX < 0.) {
            if (pointY < 0.) {
                data = PrivateAPI.computeClosestPointOnShallowEllipseInPositiveQuadrant(
                        -pointX, -pointY,
                        e0, e1, false);
                pointY = -data[1];
            } else {
                data = PrivateAPI.computeClosestPointOnShallowEllipseInPositiveQuadrant(
                        -pointX, pointY,
                        e0, e1, false);
                pointY = data[1];
            }
            pointX = -data[0];
        } else {
            if (pointY < 0.) {
                data = PrivateAPI.computeClosestPointOnShallowEllipseInPositiveQuadrant(
                        pointX, -pointY,
                        e0, e1, false);
                pointY = -data[1];
            } else {
                data = PrivateAPI.computeClosestPointOnShallowEllipseInPositiveQuadrant(
                        pointX, pointY,
                        e0, e1, false);
                pointY = data[1];
            }
            pointX = data[0];
        }

        // Revert translation and swaping of coordinates
        if (ew >= eh) {
            result.set(pointX + translateX, pointY + translateY);
        } else {
            result.set(pointY + translateX, pointX + translateY);
        }
    }

    /** Replies the farthest point from the given point in the shallow ellipse.
     * A shallow ellipse is an ellipse with a border and not an interior area.
     *
     * @param px is the coordinate of the point.
     * @param py is the coordinate of the point.
     * @param ex is the coordinate of the min corner of the ellipse
     * @param ey is the coordinate of the min corner of the ellipse
     * @param ew is the width of the ellipse
     * @param eh is the height of the ellipse
     * @param result the farthest point in the ellipse.
     */
    @Unefficient
    @SuppressWarnings({"checkstyle:parameternumber", "checkstyle:magicnumber"})
    static void findsFarthestPointShallowEllipsePoint(double px, double py, double ex, double ey,
            double ew, double eh, Point2D<?, ?> result) {
        assert ew >= 0. : AssertMessages.positiveOrZeroParameter(4);
        assert eh >= 0. : AssertMessages.positiveOrZeroParameter(5);
        assert result != null : AssertMessages.notNullParameter(6);

        // Translate the point in the local ellipse's coordinate system.
        final double e0;
        final double e1;
        final double translateX;
        final double translateY;
        double pointX;
        double pointY;
        final double[] data;

        // The basic algorithm assumes ew >= eh => swap coordinates.
        if (ew >= eh) {
            e0 = ew / 2.;
            e1 = eh / 2.;
            translateX = ex + e0;
            translateY = ey + e1;
            pointX = px - translateX;
            pointY = py - translateY;
        } else {
            e0 = eh / 2.;
            e1 = ew / 2.;
            translateX = ex + e1;
            translateY = ey + e0;
            pointX = py - translateY;
            pointY = px - translateX;
        }

        // The basic algorithm work only for the positive quadrant for the ellipse, and the
        // negative quadrant for the point => switch the coordinates if necessary.
        if (pointX < 0.) {
            if (pointY < 0.) {
                data = PrivateAPI.computeFarthestPointOnShallowEllipseInPositiveQuadrant(
                        pointX, pointY,
                        e0, e1, false);
                pointY = data[1];
            } else {
                data = PrivateAPI.computeFarthestPointOnShallowEllipseInPositiveQuadrant(
                        pointX, -pointY,
                        e0, e1, false);
                pointY = -data[1];
            }
            pointX = data[0];
        } else {
            if (pointY < 0.) {
                data = PrivateAPI.computeFarthestPointOnShallowEllipseInPositiveQuadrant(
                        -pointX, pointY,
                        e0, e1, false);
                pointY = data[1];
            } else {
                data = PrivateAPI.computeFarthestPointOnShallowEllipseInPositiveQuadrant(
                        -pointX, -pointY,
                        e0, e1, false);
                pointY = -data[1];
            }
            pointX = -data[0];
        }

        // Revert translation and swaping of coordinates
        if (ew >= eh) {
            result.set(pointX + translateX, pointY + translateY);
        } else {
            result.set(pointY + translateX, pointX + translateY);
        }
    }

    /** Replies if a rectangle is inside in the ellipse.
     *
     * @param ex is the lowest corner of the ellipse.
     * @param ey is the lowest corner of the ellipse.
     * @param ewidth is the width of the ellipse.
     * @param eheight is the height of the ellipse.
     * @param rxmin is the lowest corner of the rectangle.
     * @param rymin is the lowest corner of the rectangle.
     * @param rxmax is the uppest corner of the rectangle.
     * @param rymax is the uppest corner of the rectangle.
     * @return <code>true</code> if the given rectangle is inside the ellipse;
     *     otherwise <code>false</code>.
     */
    @Pure
    @SuppressWarnings("checkstyle:magicnumber")
    static boolean containsEllipseRectangle(double ex, double ey, double ewidth, double eheight,
            double rxmin, double rymin, double rxmax, double rymax) {
        assert ewidth >= 0. : AssertMessages.positiveOrZeroParameter(2);
        assert eheight >= 0. : AssertMessages.positiveOrZeroParameter(3);
        assert rxmin <= rxmax : AssertMessages.lowerEqualParameters(4, rxmin, 6, rxmax);
        assert rymin <= rymax : AssertMessages.lowerEqualParameters(5, rymin, 7, rymax);
        final double ecx = ex + ewidth / 2.;
        final double ecy = ey + eheight / 2.;
        final double rcx = (rxmin + rxmax) / 2.;
        final double rcy = (rymin + rymax) / 2.;
        final double farX;
        if (ecx <= rcx) {
            farX = rxmax;
        } else {
            farX = rxmin;
        }
        final double farY;
        if (ecy <= rcy) {
            farY = rymax;
        } else {
            farY = rymin;
        }
        return containsEllipsePoint(ex, ey, ewidth, eheight, farX, farY);
    }

    /** Replies if two ellipses are intersecting.
     *
     * @param x1 is the lowest corner of the first ellipse.
     * @param y1 is the lowest corner of the first ellipse.
     * @param width1 is the width of the first ellipse.
     * @param height1 is the height of the first ellipse.
     * @param x2 is the lowest corner of the second ellipse.
     * @param y2 is the lowest corner of the second ellipse.
     * @param width2 is the width of the second ellipse.
     * @param height2 is the height of the second ellipse.
     * @return <code>true</code> if the two shapes are intersecting; otherwise
     * <code>false</code>
     */
    @Pure
    @Unefficient
    @SuppressWarnings("checkstyle:magicnumber")
    static boolean intersectsEllipseEllipse(double x1, double y1, double width1, double height1,
            double x2, double y2, double width2, double height2) {
        assert width1 >= 0. : AssertMessages.positiveOrZeroParameter(2);
        assert height1 >= 0. : AssertMessages.positiveOrZeroParameter(3);
        assert width2 >= 0. : AssertMessages.positiveOrZeroParameter(6);
        assert height2 >= 0. : AssertMessages.positiveOrZeroParameter(7);

        if (width2 <= 0 || height2 <= 0 || width1 <= 0 || height1 <= 0) {
            return false;
        }

        // Normalize coordinates for tranqforming the first ellipse to an origin-centric circle with radius 1.
        final double radius1 = width1 / 2;
        final double radius2 = height1 / 2;
        final double transformedX = (x2 - x1) / radius1 - 1;
        final double transformedY = (y2 - y1) / radius2 - 1;
        final double transformedWidth = width2 / radius1;
        final double transformedHeight = height2 / radius2;

        // Use the standard ellipse-circle intersection test
        return intersectsEllipseCircle(transformedX, transformedY, transformedWidth, transformedHeight, 0, 0, 1);
    }

    /** Replies if an ellipse and a circle are intersecting.
     *
     * @param ex is the lowest corner of the ellipse.
     * @param ey is the lowest corner of the ellipse.
     * @param ewidth is the width  of the ellipse.
     * @param eheight is the height of the ellipse.
     * @param cx is the center of the circle.
     * @param cy is the center of the circle.
     * @param cradius the radius of the circle.
     * @return <code>true</code> if the two shapes are intersecting; otherwise
     * <code>false</code>
     */
    @Pure
    @Unefficient
    @SuppressWarnings("checkstyle:magicnumber")
    static boolean intersectsEllipseCircle(double ex, double ey, double ewidth, double eheight,
            double cx, double cy, double cradius) {
        assert ewidth >= 0. : AssertMessages.positiveOrZeroParameter(2);
        assert eheight >= 0. : AssertMessages.positiveOrZeroParameter(3);
        assert cradius >= 0. : AssertMessages.positiveOrZeroParameter(6);
        final Point2D<?, ?> p = new InnerComputationPoint2afp();
        findsClosestPointSolidEllipsePoint(cx, cy, ex, ey, ewidth, eheight, p);
        final double dx = p.getX() - cx;
        final double dy = p.getY() - cy;
        return (dx * dx + dy * dy) < (cradius * cradius);
    }

    /** Replies if an ellipse and a line are intersecting.
     *
     * @param ex is the lowest corner of the ellipse.
     * @param ey is the lowest corner of the ellipse.
     * @param ew is the width of the ellipse.
     * @param eh is the height of the ellipse.
     * @param x1 is the first point of the line.
     * @param y1 is the first point of the line.
     * @param x2 is the second point of the line.
     * @param y2 is the second point of the line.
     * @return <code>true</code> if the two shapes are intersecting; otherwise
     * <code>false</code>
     * @see "http://blog.csharphelper.com/2012/09/24/calculate-where-a-line-segment-and-an-ellipse-intersect-in-c.aspx"
     */
    @Pure
    static boolean intersectsEllipseLine(double ex, double ey, double ew, double eh,
            double x1, double y1, double x2, double y2) {
        assert ew >= 0. : AssertMessages.positiveOrZeroParameter(2);
        assert eh >= 0. : AssertMessages.positiveOrZeroParameter(3);
        // If the ellipse or line segment are empty, return no intersections.
        if (eh <= 0 || ew <= 0) {
            return false;
        }

        // Get the semimajor and semiminor axes.
        final double a = ew / 2.;
        final double b = eh / 2.;

        // Translate so the ellipse is centered at the origin.
        final double ecx = ex + a;
        final double ecy = ey + b;
        final double px1 = x1 - ecx;
        final double py1 = y1 - ecy;
        final double px2 = x2 - ecx;
        final double py2 = y2 - ecy;

        final double sqA = a * a;
        final double sqB = b * b;
        final double vx = px2 - px1;
        final double vy = py2 - py1;

        assert sqA != 0 && sqB != 0;

        // Calculate the quadratic parameters.
        final double aParam = vx * vx / sqA + vy * vy / sqB;
        final double bParam = 2 * px1 * vx / sqA + 2 * py1 * vy / sqB;
        final double cParam = px1 * px1 / sqA + py1 * py1 / sqB - 1.;

        // Calculate the discriminant.
        final double discriminant = bParam * bParam - 4. * aParam * cParam;
        return discriminant >= 0.;
    }

    /** Replies if an ellipse and a segment are intersecting.
     *
     * @param ex is the lowest corner of the ellipse.
     * @param ey is the lowest corner of the ellipse.
     * @param ew is the width of the ellipse.
     * @param eh is the height of the ellipse.
     * @param x1 is the first point of the segment.
     * @param y1 is the first point of the segment.
     * @param x2 is the second point of the segment.
     * @param y2 is the second point of the segment.
     * @param intersectsWhenTouching indicates if there is an intersection if the segment is touching
     *     the ellipse at one point.
     * @return <code>true</code> if the two shapes are intersecting; otherwise
     * <code>false</code>
     * @see "http://blog.csharphelper.com/2012/09/24/calculate-where-a-line-segment-and-an-ellipse-intersect-in-c.aspx"
     */
    @Pure
    @SuppressWarnings("checkstyle:parameternumber")
    static boolean intersectsEllipseSegment(double ex, double ey, double ew, double eh,
            double x1, double y1, double x2, double y2, boolean intersectsWhenTouching) {
        assert ew >= 0. : AssertMessages.positiveOrZeroParameter(2);
        assert eh >= 0. : AssertMessages.positiveOrZeroParameter(3);
        // If the ellipse or line segment are empty, return no intersections.
        if (eh <= 0. || ew <= 0.) {
            return false;
        }

        // Get the semimajor and semiminor axes.
        final double a = ew / 2.;
        final double b = eh / 2.;

        // Translate so the ellipse is centered at the origin.
        final double ecx = ex + a;
        final double ecy = ey + b;
        final double px1 = x1 - ecx;
        final double py1 = y1 - ecy;
        final double px2 = x2 - ecx;
        final double py2 = y2 - ecy;

        final double sqA = a * a;
        final double sqB = b * b;
        final double vx = px2 - px1;
        final double vy = py2 - py1;

        assert sqA != 0 && sqB != 0;

        // Calculate the quadratic parameters.
        final double aParam = vx * vx / sqA + vy * vy / sqB;
        final double bParam = 2 * px1 * vx / sqA + 2 * py1 * vy / sqB;
        final double cParam = px1 * px1 / sqA + py1 * py1 / sqB - 1;

        // Calculate the discriminant.
        final double discriminant = bParam * bParam - 4 * aParam * cParam;
        if (discriminant < 0.) {
            // No solution
            return false;
        }

        if (discriminant == 0.) {
            // One real solution.
            if (intersectsWhenTouching) {
                final double t = -bParam / 2 / aParam;
                return (t >= 0.) && (t <= 1.);
            }
            return false;
        }

        assert discriminant > 0;

        // Two real solutions.
        final double t1 = (-bParam + Math.sqrt(discriminant)) / 2 / aParam;
        final double t2 = (-bParam - Math.sqrt(discriminant)) / 2 / aParam;

        return (t1 >= 0 || t2 >= 0) && (t1 <= 1 || t2 <= 1);
    }

    /** Replies if two ellipses are intersecting.
     *
     * @param ex is the first corner of the first ellipse.
     * @param ey is the first corner of the first ellipse.
     * @param ewidth is the width of the first ellipse.
     * @param eheight is the height of the first ellipse.
     * @param x3 is the first corner of the second rectangle.
     * @param y3 is the first corner of the second rectangle.
     * @param x4 is the second corner of the second rectangle.
     * @param y4 is the second corner of the second rectangle.
     * @return <code>true</code> if the two shapes are intersecting; otherwise
     * <code>false</code>
     */
    @Pure
    @SuppressWarnings("checkstyle:magicnumber")
    static boolean intersectsEllipseRectangle(double ex, double ey, double ewidth, double eheight,
            double x3, double y3, double x4, double y4) {
        assert ewidth >= 0. : AssertMessages.positiveOrZeroParameter(2);
        assert eheight >= 0. : AssertMessages.positiveOrZeroParameter(3);
        // From AWT Ellipse2D

        final double rectw = Math.abs(x4 - x3);
        final double recth = Math.abs(y4 - y3);

        if (rectw <= 0 || recth <= 0) {
            return false;
        }
        if (ewidth <= 0 || eheight <= 0) {
            return false;
        }

        // Normalize the rectangular coordinates compared to the ellipse
        // having a center at 0, 0 and a radius of 0.5.
        final double normx0 = (x3 - ex) / ewidth - 0.5;
        final double normx1 = normx0 + rectw / ewidth;
        final double normy0 = (y3 - ey) / eheight - 0.5;
        final double normy1 = normy0 + recth / eheight;

        // find nearest x (left edge, right edge, 0.0)
        // find nearest y (top edge, bottom edge, 0.0)
        // if nearest x, y is inside circle of radius 0.5, then intersects
        final double nearx;
        final double neary;
        if (normx0 > 0) {
            // center to left of X extents
            nearx = normx0;
        } else if (normx1 < 0) {
            // center to right of X extents
            nearx = normx1;
        } else {
            nearx = 0;
        }
        if (normy0 > 0) {
            // center above Y extents
            neary = normy0;
        } else if (normy1 < 0) {
            // center below Y extents
            neary = normy1;
        } else {
            neary = 0;
        }
        return (nearx * nearx + neary * neary) < 0.25;
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
        return getMinX() == shape.getMinX()
                && getMinY() == shape.getMinY()
                && getMaxX() == shape.getMaxX()
                && getMaxY() == shape.getMaxY();
    }

    @Pure
    @Override
    default double getDistanceSquared(Point2D<?, ?> pt) {
        assert pt != null : AssertMessages.notNullParameter();
        final Point2D<?, ?> r = getClosestPointTo(pt);
        return r.getDistanceSquared(pt);
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
        return containsEllipsePoint(
                getMinX(), getMinY(), getWidth(), getHeight(),
                x, y);
    }

    @Override
    default boolean contains(Rectangle2afp<?, ?, ?, ?, ?, ?> rectangle) {
        assert rectangle != null : AssertMessages.notNullParameter();
        return containsEllipseRectangle(
                getMinX(), getMinY(), getWidth(), getHeight(),
                rectangle.getMinX(), rectangle.getMinY(), rectangle.getMaxX(), rectangle.getMaxY());
    }

    @Override
    default boolean intersects(Rectangle2afp<?, ?, ?, ?, ?, ?> rectangle) {
        assert rectangle != null : AssertMessages.notNullParameter();
        return intersectsEllipseRectangle(
                getMinX(), getMinY(),
                getWidth(), getHeight(),
                rectangle.getMinX(), rectangle.getMinY(),
                rectangle.getMaxX(), rectangle.getMaxY());
    }

    @Override
    default boolean intersects(Ellipse2afp<?, ?, ?, ?, ?, ?> ellipse) {
        assert ellipse != null : AssertMessages.notNullParameter();
        return intersectsEllipseEllipse(
                getMinX(), getMinY(),
                getWidth(), getHeight(),
                ellipse.getMinX(), ellipse.getMinY(),
                ellipse.getWidth(), ellipse.getHeight());
    }

    @Pure
    @Override
    default boolean intersects(Circle2afp<?, ?, ?, ?, ?, ?> circle) {
        assert circle != null : AssertMessages.notNullParameter();
        return intersectsEllipseCircle(
                getMinX(), getMinY(),
                getWidth(), getHeight(),
                circle.getX(), circle.getY(), circle.getRadius());
    }

    @Pure
    @Override
    default boolean intersects(Segment2afp<?, ?, ?, ?, ?, ?> segment) {
        assert segment != null : AssertMessages.notNullParameter();
        return intersectsEllipseSegment(
                getMinX(), getMinY(),
                getWidth(), getHeight(),
                segment.getX1(), segment.getY1(),
                segment.getX2(), segment.getY2(),
                false);
    }

    @Pure
    @Override
    default boolean intersects(OrientedRectangle2afp<?, ?, ?, ?, ?, ?> orientedRectangle) {
        assert orientedRectangle != null : AssertMessages.notNullParameter();
        return OrientedRectangle2afp.intersectsOrientedRectangleEllipse(
                orientedRectangle.getCenterX(), orientedRectangle.getCenterY(),
                orientedRectangle.getFirstAxisX(), orientedRectangle.getFirstAxisY(), orientedRectangle.getFirstAxisExtent(),
                orientedRectangle.getSecondAxisExtent(),
                getMinX(), getMinY(), getWidth(), getHeight());
    }

    @Pure
    @Override
    default boolean intersects(Triangle2afp<?, ?, ?, ?, ?, ?> triangle) {
        assert triangle != null : AssertMessages.notNullParameter();
        return Triangle2afp.intersectsTriangleEllipse(
                triangle.getX1(), triangle.getY1(),
                triangle.getX2(), triangle.getY2(),
                triangle.getX3(), triangle.getY3(),
                getMinX(), getMinY(),
                getWidth(), getHeight());
    }

    @Pure
    @Override
    default boolean intersects(Parallelogram2afp<?, ?, ?, ?, ?, ?> parallelogram) {
        assert parallelogram != null : AssertMessages.notNullParameter();
        return Parallelogram2afp.intersectsParallelogramEllipse(
                parallelogram.getCenterX(), parallelogram.getCenterY(),
                parallelogram.getFirstAxisX(), parallelogram.getFirstAxisY(), parallelogram.getFirstAxisExtent(),
                parallelogram.getSecondAxisX(), parallelogram.getSecondAxisY(), parallelogram.getSecondAxisExtent(),
                getMinX(), getMinY(), getWidth(), getHeight());
    }

    @Pure
    @Override
    default boolean intersects(RoundRectangle2afp<?, ?, ?, ?, ?, ?> roundRectangle) {
        assert roundRectangle != null : AssertMessages.notNullParameter();
        return RoundRectangle2afp.intersectsRoundRectangleEllipse(
                roundRectangle.getMinX(), roundRectangle.getMinY(),
                roundRectangle.getMaxX(), roundRectangle.getMaxY(),
                roundRectangle.getArcWidth(), roundRectangle.getArcHeight(),
                getMinX(), getMinY(), getWidth(), getHeight());
    }

    @Override
    default boolean intersects(PathIterator2afp<?> iterator) {
        assert iterator != null : AssertMessages.notNullParameter();
        final int mask = iterator.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
        final int crossings = Path2afp.calculatesCrossingsPathIteratorEllipseShadow(
                0,
                iterator,
                getMinX(), getMinY(), getWidth(), getHeight(),
                CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
        return crossings == GeomConstants.SHAPE_INTERSECTS
                || (crossings & mask) != 0;

    }

    @Pure
    @Override
    default boolean intersects(MultiShape2afp<?, ?, ?, ?, ?, ?, ?> multishape) {
        assert multishape != null : AssertMessages.notNullParameter();
        return multishape.intersects(this);
    }

    @Override
    default PathIterator2afp<IE> getPathIterator(Transform2D transform) {
        if (transform == null || transform.isIdentity()) {
            return new EllipsePathIterator<>(this);
        }
        return new TransformedEllipsePathIterator<>(this, transform);
    }

    /** Replies a path iterator on this shape that is replacing the
     * curves by line approximations.
     *
     * @return the iterator on the approximation.
     * @see #getPathIterator()
     * @see GeomConstants#SPLINE_APPROXIMATION_RATIO
     */
    @Pure
    default PathIterator2afp<IE> getFlatteningPathIterator() {
        return new Path2afp.FlatteningPathIterator<>(
                getPathIterator(null),
                getGeomFactory().getSplineApproximationRatio(),
                Path2afp.DEFAULT_FLATTENING_LIMIT);
    }

    @Override
    default P getClosestPointTo(Point2D<?, ?> pt) {
        assert pt != null : AssertMessages.notNullParameter();
        final P point = getGeomFactory().newPoint();
        Ellipse2afp.findsClosestPointSolidEllipsePoint(
                pt.getX(), pt.getY(),
                getMinX(), getMinY(),
                getWidth(), getHeight(),
                point);
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
        final P pointOnSecondEllipse = getGeomFactory().newPoint();
        Path2afp.findsClosestPointPathIteratorPathIterator(
                ellipse.getFlatteningPathIterator(),
                getPathIterator(), pointOnSecondEllipse);
        return getClosestPointTo(pointOnSecondEllipse);
    }

    @Override
    @Unefficient
    default P getClosestPointTo(Rectangle2afp<?, ?, ?, ?, ?, ?> rectangle) {
        assert rectangle != null : AssertMessages.notNullParameter();
        final Point2D<?, ?> pointOnRectangle = rectangle.getClosestPointTo(this);
        return getClosestPointTo(pointOnRectangle);
    }

    @Override
    @Unefficient
    default P getClosestPointTo(Segment2afp<?, ?, ?, ?, ?, ?> segment) {
        assert segment != null : AssertMessages.notNullParameter();
        final Point2D<?, ?> pointOnSegment = segment.getClosestPointTo(this);
        return getClosestPointTo(pointOnSegment);
    }

    @Override
    @Unefficient
    default P getClosestPointTo(Triangle2afp<?, ?, ?, ?, ?, ?> triangle) {
        assert triangle != null : AssertMessages.notNullParameter();
        final Point2D<?, ?> pointOnTriangle = triangle.getClosestPointTo(this);
        return getClosestPointTo(pointOnTriangle);
    }

    @Override
    @Unefficient
    default P getClosestPointTo(OrientedRectangle2afp<?, ?, ?, ?, ?, ?> orientedRectangle) {
        assert orientedRectangle != null : AssertMessages.notNullParameter();
        final Point2D<?, ?> pointOnRectangle = orientedRectangle.getClosestPointTo(this);
        return getClosestPointTo(pointOnRectangle);
    }

    @Override
    @Unefficient
    default P getClosestPointTo(Parallelogram2afp<?, ?, ?, ?, ?, ?> parallelogram) {
        assert parallelogram != null : AssertMessages.notNullParameter();
        final Point2D<?, ?> pointOnParallelogram = parallelogram.getClosestPointTo(this);
        return getClosestPointTo(pointOnParallelogram);
    }

    @Override
    @Unefficient
    default P getClosestPointTo(RoundRectangle2afp<?, ?, ?, ?, ?, ?> roundRectangle) {
        assert roundRectangle != null : AssertMessages.notNullParameter();
        final Point2D<?, ?> pointOnRectangle = roundRectangle.getClosestPointTo(this);
        return getClosestPointTo(pointOnRectangle);
    }

    @Override
    @Unefficient
    default P getClosestPointTo(Path2afp<?, ?, ?, ?, ?, ?> path) {
        assert path != null : AssertMessages.notNullParameter();
        final P point = getGeomFactory().newPoint();
        Path2afp.findsClosestPointPathIteratorPathIterator(
                getFlatteningPathIterator(),
                path.getPathIterator(), point);
        return point;
    }

    @Override
    default P getFarthestPointTo(Point2D<?, ?> pt) {
        assert pt != null : AssertMessages.notNullParameter();
        final P point = getGeomFactory().newPoint();
        Ellipse2afp.findsFarthestPointShallowEllipsePoint(
                pt.getX(), pt.getY(),
                getMinX(), getMinY(),
                getWidth(), getHeight(),
                point);
        return point;
    }

    /** Replies the horizontal radius of the ellipse.
     *
     * @return the horizontal radius.
     */
    default double getHorizontalRadius() {
        return getWidth() / 2;
    }

    /** Replies the vertical radius of the ellipse.
     *
     * @return the vertical radius.
     */
    default double getVerticalRadius() {
        return getHeight() / 2;
    }

    /** Replies the focus point with the lower coordinates.
     *
     * <p>The foci always lie on the major (longest) axis, spaced equally
     * each side of the center.
     * If the major axis and minor axis are the same length, the figure is a circle and both foci are at the center.
     *
     * @return the focus point.
     */
    default P getMinFocusPoint() {
        final double radius1 = getHorizontalRadius();
        final double radius2 = getVerticalRadius();
        final double squaredRadius1 = radius1 * radius1;
        final double squaredRadius2 = radius2 * radius2;
        final double centerX = getCenterX();
        final double centerY = getCenterY();
        final GeomFactory2afp<?, P, V, ?> factory = getGeomFactory();
        if (radius1 >= radius2) {
            final double focusDistance = Math.sqrt(squaredRadius1 - squaredRadius2);
            return factory.newPoint(centerX - focusDistance, centerY);
        }
        final double focusDistance = Math.sqrt(squaredRadius2 - squaredRadius1);
        return factory.newPoint(centerX, centerY - focusDistance);
    }

    /** Replies the focus point with the higher coordinates.
     *
     * <p>The foci always lie on the major (longest) axis, spaced equally
     * each side of the center.
     * If the major axis and minor axis are the same length, the figure is a circle and both foci are at the center.
     *
     * @return the focus point.
     */
    default P getMaxFocusPoint() {
        final double radius1 = getHorizontalRadius();
        final double radius2 = getVerticalRadius();
        final double squaredRadius1 = radius1 * radius1;
        final double squaredRadius2 = radius2 * radius2;
        final double centerX = getCenterX();
        final double centerY = getCenterY();
        final GeomFactory2afp<?, P, V, ?> factory = getGeomFactory();
        if (radius1 >= radius2) {
            final double focusDistance = Math.sqrt(squaredRadius1 - squaredRadius2);
            return factory.newPoint(centerX + focusDistance, centerY);
        }
        final double focusDistance = Math.sqrt(squaredRadius2 - squaredRadius1);
        return factory.newPoint(centerX, centerY + focusDistance);
    }

    /** Abstract iterator on the path elements of the ellipse.
     *
     * @param <T> the type of the path elements.
     * @author $Author: sgalland$
     * @version $FullVersion$
     * @mavengroupid $GroupId$
     * @mavenartifactid $ArtifactId$
     */
    abstract class AbstractEllipsePathIterator<T extends PathElement2afp> implements PathIterator2afp<T> {

        /** Positive curve tangent size.
         */
        protected static final double PCV = 0.5 + AbstractCirclePathIterator.CTRL_POINT_DISTANCE * 0.5;

        /** Negative curve tangent size.
         */
        protected static final double NCV = 0.5 - AbstractCirclePathIterator.CTRL_POINT_DISTANCE * 0.5;

        /**
         * The control points for a set of 4 cubic
         * bezier curves that approximate a circle of radius 0.5
         * centered at 0.5, 0.5.
         */
        protected static final double[][] BEZIER_CONTROL_POINTS = {
            {1.0, PCV,  PCV, 1.0,  0.5,  1.0},
            {NCV, 1.0,  0.0, PCV,  0.0,  0.5},
            {0.0, NCV,  NCV, 0.0,  0.5,  0.0},
            {PCV, 0.0,  1.0, NCV,  1.0,  0.5},
        };

        /** 4 segments + close.
         */
        protected static final int NUMBER_ELEMENTS = 5;

        /** The iterated shape.
         */
        protected final Ellipse2afp<?, ?, T, ?, ?, ?> ellipse;

        /** Constructor.
         * @param ellipse the ellipse to iterate on.
         */
        public AbstractEllipsePathIterator(Ellipse2afp<?, ?, T, ?, ?, ?> ellipse) {
            assert ellipse != null : AssertMessages.notNullParameter();
            this.ellipse = ellipse;
        }

        @Override
        public GeomFactory2afp<T, ?, ?, ?> getGeomFactory() {
            return this.ellipse.getGeomFactory();
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
            return true;
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

    /** Iterator on the ellipse path elements.
     *
     * @param <T> the type of the path elements.
     * @author $Author: sgalland$
     * @version $FullVersion$
     * @mavengroupid $GroupId$
     * @mavenartifactid $ArtifactId$
     * @since 13.0
     */
    class EllipsePathIterator<T extends PathElement2afp> extends AbstractEllipsePathIterator<T> {

        private double x;

        private double y;

        private double width;

        private double height;

        private int index;

        private double lastX;

        private double lastY;

        /** Constructor.
         * @param ellipse the ellipse to iterate on.
         */
        public EllipsePathIterator(Ellipse2afp<?, ?, T, ?, ?, ?> ellipse) {
            super(ellipse);
            if (ellipse.isEmpty()) {
                this.index = NUMBER_ELEMENTS;
            } else {
                this.x = ellipse.getMinX();
                this.y = ellipse.getMinY();
                this.width = ellipse.getWidth();
                this.height = ellipse.getHeight();
                this.index = -1;
            }
        }

        @Override
        public PathIterator2afp<T> restartIterations() {
            return new EllipsePathIterator<>(this.ellipse);
        }

        @Pure
        @Override
        public boolean hasNext() {
            return this.index < NUMBER_ELEMENTS;
        }

        @Override
        @SuppressWarnings("checkstyle:magicnumber")
        public T next() {
            if (this.index >= NUMBER_ELEMENTS) {
                throw new NoSuchElementException();
            }
            final int idx = this.index;
            ++this.index;

            if (idx < 0) {
                final double[] ctrls = BEZIER_CONTROL_POINTS[3];
                this.lastX = this.x + ctrls[4] * this.width;
                this.lastY = this.y + ctrls[5] * this.height;
                return getGeomFactory().newMovePathElement(
                        this.lastX,  this.lastY);
            }

            if (idx < (NUMBER_ELEMENTS - 1)) {
                final double[] ctrls = BEZIER_CONTROL_POINTS[idx];
                final double ix = this.lastX;
                final double iy = this.lastY;
                this.lastX = this.x + ctrls[4] * this.width;
                this.lastY = this.y + ctrls[5] * this.height;
                return getGeomFactory().newCurvePathElement(
                        ix,  iy,
                        this.x + ctrls[0] * this.width,
                        this.y + ctrls[1] * this.height,
                        this.x + ctrls[2] * this.width,
                        this.y + ctrls[3] * this.height,
                        this.lastX,
                        this.lastY);
            }

            return getGeomFactory().newClosePathElement(
                    this.lastX, this.lastY,
                    this.lastX, this.lastY);
        }

    }

    /** Iterator on the path elements of a transformed ellipse.
     *
     * @param <T> the type of the path elements.
     * @author $Author: sgalland$
     * @version $FullVersion$
     * @mavengroupid $GroupId$
     * @mavenartifactid $ArtifactId$
     * @since 13.0
     */
    @SuppressWarnings("checkstyle:magicnumber")
    class TransformedEllipsePathIterator<T extends PathElement2afp> extends AbstractEllipsePathIterator<T> {

        private final Transform2D transform;

        private Point2D<?, ?> lastPoint;

        private Point2D<?, ?> ptmp1;

        private Point2D<?, ?> ptmp2;

        private double x1;

        private double y1;

        private double width;

        private double height;

        private int index;

        /** Constructor.
         * @param ellipse the ellipse to iterate on.
         * @param transform the transformation to apply to the ellipse.
         */
        public TransformedEllipsePathIterator(Ellipse2afp<?, ?, T, ?, ?, ?> ellipse, Transform2D transform) {
            super(ellipse);
            assert transform != null : AssertMessages.notNullParameter(1);
            this.transform = transform;
            if (ellipse.isEmpty()) {
                this.index = 6;
            } else {
                this.lastPoint = new InnerComputationPoint2afp();
                this.ptmp1 = new InnerComputationPoint2afp();
                this.ptmp2 = new InnerComputationPoint2afp();
                this.x1 = ellipse.getMinX();
                this.y1 = ellipse.getMinY();
                this.width = ellipse.getWidth();
                this.height = ellipse.getHeight();
            }
        }

        @Override
        public PathIterator2afp<T> restartIterations() {
            return new TransformedEllipsePathIterator<>(this.ellipse, this.transform);
        }

        @Pure
        @Override
        public boolean hasNext() {
            return this.index <= 5;
        }

        @Override
        public T next() {
            if (this.index > 5) {
                throw new NoSuchElementException();
            }
            final int idx = this.index;
            ++this.index;

            if (idx == 0) {
                final double[] ctrls = BEZIER_CONTROL_POINTS[3];
                this.lastPoint.set(
                        this.x1 + ctrls[4] * this.width,
                        this.y1 + ctrls[5] * this.height);
                this.transform.transform(this.lastPoint);
                return getGeomFactory().newMovePathElement(
                        this.lastPoint.getX(), this.lastPoint.getY());
            } else if (idx < 5) {
                final double[] ctrls = BEZIER_CONTROL_POINTS[idx - 1];
                final double ix = this.lastPoint.getX();
                final double iy = this.lastPoint.getY();
                this.lastPoint.set(
                        this.x1 + ctrls[4] * this.width,
                        this.y1 + ctrls[5] * this.height);
                this.transform.transform(this.lastPoint);
                this.ptmp1.set(
                        this.x1 + ctrls[0] * this.width,
                        this.y1 + ctrls[1] * this.height);
                this.transform.transform(this.ptmp1);
                this.ptmp2.set(
                        this.x1 + ctrls[2] * this.width,
                        this.y1 + ctrls[3] * this.height);
                this.transform.transform(this.ptmp2);
                return getGeomFactory().newCurvePathElement(
                        ix,  iy,
                        this.ptmp1.getX(), this.ptmp1.getY(),
                        this.ptmp2.getX(), this.ptmp2.getY(),
                        this.lastPoint.getX(), this.lastPoint.getY());
            }

            final double ix = this.lastPoint.getX();
            final double iy = this.lastPoint.getY();
            return getGeomFactory().newClosePathElement(
                    ix, iy,
                    ix, iy);
        }

    }

    /** Private API functions for the ellipses.
     *
     * @author $Author: sgalland$
     * @version $Name$ $Revision$ $Date$
     * @mavengroupid $GroupId$
     * @mavenartifactid $ArtifactId$
     * @since 13.0
     */
    final class PrivateAPI {

        private static final int MAX_ITERATIONS = 1074;

        private PrivateAPI() {
            //
        }

        @Unefficient
        private static double getClosestNormalPointRoot(double r0, double zx, double zy, double gval) {
            final double n0 = r0 * zx;
            double s0 = zy - 1;
            double localG = gval;
            double s1 = (localG < 0) ? 0 : Math.hypot(n0, zy) - 1.;
            double result = Double.NaN;
            for (int i = 0; i < MAX_ITERATIONS; ++i) {
                result = (s0 + s1) / 2.;
                if (result == s0 || result == s1) {
                    return result;
                }
                final double ratio0 = n0 / (result + r0);
                final double ratio1 = zy / (result + 1.);
                localG = ratio0 * ratio0 + ratio1 * ratio1 - 1.;
                if (localG > 0) {
                    s0 = result;
                } else if (localG < 0) {
                    s1 = result;
                } else {
                    return result;
                }
            }
            return result;
        }

        /** Compute the closest point to a shallow ellipse centered on (0, 0) and in the positive quadrant.
         * The coordinates of the point must be positive.
         *
         * <p>The mathematrical definition of the algorithm is explained in:
         * <a href="./doc-files/DistancePointEllipseEllipsoid.pdf">DistancePointEllipseEllipsoid.pdf</a>
         * (source: <a href="http://www.geometrictools.com/">geometrictools.com</a>).
         *
         * @param px the x coordinate of the point. It must be positive or nul.
         * @param py the y coordinate of the point. It must be positive or nul.
         * @param horizontalRadius the horizontal radius.
         * @param verticalRadius the vertical radius.
         * @param computeDistance indicates if the distance musst be computed and replied.
         * @return the triplet (closest point x, closest point y, distance to closest point) if
         * <code>computeDistance</code> if <code>true</code>. Otherwise, the triplet (closest point x, closest point y).
         */
        @Unefficient
        public static double[] computeClosestPointOnShallowEllipseInPositiveQuadrant(
                double px, double py,
                double horizontalRadius, double verticalRadius,
                boolean computeDistance) {
            assert px >= 0 : AssertMessages.positiveOrZeroParameter(0);
            assert py >= 0 : AssertMessages.positiveOrZeroParameter(1);
            assert horizontalRadius >= 0 : AssertMessages.positiveOrZeroParameter(2);
            assert verticalRadius >= 0 : AssertMessages.positiveOrZeroParameter(3);
            assert verticalRadius <= horizontalRadius
                    : AssertMessages.lowerEqualParameters(3, verticalRadius, 2, horizontalRadius);
            final double closeX;
            final double closeY;
            double distance = 0;
            if (py > 0) {
                if (px > 0) {
                    final double zx = px / horizontalRadius;
                    final double zy = py / verticalRadius;
                    final double g = zx * zx + zy * zy - 1.;
                    // g > 0, then point is outside ellipse
                    // g = 0, then point is on ellipse
                    // g < 0, then point is inside ellipse
                    if (g != 0) {
                        double r0 = horizontalRadius / verticalRadius;
                        r0 = r0 * r0;
                        final double sbar = getClosestNormalPointRoot(r0, zx, zy, g);
                        closeX = r0 * px / (sbar + r0);
                        closeY = py / (sbar + 1.);
                        if (computeDistance) {
                            distance = Math.hypot(closeX - px, closeY - py);
                        }
                    } else {
                        closeX = px;
                        closeY = py;
                    }
                } else {
                    // px == 0
                    closeX = 0;
                    closeY = verticalRadius;
                    if (computeDistance) {
                        distance = Math.abs(py - verticalRadius);
                    }
                }
            } else {
                // py == 0
                final double numer0 = horizontalRadius * px;
                final double denom0 = horizontalRadius * horizontalRadius - verticalRadius * verticalRadius;
                if (numer0 < denom0) {
                    final double xde0 = numer0 / denom0;
                    closeX = horizontalRadius * xde0;
                    closeY = verticalRadius * Math.sqrt(1. - xde0 * xde0);
                    if (computeDistance) {
                        distance = Math.hypot(closeX - px, closeY);
                    }
                } else {
                    closeX = horizontalRadius;
                    closeY = 0;
                    if (computeDistance) {
                        distance = Math.abs(px - horizontalRadius);
                    }
                }
            }
            if (computeDistance) {
                return new double[] {closeX,  closeY, distance};
            }
            return new double[] {closeX,  closeY};
        }

        /** Compute the closest point to a solid ellipse centered on (0, 0) and in the positive quadrant.
         * The coordinates of the point must be positive.
         *
         * <p>The mathematrical definition of the algorithm is explained in:
         * <a href="./doc-files/DistancePointEllipseEllipsoid.pdf">DistancePointEllipseEllipsoid.pdf</a>
         * (source: <a href="http://www.geometrictools.com/">geometrictools.com</a>).
         *
         * @param px the x coordinate of the point. It must be positive or nul.
         * @param py the y coordinate of the point. It must be positive or nul.
         * @param horizontalRadius the horizontal radius.
         * @param verticalRadius the vertical radius.
         * @param computeDistance indicates if the distance musst be computed and replied.
         * @return the triplet (closest point x, closest point y, distance to closest point) if
         * <code>computeDistance</code> if <code>true</code>. Otherwise, the triplet (closest point x, closest point y).
         */
        @Unefficient
        @SuppressWarnings("checkstyle:nestedifdepth")
        public static double[] computeClosestPointOnSolidEllipseInPositiveQuadrant(
                double px, double py,
                double horizontalRadius, double verticalRadius,
                boolean computeDistance) {
            assert px >= 0 : AssertMessages.positiveOrZeroParameter(0);
            assert py >= 0 : AssertMessages.positiveOrZeroParameter(1);
            assert horizontalRadius >= 0 : AssertMessages.positiveOrZeroParameter(2);
            assert verticalRadius >= 0 : AssertMessages.positiveOrZeroParameter(3);
            assert verticalRadius <= horizontalRadius
                    : AssertMessages.lowerEqualParameters(3, verticalRadius, 2, horizontalRadius);
            final double closeX;
            final double closeY;
            double distance = 0;
            if (py > 0) {
                if (px > 0) {
                    final double zx = px / horizontalRadius;
                    final double zy = py / verticalRadius;
                    final double g = zx * zx + zy * zy - 1;
                    // g > 0, then point is outside ellipse
                    // g = 0, then point is on ellipse
                    // g < 0, then point is inside ellipse
                    if (g > 0) {
                        double r0 = horizontalRadius / verticalRadius;
                        r0 = r0 * r0;
                        final double sbar = getClosestNormalPointRoot(r0, zx, zy, g);
                        closeX = r0 * px / (sbar + r0);
                        closeY = py / (sbar + 1);
                        if (computeDistance) {
                            distance = Math.hypot(closeX - px, closeY - py);
                        }
                    } else {
                        closeX = px;
                        closeY = py;
                    }
                } else {
                    // px == 0
                    closeX = 0;
                    if (py <= verticalRadius) {
                        closeY = py;
                    } else {
                        closeY = verticalRadius;
                        if (computeDistance) {
                            distance = Math.abs(py - verticalRadius);
                        }
                    }
                }
            } else {
                // py == 0
                if (px <= horizontalRadius) {
                    closeX = px;
                    closeY = py;
                } else {
                    final double numer0 = horizontalRadius * px;
                    final double denom0 = horizontalRadius * horizontalRadius - verticalRadius * verticalRadius;
                    if (numer0 < denom0) {
                        final double xde0 = numer0 / denom0;
                        closeX = horizontalRadius * xde0;
                        closeY = verticalRadius * Math.sqrt(1 - xde0 * xde0);
                        if (computeDistance) {
                            distance = Math.hypot(closeX - px, closeY);
                        }
                    } else {
                        closeX = horizontalRadius;
                        closeY = 0;
                        if (computeDistance) {
                            distance = Math.abs(px - horizontalRadius);
                        }
                    }
                }
            }
            if (computeDistance) {
                return new double[] {closeX,  closeY, distance};
            }
            return new double[] {closeX,  closeY};
        }

        @Unefficient
        private static double getFarthestNormalPointRoot(double r0, double e0, double e1, double zx, double zy) {
            double localG;
            double s0 = -Math.hypot(zx, r0 * zy) - 1.;
            double s1 = zx - 1;
            double result = Double.NaN;
            final double v0 = e1 * e1;
            final double v1 = v0 * zy;
            final double v2 = e0 * e0;
            for (int i = 0; i < MAX_ITERATIONS; ++i) {
                result = (s0 + s1) / 2.;
                if (result == s0 || result == s1) {
                    return result;
                }
                final double ratio0 = zx / (result + 1.);
                final double ratio1 = v1 / (result * v2  + v1);
                localG = ratio0 * ratio0 + ratio1 * ratio1 - 1.;
                if (localG > 0) {
                    s1 = result;
                } else if (localG < 0) {
                    s0 = result;
                } else {
                    return result;
                }
            }
            return result;
        }

        /** Compute the farthest point to a shallow ellipse centered on (0, 0) and in the positive quadrant.
         * The coordinates of the point must be negative.
         *
         * <p>This function is an adaptation of the mathematrical definition that is explained in:
         * <a href="./doc-files/DistancePointEllipseEllipsoid.pdf">DistancePointEllipseEllipsoid.pdf</a>
         * (source: <a href="http://www.geometrictools.com/">geometrictools.com</a>).
         *
         * @param px the x coordinate of the point. It must be positive or nul.
         * @param py the y coordinate of the point. It must be positive or nul.
         * @param horizontalRadius the horizontal radius.
         * @param verticalRadius the vertical radius.
         * @param computeDistance indicates if the distance musst be computed and replied.
         * @return the triplet (closest point x, closest point y, distance to closest point) if
         * <code>computeDistance</code> if <code>true</code>. Otherwise, the triplet (closest point x, closest point y).
         */
        @Unefficient
        public static double[] computeFarthestPointOnShallowEllipseInPositiveQuadrant(
                double px, double py,
                double horizontalRadius, double verticalRadius,
                boolean computeDistance) {
            assert px <= 0 : AssertMessages.negativeOrZeroParameter(0);
            assert py <= 0 : AssertMessages.negativeOrZeroParameter(1);
            assert horizontalRadius >= 0 : AssertMessages.positiveOrZeroParameter(2);
            assert verticalRadius >= 0 : AssertMessages.positiveOrZeroParameter(3);
            assert verticalRadius <= horizontalRadius
                    : AssertMessages.lowerEqualParameters(3, verticalRadius, 2, horizontalRadius);
            final double farX;
            final double farY;
            double distance = 0;

            if (py < 0) {
                if (px < 0) {
                    final double zx = px / horizontalRadius;
                    final double zy = py / verticalRadius;
                    final double g = zx * zx + zy * zy - 1.;
                    // g > 0, then point is outside ellipse
                    // g = 0, then point is on ellipse
                    // g < 0, then point is inside ellipse
                    if (g != 0) {
                        double r0 = verticalRadius / horizontalRadius;
                        r0 = r0 * r0;
                        final double sbar = getFarthestNormalPointRoot(r0, horizontalRadius, verticalRadius, zx, zy);
                        farX = px / (sbar + 1.);
                        farY = r0 * py / (sbar + r0);
                        if (computeDistance) {
                            distance = Math.hypot(farX - px, farY - py);
                        }
                    } else {
                        farX = px;
                        farY = py;
                    }
                } else {
                    // px == 0
                    final double psquare = py * py;
                    final double d1 = psquare + horizontalRadius * horizontalRadius;
                    final double d2 = psquare + 2 * Math.abs(py) * verticalRadius * verticalRadius;
                    if (d1 > d2) {
                        farX = horizontalRadius;
                        farY = 0;
                        if (computeDistance) {
                            distance = Math.sqrt(d1);
                        }
                    } else {
                        farX = 0;
                        farY = verticalRadius;
                        if (computeDistance) {
                            distance = Math.abs(px) + horizontalRadius;
                        }
                    }
                }
            } else {
                // py == 0
                final double psquare = px * px;
                final double d1 = psquare + verticalRadius * verticalRadius;
                final double d2 = psquare + 2 * Math.abs(px) * horizontalRadius * horizontalRadius;
                if (d1 > d2) {
                    farX = 0;
                    farY = verticalRadius;
                    if (computeDistance) {
                        distance = Math.sqrt(d1);
                    }
                } else {
                    farX = horizontalRadius;
                    farY = 0;
                    if (computeDistance) {
                        distance = Math.abs(px) + horizontalRadius;
                    }
                }
            }
            if (computeDistance) {
                return new double[] {farX,  farY, distance};
            }
            return new double[] {farX,  farY};
        }

    }

}

