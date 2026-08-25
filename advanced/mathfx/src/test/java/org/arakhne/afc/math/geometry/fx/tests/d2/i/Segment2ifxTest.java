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
import org.arakhne.afc.math.geometry.d2.tests.ai.AbstractSegment2aiTestCase;
import org.arakhne.afc.math.geometry.fx.d2.i.Rectangle2ifx;
import org.arakhne.afc.math.geometry.fx.d2.i.Segment2ifx;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@DisplayName("Segment2ifx")
@SuppressWarnings("all")
public class Segment2ifxTest extends AbstractSegment2aiTestCase<Segment2ifx, Rectangle2ifx> {

	@Override
	protected TestShapeFactory2ifx createFactory() {
		return TestShapeFactory2ifx.SINGLETON;
	}

	@DisplayName("x1Property")
	@Nested
	public class X1Property {

		@DisplayName("get")
		@Nested
		public class Get {

			private IntegerProperty property;
			private IntegerProperty property2;

			public void setUp(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				property = shape.x1Property();
				property2 = shape.x1Property();
			}

			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void x1Property_assertion1(CoordinateSystem2D cs) {
				setUp(cs);
				assertNotNull(property);
			}

			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void x1Property_assertion2(CoordinateSystem2D cs) {
				setUp(cs);
				assertSame(property, property2);
			}

			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void x1Property_assertion3(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(0, property.get());
			}
		}

		@DisplayName("set")
		@Nested
		public class Set {

			private IntegerProperty property;

			public void setUp(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				property = shape.x1Property();
			}

			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void x1PropertySetter_assertion1(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(0, shape.getX1());
			}

			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void x1PropertySetter_assertion2(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(0, shape.getY1());
			}

			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void x1PropertySetter_assertion3(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(10, shape.getX2());
			}

			@DisplayName("#4")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void x1PropertySetter_assertion4(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(5, shape.getY2());
			}

			@DisplayName("#5")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void x1PropertySetter_assertion5(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345);
				assertEquals(345, shape.getX1());
			}

			@DisplayName("#6")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void x1PropertySetter_assertion6(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345);
				assertEquals(0, shape.getY1());
			}

