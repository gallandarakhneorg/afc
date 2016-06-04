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
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.ai.Segment2ai.BresenhamLineIterator;

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
public interface Rectangle2ai<
		ST extends Shape2ai<?, ?, IE, P, V, B>,
		IT extends Rectangle2ai<?, ?, IE, P, V, B>,
		IE extends PathElement2ai,
		P extends Point2D<? super P, ? super V>,
		V extends Vector2D<? super V, ? super P>,
		B extends Rectangle2ai<?, ?, IE, P, V, B>>
		extends RectangularShape2ai<ST, IT, IE, P, V, B> {

	/** Replies if two rectangles are intersecting.
	 *
	 * @param x1 is the first corner of the first rectangle.
	 * @param y1 is the first corner of the first rectangle.
	 * @param x2 is the second corner of the first rectangle.
	 * @param y2 is the second corner of the first rectangle.
	 * @param x3 is the first corner of the second rectangle.
	 * @param y3 is the first corner of the second rectangle.
	 * @param x4 is the second corner of the second rectangle.
	 * @param y4 is the second corner of the second rectangle.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	@Pure
	static boolean intersectsRectangleRectangle(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
		assert x1 <= x2 : "x1 must be lower or equal to x2"; 
		assert y1 <= y2 : "y1 must be lower or equal to y2"; 
		assert x3 <= x4 : "x3 must be lower or equal to x4"; 
		assert y3 <= y4 : "y3 must be lower or equal to y4"; 
		return x2 > x3 && x1 < x4 && y2 > y3 && y1 < y4;
	}

	/** Replies if a rectangle is intersecting a segment.
	 *
	 * <p>The intersection test is partly based on the Cohen-Sutherland
	 * classification of the segment.
	 * This classification permits to detect the base cases;
	 * and to run a clipping-like algorithm for the intersection
	 * detection.
	 *
	 * @param x1 is the first corner of the rectangle.
	 * @param y1 is the first corner of the rectangle.
	 * @param x2 is the second corner of the rectangle.
	 * @param y2 is the second corner of the rectangle.
	 * @param x3 is the first point of the segment.
	 * @param y3 is the first point of the segment.
	 * @param x4 is the second point of the segment.
	 * @param y4 is the second point of the segment.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	@Pure
	@SuppressWarnings("checkstyle:cyclomaticcomplexity")
	static boolean intersectsRectangleSegment(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
		assert x1 <= x2 : "x1 must be lower or equal to x2"; 
		assert y1 <= y2 : "y1 must be lower or equal to y2"; 

		int c1 = MathUtil.getCohenSutherlandCode(x3, y3, x1, y1, x2, y2);
		final int c2 = MathUtil.getCohenSutherlandCode(x4, y4, x1, y1, x2, y2);

		if (c1 == MathConstants.COHEN_SUTHERLAND_INSIDE || c2 == MathConstants.COHEN_SUTHERLAND_INSIDE) {
			return true;
		}
		if ((c1 & c2) != 0) {
			return false;
		}

		int sx1 = x3;
		int sy1 = y3;
		final int sx2 = x4;
		final int sy2 = y4;

		// Only for internal use
		final Point2D<?, ?> pts = new InnerComputationPoint2ai();
		final BresenhamLineIterator<InnerComputationPoint2ai, InnerComputationVector2ai> iterator =
				new BresenhamLineIterator<>(
				InnerComputationGeomFactory.SINGLETON, sx1, sy1, sx2, sy2);

		while (iterator.hasNext() && c1 != MathConstants.COHEN_SUTHERLAND_INSIDE
				&& c2 != MathConstants.COHEN_SUTHERLAND_INSIDE && (c1 & c2) == 0) {
			if ((c1 & MathConstants.COHEN_SUTHERLAND_TOP) != 0) {
				do {
					iterator.next(pts);
					sy1 = pts.iy();
				}
				while (iterator.hasNext() && sy1 != y2);
				if (sy1 != y2) {
					return false;
				}
				sx1 = pts.ix();
			} else if ((c1 & MathConstants.COHEN_SUTHERLAND_BOTTOM) != 0) {
				do {
					iterator.next(pts);
					sy1 = pts.iy();
				}
				while (iterator.hasNext() && sy1 != y1);
				if (sy1 != y1) {
					return false;
				}
				sx1 = pts.ix();
			} else if ((c1 & MathConstants.COHEN_SUTHERLAND_RIGHT) != 0) {
				do {
					iterator.next(pts);
					sx1 = pts.ix();
				}
				while (iterator.hasNext() && sx1 != x2);
				if (sx1 != x2) {
					return false;
				}
				sy1 = pts.iy();
			} else {
				do {
					iterator.next(pts);
					sx1 = pts.ix();
				}
				while (iterator.hasNext() && sx1 != x1);
				if (sx1 != x1) {
					return false;
				}
				sy1 = pts.iy();
			}
			c1 = MathUtil.getCohenSutherlandCode(sx1, sy1, x1, y1, x2, y2);
		}

		return c1 == MathConstants.COHEN_SUTHERLAND_INSIDE || c2 == MathConstants.COHEN_SUTHERLAND_INSIDE;
	}

	/** Compute the closest point on the rectangle from the given point.
	 *
	 * @param minx is the x-coordinate of the lowest coordinate of the rectangle.
	 * @param miny is the y-coordinate of the lowest coordinate of the rectangle.
	 * @param maxx is the x-coordinate of the highest coordinate of the rectangle.
	 * @param maxy is the y-coordinate of the highest coordinate of the rectangle.
	 * @param px is the x-coordinate of the point.
	 * @param py is the y-coordinate of the point.
	 * @param result the closest point.
	 */
	@Pure
	static void computeClosestPoint(int minx, int miny, int maxx, int maxy, int px, int py, Point2D<?, ?> result) {
		assert minx <= maxx : "minx must be lower or equal to maxx"; 
		assert miny <= maxy : "maxx must be lower or equal to maxy"; 
		assert result != null : "Point must not be null"; 

		final int x;
		int same = 0;
		if (px < minx) {
			x = minx;
		} else if (px > maxx) {
			x = maxx;
		} else {
			x = px;
			++same;
		}
		final int y;
		if (py < miny) {
			y = miny;
		} else if (py > maxy) {
			y = maxy;
		} else {
			y = py;
			++same;
		}
		if (same == 2) {
			result.set(px, py);
		} else {
			result.set(x, y);
		}
	}

	/** Compute the farthest point on the rectangle from the given point.
	 *
	 * @param minx is the x-coordinate of the lowest coordinate of the rectangle.
	 * @param miny is the y-coordinate of the lowest coordinate of the rectangle.
	 * @param maxx is the x-coordinate of the highest coordinate of the rectangle.
	 * @param maxy is the y-coordinate of the highest coordinate of the rectangle.
	 * @param px is the x-coordinate of the point.
	 * @param py is the y-coordinate of the point.
	 * @param result the farthest point.
	 */
	@Pure
	static void computeFarthestPoint(int minx, int miny, int maxx, int maxy, int px, int py, Point2D<?, ?> result) {
		assert minx <= maxx : "minx must be lower or equal to maxx"; 
		assert miny <= maxy : "maxx must be lower or equal to maxy"; 
		assert result != null : "Point must not be null"; 

		final int x;
		if (px <= ((minx + maxx) / 2)) {
			x = maxx;
		} else {
			x = minx;
		}
		final int y;
		if (py <= ((miny + maxy) / 2)) {
			y = maxy;
		} else {
			y = miny;
		}
		result.set(x, y);
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
	default boolean intersects(Rectangle2ai<?, ?, ?, ?, ?, ?> rectangle) {
		assert rectangle != null : "Rectangle must not be null"; 
		return intersectsRectangleRectangle(
				getMinX(), getMinY(),
				getMaxX(), getMaxY(),
				rectangle.getMinX(), rectangle.getMinY(),
				rectangle.getMaxX(), rectangle.getMaxY());
	}

	@Pure
	@Override
	default boolean intersects(Circle2ai<?, ?, ?, ?, ?, ?> circle) {
		assert circle != null : "Circle must not be null"; 
		return Circle2ai.intersectsCircleRectangle(
				circle.getX(), circle.getY(),
				circle.getRadius(),
				getMinX(), getMinY(),
				getMaxX(), getMaxY());
	}

	@Pure
	@Override
	default boolean intersects(Segment2ai<?, ?, ?, ?, ?, ?> segment) {
		assert segment != null : "Segment must not be null"; 
		return intersectsRectangleSegment(
				getMinX(), getMinY(),
				getMaxX(), getMaxY(),
				segment.getX1(), segment.getY1(), segment.getX2(), segment.getY2());
	}

	@Pure
	@Override
	default boolean intersects(PathIterator2ai<?> iterator) {
		assert iterator != null : "Iterator must not be null"; 
		final int mask = iterator.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
		final int crossings = Path2ai.computeCrossingsFromRect(
				0,
				iterator,
				getMinX(), getMinY(), getMaxX(), getMaxY(),
				CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
		return crossings == MathConstants.SHAPE_INTERSECTS
				|| (crossings & mask) != 0;

	}

	@Pure
	@Override
	default boolean intersects(MultiShape2ai<?, ?, ?, ?, ?, ?, ?> multishape) {
		assert multishape != null : "MultiShape must be not null"; 
		return multishape.intersects(this);
	}

	@Pure
	@Override
	default boolean contains(int x, int y) {
		return x >= getMinX() && x <= this.getMaxX() && y >= getMinY() && y <= getMaxY();
	}

	@Pure
	@Override
	default boolean contains(Rectangle2ai<?, ?, ?, ?, ?, ?> box) {
		assert box != null : "Rectangle must not be null"; 
		return box.getMinX() >= getMinX() && box.getMaxX() <= getMaxX()
				&& box.getMinY() >= getMinY() && box.getMaxY() <= getMaxY();
	}

	@Override
	default void set(IT shape) {
		assert shape != null : "Rectangle must not be null"; 
		setFromCorners(shape.getMinX(), shape.getMinY(), shape.getMaxX(), shape.getMaxY());
	}

	@Pure
	@Override
	default P getClosestPointTo(Point2D<?, ?> pt) {
		assert pt != null : "Point must not be null"; 
		final P point = getGeomFactory().newPoint();
		computeClosestPoint(getMinX(), getMinY(), getMaxX(), getMaxY(), pt.ix(), pt.iy(), point);
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
		assert pt != null : "Point must not be null"; 
		final P point = getGeomFactory().newPoint();
		computeFarthestPoint(getMinX(), getMinY(), getMaxX(), getMaxY(), pt.ix(), pt.iy(), point);
		return point;
	}

	@Pure
	@Override
	default double getDistanceSquared(Point2D<?, ?> pt) {
		assert pt != null : "Point must not be null"; 
		final int dx;
		if (pt.ix() < getMinX()) {
			dx = getMinX() - pt.ix();
		} else if (pt.ix() > getMaxX()) {
			dx = pt.ix() - getMaxX();
		} else {
			dx = 0;
		}
		final int dy;
		if (pt.iy() < getMinY()) {
			dy = getMinY() - pt.iy();
		} else if (pt.iy() > getMaxY()) {
			dy = pt.iy() - getMaxY();
		} else {
			dy = 0;
		}
		return dx * dx + dy * dy;
	}

	@Pure
	@Override
	default double getDistanceL1(Point2D<?, ?> pt) {
		assert pt != null : "Point must not be null"; 
		final int dx;
		if (pt.ix() < getMinX()) {
			dx = getMinX() - pt.ix();
		} else if (pt.ix() > getMaxX()) {
			dx = pt.ix() - getMaxX();
		} else {
			dx = 0;
		}
		final int dy;
		if (pt.iy() < getMinY()) {
			dy = getMinY() - pt.iy();
		} else if (pt.iy() > getMaxY()) {
			dy = pt.iy() - getMaxY();
		} else {
			dy = 0;
		}
		return dx + dy;
	}

	@Pure
	@Override
	default double getDistanceLinf(Point2D<?, ?> pt) {
		assert pt != null : "Point must not be null"; 
		final int dx;
		if (pt.ix() < getMinX()) {
			dx = getMinX() - pt.ix();
		} else if (pt.ix() > getMaxX()) {
			dx = pt.ix() - getMaxX();
		} else {
			dx = 0;
		}
		final int dy;
		if (pt.iy() < getMinY()) {
			dy = getMinY() - pt.iy();
		} else if (pt.iy() > getMaxY()) {
			dy = pt.iy() - getMaxY();
		} else {
			dy = 0;
		}
		return Math.max(dx, dy);
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
		assert startingBorder != null : "Side border must not be null"; 
		return new RectangleSideIterator<>(this, startingBorder);
	}

	@Override
	default PathIterator2ai<IE> getPathIterator(Transform2D transform) {
		if (transform == null || transform.isIdentity()) {
			return new RectanglePathIterator<>(this);
		}
		return new TransformedRectanglePathIterator<>(this, transform);
	}

	/** Compute and replies the union of this rectangle and the given rectangle.
	 * This function does not change this rectangle.
	 *
	 * <p>It is equivalent to (where <code>ur</code> is the union):
	 * <pre><code>
	 * Rectangle2f ur = new Rectangle2f(this);
	 * ur.setUnion(r);
	 * </code></pre>
	 *
	 * @param rect the rectangular shape.
	 * @return the union of this rectangle and the given rectangle.
	 * @see #setUnion(RectangularShape2ai)
	 */
	@Pure
	default B createUnion(RectangularShape2ai<?, ?, ?, ?, ?, ?> rect) {
		assert rect != null : "Shape must be not null"; 
		final B rr = getGeomFactory().newBox();
		rr.setFromCorners(getMinX(), getMinY(), getMaxX(), getMaxY());
		rr.setUnion(rect);
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
	 * @param rect the rectangular shape.
	 * @return the union of this rectangle and the given rectangle.
	 * @see #setIntersection(RectangularShape2ai)
	 */
	@Pure
	default B createIntersection(RectangularShape2ai<?, ?, ?, ?, ?, ?> rect) {
		assert rect != null : "Shape must be not null"; 
		final B rr = getGeomFactory().newBox();
		final int x1 = Math.max(getMinX(), rect.getMinX());
		final int y1 = Math.max(getMinY(), rect.getMinY());
		final int x2 = Math.min(getMaxX(), rect.getMaxX());
		final int y2 = Math.min(getMaxY(), rect.getMaxY());
		if (x1 <= x2 && y1 <= y2) {
			rr.setFromCorners(x1, y1, x2, y2);
		} else {
			rr.clear();
		}
		return rr;
	}

	/** Compute the union of this rectangle and the given rectangle and
	 * change this rectangle with the result of the union.
	 *
	 * @param rect the rectangular shape.
	 * @see #createUnion(RectangularShape2ai)
	 */
	default void setUnion(RectangularShape2ai<?, ?, ?, ?, ?, ?> rect) {
		assert rect != null : "Shape must be not null"; 
		setFromCorners(
				Math.min(getMinX(), rect.getMinX()),
				Math.min(getMinY(), rect.getMinY()),
				Math.max(getMaxX(), rect.getMaxX()),
				Math.max(getMaxY(), rect.getMaxY()));
	}

	/** Compute the intersection of this rectangle and the given rectangle.
	 * This function changes this rectangle.
	 *
	 * <p>If there is no intersection, this rectangle is cleared.
	 *
	 * @param rect the rectangular shape.
	 * @see #createIntersection(RectangularShape2ai)
	 * @see #clear()
	 */
	default void setIntersection(RectangularShape2ai<?, ?, ?, ?, ?, ?> rect) {
		assert rect != null : "Shape must be not null"; 
		final int x1 = Math.max(getMinX(), rect.getMinX());
		final int y1 = Math.max(getMinY(), rect.getMinY());
		final int x2 = Math.min(getMaxX(), rect.getMaxX());
		final int y2 = Math.min(getMaxY(), rect.getMaxY());
		if (x1 <= x2 && y1 <= y2) {
			setFromCorners(x1, y1, x2, y2);
		} else {
			clear();
		}
	}

	/** Sides of a rectangle.
	 *
	 * @author $Author: sgalland$
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
		LEFT;
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
	class RectangleSideIterator<P extends Point2D<? super P, ? super V>,
			V extends Vector2D<? super V, ? super P>> implements Iterator<P> {

		private final GeomFactory2ai<?, P, V, ?> factory;

		private final int x0;

		private final int y0;

		private final int x1;

		private final int y1;

		private final Side firstSide;

		private Side currentSide;

		private int index;

		/**
		 * @param rectangle is the rectangle to iterate.
		 * @param firstSide the first side to iterate on.
		 */
		RectangleSideIterator(Rectangle2ai<?, ?, ?, P, V, ?> rectangle, Side firstSide) {
			assert rectangle != null : "Rectangle must not be null"; 
			assert firstSide != null : "First side must not be null"; 
			this.factory = rectangle.getGeomFactory();
			this.firstSide = firstSide;
			this.x0 = rectangle.getMinX();
			this.y0 = rectangle.getMinY();
			this.x1 = rectangle.getMaxX();
			this.y1 = rectangle.getMaxY();
			this.currentSide = (this.x1 > this.x0 && this.y1 > this.y0) ? this.firstSide : null;
			this.index = 0;
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.currentSide != null;
		}

		@Override
		@SuppressWarnings("checkstyle:npathcomplexity")
		public P next() {
			final int x;
			final int y;

			switch (this.currentSide) {
			case TOP:
				x = this.x0 + this.index;
				y = this.y0;
				break;
			case RIGHT:
				x = this.x1;
				y = this.y0 + this.index + 1;
				break;
			case BOTTOM:
				x = this.x1 - this.index - 1;
				y = this.y1;
				break;
			case LEFT:
				x = this.x0;
				y = this.y1 - this.index - 1;
				break;
			default:
				throw new NoSuchElementException();
			}

			++this.index;
			Side newSide = null;

			switch (this.currentSide) {
			case TOP:
				if (x >= this.x1) {
					newSide = Side.RIGHT;
					this.index = 0;
				}
				break;
			case RIGHT:
				if (y >= this.y1) {
					newSide = Side.BOTTOM;
					this.index = 0;
				}
				break;
			case BOTTOM:
				if (x <= this.x0) {
					newSide = Side.LEFT;
					this.index = 0;
				}
				break;
			case LEFT:
				if (y <= this.y0 + 1) {
					newSide = Side.TOP;
					this.index = 0;
				}
				break;
			default:
				throw new NoSuchElementException();
			}

			if (newSide != null) {
				this.currentSide = (this.firstSide == newSide) ? null : newSide;
			}

			return this.factory.newPoint(x, y);
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
	@SuppressWarnings("checkstyle:magicnumber")
	class RectanglePathIterator<E extends PathElement2ai> implements PathIterator2ai<E> {

		private final Rectangle2ai<?, ?, E, ?, ?, ?> rectangle;

		private int x1;

		private int y1;

		private int x2;

		private int y2;

		private int index;

		/**
		 * @param rectangle is the rectangle to iterate.
		 */
		RectanglePathIterator(Rectangle2ai<?, ?, E, ?, ?, ?> rectangle) {
			assert rectangle != null : "Rectangle must not be null"; 
			this.rectangle = rectangle;
			if (rectangle.isEmpty()) {
				this.index = 5;
			} else {
				this.x1 = rectangle.getMinX();
				this.y1 = rectangle.getMinY();
				this.x2 = rectangle.getMaxX();
				this.y2 = rectangle.getMaxY();
			}
		}

		@Override
		public PathIterator2ai<E> restartIterations() {
			return new RectanglePathIterator<>(this.rectangle);
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index <= 4;
		}

		@Override
		public E next() {
			final int idx = this.index;
			++this.index;
			switch (idx) {
			case 0:
				return this.rectangle.getGeomFactory().newMovePathElement(
						this.x1, this.y1);
			case 1:
				return this.rectangle.getGeomFactory().newLinePathElement(
						this.x1, this.y1,
						this.x2, this.y1);
			case 2:
				return this.rectangle.getGeomFactory().newLinePathElement(
						this.x2, this.y1,
						this.x2, this.y2);
			case 3:
				return this.rectangle.getGeomFactory().newLinePathElement(
						this.x2, this.y2,
						this.x1, this.y2);
			case 4:
				return this.rectangle.getGeomFactory().newClosePathElement(
						this.x1, this.y2,
						this.x1, this.y1);
			default:
				throw new NoSuchElementException();
			}
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
		public GeomFactory2ai<E, ?, ?, ?> getGeomFactory() {
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
	@SuppressWarnings("checkstyle:magicnumber")
	class TransformedRectanglePathIterator<E extends PathElement2ai> implements PathIterator2ai<E> {

		private final Rectangle2ai<?, ?, E, ?, ?, ?> rectangle;

		private final Transform2D transform;

		private int x1;

		private int y1;

		private int x2;

		private int y2;

		private int index;

		private Point2D<?, ?> move;

		private Point2D<?, ?> p1;

		private Point2D<?, ?> p2;

		/**
		 * @param rectangle is the rectangle to iterate.
		 * @param transform the transformation to apply on the rectangle.
		 */
		TransformedRectanglePathIterator(Rectangle2ai<?, ?, E, ?, ?, ?> rectangle, Transform2D transform) {
			assert rectangle != null : "Rectangle must not be null"; 
			assert transform != null : "Transformation must not be null"; 
			this.rectangle = rectangle;
			this.transform = transform;
			if (rectangle.isEmpty()) {
				this.index = 5;
			} else {
				this.move = new InnerComputationPoint2ai();
				this.p1 = new InnerComputationPoint2ai();
				this.p2 = new InnerComputationPoint2ai();
				this.x1 = rectangle.getMinX();
				this.y1 = rectangle.getMinY();
				this.x2 = rectangle.getMaxX();
				this.y2 = rectangle.getMaxY();
			}
		}

		@Override
		public PathIterator2ai<E> restartIterations() {
			return new TransformedRectanglePathIterator<>(this.rectangle, this.transform);
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index <= 4;
		}

		@Override
		public E next() {
			final int idx = this.index;
			++this.index;
			switch (idx) {
			case 0:
				this.p2.set(this.x1, this.y1);
				if (this.transform != null) {
					this.transform.transform(this.p2);
				}
				this.move.set(this.p2);
				return this.rectangle.getGeomFactory().newMovePathElement(
						this.p2.ix(), this.p2.iy());
			case 1:
				this.p1.set(this.p2);
				this.p2.set(this.x2, this.y1);
				if (this.transform != null) {
					this.transform.transform(this.p2);
				}
				return this.rectangle.getGeomFactory().newLinePathElement(
						this.p1.ix(), this.p1.iy(),
						this.p2.ix(), this.p2.iy());
			case 2:
				this.p1.set(this.p2);
				this.p2.set(this.x2, this.y2);
				if (this.transform != null) {
					this.transform.transform(this.p2);
				}
				return this.rectangle.getGeomFactory().newLinePathElement(
						this.p1.ix(), this.p1.iy(),
						this.p2.ix(), this.p2.iy());
			case 3:
				this.p1.set(this.p2);
				this.p2.set(this.x1, this.y2);
				if (this.transform != null) {
					this.transform.transform(this.p2);
				}
				return this.rectangle.getGeomFactory().newLinePathElement(
						this.p1.ix(), this.p1.iy(),
						this.p2.ix(), this.p2.iy());
			case 4:
				return this.rectangle.getGeomFactory().newClosePathElement(
						this.p2.ix(), this.p2.iy(),
						this.move.ix(), this.move.iy());
			default:
				throw new NoSuchElementException();
			}
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
		public GeomFactory2ai<E, ?, ?, ?> getGeomFactory() {
			return this.rectangle.getGeomFactory();
		}

	}

}
