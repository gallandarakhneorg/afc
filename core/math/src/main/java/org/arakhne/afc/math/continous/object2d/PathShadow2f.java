/* 
 * $Id$
 * 
 * Copyright (C) 2005-09 Stephane GALLAND.
 * Copyright (C) 2012-13 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */
package org.arakhne.afc.math.continous.object2d;

import java.util.Iterator;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.generic.Path2D;
import org.arakhne.afc.math.generic.PathElementType;
import org.arakhne.afc.math.generic.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.afp.PathShadow2afp;
import org.arakhne.afc.math.geometry.d2.afp.Segment2afp;

/** Shadow of a path.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see {@link PathShadow2afp}
 */
@Deprecated
public class PathShadow2f {

	private final Path2D<?,Rectangle2f,PathElement2f,PathIterator2f> path;
	private final Rectangle2f bounds;

	/**
	 * @param path
	 */
	public PathShadow2f(Path2D<?,Rectangle2f,PathElement2f,PathIterator2f> path) {
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
	public int computeCrossings(
			int crossings,
			float x0, float y0,
			float x1, float y1) {
		if (this.bounds==null) return crossings;

		int numCrosses = 
				Segment2f.computeCrossingsFromRect(crossings,
						this.bounds.getMinX(),
						this.bounds.getMinY(),
						this.bounds.getMaxX(),
						this.bounds.getMaxY(),
						x0, y0, 
						x1, y1);

		if (numCrosses==MathConstants.SHAPE_INTERSECTS) {
			// The segment is intersecting the bounds of the shadow path.
			// We must consider the shape of shadow path now.
			PathShadowData data = new PathShadowData(
					this.bounds.getMaxX(),
					this.bounds.getMinY(),
					this.bounds.getMaxY());

			computeCrossings1(
					this.path.getPathIterator((float) MathConstants.SPLINE_APPROXIMATION_RATIO),
					x0, y0, x1, y1,
					false,
					data);
			numCrosses = data.crossings;

			int mask = (this.path.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
			if (numCrosses == MathConstants.SHAPE_INTERSECTS ||
					(numCrosses & mask) != 0) {
				// The given line is intersecting the path shape
				return MathConstants.SHAPE_INTERSECTS;
			}

			// There is no intersection with the shadow path's shape.
			int inc = 0;
			if (data.hasX4ymin && 
					data.x4ymin>=data.xmin4ymin) {
				++inc;
			}
			if (data.hasX4ymax && 
					data.x4ymax>=data.xmin4ymax) {
				++inc;
			}

			if (y0<y1) {
				numCrosses += inc;
			}
			else {
				numCrosses -= inc;
			}

			// Apply the previously computed crossings
			numCrosses += crossings;
		}

		return numCrosses;
	}

	private static void computeCrossings1(
			Iterator<PathElement2f> pi, 
			float x1, float y1, float x2, float y2, 
			boolean closeable, PathShadowData data) {	
		if (!pi.hasNext() || data.crossings==MathConstants.SHAPE_INTERSECTS) return;
		PathElement2f element;

		element = pi.next();
		if (element.type != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		float movx = element.toX;
		float movy = element.toY;
		float curx = movx;
		float cury = movy;
		float endx, endy;
		while (data.crossings!=MathConstants.SHAPE_INTERSECTS && pi.hasNext()) {
			element = pi.next();
			switch (element.type) {
			case MOVE_TO:
				movx = curx = element.toX;
				movy = cury = element.toY;
				break;
			case LINE_TO:
				endx = element.toX;
				endy = element.toY;
				computeCrossings2(
						curx, cury,
						endx, endy,
						x1, y1, x2, y2,
						data);
				if (data.crossings==MathConstants.SHAPE_INTERSECTS) {
					return;
				}
				curx = endx;
				cury = endy;
				break;
			case QUAD_TO:
			{
				endx = element.toX;
				endy = element.toY;
				Path2f localPath = new Path2f();
				localPath.moveTo(curx, cury);
				localPath.quadTo(
						element.ctrlX1, element.ctrlY1,
						endx, endy);
				computeCrossings1(
						localPath.getPathIterator((float) MathConstants.SPLINE_APPROXIMATION_RATIO),
						x1, y1, x2, y2,
						false,
						data);
				if (data.crossings==MathConstants.SHAPE_INTERSECTS) {
					return;
				}
				curx = endx;
				cury = endy;
				break;
			}
			case CURVE_TO:
				endx = element.toX;
				endy = element.toY;
				Path2f localPath = new Path2f();
				localPath.moveTo(curx, cury);
				localPath.curveTo(
						element.ctrlX1, element.ctrlY1,
						element.ctrlX2, element.ctrlY2,
						endx, endy);
				computeCrossings1(
						localPath.getPathIterator((float) MathConstants.SPLINE_APPROXIMATION_RATIO),
						x1, y1, x2, y2,
						false,
						data);
				if (data.crossings==MathConstants.SHAPE_INTERSECTS) {
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
				if (data.crossings!=0)	return;
				curx = movx;
				cury = movy;
				break;
			default:
			}
		}

		assert(data.crossings!=MathConstants.SHAPE_INTERSECTS);

		boolean isOpen = (curx != movx) || (cury != movy);

		if (isOpen) {
			if (closeable) {
				computeCrossings2(
						curx, cury,
						movx, movy,
						x1, y1, x2, y2,
						data);
			}
			else {
				// Assume that when is the path is open, only
				// SHAPE_INTERSECTS may be return
				data.crossings = 0;
			}
		}
	}

	private static void computeCrossings2(
			float shadow_x0, float shadow_y0,
			float shadow_x1, float shadow_y1,
			float sx0, float sy0,
			float sx1, float sy1,
			PathShadowData data) {
		float shadow_xmin = Math.min(shadow_x0, shadow_x1);
		float shadow_xmax = Math.max(shadow_x0, shadow_x1);
		float shadow_ymin = Math.min(shadow_y0, shadow_y1);
		float shadow_ymax = Math.max(shadow_y0, shadow_y1);

		data.updateShadowLimits(shadow_x0, shadow_y0, shadow_x1, shadow_y1);

		if (sy0<=shadow_ymin && sy1<=shadow_ymin) return;
		if (sy0>=shadow_ymax && sy1>=shadow_ymax) return;
		if (sx0<=shadow_xmin && sx1<=shadow_xmin) return;
		if (sx0>=shadow_xmax && sx1>=shadow_xmax) {
			float xintercept;
			// The line is entirely at the right of the shadow
			float alpha = (sx1 - sx0) / (sy1 - sy0);
			if (sy0<sy1) {
				if (sy0<=shadow_ymin) {
					xintercept = sx0 + (shadow_ymin - sy0) * alpha;
					data.setCrossingForYMin(xintercept, shadow_ymin);
					++data.crossings;
				}
				if (sy1>=shadow_ymax) {
					xintercept = sx0 + (shadow_ymax - sy0) * alpha;
					data.setCrossingForYMax(xintercept, shadow_ymax);
					++data.crossings;
				}
			}
			else {
				if (sy1<=shadow_ymin) {
					xintercept = sx0 + (shadow_ymin - sy0) * alpha;
					data.setCrossingForYMin(xintercept, shadow_ymin);
					--data.crossings;
				}
				if (sy0>=shadow_ymax) {
					xintercept = sx0 + (shadow_ymax - sy0) * alpha;
					data.setCrossingForYMax(xintercept, shadow_ymax);
					--data.crossings;
				}
			}
		}
		else if (Segment2f.intersectsSegmentSegmentWithoutEnds(
				shadow_x0, shadow_y0, shadow_x1, shadow_y1,
				sx0, sy0, sx1, sy1)) {
			data.crossings = MathConstants.SHAPE_INTERSECTS;
		}
		else {
			int side1, side2;
			boolean isUp = (shadow_y0<=shadow_y1);
			if (isUp) {
				side1 = Segment2afp.computeSideLinePoint(
						shadow_x0, shadow_y0,
						shadow_x1, shadow_y1,
						sx0, sy0, 0);
				side2 = Segment2afp.computeSideLinePoint(
						shadow_x0, shadow_y0,
						shadow_x1, shadow_y1,
						sx1, sy1, 0);
			}
			else {
				side1 = Segment2afp.computeSideLinePoint(
						shadow_x1, shadow_y1,
						shadow_x0, shadow_y0,
						sx0, sy0, 0);
				side2 = Segment2afp.computeSideLinePoint(
						shadow_x1, shadow_y1,
						shadow_x0, shadow_y0,
						sx1, sy1, 0);
			}
			if (side1>0 || side2>0) {
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
			float shadowx, float shadowy,
			float sx0, float sy0,
			float sx1, float sy1,
			PathShadowData data,
			boolean isUp) {
		if (shadowy <  sy0 && shadowy <  sy1) return;
		if (shadowy > sy0 && shadowy > sy1) return;
		if (shadowx > sx0 && shadowx > sx1) return;
		float xintercept = sx0 + (shadowy - sy0) * (sx1 - sx0) / (sy1 - sy0);
		if (shadowx > xintercept) return;
		if (isUp) {
			data.setCrossingForYMax(xintercept, shadowy);
		}
		else {
			data.setCrossingForYMin(xintercept, shadowy);
		}
		data.crossings += (sy0 < sy1) ? 1 : -1;
	}

	/** 
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class PathShadowData {

		public int crossings = 0;
		public boolean hasX4ymin = false;
		public boolean hasX4ymax = false;
		public float x4ymin;
		public float x4ymax;
		public float xmin4ymin;
		public float xmin4ymax;
		public float ymin;
		public float ymax;

		@Override
		public String toString() {
			StringBuilder b = new StringBuilder();
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
			}
			else {
				b.append("none\n"); //$NON-NLS-1$
			}
			b.append("\thigh: "); //$NON-NLS-1$
			if (this.hasX4ymax) {
				b.append("( "); //$NON-NLS-1$
				b.append(this.x4ymax);
				b.append(" | "); //$NON-NLS-1$
				b.append(this.ymax);
				b.append(" )\n"); //$NON-NLS-1$
			}
			else {
				b.append("none\n"); //$NON-NLS-1$
			}
			b.append("}\n"); //$NON-NLS-1$
			return b.toString();
		}

		public PathShadowData(float xmax, float miny, float maxy) {
			this.x4ymin = this.x4ymax = xmax;
			this.xmin4ymax = this.xmin4ymin = xmax;
			this.ymin = miny;
			this.ymax = maxy;
		}

		public void setCrossingForYMax(float x, float y) {
			if (y>=this.ymax) {
				if (x<this.x4ymax) {
					this.x4ymax = x;
					this.hasX4ymax = true;
				}
			}
		}

		public void setCrossingForYMin(float x, float y) {
			if (y<=this.ymin) {
				if (x<this.x4ymin) {
					this.x4ymin = x;
					this.hasX4ymin = true;
				}
			}
		}

		public void updateShadowLimits(float shadow_x0, float shadow_y0, float shadow_x1, float shadow_y1) {
			float xl, yl;
			float xh, yh;
			if (shadow_y0<shadow_y1) {
				xl = shadow_x0;
				yl = shadow_y0;
				xh = shadow_x1;
				yh = shadow_y1;
			}
			else if (shadow_y1<shadow_y0) {
				xl = shadow_x1;
				yl = shadow_y1;
				xh = shadow_x0;
				yh = shadow_y0;
			}
			else {
				xl = xh = Math.min(shadow_x0, shadow_x1);
				yl = yh = shadow_y0;
			}

			if (yl<=this.ymin && xl<this.xmin4ymin) {
				this.xmin4ymin = xl;
			}

			if (yh>=this.ymax && xh<this.xmin4ymax) {
				this.xmin4ymax = xh;
			}
		}

	}

}