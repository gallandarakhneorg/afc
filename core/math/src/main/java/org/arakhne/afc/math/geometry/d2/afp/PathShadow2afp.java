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

import java.util.Iterator;

import org.eclipse.xtext.xbase.lib.Pure;

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
class PathShadow2afp {

    private final PathIterator2afp<?> pathIterator;

    private final double boundingMinX;

    private final double boundingMinY;

    private final double boundingMaxX;

    private final double boundingMaxY;

    private boolean started;

    /** Construct new path shadow.
     * @param path the path that is constituting the shadow.
     */
    PathShadow2afp(Path2afp<?, ?, ?, ?, ?, ?> path) {
        this(path.getPathIterator(), path.toBoundingBox());
    }

    /** Construct new path shadow.
     * @param pathIterator the iterator on the path that is constituting the shadow.
     * @param bounds the bounds of the shadow.
     */
    PathShadow2afp(PathIterator2afp<?> pathIterator, Rectangle2afp<?, ?, ?, ?, ?, ?> bounds) {
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
    PathShadow2afp(PathIterator2afp<?> pathIterator, double minX, double minY, double maxX, double maxY) {
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
    @Pure
    @SuppressWarnings("checkstyle:npathcomplexity")
    public int computeCrossings(
            int crossings,
            double x0, double y0,
            double x1, double y1) {
        int numCrosses =
                Segment2afp.computeCrossingsFromRect(crossings,
                        this.boundingMinX,
                        this.boundingMinY,
                        this.boundingMaxX,
                        this.boundingMaxY,
                        x0, y0,
                        x1, y1);

        if (numCrosses == MathConstants.SHAPE_INTERSECTS) {
            // The segment is intersecting the bounds of the shadow path.
            // We must consider the shape of shadow path now.
            final PathShadowData data = new PathShadowData(
                    this.boundingMinX,
                    this.boundingMinY,
                    this.boundingMaxY);

            final PathIterator2afp<?> iterator;
            if (this.started) {
                iterator = this.pathIterator.restartIterations();
            } else {
                this.started = true;
                iterator = this.pathIterator;
            }

            discretizePathIterator(
                    iterator,
                    x0, y0, x1, y1,
                    false,
                    iterator.getWindingRule(),
                    iterator.getGeomFactory(),
                    data);

            // Test if the shape is intesecting the shadow shape.
            final int exactPathCrossings = data.getCrossings();
            final int mask = iterator.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
            if (exactPathCrossings == MathConstants.SHAPE_INTERSECTS
                    || (exactPathCrossings & mask) != 0) {
                // The given line is intersecting the path shape
                return MathConstants.SHAPE_INTERSECTS;
            }

            // There is no intersection with the shadow path's shape.
            // Compute the crossings with the minimum/maximum y borders.
            int inc = 0;
            if (data.hasX4ymin()) {
                ++inc;
            }
            if (data.hasX4ymax()) {
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
    private static <E extends PathElement2afp> void discretizePathIterator(
            Iterator<? extends PathElement2afp> pi,
            double x1, double y1, double x2, double y2,
            boolean closeable,
            PathWindingRule rule,
            GeomFactory2afp<E, ?, ?, ?> factory,
            PathShadowData data) {
        if (!pi.hasNext() || data.getCrossings() == MathConstants.SHAPE_INTERSECTS) {
            return;
        }
        PathElement2afp element;

        element = pi.next();
        if (element.getType() != PathElementType.MOVE_TO) {
            throw new IllegalArgumentException(Locale.getString(Path2afp.class, "E1")); //$NON-NLS-1$
        }

        Path2afp<?, ?, E, ?, ?, ?> localPath;
        double movx = element.getToX();
        double movy = element.getToY();
        double curx = movx;
        double cury = movy;
        double endx;
        double endy;
        while (data.getCrossings() != MathConstants.SHAPE_INTERSECTS && pi.hasNext()) {
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
                crossSegmentTwoShadowLines(
                        curx, cury,
                        endx, endy,
                        x1, y1, x2, y2,
                        data);
                if (data.getCrossings() == MathConstants.SHAPE_INTERSECTS) {
                    return;
                }
                curx = endx;
                cury = endy;
                break;
            case QUAD_TO:
                endx = element.getToX();
                endy = element.getToY();
                // only for local use.
                localPath = factory.newPath(rule);
                localPath.moveTo(curx, cury);
                localPath.quadTo(
                        element.getCtrlX1(), element.getCtrlY1(),
                        endx, endy);
                discretizePathIterator(
                        localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
                        x1, y1, x2, y2,
                        false,
                        rule,
                        factory,
                        data);
                if (data.getCrossings() == MathConstants.SHAPE_INTERSECTS) {
                    return;
                }
                curx = endx;
                cury = endy;
                break;
            case CURVE_TO:
                endx = element.getToX();
                endy = element.getToY();
                // only for local use.
                localPath = factory.newPath(rule);
                localPath.moveTo(curx, cury);
                localPath.curveTo(
                        element.getCtrlX1(), element.getCtrlY1(),
                        element.getCtrlX2(), element.getCtrlY2(),
                        endx, endy);
                discretizePathIterator(
                        localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
                        x1, y1, x2, y2,
                        false,
                        rule,
                        factory,
                        data);
                if (data.getCrossings() == MathConstants.SHAPE_INTERSECTS) {
                    return;
                }
                curx = endx;
                cury = endy;
                break;
            case ARC_TO:
                endx = element.getToX();
                endy = element.getToY();
                // only for local use.
                localPath = factory.newPath(rule);
                localPath.moveTo(curx, cury);
                localPath.arcTo(
                        endx, endy,
                        element.getRadiusX(), element.getRadiusY(),
                        element.getRotationX(), element.getLargeArcFlag(),
                        element.getSweepFlag());
                discretizePathIterator(
                        localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
                        x1, y1, x2, y2,
                        false,
                        rule,
                        factory,
                        data);
                if (data.getCrossings() == MathConstants.SHAPE_INTERSECTS) {
                    return;
                }
                curx = endx;
                cury = endy;
                break;
            case CLOSE:
                if (cury != movy || curx != movx) {
                    crossSegmentTwoShadowLines(
                            curx, cury,
                            movx, movy,
                            x1, y1, x2, y2,
                            data);
                }
                if (data.getCrossings() != 0) {
                    return;
                }
                curx = movx;
                cury = movy;
                break;
            default:
            }
        }

        assert data.getCrossings() != MathConstants.SHAPE_INTERSECTS;

        final boolean isOpen = (curx != movx) || (cury != movy);

        if (isOpen) {
            if (closeable) {
                crossSegmentTwoShadowLines(
                        curx, cury,
                        movx, movy,
                        x1, y1, x2, y2,
                        data);
            } else {
                // Assume that when is the path is open, only
                // SHAPE_INTERSECTS may be return
                data.setCrossings(0);
            }
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
    * @param data the data to update.
    */
    @SuppressWarnings({"checkstyle:parameternumber", "checkstyle:cyclomaticcomplexity",
            "checkstyle:npathcomplexity"})
    protected static void crossSegmentTwoShadowLines(
            double shadowX0, double shadowY0,
            double shadowX1, double shadowY1,
            double sx0, double sy0,
            double sx1, double sy1,
            PathShadowData data) {
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
                    data.setCrossingCoordinateForYMin(xintercept, shadowYmin);
                    data.incrementCrossings();
                }
                if (sy1 >= shadowYmax) {
                    final double xintercept = sx0 + (shadowYmax - sy0) * alpha;
                    data.setCrossingCoordinateForYMax(xintercept, shadowYmax);
                    data.incrementCrossings();
                }
            } else {
                if (sy1 <= shadowYmin) {
                    final double xintercept = sx0 + (shadowYmin - sy0) * alpha;
                    data.setCrossingCoordinateForYMin(xintercept, shadowYmin);
                    data.decrementCrossings();
                }
                if (sy0 >= shadowYmax) {
                    final double xintercept = sx0 + (shadowYmax - sy0) * alpha;
                    data.setCrossingCoordinateForYMax(xintercept, shadowYmax);
                    data.decrementCrossings();
                }
            }
        } else if (Segment2afp.intersectsSegmentSegmentWithoutEnds(
                shadowX0, shadowY0, shadowX1, shadowY1,
                sx0, sy0, sx1, sy1)) {
            // The segment is intersecting the shadowed segment.
            data.setCrossings(MathConstants.SHAPE_INTERSECTS);
        } else {
            final int side1;
            final int side2;
            final boolean isUp = shadowY0 <= shadowY1;
            if (isUp) {
                side1 = Segment2afp.computeSideLinePoint(
                        shadowX0, shadowY0,
                        shadowX1, shadowY1,
                        sx0, sy0, 0.);
                side2 = Segment2afp.computeSideLinePoint(
                        shadowX0, shadowY0,
                        shadowX1, shadowY1,
                        sx1, sy1, 0.);
            } else {
                side1 = Segment2afp.computeSideLinePoint(
                        shadowX1, shadowY1,
                        shadowX0, shadowY0,
                        sx0, sy0, 0.);
                side2 = Segment2afp.computeSideLinePoint(
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
                        isUp, data);
                crossSegmentShadowLine(
                        x0, shadowYmin,
                        sx0, sy0, sx1, sy1,
                        !isUp, data);
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
     * @param data the data to update.
     */
    protected static void crossSegmentShadowLine(
            double shadowx, double shadowy,
            double sx0, double sy0,
            double sx1, double sy1,
            boolean isMax,
            PathShadowData data) {
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
            data.setCrossingCoordinateForYMax(xintercept, shadowy);
        } else {
            data.setCrossingCoordinateForYMin(xintercept, shadowy);
        }
        if (sy0 < sy1) {
            data.incrementCrossings();
        } else {
            data.decrementCrossings();
        }
    }

    /** Shadow data.
     * @author $Author: sgalland$
     * @version $FullVersion$
     * @mavengroupid $GroupId$
     * @mavenartifactid $ArtifactId$
     */
    private static class PathShadowData {

        private int crossings;

        private boolean hasX4ymin;

        private boolean hasX4ymax;

        private double x4ymin;

        private double x4ymax;

        private final double ymin;

        private final double ymax;

        PathShadowData(double xmin, double miny, double maxy) {
            this.x4ymin = xmin;
            this.x4ymax = xmin;
            this.ymin = miny;
            this.ymax = maxy;
        }

        /** Replies the number of crossings.
         *
         * @return the number of crossings.
         */
        public int getCrossings() {
            return this.crossings;
        }

        /** Change the number of crossings.
         *
         * @param crossings the new number of crossings.
         */
        public void setCrossings(int crossings) {
            this.crossings = crossings;
        }

        /** Increment number of crossings.
         */
        public void incrementCrossings() {
            ++this.crossings;
        }

        /** Decrement number of crossings.
         */
        public void decrementCrossings() {
            --this.crossings;
        }

        /** Replies if a x coordinate is known for ymin.
         *
         * @return <code>true</code> if a x coordinate is known.
         */
        public boolean hasX4ymin() {
            return this.hasX4ymin;
        }

        /** Replies if a x coordinate is known for ymax.
         *
         * @return <code>true</code> if a x coordinate is known.
         */
        public boolean hasX4ymax() {
            return this.hasX4ymax;
        }

        @Pure
        @Override
        public String toString() {
            final StringBuilder b = new StringBuilder();
            b.append("y min line:\n\tymin: "); //$NON-NLS-1$
            b.append(this.ymin);
            b.append("\n\tx: "); //$NON-NLS-1$
            if (this.hasX4ymin) {
                b.append(this.x4ymin);
            } else {
                b.append("none"); //$NON-NLS-1$
            }
            b.append("\ny max line:\n\tymax: "); //$NON-NLS-1$
            b.append(this.ymax);
            b.append("\n\tx: "); //$NON-NLS-1$
            if (this.hasX4ymax) {
                b.append(this.x4ymax);
            } else {
                b.append("none"); //$NON-NLS-1$
            }
            b.append("\ncrossings: "); //$NON-NLS-1$
            b.append(this.crossings);
            return b.toString();
        }

        public void setCrossingCoordinateForYMax(double x, double y) {
            if (MathUtil.compareEpsilon(y, this.ymax) >= 0) {
                if (x > this.x4ymax) {
                    this.x4ymax = x;
                    this.hasX4ymax = true;
                }
            }
        }

        public void setCrossingCoordinateForYMin(double x, double y) {
            if (MathUtil.compareEpsilon(y, this.ymin) <= 0) {
                if (x > this.x4ymin) {
                    this.x4ymin = x;
                    this.hasX4ymin = true;
                }
            }
        }

    }

}
