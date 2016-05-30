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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

@SuppressWarnings("all")
public abstract class AbstractRectangularShape2aiTest<T extends Rectangle2ai<?, T, ?, ?, ?, B>,
		B extends Rectangle2ai<?, ?, ?, ?, ?, B>> extends AbstractShape2aiTest<T, B> {

	protected static final int MINX = 5;
	
	protected static final int MINY = 8;
	
	protected static final int WIDTH = 10;
	
	protected static final int HEIGHT = 5;
	 
	protected static final int MAXX = MINX + WIDTH;
	
	protected static final int MAXY = MINY + HEIGHT;

	@Test
	@Override
	public void toBoundingBoxB() {
		B box = (B) createRectangle(0, 0, 0, 0);
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
	public abstract void inflate();

}