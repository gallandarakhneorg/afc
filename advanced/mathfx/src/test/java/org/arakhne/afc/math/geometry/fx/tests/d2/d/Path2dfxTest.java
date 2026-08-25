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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyListProperty;
import org.arakhne.afc.math.geometry.base.PathElementType;
import org.arakhne.afc.math.geometry.base.PathWindingRule;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.d2.tests.afp.AbstractPath2afpTestCase;
import org.arakhne.afc.math.geometry.fx.d2.d.Path2dfx;
import org.arakhne.afc.math.geometry.fx.d2.d.Point2dfx;
import org.arakhne.afc.math.geometry.fx.d2.d.Rectangle2dfx;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@DisplayName("Path2dfx")
@SuppressWarnings("all")
public class Path2dfxTest extends AbstractPath2afpTestCase<Path2dfx, Rectangle2dfx> {

	@Override
	protected TestShapeFactory2dfx createFactory() {
		return TestShapeFactory2dfx.SINGLETON;
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
			assertEpsilonEquals(-5, box.getMinY());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void boundingBoxProperty_assertion5(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(7, box.getMaxX());
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void boundingBoxProperty_assertion6(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(3, box.getMaxY());
		}
	}

	@DisplayName("controlPointBoundingBoxProperty")
	@Nested
	public class ControlPointBoundingBoxProperty {

		private ObjectProperty<Rectangle2dfx> property;
		private Rectangle2dfx box;

		public void setUp(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			property = shape.controlPointBoundingBoxProperty();
			box = property.get();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void controlPointBoundingBoxProperty_assertion1(CoordinateSystem2D cs) {
			setUp(cs);
			assertNotNull(property);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void controlPointBoundingBoxProperty_assertion2(CoordinateSystem2D cs) {
			setUp(cs);
			assertNotNull(box);
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void controlPointBoundingBoxProperty_assertion3(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(0, box.getMinX());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void controlPointBoundingBoxProperty_assertion4(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(-5, box.getMinY());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void controlPointBoundingBoxProperty_assertion5(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(7, box.getMaxX());
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void controlPointBoundingBoxProperty_assertion6(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(5, box.getMaxY());
		}
	}

	@DisplayName("coordinatesProperty")
	@Nested
	public class CoordinatesProperty {

		private ReadOnlyListProperty<Point2dfx> property;

		public void setUp(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			property = shape.coordinatesProperty();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void coordinatesProperty_assertion1(CoordinateSystem2D cs) {
			setUp(cs);
			assertNotNull(property);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void coordinatesProperty_assertion2(CoordinateSystem2D cs) {
			setUp(cs);
			assertEquals(7, property.size());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void coordinatesProperty_assertion3(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(0, property.get(0).getX());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void coordinatesProperty_assertion4(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(0, property.get(0).getY());
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void coordinatesProperty_assertion5(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(1, property.get(1).getX());
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void coordinatesProperty_assertion6(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(1, property.get(1).getY());
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void coordinatesProperty_assertion7(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(3, property.get(2).getX());
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void coordinatesProperty_assertion8(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(0, property.get(2).getY());
		}

		@DisplayName("#9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void coordinatesProperty_assertion9(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(4, property.get(3).getX());
		}

		@DisplayName("#10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void coordinatesProperty_assertion10(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(3, property.get(3).getY());
		}

		@DisplayName("#11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void coordinatesProperty_assertion11(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(5, property.get(4).getX());
		}

		@DisplayName("#12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void coordinatesProperty_assertion12(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(-1, property.get(4).getY());
		}

		@DisplayName("#13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void coordinatesProperty_assertion13(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(6, property.get(5).getX());
		}

		@DisplayName("#14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void coordinatesProperty_assertion14(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(5, property.get(5).getY());
		}

		@DisplayName("#15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void coordinatesProperty_assertion15(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(7, property.get(6).getX());
		}

		@DisplayName("#16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void coordinatesProperty_assertion16(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(-5, property.get(6).getY());
		}
	}

	@DisplayName("isCurvedProperty")
	@Nested
	public class IsCurvedProperty {

		private BooleanProperty property;

		public void setUp(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			property = shape.isCurvedProperty();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void isCurvedProperty_assertion1(CoordinateSystem2D cs) {
			setUp(cs);
			assertNotNull(property);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void isCurvedProperty_assertion2(CoordinateSystem2D cs) {
			setUp(cs);
			assertTrue(property.get());
		}
	}

	@DisplayName("isEmptyProperty")
	@Nested
	public class IsEmptyProperty {

		private BooleanProperty property;

		public void setUp(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			property = shape.isEmptyProperty();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void isEmptyProperty_assertion1(CoordinateSystem2D cs) {
			setUp(cs);
			assertNotNull(property);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void isEmptyProperty_assertion2(CoordinateSystem2D cs) {
			setUp(cs);
			assertFalse(property.get());
		}
	}

	@DisplayName("isMultiPartsProperty")
	@Nested
	public class IsMultiPartsProperty {

		private BooleanProperty property;

		public void setUp(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			property = shape.isMultiPartsProperty();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void isMultiPartsProperty_assertion1(CoordinateSystem2D cs) {
			setUp(cs);
			assertNotNull(property);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void isMultiPartsProperty_assertion2(CoordinateSystem2D cs) {
			setUp(cs);
			assertFalse(property.get());
		}
	}

	@DisplayName("isPolygonProperty")
	@Nested
	public class IsPolygonProperty {

		private BooleanProperty property;

		public void setUp(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			property = shape.isPolygonProperty();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void isPolygonProperty_assertion1(CoordinateSystem2D cs) {
			setUp(cs);
			assertNotNull(property);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void isPolygonProperty_assertion2(CoordinateSystem2D cs) {
			setUp(cs);
			assertFalse(property.get());
		}
	}

	@DisplayName("isPolylineProperty")
	@Nested
	public class IsPolylineProperty {

		private BooleanProperty property;

		public void setUp(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			property = shape.isPolylineProperty();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void isPolylineProperty_assertion1(CoordinateSystem2D cs) {
			setUp(cs);
			assertNotNull(property);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void isPolylineProperty_assertion2(CoordinateSystem2D cs) {
			setUp(cs);
			assertFalse(property.get());
		}
	}

	@DisplayName("lengthProperty")
	@Nested
	public class LengthProperty {

		private DoubleProperty property;

		public void setUp(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			property = shape.lengthProperty();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lengthProperty_assertion1(CoordinateSystem2D cs) {
			setUp(cs);
			assertNotNull(property);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lengthProperty_assertion2(CoordinateSystem2D cs) {
			setUp(cs);
			assertEpsilonEquals(14.71628, property.get());
		}
	}

	@DisplayName("typesProperty")
	@Nested
	public class TypesProperty {

		private ReadOnlyListProperty<PathElementType> property;

		public void setUp(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			property = shape.typesProperty();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void typesProperty_assertion1(CoordinateSystem2D cs) {
			setUp(cs);
			assertNotNull(property);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void typesProperty_assertion2(CoordinateSystem2D cs) {
			setUp(cs);
			assertEquals(4, property.size());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void typesProperty_assertion3(CoordinateSystem2D cs) {
			setUp(cs);
			assertSame(PathElementType.MOVE_TO, property.get(0));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void typesProperty_assertion4(CoordinateSystem2D cs) {
			setUp(cs);
			assertSame(PathElementType.LINE_TO, property.get(1));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void typesProperty_assertion5(CoordinateSystem2D cs) {
			setUp(cs);
			assertSame(PathElementType.QUAD_TO, property.get(2));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void typesProperty_assertion6(CoordinateSystem2D cs) {
			setUp(cs);
			assertSame(PathElementType.CURVE_TO, property.get(3));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void typesProperty_assertion7(CoordinateSystem2D cs) {
			setUp(cs);
			shape.closePath();
			assertEquals(5, property.size());
		}

		@DisplayName("#8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void typesProperty_assertion8(CoordinateSystem2D cs) {
			setUp(cs);
			shape.closePath();
			assertSame(PathElementType.CLOSE, property.get(4));
		}
	}

	@DisplayName("windingRuleProperty")
	@Nested
	public class WindingRuleProperty {

		private ObjectProperty<PathWindingRule> property;

		public void setUp(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			property = shape.windingRuleProperty();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void windingRuleProperty_assertion1(CoordinateSystem2D cs) {
			setUp(cs);
			assertNotNull(property);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void windingRuleProperty_assertion2(CoordinateSystem2D cs) {
			setUp(cs);
			assertSame(PathWindingRule.NON_ZERO, property.get());
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void windingRuleProperty_assertion3(CoordinateSystem2D cs) {
			setUp(cs);
			shape.setWindingRule(PathWindingRule.EVEN_ODD);
			assertSame(PathWindingRule.EVEN_ODD, property.get());
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void windingRuleProperty_assertion4(CoordinateSystem2D cs) {
			setUp(cs);
			property.set(PathWindingRule.NON_ZERO);
			assertSame(PathWindingRule.NON_ZERO, property.get());
		}
	}
}