			@DisplayName("#7")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void x1PropertySetter_assertion7(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345);
				assertEquals(10, shape.getX2());
			}

			@DisplayName("#8")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void x1PropertySetter_assertion8(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345);
				assertEquals(5, shape.getY2());
			}
		}
	}

	@DisplayName("y1Property")
	@Nested
	public class Y1Property {

		@DisplayName("get")
		@Nested
		public class Get {

			private IntegerProperty property;
			private IntegerProperty property2;

			public void setUp(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				property = shape.y1Property();
				property2 = shape.y1Property();
			}

			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void y1Property_assertion1(CoordinateSystem2D cs) {
				setUp(cs);
				assertNotNull(property);
			}

			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void y1Property_assertion2(CoordinateSystem2D cs) {
				setUp(cs);
				assertSame(property, property2);
			}

			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void y1Property_assertion3(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(0, property.get());
			}
		}

		@DisplayName("set")
		@Nested
		public class Set {

			private IntegerProperty property;

			public void setUp(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				property = shape.y1Property();
			}

			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void y1PropertySetter_assertion1(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(0, shape.getX1());
			}

			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void y1PropertySetter_assertion2(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(0, shape.getY1());
			}

			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void y1PropertySetter_assertion3(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(10, shape.getX2());
			}

			@DisplayName("#4")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void y1PropertySetter_assertion4(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(5, shape.getY2());
			}

			@DisplayName("#5")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void y1PropertySetter_assertion5(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345);
				assertEquals(0, shape.getX1());
			}

			@DisplayName("#6")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void y1PropertySetter_assertion6(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345);
				assertEquals(345, shape.getY1());
			}

			@DisplayName("#7")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void y1PropertySetter_assertion7(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345);
				assertEquals(10, shape.getX2());
			}

			@DisplayName("#8")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void y1PropertySetter_assertion8(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345);
				assertEquals(5, shape.getY2());
			}
		}
	}

	@DisplayName("x2Property")
	@Nested
	public class X2Property {

		@DisplayName("get")
		@Nested
		public class Get {

			private IntegerProperty property;
			private IntegerProperty property2;

			public void setUp(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				property = shape.x2Property();
				property2 = shape.x2Property();
			}

			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void x2Property_assertion1(CoordinateSystem2D cs) {
				setUp(cs);
				assertNotNull(property);
			}

			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void x2Property_assertion2(CoordinateSystem2D cs) {
				setUp(cs);
				assertSame(property, property2);
			}

			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void x2Property_assertion3(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(10, property.get());
			}
		}

		@DisplayName("set")
		@Nested
		public class Set {

			private IntegerProperty property;

			public void setUp(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				property = shape.x2Property();
			}

			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void x2PropertySetter_assertion1(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(0, shape.getX1());
			}

			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void x2PropertySetter_assertion2(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(0, shape.getY1());
			}

			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void x2PropertySetter_assertion3(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(10, shape.getX2());
			}

			@DisplayName("#4")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void x2PropertySetter_assertion4(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(5, shape.getY2());
			}

			@DisplayName("#5")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void x2PropertySetter_assertion5(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345);
				assertEquals(0, shape.getX1());
			}

			@DisplayName("#6")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void x2PropertySetter_assertion6(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345);
				assertEquals(0, shape.getY1());
			}

			@DisplayName("#7")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void x2PropertySetter_assertion7(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345);
				assertEquals(345, shape.getX2());
			}

			@DisplayName("#8")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void x2PropertySetter_assertion8(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345);
				assertEquals(5, shape.getY2());
			}
		}
	}

	@DisplayName("y2Property")
	@Nested
	public class Y2Property {

		@DisplayName("get")
		@Nested
		public class Get {

			private IntegerProperty property;
			private IntegerProperty property2;

			public void setUp(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				property = shape.y2Property();
				property2 = shape.y2Property();
			}

			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void y2Property_assertion1(CoordinateSystem2D cs) {
				setUp(cs);
				assertNotNull(property);
			}

			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void y2Property_assertion2(CoordinateSystem2D cs) {
				setUp(cs);
				assertSame(property, property2);
			}

			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void y2Property_assertion3(CoordinateSystem2D cs) {
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
				property = shape.y2Property();
			}

			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void y2PropertySetter_assertion1(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(0, shape.getX1());
			}

			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void y2PropertySetter_assertion2(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(0, shape.getY1());
			}

			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void y2PropertySetter_assertion3(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(10, shape.getX2());
			}

			@DisplayName("#4")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void y2PropertySetter_assertion4(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(5, shape.getY2());
			}

			@DisplayName("#5")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void y2PropertySetter_assertion5(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345);
				assertEquals(0, shape.getX1());
			}

			@DisplayName("#6")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void y2PropertySetter_assertion6(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345);
				assertEquals(0, shape.getY1());
			}

			@DisplayName("#7")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void y2PropertySetter_assertion7(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345);
				assertEquals(10, shape.getX2());
			}

			@DisplayName("#8")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void y2PropertySetter_assertion8(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345);
				assertEquals(345, shape.getY2());
			}
		}
	}

	@DisplayName("clone")
	@Nested
	public class CloneTest {

		private Segment2ifx clone;

		public void setUp(CoordinateSystem2D cs) {
			clone = shape.clone();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void testClone_assertion1(CoordinateSystem2D cs) {
			setUp(cs);
			assertNotSame(shape.x1Property(), clone.x1Property());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void testClone_assertion2(CoordinateSystem2D cs) {
			setUp(cs);
			assertNotSame(shape.y1Property(), clone.y1Property());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void testClone_assertion3(CoordinateSystem2D cs) {
			setUp(cs);
			assertNotSame(shape.x2Property(), clone.x2Property());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void testClone_assertion4(CoordinateSystem2D cs) {
			setUp(cs);
			assertNotSame(shape.y2Property(), clone.y2Property());
		}
	}
}
