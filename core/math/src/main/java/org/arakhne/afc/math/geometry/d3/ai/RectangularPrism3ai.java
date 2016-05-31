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

package org.arakhne.afc.math.geometry.d3.ai;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.ai.Path3ai.CrossingComputationType;
import org.arakhne.afc.math.geometry.d3.ai.Segment3ai.BresenhamLineIterator;
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
public interface RectangularPrism3ai<
		ST extends Shape3ai<?, ?, IE, P, V, B>,
		IT extends RectangularPrism3ai<?, ?, IE, P, V, B>,
		IE extends PathElement3ai,
		P extends Point3D<? super P, ? super V>,
		V extends Vector3D<? super V, ? super P>,
		B extends RectangularPrism3ai<?, ?, IE, P, V, B>>
		extends Prism3ai<ST, IT, IE, P, V, B> {

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
	public static boolean intersectsRectangleRectangle(int x1, int y1, int z1, int x2, int y2, int z2, int x3, int y3, int z3, int x4, int y4, int z4) {
		assert (x1 <= x2) : "x1 must be lower or equal to x2"; //$NON-NLS-1$
		assert (y1 <= y2) : "y1 must be lower or equal to y2"; //$NON-NLS-1$
		assert (z1 <= z2) : "z1 must be lower or equal to z2"; //$NON-NLS-1$
		assert (x3 <= x4) : "x3 must be lower or equal to x4"; //$NON-NLS-1$
		assert (y3 <= y4) : "y3 must be lower or equal to y4"; //$NON-NLS-1$
		assert (z3 <= z4) : "z3 must be lower or equal to z4"; //$NON-NLS-1$
		return x2 > x3 && x1 < x4 && y2 > y3 && y1 < y4 && z2  > z3 && z1 < z4;
	}

	/** Replies if a rectangle is intersecting a segment.
	 * <p>
	 * The intersection test is partly based on the Cohen-Sutherland
	 * classification of the segment.
	 * This classification permits to detect the base cases;
	 * and to run a clipping-like algorithm for the intersection
	 * detection.
	 * 
	 * @param x1 is the first corner of the rectangle.
	 * @param y1 is the first corner of the rectangle.
	 * @param z1 is the first corner of the rectangle.
	 * @param x2 is the second corner of the rectangle.
	 * @param y2 is the second corner of the rectangle.
	 * @param z2 is the second corner of the rectangle.
	 * @param x3 is the first point of the segment.
	 * @param y3 is the first point of the segment.
	 * @param z3 is the first point of the segment.
	 * @param x4 is the second point of the segment.
	 * @param y4 is the second point of the segment.
	 * @param z4 is the second point of the segment.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	@Pure
	public static boolean intersectsRectangleSegment(int x1, int y1, int z1, int x2, int y2, int z2, int x3, int y3, int z3, int x4, int y4, int z4) {
		assert (x1 <= x2) : "x1 must be lower or equal to x2"; //$NON-NLS-1$
		assert (y1 <= y2) : "y1 must be lower or equal to y2"; //$NON-NLS-1$
		assert (z1 <= z2) : "y1 must be lower or equal to y2"; //$NON-NLS-1$

		int c1 = MathUtil.getCohenSutherlandCode3D(x3, y3, z3, x1, y1, z1, x2, y2, z2);
		int c2 = MathUtil.getCohenSutherlandCode3D(x4, y4, z4, x1, y1, z1, x2, y2, z2);
		
//		0x8; //COHEN_SUTHERLAND_LEFT
//		0x4; //COHEN_SUTHERLAND_RIGHT
//		0x2; //COHEN_SUTHERLAND_BOTTOM
//		0x1; //COHEN_SUTHERLAND_TOP

		if (c1 == MathConstants.COHEN_SUTHERLAND_INSIDE || c2 == MathConstants.COHEN_SUTHERLAND_INSIDE) {
			return true;
		}
		if ((c1 & c2) != 0) {
			return false;
		}
		
		int sx1 = x3;
		int sy1 = y3;
		int sz1 = z3;
		int sx2 = x4;
		int sy2 = y4;
		int sz2 = z4;

		// Only for internal use
		Point3D<?, ?> pts = new InnerComputationPoint3ai();
		BresenhamLineIterator<InnerComputationPoint3ai, InnerComputationVector3ai> iterator =
				new BresenhamLineIterator<>(
						InnerComputationGeomFactory.SINGLETON, sx1, sy1, sz1, sx2, sy2, sz2);
		
		while (iterator.hasNext() && c1 != MathConstants.COHEN_SUTHERLAND_INSIDE
				&& c2 != MathConstants.COHEN_SUTHERLAND_INSIDE && (c1 & c2) == 0) {
			if ((c1 & MathConstants.COHEN_SUTHERLAND_TOP) != 0) {
				do {
					iterator.next(pts);
					sy1 = pts.iy();
				}
				while (iterator.hasNext() && sy1 != y2);
				if (sy1!=y2) return false;
				sx1 = pts.ix();
			}
			else if ((c1 & MathConstants.COHEN_SUTHERLAND_BOTTOM) != 0) {
				do {
					iterator.next(pts);
					sy1 = pts.iy();
				}
				while (iterator.hasNext() && sy1!=y1);
				if (sy1!=y1) return false;
				sx1 = pts.ix();
			}
			else if ((c1 & MathConstants.COHEN_SUTHERLAND_RIGHT) != 0) {
				do {
					iterator.next(pts);
					sx1 = pts.ix();
				}
				while (iterator.hasNext() && sx1!=x2);
				if (sx1!=x2) return false;
				sy1 = pts.iy();
			}
			else {
				do {
					iterator.next(pts);
					sx1 = pts.ix();
				}
				while (iterator.hasNext() && sx1!=x1);
				if (sx1!=x1) return false;
				sy1 = pts.iy();
			}
			c1 = MathUtil.getCohenSutherlandCode(sx1, sy1, x1, y1, x2, y2);
		}
		
		return c1==MathConstants.COHEN_SUTHERLAND_INSIDE || c2==MathConstants.COHEN_SUTHERLAND_INSIDE;
	}

	/** Compute the closest point on the rectangle from the given point.
	 * 
	 * @param minx is the x-coordinate of the lowest coordinate of the rectangle.
	 * @param miny is the y-coordinate of the lowest coordinate of the rectangle.
	 * @param minz is the z-coordinate of the lowest coordinate of the rectangle.
	 * @param maxx is the x-coordinate of the highest coordinate of the rectangle.
	 * @param maxy is the y-coordinate of the highest coordinate of the rectangle.
	 * @param maxz is the z-coordinate of the highest coordinate of the rectangle.
	 * @param px is the x-coordinate of the point.
	 * @param py is the y-coordinate of the point.
	 * @param pz is the z-coordinate of the point.
	 * @param result the closest point.
	 */
	@Pure
	public static void computeClosestPoint(int minx, int miny, int minz, int maxx, int maxy, int maxz, int px, int py, int pz, Point3D<?, ?> result) {
		assert (minx <= maxx) : "minx must be lower or equal to maxx"; //$NON-NLS-1$
		assert (miny <= maxy) : "miny must be lower or equal to maxy"; //$NON-NLS-1$
		assert (minz <= maxz) : "minz must be lower or equal to maxz"; //$NON-NLS-1$
		assert (result != null) : "Point must not be null"; //$NON-NLS-1$

		int x;
		int same = 0;
		if (px < minx) {
			x = minx;
		}
		else if (px > maxx) {
			x = maxx;
		}
		else {
			x = px;
			++same;
		}
		int y;
		if (py < miny) {
			y = miny;
		}
		else if (py > maxy) {
			y = maxy;
		}
		else {
			y = py;
			++same;
		}
		int z;
		if (pz < minz) {
			z = minz;
		}
		else if (pz > maxz) {
			z = maxz;
		}
		else {
			z = pz;
			++same;
		}
		if (same == 3) {
			result.set(px,py,pz);
		} else {
			result.set(x,y,z);
		}
	}

	/** Compute the farthest point on the rectangle from the given point.
	 * 
	 * @param minx is the x-coordinate of the lowest coordinate of the rectangle.
	 * @param miny is the y-coordinate of the lowest coordinate of the rectangle.
	 * @param minz is the z-coordinate of the lowest coordinate of the rectangle.
	 * @param maxx is the x-coordinate of the highest coordinate of the rectangle.
	 * @param maxy is the y-coordinate of the highest coordinate of the rectangle.
	 * @param maxz is the z-coordinate of the highest coordinate of the rectangle.
	 * @param px is the x-coordinate of the point.
	 * @param py is the y-coordinate of the point.
	 * @param pz is the z-coordinate of the point.
	 * @param result the farthest point.
	 */
	@Pure
	public static void computeFarthestPoint(int minx, int miny, int minz, int maxx, int maxy, int maxz, int px, int py, int pz, Point3D<?, ?> result) {
		assert (minx <= maxx) : "minx must be lower or equal to maxx"; //$NON-NLS-1$
		assert (miny <= maxy) : "miny must be lower or equal to maxy"; //$NON-NLS-1$
		assert (minz <= maxz) : "minz must be lower or equal to maxy"; //$NON-NLS-1$
		assert (result != null) : "Point must not be null"; //$NON-NLS-1$

		int x;
		if (px <= ((minx + maxx) / 2)) {
			x = maxx;
		}
		else {
			x = minx;
		}
		int y;
		if (py <= ((miny + maxy) / 2)) {
			y = maxy;
		}
		else {
			y = miny;
		}
		int z;
		if (pz <= ((minz + maxz) / 2)) {
			z = maxz;
		}
		else {
			z = minz;
		}
		result.set(x,y,z);
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

	@Pure
	@Override
	default boolean intersects(RectangularPrism3ai<?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "Rectangle must not be null"; //$NON-NLS-1$
		return intersectsRectangleRectangle(
				getMinX(), getMinY(), getMinZ(),
				getMaxX(), getMaxY(), getMaxZ(),
				s.getMinX(), s.getMinY(), s.getMinZ(),
				s.getMaxX(), s.getMaxY(), s.getMaxZ());
	}

	@Pure
	@Override
	default boolean intersects(Sphere3ai<?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "Circle must not be null"; //$NON-NLS-1$
		return Sphere3ai.intersectsCircleRectangle(
				s.getX(), s.getY(), s.getZ(),
				s.getRadius(),
				getMinX(), getMinY(), getMinZ(),
				getMaxX(), getMaxY(), getMaxZ());
	}

	@Pure
	@Override
	default boolean intersects(Segment3ai<?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "Segment must not be null"; //$NON-NLS-1$
		return intersectsRectangleSegment(
				getMinX(), getMinY(), getMinZ(),
				getMaxX(), getMaxY(), getMaxZ(),
				s.getX1(), s.getY1(), s.getZ1(), s.getX2(), s.getY2(), s.getZ2());
	}

	@Pure
	@Override
	default boolean intersects(PathIterator3ai<?> iterator) {
		assert (iterator != null) : "Iterator must not be null"; //$NON-NLS-1$
		int mask = (iterator.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
		int crossings = Path3ai.computeCrossingsFromRect(
				0, 
				iterator,
				getMinX(), getMinY(), getMinZ(), getMaxX(), getMaxY(), getMaxZ(),
				CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);

	}

	@Pure
	@Override
	default boolean intersects(MultiShape3ai<?, ?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "MultiShape must be not null"; //$NON-NLS-1$
		return s.intersects(this);
	}

	@Pure
	@Override
	default boolean contains(int x, int y, int z) {
		return x >= getMinX() && x <= this.getMaxX() && y >= getMinY() && y <= getMaxY() && z >= getMinZ() && z <= getMaxZ();
	}

	@Pure
	@Override
	default boolean contains(RectangularPrism3ai<?, ?, ?, ?, ?, ?> box) {
		assert (box != null) : "Rectangle must not be null"; //$NON-NLS-1$
		return box.getMinX() >= getMinX() && box.getMaxX() <= getMaxX()
				&& box.getMinY() >= getMinY() && box.getMaxY() <= getMaxY()		
				&& box.getMinZ() >= getMinZ() && box.getMaxZ() <= getMaxZ();		
	}

	@Override
	default void set(IT s) {
		assert (s != null) : "Rectangle must not be null"; //$NON-NLS-1$
		setFromCorners(s.getMinX(), s.getMinY(), s.getMinZ(), s.getMaxX(), s.getMaxY(), s.getMaxZ());
	}
	
	@Pure
	@Override
	default P getClosestPointTo(Point3D<?, ?> p) {
		assert (p != null) : "Point must not be null"; //$NON-NLS-1$
		P point = getGeomFactory().newPoint();
		computeClosestPoint(getMinX(), getMinY(), getMinZ(), getMaxX(), getMaxY(), getMaxZ(), p.ix(), p.iy(), p.iz(), point);
		return point;
	}
	
	@Pure
	@Override
	default P getFarthestPointTo(Point3D<?, ?> p) {
		assert (p != null) : "Point must not be null"; //$NON-NLS-1$
		P point = getGeomFactory().newPoint();
		computeFarthestPoint(getMinX(), getMinY(), getMinZ(), getMaxX(), getMaxY(), getMaxZ(), p.ix(), p.iy(), p.iz(), point);
		return point;
	}

	@Pure
	@Override
	default double getDistanceSquared(Point3D<?, ?> p) {
		assert (p != null) : "Point must not be null"; //$NON-NLS-1$
		int dx;
		if (p.ix()<getMinX()) {
			dx = getMinX() - p.ix();
		}
		else if (p.ix()>getMaxX()) {
			dx = p.ix() - getMaxX();
		}
		else {
			dx = 0;
		}
		int dy;
		if (p.iy()<getMinY()) {
			dy = getMinY() - p.iy();
		}
		else if (p.iy()>getMaxY()) {
			dy = p.iy() - getMaxY();
		}
		else {
			dy = 0;
		}
		int dz;
		if (p.iz()<getMinZ()) {
			dz = getMinZ() - p.iz();
		}
		else if (p.iz()>getMaxZ()) {
			dz = p.iz() - getMaxZ();
		}
		else {
			dz = 0;
		}
		return dx*dx+dy*dy+dz*dz;
	}

	@Pure
	@Override
	default double getDistanceL1(Point3D<?, ?> p) {
		assert (p != null) : "Point must not be null"; //$NON-NLS-1$
		int dx;
		if (p.ix()<getMinX()) {
			dx = getMinX() - p.ix();
		}
		else if (p.ix()>getMaxX()) {
			dx = p.ix() - getMaxX();
		}
		else {
			dx = 0;
		}
		int dy;
		if (p.iy()<getMinY()) {
			dy = getMinY() - p.iy();
		}
		else if (p.iy()>getMaxY()) {
			dy = p.iy() - getMaxY();
		}
		else {
			dy = 0;
		}
		int dz;
		if (p.iz()<getMinZ()) {
			dz = getMinZ() - p.iz();
		}
		else if (p.iy()>getMaxY()) {
			dz = p.iz() - getMaxZ();
		}
		else {
			dz = 0;
		}
		return dx + dy + dz;
	}
	
	@Pure
	@Override
	default double getDistanceLinf(Point3D<?, ?> p) {
		assert (p != null) : "Point must not be null"; //$NON-NLS-1$
		int dx;
		if (p.ix()<getMinX()) {
			dx = getMinX() - p.ix();
		}
		else if (p.ix()>getMaxX()) {
			dx = p.ix() - getMaxX();
		}
		else {
			dx = 0;
		}
		int dy;
		if (p.iy()<getMinY()) {
			dy = getMinY() - p.iy();
		}
		else if (p.iy()>getMaxY()) {
			dy = p.iy() - getMaxY();
		}
		else {
			dy = 0;
		}
		int dz;
		if (p.iz()<getMinZ()) {
			dz = getMinZ() - p.iz();
		}
		else if (p.iz()>getMaxZ()) {
			dz = p.iz() - getMaxZ();
		}
		else {
			dz = 0;
		}
		return MathUtil.max(dx, dy, dz);
	}

	@Pure
	@Override
	default Iterator<P> getPointIterator() {
		return getPointIterator(Side.TOP);
	}

	/** Replies the points on the bounds of the rectangle.
	 *
	 * @param startingBorder is the first border to reply.
	 * @return the points on the bounds of the rectangle.
	 */
	@Pure
	default Iterator<P> getPointIterator(Side startingBorder) {
		assert (startingBorder != null) : "Side border must not be null"; //$NON-NLS-1$
		return new RectangleSideIterator<>(this, startingBorder);
	}

	@Override
	default PathIterator3ai<IE> getPathIterator(Transform3D transform) {
		if (transform == null || transform.isIdentity()) {
			return new RectanglePathIterator<>(this);
		}
		return new TransformedRectanglePathIterator<>(this, transform);
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
	 * @see #setUnion(Prism3ai)
	 */
	@Pure
	default B createUnion(Prism3ai<?, ?, ?, ?, ?, ?> r) {
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
	 * @see #setIntersection(Prism3ai)
	 */
	@Pure
	default B createIntersection(Prism3ai<?, ?, ?, ?, ?, ?> r) {
		assert (r != null) : "Shape must be not null"; //$NON-NLS-1$
		B rr = getGeomFactory().newBox();
		int x1 = Math.max(getMinX(), r.getMinX());
		int y1 = Math.max(getMinY(), r.getMinY());
		int z1 = Math.max(getMinZ(), r.getMinZ());
		int x2 = Math.min(getMaxX(), r.getMaxX());
		int y2 = Math.min(getMaxY(), r.getMaxY());
		int z2 = Math.min(getMaxZ(), r.getMaxZ());
		if (x1<=x2 && y1<=y2 && z1 <= z2) {
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
	 * @see #createUnion(Prism3ai)
	 */
	default void setUnion(Prism3ai<?, ?, ?, ?, ?, ?> r) {
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
	 * @see #createIntersection(Prism3ai)
	 * @see #clear()
	 */
	default void setIntersection(Prism3ai<?, ?, ?, ?, ?, ?> r) {
		assert (r != null) : "Shape must be not null"; //$NON-NLS-1$
		int x1 = Math.max(getMinX(), r.getMinX());
		int y1 = Math.max(getMinY(), r.getMinY());
		int z1 = Math.max(getMinZ(), r.getMinZ());
		int x2 = Math.min(getMaxX(), r.getMaxX());
		int y2 = Math.min(getMaxY(), r.getMaxY());
		int z2 = Math.min(getMaxZ(), r.getMaxZ());
		if (x1<=x2 && y1<=y2 && z1<=z2) {
			setFromCorners(x1, y1, z1, x2, y2, z2);
		}
		else {
			clear();
		}
	}

	/** Sides of a prism.
	 * 
	 * @author $Author: sgalland$
	 * @author $Author: tpiotrow$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	enum Side {
		/** Top.
		 */
		TOP,
		/** Right.
		 */
		RIGHT,
		/** Bottom.
		 */
		BOTTOM,
		/** Left.
		 */
		LEFT,
		/** Front.
		 */
		FRONT,
		/** Back.
		 */
		BACK;
	}

	/** Iterates on points on the sides of a rectangle.
	 * 
	 * @param <P> type of the points.
	 * @param <V> type of the vectors.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	class RectangleSideIterator<P extends Point3D<? super P, ? super V>,
			V extends Vector3D<? super V, ? super P>> implements Iterator<P> {

		private final GeomFactory3ai<?, P, V, ?> factory;
		
		private final int x0;

		private final int y0;

		private final int z0;
		
		private final int x1;
		
		private final int y1;

		private final int z1;
		
		private final Side firstSide;
		
		private Side currentSide;
		
		private int i;
		
		/**
		 * @param rectangle is the rectangle to iterate.
		 * @param firstSide the first side to iterate on.
		 */
		public RectangleSideIterator(RectangularPrism3ai<?, ?, ?, P, V, ?> rectangle, Side firstSide) {
			assert (rectangle != null) : "Rectangle must not be null"; //$NON-NLS-1$
			assert (firstSide != null) : "First side must not be null"; //$NON-NLS-1$
			this.factory = rectangle.getGeomFactory();
			this.firstSide = firstSide;
			this.x0 = rectangle.getMinX();
			this.y0 = rectangle.getMinY();
			this.z0 = rectangle.getMinZ();
			this.x1 = rectangle.getMaxX();
			this.y1 = rectangle.getMaxY();
			this.z1 = rectangle.getMaxZ();
			this.currentSide = (this.x1 > this.x0 && this.y1 > this.y0 && this.z1 > this.z0) ? this.firstSide : null;
			this.i = 0;
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.currentSide!=null;
		}

		@Override
		// TODO : integrate z coordinate
		public P next() {
			int x, y, z = 0;
			
			switch(this.currentSide) {
			case TOP:
				x = this.x0+this.i;
				y = this.y0;
				break;
			case RIGHT:
				x = this.x1;
				y = this.y0+this.i+1;
				break;
			case BOTTOM:
				x = this.x1-this.i-1;
				y = this.y1;
				break;
			case LEFT:
				x = this.x0;
				y = this.y1-this.i-1;
				break;
			default:
				throw new NoSuchElementException();
			}
			
			++ this.i;
			Side newSide = null;
			
			switch(this.currentSide) {
			case TOP:
				if (x>=this.x1) {
					newSide = Side.RIGHT;
					this.i = 0;
				}
				break;
			case RIGHT:
				if (y>=this.y1) {
					newSide = Side.BOTTOM;
					this.i = 0;
				}
				break;
			case BOTTOM:
				if (x<=this.x0) {
					newSide = Side.LEFT;
					this.i = 0;
				}
				break;
			case LEFT:
				if (y<=this.y0+1) {
					newSide = Side.TOP;
					this.i = 0;
				}
				break;
			default:
				throw new NoSuchElementException();
			}

			if (newSide!=null) {
				this.currentSide = (this.firstSide==newSide) ? null : newSide;
			}
			
			return this.factory.newPoint(x, y, z);
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}

	/** Iterator on the path elements of the rectangle.
	 * 
	 * @param <E> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	class RectanglePathIterator<E extends PathElement3ai> implements PathIterator3ai<E> {
		
		private final RectangularPrism3ai<?, ?, E, ?, ?, ?> rectangle;

		private int x1;
		
		private int y1;

		private int z1;
		
		private int x2;
		
		private int y2;

		private int z2;
		
		private int index = 0;
		
		/**
		 * @param rectangle is the rectangle to iterate.
		 */
		public RectanglePathIterator(RectangularPrism3ai<?, ?, E, ?, ?, ?> rectangle) {
			assert (rectangle != null) : "Rectangle must not be null"; //$NON-NLS-1$
			this.rectangle = rectangle;
			if (rectangle.isEmpty()) {
				this.index = 5;
			} else {
				this.x1 = rectangle.getMinX();
				this.y1 = rectangle.getMinY();
				this.z1 = rectangle.getMinZ();
				this.x2 = rectangle.getMaxX();
				this.y2 = rectangle.getMaxY();
				this.z2 = rectangle.getMaxZ();
			}
		}
		
		@Override
		public PathIterator3ai<E> restartIterations() {
			return new RectanglePathIterator<>(this.rectangle);
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index <= 4;
		}

		@Override
		public E next() {
			int idx = this.index;
			++this.index;
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
			return true;
		}

		@Override
		public GeomFactory3ai<E, ?, ?, ?> getGeomFactory() {
			return this.rectangle.getGeomFactory();
		}
		
	}

	/** Iterator on the path elements of the rectangle.
	 * 
	 * @param <E> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	class TransformedRectanglePathIterator<E extends PathElement3ai> implements PathIterator3ai<E> {
		
		private final RectangularPrism3ai<?, ?, E, ?, ?, ?> rectangle;

		private final Transform3D transform;
		
		private int x1;
		
		private int y1;

		private int z1;
		
		private int x2;
		
		private int y2;

		private int z2;
		
		private int index = 0;
		
		private Point3D<?, ?> move;

		private Point3D<?, ?> p1;
		
		private Point3D<?, ?> p2;
		
		/**
		 * @param rectangle is the rectangle to iterate.
		 * @param transform the transformation to apply on the rectangle.
		 */
		public TransformedRectanglePathIterator(RectangularPrism3ai<?, ?, E, ?, ?, ?> rectangle, Transform3D transform) {
			assert (rectangle != null) : "Rectangle must not be null"; //$NON-NLS-1$
			assert (transform != null) : "Transformation must not be null"; //$NON-NLS-1$
			this.rectangle = rectangle;
			this.transform = transform;
			if (rectangle.isEmpty()) {
				this.index = 5;
			} else {
				this.move = new InnerComputationPoint3ai();
				this.p1 = new InnerComputationPoint3ai();
				this.p2 = new InnerComputationPoint3ai();
				this.x1 = rectangle.getMinX();
				this.y1 = rectangle.getMinY();
				this.z1 = rectangle.getMinZ();
				this.x2 = rectangle.getMaxX();
				this.y2 = rectangle.getMaxY();
				this.z2 = rectangle.getMaxZ();
			}
		}
		
		@Override
		public PathIterator3ai<E> restartIterations() {
			return new TransformedRectanglePathIterator<>(this.rectangle, this.transform);
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index <= 4;
		}

		@Override
		public E next() {
			int idx = this.index;
			++this.index;
			switch(idx) {
			case 0:
				this.p2.set(this.x1, this.y1, this.z1);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				this.move.set(this.p2);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return this.rectangle.getGeomFactory().newMovePathElement(
						this.p2.ix(), this.p2.iy(), this.p2.iz());
			case 1:
				this.p1.set(this.p2);
				this.p2.set(this.x1, this.y1, this.z2);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return this.rectangle.getGeomFactory().newLinePathElement(
						this.p1.ix(), this.p1.iy(), this.p1.iz(),
						this.p2.ix(), this.p2.iy(), this.p2.iz());
			case 2:
				this.p1.set(this.p2);
				this.p2.set(this.x1, this.y2, this.z2);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return this.rectangle.getGeomFactory().newLinePathElement(
						this.p1.ix(), this.p1.iy(), this.p1.iz(),
						this.p2.ix(), this.p2.iy(), this.p2.iz());
			case 3:
				this.p1.set(this.p2);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				this.transform.transform(this.p2);
				return this.rectangle.getGeomFactory().newLinePathElement(
						this.p1.ix(), this.p1.iy(), this.p1.iz(),
						this.p2.ix(), this.p2.iy(), this.p2.iz());
			case 4:
				this.p1.set(this.p2);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				this.transform.transform(this.p2);
				return this.rectangle.getGeomFactory().newLinePathElement(
						this.p1.ix(), this.p1.iy(), this.p1.iz(),
						this.p2.ix(), this.p2.iy(), this.p2.iz());
			case 5:
				this.p1.set(this.p2);
				this.p2.set(this.x2, this.y2, this.z2);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return this.rectangle.getGeomFactory().newLinePathElement(
						this.p1.ix(), this.p1.iy(), this.p1.iz(),
						this.p2.ix(), this.p2.iy(), this.p2.iz());
			case 6:
				this.p1.set(this.p2);
				this.p2.set(this.x2, this.y1, this.z2);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return this.rectangle.getGeomFactory().newLinePathElement(
						this.p1.ix(), this.p1.iy(), this.p1.iz(),
						this.p2.ix(), this.p2.iy(), this.p2.iz());
			case 7:
				return this.rectangle.getGeomFactory().newClosePathElement(
						this.p2.ix(), this.p2.iy(), this.p2.iz(),
						this.move.ix(), this.move.iy(), this.move.iz());
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
			return true;
		}

		@Override
		public GeomFactory3ai<E, ?, ?, ?> getGeomFactory() {
			return this.rectangle.getGeomFactory();
		}
		
	}

}