/* 
 * $Id$
 * 
 * Copyright (C) 2011 Janus Core Developers
 * Copyright (C) 2012-13 Stephane GALLAND.
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
package org.arakhne.afc.math.geometry.d2.ifx;

import static org.junit.Assert.assertNotSame;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.ai.AbstractRectangle2aiTest;
import org.arakhne.afc.math.geometry.d2.ai.Path2ai;
import org.arakhne.afc.math.geometry.d2.ai.Segment2ai;
import org.junit.Test;

@SuppressWarnings("all")
public class Rectangle2ifxTest extends AbstractRectangle2aiTest<Rectangle2ifx> {

	@Override
	protected Segment2ai<?, ?, ?, ?, ?> createSegment(int x1, int y1, int x2, int y2) {
		return new Segment2ifx(x1, y1, x2, y2);
	}

	@Override
	protected Rectangle2ifx createRectangularShape(int x, int y, int width, int height) {
		return new Rectangle2ifx(x, y, width, height);
	}

	@Override
	protected Circle2ifx createCircle(int x, int y, int radius) {
		return new Circle2ifx(x, y, radius);
	}

	@Override
	protected Point2D createPoint(int x, int y) {
		return new Point2ifx(x, y);
	}

	@Override
	protected Vector2D createVector(int x, int y) {
		return new Vector2ifx(x, y);
	}

	@Override
	protected Path2ai<?, ?, ?, ?, ?> createPath() {
		return new Path2ifx();
	}

	@Test
	@Override
	public void testClone() {
		super.testClone();
		Rectangle2ifx clone = this.shape.clone();
		assertNotSame(this.shape.minXProperty(), clone.minXProperty());
		assertNotSame(this.shape.maxXProperty(), clone.maxXProperty());
		assertNotSame(this.shape.minYProperty(), clone.minYProperty());
		assertNotSame(this.shape.maxYProperty(), clone.maxYProperty());
	}

}