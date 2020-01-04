/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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

package org.arakhne.afc.math.test.geometry.d3.ai;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.d3.ai.RectangularPrism3ai;

@SuppressWarnings("all")
public abstract class AbstractPrism3aiTest<T extends RectangularPrism3ai<?, T, ?, ?, ?, B>,
		B extends RectangularPrism3ai<?, ?, ?, ?, ?, B>> extends AbstractShape3aiTest<T, B> {

	protected static final int MINX = 5;
	
	protected static final int MINY = 8;
	
	protected static final int WIDTH = 10;
	
	protected static final int HEIGHT = 5;
	 
	protected static final int MAXX = MINX + WIDTH;
	
	protected static final int MAXY = MINY + HEIGHT;

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void toBoundingBoxB(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		B box = createRectangularPrism(0, 0, 0, 0, 0, 0);
		this.shape.toBoundingBox(box);
		assertEquals(this.shape.getMinX(), box.getMinX());
		assertEquals(this.shape.getMinY(), box.getMinY());
		assertEquals(this.shape.getMinZ(), box.getMinZ());
		assertEquals(this.shape.getMaxX(), box.getMaxX());
		assertEquals(this.shape.getMaxY(), box.getMaxY());
		assertEquals(this.shape.getMaxZ(), box.getMaxZ());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void toBoundingBox(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		B box = this.shape.toBoundingBox();
		assertEquals(this.shape.getMinX(), box.getMinX());
		assertEquals(this.shape.getMinY(), box.getMinY());
		assertEquals(this.shape.getMinZ(), box.getMinZ());
		assertEquals(this.shape.getMaxX(), box.getMaxX());
		assertEquals(this.shape.getMaxY(), box.getMaxY());
		assertEquals(this.shape.getMaxZ(), box.getMaxZ());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void clear(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.clear();
		assertEquals(0, this.shape.getMinX());
		assertEquals(0, this.shape.getMinY());
		assertEquals(0, this.shape.getMinZ());
		assertEquals(0, this.shape.getMaxX());
		assertEquals(0, this.shape.getMaxY());
		assertEquals(0, this.shape.getMaxZ());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setIntIntIntInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.set(10, 12, 0, 14, 16, 0);
		assertEquals(10, this.shape.getMinX());
		assertEquals(12, this.shape.getMinY());
		assertEquals(24, this.shape.getMaxX());
		assertEquals(28, this.shape.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setPoint2DPoint2D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.set(createPoint(10, 12, 0), createPoint(14, 16, 0));
		assertEquals(10, this.shape.getMinX());
		assertEquals(12, this.shape.getMinY());
		assertEquals(14, this.shape.getMaxX());
		assertEquals(16, this.shape.getMaxY());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setWidth(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setWidth(150);
		assertEquals(5, this.shape.getMinX());
		assertEquals(8, this.shape.getMinY());
		assertEquals(155, this.shape.getMaxX());
		assertEquals(13, this.shape.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setHeight(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setHeight(150);
		assertEquals(5, this.shape.getMinX());
		assertEquals(8, this.shape.getMinY());
		assertEquals(15, this.shape.getMaxX());
		assertEquals(158, this.shape.getMaxY());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setFromCornersIntIntIntInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setFromCorners(2, 3, 0, 4, 5, 0);
		assertEquals(2, this.shape.getMinX());
		assertEquals(3, this.shape.getMinY());
		assertEquals(4, this.shape.getMaxX()); 
		assertEquals(5, this.shape.getMaxY());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setFromCornersPoint2DPoint2D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setFromCorners(createPoint(2, 3, 0), createPoint(4, 5, 0));
		assertEquals(2, this.shape.getMinX());
		assertEquals(3, this.shape.getMinY());
		assertEquals(4, this.shape.getMaxX()); 
		assertEquals(5, this.shape.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setFromCenterIntIntIntInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setFromCenter(2, 3, 0, 4, 5, 0);
		assertEquals(0, this.shape.getMinX());
		assertEquals(1, this.shape.getMinY());
		assertEquals(4, this.shape.getMaxX()); 
		assertEquals(5, this.shape.getMaxY());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setFromCenterPoint2DPoint2D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setFromCenter(createPoint(2, 3, 0), createPoint(4, 5, 0));
		assertEquals(0, this.shape.getMinX());
		assertEquals(1, this.shape.getMinY());
		assertEquals(4, this.shape.getMaxX()); 
		assertEquals(5, this.shape.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getMinX(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals(MINX, this.shape.getMinX());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setMinX(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setMinX(-45);
		assertEquals(-45, this.shape.getMinX());
		assertEquals(MINY, this.shape.getMinY());
		assertEquals(MAXX, this.shape.getMaxX()); 
		assertEquals(MAXY, this.shape.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getCenterX(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals(MINX + WIDTH / 2, this.shape.getCenterX());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getMaxX(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals(MAXX, this.shape.getMaxX());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setMaxX(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setMaxX(45);
		assertEquals(MINX, this.shape.getMinX());
		assertEquals(MINY, this.shape.getMinY());
		assertEquals(45, this.shape.getMaxX()); 
		assertEquals(MAXY, this.shape.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getMinY(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals(MINY, this.shape.getMinY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setMinY(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setMinY(-45);
		assertEquals(MINX, this.shape.getMinX());
		assertEquals(-45, this.shape.getMinY());
		assertEquals(MAXX, this.shape.getMaxX()); 
		assertEquals(MAXY, this.shape.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getCenterY(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals(MINY + HEIGHT / 2, this.shape.getCenterY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getMaxY(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals(MAXY, this.shape.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setMaxY(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setMaxY(45);
		assertEquals(MINX, this.shape.getMinX());
		assertEquals(MINY, this.shape.getMinY());
		assertEquals(MAXX, this.shape.getMaxX()); 
		assertEquals(45, this.shape.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getWidth(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals(WIDTH, this.shape.getWidth());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getHeight(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals(HEIGHT, this.shape.getHeight());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void translateIntInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.translate(3, 4, 0);
		assertEquals(8, this.shape.getMinX());
		assertEquals(12, this.shape.getMinY());
		assertEquals(18, this.shape.getMaxX());
		assertEquals(17, this.shape.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void isEmpty(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.isEmpty());
		this.shape.clear();
		assertTrue(this.shape.isEmpty());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public abstract void inflate(CoordinateSystem3D cs);

}
