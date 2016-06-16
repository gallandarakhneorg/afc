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

package org.arakhne.afc.math.geometry.d3.ai;

import java.util.Iterator;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.vmutil.ReflectionUtil;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.arakhne.afc.vmutil.locale.Locale;

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
public class PathShadow3ai<B extends RectangularPrism3ai<?, ?, ?, ?, ?, B>> {

	private final Path3ai<?, ?, ?, ?, ?, B> path;

	private final B bounds;

	/** Construct new path shadow.
     * @param path the path that is constituting the shadow.
     */
	public PathShadow3ai(Path3ai<?, ?, ?, ?, ?, B> path) {
		assert path != null : AssertMessages.notNullParameter();
		this.path = path;
		this.bounds = this.path.toBoundingBox();
		assert this.bounds != null;
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
			int x0, int y0, int z0,
			int x1, int y1, int z1) {
		int numCrosses =
				Segment3ai.computeCrossingsFromRect(crossings,
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
					this.path.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
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
            if (data.isHasX4ymin() && data.getX4ymin() >= data.getXmin4ymin()) {
				++inc;
			}
            if (data.isHasX4ymax() && data.getX4ymax() >= data.getXmin4ymax()) {
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

	@SuppressWarnings({"checkstyle:npathcomplexity", "checkstyle:cyclomaticcomplexity", "checkstyle:parameternumber"})
	private static <E extends PathElement3ai> void computeCrossings1(
			Iterator<? extends PathElement3ai> pi,
			int x1, int y1, int z1, int x2, int y2, int z2,
			boolean closeable,
			PathWindingRule rule,
			GeomFactory3ai<E, ?, ?, ?> factory,
			PathShadowData data) {
        if (!pi.hasNext() || data.getCrossings() == MathConstants.SHAPE_INTERSECTS) {
            return;
        }
		PathElement3ai element;

		element = pi.next();
		if (element.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException(Locale.getString(Path3ai.class, "E1")); //$NON-NLS-1$
		}

		Path3ai<?, ?, E, ?, ?, ?> localPath;
		int movx = element.getToX();
		int movy = element.getToY();
		int movz = element.getToZ();
		int curx = movx;
		int cury = movy;
		int curz = movz;
		int endx;
		int endy;
		int endz;
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

	@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:npathcomplexity", "checkstyle:cyclomaticcomplexity"})
	private static void computeCrossings2(
			int shadow_x0, int shadow_y0, int shadow_z0,
			int shadow_x1, int shadow_y1, int shadow_z1,
			int sx0, int sy0, int sz0,
			int sx1, int sy1, int sz1,
			PathShadowData data) {
		final int shadowXmin = Math.min(shadow_x0, shadow_x1);
		final int shadowXmax = Math.max(shadow_x0, shadow_x1);
		final int shadowYmin = Math.min(shadow_y0, shadow_y1);
		final int shadowYmax = Math.max(shadow_y0, shadow_y1);
		//TODO final int shadowZmin = Math.min(shadow_z0, shadow_z1);
		//TODO final int shadowZmax = Math.max(shadow_z0, shadow_z1);

		data.updateShadowLimits(shadow_x0, shadow_y0, shadow_z0, shadow_x1, shadow_y1, shadow_z1);

        if (sy0 < shadowYmin && sy1 < shadowYmin) {
            return;
        }
        if (sy0 > shadowYmax && sy1 > shadowYmax) {
            return;
        }
        if (sx0 < shadowXmin && sx1 < shadowXmin) {
            return;
        }
        if (sx0 > shadowXmax && sx1 > shadowXmax) {
			// The line is entirely at the right of the shadow
			if (sy1 != sy0) {
                final double alpha = (sx1 - sx0) / (sy1 - sy0);
                if (sy0 < sy1) {
                    if (sy0 <= shadowYmin) {
						final int xintercept = (int) Math.round(sx0 + (shadowYmin - sy0) * alpha);
						data.setCrossingForYMin(xintercept, shadowYmin);
						data.setCrossings(data.getCrossings() + 1);
					}
                    if (sy1 >= shadowYmax) {
						final int xintercept = (int) Math.round(sx0 + (shadowYmax - sy0) * alpha);
						data.setCrossingForYMax(xintercept, shadowYmax);
						data.setCrossings(data.getCrossings() + 1);
					}
				} else {
                    if (sy1 <= shadowYmin) {
						final int xintercept = (int) Math.round(sx0 + (shadowYmin - sy0) * alpha);
						data.setCrossingForYMin(xintercept, shadowYmin);
						data.setCrossings(data.getCrossings() - 1);
					}
                    if (sy0 >= shadowYmax) {
						final int xintercept = (int) Math.round(sx0 + (shadowYmax - sy0) * alpha);
						data.setCrossingForYMax(xintercept, shadowYmax);
						data.setCrossings(data.getCrossings() - 1);
					}
				}
			}
		} else if (Segment3ai.intersectsSegmentSegment(
				shadow_x0, shadow_y0, shadow_z0, shadow_x1, shadow_y1, shadow_z1,
				sx0, sy0, sz0, sx1, sy1, sz1)) {
			data.setCrossings(MathConstants.SHAPE_INTERSECTS);
		} else {
			final int side1;
			final int side2;
            final boolean isUp = shadow_y0 <= shadow_y1;
			if (isUp) {
				side1 = Segment3ai.computeSideLinePoint(
						shadow_x0, shadow_y0, shadow_z0,
						shadow_x1, shadow_y1, shadow_z1,
						sx0, sy0, sz0);
				side2 = Segment3ai.computeSideLinePoint(
						shadow_x0, shadow_y0, shadow_z0,
						shadow_x1, shadow_y1, shadow_z1,
						sx1, sy1, sz1);
			} else {
				side1 = Segment3ai.computeSideLinePoint(
						shadow_x1, shadow_y1, shadow_z1,
						shadow_x0, shadow_y0, shadow_z0,
						sx0, sy0, sz0);
				side2 = Segment3ai.computeSideLinePoint(
						shadow_x1, shadow_y1, shadow_z1,
						shadow_x0, shadow_y0, shadow_z0,
						sx1, sy1, sz1);
			}
            if (side1 > 0 || side2 > 0) {
				computeCrossings3(
						shadow_x0, shadow_y0, shadow_z0,
						sx0, sy0, sz0, sx1, sy1, sz1,
						data, isUp);
				computeCrossings3(
						shadow_x1, shadow_y1, shadow_z1,
						sx0, sy0, sz0, sx1, sy1, sz1,
						data, !isUp);
			}
		}
	}

	@SuppressWarnings("checkstyle:parameternumber")
	private static void computeCrossings3(
			int shadowx, int shadowy, int shadowz,
			int sx0, int sy0, int sz0,
			int sx1, int sy1, int sz1,
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
		final int xintercept = (int) Math.round((double) sx0 + (shadowy - sy0) * (sx1 - sx0) / (sy1 - sy0));
		if (shadowx > xintercept) {
            return;
        }
		if (isUp) {
			data.setCrossingForYMax(xintercept, shadowy);
		} else {
			data.setCrossingForYMin(xintercept, shadowy);
		}
		data.setCrossings(data.getCrossings() + ((sy0 < sy1) ? 1 : -1));
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

		private int xmin4ymin;

		private int xmin4ymax;

		private int ymin;

		private int ymax;

        PathShadowData(int xmax, int miny, int maxy) {
            this.setX4ymin(this.setX4ymax(xmax));
            this.setXmin4ymax(this.setXmin4ymin(xmax));
            this.setYmin(miny);
            this.setYmax(maxy);
        }

		@Pure
		@Override
		public String toString() {
			return ReflectionUtil.toString(this);
		}

		public void setCrossingForYMax(int x, int y) {
            if (y >= this.getYmax() && x < this.getX4ymax()) {
				this.setX4ymax(x);
				this.setHasX4ymax(true);
			}
		}

		public void setCrossingForYMin(int x, int y) {
            if (y <= this.getYmin() && x < this.getX4ymin()) {
				this.setX4ymin(x);
				this.setHasX4ymin(true);
			}
		}

		public void updateShadowLimits(int shadow_x0, int shadow_y0, int shadow_z0, int shadow_x1, int shadow_y1, int shadow_z1) {
			final int xl;
			final int yl;
			final int xh;
			final int yh;
            if (shadow_y0 < shadow_y1) {
				xl = shadow_x0;
				yl = shadow_y0;
				xh = shadow_x1;
				yh = shadow_y1;
            } else if (shadow_y1 < shadow_y0) {
				xl = shadow_x1;
				yl = shadow_y1;
				xh = shadow_x0;
				yh = shadow_y0;
			} else {
				xl = Math.min(shadow_x0, shadow_x1);
				xh = xl;
				yl = shadow_y0;
				yh = yl;
			}

            if (yl <= this.getYmin() && xl < this.getXmin4ymin()) {
				this.setXmin4ymin(xl);
			}

            if (yh >= this.getYmax() && xh < this.getXmin4ymax()) {
				this.setXmin4ymax(xh);
			}
		}

        public int getCrossings() {
            return this.crossings;
        }

        public void setCrossings(int crossings) {
            this.crossings = crossings;
        }

        public boolean isHasX4ymin() {
            return this.hasX4ymin;
        }

        public void setHasX4ymin(boolean hasX4ymin) {
            this.hasX4ymin = hasX4ymin;
        }

        public boolean isHasX4ymax() {
            return this.hasX4ymax;
        }

        public void setHasX4ymax(boolean hasX4ymax) {
            this.hasX4ymax = hasX4ymax;
        }

        public int getX4ymin() {
            return this.x4ymin;
        }

        public void setX4ymin(int x4ymin) {
            this.x4ymin = x4ymin;
        }

        public int getX4ymax() {
            return this.x4ymax;
        }

        public int setX4ymax(int x4ymax) {
            this.x4ymax = x4ymax;
            return x4ymax;
        }

        public int getXmin4ymin() {
            return this.xmin4ymin;
        }

        public int setXmin4ymin(int xmin4ymin) {
            this.xmin4ymin = xmin4ymin;
            return xmin4ymin;
        }

        public int getXmin4ymax() {
            return this.xmin4ymax;
        }

        public void setXmin4ymax(int xmin4ymax) {
            this.xmin4ymax = xmin4ymax;
        }

        public int getYmin() {
            return this.ymin;
        }

        public void setYmin(int ymin) {
            this.ymin = ymin;
        }

        public int getYmax() {
            return this.ymax;
        }

        public void setYmax(int ymax) {
            this.ymax = ymax;
        }

	}

}
