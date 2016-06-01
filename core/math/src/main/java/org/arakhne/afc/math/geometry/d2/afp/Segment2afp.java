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

import java.util.NoSuchElementException;

import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.Unefficient;
import org.arakhne.afc.math.geometry.CrossingComputationType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;

/**
 * Fonctional interface that represented a 2D segment/line on a plane.
 *
 * @param <ST> is the type of the general implementation.
 * @param <IT> is the type of the implementation of this shape.
 * @param <IE> is the type of the path elements.
 * @param <P> is the type of the points.
 * @param <V> is the type of the vectors.
 * @param <B> is the type of the bounding boxes.
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("checkstyle:methodcount")
public interface Segment2afp<
        ST extends Shape2afp<?, ?, IE, P, V, B>,
        IT extends Segment2afp<?, ?, IE, P, V, B>,
        IE extends PathElement2afp,
        P extends Point2D<? super P, ? super V>,
        V extends Vector2D<? super V, ? super P>,
        B extends Rectangle2afp<?, ?, IE, P, V, B>>
        extends Shape2afp<ST, IT, IE, P, V, B> {

	/** Iterator on the path elements of the segment.
	 *
	 * @param <T> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	class SegmentPathIterator<T extends PathElement2afp> implements PathIterator2afp<T> {

		private int index;

		private final Point2D<?, ?> p1;

		private final Point2D<?, ?> p2;

		private final Segment2afp<?, ?, T, ?, ?, ?> segment;

		private final Transform2D transform;

		private final double x1;

		private final double x2;

		private final double y1;

		private final double y2;

		/**
		 * @param segment the iterated segment.
		 * @param transform the transformation, or <code>null</code>.
		 */
		public SegmentPathIterator(Segment2afp<?, ?, T, ?, ?, ?> segment, Transform2D transform) {
			assert segment != null : "Segment must be not null"; //$NON-NLS-1$
			this.segment = segment;
			this.p1 = new InnerComputationPoint2afp();
			this.p2 = new InnerComputationPoint2afp();
			this.transform = (transform == null || transform.isIdentity()) ? null : transform;
			this.x1 = segment.getX1();
			this.y1 = segment.getY1();
			this.x2 = segment.getX2();
			this.y2 = segment.getY2();
			if (this.x1 == this.x2 && this.y1 == this.y2) {
				this.index = 2;
			}
		}

		@Override
		public GeomFactory2afp<T, ?, ?, ?> getGeomFactory() {
			return this.segment.getGeomFactory();
		}

		@Pure
		@Override
		public PathWindingRule getWindingRule() {
			return PathWindingRule.NON_ZERO;
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index <= 1;
		}

		@Pure
		@Override
		public boolean isCurved() {
			return false;
		}

		@Pure
		@Override
		public boolean isMultiParts() {
			return false;
		}

		@Pure
		@Override
		public boolean isPolygon() {
			return false;
		}

		@Pure
		@Override
		public boolean isPolyline() {
			return true;
		}

		@Override
		public T next() {
			if (this.index > 1) {
				throw new NoSuchElementException();
			}
			final int idx = this.index;
			++this.index;
			switch (idx) {
			case 0:
				this.p2.set(this.x1, this.y1);
				if (this.transform != null) {
					this.transform.transform(this.p2);
				}
				return this.segment.getGeomFactory().newMovePathElement(
						this.p2.getX(), this.p2.getY());
			case 1:
				this.p1.set(this.p2);
				this.p2.set(this.x2, this.y2);
				if (this.transform != null) {
					this.transform.transform(this.p2);
				}
				return this.segment.getGeomFactory().newLinePathElement(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
			default:
				throw new NoSuchElementException();
			}
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		@Override
		public PathIterator2afp<T> restartIterations() {
			return new SegmentPathIterator<>(this.segment, this.transform);
		}

	}

	/** Result of the intersection between segments in a context where a single
	 * test is not enough.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 * @see Segment2afp#getNoSegmentSegmentWithEndsIntersection(double, double, double, double, double, double, double, double)
	 * @see Segment2afp#getNoSegmentSegmentWithoutEndsIntersection(double, double, double, double, double, double, double, double)
	 */
	enum UncertainIntersection {
		/** No intersection, certainly.
		 */
		NO {
			@Override
			public boolean booleanValue() {
				return false;
			}
		},
		/** Intersection, uncertainly.
		 */
		PERHAPS {
			@Override
			public boolean booleanValue() {
				return true;
			}
		};

		/** Replies the UncertainIntersection for the given boolean.
		 *
		 * @param value the boolean value.
		 * @return the UncertainIntersection.
		 */
		public static UncertainIntersection fromBoolean(boolean value) {
			if (value) {
				return UncertainIntersection.PERHAPS;
			}
			return UncertainIntersection.NO;
		}

		/** Replies the boolean representation of the constant.
		 *
		 * @return the boolean representation.
		 */
		public abstract boolean booleanValue();

	}

	/**
	 * Replies the relative counterclockwise (CCW) of a segment against a point. Returns an indicator of where
	 * the specified point {@code (px, py)} lies with respect to the line segment from {@code (x1, y1)}
	 *  to {@code (x2, y2)}. The return value can be either 1, -1, or 0 and indicates in which
	 *  direction the specified line must pivot around its first end point, {@code (x1, y1)}, in
	 *  order to point at the specified point {@code (px, py)}.
	 * In other words, given three point P1, P2, and P, is the segments (P1-P2-P) a counterclockwise turn?
	 *
	 * <p>In opposite to {@link #computeSideLinePoint(double, double, double, double, double, double, double)},
	 * this function tries to classifies the point if it is colinear to the segment.
	 * The classification is explained below.
	 *
	 * <p>A return value of 1 indicates that the line segment must turn in the direction that takes the
	 * positive X axis towards the negative Y axis. In the default coordinate system used by Java 2D,
	 * this direction is counterclockwise.
	 *
	 * <p>A return value of -1 indicates that the line segment must turn in the direction that takes the
	 * positive X axis towards the positive Y axis. In the default coordinate system, this
	 * direction is clockwise.
	 *
	 * <p>A return value of 0 indicates that the point lies exactly on the line segment.
	 * Note that an indicator value of 0 is rare and not useful for determining colinearity
	 * because of floating point rounding issues.
	 *
	 * <p>If the point is colinear with the line segment, but not between the end points, then the value will be
	 * -1 if the point lies "beyond {@code (x1, y1)}" or 1 if the point lies "beyond {@code (x2, y2)}".
	 *
	 * @param x1
	 *            the X coordinate of the start point of the specified line segment
	 * @param y1
	 *            the Y coordinate of the start point of the specified line segment
	 * @param x2
	 *            the X coordinate of the end point of the specified line segment
	 * @param y2
	 *            the Y coordinate of the end point of the specified line segment
	 * @param px
	 *            the X coordinate of the specified point to be compared with the specified line segment
	 * @param py
	 *            the Y coordinate of the specified point to be compared with the specified line segment
	 * @param epsilon approximation of the tests for equality to zero.
	 * @return an integer that indicates the position of the third specified coordinates with
	 *      respect to the line segment formed by the first two specified coordinates.
	 * @see #computeRelativeDistanceLinePoint(double, double, double, double, double, double)
	 * @see #computeSideLinePoint(double, double, double, double, double, double, double)
	 */
	@Pure
	static int ccw(double x1, double y1, double x2, double y2, double px, double py, double epsilon) {
		final double x21 = x2 - x1;
		final double y21 = y2 - y1;
		double xp1 = px - x1;
		double yp1 = py - y1;
		double ccw = xp1 * y21 - yp1 * x21;
		if (MathUtil.isEpsilonZero(ccw, epsilon)) {
			// The point is colinear, classify based on which side of
			// the segment the point falls on. We can calculate a
			// relative value using the projection of px, py onto the
			// segment - a negative value indicates the point projects
			// outside of the segment in the direction of the particular
			// endpoint used as the origin for the projection.
			ccw = xp1 * x21 + yp1 * y21;
			if (ccw > 0.) {
				// Reverse the projection to be relative to the original x2, y2
				// x2 and y2 are simply negated.
				// px and py need to have (x2 - x1) or (y2 - y1) subtracted
				// from them (based on the original values)
				// Since we really want to get a positive answer when the
				// point is "beyond (x2, y2)", then we want to calculate
				// the inverse anyway - thus we leave x2 & y2 negated.
				xp1 -= x21;
				yp1 -= y21;
				ccw = xp1 * x21 + yp1 * y21;
				if (ccw < 0) {
					ccw = 0.;
				}
			}
		}
		return (ccw < 0.) ? -1 : ((ccw > 0.) ? 1 : 0);
	}

	/** Replies the point on the segment that is closest to the given point.
	 *
	 * @param ax is the x coordinate of the first point of the segment.
	 * @param ay is the y coordinate of the first point of the segment.
	 * @param bx is the x coordinate of the second point of the segment.
	 * @param by is the y coordinate of the second point of the segment.
	 * @param px is the x coordinate of the point.
	 * @param py is the y coordinate of the point.
	 * @param result the is point on the shape.
	 */
	@Pure
	static void computeClosestPointToPoint(
			double ax, double ay, double bx, double by, double px, double py,
			Point2D<?, ?> result) {
		assert result != null : "Result must be not null"; //$NON-NLS-1$
		final double ratio = Segment2afp.computeProjectedPointOnLine(px, py, ax, ay, bx, by);
		if (ratio <= 0.) {
			result.set(ax, ay);
		} else if (ratio >= 1.) {
			result.set(bx, by);
		} else {
			result.set(
					ax + (bx - ax) * ratio,
					ay + (by - ay) * ratio);
		}
	}

	/** Replies the point on the segment that is closest to the rectangle.
	 *
	 * @param sx1 is the x coordinate of the first point of the segment.
	 * @param sy1 is the y coordinate of the first point of the segment.
	 * @param sx2 is the x coordinate of the second point of the segment.
	 * @param sy2 is the y coordinate of the second point of the segment.
	 * @param rx is the x coordinate of the rectangle.
	 * @param ry is the y coordinate of the rectangle.
	 * @param rwidth is the width of the rectangle.
	 * @param rheight is the height of the rectangle.
	 * @param result the is point on the segment.
	 */
	@Pure
	@SuppressWarnings("checkstyle:parameternumber")
	static void computeClosestPointToRectangle(double sx1, double sy1, double sx2, double sy2,
			double rx, double ry, double rwidth, double rheight, Point2D<?, ?> result) {
		assert rwidth >= 0. : "Width of the rectangle must be positive or zero"; //$NON-NLS-1$
		assert rheight >= 0. : "Hei of the rectangle must be positive or zero"; //$NON-NLS-1$
		final double rmaxx = rx + rwidth;
		final double rmaxy = ry + rheight;
		final int code1 = MathUtil.getCohenSutherlandCode(sx1, sy1, rx, ry, rmaxx, rmaxy);
		final int code2 = MathUtil.getCohenSutherlandCode(sx2, sy2, rx, ry, rmaxx, rmaxy);
		final Point2D<?, ?> tmp1 = new InnerComputationPoint2afp();
		final Point2D<?, ?> tmp2 = new InnerComputationPoint2afp();
		final int zone = Rectangle2afp.reduceCohenSutherlandZoneRectangleSegment(
				rx, ry, rmaxx, rmaxy,
				sx1, sy1, sx2, sy2,
				code1, code2,
				tmp1, tmp2);
		if ((zone & MathConstants.COHEN_SUTHERLAND_LEFT) != 0) {
			computeClosestPointToSegment(
					sx1, sy1, sx2, sy2,
					rx, ry, rx, rmaxy, result);
		} else if ((zone & MathConstants.COHEN_SUTHERLAND_RIGHT) != 0) {
			computeClosestPointToSegment(
					sx1, sy1, sx2, sy2,
					rmaxx, ry, rmaxx, rmaxy, result);
		} else if ((zone & MathConstants.COHEN_SUTHERLAND_BOTTOM) != 0) {
			computeClosestPointToSegment(
					sx1, sy1, sx2, sy2,
					rx, ry, rmaxx, ry, result);
		} else if ((zone & MathConstants.COHEN_SUTHERLAND_TOP) != 0) {
			computeClosestPointToSegment(
					sx1, sy1, sx2, sy2,
					rx, rmaxy, rmaxx, rmaxy, result);
		} else {
			computeClosestPointToPoint(
					tmp1.getX(), tmp1.getY(), tmp2.getX(), tmp2.getY(),
					(rx + rmaxx) / 2., (ry + rmaxy) / 2., result);
		}
	}

	/** Replies the point on the first segment that is closest to the second segment.
	 *
	 * @param s1x1 is the x coordinate of the first point of the first segment.
	 * @param s1y1 is the y coordinate of the first point of the first segment.
	 * @param s1x2 is the x coordinate of the second point of the first segment.
	 * @param s1y2 is the y coordinate of the second point of the first segment.
	 * @param s2x1 is the x coordinate of the first point of the second segment.
	 * @param s2y1 is the y coordinate of the first point of the second segment.
	 * @param s2x2 is the x coordinate of the second point of the second segment.
	 * @param s2y2 is the y coordinate of the second point of the second segment.
	 * @param result the is point on the shape.
	 * @return the square distance between the segments.
	 */
	@Pure
	@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:npathcomplexity"})
	static double computeClosestPointToSegment(
			double s1x1, double s1y1, double s1x2, double s1y2,
			double s2x1, double s2y1, double s2x2, double s2y2,
			Point2D<?, ?> result) {
	    final double ux = s1x2 - s1x1;
	    final double uy = s1y2 - s1y1;
	    final double vx = s2x2 - s2x1;
	    final double vy = s2y2 - s2y1;
	    final double wx = s1x1 - s2x1;
	    final double wy = s1y1 - s2y1;
	    final double a = Vector2D.dotProduct(ux, uy, ux, uy);
	    final double b = Vector2D.dotProduct(ux, uy, vx, vy);
	    final double c = Vector2D.dotProduct(vx, vy, vx, vy);
	    final double d = Vector2D.dotProduct(ux, uy, wx, wy);
	    final double e = Vector2D.dotProduct(vx, vy, wx, wy);
	    final double bigD = a * c - b * b;
	    double svD = bigD;
	    double tvD = bigD;
	    double svN;
	    double tvN;
		// compute the line parameters of the two closest points
		if (MathUtil.isEpsilonZero(bigD)) {
			// the lines are almost parallel
			// force using point P0 on segment S1
			svN = 0.;
			// to prevent possible division by 0.0 later
			svD = 1.;
			tvN = e;
			tvD = c;
		} else {
			// get the closest points on the infinite lines
			svN = b * e - c * d;
			tvN = a * e - b * d;
			if (svN < 0.) {
				// sc < 0 => the s=0 edge is visible
				svN = 0.;
				tvN = e;
				tvD = c;
			} else if (svN > svD) {
				// sc > 1  => the s=1 edge is visible
				svN = svD;
				tvN = e + b;
				tvD = c;
			}
		}

		if (tvN < 0.) {
			// tc < 0 => the t=0 edge is visible
			tvN = 0.;
			// recompute sc for this edge
			if (-d < 0.) {
				svN = 0.0;
			} else if (-d > a) {
				svN = svD;
			} else {
				svN = -d;
				svD = a;
			}
		} else if (tvN > tvD) {
			// tc > 1  => the t=1 edge is visible
			tvN = tvD;
			// recompute sc for this edge
			if ((-d + b) < 0.) {
				svN = 0;
			} else if ((-d + b) > a) {
				svN = svD;
			} else {
				svN = -d +  b;
				svD = a;
			}
		}

		// finally do the division to get sc and tc
		final double sc = MathUtil.isEpsilonZero(svN) ? 0. : (svN / svD);
		final double tc = MathUtil.isEpsilonZero(tvN) ? 0. : (tvN / tvD);

		// get the difference of the two closest points
		// =  S1(sc) - S2(tc)
		final double dPx = wx + (sc * ux) - (tc * vx);
		final double dPy = wy + (sc * uy) - (tc * vy);

		if (result != null) {
			result.set(s1x1 + sc * ux, s1y1 + sc * uy);
		}

		return dPx * dPx + dPy * dPy;
	}

	/**
	 * Calculates the number of times the line from (x0, y0) to (x1, y1)
	 * crosses the circle (cx, cy) with radius extending to the right.
	 *
	 * @param crossings is the initial value for the number of crossings.
	 * @param cx is the center of the circle to extend.
	 * @param cy is the center of the circle to extend.
	 * @param radius is the radius of the circle to extend.
	 * @param x0 is the first point of the line.
	 * @param y0 is the first point of the line.
	 * @param x1 is the second point of the line.
	 * @param y1 is the secondpoint of the line.
	 * @return the crossing, or {@link MathConstants#SHAPE_INTERSECTS}.
	 */
	@Pure
	static int computeCrossingsFromCircle(
			int crossings,
			double cx, double cy,
			double radius,
			double x0, double y0,
			double x1, double y1) {
		assert radius >= 0. : "Circle radius must be positive or zero"; //$NON-NLS-1$
		int numCrosses = crossings;

		final double xmin = cx - Math.abs(radius);
		final double ymin = cy - Math.abs(radius);
		final double ymax = cy + Math.abs(radius);

		if (y0 <= ymin && y1 <= ymin) {
			return numCrosses;
		}
		if (y0 >= ymax && y1 >= ymax) {
			return numCrosses;
		}
		if (x0 <= xmin && x1 <= xmin) {
			return numCrosses;
		}

		if (x0 >= cx + radius && x1 >= cx + radius) {
			// The line is entirely at the right of the shadow
			if (y0 < y1) {
				if (y0 <= ymin) {
					++numCrosses;
				}
				if (y1 >= ymax) {
					++numCrosses;
				}
			} else {
				if (y1 <= ymin) {
					--numCrosses;
				}
				if (y0 >= ymax) {
					--numCrosses;
				}
			}
		} else if (Circle2afp.intersectsCircleSegment(
				cx, cy, radius,
				x0, y0, x1, y1)) {
			return MathConstants.SHAPE_INTERSECTS;
		} else {
			numCrosses += computeCrossingsFromPoint(cx, ymin, x0, y0, x1, y1);
			numCrosses += computeCrossingsFromPoint(cx, ymax, x0, y0, x1, y1);
		}

		return numCrosses;
	}

	/**
	 * Calculates the number of times the line from (x0, y0) to (x1, y1)
	 * crosses the ellipse (ex0, ey0) to (ex1, ey1) extending to the right.
	 *
	 * @param crossings is the initial value for the number of crossings.
	 * @param ex is the first corner of the ellipse to extend.
	 * @param ey is the first corner of the ellipse to extend.
	 * @param ew is the width of the ellipse to extend.
	 * @param eh is the height of the ellipse  to extend.
	 * @param x0 is the first point of the line.
	 * @param y0 is the first point of the line.
	 * @param x1 is the second point of the line.
	 * @param y1 is the secondpoint of the line.
	 * @return the crossing, or {@link MathConstants#SHAPE_INTERSECTS}.
	 */
	@Pure
	@SuppressWarnings("checkstyle:parameternumber")
	static int computeCrossingsFromEllipse(
			int crossings,
			double ex, double ey,
			double ew, double eh,
			double x0, double y0,
			double x1, double y1) {
		assert ew >= 0. : "Ellipse width must be positive or zero"; //$NON-NLS-1$
		assert eh >= 0 : "Ellipse height must be positive or zero"; //$NON-NLS-1$
		int numCrosses = crossings;

		final double xmin = ex;
		final double ymin = ey;
		final double xmax = ex + ew;
		final double ymax = ey + eh;

		if (y0 <= ymin && y1 <= ymin) {
			return numCrosses;
		}
		if (y0 >= ymax && y1 >= ymax) {
			return numCrosses;
		}
		if (x0 <= xmin && x1 <= xmin) {
			return numCrosses;
		}

		if (x0 >= xmax && x1 >= xmax) {
			// The line is entirely at the right of the shadow
			if (y0 < y1) {
				if (y0 <= ymin) {
					++numCrosses;
				}
				if (y1 >= ymax) {
					++numCrosses;
				}
			} else {
				if (y1 <= ymin) {
					--numCrosses;
				}
				if (y0 >= ymax) {
					--numCrosses;
				}
			}
		} else if (Ellipse2afp.intersectsEllipseSegment(
				xmin, ymin, xmax - xmin, ymax - ymin,
				x0, y0, x1, y1, true)) {
			return MathConstants.SHAPE_INTERSECTS;
		} else {
			final double xcenter = (xmin + xmax) / 2.;
			numCrosses += computeCrossingsFromPoint(xcenter, ymin, x0, y0, x1, y1);
			numCrosses += computeCrossingsFromPoint(xcenter, ymax, x0, y0, x1, y1);
		}

		return numCrosses;
	}

	/**
	 * Calculates the number of times the line from (x0, y0) to (x1, y1)
	 * crosses the ray extending to the right from (px, py).
	 * If the point lies on the line, then no crossings are recorded.
	 * +1 is returned for a crossing where the Y coordinate is increasing
	 * -1 is returned for a crossing where the Y coordinate is decreasing
	 *
	 * <p>This function differs from {@link #computeCrossingsFromPointWithoutEquality(double,
	 * double, double, double, double, double)}.
	 * The equality test is used in this function.
	 *
	 * @param px is the reference point to test.
	 * @param py is the reference point to test.
	 * @param x0 is the first point of the line.
	 * @param y0 is the first point of the line.
	 * @param x1 is the second point of the line.
	 * @param y1 is the secondpoint of the line.
	 * @return the crossing.
	 */
	@Pure
	static int computeCrossingsFromPoint(
			double px, double py,
			double x0, double y0,
			double x1, double y1) {
		// Copied from AWT API
		if (py < y0 && py < y1) {
			return 0;
		}
		if (py >= y0 && py >= y1) {
			return 0;
		}
		// assert y0 != y1;
		if (px >= x0 && px >= x1) {
			return 0;
		}
		if (px < x0 && px < x1) {
			return (y0 < y1) ? 1 : -1;
		}
		final double xintercept = x0 + (py - y0) * (x1 - x0) / (y1 - y0);
		if (px >= xintercept) {
			return 0;
		}
		return (y0 < y1) ? 1 : -1;
	}

	/**
	 * Calculates the number of times the line from (x0, y0) to (x1, y1)
	 * crosses the ray extending to the right from (px, py).
	 * If the point lies on the line, then no crossings are recorded.
	 * +1 is returned for a crossing where the Y coordinate is increasing
	 * -1 is returned for a crossing where the Y coordinate is decreasing
	 *
	 * <p>This function differs from {@link #computeCrossingsFromPoint(double, double, double, double, double, double)}.
	 * The equality test is not used in this function.
	 *
	 * @param px is the reference point to test.
	 * @param py is the reference point to test.
	 * @param x0 is the first point of the line.
	 * @param y0 is the first point of the line.
	 * @param x1 is the second point of the line.
	 * @param y1 is the secondpoint of the line.
	 * @return the crossing.
	 */
	@Pure
	static int computeCrossingsFromPointWithoutEquality(
			double px, double py,
			double x0, double y0,
			double x1, double y1) {
		// Copied from AWT API
		if (py < y0 && py < y1) {
			return 0;
		}
		if (py > y0 && py > y1) {
			return 0;
		}
		// assert y0 != y1;
		if (px > x0 && px > x1) {
			return 0;
		}
		if (px < x0 && px < x1) {
			return (y0 < y1) ? 1 : -1;
		}
		final double xintercept = x0 + (py - y0) * (x1 - x0) / (y1 - y0);
		if (px > xintercept) {
			return 0;
		}
		return (y0 < y1) ? 1 : -1;
	}

	/**
	 * Accumulate the number of times the line crosses the shadow
	 * extending to the right of the rectangle.  See the comment
	 * for the {@link MathConstants#SHAPE_INTERSECTS} constant for more complete details.
	 *
	 * @param crossings is the initial value for the number of crossings.
	 * @param rxmin is the first corner of the rectangle.
	 * @param rymin is the first corner of the rectangle.
	 * @param rxmax is the second corner of the rectangle.
	 * @param rymax is the second corner of the rectangle.
	 * @param x0 is the first point of the line.
	 * @param y0 is the first point of the line.
	 * @param x1 is the second point of the line.
	 * @param y1 is the secondpoint of the line.
	 * @return the crossing, or {@link MathConstants#SHAPE_INTERSECTS}.
	 */
	@Pure
	@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:cyclomaticcomplexity",
			"checkstyle:npathcomplexity", "checkstyle:returncount",
			"checkstyle:booleanexpressioncomplexity"})
	static int computeCrossingsFromRect(
			int crossings,
			double rxmin, double rymin,
			double rxmax, double rymax,
			double x0, double y0,
			double x1, double y1) {
		assert rxmin <= rxmax : "rxmin must be lower or equal to rxmax"; //$NON-NLS-1$
		assert rymin <= rymax : "rymin must be lower or equal to rymax"; //$NON-NLS-1$
		int numCrosses = crossings;

		if (y0 >= rymax && y1 >= rymax) {
			return numCrosses;
		}
		if (y0 <= rymin && y1 <= rymin) {
			return numCrosses;
		}
		if (x0 <= rxmin && x1 <= rxmin) {
			return numCrosses;
		}
		if (x0 >= rxmax && x1 >= rxmax) {
			// Line is entirely to the right of the rect
			// and the vertical ranges of the two overlap by a non-empty amount
			// Thus, this line segment is partially in the "right-shadow"
			// Path may have done a complete crossing
			// Or path may have entered or exited the right-shadow
			if (y0 < y1) {
				// y-increasing line segment...
				// We know that y0 < rymax and y1 > rymin
				if (y0 <= rymin) {
					++numCrosses;
				}
				if (y1 >= rymax) {
					++numCrosses;
				}
			} else if (y1 < y0) {
				// y-decreasing line segment...
				// We know that y1 < rymax and y0 > rymin
				if (y1 <= rymin) {
					--numCrosses;
				}
				if (y0 >= rymax) {
					--numCrosses;
				}
			}
			return numCrosses;
		}
		// Remaining case:
		// Both x and y ranges overlap by a non-empty amount
		// First do trivial INTERSECTS rejection of the cases
		// where one of the endpoints is inside the rectangle.
		if ((x0 > rxmin && x0 < rxmax && y0 > rymin && y0 < rymax)
				|| (x1 > rxmin && x1 < rxmax && y1 > rymin && y1 < rymax)) {
			return MathConstants.SHAPE_INTERSECTS;
		}
		// Otherwise calculate the y intercepts and see where
		// they fall with respect to the rectangle
		double xi0 = x0;
		if (y0 < rymin) {
			xi0 += (rymin - y0) * (x1 - x0) / (y1 - y0);
		} else if (y0 > rymax) {
			xi0 += (rymax - y0) * (x1 - x0) / (y1 - y0);
		}
		double xi1 = x1;
		if (y1 < rymin) {
			xi1 += (rymin - y1) * (x0 - x1) / (y0 - y1);
		} else if (y1 > rymax) {
			xi1 += (rymax - y1) * (x0 - x1) / (y0 - y1);
		}
		if (xi0 <= rxmin && xi1 <= rxmin) {
			return numCrosses;
		}
		if (xi0 >= rxmax && xi1 >= rxmax) {
			if (y0 < y1) {
				// y-increasing line segment...
				// We know that y0 < rymax and y1 > rymin
				if (y0 <= rymin) {
					++numCrosses;
				}
				if (y1 >= rymax) {
					++numCrosses;
				}
			} else if (y1 < y0) {
				// y-decreasing line segment...
				// We know that y1 < rymax and y0 > rymin
				if (y1 <= rymin) {
					--numCrosses;
				}
				if (y0 >= rymax) {
					--numCrosses;
				}
			}
			return numCrosses;
		}
		return MathConstants.SHAPE_INTERSECTS;
	}

	/**
	 * Accumulate the number of times the line crosses the shadow
	 * extending to the right of the round rectangle.  See the comment
	 * for the {@link MathConstants#SHAPE_INTERSECTS} constant for more complete details.
	 *
	 * @param crossings is the initial value for the number of crossings.
	 * @param rxmin is the first corner of the rectangle.
	 * @param rymin is the first corner of the rectangle.
	 * @param rxmax is the second corner of the rectangle.
	 * @param rymax is the second corner of the rectangle.
	 * @param arcWidth is the width of the rectangle arcs.
	 * @param arcHeight is the height of the rectangle arcs.
	 * @param x0 is the first point of the line.
	 * @param y0 is the first point of the line.
	 * @param x1 is the second point of the line.
	 * @param y1 is the secondpoint of the line.
	 * @return the crossing, or {@link MathConstants#SHAPE_INTERSECTS}.
	 */
	@Pure
	@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:cyclomaticcomplexity",
			"checkstyle:npathcomplexity"})
	static int computeCrossingsFromRoundRect(
			int crossings,
			double rxmin, double rymin,
			double rxmax, double rymax,
			double arcWidth, double arcHeight,
			double x0, double y0,
			double x1, double y1) {
		assert rxmin <= rxmax : "rxmin must be lower or equal to rxmax"; //$NON-NLS-1$
		assert rymin <= rymax : "rymin must be lower or equal to rymax"; //$NON-NLS-1$
		assert arcWidth >= 0. && arcWidth <= (rxmax - rxmin) / 2. : "arcWidth is too big"; //$NON-NLS-1$
		assert arcHeight >= 0. && arcHeight <= (rymax - rymin) / 2. : "arcHeight is too big"; //$NON-NLS-1$
		int numCrosses = crossings;

		if (y0 >= rymax && y1 >= rymax) {
			return numCrosses;
		}
		if (y0 <= rymin && y1 <= rymin) {
			return numCrosses;
		}
		if (x0 <= rxmin && x1 <= rxmin) {
			return numCrosses;
		}
		if (x0 >= rxmax && x1 >= rxmax) {
			// Line is entirely to the right of the rect
			// and the vertical ranges of the two overlap by a non-empty amount
			// Thus, this line segment is partially in the "right-shadow"
			// Path may have done a complete crossing
			// Or path may have entered or exited the right-shadow
			if (y0 < y1) {
				// y-increasing line segment...
				// We know that y0 < rymax and y1 > rymin
				if (y0 <= rymin) {
					++numCrosses;
				}
				if (y1 >= rymax) {
					++numCrosses;
				}
			} else if (y1 < y0) {
				// y-decreasing line segment...
				// We know that y1 < rymax and y0 > rymin
				if (y1 <= rymin) {
					--numCrosses;
				}
				if (y0 >= rymax) {
					--numCrosses;
				}
			}
			return numCrosses;
		}
		// Remaining case:
		// Both x and y ranges overlap by a non-empty amount
		// First do trivial INTERSECTS rejection.
		if (RoundRectangle2afp.intersectsRoundRectangleSegment(
				rxmin, rymin, rxmax, rymax, arcWidth, arcHeight,
				x0, y0, x1, y1)) {
			return MathConstants.SHAPE_INTERSECTS;
		}

		final double x = rxmax - arcWidth;
		numCrosses += computeCrossingsFromPoint(x, rymin, x0, y0, x1, y1);
		numCrosses += computeCrossingsFromPoint(x, rymax, x0, y0, x1, y1);

		return numCrosses;
	}

	/**
	 * Calculates the number of times the line from (x0, y0) to (x1, y1)
	 * crosses the segment (sx0, sy0) to (sx1, sy1) extending to the right.
	 *
	 * @param crossings is the initial value for the number of crossings.
	 * @param sx1 is the first point of the segment to extend.
	 * @param sy1 is the first point of the segment to extend.
	 * @param sx2 is the second point of the segment to extend.
	 * @param sy2 is the second point of the segment to extend.
	 * @param x0 is the first point of the line.
	 * @param y0 is the first point of the line.
	 * @param x1 is the second point of the line.
	 * @param y1 is the secondpoint of the line.
	 * @return the crossing, or {@link MathConstants#SHAPE_INTERSECTS}
	 */
	@Pure
	@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:cyclomaticcomplexity",
			"checkstyle:npathcomplexity"})
	static int computeCrossingsFromSegment(
			int crossings,
			double sx1, double sy1,
			double sx2, double sy2,
			double x0, double y0,
			double x1, double y1) {
		int numCrosses = crossings;

		final double xmin = Math.min(sx1, sx2);
		final double xmax = Math.max(sx1, sx2);
		final double ymin = Math.min(sy1, sy2);
		final double ymax = Math.max(sy1, sy2);

		if (y0 <= ymin && y1 <= ymin) {
			return numCrosses;
		}
		if (y0 >= ymax && y1 >= ymax) {
			return numCrosses;
		}
		if (x0 <= xmin && x1 <= xmin) {
			return numCrosses;
		}
		if (x0 >= xmax && x1 >= xmax) {
			// The line is entirely at the right of the shadow
			if (y0 < y1) {
				if (y0 <= ymin) {
					++numCrosses;
				}
				if (y1 >= ymax) {
					++numCrosses;
				}
			} else {
				if (y1 <= ymin) {
					--numCrosses;
				}
				if (y0 >= ymax) {
					--numCrosses;
				}
			}
		} else if (intersectsSegmentSegmentWithEnds(x0, y0, x1, y1, sx1, sy1, sx2, sy2)) {
			return MathConstants.SHAPE_INTERSECTS;
		} else {
			final int side1;
			final int side2;
			if (sy1 <= sy2) {
				side1 = computeSideLinePoint(sx1, sy1, sx2, sy2, x0, y0, 0.);
				side2 = computeSideLinePoint(sx1, sy1, sx2, sy2, x1, y1, 0.);
			} else {
				side1 = computeSideLinePoint(sx2, sy2, sx1, sy1, x0, y0, 0.);
				side2 = computeSideLinePoint(sx2, sy2, sx1, sy1, x1, y1, 0.);
			}
			if (side1 > 0 || side2 > 0) {
				final int n1 = computeCrossingsFromPoint(sx1, sy1, x0, y0, x1, y1);
				final int n2;
				if (n1 != 0) {
					n2 = computeCrossingsFromPointWithoutEquality(sx2, sy2, x0, y0, x1, y1);
				} else {
					n2 = computeCrossingsFromPoint(sx2, sy2, x0, y0, x1, y1);
				}
				numCrosses += n1;
				numCrosses += n2;
			}
		}

		return numCrosses;
	}

	/**
	 * Accumulate the number of times the line crosses the shadow
	 * extending to the right of the triangle.  See the comment
	 * for the {@link MathConstants#SHAPE_INTERSECTS} constant for more complete details.
	 *
	 * @param crossings is the initial value for the number of crossings.
	 * @param tx1 is the first point of the triangle.
	 * @param ty1 is the first point of the triangle.
	 * @param tx2 is the second point of the triangle.
	 * @param ty2 is the second point of the triangle.
	 * @param tx3 is the third point of the triangle.
	 * @param ty3 is the third point of the triangle.
	 * @param x0 is the first point of the line.
	 * @param y0 is the first point of the line.
	 * @param x1 is the second point of the line.
	 * @param y1 is the secondpoint of the line.
	 * @return the crossing, or {@link MathConstants#SHAPE_INTERSECTS}.
	 */
	@Pure
	@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:cyclomaticcomplexity",
			"checkstyle:npathcomplexity"})
	static int computeCrossingsFromTriangle(
			int crossings,
			double tx1, double ty1,
			double tx2, double ty2,
			double tx3, double ty3,
			double x0, double y0,
			double x1, double y1) {
		int numCrosses = crossings;

		double xmin = tx1;
		double xmax = tx1;

		if (tx2 < xmin) {
			xmin = tx2;
		}
		if (tx2 > xmax) {
			xmax = tx2;
		}
		double ymin = ty1;
		double ymax = ty1;
		double x4ymin = tx1;
		double x4ymax = tx1;
		if (ty2 == ymin) {
			x4ymin = Math.max(tx2, x4ymin);
		} else if (ty2 < ymin) {
			ymin = ty2;
			x4ymin = tx2;
		}
		if (ty2 == ymax) {
			x4ymax = Math.max(tx2, x4ymax);
		} else if (ty2 > ymax) {
			ymax = ty2;
			x4ymax = tx2;
		}

		if (tx3 < xmin) {
			xmin = tx3;
		}
		if (tx3 > xmax) {
			xmax = tx3;
		}
		if (ty3 == ymin) {
			x4ymin = Math.max(tx3, x4ymin);
		} else if (ty3 < ymin) {
			ymin = ty3;
			x4ymin = tx3;
		}
		if (ty3 == ymax) {
			x4ymax = Math.max(tx3, x4ymax);
		} else if (ty3 > ymax) {
			ymax = ty3;
			x4ymax = tx3;
		}

		if (y0 <= ymin && y1 <= ymin) {
			return numCrosses;
		}
		if (y0 >= ymax && y1 >= ymax) {
			return numCrosses;
		}
		if (x0 <= xmin && x1 <= xmin) {
			return numCrosses;
		}

		if (x0 >= xmax && x1 >= xmax) {
			// The line is entirely at the right of the shadow
			if (y0 < y1) {
				if (y0 <= ymin) {
					++numCrosses;
				}
				if (y1 >= ymax) {
					++numCrosses;
				}
			} else {
				if (y1 <= ymin) {
					--numCrosses;
				}
				if (y0 >= ymax) {
					--numCrosses;
				}
			}
		} else if (Triangle2afp.intersectsTriangleSegment(
				tx1, ty1, tx2, ty2, tx3, ty3,
				x0, y0, x1, y1)) {
			return MathConstants.SHAPE_INTERSECTS;
		} else {
			numCrosses += computeCrossingsFromPoint(x4ymin, ymin, x0, y0, x1, y1);
			numCrosses += computeCrossingsFromPoint(x4ymax, ymax, x0, y0, x1, y1);
		}

		return numCrosses;
	}

	/** Compute the distance between a point and a line.
	 *
	 * @param x1 horizontal position of the first point of the line.
	 * @param y1 vertical position of the first point of the line.
	 * @param x2 horizontal position of the second point of the line.
	 * @param y2 vertical position of the second point of the line.
	 * @param px horizontal position of the point.
	 * @param py vertical position of the point.
	 * @return the distance beetween the point and the line.
	 * @see #computeDistanceSquaredLinePoint(double, double, double, double, double, double)
	 * @see #computeRelativeDistanceLinePoint(double, double, double, double, double, double)
	 */
	@Pure
	static double computeDistanceLinePoint(double x1, double y1, double x2, double y2, double px, double py) {
		final double x21 = x2 - x1;
		final double y21 = y2 - y1;
		final double denomenator = x21 * x21 + y21 * y21;
		if (denomenator == 0.) {
			return Point2D.getDistancePointPoint(px, py, x1, y1);
		}
		final double factor = ((y1 - py) * x21 - (x1 - px) * y21) / denomenator;
		return Math.abs(factor) * Math.sqrt(denomenator);
	}

	/** Compute the distance between a point and a segment.
	 *
	 * @param x1 horizontal position of the first point of the segment.
	 * @param y1 vertical position of the first point of the segment.
	 * @param x2 horizontal position of the second point of the segment.
	 * @param y2 vertical position of the second point of the segment.
	 * @param px horizontal position of the point.
	 * @param py vertical position of the point.
	 * @return the distance beetween the point and the segment.
	 */
	static double computeDistanceSegmentPoint(double x1, double y1, double x2, double y2, double px, double py) {
		final double x21 = x2 - x1;
		final double y21 = y2 - y1;

		final double denomenator = x21 * x21 + y21 * y21;
		if (denomenator == 0.) {
			return Point2D.getDistancePointPoint(px, py, x1, y1);
		}

		final double xp1 = px - x1;
		final double yp1 = py - y1;
		final double numerator = xp1 * x21 + yp1 * y21;
		final double ratio = numerator / denomenator;

		if (ratio <= 0.) {
			return Math.sqrt(xp1 * xp1 + yp1 * yp1);
		}

		if (ratio >= 1.) {
			final double xp2 = px - x2;
			final double yp2 = py - y2;
			return Math.sqrt(xp2 * xp2 + yp2 * yp2);
		}

		final double factor = (xp1 * y21 - yp1 * x21) / denomenator;
		return Math.abs(factor) * Math.sqrt(denomenator);
	}

	/** Compute the distance between a point and a line.
	 *
	 * @param x1 horizontal position of the first point of the line.
	 * @param y1 vertical position of the first point of the line.
	 * @param x2 horizontal position of the second point of the line.
	 * @param y2 vertical position of the second point of the line.
	 * @param px horizontal position of the point.
	 * @param py vertical position of the point.
	 * @return the distance beetween the point and the line.
	 * @see #computeDistanceLinePoint(double, double, double, double, double, double)
	 */
	@Pure
	static double computeDistanceSquaredLinePoint(double x1, double y1, double x2, double y2, double px, double py) {
		final double x21 = x2 - x1;
		final double y21 = y2 - y1;
		final double denomenator = x21 * x21 + y21 * y21;
		if (denomenator == 0.) {
			return Point2D.getDistanceSquaredPointPoint(px, py, x1, y1);
		}
		final double s = ((y1 - py) * x21 - (x1 - px) * y21) / denomenator;
		return (s * s) * Math.abs(denomenator);
	}

	/** Compute the square distance between a point and a segment.
	 *
	 * @param x1 horizontal position of the first point of the segment.
	 * @param y1 vertical position of the first point of the segment.
	 * @param x2 horizontal position of the second point of the segment.
	 * @param y2 vertical position of the second point of the segment.
	 * @param px horizontal position of the point.
	 * @param py vertical position of the point.
	 * @return the distance beetween the point and the segment.
	 */
	static double computeDistanceSquaredSegmentPoint(double x1, double y1, double x2, double y2, double px, double py) {
		final double x21 = x2 - x1;
		final double y21 = y2 - y1;
		final double denomenator = x21 * x21 + y21 * y21;
		if (denomenator == 0.) {
			return Point2D.getDistanceSquaredPointPoint(px, py, x1, y1);
		}

		final double xp1 = px - x1;
		final double yp1 = py - y1;
		final double numerator = xp1 * x21 + yp1 * y21;
		final double ratio = numerator / denomenator;

		if (ratio <= 0.) {
			return Math.abs(xp1 * xp1 + yp1 * yp1);
		}

		if (ratio >= 1.) {
			final double xp2 = px - x2;
			final double yp2 = py - y2;
			return Math.abs(xp2 * xp2 + yp2 * yp2);
		}

		final double factor =  (xp1 * y21 - yp1 * x21) / denomenator;
		return (factor * factor) * Math.abs(denomenator);
	}

	/** Replies the distance between the two segments.
	 *
	 * @param s1x1 x coordinate of the first point of the first triangle.
	 * @param s1y1 y coordinate of the first point of the first triangle.
	 * @param s1x2 x coordinate of the second point of the first triangle.
	 * @param s1y2 y coordinate of the second point of the first triangle.
     * @param s2x1 x coordinate of the first point of the second triangle.
     * @param s2y1 y coordinate of the first point of the second triangle.
     * @param s2x2 x coordinate of the second point of the second triangle.
     * @param s2y2 y coordinate of the second point of the second triangle.
	 * @return the distance between the two segments.
	 */
	@Pure
	@Inline(value = "(Segment2afp.computeClosestPointToSegment($1, $2, $3, $4, $5, $6, $7, $8, null))",
			imported = {Segment2afp.class})
	static double computeDistanceSquaredSegmentSegment(double s1x1, double s1y1, double s1x2, double s1y2,
			double s2x1, double s2y1, double s2x2, double s2y2) {
		return computeClosestPointToSegment(s1x1, s1y1, s1x2, s1y2, s2x1, s2y1, s2x2, s2y2, null);
	}

	/** Replies the point on the segment that is farthest to the given point.
	 *
	 * @param ax is the x coordinate of the first point of the segment.
	 * @param ay is the y coordinate of the first point of the segment.
	 * @param bx is the x coordinate of the second point of the segment.
	 * @param by is the y coordinate of the second point of the segment.
	 * @param px is the x coordinate of the point.
	 * @param py is the y coordinate of the point.
	 * @param result the farthest point on the shape.
	 */
	@Pure
	static void computeFarthestPointToPoint(
			double ax, double ay, double bx, double by, double px, double py, Point2D<?, ?> result) {
		assert result != null : "Result must be not null"; //$NON-NLS-1$
		final double xpa = px - ax;
		final double ypa = py - ay;
		final double xpb = px - bx;
		final double ypb = py - by;
		if ((xpa * xpa + ypa * ypa) >= (xpb * xpb + ypb * ypb)) {
			result.set(ax, ay);
		} else {
			result.set(bx, by);
		}
	}

	/** Replies the point on the segment that is farthest to the rectangle.
	 *
	 * @param sx1 is the x coordinate of the first point of the segment.
	 * @param sy1 is the y coordinate of the first point of the segment.
	 * @param sx2 is the x coordinate of the second point of the segment.
	 * @param sy2 is the y coordinate of the second point of the segment.
	 * @param rx is the x coordinate of the rectangle.
	 * @param ry is the y coordinate of the rectangle.
	 * @param rwidth is the width of the rectangle.
	 * @param rheight is the height of the rectangle.
	 * @param result the is point on the segment.
	 */
	@Pure
	@SuppressWarnings("checkstyle:parameternumber")
	static void computeFarthestPointToRectangle(double sx1, double sy1, double sx2, double sy2,
			double rx, double ry, double rwidth, double rheight, Point2D<?, ?> result) {
		final double rmaxx = rx + rwidth;
		final double rmaxy = ry + rheight;
		final int code1 = MathUtil.getCohenSutherlandCode(sx1, sy1, rx, ry, rmaxx, rmaxy);
		final int code2 = MathUtil.getCohenSutherlandCode(sx2, sy2, rx, ry, rmaxx, rmaxy);
		final Point2D<?, ?> tmp1 = new InnerComputationPoint2afp();
		final Point2D<?, ?> tmp2 = new InnerComputationPoint2afp();
		final int zone;
		if (code1 != code2) {
			zone = Rectangle2afp.reduceCohenSutherlandZoneRectangleSegment(
					rx, ry, rmaxx, rmaxy,
					sx1, sy1, sx2, sy2,
					code1, code2,
					tmp1, tmp2);
		} else {
			zone = code1;
			tmp1.set(sx1, sy1);
			tmp2.set(sx2, sy2);
		}
		if ((zone & MathConstants.COHEN_SUTHERLAND_LEFT) != 0) {
			computeFarthestPointToSegment(
					sx1, sy1, sx2, sy2,
					rx, ry, rx, rmaxy, result);
		} else if ((zone & MathConstants.COHEN_SUTHERLAND_RIGHT) != 0) {
			computeFarthestPointToSegment(
					sx1, sy1, sx2, sy2,
					rmaxx, ry, rmaxx, rmaxy, result);
		} else if ((zone & MathConstants.COHEN_SUTHERLAND_BOTTOM) != 0) {
			computeFarthestPointToSegment(
					sx1, sy1, sx2, sy2,
					rx, ry, rmaxx, ry, result);
		} else if ((zone & MathConstants.COHEN_SUTHERLAND_TOP) != 0) {
			computeFarthestPointToSegment(
					sx1, sy1, sx2, sy2,
					rx, rmaxy, rmaxx, rmaxy, result);
		} else {
		    final double dist1 = Point2D.getDistanceSquaredPointPoint(tmp1.getX(), tmp1.getY(), sx1, sy1);
		    final double dist2 = Point2D.getDistanceSquaredPointPoint(tmp2.getX(), tmp2.getY(), sx2, sy2);
			if (dist1 >= dist2) {
				result.set(sx1, sy1);
			} else {
				result.set(sx2, sy2);
			}
		}
	}

	/** Replies the point on the first segment that is farthest to the second segment.
	 *
	 * @param s1x1 is the x coordinate of the first point of the first segment.
	 * @param s1y1 is the y coordinate of the first point of the first segment.
	 * @param s1x2 is the x coordinate of the second point of the first segment.
	 * @param s1y2 is the y coordinate of the second point of the first segment.
	 * @param s2x1 is the x coordinate of the first point of the second segment.
	 * @param s2y1 is the y coordinate of the first point of the second segment.
	 * @param s2x2 is the x coordinate of the second point of the second segment.
	 * @param s2y2 is the y coordinate of the second point of the second segment.
	 * @param result the is point on the shape.
	 * @return the minimal square distance between the point on the first segment and any point on the second segment.
	 */
	@Pure
	@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:npathcomplexity", "checkstyle:magicnumber"})
	static double computeFarthestPointToSegment(
			double s1x1, double s1y1, double s1x2, double s1y2,
			double s2x1, double s2y1, double s2x2, double s2y2,
			Point2D<?, ?> result) {
	    final double ux = s1x2 - s1x1;
	    final double uy = s1y2 - s1y1;
	    final double vx = s2x2 - s2x1;
		final double vy = s2y2 - s2y1;
		final double wx = s1x1 - s2x1;
		final double wy = s1y1 - s2y1;
		final double a = Vector2D.dotProduct(ux, uy, ux, uy);
		final double b = Vector2D.dotProduct(ux, uy, vx, vy);
		final double c = Vector2D.dotProduct(vx, vy, vx, vy);
		final double d = Vector2D.dotProduct(ux, uy, wx, wy);
		final double e = Vector2D.dotProduct(vx, vy, wx, wy);
		final double bigD = a * c - b * b;
		double svD = bigD;
		double tvD = bigD;
		double svN;
		double tvN;
		// compute the line parameters of the two closest points
		if (MathUtil.isEpsilonZero(bigD)) {
			// the lines are almost parallel
			// force using point P0 on segment S1
			svN = 0.;
			// to prevent possible division by 0.0 later
			svD = 1.;
			tvN = e;
			tvD = c;
		} else {
			// get the closest points on the infinite lines
			svN = b * e - c * d;
			tvN = a * e - b * d;
			if (svN < 0.) {
				// sc < 0 => the s=0 edge is visible
				svN = 0.;
				tvN = e;
				tvD = c;
			} else if (svN > svD) {
				// sc > 1  => the s=1 edge is visible
				svN = svD;
				tvN = e + b;
				tvD = c;
			}
		}

		if (tvN < 0.) {
			// tc < 0 => the t=0 edge is visible
			tvN = 0.;
			// recompute sc for this edge
			if (-d < 0.) {
				svN = 0.0;
			} else if (-d > a) {
				svN = svD;
			} else {
				svN = -d;
				svD = a;
			}
		} else if (tvN > tvD) {
			// tc > 1  => the t=1 edge is visible
			tvN = tvD;
			// recompute sc for this edge
			if ((-d + b) < 0.) {
				svN = 0;
			} else if ((-d + b) > a) {
				svN = svD;
			} else {
				svN = -d +  b;
				svD = a;
			}
		}

		final double sc = MathUtil.isEpsilonZero(svN) ? 0. : (svN / svD);
		final double tc = MathUtil.isEpsilonZero(tvN) ? 0. : (tvN / tvD);

		if (result != null) {
			if (sc <= .5) {
				result.set(s1x2, s1y2);
			} else {
				result.set(s1x1, s1y1);
			}
		}

		// get the difference of the two closest points
		final double dPx = wx + (sc * ux) - (tc * vx);
		final double dPy = wy + (sc * uy) - (tc * vy);

		return dPx * dPx + dPy * dPy;
	}

	/** Compute the intersection of two lines specified
	 * by the specified points and vectors.
	 *
	 * @param x1 horizontal position of the first point of the line.
	 * @param y1 vertical position of the first point of the line.
	 * @param x2 horizontal position of the second point of the line.
	 * @param y2 vertical position of the second point of the line.
	 * @param x3 horizontal position of the first point of the line.
	 * @param y3 vertical position of the first point of the line.
	 * @param x4 horizontal position of the second point of the line.
	 * @param y4 vertical position of the second point of the line.
	 * @param result the intersection point.
	 * @return <code>true</code> if there is an intersection.
	 */
	@Pure
	@SuppressWarnings("checkstyle:parameternumber")
	static boolean computeLineLineIntersection(double x1, double y1, double x2, double y2,
			double x3, double y3, double x4, double y4,
			Point2D<?, ?> result) {
		assert result != null : "Result must be not null"; //$NON-NLS-1$
		final double x21 = x2 - x1;
		final double x43 = x4 - x3;
		final double y21 = y2 - y1;
		final double y43 = y4 - y3;

		final double denom = y43 * x21 - x43 * y21;
		if (denom == 0.) {
			return false;
		}
		final double x13 = x1 - x3;
		final double y13 = y1 - y3;
		double intersectionFactor1 = x43 * y13 - y43 * x13;
		final double intersectionFactor2 = x21 * y13 - y21 * x13;
		if (intersectionFactor1 == intersectionFactor2) {
			return false;
		}
		intersectionFactor1 = intersectionFactor1 / denom;
		result.set(
				x1 + intersectionFactor1 * x21,
				y1 + intersectionFactor1 * y21);
		return true;
	}

	/**
	 * Replies the position factory for the intersection point between two lines.
	 *
	 * <p>Let line equations for L1 and L2:<br>
	 * <code>L1: P1 + factor1 * (P2-P1)</code><br>
	 * <code>L2: P3 + factor2 * (P4-P3)</code><br>
	 * If lines are intersecting, then<br>
	 * <code>P1 + factor1 * (P2-P1) = P3 + factor2 * (P4-P3)</code>
	 *
	 * <p>This function computes and replies <code>factor1</code>.
	 *
	 * @param x1
	 *            is the X coordinate of the first point of the first line.
	 * @param y1
	 *            is the Y coordinate of the first point of the first line.
	 * @param x2
	 *            is the X coordinate of the second point of the first line.
	 * @param y2
	 *            is the Y coordinate of the second point of the first line.
	 * @param x3
	 *            is the X coordinate of the first point of the second line.
	 * @param y3
	 *            is the Y coordinate of the first point of the second line.
	 * @param x4
	 *            is the X coordinate of the second point of the second line.
	 * @param y4
	 *            is the Y coordinate of the second point of the second line.
	 * @return <code>factor1</code> or {@link Double#NaN} if no intersection.
	 */
	@Pure
	static double computeLineLineIntersectionFactor(double x1, double y1, double x2, double y2,
			double x3, double y3, double x4, double y4) {
		final double vx1 = x2 - x1;
		final double vy1 = y2 - y1;
		final double vx2 = x4 - x3;
		final double vy2 = y4 - y3;

		// determinant is zero when parallel = det(L1, L2)
		final double det = Vector2D.perpProduct(vx1, vy1, vx2, vy2);
		if (det == 0.) {
			return Double.NaN;
		}

		// Given line equations:
		// Pa = P1 + ua (P2-P1), and
		// Pb = P3 + ub (P4-P3)
		// and
		// V = (P1-P3)
		// then
		// ua = det(L2, V) / det(L1, L2)
		// ub = det(L1, V) / det(L1, L2)
		return Vector2D.perpProduct(vx2, vy2, x1 - x3, y1 - y3) / det;
	}

	/**
	 * Replies the projection a point on a segment.
	 *
	 * @param px
	 *            is the coordiante of the point to project
	 * @param py
	 *            is the coordiante of the point to project
	 * @param s1x
	 *            is the x-coordinate of the first line point.
	 * @param s1y
	 *            is the y-coordinate of the first line point.
	 * @param s2x
	 *            is the x-coordinate of the second line point.
	 * @param s2y
	 *            is the y-coordinate of the second line point.
	 * @return the projection of the specified point on the line. If
	 *     equal to {@code 0}, the projection is equal to the first segment point.
	 *     If equal to {@code 1}, the projection is equal to the second segment point.
	 *     If inside {@code ]0;1[}, the projection is between the two segment points.
	 *     If inside {@code ]-inf;0[}, the projection is outside on the side of the
	 *     first segment point. If inside {@code ]1;+inf[}, the projection is
	 *     outside on the side of the second segment point.
	 */
	@Pure
	static double computeProjectedPointOnLine(double px, double py, double s1x, double s1y, double s2x, double s2y) {
		final double vx = s2x - s1x;
		final double vy = s2y - s1y;
		final double numerator = (px - s1x) * vx + (py - s1y) * vy;
		final double denomenator = vx * vx + vy * vy;
		return numerator / denomenator;
	}

	/**
	 * Replies the relative distance from the given point to the given line.
	 * The replied distance may be negative, depending on which side of
	 * the line the point is.
	 *
	 * @param x1
	 *            the X coordinate of the start point of the specified line segment
	 * @param y1
	 *            the Y coordinate of the start point of the specified line segment
	 * @param x2
	 *            the X coordinate of the end point of the specified line segment
	 * @param y2
	 *            the Y coordinate of the end point of the specified line segment
	 * @param px
	 *            the X coordinate of the specified point to be compared with the specified line segment
	 * @param py
	 *            the Y coordinate of the specified point to be compared with the specified line segment
	 * @return the positive or negative distance from the point to the line
	 * @see #ccw(double, double, double, double, double, double, double)
	 * @see #computeSideLinePoint(double, double, double, double, double, double, double)
	 */
	@Pure
	static double computeRelativeDistanceLinePoint(double x1, double y1, double x2, double y2, double px, double py) {
		final double x21 = x2 - x1;
		final double y21 = y2 - y1;
		final double denomenator = x21 * x21 + y21 * y21;
		if (denomenator == 0.) {
			return Point2D.getDistancePointPoint(px, py, x1, y1);
		}
		final double factor = ((y1 - py) * x21 - (x1 - px) * y21) / denomenator;
		return factor * Math.sqrt(denomenator);
	}

	/**
	 * Replies the intersection point between two segments.
	 *
	 * @param x1
	 *            is the X coordinate of the first point of the first segment.
	 * @param y1
	 *            is the Y coordinate of the first point of the first segment.
	 * @param x2
	 *            is the X coordinate of the second point of the first segment.
	 * @param y2
	 *            is the Y coordinate of the second point of the first segment.
	 * @param x3
	 *            is the X coordinate of the first point of the second segment.
	 * @param y3
	 *            is the Y coordinate of the first point of the second segment.
	 * @param x4
	 *            is the X coordinate of the second point of the second segment.
	 * @param y4
	 *            is the Y coordinate of the second point of the second segment.
	 * @param result the intersection point.
	 * @return <code>true</code> if an intersection exists.
	 */
	@Pure
	@SuppressWarnings("checkstyle:parameternumber")
	static boolean computeSegmentSegmentIntersection(double x1, double y1, double x2, double y2,
			double x3, double y3, double x4, double y4,
			Point2D<?, ?> result) {
		assert result != null : "Result must be not null"; //$NON-NLS-1$
		final double m = computeSegmentSegmentIntersectionFactor(x1, y1, x2, y2, x3, y3, x4, y4);
		if (Double.isNaN(m)) {
			return false;
		}
		result.set(x1 + m * (x2 - x1), y1 + m * (y2 - y1));
		return true;
	}

	/**
	 * Replies one position factor for the intersection point between two lines.
	 *
	 * <p>Let line equations for L1 and L2:<br>
	 * <code>L1: P1 + factor1 * (P2-P1)</code><br>
	 * <code>L2: P3 + factor2 * (P4-P3)</code><br>
	 * If lines are intersecting, then<br>
	 * <code>P1 + factor1 * (P2-P1) = P3 + factor2 * (P4-P3)</code>
	 *
	 * <p>This function computes and replies <code>factor1</code>.
	 *
	 * @param x1
	 *            is the X coordinate of the first point of the first segment.
	 * @param y1
	 *            is the Y coordinate of the first point of the first segment.
	 * @param x2
	 *            is the X coordinate of the second point of the first segment.
	 * @param y2
	 *            is the Y coordinate of the second point of the first segment.
	 * @param x3
	 *            is the X coordinate of the first point of the second segment.
	 * @param y3
	 *            is the Y coordinate of the first point of the second segment.
	 * @param x4
	 *            is the X coordinate of the second point of the second segment.
	 * @param y4
	 *            is the Y coordinate of the second point of the second segment.
	 * @return <code>factor1</code> or {@link Double#NaN} if no intersection.
	 */
	@Pure
	static double computeSegmentSegmentIntersectionFactor(double x1, double y1, double x2, double y2,
			double x3, double y3, double x4, double y4) {
		final double vx1 = x2 - x1;
		final double vy1 = y2 - y1;
		final double vx2 = x4 - x3;
		final double vy2 = y4 - y3;

		// determinant is zero when parallel = det(L1, L2)
		final double det = Vector2D.perpProduct(vx1, vy1, vx2, vy2);
		if (det == 0.) {
			return Double.NaN;
		}

		// Given line equations:
		// Pa = P1 + ua (P2-P1), and
		// Pb = P3 + ub (P4-P3)
		// and
		// V = (P1-P3)
		// then
		// ua = det(L2, V) / det(L1, L2)
		// ub = det(L1, V) / det(L1, L2)
		final double vx3 = x1 - x3;
		final double vy3 = y1 - y3;
		double intersectionFactor = Vector2D.perpProduct(vx1, vy1, vx3, vy3) / det;
		if (intersectionFactor < 0. || intersectionFactor > 1.) {
			return Double.NaN;
		}
		intersectionFactor = Vector2D.perpProduct(vx2, vy2, vx3, vy3) / det;
		return (intersectionFactor < 0. || intersectionFactor > 1.) ? Double.NaN : intersectionFactor;
	}

	/**
	 * Replies on which side of a line the given point is located.
	 *
	 * <p>A return value of 1 indicates that the line segment must turn in the direction
	 * that takes the positive X axis towards the negative Y axis. In the default
	 * coordinate system used by Java 2D, this direction is counterclockwise.
	 *
	 * <p>A return value of -1 indicates that the line segment must turn in the direction that
	 * takes the positive X axis towards the positive Y axis. In the default coordinate system,
	 * this direction is clockwise.
	 *
	 * <p>A return value of 0 indicates that the point lies exactly on the line segment. Note that
	 * an indicator value of 0 is rare and not useful for determining colinearity because of
	 * floating point rounding issues.
	 *
	 * <p>This function uses the equal-to-zero test with the error {@link Math#ulp(double)}.
	 *
	 * <p>In opposite of {@link #ccw(double, double, double, double, double, double, double)},
	 * this function does not try to classify the point if it is colinear
	 * to the segment. If the point is colinear, O is always returns.
	 *
	 * @param x1
	 *            the X coordinate of the start point of the specified line segment
	 * @param y1
	 *            the Y coordinate of the start point of the specified line segment
	 * @param x2
	 *            the X coordinate of the end point of the specified line segment
	 * @param y2
	 *            the Y coordinate of the end point of the specified line segment
	 * @param px
	 *            the X coordinate of the specified point to be compared with the specified line segment
	 * @param py
	 *            the Y coordinate of the specified point to be compared with the specified line segment
	 * @param epsilon approximate epsilon.
	 * @return an integer that indicates the position of the third specified coordinates with
	 *     respect to the line segment formed by the first two specified coordinates.
	 * @see #computeRelativeDistanceLinePoint(double, double, double, double, double, double)
	 * @see MathUtil#isEpsilonZero(double)
	 * @see #ccw(double, double, double, double, double, double, double)
	 */
	@Pure
	static int computeSideLinePoint(double x1, double y1, double x2, double y2, double px, double py, double epsilon) {
		final double x21 = x2 - x1;
		final double y21 = y2 - y1;
		final double xp1 = px - x1;
		final double yp1 = py - y1;
		double side = xp1 * y21 - yp1 * x21;
		if (side != 0. && MathUtil.isEpsilonZero(side, epsilon)) {
			side = 0.;
		}
		return (side < 0) ? -1 : ((side > 0) ? 1 : 0);
	}

	/** Do an intersection test of two segments for ensuring that the answer of "no intersect" is safe.
	 *
	 * <p>If the function replies {@link UncertainIntersection#NO}, we are sure that the two given segments are not
	 * intersecting. If the function replies {@link UncertainIntersection#PERHAPS}, the two segments may intersects
	 * (uncertain answer).
	 *
	 * <p>This function considers that the ends of the segments are intersecting.
	 *
	 * @param x1 is the first point of the first segment.
	 * @param y1 is the first point of the first segment.
	 * @param x2 is the second point of the first segment.
	 * @param y2 is the second point of the first segment.
	 * @param x3 is the first point of the second segment.
	 * @param y3 is the first point of the second segment.
	 * @param x4 is the second point of the second segment.
	 * @param y4 is the second point of the second segment.
	 * @return the type of intersection.
	 * @see #intersectsSegmentSegmentWithEnds(double, double, double, double, double, double, double, double)
	 */
	@Pure
	static UncertainIntersection getNoSegmentSegmentWithEndsIntersection(double x1, double y1, double x2, double y2,
			double x3, double y3, double x4, double y4) {
		final double vx1 = x2 - x1;
		final double vy1 = y2 - y1;

		// Based on CCW. It is different than MathUtil.ccw(); because
		// this small algorithm is computing a ccw of 0 for colinear points.
		final double vx2a = x3 - x1;
		final double vy2a = y3 - y1;
		double f1 = vx2a * vy1 - vy2a * vx1;

		final double vx2b = x4 - x1;
		final double vy2b = y4 - y1;
		double f2 = vx2b * vy1 - vy2b * vx1;

		// s = f1 * f2
		//
		// f1  f2  s   intersect
		// -1  -1   1  F
		// -1   0   0  ON SEGMENT?
		// -1   1  -1  T
		//  0  -1   0  ON SEGMENT?
		//  0   0   0  SEGMENT INTERSECTION?
		//  0   1   1  ON SEGMENT?
		//  1  -1  -1  T
		//  1   0   0  ON SEGMENT?
		//  1   1   1  F
		final double sign = f1 * f2;

		if (sign < 0) {
			return UncertainIntersection.PERHAPS;
		}
		if (sign > 0) {
			return UncertainIntersection.NO;
		}

		final double squaredLength = vx1 * vx1 + vy1 * vy1;

		if (f1 == 0. && f2 == 0.) {
			// Projection of the point on the segment line:
			// <0 -> means before first point
			// >1 -> means after second point
			// otherwhise on the segment.

			f1 = (vx2a * vx1 + vy2a * vy1) / squaredLength;
			f2 = (vx2b * vx1 + vy2b * vy1) / squaredLength;

			// No intersection when:
			// f1<0 && f2<0 or
			// f1>1 && f2>1

			return UncertainIntersection.fromBoolean((f1 >= 0. || f2 >= 0.) && (f1 <= 1. || f2 <= 1.));
		}

		if (f1 == 0) {
			// Projection of the point on the segment line:
			// <0 -> means before first point
			// >1 -> means after second point
			// otherwhise on the segment.

			f1 = (vx2a * vx1 + vy2a * vy1) / squaredLength;

			// No intersection when:
			// f1<=0 && f2<=0 or
			// f1>=1 && f2>=1

			return UncertainIntersection.fromBoolean(f1 >= 0. && f1 <= 1.);
		}

		if (f2 == 0) {
			// Projection of the point on the segment line:
			// <0 -> means before first point
			// >1 -> means after second point
			// otherwhise on the segment.

			f2 = (vx2b * vx1 + vy2b * vy1) / squaredLength;

			// No intersection when:
			// f1<0 && f2<0 or
			// f1>1 && f2>1

			return UncertainIntersection.fromBoolean(f2 >= 0. && f2 <= 1.);
		}

		return UncertainIntersection.NO;
	}

	/** Do an intersection test of two segments for ensuring that the answer of "no intersect" is safe.
	 * If the function replies <code>true</code>, it may
	 * This function does not consider that the ends of
	 * the segments are intersecting.
	 *
	 * @param x1 is the first point of the first segment.
	 * @param y1 is the first point of the first segment.
	 * @param x2 is the second point of the first segment.
	 * @param y2 is the second point of the first segment.
	 * @param x3 is the first point of the second segment.
	 * @param y3 is the first point of the second segment.
	 * @param x4 is the second point of the second segment.
	 * @param y4 is the second point of the second segment.
	 * @return the type of intersection.
	 * @see #intersectsSegmentSegmentWithoutEnds(double, double, double, double, double, double, double, double)
	 */
	@Pure
	static UncertainIntersection getNoSegmentSegmentWithoutEndsIntersection(double x1, double y1, double x2, double y2,
			double x3, double y3, double x4, double y4) {
		final double vx1 = x2 - x1;
		final double vy1 = y2 - y1;

		final double vx2a = x3 - x1;
		final double vy2a = y3 - y1;
		double f1 = vx2a * vy1 - vy2a * vx1;

		final double vx2b = x4 - x1;
		final double vy2b = y4 - y1;
		double f2 = vx2b * vy1 - vy2b * vx1;

		// s = f1 * f2
		//
		// f1  f2  s   intersect
		// -1  -1   1  F
		// -1   0   0  F
		// -1   1  -1  T
		//  0  -1   0  F
		//  0   0   0  SEGMENT INTERSECTION?
		//  0   1   1  F
		//  1  -1  -1  T
		//  1   0   0  F
		//  1   1   1  F

		final double sign = f1 * f2;

		if (sign < 0) {
			return UncertainIntersection.PERHAPS;
		}
		if (sign > 0) {
			return UncertainIntersection.NO;
		}

		if (f1 == 0. && f2 == 0.) {
			// Projection of the point on the segment line:
			// <0 -> means before first point
			// >1 -> means after second point
			// otherwhise on the segment.

			final double squaredLength = vx1 * vx1 + vy1 * vy1;

			f1 = (vx2a * vx1 + vy2a * vy1) / squaredLength;
			f2 = (vx2b * vx1 + vy2b * vy1) / squaredLength;

			// No intersection when:
			// f1<=0 && f2<=0 or
			// f1>=1 && f2>=1

			return UncertainIntersection.fromBoolean((f1 > 0. || f2 > 0.) && (f1 < 1. || f2 < 1.));
		}

		return UncertainIntersection.NO;
	}

	/** Compute the interpolate point between the two points.
	 *
	 * @param p1x x coordinate of the first point.
	 * @param p1y y coordinate of the first point.
	 * @param p2x x coordinate of the second point.
	 * @param p2y y coordinate of the second point.
	 * @param factor is between 0 and 1; 0 for p1, and 1 for p2.
	 * @param result the interpolate point.
	 */
	@Pure
	static void interpolate(double p1x, double p1y, double p2x, double p2y, double factor, Point2D<?, ?> result) {
		assert result != null : "Result must be not null"; //$NON-NLS-1$
		assert factor >= 0. && factor <= 1. : "Factor must be in [0;1]"; //$NON-NLS-1$
		final double vx = p2x - p1x;
		final double vy = p2y - p1y;
		result.set(
				p1x + factor * vx,
				p1y + factor * vy);
	}

	/** Replies if two lines are intersecting.
	 *
	 * @param x1 is the first point of the first line.
	 * @param y1 is the first point of the first line.
	 * @param x2 is the second point of the first line.
	 * @param y2 is the second point of the first line.
	 * @param x3 is the first point of the second line.
	 * @param y3 is the first point of the second line.
	 * @param x4 is the second point of the second line.
	 * @param y4 is the second point of the second line.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	@Pure
	static boolean intersectsLineLine(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
		if (isParallelLines(x1, y1, x2, y2, x3, y3, x4, y4)) {
			return Point2D.isCollinearPoints(x1, y1, x2, y2, x3, y3);
		}
		return true;
	}

	/** Replies if a segment and a line are intersecting.
	 *
	 * @param x1 is the first point of the first segment.
	 * @param y1 is the first point of the first segment.
	 * @param x2 is the second point of the first segment.
	 * @param y2 is the second point of the first segment.
	 * @param x3 is the first point of the second line.
	 * @param y3 is the first point of the second line.
	 * @param x4 is the second point of the second line.
	 * @param y4 is the second point of the second line.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	@Pure
	static boolean intersectsSegmentLine(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
		return (computeSideLinePoint(x3, y3, x4, y4, x1, y1, Double.NaN)
				* computeSideLinePoint(x3, y3, x4, y4, x2, y2, Double.NaN)) <= 0;
	}

	/** Replies if two segments are intersecting.
	 * This function considers that the ends of
	 * the segments are intersecting.
	 * To ignore the ends of the segments, see
	 * {@link #intersectsSegmentSegmentWithoutEnds(double, double, double, double, double, double, double, double)}.
	 *
	 * @param x1 is the first point of the first segment.
	 * @param y1 is the first point of the first segment.
	 * @param x2 is the second point of the first segment.
	 * @param y2 is the second point of the first segment.
	 * @param x3 is the first point of the second segment.
	 * @param y3 is the first point of the second segment.
	 * @param x4 is the second point of the second segment.
	 * @param y4 is the second point of the second segment.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 * @see #intersectsSegmentSegmentWithoutEnds(double, double, double, double, double, double, double, double)
	 */
	@Pure
	static boolean intersectsSegmentSegmentWithEnds(double x1, double y1, double x2, double y2,
			double x3, double y3, double x4, double y4) {
		final UncertainIntersection result = getNoSegmentSegmentWithEndsIntersection(x1, y1, x2, y2, x3, y3, x4, y4);
		if (!result.booleanValue()) {
			return result.booleanValue();
		}
		return getNoSegmentSegmentWithEndsIntersection(x3, y3, x4, y4, x1, y1, x2, y2).booleanValue();
	}

	/** Replies if two segments are intersecting.
	 * This function considers that the ends of
	 * the segments are not intersecting.
	 * To include the ends of the segments in the intersection ranges, see
	 * {@link #intersectsSegmentSegmentWithEnds(double, double, double, double, double, double, double, double)}.
	 *
	 * @param x1 is the first point of the first segment.
	 * @param y1 is the first point of the first segment.
	 * @param x2 is the second point of the first segment.
	 * @param y2 is the second point of the first segment.
	 * @param x3 is the first point of the second segment.
	 * @param y3 is the first point of the second segment.
	 * @param x4 is the second point of the second segment.
	 * @param y4 is the second point of the second segment.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 * @see #intersectsSegmentSegmentWithEnds(double, double, double, double, double, double, double, double)
	 */
	@Pure
	static boolean intersectsSegmentSegmentWithoutEnds(double x1, double y1, double x2, double y2,
			double x3, double y3, double x4, double y4) {
		final UncertainIntersection result = getNoSegmentSegmentWithoutEndsIntersection(x1, y1, x2, y2, x3, y3, x4, y4);
		if (!result.booleanValue()) {
			return result.booleanValue();
		}
		return getNoSegmentSegmentWithoutEndsIntersection(x3, y3, x4, y4, x1, y1, x2, y2).booleanValue();
	}

	/**
	 * Replies if two lines are colinear.
	 *
	 * <p>The given two lines are described respectivaly by two points, i.e. {@code (x1, y1)}
	 * and {@code (x2, y2)} for the first line, and {@code (x3, y3)} and {@code (x4, y4)} for the second line.
	 *
	 * <p>If you are interested to test if the two lines are parallel, see
	 * {@link #isParallelLines(double, double, double, double, double, double, double, double)}.
	 *
	 * @param x1
	 *            is the X coordinate of the first point of the first line.
	 * @param y1
	 *            is the Y coordinate of the first point of the first line.
	 * @param x2
	 *            is the X coordinate of the second point of the first line.
	 * @param y2
	 *            is the Y coordinate of the second point of the first line.
	 * @param x3
	 *            is the X coordinate of the first point of the second line.
	 * @param y3
	 *            is the Y coordinate of the first point of the second line.
	 * @param x4
	 *            is the X coordinate of the second point of the second line.
	 * @param y4
	 *            is the Y coordinate of the second point of the second line.
	 * @return <code>true</code> if the two given lines are collinear.
	 * @see #isParallelLines(double, double, double, double, double, double, double, double)
	 * @see Point2D#isCollinearPoints(double, double, double, double, double, double)
	 */
	@Pure
	static boolean isCollinearLines(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
		return isParallelLines(x1, y1, x2, y2, x3, y3, x4, y4)
				&& Point2D.isCollinearPoints(x1, y1, x2, y2, x3, y3);
	}

	/**
	 * Replies if two lines are parallel.
	 *
	 * <p>The given two lines are described respectivaly by two points, i.e. {@code (x1, y1)}
	 * and {@code (x2, y2)} for the first line, and {@code (x3, y3)} and {@code (x4, y4)} for the second line.
	 *
	 * <p>If you are interested to test if the two lines are colinear, see
	 * {@link #isCollinearLines(double, double, double, double, double, double, double, double)}.
	 *
	 * @param x1
	 *            is the X coordinate of the first point of the first line.
	 * @param y1
	 *            is the Y coordinate of the first point of the first line.
	 * @param x2
	 *            is the X coordinate of the second point of the first line.
	 * @param y2
	 *            is the Y coordinate of the second point of the first line.
	 * @param x3
	 *            is the X coordinate of the first point of the second line.
	 * @param y3
	 *            is the Y coordinate of the first point of the second line.
	 * @param x4
	 *            is the X coordinate of the second point of the second line.
	 * @param y4
	 *            is the Y coordinate of the second point of the second line.
	 * @return <code>true</code> if the two given lines are parallel.
	 * @see #isCollinearLines(double, double, double, double, double, double, double, double)
	 */
	@Pure
	static boolean isParallelLines(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
		return Vector2D.isCollinearVectors(x2 - x1, y2 - y1, x4 - x3, y4 - y3);
	}

	/** Replies if a point is closed to a line.
	 *
	 * @param x1 horizontal location of the first-line begining.
	 * @param y1 vertical location of the first-line ending.
	 * @param x2 horizontal location of the second-line begining.
	 * @param y2 vertical location of the second-line ending.
	 * @param x horizontal location of the point.
	 * @param y vertical location of the point.
	 * @param hitDistance is the maximal hitting distance.
	 * @return <code>true</code> if the point and the
	 *         line have closed locations.
	 */
	@Pure
	static boolean isPointClosedToLine(double x1, double y1,
			double x2, double y2,
			double x, double y, double hitDistance) {
		assert hitDistance >= 0. : "Hit distance must be positive or zero"; //$NON-NLS-1$
		return computeDistanceLinePoint(x1, y1, x2, y2, x, y) < hitDistance;
	}

	/** Replies if a point is closed to a segment.
	 *
	 * @param x1 horizontal location of the first-segment begining.
	 * @param y1 vertical location of the first-segment ending.
	 * @param x2 horizontal location of the second-segment begining.
	 * @param y2 vertical location of the second-segment ending.
	 * @param x horizontal location of the point.
	 * @param y vertical location of the point.
	 * @param hitDistance is the maximal hitting distance.
	 * @return <code>true</code> if the point and the
	 *         line have closed locations.
	 */
	@Pure
	static boolean isPointClosedToSegment(double x1, double y1,
			double x2, double y2,
			double x, double y, double hitDistance) {
		assert hitDistance >= 0. : "Hit distance must be positive or zero"; //$NON-NLS-1$
		return computeDistanceSegmentPoint(x1, y1, x2, y2, x, y) < hitDistance;
	}

	@Override
	default void clear() {
		set(0, 0, 0, 0);
	}

	/** Clip the segment against the clipping rectangle
	 * according to the <a href="http://en.wikipedia.org/wiki/Cohen%E2%80%93Sutherland_algorithm">Cohen-Sutherland algorithm</a>.
	 *
	 * @param rxmin is the min of the coordinates of the rectangle.
	 * @param rymin is the min of the coordinates of the rectangle.
	 * @param rxmax is the max of the coordinates of the rectangle.
	 * @param rymax is the max of the coordinates of the rectangle.
	 * @return <code>true</code> if the segment has an intersection with the
	 *     rectangle and the segment was clipped; <code>false</code> if the segment
	 *     does not intersect the rectangle.
	 */
	@Pure
	default boolean clipToRectangle(double rxmin, double rymin, double rxmax, double rymax) {
		assert rxmin <= rxmax : "rxmin must be lower or equal to rymin"; //$NON-NLS-1$
		assert rymin <= rymax : "rxmin must be lower or equal to rymin"; //$NON-NLS-1$
		double x0 = getX1();
		double y0 = getY1();
		double x1 = getX2();
		double y1 = getY2();
		int code1 = MathUtil.getCohenSutherlandCode(x0, y0, rxmin, rymin, rxmax, rymax);
		int code2 = MathUtil.getCohenSutherlandCode(x1, y1, rxmin, rymin, rxmax, rymax);
		boolean accept = false;
		boolean cont = true;
		double x = 0;
		double y = 0;

		while (cont) {
			if ((code1 | code2) == 0) {
				// Bitwise OR is 0. Trivially accept and get out of loop
				accept = true;
				cont = false;
			} else if ((code1 & code2) != 0) {
				// Bitwise AND is not 0. Trivially reject and get out of loop
				cont = false;
			} else {
				// failed both tests, so calculate the line segment to clip
				// from an outside point to an intersection with clip edge

				// At least one endpoint is outside the clip rectangle; pick it.
				int code3 = code1 != 0 ? code1 : code2;

				// Now find the intersection point;
				// use formulas y = y0 + slope * (x - x0), x = x0 + (1 / slope) * (y - y0)
				if ((code3 & MathConstants.COHEN_SUTHERLAND_TOP) != 0) {
					// point is above the clip rectangle
					x = x0 + (x1 - x0) * (rymax - y0) / (y1 - y0);
					y = rymax;
				} else if ((code3 & MathConstants.COHEN_SUTHERLAND_BOTTOM) != 0) {
					// point is below the clip rectangle
					x = x0 + (x1 - x0) * (rymin - y0) / (y1 - y0);
					y = rymin;
				} else if ((code3 & MathConstants.COHEN_SUTHERLAND_RIGHT) != 0) {
					// point is to the right of clip rectangle
					y = y0 + (y1 - y0) * (rxmax - x0) / (x1 - x0);
					x = rxmax;
				} else if ((code3 & MathConstants.COHEN_SUTHERLAND_LEFT) != 0) {
					// point is to the left of clip rectangle
					y = y0 + (y1 - y0) * (rxmin - x0) / (x1 - x0);
					x = rxmin;
				} else {
					code3 = 0;
				}

				if (code3 != 0) {
					// Now we move outside point to intersection point to clip
					// and get ready for next pass.
					if (code3 == code1) {
						x0 = x;
						y0 = y;
						code1 = MathUtil.getCohenSutherlandCode(x0, y0, rxmin, rymin, rxmax, rymax);
					} else {
						x1 = x;
						y1 = y;
						code2 = MathUtil.getCohenSutherlandCode(x1, y1, rxmin, rymin, rxmax, rymax);
					}
				}
			}
		}
		if (accept) {
			set(x0, y0, x1, y1);
		}
		return accept;
	}

	/** {@inheritDoc}
	 *
	 * <p>This function uses the equal-to-zero test with the error {@link Math#ulp(double)}.
	 *
	 * @see MathUtil#isEpsilonZero(double)
	 */
	@Override
	default boolean contains(double x, double y) {
		return MathUtil.isEpsilonZero(
				computeDistanceSquaredSegmentPoint(
						getX1(), getY1(),
						getX2(), getY2(),
						x, y));
	}

	@Pure
	@Override
	default boolean contains(Rectangle2afp<?, ?, ?, ?, ?, B> rectangle) {
		return false;
	}

	@Pure
	@Override
	default boolean contains(Shape2afp<?, ?, ?, ?, ?, B> shape) {
		return false;
	}

	@Pure
	@Override
	default boolean equalsToShape(IT shape) {
		if (shape == null) {
			return false;
		}
		if (shape == this) {
			return true;
		}
		return getX1() == shape.getX1()
			&& getY1() == shape.getY1()
			&& getX2() == shape.getX2()
			&& getY2() == shape.getY2();
	}

	@Pure
	@Override
	default P getClosestPointTo(Circle2afp<?, ?, ?, ?, ?, ?> circle) {
		assert circle != null : "Circle must be not null"; //$NON-NLS-1$
		return getClosestPointTo(circle.getCenter());
	}

	@Override
	@Unefficient
	default P getClosestPointTo(Ellipse2afp<?, ?, ?, ?, ?, ?> ellipse) {
		assert ellipse != null : "Ellipse must be not null"; //$NON-NLS-1$
		final P point = getGeomFactory().newPoint();
		Path2afp.getClosestPointTo(getPathIterator(), ellipse.getPathIterator(), point);
		return point;
	}

	@Override
	default P getClosestPointTo(OrientedRectangle2afp<?, ?, ?, ?, ?, ?> orientedRectngle) {
		assert orientedRectngle != null : "Oriented rectangle must be not null"; //$NON-NLS-1$
		// Change of basis to the local basis of the oriented rectangle
		final double cx = orientedRectngle.getCenterX();
		final double cy = orientedRectngle.getCenterY();
		final double sx1 = getX1() - cx;
		final double sy1 = getY1() - cy;
		final double sx2 = getX2() - cx;
		final double sy2 = getY2() - cy;
		final double rx = orientedRectngle.getFirstAxisX();
		final double ry = orientedRectngle.getFirstAxisY();
		double x1 = OrientedRectangle2afp.projectVectorOnOrientedRectangleRAxis(rx, ry, sx1, sy1);
		double y1 = OrientedRectangle2afp.projectVectorOnOrientedRectangleSAxis(rx, ry, sx1, sy1);
		final double x2 = OrientedRectangle2afp.projectVectorOnOrientedRectangleRAxis(rx, ry, sx2, sy2);
		final double y2 = OrientedRectangle2afp.projectVectorOnOrientedRectangleSAxis(rx, ry, sx2, sy2);
		final double extent1 = orientedRectngle.getFirstAxisExtent();
		final double extent2 = orientedRectngle.getSecondAxisExtent();
		final P point = getGeomFactory().newPoint();
		computeClosestPointToRectangle(x1, y1, x2, y2, -extent1, -extent2, 2. * extent1, 2. * extent2, point);
		// Invert change of basis
		x1 = cx + point.getX() * rx - point.getY() * ry;
		y1 = cy + point.getX() * ry + point.getY() * rx;
		point.set(x1, y1);
		return point;
	}

	@Override
	default P getClosestPointTo(Parallelogram2afp<?, ?, ?, ?, ?, ?> parallelogram) {
		assert parallelogram != null : "Parallelogram must be not null"; //$NON-NLS-1$
		// Change of basis to the local basis of the oriented rectangle
		final double cx = parallelogram.getCenterX();
		final double cy = parallelogram.getCenterY();
		final double sx1 = getX1() - cx;
		final double sy1 = getY1() - cy;
		final double sx2 = getX2() - cx;
		final double sy2 = getY2() - cy;
		final double rx = parallelogram.getFirstAxisX();
		final double ry = parallelogram.getFirstAxisY();
		final double sx = parallelogram.getSecondAxisX();
		final double sy = parallelogram.getSecondAxisY();
		final double extent1 = parallelogram.getFirstAxisExtent();
		final double extent2 = parallelogram.getSecondAxisExtent();
		final P point = getGeomFactory().newPoint();
		//
		final double x1 = Parallelogram2afp.projectVectorOnParallelogramRAxis(rx, ry, sx, sy, sx1, sy1);
		final double y1 = Parallelogram2afp.projectVectorOnParallelogramSAxis(rx, ry, sx, sy, sx1, sy1);
		final double x2 = Parallelogram2afp.projectVectorOnParallelogramRAxis(rx, ry, sx, sy, sx2, sy2);
		final double y2 = Parallelogram2afp.projectVectorOnParallelogramSAxis(rx, ry, sx, sy, sx2, sy2);
		final int code1 = MathUtil.getCohenSutherlandCode(x1, y1, -extent1, -extent2, extent1, extent2);
		final int code2 = MathUtil.getCohenSutherlandCode(x2, y2, -extent1, -extent2, extent1, extent2);
		final Point2D<?, ?> tmp1 = new InnerComputationPoint2afp();
		final Point2D<?, ?> tmp2 = new InnerComputationPoint2afp();
		final int zone = Rectangle2afp.reduceCohenSutherlandZoneRectangleSegment(
				-extent1, -extent2, extent1, extent2,
				x1, y1, x2, y2,
				code1, code2,
				tmp1, tmp2);
		if ((zone & MathConstants.COHEN_SUTHERLAND_LEFT) != 0) {
			computeClosestPointToSegment(
					sx1, sy1, sx2, sy2,
					-rx * extent1 + sx * extent2, -ry * extent1 + sy * extent2,
					-rx * extent1 - sx * extent2, -ry * extent1 - sy * extent2,
					point);
		} else if ((zone & MathConstants.COHEN_SUTHERLAND_RIGHT) != 0) {
			computeClosestPointToSegment(
					sx1, sy1, sx2, sy2,
					rx * extent1 + sx * extent2, ry * extent1 + sy * extent2,
					rx * extent1 - sx * extent2, ry * extent1 - sy * extent2,
					point);
		} else if ((zone & MathConstants.COHEN_SUTHERLAND_BOTTOM) != 0) {
			computeClosestPointToSegment(
					sx1, sy1, sx2, sy2,
					-rx * extent1 - sx * extent2, -ry * extent1 - sy * extent2,
					rx * extent1 - sx * extent2, ry * extent1 - sy * extent2,
					point);
		} else if ((zone & MathConstants.COHEN_SUTHERLAND_TOP) != 0) {
			computeClosestPointToSegment(
					sx1, sy1, sx2, sy2,
					-rx * extent1 + sx * extent2, -ry * extent1 + sy * extent2,
					rx * extent1 + sx * extent2, ry * extent1 + sy * extent2,
					point);
		} else {
			computeClosestPointToPoint(
					tmp1.getX(), tmp1.getY(), tmp2.getX(), tmp2.getY(),
					0, 0,
					point);
		}
		// Invert change of basis
		point.add(cx, cy);
		return point;
	}

	@Override
	@Unefficient
	default P getClosestPointTo(Path2afp<?, ?, ?, ?, ?, ?> path) {
		assert path != null : "Path must be not null"; //$NON-NLS-1$
		final P point = getGeomFactory().newPoint();
		Path2afp.getClosestPointTo(getPathIterator(), path.getPathIterator(), point);
		return point;
	}

	@Pure
	@Override
	default P getClosestPointTo(Point2D<?, ?> pt) {
		assert pt != null : "Point must be not null"; //$NON-NLS-1$
		final P point = getGeomFactory().newPoint();
		Segment2afp.computeClosestPointToPoint(
				getX1(), getY1(),
				getX2(), getY2(),
				pt.getX(), pt.getY(),
				point);
		return point;
	}

	@Pure
	@Override
	default P getClosestPointTo(Rectangle2afp<?, ?, ?, ?, ?, ?> rectangle) {
		assert rectangle != null : "Rectangle must be not null"; //$NON-NLS-1$
		final P point = getGeomFactory().newPoint();
		computeClosestPointToRectangle(getX1(), getY1(), getX2(), getY2(),
				rectangle.getMinX(), rectangle.getMinY(), rectangle.getWidth(), rectangle.getHeight(), point);
		return point;
	}

	@Override
	@Unefficient
	default P getClosestPointTo(RoundRectangle2afp<?, ?, ?, ?, ?, ?> roundRectangle) {
		assert roundRectangle != null : "Round rectangle must be not null"; //$NON-NLS-1$
		final P point = getGeomFactory().newPoint();
		Path2afp.getClosestPointTo(getPathIterator(), roundRectangle.getPathIterator(), point);
		return point;
	}

	@Pure
	@Override
	default P getClosestPointTo(Segment2afp<?, ?, ?, ?, ?, ?> segment) {
		assert segment != null : "Segment must be not null"; //$NON-NLS-1$
		final P point = getGeomFactory().newPoint();
		computeClosestPointToSegment(getX1(), getY1(), getX2(), getY2(),
				segment.getX1(), segment.getY1(), segment.getX2(), segment.getY2(), point);
		return point;
	}

	@Pure
	@Override
	default P getClosestPointTo(Triangle2afp<?, ?, ?, ?, ?, ?> triangle) {
		assert triangle != null : "Triangle must be not null"; //$NON-NLS-1$
		final double[] segments = new double[] {
			triangle.getX1(), triangle.getY1(), triangle.getX2(), triangle.getY2(),
			triangle.getX2(), triangle.getY2(), triangle.getX3(), triangle.getY3(),
			triangle.getX3(), triangle.getY3(), triangle.getX1(), triangle.getY1(),
		};
		final P point = getGeomFactory().newPoint();
		final P tmp = getGeomFactory().newPoint();
		double minDistance = Double.POSITIVE_INFINITY;
		final double ox1 = getX1();
		final double oy1 = getY1();
		final double ox2 = getX2();
		final double oy2 = getY2();
		for (int i = 0; i < segments.length;) {
			final double x1 = segments[i++];
			final double y1 = segments[i++];
			final double x2 = segments[i++];
			final double y2 = segments[i++];
			final double distance = computeClosestPointToSegment(ox1, oy1, ox2, oy2, x1, y1, x2, y2, tmp);
			if (distance < minDistance) {
				minDistance = distance;
				point.set(tmp);
			}
		}
		return point;
	}

	@Pure
	@Override
	default double getDistanceL1(Point2D<?, ?> pt) {
		assert pt != null : "Point must be not null"; //$NON-NLS-1$
		double ratio = computeProjectedPointOnLine(pt.getX(), pt.getY(), getX1(), getY1(), getX2(), getY2());
		ratio = MathUtil.clamp(ratio, 0, 1);
		final double vx = (getX2() - getX1()) * ratio;
		final double vy = (getY2() - getY1()) * ratio;
		return Math.abs(getX1() + vx - pt.getX())
				+ Math.abs(getY1() + vy - pt.getY());
	}

	@Pure
	@Override
	default double getDistanceLinf(Point2D<?, ?> pt) {
		assert pt != null : "Point must be not null"; //$NON-NLS-1$
		double ratio = computeProjectedPointOnLine(pt.getX(), pt.getY(), getX1(), getY1(), getX2(), getY2());
		ratio = MathUtil.clamp(ratio, 0, 1);
		final double vx = (getX2() - getX1()) * ratio;
		final double vy = (getY2() - getY1()) * ratio;
		return Math.max(
				Math.abs(this.getX1() + vx - pt.getX()),
				Math.abs(this.getY1() + vy - pt.getY()));
	}

	@Pure
	@Override
	default double getDistanceSquared(Point2D<?, ?> pt) {
		assert pt != null : "Point must be not null"; //$NON-NLS-1$
		return computeDistanceSquaredSegmentPoint(
				getX1(), getY1(),
				getX2(), getY2(),
				pt.getX(), pt.getY());
	}

	@Override
	@Pure
	default double getDistanceSquared(Segment2afp<?, ?, ?, ?, ?, ?> segment) {
		assert segment != null : "Segment must be not null"; //$NON-NLS-1$
		return computeDistanceSquaredSegmentSegment(getX1(), getY1(), getX2(), getY2(),
				segment.getX1(), segment.getY1(), segment.getX2(), segment.getY2());
	}

	@Pure
	@Override
	default P getFarthestPointTo(Point2D<?, ?> pt) {
		assert pt != null : "Point must be not null"; //$NON-NLS-1$
		final P point = getGeomFactory().newPoint();
		Segment2afp.computeFarthestPointToPoint(
				getX1(), getY1(),
				getX2(), getY2(),
				pt.getX(), pt.getY(),
				point);
		return point;
	}

	/** Replies the length of the segment.
	 *
	 * @return the length.
	 */
	@Pure
	default double getLength() {
		return Point2D.getDistancePointPoint(getX1(), getY1(), getX2(), getY2());
	}

	/** Replies the squared length of the segment.
	 *
	 * @return the squared length.
	 */
	@Pure
	default double getLengthSquared() {
		return Point2D.getDistanceSquaredPointPoint(getX1(), getY1(), getX2(), getY2());
	}

	/** Replies the first point.
	 *
	 * @return the first point.
	 */
	@Pure
	default P getP1() {
		return getGeomFactory().newPoint(getX1(), getY1());
	}

	/** Replies the second point.
	 *
	 * @return the second point.
	 */
	@Pure
	default P getP2() {
		return getGeomFactory().newPoint(getX2(), getY2());
	}

	@Pure
	@Override
	default PathIterator2afp<IE> getPathIterator(Transform2D transform) {
		return new SegmentPathIterator<>(this, transform);
	}

	/** Replies the X of the first point.
	 *
	 * @return the x of the first point.
	 */
	@Pure
	double getX1();

	/** Replies the X of the second point.
	 *
	 * @return the x of the second point.
	 */
	@Pure
	double getX2();

	/** Replies the Y of the first point.
	 *
	 * @return the y of the first point.
	 */
	@Pure
	double getY1();

	/** Replies the Y of the second point.
	 *
	 * @return the y of the second point.
	 */
	@Pure
	double getY2();

	@Pure
	@Override
	default boolean intersects(Circle2afp<?, ?, ?, ?, ?, ?> circle) {
		assert circle != null : "Circle must be not null"; //$NON-NLS-1$
		return Circle2afp.intersectsCircleSegment(
				circle.getX(), circle.getY(),
				circle.getRadius(),
				getX1(), getY1(),
				getX2(), getY2());
	}

	@Pure
	@Override
	default boolean intersects(Ellipse2afp<?, ?, ?, ?, ?, ?> ellipse) {
		assert ellipse != null : "Ellipse must be not null"; //$NON-NLS-1$
		return Ellipse2afp.intersectsEllipseSegment(
				ellipse.getMinX(), ellipse.getMinY(),
				ellipse.getWidth(), ellipse.getHeight(),
				getX1(), getY1(),
				getX2(), getY2(), false);
	}

	@Pure
	@Override
	default boolean intersects(MultiShape2afp<?, ?, ?, ?, ?, ?, ?> multishape) {
		assert multishape != null : "MultiShape must be not null"; //$NON-NLS-1$
		return multishape.intersects(this);
	}

	@Pure
	@Override
	default boolean intersects(OrientedRectangle2afp<?, ?, ?, ?, ?, ?> orientedRectangle) {
		assert orientedRectangle != null : "Oriented rectangle must be not null"; //$NON-NLS-1$
		return OrientedRectangle2afp.intersectsOrientedRectangleSegment(
				orientedRectangle.getCenterX(), orientedRectangle.getCenterY(),
				orientedRectangle.getFirstAxisX(), orientedRectangle.getFirstAxisY(), orientedRectangle.getFirstAxisExtent(),
				orientedRectangle.getSecondAxisExtent(),
				getX1(), getY1(), getX2(), getY2());
	}

	@Pure
	@Override
	default boolean intersects(Parallelogram2afp<?, ?, ?, ?, ?, ?> parallelogram) {
		assert parallelogram != null : "Parallelogram must be not null"; //$NON-NLS-1$
		return Parallelogram2afp.intersectsParallelogramSegment(
				parallelogram.getCenterX(), parallelogram.getCenterY(),
				parallelogram.getFirstAxisX(), parallelogram.getFirstAxisY(), parallelogram.getFirstAxisExtent(),
				parallelogram.getSecondAxisX(), parallelogram.getSecondAxisY(), parallelogram.getSecondAxisExtent(),
				getX1(), getY1(), getX2(), getY2());
	}

	@Pure
	@Override
	default boolean intersects(PathIterator2afp<?> iterator) {
		assert iterator != null : "Iterator must be not null"; //$NON-NLS-1$
		final int mask = iterator.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
		final int crossings = Path2afp.computeCrossingsFromSegment(
				0,
				iterator,
				getX1(), getY1(), getX2(), getY2(),
				CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
		return crossings == MathConstants.SHAPE_INTERSECTS
				|| (crossings & mask) != 0;

	}

	@Pure
	@Override
	default boolean intersects(Rectangle2afp<?, ?, ?, ?, ?, ?> rectangle) {
		assert rectangle != null : "Rectangle must be not null"; //$NON-NLS-1$
		return Rectangle2afp.intersectsRectangleSegment(
				rectangle.getMinX(), rectangle.getMinY(),
				rectangle.getMaxX(), rectangle.getMaxY(),
				getX1(), getY1(),
				getX2(), getY2());
	}

	@Pure
	@Override
	default boolean intersects(RoundRectangle2afp<?, ?, ?, ?, ?, ?> roundRectangle) {
		assert roundRectangle != null : "Round rectangle must be not null"; //$NON-NLS-1$
		return RoundRectangle2afp.intersectsRoundRectangleSegment(
				roundRectangle.getMinX(), roundRectangle.getMinY(), roundRectangle.getMaxX(),
				roundRectangle.getMaxY(), roundRectangle.getArcWidth(), roundRectangle.getArcHeight(),
				getX1(), getY1(), getX2(), getY2());
	}

	@Pure
	@Override
	default boolean intersects(Segment2afp<?, ?, ?, ?, ?, ?> segment) {
		assert segment != null : "Segment must be not null"; //$NON-NLS-1$
		return intersectsSegmentSegmentWithEnds(
				getX1(), getY1(),
				getX2(), getY2(),
				segment.getX1(), segment.getY1(),
				segment.getX2(), segment.getY2());
	}

	@Pure
	@Override
	default boolean intersects(Triangle2afp<?, ?, ?, ?, ?, ?> triangle) {
		assert triangle != null : "Segment must be not null"; //$NON-NLS-1$
		return Triangle2afp.intersectsTriangleSegment(
				triangle.getX1(), triangle.getY1(),
				triangle.getX2(), triangle.getY2(),
				triangle.getX3(), triangle.getY3(),
				getX1(), getY1(),
				getX2(), getY2());
	}

	@Override
	default boolean isEmpty() {
		return getX1() == getX2() && getY1() == getY2();
	}

	/** Change the line.
	 *
	 * @param x1 x coordinate of the first point.
	 * @param y1 y coordinate of the first point.
	 * @param x2 x coordinate of the second point.
	 * @param y2 y coordinate of the second point.
	 */
	// No default implementation for ensuring atomic change.
	void set(double x1, double y1, double x2, double y2);

	@Override
	default void set(IT shape) {
		assert shape != null : "Shape must be not null"; //$NON-NLS-1$
		set(shape.getX1(), shape.getY1(), shape.getX2(), shape.getY2());
	}

	/** Change the line.
	 *
	 * @param firstPoint the first point.
	 * @param secondPoint the second point.
	 */
	default void set(Point2D<?, ?> firstPoint, Point2D<?, ?> secondPoint) {
		assert firstPoint != null : "First point must be not null"; //$NON-NLS-1$
		assert secondPoint != null : "Second point must be not null"; //$NON-NLS-1$
		set(firstPoint.getX(), firstPoint.getY(), secondPoint.getX(), secondPoint.getY());
	}

	/** Change the first point.
	 *
	 * @param x x coordinate of the first point.
	 * @param y y coordinate of the first point.
	 */
	default void setP1(double x, double y) {
		set(x, y, getX2(), getY2());
	}

	/** Change the first point.
	 *
	 * @param pt the first point.
	 */
	default void setP1(Point2D<?, ?> pt) {
		assert pt != null : "Point must be not null"; //$NON-NLS-1$
		set(pt.getX(), pt.getY(), getX2(), getY2());
	}

	/** Change the second point.
	 *
	 * @param x x coordinate of the second point.
	 * @param y y coordinate of the second point.
	 */
	default void setP2(double x, double y) {
		set(getX1(), getY1(), x, y);
	}

	/** Change the second point.
	 *
	 * @param pt the second point.
	 */
	default void setP2(Point2D<?, ?> pt) {
		assert pt != null : "Point must be not null"; //$NON-NLS-1$
		set(getX1(), getY1(), pt.getX(), pt.getY());
	}

	/** Sets a new value in the X of the first point.
	 *
	 * @param x the new value double x
	 */
	void setX1(double x);

	/**Sets a new value in the X of the second point.
	 *
	 * @param x the new value double x
	 */
	void setX2(double x);

	/**Sets a new value in the Y of the first point.
	 *
	 * @param y the new value double y
	 */
	void setY1(double y);

	/**Sets a new value in the Y of the second point.
	 *
	 * @param y the new value double y
	 */
	void setY2(double y);

	@Pure
	@Override
	default void toBoundingBox(B box) {
		assert box != null : "Rectangle must be not null"; //$NON-NLS-1$
		box.setFromCorners(
				this.getX1(),
				this.getY1(),
				this.getX2(),
				this.getY2());
	}

	/** Transform the current segment.
	 * This function changes the current segment.
	 *
	 * @param transform is the affine transformation to apply.
	 * @see #createTransformedShape
	 */
	default void transform(Transform2D transform) {
		assert transform != null : "Transformation must be not null"; //$NON-NLS-1$
		final Point2D<?, ?> p = new InnerComputationPoint2afp(getX1(), getY1());
		transform.transform(p);
		final double x1 = p.getX();
		final double y1 = p.getY();
		p.set(getX2(), getY2());
		transform.transform(p);
		set(x1, y1, p.getX(), p.getY());
	}

	@Override
	default void translate(double dx, double dy) {
		set(getX1() + dx, getY1() + dy, getX2() + dx, getY2() + dy);
	}

}
