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

package org.arakhne.afc.math.geometry.d3.afp;

import java.util.Iterator;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.vmutil.ReflectionUtil;

/** Shadow of a path that is used for computing the crossing values
 * between a shape and the shadow.
 *
 * @param <B> the type of the bounds.
 * @author $Author: sgalland$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class PathShadow3afp<B extends RectangularPrism3afp<?, ?, ?, ?, ?, B>> {

	private final PathIterator3afp<?> path;

	private final B bounds;

	private boolean started;

	/** Construct new path shadow.
	 * @param path the path that is constituting the shadow.
	 */
	public PathShadow3afp(Path3afp<?, ?, ?, ?, ?, B> path) {
	    this(path.getPathIterator(), path.toBoundingBox());
	}

	/** Construct new path shadow.
	 * @param pathIterator the iterator on the path that is constituting the shadow.
	 * @param bounds the bounds of the shadow.
	 */
	public PathShadow3afp(PathIterator3afp<?> pathIterator, B bounds) {
	    assert pathIterator != null : "PathIterator must be not null";
		assert bounds != null : "Bounds must be not null";
		this.path = pathIterator;
		this.bounds = bounds;
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
	@Pure
	public int computeCrossings(
			int crossings,
			double x0, double y0, double z0,
			double x1, double y1, double z1) {

        if (this.bounds == null) {
            return crossings;
        }

		int numCrosses =
				Segment3afp.computeCrossingsFromRect(crossings,
						this.bounds.getMinX(),
						this.bounds.getMinY(),
						this.bounds.getMinZ(),
						this.bounds.getMaxX(),
						this.bounds.getMaxY(),
						this.bounds.getMaxZ(),
						x0, y0, z0,
						x1, y1, z1);

        if (numCrosses == MathConstants.SHAPE_INTERSECTS) {
			// The segment is intersecting the bounds of the shadow path.
			// We must consider the shape of shadow path now.
			final PathShadowData data = new PathShadowData(
					this.bounds.getMaxX(),
					this.bounds.getMinY(),
					this.bounds.getMaxY());

			computeCrossings1(
					this.path,
					x0, y0, z0, x1, y1, z1,
					false,
					this.path.getWindingRule(),
					this.path.getGeomFactory(),
					data);
			numCrosses = data.getCrossings();

            final int mask = this.path.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
            if (numCrosses == MathConstants.SHAPE_INTERSECTS || (numCrosses & mask) != 0) {
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
                numCrosses += inc;
            } else {
                numCrosses -= inc;
            }

			// Apply the previously computed crossings
			numCrosses += crossings;
		}

		return numCrosses;
	}

	@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:cyclomaticcomplexity",
      "checkstyle:npathcomplexity"})
	private static <E extends PathElement3afp> void computeCrossings1(
			Iterator<? extends PathElement3afp> pi,
			double x1, double y1, double z1, double x2, double y2, double z2,
			boolean closeable,
			PathWindingRule rule,
			GeomFactory3afp<E, ?, ?, ?> factory,
			PathShadowData data) {
        if (!pi.hasNext() || data.getCrossings() == MathConstants.SHAPE_INTERSECTS) {
            return;
        }
		PathElement3afp element;

		element = pi.next();
		if (element.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition");
		}

		Path3afp<?, ?, E, ?, ?, ?> localPath;
		double movx = element.getToX();
		double movy = element.getToY();
		double movz = element.getToZ();
		double curx = movx;
		double cury = movy;
		double curz = movz;
		double endx;
		double endy;
		double endz;
        while (data.getCrossings() != MathConstants.SHAPE_INTERSECTS && pi.hasNext()) {
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
				computeCrossings2(
						curx, cury, curz,
						endx, endy, endz,
						x1, y1, z1, x2, y2, z2,
						data);
                if (data.getCrossings() == MathConstants.SHAPE_INTERSECTS) {
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
                localPath = factory.newPath(rule);
                localPath.moveTo(curx, cury, curz);
                localPath.quadTo(
						element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(),
						endx, endy, endz);
                computeCrossings1(
						localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						x1, y1, z1, x2, y2, z2,
						false,
						rule,
						factory,
						data);
                if (data.getCrossings() == MathConstants.SHAPE_INTERSECTS) {
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
				localPath = factory.newPath(rule);
				localPath.moveTo(curx, cury, curz);
				localPath.curveTo(
						element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(),
						element.getCtrlX2(), element.getCtrlY2(), element.getCtrlZ2(),
						endx, endy, endz);
				computeCrossings1(
						localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						x1, y1, z1, x2, y2, z2,
						false,
						rule,
						factory,
						data);
                if (data.getCrossings() == MathConstants.SHAPE_INTERSECTS) {
					return;
				}
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CLOSE:
				if (cury != movy || curx != movx || curz != movz) {
					computeCrossings2(
							curx, cury, curz,
							movx, movy, movz,
							x1, y1, z1, x2, y2, z2,
							data);
				}
                if (data.getCrossings() != 0) {
                    return;
                }
				curx = movx;
				cury = movy;
				curz = movz;
				break;
            case ARC_TO:
			default:
			}
		}

        assert data.getCrossings() != MathConstants.SHAPE_INTERSECTS;

        final boolean isOpen = (curx != movx) || (cury != movy) || (curz != movz);

		if (isOpen) {
			if (closeable) {
				computeCrossings2(
						curx, cury, curz,
						movx, movy, movz,
						x1, y1, z1, x2, y2, z2,
						data);
			} else {
				// Assume that when is the path is open, only
				// SHAPE_INTERSECTS may be return
				data.setCrossings(0);
			}
		}
	}

	@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:cyclomaticcomplexity",
      "checkstyle:npathcomplexity"})
	private static void computeCrossings2(
			double shadow_x0, double shadow_y0, double shadow_z0,
			double shadow_x1, double shadow_y1, double shadow_z1,
			double sx0, double sy0, double sz0,
			double sx1, double sy1, double sz1,
			PathShadowData data) {
		final double shadowXMin = Math.min(shadow_x0, shadow_x1);
		final double shadowXMax = Math.max(shadow_x0, shadow_x1);
		final double shadowYMin = Math.min(shadow_y0, shadow_y1);
		final double shadowYMax = Math.max(shadow_y0, shadow_y1);
		final double shadowZMin = Math.min(shadow_z0, shadow_z1);
		final double shadowZMax = Math.max(shadow_z0, shadow_z1);


        if (sy0 <= shadowYMin && sy1 <= shadowYMin) {
            return;
        }
        if (sy0 >= shadowYMax && sy1 >= shadowYMax) {
            return;
        }
        if (sx0 <= shadowXMin && sx1 <= shadowXMin) {
            return;
        }
        if (sx0 >= shadowXMax && sx1 >= shadowXMax) {
			// The line is entirely at the right of the shadow
			final double alpha = (sx1 - sx0) / (sy1 - sy0);
            if (sy0 < sy1) {
                if (sy0 <= shadowYMin) {
					final double xintercept = sx0 + (shadowYMin - sy0) * alpha;
					data.setCrossingCoordinateForYMin(xintercept, shadowYMin);
					data.setCrossings(data.getCrossings() + 1);
				}
                if (sy1 >= shadowYMax) {
					final double xintercept = sx0 + (shadowYMax - sy0) * alpha;
					data.setCrossingCoordinateForYMax(xintercept, shadowYMax);
					data.setCrossings(data.getCrossings() + 1);
				}
			} else {
                if (sy1 <= shadowYMin) {
					final double xintercept = sx0 + (shadowYMin - sy0) * alpha;
					data.setCrossingCoordinateForYMin(xintercept, shadowYMin);
					data.setCrossings(data.getCrossings() - 1);
				}
                if (sy0 >= shadowYMax) {
					final double xintercept = sx0 + (shadowYMax - sy0) * alpha;
					data.setCrossingCoordinateForYMax(xintercept, shadowYMax);
					data.setCrossings(data.getCrossings() - 1);
				}
			}
		} else if (Segment3afp.intersectsSegmentSegmentWithoutEnds(
				shadow_x0, shadow_y0, shadow_z0, shadow_x1, shadow_y1, shadow_z1,
				sx0, sy0, sz0, sx1, sy1, sz1)) {
			data.setCrossings(MathConstants.SHAPE_INTERSECTS);
		} else {
			final int side1;
			final int side2;
            final boolean isUp = shadow_y0 <= shadow_y1;
			if (isUp) {
				side1 = Segment3afp.computeSideLinePoint(
						shadow_x0, shadow_y0, shadow_z0,
						shadow_x1, shadow_y1, shadow_z1,
						sx0, sy0, sz0, 0.);
				side2 = Segment3afp.computeSideLinePoint(
						shadow_x0, shadow_y0, shadow_z0,
						shadow_x1, shadow_y1, shadow_z1,
						sx1, sy1, sz1, 0.);
			} else {
				side1 = Segment3afp.computeSideLinePoint(
						shadow_x1, shadow_y1, shadow_z1,
						shadow_x0, shadow_y0, shadow_z0,
						sx0, sy0, sz0, 0.);
				side2 = Segment3afp.computeSideLinePoint(
						shadow_x1, shadow_y1, shadow_z1,
						shadow_x0, shadow_y0, shadow_z0,
						sx1, sy1, sz1, 0.);
			}
            if (side1 > 0 || side2 > 0) {
				computeCrossings3(
						shadow_x0, shadow_y0,
						sx0, sy0, sx1, sy1,
						data, isUp);
				computeCrossings3(
						shadow_x1, shadow_y1,
						sx0, sy0, sx1, sy1,
						data, !isUp);
			}
		}
	}

	private static void computeCrossings3(
			double shadowx, double shadowy,
			double sx0, double sy0,
			double sx1, double sy1,
			PathShadowData data,
			boolean isUp) {
        if (shadowy < sy0 && shadowy < sy1) {
            return;
        }
        if (shadowy > sy0 && shadowy > sy1) {
            return;
        }
        if (shadowx > sx0 && shadowx > sx1) {
            return;
        }
        final double xintercept = sx0 + (shadowy - sy0) * (sx1 - sx0) / (sy1 - sy0);
        if (shadowx > xintercept) {
            return;
        }
		if (isUp) {
			data.setCrossingCoordinateForYMax(xintercept, shadowy);
		} else {
			data.setCrossingCoordinateForYMin(xintercept, shadowy);
		}
		data.setCrossings(data.getCrossings() + ((sy0 < sy1) ? 1 : -1));
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
            return ReflectionUtil.toString(this);
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
