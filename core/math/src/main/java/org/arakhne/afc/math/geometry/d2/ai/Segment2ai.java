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
import java.util.NoSuchElementException;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.CrossingComputationType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.GeomFactory;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;

/** Fonctional interface that represented a 2D segment/line on a plane.
 *
 * @param <ST> is the type of the general implementation.
 * @param <IT> is the type of the implementation of this shape.
 * @param <IE> is the type of the path elements.
 * @param <P> is the type of the points.
 * @param <V> is the type of the vectors.
 * @param <B> is the type of the bounding boxes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface Segment2ai<
		ST extends Shape2ai<?, ?, IE, P, V, B>,
		IT extends Segment2ai<?, ?, IE, P, V, B>,
		IE extends PathElement2ai,
		P extends Point2D<? super P, ? super V>,
		V extends Vector2D<? super V, ? super P>,
		B extends Rectangle2ai<?, ?, IE, P, V, B>>
		extends Shape2ai<ST, IT, IE, P, V, B> {

	/** Replies the closest point in a circle to a point.
	 *
	 * @param ax is the x-coordinate of the first point of the segment
	 * @param ay is the y-coordinate of the first point of the segment
	 * @param bx is the x-coordinate of the second point of the segment
	 * @param by is the y-coordinate of the second point of the segment
	 * @param px is the x-coordinate of the point
	 * @param py is the x-coordinate of the point
	 * @param result the closest point in the segment to the point.
	 */
	@Pure
	static void computeClosestPointTo(int ax, int ay, int bx, int by, int px, int py, Point2D<?, ?> result) {
		assert result != null : "Result must be not be null"; //$NON-NLS-1$

		// Special case
		//    0 1 2 3 4 5 6 7 8 9 10
		// 5) | | | | | | | | | | |X|
		// 4) | | |O| | | | | |X|X| |
		// 3) | | | | | | |X|X| | | |
		// 2) | | | | |X|X| | | | | |
		// 1) | | |X|X| | | | | | | |
		// 0) |X|X| | | | | | | | | |
		//
		// The closest point to point O is (4;2) even
		// if the distance is increasing between (2;1)
		// and (4;2). The algo must take this special
		// case into account.

		int minDist = Integer.MAX_VALUE;
		boolean oneBestFound = false;
		result.set(ax, ay);
		// Only for internal use
		final InnerComputationPoint2ai cp = new InnerComputationPoint2ai();
		final BresenhamLineIterator<InnerComputationPoint2ai, InnerComputationVector2ai> iterator =
				new BresenhamLineIterator<>(
				InnerComputationGeomFactory.SINGLETON, ax, ay, bx, by);
		while (iterator.hasNext()) {
			iterator.next(cp);
			final int a = Math.abs(px - cp.ix());
			final int b = Math.abs(py - cp.iy());
			final int d = a * a + b * b;
			if (d == 0) {
				// We are sure that the closest point was found
				result.set(cp);
				return;
			}
			if (d > minDist) {
				// here we have found a good candidate, but
				// but due to the rasterization the optimal solution
				// may be one pixel after the already found.
				// See the special case configuration at the beginning
				// of this function.
				if (oneBestFound) {
					return;
				}
				oneBestFound = true;
			} else {
				minDist = d;
				result.set(cp);
				// here we have found a good candidate, but
				// but due to the rasterization the optimal solution
				// may be one pixel after the already found.
				// See the special case configuration at the beginning
				// of this function.
				if (oneBestFound) {
					return;
				}
			}
		}
	}

	/** Replies the farthest point in a circle to a point.
	 *
	 * @param ax is the x-coordinate of the first point of the segment
	 * @param ay is the y-coordinate of the first point of the segment
	 * @param bx is the x-coordinate of the second point of the segment
	 * @param by is the y-coordinate of the second point of the segment
	 * @param px is the x-coordinate of the point
	 * @param py is the x-coordinate of the point
	 * @param result the farthest point in the segment to the point.
	 */
	@Pure
	static void computeFarthestPointTo(int ax, int ay, int bx, int by, int px, int py, Point2D<?, ?> result) {
		assert result != null : "Result must be not be null"; //$NON-NLS-1$
		final int v1x = px - ax;
		final int v1y = py - ay;
		final int v2x = px - bx;
		final int v2y = py - by;
		if ((v1x * v1x + v1y * v1y) >= (v2x * v2x + v2y * v2y)) {
			result.set(ax, ay);
		} else {
			result.set(bx, by);
		}
	}

	/**
	 * Replies on which side of a line the given point is located.
	 *
	 * <p>A return value of 1 indicates that the line segment must turn in the direction
	 * that takes the positive X axis towards the negative Y axis. In the default
	 * coordinate system used by Java 2D, this direction is counterclockwise.
	 *
	 * <p>A return value of -1 indicates that the line segment must turn in the direction that takes the
	 * positive X axis towards the positive Y axis. In the default coordinate system by Java 2D, this direction is clockwise.
	 *
	 * <p>A return value of 0 indicates that the point lies exactly on the line segment.
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
	 * @return an integer that indicates the position of the third specified coordinates with respect to
	 *     the line segment formed by the first two specified coordinates.
	 * @see MathUtil#isEpsilonZero(double)
	 */
	@Pure
	static int computeSideLinePoint(int x1, int y1, int x2, int y2, int px, int py) {
		final int segmentX = x2 - x1;
		final int segmentY = y2 - y1;
		final int targetX = px - x1;
		final int targetY = py - y1;
		final int side = segmentX * targetY - segmentY * targetX;
		return (side < 0) ? -1 : ((side > 0) ? 1 : 0);
	}

	/**
	 * Calculates the number of times the line from (x0, y0) to (x1, y1)
	 * crosses the ellipse (ex0, ey0) to (ex1, ey1) extending to the right.
	 *
	 * <p>When the line (x0;y0) to (x1;y1) is crossing one of the up or
	 * bottom borders of the shadow of the circle, the crossings
	 * count is increased or decreased, depending if the line is
	 * going down or up, respectively.
	 * In the following figure, the circle is represented.
	 * The "shadow" is the projection of the circle on the right.
	 * The red lines represent the up and bottom borders.
	 *
	 * <p><a href="doc-files/crossing_circle.png"><img src="doc-files/crossing_circle.png" width="300"/></a>
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
			int cx, int cy,
			int radius,
			int x0, int y0,
			int x1, int y1) {
		assert radius >= 0 : "Redius must be positive or zero"; //$NON-NLS-1$

		int numCrosses = crossings;

		final int xmin = cx - Math.abs(radius);
		final int xmax = cx + Math.abs(radius);
		final int ymin = cy - Math.abs(radius);
		final int ymax = cy + Math.abs(radius);

		// The line is entirely on the top or on the bottom of the shadow
		if (y0 < ymin && y1 < ymin) {
			return numCrosses;
		}
		if (y0 > ymax && y1 > ymax) {
			return numCrosses;
		}
		// The line is entierly on the left of the shadow.
		if (x0 < xmin && x1 < xmin) {
			return numCrosses;
		}

		if (x0 > xmax && x1 > xmax) {
			// The line is entirely at the right of the center of the shadow.
			// We may use the standard "rectangle" crossing computation
			if (y0 < y1) {
				if (y0 < ymin) {
					++numCrosses;
				}
				if (y1 > ymax) {
					++numCrosses;
				}
			} else {
				if (y1 < ymin) {
					--numCrosses;
				}
				if (y0 > ymax) {
					--numCrosses;
				}
			}
		} else if (Circle2ai.intersectsCircleSegment(
				cx, cy, radius,
				x0, y0, x1, y1)) {
			return MathConstants.SHAPE_INTERSECTS;
		} else {
			numCrosses = computeCrossingsFromPoint(numCrosses, cx, ymin, x0, y0, x1, y1, true, false);
			numCrosses = computeCrossingsFromPoint(numCrosses, cx, ymax, x0, y0, x1, y1, false, true);
		}

		return numCrosses;
	}

	/**
	 * Calculates the number of times the line from (x0, y0) to (x1, y1)
	 * crosses the segment (sx0, sy0) to (sx1, sy1) extending to the right.
	 *
	 * <p>When the line (x0;y0) to (x1;y1) is crossing one of the up or
	 * bottom borders of the shadow of the segment, the crossings
	 * count is increased or decreased, depending if the line is
	 * going down or up, respectively.
	 * In the following figure, the segment is represented.
	 * The "shadow" is the projection of the segment on the right.
	 * The red lines represent the up and bottom borders.
	 *
	 * <p><a href="doc-files/crossing_segment.png"><img src="doc-files/crossing_segment.png" width="300"/></a>
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
	 * @return the crossing, or {@link MathConstants#SHAPE_INTERSECTS}.
	 */
	@Pure
	@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:cyclomaticcomplexity",
			"checkstyle:npathcomplexity"})
	static int computeCrossingsFromSegment(
			int crossings,
			int sx1, int sy1,
			int sx2, int sy2,
			int x0, int y0,
			int x1, int y1) {
		/* CAUTION:
		 * --------
		 * In the comment of this function, it is assumed that y0<=y1,
		 * to simplify the explanations.
		 * The source code is handled y0<=y1 and y0>y1.
		 */
		int numCrosses = crossings;

		final int xmin = Math.min(sx1, sx2);
		final int xmax = Math.max(sx1, sx2);
		final int ymin = Math.min(sy1, sy2);
		final int ymax = Math.max(sy1, sy2);

		// The line is entirely below or up to the shadow of the segment
		if (y0 < ymin && y1 < ymin) {
			return numCrosses;
		}
		if (y0 > ymax && y1 > ymax) {
			return numCrosses;
		}
		// The line is entirely at te left of the segment
		if (x0 < xmin && x1 < xmin) {
			return numCrosses;
		}
		if (x0 > xmax && x1 > xmax) {
			// The line is entirely at the right of the shadow
			if (y0 < y1) {
				if (y0 < ymin) {
					++numCrosses;
				}
				if (y1 > ymax) {
					++numCrosses;
				}
			} else {
				if (y1 < ymin) {
					--numCrosses;
				}
				if (y0 > ymax) {
					--numCrosses;
				}
			}
		} else if (intersectsSegmentSegment(x0, y0, x1, y1, sx1, sy1, sx2, sy2, true, true, null)) {
			return MathConstants.SHAPE_INTERSECTS;
		} else {
			// The line is intersectly partly the bounding rectangle of the segment.
			// We must determine on which side of the segment the points of the line are.
			// If side1 is positive, the first point of the line is on the side of the shadow, relatively to the segment
			// If it is negative, the first point is on the opposite side of the shadow, relatively to the segment.
			// If it is nul, the point is on line colinear to the segment.
			// Same for side2 and the second point of the line.
			final int side1;
			final int side2;
			final boolean firstIsTop = sy1 <= sy2;
			if (firstIsTop) {
				side1 = computeSideLinePoint(sx1, sy1, sx2, sy2, x0, y0);
				side2 = computeSideLinePoint(sx1, sy1, sx2, sy2, x1, y1);
			} else {
				side1 = computeSideLinePoint(sx2, sy2, sx1, sy1, x0, y0);
				side2 = computeSideLinePoint(sx2, sy2, sx1, sy1, x1, y1);
			}
			if (side1 <= 0 || side2 <= 0) {
				// At least one point is on the side of the shadow.
				// Now we compute the intersection with the up and bottom borders.
				// Intersection is obtained by computed the crossing value from
				// the two points of the segment.
				final int n1 = computeCrossingsFromPoint(0, sx1, sy1, x0, y0, x1, y1, firstIsTop, !firstIsTop);
				final int n2 = computeCrossingsFromPoint(0, sx2, sy2, x0, y0, x1, y1, !firstIsTop, firstIsTop);

				// The total crossing value must be updated with the border's crossing values.
				numCrosses += n1 + n2;
			}
		}

		return numCrosses;
	}

	/**
	 * Accumulate the number of times the line crosses the shadow
	 * extending to the right of the rectangle.
	 *
	 * <p>When the line (x0;y0) to (x1;y1) is intersecting the rectangle,
	 * the value {@link MathConstants#SHAPE_INTERSECTS} is returned.
	 * When the line (x0;y0) to (x1;y1) is crossing one of the up or
	 * bottom borders of the shadow of the rectangle, the crossings
	 * count is increased or decreased, depending if the line is
	 * going down or up, respectively.
	 * In the following figure, the rectangle is represented.
	 * The "shadow" is the projection of the rectangle on the right.
	 * The red lines represent the up and bottom borders.
	 *
	 * <p><a href="doc-files/crossing_rect.png"><img src="doc-files/crossing_rect.png" width="300"/></a>
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
			"checkstyle:booleanexpressioncomplexity", "checkstyle:nestedifdepth"})
	static int computeCrossingsFromRect(
			int crossings,
			int rxmin, int rymin,
			int rxmax, int rymax,
			int x0, int y0,
			int x1, int y1) {
		int numCrosses = crossings;
		// The line is horizontal, only SHAPE_INTERSECT may be replies
		if (y0 == y1) {
			if (y0 >= rymin && y0 <= rymax
					&& (x0 >= rxmin || x1 >= rxmin)
					&& (x0 <= rxmax || x1 <= rxmax)) {
				return MathConstants.SHAPE_INTERSECTS;
			}
			return crossings;
		}
		// The line is entirely at the top or at the bottom of the rectangle
		if (y0 > rymax && y1 > rymax) {
			return numCrosses;
		}
		if (y0 < rymin && y1 < rymin) {
			return numCrosses;
		}
		// The line is entirely on the left of the rectangle
		if (x0 < rxmin && x1 < rxmin) {
			return numCrosses;
		}
		if (x0 > rxmax && x1 > rxmax) {
			// Line is entirely to the right of the rect
			// and the vertical ranges of the two overlap by a non-empty amount
			// Thus, this line segment is partially in the "right-shadow"
			// Path may have done a complete crossing
			// Or path may have entered or exited the right-shadow
			if (y0 < y1) {
				// y-increasing line segment...
				// We know that y0 < rymax and y1 > rymin
				if (y0 < rymin) {
					++numCrosses;
				}
				if (y1 > rymax) {
					++numCrosses;
				}
			} else if (y1 < y0) {
				// y-decreasing line segment...
				// We know that y1 < rymax and y0 > rymin
				if (y1 < rymin) {
					--numCrosses;
				}
				if (y0 > rymax) {
					--numCrosses;
				}
			}
		} else {
			// Remaining case:
			// Both x and y ranges overlap by a non-empty amount
			// First do trivial INTERSECTS rejection of the cases
			// where one of the endpoints is inside the rectangle.
			if ((x0 >= rxmin && x0 <= rxmax && y0 >= rymin && y0 <= rymax)
					|| (x1 >= rxmin && x1 <= rxmax && y1 >= rymin && y1 <= rymax)) {
				return MathConstants.SHAPE_INTERSECTS;
			}

			// Otherwise calculate the y intercepts and see where
			// they fall with respect to the rectangle
			final BresenhamLineIterator<InnerComputationPoint2ai, InnerComputationVector2ai> iterator;
			final int ymaxline;
			if (y0 <= y1) {
				iterator = new BresenhamLineIterator<>(
						InnerComputationGeomFactory.SINGLETON, x0, y0, x1, y1);
				ymaxline = y1;
			} else {
				iterator = new BresenhamLineIterator<>(
						InnerComputationGeomFactory.SINGLETON, x1, y1, x0, y0);
				ymaxline = y0;
			}
			final InnerComputationPoint2ai p = new InnerComputationPoint2ai();
			Integer xintercept1 = null;
			Integer xintercept2 = null;
			boolean cont = true;
			while (iterator.hasNext() && cont) {
				iterator.next(p);
				if (p.iy() == rymin && (xintercept1 == null || xintercept1.intValue() > p.ix())) {
					xintercept1 = Integer.valueOf(p.ix());
				}
				if (p.iy() == rymax && (xintercept2 == null || xintercept2.intValue() > p.ix())) {
					xintercept2 = Integer.valueOf(p.ix());
				}
				cont = p.iy() <= ymaxline;
			}

			if (xintercept1 != null && xintercept2 != null) {
				if (xintercept1.intValue() < rxmin && xintercept2.intValue() < rxmin) {
					// the intersection points are entirely on the left
				} else if (xintercept1.intValue() > rxmax && xintercept2.intValue() > rxmax) {
					// the intersection points are entirely on the right
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
				} else {
					return MathConstants.SHAPE_INTERSECTS;
				}
			} else if (xintercept1 != null) {
				// Only the top line of the rectangle is intersecting the segment
				if (xintercept1.intValue() < rxmin) {
					// the intersection point is at entirely on the left
				} else if (xintercept1.intValue() > rxmax) {
					if (y0 < y1) {
						// y-increasing line segment...
						// We know that y0 < rymax and y1 > rymin
						if (y0 <= rymin) {
							++numCrosses;
						}
					} else if (y1 < y0) {
						// y-decreasing line segment...
						// We know that y1 < rymax and y0 > rymin
						if (y1 <= rymin) {
							--numCrosses;
						}
					}
				} else {
					return MathConstants.SHAPE_INTERSECTS;
				}
			} else if (xintercept2 != null) {
				// Only the bottom line of the rectangle is intersecting the segment
				if (xintercept2.intValue() < rxmin) {
					// the intersection point is at entirely on the left
				} else if (xintercept2.intValue() > rxmax) {
					if (y0 < y1) {
						// y-increasing line segment...
						// We know that y0 < rymax and y1 > rymin
						if (y0 <= rymax) {
							++numCrosses;
						}
					} else if (y1 < y0) {
						// y-decreasing line segment...
						// We know that y1 < rymax and y0 > rymin
						if (y1 <= rymax) {
							--numCrosses;
						}
					}
				} else {
					return MathConstants.SHAPE_INTERSECTS;
				}
			}
		}

		return numCrosses;
	}

	/**
	 * Calculates the number of times the line from (x0, y0) to (x1, y1)
	 * crosses the up/bottom borders of the ray extending to the right from (px, py).
	 * +x is returned for a crossing where the Y coordinate is increasing.
	 * -x is returned for a crossing where the Y coordinate is decreasing.
	 * x is the number of border crossed by the lines.
	 *
	 * <p>The borders of the segment are the two side limits between the cells covered by the segment
	 * and the adjacents cells (not covered by the segment).
	 * In the following figure, the point (px;py) is represented.
	 * The "shadow line" is the projection of (px;py) on the right.
	 * The red lines represent the up and bottom borders.
	 *
	 * <p><a href="doc-files/crossing_point.png"><img src="doc-files/crossing_point.png" width="300"/></a>
	 *
	 * @param crossing is the initial value of the crossing.
	 * @param px is the reference point to test.
	 * @param py is the reference point to test.
	 * @param x0 is the first point of the line.
	 * @param y0 is the first point of the line.
	 * @param x1 is the second point of the line.
	 * @param y1 is the secondpoint of the line.
	 * @return the crossing, {@link MathConstants#SHAPE_INTERSECTS}
	 */
	@Pure
	static int computeCrossingsFromPoint(
			int crossing,
			int px, int py,
			int x0, int y0,
			int x1, int y1) {
		return computeCrossingsFromPoint(crossing, px, py, x0, y0, x1, y1, true, true);
	}

	/**
	 * Calculates the number of times the line from (x0, y0) to (x1, y1)
	 * crosses the up/bottom borders of the ray extending to the right from (px, py).
	 * +x is returned for a crossing where the Y coordinate is increasing.
	 * -x is returned for a crossing where the Y coordinate is decreasing.
	 * x is the number of border crossed by the lines.
	 *
	 * <p>The borders of the segment are the two side limits between the cells covered by the segment
	 * and the adjacents cells (not covered by the segment).
	 * In the following figure, the point (px;py) is represented.
	 * The "shadow line" is the projection of (px;py) on the right.
	 * The red lines represent the up and bottom borders.
	 *
	 * <p><a href="doc-files/crossing_point.png"><img src="doc-files/crossing_point.png" width="300"/></a>
	 *
	 * @param crossing is the initial value of the crossing.
	 * @param px is the reference point to test.
	 * @param py is the reference point to test.
	 * @param x0 is the first point of the line.
	 * @param y0 is the first point of the line.
	 * @param x1 is the second point of the line.
	 * @param y1 is the secondpoint of the line.
	 * @param enableTopBorder indicates if the top border must be enabled in the crossing computation.
	 * @param enableBottomBorder indicates if the bottom border must be enabled in the crossing computation.
	 * @return the crossing; or {@link MathConstants#SHAPE_INTERSECTS} if the segment is on the point.
	 */
	@Pure
	@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:cyclomaticcomplexity",
			"checkstyle:npathcomplexity"})
	static int computeCrossingsFromPoint(
			int crossing,
			int px, int py,
			int x0, int y0,
			int x1, int y1,
			boolean enableTopBorder,
			boolean enableBottomBorder) {
		// The line is horizontal, impossible to intersect the borders.
		if (y0 == y1) {
			return crossing;
		}
		// The line does cross the shadow line
		if (py < y0 && py < y1) {
			return crossing;
		}
		if (py > y0 && py > y1) {
			return crossing;
		}
		// The line is entirely on the left of the point
		if (px > x0 && px > x1) {
			return crossing;
		}

		// General case: try to detect crossing

		final BresenhamLineIterator<InnerComputationPoint2ai, InnerComputationVector2ai> iterator =
				new BresenhamLineIterator<>(
				InnerComputationGeomFactory.SINGLETON, x0, y0, x1, y1);

		// Only for internal use.
		final Point2D<?, ?> p = new InnerComputationPoint2ai();
		while (iterator.hasNext()) {
			iterator.next(p);
			if (p.iy() == py) {
				if (p.ix() == px) {
					return MathConstants.SHAPE_INTERSECTS;
				}
				if (p.ix() > px) {
					// Found an intersection
					int numCrosses = crossing;
					if (y0 <= y1) {
						if (y0 < py && enableTopBorder) {
							++numCrosses;
						}
						if (y1 > py && enableBottomBorder) {
							++numCrosses;
						}
					} else {
						if (y0 > py && enableBottomBorder) {
							--numCrosses;
						}
						if (y1 < py && enableTopBorder) {
							--numCrosses;
						}
					}
					return numCrosses;
				}
			}
		}

		return crossing;
	}

	/** Replies if two segments are intersecting.
	 * This function determines if the segments' lines
	 * are intersecting because using the pixel-based test.
	 * This function uses the pixels of the segments that are
	 * computed according to a Bresenham line algorithm.
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
	 */
	@Pure
	static boolean intersectsSegmentSegment(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
		final int side1 = computeSideLinePoint(x1, y1, x2, y2, x3, y3);
		final int side2 = computeSideLinePoint(x1, y1, x2, y2, x4, y4);
		if ((side1 * side2) <= 0) {
			return computeIntersectionTypeSegmentSegment(x1, y1, x2, y2, x3, y3, x4, y4, true, true, null) != 0;
		}
		return false;
	}

	/** Replies if two segments are intersecting pixel per pixel.
	 * This function does not determine if the segments' lines
	 * are intersecting because using the pixel-based test.
	 * This function uses the pixels of the segments that are
	 * computed according to a Bresenham line algorithm.
	 *
	 * @param x1 is the first point of the first segment.
	 * @param y1 is the first point of the first segment.
	 * @param x2 is the second point of the first segment.
	 * @param y2 is the second point of the first segment.
	 * @param x3 is the first point of the second segment.
	 * @param y3 is the first point of the second segment.
	 * @param x4 is the second point of the second segment.
	 * @param y4 is the second point of the second segment.
	 * @param enableThirdPoint indicates if the intersection on the third point is computed.
	 * @param enableFourthPoint indicates if the intersection on the fourth point is computed.
	 * @param intersectionPoint are the coordinates of the intersection, if exist.
	 * @return <code>true</code> if the two segments are intersecting; otherwise
	 * <code>false</code>
	 */
	@SuppressWarnings("checkstyle:parameternumber")
	static boolean intersectsSegmentSegment(int x1, int y1, int x2, int y2, int x3, int y3,
			int x4, int y4, boolean enableThirdPoint, boolean enableFourthPoint, Point2D<?, ?> intersectionPoint) {
		return computeIntersectionTypeSegmentSegment(x1, y1, x2, y2, x3, y3, x4, y4,
				enableThirdPoint, enableFourthPoint, intersectionPoint) != 0;
	}

	/** Replies if two segments are intersecting pixel per pixel.
	 * This function does not determine if the segments' lines
	 * are intersecting because using the pixel-based test.
	 * This function uses the pixels of the segments that are
	 * computed according to a Bresenham line algorithm.
	 *
	 * @param x1 is the first point of the first segment.
	 * @param y1 is the first point of the first segment.
	 * @param x2 is the second point of the first segment.
	 * @param y2 is the second point of the first segment.
	 * @param x3 is the first point of the second segment.
	 * @param y3 is the first point of the second segment.
	 * @param x4 is the second point of the second segment.
	 * @param y4 is the second point of the second segment.
	 * @param enableThirdPoint indicates if the intersection on the third point is computed.
	 * @param enableFourthPoint indicates if the intersection on the fourth point is computed.
	 * @param intersectionPoint are the coordinates of the intersection, if exist.
	 * @return an integer value; if <code>0</code> the two segments are not intersecting;
	 *     <code>1</code> if the two segments are intersecting and the segment 2 has pixels on both
	 *     sides of the segment 1; <code>2</code> if the segments are intersecting and the segment 2
	 *     is only in one side of the segment 1.
	 */
	@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:cyclomaticcomplexity",
			"checkstyle:npathcomplexity"})
	static int computeIntersectionTypeSegmentSegment(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4,
			boolean enableThirdPoint, boolean enableFourthPoint, Point2D<?, ?> intersectionPoint) {
		final BresenhamLineIterator<InnerComputationPoint2ai, InnerComputationVector2ai> it1;
		if (x1 < x2) {
			it1 = new BresenhamLineIterator<>(
					InnerComputationGeomFactory.SINGLETON, x1, y1, x2, y2);
		} else {
			it1 = new BresenhamLineIterator<>(
					InnerComputationGeomFactory.SINGLETON, x2, y2, x1, y1);
		}
		final BresenhamLineIterator<InnerComputationPoint2ai, InnerComputationVector2ai> it2;
		if (x3 < x4) {
			it2 = new BresenhamLineIterator<>(
					InnerComputationGeomFactory.SINGLETON, x3, y3, x4, y4);
		} else {
			it2 = new BresenhamLineIterator<>(
					InnerComputationGeomFactory.SINGLETON, x4, y4, x3, y3);
		}

		if (it1.hasNext() && it2.hasNext()) {
			// Only for internal use
			final Point2D<?, ?> p1 = new InnerComputationPoint2ai();
			// Only for internal use
			final Point2D<?, ?> p2 = new InnerComputationPoint2ai();

			boolean isFirstPointOfSecondSegment = true;

			it1.next(p1);
			it2.next(p2);

			do {

				if (p1.ix() < p2.ix()) {
					while (it1.hasNext() && p1.ix() < p2.ix()) {
						it1.next(p1);
					}
				} else if (p2.ix() < p1.ix()) {
					while (it2.hasNext() && p2.ix() < p1.ix()) {
						it2.next(p2);
						isFirstPointOfSecondSegment = false;
					}
				}

				final int x = p1.ix();
				int min1 = p1.iy();
				int max1 = p1.iy();
				int min2 = isFirstPointOfSecondSegment && !enableThirdPoint ? Integer.MAX_VALUE : p2.iy();
				int max2 = isFirstPointOfSecondSegment && !enableThirdPoint ? Integer.MIN_VALUE : p2.iy();

				while (it1.hasNext()) {
					it1.next(p1);
					if (p1.ix() == x) {
						if (p1.iy() < min1) {
							min1 = p1.iy();
						}
						if (p1.iy() > max1) {
							max1 = p1.iy();
						}
					} else {
						break;
					}
				}

				while (it2.hasNext()) {
					it2.next(p2);
					isFirstPointOfSecondSegment = false;
					if (p2.ix() == x) {
						if (p2.iy() < min2) {
							min2 = p2.iy();
						}
						if (p2.iy() > max2) {
							max2 = p2.iy();
						}
					} else {
						break;
					}
				}

				if (max2 >= min1 && max1 >= min2) {
					if (intersectionPoint != null) {
						intersectionPoint.set(x, Math.max(min1, min2));
					}
					return !isFirstPointOfSecondSegment && (it2.hasNext()) ? 1 : 2;
				}
			}
			while (it1.hasNext() && it2.hasNext());

			if (enableFourthPoint && p1.equals(p2)) {
				if (intersectionPoint != null) {
					intersectionPoint.set(p1);
				}
				return !isFirstPointOfSecondSegment && (it2.hasNext()) ? 1 : 2;
			}
		}

		return 0;
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

	@Override
	default void clear() {
		set(0, 0, 0, 0);
	}

	@Pure
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
	void set(int x1, int y1, int x2, int y2);

	/** Change the line.
	 *
	 * @param firstPoint the first point.
	 * @param secondPoint the second point.
	 */
	default void set(Point2D<?, ?> firstPoint, Point2D<?, ?> secondPoint) {
		assert firstPoint != null : "First point must be not be null"; //$NON-NLS-1$
		assert secondPoint != null : "Second point must be not be null"; //$NON-NLS-1$
		set(firstPoint.ix(), firstPoint.iy(), secondPoint.ix(), secondPoint.iy());
	}

	@Override
	default void set(IT shape) {
		assert shape != null : "Shape must be not be null"; //$NON-NLS-1$
		set(shape.getX1(), shape.getY1(), shape.getX2(), shape.getY2());
	}

	/** Replies the X of the first point.
	 *
	 * @return the x of the first point.
	 */
	@Pure
	int getX1();

	/** Replies the Y of the first point.
	 *
	 * @return the y of the first point.
	 */
	@Pure
	int getY1();

	/** Replies the X of the second point.
	 *
	 * @return the x of the second point.
	 */
	@Pure
	int getX2();

	/** Replies the Y of the second point.
	 *
	 * @return the y of the second point.
	 */
	@Pure
	int getY2();

	/** Change the X of the first point.
	 *
	 * @param x the x of the first point.
	 */
	@Pure
	void setX1(int x);

	/** Change the Y of the first point.
	 *
	 * @param y the y of the first point.
	 */
	@Pure
	void setY1(int y);

	/** Change the X of the second point.
	 *
	 * @param x the x of the second point.
	 */
	@Pure
	void setX2(int x);

	/** Change the Y of the second point.
	 *
	 * @param y the y of the second point.
	 */
	@Pure
	void setY2(int y);

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

	/** Change the first point.
	 *
	 * @param point the first point.
	 */
	@Pure
	default void setP1(Point2D<?, ?> point) {
		assert point != null : "Point must be not be null"; //$NON-NLS-1$
		set(point.ix(), point.iy(), getX2(), getY2());
	}

	/** Change the first point.
	 *
	 * @param x x coordinate of the first point.
	 * @param y y coordinate of the first point.
	 */
	@Pure
	default void setP1(int x, int y) {
		set(x, y, getX2(), getY2());
	}

	/** Change the second point.
	 *
	 * @param point the second point.
	 */
	@Pure
	default void setP2(Point2D<?, ?> point) {
		assert point != null : "Point must be not be null"; //$NON-NLS-1$
		set(getX1(), getY1(), point.ix(), point.iy());
	}

	/** Change the second point.
	 *
	 * @param x x coordinate the second point.
	 * @param y y coordinate the second point.
	 */
	@Pure
	default void setP2(int x, int y) {
		set(getX1(), getY1(), x, y);
	}

	@Override
	@Pure
	default void toBoundingBox(B box) {
		assert box != null : "Rectangle must be not be null"; //$NON-NLS-1$
		box.setFromCorners(getX1(), getY1(), getX2(), getY2());
	}

	@Pure
	@Override
	default double getDistanceSquared(Point2D<?, ?> pt) {
		assert pt != null : "Point must be not be null"; //$NON-NLS-1$
		final P closestPoint = getClosestPointTo(pt);
		return closestPoint.getDistanceSquared(pt);
	}

	@Pure
	@Override
	default double getDistanceL1(Point2D<?, ?> pt) {
		assert pt != null : "Point must be not be null"; //$NON-NLS-1$
		final P closestPoint = getClosestPointTo(pt);
		return closestPoint.getDistanceL1(pt);
	}

	@Pure
	@Override
	default double getDistanceLinf(Point2D<?, ?> pt) {
		assert pt != null : "Point must be not be null"; //$NON-NLS-1$
		final P closestPoint = getClosestPointTo(pt);
		return closestPoint.getDistanceLinf(pt);
	}

	@Pure
	@Override
	default boolean contains(int x, int y) {
		final int ax = getX1();
		final int ay = getY1();
		final int bx = getX2();
		final int by = getY2();
		if (x >= ax && x <= bx && y >= ay && y <= by) {
			if (ax == bx || ay == by) {
				return true;
			}

			int minDist = Integer.MAX_VALUE;
			final Point2D<?, ?> p = new InnerComputationPoint2ai();
			final BresenhamLineIterator<InnerComputationPoint2ai, InnerComputationVector2ai> iterator =
					new BresenhamLineIterator<>(InnerComputationGeomFactory.SINGLETON, ax, ay, bx, by);
			while (iterator.hasNext()) {
				iterator.next(p);
				final int a = Math.abs(x - p.ix());
				final int b = Math.abs(y - p.iy());
				final int d = a * a + b * b;
				if (d == 0) {
					return true;
				}
				if (d > minDist) {
					return false;
				}
				minDist = d;
			}
		}
		return false;
	}

	@Pure
	@Override
	default boolean contains(Rectangle2ai<?, ?, ?, ?, ?, B> rectangle) {
		assert rectangle != null : "Rectangle must be not be null"; //$NON-NLS-1$
		if (rectangle.isEmpty()) {
			return contains(rectangle.getMinX(), rectangle.getMinY());
		}
		if (rectangle.getMinX() == rectangle.getMaxX()) {
			return getX1() == getX2() && contains(rectangle.getMinX(), rectangle.getMinY())
					&& contains(rectangle.getMaxX(), rectangle.getMaxY());
		}
		if (rectangle.getMinY() == rectangle.getMaxY()) {
			return getY1() == getY2() && contains(rectangle.getMinX(), rectangle.getMinY())
					&& contains(rectangle.getMaxX(), rectangle.getMaxY());
		}
		return false;
	}

	@Pure
	@Override
	default P getClosestPointTo(Point2D<?, ?> pt) {
		assert pt != null : "Point must be not be null"; //$NON-NLS-1$
		final P point = getGeomFactory().newPoint();
		computeClosestPointTo(getX1(), getY1(), getX2(), getY2(), pt.ix(), pt.iy(), point);
		return point;
	}

	@Override
	default P getClosestPointTo(Rectangle2ai<?, ?, ?, ?, ?, ?> rectangle) {
		throw new UnsupportedOperationException();
	}

	@Override
	default P getClosestPointTo(Circle2ai<?, ?, ?, ?, ?, ?> circle) {
		throw new UnsupportedOperationException();
	}

	@Override
	default P getClosestPointTo(Segment2ai<?, ?, ?, ?, ?, ?> segment) {
		throw new UnsupportedOperationException();
	}

	@Override
	default P getClosestPointTo(MultiShape2ai<?, ?, ?, ?, ?, ?, ?> multishape) {
		throw new UnsupportedOperationException();
	}

	@Override
	default P getClosestPointTo(Path2ai<?, ?, ?, ?, ?, ?> path) {
		throw new UnsupportedOperationException();
	}

	@Pure
	@Override
	default P getFarthestPointTo(Point2D<?, ?> pt) {
		assert pt != null : "Point must be not be null"; //$NON-NLS-1$
		final P point = getGeomFactory().newPoint();
		computeFarthestPointTo(getX1(), getY1(), getX2(), getY2(), pt.ix(), pt.iy(), point);
		return point;
	}

	@Override
	default void translate(int dx, int dy) {
		set(getX1() + dx, getY1() + dy, getX2() + dx, getY2() + dy);
	}

	@Pure
	@Override
	default PathIterator2ai<IE> getPathIterator(Transform2D transform) {
		if (transform == null || transform.isIdentity()) {
			return new SegmentPathIterator<>(this);
		}
		return new TransformedSegmentPathIterator<>(this, transform);
	}

	/** Replies an iterator on the points of the segment.
	 *
	 * <p>The Bresenham line algorithm is an algorithm which determines which points in
	 * an n-dimensional raster should be plotted in order to form a close
	 * approximation to a straight line between two given points. It is
	 * commonly used to draw lines on a computer screen, as it uses only
	 * integer addition, subtraction and bit shifting, all of which are
	 * very cheap operations in standard computer architectures. It is one of the
	 * earliest algorithms developed in the field of computer graphics. A minor extension
	 * to the original algorithm also deals with drawing circles.
	 *
	 * <p>While algorithms such as Wu's algorithm are also frequently used in modern
	 * computer graphics because they can support antialiasing, the speed and
	 * simplicity of Bresenham's line algorithm mean that it is still important.
	 * The algorithm is used in hardware such as plotters and in the graphics
	 * chips of modern graphics cards. It can also be found in many software
	 * graphics libraries. Because the algorithm is very simple, it is often
	 * implemented in either the firmware or the hardware of modern graphics cards.
	 *
	 * @return an iterator on the points along the Bresenham line.
	 */
	@Pure
	@Override
	default Iterator<P> getPointIterator() {
		return new BresenhamLineIterator<>(getGeomFactory(), getX1(), getY1(), getX2(), getY2());
	}

	/** Transform the current segment.
	 * This function changes the current segment.
	 *
	 * @param transform is the affine transformation to apply.
	 * @see #createTransformedShape(Transform2D)
	 */
	default void transform(Transform2D transform) {
		assert transform != null : "Transformation must be not be null"; //$NON-NLS-1$
		final P p = getGeomFactory().newPoint(getX1(), getY1());
		transform.transform(p);
		setP1(p);
		p.set(getX2(), getY2());
		transform.transform(p);
		setP2(p);
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
	default boolean clipToRectangle(int rxmin, int rymin, int rxmax, int rymax) {
		assert rxmin <= rxmax : "rxmin must be lower or equal to rxmax"; //$NON-NLS-1$
		assert rymin <= rymax : "rymin must be lower or equal to rymax"; //$NON-NLS-1$

		int x0 = getX1();
		int y0 = getY1();
		int x1 = getX2();
		int y1 = getY2();
		int code1 = MathUtil.getCohenSutherlandCode(x0, y0, rxmin, rymin, rxmax, rymax);
		int code2 = MathUtil.getCohenSutherlandCode(x1, y1, rxmin, rymin, rxmax, rymax);
		boolean accept = false;
		boolean cont = true;
		int x = 0;
		int y = 0;

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

	@Pure
	@Override
	default boolean intersects(Rectangle2ai<?, ?, ?, ?, ?, ?> rectangle) {
		assert rectangle != null : "Rectangle must be not be null"; //$NON-NLS-1$
		return Rectangle2ai.intersectsRectangleSegment(
				rectangle.getMinX(), rectangle.getMinY(),
				rectangle.getMaxX(), rectangle.getMaxY(),
				getX1(), getY1(),
				getX2(), getY2());
	}

	@Pure
	@Override
	default boolean intersects(Circle2ai<?, ?, ?, ?, ?, ?> circle) {
		assert circle != null : "Circle must be not be null"; //$NON-NLS-1$
		return Circle2ai.intersectsCircleSegment(
				circle.getX(), circle.getY(),
				circle.getRadius(),
				getX1(), getY1(),
				getX2(), getY2());
	}

	@Pure
	@Override
	default boolean intersects(Segment2ai<?, ?, ?, ?, ?, ?> segment) {
		assert segment != null : "Segment must be not be null"; //$NON-NLS-1$
		return intersectsSegmentSegment(
				getX1(), getY1(), getX2(), getY2(),
				segment.getX1(), segment.getY1(), segment.getX2(), segment.getY2());
	}

	@Pure
	@Override
	default boolean intersects(PathIterator2ai<?> iterator) {
		assert iterator != null : "Iterator must be not be null"; //$NON-NLS-1$
		final int mask = iterator.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
		final int crossings = Path2ai.computeCrossingsFromSegment(
				0,
				iterator,
				getX1(), getY1(), getX2(), getY2(),
				CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
		return crossings == MathConstants.SHAPE_INTERSECTS
				|| (crossings & mask) != 0;

	}

	@Pure
	@Override
	default boolean intersects(MultiShape2ai<?, ?, ?, ?, ?, ?, ?> multishape) {
		assert multishape != null : "MultiShape must be not null"; //$NON-NLS-1$
		return multishape.intersects(this);
	}

	/** The Bresenham line algorithm is an algorithm which determines which points in
	 * an n-dimensional raster should be plotted in order to form a close
	 * approximation to a straight line between two given points. It is
	 * commonly used to draw lines on a computer screen, as it uses only
	 * integer addition, subtraction and bit shifting, all of which are
	 * very cheap operations in standard computer architectures. It is one of the
	 * earliest algorithms developed in the field of computer graphics. A minor extension
	 * to the original algorithm also deals with drawing circles.
	 *
	 * <p>While algorithms such as Wu's algorithm are also frequently used in modern
	 * computer graphics because they can support antialiasing, the speed and
	 * simplicity of Bresenham's line algorithm mean that it is still important.
	 * The algorithm is used in hardware such as plotters and in the graphics
	 * chips of modern graphics cards. It can also be found in many software
	 * graphics libraries. Because the algorithm is very simple, it is often
	 * implemented in either the firmware or the hardware of modern graphics cards.
	 *
	 * @param <P> the type of the points.
	 * @param <V> the type of the vectors.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	class BresenhamLineIterator<P extends Point2D<? super P, ? super V>,
			V extends Vector2D<? super V, ? super P>> implements Iterator<P> {

		private final GeomFactory<V, P> factory;

		private final boolean steep;

		private final int ystep;

		private final int xstep;

		private final int deltax;

		private final int deltay;

		private final int x1;

		private int y;

		private int x;

		private int error;

		/**
		 * @param factory the point factory.
		 * @param x0 is the x-coordinate of the first point of the Bresenham line.
		 * @param y0 is the y-coordinate of the first point of the Bresenham line.
		 * @param x1 is the x-coordinate of the last point of the Bresenham line.
		 * @param y1 is the y-coordinate of the last point of the Bresenham line.
		 */
		public BresenhamLineIterator(GeomFactory<V, P> factory, int x0, int y0, int x1, int y1) {
			assert factory != null : "Factory must be not be null"; //$NON-NLS-1$
			this.factory = factory;
			int localx0 = x0;
			int localy0 = y0;
			int localx1 = x1;
			int localy1 = y1;

			this.steep = Math.abs(localy1 - localy0) > Math.abs(localx1 - localx0);

			int swapv;
			if (this.steep) {
				//swap(x0, y0);
				swapv = localx0;
				localx0 = localy0;
				localy0 = swapv;
				//swap(x1, y1);
				swapv = localx1;
				localx1 = localy1;
				localy1 = swapv;
			}
			/*if (localx0 > localx1) {
				//swap(x0, x1);
				swapv = localx0;
				localx0 = localx1;
				localx1 = swapv;
				//swap(y0, y1);
				swapv = localy0;
				localy0 = localy1;
				localy1 = swapv;
			}*/

			this.deltax = Math.abs(localx1 - localx0);
			this.deltay = Math.abs(localy1 - localy0);
			this.error = this.deltax / 2;
			this.y = localy0;

			if (localx0 < localx1) {
				this.xstep = 1;
			} else {
				this.xstep = -1;
			}

			if (localy0 < localy1) {
				this.ystep = 1;
			} else {
				this.ystep = -1;
			}

			this.x1 = localx1;
			this.x = localx0;
		}

		@Pure
		@Override
		public boolean hasNext() {
			return ((this.xstep > 0) && (this.x <= this.x1))
					|| ((this.xstep < 0) && (this.x1 <= this.x));
		}

		/** Replies the next point in the given parameter.
		 *
		 * @param pt the output point.
		 */
		public void next(Point2D<?, ?> pt) {
			if (this.steep) {
				pt.set(this.y, this.x);
			} else {
				pt.set(this.x, this.y);
			}

			this.error = this.error - this.deltay;

			if (this.error < 0) {
				this.y = this.y + this.ystep;
				this.error = this.error + this.deltax;
			}

			this.x += this.xstep;
		}

		@Pure
		@Override
		public P next() {
			final P p = this.factory.newPoint();
			next(p);
			return p;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	/** Abstract iterator on the path elements of the segment.
	 *
	 * @param <IE> is the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	abstract class AbstractSegmentPathIterator<IE extends PathElement2ai> implements PathIterator2ai<IE> {

		/** Element.
		 */
		protected final Segment2ai<?, ?, IE, ?, ?, ?> segment;

		/**
		 * @param segment the element.
		 */
		public AbstractSegmentPathIterator(Segment2ai<?, ?, IE, ?, ?, ?> segment) {
			assert segment != null : "Factory must be not be null"; //$NON-NLS-1$
			this.segment = segment;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		@Override
		public PathWindingRule getWindingRule() {
			return PathWindingRule.NON_ZERO;
		}

		@Pure
		@Override
		public boolean isPolyline() {
			return true;
		}

		@Override
		public boolean isCurved() {
			return false;
		}

		@Override
		public boolean isMultiParts() {
			return false;
		}

		@Override
		public boolean isPolygon() {
			return false;
		}

		@Override
		public GeomFactory2ai<IE, ?, ?, ?> getGeomFactory() {
			return this.segment.getGeomFactory();
		}

	}

	/** Iterator on the path elements of the segment.
	 *
	 * @param <IE> is the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	class TransformedSegmentPathIterator<IE extends PathElement2ai> extends AbstractSegmentPathIterator<IE> {

		private final Transform2D transform;

		private Point2D<?, ?> p1;

		private Point2D<?, ?> p2;

		private int x1;

		private int y1;

		private int x2;

		private int y2;

		private int index;

		/**
		 * @param segment the segment to iterate on.
		 * @param transform the transformation to apply.
		 */
		public TransformedSegmentPathIterator(Segment2ai<?, ?, IE, ?, ?, ?> segment, Transform2D transform) {
			super(segment);
			assert transform != null : "Transformation must be not be null"; //$NON-NLS-1$
			this.transform = transform;
			if (segment.getX1() == segment.getX2() && segment.getY1() == segment.getY2()) {
				this.index = 2;
			} else {
				this.p1 = new InnerComputationPoint2ai();
				this.p2 = new InnerComputationPoint2ai();
				this.x1 = segment.getX1();
				this.y1 = segment.getY1();
				this.x2 = segment.getX2();
				this.y2 = segment.getY2();
			}
		}

		@Override
		public PathIterator2ai<IE> restartIterations() {
			return new TransformedSegmentPathIterator<>(this.segment, this.transform);
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index <= 1;
		}

		@Override
		public IE next() {
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
				return getGeomFactory().newMovePathElement(
						this.p2.ix(), this.p2.iy());
			case 1:
				this.p1.set(this.p2);
				this.p2.set(this.x2, this.y2);
				if (this.transform != null) {
					this.transform.transform(this.p2);
				}
				return getGeomFactory().newLinePathElement(
						this.p1.ix(), this.p1.iy(),
						this.p2.ix(), this.p2.iy());
			default:
				throw new NoSuchElementException();
			}
		}

	}

	/** Iterator on the path elements of the segment.
	 *
	 * @param <IE> is the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	class SegmentPathIterator<IE extends PathElement2ai> extends AbstractSegmentPathIterator<IE> {

		private int x1;

		private int y1;

		private int x2;

		private int y2;

		private int index;

		/**
		 * @param segment the segment to iterate on.
		 */
		public SegmentPathIterator(Segment2ai<?, ?, IE, ?, ?, ?> segment) {
			super(segment);
			if (segment.getX1() == segment.getX2() && segment.getY1() == segment.getY2()) {
				this.index = 2;
			} else {
				this.x1 = segment.getX1();
				this.y1 = segment.getY1();
				this.x2 = segment.getX2();
				this.y2 = segment.getY2();
			}
		}

		@Override
		public PathIterator2ai<IE> restartIterations() {
			return new SegmentPathIterator<>(this.segment);
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index <= 1;
		}

		@Override
		public IE next() {
			if (this.index > 1) {
				throw new NoSuchElementException();
			}
			final int idx = this.index;
			++this.index;
			switch (idx) {
			case 0:
				return getGeomFactory().newMovePathElement(this.x1, this.y1);
			case 1:
				return getGeomFactory().newLinePathElement(
						this.x1, this.y1,
						this.x2, this.y2);
			default:
				throw new NoSuchElementException();
			}
		}

	}

}
