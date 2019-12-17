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

package org.arakhne.afc.math.geometry.d3.ai;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.GeomConstants;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.ai.Segment2ai;
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
class BasicPathShadow3ai {

    private final PathIterator3ai<?> pathIterator;

    private final int boundingMinX;

    private final int boundingMinY;

    private final int boundingMinZ;

    private final int boundingMaxX;

    private final int boundingMaxY;

    private final int boundingMaxZ;

    private boolean started;

    private int crossings;

    private boolean hasX4ymin;

    private boolean hasX4ymax;

    private int x4ymin;

    private int x4ymax;

    /** Construct new path shadow.
     * @param path the path that is constituting the shadow.
     */
    BasicPathShadow3ai(Path3ai<?, ?, ?, ?, ?, ?> path) {
        assert path != null : AssertMessages.notNullParameter();
        this.pathIterator = path.getPathIterator();
        final RectangularPrism3ai<?, ?, ?, ?, ?, ?> box = path.toBoundingBox();
        this.boundingMinX = box.getMinX();
        this.boundingMinY = box.getMinY();
        this.boundingMinZ = box.getMinZ();
        this.boundingMaxX = box.getMaxX();
        this.boundingMaxY = box.getMaxY();
        this.boundingMaxZ = box.getMaxZ();
    }

    /** Construct new path shadow.
     * @param pathIterator the iterator on the path that is constituting the shadow.
     * @param bounds the bounding box enclosing the primitives of the path iterator.
     */
    BasicPathShadow3ai(PathIterator3ai<?> pathIterator, RectangularPrism3ai<?, ?, ?, ?, ?, ?> bounds) {
        assert pathIterator != null : AssertMessages.notNullParameter(0);
        assert bounds != null : AssertMessages.notNullParameter(1);
        this.pathIterator = pathIterator;
        this.boundingMinX = bounds.getMinX();
        this.boundingMinY = bounds.getMinY();
        this.boundingMinZ = bounds.getMinZ();
        this.boundingMaxX = bounds.getMaxX();
        this.boundingMaxY = bounds.getMaxY();
        this.boundingMaxZ = bounds.getMaxZ();
    }

    /** Construct new path shadow.
     * @param pathIterator the iterator on the path that is constituting the shadow.
     * @param minX minimum x coordinate of the bounding box enclosing the primitives of the path iterator.
     * @param minY minimum y coordinate of the bounding box enclosing the primitives of the path iterator.
     * @param minZ minimum z coordinate of the bounding box enclosing the primitives of the path iterator.
     * @param maxX maximum x coordinate of the bounding box enclosing the primitives of the path iterator.
     * @param maxY maximum y coordinate of the bounding box enclosing the primitives of the path iterator.
     * @param maxZ maximum z coordinate of the bounding box enclosing the primitives of the path iterator.
     */
    @SuppressWarnings("checkstyle:magicnumber")
    BasicPathShadow3ai(PathIterator3ai<?> pathIterator, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        assert pathIterator != null : AssertMessages.notNullParameter(0);
        assert minX <= maxX : AssertMessages.lowerEqualParameters(1, minX, 4, maxX);
        assert minY <= maxY : AssertMessages.lowerEqualParameters(2, minY, 5, maxY);
        assert minZ <= maxZ : AssertMessages.lowerEqualParameters(3, minY, 6, maxY);
        this.pathIterator = pathIterator;
        this.boundingMinX = minX;
        this.boundingMinY = minY;
        this.boundingMinZ = minZ;
        this.boundingMaxX = maxX;
        this.boundingMaxY = maxY;
        this.boundingMaxZ = maxZ;
    }

