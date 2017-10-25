/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

package org.arakhne.afc.math.geometry.d2.afp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

@SuppressWarnings("all")
public abstract class AbstractRectangularShape2afpTest<T extends RectangularShape2afp<?, T, ?, ?, ?, B>,
		B extends Rectangle2afp<?, ?, ?, ?, ?, B>> extends AbstractShape2afpTest<T, B> {

	@Test
	public abstract void inflate();

	@Test
	public void setDoubleDoubleDoubleDouble() {
		this.shape.set(123.456, 456.789, 789.123, 159.753);
		assertEpsilonEquals(123.456, this.shape.getMinX());
		assertEpsilonEquals(456.789, this.shape.getMinY());
		assertEpsilonEquals(912.579, this.shape.getMaxX());
		assertEpsilonEquals(616.542, this.shape.getMaxY());
	}
	
	@Test
	public void setPoint2DPoint2D() {
		this.shape.set(createPoint(123.456, 456.789), createPoint(789.123, 159.753));
		assertEpsilonEquals(123.456, this.shape.getMinX());
		assertEpsilonEquals(159.753, this.shape.getMinY());
		assertEpsilonEquals(789.123, this.shape.getMaxX());
		assertEpsilonEquals(456.789, this.shape.getMaxY());
	}
	
	@Test
	public void setWidth() {
		this.shape.setWidth(123.456);
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(128.456, this.shape.getMaxX());
		assertEpsilonEquals(18, this.shape.getMaxY());
	}

	@Test
	public void setHeight() {
		this.shape.setHeight(123.456);
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(10, this.shape.getMaxX());
		assertEpsilonEquals(131.456, this.shape.getMaxY());
	}
	
	@Test
	public void setFromCornersDoubleDoubleDoubleDouble() {
		this.shape.setFromCorners(123.456, 456.789, 789.123, 159.753);
		assertEpsilonEquals(123.456, this.shape.getMinX());
		assertEpsilonEquals(159.753, this.shape.getMinY());
		assertEpsilonEquals(789.123, this.shape.getMaxX());
		assertEpsilonEquals(456.789, this.shape.getMaxY());
	}
	
	@Test
	public void setFromCornersPoint2DPoint2D() {
		this.shape.setFromCorners(createPoint(123.456, 456.789), createPoint(789.123, 159.753));
		assertEpsilonEquals(123.456, this.shape.getMinX());
		assertEpsilonEquals(159.753, this.shape.getMinY());
		assertEpsilonEquals(789.123, this.shape.getMaxX());
		assertEpsilonEquals(456.789, this.shape.getMaxY());
	}

	@Test
	public void setFromCenterDoubleDoubleDoubleDouble() {
		this.shape.setFromCenter(123.456, 456.789, 789.123, 159.753);
		assertEpsilonEquals(-542.211, this.shape.getMinX());
		assertEpsilonEquals(159.753, this.shape.getMinY());
		assertEpsilonEquals(789.123, this.shape.getMaxX());
		assertEpsilonEquals(753.825, this.shape.getMaxY());
	}
	
	@Test
	public void setFromCenterPoint2DPoint2D() {
		this.shape.setFromCenter(createPoint(123.456, 456.789), createPoint(789.123, 159.753));
		assertEpsilonEquals(-542.211, this.shape.getMinX());
		assertEpsilonEquals(159.753, this.shape.getMinY());
		assertEpsilonEquals(789.123, this.shape.getMaxX());
		assertEpsilonEquals(753.825, this.shape.getMaxY());
	}

	@Test
	public void getMinX() {
		assertEpsilonEquals(5, this.shape.getMinX());
	}

	@Test
	public void setMinX() {
		this.shape.setMinX(123.456);
		assertEpsilonEquals(123.456, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(123.456, this.shape.getMaxX());
		assertEpsilonEquals(18, this.shape.getMaxY());
	}

	@Test
	public void getCenterX() {
		assertEpsilonEquals(7.5, this.shape.getCenterX());
	}

	@Test
	public void getMaxX() {
		assertEpsilonEquals(10, this.shape.getMaxX());
	}

	@Test
	public void setMaxX() {
		this.shape.setMaxX(-123.456);
		assertEpsilonEquals(-123.456, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(-123.456, this.shape.getMaxX());
		assertEpsilonEquals(18, this.shape.getMaxY());
	}

	@Test
	public void getMinY() {
		assertEpsilonEquals(8, this.shape.getMinY());
	}

	@Test
	public void setMinY() {
		this.shape.setMinY(123.456);
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(123.456, this.shape.getMinY());
		assertEpsilonEquals(10, this.shape.getMaxX());
		assertEpsilonEquals(123.456, this.shape.getMaxY());
	}

	@Test
	public void getCenterY() {
		assertEpsilonEquals(13, this.shape.getCenterY());
	}

	@Test
	public void getMaxY() {
		assertEpsilonEquals(18, this.shape.getMaxY());
	}
	
	@Test
	public void setMaxY() {
		this.shape.setMaxY(-123.456);
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(-123.456, this.shape.getMinY());
		assertEpsilonEquals(10, this.shape.getMaxX());
		assertEpsilonEquals(-123.456, this.shape.getMaxY());
	}

	@Test
	public void getWidth() {
		assertEpsilonEquals(5, this.shape.getWidth());
	}

	@Test
	public void getHeight() {
		assertEpsilonEquals(10, this.shape.getHeight());
	}
		
	@Test
	@Override
	public void testClone() {
		T clone = this.shape.clone();
		assertNotNull(clone);
		assertNotSame(this.shape, clone);
		assertEquals(this.shape.getClass(), clone.getClass());
		assertEpsilonEquals(5, clone.getMinX());
		assertEpsilonEquals(8, clone.getMinY());
		assertEpsilonEquals(10, clone.getMaxX());
		assertEpsilonEquals(18, clone.getMaxY());
	}

	@Test
	@Override
	public void isEmpty() {
		assertFalse(this.shape.isEmpty());
		this.shape.clear();
		assertTrue(this.shape.isEmpty());
	}

	@Test
	@Override
	public void clear() {
		this.shape.clear();
		assertEpsilonEquals(0, this.shape.getMinX());
		assertEpsilonEquals(0, this.shape.getMinY());
		assertEpsilonEquals(0, this.shape.getMaxX());
		assertEpsilonEquals(0, this.shape.getMaxY());
	}

	@Override
	public void translateDoubleDouble() {
		this.shape.translate(123.456, 456.789);
		assertEpsilonEquals(128.456, this.shape.getMinX());
		assertEpsilonEquals(464.789, this.shape.getMinY());
		assertEpsilonEquals(133.456, this.shape.getMaxX());
		assertEpsilonEquals(474.789, this.shape.getMaxY());
	}

	@Override
	public void translateVector2D() {
		this.shape.translate(createVector(123.456, 456.789));
		assertEpsilonEquals(128.456, this.shape.getMinX());
		assertEpsilonEquals(464.789, this.shape.getMinY());
		assertEpsilonEquals(133.456, this.shape.getMaxX());
		assertEpsilonEquals(474.789, this.shape.getMaxY());
	}

	@Override
	public void toBoundingBox() {
		B box = this.shape.toBoundingBox();
		assertNotSame(this.shape, box);
		assertEpsilonEquals(5, box.getMinX());
		assertEpsilonEquals(8, box.getMinY());
		assertEpsilonEquals(10, box.getMaxX());
		assertEpsilonEquals(18, box.getMaxY());
	}

	@Override
	public void toBoundingBoxB() {
		B box = createRectangle(0, 0, 0, 0);
		this.shape.toBoundingBox(box);
		assertEpsilonEquals(5, box.getMinX());
		assertEpsilonEquals(8, box.getMinY());
		assertEpsilonEquals(10, box.getMaxX());
		assertEpsilonEquals(18, box.getMaxY());
	}

}