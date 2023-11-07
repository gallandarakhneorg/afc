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
import org.arakhne.afc.math.test.geometry.d3.ai.AbstractSegment3aiTest;

@SuppressWarnings("all")
@Disabled("temporary")
public class Segment3ifxTest extends AbstractSegment3aiTest<Segment3ifx, AlignedBox3ifx> {

	@Override
	protected TestShapeFactory3ifx createFactory() {
		return TestShapeFactory3ifx.SINGLETON;
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void x1Property(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		IntegerProperty property = this.shape.x1Property();
		assertNotNull(property);
		IntegerProperty property2 = this.shape.x1Property();
		assertSame(property, property2);
		assertEquals(0, property.get());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void y1Property(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		IntegerProperty property = this.shape.y1Property();
		assertNotNull(property);
		IntegerProperty property2 = this.shape.y1Property();
		assertSame(property, property2);
		assertEquals(0, property.get());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void x2Property(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		IntegerProperty property = this.shape.x2Property();
		assertNotNull(property);
		IntegerProperty property2 = this.shape.x2Property();
		assertSame(property, property2);
		assertEquals(10, property.get());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void y2Property(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		IntegerProperty property = this.shape.y2Property();
		assertNotNull(property);
		IntegerProperty property2 = this.shape.y2Property();
		assertSame(property, property2);
		assertEquals(5, property.get());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void x1PropertySetter(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals(0, this.shape.getX1());
		assertEquals(0, this.shape.getY1());
		assertEquals(10, this.shape.getX2());
		assertEquals(5, this.shape.getY2());
		IntegerProperty property = this.shape.x1Property();
		property.set(345);
		assertEquals(345, this.shape.getX1());
		assertEquals(0, this.shape.getY1());
		assertEquals(10, this.shape.getX2());
		assertEquals(5, this.shape.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void y1PropertySetter(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals(0, this.shape.getX1());
		assertEquals(0, this.shape.getY1());
		assertEquals(10, this.shape.getX2());
		assertEquals(5, this.shape.getY2());
		IntegerProperty property = this.shape.y1Property();
		property.set(345);
		assertEquals(0, this.shape.getX1());
		assertEquals(345, this.shape.getY1());
		assertEquals(10, this.shape.getX2());
		assertEquals(5, this.shape.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void x2PropertySetter(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals(0, this.shape.getX1());
		assertEquals(0, this.shape.getY1());
		assertEquals(10, this.shape.getX2());
		assertEquals(5, this.shape.getY2());
		IntegerProperty property = this.shape.x2Property();
		property.set(345);
		assertEquals(0, this.shape.getX1());
		assertEquals(0, this.shape.getY1());
		assertEquals(345, this.shape.getX2());
		assertEquals(5, this.shape.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void y2PropertySetter(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals(0, this.shape.getX1());
		assertEquals(0, this.shape.getY1());
		assertEquals(10, this.shape.getX2());
		assertEquals(5, this.shape.getY2());
		IntegerProperty property = this.shape.y2Property();
		property.set(345);
		assertEquals(0, this.shape.getX1());
		assertEquals(0, this.shape.getY1());
		assertEquals(10, this.shape.getX2());
		assertEquals(345, this.shape.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void testClone(CoordinateSystem3D cs) {
		super.testClone(cs);
		Segment3ifx clone = this.shape.clone();
		assertNotSame(this.shape.x1Property(), clone.x1Property());
		assertNotSame(this.shape.y1Property(), clone.y1Property());
		assertNotSame(this.shape.x2Property(), clone.x2Property());
		assertNotSame(this.shape.y2Property(), clone.y2Property());
	}

}
