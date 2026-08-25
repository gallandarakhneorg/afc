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

import static org.arakhne.afc.testtools.XbaseInlineTestUtil.assertInlineParameterUsage;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javafx.beans.property.ReadOnlyDoubleProperty;
import org.arakhne.afc.math.geometry.base.tests.AbstractVector2DTestCase;
import org.arakhne.afc.math.geometry.fx.d2.d.Point2dfx;
import org.arakhne.afc.math.geometry.fx.d2.d.Vector2dfx;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Vector2dfx")
@SuppressWarnings("all")
public class Vector2dfxTest extends AbstractVector2DTestCase<Vector2dfx, Point2dfx, Vector2dfx> {

	@Override
	public boolean isIntCoordinates() {
		return false;
	}
	
	@Override
	public Vector2dfx createTuple(double x, double y) {
		return new Vector2dfx(x, y);
	}
	
	@Override
	public Vector2dfx createVector(double x, double y) {
		return new Vector2dfx(x, y);
	}

	@Override
	public Point2dfx createPoint(double x, double y) {
		return new Point2dfx(x, y);
	}

	@DisplayName("toOrientationVector")
	@Nested
	public class ToOrientationVector {

		@DisplayName("staticToOrientationVector_assertion1")
		@Test
		public void staticToOrientationVector_assertion1() {
			assertFpVectorEquals(1, 0, Vector2dfx.toOrientationVector(0));
		}

		@DisplayName("staticToOrientationVector_assertion2")
		@Test
		public void staticToOrientationVector_assertion2() {
			assertFpVectorEquals(-1, 0, Vector2dfx.toOrientationVector(Math.PI));
		}

		@DisplayName("staticToOrientationVector_assertion3")
		@Test
		public void staticToOrientationVector_assertion3() {
			assertFpVectorEquals(0, 1, Vector2dfx.toOrientationVector(Math.PI/2));
		}

		@DisplayName("staticToOrientationVector_assertion4")
		@Test
		public void staticToOrientationVector_assertion4() {
			assertFpVectorEquals(0, -1, Vector2dfx.toOrientationVector(-Math.PI/2));
		}

		@DisplayName("inlineParameterUsage")
		@Test
		public void inlineParameterUsage() {
			assertInlineParameterUsage(Vector2dfx.class, "toOrientationVector", double.class);
		}
	}
	
	@DisplayName("lengthProperty")
	@Nested
	public class LengthProperty {

		private Vector2dfx vector;
		private ReadOnlyDoubleProperty property;
		
		@BeforeEach
		public void setUp() {
			vector = new Vector2dfx(1, 2);
			property = vector.lengthProperty();
		}
		
		@DisplayName("lengthProperty_1")
		@Test
		public void lengthProperty_1() {
			assertEpsilonEquals(2.23607, vector.getLength());
		}
		
		@DisplayName("lengthProperty_2")
		@Test
		public void lengthProperty_2() {
			assertNotNull(property);
		}
		
		@DisplayName("lengthProperty_3")
		@Test
		public void lengthProperty_3() {
			assertEpsilonEquals(2.23607, property.get());
		}
		
		@DisplayName("lengthProperty_4")
		@Test
		public void lengthProperty_4() {
			vector.set(4, -10);
			assertEpsilonEquals(10.77033, property.get());
		}
	}
	
	@DisplayName("lengthSquaredProperty")
	@Nested
	public class LengthSquaredProperty {

		private Vector2dfx vector;
		private ReadOnlyDoubleProperty property;

		@BeforeEach
		public void setUp() {
			vector = new Vector2dfx(1, 2);
			property = vector.lengthSquaredProperty();
		}

		@DisplayName("lengthSquaredProperty_assertion1")
		@Test
		public void lengthSquaredProperty_assertion1() {
			assertEpsilonEquals(5, vector.getLengthSquared());
		}

		@DisplayName("lengthSquaredProperty_assertion2")
		@Test
		public void lengthSquaredProperty_assertion2() {
			assertNotNull(property);
		}

		@DisplayName("lengthSquaredProperty_assertion3")
		@Test
		public void lengthSquaredProperty_assertion3() {
			assertEpsilonEquals(5, property.get());
		}

		@DisplayName("lengthSquaredProperty_assertion4")
		@Test
		public void lengthSquaredProperty_assertion4() {
			vector.set(4, -10);
			assertEpsilonEquals(116, property.get());
		}
	}
}
