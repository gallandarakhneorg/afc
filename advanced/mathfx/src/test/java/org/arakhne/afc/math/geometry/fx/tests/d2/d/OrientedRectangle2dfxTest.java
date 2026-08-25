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
import static org.junit.jupiter.api.Assertions.assertThrows;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.d2.tests.afp.AbstractOrientedRectangle2afpTestCase;
import org.arakhne.afc.math.geometry.fx.d2.d.OrientedRectangle2dfx;
import org.arakhne.afc.math.geometry.fx.d2.d.ReadOnlyUnitVectorProperty;
import org.arakhne.afc.math.geometry.fx.d2.d.Rectangle2dfx;
import org.arakhne.afc.math.geometry.fx.d2.d.UnitVectorProperty;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@DisplayName("OrientedRectangle2dfx")
@SuppressWarnings("all")
public class OrientedRectangle2dfxTest extends AbstractOrientedRectangle2afpTestCase<OrientedRectangle2dfx, Rectangle2dfx> {

	@Override
	protected TestShapeFactory2dfx createFactory() {
		return TestShapeFactory2dfx.SINGLETON;
	}

	@DisplayName("centerXProperty")
	@Nested
	public class CenterXProperty {

		private DoubleProperty property;

		public void setUp(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			property = shape.centerXProperty();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void centerXProperty_assertion1(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(cx, shape.getCenterX());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void centerXProperty_assertion2(CoordinateSystem2D cs) {
			setUp(cs);
			assertNotNull(property);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void centerXProperty_assertion3(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(cx, property.get());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void centerXProperty_assertion4(CoordinateSystem2D cs) {
			setUp(cs);
			shape.setCenterX(456.159);
			assertEpsilonEquals(456.159, property.get());
		}
	}

	@DisplayName("centerYProperty")
	@Nested
	public class CenterYProperty {

		private DoubleProperty property;

		public void setUp(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			property = shape.centerYProperty();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void centerYProperty_assertion1(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(cy, shape.getCenterY());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void centerYProperty_assertion2(CoordinateSystem2D cs) {
			setUp(cs);
			assertNotNull(property);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void centerYProperty_assertion3(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(cy, property.get());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void centerYProperty_assertion4(CoordinateSystem2D cs) {
			setUp(cs);
			shape.setCenterY(456.159);
			assertEpsilonEquals(456.159, property.get());
		}
	}

	@DisplayName("firstAxisProperty")
	@Nested
	public class FirstAxisProperty {

		@DisplayName("Set target object")
		@Nested
		public class SetTargetObject {

			private UnitVectorProperty property;

			public void setUp(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				property = shape.firstAxisProperty();
			}

			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void firstAxisProperty_setObject_assertion1(CoordinateSystem2D cs) {
				setUp(cs);
				assertEpsilonEquals(ux, shape.getFirstAxisX());
			}

			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void firstAxisProperty_setObject_assertion2(CoordinateSystem2D cs) {
				setUp(cs);
				assertEpsilonEquals(uy, shape.getFirstAxisY());
			}

			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void firstAxisProperty_setObject_assertion3(CoordinateSystem2D cs) {
				setUp(cs);
				assertNotNull(property);
			}

			@DisplayName("#4")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void firstAxisProperty_setObject_assertion4(CoordinateSystem2D cs) {
				setUp(cs);
				assertEpsilonEquals(ux, property.getX());
			}

			@DisplayName("#5")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void firstAxisProperty_setObject_assertion5(CoordinateSystem2D cs) {
				setUp(cs);
				assertEpsilonEquals(uy, property.getY());
			}

			@DisplayName("#6")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void firstAxisProperty_setObject_assertion6(CoordinateSystem2D cs) {
				setUp(cs);
				shape.setFirstAxis(0.500348, 0.865824);
				assertEpsilonEquals(0.500348, property.getX());
			}

			@DisplayName("#7")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void firstAxisProperty_setObject_assertion7(CoordinateSystem2D cs) {
				setUp(cs);
				shape.setFirstAxis(0.500348, 0.865824);
				assertEpsilonEquals(0.865824, property.getY());
			}
		}

		@DisplayName("Set property")
		@Nested
		public class SetProperty {

			private UnitVectorProperty property;

			public void setUp(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				property = shape.firstAxisProperty();
			}

			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void firstAxisProperty_setProperty_assertion1(CoordinateSystem2D cs) {
				setUp(cs);
				assertEpsilonEquals(ux, shape.getFirstAxisX());
			}

			@DisplayName("#2")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void firstAxisProperty_setProperty_assertion2(CoordinateSystem2D cs) {
				setUp(cs);
				assertEpsilonEquals(uy, shape.getFirstAxisY());
			}

			@DisplayName("#3")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void firstAxisProperty_setProperty_assertion3(CoordinateSystem2D cs) {
				setUp(cs);
				assertNotNull(property);
			}

			@DisplayName("#4")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void firstAxisProperty_setProperty_assertion4(CoordinateSystem2D cs) {
				setUp(cs);
				assertEpsilonEquals(ux, property.getX());
			}

			@DisplayName("#5")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void firstAxisProperty_setProperty_assertion5(CoordinateSystem2D cs) {
				setUp(cs);
				assertEpsilonEquals(uy, property.getY());
			}

			@DisplayName("#6")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void firstAxisProperty_setProperty_assertion6(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(0.500348, 0.865824);
				assertEpsilonEquals(0.500348, property.getX());
			}

			@DisplayName("#7")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void firstAxisProperty_setProperty_assertion7(CoordinateSystem2D cs) {
				setUp(cs);
				property.set(0.500348, 0.865824);
				assertEpsilonEquals(0.865824, property.getY());
			}
		}

		@DisplayName("Set not unit vector")
		@Nested
		public class SetNotUnitVector {

			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@EnumSource(CoordinateSystem2D.class)
			public void firstAxisProperty_setProperty_notUnitVector_assertion1(CoordinateSystem2D cs) {
				CoordinateSystem2D.setDefaultCoordinateSystem(cs);
				assertThrows(AssertionError.class, () -> {
					UnitVectorProperty property = shape.firstAxisProperty();
					property.set(456.159, 789.357);
				});
			}
		}
	}

	@DisplayName("firstAxisExtentProperty")
	@Nested
	public class FirstAxisExtentProperty {

		private DoubleProperty property;

		public void setUp(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			property = shape.firstAxisExtentProperty();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void firstAxisExtentProperty_assertion1(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(e1, shape.getFirstAxisExtent());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void firstAxisExtentProperty_assertion2(CoordinateSystem2D cs) {
			setUp(cs);
			assertNotNull(property);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void firstAxisExtentProperty_assertion3(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(e1, property.get());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void firstAxisExtentProperty_assertion4(CoordinateSystem2D cs) {
			setUp(cs);
			shape.setFirstAxisExtent(456.159);
			assertEpsilonEquals(456.159, property.get());
		}
	}

	@DisplayName("secondAxisProperty")
	@Nested
	public class SecondAxisProperty {

		private ReadOnlyUnitVectorProperty property;

		public void setUp(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			property = shape.secondAxisProperty();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void secondAxisProperty_assertion1(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(vx, shape.getSecondAxisX());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void secondAxisProperty_assertion2(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(vy, shape.getSecondAxisY());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void secondAxisProperty_assertion3(CoordinateSystem2D cs) {
			setUp(cs);
			assertNotNull(property);
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void secondAxisProperty_assertion4(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(vx, property.getX());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void secondAxisProperty_assertion5(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(vy, property.getY());
		}
	}

	@DisplayName("secondAxisExtentProperty")
	@Nested
	public class SecondAxisExtentProperty {

		private DoubleProperty property;

		public void setUp(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			property = shape.secondAxisExtentProperty();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void secondAxisExtentProperty_assertion1(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(e2, shape.getSecondAxisExtent());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void secondAxisExtentProperty_assertion2(CoordinateSystem2D cs) {
			setUp(cs);
			assertNotNull(property);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void secondAxisExtentProperty_assertion3(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(e2, property.get());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void secondAxisExtentProperty_assertion4(CoordinateSystem2D cs) {
			setUp(cs);
			shape.setSecondAxisExtent(456.159);
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
			assertEpsilonEquals(pEx, box.getMinX());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void boundingBoxProperty_assertion4(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(pFy, box.getMinY());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void boundingBoxProperty_assertion5(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(pGx, box.getMaxX());
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void boundingBoxProperty_assertion6(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(pHy, box.getMaxY());
		}
	}
}
