/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.Unefficient;
import org.arakhne.afc.math.geometry.CrossingComputationType;
import org.arakhne.afc.math.geometry.IntersectionType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** Fonctional interface that represented a 2D rectangle on a plane.
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
 * @since 13.0
 */
@SuppressWarnings("checkstyle:methodcount")
public interface Rectangle2afp<
        ST extends Shape2afp<?, ?, IE, P, V, B>,
        IT extends Rectangle2afp<?, ?, IE, P, V, B>,
        IE extends PathElement2afp,
        P extends Point2D<? super P, ? super V>,
        V extends Vector2D<? super V, ? super P>,
        B extends Rectangle2afp<?, ?, IE, P, V, B>>
        extends RectangularShape2afp<ST, IT, IE, P, V, B>, OrientedRectangle2afp<ST, IT, IE, P, V, B> {

    /** Compute the point on the rectangle that is the closest to the given point.
     *
     * @param rx the minimum x coordinate of the rectangle.
     * @param ry the minimum y coordinate of the rectangle.
     * @param rmaxx the maximum x coordinate of the rectangle.
     * @param rmaxy the maximum y coordinate of the rectangle.
     * @param px the x coordinate of the point.
     * @param py the y coordinate of the point.
     * @param closest is set with the closest point on the rectangle.
     */
    static void findsClosestPointRectanglePoint(
            double rx, double ry, double rmaxx, double rmaxy,
            double px, double py,
            Tuple2D<?> closest) {
        assert rmaxx >= rx : AssertMessages.lowerEqualParameters(0, rx, 2, rmaxx);
        assert rmaxy >= ry : AssertMessages.lowerEqualParameters(1, ry, 3, rmaxy);
        final double x;
        if (px < rx) {
            x = rx;
        } else if (px > rmaxx) {
            x = rmaxx;
        } else {
            x = px;
        }
        final double y;
        if (py < ry) {
            y = ry;
        } else if (py > rmaxy) {
            y = rmaxy;
        } else {
            y = py;
        }
        closest.set(x, y);
    }

    /** Compute the point on the first rectangle that is the closest to the second rectangle.
     *
     * @param rx1 the minimum x coordinate of the first rectangle.
     * @param ry1 the minimum y coordinate of the first rectangle.
     * @param rmaxx1 the maximum x coordinate of the first rectangle.
     * @param rmaxy1 the maximum y coordinate of the first rectangle.
     * @param rx2 the minimum x coordinate of the second rectangle.
     * @param ry2 the minimum y coordinate of the second rectangle.
     * @param rmaxx2 the maximum x coordinate of the second rectangle.
     * @param rmaxy2 the maximum y coordinate of the second rectangle.
     * @param closest is set with the closest point on the first rectangle.
     */
    @SuppressWarnings({"checkstyle:parameternumber", "checkstyle:magicnumber"})
    static void findsClosestPointRectangleRectangle(
            double rx1, double ry1, double rmaxx1, double rmaxy1,
            double rx2, double ry2, double rmaxx2, double rmaxy2,
            Tuple2D<?> closest) {
        assert rmaxx1 >= rx1 : AssertMessages.lowerEqualParameters(0, rx1, 2, rmaxx1);
        assert rmaxy1 >= ry1 : AssertMessages.lowerEqualParameters(1, ry1, 3, rmaxy1);
        assert rmaxx2 >= rx2 : AssertMessages.lowerEqualParameters(4, rx2, 6, rmaxx2);
        assert rmaxy2 >= ry2 : AssertMessages.lowerEqualParameters(5, rx2, 7, rmaxx2);
        final double px;
        final double cx = (rx2 + rmaxx2) / 2.;
        if (cx <= rx1) {
            px = rx1;
        } else if (cx >= rmaxx1) {
            px = rmaxx1;
        } else {
            px = cx;
        }
        final double py;
        final double cy = (ry2 + rmaxy2) / 2.;
        if (cy <= rx1) {
            py = ry1;
        } else if (cy >= rmaxy1) {
            py = rmaxy1;
        } else {
            py = cy;
        }
        closest.set(px, py);
    }

    /** Compute the point on the rectangle that is the closest to the segment.
     *
     * @param rx the minimum x coordinate of the rectangle.
     * @param ry the minimum y coordinate of the rectangle.
     * @param rmaxx the maximum x coordinate of the rectangle.
     * @param rmaxy the maximum y coordinate of the rectangle.
     * @param sx1 the x coordinate of the first point of the segment.
     * @param sy1 the y coordinate of the first point of the segment.
     * @param sx2 the x coordinate of the second point of the segment.
     * @param sy2 the y coordinate of the second point of the segment.
     * @param closest is set with the closest point on the rectangle.
     */
    @SuppressWarnings("checkstyle:parameternumber")
    static void findsClosestPointRectangleSegment(
            double rx, double ry, double rmaxx, double rmaxy,
            double sx1, double sy1, double sx2, double sy2,
            Tuple2D<?> closest) {
        assert rmaxx >= rx : AssertMessages.lowerEqualParameters(0, rx, 2, rmaxx);
        assert rmaxy >= ry : AssertMessages.lowerEqualParameters(1, ry, 3, rmaxy);
        final int code1 = MathUtil.getCohenSutherlandCode(sx1, sy1, rx, ry, rmaxx, rmaxy);
        final int code2 = MathUtil.getCohenSutherlandCode(sx2, sy2, rx, ry, rmaxx, rmaxy);
        final Point2D<?, ?> tmp1 = new InnerComputationPoint2afp();
        final int zone = Rectangle2afp.reducesCohenSutherlandZoneRectangleSegment(
                rx, ry, rmaxx, rmaxy,
                sx1, sy1, sx2, sy2,
                code1, code2,
                tmp1, null);
        final double closex;
        final double closey;
        if ((zone & MathConstants.COHEN_SUTHERLAND_LEFT) != 0) {
            closex = rx;
            if (sx1 >= sx2) {
                closey = MathUtil.clamp(sy1, ry, rmaxy);
            } else {
                closey = MathUtil.clamp(sy2, ry, rmaxy);
            }
        } else if ((zone & MathConstants.COHEN_SUTHERLAND_RIGHT) != 0) {
            closex = rmaxx;
            if (sx1 <= sx2) {
                closey = MathUtil.clamp(sy1, ry, rmaxy);
            } else {
                closey = MathUtil.clamp(sy2, ry, rmaxy);
            }
        } else if ((zone & MathConstants.COHEN_SUTHERLAND_BOTTOM) != 0) {
            closey = ry;
            if (sy1 >= sy2) {
                closex = MathUtil.clamp(sx1, rx, rmaxx);
            } else {
                closex = MathUtil.clamp(sx2, rx, rmaxx);
            }
        } else if ((zone & MathConstants.COHEN_SUTHERLAND_TOP) != 0) {
            closey = rmaxy;
            if (sy1 <= sy2) {
                closex = MathUtil.clamp(sx1, rx, rmaxx);
            } else {
                closex = MathUtil.clamp(sx2, rx, rmaxx);
            }
        } else {
            closex = tmp1.getX();
            closey = tmp1.getY();
        }
        closest.set(closex, closey);
    }

    /** Compute the point on the rectangle that is the closest to the parallelogram.
     *
     * @param rx the minimum x coordinate of the rectangle.
     * @param ry the minimum y coordinate of the rectangle.
     * @param rmaxx the maximum x coordinate of the rectangle.
     * @param rmaxy the maximum y coordinate of the rectangle.
     * @param centerX the x coordinate of the center of the parallelogram.
     * @param centerY the y coordinate of the center of the parallelogram.
     * @param axis1X the x coordinate of the first axis of the parallelogram.
     * @param axis1Y the y coordinate of the first axis of the parallelogram.
     * @param axis1Extent the size of the parallelogram along its first axis.
     * @param axis2X the x coordinate of the second axis of the parallelogram.
     * @param axis2Y the y coordinate of the secondaxis of the parallelogram.
     * @param axis2Extent the size of the parallelogram along its second axis.
     * @param closest is set with the closest point on the first rectangle.
     */
    @SuppressWarnings({"checkstyle:parameternumber", "checkstyle:magicnumber"})
    static void findsClosestPointRectangleParallelogram(
            double rx, double ry, double rmaxx, double rmaxy,
            double centerX, double centerY, double axis1X, double axis1Y, double axis1Extent,
            double axis2X, double axis2Y, double axis2Extent,
            Tuple2D<?> closest) {
        assert rmaxx >= rx : AssertMessages.lowerEqualParameters(0, rx, 2, rmaxx);
        assert rmaxy >= ry : AssertMessages.lowerEqualParameters(1, ry, 3, rmaxy);
        assert Vector2D.isUnitVector(axis1X,  axis1Y) : AssertMessages.normalizedParameters(6, 7);
        assert axis1Extent >= 0. : AssertMessages.positiveOrZeroParameter(8);
        assert Vector2D.isUnitVector(axis2X,  axis2Y) : AssertMessages.normalizedParameters(9, 10);
        assert axis2Extent >= 0. : AssertMessages.positiveOrZeroParameter(11);
        final double a1x = axis1X * axis1Extent;
        final double a1y = axis1Y * axis1Extent;
        final double a2x = axis2X * axis2Extent;
        final double a2y = axis2Y * axis2Extent;
        final Point2D<?, ?> point = new InnerComputationPoint2afp();
        double x1 = centerX + a1x + a2x;
        double y1 = centerY + a1y + a2y;
        double min = Double.POSITIVE_INFINITY;
        final double[] segments = new double[] {
            centerX - a1x + a2x,
            centerY - a1y + a2y,
            centerX - a1x - a2x,
            centerY - a1y - a2y,
            centerX + a1x - a2x,
            centerY + a1y - a2y,
            x1,
            y1,
        };
        for (int i = 0; i < segments.length; i += 2) {
            final double x2 = segments[i];
            final double y2 = segments[i + 1];
            findsClosestPointRectangleSegment(rx, ry, rmaxx, rmaxy, x1, y1, x2, y2, point);
            final double dist = Segment2afp.calculatesDistanceSquaredSegmentPoint(x1, y1, x2, y2, point.getX(), point.getY());
            if (dist <= 0.) {
                closest.set(point);
                return;
            }
            if (dist < min) {
                min = dist;
                closest.set(point);
            }
            x1 = x2;
            y1 = y2;
        }
    }

    /** Update the given Cohen-Sutherland code that corresponds to the given segment in order
     * to obtain a segment restricted to a single Cohen-Sutherland zone.
     * This function is at the heart of the
     * <a href="http://en.wikipedia.org/wiki/Cohen%E2%80%93Sutherland_algorithm">Cohen-Sutherland algorithm</a>.
     *
     * <p>The result of this function may be: <ul>
     * <li>the code for a single zone, or</li>
     * <li>the code that corresponds to a single column, or </li>
     * <li>the code that corresponds to a single row.</li>
     * </ul>
     *
     * @param rx1 is the first corner of the rectangle.
     * @param ry1 is the first corner of the rectangle.
     * @param rx2 is the second corner of the rectangle.
     * @param ry2 is the second corner of the rectangle.
     * @param sx1 is the first point of the segment.
     * @param sy1 is the first point of the segment.
     * @param sx2 is the second point of the segment.
     * @param sy2 is the second point of the segment.
     * @param codePoint1 the Cohen-Sutherland code for the first point of the segment.
     * @param codePoint2 the Cohen-Sutherland code for the second point of the segment.
     * @param newSegmentP1 is set with the new coordinates of the segment first point. If <code>null</code>,
     *     this parameter is ignored.
     * @param newSegmentP2 is set with the new coordinates of the segment second point. If <code>null</code>,
     *     this parameter is ignored.
     * @return the rectricted Cohen-Sutherland zone.
     */
    @SuppressWarnings({"checkstyle:parameternumber", "checkstyle:npathcomplexity", "checkstyle:magicnumber"})
    static int reducesCohenSutherlandZoneRectangleSegment(double rx1, double ry1, double rx2, double ry2,
            double sx1, double sy1, double sx2, double sy2, int codePoint1, int codePoint2,
            Tuple2D<?> newSegmentP1, Tuple2D<?> newSegmentP2) {
        assert rx1 <= rx2 : AssertMessages.lowerEqualParameters(0, rx1, 2, rx2);
        assert ry1 <= ry2 : AssertMessages.lowerEqualParameters(1, ry1, 3, ry2);
        assert codePoint1 == MathUtil.getCohenSutherlandCode(sx1, sy1, rx1, ry1, rx2, ry2) : AssertMessages.invalidValue(8);
        assert codePoint2 == MathUtil.getCohenSutherlandCode(sx2, sy2, rx1, ry1, rx2, ry2) : AssertMessages.invalidValue(9);
        double segmentX1 = sx1;
        double segmentY1 = sy1;
        double segmentX2 = sx2;
        double segmentY2 = sy2;

        int code1 = codePoint1;
        int code2 = codePoint2;

        while (true) {
            if ((code1 | code2) == 0) {
                // Bitwise OR is 0. Trivially accept and get out of loop
                if (newSegmentP1 != null) {
                    newSegmentP1.set(segmentX1, segmentY1);
                }
                if (newSegmentP2 != null) {
                    newSegmentP2.set(segmentX2, segmentY2);
                }
                return 0;
            }
            if ((code1 & code2) != 0) {
                // Bitwise AND is not 0. Trivially reject and get out of loop
                if (newSegmentP1 != null) {
                    newSegmentP1.set(segmentX1, segmentY1);
                }
                if (newSegmentP2 != null) {
                    newSegmentP2.set(segmentX2, segmentY2);
                }
                return code1 & code2;
            }

            // failed both tests, so calculate the line segment intersection

            // At least one endpoint is outside the clip rectangle; pick it.
            int code3 = (code1 != 0) ? code1 : code2;

            double x = 0;
            double y = 0;

            // Now find the intersection point;
            // use formulas y = y0 + slope * (x - x0), x = x0 + (1 / slope) * (y - y0)
            if ((code3 & MathConstants.COHEN_SUTHERLAND_TOP) != 0) {
                // point is above the clip rectangle
                x = segmentX1 + (segmentX2 - segmentX1) * (ry2 - segmentY1) / (segmentY2 - segmentY1);
                y = ry2;
            } else if ((code3 & MathConstants.COHEN_SUTHERLAND_BOTTOM) != 0) {
                // point is below the clip rectangle
                x = segmentX1 + (segmentX2 - segmentX1) * (ry1 - segmentY1) / (segmentY2 - segmentY1);
                y = ry1;
            } else if ((code3 & MathConstants.COHEN_SUTHERLAND_RIGHT) != 0) {
                // point is to the right of clip rectangle
                y = segmentY1 + (segmentY2 - segmentY1) * (rx2 - segmentX1) / (segmentX2 - segmentX1);
                x = rx2;
            } else if ((code3 & MathConstants.COHEN_SUTHERLAND_LEFT) != 0) {
                // point is to the left of clip rectangle
                y = segmentY1 + (segmentY2 - segmentY1) * (rx1 - segmentX1) / (segmentX2 - segmentX1);
                x = rx1;
            } else {
                code3 = 0;
            }

            if (code3 != 0) {
                // Now we move outside point to intersection point to clip
                // and get ready for next pass.
                if (code3 == code1) {
                    segmentX1 = x;
                    segmentY1 = y;
                    code1 = MathUtil.getCohenSutherlandCode(segmentX1, segmentY1, rx1, ry1, rx2, ry2);
                } else {
                    segmentX2 = x;
                    segmentY2 = y;
                    code2 = MathUtil.getCohenSutherlandCode(segmentX2, segmentY2, rx1, ry1, rx2, ry2);
                }
            }
        }
    }

    /** Compute the square distance between a rectangle and a point.
     *
     * @param rx the minimum x coordinate of the rectangle.
     * @param ry the minimum y coordinate of the rectangle.
     * @param rmaxx the maximum x coordinate of the rectangle.
     * @param rmaxy the maximum y coordinate of the rectangle.
     * @param px the x coordinate of the point.
     * @param py the y coordinate of the point.
     * @return the square distance.
     */
    @Pure
    static double calculatesDistanceSquaredRectanglePoint(double rx, double ry, double rmaxx, double rmaxy,
            double px, double py) {
        assert rmaxx >= rx : AssertMessages.lowerEqualParameters(0, rx, 2, rmaxx);
        assert rmaxy >= ry : AssertMessages.lowerEqualParameters(1, ry, 3, rmaxy);
        final double dx;
        if (px < rx) {
            dx = rx - px;
        } else if (px > rmaxx) {
            dx = px - rmaxx;
        } else {
            dx = 0;
        }
        final double dy;
        if (py < ry) {
            dy = ry - py;
        } else if (py > rmaxy) {
            dy = py - rmaxy;
        } else {
            dy = 0;
        }
        return dx * dx + dy * dy;
    }

    /** Replies if two rectangles are intersecting.
     *
     * @param x1 is the first corner of the first rectangle.
     * @param y1 is the first corner of the first rectangle.
     * @param x2 is the second corner of the first rectangle.
     * @param y2 is the second corner of the first rectangle.
     * @param x3 is the first corner of the second rectangle.
     * @param y3 is the first corner of the second rectangle.
     * @param x4 is the second corner of the second rectangle.
     * @param y4 is the second corner of the second rectangle.
     * @return <code>true</code> if the two shapes are intersecting; otherwise
     * <code>false</code>
     */
    @Pure
    @SuppressWarnings("checkstyle:magicnumber")
    static boolean intersectsRectangleRectangle(double x1, double y1, double x2, double y2,
            double x3, double y3, double x4, double y4) {
        assert x1 <= x2 : AssertMessages.lowerEqualParameters(0, x1, 2, x2);
        assert y1 <= y2 : AssertMessages.lowerEqualParameters(1, y1, 3, y2);
        assert x3 <= x4 : AssertMessages.lowerEqualParameters(4, x3, 6, x4);
        assert y3 <= y4 : AssertMessages.lowerEqualParameters(5, y3, 7, y4);
        return x2 > x3 && x1 < x4 && y2 > y3 && y1 < y4;
    }

    /** Replies if two rectangles are intersecting.
     *
     * @param x1 is the first corner of the rectangle.
     * @param y1 is the first corner of the rectangle.
     * @param x2 is the second corner of the rectangle.
     * @param y2 is the second corner of the rectangle.
     * @param x3 is the first point of the line.
     * @param y3 is the first point of the line.
     * @param x4 is the second point of the line.
     * @param y4 is the second point of the line.
     * @return <code>true</code> if the two shapes are intersecting; otherwise
     * <code>false</code>
     */
    @Pure
    static boolean intersectsRectangleLine(double x1, double y1, double x2, double y2,
            double x3, double y3, double x4, double y4) {
        assert x1 <= x2 : AssertMessages.lowerEqualParameters(0, x1, 2, x2);
        assert y1 <= y2 : AssertMessages.lowerEqualParameters(1, y1, 3, y2);
        final int a = Segment2afp.ccw(x3, y3, x4, y4, x1, y1, 0.);
        int b = Segment2afp.ccw(x3, y3, x4, y4, x2, y1, 0.);
        if (a != b && b != 0) {
            return true;
        }
        b = Segment2afp.ccw(x3, y3, x4, y4, x2, y2, 0.);
        if (a != b && b != 0) {
            return true;
        }
        b = Segment2afp.ccw(x3, y3, x4, y4, x1, y2, 0.);
        return a != b && b != 0;
    }

    /** Replies if the rectangle is intersecting the segment.
     *
     * @param rx1 is the first corner of the rectangle.
     * @param ry1 is the first corner of the rectangle.
     * @param rx2 is the second corner of the rectangle.
     * @param ry2 is the second corner of the rectangle.
     * @param sx1 is the first point of the segment.
     * @param sy1 is the first point of the segment.
     * @param sx2 is the second point of the segment.
     * @param sy2 is the second point of the segment.
     * @return <code>true</code> if the two shapes are intersecting; otherwise
     * <code>false</code>
     */
    @Pure
    @SuppressWarnings("checkstyle:npathcomplexity")
    static boolean intersectsRectangleSegment(double rx1, double ry1, double rx2, double ry2,
            double sx1, double sy1, double sx2, double sy2) {
        assert rx1 <= rx2 : AssertMessages.lowerEqualParameters(0, rx1, 2, rx2);
        assert ry1 <= ry2 : AssertMessages.lowerEqualParameters(1, ry1, 3, ry2);
        double segmentX1 = sx1;
        double segmentY1 = sy1;
        double segmentX2 = sx2;
        double segmentY2 = sy2;

        int code1 = MathUtil.getCohenSutherlandCode(segmentX1, segmentY1, rx1, ry1, rx2, ry2);
        int code2 = MathUtil.getCohenSutherlandCode(segmentX2, segmentY2, rx1, ry1, rx2, ry2);

        while (true) {
            if ((code1 | code2) == 0) {
                // Bitwise OR is 0. Trivially accept and get out of loop
                // Special case: if the segment is empty, it is on the border => no intersection.
                return segmentX1 != segmentX2 || segmentY1 != segmentY2;
            }
            if ((code1 & code2) != 0) {
                // Bitwise AND is not 0. Trivially reject and get out of loop
                return false;
            }

            // failed both tests, so calculate the line segment intersection

            // At least one endpoint is outside the clip rectangle; pick it.
            int code3 = (code1 != 0) ? code1 : code2;

            double x = 0;
            double y = 0;

            // Now find the intersection point;
            // use formulas y = y0 + slope * (x - x0), x = x0 + (1 / slope) * (y - y0)
            if ((code3 & MathConstants.COHEN_SUTHERLAND_TOP) != 0) {
                // point is above the clip rectangle
                x = segmentX1 + (segmentX2 - segmentX1) * (ry2 - segmentY1) / (segmentY2 - segmentY1);
                y = ry2;
            } else if ((code3 & MathConstants.COHEN_SUTHERLAND_BOTTOM) != 0) {
                // point is below the clip rectangle
                x = segmentX1 + (segmentX2 - segmentX1) * (ry1 - segmentY1) / (segmentY2 - segmentY1);
                y = ry1;
            } else if ((code3 & MathConstants.COHEN_SUTHERLAND_RIGHT) != 0) {
                // point is to the right of clip rectangle
                y = segmentY1 + (segmentY2 - segmentY1) * (rx2 - segmentX1) / (segmentX2 - segmentX1);
                x = rx2;
            } else if ((code3 & MathConstants.COHEN_SUTHERLAND_LEFT) != 0) {
                // point is to the left of clip rectangle
                y = segmentY1 + (segmentY2 - segmentY1) * (rx1 - segmentX1) / (segmentX2 - segmentX1);
                x = rx1;
            } else {
                code3 = 0;
            }

            if (code3 != 0) {
                // Now we move outside point to intersection point to clip
                // and get ready for next pass.
                if (code3 == code1) {
                    segmentX1 = x;
                    segmentY1 = y;
                    code1 = MathUtil.getCohenSutherlandCode(segmentX1, segmentY1, rx1, ry1, rx2, ry2);
                } else {
                    segmentX2 = x;
                    segmentY2 = y;
                    code2 = MathUtil.getCohenSutherlandCode(segmentX2, segmentY2, rx1, ry1, rx2, ry2);
                }
            }
        }
    }

    /** Replies if a rectangle is inside in the rectangle.
     *
     * @param enclosingX1 is the lowest corner of the enclosing-candidate rectangle.
     * @param enclosingY1 is the lowest corner of the enclosing-candidate rectangle.
     * @param enclosingX2 is the uppest corner of the enclosing-candidate rectangle.
     * @param enclosingY2 is the uppest corner of the enclosing-candidate rectangle.
     * @param innerX1 is the lowest corner of the inner-candidate rectangle.
     * @param innerY1 is the lowest corner of the inner-candidate rectangle.
     * @param innerX2 is the uppest corner of the inner-candidate rectangle.
     * @param innerY2 is the uppest corner of the inner-candidate rectangle.
     * @return <code>true</code> if the given rectangle is inside the ellipse;
     *     otherwise <code>false</code>.
     */
    @Pure
    @SuppressWarnings("checkstyle:magicnumber")
    static boolean containsRectangleRectangle(double enclosingX1, double enclosingY1,
            double enclosingX2, double enclosingY2,
            double innerX1, double innerY1,
            double innerX2, double innerY2) {
        assert enclosingX1 <= enclosingX2 : AssertMessages.lowerEqualParameters(0, enclosingX1, 2, enclosingX2);
        assert enclosingY1 <= enclosingY2 : AssertMessages.lowerEqualParameters(1, enclosingY1, 3, enclosingY2);
        assert innerX1 <= innerX2 : AssertMessages.lowerEqualParameters(4, innerX1, 6, innerX2);
        assert innerY1 <= innerY2 : AssertMessages.lowerEqualParameters(5, innerY1, 7, innerY2);
        return innerX1 >= enclosingX1
                && innerY1 >= enclosingY1
                && innerX2 <= enclosingX2
                && innerY2 <= enclosingY2;
    }

    /** Replies if a point is inside in the rectangle.
     *
     * @param rx1 is the lowest corner of the rectangle.
     * @param ry1 is the lowest corner of the rectangle.
     * @param rx2 is the uppest corner of the rectangle.
     * @param ry2 is the uppest corner of the rectangle.
     * @param px is the point.
     * @param py is the point.
     * @return <code>true</code> if the given point is inside the rectangle;
     *     otherwise <code>false</code>.
     */
    @Pure
    static boolean containsRectanglePoint(
            double rx1, double ry1, double rx2, double ry2,
            double px, double py) {
        assert rx1 <= rx2 : AssertMessages.lowerEqualParameters(0, rx1, 2, rx2);
        assert ry1 <= ry2 : AssertMessages.lowerEqualParameters(1, ry1, 3, ry2);
        return (px >= rx1 && px <= rx2) && (py >= ry1 && py <= ry2);
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

    @Override
    default void set(IT shape) {
        assert shape != null : AssertMessages.notNullParameter();
        setFromCorners(shape.getMinX(), shape.getMinY(), shape.getMaxX(), shape.getMaxY());
    }

    /** {@inheritDoc}
     *
     * <p>The rectangle is always aligned on the global axes.
     * It means that the rectangle is set to the enclosing box related to the given parameters.
     */
    @Override
    @SuppressWarnings("checkstyle:magicnumber")
    default void set(double centerX, double centerY, double axis1x, double axis1y, double axis1Extent,
            double axis2Extent) {
        assert Vector2D.isUnitVector(axis1x, axis1y) : AssertMessages.normalizedParameters(2, 3);
        assert axis1Extent >= 0 : AssertMessages.positiveOrZeroParameter(4);
        assert axis2Extent >= 0 : AssertMessages.positiveOrZeroParameter(5);
        final double mx = Math.max(Math.abs(axis1x * axis1Extent), Math.abs(-axis1y * axis2Extent));
        final double my = Math.max(Math.abs(axis1y * axis1Extent), Math.abs(axis1x * axis2Extent));
        final double vx = OrientedRectangle2afp.findsVectorProjectionRAxisVector(1, 0, mx, my);
        final double vy = OrientedRectangle2afp.findsVectorProjectionSAxisVector(1, 0, mx, my);
        set(centerX - vx, centerY - vy, vx * 2, vy * 2);
    }

    @Pure
    @Override
    default double getDistanceSquared(Point2D<?, ?> pt) {
        assert pt != null : AssertMessages.notNullParameter();
        final double dx;
        if (pt.getX() < getMinX()) {
            dx = getMinX() - pt.getX();
        } else if (pt.getX() > getMaxX()) {
            dx = pt.getX() - getMaxX();
        } else {
            dx = 0f;
        }
        final double dy;
        if (pt.getY() < getMinY()) {
            dy = getMinY() - pt.getY();
        } else if (pt.getY() > getMaxY()) {
            dy = pt.getY() - getMaxY();
        } else {
            dy = 0f;
        }
        return dx * dx + dy * dy;
    }

    @Override
    default double getDistanceSquared(Rectangle2afp<?, ?, ?, ?, ?, ?> rectangle) {
        assert rectangle != null : AssertMessages.notNullParameter();
        final double dx;
        if (rectangle.getMaxX() <= getMinX()) {
            dx = getMinX() - rectangle.getMaxX();
        } else if (rectangle.getMinX() >= getMaxX()) {
            dx = rectangle.getMinX() - getMaxX();
        } else {
            dx = 0;
        }
        final double dy;
        if (rectangle.getMaxY() <= getMinY()) {
            dy = getMinY() - rectangle.getMaxY();
        } else if (rectangle.getMinY() >= getMaxY()) {
            dy = rectangle.getMinY() - getMaxY();
        } else {
            dy = 0;
        }
        return dx * dx + dy * dy;
    }

    @Pure
    @Override
    default double getDistanceL1(Point2D<?, ?> pt) {
        assert pt != null : AssertMessages.notNullParameter();
        final double dx;
        if (pt.getX() < getMinX()) {
            dx = getMinX() - pt.getX();
        } else if (pt.getX() > getMaxX()) {
            dx = pt.getX() - getMaxX();
        } else {
            dx = 0f;
        }
        final double dy;
        if (pt.getY() < getMinY()) {
            dy = getMinY() - pt.getY();
        } else if (pt.getY() > getMaxY()) {
            dy = pt.getY() - getMaxY();
        } else {
            dy = 0f;
        }
        return dx + dy;
    }

    @Pure
    @Override
    default double getDistanceLinf(Point2D<?, ?> pt) {
        assert pt != null : AssertMessages.notNullParameter();
        final double dx;
        if (pt.getX() < getMinX()) {
            dx = getMinX() - pt.getX();
        } else if (pt.getX() > getMaxX()) {
            dx = pt.getX() - getMaxX();
        } else {
            dx = 0f;
        }
        final double dy;
        if (pt.getY() < getMinY()) {
            dy = getMinY() - pt.getY();
        } else if (pt.getY() > getMaxY()) {
            dy = pt.getY() - getMaxY();
        } else {
            dy = 0f;
        }
        return Math.max(dx, dy);
    }

    @Pure
    @Override
    default boolean contains(double x, double y) {
        return (x >= getMinX() && x <= getMaxX())
                &&
                (y >= getMinY() && y <= getMaxY());
    }

    @Pure
    @Override
    default boolean contains(Rectangle2afp<?, ?, ?, ?, ?, ?> rectangle) {
        assert rectangle != null : AssertMessages.notNullParameter();
        return containsRectangleRectangle(
                getMinX(), getMinY(), getMaxX(), getMaxY(),
                rectangle.getMinX(), rectangle.getMinY(), rectangle.getMaxX(), rectangle.getMaxY());
    }

    /** Add the given coordinate in the rectangle.
     *
     * <p>The corners of the rectangles are moved to
     * enclosed the given coordinate.
     *
     * @param pt the point.
     */
    default void add(Point2D<?, ?> pt) {
        assert pt != null : AssertMessages.notNullParameter();
        add(pt.getX(), pt.getY());
    }

    /** Add the given coordinate in the rectangle.
     *
     * <p>The corners of the rectangles are moved for
     * enclosing the given coordinate.
     *
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     */
    default void add(double x, double y) {
        if (x < getMinX()) {
            setMinX(x);
        } else if (x > getMaxX()) {
            setMaxX(x);
        }
        if (y < getMinY()) {
            setMinY(y);
        } else if (y > getMaxY()) {
            setMaxY(y);
        }
    }

    /** Compute and replies the union of this rectangle and the given rectangle.
     * This function does not change this rectangle.
     *
     * <p>It is equivalent to (where <code>ur</code> is the union):
     * <pre><code>
     * Rectangle2f ur = new Rectangle2f(this);
     * ur.setUnion(r);
     * </code></pre>
     *
     * @param rect the rectangular shape.
     * @return the union of this rectangle and the given rectangle.
     * @see #setUnion(RectangularShape2afp)
     */
    @Pure
    default B createUnion(RectangularShape2afp<?, ?, ?, ?, ?, ?> rect) {
        assert rect != null : AssertMessages.notNullParameter();
        final B rr = getGeomFactory().newBox();
        rr.setFromCorners(getMinX(), getMinY(), getMaxX(), getMaxY());
        rr.setUnion(rect);
        return rr;
    }

    /** Compute and replies the intersection of this rectangle and the given rectangle.
     * This function does not change this rectangle.
     *
     * <p>It is equivalent to (where <code>ir</code> is the intersection):
     * <pre><code>
     * Rectangle2f ir = new Rectangle2f(this);
     * ir.setIntersection(r);
     * </code></pre>
     *
     * @param rect the rectangular shape.
     * @return the union of this rectangle and the given rectangle.
     * @see #setIntersection(RectangularShape2afp)
     */
    @Pure
    default B createIntersection(RectangularShape2afp<?, ?, ?, ?, ?, ?> rect) {
        assert rect != null : AssertMessages.notNullParameter();
        final B rr = getGeomFactory().newBox();
        final double x1 = Math.max(getMinX(), rect.getMinX());
        final double y1 = Math.max(getMinY(), rect.getMinY());
        final double x2 = Math.min(getMaxX(), rect.getMaxX());
        final double y2 = Math.min(getMaxY(), rect.getMaxY());
        if (x1 <= x2 && y1 <= y2) {
            rr.setFromCorners(x1, y1, x2, y2);
        } else {
            rr.clear();
        }
        return rr;
    }

    /** Compute the union of this rectangle and the given rectangle and
     * change this rectangle with the result of the union.
     *
     * @param rect the rectangular shape.
     * @see #createUnion(RectangularShape2afp)
     */
    default void setUnion(RectangularShape2afp<?, ?, ?, ?, ?, ?> rect) {
        assert rect != null : AssertMessages.notNullParameter();
        setFromCorners(
                Math.min(getMinX(), rect.getMinX()),
                Math.min(getMinY(), rect.getMinY()),
                Math.max(getMaxX(), rect.getMaxX()),
                Math.max(getMaxY(), rect.getMaxY()));
    }

    /** Compute the intersection of this rectangle and the given rectangle.
     * This function changes this rectangle.
     *
     * <p>If there is no intersection, this rectangle is cleared.
     *
     * @param rect the rectangular shape.
     * @see #createIntersection(RectangularShape2afp)
     * @see #clear()
     */
    default void setIntersection(RectangularShape2afp<?, ?, ?, ?, ?, ?> rect) {
        assert rect != null : AssertMessages.notNullParameter();
        final double x1 = Math.max(getMinX(), rect.getMinX());
        final double y1 = Math.max(getMinY(), rect.getMinY());
        final double x2 = Math.min(getMaxX(), rect.getMaxX());
        final double y2 = Math.min(getMaxY(), rect.getMaxY());
        if (x1 <= x2 && y1 <= y2) {
            setFromCorners(x1, y1, x2, y2);
        } else {
            clear();
        }
    }

    @Pure
    @Override
    default boolean intersects(Rectangle2afp<?, ?, ?, ?, ?, ?> rectangle) {
        assert rectangle != null : AssertMessages.notNullParameter();
        return intersectsRectangleRectangle(
                getMinX(), getMinY(),
                getMaxX(), getMaxY(),
                rectangle.getMinX(), rectangle.getMinY(),
                rectangle.getMaxX(), rectangle.getMaxY());
    }

    @Pure
    @Override
    default boolean intersects(Ellipse2afp<?, ?, ?, ?, ?, ?> ellipse) {
        assert ellipse != null : AssertMessages.notNullParameter();
        return Ellipse2afp.intersectsEllipseRectangle(
                ellipse.getMinX(), ellipse.getMinY(),
                ellipse.getWidth(), ellipse.getHeight(),
                getMinX(), getMinY(),
                getMaxX(), getMaxY());
    }

    @Pure
    @Override
    default boolean intersects(Circle2afp<?, ?, ?, ?, ?, ?> circle) {
        assert circle != null : AssertMessages.notNullParameter();
        return Circle2afp.intersectsCircleRectangle(
                circle.getX(), circle.getY(),
                circle.getRadius(),
                getMinX(), getMinY(),
                getMaxX(), getMaxY());
    }

    @Pure
    @Override
    default boolean intersects(Segment2afp<?, ?, ?, ?, ?, ?> segment) {
        assert segment != null : AssertMessages.notNullParameter();
        return intersectsRectangleSegment(
                getMinX(), getMinY(),
                getMaxX(), getMaxY(),
                segment.getX1(), segment.getY1(),
                segment.getX2(), segment.getY2());
    }

    @Pure
    @Override
    default boolean intersects(OrientedRectangle2afp<?, ?, ?, ?, ?, ?> orientedRectangle) {
        assert orientedRectangle != null : AssertMessages.notNullParameter();
        return OrientedRectangle2afp.intersectsOrientedRectangleRectangle(
                orientedRectangle.getCenterX(), orientedRectangle.getCenterY(),
                orientedRectangle.getFirstAxisX(), orientedRectangle.getFirstAxisY(), orientedRectangle.getFirstAxisExtent(),
                orientedRectangle.getSecondAxisExtent(),
                getMinX(), getMinY(), getWidth(), getHeight());
    }

    @Pure
    @Override
    default boolean intersects(Parallelogram2afp<?, ?, ?, ?, ?, ?> parallelogram) {
        assert parallelogram != null : AssertMessages.notNullParameter();
        return Parallelogram2afp.intersectsParallelogramRectangle(
                parallelogram.getCenterX(), parallelogram.getCenterY(),
                parallelogram.getFirstAxisX(), parallelogram.getFirstAxisY(), parallelogram.getFirstAxisExtent(),
                parallelogram.getSecondAxisX(), parallelogram.getSecondAxisY(), parallelogram.getSecondAxisExtent(),
                getMinX(), getMinY(), getWidth(), getHeight());
    }

    @Pure
    @Override
    default boolean intersects(RoundRectangle2afp<?, ?, ?, ?, ?, ?> roundRectangle) {
        assert roundRectangle != null : AssertMessages.notNullParameter();
        return RoundRectangle2afp.intersectsRoundRectangleRectangle(
                roundRectangle.getMinX(), roundRectangle.getMinY(),
                roundRectangle.getMaxX(), roundRectangle.getMaxY(),
                roundRectangle.getArcWidth(), roundRectangle.getArcHeight(),
                getMinX(), getMinY(),
                getMaxX(), getMaxY());
    }

    @Override
    default boolean intersects(Triangle2afp<?, ?, ?, ?, ?, ?> triangle) {
        assert triangle != null : AssertMessages.notNullParameter();
        return Triangle2afp.intersectsTriangleRectangle(
                triangle.getX1(), triangle.getY1(),
                triangle.getX2(), triangle.getY2(),
                triangle.getX3(), triangle.getY3(),
                getMinX(), getMinY(),
                getWidth(), getHeight());
    }

    @Pure
    @Override
    default boolean intersects(PathIterator2afp<?> iterator) {
        assert iterator != null : AssertMessages.notNullParameter();
        final int mask = iterator.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
        final int crossings = Path2afp.calculatesCrossingsPathIteratorRectangleShadow(
                0,
                iterator,
                getMinX(), getMinY(), getMaxX(), getMaxY(),
                CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
        return crossings == MathConstants.SHAPE_INTERSECTS
                || (crossings & mask) != 0;

    }

    @Pure
    @Override
    default boolean intersects(MultiShape2afp<?, ?, ?, ?, ?, ?, ?> multishape) {
        assert multishape != null : AssertMessages.notNullParameter();
        return multishape.intersects(this);
    }

    /** Move this rectangle to avoid collision
     * with the reference rectangle.
     *
     * @param reference is the rectangle to avoid collision with.
     * @param result the displacement vector.
     */
    default void avoidCollisionWith(Rectangle2afp<?, ?, ?, ?, ?, ?> reference, Vector2D<?, ?> result) {
        assert reference != null : AssertMessages.notNullParameter();
        assert result != null : AssertMessages.notNullParameter();
        final double dx1 = reference.getMaxX() - getMinX();
        final double dx2 = getMaxX() - reference.getMinX();
        final double dy1 = reference.getMaxY() - getMinY();
        final double dy2 = getMaxY() - reference.getMinY();

        final double absdx1 = Math.abs(dx1);
        final double absdx2 = Math.abs(dx2);
        final double absdy1 = Math.abs(dy1);
        final double absdy2 = Math.abs(dy2);

        final double dx;
        final double dy;

        if (dx1 >= 0 && absdx1 <= absdx2 && absdx1 <= absdy1 && absdx1 <= absdy2) {
            // Move according to dx1
            dx = dx1;
            dy = 0;
        } else if (dx2 >= 0 && absdx2 <= absdx1 && absdx2 <= absdy1 && absdx2 <= absdy2) {
            // Move according to dx2
            dx = -dx2;
            dy = 0;
        } else if (dy1 >= 0 && absdy1 <= absdx1 && absdy1 <= absdx2 && absdy1 <= absdy2) {
            // Move according to dy1
            dx = 0;
            dy = dy1;
        } else {
            // Move according to dy2
            dx = 0;
            dy = -dy2;
        }

        set(
                getMinX() + dx,
                getMinY() + dy,
                getWidth(),
                getHeight());

        result.set(dx, dy);
    }

    /** Move this rectangle to avoid collision
     * with the reference rectangle.
     *
     * @param reference is the rectangle to avoid collision with.
     * @param displacementDirection is the direction of the allowed displacement (it is an input).
     *     This vector is set according to the result before returning.
     * @param result the displacement vector.
     */
    default void avoidCollisionWith(Rectangle2afp<?, ?, ?, ?, ?, ?> reference,
            Vector2D<?, ?> displacementDirection, Vector2D<?, ?> result) {
        assert reference != null : AssertMessages.notNullParameter(0);
        assert result != null : AssertMessages.notNullParameter(2);
        if (displacementDirection == null || displacementDirection.getLengthSquared() == 0) {
            avoidCollisionWith(reference, result);
            return;
        }

        final double dx1 = reference.getMaxX() - getMinX();
        final double dx2 = reference.getMinX() - getMaxX();
        final double dy1 = reference.getMaxY() - getMinY();
        final double dy2 = reference.getMinY() - getMaxY();

        final double absdx1 = Math.abs(dx1);
        final double absdx2 = Math.abs(dx2);
        final double absdy1 = Math.abs(dy1);
        final double absdy2 = Math.abs(dy2);

        final double dx;
        final double dy;

        if (displacementDirection.getX() < 0) {
            dx = -Math.min(absdx1, absdx2);
        } else {
            dx = Math.min(absdx1, absdx2);
        }

        if (displacementDirection.getY() < 0) {
            dy = -Math.min(absdy1, absdy2);
        } else {
            dy = Math.min(absdy1, absdy2);
        }

        set(
                getMinX() + dx,
                getMinY() + dy,
                getWidth(),
                getHeight());

        displacementDirection.set(dx, dy);
        result.set(dx, dy);
    }

    @Pure
    @Override
    default P getClosestPointTo(Point2D<?, ?> pt) {
        assert pt != null : AssertMessages.notNullParameter();
        final double x;
        int same = 0;
        if (pt.getX() < getMinX()) {
            x = getMinX();
        } else if (pt.getX() > getMaxX()) {
            x = getMaxX();
        } else {
            x = pt.getX();
            ++same;
        }
        final double y;
        if (pt.getY() < getMinY()) {
            y = getMinY();
        } else if (pt.getY() > getMaxY()) {
            y = getMaxY();
        } else {
            y = pt.getY();
            ++same;
        }
        if (same == 2) {
            return getGeomFactory().convertToPoint(pt);
        }
        return getGeomFactory().newPoint(x, y);
    }

    @Override
    default P getClosestPointTo(Circle2afp<?, ?, ?, ?, ?, ?> circle) {
        assert circle != null : AssertMessages.notNullParameter();
        return getClosestPointTo(circle.getCenter());
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
    default P getClosestPointTo(Path2afp<?, ?, ?, ?, ?, ?> path) {
        assert path != null : AssertMessages.notNullParameter();
        final P point = getGeomFactory().newPoint();
        Path2afp.findsClosestPointPathIteratorPathIterator(getPathIterator(), path.getPathIterator(), point);
        return point;
    }

    @Override
    default P getClosestPointTo(Rectangle2afp<?, ?, ?, ?, ?, ?> rectangle) {
        assert rectangle != null : AssertMessages.notNullParameter();
        final P point = getGeomFactory().newPoint();
        findsClosestPointRectangleRectangle(getMinX(), getMinY(), getMaxX(), getMaxY(),
                rectangle.getMinX(), rectangle.getMinY(), rectangle.getMaxX(), rectangle.getMaxY(),
                point);
        return point;
    }

    @Override
    default P getClosestPointTo(Segment2afp<?, ?, ?, ?, ?, ?> segment) {
        assert segment != null : AssertMessages.notNullParameter();
        final P point = getGeomFactory().newPoint();
        findsClosestPointRectangleSegment(
                getMinX(), getMinY(), getMaxX(), getMaxY(),
                segment.getX1(), segment.getY1(), segment.getX2(), segment.getY2(), point);
        return point;
    }

    @Override
    default P getClosestPointTo(Ellipse2afp<?, ?, ?, ?, ?, ?> ellipse) {
        assert ellipse != null : AssertMessages.notNullParameter();
        final double cx = ellipse.getCenterX();
        final double cy = ellipse.getCenterY();
        final double sx = ellipse.getHorizontalRadius();
        final double sy = ellipse.getVerticalRadius();
        // Scale the rectangle
        final double rx = (getMinX() - cx) / sx;
        final double ry = (getMinY() - cy) / sy;
        final double rwidth = getWidth() / sx;
        final double rheight = getHeight() / sy;
        // Compute the closest point
        final P closest = getGeomFactory().newPoint();
        findsClosestPointRectanglePoint(rx, ry, rx + rwidth, ry + rheight, 0, 0, closest);
        // Invert scale
        closest.set(closest.getX() * sx + cx, closest.getY() * sy + cy);
        return closest;
    }

    @Override
    default P getClosestPointTo(RoundRectangle2afp<?, ?, ?, ?, ?, ?> roundRectangle) {
        assert roundRectangle != null : AssertMessages.notNullParameter();
        final P point = getGeomFactory().newPoint();
        findsClosestPointRectangleRectangle(getMinX(), getMinY(), getMaxX(), getMaxY(),
                roundRectangle.getMinX(), roundRectangle.getMinY(), roundRectangle.getMaxX(), roundRectangle.getMaxY(),
                point);
        return point;
    }

    @Override
    default P getClosestPointTo(Parallelogram2afp<?, ?, ?, ?, ?, ?> parallelogram) {
        assert parallelogram != null : AssertMessages.notNullParameter();
        final P point = getGeomFactory().newPoint();
        findsClosestPointRectangleParallelogram(
                getMinX(), getMinY(), getMaxX(), getMaxY(),
                parallelogram.getCenterX(), parallelogram.getCenterY(),
                parallelogram.getFirstAxisX(), parallelogram.getFirstAxisY(), parallelogram.getFirstAxisExtent(),
                parallelogram.getSecondAxisX(), parallelogram.getSecondAxisY(), parallelogram.getSecondAxisExtent(),
                point);
        return point;
    }

    @Override
    default P getClosestPointTo(OrientedRectangle2afp<?, ?, ?, ?, ?, ?> orientedRectangle) {
        assert orientedRectangle != null : AssertMessages.notNullParameter();
        final P point = getGeomFactory().newPoint();
        findsClosestPointRectangleParallelogram(
                getMinX(), getMinY(), getMaxX(), getMaxY(),
                orientedRectangle.getCenterX(), orientedRectangle.getCenterY(),
                orientedRectangle.getFirstAxisX(), orientedRectangle.getFirstAxisY(), orientedRectangle.getFirstAxisExtent(),
                orientedRectangle.getSecondAxisX(), orientedRectangle.getSecondAxisY(), orientedRectangle.getSecondAxisExtent(),
                point);
        return point;
    }

    @Pure
    @Override
    default P getFarthestPointTo(Point2D<?, ?> pt) {
        assert pt != null : AssertMessages.notNullParameter();
        final double x;
        if (pt.getX() <= getCenterX()) {
            x = getMaxX();
        } else {
            x = getMinX();
        }
        final double y;
        if (pt.getY() <= getCenterY()) {
            y = getMaxY();
        } else {
            y = getMinY();
        }
        return getGeomFactory().newPoint(x, y);
    }

    @Pure
    @Override
    default PathIterator2afp<IE> getPathIterator(Transform2D transform) {
        if (transform == null || transform.isIdentity()) {
            return new RectanglePathIterator<>(this);
        }
        return new TransformedRectanglePathIterator<>(this, transform);
    }

    @Override
    default P getCenter() {
        return getGeomFactory().newPoint(getCenterX(), getCenterY());
    }

    @Override
    default void setCenter(double cx, double cy) {
        final double demiWidth = getWidth() / 2.;
        final double demiHeight = getHeight() / 2.;
        setMinX(cx - demiWidth);
        setMinY(cy - demiHeight);
        setMaxX(cx + demiWidth);
        setMaxY(cy + demiHeight);
    }

    @Override
    default void setCenterX(double cx) {
        final double demiWidth = getWidth() / 2.;
        setMinX(cx - demiWidth);
        setMaxX(cx + demiWidth);
    }

    @Override
    default void setCenterY(double cy) {
        final double demiHeight = getHeight() / 2.;
        setMinY(cy - demiHeight);
        setMaxY(cy + demiHeight);
    }

    @Override
    default V getFirstAxis() {
        return getGeomFactory().newVector(getFirstAxisX(), getFirstAxisY());
    }

    @Override
    default double getFirstAxisX() {
        return 1.;
    }

    @Override
    default double getFirstAxisY() {
        return 0.;
    }

    @Override
    default V getSecondAxis() {
        return getGeomFactory().newVector(getSecondAxisX(), getSecondAxisY());
    }

    @Override
    default double getSecondAxisX() {
        return 0.;
    }

    @Override
    default double getSecondAxisY() {
        return 1.;
    }

    @Override
    default double getFirstAxisExtent() {
        return getWidth() / 2.;
    }

    @Override
    default void setFirstAxisExtent(double extent) {
        final double x = getCenterX();
        setMinX(x - extent);
        setMaxX(x + extent);
    }

    @Override
    default double getSecondAxisExtent() {
        return getHeight() / 2.;
    }

    @Override
    default void setSecondAxisExtent(double extent) {
        final double y = getCenterY();
        setMinY(y - extent);
        setMaxY(y + extent);
    }

    /** {@inheritDoc}
     *
     * <p>The rectangle is always aligned on the global axes.
     * It means that the rectangle is set to the enclosing box related to the given parameters.
     */
    @Override
    default void setFirstAxis(double x, double y, double extent) {
        assert Vector2D.isUnitVector(x, y) : AssertMessages.normalizedParameters(0, 1);
        assert extent >= 0. : AssertMessages.positiveOrZeroParameter(2);
        set(getCenterX(), getCenterY(), x, y, extent, getSecondAxisExtent());
    }

    /** {@inheritDoc}
     *
     * <p>The rectangle is always aligned on the global axes.
     * It means that the rectangle is set to the enclosing box related to the given parameters.
     */
    @Override
    default void setSecondAxis(double x, double y, double extent) {
        assert Vector2D.isUnitVector(x, y) : AssertMessages.normalizedParameters(0, 1);
        assert extent >= 0. : AssertMessages.positiveOrZeroParameter(2);
        set(getCenterX(), getCenterY(), y, -x, getFirstAxisExtent(), extent);
    }

    //
    // Avoid multiple inheritance error
    //

    @Override
    default void clear() {
        RectangularShape2afp.super.clear();
    }

    @Override
    default double getCenterX() {
        return RectangularShape2afp.super.getCenterX();
    }

    @Override
    default double getCenterY() {
        return RectangularShape2afp.super.getCenterY();
    }

    @Override
    default void translate(double dx, double dy) {
        RectangularShape2afp.super.translate(dx, dy);
    }

    @Override
    default void toBoundingBox(B box) {
        RectangularShape2afp.super.toBoundingBox(box);
    }

    @Override
    default boolean isEmpty() {
        return RectangularShape2afp.super.isEmpty();
    }

    /**
     * Classifies two 2D axis-aligned rectangles.
     *
     * <p>This function is assuming that <var>lx1</var> is lower
     * or equal to <var>ux1</var>, <var>ly1</var> is lower
     * or equal to <var>uy1</var>, and so on.
     *
     * @param lx1 the X coordinate of the lowest point of the first rectangle.
     * @param ly1 the Y coordinate of the lowest point of the first rectangle.
     * @param ux1 the X coordinate of the uppest point of the first rectangle.
     * @param uy1 the Y coordinate of the uppest point of the first rectangle.
     * @param lx2 the X coordinate of the lowest point of the second rectangle.
     * @param ly2 the Y coordinate of the lowest point of the second rectangle.
     * @param ux2 the X coordinate of the uppest point of the second rectangle.
     * @param uy2 the Y coordinate of the uppest point of the second rectangle.
	 * @return the value {@link IntersectionType#INSIDE} if the first rectangle is inside
	 *     the second rectangle; {@link IntersectionType#OUTSIDE} if the first rectangle is
	 *     outside the second rectangle; {@link IntersectionType#ENCLOSING} if the
	 *     first rectangle is enclosing the second rectangle;
	 *     {@link IntersectionType#ENCLOSING} if the first rectangle is the same as the second rectangle;
	 *     {@link IntersectionType#SPANNING} otherwise.
	 * @since 14.0
     */
    @SuppressWarnings({"checkstyle:magicnumber", "checkstyle:cyclomaticcomplexity", "checkstyle:returncount"})
    static IntersectionType classifiesRectangleRectangle(double lx1, double ly1, double ux1, double uy1,
    		double lx2, double ly2, double ux2, double uy2) {
    	assert lx1 <= ux1 : AssertMessages.lowerEqualParameters(0, lx1, 2, ux1);
    	assert ly1 <= uy1 : AssertMessages.lowerEqualParameters(1, ly1, 3, uy1);
    	assert lx2 <= ux2 : AssertMessages.lowerEqualParameters(4, lx2, 6, ux2);
    	assert ly2 <= uy2 : AssertMessages.lowerEqualParameters(5, ly2, 7, uy2);

    	final IntersectionType inter;
    	if (lx1 < lx2) {
    		if (ux1 <= lx2) {
    			return IntersectionType.OUTSIDE;
    		}
    		if (ux1 < ux2) {
    			inter = IntersectionType.SPANNING;
    		} else {
    			inter = IntersectionType.ENCLOSING;
    		}
    	} else if (lx1 > lx2) {
			if (ux2 <= lx1) {
				return IntersectionType.OUTSIDE;
			}
			if (ux1 <= ux2) {
				inter = IntersectionType.INSIDE;
			} else {
				inter = IntersectionType.SPANNING;
			}
    	} else {
    		if (ux1 == ux2) {
    			inter = IntersectionType.SAME;
    		} else if (ux1 < ux2) {
    			inter = IntersectionType.INSIDE;
    		} else {
    			inter = IntersectionType.ENCLOSING;
    		}
    	}

    	if (ly1 < ly2) {
    		if (uy1 <= ly2) {
    			return IntersectionType.OUTSIDE;
    		}
    		if (uy1 < uy2) {
    			return inter.and(IntersectionType.SPANNING);
    		}
    		return inter.and(IntersectionType.ENCLOSING);
    	} else if (ly1 > ly2) {
			if (uy2 <= ly1) {
				return IntersectionType.OUTSIDE;
			}
			if (uy1 <= uy2) {
				return inter.and(IntersectionType.INSIDE);
			}
			return inter.and(IntersectionType.SPANNING);
    	} else {
    		if (uy1 == uy2) {
    			return inter.and(IntersectionType.SAME);
    		}
    		if (uy1 < uy2) {
    			return inter.and(IntersectionType.INSIDE);
    		}
    		return inter.and(IntersectionType.ENCLOSING);
    	}
    }

    /** Iterator on the path elements of the rectangle.
     *
     * @param <T> the type of the path elements.
     * @author $Author: sgalland$
     * @version $FullVersion$
     * @mavengroupid $GroupId$
     * @mavenartifactid $ArtifactId$
     * @since 13.0
     */
    @SuppressWarnings("checkstyle:magicnumber")
    class RectanglePathIterator<T extends PathElement2afp>
            implements PathIterator2afp<T> {

        private final Rectangle2afp<?, ?, T, ?, ?, ?> rectangle;

        private double x1;

        private double y1;

        private double x2;

        private double y2;

        private int index;

        /** Constructor.
         * @param rectangle the iterated rectangle.
         */
        public RectanglePathIterator(Rectangle2afp<?, ?, T, ?, ?, ?> rectangle) {
            assert rectangle != null : AssertMessages.notNullParameter();
            this.rectangle = rectangle;
            if (rectangle.isEmpty()) {
                this.index = 5;
            } else {
                this.x1 = rectangle.getMinX();
                this.x2 = rectangle.getMaxX();
                this.y1 = rectangle.getMinY();
                this.y2 = rectangle.getMaxY();
            }
        }

        @Override
        public PathIterator2afp<T> restartIterations() {
            return new RectanglePathIterator<>(this.rectangle);
        }

        @Pure
        @Override
        public boolean hasNext() {
            return this.index <= 4;
        }

        @Override
        public T next() {
            final int idx = this.index;
            ++this.index;
            switch (idx) {
            case 0:
                return this.rectangle.getGeomFactory().newMovePathElement(
                        this.x1, this.y1);
            case 1:
                return this.rectangle.getGeomFactory().newLinePathElement(
                        this.x1, this.y1,
                        this.x2, this.y1);
            case 2:
                return this.rectangle.getGeomFactory().newLinePathElement(
                        this.x2, this.y1,
                        this.x2, this.y2);
            case 3:
                return this.rectangle.getGeomFactory().newLinePathElement(
                        this.x2, this.y2,
                        this.x1, this.y2);
            case 4:
                return this.rectangle.getGeomFactory().newClosePathElement(
                        this.x1, this.y2,
                        this.x1, this.y1);
            default:
                throw new NoSuchElementException();
            }
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

        @Override
        public GeomFactory2afp<T, ?, ?, ?> getGeomFactory() {
            return this.rectangle.getGeomFactory();
        }

    }

    /** Iterator on the path elements of the rectangle.
     *
     * @param <T> the type of the path elements.
     * @author $Author: sgalland$
     * @version $FullVersion$
     * @mavengroupid $GroupId$
     * @mavenartifactid $ArtifactId$
     */
    @SuppressWarnings("checkstyle:magicnumber")
    class TransformedRectanglePathIterator<T extends PathElement2afp> implements PathIterator2afp<T> {

        private final Transform2D transform;

        private final Rectangle2afp<?, ?, T, ?, ?, ?> rectangle;

        private Point2D<?, ?> p1;

        private Point2D<?, ?> p2;

        private double x1;

        private double y1;

        private double x2;

        private double y2;

        private int index;

        /** Constructor.
         * @param rectangle the iterated rectangle.
         * @param transform the transformation.
         */
        public TransformedRectanglePathIterator(Rectangle2afp<?, ?, T, ?, ?, ?> rectangle, Transform2D transform) {
            assert rectangle != null : AssertMessages.notNullParameter(0);
            assert transform != null : AssertMessages.notNullParameter(1);
            this.rectangle = rectangle;
            this.transform = transform;
            if (rectangle.isEmpty()) {
                this.index = 5;
            } else {
                this.index = 0;
                this.p1 = new InnerComputationPoint2afp();
                this.p2 = new InnerComputationPoint2afp();
                this.x1 = rectangle.getMinX();
                this.x2 = rectangle.getMaxX();
                this.y1 = rectangle.getMinY();
                this.y2 = rectangle.getMaxY();
            }
        }

        @Override
        public PathIterator2afp<T> restartIterations() {
            return new TransformedRectanglePathIterator<>(this.rectangle, this.transform);
        }

        @Pure
        @Override
        public boolean hasNext() {
            return this.index <= 4;
        }

        @Override
        public T next() {
            final int idx = this.index;
            ++this.index;
            switch (idx) {
            case 0:
                this.p2.set(this.x1, this.y1);
                this.transform.transform(this.p2);
                return this.rectangle.getGeomFactory().newMovePathElement(
                        this.p2.getX(), this.p2.getY());
            case 1:
                this.p1.set(this.p2);
                this.p2.set(this.x2, this.y1);
                this.transform.transform(this.p2);
                return this.rectangle.getGeomFactory().newLinePathElement(
                        this.p1.getX(), this.p1.getY(),
                        this.p2.getX(), this.p2.getY());
            case 2:
                this.p1.set(this.p2);
                this.p2.set(this.x2, this.y2);
                this.transform.transform(this.p2);
                return this.rectangle.getGeomFactory().newLinePathElement(
                        this.p1.getX(), this.p1.getY(),
                        this.p2.getX(), this.p2.getY());
            case 3:
                this.p1.set(this.p2);
                this.p2.set(this.x1, this.y2);
                this.transform.transform(this.p2);
                return this.rectangle.getGeomFactory().newLinePathElement(
                        this.p1.getX(), this.p1.getY(),
                        this.p2.getX(), this.p2.getY());
            case 4:
                this.p1.set(this.p2);
                this.p2.set(this.x1, this.y1);
                this.transform.transform(this.p2);
                return this.rectangle.getGeomFactory().newClosePathElement(
                        this.p1.getX(), this.p1.getY(),
                        this.p2.getX(), this.p2.getY());
            default:
                throw new NoSuchElementException();
            }
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

        @Override
        public GeomFactory2afp<T, ?, ?, ?> getGeomFactory() {
            return this.rectangle.getGeomFactory();
        }

    }

}
