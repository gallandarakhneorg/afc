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
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;

/** Shadow of a path that is used for computing the crossing values
 * between a shape and the shadow.
 *
 * @param <B> the type of the bounds.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class PathShadow2afp<B extends Rectangle2afp<?, ?, ?, ?, ?, B>> {

	private final Path2afp<?, ?, ?, ?, ?, B> path;

	private final B bounds;

	/** Construct new path shadow.
	 * @param path the path.
	 */
	public PathShadow2afp(Path2afp<?, ?, ?, ?, ?, B> path) {
		assert path != null : "Path must be not null"; //$NON-NLS-1$
		this.path = path;
		this.bounds = this.path.toBoundingBox();
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
			double x0, double y0,
			double x1, double y1) {
		if (this.bounds == null) {
			return crossings;
		}

		int numCrosses =
				Segment2afp.computeCrossingsFromRect(crossings,
				this.bounds.getMinX(),
				this.bounds.getMinY(),
				this.bounds.getMaxX(),
				this.bounds.getMaxY(),
				x0, y0,
				x1, y1);

		if (numCrosses == MathConstants.SHAPE_INTERSECTS) {
			// The segment is intersecting the bounds of the shadow path.
			// We must consider the shape of shadow path now.
			final PathShadowData data = new PathShadowData(
					this.bounds.getMaxX(),
					this.bounds.getMinY(),
					this.bounds.getMaxY());

			computeCrossings1(
					this.path.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
					x0, y0, x1, y1,
					false,
					this.path.getWindingRule(),
					this.path.getGeomFactory(),
					data);
			numCrosses = data.getCrossings();

			final int mask = this.path.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
			if (numCrosses == MathConstants.SHAPE_INTERSECTS
					|| (numCrosses & mask) != 0) {
				// The given line is intersecting the path shape
				return MathConstants.SHAPE_INTERSECTS;
			}

			// There is no intersection with the shadow path's shape.
			int inc = 0;
			if (data.hasX4ymin() && data.getX4ymin() >= data.getXmin4ymin()) {
				++inc;
			}
			if (data.hasX4ymax() && data.getX4ymax() >= data.getXmin4ymax()) {
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
	private static <E extends PathElement2afp> void computeCrossings1(
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
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
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
				computeCrossings2(
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
				computeCrossings1(
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
				computeCrossings1(
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
				computeCrossings1(
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
					computeCrossings2(
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
				computeCrossings2(
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

	@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:cyclomaticcomplexity",
			"checkstyle:npathcomplexity"})
	private static void computeCrossings2(
			double shadowX0, double shadowY0,
			double shadowX1, double shadowY1,
			double sx0, double sy0,
			double sx1, double sy1,
			PathShadowData data) {
		final double shadowXmin = Math.min(shadowX0, shadowX1);
		final double shadowXmax = Math.max(shadowX0, shadowX1);
		final double shadowYmin = Math.min(shadowY0, shadowY1);
		final double shadowYmax = Math.max(shadowY0, shadowY1);

		data.updateShadowLimits(shadowX0, shadowY0, shadowX1, shadowY1);

		if (sy0 <= shadowYmin && sy1 <= shadowYmin) {
			return;
		}
		if (sy0 >= shadowYmax && sy1 >= shadowYmax) {
			return;
		}
		if (sx0 <= shadowXmin && sx1 <= shadowXmin) {
			return;
		}
		if (sx0 >= shadowXmax && sx1 >= shadowXmax) {
			// The line is entirely at the right of the shadow
			final double alpha = (sx1 - sx0) / (sy1 - sy0);
			if (sy0 < sy1) {
				if (sy0 <= shadowYmin) {
					final double xintercept = sx0 + (shadowYmin - sy0) * alpha;
					data.setCrossingForYMin(xintercept, shadowYmin);
					data.incrementCrossings();
				}
				if (sy1 >= shadowYmax) {
					final double xintercept = sx0 + (shadowYmax - sy0) * alpha;
					data.setCrossingForYMax(xintercept, shadowYmax);
					data.incrementCrossings();
				}
			} else {
				if (sy1 <= shadowYmin) {
					final double xintercept = sx0 + (shadowYmin - sy0) * alpha;
					data.setCrossingForYMin(xintercept, shadowYmin);
					data.decrementCrossings();
				}
				if (sy0 >= shadowYmax) {
					final double xintercept = sx0 + (shadowYmax - sy0) * alpha;
					data.setCrossingForYMax(xintercept, shadowYmax);
					data.decrementCrossings();
				}
			}
		} else if (Segment2afp.intersectsSegmentSegmentWithoutEnds(
				shadowX0, shadowY0, shadowX1, shadowY1,
				sx0, sy0, sx1, sy1)) {
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
				computeCrossings3(
						shadowX0, shadowY0,
						sx0, sy0, sx1, sy1,
						data, isUp);
				computeCrossings3(
						shadowX1, shadowY1,
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
		if (shadowy <  sy0 && shadowy <  sy1) {
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
			data.setCrossingForYMax(xintercept, shadowy);
		} else {
			data.setCrossingForYMin(xintercept, shadowy);
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

		private double xmin4ymin;

		private double xmin4ymax;

		private double ymin;

		private double ymax;

		PathShadowData(double xmax, double miny, double maxy) {
			this.x4ymin = xmax;
			this.x4ymax = xmax;
			this.xmin4ymax = xmax;
			this.xmin4ymin = xmax;
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

		/** Replies x coordinate for ymin.
		 *
		 * @return x coordinate for ymin.
		 */
		public double getX4ymin() {
			return this.x4ymin;
		}

		/** Replies x coordinate for ymax.
		 *
		 * @return x coordinate for ymax.
		 */
		public double getX4ymax() {
			return this.x4ymax;
		}

		/** Replies minimum x coordinate for ymin.
		 *
		 * @return minimum x coordinate for ymin.
		 */
		public double getXmin4ymin() {
			return this.xmin4ymin;
		}

		/** Replies minimum x coordinate for ymax.
		 *
		 * @return minimum x coordinate for ymax.
		 */
		public double getXmin4ymax() {
			return this.xmin4ymax;
		}

		@Pure
		@Override
		public String toString() {
			final StringBuilder b = new StringBuilder();
			b.append("SHADOW {\n\tlow: ( "); //$NON-NLS-1$
			b.append(this.xmin4ymin);
			b.append(" | "); //$NON-NLS-1$
			b.append(this.ymin);
			b.append(" )\n\thigh: ( "); //$NON-NLS-1$
			b.append(this.xmin4ymax);
			b.append(" | "); //$NON-NLS-1$
			b.append(this.ymax);
			b.append(")\n}\nCROSSINGS {\n\tcrossings="); //$NON-NLS-1$
			b.append(this.crossings);
			b.append("\n\tlow: "); //$NON-NLS-1$
			if (this.hasX4ymin) {
				b.append("( "); //$NON-NLS-1$
				b.append(this.x4ymin);
				b.append(" | "); //$NON-NLS-1$
				b.append(this.ymin);
				b.append(" )\n"); //$NON-NLS-1$
			} else {
				b.append("none\n"); //$NON-NLS-1$
			}
			b.append("\thigh: "); //$NON-NLS-1$
			if (this.hasX4ymax) {
				b.append("( "); //$NON-NLS-1$
				b.append(this.x4ymax);
				b.append(" | "); //$NON-NLS-1$
				b.append(this.ymax);
				b.append(" )\n"); //$NON-NLS-1$
			} else {
				b.append("none\n"); //$NON-NLS-1$
			}
			b.append("}\n"); //$NON-NLS-1$
			return b.toString();
		}

		public void setCrossingForYMax(double x, double y) {
			if (y >= this.ymax) {
				if (x < this.x4ymax) {
					this.x4ymax = x;
					this.hasX4ymax = true;
				}
			}
		}

		public void setCrossingForYMin(double x, double y) {
			if (y <= this.ymin) {
				if (x < this.x4ymin) {
					this.x4ymin = x;
					this.hasX4ymin = true;
				}
			}
		}

		public void updateShadowLimits(double shadowX0, double shadowY0, double shadowX1, double shadowY1) {
			final double xl;
			final double yl;
			final double xh;
			final double yh;
			if (shadowY0 < shadowY1) {
				xl = shadowX0;
				yl = shadowY0;
				xh = shadowX1;
				yh = shadowY1;
			} else if (shadowY1 < shadowY0) {
				xl = shadowX1;
				yl = shadowY1;
				xh = shadowX0;
				yh = shadowY0;
			} else {
				xl = Math.min(shadowX0, shadowX1);
				xh = xl;
				yl = shadowY0;
				yh = yl;
			}

			if (yl <= this.ymin && xl < this.xmin4ymin) {
				this.xmin4ymin = xl;
			}

			if (yh >= this.ymax && xh < this.xmin4ymax) {
				this.xmin4ymax = xh;
			}
		}

	}

}
