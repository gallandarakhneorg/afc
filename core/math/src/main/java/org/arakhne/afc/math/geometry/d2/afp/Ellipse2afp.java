/* 
 * $Id$
 * 
 * Copyright (C) 2010-2013 Stephane GALLAND.
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

package org.arakhne.afc.math.geometry.d2.afp;

import java.util.NoSuchElementException;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.eclipse.xtext.xbase.lib.Pure;

/** Fonctional interface that represented a 2D ellipse on a plane.
 *
 * @param <ST> is the type of the general implementation.
 * @param <IT> is the type of the implementation of this shape.
 * @param <IE> is the type of the path elements.
 * @param <P> is the type of the points.
 * @param <B> is the type of the bounding boxes.
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Ellipse2afp<
		ST extends Shape2afp<?, ?, IE, P, B>,
		IT extends Ellipse2afp<?, ?, IE, P, B>,
		IE extends PathElement2afp,
		P extends Point2D,
		B extends Rectangle2afp<?, ?, IE, P, B>>
		extends RectangularShape2afp<ST, IT, IE, P, B> {

	/**
	 * Replies if the given point is inside the given ellipse.
	 * 
	 * @param ellx is the min corner of the ellipse.
	 * @param elly is the min corner of the ellipse.
	 * @param ellw is the width of the ellipse.
	 * @param ellh is the height of the ellipse.
	 * @param px is the point to test.
	 * @param py is the point to test.
	 * @return <code>true</code> if the point is inside the ellipse;
	 * <code>false</code> if not.
	 */
	@Pure
	public static boolean containsEllipsePoint(double ellx, double elly, double ellw, double ellh, double px, double py) {
		// Copied from AWT Ellipse2D

		// Normalize the coordinates compared to the ellipse
		// having a center at 0,0 and a radius of 0.5.
		if (ellw <= 0f || ellh <= 0f) {
			return false;
		}
		double normx = (px - ellx) / ellw - 0.5f;
		double normy = (py - elly) / ellh - 0.5f;
		return (normx * normx + normy * normy) <= 0.25f;
	}

	/** Replies the closest point from the given point in the solid ellipse.
	 * A solid ellipse is an ellipse with a border and an interior area.
	 * 
	 * @param px is the coordinate of the point.
	 * @param py is the coordinate of the point.
	 * @param ex is the coordinate of the min corner of the ellipse
	 * @param ey is the coordinate of the min corner of the ellipse
	 * @param ew is the width of the ellipse
	 * @param eh is the height of the ellipse
	 * @param result the closest point in the ellipse.
	 * @return <code>true</code> if the point is inside the ellipse.
	 * @see #computeClosestPointToShallowEllipse(double, double, double, double, double, double, Point2D)
	 */
	@Pure
	public static boolean computeClosestPointToSolidEllipse(
			double px, double py, double ex, double ey, double ew, double eh,
			Point2D result) {
		double x, y;
		boolean isInside = false;
		if (ew<=0 || eh<=0) {
			x = ex;
			y = ey;
		}
		else {
			// Normalize the coordinates compared to the ellipse
			// having a center at 0,0 and a radius of 0.5.
			double normx = (px - ex) / ew - 0.5;
			double normy = (py - ey) / eh - 0.5;
			if ((normx * normx + normy * normy) < 0.25) {
				// The point is inside the ellipse
				isInside = true;
				x = px;
				y = py;
			}
			else {
				// Compute the intersection between the ellipse
				// centered on (0;0) and the line (0;0)-(x0;y0)
				double a = ew / 2;
				double b = eh / 2;
				double x0 = px - (ex + a);
				double y0 = py - (ey + b);

				double denom = a*a*y0*y0 + b*b*x0*x0;
				assert(denom>0f); // because the "inside"-test should discard this case.

				denom = Math.sqrt(denom);
				double factor = (a * b) / denom;
				x = factor * x0;
				y = factor * y0;

				// Translate the point to the original coordinate system
				x += (ex + a);
				y += (ey + b);
			}
		}
		result.set(x, y);
		return isInside;
	}

	/** Replies the closest point from the given point in the shallow ellipse.
	 * A shallow ellipse is an ellipse with a border and not an interior area.
	 * 
	 * @param px is the coordinate of the point.
	 * @param py is the coordinate of the point.
	 * @param ex is the coordinate of the min corner of the ellipse
	 * @param ey is the coordinate of the min corner of the ellipse
	 * @param ew is the width of the ellipse
	 * @param eh is the height of the ellipse
	 * @param result the closest point in the ellipse.
	 * @return <code>true</code> if the given point is exactly at the center of the ellipse.
	 * @see #computeClosestPointToSolidEllipse(double, double, double, double, double, double, Point2D)
	 */
	@Pure
	public static boolean computeClosestPointToShallowEllipse(double px, double py, double ex, double ey, double ew, double eh,
			Point2D result) {
		double x, y;
		if (ew<=0 || eh<=0) {
			x = ex;
			y = ey;
		}
		else {
			// Compute the intersection between the ellipse
			// centered on (0;0) and the line (0;0)-(x0;y0)
			double a = ew / 2;
			double b = eh / 2;
			double x0 = px - (ex + a);
			double y0 = py - (ey + b);

			double denom = a*a*y0*y0 + b*b*x0*x0;
			if (denom==0f) {
				// The point is at the center of the ellipse.
				// Replies allways the same point.
				return true;
			}

			denom = Math.sqrt(denom);
			double factor = (a * b) / denom;
			x = factor * x0;
			y = factor * y0;

			// Translate the point to the original coordinate system
			x += (ex + a);
			y += (ey + b);
		}
		result.set(x, y);
		return false;
	}

	/** Replies the farthest point from the given point in the shallow ellipse.
	 * A shallow ellipse is an ellipse with a border and not an interior area.
	 * 
	 * @param px is the coordinate of the point.
	 * @param py is the coordinate of the point.
	 * @param ex is the coordinate of the min corner of the ellipse
	 * @param ey is the coordinate of the min corner of the ellipse
	 * @param ew is the width of the ellipse
	 * @param eh is the height of the ellipse
	 * @param result the farthest point in the ellipse.
	 */
	@Pure
	static void computeFarthestPointToShallowEllipse(double px, double py, double ex, double ey, double ew, double eh, Point2D result) {
		double x, y;
		if (ew<=0 || eh<=0) {
			x = ex;
			y = ey;
		}
		else {
			// Compute the intersection between the ellipse
			// centered on (0;0) and the line (0;0)-(x0;y0)
			double a = ew / 2.;
			double b = eh / 2.;
			double x0 = px - (ex + a);
			double y0 = py - (ey + b);

			double denom = a*a*y0*y0 + b*b*x0*x0;
			if (denom==0f) {
				if (ew >= eh) {
					x = ex + ew;
					y = ey + b;
				} else {
					x = ex + a;
					y = ey + eh;
				}
			} else {
				denom = Math.sqrt(denom);
				double factor = (a * b) / denom;
				x = - (factor * x0);
				y = - (factor * y0);

				// Translate the point to the original coordinate system
				x += (ex + a);
				y += (ey + b);
			}
		}
		result.set(x, y);
	}

	/** Replies if a rectangle is inside in the ellipse.
	 * 
	 * @param ex is the lowest corner of the ellipse.
	 * @param ey is the lowest corner of the ellipse.
	 * @param ewidth is the width of the ellipse.
	 * @param eheight is the height of the ellipse.
	 * @param rx is the lowest corner of the rectangle.
	 * @param ry is the lowest corner of the rectangle.
	 * @param rwidth is the width of the rectangle.
	 * @param rheight is the height of the rectangle.
	 * @return <code>true</code> if the given rectangle is inside the ellipse;
	 * otherwise <code>false</code>.
	 */
	@Pure
	static boolean containsEllipseRectangle(double ex, double ey, double ewidth, double eheight,
			double rx, double ry, double rwidth, double rheight) {
		double ecx = (ex + ewidth/2);
		double ecy = (ey + eheight/2);
		double rcx = (rx + rwidth/2);
		double rcy = (ry + rheight/2);
		double farX;
		if (ecx<=rcx) farX = rx + rwidth;
		else farX = rx;
		double farY;
		if (ecy<=rcy) farY = ry + rheight;
		else farY = ry;
		return containsEllipsePoint(ex, ey, ewidth, eheight, farX, farY);
	}

	/** Replies if two ellipses are intersecting.
	 * 
	 * @param x1 is the first corner of the first ellipse.
	 * @param y1 is the first corner of the first ellipse.
	 * @param x2 is the second corner of the first ellipse.
	 * @param y2 is the second corner of the first ellipse.
	 * @param x3 is the first corner of the second ellipse.
	 * @param y3 is the first corner of the second ellipse.
	 * @param x4 is the second corner of the second ellipse.
	 * @param y4 is the second corner of the second ellipse.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	@Pure
	static boolean intersectsEllipseEllipse(double x1, double y1, double x2, double y2,
			double x3, double y3, double x4, double y4) {
		double ell2w = Math.abs(x4 - x3);
		double ell2h = Math.abs(y4 - y3);
		double ellw = Math.abs(x2 - x1);
		double ellh = Math.abs(y2 - y1);

		if (ell2w <= 0 || ell2h <= 0) return false;
		if (ellw <= 0 || ellh <= 0) return false;

		// Normalize the second ellipse coordinates compared to the ellipse
		// having a center at 0,0 and a radius of 0.5.
		double normx0 = (x3 - x1) / ellw - 0.5;
		double normx1 = normx0 + ell2w / ellw;
		double normy0 = (y3 - y1) / ellh - 0.5;
		double normy1 = normy0 + ell2h / ellh;

		// find nearest x (left edge, right edge, 0.0)
		// find nearest y (top edge, bottom edge, 0.0)
		// if nearest x,y is inside circle of radius 0.5, then intersects
		double nearx, neary;
		if (normx0 > 0) {
			// center to left of X extents
			nearx = normx0;
		} else if (normx1 < 0) {
			// center to right of X extents
			nearx = normx1;
		} else {
			nearx = 0;
		}
		if (normy0 > 0) {
			// center above Y extents
			neary = normy0;
		} else if (normy1 < 0) {
			// center below Y extents
			neary = normy1;
		} else {
			neary = 0;
		}
		return (nearx * nearx + neary * neary) < 0.25;
	}

	/** Replies if an ellipse and a line are intersecting.
	 * 
	 * @param ex is the lowest corner of the ellipse.
	 * @param ey is the lowest corner of the ellipse.
	 * @param ew is the width of the ellipse.
	 * @param eh is the height of the ellipse.
	 * @param x1 is the first point of the line.
	 * @param y1 is the first point of the line.
	 * @param x2 is the second point of the line.
	 * @param y2 is the second point of the line.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 * @see "http://blog.csharphelper.com/2012/09/24/calculate-where-a-line-segment-and-an-ellipse-intersect-in-c.aspx"
	 */
	@Pure
	static boolean intersectsEllipseLine(double ex, double ey, double ew, double eh,
			double x1, double y1, double x2, double y2) {
		// If the ellipse or line segment are empty, return no intersections.
		if (eh<=0 || ew<=0) {
			return false;
		}

		// Get the semimajor and semiminor axes.
		double a = ew / 2;
		double b = eh / 2;

		// Translate so the ellipse is centered at the origin.
		double ecx = ex + a;
		double ecy = ey + b;
		double px1 = x1 - ecx;
		double py1 = y1 - ecy;
		double px2 = x2 - ecx;
		double py2 = y2 - ecy;

		double sq_a = a*a;
		double sq_b = b*b;
		double vx = px2 - px1;
		double vy = py2 - py1;

		assert(sq_a!=0 && sq_b!=0);

		// Calculate the quadratic parameters.
		double A = vx * vx / sq_a +
				vy * vy / sq_b;
		double B = 2 * px1 * vx / sq_a +
				2 * py1 * vy / sq_b;
		double C = px1 * px1 / sq_a + py1 * py1 / sq_b - 1;

		// Calculate the discriminant.
		double discriminant = B * B - 4 * A * C;
		return (discriminant>=0);
	}

	/** Replies if an ellipse and a segment are intersecting.
	 * 
	 * @param ex is the lowest corner of the ellipse.
	 * @param ey is the lowest corner of the ellipse.
	 * @param ew is the width of the ellipse.
	 * @param eh is the height of the ellipse.
	 * @param x1 is the first point of the segment.
	 * @param y1 is the first point of the segment.
	 * @param x2 is the second point of the segment.
	 * @param y2 is the second point of the segment.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 * @see "http://blog.csharphelper.com/2012/09/24/calculate-where-a-line-segment-and-an-ellipse-intersect-in-c.aspx"
	 */
	@Pure
	static boolean intersectsEllipseSegment(double ex, double ey, double ew, double eh, 
			double x1, double y1, double x2, double y2) {
		// If the ellipse or line segment are empty, return no intersections.
		if (eh<=0 || ew<=0) {
			return false;
		}

		// Get the semimajor and semiminor axes.
		double a = ew / 2;
		double b = eh / 2;

		// Translate so the ellipse is centered at the origin.
		double ecx = ex + a;
		double ecy = ey + b;
		double px1 = x1 - ecx;
		double py1 = y1 - ecy;
		double px2 = x2 - ecx;
		double py2 = y2 - ecy;

		double sq_a = a*a;
		double sq_b = b*b;
		double vx = px2 - px1;
		double vy = py2 - py1;

		assert(sq_a!=0 && sq_b!=0);

		// Calculate the quadratic parameters.
		double A = vx * vx / sq_a + vy * vy / sq_b;
		double B = 2 * px1 * vx / sq_a + 2 * py1 * vy / sq_b;
		double C = px1 * px1 / sq_a + py1 * py1 / sq_b - 1;

		// Calculate the discriminant.
		double discriminant = B * B - 4 * A * C;
		if (discriminant<0f) {
			// No solution
			return false;
		}

		if (discriminant==0f) {
			// One real solution.
			double t = -B / 2 / A;
			return ((t >= 0) && (t <= 1));
		}

		assert(discriminant>0);

		// Two real solutions.
		double t1 = (-B + Math.sqrt(discriminant)) / 2 / A;
		double t2 = (-B - Math.sqrt(discriminant)) / 2 / A;

		return (t1>=0 || t2>=0) && (t1<=1 || t2<=1);
	}

	/** Replies if two ellipses are intersecting.
	 * 
	 * @param x1 is the first corner of the first ellipse.
	 * @param y1 is the first corner of the first ellipse.
	 * @param x2 is the second corner of the first ellipse.
	 * @param y2 is the second corner of the first ellipse.
	 * @param x3 is the first corner of the second rectangle.
	 * @param y3 is the first corner of the second rectangle.
	 * @param x4 is the second corner of the second rectangle.
	 * @param y4 is the second corner of the second rectangle.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	@Pure
	static boolean intersectsEllipseRectangle(double x1, double y1, double x2, double y2,
			double x3, double y3, double x4, double y4) {
		// From AWT Ellipse2D

		double rectw = Math.abs(x4 - x3);
		double recth = Math.abs(y4 - y3);
		double ellw = Math.abs(x2 - x1);
		double ellh = Math.abs(y2 - y1);

		if (rectw <= 0 || recth <= 0) return false;
		if (ellw <= 0 || ellh <= 0) return false;

		// Normalize the rectangular coordinates compared to the ellipse
		// having a center at 0,0 and a radius of 0.5.
		double normx0 = (x3 - x1) / ellw - 0.5;
		double normx1 = normx0 + rectw / ellw;
		double normy0 = (y3 - y1) / ellh - 0.5;
		double normy1 = normy0 + recth / ellh;
		// find nearest x (left edge, right edge, 0.0)
		// find nearest y (top edge, bottom edge, 0.0)
		// if nearest x,y is inside circle of radius 0.5, then intersects
		double nearx, neary;
		if (normx0 > 0) {
			// center to left of X extents
			nearx = normx0;
		} else if (normx1 < 0) {
			// center to right of X extents
			nearx = normx1;
		} else {
			nearx = 0;
		}
		if (normy0 > 0) {
			// center above Y extents
			neary = normy0;
		} else if (normy1 < 0) {
			// center below Y extents
			neary = normy1;
		} else {
			neary = 0;
		}
		return (nearx * nearx + neary * neary) < 0.25;
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
		return getMinX() == shape.getMinX()
			&& getMinY() == shape.getMinY()
			&& getMaxX() == shape.getMaxX()
			&& getMaxY() == shape.getMaxY();
	}

	@Pure
	@Override
	default double getDistanceSquared(Point2D p) {
		Point2D r = getClosestPointTo(p);
		return r.getDistanceSquared(p);
	}

	@Pure
	@Override
	default double getDistanceL1(Point2D p) {
		Point2D r = getClosestPointTo(p);
		return r.getDistanceL1(p);
	}

	@Pure
	@Override
	default double getDistanceLinf(Point2D p) {
		Point2D r = getClosestPointTo(p);
		return r.getDistanceLinf(p);
	}

	@Pure
	@Override
	default boolean contains(double x, double y) {
		return containsEllipsePoint(
				getMinX(), getMinY(), getWidth(), getHeight(),
				x, y);
	}

	@Override
	default boolean contains(Rectangle2afp<?, ?, ?, ?, ?> r) {
		return containsEllipseRectangle(
				getMinX(), getMinY(), getWidth(), getHeight(),
				r.getMinX(), r.getMinY(), r.getWidth(), r.getHeight());
	}
	
	@Override
	default boolean intersects(Rectangle2afp<?, ?, ?, ?, ?> s) {
		return intersectsEllipseRectangle(
				getMinX(), getMinY(),
				getMaxX(), getMaxY(),
				s.getMinX(), s.getMinY(),
				s.getMaxX(), s.getMaxY());
	}
	
	@Override
	default boolean intersects(Ellipse2afp<?, ?, ?, ?, ?> s) {
		return intersectsEllipseRectangle(
				getMinX(), getMinY(),
				getMaxX(), getMaxY(),
				s.getMinX(), s.getMinY(),
				s.getMaxX(), s.getMaxY());
	}
	
	@Override
	default boolean intersects(Circle2afp<?, ?, ?, ?, ?> s) {
		return intersectsEllipseEllipse(
				getMinX(), getMinY(),
				getMaxX(), getMaxY(),
				s.getX()-s.getRadius(), s.getY()-s.getRadius(),
				s.getX()+s.getRadius(), s.getY()+s.getRadius());
	}
	
	@Override
	default boolean intersects(Segment2afp<?, ?, ?, ?, ?> s) {
		return intersectsEllipseSegment(
				getMinX(), getMinY(),
				getWidth(), getHeight(),
				s.getX1(), s.getY1(),
				s.getX2(), s.getY2());
	}
	
	@Override
	default boolean intersects(OrientedRectangle2afp<?, ?, ?, ?, ?> s) {
		return OrientedRectangle2afp.intersectsOrientedRectangleEllipse(
				s.getCenterX(), s.getCenterY(), 
				s.getFirstAxisX(), s.getFirstAxisY(), s.getFirstAxisExtent(),
				s.getSecondAxisX(), s.getSecondAxisY(), s.getSecondAxisExtent(),
				getMinX(), getMinY(), getWidth(), getHeight());
	}
	
	@Override
	default boolean intersects(RoundRectangle2afp<?, ?, ?, ?, ?> s) {
		return s.intersects(this);
	}

	@Override
	default boolean intersects(PathIterator2afp<?> iterator) {
		int mask = (iterator.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = Path2afp.computeCrossingsFromEllipse(
				0,
				iterator,
				getMinX(), getMinY(), getWidth(), getHeight(),
				false,
				true);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);

	}

	@Override
	default PathIterator2afp<IE> getPathIterator(Transform2D transform) {
		if (transform==null || transform.isIdentity()) {
			return new EllipsePathIterator<>(this);
		}
		return new TransformedEllipsePathIterator<>(this, transform);
	}
	
	@Override
	default P getClosestPointTo(Point2D p) {
		P point = getGeomFactory().newPoint();
		Ellipse2afp.computeClosestPointToSolidEllipse(
				p.getX(), p.getY(),
				getMinX(), getMinY(),
				getWidth(), getHeight(),
				point);
		return point;
	}

	@Override
	default P getFarthestPointTo(Point2D p) {
		P point = getGeomFactory().newPoint();
		Ellipse2afp.computeFarthestPointToShallowEllipse(
				p.getX(), p.getY(),
				getMinX(), getMinY(),
				getWidth(), getHeight(),
				point);
		return point;
	}

	
	/** Abstract iterator on the path elements of the ellipse.
	 * 
	 * @param <T> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	abstract class AbstractEllipsePathIterator<T extends PathElement2afp> implements PathIterator2afp<T> {
				
		private final GeomFactory2afp<T, ?, ?> factory;

		/**
		 * ArcIterator.btan(Math.PI/2)
		 */
		protected static final double CTRL_VAL = 0.5522847498307933f;

		/**
		 * ctrlpts contains the control points for a set of 4 cubic
		 * bezier curves that approximate a circle of radius 0.5
		 * centered at 0.5, 0.5
		 */
		protected static final double PCV = 0.5f + CTRL_VAL * 0.5f;
		
		/**
		 * ctrlpts contains the control points for a set of 4 cubic
		 * bezier curves that approximate a circle of radius 0.5
		 * centered at 0.5, 0.5
		 */
		protected static final double NCV = 0.5f - CTRL_VAL * 0.5f;
		
		/**
		 * ctrlpts contains the control points for a set of 4 cubic
		 * bezier curves that approximate a circle of radius 0.5
		 * centered at 0.5, 0.5
		 */
		protected static final double CTRL_PTS[][] = {
				{  1.0f,  PCV,  PCV,  1.0f,  0.5f,  1.0f },
				{  NCV,  1.0f,  0.0f,  PCV,  0.0f,  0.5f },
				{  0.0f,  NCV,  NCV,  0.0f,  0.5f,  0.0f },
				{  PCV,  0.0f,  1.0f,  NCV,  1.0f,  0.5f }
		};

		/**
		 * @param factory the factory.
		 */
		public AbstractEllipsePathIterator(GeomFactory2afp<T, ?, ?> factory) {
			this.factory = factory;
		}

		@Override
		public GeomFactory2afp<T, ?, ?> getGeomFactory() {
			return this.factory;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		@Pure
		@Override
		public PathWindingRule getWindingRule() {
			return PathWindingRule.NON_ZERO;
		}

		@Pure
		@Override
		public boolean isPolyline() {
			return false;
		}

		@Pure
		@Override
		public boolean isCurved() {
			return true;
		}

		@Pure
		@Override
		public boolean isPolygon() {
			return true;
		}

		@Pure
		@Override
		public boolean isMultiParts() {
			return false;
		}

	}

	/** Iterator on the ellipse path elements.
	 *
	 * @param <T> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	public static class EllipsePathIterator<T extends PathElement2afp> extends AbstractEllipsePathIterator<T> {

		private double x;
		
		private double y;
		
		private double w;
		
		private double h;
		
		private int index;
		
		private double lastX, lastY;

		/**
		 * @param ellipse the ellipse to iterate on.
		 */
		public EllipsePathIterator(Ellipse2afp<?, ?, T, ?, ?> ellipse) {
			super(ellipse.getGeomFactory());
			if (ellipse.isEmpty()) {
				this.index = 6;
			} else {
				this.x = ellipse.getMinX();
				this.y = ellipse.getMaxY();
				this.w = ellipse.getWidth();
				this.h = ellipse.getHeight();
			}
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index<=5;
		}

		@Override
		public T next() {
			if (this.index>5) throw new NoSuchElementException();
			int idx = this.index;
			++this.index;

			if (idx==0) {
				double ctrls[] = CTRL_PTS[3];
				this.lastX = this.x + ctrls[4] * this.w;
				this.lastY = this.y + ctrls[5] * this.h;
				return getGeomFactory().newMovePathElement(
						this.lastX,  this.lastY);
			}
			else if (idx<5) {
				double ctrls[] = CTRL_PTS[idx - 1];
				double ix = this.lastX;
				double iy = this.lastY;
				this.lastX = (this.x + ctrls[4] * this.w);
				this.lastY = (this.y + ctrls[5] * this.h);
				return getGeomFactory().newCurvePathElement(
						ix,  iy,
						(this.x + ctrls[0] * this.w),
						(this.y + ctrls[1] * this.h),
						(this.x + ctrls[2] * this.w),
						(this.y + ctrls[3] * this.h),
						this.lastX,
						this.lastY);
			}

			return getGeomFactory().newClosePathElement(
					this.lastX, this.lastY,
					this.lastX, this.lastY);
		}

	}

	/** Iterator on the path elements of a transformed ellipse.
	 *
	 * @param <T> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	public static class TransformedEllipsePathIterator<T extends PathElement2afp> extends AbstractEllipsePathIterator<T> {

		private final Transform2D transform;

		private Point2D lastPoint;
		
		private Point2D ptmp1;
		
		private Point2D ptmp2;

		private double x1;
		
		private double y1;
		
		private double w;
		
		private double h;
		
		private int index;

		/**
		 * @param ellipse the ellipse to iterate on.
		 * @param transform the transformation to apply to the ellipse.
		 */
		public TransformedEllipsePathIterator(Ellipse2afp<?, ?, T, ?, ?> ellipse, Transform2D transform) {
			super(ellipse.getGeomFactory());
			this.transform = transform;
			if (ellipse.isEmpty()) {
				this.index = 6;
			} else {
				GeomFactory2afp<T, ?, ?> factory = getGeomFactory();
				this.lastPoint = factory.newPoint();
				this.ptmp1 = factory.newPoint();
				this.ptmp2 = factory.newPoint();
				this.x1 = ellipse.getMinX();
				this.y1 = ellipse.getMinY();
				this.w = ellipse.getWidth();
				this.h = ellipse.getHeight();
			}
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index<=5;
		}

		@Override
		public T next() {
			if (this.index>5) throw new NoSuchElementException();
			int idx = this.index;
			++this.index;

			if (idx==0) {
				double ctrls[] = CTRL_PTS[3];
				this.lastPoint.set(
						this.x1 + ctrls[4] * this.w,
						this.y1 + ctrls[5] * this.h);
				this.transform.transform(this.lastPoint);
				return getGeomFactory().newMovePathElement(
						this.lastPoint.getX(), this.lastPoint.getY());
			}
			else if (idx<5) {
				double ctrls[] = CTRL_PTS[idx - 1];
				double ix = this.lastPoint.getX();
				double iy = this.lastPoint.getY();
				this.lastPoint.set(
						(this.x1 + ctrls[4] * this.w),
						(this.y1 + ctrls[5] * this.h));
				this.transform.transform(this.lastPoint);
				this.ptmp1.set(
						(this.x1 + ctrls[0] * this.w),
						(this.y1 + ctrls[1] * this.h));
				this.transform.transform(this.ptmp1);
				this.ptmp2.set(
						(this.x1 + ctrls[2] * this.w),
						(this.y1 + ctrls[3] * this.h));
				this.transform.transform(this.ptmp2);
				return getGeomFactory().newCurvePathElement(
						ix,  iy,
						this.ptmp1.getX(), this.ptmp1.getY(),
						this.ptmp2.getX(), this.ptmp2.getY(),
						this.lastPoint.getX(), this.lastPoint.getY());
			}

			double ix = this.lastPoint.getX();
			double iy = this.lastPoint.getY();
			return getGeomFactory().newClosePathElement(
					ix, iy,
					ix, iy);
		}

	}

}
