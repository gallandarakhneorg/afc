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

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.util.OutputParameter;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.arakhne.afc.vmutil.locale.Locale;

/** Shadow of a path that is used for computing the crossing values
 * between a shape and the shadow.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
class ClosestPointPathShadow2ai {

    private final PathIterator2ai<?> pathIterator;

    private final Point2D<?, ?> otherShapeClosestPoint = new InnerComputationPoint2ai();

    private final Point2D<?, ?> shadowShapeClosestPoint = new InnerComputationPoint2ai();

    private final Point2D<?, ?> temporaryPoint1 = new InnerComputationPoint2ai();

    private final Point2D<?, ?> temporaryPoint2 = new InnerComputationPoint2ai();

    private double minDistance = Double.POSITIVE_INFINITY;

    private final int boundingMinX;

    private final int boundingMinY;

    private final int boundingMaxY;

    private boolean started;

    private int crossings;

    private boolean hasX4ymin;

    private boolean hasX4ymax;

    private int x4ymin;

    private int x4ymax;

    /** Construct new path shadow.
     * @param pathIterator the iterator on the path that is constituting the shadow.
     * @param bounds the bounds of the shadow.
     */
    ClosestPointPathShadow2ai(PathIterator2ai<?> pathIterator, Rectangle2ai<?, ?, ?, ?, ?, ?> bounds) {
        assert pathIterator != null : AssertMessages.notNullParameter(0);
        assert bounds != null : AssertMessages.notNullParameter(1);
        this.pathIterator = pathIterator;
        this.boundingMinX = bounds.getMinX();
        this.boundingMinY = bounds.getMinY();
        this.boundingMaxY = bounds.getMaxY();
    }

    /** Compute the crossings between this shadow and
     * the given segment.
     *
     * @param crossings is the initial value of the crossings.
     * @param x0 is the first point of the segment.
     * @param y0 is the first point of the segment.
     * @param x1 is the second point of the segment.
     * @param y1 is the second point of the segment.
     * @return the crossings or {@link MathConstants#SHAPE_INTERSECTS}.
     */
    @SuppressWarnings("checkstyle:npathcomplexity")
    public int computeCrossings(
            int crossings,
            int x0, int y0,
            int x1, int y1) {
        // The segment is intersecting the bounds of the shadow path.
        // We must consider the shape of shadow path now.
        this.x4ymin = this.boundingMinX;
        this.x4ymax = this.boundingMinX;
        this.crossings = 0;
        this.hasX4ymin = false;
        this.hasX4ymax = false;
        final PathIterator2ai<?> iterator;
        if (this.started) {
            iterator = this.pathIterator.restartIterations();
        } else {
            this.started = true;
            iterator = this.pathIterator;
        }

        discretizePathIterator(
                iterator,
                x0, y0, x1, y1);

        // Test if the shape is intesecting the shadow shape.
        if (this.crossings == MathConstants.SHAPE_INTERSECTS) {
            // The given line is intersecting the path shape
            return MathConstants.SHAPE_INTERSECTS;
        }

        // There is no intersection with the shadow path's shape.
        // Compute the crossings with the minimum/maximum y borders.
        int inc = 0;
        if (this.hasX4ymin) {
            ++inc;
        }
        if (this.hasX4ymax) {
            ++inc;
        }

        final int numCrosses;
        if (y0 < y1) {
            numCrosses = inc;
        } else {
            numCrosses = -inc;
        }

        // Apply the previously computed crossings
        return crossings + numCrosses;
    }

    @SuppressWarnings({"checkstyle:parameternumber", "checkstyle:cyclomaticcomplexity",
        "checkstyle:npathcomplexity", "checkstyle:returncount"})
    private void discretizePathIterator(
            PathIterator2ai<?> pi,
            int x1, int y1, int x2, int y2) {
        if (!pi.hasNext() || this.crossings == MathConstants.SHAPE_INTERSECTS) {
            return;
        }
        PathElement2ai element;

        element = pi.next();
        if (element.getType() != PathElementType.MOVE_TO) {
            throw new IllegalArgumentException(Locale.getString(Path2ai.class, "E1")); //$NON-NLS-1$
        }

        Path2ai<?, ?, ?, ?, ?, ?> localPath;
        int movx = element.getToX();
        int movy = element.getToY();
        int curx = movx;
        int cury = movy;
        int endx;
        int endy;
        double distance;
        while (pi.hasNext()) {
            element = pi.next();
            switch (element.getType()) {
            case MOVE_TO:
                movx = element.getToX();
                curx = movx;
                movy = element.getToY();
                cury = movy;
                break;
            case LINE_TO:
                endx = element.getToX();
                endy = element.getToY();
                distance = Segment2ai.findsClosestPointsSegmentSegment(x1, y1, x2, y2, curx, cury, endx, endy,
                        this.temporaryPoint1, this.temporaryPoint2);
                if (distance <= 0.) {
                    this.otherShapeClosestPoint.set(this.temporaryPoint1);
                    this.shadowShapeClosestPoint.set(this.temporaryPoint2);
                    this.crossings = MathConstants.SHAPE_INTERSECTS;
                    return;
                } else if (distance < this.minDistance) {
                    this.minDistance = distance;
                    this.otherShapeClosestPoint.set(this.temporaryPoint1);
                    this.shadowShapeClosestPoint.set(this.temporaryPoint2);
                }
                crossSegmentTwoShadowLines(
                        curx, cury,
                        endx, endy,
                        x1, y1, x2, y2);
                if (this.crossings == MathConstants.SHAPE_INTERSECTS) {
                    return;
                }
                curx = endx;
                cury = endy;
                break;
            case QUAD_TO:
                endx = element.getToX();
                endy = element.getToY();
                // only for local use.
                localPath = pi.getGeomFactory().newPath(pi.getWindingRule());
                localPath.moveTo(curx, cury);
                localPath.quadTo(
                        element.getCtrlX1(), element.getCtrlY1(),
                        endx, endy);
                discretizePathIterator(
                        localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
                        x1, y1, x2, y2);
                if (this.crossings == MathConstants.SHAPE_INTERSECTS) {
                    return;
                }
                curx = endx;
                cury = endy;
                break;
            case CURVE_TO:
                endx = element.getToX();
                endy = element.getToY();
                // only for local use.
                localPath = pi.getGeomFactory().newPath(pi.getWindingRule());
                localPath.moveTo(curx, cury);
                localPath.curveTo(
                        element.getCtrlX1(), element.getCtrlY1(),
                        element.getCtrlX2(), element.getCtrlY2(),
                        endx, endy);
                discretizePathIterator(
                        localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
                        x1, y1, x2, y2);
                if (this.crossings == MathConstants.SHAPE_INTERSECTS) {
                    return;
                }
                curx = endx;
                cury = endy;
                break;
            case ARC_TO:
                endx = element.getToX();
                endy = element.getToY();
                // only for local use.
                localPath = pi.getGeomFactory().newPath(pi.getWindingRule());
                localPath.moveTo(curx, cury);
                localPath.arcTo(
                        endx, endy,
                        element.getRadiusX(), element.getRadiusY(),
                        element.getRotationX(), element.getLargeArcFlag(),
                        element.getSweepFlag());
                discretizePathIterator(
                        localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
                        x1, y1, x2, y2);
                if (this.crossings == MathConstants.SHAPE_INTERSECTS) {
                    return;
                }
                curx = endx;
                cury = endy;
                break;
            case CLOSE:
                if (cury != movy || curx != movx) {
                    distance = Segment2ai.findsClosestPointsSegmentSegment(x1, y1, x2, y2, curx, cury, movx, movy,
                            this.temporaryPoint1, this.temporaryPoint2);
                    if (distance <= 0.) {
                        this.otherShapeClosestPoint.set(this.temporaryPoint1);
                        this.shadowShapeClosestPoint.set(this.temporaryPoint2);
                        this.crossings = MathConstants.SHAPE_INTERSECTS;
                        return;
                    } else if (distance < this.minDistance) {
                        this.minDistance = distance;
                        this.otherShapeClosestPoint.set(this.temporaryPoint1);
                        this.shadowShapeClosestPoint.set(this.temporaryPoint2);
                    }
                    crossSegmentTwoShadowLines(
                            curx, cury,
                            movx, movy,
                            x1, y1, x2, y2);
                }
                if (this.crossings != 0) {
                    return;
                }
                curx = movx;
                cury = movy;
                break;
            default:
            }
        }

        assert this.crossings != MathConstants.SHAPE_INTERSECTS;
        if (curx != movx || cury != movy) {
            // Assume that when is the path is open, only
            // SHAPE_INTERSECTS may be return
            this.crossings = 0;
        }
    }

    private void setCrossingCoordinateForYMax(int x, int y) {
        if (y >= this.boundingMaxY && x > this.x4ymax) {
            this.x4ymax = x;
            this.hasX4ymax = true;
        }
    }

    private void setCrossingCoordinateForYMin(int x, int y) {
        if (y <= this.boundingMinY && x > this.x4ymin) {
            this.x4ymin = x;
            this.hasX4ymin = true;
        }
    }

    /** Determine where the segment is crossing the two shadow lines.
     *
     * @param shadowX0 x coordinate of the reference point of the first shadow line.
     * @param shadowY0 y coordinate of the reference point of the first shadow line.
     * @param shadowX1 x coordinate of the reference point of the second shadow line.
     * @param shadowY1 y coordinate of the reference point of the second shadow line.
     * @param sx0 x coordinate of the first point of the segment.
     * @param sy0 y coordinate of the first point of the segment.
     * @param sx1 x coordinate of the second point of the segment.
     * @param sy1 y coordinate of the second point of the segment.
     */
    @SuppressWarnings({"checkstyle:parameternumber", "checkstyle:cyclomaticcomplexity",
            "checkstyle:npathcomplexity"})
    private void crossSegmentTwoShadowLines(
            int shadowX0, int shadowY0,
            int shadowX1, int shadowY1,
            int sx0, int sy0,
            int sx1, int sy1) {
        // Update the global bounds of the shadow.
        final int shadowYmin = Math.min(shadowY0, shadowY1);
        final int shadowYmax = Math.max(shadowY0, shadowY1);

        if (shadowYmin != this.boundingMinY && shadowYmax != this.boundingMaxY) {
            // Shadow is not contributing to the crossing computation.
            return;
        }

        if (sy0 < shadowYmin && sy1 < shadowYmin) {
            // The segment is entirely at the bottom of the shadow.
            return;
        }
        if (sy0 > shadowYmax && sy1 > shadowYmax) {
            // The segment is entirely at the top of the shadow.
            return;
        }

        final int shadowXmin = Math.min(shadowX0, shadowX1);
        final int shadowXmax = Math.max(shadowX0, shadowX1);

        if (sx0 < shadowXmin && sx1 < shadowXmin) {
            // The segment is entirely at the left of the shadow.
            return;
        }
        if (sx0 > shadowXmax && sx1 > shadowXmax) {
            // The line is entirely at the right of the shadow
            final OutputParameter<Integer> param = new OutputParameter<>();
            if (shadowYmin == shadowYmax) {
                final int cross = this.crossings;
                this.crossings = Segment2ai.calculatesCrossingsAndXPointShadowSegment(
                        cross,
                        shadowXmax, shadowYmin,
                        sx0, sy0, sx1, sy1,
                        shadowYmin == this.boundingMinY, shadowYmax == this.boundingMaxY, param);
                if (cross != this.crossings) {
                    final int xintercept = param.get().intValue();
                    setCrossingCoordinateForYMax(xintercept, shadowYmin);
                    setCrossingCoordinateForYMin(xintercept, shadowYmin);
                    this.crossings = cross;
                }
            } else {
                if (shadowYmin == this.boundingMinY) {
                    final int cross = Segment2ai.calculatesCrossingsAndXPointShadowSegment(
                            this.crossings,
                            shadowXmax, shadowYmin,
                            sx0, sy0, sx1, sy1,
                            shadowYmin == this.boundingMinY, false, param);
                    if (cross != this.crossings) {
                        final int xintercept = param.get().intValue();
                        setCrossingCoordinateForYMax(xintercept, shadowYmin);
                        setCrossingCoordinateForYMin(xintercept, shadowYmin);
                        this.crossings = cross;
                    }
                }
                if (shadowYmax == this.boundingMaxY) {
                    final int cross = Segment2ai.calculatesCrossingsAndXPointShadowSegment(
                            this.crossings,
                            shadowXmax, shadowYmax,
                            sx0, sy0, sx1, sy1,
                            false, shadowYmax == this.boundingMaxY, param);
                    if (cross != this.crossings) {
                        final int xintercept = param.get().intValue();
                        setCrossingCoordinateForYMax(xintercept, shadowYmax);
                        setCrossingCoordinateForYMin(xintercept, shadowYmax);
                        this.crossings = cross;
                    }
                }
            }
        } else if (Segment2ai.intersectsSegmentSegment(
                shadowX0, shadowY0, shadowX1, shadowY1,
                sx0, sy0, sx1, sy1)) {
            // The segment is intersecting the shadowed segment.
            this.crossings = MathConstants.SHAPE_INTERSECTS;
        } else {
            final int side1;
            final int side2;
            if (shadowY0 <= shadowY1) {
                side1 = Segment2ai.findsSideLinePoint(
                        shadowX0, shadowY0,
                        shadowX1, shadowY1,
                        sx0, sy0);
                side2 = Segment2ai.findsSideLinePoint(
                        shadowX0, shadowY0,
                        shadowX1, shadowY1,
                        sx1, sy1);
            } else {
                side1 = Segment2ai.findsSideLinePoint(
                        shadowX1, shadowY1,
                        shadowX0, shadowY0,
                        sx0, sy0);
                side2 = Segment2ai.findsSideLinePoint(
                        shadowX1, shadowY1,
                        shadowX0, shadowY0,
                        sx1, sy1);
            }
            if (side1 >= 0 || side2 >= 0) {
                final int x0;
                final int x1;
                if (shadowYmin == shadowY0) {
                    x0 = shadowX0;
                    x1 = shadowX1;
                } else {
                    x0 = shadowX1;
                    x1 = shadowX0;
                }
                crossSegmentShadowLine(
                        x0, shadowYmin,
                        sx0, sy0, sx1, sy1, true, false);
                crossSegmentShadowLine(
                        x1, shadowYmax,
                        sx0, sy0, sx1, sy1, false, true);
            }
        }
    }

    /** Determine where the segment is crossing the shadow line.
     *
     * @param shadowx x coordinate of the reference point of the shadow line.
     * @param shadowy y coordinate of the reference point of the shadow line.
     * @param sx0 x coordinate of the first point of the segment.
     * @param sy0 y coordinate of the first point of the segment.
     * @param sx1 x coordinate of the second point of the segment.
     * @param sy1 y coordinate of the second point of the segment.
     */
    private void crossSegmentShadowLine(
            int shadowx, int shadowy,
            int sx0, int sy0,
            int sx1, int sy1,
            boolean top, boolean bottom) {
        if (shadowy <  sy0 && shadowy <  sy1) {
            // Segment is entirely at the top of shadow line
            return;
        }
        if (shadowy > sy0 && shadowy > sy1) {
            // Segment is entirely at the bottom of the shadow line
            return;
        }
        if (shadowx > sx0 && shadowx > sx1) {
            // Segment is entirely at the left of the shadow line
            return;
        }
        // Compute the intersection point between the segment and the shadow line
        final OutputParameter<Integer> param = new OutputParameter<>();
        final int cross = Segment2ai.calculatesCrossingsAndXPointShadowSegment(
                this.crossings,
                shadowx, shadowy, sx0, sy0, sx1, sy1, top, bottom, param);
        if (cross != this.crossings) {
            final int xintercept = param.get().intValue();
            setCrossingCoordinateForYMax(xintercept, shadowy);
            setCrossingCoordinateForYMin(xintercept, shadowy);
            this.crossings = cross;
        }
    }

    /** Replies the closest point on the shape that is compared to the shadow.
     *
     * @return the closest point.
     */
    public Point2D<?, ?> getClosestPointInOtherShape() {
        return this.otherShapeClosestPoint;
    }

    /** Replies the closest point on the shape that is compared to the shadow.
     *
     * @return the closest point.
     */
    public Point2D<?, ?> getClosestPointInShadowShape() {
        return this.shadowShapeClosestPoint;
    }

}
