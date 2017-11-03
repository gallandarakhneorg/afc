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
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.afp.Circle2afp.AbstractCirclePathIterator;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** Fonctional interface that represented a 2D round rectangle on a plane.
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
public interface RoundRectangle2afp<
        ST extends Shape2afp<?, ?, IE, P, V, B>,
        IT extends RoundRectangle2afp<?, ?, IE, P, V, B>,
        IE extends PathElement2afp,
        P extends Point2D<? super P, ? super V>,
        V extends Vector2D<? super V, ? super P>,
        B extends Rectangle2afp<?, ?, IE, P, V, B>>
        extends RectangularShape2afp<ST, IT, IE, P, V, B> {

    /** Replies if a rectangle is inside in the round rectangle.
     *
     * @param rx1 is the lowest corner of the round rectangle.
     * @param ry1 is the lowest corner of the round rectangle.
     * @param rwidth1 is the width of the round rectangle.
     * @param rheight1 is the height of the round rectangle.
     * @param awidth is the width of the arc of the round rectangle.
     * @param aheight is the height of the arc of the round rectangle.
     * @param rx2 is the lowest corner of the inner-candidate rectangle.
     * @param ry2 is the lowest corner of the inner-candidate rectangle.
     * @param rwidth2 is the width of the inner-candidate rectangle.
     * @param rheight2 is the height of the inner-candidate rectangle.
     * @return <code>true</code> if the given rectangle is inside the ellipse;
     *      otherwise <code>false</code>.
     */
    @Pure
    @SuppressWarnings({"checkstyle:parameternumber", "checkstyle:magicnumber"})
    static boolean containsRoundRectangleRectangle(double rx1, double ry1, double rwidth1, double rheight1,
            double awidth, double aheight, double rx2, double ry2, double rwidth2, double rheight2) {
        assert rwidth1 >= 0. : AssertMessages.positiveOrZeroParameter(2);
        assert rheight1 >= 0. : AssertMessages.positiveOrZeroParameter(3);
        assert awidth >= 0. : AssertMessages.positiveOrZeroParameter(4);
        assert aheight >= 0. : AssertMessages.positiveOrZeroParameter(5);
        assert rwidth2 >= 0. : AssertMessages.positiveOrZeroParameter(8);
        assert rheight2 >= 0. : AssertMessages.positiveOrZeroParameter(9);
        final double rcx1 = rx1 + rwidth1 / 2;
        final double rcy1 = ry1 + rheight1 / 2;
        final double rcx2 = rx2 + rwidth2 / 2;
        final double rcy2 = ry2 + rheight2 / 2;
        final double farX;
        if (rcx1 <= rcx2) {
            farX = rx2 + rwidth2;
        } else {
            farX = rx2;
        }
        final double farY;
        if (rcy1 <= rcy2) {
            farY = ry2 + rheight2;
        } else {
            farY = ry2;
        }
        return containsRoundRectanglePoint(rx1, ry1, rwidth1, rheight1, awidth, aheight, farX, farY);
    }

    /** Replies if a point is inside in the round rectangle.
     *
     * @param rx is the lowest corner of the round rectangle.
     * @param ry is the lowest corner of the round rectangle.
     * @param rwidth is the width of the round rectangle.
     * @param rheight is the height of the round rectangle.
     * @param awidth is the width of the arc of the round rectangle.
     * @param aheight is the height of the arc of the round rectangle.
     * @param px is the point.
     * @param py is the point.
     * @return <code>true</code> if the given rectangle is inside the ellipse;
     *     otherwise <code>false</code>.
     */
    @Pure
    @SuppressWarnings("checkstyle:magicnumber")
    static boolean containsRoundRectanglePoint(double rx, double ry, double rwidth, double rheight,
            double awidth, double aheight, double px, double py) {
        assert rwidth >= 0. : AssertMessages.positiveOrZeroParameter(2);
        assert rheight >= 0. : AssertMessages.positiveOrZeroParameter(3);
        assert awidth >= 0. : AssertMessages.positiveOrZeroParameter(4);
        assert aheight >= 0. : AssertMessages.positiveOrZeroParameter(5);
        if (rwidth <= 0 && rheight <= 0) {
            return rx == px && ry == py;
        }
        final double rrx = rx + rwidth;
        final double rry = ry + rheight;
        // Check for trivial rejection - point is outside bounding rectangle
        if (px < rx || py < ry || px > rrx || py > rry) {
            return false;
        }
        final double aw = Math.min(rwidth, Math.abs(awidth)) / 2.;
        final double ah = Math.min(rheight, Math.abs(aheight)) / 2.;
        // Check which corner point is in and do circular containment
        // test - otherwise simple acceptance
        final double irx = rx + aw;
        final double bx;
        if (px < irx) {
            bx = irx;
        } else {
            final double irrx = rrx - aw;
            if (px > irrx) {
                bx = irrx;
            } else {
                return true;
            }
        }
        final double iry = ry + ah;
        final double by;
        if (py < iry) {
            by = iry;
        } else {
            final double irry = rry - ah;
            if (py > irry) {
                by = irry;
            } else {
                return true;
            }
        }
        final double xx = (px - bx) / aw;
        final double yy = (py - by) / ah;
        return xx * xx + yy * yy <= 1.;
    }

    /** Replies if the round rectangle is intersecting the segment.
     *
     * @param rx1 is the first corner of the rectangle.
     * @param ry1 is the first corner of the rectangle.
     * @param rx2 is the second corner of the rectangle.
     * @param ry2 is the second corner of the rectangle.
     * @param aw is the width of the arcs of the rectangle.
     * @param ah is the height of the arcs of the rectangle.
     * @param sx1 is the first point of the segment.
     * @param sy1 is the first point of the segment.
     * @param sx2 is the second point of the segment.
     * @param sy2 is the second point of the segment.
     * @return <code>true</code> if the two shapes are intersecting; otherwise
     * <code>false</code>
     */
    @Pure
    @SuppressWarnings({"checkstyle:parameternumber", "checkstyle:cyclomaticcomplexity",
        "checkstyle:npathcomplexity", "checkstyle:nestedifdepth", "checkstyle:magicnumber"})
    static boolean intersectsRoundRectangleSegment(double rx1, double ry1, double rx2, double ry2,
            double aw, double ah, double sx1, double sy1, double sx2, double sy2) {
        assert rx1 <= rx2 : AssertMessages.lowerEqualParameters(0, rx1, 2, rx2);
        assert ry1 <= ry2 : AssertMessages.lowerEqualParameters(1, ry1, 3, ry2);
        assert aw >= 0. : AssertMessages.positiveOrZeroParameter(4);
        assert ah >= 0. : AssertMessages.positiveOrZeroParameter(5);
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
                if (segmentX1 != segmentX2 || segmentY1 != segmentY2) {
                    // Special case: intersecting outside the corner's ellipse.
                    final double ellipseMinX = rx1 + aw;
                    if (segmentX1 <= ellipseMinX && segmentX2 <= ellipseMinX) {
                        final double ellipseMinY = ry1 + ah;
                        if (segmentY1 <= ellipseMinY && segmentY2 <= ellipseMinY) {
                            return Ellipse2afp.intersectsEllipseSegment(
                                    rx1, ry1, aw * 2, ah * 2,
                                    sx1, sy1, sx2, sy2, false);
                        }
                        final double ellipseMaxY = ry2 - ah;
                        if (segmentY1 >= ellipseMaxY && segmentY2 >= ellipseMaxY) {
                            final double ellipseHeight = ah * 2;
                            return Ellipse2afp.intersectsEllipseSegment(
                                    rx1, ry1 - ellipseHeight, aw * 2, ellipseHeight,
                                    sx1, sy1, sx2, sy2, false);
                        }
                    } else {
                        final double ellipseMaxX = rx2 - aw;
                        if (segmentX1 >= ellipseMaxX && segmentX2 >= ellipseMaxX) {
                            final double ellipseMinY = ry1 + ah;
                            final double ellipseWidth = aw * 2;
                            if (segmentY1 <= ellipseMinY && segmentY2 <= ellipseMinY) {
                                return Ellipse2afp.intersectsEllipseSegment(
                                        rx2 - ellipseWidth, ry1, ellipseWidth, ah * 2,
                                        sx1, sy1, sx2, sy2, false);
                            }
                            final double ellipseMaxY = ry2 - ah;
                            if (segmentY1 >= ellipseMaxY && segmentY2 >= ellipseMaxY) {
                                final double ellipseHeight = ah * 2;
                                return Ellipse2afp.intersectsEllipseSegment(
                                        rx2 - ellipseWidth, ry2 - ellipseHeight, ellipseWidth, ellipseHeight,
                                        sx1, sy1, sx2, sy2, false);
                            }
                        }
                    }
                    return true;
                }
                return false;
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

    /** Replies if two round rectangles are intersecting.
     *
     * @param r1x1 is the first corner of the first rectangle.
     * @param r1y1 is the first corner of the first rectangle.
     * @param r1x2 is the second corner of the first rectangle.
     * @param r1y2 is the second corner of the first rectangle.
     * @param r1aw is the width of the arcs of the first rectangle.
     * @param r1ah is the height of the arcs of the first rectangle.
     * @param r2x1 is the first corner of the second rectangle.
     * @param r2y1 is the first corner of the second rectangle.
     * @param r2x2 is the second corner of the second rectangle.
     * @param r2y2 is the second corner of the second rectangle.
     * @param r2aw is the width of the arcs of the second rectangle.
     * @param r2ah is the height of the arcs of the second rectangle.
     * @return <code>true</code> if the two shapes are intersecting; otherwise
     * <code>false</code>
     */
    @Pure
    @Unefficient
    @SuppressWarnings({"checkstyle:parameternumber", "checkstyle:magicnumber"})
    static boolean intersectsRoundRectangleRoundRectangle(double r1x1, double r1y1, double r1x2,
            double r1y2, double r1aw, double r1ah,
            double r2x1, double r2y1, double r2x2, double r2y2, double r2aw, double r2ah) {
        assert r1x1 <= r1x2 : AssertMessages.lowerEqualParameters(0, r1x1, 2, r1x2);
        assert r1y1 <= r1y2 : AssertMessages.lowerEqualParameters(1, r1y1, 3, r1y2);
        assert r1aw >= 0. : AssertMessages.positiveOrZeroParameter(4);
        assert r1ah >= 0. : AssertMessages.positiveOrZeroParameter(5);
        assert r2x1 <= r2x2 : AssertMessages.lowerEqualParameters(6, r2x1, 8, r2x2);
        assert r2y1 <= r2y2 : AssertMessages.lowerEqualParameters(7, r2y1, 9, r2y2);
        assert r2aw >= 0. : AssertMessages.positiveOrZeroParameter(10);
        assert r2ah >= 0. : AssertMessages.positiveOrZeroParameter(11);
        if (Rectangle2afp.intersectsRectangleRectangle(r1x1, r1y1, r1x2, r1y2, r2x1, r2y1, r2x2, r2y2)) {
            // Boundings rectangles are intersecting

            // Test internal rectangles
            final double r1InnerMinX = r1x1 + r1aw;
            final double r1InnerMaxX = r1x2 - r1aw;
            final double r1InnerMinY = r1y1 + r1ah;
            final double r1InnerMaxY = r1y2 - r1ah;
            final double r2InnerMinX = r2x1 + r2aw;
            final double r2InnerMaxX = r2x2 - r2aw;
            final double r2InnerMinY = r2y1 + r2ah;
            final double r2InnerMaxY = r2y2 - r2ah;
            if (Rectangle2afp.intersectsRectangleRectangle(
                    r1InnerMinX, r1y1, r1InnerMaxX, r1y2,
                    r2InnerMinX, r2y1, r2InnerMaxX, r2y2)
                    || Rectangle2afp.intersectsRectangleRectangle(
                            r1InnerMinX, r1y1, r1InnerMaxX, r1y2,
                            r2x1, r2InnerMinY, r2x2, r2InnerMaxY)
                    || Rectangle2afp.intersectsRectangleRectangle(
                            r1x1, r1InnerMinY, r1x2, r1InnerMaxY,
                            r2InnerMinX, r2y1, r2InnerMaxX, r2y2)
                    || Rectangle2afp.intersectsRectangleRectangle(
                            r1x1, r1InnerMinY, r1x2, r1InnerMaxY,
                            r2x1, r2InnerMinY, r2x2, r2InnerMaxY)) {
                return true;
            }

            // Test closest corner ellipses
            final double ex1;
            final double ey1;
            final double ex2;
            final double ey2;

            if (r1InnerMaxX <= r2x1) {
                ex1 = r1InnerMaxX - r1aw;
                ex2 = r2x1;
            } else {
                ex1 = r1x1;
                ex2 = r2InnerMaxX - r2aw;
            }

            if (r1InnerMaxY < r2y1) {
                ey1 = r1InnerMaxY - r1ah;
                ey2 = r2y1;
            } else {
                ey1 = r1y1;
                ey2 = r2InnerMaxY - r2ah;
            }

            return Ellipse2afp.intersectsEllipseEllipse(
                    ex1, ey1, r1aw * 2, r1ah * 2,
                    ex2, ey2, r2aw * 2, r2ah * 2);
        }
        return false;
    }

    /** Replies if a round rectangle and a circle are intersecting.
     *
     * @param rx1 is the first corner of the rectangle.
     * @param ry1 is the first corner of the rectangle.
     * @param rx2 is the second corner of the rectangle.
     * @param ry2 is the second corner of the rectangle.
     * @param aw is the width of the arcs of the rectangle.
     * @param ah is the height of the arcs of the rectangle.
     * @param cx is the center of the circle.
     * @param cy is the center of the circle.
     * @param radius is the radius of the circle.
     * @return <code>true</code> if the two shapes are intersecting; otherwise
     * <code>false</code>
     */
    @Pure
    @Unefficient
    @SuppressWarnings({"checkstyle:parameternumber", "checkstyle:magicnumber"})
    static boolean intersectsRoundRectangleCircle(double rx1, double ry1, double rx2,
            double ry2, double aw, double ah,
            double cx, double cy, double radius) {
        assert rx1 <= rx2 : AssertMessages.lowerEqualParameters(0, rx1, 2, rx2);
        assert ry1 <= ry2 : AssertMessages.lowerEqualParameters(1, ry1, 3, ry2);
        assert aw >= 0. : AssertMessages.positiveOrZeroParameter(4);
        assert ah >= 0. : AssertMessages.positiveOrZeroParameter(5);
        assert radius >= 0. : AssertMessages.positiveOrZeroParameter(8);

        final double rInnerMinX = rx1 + aw;
        final double rInnerMaxX = rx2 - aw;
        final double rInnerMinY = ry1 + ah;
        final double rInnerMaxY = ry2 - ah;

        if (cx < rInnerMinX) {
            if (cy < rInnerMinY) {
                return Ellipse2afp.intersectsEllipseCircle(rx1, ry1, aw * 2, ah * 2, cx, cy, radius);
            }
            if (cy > rInnerMaxY) {
                return Ellipse2afp.intersectsEllipseCircle(rx1, rInnerMaxY - ah, aw * 2, ah * 2, cx, cy, radius);
            }
            return cx > (rx1 - radius) && cx < (rx2 + radius);
        }
        if (cx > rInnerMaxX) {
            if (cy < ry1) {
                return Ellipse2afp.intersectsEllipseCircle(rInnerMaxX - aw, ry1, aw * 2, ah * 2, cx, cy, radius);
            }
            if (cy > rInnerMaxY) {
                return Ellipse2afp.intersectsEllipseCircle(rInnerMaxX - aw, rInnerMaxY - ah, aw * 2, ah * 2, cx, cy, radius);
            }
            return cx > (rx1 - radius) && cx < (rx2 + radius);
        }
        return cy > (ry1 - radius) && cy < (ry2 + radius);
    }

    /** Replies if a round rectangle and a rectangle are intersecting.
     *
     * @param r1x1 is the first corner of the first rectangle.
     * @param r1y1 is the first corner of the first rectangle.
     * @param r1x2 is the second corner of the first rectangle.
     * @param r1y2 is the second corner of the first rectangle.
     * @param r1aw is the width of the arcs of the first rectangle.
     * @param r1ah is the height of the arcs of the first rectangle.
     * @param r2x1 is the first corner of the second rectangle.
     * @param r2y1 is the first corner of the second rectangle.
     * @param r2x2 is the second corner of the second rectangle.
     * @param r2y2 is the second corner of the second rectangle.
     * @return <code>true</code> if the two shapes are intersecting; otherwise
     * <code>false</code>
     */
    @Pure
    @Unefficient
    @SuppressWarnings({"checkstyle:parameternumber", "checkstyle:magicnumber"})
    static boolean intersectsRoundRectangleRectangle(double r1x1, double r1y1, double r1x2,
            double r1y2, double r1aw, double r1ah,
            double r2x1, double r2y1, double r2x2, double r2y2) {
        assert r1x1 <= r1x2 : AssertMessages.lowerEqualParameters(0, r1x1, 2, r1x2);
        assert r1y1 <= r1y2 : AssertMessages.lowerEqualParameters(1, r1y1, 3, r1y2);
        assert r1aw >= 0. : AssertMessages.positiveOrZeroParameter(4);
        assert r1ah >= 0. : AssertMessages.positiveOrZeroParameter(5);
        assert r2x1 <= r2x2 : AssertMessages.lowerEqualParameters(6, r2x1, 8, r2x2);
        assert r2y1 <= r2y2 : AssertMessages.lowerEqualParameters(7, r2y1, 9, r2y2);
        if (Rectangle2afp.intersectsRectangleRectangle(r1x1, r1y1, r1x2, r1y2, r2x1, r2y1, r2x2, r2y2)) {
            // Boundings rectangles are intersecting

            // Test internal rectangles
            final double r1InnerMinX = r1x1 + r1aw;
            final double r1InnerMaxX = r1x2 - r1aw;
            final double r1InnerMinY = r1y1 + r1ah;
            final double r1InnerMaxY = r1y2 - r1ah;
            if (Rectangle2afp.intersectsRectangleRectangle(
                    r1InnerMinX, r1y1, r1InnerMaxX, r1y2,
                    r2x1, r2y1, r2x2, r2y2)
                    || Rectangle2afp.intersectsRectangleRectangle(
                            r1x1, r1InnerMinY, r1x2, r1InnerMaxY,
                            r2x1, r2y1, r2x2, r2y2)) {
                return true;
            }

            // Test closest corner ellipses
            final double ex1;
            final double ey1;

            if (r1InnerMaxX <= r2x1) {
                ex1 = r1InnerMaxX - r1aw;
            } else {
                ex1 = r1x1;
            }

            if (r1InnerMaxY < r2y1) {
                ey1 = r1InnerMaxY - r1ah;
            } else {
                ey1 = r1y1;
            }

            return Ellipse2afp.intersectsEllipseRectangle(
                    ex1, ey1, r1aw * 2, r1ah * 2,
                    r2x1, r2y1, r2x2, r2y2);
        }
        return false;
    }

    /** Replies if a round rectangle and an ellipse are intersecting.
     *
     * @param rx1 is the first corner of the rectangle.
     * @param ry1 is the first corner of the rectangle.
     * @param rx2 is the second corner of the rectangle.
     * @param ry2 is the second corner of the rectangle.
     * @param aw is the width of the arcs of the rectangle.
     * @param ah is the height of the arcs of the rectangle.
     * @param ex is the coordinate of the ellipse corner.
     * @param ey is the coordinate of the ellipse corner.
     * @param ew is the width of the ellipse.
     * @param eh is the height of the ellipse.
     * @return <code>true</code> if the two shapes are intersecting; otherwise
     * <code>false</code>
     */
    @Pure
    @Unefficient
    @SuppressWarnings({"checkstyle:parameternumber", "checkstyle:magicnumber"})
    static boolean intersectsRoundRectangleEllipse(double rx1, double ry1, double rx2,
            double ry2, double aw, double ah,
            double ex, double ey, double ew, double eh) {
        assert rx1 <= rx2 : AssertMessages.lowerEqualParameters(0, rx1, 2, rx2);
        assert ry1 <= ry2 : AssertMessages.lowerEqualParameters(1, ry1, 3, ry2);
        assert aw >= 0. : AssertMessages.positiveOrZeroParameter(2);
        assert ah >= 0. : AssertMessages.positiveOrZeroParameter(5);
        assert ew >= 0. : AssertMessages.positiveOrZeroParameter(8);
        assert eh >= 0. : AssertMessages.positiveOrZeroParameter(9);

        final double rInnerMinX = rx1 + aw;
        final double rInnerMaxX = rx2 - aw;
        final double rInnerMinY = ry1 + ah;
        final double rInnerMaxY = ry2 - ah;

        final double radius1 = ew / 2;
        final double radius2 = eh / 2;
        final double centerX = ex + radius1;
        final double centerY = ey + radius2;

        if (centerX < rInnerMinX) {
            if (centerY < rInnerMinY) {
                return Ellipse2afp.intersectsEllipseEllipse(rx1, ry1, aw * 2, ah * 2, ex, ey, ew, eh);
            }
            if (centerY > rInnerMaxY) {
                return Ellipse2afp.intersectsEllipseEllipse(rx1, rInnerMaxY - ah, aw * 2, ah * 2, ex, ey, ew, eh);
            }
            return centerX > (rx1 - radius1) && centerX < (rx2 + radius1);
        }
        if (centerX > rInnerMaxX) {
            if (centerY < ry1) {
                return Ellipse2afp.intersectsEllipseEllipse(rInnerMaxX - aw, ry1, aw * 2, ah * 2, ex, ey, ew, eh);
            }
            if (centerY > rInnerMaxY) {
                return Ellipse2afp.intersectsEllipseEllipse(rInnerMaxX - aw, rInnerMaxY - ah, aw * 2, ah * 2, ex, ey, ew, eh);
            }
            return centerX > (rx1 - radius1) && centerX < (rx2 + radius1);
        }
        return centerY > (ry1 - radius2) && centerY < (ry2 + radius2);
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
                && getMaxY() == shape.getMaxY()
                && getArcWidth() == shape.getArcWidth()
                && getArcHeight() == shape.getArcHeight();
    }

    /**
     * Gets the width of the arc that rounds off the corners.
     * @return the width of the arc that rounds off the corners
     *     of this <code>RoundRectangle2afp</code>.
     */
    @Pure
    double getArcWidth();

    /**
     * Gets the height of the arc that rounds off the corners.
     * @return the height of the arc that rounds off the corners
     *     of this <code>RoundRectangle2afp</code>.
     */
    @Pure
    double getArcHeight();

    /**
     * Set the width of the arc that rounds off the corners.
     * @param arcWidth is the width of the arc that rounds off the corners
     *     of this <code>RoundRectangle2afp</code>.
     */
    void setArcWidth(double arcWidth);

    /**
     * Set the height of the arc that rounds off the corners.
     * @param arcHeight is the height of the arc that rounds off the corners
     *     of this <code>RoundRectangle2afp</code>.
     */
    void setArcHeight(double arcHeight);

    /** Change the frame of the rectangle.
     *
     * @param x new x coordinate of the minimum corner.
     * @param y new y coordinate of the minimum corner.
     * @param width new width of the rectangle.
     * @param height new height of the rectangle.
     * @param arcWidth is the width of the arc that rounds off the corners
     *     of this <code>RoundRectangle2afp</code>.
     * @param arcHeight is the height of the arc that rounds off the corners
     *     of this <code>RoundRectangle2afp</code>.
     */
    @SuppressWarnings("checkstyle:magicnumber")
    default void set(double x, double y, double width, double height, double arcWidth, double arcHeight) {
        assert width >= 0. : AssertMessages.positiveOrZeroParameter(2);
        assert height >= 0. : AssertMessages.positiveOrZeroParameter(3);
        assert arcWidth >= 0. : AssertMessages.positiveOrZeroParameter(4);
        assert arcHeight >= 0. : AssertMessages.positiveOrZeroParameter(5);
        setFromCorners(x, y, x + width, y + height, arcWidth, arcHeight);
    }

    @Override
    default void set(IT shape) {
        assert shape != null : AssertMessages.notNullParameter();
        setFromCorners(shape.getMinX(), shape.getMinY(), shape.getMaxX(), shape.getMaxY(),
                shape.getArcWidth(), shape.getArcHeight());
    }

    /** Change the frame of the rectangle.
     *
     * @param x1 is the coordinate of the first corner.
     * @param y1 is the coordinate of the first corner.
     * @param x2 is the coordinate of the second corner.
     * @param y2 is the coordinate of the second corner.
     * @param arcWidth is the width of the arc that rounds off the corners
     *     of this <code>RoundRectangle2afp</code>.
     * @param arcHeight is the height of the arc that rounds off the corners
     *     of this <code>RoundRectangle2afp</code>.
     */
    void setFromCorners(double x1, double y1, double x2, double y2, double arcWidth, double arcHeight);

    @Override
    default boolean contains(double x, double y) {
        return containsRoundRectanglePoint(
                getMinX(), getMinY(), getWidth(), getHeight(), getArcWidth(), getArcHeight(),
                x, y);
    }

    @Override
    default boolean contains(Rectangle2afp<?, ?, ?, ?, ?, ?> rectangle) {
        assert rectangle != null : AssertMessages.notNullParameter();
        return containsRoundRectangleRectangle(
                getMinX(), getMinY(), getWidth(), getHeight(), getArcWidth(), getArcHeight(),
                rectangle.getMinX(), rectangle.getMinY(), rectangle.getWidth(), rectangle.getHeight());
    }

    @Pure
    @Override
    default double getDistanceSquared(Point2D<?, ?> pt) {
        assert pt != null : AssertMessages.notNullParameter();
        final Point2D<?, ?> n = getClosestPointTo(pt);
        return n.getDistanceSquared(pt);
    }

    @Pure
    @Override
    default double getDistanceL1(Point2D<?, ?> pt) {
        assert pt != null : AssertMessages.notNullParameter();
        final Point2D<?, ?> n = getClosestPointTo(pt);
        return n.getDistanceL1(pt);
    }

    @Pure
    @Override
    default double getDistanceLinf(Point2D<?, ?> pt) {
        assert pt != null : AssertMessages.notNullParameter();
        final Point2D<?, ?> n = getClosestPointTo(pt);
        return n.getDistanceLinf(pt);
    }

    @Override
    default boolean intersects(Circle2afp<?, ?, ?, ?, ?, ?> circle) {
        assert circle != null : AssertMessages.notNullParameter();
        return intersectsRoundRectangleCircle(
                getMinX(), getMinY(),
                getMaxX(), getMaxY(),
                getArcWidth(), getArcHeight(),
                circle.getX(), circle.getY(),
                circle.getRadius());
    }

    @Override
    default boolean intersects(Ellipse2afp<?, ?, ?, ?, ?, ?> ellipse) {
        assert ellipse != null : AssertMessages.notNullParameter();
        return intersectsRoundRectangleEllipse(
                getMinX(), getMinY(),
                getMaxX(), getMaxY(),
                getArcWidth(), getArcHeight(),
                ellipse.getMinX(), ellipse.getMinY(),
                ellipse.getWidth(), ellipse.getHeight());
    }

    @Override
    default boolean intersects(OrientedRectangle2afp<?, ?, ?, ?, ?, ?> orientedRectangle) {
        assert orientedRectangle != null : AssertMessages.notNullParameter();
        return OrientedRectangle2afp.intersectsOrientedRectangleRoundRectangle(
                orientedRectangle.getCenterX(), orientedRectangle.getCenterY(),
                orientedRectangle.getFirstAxisX(), orientedRectangle.getFirstAxisY(), orientedRectangle.getFirstAxisExtent(),
                orientedRectangle.getSecondAxisExtent(),
                getMinX(), getMinY(), getWidth(), getHeight(), getArcWidth(), getArcHeight());
    }

    @Override
    default boolean intersects(Parallelogram2afp<?, ?, ?, ?, ?, ?> oarallelogram) {
        assert oarallelogram != null : AssertMessages.notNullParameter();
        return Parallelogram2afp.intersectsParallelogramRoundRectangle(
                oarallelogram.getCenterX(), oarallelogram.getCenterY(),
                oarallelogram.getFirstAxisX(), oarallelogram.getFirstAxisY(), oarallelogram.getFirstAxisExtent(),
                oarallelogram.getSecondAxisX(), oarallelogram.getSecondAxisY(), oarallelogram.getSecondAxisExtent(),
                getMinX(), getMinY(), getWidth(), getHeight(), getArcWidth(), getArcHeight());
    }

    @Override
    default boolean intersects(Rectangle2afp<?, ?, ?, ?, ?, ?> rectangle) {
        assert rectangle != null : AssertMessages.notNullParameter();
        return intersectsRoundRectangleRectangle(
                getMinX(), getMinY(),
                getMaxX(), getMaxY(),
                getArcWidth(), getArcHeight(),
                rectangle.getMinX(), rectangle.getMinY(),
                rectangle.getMaxX(), rectangle.getMaxY());
    }

    @Override
    default boolean intersects(RoundRectangle2afp<?, ?, ?, ?, ?, ?> roundRectangle) {
        assert roundRectangle != null : AssertMessages.notNullParameter();
        return intersectsRoundRectangleRoundRectangle(
                getMinX(), getMinY(),
                getMaxX(), getMaxY(),
                getArcWidth(), getArcHeight(),
                roundRectangle.getMinX(), roundRectangle.getMinY(),
                roundRectangle.getMaxX(), roundRectangle.getMaxY(),
                roundRectangle.getArcWidth(), roundRectangle.getArcHeight());
    }

    @Override
    default boolean intersects(Segment2afp<?, ?, ?, ?, ?, ?> segment) {
        assert segment != null : AssertMessages.notNullParameter();
        return RoundRectangle2afp.intersectsRoundRectangleSegment(
                getMinX(), getMinY(),
                getMaxX(), getMaxY(),
                getArcWidth(), getArcHeight(),
                segment.getX1(), segment.getY1(),
                segment.getX2(), segment.getY2());
    }

    @Override
    default boolean intersects(Triangle2afp<?, ?, ?, ?, ?, ?> triangle) {
        assert triangle != null : AssertMessages.notNullParameter();
        return intersects(triangle.getPathIterator());
    }

    @Override
    default boolean intersects(PathIterator2afp<?> iterator) {
        assert iterator != null : AssertMessages.notNullParameter();
        final int mask = iterator.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
        final int crossings = Path2afp.calculatesCrossingsPathIteratorRoundRectangleShadow(
                0,
                iterator,
                getMinX(), getMinY(), getMaxX(), getMaxY(), getArcWidth(), getArcHeight(),
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

    @Pure
    @Override
    default PathIterator2afp<IE> getPathIterator(Transform2D transform) {
        if (transform == null || transform.isIdentity()) {
            return new RoundRectanglePathIterator<>(this);
        }
        return new TransformedRoundRectanglePathIterator<>(this, transform);
    }

    /** Replies a path iterator on this round rectangle that is replacing the
     * corner arcs by line approximations.
     *
     * @return the iterator on the approximation.
     * @see #getPathIterator()
     * @see MathConstants#SPLINE_APPROXIMATION_RATIO
     */
    @Pure
    @SuppressWarnings("checkstyle:magicnumber")
    default PathIterator2afp<IE> getFlatteningPathIterator() {
        // TODO: Remove this part of the code when SPLINE_APPROXIMATION_RATIO is decreased.
        return new Path2afp.FlatteningPathIterator<>(getPathIterator(null),
                MathConstants.SPLINE_APPROXIMATION_RATIO / 10.,
                Path2afp.DEFAULT_FLATTENING_LIMIT);
    }

    @Pure
    @Override
    @SuppressWarnings("checkstyle:npathcomplexity")
    default P getClosestPointTo(Point2D<?, ?> pt) {
        assert pt != null : AssertMessages.notNullParameter();
        final double px = pt.getX();
        final double py = pt.getY();
        final double rx1 = getMinX();
        final double ry1 = getMinY();
        final double rx2 = getMaxX();
        final double ry2 = getMaxY();

        final double aw = getArcWidth();
        final double ah = getArcHeight();

        final GeomFactory2afp<?, P, V, ?> factory = getGeomFactory();

        if (px < rx1 + aw) {
            if (py < ry1 + ah) {
                final P point = factory.newPoint();
                Ellipse2afp.findsClosestPointSolidEllipsePoint(
                        px, py,
                        rx1, ry1,
                        aw * 2, ah * 2,
                        point);
                return point;
            }
            if (py > ry2 - ah) {
                final double eh = ah * 2;
                final P point = factory.newPoint();
                Ellipse2afp.findsClosestPointSolidEllipsePoint(
                        px, py,
                        rx1, ry2 - eh,
                        aw * 2, eh,
                        point);
                return point;
            }
        } else if (px > rx2 - aw) {
            if (py < ry1 + ah) {
                final double ew = aw * 2;
                final P point = factory.newPoint();
                Ellipse2afp.findsClosestPointSolidEllipsePoint(
                        px, py,
                        rx2 - ew, ry1,
                        ew, ah * 2,
                        point);
                return point;
            }
            if (py > ry2 - ah) {
                final double ew = aw * 2;
                final double eh = ah * 2;
                final P point = factory.newPoint();
                Ellipse2afp.findsClosestPointSolidEllipsePoint(
                        px, py,
                        rx2 - ew, ry2 - eh,
                        ew, eh,
                        point);
                return point;
            }
        }

        int same = 0;
        final double x;
        final double y;

        if (px < rx1) {
            x = rx1;
        } else if (px > rx2) {
            x = rx2;
        } else {
            x = px;
            ++same;
        }

        if (py < ry1) {
            y = ry1;
        } else if (py > ry2) {
            y = ry2;
        } else {
            y = py;
            ++same;
        }

        if (same == 2) {
            return factory.convertToPoint(pt);
        }
        return factory.newPoint(x, y);
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
        final P point = getGeomFactory().newPoint();
        Path2afp.findsClosestPointPathIteratorPathIterator(getFlatteningPathIterator(), ellipse.getPathIterator(), point);
        return point;
    }

    @Override
    @Unefficient
    default P getClosestPointTo(Rectangle2afp<?, ?, ?, ?, ?, ?> rectangle) {
        assert rectangle != null : AssertMessages.notNullParameter();
        final P point = getGeomFactory().newPoint();
        Path2afp.findsClosestPointPathIteratorPathIterator(getFlatteningPathIterator(), rectangle.getPathIterator(), point);
        return point;
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
        final P point = getGeomFactory().newPoint();
        Path2afp.findsClosestPointPathIteratorPathIterator(getFlatteningPathIterator(), triangle.getPathIterator(), point);
        return point;
    }

    @Override
    @Unefficient
    default P getClosestPointTo(OrientedRectangle2afp<?, ?, ?, ?, ?, ?> orientedRectangle) {
        assert orientedRectangle != null : AssertMessages.notNullParameter();
        final P point = getGeomFactory().newPoint();
        Path2afp.findsClosestPointPathIteratorPathIterator(getFlatteningPathIterator(),
                orientedRectangle.getPathIterator(), point);
        return point;
    }

    @Override
    @Unefficient
    default P getClosestPointTo(Parallelogram2afp<?, ?, ?, ?, ?, ?> parallelogram) {
        assert parallelogram != null : AssertMessages.notNullParameter();
        final P point = getGeomFactory().newPoint();
        Path2afp.findsClosestPointPathIteratorPathIterator(getFlatteningPathIterator(), parallelogram.getPathIterator(), point);
        return point;
    }

    @Override
    @Unefficient
    default P getClosestPointTo(RoundRectangle2afp<?, ?, ?, ?, ?, ?> roundRectangle) {
        assert roundRectangle != null : AssertMessages.notNullParameter();
        final P point = getGeomFactory().newPoint();
        Path2afp.findsClosestPointPathIteratorPathIterator(getFlatteningPathIterator(), roundRectangle.getPathIterator(), point);
        return point;
    }

    @Override
    @Unefficient
    default P getClosestPointTo(Path2afp<?, ?, ?, ?, ?, ?> path) {
        assert path != null : AssertMessages.notNullParameter();
        final P point = getGeomFactory().newPoint();
        Path2afp.findsClosestPointPathIteratorPathIterator(getFlatteningPathIterator(), path.getPathIterator(), point);
        return point;
    }

    @Pure
    @Override
    default P getFarthestPointTo(Point2D<?, ?> pt) {
        assert pt != null : AssertMessages.notNullParameter();
        final double px = pt.getX();
        final double py = pt.getY();
        final double rx1 = getMinX();
        final double ry1 = getMinY();
        final double rx2 = getMaxX();
        final double ry2 = getMaxY();
        final double centerX = getCenterX();
        final double centerY = getCenterY();

        final double aw = getArcWidth();
        final double ah = getArcHeight();

        final P point = getGeomFactory().newPoint();

        if (px <= centerX) {
            if (py <= centerY) {
                final double ew = aw * 2;
                final double eh = ah * 2;
                Ellipse2afp.findsFarthestPointShallowEllipsePoint(
                        px, py,
                        rx2 - ew, ry2 - eh,
                        ew, eh,
                        point);
            } else {
                final double ew = aw * 2;
                Ellipse2afp.findsFarthestPointShallowEllipsePoint(
                        px, py,
                        rx2 - ew, ry1,
                        ew, ah * 2,
                        point);
            }
        } else if (px <= centerY) {
            final double eh = ah * 2;
            Ellipse2afp.findsFarthestPointShallowEllipsePoint(
                    px, py,
                    rx1, ry2 - eh,
                    aw * 2, eh,
                    point);
        } else {
            Ellipse2afp.findsFarthestPointShallowEllipsePoint(
                    px, py,
                    rx1, ry1,
                    aw * 2, ah * 2,
                    point);
        }

        return point;
    }


    /** Abstract iterator on the path elements of the round rectangle.
     *
     * @param <T> the type of the path elements.
     * @author $Author: sgalland$
     * @version $FullVersion$
     * @mavengroupid $GroupId$
     * @mavenartifactid $ArtifactId$
     */
    abstract class AbstractRoundRectanglePathIterator<T extends PathElement2afp> implements PathIterator2afp<T> {

        /** Number of elements in the path (except move).
         */
        protected static final int ELEMENT_COUNT = 9;

        /** The iterator round rectangle.
         */
        protected final RoundRectangle2afp<?, ?, T, ?, ?, ?> rectangle;

        /** Constructor.
         * @param rectangle the iterated rectangle.
         */
        public AbstractRoundRectanglePathIterator(RoundRectangle2afp<?, ?, T, ?, ?, ?> rectangle) {
            assert rectangle != null : AssertMessages.notNullParameter();
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

    /** Iterator on the path elements of the rectangle.
     *
     * @param <T> the type of the path elements.
     * @author $Author: sgalland$
     * @version $FullVersion$
     * @mavengroupid $GroupId$
     * @mavenartifactid $ArtifactId$
     * @since 13.0
     */
    class RoundRectanglePathIterator<T extends PathElement2afp> extends AbstractRoundRectanglePathIterator<T> {

        private double x;

        private double y;

        private double width;

        private double height;

        private double arcWidth;

        private double arcHeight;

        private int index;

        private double lastX;

        private double lastY;

        private double moveX;

        private double moveY;

        /** Constructor.
         * @param rectangle the round rectangle to iterate on.
         */
        public RoundRectanglePathIterator(RoundRectangle2afp<?, ?, T, ?, ?, ?> rectangle) {
            super(rectangle);
            if (rectangle.isEmpty()) {
                this.index = ELEMENT_COUNT;
            } else {
                this.x = rectangle.getMinX();
                this.y = rectangle.getMinY();
                this.width = rectangle.getWidth();
                this.height = rectangle.getHeight();
                this.arcWidth = rectangle.getArcWidth();
                this.arcHeight = rectangle.getArcHeight();
                this.index = -1;
            }
        }

        @Override
        public PathIterator2afp<T> restartIterations() {
            return new RoundRectanglePathIterator<>(this.rectangle);
        }

        @Pure
        @Override
        public boolean hasNext() {
            return this.index < ELEMENT_COUNT;
        }

        @Override
        @SuppressWarnings({"checkstyle:returncount", "checkstyle:magicnumber"})
        public T next() {
            if (this.index >= ELEMENT_COUNT) {
                throw new NoSuchElementException();
            }
            final int idx = this.index;
            ++this.index;

            if (idx < 0) {
                this.moveX = this.x + this.arcWidth;
                this.moveY = this.y;
                this.lastX = this.moveX;
                this.lastY = this.moveY;
                return getGeomFactory().newMovePathElement(
                        this.lastX, this.lastY);
            }

            final double x = this.lastX;
            final double y = this.lastY;

            final double curveX;
            final double curveY;

            switch (idx) {
            case 0:
                this.lastX = this.x + this.width - this.arcWidth;
                return getGeomFactory().newLinePathElement(
                        x, y,
                        this.lastX, this.lastY);
            case 1:
                this.lastX += this.arcWidth;
                this.lastY += this.arcHeight;
                curveX = x + AbstractCirclePathIterator.CTRL_POINT_DISTANCE * this.arcWidth;
                curveY = this.lastY - AbstractCirclePathIterator.CTRL_POINT_DISTANCE * this.arcHeight;
                return getGeomFactory().newCurvePathElement(
                        x, y,
                        curveX, y,
                        this.lastX, curveY,
                        this.lastX, this.lastY);
            case 2:
                this.lastY = this.y + this.height - this.arcHeight;
                return getGeomFactory().newLinePathElement(
                        x, y,
                        this.lastX, this.lastY);
            case 3:
                this.lastX -= this.arcWidth;
                this.lastY += this.arcHeight;
                curveX = this.lastX + AbstractCirclePathIterator.CTRL_POINT_DISTANCE * this.arcWidth;
                curveY = y + AbstractCirclePathIterator.CTRL_POINT_DISTANCE * this.arcHeight;
                return getGeomFactory().newCurvePathElement(
                        x, y,
                        x, curveY,
                        curveX, this.lastY,
                        this.lastX, this.lastY);
            case 4:
                this.lastX = this.x + this.arcWidth;
                return getGeomFactory().newLinePathElement(
                        x, y,
                        this.lastX, this.lastY);
            case 5:
                this.lastX -= this.arcWidth;
                this.lastY -= this.arcHeight;
                curveX = x - AbstractCirclePathIterator.CTRL_POINT_DISTANCE * this.arcWidth;
                curveY = this.lastY + AbstractCirclePathIterator.CTRL_POINT_DISTANCE * this.arcHeight;
                return getGeomFactory().newCurvePathElement(
                        x, y,
                        curveX, y,
                        this.lastX, curveY,
                        this.lastX, this.lastY);
            case 6:
                this.lastY = this.y + this.arcHeight;
                return getGeomFactory().newLinePathElement(
                        x, y,
                        this.lastX, this.lastY);
            case 7:
                this.lastX += this.arcWidth;
                this.lastY -= this.arcHeight;
                curveX = this.lastX - AbstractCirclePathIterator.CTRL_POINT_DISTANCE * this.arcWidth;
                curveY = y - AbstractCirclePathIterator.CTRL_POINT_DISTANCE * this.arcHeight;
                return getGeomFactory().newCurvePathElement(
                        x, y,
                        x, curveY,
                        curveX, this.lastY,
                        this.lastX, this.lastY);
            default:
                return getGeomFactory().newClosePathElement(
                        x, y,
                        this.moveX, this.moveY);
            }
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
    class TransformedRoundRectanglePathIterator<T extends PathElement2afp> extends AbstractRoundRectanglePathIterator<T> {

        private final Transform2D transform;

        private double x;

        private double y;

        private double width;

        private double height;

        private double arcWidth;

        private double arcHeight;

        private int index;

        private Point2D<?, ?> last;

        private Point2D<?, ?> move;

        private Point2D<?, ?> controlPoint1;

        private Point2D<?, ?> controlPoint2;

        /** Constructor.
         * @param rectangle the round rectangle to iterate on.
         * @param transform the transformation.
         */
        public TransformedRoundRectanglePathIterator(RoundRectangle2afp<?, ?, T, ?, ?, ?> rectangle, Transform2D transform) {
            super(rectangle);
            assert transform != null : AssertMessages.notNullParameter(1);
            this.transform = transform;
            if (rectangle.isEmpty()) {
                this.index = ELEMENT_COUNT;
            } else {
                this.last = new InnerComputationPoint2afp();
                this.move = new InnerComputationPoint2afp();
                this.controlPoint1 = new InnerComputationPoint2afp();
                this.controlPoint2 = new InnerComputationPoint2afp();
                this.x = rectangle.getMinX();
                this.y = rectangle.getMinY();
                this.width = rectangle.getWidth();
                this.height = rectangle.getHeight();
                this.arcWidth = rectangle.getArcWidth();
                this.arcHeight = rectangle.getArcHeight();
                this.index = -1;
            }
        }

        @Override
        public PathIterator2afp<T> restartIterations() {
            return new TransformedRoundRectanglePathIterator<>(this.rectangle, this.transform);
        }

        @Pure
        @Override
        public boolean hasNext() {
            return this.index < ELEMENT_COUNT;
        }

        @Override
        @SuppressWarnings({"checkstyle:returncount", "checkstyle:magicnumber"})
        public T next() {
            if (this.index >= ELEMENT_COUNT) {
                throw new NoSuchElementException();
            }
            final int idx = this.index;
            ++this.index;

            if (idx < 0) {
                this.move.set(this.x + this.arcWidth, this.y);
                this.transform.transform(this.move);
                this.last.set(this.move);
                return getGeomFactory().newMovePathElement(
                        this.last.getX(), this.last.getY());
            }

            final double x = this.last.getX();
            final double y = this.last.getY();

            switch (idx) {
            case 0:
                this.last.set(
                        this.x + this.width - this.arcWidth,
                        this.y);
                this.transform.transform(this.last);
                return getGeomFactory().newLinePathElement(
                        x, y,
                        this.last.getX(), this.last.getY());
            case 1:
                this.last.set(
                        this.x + this.width,
                        this.y + this.arcHeight);
                this.controlPoint1.set(
                        this.x + this.width - this.arcWidth + AbstractCirclePathIterator.CTRL_POINT_DISTANCE * this.arcWidth,
                        this.y);
                this.controlPoint2.set(
                        this.x + this.width,
                        this.y + this.arcHeight - AbstractCirclePathIterator.CTRL_POINT_DISTANCE * this.arcHeight);
                this.transform.transform(this.last);
                this.transform.transform(this.controlPoint1);
                this.transform.transform(this.controlPoint2);
                return getGeomFactory().newCurvePathElement(
                        x, y,
                        this.controlPoint1.getX(), this.controlPoint1.getY(),
                        this.controlPoint2.getX(), this.controlPoint2.getY(),
                        this.last.getX(), this.last.getY());
            case 2:
                this.last.set(
                        this.x + this.width,
                        this.y + this.height - this.arcHeight);
                this.transform.transform(this.last);
                return getGeomFactory().newLinePathElement(
                        x, y,
                        this.last.getX(), this.last.getY());
            case 3:
                this.last.set(
                        this.x + this.width - this.arcWidth,
                        this.y + this.height);
                this.controlPoint1.set(
                        this.x + this.width,
                        this.y + this.height - this.arcHeight + AbstractCirclePathIterator.CTRL_POINT_DISTANCE * this.arcHeight);
                this.controlPoint2.set(
                        this.x + this.width - this.arcWidth + AbstractCirclePathIterator.CTRL_POINT_DISTANCE * this.arcWidth,
                        this.y + this.height);
                this.transform.transform(this.last);
                this.transform.transform(this.controlPoint1);
                this.transform.transform(this.controlPoint2);
                return getGeomFactory().newCurvePathElement(
                        x, y,
                        this.controlPoint1.getX(), this.controlPoint1.getY(),
                        this.controlPoint2.getX(), this.controlPoint2.getY(),
                        this.last.getX(), this.last.getY());
            case 4:
                this.last.set(
                        this.x + this.arcWidth,
                        this.y + this.height);
                this.transform.transform(this.last);
                return getGeomFactory().newLinePathElement(
                        x, y,
                        this.last.getX(), this.last.getY());
            case 5:
                this.last.set(
                        this.x,
                        this.y + this.height - this.arcHeight);
                this.controlPoint1.set(
                        this.x + this.arcWidth - AbstractCirclePathIterator.CTRL_POINT_DISTANCE * this.arcWidth,
                        this.y + this.height);
                this.controlPoint2.set(
                        this.x,
                        this.y + this.height - this.arcHeight + AbstractCirclePathIterator.CTRL_POINT_DISTANCE * this.arcHeight);
                this.transform.transform(this.last);
                this.transform.transform(this.controlPoint1);
                this.transform.transform(this.controlPoint2);
                return getGeomFactory().newCurvePathElement(
                        x, y,
                        this.controlPoint1.getX(), this.controlPoint1.getY(),
                        this.controlPoint2.getX(), this.controlPoint2.getY(),
                        this.last.getX(), this.last.getY());
            case 6:
                this.last.set(
                        this.x,
                        this.y + this.arcHeight);
                this.transform.transform(this.last);
                return getGeomFactory().newLinePathElement(
                        x, y,
                        this.last.getX(), this.last.getY());
            case 7:
                this.controlPoint1.set(
                        this.x,
                        this.y + this.arcHeight - AbstractCirclePathIterator.CTRL_POINT_DISTANCE * this.arcHeight);
                this.controlPoint2.set(
                        this.x + this.arcWidth - AbstractCirclePathIterator.CTRL_POINT_DISTANCE * this.arcWidth,
                        this.y);
                this.transform.transform(this.controlPoint1);
                this.transform.transform(this.controlPoint2);
                return getGeomFactory().newCurvePathElement(
                        x, y,
                        this.controlPoint1.getX(), this.controlPoint1.getY(),
                        this.controlPoint2.getX(), this.controlPoint2.getY(),
                        this.move.getX(), this.move.getY());
            default:
                return getGeomFactory().newClosePathElement(
                        x, y,
                        this.move.getX(), this.move.getY());
            }
        }

    }

}
