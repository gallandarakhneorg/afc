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
import javafx.beans.property.ReadOnlyDoubleProperty;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.d2.tests.afp.AbstractRectangle2afpTestCase;
import org.arakhne.afc.math.geometry.fx.d2.d.Rectangle2dfx;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@DisplayName("Rectangle2dfx")
@SuppressWarnings("all")
public class Rectangle2dfxTest extends AbstractRectangle2afpTestCase<Rectangle2dfx, Rectangle2dfx> {

	@Override
	protected TestShapeFactory2dfx createFactory() {
		return TestShapeFactory2dfx.SINGLETON;
	}

	@DisplayName("minXProperty")
	@Nested
	public class MinXProperty {

		private DoubleProperty property;

		public void setUp(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			property = shape.minXProperty();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void minXProperty_assertion1(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(5, shape.getMinX());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void minXProperty_assertion2(CoordinateSystem2D cs) {
			setUp(cs);
			assertNotNull(property);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void minXProperty_assertion3(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(5, property.get());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void minXProperty_assertion4(CoordinateSystem2D cs) {
			setUp(cs);
			shape.setMinX(456.159);
			assertEpsilonEquals(456.159, property.get());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void minXProperty_assertion5(CoordinateSystem2D cs) {
			setUp(cs);
			shape.setMinX(456.159);
			assertEpsilonEquals(456.159, shape.getMaxX());
		}
	}

	@DisplayName("maxXProperty")
	@Nested
	public class MaxXProperty {

		private DoubleProperty property;

		public void setUp(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			property = shape.maxXProperty();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void maxXProperty_assertion1(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(10, shape.getMaxX());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void maxXProperty_assertion2(CoordinateSystem2D cs) {
			setUp(cs);
			assertNotNull(property);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void maxXProperty_assertion3(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(10, property.get());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void maxXProperty_assertion4(CoordinateSystem2D cs) {
			setUp(cs);
			shape.setMaxX(456.159);
			assertEpsilonEquals(456.159, property.get());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void maxXProperty_assertion5(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(5, shape.getMinX());
		}
	}

	@DisplayName("minYProperty")
	@Nested
	public class MinYProperty {

		private DoubleProperty property;

		public void setUp(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			property = shape.minYProperty();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void minYProperty_assertion1(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(8, shape.getMinY());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void minYProperty_assertion2(CoordinateSystem2D cs) {
			setUp(cs);
			assertNotNull(property);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void minYProperty_assertion3(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(8, property.get());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void minYProperty_assertion4(CoordinateSystem2D cs) {
			setUp(cs);
			shape.setMinY(456.159);
			assertEpsilonEquals(456.159, property.get());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void minYProperty_assertion5(CoordinateSystem2D cs) {
			setUp(cs);
			shape.setMinY(456.159);
			assertEpsilonEquals(456.159, shape.getMaxY());
		}
	}

	@DisplayName("maxYProperty")
	@Nested
	public class MaxYProperty {

		private DoubleProperty property;

		public void setUp(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			property = shape.maxYProperty();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void maxYProperty_assertion1(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(18, shape.getMaxY());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void maxYProperty_assertion2(CoordinateSystem2D cs) {
			setUp(cs);
			assertNotNull(property);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void maxYProperty_assertion3(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(18, property.get());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void maxYProperty_assertion4(CoordinateSystem2D cs) {
			setUp(cs);
			shape.setMaxY(456.159);
			assertEpsilonEquals(456.159, property.get());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void maxYProperty_assertion5(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(8, shape.getMinY());
		}
	}

	@DisplayName("widthProperty")
	@Nested
	public class WidthProperty {

		private ReadOnlyDoubleProperty property;

		public void setUp(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			property = shape.widthProperty();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void widthProperty_assertion1(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(5, shape.getWidth());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void widthProperty_assertion2(CoordinateSystem2D cs) {
			setUp(cs);
			assertNotNull(property);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void widthProperty_assertion3(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(5, property.get());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void widthProperty_assertion4(CoordinateSystem2D cs) {
			setUp(cs);
			shape.setMinX(7);
			assertEpsilonEquals(3, property.get());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void widthProperty_assertion5(CoordinateSystem2D cs) {
			setUp(cs);
			shape.setMinX(-5);
			assertEpsilonEquals(15, property.get());
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void widthProperty_assertion6(CoordinateSystem2D cs) {
			setUp(cs);
			shape.setMinX(-5);
			shape.setMaxX(0);
			assertEpsilonEquals(5, property.get());
		}
	}

	@DisplayName("heightProperty")
	@Nested
	public class HeightProperty {

		private ReadOnlyDoubleProperty property;

		public void setUp(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			property = shape.heightProperty();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void heightProperty_assertion1(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(10, shape.getHeight());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void heightProperty_assertion2(CoordinateSystem2D cs) {
			setUp(cs);
			assertNotNull(property);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void heightProperty_assertion3(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(10, property.get());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void heightProperty_assertion4(CoordinateSystem2D cs) {
			setUp(cs);
			shape.setMinY(9);
			assertEpsilonEquals(9, property.get());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void heightProperty_assertion5(CoordinateSystem2D cs) {
			setUp(cs);
			shape.setMinY(-5);
			assertEpsilonEquals(23, property.get());
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void heightProperty_assertion6(CoordinateSystem2D cs) {
			setUp(cs);
			shape.setMinY(-5);
			shape.setMaxY(0);
			assertEpsilonEquals(5, property.get());
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
			assertEpsilonEquals(5, box.getMinX());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void boundingBoxProperty_assertion4(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(8, box.getMinY());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void boundingBoxProperty_assertion5(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(10, box.getMaxX());
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void boundingBoxProperty_assertion6(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(18, box.getMaxY());
		}
	}
}
