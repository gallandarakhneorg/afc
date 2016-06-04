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

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;

/** Fonctional interface that represented a rectangular shape on a plane.
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
public interface RectangularShape2ai<
		ST extends Shape2ai<?, ?, IE, P, V, B>,
		IT extends RectangularShape2ai<?, ?, IE, P, V, B>,
		IE extends PathElement2ai,
		P extends Point2D<? super P, ? super V>,
		V extends Vector2D<? super V, ? super P>,
		B extends Rectangle2ai<?, ?, IE, P, V, B>>
		extends Shape2ai<ST, IT, IE, P, V, B> {

	@Override
	default void toBoundingBox(B box) {
		assert box != null : "Rectangle must not be null"; 
		box.setFromCorners(getMinX(), getMinY(), getMaxX(), getMaxY());
	}

	@Override
	default void clear() {
		setFromCorners(0, 0, 0, 0);
	}

	/** Change the frame of the rectangle.
	 *
	 * @param x x coordinate of the lower corner.
	 * @param y y coordinate of the lower corner.
	 * @param width width of the rectangular shape.
	 * @param height height of the rectangular shape.
	 */
	default void set(int x, int y, int width, int height) {
		assert width >= 0 : "Width must be positive or zero"; 
		assert height >= 0 : "Height must be positive or zero"; 
		setFromCorners(x, y, x + width, y + height);
	}

	/** Change the frame of the rectangle.
	 *
	 * @param min is the min corner of the rectangle.
	 * @param max is the max corner of the rectangle.
	 */
	default void set(Point2D<?, ?> min, Point2D<?, ?> max) {
		assert min != null : "Minimum point must be not be null"; 
		assert max != null : "Maximum point must be not be null"; 
		setFromCorners(min.ix(), min.iy(), max.ix(), max.iy());
	}

	/** Change the width of the rectangle, not the min corner.
	 *
	 * @param width width of the rectangular shape.
	 */
	default void setWidth(int width) {
		assert width >= 0 : "Width must be positive or zero"; 
		setMaxX(getMinX() + width);
	}

	/** Change the height of the rectangle, not the min corner.
	 *
	 * @param height height of the rectangular shape.
	 */
	default void setHeight(int height) {
		assert height >= 0 : "Height must be positive or zero"; 
		setMaxY(getMinY() + height);
	}

	/** Change the frame of the rectangle conserving previous min and max if needed.
	 *
	 * @param x1 is the coordinate of the first corner.
	 * @param y1 is the coordinate of the first corner.
	 * @param x2 is the coordinate of the second corner.
	 * @param y2 is the coordinate of the second corner.
	 */
	void setFromCorners(int x1, int y1, int x2, int y2);

	/** Change the frame of the rectangle conserving previous min and max if needed.
	 *
	 * @param p1 the first corner.
	 * @param p2 the second corner.
	 */
	default void setFromCorners(Point2D<?, ?> p1, Point2D<?, ?> p2) {
		assert p1 != null : "First corner must be not be null"; 
		assert p2 != null : "Second corner must be not be null"; 
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
		final int demiWidth = Math.abs(centerX - cornerX);
		final int demiHeight = Math.abs(centerY - cornerY);
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
	default void setFromCenter(Point2D<?, ?> center, Point2D<?, ?> corner) {
		assert center != null : "Center must be not be null"; 
		assert corner != null : "Corner must be not be null"; 
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
		return getMinX() == getMaxX() && getMinY() == getMaxY();
	}

	/** Inflate this rectangle with the given amounts.
	 *
	 * <p>The four borders may be inflated. If the value associated to a border
	 * is positive, the border is moved outside the current rectangle.
	 * If the value is negative, the border is moved inside the rectangle.
	 *
	 * @param minXBorder the value to substract to the minimum x.
	 * @param minYBorder the value to substract to the minimum y.
	 * @param maxXBorder the value to add to the maximum x.
	 * @param maxYBorder the value to add to the maximum y.
	 */
	default void inflate(int minXBorder, int minYBorder, int maxXBorder, int maxYBorder) {
		setFromCorners(
				getMinX() - minXBorder,
				getMinY() - minYBorder,
				getMaxX() + maxXBorder,
				getMaxY() + maxYBorder);
	}

}
