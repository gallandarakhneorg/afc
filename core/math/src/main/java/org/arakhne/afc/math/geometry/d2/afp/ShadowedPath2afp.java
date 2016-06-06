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

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.arakhne.afc.vmutil.locale.Locale;

/** Shadow of a path that is used for computing the crossing values
 * between a shape and the shadow and the cloest point.
 *
 * <p>Two classes have close features: <ul>
 * <li>{@link PathShadow2afp}: the shadow of a path that is used for computing the crossings only, and</li>
 * <li>{@link ShadowedPath2afp}: the shadow of a path that is used for closest point to another path
 * (using the crossings for detection inclusion of shapes).</li>
 * </ul>
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 * @see PathShadow2afp
 */
class ShadowedPath2afp {

    private final PathIterator2afp<?> pathIterator;

    private boolean started;

    private final Point2D<?, ?> closestPoint = new InnerComputationPoint2afp();

    private double minDistance = Double.POSITIVE_INFINITY;

    /** Construct new path shadow.
     * @param pathIterator the iterator on the path that is constituting the shadow.
     */
    ShadowedPath2afp(PathIterator2afp<?> pathIterator) {
        assert pathIterator != null : AssertMessages.notNullParameter(0);
        this.pathIterator = pathIterator;
    }

    /** Update the closest point with the given segment.
     *
     * @param x0 is the first point of the segment.
     * @param y0 is the first point of the segment.
     * @param x1 is the second point of the segment.
     * @param y1 is the second point of the segment.
     * @return <code>true</code> if the segment intersects or is inside the shadowed path.
     */
    @Pure
    @SuppressWarnings("checkstyle:npathcomplexity")
    public boolean update(
            double x0, double y0,
            double x1, double y1) {
        final PathIterator2afp<?> iterator;
        if (this.started) {
            iterator = this.pathIterator.restartIterations();
        } else {
            this.started = true;
            iterator = this.pathIterator;
        }
        final int crossings = update(0, x0, y0, x1, y1, iterator);
        final int mask = iterator.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
        return crossings == MathConstants.SHAPE_INTERSECTS || (crossings & mask) != 0;
    }

    /** Update the closest point with the given segment.
    *
    * @param initialCrossings the initial number of crossings.
    * @param x0 is the first point of the segment.
    * @param y0 is the first point of the segment.
    * @param x1 is the second point of the segment.
    * @param y1 is the second point of the segment.
    * @param iterator the iterator of the shadowed path.
    * @return thr crossings of the given segment.
    */
    @SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity", "checkstyle:returncount"})
    protected int update(
            int initialCrossings,
            double x0, double y0,
            double x1, double y1,
            PathIterator2afp<?> iterator) {
        if (initialCrossings == MathConstants.SHAPE_INTERSECTS) {
            return initialCrossings;
        }
        if (!iterator.hasNext()) {
            return 0;
        }
        PathElement2afp element;

        element = iterator.next();
        if (element.getType() != PathElementType.MOVE_TO) {
            throw new IllegalArgumentException(Locale.getString(Path2afp.class, "E1")); //$NON-NLS-1$
        }

        final Point2D<?, ?> point = new InnerComputationPoint2afp();
        Path2afp<?, ?, ?, ?, ?, ?> localPath;
        int crossings = initialCrossings;
        double movx = element.getToX();
        double movy = element.getToY();
        double curx = movx;
        double cury = movy;
        double endx;
        double endy;
        double distance;
        while (iterator.hasNext()) {
            element = iterator.next();
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
                distance = Segment2afp.computeClosestPointToSegment(x0, y0, x1, y1, curx, cury, endx, endy, point);
                if (distance <= 0.) {
                    this.closestPoint.set(point);
                    return MathConstants.SHAPE_INTERSECTS;
                } else if (distance < this.minDistance) {
                    this.minDistance = distance;
                    this.closestPoint.set(point);
                }
                crossings = Segment2afp.computeCrossingsFromSegment(crossings, curx, cury, endx, endy, x0, y0, x1, y1);
                if (crossings == MathConstants.SHAPE_INTERSECTS) {
                    // Distance is close to zero
                    this.closestPoint.set(point);
                    return MathConstants.SHAPE_INTERSECTS;
                }
                curx = endx;
                cury = endy;
                break;
            case QUAD_TO:
                endx = element.getToX();
                endy = element.getToY();
                // only for local use.
                localPath = iterator.getGeomFactory().newPath(iterator.getWindingRule());
                localPath.moveTo(curx, cury);
                localPath.quadTo(
                        element.getCtrlX1(), element.getCtrlY1(),
                        endx, endy);
                crossings = update(crossings,
                        x0, y0, x1, y1,
                        localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO));
                if (crossings == MathConstants.SHAPE_INTERSECTS) {
                    return MathConstants.SHAPE_INTERSECTS;
                }
                curx = endx;
                cury = endy;
                break;
            case CURVE_TO:
                endx = element.getToX();
                endy = element.getToY();
                // only for local use.
                localPath = iterator.getGeomFactory().newPath(iterator.getWindingRule());
                localPath.moveTo(curx, cury);
                localPath.curveTo(
                        element.getCtrlX1(), element.getCtrlY1(),
                        element.getCtrlX2(), element.getCtrlY2(),
                        endx, endy);
                crossings = update(crossings,
                        x0, y0, x1, y1,
                        localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO));
                if (crossings == MathConstants.SHAPE_INTERSECTS) {
                    return MathConstants.SHAPE_INTERSECTS;
                }
                curx = endx;
                cury = endy;
                break;
            case ARC_TO:
                endx = element.getToX();
                endy = element.getToY();
                // only for local use.
                localPath = iterator.getGeomFactory().newPath(iterator.getWindingRule());
                localPath.moveTo(curx, cury);
                localPath.arcTo(
                        endx, endy,
                        element.getRadiusX(), element.getRadiusY(),
                        element.getRotationX(), element.getLargeArcFlag(),
                        element.getSweepFlag());
                crossings = update(crossings,
                        x0, y0, x1, y1,
                        localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO));
                if (crossings == MathConstants.SHAPE_INTERSECTS) {
                    return MathConstants.SHAPE_INTERSECTS;
                }
                curx = endx;
                cury = endy;
                break;
            case CLOSE:
                if (cury != movy || curx != movx) {
                    distance = Segment2afp.computeClosestPointToSegment(x0, y0, x1, y1, curx, cury, movx, movy, point);
                    if (distance <= 0.) {
                        this.closestPoint.set(point);
                        return MathConstants.SHAPE_INTERSECTS;
                    } else if (distance < this.minDistance) {
                        this.minDistance = distance;
                        this.closestPoint.set(point);
                    }
                    crossings = Segment2afp.computeCrossingsFromSegment(crossings, curx, cury, movx, movy, x0, y0, x1, y1);
                    if (crossings == MathConstants.SHAPE_INTERSECTS) {
                        // Distance is close to zero
                        this.closestPoint.set(point);
                        return MathConstants.SHAPE_INTERSECTS;
                    }
                }
                curx = movx;
                cury = movy;
                break;
            default:
            }
        }
        assert crossings != MathConstants.SHAPE_INTERSECTS;
        final boolean isClose = (curx == movx) && (cury == movy);
        if (isClose) {
            return crossings;
        }
        return 0;
    }

    /** Replies the closest point.
     *
     * @return the closest point.
     */
    public Point2D<?, ?> getClosestPoint() {
        return this.closestPoint;
    }

}