    /** Compute the crossings between this shadow and
     * the given segment.
     *
     * @param crossings is the initial value of the crossings.
     * @param x0 is the first point of the segment.
     * @param y0 is the first point of the segment.
     * @param z0 is the first point of the segment.
     * @param x1 is the second point of the segment.
     * @param y1 is the second point of the segment.
     * @param z1 is the second point of the segment.
     * @return the crossings or {@link MathConstants#SHAPE_INTERSECTS}.
     */
    public int computeCrossings(
            int crossings,
            int x0, int y0, int z0,
            int x1, int y1, int z1) {
        int numCrosses =
                Segment3ai.computeCrossingsFromRect(crossings,
                        this.boundingMinX,
                        this.boundingMinY,
                        this.boundingMinZ,
                        this.boundingMaxX,
                        this.boundingMaxY,
                        this.boundingMaxZ,
                        x0, y0, z0,
                        x1, y1, z1);

        if (numCrosses == GeomConstants.SHAPE_INTERSECTS) {
            // The segment is intersecting the bounds of the shadow path.
            // We must consider the shape of shadow path now.
            this.crossings = 0;
            this.hasX4ymin = false;
            this.hasX4ymax = false;
            this.x4ymin = this.boundingMinX;
            this.x4ymax = this.boundingMinX;

            final PathIterator3ai<?> iterator;
            if (this.started) {
                iterator = this.pathIterator.restartIterations();
            } else {
                this.started = true;
                iterator = this.pathIterator;
            }

            discretizePathIterator(
                    iterator,
                    x0, y0, z0, x1, y1, z1);

            // Test if the shape is intesecting the shadow shape.
            final int mask = iterator.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
            if (this.crossings == GeomConstants.SHAPE_INTERSECTS
                    || (this.crossings & mask) != 0) {
                // The given line is intersecting the path shape
                return GeomConstants.SHAPE_INTERSECTS;
            }

            // There is no intersection with the shadow path's shape.
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
            PathIterator3ai<?> pi,
            int x1, int y1, int z1, int x2, int y2, int z2) {
        if (!pi.hasNext() || this.crossings == GeomConstants.SHAPE_INTERSECTS) {
            return;
        }
        PathElement3ai element;

        element = pi.next();
        if (element.getType() != PathElementType.MOVE_TO) {
            throw new IllegalArgumentException(Locale.getString(Path3ai.class, "E1")); //$NON-NLS-1$
        }

        Path3ai<?, ?, ?, ?, ?, ?> localPath;
        int movx = element.getToX();
        int movy = element.getToY();
        int movz = element.getToZ();
        int curx = movx;
        int cury = movy;
        int curz = movz;
        int endx;
        int endy;
        int endz;
        while (pi.hasNext()) {
            element = pi.next();
            switch (element.getType()) {
            case MOVE_TO:
                movx = element.getToX();
                curx = movx;
                movy = element.getToY();
                cury = movy;
                movz = element.getToZ();
                curz = movz;
                break;
            case LINE_TO:
                endx = element.getToX();
                endy = element.getToY();
                endz = element.getToZ();
                crossSegmentTwoShadowLines(
                        curx, cury, curz,
                        endx, endy, endz,
                        x1, y1, z1, x2, y2, z2);
                if (this.crossings == GeomConstants.SHAPE_INTERSECTS) {
                    return;
                }
                curx = endx;
                cury = endy;
                curz = endz;
                break;
            case QUAD_TO:
                endx = element.getToX();
                endy = element.getToY();
                endz = element.getToZ();
                // only for local use.
                localPath = pi.getGeomFactory().newPath(pi.getWindingRule());
                localPath.moveTo(curx, cury, curz);
                localPath.quadTo(
                        element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(),
                        endx, endy, endz);
                discretizePathIterator(
                        localPath.getPathIterator(pi.getGeomFactory().getSplineApproximationRatio()),
                        x1, y1, z1, x2, y2, z2);
                if (this.crossings == GeomConstants.SHAPE_INTERSECTS) {
                    return;
                }
                curx = endx;
                cury = endy;
                curz = endz;
                break;
            case CURVE_TO:
                endx = element.getToX();
                endy = element.getToY();
                endz = element.getToZ();
                // only for local use.
                localPath = pi.getGeomFactory().newPath(pi.getWindingRule());
                localPath.moveTo(curx, cury, curz);
                localPath.curveTo(
                        element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(),
                        element.getCtrlX2(), element.getCtrlY2(), element.getCtrlZ2(),
                        endx, endy, endz);
                discretizePathIterator(
                        localPath.getPathIterator(pi.getGeomFactory().getSplineApproximationRatio()),
                        x1, y1, z1, x2, y2, z2);
                if (this.crossings == GeomConstants.SHAPE_INTERSECTS) {
                    return;
                }
                curx = endx;
                cury = endy;
                curz = endz;
                break;
            case ARC_TO:
                endx = element.getToX();
                endy = element.getToY();
                endz = element.getToZ();
                // only for local use.
                localPath = pi.getGeomFactory().newPath(pi.getWindingRule());
                localPath.moveTo(curx, cury, curz);
                //localPath.arcTo(
                //        endx, endy,
                //        element.getRadiusX(), element.getRadiusY(),
                //        element.getRotationX(), element.getLargeArcFlag(),
                //        element.getSweepFlag());
                discretizePathIterator(
                        localPath.getPathIterator(pi.getGeomFactory().getSplineApproximationRatio()),
                        x1, y1, z1, x2, y2, z2);
                if (this.crossings == GeomConstants.SHAPE_INTERSECTS) {
                    return;
                }
                curx = endx;
                cury = endy;
                curz = endz;
                break;
            case CLOSE:
                if (cury != movy || curx != movx || curz != movz) {
                    crossSegmentTwoShadowLines(
                            curx, cury, curz,
                            movx, movy, movz,
                            x1, y1, z1, x2, y2, z2);
                }
                if (this.crossings != 0) {
                    return;
                }
                curx = movx;
                cury = movy;
                curz = movz;
                break;
            default:
            }
        }

        assert this.crossings != GeomConstants.SHAPE_INTERSECTS;

        final boolean isOpen = (curx != movx) || (cury != movy) || (curz != movz);

        if (isOpen) {
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
    * @param shadowZ0 z coordinate of the reference point of the first shadow line.
    * @param shadowX1 x coordinate of the reference point of the second shadow line.
    * @param shadowY1 y coordinate of the reference point of the second shadow line.
    * @param shadowZ1 z coordinate of the reference point of the second shadow line.
    * @param sx0 x coordinate of the first point of the segment.
    * @param sy0 y coordinate of the first point of the segment.
    * @param sz0 z coordinate of the first point of the segment.
    * @param sx1 x coordinate of the second point of the segment.
    * @param sy1 y coordinate of the second point of the segment.
    * @param sz1 z coordinate of the second point of the segment.
    */
    @SuppressWarnings({"checkstyle:parameternumber", "checkstyle:cyclomaticcomplexity",
            "checkstyle:npathcomplexity"})
    private void crossSegmentTwoShadowLines(
            int shadowX0, int shadowY0, int shadowZ0,
            int shadowX1, int shadowY1, int shadowZ1,
            int sx0, int sy0, int sz0,
            int sx1, int sy1, int sz1) {
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
                        setCrossingCoordinateForYMin(xintercept, shadowYmin);
                        ++this.crossings;
                    }
                    if (sy1 >= shadowYmax) {
                        final int xintercept = (int) Math.round(sx0 + (shadowYmax - sy0) * alpha);
                        setCrossingCoordinateForYMax(xintercept, shadowYmax);
                        ++this.crossings;
                    }
                } else {
                    if (sy1 <= shadowYmin) {
                        final int xintercept = (int) Math.round(sx0 + (shadowYmin - sy0) * alpha);
                        setCrossingCoordinateForYMin(xintercept, shadowYmin);
                        --this.crossings;
                    }
                    if (sy0 >= shadowYmax) {
                        final int xintercept = (int) Math.round(sx0 + (shadowYmax - sy0) * alpha);
                        setCrossingCoordinateForYMax(xintercept, shadowYmax);
                        --this.crossings;
                    }
                }
            }
        } else if (Segment2ai.intersectsSegmentSegment(
                shadowX0, shadowY0, shadowX1, shadowY1,
                sx0, sy0, sx1, sy1)) {
            // The segment is intersecting the shadowed segment.
            this.crossings = GeomConstants.SHAPE_INTERSECTS;
        } else {
            final int side1;
            final int side2;
            final boolean isUp = shadowY0 <= shadowY1;
            if (isUp) {
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
     * @param data the data to update.
     */
    private void crossSegmentShadowLine(
            int shadowx, int shadowy,
            int sx0, int sy0,
            int sx1, int sy1,
            boolean isMax) {
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

}
