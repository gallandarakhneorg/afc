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
import static org.junit.jupiter.api.Assertions.assertSame;

import javafx.beans.property.DoubleProperty;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.base.tests.AbstractTuple2DTestCase;
import org.arakhne.afc.math.geometry.fx.d2.d.Tuple2dfx;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@DisplayName("Tuple2dfx")
@SuppressWarnings("all")
public class Tuple2dfxTest extends AbstractTuple2DTestCase<Tuple2dfx> {

	@Override
	public boolean isIntCoordinates() {
		return false;
	}
	
	@Override
	public Tuple2dfx createTuple(double x, double y) {
		return new Tuple2dfx(x, y);
	}

	@DisplayName("xProperty")
	@Nested
	public class XProperty {

		@DisplayName("get")
		@Nested
		public class Get {

			private DoubleProperty property;
			private DoubleProperty property2;

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
				assertEpsilonEquals(1, property.get());
			}
		}

		@DisplayName("Set")
		@Nested
		public class Set {

			private DoubleProperty property;

			public void setUp(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				property = t.xProperty();
			}

			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void xPropertySetter_assertion1(CoordinateSystem2D cs) {
				setUp(cs);
				assertEpsilonEquals(1, t.getX());
			}

			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void xPropertySetter_assertion2(CoordinateSystem2D cs) {
				setUp(cs);
				assertEpsilonEquals(-2, t.getY());
			}

			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void xPropertySetter_assertion3(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345.);
				assertEpsilonEquals(345., t.getX());
			}

			@DisplayName("#4")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void xPropertySetter_assertion4(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345.);
				assertEpsilonEquals(-2, t.getY());
			}
		}
	}

	@DisplayName("yProperty")
	@Nested
	public class YProperty {

		@DisplayName("get")
		@Nested
		public class Get {

			private DoubleProperty property;
			private DoubleProperty property2;

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
				assertEpsilonEquals(-2, property.get());
			}
		}

		@DisplayName("Set")
		@Nested
		public class Set {

			private DoubleProperty property;

			public void setUp(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				property = t.yProperty();
			}

			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void yPropertySetter_assertion1(CoordinateSystem2D cs) {
				setUp(cs);
				assertEpsilonEquals(1, t.getX());
			}

			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void yPropertySetter_assertion2(CoordinateSystem2D cs) {
				setUp(cs);
				assertEpsilonEquals(-2, t.getY());
			}

			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void yPropertySetter_assertion3(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345.);
				assertEpsilonEquals(1, t.getX());
			}

			@DisplayName("#4")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void yPropertySetter_assertion4(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(345.);
				assertEpsilonEquals(345., t.getY());
			}
		}
	}
}
