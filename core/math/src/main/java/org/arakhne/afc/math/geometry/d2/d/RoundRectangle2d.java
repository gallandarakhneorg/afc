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

package org.arakhne.afc.math.geometry.d2.d;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.afp.RectangularShape2afp;
import org.arakhne.afc.math.geometry.d2.afp.RoundRectangle2afp;

/** A round rectangle with 2 double precision floating-point numbers.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class RoundRectangle2d extends AbstractRectangularShape2d<RoundRectangle2d>
		implements RoundRectangle2afp<Shape2d<?>, RoundRectangle2d, PathElement2d, Point2d, Vector2d, Rectangle2d> {

	private static final long serialVersionUID = -6419985193487310000L;

	private double arcWidth;

	private double arcHeight;

	/** Construct an empty round rectangle.
	 */
	public RoundRectangle2d() {
		super();
	}

	/** Construct a round rectangle with the given corners and arcs.
	 * @param min is the min corner of the rectangle.
	 * @param max is the max corner of the rectangle.
	 * @param arcWidth the width of the arcs.
	 * @param arcHeight the height of the arcs.
	 */
	public RoundRectangle2d(Point2D<?, ?> min, Point2D<?, ?> max, double arcWidth, double arcHeight) {
		this(min.getX(), min.getY(), max.getX(), max.getY(), arcWidth, arcHeight);
	}

	/** Constructor by copy.
	 * @param roundRectangle the round rectangle to copy.
	 */
	public RoundRectangle2d(RoundRectangle2afp<?, ?, ?, ?, ?, ?> roundRectangle) {
		this(roundRectangle.getMinX(), roundRectangle.getMinY(), roundRectangle.getMaxX(),
				roundRectangle.getMaxY(), roundRectangle.getArcWidth(), roundRectangle.getArcHeight());
	}

	/** Construct a round rectangle with the corners of the given shape, and zero arcs.
	 * @param shape the shape to copy.
	 */
	public RoundRectangle2d(RectangularShape2afp<?, ?, ?, ?, ?, ?> shape) {
		this(shape.getMinX(), shape.getMinY(), shape.getMaxX(), shape.getMaxY(), 0, 0);
	}

	/** Construct a round rectangle with the given corners and arcs.
	 * @param x x coordinate of the minimum corner.
	 * @param y y coordinate of the minimum corner.
	 * @param width width of the rectangle.
	 * @param height height of the rectangle.
	 * @param arcWidth width of the arcs.
	 * @param arcHeight height of the arcs.
	 */
	public RoundRectangle2d(double x, double y, double width, double height, double arcWidth, double arcHeight) {
		set(x, y, width, height, arcWidth, arcHeight);
	}

	@Pure
	@Override
	public int hashCode() {
		long bits = super.hashCode();
		bits = 31 * bits + Double.doubleToLongBits(this.arcWidth);
		bits = 31 * bits + Double.doubleToLongBits(this.arcHeight);
		final int b = (int) bits;
		return b ^ (b >> 32);
	}

	@Pure
	@Override
	public String toString() {
		final StringBuilder b = new StringBuilder();
		b.append("["); //$NON-NLS-1$
		b.append(getMinX());
		b.append(";"); //$NON-NLS-1$
		b.append(getMinY());
		b.append(";"); //$NON-NLS-1$
		b.append(getMaxX());
		b.append(";"); //$NON-NLS-1$
		b.append(getMaxY());
		b.append("|"); //$NON-NLS-1$
		b.append(getArcWidth());
		b.append("x"); //$NON-NLS-1$
		b.append(getArcHeight());
		b.append("]"); //$NON-NLS-1$
		return b.toString();
	}

	@Pure
	@Override
	public double getArcWidth() {
		return this.arcWidth;
	}

	@Pure
	@Override
	public double getArcHeight() {
		return this.arcHeight;
	}

	@Override
	public void setArcWidth(double arcWidth) {
		assert arcWidth >= 0 : "Arc width must be positive or zero"; //$NON-NLS-1$
		this.arcWidth = Math.min(getWidth() / 2, arcWidth);
	}

	@Override
	public void setArcHeight(double arcHeight) {
		assert arcHeight >= 0 : "Arc height must be positive or zero"; //$NON-NLS-1$
		this.arcHeight = Math.min(getHeight() / 2, arcHeight);
	}

	@Override
	public void setFromCorners(double x1, double y1, double x2, double y2) {
		super.setFromCorners(x1, y1, x2, y2);
		ensureValidArcWidth();
		ensureValidArcHeight();
	}

	@Override
	public void setFromCorners(double x1, double y1, double x2, double y2, double arcWidth, double arcHeight) {
		super.setFromCorners(x1, y1, x2, y2);
		setArcWidth(arcWidth);
		setArcHeight(arcHeight);
	}

	/** Ensure that the size of the arc width is valid, i.e. not too big or too small.
	 */
	protected void ensureValidArcWidth() {
		final double halfWidth = getWidth() / 2;
		if (this.arcWidth > halfWidth) {
			this.arcWidth = halfWidth;
		}
	}

	/** Ensure that the size of the arc height is valid, i.e. not too big or too small.
	 */
	protected void ensureValidArcHeight() {
		final double halfHeight = getHeight() / 2;
		if (this.arcHeight > halfHeight) {
			this.arcHeight = halfHeight;
		}
	}

	@Override
	public void setMinX(double x) {
		super.setMinX(x);
		ensureValidArcWidth();
	}

	@Override
	public void setMaxX(double x) {
		super.setMaxX(x);
		ensureValidArcWidth();
	}

	@Override
	public void setMinY(double y) {
		super.setMinY(y);
		ensureValidArcHeight();
	}

	@Override
	public void setMaxY(double y) {
		super.setMaxY(y);
		ensureValidArcHeight();
	}

}
