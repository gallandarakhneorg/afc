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

package org.arakhne.afc.math.geometry.d3.ad;

import java.util.NoSuchElementException;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.ad.Path3ad.CrossingComputationType;
import org.eclipse.xtext.xbase.lib.Pure;

/** Fonctional interface that represented a 2D rectangle on a plane.
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
 * @since 13.0
 */
public interface RectangularPrism3ad<
		ST extends Shape3ad<?, ?, IE, P, V, B>,
		IT extends RectangularPrism3ad<?, ?, IE, P, V, B>,
		IE extends PathElement3ad,
		P extends Point3D<? super P, ? super V>,
		V extends Vector3D<? super V, ? super P>,
		B extends RectangularPrism3ad<?, ?, IE, P, V, B>>
		extends Prism3ad<ST, IT, IE, P, V, B>{

	/** Replies if two rectangles are intersecting.
	 * 
	 * @param x1 is the first corner of the first rectangle.
	 * @param y1 is the first corner of the first rectangle.
	 * @param z1 is the first corner of the first rectangle.
	 * @param x2 is the second corner of the first rectangle.
	 * @param y2 is the second corner of the first rectangle.
	 * @param z2 is the second corner of the first rectangle.
	 * @param x3 is the first corner of the second rectangle.
	 * @param y3 is the first corner of the second rectangle.
	 * @param z3 is the first corner of the second rectangle.
	 * @param x4 is the second corner of the second rectangle.
	 * @param y4 is the second corner of the second rectangle.
	 * @param z4 is the second corner of the second rectangle.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	@Pure
	static boolean intersectsRectangleRectangle(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4) {
		assert (x1 <= x2) : "x1 must be lower or equal to x2"; //$NON-NLS-1$
		assert (y1 <= y2) : "y1 must be lower or equal to y2"; //$NON-NLS-1$
		assert (z1 <= z2) : "z1 must be lower or equal to z2"; //$NON-NLS-1$
		assert (x3 <= x4) : "x3 must be lower or equal to x4"; //$NON-NLS-1$
		assert (y3 <= y4) : "y3 must be lower or equal to x4"; //$NON-NLS-1$
		assert (z3 <= z4) : "z3 must be lower or equal to z4"; //$NON-NLS-1$
		return x2 > x3 && x1 < x4 && y2 > y3 && y1 < y4 && z2 > z3 && z1 < z4;
	}

	/** Replies if two rectangles are intersecting.
	 * 
	 * @param x1 is the first corner of the rectangle.
	 * @param y1 is the first corner of the rectangle.
	 * @param z1 is the first corner of the rectangle.
	 * @param x2 is the second corner of the rectangle.
	 * @param y2 is the second corner of the rectangle.
	 * @param z2 is the second corner of the rectangle.
	 * @param x3 is the first point of the line.
	 * @param y3 is the first point of the line.
	 * @param z3 is the first point of the line.
	 * @param x4 is the second point of the line.
	 * @param y4 is the second point of the line.
	 * @param z4 is the second point of the line.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	@Pure
	// TODO
	static boolean intersectsRectangleLine(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4) {
		assert (x1 <= x2) : "x1 must be lower or equal to x2"; //$NON-NLS-1$
		assert (y1 <= y2) : "y1 must be lower or equal to y2"; //$NON-NLS-1$
//		int a, b;
//		a = Segment2afp.ccw(x3, y3, x4, y4, x1, y1, 0.);
//		b = Segment2afp.ccw(x3, y3, x4, y4, x2, y1, 0.);
//		if (a!=b && b!=0) return true;
//		b = Segment2afp.ccw(x3, y3, x4, y4, x2, y2, 0.);
//		if (a!=b && b!=0) return true;
//		b = Segment2afp.ccw(x3, y3, x4, y4, x1, y2, 0.);
//		return (a!=b && b!=0);
		return false;
	}

	/** Replies if the rectangle is intersecting the segment.
	 * 
	 * @param rx1 is the first corner of the rectangle.
	 * @param ry1 is the first corner of the rectangle.
	 * @param rx2 is the second corner of the rectangle.
	 * @param ry2 is the second corner of the rectangle.
	 * @param sx1 is the first point of the segment.
	 * @param sy1 is the first point of the segment.
	 * @param sx2 is the second point of the segment.
	 * @param sy2 is the second point of the segment.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	@Pure
	// TODO
	static boolean intersectsRectangleSegment(double rx1, double ry1, double rx2, double ry2, double sx1, double sy1, double sx2, double sy2) {
		assert (rx1 <= rx2) : "rx1 must be lower or equal to rx2"; //$NON-NLS-1$
		assert (ry1 <= ry2) : "ry1 must be lower or equal to ry2"; //$NON-NLS-1$
//		double segmentX1 = sx1;
//		double segmentY1 = sy1;
//		double segmentX2 = sx2;
//		double segmentY2 = sy2;
//		
//		int code1 = MathUtil.getCohenSutherlandCode(segmentX1, segmentY1, rx1, ry1, rx2, ry2);
//		int code2 = MathUtil.getCohenSutherlandCode(segmentX2, segmentY2, rx1, ry1, rx2, ry2);
//
//		while (true) {
//			if ((code1 | code2) == 0) {
//				// Bitwise OR is 0. Trivially accept and get out of loop
//				// Special case: if the segment is empty, it is on the border => no intersection.
//				return segmentX1 != segmentX2 || segmentY1 != segmentY2;
//			}
//			if ((code1 & code2) != 0) {
//				// Bitwise AND is not 0. Trivially reject and get out of loop
//				return false;
//			}
//
//			// failed both tests, so calculate the line segment intersection
//
//			// At least one endpoint is outside the clip rectangle; pick it.
//			int code3 = (code1 != 0) ? code1 : code2;
//
//			double x = 0;
//			double y = 0;
//			
//			// Now find the intersection point;
//			// use formulas y = y0 + slope * (x - x0), x = x0 + (1 / slope) * (y - y0)
//			if ((code3 & MathConstants.COHEN_SUTHERLAND_TOP) != 0) {
//				// point is above the clip rectangle
//				x = segmentX1 + (segmentX2 - segmentX1) * (ry2 - segmentY1) / (segmentY2 - segmentY1);
//				y = ry2;
//			}
//			else if ((code3 & MathConstants.COHEN_SUTHERLAND_BOTTOM) != 0) {
//				// point is below the clip rectangle
//				x = segmentX1 + (segmentX2 - segmentX1) * (ry1 - segmentY1) / (segmentY2 - segmentY1);
//				y = ry1;
//			}
//			else if ((code3 & MathConstants.COHEN_SUTHERLAND_RIGHT) != 0) { 
//				// point is to the right of clip rectangle
//				y = segmentY1 + (segmentY2 - segmentY1) * (rx2 - segmentX1) / (segmentX2 - segmentX1);
//				x = rx2;
//			}
//			else if ((code3 & MathConstants.COHEN_SUTHERLAND_LEFT) != 0) {
//				// point is to the left of clip rectangle
//				y = segmentY1 + (segmentY2 - segmentY1) * (rx1 - segmentX1) / (segmentX2 - segmentX1);
//				x = rx1;
//			}
//			else {
//				code3 = 0;
//			}
//
//			if (code3 != 0) {
//				// Now we move outside point to intersection point to clip
//				// and get ready for next pass.
//				if (code3 == code1) {
//					segmentX1 = x;
//					segmentY1 = y;
//					code1 = MathUtil.getCohenSutherlandCode(segmentX1, segmentY1, rx1, ry1, rx2, ry2);
//				}
//				else {
//					segmentX2 = x;
//					segmentY2 = y;
//					code2 = MathUtil.getCohenSutherlandCode(segmentX2, segmentY2, rx1, ry1, rx2, ry2);
//				}
//			}
//		}
		return false;
	}

	/** Replies if a rectangle is inside in the rectangle.
	 * 
	 * @param enclosingX1 is the lowest corner of the enclosing-candidate rectangle.
	 * @param enclosingY1 is the lowest corner of the enclosing-candidate rectangle.
	 * @param enclosingZ1 is the lowest corner of the enclosing-candidate rectangle.
	 * @param enclosingX2 is the uppest corner of the enclosing-candidate rectangle.
	 * @param enclosingY2 is the uppest corner of the enclosing-candidate rectangle.
	 * @param enclosingZ2 is the uppest corner of the enclosing-candidate rectangle.
	 * @param innerX1 is the lowest corner of the inner-candidate rectangle.
	 * @param innerY1 is the lowest corner of the inner-candidate rectangle.
	 * @param innerZ1 is the lowest corner of the inner-candidate rectangle.
	 * @param innerX2 is the uppest corner of the inner-candidate rectangle.
	 * @param innerY2 is the uppest corner of the inner-candidate rectangle.
	 * @param innerZ2 is the uppest corner of the inner-candidate rectangle.
	 * @return <code>true</code> if the given rectangle is inside the ellipse;
	 * otherwise <code>false</code>.
	 */
	@Pure
	public static boolean containsRectangleRectangle(double enclosingX1, double enclosingY1, double enclosingZ1,
			double enclosingX2, double enclosingY2, double enclosingZ2,
			double innerX1, double innerY1, double innerZ1,
			double innerX2, double innerY2, double innerZ2) {
		assert (enclosingX1 <= enclosingX2) : "Enclosing x1 must be lower or equal to enclosing x2"; //$NON-NLS-1$
		assert (enclosingY1 <= enclosingY2) : "Enclosing y1 must be lower or equal to enclosing y2"; //$NON-NLS-1$
		assert (enclosingZ1 <= enclosingZ2) : "Enclosing z1 must be lower or equal to enclosing z2"; //$NON-NLS-1$
		assert (innerX1 <= innerX2) : "Inner x1 must be lower or equal to inner x2"; //$NON-NLS-1$
		assert (innerY1 <= innerY2) : "Inner y1 must be lower or equal to inner y2"; //$NON-NLS-1$
		assert (innerZ1 <= innerZ2) : "Inner y1 must be lower or equal to inner y2"; //$NON-NLS-1$
		return innerX1 >= enclosingX1 &&
				innerY1 >= enclosingY1 &&
				innerZ1 >= enclosingZ1 &&
				innerX2 <= enclosingX2 &&
				innerY2 <= enclosingY2 &&
				innerZ2 <= enclosingZ2;
	}

	/** Replies if a point is inside in the rectangle.
	 * 
	 * @param rx1 is the lowest corner of the rectangle.
	 * @param ry1 is the lowest corner of the rectangle.
	 * @param rz1 is the lowest corner of the rectangle.
	 * @param rx2 is the uppest corner of the rectangle.
	 * @param ry2 is the uppest corner of the rectangle.
	 * @param rz2 is the uppest corner of the rectangle.
	 * @param px is the point.
	 * @param py is the point.
	 * @param pz is the point.
	 * @return <code>true</code> if the given point is inside the rectangle;
	 * otherwise <code>false</code>.
	 */
	@Pure
	static boolean containsRectanglePoint(
			double rx1, double ry1, double rz1, double rx2, double ry2, double rz2,
			double px, double py, double pz) {
		assert (rx1 <= rx2) : "rx1 must be lower or equal to rx2"; //$NON-NLS-1$
		assert (ry1 <= ry2) : "ry1 must be lower or equal to ry2"; //$NON-NLS-1$
		assert (rz1 <= rz2) : "rz1 must be lower or equal to rz2"; //$NON-NLS-1$
		return (px >= rx1 && px <= rx2) && (py >= ry1 && py <= ry2) && (pz >= rz1 && pz <= rz2);
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
			&& getMinZ() == shape.getMinZ()
			&& getMaxX() == shape.getMaxX()
			&& getMaxY() == shape.getMaxY()
			&& getMaxZ() == shape.getMaxZ();
	}

	@Override
	default void set(IT s) {
		assert (s != null) : "Shape must be not null"; //$NON-NLS-1$
		setFromCorners(s.getMinX(), s.getMinY(), s.getMinZ(), s.getMaxX(), s.getMaxY(), s.getMaxZ());
	}

	@Pure
	@Override
	default double getDistanceSquared(Point3D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		double dx;
		if (p.getX()<getMinX()) {
			dx = getMinX() - p.getX();
		}
		else if (p.getX()>getMaxX()) {
			dx = p.getX() - getMaxX();
		}
		else {
			dx = 0f;
		}
		double dy;
		if (p.getY()<getMinY()) {
			dy = getMinY() - p.getY();
		}
		else if (p.getY()>getMaxY()) {
			dy = p.getY() - getMaxY();
		}
		else {
			dy = 0f;
		}
		double dz;
		if (p.getZ()<getMinZ()) {
			dz = getMinZ() - p.getZ();
		}
		else if (p.getZ()>getMaxZ()) {
			dz = p.getZ() - getMaxZ();
		}
		else {
			dz = 0f;
		}
		return dx*dx+dy*dy+dz*dz;
	}

	@Pure
	@Override
	default double getDistanceL1(Point3D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		double dx;
		if (p.getX()<getMinX()) {
			dx = getMinX() - p.getX();
		}
		else if (p.getX()>getMaxX()) {
			dx = p.getX() - getMaxX();
		}
		else {
			dx = 0f;
		}
		double dy;
		if (p.getY()<getMinY()) {
			dy = getMinY() - p.getY();
		}
		else if (p.getY()>getMaxY()) {
			dy = p.getY() - getMaxY();
		}
		else {
			dy = 0f;
		}
		double dz;
		if (p.getZ()<getMinZ()) {
			dz = getMinZ() - p.getZ();
		}
		else if (p.getZ()>getMaxZ()) {
			dz = p.getZ() - getMaxZ();
		}
		else {
			dz = 0f;
		}
		return dx + dy + dz;
	}

	@Pure
	@Override
	default double getDistanceLinf(Point3D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		double dx;
		if (p.getX()<getMinX()) {
			dx = getMinX() - p.getX();
		}
		else if (p.getX()>getMaxX()) {
			dx = p.getX() - getMaxX();
		}
		else {
			dx = 0f;
		}
		double dy;
		if (p.getY()<getMinY()) {
			dy = getMinY() - p.getY();
		}
		else if (p.getY()>getMaxY()) {
			dy = p.getY() - getMaxY();
		}
		else {
			dy = 0f;
		}
		double dz;
		if (p.getZ()<getMinZ()) {
			dz = getMinZ() - p.getZ();
		}
		else if (p.getZ()>getMaxZ()) {
			dz = p.getZ() - getMaxZ();
		}
		else {
			dz = 0f;
		}
		return MathUtil.max(dx, dy, dz);
	}

	@Pure
	@Override
	default boolean contains(double x, double y, double z) {
		return (x>=getMinX() && x<=getMaxX())
				&&
				(y>=getMinY() && y<=getMaxY())
				&&
				(z>=getMinZ() && z<=getMaxZ());
	}

	@Pure
	@Override
	default boolean contains(RectangularPrism3ad<?, ?, ?, ?, ?, ?> r) {
		assert (r != null) : "Rectangle must be not null"; //$NON-NLS-1$
		return containsRectangleRectangle(
				getMinX(), getMinY(), getMinZ(), getMaxX(), getMaxY(), getMaxZ(),
				r.getMinX(), r.getMinY(), r.getMinZ(), r.getMaxX(), r.getMaxY(), r.getMaxZ());
	}

	/** Add the given coordinate in the rectangle.
	 * <p>
	 * The corners of the rectangles are moved to
	 * enclosed the given coordinate.
	 * 
	 * @param p
	 */
	default void add(Point3D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		add(p.getX(), p.getY(), p.getZ());
	}

	/** Add the given coordinate in the rectangle.
	 * <p>
	 * The corners of the rectangles are moved for
	 * enclosing the given coordinate.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	default void add(double x, double y, double z) {
		if (x < getMinX()) {
			setMinX(x);
		} else if (x > getMaxX()) {
			setMaxX(x);
		}
		if (y < getMinY()) {
			setMinY(y);
		} else if (y > getMaxY()) {
			setMaxY(y);
		}
		if (z < getMinZ()) {
			setMinZ(z);
		} else if (z > getMaxZ()) {
			setMaxZ(z);
		}
	}
	
	/** Compute and replies the union of this rectangle and the given rectangle.
	 * This function does not change this rectangle.
	 * <p>
	 * It is equivalent to (where <code>ur</code> is the union):
	 * <pre><code>
	 * Rectangle2f ur = new Rectangle2f(this);
	 * ur.setUnion(r);
	 * </code></pre>
	 * 
	 * @param r
	 * @return the union of this rectangle and the given rectangle.
	 * @see #setUnion(Prism3ad)
	 */
	@Pure
	default B createUnion(Prism3ad<?, ?, ?, ?, ?, ?> r) {
		assert (r != null) : "Shape must be not null"; //$NON-NLS-1$
		B rr = getGeomFactory().newBox();
		rr.setFromCorners(getMinX(), getMinY(), getMinZ(), getMaxX(), getMaxY(), getMaxZ());
		rr.setUnion(r);
		return rr;
	}

	/** Compute and replies the intersection of this rectangle and the given rectangle.
	 * This function does not change this rectangle.
	 * 
	 * <p>It is equivalent to (where <code>ir</code> is the intersection):
	 * <pre><code>
	 * Rectangle2f ir = new Rectangle2f(this);
	 * ir.setIntersection(r);
	 * </code></pre>
	 * 
	 * @param r
	 * @return the union of this rectangle and the given rectangle.
	 * @see #setIntersection(Prism3ad)
	 */
	@Pure
	default B createIntersection(Prism3ad<?, ?, ?, ?, ?, ?> r) {
		assert (r != null) : "Shape must be not null"; //$NON-NLS-1$
		B rr = getGeomFactory().newBox();
		double x1 = Math.max(getMinX(), r.getMinX());
		double y1 = Math.max(getMinY(), r.getMinY());
		double z1 = Math.max(getMinZ(), r.getMinZ());
		double x2 = Math.min(getMaxX(), r.getMaxX());
		double y2 = Math.min(getMaxY(), r.getMaxY());
		double z2 = Math.min(getMaxZ(), r.getMaxZ());
		if (x1<=x2 && y1<=y2 && z1<=z2) {
			rr.setFromCorners(x1, y1, z1, x2, y2, z2);
		}
		else {
			rr.clear();
		}
		return rr;
	}

	/** Compute the union of this rectangle and the given rectangle and
	 * change this rectangle with the result of the union.
	 * 
	 * @param r
	 * @see #createUnion(Prism3ad)
	 */
	default void setUnion(Prism3ad<?, ?, ?, ?, ?, ?> r) {
		assert (r != null) : "Shape must be not null"; //$NON-NLS-1$
		setFromCorners(
				Math.min(getMinX(), r.getMinX()),
				Math.min(getMinY(), r.getMinY()),
				Math.min(getMinZ(), r.getMinZ()),
				Math.max(getMaxX(), r.getMaxX()),
				Math.max(getMaxY(), r.getMaxY()),
				Math.max(getMaxZ(), r.getMaxZ()));
	}

	/** Compute the intersection of this rectangle and the given rectangle.
	 * This function changes this rectangle.
	 * 
	 * <p>If there is no intersection, this rectangle is cleared.
	 * 
	 * @param r
	 * @see #createIntersection(Prism3ad)
	 * @see #clear()
	 */
	default void setIntersection(RectangularPrism3ad<?, ?, ?, ?, ?, ?> r) {
		assert (r != null) : "Shape must be not null"; //$NON-NLS-1$
		double x1 = Math.max(getMinX(), r.getMinX());
		double y1 = Math.max(getMinY(), r.getMinY());
		double z1 = Math.max(getMinZ(), r.getMinZ());
		double x2 = Math.min(getMaxX(), r.getMaxX());
		double y2 = Math.min(getMaxY(), r.getMaxY());
		double z2 = Math.min(getMaxZ(), r.getMaxZ());
		if (x1<=x2 && y1<=y2 && z1<=z2) {
			setFromCorners(x1, y1, z1, x2, y2, z2);
		}
		else {
			clear();
		}
	}

	@Pure
	@Override
	default boolean intersects(Prism3ad<?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "Rectangle must be not null"; //$NON-NLS-1$
		return intersectsRectangleRectangle(
				getMinX(), getMinY(), getMinZ(),
				getMaxX(), getMaxY(), getMaxZ(),
				s.getMinX(), s.getMinY(), s.getMinZ(),
				s.getMaxX(), s.getMaxY(), s.getMaxZ());
	}
	

	
	@Pure
	@Override
	default boolean intersects(Sphere3ad<?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "Circle must be not null"; //$NON-NLS-1$
		return Sphere3ad.intersectsCircleRectangle(
				s.getX(), s.getY(),
				s.getRadius(),
				getMinX(), getMinY(),
				getMaxX(), getMaxY());
	}
	
	@Pure
	@Override
	default boolean intersects(Segment3ad<?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "Segment must be not null"; //$NON-NLS-1$
		return intersectsRectangleSegment(
				getMinX(), getMinY(),
				getMaxX(), getMaxY(),
				s.getX1(), s.getY1(),
				s.getX2(), s.getY2());
	}

	@Pure
	@Override
	default boolean intersects(PathIterator3ad<?> iterator) {
		assert (iterator != null) : "Iterator must be not null"; //$NON-NLS-1$
		int mask = (iterator.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = Path3ad.computeCrossingsFromRect(
				0,
				iterator,
				getMinX(), getMinY(), getMinZ(), getMaxX(), getMaxY(), getMaxZ(),
				CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);

	}
	
	@Pure
	@Override
	default boolean intersects(MultiShape3ad<?, ?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "MultiShape must be not null"; //$NON-NLS-1$
		return s.intersects(this);
	}

	/** Move this rectangle to avoid collision 
	 * with the reference rectangle.
	 * 
	 * @param reference is the rectangle to avoid collision with.
	 * @param result the displacement vector.
	 */
	// TODO
	default void avoidCollisionWith(RectangularPrism3ad<?, ?, ?, ?, ?, ?> reference, Vector3D<?, ?> result) {
		assert (reference != null) : "Reference rectangle must be not null"; //$NON-NLS-1$
		assert (result != null) : "Result vector must be not null"; //$NON-NLS-1$
		double dx1 = reference.getMaxX() - getMinX();
		double dx2 = getMaxX() - reference.getMinX();
		double dy1 = reference.getMaxY() - getMinY();
		double dy2 = getMaxY() - reference.getMinY();
//		double dz1 = reference.getMaxZ() - getMinZ();
//		double dz2 = getMaxZ() - reference.getMinZ();

		double absdx1 = Math.abs(dx1);
		double absdx2 = Math.abs(dx2);
		double absdy1 = Math.abs(dy1);
		double absdy2 = Math.abs(dy2);
//		double absdz1 = Math.abs(dy1);
//		double absdz2 = Math.abs(dy2);

		double dx = 0;
		double dy = 0;
		double dz = 0;

		// TODO compute dz
		if (dx1>=0 && absdx1<=absdx2 && absdx1<=absdy1 && absdx1<=absdy2) {
			// Move according to dx1
			dx = dx1; 
		}
		else if (dx2>=0 && absdx2<=absdx1 && absdx2<=absdy1 && absdx2<=absdy2) {
			// Move according to dx2
			dx = - dx2;
		}
		else if (dy1>=0 && absdy1<=absdx1 && absdy1<=absdx2 && absdy1<=absdy2) {
			// Move according to dy1
			dy = dy1; 
		}
		else {
			// Move according to dy2
			dy = - dy2;
		}
		set(
				getMinX()+dx,
				getMinY()+dy,
				getMinZ()+dz,
				getWidth(),
				getHeight(),
				getDepth());

		result.set(dx, dy, dz);
	}

	/** Move this rectangle to avoid collision 
	 * with the reference rectangle.
	 * 
	 * @param reference is the rectangle to avoid collision with.
	 * @param displacementDirection is the direction of the allowed displacement (it is an input).
	 *     This vector is set according to the result before returning.
	 * @param result the displacement vector.
	 */
	default void avoidCollisionWith(RectangularPrism3ad<?, ?, ?, ?, ?, ?> reference, Vector3D<?, ?> displacementDirection, Vector3D<?, ?> result) {
		assert (reference != null) : "Reference rectangle must be not null"; //$NON-NLS-1$
		assert (result != null) : "Result vector must be not null"; //$NON-NLS-1$
		if (displacementDirection == null || displacementDirection.getLengthSquared() == 0) {
			avoidCollisionWith(reference, result);
			return;
		}

		double dx1 = reference.getMaxX() - getMinX();
		double dx2 = reference.getMinX() - getMaxX();
		double dy1 = reference.getMaxY() - getMinY();
		double dy2 = reference.getMinY() - getMaxY();
		double dz1 = reference.getMaxZ() - getMinZ();
		double dz2 = reference.getMinZ() - getMaxZ();

		double absdx1 = Math.abs(dx1);
		double absdx2 = Math.abs(dx2);
		double absdy1 = Math.abs(dy1);
		double absdy2 = Math.abs(dy2);
		double absdz1 = Math.abs(dz1);
		double absdz2 = Math.abs(dz2);

		double dx, dy, dz;

		if (displacementDirection.getX()<0) {
			dx = -Math.min(absdx1, absdx2);
		}
		else {
			dx = Math.min(absdx1, absdx2);
		}

		if (displacementDirection.getY()<0) {
			dy = -Math.min(absdy1, absdy2);
		}
		else {
			dy = Math.min(absdy1, absdy2);
		}
		
		if (displacementDirection.getZ()<0) {
			dz = -Math.min(absdz1, absdz2);
		}
		else {
			dz = Math.min(absdz1, absdz2);
		}

		set(
				getMinX()+dx,
				getMinY()+dy,
				getMinZ()+dz,
				getWidth(),
				getHeight(),
				getDepth());

		displacementDirection.set(dx, dy, dz);
		result.set(dx, dy, dz);
	}

	@Pure
	@Override
	default P getClosestPointTo(Point3D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		double x;
		int same = 0;
		if (p.getX()<getMinX()) {
			x = getMinX();
		}
		else if (p.getX()>getMaxX()) {
			x = getMaxX();
		}
		else {
			x = p.getX();
			++same;
		}
		double y;
		if (p.getY()<getMinY()) {
			y = getMinY();
		}
		else if (p.getY()>getMaxY()) {
			y = getMaxY();
		}
		else {
			y = p.getY();
			++same;
		}
		double z;
		if (p.getZ()<getMinZ()) {
			z = getMinZ();
		}
		else if (p.getZ()>getMaxZ()) {
			z = getMaxZ();
		}
		else {
			z = p.getZ();
			++same;
		}
		if (same==3) {
			return getGeomFactory().convertToPoint(p);
		}
		return getGeomFactory().newPoint(x, y, z);
	}

	@Pure
	@Override
	default P getFarthestPointTo(Point3D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		double x;
		if (p.getX()<=getCenterX()) {
			x = getMaxX();
		}
		else {
			x = getMinX();
		}
		double y;
		if (p.getY()<=getCenterY()) {
			y = getMaxY();
		}
		else {
			y = getMinY();
		}
		double z;
		if (p.getZ()<=getCenterZ()) {
			z = getMaxZ();
		}
		else {
			z = getMinZ();
		}
		return getGeomFactory().newPoint(x, y, z);
	}

	@Pure
	@Override
	default PathIterator3ad<IE> getPathIterator(Transform3D transform) {
		if (transform == null || transform.isIdentity()) {
			return new RectanglePathIterator<>(this);
		}
		return new TransformedRectanglePathIterator<>(this, transform);
	}


	/** Iterator on the path elements of the rectangle.
	 * 
	 * @param <T> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	class RectanglePathIterator<T extends PathElement3ad>
			implements PathIterator3ad<T> {

		private final RectangularPrism3ad<?, ?, T, ?, ?, ?> rectangle;

		private double x1;
		
		private double y1;

		private double z1;
		
		private double x2;
		
		private double y2;

		private double z2;
		
		private int index = 0;
		
		/**
		 * @param rectangle the iterated rectangle.
		 */
		public RectanglePathIterator(RectangularPrism3ad<?, ?, T, ?, ?, ?> rectangle) {
			assert (rectangle != null) : "Rectangle must be not null"; //$NON-NLS-1$
			this.rectangle = rectangle;
			if (rectangle.isEmpty()) {
				this.index = 5;
			} else {
				this.x1 = rectangle.getMinX();
				this.x2 = rectangle.getMaxX();
				this.y1 = rectangle.getMinY();
				this.y2 = rectangle.getMaxY();
				this.z1 = rectangle.getMinZ();
				this.z2 = rectangle.getMaxZ();
			}
		}
		
		@Override
		public PathIterator3ad<T> restartIterations() {
			return new RectanglePathIterator<>(this.rectangle);
		}
		
		@Pure
		@Override
		public boolean hasNext() {
			return this.index <= 4;
		}

		@Override
		public T next() {
			int idx = this.index;
			++ this.index;
			switch(idx) {
			case 0:
				return this.rectangle.getGeomFactory().newMovePathElement(
						this.x1, this.y1, this.z1);
			case 1:
				return this.rectangle.getGeomFactory().newLinePathElement(
						this.x1, this.y1, this.z1,
						this.x1, this.y1, this.z2);
			case 2:
				return this.rectangle.getGeomFactory().newLinePathElement(
						this.x1, this.y1, this.z2,
						this.x1, this.y2, this.z2);
			case 3:
				return this.rectangle.getGeomFactory().newLinePathElement(
						this.x1, this.y2, this.z2,
						this.x1, this.y2, this.z1);
			case 4:
				return this.rectangle.getGeomFactory().newLinePathElement(
						this.x1, this.y2, this.z1,
						this.x2, this.y2, this.z1);
			case 5:
				return this.rectangle.getGeomFactory().newLinePathElement(
						this.x2, this.y2, this.z1,
						this.x2, this.y2, this.z2);
			case 6:
				return this.rectangle.getGeomFactory().newLinePathElement(
						this.x2, this.y2, this.z2,
						this.x2, this.y1, this.z2);
			case 7:
				return this.rectangle.getGeomFactory().newClosePathElement(
						this.x2, this.y1, this.z2,
						this.x2, this.y1, this.z1);
			default:
				throw new NoSuchElementException();
			}
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
			return false;
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

		@Override
		public GeomFactory3ad<T, ?, ?, ?> getGeomFactory() {
			return this.rectangle.getGeomFactory();
		}

	}

	/** Iterator on the path elements of the rectangle.
	 * 
	 * @param <T> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	class TransformedRectanglePathIterator<T extends PathElement3ad> implements PathIterator3ad<T> {

		private final Transform3D transform;

		private final RectangularPrism3ad<?, ?, T, ?, ?, ?> rectangle;

		private Point3D<?, ?> p1;

		private Point3D<?, ?> p2;

		private double x1;
		
		private double y1;

		private double z1;
		
		private double x2;
		
		private double y2;

		private double z2;

		private int index;

		/**
		 * @param rectangle the iterated rectangle.
		 * @param transform the transformation.
		 */
		public TransformedRectanglePathIterator(RectangularPrism3ad<?, ?, T, ?, ?, ?> rectangle, Transform3D transform) {
			assert (rectangle != null) : "Rectangle must be not null"; //$NON-NLS-1$
			assert (transform != null) : "Transformation must be not null"; //$NON-NLS-1$
			this.rectangle = rectangle;
			this.transform = transform;
			if (rectangle.isEmpty()) {
				this.index = 5;
			} else {
				this.index = 0;
				this.p1 = new InnerComputationPoint3ad();
				this.p2 = new InnerComputationPoint3ad();
				this.x1 = rectangle.getMinX();
				this.x2 = rectangle.getMaxX();
				this.y1 = rectangle.getMinY();
				this.y2 = rectangle.getMaxY();
				this.z1 = rectangle.getMinZ();
				this.z2 = rectangle.getMaxZ();
			}
		}
		
		@Override
		public PathIterator3ad<T> restartIterations() {
			return new TransformedRectanglePathIterator<>(this.rectangle, this.transform);
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index <= 4;
		}

		@Override
		public T next() {
			int idx = this.index;
			++this.index;
			switch(idx) {
			case 0:
				this.p2.set(this.x1, this.y1, this.z1);
				this.transform.transform(this.p2);
				return this.rectangle.getGeomFactory().newMovePathElement(
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
			case 1:
				this.p1.set(this.p2);
				this.p2.set(this.x1, this.y1, this.z2);
				this.transform.transform(this.p2);
				return this.rectangle.getGeomFactory().newLinePathElement(
						this.p1.getX(), this.p1.getY(), this.p1.getZ(),
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
			case 2:
				this.p1.set(this.p2);
				this.p2.set(this.x1, this.y2, this.z2);
				this.transform.transform(this.p2);
				return this.rectangle.getGeomFactory().newLinePathElement(
						this.p1.getX(), this.p1.getY(), this.p1.getZ(),
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
			case 3:
				this.p1.set(this.p2);
				this.p2.set(this.x1, this.y2, this.z1);
				this.transform.transform(this.p2);
				return this.rectangle.getGeomFactory().newLinePathElement(
						this.p1.getX(), this.p1.getY(), this.p1.getZ(),
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
			case 4:
				this.p1.set(this.p2);
				this.p2.set(this.x2, this.y2, this.z1);
				this.transform.transform(this.p2);
				return this.rectangle.getGeomFactory().newLinePathElement(
						this.p1.getX(), this.p1.getY(), this.p1.getZ(),
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
			case 5:
				this.p1.set(this.p2);
				this.p2.set(this.x2, this.y2, this.z2);
				this.transform.transform(this.p2);
				return this.rectangle.getGeomFactory().newLinePathElement(
						this.p1.getX(), this.p1.getY(), this.p1.getZ(),
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
			case 6:
				this.p1.set(this.p2);
				this.p2.set(this.x2, this.y1, this.z2);
				this.transform.transform(this.p2);
				return this.rectangle.getGeomFactory().newLinePathElement(
						this.p1.getX(), this.p1.getY(), this.p1.getZ(),
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
			case 7:
				this.p1.set(this.p2);
				this.p2.set(this.x2, this.y1, this.z1);
				this.transform.transform(this.p2);
				return this.rectangle.getGeomFactory().newClosePathElement(
						this.p1.getX(), this.p1.getY(), this.p1.getZ(),
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
			default:
				throw new NoSuchElementException();
			}
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
			return false;
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

		@Override
		public GeomFactory3ad<T, ?, ?, ?> getGeomFactory() {
			return this.rectangle.getGeomFactory();
		}

	}

}
