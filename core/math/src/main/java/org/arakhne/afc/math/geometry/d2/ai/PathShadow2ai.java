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

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;

/** Shadow of a path that is used for computing the crossing values
 * between a shape and the shadow.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class PathShadow2ai {

    private final PathIterator2ai<?> pathIterator;

    private final int boundingMinX;

    private final int boundingMinY;

    private final int boundingMaxX;

    private final int boundingMaxY;

    private boolean started;

    /** Construct new path shadow.
     * @param path the path that is constituting the shadow.
     */
    public PathShadow2ai(Path2ai<?, ?, ?, ?, ?, ?> path) {
        assert path != null : "Path must be not null"; 
        this.pathIterator = path.getPathIterator();
        final Rectangle2ai<?, ?, ?, ?, ?, ?> box = path.toBoundingBox();
        this.boundingMinX = box.getMinX();
        this.boundingMinY = box.getMinY();
        this.boundingMaxX = box.getMaxX();
        this.boundingMaxY = box.getMaxY();
    }

    /** Construct new path shadow.
     * @param pathIterator the iterator on the path that is constituting the shadow.
     * @param bounds the bounding box enclosing the primitives of the path iterator.
     */
    public PathShadow2ai(PathIterator2ai<?> pathIterator, Rectangle2ai<?, ?, ?, ?, ?, ?> bounds) {
        assert pathIterator != null : "Path iterator must be not null"; 
        assert bounds != null : "Bonuding box xmust be not null"; 
        this.pathIterator = pathIterator;
        this.boundingMinX = bounds.getMinX();
        this.boundingMinY = bounds.getMinY();
        this.boundingMaxX = bounds.getMaxX();
        this.boundingMaxY = bounds.getMaxY();
    }

    /** Construct new path shadow.
     * @param pathIterator the iterator on the path that is constituting the shadow.
     * @param minX minimum x coordinate of the bounding box enclosing the primitives of the path iterator.
     * @param minY minimum y coordinate of the bounding box enclosing the primitives of the path iterator.
     * @param maxX maximum x coordinate of the bounding box enclosing the primitives of the path iterator.
     * @param maxY maximum y coordinate of the bounding box enclosing the primitives of the path iterator.
     */
    public PathShadow2ai(PathIterator2ai<?> pathIterator, int minX, int minY, int maxX, int maxY) {
        assert pathIterator != null : "Path iterator must be not null"; 
        assert minX <= maxX : "Minimum X coordinate must be lower than or equal to the maxmimum X coordinate"; 
        assert minY <= maxY : "Minimum X coordinate must be lower than or equal to the maxmimum X coordinate"; 
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
    public int computeCrossings(
            int crossings,
            int x0, int y0,
            int x1, int y1) {
        int numCrosses =
                Segment2ai.computeCrossingsFromRect(crossings,
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

            final PathIterator2ai<?> iterator;
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
    private static <E extends PathElement2ai> void discretizePathIterator(
            Iterator<? extends PathElement2ai> pi,
            int x1, int y1, int x2, int y2,
            boolean closeable,
            PathWindingRule rule,
            GeomFactory2ai<E, ?, ?, ?> factory,
            PathShadowData data) {
        if (!pi.hasNext() || data.getCrossings() == MathConstants.SHAPE_INTERSECTS) {
            return;
        }
        PathElement2ai element;

        element = pi.next();
        if (element.getType() != PathElementType.MOVE_TO) {
            throw new IllegalArgumentException("missing initial moveto in path definition"); 
        }

        Path2ai<?, ?, E, ?, ?, ?> localPath;
        int movx = element.getToX();
        int movy = element.getToY();
        int curx = movx;
        int cury = movy;
        int endx;
        int endy;
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
    private static void crossSegmentTwoShadowLines(
            int shadowX0, int shadowY0,
            int shadowX1, int shadowY1,
            int sx0, int sy0,
            int sx1, int sy1,
            PathShadowData data) {
        // Update the global bounds of the shadow.
        final int shadowXmin = Math.min(shadowX0, shadowX1);
        final int shadowXmax = Math.max(shadowX0, shadowX1);
        final int shadowYmin = Math.min(shadowY0, shadowY1);
        final int shadowYmax = Math.max(shadowY0, shadowY1);

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
        if (sx0 > shadowXmax && sx1 > shadowXmax) {
            // The line is entirely at the right of the shadow
            if (sy1 != sy0) {
                final double alpha = (sx1 - sx0) / (sy1 - sy0);
                if (sy0 < sy1) {
                    if (sy0 <= shadowYmin) {
                        final int xintercept = (int) Math.round(sx0 + (shadowYmin - sy0) * alpha);
                        data.setCrossingCoordinateForYMin(xintercept, shadowYmin);
                        data.incrementCrossings();
                    }
                    if (sy1 >= shadowYmax) {
                        final int xintercept = (int) Math.round(sx0 + (shadowYmax - sy0) * alpha);
                        data.setCrossingCoordinateForYMax(xintercept, shadowYmax);
                        data.incrementCrossings();
                    }
                } else {
                    if (sy1 <= shadowYmin) {
                        final int xintercept = (int) Math.round(sx0 + (shadowYmin - sy0) * alpha);
                        data.setCrossingCoordinateForYMin(xintercept, shadowYmin);
                        data.decrementCrossings();
                    }
                    if (sy0 >= shadowYmax) {
                        final int xintercept = (int) Math.round(sx0 + (shadowYmax - sy0) * alpha);
                        data.setCrossingCoordinateForYMax(xintercept, shadowYmax);
                        data.decrementCrossings();
                    }
                }
            }
        } else if (Segment2ai.intersectsSegmentSegment(
                shadowX0, shadowY0, shadowX1, shadowY1,
                sx0, sy0, sx1, sy1)) {
            // The segment is intersecting the shadowed segment.
            data.setCrossings(MathConstants.SHAPE_INTERSECTS);
        } else {
            final int side1;
            final int side2;
            final boolean isUp = shadowY0 <= shadowY1;
            if (isUp) {
                side1 = Segment2ai.computeSideLinePoint(
                        shadowX0, shadowY0,
                        shadowX1, shadowY1,
                        sx0, sy0);
                side2 = Segment2ai.computeSideLinePoint(
                        shadowX0, shadowY0,
                        shadowX1, shadowY1,
                        sx1, sy1);
            } else {
                side1 = Segment2ai.computeSideLinePoint(
                        shadowX1, shadowY1,
                        shadowX0, shadowY0,
                        sx0, sy0);
                side2 = Segment2ai.computeSideLinePoint(
                        shadowX1, shadowY1,
                        shadowX0, shadowY0,
                        sx1, sy1);
            }
            if (side1 > 0 || side2 > 0) {
                final int x0;
                final int x1;
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
            int shadowx, int shadowy,
            int sx0, int sy0,
            int sx1, int sy1,
            boolean isMax,
            PathShadowData data) {
        if (shadowy < sy0 && shadowy < sy1) {
            // Segment is entirely at the top of the shadow line
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
        final int xintercept = (int) Math.round((double) sx0 + (shadowy - sy0) * (sx1 - sx0) / (sy1 - sy0));
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

    /** Data for shadow.
     * @author $Author: sgalland$
     * @version $FullVersion$
     * @mavengroupid $GroupId$
     * @mavenartifactid $ArtifactId$
     */
    private static class PathShadowData {

        private int crossings;

        private boolean hasX4ymin;

        private boolean hasX4ymax;

        private int x4ymin;

        private int x4ymax;

        private final int ymin;

        private final int ymax;

        PathShadowData(int xmin, int miny, int maxy) {
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
            b.append("y min line:\n\tymin: "); 
            b.append(this.ymin);
            b.append("\n\tx: "); 
            if (this.hasX4ymin) {
                b.append(this.x4ymin);
            } else {
                b.append("none"); 
            }
            b.append("\ny max line:\n\tymax: "); 
            b.append(this.ymax);
            b.append("\n\tx: "); 
            if (this.hasX4ymax) {
                b.append(this.x4ymax);
            } else {
                b.append("none"); 
            }
            b.append("\ncrossings: "); 
            b.append(this.crossings);
            return b.toString();
        }

        public void setCrossingCoordinateForYMax(int x, int y) {
            if (y >= this.ymax) {
                if (x > this.x4ymax) {
                    this.x4ymax = x;
                    this.hasX4ymax = true;
                }
            }
        }

        public void setCrossingCoordinateForYMin(int x, int y) {
            if (y <= this.ymin) {
                if (x > this.x4ymin) {
                    this.x4ymin = x;
                    this.hasX4ymin = true;
                }
            }
        }

    }

}
