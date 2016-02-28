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
package org.arakhne.afc.math.geometry.d2.continuous;

import java.util.NoSuchElementException;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.eclipse.xtext.xbase.lib.Pure;

/** 
 * @author $Author: hjaffali$
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractEllipse2F<T extends AbstractRectangularShape2F<T>> extends AbstractRectangularShape2F<T>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4493000182971183282L;


	// ArcIterator.btan(Math.PI/2)
	private static final double CTRL_VAL = 0.5522847498307933f;

	/**
	 * ctrlpts contains the control points for a set of 4 cubic
	 * bezier curves that approximate a circle of radius 0.5
	 * centered at 0.5, 0.5
	 */
	static final double PCV = 0.5f + CTRL_VAL * 0.5f;
	/**
	 * ctrlpts contains the control points for a set of 4 cubic
	 * bezier curves that approximate a circle of radius 0.5
	 * centered at 0.5, 0.5
	 */
	static final double NCV = 0.5f - CTRL_VAL * 0.5f;
	/**
	 * ctrlpts contains the control points for a set of 4 cubic
	 * bezier curves that approximate a circle of radius 0.5
	 * centered at 0.5, 0.5
	 */
	static final double CTRL_PTS[][] = {
			{  1.0f,  PCV,  PCV,  1.0f,  0.5f,  1.0f },
			{  NCV,  1.0f,  0.0f,  PCV,  0.0f,  0.5f },
			{  0.0f,  NCV,  NCV,  0.0f,  0.5f,  0.0f },
			{  PCV,  0.0f,  1.0f,  NCV,  1.0f,  0.5f }
	};

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
	 * @param returnNullWhenInside indicates if a <code>null</code> value
	 * may be returned when the point is inside the ellipse, if
	 * <code>true</code>; or a point all the time if <code>false</code>.
	 * @return the closest point in the ellipse
	 * @see #computeClosestPointToShallowEllipse(double, double, double, double, double, double)
	 */
	@Pure
	public static Point2D computeClosestPointToSolidEllipse(double px, double py, double ex, double ey, double ew, double eh, boolean returnNullWhenInside) {
		double x, y;
		if (ew<=0f || eh<=0f) {
			x = ex;
			y = ey;
		}
		else {
			// Normalize the coordinates compared to the ellipse
			// having a center at 0,0 and a radius of 0.5.
			double normx = (px - ex) / ew - 0.5f;
			double normy = (py - ey) / eh - 0.5f;
			if ((normx * normx + normy * normy) < 0.25f) {
				// The point is inside the ellipse
				if (returnNullWhenInside) return null;
				x = px;
				y = py;
			}
			else {
				// Compute the intersection between the ellipse
				// centered on (0;0) and the line (0;0)-(x0;y0)
				double a = ew / 2f;
				double b = eh / 2f;
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
		return new Point2f(x, y);
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
	 * @return the closest point in the ellipse, or <code>null</code> if 
	 * the given point is exactly at the center of the ellipse.
	 * @see #computeClosestPointToSolidEllipse(double, double, double, double, double, double, boolean)
	 */
	@Pure
	public static Point2D computeClosestPointToShallowEllipse(double px, double py, double ex, double ey, double ew, double eh) {
		double x, y;
		if (ew<=0f || eh<=0f) {
			x = ex;
			y = ey;
		}
		else {
			// Compute the intersection between the ellipse
			// centered on (0;0) and the line (0;0)-(x0;y0)
			double a = ew / 2f;
			double b = eh / 2f;
			double x0 = px - (ex + a);
			double y0 = py - (ey + b);

			double denom = a*a*y0*y0 + b*b*x0*x0;
			if (denom==0f) {
				// The point is at the center of the ellipse.
				// Replies allways the same point.
				return null;
			}

			denom = Math.sqrt(denom);
			double factor = (a * b) / denom;
			x = factor * x0;
			y = factor * y0;

			// Translate the point to the original coordinate system
			x += (ex + a);
			y += (ey + b);
		}
		return new Point2f(x, y);
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
	 * @return the farthest point in the ellipse.
	 */
	@Pure
	public static Point2D computeFarthestPointToShallowEllipse(double px, double py, double ex, double ey, double ew, double eh) {
		double x, y;
		if (ew<=0f || eh<=0f) {
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
		return new Point2f(x, y);
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
	public static boolean containsEllipseRectangle(double ex, double ey, double ewidth, double eheight, double rx, double ry, double rwidth, double rheight) {
		double ecx = (ex + ewidth/2f);
		double ecy = (ey + eheight/2f);
		double rcx = (rx + rwidth/2f);
		double rcy = (ry + rheight/2f);
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
	public static boolean intersectsEllipseEllipse(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
		double ell2w = Math.abs(x4 - x3);
		double ell2h = Math.abs(y4 - y3);
		double ellw = Math.abs(x2 - x1);
		double ellh = Math.abs(y2 - y1);

		if (ell2w <= 0f || ell2h <= 0f) return false;
		if (ellw <= 0f || ellh <= 0f) return false;

		// Normalize the second ellipse coordinates compared to the ellipse
		// having a center at 0,0 and a radius of 0.5.
		double normx0 = (x3 - x1) / ellw - 0.5f;
		double normx1 = normx0 + ell2w / ellw;
		double normy0 = (y3 - y1) / ellh - 0.5f;
		double normy1 = normy0 + ell2h / ellh;

		// find nearest x (left edge, right edge, 0.0)
		// find nearest y (top edge, bottom edge, 0.0)
		// if nearest x,y is inside circle of radius 0.5, then intersects
		double nearx, neary;
		if (normx0 > 0f) {
			// center to left of X extents
			nearx = normx0;
		} else if (normx1 < 0f) {
			// center to right of X extents
			nearx = normx1;
		} else {
			nearx = 0f;
		}
		if (normy0 > 0f) {
			// center above Y extents
			neary = normy0;
		} else if (normy1 < 0f) {
			// center below Y extents
			neary = normy1;
		} else {
			neary = 0f;
		}
		return (nearx * nearx + neary * neary) < 0.25f;
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
	public static boolean intersectsEllipseLine(double ex, double ey, double ew, double eh, double x1, double y1, double x2, double y2) {
		// If the ellipse or line segment are empty, return no intersections.
		if (eh<=0f || ew<=0f) {
			return false;
		}

		// Get the semimajor and semiminor axes.
		double a = ew / 2f;
		double b = eh / 2f;

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

		assert(sq_a!=0f && sq_b!=0f);

		// Calculate the quadratic parameters.
		double A = vx * vx / sq_a +
				vy * vy / sq_b;
		double B = 2f * px1 * vx / sq_a +
				2f * py1 * vy / sq_b;
		double C = px1 * px1 / sq_a + py1 * py1 / sq_b - 1;

		// Calculate the discriminant.
		double discriminant = B * B - 4f * A * C;
		return (discriminant>=0f);
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
	public static boolean intersectsEllipseSegment(double ex, double ey, double ew, double eh, double x1, double y1, double x2, double y2) {
		// If the ellipse or line segment are empty, return no intersections.
		if (eh<=0f || ew<=0f) {
			return false;
		}

		// Get the semimajor and semiminor axes.
		double a = ew / 2f;
		double b = eh / 2f;

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

		assert(sq_a!=0f && sq_b!=0f);

		// Calculate the quadratic parameters.
		double A = vx * vx / sq_a + vy * vy / sq_b;
		double B = 2f * px1 * vx / sq_a + 2f * py1 * vy / sq_b;
		double C = px1 * px1 / sq_a + py1 * py1 / sq_b - 1;

		// Calculate the discriminant.
		double discriminant = B * B - 4f * A * C;
		if (discriminant<0f) {
			// No solution
			return false;
		}

		if (discriminant==0f) {
			// One real solution.
			double t = -B / 2f / A;
			return ((t >= 0f) && (t <= 1f));
		}

		assert(discriminant>0f);

		// Two real solutions.
		double t1 = (-B + Math.sqrt(discriminant)) / 2f / A;
		double t2 = (-B - Math.sqrt(discriminant)) / 2f / A;

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
	public static boolean intersectsEllipseRectangle(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
		// From AWT Ellipse2D

		double rectw = Math.abs(x4 - x3);
		double recth = Math.abs(y4 - y3);
		double ellw = Math.abs(x2 - x1);
		double ellh = Math.abs(y2 - y1);

		if (rectw <= 0f || recth <= 0f) return false;
		if (ellw <= 0f || ellh <= 0f) return false;

		// Normalize the rectangular coordinates compared to the ellipse
		// having a center at 0,0 and a radius of 0.5.
		double normx0 = (x3 - x1) / ellw - 0.5f;
		double normx1 = normx0 + rectw / ellw;
		double normy0 = (y3 - y1) / ellh - 0.5f;
		double normy1 = normy0 + recth / ellh;
		// find nearest x (left edge, right edge, 0.0)
		// find nearest y (top edge, bottom edge, 0.0)
		// if nearest x,y is inside circle of radius 0.5, then intersects
		double nearx, neary;
		if (normx0 > 0f) {
			// center to left of X extents
			nearx = normx0;
		} else if (normx1 < 0f) {
			// center to right of X extents
			nearx = normx1;
		} else {
			nearx = 0f;
		}
		if (normy0 > 0f) {
			// center above Y extents
			neary = normy0;
		} else if (normy1 < 0f) {
			// center below Y extents
			neary = normy1;
		} else {
			neary = 0f;
		}
		return (nearx * nearx + neary * neary) < 0.25f;
	}



	/**
	 */
	public AbstractEllipse2F() {
		//
	}

	/**
	 * @param min is the min corner of the ellipse.
	 * @param max is the max corner of the ellipse.
	 */
	public AbstractEllipse2F(Point2f min, Point2f max) {
		super(min, max);
	}

	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public AbstractEllipse2F(double x, double y, double width, double height) {
		super(x, y, width, height);
	}

	/**
	 * @param e
	 */
	public AbstractEllipse2F(Ellipse2f e) {
		super(e);
	}


	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public double distanceSquared(Point2D p) {
		Point2D r = getClosestPointTo(p);
		return r.getDistanceSquared(p);
	}

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public double distanceL1(Point2D p) {
		Point2D r = getClosestPointTo(p);
		return r.getDistanceL1(p);
	}

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public double distanceLinf(Point2D p) {
		Point2D r = getClosestPointTo(p);
		return r.getDistanceLinf(p);
	}

	/**
	 * {@inheritDoc}
	 */
	@Pure
	@Override
	public boolean contains(double x, double y) {
		return containsEllipsePoint(
				getMinX(), getMinY(), getWidth(), getHeight(),
				x, y);
	}

	@Pure
	@Override
	public boolean contains(AbstractRectangle2F<?> r) {
		return containsEllipseRectangle(
				getMinX(), getMinY(), getWidth(), getHeight(),
				r.getMinX(), r.getMinY(), r.getWidth(), r.getHeight());
	}

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public Point2D getClosestPointTo(Point2D p) {
		return computeClosestPointToSolidEllipse(
				p.getX(), p.getY(),
				getMinX(), getMinY(),
				getWidth(), getHeight(),
				false);
	}

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public Point2D getFarthestPointTo(Point2D p) {
		return computeFarthestPointToShallowEllipse(
				p.getX(), p.getY(),
				getMinX(), getMinY(),
				getWidth(), getHeight());
	}

	@Pure
	@Override
	public PathIterator2f getPathIterator(Transform2D transform) {
		if (transform==null) {
			return new CopyPathIterator2f(
					getMinX(), getMinY(),
					getMaxX(), getMaxY());
		}
		return new TransformPathIterator2f(
				getMinX(), getMinY(),
				getMaxX(), getMaxY(),
				transform);
	}
	
	@Pure
	@Override
	public PathIterator2d getPathIteratorProperty(Transform2D transform) {
		if (transform==null) {
			return new CopyPathIterator2d(
					getMinX(), getMinY(),
					getMaxX(), getMaxY());
		}
		return new TransformPathIterator2d(
				getMinX(), getMinY(),
				getMaxX(), getMaxY(),
				transform);
	}

	@SuppressWarnings("unchecked")
	@Pure
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof AbstractEllipse2F) {
			AbstractEllipse2F<T> rr2d = (AbstractEllipse2F<T>) obj;
			return ((getMinX() == rr2d.getMinX()) &&
					(getMinY() == rr2d.getMinY()) &&
					(getWidth() == rr2d.getWidth()) &&
					(getHeight() == rr2d.getHeight()));
		}
		return false;
	}

	@Pure
	@Override
	public int hashCode() {
		long bits = 1L;
		bits = 31L * bits + doubleToLongBits(getMinX());
		bits = 31L * bits + doubleToLongBits(getMinY());
		bits = 31L * bits + doubleToLongBits(getMaxX());
		bits = 31L * bits + doubleToLongBits(getMaxY());
		return (int) (bits ^ (bits >> 32));
	}

	@Pure
	@Override
	public boolean intersects(AbstractRectangle2F<?> s) {
		return intersectsEllipseRectangle(
				getMinX(), getMinY(),
				getMaxX(), getMaxY(),
				s.getMinX(), s.getMinY(),
				s.getMaxX(), s.getMaxY());
	}

	@Pure
	@Override
	public boolean intersects(AbstractEllipse2F<?> s) {
		return intersectsEllipseRectangle(
				getMinX(), getMinY(),
				getMaxX(), getMaxY(),
				s.getMinX(), s.getMinY(),
				s.getMaxX(), s.getMaxY());
	}

	@Pure
	@Override
	public boolean intersects(AbstractCircle2F<?> s) {
		return intersectsEllipseEllipse(
				getMinX(), getMinY(),
				getMaxX(), getMaxY(),
				s.getX()-s.getRadius(), s.getY()-s.getRadius(),
				s.getX()+s.getRadius(), s.getY()+s.getRadius());
	}

	@Pure
	@Override
	public boolean intersects(AbstractSegment2F<?> s) {
		return intersectsEllipseSegment(
				getMinX(), getMinY(),
				getWidth(), getHeight(),
				s.getX1(), s.getY1(),
				s.getX2(), s.getY2());
	}

	@Pure
	@Override
	public boolean intersects(Path2f s) {
		return intersects(s.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO));
	}
	
	@Pure
	@Override
	public boolean intersects(Path2d s) {
		return intersects(s.getPathIteratorProperty(MathConstants.SPLINE_APPROXIMATION_RATIO));
	}

	@Override
	public boolean intersects(PathIterator2f s) {
		int mask = (s.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = Path2f.computeCrossingsFromEllipse(
				0,
				s,
				getMinX(), getMinY(), getWidth(), getHeight(),
				false, true);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}
	
	@Override
	public boolean intersects(PathIterator2d s) {
		int mask = (s.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = Path2d.computeCrossingsFromEllipse(
				0,
				s,
				getMinX(), getMinY(), getWidth(), getHeight(),
				false, true);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}

	@Pure
	@Override
	public boolean intersects(AbstractOrientedRectangle2F<?> s) {
		return AbstractOrientedRectangle2F.intersectsOrientedRectangleEllipse(
				s.getCenterX(), s.getCenterY(), 
				s.getFirstAxisX(), s.getFirstAxisY(), s.getFirstAxisExtent(),
				s.getSecondAxisX(), s.getSecondAxisY(), s.getSecondAxisExtent(),
				this.getMinX(), this.getMinY(), getWidth(), getHeight());
	}

	@Override
	public void set(Shape2F s) {
		AbstractRectangle2F<?> r = s.toBoundingBox();
		setFromCorners(r.getMinX(), r.getMinY(), r.getMaxX(), r.getMaxY());
	}


	@Override
	public void clear() {
		this.set(0f, 0f, 0f, 0f);
	}

	@Pure
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("["); //$NON-NLS-1$
		b.append(getMinX());
		b.append(";"); //$NON-NLS-1$
		b.append(getMinY());
		b.append(";"); //$NON-NLS-1$
		b.append(getMaxX());
		b.append(";"); //$NON-NLS-1$
		b.append(getMaxY());
		b.append("]"); //$NON-NLS-1$
		return b.toString();
	}

	/**
	 * @author $Author: galland$
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static class CopyPathIterator2f implements PathIterator2f {

		private final double x1;
		private final double y1;
		private final double w;
		private final double h;
		private int index;
		private double lastX, lastY;

		/**
		 * @param x11
		 * @param y11
		 * @param x2
		 * @param y2
		 */
		public CopyPathIterator2f(double x11, double y11, double x2, double y2) {
			this.x1 = x11;
			this.y1 = y11;
			this.w = x2 - x11;
			this.h = y2 - y11;
			if (this.w==0f && this.h==0f) {
				this.index = 6;
			}
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index<=5;
		}

		@Override
		public AbstractPathElement2F next() {
			if (this.index>5) throw new NoSuchElementException();
			int idx = this.index;
			++this.index;

			if (idx==0) {
				double ctrls[] = CTRL_PTS[3];
				this.lastX = this.x1 + ctrls[4] * this.w;
				this.lastY = this.y1 + ctrls[5] * this.h;
				return new AbstractPathElement2F.MovePathElement2f(
						this.lastX,  this.lastY);
			}
			else if (idx<5) {
				double ctrls[] = CTRL_PTS[idx - 1];
				double ix = this.lastX;
				double iy = this.lastY;
				this.lastX = (this.x1 + ctrls[4] * this.w);
				this.lastY = (this.y1 + ctrls[5] * this.h);
				return new AbstractPathElement2F.CurvePathElement2f(
						ix,  iy,
						(this.x1 + ctrls[0] * this.w),
						(this.y1 + ctrls[1] * this.h),
						(this.x1 + ctrls[2] * this.w),
						(this.y1 + ctrls[3] * this.h),
						this.lastX,
						this.lastY);
			}

			return new AbstractPathElement2F.ClosePathElement2f(
					this.lastX, this.lastY,
					this.lastX, this.lastY);
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

	}
	
	/**
	 * @author $Author: galland$
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static class CopyPathIterator2d implements PathIterator2d {

		private final double x1;
		private final double y1;
		private final double w;
		private final double h;
		private int index;
		private double lastX, lastY;

		/**
		 * @param x11
		 * @param y11
		 * @param x2
		 * @param y2
		 */
		public CopyPathIterator2d(double x11, double y11, double x2, double y2) {
			this.x1 = x11;
			this.y1 = y11;
			this.w = x2 - x11;
			this.h = y2 - y11;
			if (this.w==0f && this.h==0f) {
				this.index = 6;
			}
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index<=5;
		}

		@Override
		public AbstractPathElement2D next() {
			if (this.index>5) throw new NoSuchElementException();
			int idx = this.index;
			++this.index;

			if (idx==0) {
				double ctrls[] = CTRL_PTS[3];
				this.lastX = this.x1 + ctrls[4] * this.w;
				this.lastY = this.y1 + ctrls[5] * this.h;
				return new AbstractPathElement2D.MovePathElement2d(
						this.lastX,  this.lastY);
			}
			else if (idx<5) {
				double ctrls[] = CTRL_PTS[idx - 1];
				double ix = this.lastX;
				double iy = this.lastY;
				this.lastX = (this.x1 + ctrls[4] * this.w);
				this.lastY = (this.y1 + ctrls[5] * this.h);
				return new AbstractPathElement2D.CurvePathElement2d(
						ix,  iy,
						(this.x1 + ctrls[0] * this.w),
						(this.y1 + ctrls[1] * this.h),
						(this.x1 + ctrls[2] * this.w),
						(this.y1 + ctrls[3] * this.h),
						this.lastX,
						this.lastY);
			}

			return new AbstractPathElement2D.ClosePathElement2d(
					this.lastX, this.lastY,
					this.lastX, this.lastY);
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

	}

	/**
	 * @author $Author: galland$
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static class TransformPathIterator2f implements PathIterator2f {

		private final Point2D lastPoint = new Point2f();
		private final Point2D ptmp1 = new Point2f();
		private final Point2D ptmp2 = new Point2f();
		private final Transform2D transform;
		private final double x1;
		private final double y1;
		private final double w;
		private final double h;
		private int index;

		/**
		 * @param x11
		 * @param y11
		 * @param x2
		 * @param y2
		 * @param transform1
		 */
		public TransformPathIterator2f(double x11, double y11, double x2, double y2, Transform2D transform1) {
			this.transform = transform1;
			this.x1 = x11;
			this.y1 = y11;
			this.w = x2 - x11;
			this.h = y2 - y11;
			if (this.w==0f && this.h==0f) {
				this.index = 6;
			}
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index<=5;
		}

		@Override
		public AbstractPathElement2F next() {
			if (this.index>5) throw new NoSuchElementException();
			int idx = this.index;
			++this.index;

			if (idx==0) {
				double ctrls[] = CTRL_PTS[3];
				this.lastPoint.set(
						this.x1 + ctrls[4] * this.w,
						this.y1 + ctrls[5] * this.h);
				this.transform.transform(this.lastPoint);
				return new AbstractPathElement2F.MovePathElement2f(
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
				return new AbstractPathElement2F.CurvePathElement2f(
						ix,  iy,
						this.ptmp1.getX(), this.ptmp1.getY(),
						this.ptmp2.getX(), this.ptmp2.getY(),
						this.lastPoint.getX(), this.lastPoint.getY());
			}

			double ix = this.lastPoint.getX();
			double iy = this.lastPoint.getY();
			return new AbstractPathElement2F.ClosePathElement2f(
					ix, iy,
					ix, iy);
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

	}
	
	/**
	 * @author $Author: galland$
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static class TransformPathIterator2d implements PathIterator2d {

		private final Point2D lastPoint = new Point2f();
		private final Point2D ptmp1 = new Point2f();
		private final Point2D ptmp2 = new Point2f();
		private final Transform2D transform;
		private final double x1;
		private final double y1;
		private final double w;
		private final double h;
		private int index;

		/**
		 * @param x11
		 * @param y11
		 * @param x2
		 * @param y2
		 * @param transform1
		 */
		public TransformPathIterator2d(double x11, double y11, double x2, double y2, Transform2D transform1) {
			this.transform = transform1;
			this.x1 = x11;
			this.y1 = y11;
			this.w = x2 - x11;
			this.h = y2 - y11;
			if (this.w==0f && this.h==0f) {
				this.index = 6;
			}
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index<=5;
		}

		@Override
		public AbstractPathElement2D next() {
			if (this.index>5) throw new NoSuchElementException();
			int idx = this.index;
			++this.index;

			if (idx==0) {
				double ctrls[] = CTRL_PTS[3];
				this.lastPoint.set(
						this.x1 + ctrls[4] * this.w,
						this.y1 + ctrls[5] * this.h);
				this.transform.transform(this.lastPoint);
				return new AbstractPathElement2D.MovePathElement2d(
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
				return new AbstractPathElement2D.CurvePathElement2d(
						ix,  iy,
						this.ptmp1.getX(), this.ptmp1.getY(),
						this.ptmp2.getX(), this.ptmp2.getY(),
						this.lastPoint.getX(), this.lastPoint.getY());
			}

			double ix = this.lastPoint.getX();
			double iy = this.lastPoint.getY();
			return new AbstractPathElement2D.ClosePathElement2d(
					ix, iy,
					ix, iy);
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

	}

}