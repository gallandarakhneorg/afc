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
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.d2.tests.ai.AbstractCircle2aiTestCase;
import org.arakhne.afc.math.geometry.fx.d2.i.Circle2ifx;
import org.arakhne.afc.math.geometry.fx.d2.i.Rectangle2ifx;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@DisplayName("Circle2ifx")
@SuppressWarnings("all")
public class Circle2ifxTest extends AbstractCircle2aiTestCase<Circle2ifx, Rectangle2ifx> {

	@Override
	protected TestShapeFactory2ifx createFactory() {
		return TestShapeFactory2ifx.SINGLETON;
	}
	
	@DisplayName("xProperty")
	@Nested
	public class XProperty {

		@DisplayName("get")
		@Nested
		public class Get {

			private IntegerProperty property;
			private IntegerProperty property2;

			public void setUp(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				property = shape.xProperty();
				property2 = shape.xProperty();
			}

			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void xProperty_assertion1(CoordinateSystem2D cs) {
				setUp(cs);
				assertNotNull(property);
			}

			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void xProperty_assertion2(CoordinateSystem2D cs) {
				setUp(cs);
				assertSame(property, property2);
			}

			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void xProperty_assertion3(CoordinateSystem2D cs) {
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
				property = shape.xProperty();
			}

			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void xPropertySetter_assertion1(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(5, shape.getX());
			}

			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void xPropertySetter_assertion2(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(8, shape.getY());
			}

			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void xPropertySetter_assertion3(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(5, shape.getRadius());
			}

			@DisplayName("#4")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void xPropertySetter_assertion4(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345);
				assertEquals(345, shape.getX());
			}

			@DisplayName("#5")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void xPropertySetter_assertion5(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345);
				assertEquals(8, shape.getY());
			}

			@DisplayName("#6")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void xPropertySetter_assertion6(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345);
				assertEquals(5, shape.getRadius());
			}
		}
	}

	@DisplayName("yProperty")
	@Nested
	public class YProperty {

		@DisplayName("get")
		@Nested
		public class Get {

			private IntegerProperty property;
			private IntegerProperty property2;

			public void setUp(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				property = shape.yProperty();
				property2 = shape.yProperty();
			}

			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void yProperty_assertion1(CoordinateSystem2D cs) {
				setUp(cs);
				assertNotNull(property);
			}

			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void yProperty_assertion2(CoordinateSystem2D cs) {
				setUp(cs);
				assertSame(property, property2);
			}

			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void yProperty_assertion3(CoordinateSystem2D cs) {
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
				property = shape.yProperty();
			}

			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void yPropertySetter_assertion1(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(5, shape.getX());
			}

			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void yPropertySetter_assertion2(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(8, shape.getY());
			}

			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void yPropertySetter_assertion3(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(5, shape.getRadius());
			}

			@DisplayName("#4")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void yPropertySetter_assertion4(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345);
				assertEquals(5, shape.getX());
			}

			@DisplayName("#5")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void yPropertySetter_assertion5(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345);
				assertEquals(345, shape.getY());
			}

			@DisplayName("#6")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void yPropertySetter_assertion6(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345);
				assertEquals(5, shape.getRadius());
			}
		}
	}

	@DisplayName("radiusProperty")
	@Nested
	public class RadiusProperty {

		@DisplayName("get")
		@Nested
		public class Get {

			private IntegerProperty property;
			private IntegerProperty property2;

			public void setUp(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				property = shape.radiusProperty();
				property2 = shape.radiusProperty();
			}

			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void radiusProperty_assertion1(CoordinateSystem2D cs) {
				setUp(cs);
				assertNotNull(property);
			}

			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void radiusProperty_assertion2(CoordinateSystem2D cs) {
				setUp(cs);
				assertSame(property, property2);
			}

			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void radiusProperty_assertion3(CoordinateSystem2D cs) {
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
				property = shape.radiusProperty();
			}

			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void radiusPropertySetter_assertion1(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(5, shape.getX());
			}

			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void radiusPropertySetter_assertion2(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(8, shape.getY());
			}

			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void radiusPropertySetter_assertion3(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(5, shape.getRadius());
			}

			@DisplayName("#4")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void radiusPropertySetter_assertion4(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345);
				assertEquals(5, shape.getX());
			}

			@DisplayName("#5")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void radiusPropertySetter_assertion5(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345);
				assertEquals(8, shape.getY());
			}

			@DisplayName("#6")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void radiusPropertySetter_assertion6(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345);
				assertEquals(345, shape.getRadius());
			}
		}
	}

	@DisplayName("clone")
	@Nested
	public class CloneTest {

		private Circle2ifx clone;

		public void setUp(CoordinateSystem2D cs) {
			clone = shape.clone();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void testClone_assertion1(CoordinateSystem2D cs) {
			setUp(cs);
			assertNotSame(shape.xProperty(), clone.xProperty());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void testClone_assertion2(CoordinateSystem2D cs) {
			setUp(cs);
			assertNotSame(shape.yProperty(), clone.yProperty());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void testClone_assertion3(CoordinateSystem2D cs) {
			setUp(cs);
			assertNotSame(shape.radiusProperty(), clone.radiusProperty());
		}
	}
}
