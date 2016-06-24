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

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
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
class BasicPathShadow2afp {

    private final PathIterator2afp<?> pathIterator;

    private CoordinatesParam coordinateParam;

    private final double boundingMinX;

    private final double boundingMinY;

    private final double boundingMaxX;

    private final double boundingMaxY;

    private boolean started;

    private int crossings;

    private boolean hasX4ymin;

    private boolean hasX4ymax;

    private double x4ymin;

    private double x4ymax;



    /** Construct new path shadow.
     * @param path the path that is constituting the shadow.
     */
    BasicPathShadow2afp(Path2afp<?, ?, ?, ?, ?, ?> path) {
        this(path.getPathIterator(), path.toBoundingBox());
    }

    /** Construct new path shadow.
     * @param pathIterator the iterator on the path that is constituting the shadow.
     * @param bounds the bounds of the shadow.
     */
    BasicPathShadow2afp(PathIterator2afp<?> pathIterator, Rectangle2afp<?, ?, ?, ?, ?, ?> bounds) {
        assert pathIterator != null : AssertMessages.notNullParameter(0);
        assert bounds != null : AssertMessages.notNullParameter(1);
        this.pathIterator = pathIterator;
        this.boundingMinX = bounds.getMinX();
        this.boundingMinY = bounds.getMinY();
        this.boundingMaxX = bounds.getMaxX();
        this.boundingMaxY = bounds.getMaxY();
    }

