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

package org.arakhne.afc.math.geometry.d2.tests.ai;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.base.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.ai.Rectangle2ai;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("all")
public abstract class AbstractRectangularShape2aiTestCase<T extends Rectangle2ai<?, T, ?, ?, ?, B>,
		B extends Rectangle2ai<?, ?, ?, ?, ?, B>> extends AbstractShape2aiTestCase<T, B> {

	protected static final int MINX = 5;
	
	protected static final int MINY = 8;
	
	protected static final int WIDTH = 10;
	
	protected static final int HEIGHT = 5;
	 
	protected static final int MAXX = MINX + WIDTH;
	
	protected static final int MAXY = MINY + HEIGHT;

	@DisplayName("toBoundingBox(B)")
	@Nested
	public class ToBoundingBoxB {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			B box = createRectangle(0, 0, 0, 0);
			shape.toBoundingBox(box);
			assertEquals(shape.getMinX(), box.getMinX());
			assertEquals(shape.getMinY(), box.getMinY());
			assertEquals(shape.getMaxX(), box.getMaxX());
			assertEquals(shape.getMaxY(), box.getMaxY());
		}

	}
	
	@DisplayName("toBoundingBox")
	@Nested
	public class ToBoundingBox {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			B box = shape.toBoundingBox();
			assertEquals(shape.getMinX(), box.getMinX());
			assertEquals(shape.getMinY(), box.getMinY());
			assertEquals(shape.getMaxX(), box.getMaxX());
			assertEquals(shape.getMaxY(), box.getMaxY());
		}
	}

	@DisplayName("clear")
	@Nested
	public class Clear {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			shape.clear();
			assertEquals(0, shape.getMinX());
			assertEquals(0, shape.getMinY());
			assertEquals(0, shape.getMaxX());
			assertEquals(0, shape.getMaxY());
		}

	}

	@DisplayName("set(int,int,int,int)")
	@Nested
	public class SetIntIntIntInt {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			shape.set(10, 12, 14, 16);
			assertEquals(10, shape.getMinX());
			assertEquals(12, shape.getMinY());
			assertEquals(24, shape.getMaxX());
			assertEquals(28, shape.getMaxY());
		}

	}

	@DisplayName("set(Point2D,Point2D)")
	@Nested
	public class SetPoint2DPoint2D {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			shape.set(createPoint(10, 12), createPoint(14, 16));
			assertEquals(10, shape.getMinX());
			assertEquals(12, shape.getMinY());
			assertEquals(14, shape.getMaxX());
			assertEquals(16, shape.getMaxY());
		}

	}

	@DisplayName("setWidth")
	@Nested
	public class SetWidth {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			shape.setWidth(150);
			assertEquals(5, shape.getMinX());
			assertEquals(8, shape.getMinY());
			assertEquals(155, shape.getMaxX());
			assertEquals(13, shape.getMaxY());
		}
	}

	@DisplayName("setHeight")
	@Nested
	public class SetHeight {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			shape.setHeight(150);
			assertEquals(5, shape.getMinX());
			assertEquals(8, shape.getMinY());
			assertEquals(15, shape.getMaxX());
			assertEquals(158, shape.getMaxY());
		}
	}
	
	@DisplayName("setFromCorners")
	@Nested
	public class SetFromCorners {

		@DisplayName("(int,int,int,int) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			shape.setFromCorners(2, 3, 4, 5);
			assertEquals(2, shape.getMinX());
			assertEquals(3, shape.getMinY());
			assertEquals(4, shape.getMaxX()); 
			assertEquals(5, shape.getMaxY());
		}

		@DisplayName("(Point2D,Point2D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			shape.setFromCorners(createPoint(2, 3), createPoint(4, 5));
			assertEquals(2, shape.getMinX());
			assertEquals(3, shape.getMinY());
			assertEquals(4, shape.getMaxX()); 
			assertEquals(5, shape.getMaxY());
		}
	}

	@DisplayName("setFromCenter")
	@Nested
	public class SetFromCenter {

		@DisplayName("(int,int,int,int) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			shape.setFromCenter(2, 3, 4, 5);
			assertEquals(0, shape.getMinX());
			assertEquals(1, shape.getMinY());
			assertEquals(4, shape.getMaxX()); 
			assertEquals(5, shape.getMaxY());
		}
		
		@DisplayName("(Point2D,Point2D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			shape.setFromCenter(createPoint(2, 3), createPoint(4, 5));
			assertEquals(0, shape.getMinX());
			assertEquals(1, shape.getMinY());
			assertEquals(4, shape.getMaxX()); 
			assertEquals(5, shape.getMaxY());
		}
	}

	@DisplayName("getMinX")
	@Nested
	public class GetMinX {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(MINX, shape.getMinX());
		}
	}

	@DisplayName("setMinX")
	@Nested
	public class SetMinX {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			shape.setMinX(-45);
			assertEquals(-45, shape.getMinX());
			assertEquals(MINY, shape.getMinY());
			assertEquals(MAXX, shape.getMaxX()); 
			assertEquals(MAXY, shape.getMaxY());
		}
	}

	@DisplayName("getCenter")
	@Nested
	public class GetCenter {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFpPointEquals(
	                MINX + WIDTH / 2,
	                MINY + HEIGHT / 2,
	                shape.getCenter());
	    }
	}

	@DisplayName("getCenterX")
	@Nested
	public class GetCenterX {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(MINX + WIDTH / 2, shape.getCenterX());
		}
	}

	@DisplayName("getMaxX")
	@Nested
	public class GetMaxX {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(MAXX, shape.getMaxX());
		}
	}

	@DisplayName("setMaxX")
	@Nested
	public class SetMaxX {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			shape.setMaxX(45);
			assertEquals(MINX, shape.getMinX());
			assertEquals(MINY, shape.getMinY());
			assertEquals(45, shape.getMaxX()); 
			assertEquals(MAXY, shape.getMaxY());
		}
	}

	@DisplayName("getMinY")
	@Nested
	public class GetMinY {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(MINY, shape.getMinY());
		}
	}

	@DisplayName("setMinY")
	@Nested
	public class SetMinY {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			shape.setMinY(-45);
			assertEquals(MINX, shape.getMinX());
			assertEquals(-45, shape.getMinY());
			assertEquals(MAXX, shape.getMaxX()); 
			assertEquals(MAXY, shape.getMaxY());
		}
	}

	@DisplayName("getCenterY")
	@Nested
	public class GetCenterY {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(MINY + HEIGHT / 2, shape.getCenterY());
		}
	}

	@DisplayName("getMaxY")
	@Nested
	public class GetMaxY {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(MAXY, shape.getMaxY());
		}
	}

	@DisplayName("setMaxY")
	@Nested
	public class SetMaxY {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			shape.setMaxY(45);
			assertEquals(MINX, shape.getMinX());
			assertEquals(MINY, shape.getMinY());
			assertEquals(MAXX, shape.getMaxX()); 
			assertEquals(45, shape.getMaxY());
		}
	}

	@DisplayName("getWidth")
	@Nested
	public class GetWidth {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(WIDTH, shape.getWidth());
		}
	}

	@DisplayName("getHeight")
	@Nested
	public class GetHeight {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(HEIGHT, shape.getHeight());
		}
	}

	@DisplayName("translate(int,int)")
	@Nested
	public class TranslateIntInt {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			shape.translate(3,  4);
			assertEquals(8, shape.getMinX());
			assertEquals(12, shape.getMinY());
			assertEquals(18, shape.getMaxX());
			assertEquals(17, shape.getMaxY());
		}
	}

	@DisplayName("isEmpty")
	@Nested
	public class IsEmpty {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(shape.isEmpty());
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void test_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			shape.clear();
			assertTrue(shape.isEmpty());
		}
	}

}