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

package org.arakhne.afc.math.geometry.d2.tests.afp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.RectangularShape2afp;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("all")
public abstract class AbstractRectangularShape2afpTestCase<T extends RectangularShape2afp<?, T, ?, ?, ?, B>,
		B extends Rectangle2afp<?, ?, ?, ?, ?, B>> extends AbstractShape2afpTestCase<T, B> {

	@DisplayName("set(double,double,double,double)")
	@Nested
	public class SetDoubleDoubleDoubleDouble {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(123.456, 456.789, 789.123, 159.753);
			assertEpsilonEquals(123.456, getS().getMinX());
			assertEpsilonEquals(456.789, getS().getMinY());
			assertEpsilonEquals(912.579, getS().getMaxX());
			assertEpsilonEquals(616.542, getS().getMaxY());
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
			getS().set(createPoint(123.456, 456.789), createPoint(789.123, 159.753));
			assertEpsilonEquals(123.456, getS().getMinX());
			assertEpsilonEquals(159.753, getS().getMinY());
			assertEpsilonEquals(789.123, getS().getMaxX());
			assertEpsilonEquals(456.789, getS().getMaxY());
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
			getS().setWidth(123.456);
			assertEpsilonEquals(5, getS().getMinX());
			assertEpsilonEquals(8, getS().getMinY());
			assertEpsilonEquals(128.456, getS().getMaxX());
			assertEpsilonEquals(18, getS().getMaxY());
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
			getS().setHeight(123.456);
			assertEpsilonEquals(5, getS().getMinX());
			assertEpsilonEquals(8, getS().getMinY());
			assertEpsilonEquals(10, getS().getMaxX());
			assertEpsilonEquals(131.456, getS().getMaxY());
		}

	}

	@DisplayName("setFromCorners")
	@Nested
	public class SetFromCorners {

        @DisplayName("(double,double,double,double) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().setFromCorners(123.456, 456.789, 789.123, 159.753);
			assertEpsilonEquals(123.456, getS().getMinX());
			assertEpsilonEquals(159.753, getS().getMinY());
			assertEpsilonEquals(789.123, getS().getMaxX());
			assertEpsilonEquals(456.789, getS().getMaxY());
		}

        @DisplayName("(Point2D,Point2D) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().setFromCorners(createPoint(123.456, 456.789), createPoint(789.123, 159.753));
    		assertEpsilonEquals(123.456, getS().getMinX());
    		assertEpsilonEquals(159.753, getS().getMinY());
    		assertEpsilonEquals(789.123, getS().getMaxX());
    		assertEpsilonEquals(456.789, getS().getMaxY());
    	}

	}
	
	@DisplayName("setFromCenter")
	@Nested
	public class SetFromCenter {

        @DisplayName("(double,double,double,double) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().setFromCenter(123.456, 456.789, 789.123, 159.753);
			assertEpsilonEquals(-542.211, getS().getMinX());
			assertEpsilonEquals(159.753, getS().getMinY());
			assertEpsilonEquals(789.123, getS().getMaxX());
			assertEpsilonEquals(753.825, getS().getMaxY());
		}
		
        @DisplayName("(Point2D,Point2D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void setFromCenterPoint2DPoint2D(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().setFromCenter(createPoint(123.456, 456.789), createPoint(789.123, 159.753));
			assertEpsilonEquals(-542.211, getS().getMinX());
			assertEpsilonEquals(159.753, getS().getMinY());
			assertEpsilonEquals(789.123, getS().getMaxX());
			assertEpsilonEquals(753.825, getS().getMaxY());
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
			assertEpsilonEquals(5, getS().getMinX());
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
			getS().setMinX(123.456);
			assertEpsilonEquals(123.456, getS().getMinX());
			assertEpsilonEquals(8, getS().getMinY());
			assertEpsilonEquals(123.456, getS().getMaxX());
			assertEpsilonEquals(18, getS().getMaxY());
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
			assertEpsilonEquals(7.5, getS().getCenterX());
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
			assertEpsilonEquals(10, getS().getMaxX());
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
			getS().setMaxX(-123.456);
			assertEpsilonEquals(-123.456, getS().getMinX());
			assertEpsilonEquals(8, getS().getMinY());
			assertEpsilonEquals(-123.456, getS().getMaxX());
			assertEpsilonEquals(18, getS().getMaxY());
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
			assertEpsilonEquals(8, getS().getMinY());
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
			getS().setMinY(123.456);
			assertEpsilonEquals(5, getS().getMinX());
			assertEpsilonEquals(123.456, getS().getMinY());
			assertEpsilonEquals(10, getS().getMaxX());
			assertEpsilonEquals(123.456, getS().getMaxY());
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
			assertEpsilonEquals(13, getS().getCenterY());
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
			assertEpsilonEquals(18, getS().getMaxY());
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
			getS().setMaxY(-123.456);
			assertEpsilonEquals(5, getS().getMinX());
			assertEpsilonEquals(-123.456, getS().getMinY());
			assertEpsilonEquals(10, getS().getMaxX());
			assertEpsilonEquals(-123.456, getS().getMaxY());
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
			assertEpsilonEquals(5, getS().getWidth());
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
			assertEpsilonEquals(10, getS().getHeight());
		}

	}

	@DisplayName("clone")
	@Nested
	public class CloneTest {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			T clone = getS().clone();
			assertNotNull(clone);
			assertNotSame(getS(), clone);
			assertEquals(getS().getClass(), clone.getClass());
			assertEpsilonEquals(5, clone.getMinX());
			assertEpsilonEquals(8, clone.getMinY());
			assertEpsilonEquals(10, clone.getMaxX());
			assertEpsilonEquals(18, clone.getMaxY());
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
			assertFalse(getS().isEmpty());
		}

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			assertTrue(getS().isEmpty());
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
			getS().clear();
			assertEpsilonEquals(0, getS().getMinX());
			assertEpsilonEquals(0, getS().getMinY());
			assertEpsilonEquals(0, getS().getMaxX());
			assertEpsilonEquals(0, getS().getMaxY());
		}

	}

	@DisplayName("translate(double,double)")
	@Nested
	public class TranslateDoubleDouble {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().translate(123.456, 456.789);
			assertEpsilonEquals(128.456, getS().getMinX());
			assertEpsilonEquals(464.789, getS().getMinY());
			assertEpsilonEquals(133.456, getS().getMaxX());
			assertEpsilonEquals(474.789, getS().getMaxY());
		}

	}

	@DisplayName("translate(Vector2D)")
	@Nested
	public class TranslateVector2D {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().translate(createVector(123.456, 456.789));
			assertEpsilonEquals(128.456, getS().getMinX());
			assertEpsilonEquals(464.789, getS().getMinY());
			assertEpsilonEquals(133.456, getS().getMaxX());
			assertEpsilonEquals(474.789, getS().getMaxY());
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
			B box = getS().toBoundingBox();
			assertNotSame(getS(), box);
			assertEpsilonEquals(5, box.getMinX());
			assertEpsilonEquals(8, box.getMinY());
			assertEpsilonEquals(10, box.getMaxX());
			assertEpsilonEquals(18, box.getMaxY());
		}

	}

	@DisplayName("toBoundingBox(B)")
	@Nested
	public class ToBoundingBoxB {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			B box = createRectangle(0, 0, 0, 0);
			getS().toBoundingBox(box);
			assertEpsilonEquals(5, box.getMinX());
			assertEpsilonEquals(8, box.getMinY());
			assertEpsilonEquals(10, box.getMaxX());
			assertEpsilonEquals(18, box.getMaxY());
		}

	}

}