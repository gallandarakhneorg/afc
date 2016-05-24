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

package org.arakhne.afc.math.discrete.object2d;

import org.arakhne.afc.math.generic.Point2D;



/** 2D rectangular shape with integer points.
 *
 * @param <T> is the type of the shape implemented by the instance of this class.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see {@link org.arakhne.afc.math.geometry.d2.i.AbstractRectangularShape2i}
 */
@Deprecated
@SuppressWarnings("all")
public abstract class AbstractRectangularShape2i<T extends Shape2i> extends AbstractShape2i<T> {

	private static final long serialVersionUID = -2716430612894131964L;
	
	/** Lowest x-coordinate covered by the rectangular shape. */
	protected int minx = 0;
	/** Lowest y-coordinate covered by the rectangular shape. */
	protected int miny = 0;
	/** Highest x-coordinate covered by the rectangular shape. */
	protected int maxx = 0;
	/** Highest x-coordinate covered by the rectangular shape. */
	protected int maxy = 0;
	
	/**
	 */
	public AbstractRectangularShape2i() {
		//
	}
	
	/**
	 * @param s
	 */
	public AbstractRectangularShape2i(AbstractRectangularShape2i<?> s) {
		this.minx = s.minx;
		this.miny = s.miny;
		this.maxx = s.maxx;
		this.maxy = s.maxy;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Rectangle2i toBoundingBox() {
		return new Rectangle2i(this.minx, this.miny, this.maxx-this.minx, this.maxy-this.miny);
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public void toBoundingBox(Rectangle2i box) {
		box.setFromCorners(this.minx, this.miny, this.maxx, this.maxy);
	}
	
	@Override
	public void clear() {
		this.minx = this.miny = this.maxx = this.maxy = 0;
	}
	
	/** Replies if this rectangle is empty or not.
	 * A rectangle is empty when the two corners
	 * of the rectangle are at the same location.
	 * 
	 * @return <code>true</code> if the two corners are
	 * at the same location; <code>false</code> otherwise.
	 */
	@Override
	public boolean isEmpty() {
		return this.minx==this.maxx && this.miny==this.maxy; 
	}

	/** Set the coordinates of this shape from the bounds of the given shape.
	 * 
	 * @param shape
	 */
	public void set(AbstractRectangularShape2i<?> shape) {
		setFromCorners(shape.getMinX(), shape.getMinY(), shape.getMaxX(), shape.getMaxY());
	}
	
	/** Change the frame of the rectangle.
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public void set(int x, int y, int width, int height) {
		setFromCorners(x, y, x+width, y+height);
	}
	
	@Override
	public void set(Shape2i s) {
		Rectangle2i r = s.toBoundingBox();
		setFromCorners(r.getMinX(), r.getMinY(), r.getMaxX(), r.getMaxY());
	}
	
	/** Change the frame of te rectangle.
	 * 
	 * @param min is the min corner of the rectangle.
	 * @param max is the max corner of the rectangle.
	 */
	public void set(Point2D min, Point2D max) {
		setFromCorners(min.x(), min.y(), max.x(), max.y());
	}

	/** Change the frame of the rectangle.
	 * 
	 * @param x1 is the coordinate of the first corner.
	 * @param y1 is the coordinate of the first corner.
	 * @param x2 is the coordinate of the second corner.
	 * @param y2 is the coordinate of the second corner.
	 */
	public void setFromCorners(int x1, int y1, int x2, int y2) {
		if (x1<x2) {
			this.minx = x1;
			this.maxx = x2;
		}
		else {
			this.minx = x2;
			this.maxx = x1;
		}
		if (y1<y2) {
			this.miny = y1;
			this.maxy = y2;
		}
		else {
			this.miny = y2;
			this.maxy = y1;
		}
	}
	
	/** Change the frame of the rectangle.
	 * 
	 * @param c1 is the first corner.
	 * @param c2 is the first corner.
	 */
	public void setFromCorners(Point2D c2, Point2D c1) {
		setFromCorners(c1.x(),  c1.y(),  c2.x(), c2.y());
	}
	
	/**
     * Sets the framing rectangle of this <code>Shape</code>
     * based on the specified center point coordinates and corner point
     * coordinates.  The framing rectangle is used by the subclasses of
     * <code>RectangularShape</code> to define their geometry.
     *
     * @param centerX the X coordinate of the specified center point
     * @param centerY the Y coordinate of the specified center point
     * @param cornerX the X coordinate of the specified corner point
     * @param cornerY the Y coordinate of the specified corner point
     */
	public void setFromCenter(int centerX, int centerY, int cornerX, int cornerY) {
		int dx = centerX - cornerX;
		int dy = centerY - cornerY;
		setFromCorners(cornerX, cornerY, centerX + dx, centerY + dy);
	}
	
	/** Replies the min X.
	 * 
	 * @return the min x.
	 */
	public int getMinX() {
		return this.minx;
	}

	/** Replies the center x.
	 * 
	 * @return the center x.
	 */
	public int getCenterX() {
		return (this.minx + this.maxx) / 2;
	}

	/** Replies the max x.
	 * 
	 * @return the max x.
	 */
	public int getMaxX() {
		return this.maxx;
	}

	/** Replies the min y.
	 * 
	 * @return the min y.
	 */
	public int getMinY() {
		return this.miny;
	}

	/** Replies the center y.
	 * 
	 * @return the center y.
	 */
	public int getCenterY() {
		return (this.miny + this.maxy) / 2;
	}

	/** Replies the max y.
	 * 
	 * @return the max y.
	 */
	public int getMaxY() {
		return this.maxy;
	}
	
	/** Replies the width.
	 * 
	 * @return the width.
	 */
	public int getWidth() {
		return this.maxx - this.minx;
	}

	/** Replies the height.
	 * 
	 * @return the height.
	 */
	public int getHeight() {
		return this.maxy - this.miny;
	}
	
	/** Set the min X.
	 * 
	 * @param x the min x.
	 */
	public void setMinX(int x) {
		int o = this.maxx;
		if (o<x) {
			this.minx = o;
			this.maxx = x;
		}
		else {
			this.minx = x;
		}
	}
	
	/** Set the min Y.
	 * 
	 * @param y the min y.
	 */
	public void setMinY(int y) {
		int o = this.maxy;
		if (o<y) {
			this.miny = o;
			this.maxy = y;
		}
		else {
			this.miny = y;
		}
	}
	
	/** Set the max X.
	 * 
	 * @param x the max x.
	 */
	public void setMaxX(int x) {
		int o = this.minx;
		if (o>x) {
			this.maxx = o;
			this.minx = x;
		}
		else {
			this.maxx = x;
		}
	}

	/** Set the max Y.
	 * 
	 * @param y the max y.
	 */
	public void setMaxY(int y) {
		int o = this.miny;
		if (o>y) {
			this.maxy = o;
			this.miny = y;
		}
		else {
			this.maxy = y;
		}
	}
	
	/** Change the width of the rectangle, not the min corner.
	 * 
	 * @param width
	 */
	public void setWidth(int width) {
		this.maxx = this.minx + Math.max(0, width);
	}

	/** Change the height of the rectangle, not the min corner.
	 * 
	 * @param height
	 */
	public void setHeight(int height) {
		this.maxy = this.miny + Math.max(0, height);
	}
	
	@Override
	public void translate(int dx, int dy) {
		this.minx += dx;
		this.miny += dy;
		this.maxx += dx;
		this.maxy += dy;
	}
	
	/** Inflate this rectangle with the given amounts.
	 * 
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 */
	public void inflate(int left, int top, int right, int bottom) {
		this.minx -= left;
		this.miny -= top;
		this.maxx += right;
		this.maxy += bottom;
	}

}
