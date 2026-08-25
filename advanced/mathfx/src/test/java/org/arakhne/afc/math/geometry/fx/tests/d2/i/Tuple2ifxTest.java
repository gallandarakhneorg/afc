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
import static org.junit.jupiter.api.Assertions.assertSame;

import javafx.beans.property.IntegerProperty;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.base.tests.AbstractTuple2DTestCase;
import org.arakhne.afc.math.geometry.fx.d2.i.Tuple2ifx;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@DisplayName("Tuple2ifx")
@SuppressWarnings("all")
public class Tuple2ifxTest extends AbstractTuple2DTestCase<Tuple2ifx> {

	@Override
	public boolean isIntCoordinates() {
		return true;
	}
	
	@Override
	public Tuple2ifx createTuple(double x, double y) {
		return new Tuple2ifx(x, y);
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
				property = t.xProperty();
				property2 = t.xProperty();
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
				assertEquals(1, property.get());
			}
		}

		@DisplayName("set")
		@Nested
		public class Set {

			private IntegerProperty property;

			public void setUp(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				property = t.xProperty();
			}

			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void xPropertySetter_assertion1(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(1, t.ix());
			}

			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void xPropertySetter_assertion2(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(-2, t.iy());
			}

			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void xPropertySetter_assertion3(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345);
				assertEquals(345, t.ix());
			}

			@DisplayName("#4")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void xPropertySetter_assertion4(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345);
				assertEquals(-2, t.iy());
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
				property = t.yProperty();
				property2 = t.yProperty();
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
				assertEquals(-2, property.get());
			}
		}

		@DisplayName("set")
		@Nested
		public class Set {

			private IntegerProperty property;

			public void setUp(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				property = t.yProperty();
			}

			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void yPropertySetter_assertion1(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(1, t.ix());
			}

			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void yPropertySetter_assertion2(CoordinateSystem2D cs) {
				setUp(cs);
				assertEquals(-2, t.iy());
			}

			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void yPropertySetter_assertion3(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345);
				assertEquals(1, t.ix());
			}

			@DisplayName("#4")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void yPropertySetter_assertion4(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345);
				assertEquals(345, t.iy());
			}
		}
	}
}
