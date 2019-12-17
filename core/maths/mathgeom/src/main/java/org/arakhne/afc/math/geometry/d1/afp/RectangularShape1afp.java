/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

package org.arakhne.afc.math.geometry.d1.afp;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d1.Point1D;
import org.arakhne.afc.math.geometry.d1.Segment1D;
import org.arakhne.afc.math.geometry.d1.Vector1D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** Fonctional interface that represented a rectangular shape on a plane.
 *
 * @param <ST> is the type of the general implementation.
 * @param <IT> is the type of the implementation of this shape.
 * @param <P> is the type of the points.
 * @param <V> is the type of the vectors.
 * @param <S> is the type of the segment.
 * @param <B> is the type of the bounding boxes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public interface RectangularShape1afp<
		ST extends Shape1afp<?, ?, P, V, S, B>,
		IT extends RectangularShape1afp<?, ?, P, V, S, B>,
		P extends Point1D<? super P, ? super V, ? super S>,
		V extends Vector1D<? super V, ? super P, ? super S>,
		S extends Segment1D<?, ?>,
		B extends Rectangle1afp<?, ?, P, V, S, B>>
		extends Shape1afp<ST, IT, P, V, S, B> {

	@Override
	default void toBoundingBox(B box) {
		assert box != null : AssertMessages.notNullParameter();
		box.setSegment(getSegment());
		box.setFromCorners(getMinX(), getMinY(), getMaxX(), getMaxY());
	}

	@Override
	default void clear() {
		setFromCorners(0, 0, 0, 0);
	}

	/** Change the frame of the rectangle.
	 *
	 * @param x x coordinate of the lower corner of the rectangular shape.
	 * @param y y coordinate of the lower corner of the rectangular shape.
	 * @param width width of the rectangular shape.
	 * @param height height of the rectangular shape.
	 */
	default void set(double x, double y, double width, double height) {
		assert width >= 0. : AssertMessages.positiveOrZeroParameter(2);
		assert height >= 0. : AssertMessages.positiveOrZeroParameter(3);
		setFromCorners(x, y, x + width, y + height);
	}

	@Override
	default void set(IT shape) {
		assert shape != null : AssertMessages.notNullParameter();
		setFromCorners(shape.getMinX(), shape.getMinY(), shape.getMaxX(), shape.getMaxY());
	}

	/** Change the frame of the rectangle.
	 *
	 * @param min is the min corner of the rectangle.
	 * @param max is the max corner of the rectangle.
	 */
	default void set(Point2D<?, ?> min, Point2D<?, ?> max) {
		assert min != null : AssertMessages.notNullParameter(0);
		assert max != null : AssertMessages.notNullParameter(1);
		setFromCorners(min.getX(), min.getY(), max.getX(), max.getY());
	}

	/** Change the width of the rectangle, not the min corner.
	 *
	 * @param width width of the rectangular shape.
	 */
	default void setWidth(double width) {
		assert width >= 0. : AssertMessages.positiveOrZeroParameter();
		setMaxX(getMinX() + width);
	}

	/** Change the height of the rectangle, not the min corner.
	 *
	 * @param height height of the rectangular shape.
	 */
	default void setHeight(double height) {
		assert height >= 0. : AssertMessages.positiveOrZeroParameter();
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
	void setFromCorners(double x1, double y1, double x2, double y2);

	/** Change the frame of the rectangle conserving previous min and max if needed.
	 *
	 * @param p1 the first corner.
	 * @param p2 the second corner.
	 */
	// This function has no default implementation for allowing implementation to be atomic.
	default void setFromCorners(Point2D<?, ?> p1, Point2D<?, ?> p2) {
		assert p1 != null : AssertMessages.notNullParameter(0);
		assert p2 != null : AssertMessages.notNullParameter(1);
		setFromCorners(p1.getX(), p1.getY(), p2.getX(), p2.getY());
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
	default void setFromCenter(double centerX, double centerY, double cornerX, double cornerY) {
		final double demiWidth = Math.abs(centerX - cornerX);
		final double demiHeight = Math.abs(centerY - cornerY);
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
		assert center != null : AssertMessages.notNullParameter(0);
		assert corner != null : AssertMessages.notNullParameter(1);
		setFromCenter(center.getX(), center.getY(), corner.getX(), corner.getY());
	}

	/** Replies the min X.
	 *
	 * @return the min x.
	 */
	@Pure
	double getMinX();

	/** Set the min X conserving previous min if needed.
	 *
	 * @param x the min x.
	 */
	void setMinX(double x);

	/** Replies the center.
	 *
	 * @return the center.
	 */
	@Pure
	default P getCenter() {
		return getGeomFactory().newPoint(getSegment(), getCenterX(), getCenterY());
	}

	/** Replies the center x.
	 *
	 * @return the center x.
	 */
	@Pure
	default double getCenterX() {
		return (getMinX() + getMaxX()) / 2;
	}

	/** Replies the max x.
	 *
	 * @return the max x.
	 */
	@Pure
	double getMaxX();

	/** Set the max X conserving previous max if needed.
	 *
	 * @param x the max x.
	 */
	void setMaxX(double x);

	/** Replies the min y.
	 *
	 * @return the min y.
	 */
	@Pure
	double getMinY();

	/** Set the min Y conserving previous min if needed.
	 *
	 * @param y the min y.
	 */
	void setMinY(double y);

	/** Replies the center y.
	 *
	 * @return the center y.
	 */
	@Pure
	default double getCenterY() {
		return (getMinY() + getMaxY()) / 2;
	}

	/** Replies the max y.
	 *
	 * @return the max y.
	 */
	@Pure
	double getMaxY();

	/** Set the max Y conserving previous max if needed.
	 *
	 * @param y the max y.
	 */
	void setMaxY(double y);

	/** Replies the width.
	 *
	 * @return the width.
	 */
	@Pure
	default double getWidth() {
		return getMaxX() - getMinX();
	}

	/** Replies the height.
	 *
	 * @return the height.
	 */
	@Pure
	default double getHeight() {
		return getMaxY() - getMinY();
	}

	@Override
	default void translate(double dx, double dy) {
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
	default void inflate(double minXBorder, double minYBorder, double maxXBorder, double maxYBorder) {
		setFromCorners(
				getMinX() - minXBorder,
				getMinY() - minYBorder,
				getMaxX() + maxXBorder,
				getMaxY() + maxYBorder);
	}

}
