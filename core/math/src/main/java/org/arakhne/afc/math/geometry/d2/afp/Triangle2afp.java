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
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.afp.Path2afp.CrossingComputationType;
import org.eclipse.xtext.xbase.lib.Pure;

/** Fonctional interface that represented a 2D triangle on a plane.
 *
 * @param <ST> is the type of the general implementation.
 * @param <IT> is the type of the implementation of this shape.
 * @param <IE> is the type of the path elements.
 * @param <P> is the type of the points.
 * @param <V> is the type of the vectors.
 * @param <B> is the type of the bounding boxes.
 * @author $Author: sgalland$
 * @author $Author: fozgul$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface Triangle2afp<
		ST extends Shape2afp<?, ?, IE, P, V, B>,
		IT extends Triangle2afp<?, ?, IE, P, V, B>,
		IE extends PathElement2afp,
		P extends Point2D<? super P, ? super V>,
		V extends Vector2D<? super V, ? super P>,
		B extends Rectangle2afp<?, ?, IE, P, V, B>>
		extends Shape2afp<ST, IT, IE, P, V, B> {

	/**
	 * Replies if three points of a triangle are defined in a counter-clockwise order.
	 * 
	 * @param x1
	 *            is the X coordinate of the first point
	 * @param y1
	 *            is the Y coordinate of the first point
	 * @param x2
	 *            is the X coordinate of the second point
	 * @param y2
	 *            is the Y coordinate of the second point
	 * @param x3
	 *            is the X coordinate of the third point
	 * @param y3
	 *            is the Y coordinate of the third point
	 * @return <code>true</code> if the three given points are defined in a counter-clockwise order.
	 */
	@Pure
	static boolean isCCWOrderDefinition(double x1, double y1, double x2, double y2, double x3, double y3) {
		return Vector2D.perpProduct(x2 - x1, y2 - y1, x3 - x1, y3 - y1) >= 0.;
	}

	/**
	 * Replies if the given point is inside the given triangle.
	 * 
	 * @param tx1
	 * @param ty1
	 * @param tx2
	 * @param ty2
	 * @param tx3
	 * @param ty3
	 * @param px is the point to test.
	 * @param py is the point to test.
	 * @return <code>true</code> if the point is inside the triangle;
	 * <code>false</code> if not.
	 */
	@Pure
	static boolean containsTrianglePoint(double tx1, double ty1, double tx2, double ty2,
			double tx3, double ty3, double px, double py) {
		// Barycentric algorithm
		double ty23 = ty2 - ty3;
		double tx13 = tx1 - tx3;
		double tx32 = tx3 - tx2;
		double ty13 = ty1 - ty3;
		double denominator = ty23 * tx13 + tx32 * ty13;
		if (denominator == 0.) {
			return false;
		}
		double px3 = px - tx3;
		double py3 = py - ty3;
		double a = (ty23 * px3 + tx32 * py3) / denominator;
		double b = (-ty13 * px3 + tx13 * py3) / denominator;
		double c = 1. - a - b;
		return 0. <= a && a <= 1. && 0. <= b && b <= 1. && 0. <= c && c <= 1.;
	}

	/**
	 * Replies if the given point is inside the given triangle.
	 * 
	 * @param tx1
	 * @param ty1
	 * @param tx2
	 * @param ty2
	 * @param tx3
	 * @param ty3
	 * @param rx is the x coordinate of the rectangle.
	 * @param ry is the y coordinate of the rectangle.
	 * @param rwidth the width of the rectangle.
	 * @param rheight the height of the rectangle.
	 * @return <code>true</code> if the rectangle is inside the triangle;
	 * <code>false</code> if not.
	 */
	@Pure
	static boolean containsTriangleRectangle(double tx1, double ty1, double tx2, double ty2,
			double tx3, double ty3, double rx, double ry, double rwidth, double rheight) {
		assert (rwidth >= 0.) : "Width of the rectangle must be positive or zero"; //$NON-NLS-1$
		assert (rheight >= 0.) : "Height of the rectangle must be positive or zero"; //$NON-NLS-1$
		return containsTrianglePoint(tx1, ty1, tx2, ty2, tx3, ty3, rx, ry)
				&& containsTrianglePoint(tx1, ty1, tx2, ty2, tx3, ty3, rx + rwidth, ry)
				&& containsTrianglePoint(tx1, ty1, tx2, ty2, tx3, ty3, rx + rwidth, ry + rheight)
				&& containsTrianglePoint(tx1, ty1, tx2, ty2, tx3, ty3, rx, ry + rheight);
	}

	/**
	 * Replies the closest point to the given point inside the given triangle.
	 * 
	 * @param tx1
	 * @param ty1
	 * @param tx2
	 * @param ty2
	 * @param tx3
	 * @param ty3
	 * @param px is the point to test.
	 * @param py is the point to test.
	 * @param closest the closest point.
	 * @param farthest the farthest point.
	 */
	@Pure
	static void computeClosestFarthestPoints(double tx1, double ty1, double tx2, double ty2,
			double tx3, double ty3, double px, double py, Point2D<?, ?> closest, Point2D<?, ?> farthest) {
		assert (closest != null || farthest != null) : "Both closest and farthest cannot be null"; //$NON-NLS-1$
		if (closest != null) {
			double side1 = Vector2D.perpProduct(tx2 - tx1, ty2 - ty1, px - tx1, py - ty1);
			double side2 = Vector2D.perpProduct(tx3 - tx2, ty3 - ty2, px - tx2, py - ty2);
			double side3 = Vector2D.perpProduct(tx1 - tx3, ty1 - ty3, px - tx3, py - ty3);
			if (side1 <= 0) {
				if (side2 <= 0) {
					closest.set(tx2, ty2);
				} else if (side3 <= 0) {
					closest.set(tx1, ty1);
				} else {
					Segment2afp.computeClosestPointTo(tx1, ty1, tx2, ty2, px, py, closest);
				}
			} else if (side2 <= 0) {
				if (side3 <= 0) {
					closest.set(tx3, ty3);
				} else {
					Segment2afp.computeClosestPointTo(tx2, ty2, tx3, ty3, px, py, closest);
				}
			} else if (side3 <= 0) {
				Segment2afp.computeClosestPointTo(tx3, ty3, tx1, ty1, px, py, closest);
			} else {
				closest.set(px, py);
			}
		}

		if (farthest != null) {
			double dist;
			double x = tx1;
			double y = ty1;
			double max = Math.pow(tx1 - px, 2) + Math.pow(ty1 - py, 2);
			dist = Math.pow(tx2 - px, 2) + Math.pow(ty2 - py, 2);
			if (dist > max) {
				max = dist;
				x = tx2;
				y = ty2;
			}
			dist = Math.pow(tx3 - px, 2) + Math.pow(ty3 - py, 2);
			if (dist > max) {
				max = dist;
				x = tx3;
				y = ty3;
			}
			farthest.set(x, y);
		}
	}

	/**
	 * Replies the squared distance from the given triangle to the given point.
	 * 
	 * @param tx1
	 * @param ty1
	 * @param tx2
	 * @param ty2
	 * @param tx3
	 * @param ty3
	 * @param px is the point.
	 * @param py is the point.
	 * @return the squared distance from the triangle to the point.
	 */
	@Pure
	static double computeSquaredDistanceTrianglePoint(double tx1, double ty1, double tx2, double ty2,
			double tx3, double ty3, double px, double py) {
		double side1 = Vector2D.perpProduct(tx2 - tx1, ty2 - ty1, px - tx1, py - ty1);
		double side2 = Vector2D.perpProduct(tx3 - tx2, ty3 - ty2, px - tx2, py - ty2);
		double side3 = Vector2D.perpProduct(tx1 - tx3, ty1 - ty3, px - tx3, py - ty3);
		if (side1 <= 0) {
			if (side2 <= 0) {
				return Point2D.getDistanceSquaredPointPoint(px, py, tx2, ty2);
			}
			if (side3 <= 0) {
				return Point2D.getDistanceSquaredPointPoint(px, py, tx1, ty1);
			}
			return Segment2afp.computeDistanceSquaredSegmentPoint(tx1, ty1, tx2, ty2, px, py);
		}
		if (side2 <= 0) {
			if (side3 <= 0) {
				return Point2D.getDistanceSquaredPointPoint(px, py, tx3, ty3);
			}
			return Segment2afp.computeDistanceSquaredSegmentPoint(tx2, ty2, tx3, ty3, px, py);
		}
		if (side3 <= 0) {
			return Segment2afp.computeDistanceSquaredSegmentPoint(tx3, ty3, tx1, ty1, px, py);
		}
		return 0.;
	}

	/** Replies if a triangle and a circle are intersecting.
	 * 
	 * @param tx1
	 * @param ty1
	 * @param tx2
	 * @param ty2
	 * @param tx3
	 * @param ty3
	 * @param cx is the center of the circle
	 * @param cy is the center of the circle
	 * @param cradius is the radius of the circle
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	@Pure
	static boolean intersectsTriangleCircle(double tx1, double ty1, double tx2, double ty2,
			double tx3, double ty3, double cx, double cy, double cradius) {
		assert (cradius >= 0) : "Circle radius must be positive or zero"; //$NON-NLS-1$
		double distance = computeSquaredDistanceTrianglePoint(
				tx1, ty1, tx2, ty2, tx3, ty3, cx, cy);
		return distance < cradius * cradius;
	}

	/** Replies if a triangle and an ellipse are intersecting.
	 * 
	 * @param tx1
	 * @param ty1
	 * @param tx2
	 * @param ty2
	 * @param tx3
	 * @param ty3
	 * @param ex is the position of the ellipse
	 * @param ey is the position of the ellipse
	 * @param ewidth is the width of the ellipse
	 * @param eheight is the height of the ellipse
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	@Pure
	static boolean intersectsTriangleEllipse(double tx1, double ty1, double tx2, double ty2,
			double tx3, double ty3, double ex, double ey, double ewidth, double eheight) {
		assert (ewidth >= 0) : "Ellipse width must be positive or zero"; //$NON-NLS-1$
		assert (eheight >= 0) : "Ellipse height must be positive or zero"; //$NON-NLS-1$
		double a = ewidth / 2.;
		double b = eheight / 2.;
		double centerX = ex + a;
		double centerY = ey + b;
		double x1 = (tx1 - centerX) / a;
		double y1 = (ty1 - centerY) / b;
		double x2 = (tx2 - centerX) / a;
		double y2 = (ty2 - centerY) / b;
		double x3 = (tx3 - centerX) / a;
		double y3 = (ty3 - centerY) / b;
		double distance = computeSquaredDistanceTrianglePoint(
				x1, y1, x2, y2, x3, y3, 0, 0);
		return distance < 1.;
	}

	/** Replies if a triangle and a segment are intersecting.
	 * 
	 * @param tx1
	 * @param ty1
	 * @param tx2
	 * @param ty2
	 * @param tx3
	 * @param ty3
	 * @param sx1
	 * @param sy1
	 * @param sx2
	 * @param sy2
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	@Pure
	static boolean intersectsTriangleSegment(double tx1, double ty1, double tx2, double ty2,
			double tx3, double ty3, double sx1, double sy1, double sx2, double sy2) {
		// Separated axis theory on 4 axis (3 directions of the triangle, 1 direction of the segment)
		double vx, vy;
		double min1, max1;
		double min2, max2;
		double a;
		double[] coordinates = new double[] {
				tx2 - tx1, ty2 - ty1,
				tx3 - tx2, ty3 - ty3,
				tx1 - tx3, ty1 - ty3,
				sx2 - sx1, sy2 - sy1
		};
		for (int i = 0; i < coordinates.length;i+=2) {
			vx = coordinates[i];
			vy = coordinates[i+1];
			min1 = Vector2D.perpProduct(vx, vy, tx1, ty1);
			max1 = min1;
			a = Vector2D.perpProduct(vx, vy, tx2, ty2);
			if (a < min1) {
				min1 = a;
			}
			if (a > max1) {
				max1 = a;
			}
			a = Vector2D.perpProduct(vx, vy, tx3, ty3);
			if (a < min1) {
				min1 = a;
			}
			if (a > max1) {
				max1 = a;
			}
			min2 = Vector2D.perpProduct(vx, vy, sx1, sy1);
			max2 = min2;
			a = Vector2D.perpProduct(vx, vy, sx2, sy2);
			if (a < min2) {
				min2 = a;
			}
			if (a > max2) {
				max2 = a;
			}
			if (max1 <= min2 || max2 <= min1) {
				return false;
			}
		}
		return true;
	}

	/** Replies if two triangles are intersecting.
	 * The first triangle must be CCW ordered.
	 * 
	 * @param t1x1
	 * @param t1y1
	 * @param t1x2
	 * @param t1y2
	 * @param t1x3
	 * @param t1y3
	 * @param t2x1
	 * @param t2y1
	 * @param t2x2
	 * @param t2y2
	 * @param t2x3
	 * @param t2y3
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	@Pure
	static boolean intersectsTriangleTriangle(double t1x1, double t1y1, double t1x2, double t1y2,
			double t1x3, double t1y3, double t2x1, double t2y1, double t2x2, double t2y2,
			double t2x3, double t2y3) {
		assert (isCCWOrderDefinition(t1x1, t1y1, t1x2, t1y2, t1x3, t1y3)) : "First triangle must be CCW ordered"; //$NON-NLS-1$
		
		double[] coordinates = new double[] {
				t1x2 - t1x1, t1x1, t1y2 - t1y1, t1y1,
				t2x2 - t2x1, t2x1, t2y2 - t2y1, t2y1,
				t1x3 - t1x2, t1x2, t1y3 - t1y2, t1y2,
				t2x3 - t2x2, t2x2, t2y3 - t2y2, t2y2,
				t1x1 - t1x3, t1x3, t1y1 - t1y3, t1y3, 
				t2x1 - t2x3, t2x3, t2y1 - t2y3, t2y3,
		};
		double a, b, c, d, ox, oy;
		for (int i = 0; i < coordinates.length;i+=8) {
			a = coordinates[i];
			ox = coordinates[i+1];
			b = coordinates[i+2];
			oy = coordinates[i+3];
			if ((Vector2D.perpProduct(a, b, t2x1 - ox, t2y1 - oy) <= 0.)
				&& (Vector2D.perpProduct(a, b, t2x2 - ox, t2y2 - oy) <= 0.)
				&& (Vector2D.perpProduct(a, b, t2x3 - ox, t2y3 - oy) <= 0.)) {
				return false;
			}
			c = coordinates[i+4];
			ox = coordinates[i+5];
			d = coordinates[i+6];
			oy = coordinates[i+7];
			if ((Vector2D.perpProduct(c, d, t1x1 - ox, t1y1 - oy) <= 0.)
				&& (Vector2D.perpProduct(c, d, t1x2 - ox, t1y2 - oy) <= 0.)
				&& (Vector2D.perpProduct(c, d, t1x3 - ox, t1y3 - oy) <= 0.)) {
				return false;
			}
		}

		return  true; 
	}

	/** Replies if a triangle and a rectangle are intersecting.
	 * 
	 * @param tx1
	 * @param ty1
	 * @param tx2
	 * @param ty2
	 * @param tx3
	 * @param ty3
	 * @param rx
	 * @param ry
	 * @param rwidth
	 * @param rheight
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	@Pure
	static boolean intersectsTriangleRectangle(double tx1, double ty1, double tx2, double ty2,
			double tx3, double ty3, double rx, double ry, double rwidth, double rheight) {
		assert (rwidth >= 0.) : "Rectangle width must be positive or zero"; //$NON-NLS-1$
		assert (rheight >= 0.) : "Rectangle height must be positive or zero"; //$NON-NLS-1$
		// Test triangle segment intersection with the rectangle
		double rx2 = rx + rwidth;
		double ry2 = ry + rheight;
		if (Rectangle2afp.intersectsRectangleSegment(rx, ry, rx2, ry2, tx1, ty1, tx2, ty2)
			|| Rectangle2afp.intersectsRectangleSegment(rx, ry, rx2, ry2, tx2, ty2, tx3, ty3)
			|| Rectangle2afp.intersectsRectangleSegment(rx, ry, rx2, ry2, tx3, ty3, tx1, ty1)) {
			return true;
		}
		// Triangle may enclose the rectangle.
		return containsTriangleRectangle(tx1, ty1, tx2, ty2, tx3, ty3, rx, ry, rwidth, rheight);
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
				&& getY2() == shape.getY2()
				&& getX3() == shape.getX3()
				&& getY3() == shape.getY3();
	}

	/** Replies the first point X.
	 * 
	 * @return the first point x.
	 */
	@Pure
	double getX1();

	/** Replies the first point Y.
	 * 
	 * @return the first point y.
	 */
	@Pure
	double getY1();

	/** Replies the second point X.
	 * 
	 * @return the second point x.
	 */
	@Pure
	double getX2();

	/** Replies the second point Y.
	 * 
	 * @return the second point y.
	 */
	@Pure
	double getY2();

	/** Replies the third point X.
	 * 
	 * @return the third point x.
	 */
	@Pure
	double getX3();

	/** Replies the third point Y.
	 * 
	 * @return the third point y.
	 */
	@Pure
	double getY3();

	/** Replies the first point.
	 * 
	 * @return a copy of the first point.
	 */
	@Pure
	default P getP1() {
		return getGeomFactory().newPoint(getX1(), getY1());
	}

	/** Replies the second point.
	 * 
	 * @return a copy of the second point.
	 */
	@Pure
	default P getP2() {
		return getGeomFactory().newPoint(getX2(), getY2());
	}

	/** Replies the third point.
	 * 
	 * @return a copy of the third point.
	 */
	@Pure
	default P getP3() {
		return getGeomFactory().newPoint(getX3(), getY3());
	}

	/** Change the first point.
	 * 
	 * @param point
	 */
	default void setP1(Point2D<?, ?> point) {
		assert (point != null) : "Point must be not null"; //$NON-NLS-1$
		setX1(point.getX());
		setY1(point.getY());
	}

	/** Change the first point.
	 * 
	 * @param x
	 * @param y
	 */
	default void setP1(double x, double y) {
		setX1(x);
		setY1(y);
	}

	/** Change the second point.
	 * 
	 * @param point
	 */
	default void setP2(Point2D<?, ?> point) {
		assert (point != null) : "Point must be not null"; //$NON-NLS-1$
		setX2(point.getX());
		setY2(point.getY());
	}

	/** Change the second point.
	 * 
	 * @param x
	 * @param y
	 */
	default void setP2(double x, double y) {
		setX2(x);
		setY2(y);
	}

	/** Change the third point.
	 * 
	 * @param point
	 */
	default void setP3(Point2D<?, ?> point) {
		assert (point != null) : "Point must be not null"; //$NON-NLS-1$
		setX3(point.getX());
		setY3(point.getY());
	}

	/** Change the third point.
	 * 
	 * @param x
	 * @param y
	 */
	default void setP3(double x, double y) {
		setX3(x);
		setY3(y);
	}

	/** Change the x coordinate of the first point.
	 * 
	 * @param x
	 */
	void setX1(double x);

	/** Change the y coordinate of the first point.
	 * 
	 * @param y
	 */
	void setY1(double y);

	/** Change the x coordinate of the second point.
	 * 
	 * @param x
	 */
	void setX2(double x);

	/** Change the y coordinate of the second point.
	 * 
	 * @param y
	 */
	void setY2(double y);

	/** Change the x coordinate of the third point.
	 * 
	 * @param x
	 */
	void setX3(double x);

	/** Change the y coordinate of the third point.
	 * 
	 * @param y
	 */
	void setY3(double y);

	/** Change the triangle.
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param x3
	 * @param y3
	 */
	void set(double x1, double y1, double x2, double y2, double x3, double y3);

	/** Replies if the points of the triangle are defined in a counter-clockwise order.
	 *
	 * @return <code>true</code> if the triangle points are defined in a counter-clockwise order.
	 */
	boolean isCCW();

	@Override
	default void set(IT s) {
		assert (s != null) : "Shape must be not null"; //$NON-NLS-1$
		set(s.getX1(), s.getY1(), s.getX2(), s.getY2(), s.getX3(), s.getY3());
	}

	@Override
	default void clear() {
		set(0, 0, 0, 0, 0, 0);
	}

	@Override
	default void toBoundingBox(B box) {
		assert (box != null) : "Rectangle must be not null"; //$NON-NLS-1$
		double minx = getX1();
		double maxx = minx;
		double miny = getY1();
		double maxy = miny;
		if (getX2() < minx) {
			minx = getX2();
		}
		if (getX2() > maxx) {
			maxx = getX2();
		}
		if (getY2() < miny) {
			miny = getY2();
		}
		if (getY2() > maxy) {
			maxy = getY2();
		}
		if (getX3() < minx) {
			minx = getX3();
		}
		if (getX3() > maxx) {
			maxx = getX3();
		}
		if (getY3() < miny) {
			miny = getY3();
		}
		if (getY3() > maxy) {
			maxy = getY3();
		}
		box.setFromCorners(minx, miny, maxx, maxy);
	}

	@Override
	default boolean isEmpty() {
		double x = getX1();
		double y = getY1();
		return x == getX2() && x == getX3() && y == getY2() && y == getY3();
	}

	@Pure
	@Override
	default double getDistanceSquared(Point2D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		return computeSquaredDistanceTrianglePoint(getX1(), getY1(), getX2(), getY2(), getX3(), getY3(), p.getX(), p.getY());
	}

	@Pure
	@Override
	default double getDistanceL1(Point2D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		Point2D<?, ?> r = getClosestPointTo(p);
		return r.getDistanceL1(p);
	}

	@Pure
	@Override
	default double getDistanceLinf(Point2D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		Point2D<?, ?> r = getClosestPointTo(p);
		return r.getDistanceLinf(p);
	}

	@Pure
	@Override
	default boolean contains(double x, double y) {
		return containsTrianglePoint(getX1(), getY1(), getX2(), getY2(), getX3(), getY3(), x, y);
	}

	@Override
	default boolean contains(Rectangle2afp<?, ?, ?, ?, ?, ?> r) {
		assert (r != null) : "Rectangle must be not null"; //$NON-NLS-1$
		return containsTriangleRectangle(getX1(), getY1(), getX2(), getY2(), getX3(), getY3(),
				r.getMinX(), r.getMinY(), r.getWidth(), r.getHeight());
	}

	@Override
	default void translate(double dx, double dy) {
		setP1(getX1() + dx, getY1() + dy);
		setP2(getX2() + dx, getY2() + dy);
		setP3(getX3() + dx, getY3() + dy);
	}

	@Pure
	@Override
	default boolean intersects(Rectangle2afp<?, ?, ?, ?, ?, ?> r) {
		assert (r != null) : "Rectangle must be not null"; //$NON-NLS-1$
		return intersectsTriangleRectangle(getX1(), getY1(), getX2(), getY2(), getX3(), getY3(),
				r.getMinX(), r.getMinY(), r.getWidth(), r.getHeight());
	}

	@Pure
	@Override
	default boolean intersects(Ellipse2afp<?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "Ellipse must be not null"; //$NON-NLS-1$
		return intersectsTriangleEllipse(getX1(), getY1(), getX2(), getY2(), getX3(), getY3(),
				s.getMinX(), s.getMinY(), s.getWidth(), s.getHeight());
	}

	@Pure
	@Override
	default boolean intersects(Circle2afp<?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "Circle must be not null"; //$NON-NLS-1$
		return intersectsTriangleCircle(getX1(), getY1(), getX2(), getY2(), getX3(), getY3(),
				s.getX(), s.getY(), s.getRadius());
	}

	@Pure
	@Override
	default boolean intersects(Segment2afp<?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "Segment must be not null"; //$NON-NLS-1$
		return intersectsTriangleSegment(getX1(), getY1(), getX2(), getY2(), getX3(), getY3(),
				s.getX1(), s.getY1(), s.getX2(), s.getY2());
	}

	@Pure
	@Override
	default boolean intersects(Triangle2afp<?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "Triangle must be not null"; //$NON-NLS-1$
		if (isCCW()) {
			return intersectsTriangleTriangle(getX1(), getY1(), getX2(), getY2(), getX3(), getY3(),
					s.getX1(), s.getY1(), s.getX2(), s.getY2(), s.getX3(), s.getY3());
		}
		return intersectsTriangleTriangle(getX1(), getY1(), getX3(), getY3(), getX2(), getY2(),
				s.getX1(), s.getY1(), s.getX2(), s.getY2(), s.getX3(), s.getY3());
	}

	@Pure
	@Override
	default boolean intersects(OrientedRectangle2afp<?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "Oriented rectangle must be not null"; //$NON-NLS-1$
		return OrientedRectangle2afp.intersectsOrientedRectangleTriangle(
				s.getCenterX(), s.getCenterY(),
				s.getFirstAxisX(), s.getFirstAxisY(), s.getFirstAxisExtent(),
				s.getSecondAxisExtent(),
				getX1(), getY1(), getX2(), getY2(), getX3(), getY3());
	}

	@Pure
	@Override
	default boolean intersects(Parallelogram2afp<?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "Parallelogram must be not null"; //$NON-NLS-1$
		return Parallelogram2afp.intersectsParallelogramTriangle(
				s.getCenterX(), s.getCenterY(),
				s.getFirstAxisX(),
				s.getFirstAxisY(),
				s.getFirstAxisExtent(),
				s.getSecondAxisX(),
				s.getSecondAxisY(),
				s.getSecondAxisExtent(),
				getX1(), getY1(),
				getX2(), getY2(),
				getX3(), getY3());
	}

	@Pure
	@Override
	default boolean intersects(PathIterator2afp<?> iterator) {
		assert (iterator != null) : "Iterator must be not null"; //$NON-NLS-1$
		int mask = (iterator.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = Path2afp.computeCrossingsFromTriangle(
				0, 
				iterator,
				getX1(), getY1(), getX2(), getY2(), getX3(), getY3(),
				CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}

	@Pure
	@Override
	default boolean intersects(RoundRectangle2afp<?, ?, ?, ?, ?, ?> s) {
		assert (s!= null) : "Round rectangle must be not null"; //$NON-NLS-1$
		return s.intersects(getPathIterator());
	}

	@Pure
	@Override
	default boolean intersects(MultiShape2afp<?, ?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "MultiShape must be not null"; //$NON-NLS-1$
		return s.intersects(this);
	}

	@Pure
	@Override
	default P getClosestPointTo(Point2D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		P point = getGeomFactory().newPoint();
		computeClosestFarthestPoints(getX1(), getY1(), getX2(), getY2(), getX3(), getY3(),
				p.getX(), p.getY(), point, null);
		return point;
	}

	@Pure
	@Override
	default P getFarthestPointTo(Point2D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		P point = getGeomFactory().newPoint();
		computeClosestFarthestPoints(getX1(), getY1(), getX2(), getY2(), getX3(), getY3(),
				p.getX(), p.getY(), null, point);
		return point;
	}

	@Pure
	@Override
	default PathIterator2afp<IE> getPathIterator(Transform2D transform) {
		if (transform==null || transform.isIdentity()) {
			return new TrianglePathIterator<>(this);
		}
		return new TransformedTrianglePathIterator<>(this, transform);
	}

	/** Abstract iterator on the path elements of the triangle.
	 * 
	 * @param <T> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	abstract class AbstractTrianglePathIterator<T extends PathElement2afp> implements PathIterator2afp<T> {

		/** Number of path elements.
		 */
		protected final static int NUMBER_ELEMENTS = 3;

		/** The iterated shape.
		 */
		protected final Triangle2afp<?, ?, T, ?, ?, ?> triangle;

		/**
		 * @param triangle the iterated shape.
		 */
		public AbstractTrianglePathIterator(Triangle2afp<?, ?, T, ?, ?, ?> triangle) {
			assert (triangle != null) : "Triangle must be not null"; //$NON-NLS-1$
			this.triangle = triangle;
		}

		@Override
		public GeomFactory2afp<T, ?, ?, ?> getGeomFactory() {
			return this.triangle.getGeomFactory();
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

		@Override
		public boolean isCurved() {
			return true;
		}

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

	/** Iterator on the path elements of the triangle.
	 * 
	 * @param <T> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	class TrianglePathIterator<T extends PathElement2afp> extends AbstractTrianglePathIterator<T> {

		private double x1;

		private double y1;

		private double x2;

		private double y2;

		private double x3;

		private double y3;

		private int index;

		private double movex;

		private double movey;

		private double lastx;

		private double lasty;

		/**
		 * @param triangle the triangle to iterate on.
		 */
		public TrianglePathIterator(Triangle2afp<?, ?, T, ?, ?, ?> triangle) {
			super(triangle);
			if (triangle.isEmpty()) {
				this.index = NUMBER_ELEMENTS;
			} else {
				this.x1 = triangle.getX1();
				this.y1 = triangle.getY1();
				this.x2 = triangle.getX2();
				this.y2 = triangle.getY2();
				this.x3 = triangle.getX3();
				this.y3 = triangle.getY3();
				this.index = -1;
			}
		}
		
		@Override
		public PathIterator2afp<T> restartIterations() {
			return new TrianglePathIterator<>(this.triangle);
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index < NUMBER_ELEMENTS;
		}

		@Override
		public T next() {
			if (this.index >= NUMBER_ELEMENTS) {
				throw new NoSuchElementException();
			}
			int idx = this.index;
			++this.index;
			if (idx < 0) {
				this.movex = this.x1;
				this.movey = this.y1;
				this.lastx = this.movex;
				this.lasty = this.movey;
				return getGeomFactory().newMovePathElement(
						this.lastx, this.lasty);
			}
			double ppx = this.lastx;
			double ppy = this.lasty;
			switch(idx) {
			case 0:
				this.lastx = this.x2;
				this.lasty = this.y2;
				return getGeomFactory().newLinePathElement(
						ppx, ppy,
						this.lastx, this.lasty);
			case 1:
				this.lastx = this.x3;
				this.lasty = this.y3;
				return getGeomFactory().newLinePathElement(
						ppx, ppy,
						this.lastx, this.lasty);
			default:
				return getGeomFactory().newClosePathElement(
						this.lastx, this.lasty,
						this.movex, this.movey);
			}
		}

	}

	/** Iterator on the path elements of the circle.
	 * 
	 * @param <T> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	class TransformedTrianglePathIterator<T extends PathElement2afp> extends AbstractTrianglePathIterator<T> {

		private final Transform2D transform;

		private final Point2D<?, ?> tmpPoint;

		private double x1;

		private double y1;

		private double x2;

		private double y2;

		private double x3;

		private double y3;

		private int index;

		private double movex;

		private double movey;

		private double lastx;

		private double lasty;

		/**
		 * @param triangle the iterated triangle.
		 * @param transform the transformation to apply.
		 */
		public TransformedTrianglePathIterator(Triangle2afp<?, ?, T, ?, ?, ?> triangle, Transform2D transform) {
			super(triangle);
			assert(transform != null) : "Transformation must be not null"; //$NON-NLS-1$
			this.transform = transform;
			if (triangle.isEmpty()) {
				this.index = NUMBER_ELEMENTS;
				this.tmpPoint = null;
			} else {
				this.tmpPoint = new InnerComputationPoint2afp();
				this.x1 = triangle.getX1();
				this.y1 = triangle.getY1();
				this.x2 = triangle.getX2();
				this.y2 = triangle.getY2();
				this.x3 = triangle.getX3();
				this.y3 = triangle.getY3();
				this.index = -1;
			}
		}
		
		@Override
		public PathIterator2afp<T> restartIterations() {
			return new TransformedTrianglePathIterator<>(this.triangle, this.transform);
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index < NUMBER_ELEMENTS;
		}

		@Override
		public T next() {
			if (this.index >= NUMBER_ELEMENTS) {
				throw new NoSuchElementException();
			}
			int idx = this.index;
			++this.index;
			if (idx < 0) {
				this.tmpPoint.set(this.x1, this.y1);
				this.transform.transform(this.tmpPoint);
				this.movex = this.tmpPoint.getX();
				this.movey = this.tmpPoint.getY();
				this.lastx = this.movex;
				this.lasty = this.movey;
				return getGeomFactory().newMovePathElement(
						this.lastx, this.lasty);
			}
			double ppx = this.lastx;
			double ppy = this.lasty;
			switch(idx) {
			case 0:
				this.tmpPoint.set(this.x2, this.y2);
				this.transform.transform(this.tmpPoint);
				this.lastx = this.tmpPoint.getX();
				this.lasty = this.tmpPoint.getY();
				return getGeomFactory().newLinePathElement(
						ppx, ppy,
						this.lastx, this.lasty);
			case 1:
				this.tmpPoint.set(this.x3, this.y3);
				this.transform.transform(this.tmpPoint);
				this.lastx = this.tmpPoint.getX();
				this.lasty = this.tmpPoint.getY();
				return getGeomFactory().newLinePathElement(
						ppx, ppy,
						this.lastx, this.lasty);
			default:
				return getGeomFactory().newClosePathElement(
						this.lastx, this.lasty,
						this.movex, this.movey);
			}
		}

	}	

}
