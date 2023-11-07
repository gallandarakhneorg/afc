/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

package org.arakhne.afc.math.test.geometry.d3.afp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.d3.afp.Box3afp;
import org.arakhne.afc.math.geometry.d3.afp.AlignedBox3afp;

@SuppressWarnings("all")
public abstract class AbstractBox3afpTest<T extends Box3afp<?, T, ?, ?, ?, ?, B>,
		B extends AlignedBox3afp<?, ?, ?, ?, ?, ?, B>> extends AbstractShape3afpTest<T, B> {

	@DisplayName("inflate(double,double,double)")
	public abstract void inflate(CoordinateSystem3D cs);

	@DisplayName("set(double,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setDoubleDoubleDoubleDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.set(123.456, 456.789, 0, 789.123, 159.753, 0);
		assertEpsilonEquals(123.456, this.shape.getMinX());
		assertEpsilonEquals(456.789, this.shape.getMinY());
		assertEpsilonEquals(456.789, this.shape.getMinZ());
		assertEpsilonEquals(912.579, this.shape.getMaxX());
		assertEpsilonEquals(616.542, this.shape.getMaxY());
		assertEpsilonEquals(616.542, this.shape.getMaxZ());
	}
	
	@DisplayName("set(Point3D,Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setPoint3DPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.set(createPoint(123.456, 456.789, 1), createPoint(789.123, 159.753, 2));
		assertEpsilonEquals(123.456, this.shape.getMinX());
		assertEpsilonEquals(159.753, this.shape.getMinY());
		assertEpsilonEquals(1, this.shape.getMinZ());
		assertEpsilonEquals(789.123, this.shape.getMaxX());
		assertEpsilonEquals(456.789, this.shape.getMaxY());
		assertEpsilonEquals(2, this.shape.getMaxZ());
	}
	
	@DisplayName("setWidth(double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setWidth(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setWidth(123.456);
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(0, this.shape.getMinZ());
		assertEpsilonEquals(128.456, this.shape.getMaxX());
		assertEpsilonEquals(18, this.shape.getMaxY());
		assertEpsilonEquals(1, this.shape.getMaxZ());
	}

	@DisplayName("setHeight(double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setHeight(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setHeight(123.456);
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(0, this.shape.getMinZ());
		assertEpsilonEquals(10, this.shape.getMaxX());
		assertEpsilonEquals(131.456, this.shape.getMaxY());
		assertEpsilonEquals(1, this.shape.getMaxZ());
	}
	
	@DisplayName("setDepth(double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setDepth(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setDepth(123.456);
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(0, this.shape.getMinZ());
		assertEpsilonEquals(10, this.shape.getMaxX());
		assertEpsilonEquals(18, this.shape.getMaxY());
		assertEpsilonEquals(123.456, this.shape.getMaxZ());
	}

	@DisplayName("setFromCorners(double,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setFromCornersDoubleDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setFromCorners(123.456, 456.789, 0, 789.123, 159.753, 2);
		assertEpsilonEquals(123.456, this.shape.getMinX());
		assertEpsilonEquals(159.753, this.shape.getMinY());
		assertEpsilonEquals(0, this.shape.getMinZ());
		assertEpsilonEquals(789.123, this.shape.getMaxX());
		assertEpsilonEquals(456.789, this.shape.getMaxY());
		assertEpsilonEquals(2, this.shape.getMaxZ());
	}
	
	@DisplayName("setFromCorners(Point3D,Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setFromCornersPoint3DPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setFromCorners(createPoint(123.456, 456.789, 1), createPoint(789.123, 159.753, 2));
		assertEpsilonEquals(123.456, this.shape.getMinX());
		assertEpsilonEquals(159.753, this.shape.getMinY());
		assertEpsilonEquals(1, this.shape.getMinZ());
		assertEpsilonEquals(789.123, this.shape.getMaxX());
		assertEpsilonEquals(456.789, this.shape.getMaxY());
		assertEpsilonEquals(2, this.shape.getMaxZ());
	}

	@DisplayName("setFromCenter(double,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setFromCenterDoubleDoubleDoubleDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setFromCenter(123.456, 456.789, 1, 789.123, 159.753, 2);
		assertEpsilonEquals(-542.211, this.shape.getMinX());
		assertEpsilonEquals(159.753, this.shape.getMinY());
		assertEpsilonEquals(0, this.shape.getMinZ());
		assertEpsilonEquals(789.123, this.shape.getMaxX());
		assertEpsilonEquals(753.825, this.shape.getMaxY());
		assertEpsilonEquals(2, this.shape.getMaxZ());
	}
	
	@DisplayName("setFromCenter(Point3D,Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setFromCenterPoint3DPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setFromCenter(createPoint(123.456, 456.789, 1), createPoint(789.123, 159.753, 2));
		assertEpsilonEquals(-542.211, this.shape.getMinX());
		assertEpsilonEquals(159.753, this.shape.getMinY());
		assertEpsilonEquals(0, this.shape.getMinZ());
		assertEpsilonEquals(789.123, this.shape.getMaxX());
		assertEpsilonEquals(753.825, this.shape.getMaxY());
		assertEpsilonEquals(2, this.shape.getMaxZ());
	}

	@DisplayName("getMinX")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getMinX(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(5, this.shape.getMinX());
	}

	@DisplayName("setMinX(double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setMinX(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setMinX(123.456);
		assertEpsilonEquals(123.456, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(0, this.shape.getMinZ());
		assertEpsilonEquals(123.456, this.shape.getMaxX());
		assertEpsilonEquals(18, this.shape.getMaxY());
		assertEpsilonEquals(1, this.shape.getMaxZ());
	}

	@DisplayName("getCenterX")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getCenterX(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(7.5, this.shape.getCenterX());
	}

	@DisplayName("getMaxX")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getMaxX(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(10, this.shape.getMaxX());
	}

	@DisplayName("setMaxX(double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setMaxX(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setMaxX(-123.456);
		assertEpsilonEquals(-123.456, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(0, this.shape.getMinZ());
		assertEpsilonEquals(-123.456, this.shape.getMaxX());
		assertEpsilonEquals(18, this.shape.getMaxY());
		assertEpsilonEquals(1, this.shape.getMaxZ());
	}

	@DisplayName("getMinY")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getMinY(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(8, this.shape.getMinY());
	}

	@DisplayName("setMaxY(double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setMinY(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setMinY(123.456);
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(123.456, this.shape.getMinY());
		assertEpsilonEquals(0, this.shape.getMinZ());
		assertEpsilonEquals(10, this.shape.getMaxX());
		assertEpsilonEquals(123.456, this.shape.getMaxY());
		assertEpsilonEquals(1, this.shape.getMaxZ());
	}

	@DisplayName("getCenterY")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getCenterY(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(13, this.shape.getCenterY());
	}

	@DisplayName("getMaxY")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getMaxY(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(18, this.shape.getMaxY());
	}
	
	@DisplayName("setMaxY(double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setMaxY(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setMaxY(-123.456);
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(-123.456, this.shape.getMinY());
		assertEpsilonEquals(0, this.shape.getMinZ());
		assertEpsilonEquals(10, this.shape.getMaxX());
		assertEpsilonEquals(-123.456, this.shape.getMaxY());
		assertEpsilonEquals(1, this.shape.getMaxZ());
	}

	@DisplayName("getMinZ")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getMinZ(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0, this.shape.getMinZ());
	}

	@DisplayName("setMinZ(double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setMinZ(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setMinZ(123.456);
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(123.456, this.shape.getMinZ());
		assertEpsilonEquals(10, this.shape.getMaxX());
		assertEpsilonEquals(18, this.shape.getMaxY());
		assertEpsilonEquals(123.456, this.shape.getMaxZ());
	}

	@DisplayName("getCenterZ")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getCenterZ(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(13, this.shape.getCenterZ());
	}

	@DisplayName("getMaxZ")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getMaxZ(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(1, this.shape.getMaxZ());
	}

	@DisplayName("setMaxZ(double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setMaxZ(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setMaxZ(123.456);
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(0, this.shape.getMinZ());
		assertEpsilonEquals(10, this.shape.getMaxX());
		assertEpsilonEquals(18, this.shape.getMaxY());
		assertEpsilonEquals(123.456, this.shape.getMaxZ());
	}

	@DisplayName("getWidth")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getWidth(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(5, this.shape.getWidth());
	}

	@DisplayName("getHeight")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getHeight(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(10, this.shape.getHeight());
	}
		
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void testClone(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		T clone = this.shape.clone();
		assertNotNull(clone);
		assertNotSame(this.shape, clone);
		assertEquals(this.shape.getClass(), clone.getClass());
		assertEpsilonEquals(5, clone.getMinX());
		assertEpsilonEquals(8, clone.getMinY());
		assertEpsilonEquals(10, clone.getMaxX());
		assertEpsilonEquals(18, clone.getMaxY());
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
	@Override
	public void clear(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.clear();
		assertEpsilonEquals(0, this.shape.getMinX());
		assertEpsilonEquals(0, this.shape.getMinY());
		assertEpsilonEquals(0, this.shape.getMaxX());
		assertEpsilonEquals(0, this.shape.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void translateDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.translate(123.456, 456.789, 0);
		assertEpsilonEquals(128.456, this.shape.getMinX());
		assertEpsilonEquals(464.789, this.shape.getMinY());
		assertEpsilonEquals(133.456, this.shape.getMaxX());
		assertEpsilonEquals(474.789, this.shape.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void translateVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.translate(createVector(123.456, 456.789, 0));
		assertEpsilonEquals(128.456, this.shape.getMinX());
		assertEpsilonEquals(464.789, this.shape.getMinY());
		assertEpsilonEquals(133.456, this.shape.getMaxX());
		assertEpsilonEquals(474.789, this.shape.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void toBoundingBox(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		B box = this.shape.toBoundingBox();
		assertNotSame(this.shape, box);
		assertEpsilonEquals(5, box.getMinX());
		assertEpsilonEquals(8, box.getMinY());
		assertEpsilonEquals(10, box.getMaxX());
		assertEpsilonEquals(18, box.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void toBoundingBoxB(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		B box = createAlignedBox(0, 0, 0, 0, 0, 0);
		this.shape.toBoundingBox(box);
		assertEpsilonEquals(5, box.getMinX());
		assertEpsilonEquals(8, box.getMinY());
		assertEpsilonEquals(10, box.getMaxX());
		assertEpsilonEquals(18, box.getMaxY());
	}

}
