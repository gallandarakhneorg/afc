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
package org.arakhne.afc.math.geometry2d.discrete;

import org.arakhne.afc.math.geometry2d.Point2D;



/** 2D rectangular shape with integer points.
 * 
 * @param <T> is the type of the shape implemented by the instance of this class.
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
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