    /** Construct new path shadow.
     * @param pathIterator the iterator on the path that is constituting the shadow.
     * @param minX x coordinate of the lower corner of the shadow's bouding box.
     * @param minY y coordinate of the lower corner of the shadow's bouding box.
     * @param maxX x coordinate of the upper corner of the shadow's bouding box.
     * @param maxY y coordinate of the upper corner of the shadow's bouding box.
     */
    @SuppressWarnings("checkstyle:magicnumber")
    BasicPathShadow2afp(PathIterator2afp<?> pathIterator, double minX, double minY, double maxX, double maxY) {
        assert pathIterator != null : AssertMessages.notNullParameter(0);
        assert minX <= maxX : AssertMessages.lowerEqualParameters(1, minX, 3, maxX);
        assert minY <= maxY : AssertMessages.lowerEqualParameters(2, minY, 4, maxY);
        this.pathIterator = pathIterator;
        this.boundingMinX = minX;
        this.boundingMinY = minY;
        this.boundingMaxX = maxX;
        this.boundingMaxY = maxY;
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
            double x0, double y0,
            double x1, double y1) {
        int numCrosses =
                Segment2afp.calculatesCrossingsRectangleShadowSegment(crossings,
                        this.boundingMinX,
                        this.boundingMinY,
                        this.boundingMaxX,
                        this.boundingMaxY,
                        x0, y0,
                        x1, y1);

        if (numCrosses == MathConstants.SHAPE_INTERSECTS) {
            // The segment is intersecting the bounds of the shadow path.
            // We must consider the shape of shadow path now.
            this.x4ymin = this.boundingMinX;
            this.x4ymax = this.boundingMinX;
            this.crossings = 0;
            this.hasX4ymin = false;
            this.hasX4ymax = false;
            final PathIterator2afp<?> iterator;
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
            final int mask = iterator.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
            if (this.crossings == MathConstants.SHAPE_INTERSECTS
                    || (this.crossings & mask) != 0) {
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

            if (y0 < y1) {
                numCrosses = inc;
            } else {
                numCrosses = -inc;
            }

            // Apply the previously computed crossings
            numCrosses += crossings;
        }

        return numCrosses;
    }

    @SuppressWarnings({"checkstyle:parameternumber", "checkstyle:cyclomaticcomplexity",
            "checkstyle:npathcomplexity"})
    private void discretizePathIterator(
            PathIterator2afp<?> pi,
            double x1, double y1, double x2, double y2) {
        if (!pi.hasNext() || this.crossings == MathConstants.SHAPE_INTERSECTS) {
            return;
        }
        PathElement2afp element;
        element = pi.next();
        if (element.getType() != PathElementType.MOVE_TO) {
            throw new IllegalArgumentException(Locale.getString(Path2afp.class, "E1")); //$NON-NLS-1$
        }
        double movx = element.getToX();
        double movy = element.getToY();
        double curx = movx;
        double cury = movy;
        this.coordinateParam = new BasicPathShadow2afp.CoordinatesParam(x1, y1, x2, y2, curx, cury);
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
                setLineToFromDiscretizePathIterator(element, coordinateParam);
                if (this.crossings == MathConstants.SHAPE_INTERSECTS) {
                    return;
                }
                curx = element.getToX();
                cury = element.getToY();
                break;
            case QUAD_TO:
                // only for local use.
                setQuadToFromDiscretizePathIterator(element, pi, coordinateParam);
                if (this.crossings == MathConstants.SHAPE_INTERSECTS) {
                    return;
                }
                curx = element.getToX();
                cury = element.getToY();
                break;
            case CURVE_TO:
                // only for local use.
                setCurveToFromDiscretizePathIterator(element, pi, coordinateParam);
                if (this.crossings == MathConstants.SHAPE_INTERSECTS) {
                    return;
                }
                curx = element.getToX();
                cury = element.getToY();
                break;
            case ARC_TO:
                // only for local use.
                setArcToFromDiscretizePathIterator(element, pi, coordinateParam);
                if (this.crossings == MathConstants.SHAPE_INTERSECTS) {
                    return;
                }
                curx = element.getToX();
                cury = element.getToY();
                break;
            case CLOSE:
                setCloseFromDiscretizePathIterator(coordinateParam, movx, movy);
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

        final boolean isOpen = (curx != movx) || (cury != movy);

        if (isOpen) {
            // Assume that when is the path is open, only
            // SHAPE_INTERSECTS may be return
            this.crossings = 0;
        }
    }

    private void setLineToFromDiscretizePathIterator(PathElement2afp elm, CoordinatesParam param) {
        final double endx = elm.getToX();
        final double endy = elm.getToY();
        crossSegmentTwoShadowLines(
                param.getCurx(), param.getCury(),
                endx, endy,
                param.getX1(), param.getY1(), param.getX2(), param.getY2());
    }

    private void setQuadToFromDiscretizePathIterator(PathElement2afp elm, PathIterator2afp<?> pi, CoordinatesParam param) {
        final Path2afp<?, ?, ?, ?, ?, ?> localPath;
        final double endx = elm.getToX();
        final double endy = elm.getToY();
        localPath = pi.getGeomFactory().newPath(pi.getWindingRule());
        localPath.moveTo(param.getCurx(), param.getCury());
        localPath.quadTo(
                elm.getCtrlX1(), elm.getCtrlY1(),
                endx, endy);
        discretizePathIterator(
                localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
                param.getX1(), param.getY1(), param.getX2(), param.getY2());
    }

    private void setCurveToFromDiscretizePathIterator(PathElement2afp elm, PathIterator2afp<?> pi, CoordinatesParam prm) {
        final Path2afp<?, ?, ?, ?, ?, ?> localPath;
        final double endx = elm.getToX();
        final double endy = elm.getToY();
        localPath = pi.getGeomFactory().newPath(pi.getWindingRule());
        localPath.moveTo(prm.getCurx(), prm.getCury());
        localPath.curveTo(
                elm.getCtrlX1(), elm.getCtrlY1(),  elm.getCtrlX2(), elm.getCtrlY2(),
                endx, endy);
        discretizePathIterator(
                localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
                prm.getX1(), prm.getY1(), prm.getX2(), prm.getY2());
    }

    private void setArcToFromDiscretizePathIterator(PathElement2afp elm, PathIterator2afp<?> pi, CoordinatesParam param) {
        final Path2afp<?, ?, ?, ?, ?, ?> localPath;
        final double endx = elm.getToX();
        final double endy = elm.getToY();
        localPath = pi.getGeomFactory().newPath(pi.getWindingRule());
        localPath.moveTo(param.getCurx(), param.getCury());
        localPath.arcTo(
                endx, endy,
                elm.getRadiusX(), elm.getRadiusY(),
                elm.getRotationX(), elm.getLargeArcFlag(),
                elm.getSweepFlag());
        discretizePathIterator(
                localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
                param.getX1(), param.getY1(), param.getX2(), param.getY2());
    }

    private void setCloseFromDiscretizePathIterator(CoordinatesParam param, double movx, double movy) {
        if (param.getCury() != movy || param.getCurx() != movx) {
            crossSegmentTwoShadowLines(
                    param.getCurx(), param.getCury(),
                    movx, movy,
                    param.getX1(), param.getY1(), param.getX2(), param.getY2());
        }
    }

    private void setCrossingCoordinateForYMax(double x, double y) {
        if (MathUtil.compareEpsilon(y, this.boundingMaxY) >= 0 && x > this.x4ymax) {
            this.x4ymax = x;
            this.hasX4ymax = true;
        }
    }

    private void setCrossingCoordinateForYMin(double x, double y) {
        if (MathUtil.compareEpsilon(y, this.boundingMinY) <= 0 && x > this.x4ymin) {
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
            double shadowX0, double shadowY0,
            double shadowX1, double shadowY1,
            double sx0, double sy0,
            double sx1, double sy1) {
        // Update the global bounds of the shadow.
        final double shadowXmin = Math.min(shadowX0, shadowX1);
        final double shadowXmax = Math.max(shadowX0, shadowX1);
        final double shadowYmin = Math.min(shadowY0, shadowY1);
        final double shadowYmax = Math.max(shadowY0, shadowY1);

        if (sy0 < shadowYmin && sy1 < shadowYmin) {
            // The segment is entirely at the bottom of the shadow.
            return;
        }
        if (sy0 > shadowYmax && sy1 > shadowYmax) {
            // The segment is entirely at the top of the shadow.
            return;
        }
        if (sx0 < shadowXmin && sx1 < shadowXmin) {
            // The segment is entirely at the left of the shadow.
            return;
        }
        if (sx0 >= shadowXmax && sx1 >= shadowXmax) {
            // The line is entirely at the right of the shadow
            final double alpha = (sx1 - sx0) / (sy1 - sy0);
            if (sy0 < sy1) {
                if (sy0 <= shadowYmin) {
                    final double xintercept = sx0 + (shadowYmin - sy0) * alpha;
                    setCrossingCoordinateForYMin(xintercept, shadowYmin);
                    ++this.crossings;
                }
                if (sy1 >= shadowYmax) {
                    final double xintercept = sx0 + (shadowYmax - sy0) * alpha;
                    setCrossingCoordinateForYMax(xintercept, shadowYmax);
                    ++this.crossings;
                }
            } else {
                if (sy1 <= shadowYmin) {
                    final double xintercept = sx0 + (shadowYmin - sy0) * alpha;
                    setCrossingCoordinateForYMin(xintercept, shadowYmin);
                    --this.crossings;
                }
                if (sy0 >= shadowYmax) {
                    final double xintercept = sx0 + (shadowYmax - sy0) * alpha;
                    setCrossingCoordinateForYMax(xintercept, shadowYmax);
                    --this.crossings;
                }
            }
        } else if (Segment2afp.intersectsSegmentSegmentWithoutEnds(
                shadowX0, shadowY0, shadowX1, shadowY1,
                sx0, sy0, sx1, sy1)) {
            // The segment is intersecting the shadowed segment.
            this.crossings = MathConstants.SHAPE_INTERSECTS;
        } else {
            final int side1;
            final int side2;
            final boolean isUp = shadowY0 <= shadowY1;
            if (isUp) {
                side1 = Segment2afp.findsSideLinePoint(
                        shadowX0, shadowY0,
                        shadowX1, shadowY1,
                        sx0, sy0, 0.);
                side2 = Segment2afp.findsSideLinePoint(
                        shadowX0, shadowY0,
                        shadowX1, shadowY1,
                        sx1, sy1, 0.);
            } else {
                side1 = Segment2afp.findsSideLinePoint(
                        shadowX1, shadowY1,
                        shadowX0, shadowY0,
                        sx0, sy0, 0.);
                side2 = Segment2afp.findsSideLinePoint(
                        shadowX1, shadowY1,
                        shadowX0, shadowY0,
                        sx1, sy1, 0.);
            }
            if (side1 > 0 || side2 > 0) {
                final double x0;
                final double x1;
                if (shadowY0 <= shadowY1) {
                    x0 = shadowX0;
                    x1 = shadowX1;
                } else {
                    x0 = shadowX1;
                    x1 = shadowX0;
                }

                crossSegmentShadowLine(
                        x1, shadowYmax,
                        sx0, sy0, sx1, sy1,
                        isUp);
                crossSegmentShadowLine(
                        x0, shadowYmin,
                        sx0, sy0, sx1, sy1,
                        !isUp);
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
     * @param isMax <code>true</code> if the shadow line is for ymax; <code>false</code> if it is for
     *     ymin.
     */
    private void crossSegmentShadowLine(
            double shadowx, double shadowy,
            double sx0, double sy0,
            double sx1, double sy1,
            boolean isMax) {
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
        final double xintercept = sx0 + (shadowy - sy0) * (sx1 - sx0) / (sy1 - sy0);
        if (shadowx > xintercept) {
            // The intersection point is on the left of the shadow line.
            return;
        }

        if (isMax) {
            setCrossingCoordinateForYMax(xintercept, shadowy);
        } else {
            setCrossingCoordinateForYMin(xintercept, shadowy);
        }

        if (sy0 < sy1) {
            ++this.crossings;
        } else {
            --this.crossings;
        }
    }

    /** Passing params to methods via this class in order to reduce the line length.
     * @author $Author: fevzi ozgul$
     * @version $FullVersion$
     * @mavengroupid $GroupId$
     * @mavenartifactid $ArtifactId$
     */
    private    final  class CoordinatesParam {
        private  double x1;

        private double y1;

        private  double x2;

        private  double y2;

        private  double curx;

        private  double cury;

        /** Determine where the segment is crossing the shadow line.
         *
         * @param x1 x   coordinate.
         * @param y1 y coordinate .
         * @param x2 x coordinate of the end  point .
         * @param y2 y coordinate of the end point .
         * @param curx  current xcoordinate .
         * @param cury current y coordinate .
         */
        CoordinatesParam(double x1, double y1, double x2, double y2, double curx, double cury) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.curx = curx;
            this.cury = cury;
        }

        public double getX1() {
            return this.x1;
        }

        public double getY1() {
            return this.y1;
        }


        public double getX2() {
            return this.x2;
        }

        public double getY2() {
            return this.y2;
        }

        public double getCurx() {
            return this.curx;
        }

        public double getCury() {
            return this.cury;
        }
    }
}

