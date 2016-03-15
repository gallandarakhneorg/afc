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

package org.arakhne.afc.math.geometry.d2.ai;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.eclipse.xtext.xbase.lib.Pure;

/** Fonctional interface that represented a rectangular shape on a plane.
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
 * @since 13.0
 */
public interface RectangularShape2ai<
		ST extends Shape2ai<?, ?, IE, P, B>,
		IT extends RectangularShape2ai<?, ?, IE, P, B>,
		IE extends PathElement2ai,
		P extends Point2D,
		B extends Rectangle2ai<?, ?, IE, P, B>>
		extends Shape2ai<ST, IT, IE, P, B> {

	@Override
	default void toBoundingBox(B box) {
		box.setFromCorners(getMinX(), getMinY(), getMaxX(), getMaxY());
	}
	
	@Override
	default void clear() {
		setFromCorners(0, 0, 0, 0);
	}

	/** Change the frame of the rectangle.
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	default void set(int x, int y, int width, int height) {
		setFromCorners(x, y, x + width, y + height);
	}
	
	/** Change the frame of the rectangle.
	 * 
	 * @param min is the min corner of the rectangle.
	 * @param max is the max corner of the rectangle.
	 */
	default void set(Point2D min, Point2D max) {
		setFromCorners(min.ix(), min.iy(), max.ix(), max.iy());
	}
	
	/** Change the width of the rectangle, not the min corner.
	 * 
	 * @param width
	 */
	default void setWidth(int width) {
		setMaxX(getMinX() + width);
	}

	/** Change the height of the rectangle, not the min corner.
	 * 
	 * @param height
	 */
	default void setHeight(int height) {
		setMaxY(getMinY() + height);
	}
	
	/** Change the frame of the rectangle conserving previous min and max if needed.
	 * 
	 * @param x1 is the coordinate of the first corner.
	 * @param y1 is the coordinate of the first corner.
	 * @param x2 is the coordinate of the second corner.
	 * @param y2 is the coordinate of the second corner.
	 */
	// This function has no default implementation for allowing implementation to be atomic. 
	void setFromCorners(int x1, int y1, int x2, int y2);
	
	/** Change the frame of the rectangle conserving previous min and max if needed.
	 * 
	 * @param p1 the first corner.
	 * @param p2 the second corner.
	 */
	default void setFromCorners(Point2D p1, Point2D p2) {
		setFromCorners(p1.ix(), p1.iy(), p2.ix(), p2.iy());
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
	default void setFromCenter(int centerX, int centerY, int cornerX, int cornerY) {
		int demiWidth = Math.abs(centerX - cornerX);
		int demiHeight = Math.abs(centerY - cornerY);
		setFromCorners(centerX - demiWidth, centerY - demiHeight, centerX + demiWidth, centerY + demiHeight);
	}
	
	/**
     * Sets the framing rectangle of this <code>Shape</code>
     * based on the specified center point coordinates and corner point
     * coordinates.  The framing rectangle is used by the subclasses of
     * <code>RectangularShape</code> to define their geometry.
     *
     * @param center the specified center point
     * @param corner the specified corner point
     */
	default void setFromCenter(Point2D center, Point2D corner) {
		setFromCenter(center.ix(), center.iy(), corner.ix(), corner.iy());
	}

	/** Replies the min X.
	 * 
	 * @return the min x.
	 */
	@Pure
	int getMinX();

	/** Set the min X conserving previous min if needed.
	 * 
	 * @param x the min x.
	 */
	void setMinX(int x);

	/** Replies the center x.
	 * 
	 * @return the center x.
	 */
	@Pure
	default int getCenterX() {
		return (getMinX() + getMaxX()) / 2;
	}

	/** Replies the max x.
	 * 
	 * @return the max x.
	 */
	@Pure
	int getMaxX();

	/** Set the max X conserving previous max if needed.
	 * 
	 * @param x the max x.
	 */
	void setMaxX(int x);

	/** Replies the min y.
	 * 
	 * @return the min y.
	 */
	@Pure
	int getMinY();

	/** Set the min Y conserving previous min if needed.
	 * 
	 * @param y the min y.
	 */
	void setMinY(int y);

	/** Replies the center y.
	 * 
	 * @return the center y.
	 */
	@Pure
	default int getCenterY() {
		return (getMinY() + getMaxY()) / 2;
	}

	/** Replies the max y.
	 * 
	 * @return the max y.
	 */
	@Pure
	int getMaxY();

	/** Set the max Y conserving previous max if needed.
	 * 
	 * @param y the max y.
	 */
	void setMaxY(int y);

	/** Replies the width.
	 * 
	 * @return the width.
	 */
	@Pure
	default int getWidth() {
		return getMaxX() - getMinX();
	}

	/** Replies the height.
	 * 
	 * @return the height.
	 */
	@Pure
	default int getHeight() {
		return getMaxY() - getMinY();
	}
	
	@Override
	default void translate(int dx, int dy) {
		setFromCorners(getMinX() + dx, getMinY() + dy, getMaxX() + dx, getMaxY() + dy);
	}

	@Pure
	@Override
	default boolean isEmpty() {
		return getMinX()==getMaxX() && getMinY()==getMaxY(); 
	}
	
	/** Inflate this rectangle with the given amounts.
	 * 
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 */
	default void inflate(int left, int top, int right, int bottom) {
		setFromCorners(
				getMinX()+left,
				getMinY()+top,
				getMaxX()+right,
				getMaxY()+bottom);
	}

}
