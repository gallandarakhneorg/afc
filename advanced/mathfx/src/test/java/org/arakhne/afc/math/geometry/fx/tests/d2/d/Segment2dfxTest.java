/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2026 The original authors and other contributors.
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

package org.arakhne.afc.math.geometry.fx.tests.d2.d;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.d2.tests.afp.AbstractSegment2afpTestCase;
import org.arakhne.afc.math.geometry.fx.d2.d.Rectangle2dfx;
import org.arakhne.afc.math.geometry.fx.d2.d.Segment2dfx;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@DisplayName("Segment2dfx")
@SuppressWarnings("all")
public class Segment2dfxTest extends AbstractSegment2afpTestCase<Segment2dfx, Rectangle2dfx> {

	@Override
	protected TestShapeFactory2dfx createFactory() {
		return TestShapeFactory2dfx.SINGLETON;
	}

	@DisplayName("x1Property")
	@Nested
	public class X1Property {

		private DoubleProperty property;

		public void setUp(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			property = shape.x1Property();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void x1Property_assertion1(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(0, shape.getX1());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void x1Property_assertion2(CoordinateSystem2D cs) {
			setUp(cs);
			assertNotNull(property);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void x1Property_assertion3(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(0, property.get());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void x1Property_assertion4(CoordinateSystem2D cs) {
			setUp(cs);
			shape.setX1(456.159);
			assertEpsilonEquals(456.159, property.get());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void x1Property_assertion5(CoordinateSystem2D cs) {
			setUp(cs);
			shape.setX1(456.159);
			assertEpsilonEquals(456.159, shape.getX1());
		}
	}

	@DisplayName("y1Property")
	@Nested
	public class Y1Property {

		private DoubleProperty property;

		public void setUp(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			property = shape.y1Property();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void y1Property_assertion1(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(0, shape.getY1());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void y1Property_assertion2(CoordinateSystem2D cs) {
			setUp(cs);
			assertNotNull(property);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void y1Property_assertion3(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(0, property.get());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void y1Property_assertion4(CoordinateSystem2D cs) {
			setUp(cs);
			shape.setY1(456.159);
			assertEpsilonEquals(456.159, property.get());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void y1Property_assertion5(CoordinateSystem2D cs) {
			setUp(cs);
			shape.setY1(456.159);
			assertEpsilonEquals(456.159, shape.getY1());
		}
	}

	@DisplayName("x2Property")
	@Nested
	public class X2Property {

		private DoubleProperty property;

		public void setUp(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			property = shape.x2Property();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void x2Property_assertion1(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(1, shape.getX2());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void x2Property_assertion2(CoordinateSystem2D cs) {
			setUp(cs);
			assertNotNull(property);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void x2Property_assertion3(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(1, property.get());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void x2Property_assertion4(CoordinateSystem2D cs) {
			setUp(cs);
			shape.setX2(456.159);
			assertEpsilonEquals(456.159, property.get());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void x2Property_assertion5(CoordinateSystem2D cs) {
			setUp(cs);
			shape.setX2(456.159);
			assertEpsilonEquals(456.159, shape.getX2());
		}
	}

	@DisplayName("y2Property")
	@Nested
	public class Y2Property {

		private DoubleProperty property;

		public void setUp(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			property = shape.y2Property();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void y2Property_assertion1(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(1, shape.getY2());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void y2Property_assertion2(CoordinateSystem2D cs) {
			setUp(cs);
			assertNotNull(property);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void y2Property_assertion3(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(1, property.get());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void y2Property_assertion4(CoordinateSystem2D cs) {
			setUp(cs);
			shape.setY2(456.159);
			assertEpsilonEquals(456.159, property.get());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void y2Property_assertion5(CoordinateSystem2D cs) {
			setUp(cs);
			shape.setY2(456.159);
			assertEpsilonEquals(456.159, shape.getY2());
		}
	}

	@DisplayName("boundingBoxProperty")
	@Nested
	public class BoundingBoxProperty {

		private ObjectProperty<Rectangle2dfx> property;
		private Rectangle2dfx box;

		public void setUp(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			property = shape.boundingBoxProperty();
			box = property.get();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void boundingBoxProperty_assertion1(CoordinateSystem2D cs) {
			setUp(cs);
			assertNotNull(property);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void boundingBoxProperty_assertion2(CoordinateSystem2D cs) {
			setUp(cs);
			assertNotNull(box);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void boundingBoxProperty_assertion3(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(0, box.getMinX());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void boundingBoxProperty_assertion4(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(0, box.getMinY());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void boundingBoxProperty_assertion5(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(1, box.getMaxX());
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void boundingBoxProperty_assertion6(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(1, box.getMaxY());
		}
	}
}
