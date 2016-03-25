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
package org.arakhne.afc.math.geometry.d2.ai;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.junit.Ignore;
import org.junit.Test;

@SuppressWarnings("all")
public abstract class AbstractRectangularShape2aiTest<T extends Rectangle2ai<?, T, ?, ?, B>,
		B extends Rectangle2ai<?, ?, ?, ?, B>> extends AbstractShape2aiTest<T> {

	protected static final int MINX = 5;
	
	protected static final int MINY = 8;
	
	protected static final int WIDTH = 10;
	
	protected static final int HEIGHT = 5;
	 
	protected static final int MAXX = MINX + WIDTH;
	
	protected static final int MAXY = MINY + HEIGHT;

	protected abstract T createRectangularShape(int x, int y, int width, int height);

	protected abstract B createRectangle(int x, int y, int width, int height);

	protected abstract Segment2ai<?, ?, ?, ?, ?> createSegment(int x1, int y1, int x2, int y2);
	
	protected abstract Circle2ai<?, ?, ?, ?, ?> createCircle(int x, int y, int radius);
	
	protected abstract Point2D createPoint(int x, int y);
	
	protected abstract Vector2D createVector(int x, int y);
	
	@Test
	@Override
	public void toBoundingBoxB() {
		B box = createRectangle(0, 0, 0, 0);
		this.shape.toBoundingBox(box);
		assertEquals(this.shape.getMinX(), box.getMinX());
		assertEquals(this.shape.getMinY(), box.getMinY());
		assertEquals(this.shape.getMaxX(), box.getMaxX());
		assertEquals(this.shape.getMaxY(), box.getMaxY());
	}
	
	@Test
	@Override
	public void toBoundingBox() {
		B box = this.shape.toBoundingBox();
		assertEquals(this.shape.getMinX(), box.getMinX());
		assertEquals(this.shape.getMinY(), box.getMinY());
		assertEquals(this.shape.getMaxX(), box.getMaxX());
		assertEquals(this.shape.getMaxY(), box.getMaxY());
	}
	
	@Test
	@Override
	public void clear() {
		this.shape.clear();
		assertEquals(0, this.shape.getMinX());
		assertEquals(0, this.shape.getMinY());
		assertEquals(0, this.shape.getMaxX());
		assertEquals(0, this.shape.getMaxY());
	}

	@Test
	public void setIntIntIntInt() {
		this.shape.set(10, 12, 14, 16);
		assertEquals(10, this.shape.getMinX());
		assertEquals(12, this.shape.getMinY());
		assertEquals(24, this.shape.getMaxX());
		assertEquals(28, this.shape.getMaxY());
	}

	@Test
	public void setPoint2DPoint2D() {
		this.shape.set(createPoint(10, 12), createPoint(14, 16));
		assertEquals(10, this.shape.getMinX());
		assertEquals(12, this.shape.getMinY());
		assertEquals(14, this.shape.getMaxX());
		assertEquals(16, this.shape.getMaxY());
	}
	
	@Test
	public void setWidth() {
		this.shape.setWidth(150);
		assertEquals(5, this.shape.getMinX());
		assertEquals(8, this.shape.getMinY());
		assertEquals(155, this.shape.getMaxX());
		assertEquals(13, this.shape.getMaxY());
	}

	@Test
	public void setHeight() {
		this.shape.setHeight(150);
		assertEquals(5, this.shape.getMinX());
		assertEquals(8, this.shape.getMinY());
		assertEquals(15, this.shape.getMaxX());
		assertEquals(158, this.shape.getMaxY());
	}
	
	@Test
	public void setFromCornersIntIntIntInt() {
		this.shape.setFromCorners(2, 3, 4, 5);
		assertEquals(2, this.shape.getMinX());
		assertEquals(3, this.shape.getMinY());
		assertEquals(4, this.shape.getMaxX()); 
		assertEquals(5, this.shape.getMaxY());
	}
	
	@Test
	public void setFromCornersPoint2DPoint2D() {
		this.shape.setFromCorners(createPoint(2, 3), createPoint(4, 5));
		assertEquals(2, this.shape.getMinX());
		assertEquals(3, this.shape.getMinY());
		assertEquals(4, this.shape.getMaxX()); 
		assertEquals(5, this.shape.getMaxY());
	}

	@Test
	public void setFromCenterIntIntIntInt() {
		this.shape.setFromCenter(2, 3, 4, 5);
		assertEquals(0, this.shape.getMinX());
		assertEquals(1, this.shape.getMinY());
		assertEquals(4, this.shape.getMaxX()); 
		assertEquals(5, this.shape.getMaxY());
	}
	
	@Test
	public void setFromCenterPoint2DPoint2D() {
		this.shape.setFromCenter(createPoint(2, 3), createPoint(4, 5));
		assertEquals(0, this.shape.getMinX());
		assertEquals(1, this.shape.getMinY());
		assertEquals(4, this.shape.getMaxX()); 
		assertEquals(5, this.shape.getMaxY());
	}

	@Test
	public void getMinX() {
		assertEquals(MINX, this.shape.getMinX());
	}

	@Test
	public void setMinX() {
		this.shape.setMinX(-45);
		assertEquals(-45, this.shape.getMinX());
		assertEquals(MINY, this.shape.getMinY());
		assertEquals(MAXX, this.shape.getMaxX()); 
		assertEquals(MAXY, this.shape.getMaxY());
	}

	@Test
	public void getCenterX() {
		assertEquals(MINX + WIDTH / 2, this.shape.getCenterX());
	}

	@Test
	public void getMaxX() {
		assertEquals(MAXX, this.shape.getMaxX());
	}

	@Test
	public void setMaxX() {
		this.shape.setMaxX(45);
		assertEquals(MINX, this.shape.getMinX());
		assertEquals(MINY, this.shape.getMinY());
		assertEquals(45, this.shape.getMaxX()); 
		assertEquals(MAXY, this.shape.getMaxY());
	}

	@Test
	public void getMinY() {
		assertEquals(MINY, this.shape.getMinY());
	}

	@Test
	public void setMinY() {
		this.shape.setMinY(-45);
		assertEquals(MINX, this.shape.getMinX());
		assertEquals(-45, this.shape.getMinY());
		assertEquals(MAXX, this.shape.getMaxX()); 
		assertEquals(MAXY, this.shape.getMaxY());
	}

	@Test
	public void getCenterY() {
		assertEquals(MINY + HEIGHT / 2, this.shape.getCenterY());
	}

	@Test
	public void getMaxY() {
		assertEquals(MAXY, this.shape.getMaxY());
	}

	@Test
	public void setMaxY() {
		this.shape.setMaxY(45);
		assertEquals(MINX, this.shape.getMinX());
		assertEquals(MINY, this.shape.getMinY());
		assertEquals(MAXX, this.shape.getMaxX()); 
		assertEquals(45, this.shape.getMaxY());
	}

	@Test
	public void getWidth() {
		assertEquals(WIDTH, this.shape.getWidth());
	}

	@Test
	public void getHeight() {
		assertEquals(HEIGHT, this.shape.getHeight());
	}
	
	@Test
	@Override
	public void translateIntInt() {
		this.shape.translate(3,  4);
		assertEquals(8, this.shape.getMinX());
		assertEquals(12, this.shape.getMinY());
		assertEquals(18, this.shape.getMaxX());
		assertEquals(17, this.shape.getMaxY());
	}

	@Test
	@Override
	public void isEmpty() {
		assertFalse(this.shape.isEmpty());
		this.shape.clear();
		assertTrue(this.shape.isEmpty());
	}

	@Test
	@Ignore
	@Override
	public void createTransformedShape() {
		// XXX: Must be written
	}
	
	@Test
	public void inflate() {
	}

}