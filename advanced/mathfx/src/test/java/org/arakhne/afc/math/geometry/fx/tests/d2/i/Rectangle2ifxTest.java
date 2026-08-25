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

package org.arakhne.afc.math.geometry.fx.tests.d2.i;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.d2.tests.ai.AbstractRectangle2aiTestCase;
import org.arakhne.afc.math.geometry.fx.d2.i.Rectangle2ifx;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@DisplayName("Rectangle2ifx")
@SuppressWarnings("all")
public class Rectangle2ifxTest extends AbstractRectangle2aiTestCase<Rectangle2ifx> {

	@Override
	protected TestShapeFactory2ifx createFactory() {
		return TestShapeFactory2ifx.SINGLETON;
	}

	@DisplayName("minXProperty")
	@Nested
	public class MinXProperty {

		@DisplayName("get")
		@Nested
		public class Get {

			private IntegerProperty property;
			private IntegerProperty property2;

			public void setUp(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				property = shape.minXProperty();
				property2 = shape.minXProperty();
			}

			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void minXProperty_assertion1(CoordinateSystem2D cs) {
				setUp(cs);
				assertNotNull(property);
			}

			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void minXProperty_assertion2(CoordinateSystem2D cs) {
				setUp(cs);
				assertSame(property, property2);
			}

			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void minXProperty_assertion3(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(5, property.get());
			}
		}

		@DisplayName("set")
		@Nested
		public class Set {

			private IntegerProperty property;

			public void setUp(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				property = shape.minXProperty();
			}

			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void minXPropertySetter_assertion1(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(5, shape.getMinX());
			}

			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void minXPropertySetter_assertion2(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(8, shape.getMinY());
			}

			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void minXPropertySetter_assertion3(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(15, shape.getMaxX());
			}

			@DisplayName("#4")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void minXPropertySetter_assertion4(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(13, shape.getMaxY());
			}

			@DisplayName("#5")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void minXPropertySetter_assertion5(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345);
				assertEquals(345, shape.getMinX());
			}

			@DisplayName("#6")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void minXPropertySetter_assertion6(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345);
				assertEquals(8, shape.getMinY());
			}

