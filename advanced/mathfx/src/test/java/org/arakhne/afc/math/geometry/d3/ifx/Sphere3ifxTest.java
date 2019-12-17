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

package org.arakhne.afc.math.geometry.d3.ifx;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import javafx.beans.property.IntegerProperty;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.test.geometry.d3.ai.AbstractSphere3aiTest;

@SuppressWarnings("all")
@Disabled("temporary")
public class Sphere3ifxTest extends AbstractSphere3aiTest<Sphere3ifx, RectangularPrism3ifx> {

	@Override
	protected TestShapeFactory3ifx createFactory() {
		return TestShapeFactory3ifx.SINGLETON;
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void xProperty(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		IntegerProperty property = this.shape.xProperty();
		assertNotNull(property);
		IntegerProperty property2 = this.shape.xProperty();
		assertSame(property, property2);
		assertEquals(5, property.get());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void yProperty(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    IntegerProperty property = this.shape.yProperty();
	    assertNotNull(property);
	    IntegerProperty property2 = this.shape.yProperty();
	    assertSame(property, property2);
	    assertEquals(8, property.get());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void zProperty(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		IntegerProperty property = this.shape.zProperty();
		assertNotNull(property);
		IntegerProperty property2 = this.shape.zProperty();
		assertSame(property, property2);
		assertEquals(0, property.get());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void radiusProperty(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		IntegerProperty property = this.shape.radiusProperty();
		assertNotNull(property);
		IntegerProperty property2 = this.shape.radiusProperty();
		assertSame(property, property2);
		assertEquals(5, property.get());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void xPropertySetter(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals(5, this.shape.getX());
		assertEquals(8, this.shape.getY());
		assertEquals(0, this.shape.getZ());
		assertEquals(5, this.shape.getRadius());
		IntegerProperty property = this.shape.xProperty();
		property.set(345);
		assertEquals(345, this.shape.getX());
		assertEquals(8, this.shape.getY());
		assertEquals(0, this.shape.getZ());
		assertEquals(5, this.shape.getRadius());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void yPropertySetter(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    assertEquals(5, this.shape.getX());
	    assertEquals(8, this.shape.getY());
	    assertEquals(0, this.shape.getZ());
	    assertEquals(5, this.shape.getRadius());
	    IntegerProperty property = this.shape.yProperty();
	    property.set(345);
	    assertEquals(5, this.shape.getX());
	    assertEquals(345, this.shape.getY());
	    assertEquals(0, this.shape.getZ());
	    assertEquals(5, this.shape.getRadius());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void zPropertySetter(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals(5, this.shape.getX());
		assertEquals(8, this.shape.getY());
		assertEquals(0, this.shape.getZ());
		assertEquals(5, this.shape.getRadius());
		IntegerProperty property = this.shape.zProperty();
		property.set(345);
		assertEquals(5, this.shape.getX());
		assertEquals(8, this.shape.getY());
		assertEquals(345, this.shape.getZ());
		assertEquals(5, this.shape.getRadius());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void radiusPropertySetter(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals(5, this.shape.getX());
		assertEquals(8, this.shape.getY());
		assertEquals(0, this.shape.getZ());
		assertEquals(5, this.shape.getRadius());
		IntegerProperty property = this.shape.radiusProperty();
		property.set(345);
		assertEquals(5, this.shape.getX());
		assertEquals(8, this.shape.getY());
		assertEquals(0, this.shape.getZ());
		assertEquals(345, this.shape.getRadius());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void testClone(CoordinateSystem3D cs) {
		super.testClone(cs);
		Sphere3ifx clone = this.shape.clone();
		assertNotSame(this.shape.xProperty(), clone.xProperty());
		assertNotSame(this.shape.yProperty(), clone.yProperty());
		assertNotSame(this.shape.zProperty(), clone.zProperty());
		assertNotSame(this.shape.radiusProperty(), clone.radiusProperty());
	}

}
