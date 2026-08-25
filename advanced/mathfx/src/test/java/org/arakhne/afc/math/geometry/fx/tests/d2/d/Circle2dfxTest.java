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
import org.arakhne.afc.math.geometry.d2.tests.afp.AbstractCircle2afpTestCase;
import org.arakhne.afc.math.geometry.fx.d2.d.Circle2dfx;
import org.arakhne.afc.math.geometry.fx.d2.d.Rectangle2dfx;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@DisplayName("Circle2dfx")
@SuppressWarnings("all")
public class Circle2dfxTest extends AbstractCircle2afpTestCase<Circle2dfx, Rectangle2dfx> {

	@Override
	protected TestShapeFactory2dfx createFactory() {
		return TestShapeFactory2dfx.SINGLETON;
	}

	@DisplayName("xProperty")
	@Nested
	public class XProperty {

		private DoubleProperty property;

		public void setUp(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			property = shape.xProperty();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void xProperty_assertion1(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(5, shape.getX());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void xProperty_assertion2(CoordinateSystem2D cs) {
			setUp(cs);
			assertNotNull(property);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void xProperty_assertion3(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(5, property.get());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void xProperty_assertion4(CoordinateSystem2D cs) {
			setUp(cs);
			shape.setX(456.159);
			assertEpsilonEquals(456.159, property.get());
		}
	}

	@DisplayName("yProperty")
	@Nested
	public class YProperty {

		private DoubleProperty property;

		public void setUp(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			property = shape.yProperty();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void yProperty_assertion1(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(8, shape.getY());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void yProperty_assertion2(CoordinateSystem2D cs) {
			setUp(cs);
			assertNotNull(property);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void yProperty_assertion3(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(8, property.get());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void yProperty_assertion4(CoordinateSystem2D cs) {
			setUp(cs);
			shape.setY(456.159);
			assertEpsilonEquals(456.159, property.get());
		}
	}

	@DisplayName("radiusProperty")
	@Nested
	public class RadiusProperty {

		private DoubleProperty property;

		public void setUp(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			property = shape.radiusProperty();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void radiusProperty_assertion1(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(5, shape.getRadius());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void radiusProperty_assertion2(CoordinateSystem2D cs) {
			setUp(cs);
			assertNotNull(property);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void radiusProperty_assertion3(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(5, property.get());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void radiusProperty_assertion4(CoordinateSystem2D cs) {
			setUp(cs);
			shape.setRadius(456.159);
			assertEpsilonEquals(456.159, property.get());
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
			assertEpsilonEquals(3, box.getMinY());
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
			assertEpsilonEquals(13, box.getMaxY());
		}
	}
}
