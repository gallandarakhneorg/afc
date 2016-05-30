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
package org.arakhne.afc.math.geometry.d3.afp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.ai.PathIterator2ai;
import org.eclipse.xtext.xbase.lib.Pure;
import org.junit.Test;

@SuppressWarnings("all")
public abstract class AbstractPrism3afpTest<T extends Prism3afp<?, T, ?, ?, ?, B>,
		B extends RectangularPrism3afp<?, ?, ?, ?, ?, B>> extends AbstractShape3afpTest<T, B> {

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
	public void translateVector3D() {
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