			@DisplayName("#7")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void minXPropertySetter_assertion7(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345);
				assertEquals(345, shape.getMaxX());
			}

			@DisplayName("#8")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void minXPropertySetter_assertion8(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345);
				assertEquals(13, shape.getMaxY());
			}
		}
	}

	@DisplayName("minYProperty")
	@Nested
	public class MinYProperty {

		@DisplayName("get")
		@Nested
		public class Get {

			private IntegerProperty property;
			private IntegerProperty property2;

			public void setUp(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				property = shape.minYProperty();
				property2 = shape.minYProperty();
			}

			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void minYProperty_assertion1(CoordinateSystem2D cs) {
				setUp(cs);
				assertNotNull(property);
			}

			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void minYProperty_assertion2(CoordinateSystem2D cs) {
				setUp(cs);
				assertSame(property, property2);
			}

			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void minYProperty_assertion3(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(8, property.get());
			}
		}

		@DisplayName("set")
		@Nested
		public class Set {

			private IntegerProperty property;

			public void setUp(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				property = shape.minYProperty();
			}

			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void minYPropertySetter_assertion1(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(5, shape.getMinX());
			}

			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void minYPropertySetter_assertion2(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(8, shape.getMinY());
			}

			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void minYPropertySetter_assertion3(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(15, shape.getMaxX());
			}

			@DisplayName("#4")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void minYPropertySetter_assertion4(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(13, shape.getMaxY());
			}

			@DisplayName("#5")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void minYPropertySetter_assertion5(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345);
				assertEquals(5, shape.getMinX());
			}

			@DisplayName("#6")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void minYPropertySetter_assertion6(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345);
				assertEquals(345, shape.getMinY());
			}

			@DisplayName("#7")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void minYPropertySetter_assertion7(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345);
				assertEquals(15, shape.getMaxX());
			}

			@DisplayName("#8")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void minYPropertySetter_assertion8(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345);
				assertEquals(345, shape.getMaxY());
			}
		}
	}

	@DisplayName("maxXProperty")
	@Nested
	public class MaxXProperty {

		@DisplayName("get")
		@Nested
		public class Get {

			private IntegerProperty property;
			private IntegerProperty property2;

			public void setUp(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				property = shape.maxXProperty();
				property2 = shape.maxXProperty();
			}

			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void maxXProperty_assertion1(CoordinateSystem2D cs) {
				setUp(cs);
				assertNotNull(property);
			}

			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void maxXProperty_assertion2(CoordinateSystem2D cs) {
				setUp(cs);
				assertSame(property, property2);
			}

			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void maxXProperty_assertion3(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(15, property.get());
			}
		}

		@DisplayName("set")
		@Nested
		public class Set {

			private IntegerProperty property;

			public void setUp(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				property = shape.maxXProperty();
			}

			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void maxXPropertySetter_assertion1(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(5, shape.getMinX());
			}

			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void maxXPropertySetter_assertion2(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(8, shape.getMinY());
			}

			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void maxXPropertySetter_assertion3(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(15, shape.getMaxX());
			}

			@DisplayName("#4")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void maxXPropertySetter_assertion4(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(13, shape.getMaxY());
			}

			@DisplayName("#5")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void maxXPropertySetter_assertion5(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345);
				assertEquals(5, shape.getMinX());
			}

			@DisplayName("#6")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void maxXPropertySetter_assertion6(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345);
				assertEquals(8, shape.getMinY());
			}

			@DisplayName("#7")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void maxXPropertySetter_assertion7(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345);
				assertEquals(345, shape.getMaxX());
			}

			@DisplayName("#8")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void maxXPropertySetter_assertion8(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345);
				assertEquals(13, shape.getMaxY());
			}
		}
	}

	@DisplayName("maxYProperty")
	@Nested
	public class MaxYProperty {

		@DisplayName("get")
		@Nested
		public class Get {

			private IntegerProperty property;
			private IntegerProperty property2;

			public void setUp(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				property = shape.maxYProperty();
				property2 = shape.maxYProperty();
			}

			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void maxYProperty_assertion1(CoordinateSystem2D cs) {
				setUp(cs);
				assertNotNull(property);
			}

			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void maxYProperty_assertion2(CoordinateSystem2D cs) {
				setUp(cs);
				assertSame(property, property2);
			}

			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void maxYProperty_assertion3(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(13, property.get());
			}
		}

		@DisplayName("set")
		@Nested
		public class Set {

			private IntegerProperty property;

			public void setUp(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				property = shape.maxYProperty();
			}

			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void maxYPropertySetter_assertion1(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(5, shape.getMinX());
			}

			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void maxYPropertySetter_assertion2(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(8, shape.getMinY());
			}

			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void maxYPropertySetter_assertion3(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(15, shape.getMaxX());
			}

			@DisplayName("#4")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void maxYPropertySetter_assertion4(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(13, shape.getMaxY());
			}

			@DisplayName("#5")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void maxYPropertySetter_assertion5(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345);
				assertEquals(5, shape.getMinX());
			}

			@DisplayName("#6")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void maxYPropertySetter_assertion6(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345);
				assertEquals(8, shape.getMinY());
			}

			@DisplayName("#7")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void maxYPropertySetter_assertion7(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345);
				assertEquals(15, shape.getMaxX());
			}

			@DisplayName("#8")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void maxYPropertySetter_assertion8(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345);
				assertEquals(345, shape.getMaxY());
			}
		}
	}

	@DisplayName("clone")
	@Nested
	public class CloneTest {

		private Rectangle2ifx clone;

		public void setUp(CoordinateSystem2D cs) {
			clone = shape.clone();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void testClone_assertion1(CoordinateSystem2D cs) {
			setUp(cs);
			assertNotSame(shape.minXProperty(), clone.minXProperty());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void testClone_assertion2(CoordinateSystem2D cs) {
			setUp(cs);
			assertNotSame(shape.maxXProperty(), clone.maxXProperty());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void testClone_assertion3(CoordinateSystem2D cs) {
			setUp(cs);
			assertNotSame(shape.minYProperty(), clone.minYProperty());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void testClone_assertion4(CoordinateSystem2D cs) {
			setUp(cs);
			assertNotSame(shape.maxYProperty(), clone.maxYProperty());
		}
	}

	@DisplayName("widthProperty")
	@Nested
	public class WidthProperty {

		private ReadOnlyIntegerProperty property;

		public void setUp(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			property = shape.widthProperty();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void widthProperty_assertion1(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(10, shape.getWidth());
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
			assertEpsilonEquals(10, property.get());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void widthProperty_assertion4(CoordinateSystem2D cs) {
			setUp(cs);
			shape.setMinX(7);
			assertEpsilonEquals(8, property.get());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void widthProperty_assertion5(CoordinateSystem2D cs) {
			setUp(cs);
			shape.setMinX(-5);
			assertEpsilonEquals(20, property.get());
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

		private ReadOnlyIntegerProperty property;

		public void setUp(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			property = shape.heightProperty();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void heightProperty_assertion1(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(5, shape.getHeight());
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
			assertEpsilonEquals(5, property.get());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void heightProperty_assertion4(CoordinateSystem2D cs) {
			setUp(cs);
			shape.setMinY(9);
			assertEpsilonEquals(4, property.get());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void heightProperty_assertion5(CoordinateSystem2D cs) {
			setUp(cs);
			shape.setMinY(-5);
			assertEpsilonEquals(18, property.get());
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
}
