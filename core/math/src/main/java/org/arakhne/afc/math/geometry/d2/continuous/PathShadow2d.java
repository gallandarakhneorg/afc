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
package org.arakhne.afc.math.geometry.d2.continuous;

import java.util.Iterator;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Path2D;
import org.eclipse.xtext.xbase.lib.Pure;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

/** Shadow of a path.
*
* @author $Author: galland$
* @author $Author: hjaffali$
* @version $FullVersion$
* @mavengroupid $GroupId$
* @mavenartifactid $ArtifactId$
*/
public class PathShadow2d {
	
	private final Path2D<?,Rectangle2d,AbstractPathElement2D,PathIterator2d> path;
	private final Rectangle2d bounds;

	/**
	 * @param path1
	 */
	public PathShadow2d(Path2d path1) {
		this.path = path1;
		this.bounds = new Rectangle2d(path1.toBoundingBox());
	}
	
	/**
	 * @param path1
	 */
	public PathShadow2d(Path2f path1) {
		this.path = new Path2d(path1);
		this.bounds = new Rectangle2d(path1.toBoundingBox());
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
		if (this.bounds==null) return crossings;

		int numCrosses = 
				AbstractSegment2F.computeCrossingsFromRect(crossings,
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
					this.path.getPathIteratorProperty(MathConstants.SPLINE_APPROXIMATION_RATIO),
					x0, y0, x1, y1,
					false,
					data);
			numCrosses = data.crossingsProperty.get();

			int mask = (this.path.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
			if (numCrosses == MathConstants.SHAPE_INTERSECTS ||
					(numCrosses & mask) != 0) {
				// The given line is intersecting the path shape
				return MathConstants.SHAPE_INTERSECTS;
			}

			// There is no intersection with the shadow path's shape.
			int inc = 0;
			if (data.hasX4yminProperty.get() && 
					data.x4yminProperty.get()>=data.xmin4yminProperty.get()) {
				++inc;
			}
			if (data.hasX4ymaxProperty.get() && 
					data.x4ymaxProperty.get()>=data.xmin4ymaxProperty.get()) {
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
			Iterator<AbstractPathElement2D> pi, 
			double x1, double y1, double x2, double y2, 
			boolean closeable, PathShadowData data) {	
		if (!pi.hasNext() || data.crossingsProperty.get()==MathConstants.SHAPE_INTERSECTS) return;
		AbstractPathElement2D element;

		element = pi.next();
		if (element.type != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		double movx = element.getToX();
		double movy = element.getToY();
		double curx = movx;
		double cury = movy;
		double endx, endy;
		while (data.crossingsProperty.get()!=MathConstants.SHAPE_INTERSECTS && pi.hasNext()) {
			element = pi.next();
			switch (element.type) {
			case MOVE_TO:
				movx = curx = element.getToX();
				movy = cury = element.getToY();
				break;
			case LINE_TO:
				endx = element.getToX();
				endy = element.getToY();
				computeCrossings2(
						curx, cury,
						endx, endy,
						x1, y1, x2, y2,
						data);
				if (data.crossingsProperty.get()==MathConstants.SHAPE_INTERSECTS) {
					return;
				}
				curx = endx;
				cury = endy;
				break;
			case QUAD_TO:
			{
				endx = element.getToX();
				endy = element.getToY();
				Path2d localPath = new Path2d();
				localPath.moveTo(curx, cury);
				localPath.quadTo(
						element.getCtrlX1(), element.getCtrlY1(),
						endx, endy);
				computeCrossings1(
						localPath.getPathIteratorProperty(MathConstants.SPLINE_APPROXIMATION_RATIO),
						x1, y1, x2, y2,
						false,
						data);
				if (data.crossingsProperty.get()==MathConstants.SHAPE_INTERSECTS) {
					return;
				}
				curx = endx;
				cury = endy;
				break;
			}
			case CURVE_TO:
				endx = element.getToX();
				endy = element.getToY();
				Path2d localPath = new Path2d();
				localPath.moveTo(curx, cury);
				localPath.curveTo(
						element.getCtrlX1(), element.getCtrlY1(),
						element.getCtrlX2(), element.getCtrlY2(),
						endx, endy);
				computeCrossings1(
						localPath.getPathIteratorProperty(MathConstants.SPLINE_APPROXIMATION_RATIO),
						x1, y1, x2, y2,
						false,
						data);
				if (data.crossingsProperty.get()==MathConstants.SHAPE_INTERSECTS) {
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
				if (data.crossingsProperty.get()!=0)	return;
				curx = movx;
				cury = movy;
				break;
			default:
			}
		}

		assert(data.crossingsProperty.get()!=MathConstants.SHAPE_INTERSECTS);

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
				data.crossingsProperty.set(0);
			}
		}
	}

	private static void computeCrossings2(
			double shadow_x0, double shadow_y0,
			double shadow_x1, double shadow_y1,
			double sx0, double sy0,
			double sx1, double sy1,
			PathShadowData data) {
		double shadow_xmin = Math.min(shadow_x0, shadow_x1);
		double shadow_xmax = Math.max(shadow_x0, shadow_x1);
		double shadow_ymin = Math.min(shadow_y0, shadow_y1);
		double shadow_ymax = Math.max(shadow_y0, shadow_y1);

		data.updateShadowLimits(shadow_x0, shadow_y0, shadow_x1, shadow_y1);

		if (sy0<=shadow_ymin && sy1<=shadow_ymin) return;
		if (sy0>=shadow_ymax && sy1>=shadow_ymax) return;
		if (sx0<=shadow_xmin && sx1<=shadow_xmin) return;
		if (sx0>=shadow_xmax && sx1>=shadow_xmax) {
			double xintercept;
			// The line is entirely at the right of the shadow
			double alpha = (sx1 - sx0) / (sy1 - sy0);
			if (sy0<sy1) {
				if (sy0<=shadow_ymin) {
					xintercept = sx0 + (shadow_ymin - sy0) * alpha;
					data.setCrossingForYMin(xintercept, shadow_ymin);
					data.crossingsProperty.set(data.crossingsProperty.get()+1);
				}
				if (sy1>=shadow_ymax) {
					xintercept = sx0 + (shadow_ymax - sy0) * alpha;
					data.setCrossingForYMax(xintercept, shadow_ymax);
					data.crossingsProperty.set(data.crossingsProperty.get()+1);
				}
			}
			else {
				if (sy1<=shadow_ymin) {
					xintercept = sx0 + (shadow_ymin - sy0) * alpha;
					data.setCrossingForYMin(xintercept, shadow_ymin);
					data.crossingsProperty.set(data.crossingsProperty.get()-1);
				}
				if (sy0>=shadow_ymax) {
					xintercept = sx0 + (shadow_ymax - sy0) * alpha;
					data.setCrossingForYMax(xintercept, shadow_ymax);
					data.crossingsProperty.set(data.crossingsProperty.get()-1);
				}
			}
		}
		else if (AbstractSegment2F.intersectsSegmentSegmentWithoutEnds(
				shadow_x0, shadow_y0, shadow_x1, shadow_y1,
				sx0, sy0, sx1, sy1)) {
			data.crossingsProperty.set(MathConstants.SHAPE_INTERSECTS);
		}
		else {
			int side1, side2;
			boolean isUp = (shadow_y0<=shadow_y1);
			if (isUp) {
				side1 = AbstractSegment2F.computeSideLinePoint(
						shadow_x0, shadow_y0,
						shadow_x1, shadow_y1,
						sx0, sy0, 0.);
				side2 = AbstractSegment2F.computeSideLinePoint(
						shadow_x0, shadow_y0,
						shadow_x1, shadow_y1,
						sx1, sy1, 0.);
			}
			else {
				side1 = AbstractSegment2F.computeSideLinePoint(
						shadow_x1, shadow_y1,
						shadow_x0, shadow_y0,
						sx0, sy0, 0.);
				side2 = AbstractSegment2F.computeSideLinePoint(
						shadow_x1, shadow_y1,
						shadow_x0, shadow_y0,
						sx1, sy1, 0.);
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
			double shadowx, double shadowy,
			double sx0, double sy0,
			double sx1, double sy1,
			PathShadowData data,
			boolean isUp) {
		if (shadowy <  sy0 && shadowy <  sy1) return;
		if (shadowy > sy0 && shadowy > sy1) return;
		if (shadowx > sx0 && shadowx > sx1) return;
		double xintercept = sx0 + (shadowy - sy0) * (sx1 - sx0) / (sy1 - sy0);
		if (shadowx > xintercept) return;
		if (isUp) {
			data.setCrossingForYMax(xintercept, shadowy);
		}
		else {
			data.setCrossingForYMin(xintercept, shadowy);
		}
		data.crossingsProperty.set(data.crossingsProperty.get() + ((sy0 < sy1) ? 1 : -1));
	}

	/** 
	 * @author $Author: galland$
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class PathShadowData {

		public IntegerProperty crossingsProperty = new SimpleIntegerProperty(0);
		public BooleanProperty hasX4yminProperty = new SimpleBooleanProperty(false);
		public BooleanProperty hasX4ymaxProperty = new SimpleBooleanProperty(false);
		public DoubleProperty x4yminProperty;
		public DoubleProperty x4ymaxProperty;
		public DoubleProperty xmin4yminProperty;
		public DoubleProperty xmin4ymaxProperty;
		public DoubleProperty yminProperty;
		public DoubleProperty ymaxProperty;

		@Pure
		@Override
		public String toString() {
			StringBuilder b = new StringBuilder();
			b.append("SHADOW {\n\tlow: ( "); //$NON-NLS-1$
			b.append(this.xmin4yminProperty.doubleValue());
			b.append(" | "); //$NON-NLS-1$
			b.append(this.yminProperty.doubleValue());
			b.append(" )\n\thigh: ( "); //$NON-NLS-1$
			b.append(this.xmin4ymaxProperty.doubleValue());
			b.append(" | "); //$NON-NLS-1$
			b.append(this.ymaxProperty.doubleValue());
			b.append(")\n}\nCROSSINGS {\n\tcrossings="); //$NON-NLS-1$
			b.append(this.crossingsProperty.intValue());
			b.append("\n\tlow: "); //$NON-NLS-1$
			if (this.hasX4yminProperty.get()) {
				b.append("( "); //$NON-NLS-1$
				b.append(this.x4yminProperty.doubleValue());
				b.append(" | "); //$NON-NLS-1$
				b.append(this.yminProperty.doubleValue());
				b.append(" )\n"); //$NON-NLS-1$
			}
			else {
				b.append("none\n"); //$NON-NLS-1$
			}
			b.append("\thigh: "); //$NON-NLS-1$
			if (this.hasX4ymaxProperty.get()) {
				b.append("( "); //$NON-NLS-1$
				b.append(this.x4ymaxProperty.doubleValue());
				b.append(" | "); //$NON-NLS-1$
				b.append(this.ymaxProperty.doubleValue());
				b.append(" )\n"); //$NON-NLS-1$
			}
			else {
				b.append("none\n"); //$NON-NLS-1$
			}
			b.append("}\n"); //$NON-NLS-1$
			return b.toString();
		}

		public PathShadowData(double xmax, double miny, double maxy) {
			this.x4yminProperty = new SimpleDoubleProperty(xmax);
			this.x4ymaxProperty = new SimpleDoubleProperty(xmax);
			this.xmin4ymaxProperty = new SimpleDoubleProperty(xmax); 
			this.xmin4yminProperty = new SimpleDoubleProperty(xmax);
			this.yminProperty = new SimpleDoubleProperty(miny);
			this.ymaxProperty = new SimpleDoubleProperty(maxy);
		}

		public void setCrossingForYMax(double x, double y) {
			if (y>=this.ymaxProperty.get()) {
				if (x<this.x4ymaxProperty.get()) {
					this.x4ymaxProperty.set(x);
					this.hasX4ymaxProperty.set(true);
				}
			}
		}

		public void setCrossingForYMin(double x, double y) {
			if (y<=this.yminProperty.get()) {
				if (x<this.x4yminProperty.get()) {
					this.x4yminProperty.set(x);
					this.hasX4yminProperty.set(true);
				}
			}
		}

		public void updateShadowLimits(double shadow_x0, double shadow_y0, double shadow_x1, double shadow_y1) {
			double xl, yl;
			double xh, yh;
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

			if (yl<=this.yminProperty.get() && xl<this.xmin4yminProperty.get()) {
				this.xmin4yminProperty.set(xl);
			}

			if (yh>=this.ymaxProperty.get() && xh<this.xmin4ymaxProperty.get()) {
				this.xmin4ymaxProperty.set(xh);
			}
		}

	}


}
