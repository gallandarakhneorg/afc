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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.arakhne.afc.math.geometry.base.PathElementType;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.base.d2.Point2D;
import org.arakhne.afc.math.geometry.base.d2.Shape2D;
import org.arakhne.afc.math.geometry.base.d2.Transform2D;
import org.arakhne.afc.math.geometry.base.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.afp.Circle2afp;
import org.arakhne.afc.math.geometry.d2.afp.MultiShape2afp;
import org.arakhne.afc.math.geometry.d2.afp.OrientedRectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Parallelogram2afp;
import org.arakhne.afc.math.geometry.d2.afp.Path2afp;
import org.arakhne.afc.math.geometry.d2.afp.PathIterator2afp;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.RoundRectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Triangle2afp;
import org.arakhne.afc.math.geometry.d2.ai.PathIterator2ai;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("all")
public abstract class AbstractRoundRectangle2afpTest<T extends RoundRectangle2afp<?, T, ?, ?, ?, B>,
B extends Rectangle2afp<?, ?, ?, ?, ?, B>> extends AbstractRectangularShape2afpTest<T, B> {

    @Override
    protected final T createShape() {
        return (T) createRoundRectangle(5, 8, 5, 10, .1, .2);
    }

    protected Triangle2afp<?, ?, ?, ?, ?, ?> createTestTriangle(double dx, double dy) {
        return createTriangle(dx, dy, dx + 2, dy + 1.5, dx + 1.5, dy - 1.6);
    }

    protected Path2afp<?, ?, ?, ?, ?, ?> createNonEmptyPath(double x, double y) {
        Path2afp<?, ?, ?, ?, ?, ?> path = createPath();
        path.moveTo(x, y);
        path.lineTo(x + 1, y + .5);
        path.lineTo(x, y + 1);
        return path;
    }

    protected MultiShape2afp createTestMultiShape(double dx, double dy) {
        Circle2afp circle = createCircle(dx - 3, dy + 2, 2);
        Triangle2afp triangle = createTestTriangle(dx +1, dy - 1);
        MultiShape2afp multishape = createMultiShape();
        multishape.add(circle);
        multishape.add(triangle);
        return multishape;
    }
    
    protected Parallelogram2afp createTestParallelogram(double dx, double dy) {
        Vector2D r = createVector(4, 1).toUnitVector();
        Vector2D s = createVector(-1, -1).toUnitVector();
        return createParallelogram(dx, dy, r.getX(), r.getY(), 2, s.getX(), s.getY(), 1);
    }

    protected OrientedRectangle2afp createTestOrientedRectangle(double dx, double dy) {
        Vector2D r = createVector(4, 1).toUnitVector();
        return createOrientedRectangle(dx, dy, r.getX(), r.getY(), 2, 1);
    }

    @DisplayName("containsRoundRectanglePoint")
	@Nested
	public class ContainsRoundRectanglePoint {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.containsRoundRectanglePoint(5, 8, 5, 10, .1, .2, 0, 0));
        }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.containsRoundRectanglePoint(5, 8, 5, 10, .1, .2, 20, 0));
        }

        @DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.containsRoundRectanglePoint(5, 8, 5, 10, .1, .2, 20, 20));
        }

        @DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.containsRoundRectanglePoint(5, 8, 5, 10, .1, .2, 0, 20));
        }

        @DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        }

        @DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(RoundRectangle2afp.containsRoundRectanglePoint(5, 8, 5, 10, .1, .2, 8, 13));
        }

        @DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(RoundRectangle2afp.containsRoundRectanglePoint(5, 8, 5, 10, .1, .2, 5, 13));
        }

        @DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        }

        @DisplayName("#9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        }

        @DisplayName("#10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.containsRoundRectanglePoint(5, 8, 5, 10, .1, .2, 4.999, 13));
        }

        @DisplayName("#11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.containsRoundRectanglePoint(5, 8, 5, 10, .1, .2, 5, 8));
        }

        @DisplayName("#12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.containsRoundRectanglePoint(5, 8, 5, 10, .1, .2, 5, 18));
        }

        @DisplayName("#13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.containsRoundRectanglePoint(5, 8, 5, 10, .1, .2, 15, 18));
        }

        @DisplayName("#14")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_14(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.containsRoundRectanglePoint(5, 8, 5, 10, .1, .2, 15, 8));
	    }

	}

	@DisplayName("containsRoundRectangleRectangle")
	@Nested
	public class ContainsRoundRectangleRectangle {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.containsRoundRectangleRectangle(5, 8, 5, 10, .1, .2, 0, 0, 1, 1));
        }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.containsRoundRectangleRectangle(5, 8, 5, 10, .1, .2, 0, 0, 7, 10));
        }

        @DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.containsRoundRectangleRectangle(5, 8, 5, 10, .1, .2, 0, 0, 20, 20));
        }

        @DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(RoundRectangle2afp.containsRoundRectangleRectangle(5, 8, 5, 10, .1, .2, 6, 10, 1, 1));
        }

        @DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.containsRoundRectangleRectangle(5, 8, 5, 10, .1, .2, 5, 8, 5, 10));
        }

        @DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(RoundRectangle2afp.containsRoundRectangleRectangle(5, 8, 5, 10, .1, .2, 5.5, 8.5, 4, 9));
	    }

	}

	@DisplayName("intersectsRoundRectangleSegment")
	@Nested
	public class IntersectsRoundRectangleSegment {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.intersectsRoundRectangleSegment(5, 8, 10, 18, .1, .2, 0, 0, 1, 1));
        }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.intersectsRoundRectangleSegment(5, 8, 10, 18, .1, .2, 20, 20, 21, 21));
        }

        @DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(RoundRectangle2afp.intersectsRoundRectangleSegment(5, 8, 10, 18, .1, .2, 0, 0, 7, 12));
        }

        @DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.intersectsRoundRectangleSegment(5, 8, 10, 18, .1, .2, 0, 0, 7, 8));
        }

        @DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.intersectsRoundRectangleSegment(5, 8, 10, 18, .1, .2, 6, 7, 4.1, 9));
        }

        @DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(RoundRectangle2afp.intersectsRoundRectangleSegment(5, 8, 10, 18, .1, .2, 6.1, 7, 4.1, 9));
	    }

	}

	@DisplayName("intersectsRoundRectangleRoundRectangle")
	@Nested
	public class IntersectsRoundRectangleRoundRectangle {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.intersectsRoundRectangleRoundRectangle(5, 8, 10, 18, .1, .2, 0, 0, 1, 1, .1, .2));
        }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.intersectsRoundRectangleRoundRectangle(5, 8, 10, 18, .1, .2, 20, 20, 21, 21, .1, .2));
        }

        @DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(RoundRectangle2afp.intersectsRoundRectangleRoundRectangle(5, 8, 10, 18, .1, .2, 0, 0, 7, 12, .1, .2));
        }

        @DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.intersectsRoundRectangleRoundRectangle(5, 8, 10, 18, .1, .2, 0, 0, 7, 8, .1, .2));
        }

        @DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.intersectsRoundRectangleRoundRectangle(5, 8, 10, 18, .1, .2, 0, 0, 5.01, 8.01, .1, .2));
        }

        @DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.intersectsRoundRectangleRoundRectangle(5, 8, 10, 18, .1, .2, 0, 0, 5.05, 8.05, .1, .2));
        }

        @DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.intersectsRoundRectangleRoundRectangle(5, 8, 10, 18, .1, .2, 0, 0, 5.05, 8.1, .1, .2));
        }

        @DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(RoundRectangle2afp.intersectsRoundRectangleRoundRectangle(5, 8, 10, 18, .1, .2, 0, 0, 5.05, 8.15, .1, .2));
	    }

	}

	@DisplayName("intersectsRoundRectangleRectangle")
	@Nested
	public class IntersectsRoundRectangleRectangle {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.intersectsRoundRectangleRectangle(5, 8, 10, 18, .1, .2, 0, 0, 1, 1));
        }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.intersectsRoundRectangleRectangle(5, 8, 10, 18, .1, .2, 20, 20, 21, 21));
        }

        @DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(RoundRectangle2afp.intersectsRoundRectangleRectangle(5, 8, 10, 18, .1, .2, 0, 0, 7, 12));
        }

        @DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.intersectsRoundRectangleRectangle(5, 8, 10, 18, .1, .2, 0, 0, 7, 8));
        }

        @DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.intersectsRoundRectangleRectangle(5, 8, 10, 18, .1, .2, 0, 0, 5.01, 8.01));
        }

        @DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(RoundRectangle2afp.intersectsRoundRectangleRectangle(5, 8, 10, 18, .1, .2, 0, 0, 5.05, 8.05));
        }

        @DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        }

        @DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        }

        @DisplayName("#9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        }

        @DisplayName("#10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(RoundRectangle2afp.intersectsRoundRectangleRectangle(5, 8, 10, 18, .1, .2, 0, 0, 5.05, 8.1));
        }

        @DisplayName("#11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(RoundRectangle2afp.intersectsRoundRectangleRectangle(5, 8, 10, 18, .1, .2, 0, 0, 5.05, 8.15));
	    }

	}

	@DisplayName("intersectsRoundRectangleCircle")
	@Nested
	public class IntersectsRoundRectangleCircle {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.intersectsRoundRectangleCircle(5, 8, 10, 18, .1, .2, 7, 0, 1));
        }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.intersectsRoundRectangleCircle(5, 8, 10, 18, .1, .2, 7, 20, 1));
        }

        @DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.intersectsRoundRectangleCircle(5, 8, 10, 18, .1, .2, 0, 12, 1));
        }

        @DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.intersectsRoundRectangleCircle(5, 8, 10, 18, .1, .2, 20, 12, 1));
        }

        @DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.intersectsRoundRectangleCircle(5, 8, 10, 18, .1, .2, 0, 0, 1));
        }

        @DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.intersectsRoundRectangleCircle(5, 8, 10, 18, .1, .2, 20, 0, 1));
        }

        @DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.intersectsRoundRectangleCircle(5, 8, 10, 18, .1, .2, 20, 20, 1));
        }

        @DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.intersectsRoundRectangleCircle(5, 8, 10, 18, .1, .2, 0, 20, 1));
        }

        @DisplayName("#9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.intersectsRoundRectangleCircle(5, 8, 10, 18, .1, .2, 4, 12, 1));
        }

        @DisplayName("#10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(RoundRectangle2afp.intersectsRoundRectangleCircle(5, 8, 10, 18, .1, .2, 4.1, 12, 1));
        }

        @DisplayName("#11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(RoundRectangle2afp.intersectsRoundRectangleCircle(5, 8, 10, 18, .1, .2, 6, 12, 1));
        }

        @DisplayName("#12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(RoundRectangle2afp.intersectsRoundRectangleCircle(5, 8, 10, 18, .1, .2, 10.9, 12, 1));
        }

        @DisplayName("#13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.intersectsRoundRectangleCircle(5, 8, 10, 18, .1, .2, 11, 12, 1));
        }

        @DisplayName("#14")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_14(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.intersectsRoundRectangleCircle(5, 8, 10, 18, .1, .2, 7, 7, 1));
        }

        @DisplayName("#15")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_15(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(RoundRectangle2afp.intersectsRoundRectangleCircle(5, 8, 10, 18, .1, .2, 7, 7.1, 1));
        }

        @DisplayName("#16")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_16(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(RoundRectangle2afp.intersectsRoundRectangleCircle(5, 8, 10, 18, .1, .2, 7, 12, 1));
        }

        @DisplayName("#17")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_17(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(RoundRectangle2afp.intersectsRoundRectangleCircle(5, 8, 10, 18, .1, .2, 7, 18.9, 1));
        }

        @DisplayName("#18")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_18(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.intersectsRoundRectangleCircle(5, 8, 10, 18, .1, .2, 7, 19, 1));
        }

        @DisplayName("#19")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_19(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.intersectsRoundRectangleCircle(5, 8, 10, 18, .1, .2, 4.32, 7.32, 1));
        }

        @DisplayName("#20")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_20(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(RoundRectangle2afp.intersectsRoundRectangleCircle(5, 8, 10, 18, .1, .2, 4.4, 7.4, 1));
        }

        @DisplayName("#21")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_21(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(RoundRectangle2afp.intersectsRoundRectangleCircle(5, 8, 10, 18, .1, .2, 4.75, 7.75, 1));
        }

        @DisplayName("#22")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_22(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(RoundRectangle2afp.intersectsRoundRectangleCircle(5, 8, 10, 18, .1, .2, 4.19, 7.55, 1));
	    }

	}

	@DisplayName("intersectsRoundRectangleEllipse")
	@Nested
	public class IntersectsRoundRectangleEllipse {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.intersectsRoundRectangleEllipse(5, 8, 10, 18, .1, .2, 6, -.5, 2, 1));
        }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        	assertFalse(RoundRectangle2afp.intersectsRoundRectangleEllipse(5, 8, 10, 18, .1, .2, 6, 19.5, 2, 1));
        }

        @DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.intersectsRoundRectangleEllipse(5, 8, 10, 18, .1, .2, -1, 11.5, 2, 1));
        }

        @DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.intersectsRoundRectangleEllipse(5, 8, 10, 18, .1, .2, 19, 11.5, 2, 1));
        }

        @DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.intersectsRoundRectangleEllipse(5, 8, 10, 18, .1, .2, -1, -.5, 2, 1));
        }

        @DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.intersectsRoundRectangleEllipse(5, 8, 10, 18, .1, .2, 19, -.5, 2, 1));
        }

        @DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.intersectsRoundRectangleEllipse(5, 8, 10, 18, .1, .2, 19, 19.5, 2, 1));
        }

        @DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.intersectsRoundRectangleEllipse(5, 8, 10, 18, .1, .2, -1, 19.5, 2, 1));
        }

        @DisplayName("#9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.intersectsRoundRectangleEllipse(5, 8, 10, 18, .1, .2, 3, 11.5, 2, 1));
        }

        @DisplayName("#10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(RoundRectangle2afp.intersectsRoundRectangleEllipse(5, 8, 10, 18, .1, .2, 3.1, 11.5, 2, 1));
        }

        @DisplayName("#11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(RoundRectangle2afp.intersectsRoundRectangleEllipse(5, 8, 10, 18, .1, .2, 5, 11.5, 2, 1));
        }

        @DisplayName("#12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(RoundRectangle2afp.intersectsRoundRectangleEllipse(5, 8, 10, 18, .1, .2, 9.9, 11.5, 2, 1));
        }

        @DisplayName("#13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.intersectsRoundRectangleEllipse(5, 8, 10, 18, .1, .2, 10, 11.5, 2, 1));
        }

        @DisplayName("#14")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_14(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.intersectsRoundRectangleEllipse(5, 8, 10, 18, .1, .2, 6, 6.5, 2, 1));
        }

        @DisplayName("#15")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_15(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.intersectsRoundRectangleEllipse(5, 8, 10, 18, .1, .2, 6, 6.6, 2, 1));
        }

        @DisplayName("#16")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_16(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(RoundRectangle2afp.intersectsRoundRectangleEllipse(5, 8, 10, 18, .1, .2, 6, 11.5, 2, 1));
        }

        @DisplayName("#17")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_17(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.intersectsRoundRectangleEllipse(5, 8, 10, 18, .1, .2, 6, 18.4, 2, 1));
        }

        @DisplayName("#18")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_18(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.intersectsRoundRectangleEllipse(5, 8, 10, 18, .1, .2, 6, 18.5, 2, 1));
        }

        @DisplayName("#19")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_19(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.intersectsRoundRectangleEllipse(5, 8, 10, 18, .1, .2, 3.32, 6.82, 2, 1));
        }

        @DisplayName("#20")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_20(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.intersectsRoundRectangleEllipse(5, 8, 10, 18, .1, .2, 3.4, 6.9, 2, 1));
        }

        @DisplayName("#21")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_21(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(RoundRectangle2afp.intersectsRoundRectangleEllipse(5, 8, 10, 18, .1, .2, 3.75, 7.25, 2, 1));
        }

        @DisplayName("#22")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_22(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(RoundRectangle2afp.intersectsRoundRectangleEllipse(5, 8, 10, 18, .1, .2, 3.19, 7.05, 2, 1));
        }

        @DisplayName("#23")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_23(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(RoundRectangle2afp.intersectsRoundRectangleEllipse(5, 8, 10, 18, .1, .2, 3.08, 7.45, 2, 1));
	    }

	}

	@DisplayName("getArcWidth")
	@Nested
	public class GetArcWidth {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(.1, getS().getArcWidth());
	    }

	}

	@DisplayName("getArcHeight")
	@Nested
	public class GetArcHeight {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(.2, getS().getArcHeight());
	    }

	}

	@DisplayName("setArcWidth")
	@Nested
	public class SetArcWidth {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        getS().setArcWidth(123.456);
	        assertEpsilonEquals(2.5, getS().getArcWidth());
	    }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
        	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            getS().setMaxX(6);
            assertEpsilonEquals(.1, getS().getArcWidth());
            getS().setMaxX(5.1);
            assertEpsilonEquals(.05, getS().getArcWidth());
        }

        @DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
        	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            getS().setMinX(4);
            assertEpsilonEquals(.1, getS().getArcWidth());
            getS().setMinX(9.9);
            assertEpsilonEquals(.05, getS().getArcWidth());
        }

        @DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
        	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            getS().setWidth(1);
            assertEpsilonEquals(.1, getS().getArcWidth());
            getS().setWidth(.1);
            assertEpsilonEquals(.05, getS().getArcWidth());
        }

	}

	@DisplayName("setArcHeight")
	@Nested
	public class SetArcHeight {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        getS().setArcHeight(123.456);
	        assertEpsilonEquals(5, getS().getArcHeight());
	    }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
        	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            getS().setMaxY(9);
            assertEpsilonEquals(.2, getS().getArcHeight());
            getS().setMaxY(8.1);
            assertEpsilonEquals(.05, getS().getArcHeight());
        }

        @DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
        	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            getS().setMinY(7);
            assertEpsilonEquals(.2, getS().getArcHeight());
            getS().setMinY(17.9);
            assertEpsilonEquals(.05, getS().getArcHeight());
        }

        @DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
        	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            getS().setHeight(1);
            assertEpsilonEquals(.2, getS().getArcHeight());
            getS().setHeight(.2);
            assertEpsilonEquals(.1, getS().getArcHeight());
        }

	}

	@DisplayName("set(double,double,double,double,double,double)")
	@Nested
	public class SetDoubleDoubleDoubleDoubleDoubleDouble {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        getS().set(10, 20, 30, 40, 1, 2);
	        assertEpsilonEquals(10, getS().getMinX());
	        assertEpsilonEquals(20, getS().getMinY());
	        assertEpsilonEquals(40, getS().getMaxX());
	        assertEpsilonEquals(60, getS().getMaxY());
	        assertEpsilonEquals(1, getS().getArcWidth());
	        assertEpsilonEquals(2, getS().getArcHeight());
	    }

	}

	@DisplayName("setFromCorners")
	@Nested
	public class SetFromCorners {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        getS().setFromCorners(10, 20, 30, 40);
	        assertEpsilonEquals(10, getS().getMinX());
	        assertEpsilonEquals(20, getS().getMinY());
	        assertEpsilonEquals(30, getS().getMaxX());
	        assertEpsilonEquals(40, getS().getMaxY());
	        assertEpsilonEquals(.1, getS().getArcWidth());
	        assertEpsilonEquals(.2, getS().getArcHeight());
	    }
	
        @DisplayName("#2")
	    @ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        getS().setFromCorners(10, 20, 30, 40, 1, 2);
	        assertEpsilonEquals(10, getS().getMinX());
	        assertEpsilonEquals(20, getS().getMinY());
	        assertEpsilonEquals(30, getS().getMaxX());
	        assertEpsilonEquals(40, getS().getMaxY());
	        assertEpsilonEquals(1, getS().getArcWidth());
	        assertEpsilonEquals(2, getS().getArcHeight());
	    }

	}

	@DisplayName("equals(Object)")
	@Nested
	public class EqualsObject {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().equals(null));
        }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().equals(new Object()));
        }

        @DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().equals(createRoundRectangle(0, 8, 5, 12, .1, .2)));
        }

        @DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().equals(createRoundRectangle(5, 8, 5, 0, .1, .2)));
        }

        @DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().equals(createSegment(5, 8, 5, 10)));
        }

        @DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().equals(getS()));
        }

        @DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().equals(createRoundRectangle(5, 8, 5, 10, .1, .2)));
	    }

        @DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFalse(getS().equals(createRoundRectangle(0, 8, 5, 12, .1, .2).getPathIterator()));
	    }

        @DisplayName("#9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFalse(getS().equals(createRoundRectangle(5, 8, 5, 0, .1, .2).getPathIterator()));
	    }

        @DisplayName("#10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFalse(getS().equals(createSegment(5, 8, 5, 10).getPathIterator()));
	    }

        @DisplayName("#11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertTrue(getS().equals(getS().getPathIterator()));
	    }

        @DisplayName("#12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertTrue(getS().equals(createRoundRectangle(5, 8, 5, 10, .1, .2).getPathIterator()));
        }

	}

	@DisplayName("equalsToPathIterator")
	@Nested
	public class EqualsToPathIterator {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().equalsToPathIterator((PathIterator2ai) null));
        }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().equalsToPathIterator(createRoundRectangle(0, 8, 5, 12, .1, .2).getPathIterator()));
        }

        @DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().equalsToPathIterator(createRoundRectangle(5, 8, 5, 0, .1, .2).getPathIterator()));
        }

        @DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        }

        @DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().equalsToPathIterator(createSegment(5, 8, 5, 10).getPathIterator()));
        }

        @DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().equalsToPathIterator(getS().getPathIterator()));
        }

        @DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().equalsToPathIterator(createRoundRectangle(5, 8, 5, 10, .1, .2).getPathIterator()));
	    }

	}

	@DisplayName("equalsToShape")
	@Nested
	public class EqualsToShape {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().equalsToShape(null));
        }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        	assertFalse(getS().equalsToShape((T) createRoundRectangle(0, 8, 5, 12, .1, .2)));
        }

        @DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().equalsToShape((T) createRoundRectangle(5, 8, 5, 0, .1, .2)));
        }

        @DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().equalsToShape(getS()));
        }

        @DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().equalsToShape((T) createRoundRectangle(5, 8, 5, 10, .1, .2)));
        }
    }

	@DisplayName("contains")
	@Nested
	public class Contains {

        @DisplayName("(double,double) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().contains(0, 0));
        }

        @DisplayName("(double,double) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().contains(20, 0));
        }

        @DisplayName("(double,double) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().contains(20, 20));
        }

        @DisplayName("(double,double) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().contains(0, 20));
        }

        @DisplayName("(double,double) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().contains(8, 13));
        }

        @DisplayName("(double,double) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().contains(5, 13));
        }

        @DisplayName("(double,double) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().contains(4.999, 13));
        }

        @DisplayName("(double,double) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().contains(5, 8));
        }

        @DisplayName("(double,double) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().contains(5, 18));
        }

        @DisplayName("(double,double) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().contains(15, 18));
        }

        @DisplayName("(double,double) #11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().contains(15, 8));
	    }

        @DisplayName("(Point2D) #12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFalse(getS().contains(createPoint(0, 0)));
	    }

        @DisplayName("(Point2D) #13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFalse(getS().contains(createPoint(20, 0)));
	    }

        @DisplayName("(Point2D) #14")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_14(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFalse(getS().contains(createPoint(20, 20)));
	    }

        @DisplayName("(Point2D) #15")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_15(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFalse(getS().contains(createPoint(0, 20)));
	    }

        @DisplayName("(Point2D) #16")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_16(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertTrue(getS().contains(createPoint(8, 13)));
	    }

        @DisplayName("(Point2D) #17")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_17(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertTrue(getS().contains(createPoint(5, 13)));
	    }

        @DisplayName("(Point2D) #18")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_18(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFalse(getS().contains(createPoint(4.999, 13)));
	    }

        @DisplayName("(Point2D) #19")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_19(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFalse(getS().contains(createPoint(5, 8)));
	    }

        @DisplayName("(Point2D) #20")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_20(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFalse(getS().contains(createPoint(5, 18)));
	    }

        @DisplayName("(Point2D) #21")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_21(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFalse(getS().contains(createPoint(15, 18)));
	    }

        @DisplayName("(Point2D) #22")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_22(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFalse(getS().contains(createPoint(15, 8)));
        }

        @DisplayName("(Rectangle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().contains(createRectangle(0, 0, 1, 1)));
        }

        @DisplayName("(Rectangle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().contains(createRectangle(0, 0, 7, 10)));
        }

        @DisplayName("(Rectangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().contains(createRectangle(0, 0, 20, 20)));
        }

        @DisplayName("(Rectangle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().contains(createRectangle(6, 10, 1, 1)));
        }

        @DisplayName("(Rectangle2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().contains(createRectangle(5, 8, 5, 10)));
        }

        @DisplayName("(Rectangle2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().contains(createRectangle(5.5, 8.5, 4, 9)));
	    }
	
        @DisplayName("(Circle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().contains(createCircle(0, 0, 1)));
	    }
    	
        @DisplayName("(Circle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().contains(createCircle(0, 0, 7)));
	    }
    	
        @DisplayName("(Circle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().contains(createCircle(0, 0, 20)));
	    }
    	
        @DisplayName("(Circle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().contains(createCircle(6, 10, 1)));
	    }
    	
        @DisplayName("(Circle2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().contains(createCircle(5, 8, 5)));
	    }
    	
        @DisplayName("(Circle2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().contains(createCircle(5.5, 8.5, 4)));
	    }

	}

	@DisplayName("getClosestPointTo")
	@Nested
	public class GetClosestPointTo {

        @DisplayName("(Point2D) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var p = getS().getClosestPointTo(createPoint(0, 0));
	        assertEpsilonEquals(5.06983, p.getX());
	        assertEpsilonEquals(8.00932, p.getY());
        }

        @DisplayName("(Point2D) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var p = getS().getClosestPointTo(createPoint(20, 0));
	        assertEpsilonEquals(9.95303, p.getX());
	        assertEpsilonEquals(8.03044, p.getY());
        }

        @DisplayName("(Point2D) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var p = getS().getClosestPointTo(createPoint(20, 20));
	        assertEpsilonEquals(9.99206, p.getX());
	        assertEpsilonEquals(17.8781, p.getY());
        }

        @DisplayName("(Point2D) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var p = getS().getClosestPointTo(createPoint(0, 20));
	        assertEpsilonEquals(5.02287, p.getX());
	        assertEpsilonEquals(17.92730, p.getY());
        }

        @DisplayName("(Point2D) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var p = getS().getClosestPointTo(createPoint(0, 11));
	        assertEpsilonEquals(5, p.getX());
	        assertEpsilonEquals(11, p.getY());
        }

        @DisplayName("(Point2D) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var p = getS().getClosestPointTo(createPoint(20, 11));
	        assertEpsilonEquals(10, p.getX());
	        assertEpsilonEquals(11, p.getY());
        }

        @DisplayName("(Point2D) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var p = getS().getClosestPointTo(createPoint(7, 0));
	        assertEpsilonEquals(7, p.getX());
	        assertEpsilonEquals(8, p.getY());
        }

        @DisplayName("(Point2D) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var p = getS().getClosestPointTo(createPoint(7, 20));
	        assertEpsilonEquals(7, p.getX());
	        assertEpsilonEquals(18, p.getY());
        }

        @DisplayName("(Point2D) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var p = getS().getClosestPointTo(createPoint(0, 8.2));
	        assertEpsilonEquals(5, p.getX());
	        assertEpsilonEquals(8.2, p.getY());
        }

        @DisplayName("(Point2D) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var p = getS().getClosestPointTo(createPoint(5.1, 0));
	        assertEpsilonEquals(5.1, p.getX());
	        assertEpsilonEquals(8, p.getY());
        }

        @DisplayName("(Point2D) #11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var p = getS().getClosestPointTo(createPoint(7, 10));
	        assertEpsilonEquals(7, p.getX());
	        assertEpsilonEquals(10, p.getY());
	    }

        @DisplayName("(Circle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFpPointEquals(5.06982, 8.00932, getS().getClosestPointTo(createCircle(0, 0, 1)));
	    }

        @DisplayName("(Circle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFpPointEquals(10, 14, getS().getClosestPointTo(createCircle(16, 14, 1)));
	    }

        @DisplayName("(Circle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFpPointEquals(8, 18, getS().getClosestPointTo(createCircle(8, 22, 1)));
	    }

        @DisplayName("(Circle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFpPointEquals(8, 8, getS().getClosestPointTo(createCircle(8, 0, 1)));
	    }

        @DisplayName("(Circle2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFpPointEquals(9.96998, 17.94288, getS().getClosestPointTo(createCircle(14, 20, 1)));
	    }

        @DisplayName("(Circle2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFpPointEquals(5.03849, 8.0423, getS().getClosestPointTo(createCircle(4.184131706667871, 7.494673851694933, 1)));
	    }

        @DisplayName("(Circle2afp) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertClosestPointInBothShapes(getS(), createCircle(4.188017837226872, 7.537903477557079, 1));
	    }

        @DisplayName("(Circle2afp) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertClosestPointInBothShapes(getS(), createCircle(4.5, 10, 1));
	    }

        @DisplayName("(Circle2afp) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertClosestPointInBothShapes(getS(), createCircle(5.5, 10, 1));
	    }

        @DisplayName("(Circle2afp) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertClosestPointInBothShapes(getS(), createCircle(7, 15, 1));
	    }

        @DisplayName("(Segment2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFpPointEquals(5.0721, 8.00794, getS().getClosestPointTo(createSegment(0, 0, 1, 1)));
	    }

        @DisplayName("(Segment2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFpPointEquals(5.06108, 8.01572, getS().getClosestPointTo(createSegment(0, 1, 1, 0)));
	    }

        @DisplayName("(Segment2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFpPointEquals(8, 8, getS().getClosestPointTo(createSegment(6, 4, 8, 5)));
	    }

        @DisplayName("(Segment2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertClosestPointInBothShapes(getS(), createSegment(4, 14, 6, 15));
	    }

        @DisplayName("(Segment2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertClosestPointInBothShapes(getS(), createSegment(7, 9, 9, 10));
	    }

        @DisplayName("(Segment2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFpPointEquals(9.90811, 8.00066, getS().getClosestPointTo(createSegment(
                    9.315811794580389, 7.677476922530425, 11.315811794580389, 8.677476922530425)));
        }

        @DisplayName("(Triangle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFpPointEquals(5.1, 8.0, getS().getClosestPointTo(createTestTriangle(0, 0)));
        }

        @DisplayName("(Triangle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFpPointEquals(10, 16, getS().getClosestPointTo(createTestTriangle(14, 16)));
        }

        @DisplayName("(Triangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFpPointEquals(5.02929, 17.94142, getS().getClosestPointTo(createTestTriangle(3, 20)));
        }

        @DisplayName("(Triangle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertClosestPointInBothShapes(getS(), createTestTriangle(7, 19));
        }

        @DisplayName("(Triangle2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertClosestPointInBothShapes(getS(), createTestTriangle(4, 14));
        }

        @DisplayName("(Triangle2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertClosestPointInBothShapes(getS(), createTestTriangle(6, 10));
        }

        @DisplayName("(Rectangle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFpPointEquals(5.1, 8.0, getS().getClosestPointTo(createRectangle(0, 0, 3, 2)));
        }

        @DisplayName("(Rectangle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFpPointEquals(5, 12, getS().getClosestPointTo(createRectangle(-1, 12, 3, 2)));
        }

        @DisplayName("(Rectangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFpPointEquals(5.1, 18, getS().getClosestPointTo(createRectangle(1, 20, 3, 2)));
        }

        @DisplayName("(Rectangle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertClosestPointInBothShapes(getS(), createRectangle(8.5, 8.5, 3, 2));
        }

        @DisplayName("(Rectangle2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertClosestPointInBothShapes(getS(), createRectangle(6, 10, 3, 2));
        }

        @DisplayName("(Ellipse2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFpPointEquals(5.1, 8.0, getS().getClosestPointTo(createEllipse(0, 0, 3, 2)));
        }

        @DisplayName("(Ellipse2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFpPointEquals(5, 13, getS().getClosestPointTo(createEllipse(-1, 12, 3, 2)));
        }

        @DisplayName("(Ellipse2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFpPointEquals(5.1, 18, getS().getClosestPointTo(createEllipse(1, 20, 3, 2)));
        }

        @DisplayName("(Ellipse2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertClosestPointInBothShapes(getS(), createEllipse(8.5, 8.5, 3, 2));
        }

        @DisplayName("(Ellipse2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertClosestPointInBothShapes(getS(), createEllipse(6, 10, 3, 2));
        }

        @DisplayName("(RoundRectangle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFpPointEquals(5.1, 8.0, getS().getClosestPointTo(createRoundRectangle(0, 0, 3, 2, .2, .1)));
        }

        @DisplayName("(RoundRectangle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFpPointEquals(5, 12.1, getS().getClosestPointTo(createRoundRectangle(-1, 12, 3, 2, .2, .1)));
        }

        @DisplayName("(RoundRectangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFpPointEquals(5.1, 18.0, getS().getClosestPointTo(createRoundRectangle(1, 20, 3, 2, .2, .1)));
        }

        @DisplayName("(RoundRectangle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertClosestPointInBothShapes(getS(), createRoundRectangle(8.5, 8.5, 3, 2, .2, .1));
        }

        @DisplayName("(RoundRectangle2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertClosestPointInBothShapes(getS(), createRoundRectangle(6, 10, 3, 2, .2, .1));
        }

        @DisplayName("(Path2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFpPointEquals(5.1, 8.0, getS().getClosestPointTo(createNonEmptyPath(0, 0)));
        }

        @DisplayName("(Path2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFpPointEquals(5, 12.5, getS().getClosestPointTo(createNonEmptyPath(2, 12)));
        }

        @DisplayName("(Path2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertClosestPointInBothShapes(getS(), createNonEmptyPath(9.75, 14));
        }

        @DisplayName("(Path2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertClosestPointInBothShapes(getS(), createNonEmptyPath(7, 9));
        }

        @DisplayName("(MultiShape2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void multishape_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFpPointEquals(5.04453, 8.03359, getS().getClosestPointTo(createTestMultiShape(0, 0)));
        }

        @DisplayName("(MultiShape2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void multishape_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFpPointEquals(5, 12.5, getS().getClosestPointTo(createTestMultiShape(2, 12)));
        }

        @DisplayName("(MultiShape2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void multishape_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertClosestPointInBothShapes(getS(), createTestMultiShape(9.75, 14));
        }

        @DisplayName("(MultiShape2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void multishape_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertClosestPointInBothShapes(getS(), createTestMultiShape(7, 9));
        }
        
        @DisplayName("(Parallelogram2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFpPointEquals(5.1, 8.0, getS().getClosestPointTo(createTestParallelogram(0, 0)));
        }
        
        @DisplayName("(Parallelogram2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFpPointEquals(5, 13.19218, getS().getClosestPointTo(createTestParallelogram(2, 12)));
        }
        
        @DisplayName("(Parallelogram2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertClosestPointInBothShapes(getS(), createTestParallelogram(9.75, 14));
        }
        
        @DisplayName("(Parallelogram2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertClosestPointInBothShapes(getS(), createTestParallelogram(7, 9));
        }

        @DisplayName("(OrientedRectangle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFpPointEquals(5.1, 8.0, getS().getClosestPointTo(createTestOrientedRectangle(0, 0)));
        }

        @DisplayName("(OrientedRectangle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFpPointEquals(5, 11.51493, getS().getClosestPointTo(createTestOrientedRectangle(2, 12)));
        }

        @DisplayName("(OrientedRectangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertClosestPointInBothShapes(getS(), createTestOrientedRectangle(9.75, 14));
        }

        @DisplayName("(OrientedRectangle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertClosestPointInBothShapes(getS(), createTestOrientedRectangle(7, 9));
        }

	}

	@DisplayName("getFarthestPointTo")
	@Nested
	public class GetFarthestPointTo {

        @DisplayName("(Point2D) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var p = getS().getFarthestPointTo(createPoint(0, 0));
	        assertEpsilonEquals(9.92696, p.getX());
	        assertEpsilonEquals(17.99546, p.getY());
        }

        @DisplayName("(Point2D) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var p = getS().getFarthestPointTo(createPoint(20, 0));
	        assertEpsilonEquals(5.01988, p.getX());
	        assertEpsilonEquals(8.37926, p.getY());
        }

        @DisplayName("(Point2D) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var p = getS().getFarthestPointTo(createPoint(20, 20));
	        assertEpsilonEquals(5.04194, p.getX());
	        assertEpsilonEquals(8.01391, p.getY());
        }

        @DisplayName("(Point2D) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var p = getS().getFarthestPointTo(createPoint(0, 20));
	        assertEpsilonEquals(9.93974, p.getX());
	        assertEpsilonEquals(8.00821, p.getY());
        }

        @DisplayName("(Point2D) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var p = getS().getFarthestPointTo(createPoint(0, 11));
	        assertEpsilonEquals(9.96556, p.getX());
	        assertEpsilonEquals(17.98379, p.getY());
        }

        @DisplayName("(Point2D) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var p = getS().getFarthestPointTo(createPoint(20, 11));
	        assertEpsilonEquals(4.90695, p.getX());
	        assertEpsilonEquals(8.04902, p.getY());
        }

        @DisplayName("(Point2D) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var p = getS().getFarthestPointTo(createPoint(7, 0));
	        assertEpsilonEquals(9.90806, p.getX());
	        assertEpsilonEquals(17.99945, p.getY());
        }

        @DisplayName("(Point2D) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var p = getS().getFarthestPointTo(createPoint(7, 20));
	        assertEpsilonEquals(9.91206, p.getX());
	        assertEpsilonEquals(8.00115, p.getY());
        }

        @DisplayName("(Point2D) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var p = getS().getFarthestPointTo(createPoint(7, 10));
	        assertEpsilonEquals(9.91803, p.getX());
	        assertEpsilonEquals(17.99768, p.getY());
	    }

	}

	@DisplayName("getDistance")
	@Nested
	public class GetDistance {

        @DisplayName("(Point2D) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(9.47905, getS().getDistance(createPoint(0, 0)));
        }

        @DisplayName("(Point2D) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(12.86194, getS().getDistance(createPoint(20, 0)));
        }

        @DisplayName("(Point2D) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(10.23041, getS().getDistance(createPoint(20, 20)));
        }

        @DisplayName("(Point2D) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(5.43372, getS().getDistance(createPoint(0, 20)));
        }

        @DisplayName("(Point2D) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(5, getS().getDistance(createPoint(0, 11)));
        }

        @DisplayName("(Point2D) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(10, getS().getDistance(createPoint(20, 11)));
        }

        @DisplayName("(Point2D) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(8, getS().getDistance(createPoint(7, 0)));
        }

        @DisplayName("(Point2D) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(2, getS().getDistance(createPoint(7, 20)));
        }

        @DisplayName("(Point2D) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(5, getS().getDistance(createPoint(0, 8.2)));
        }

        @DisplayName("(Point2D) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(8, getS().getDistance(createPoint(5.1, 0)));
        }

        @DisplayName("(Point2D) #11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(0, getS().getDistance(createPoint(7, 10)));
	    }

	}

	@DisplayName("getDistanceSquared")
	@Nested
	public class GetDistanceSquared {

        @DisplayName("(Point2D) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(89.85239, getS().getDistanceSquared(createPoint(0, 0)));
        }

        @DisplayName("(Point2D) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(165.4295, getS().getDistanceSquared(createPoint(20, 0)));
        }

        @DisplayName("(Point2D) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(104.66129, getS().getDistanceSquared(createPoint(20, 20)));
        }

        @DisplayName("(Point2D) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(29.52531, getS().getDistanceSquared(createPoint(0, 20)));
        }

        @DisplayName("(Point2D) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(25, getS().getDistanceSquared(createPoint(0, 11)));
        }

        @DisplayName("(Point2D) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        }

        @DisplayName("(Point2D) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(100, getS().getDistanceSquared(createPoint(20, 11)));
        }

        @DisplayName("(Point2D) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(64, getS().getDistanceSquared(createPoint(7, 0)));
        }

        @DisplayName("(Point2D) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(4, getS().getDistanceSquared(createPoint(7, 20)));
        }

        @DisplayName("(Point2D) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(25, getS().getDistanceSquared(createPoint(0, 8.2)));
        }

        @DisplayName("(Point2D) #11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(64, getS().getDistanceSquared(createPoint(5.1, 0)));
        }

        @DisplayName("(Point2D) #12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(0, getS().getDistanceSquared(createPoint(7, 10)));
        }

        @DisplayName("(Point2D) #13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(0, getS().getDistanceSquared(createPoint(7.5, 8.4)));
	    }

        @DisplayName("(Circle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(71.89428, getS().getDistanceSquared(createCircle(0, 0, 1)));
	    }

        @DisplayName("(Circle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(25, getS().getDistanceSquared(createCircle(16, 14, 1)));
	    }

        @DisplayName("(Circle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(9, getS().getDistanceSquared(createCircle(8, 22, 1)));
	    }

        @DisplayName("(Circle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(49, getS().getDistanceSquared(createCircle(8, 0, 1)));
	    }

        @DisplayName("(Circle2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(12.42347, getS().getDistanceSquared(createCircle(14, 20, 1)));
	    }

        @DisplayName("(Circle2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(.00022, getS().getDistanceSquared(createCircle(4.184131706667871, 7.494673851694933, 1)));
	    }

        @DisplayName("(Circle2afp) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0, getS().getDistanceSquared(createCircle(4.188017837226872, 7.537903477557079, 1)));
	    }

        @DisplayName("(Circle2afp) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0, getS().getDistanceSquared(createCircle(4.5, 10, 1)));
	    }

        @DisplayName("(Circle2afp) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0, getS().getDistanceSquared(createCircle(5.5, 10, 1)));
	    }

        @DisplayName("(Circle2afp) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0, getS().getDistanceSquared(createCircle(7, 15, 1)));
        }

        @DisplayName("(Segment2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(65.69325, getS().getDistanceSquared(createSegment(0, 0, 1, 1)));
        }

        @DisplayName("(Segment2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(74.83131, getS().getDistanceSquared(createSegment(0, 1, 1, 0)));
        }

        @DisplayName("(Segment2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(9, getS().getDistanceSquared(createSegment(6, 4, 8, 5)));
        }

        @DisplayName("(Segment2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0, getS().getDistanceSquared(createSegment(4, 14, 6, 15)));
        }

        @DisplayName("(Segment2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0, getS().getDistanceSquared(createSegment(7, 9, 9, 10)));
        }

        @DisplayName("(Segment2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0.000585, getS().getDistanceSquared(createSegment(
                    9.315811794580389, 7.677476922530425, 11.315811794580389, 8.677476922530425)));
        }

        @DisplayName("(Triangle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(105.11999, getS().getDistanceSquared(createTestTriangle(0, 0)));
        }

        @DisplayName("(Triangle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(16, getS().getDistanceSquared(createTestTriangle(14, 16)));
        }

        @DisplayName("(Triangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0.49044, getS().getDistanceSquared(createTestTriangle(3, 20)));
        }

        @DisplayName("(Triangle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0, getS().getDistanceSquared(createTestTriangle(7, 19)));
        }

        @DisplayName("(Triangle2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0, getS().getDistanceSquared(createTestTriangle(4, 14)));
        }

        @DisplayName("(Triangle2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0, getS().getDistanceSquared(createTestTriangle(6, 10)));
        }

        @DisplayName("(Rectangle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(40.40999, getS().getDistanceSquared(createRectangle(0, 0, 3, 2)));
        }

        @DisplayName("(Rectangle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(9, getS().getDistanceSquared(createRectangle(-1, 12, 3, 2)));
        }

        @DisplayName("(Rectangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(5.20999, getS().getDistanceSquared(createRectangle(1, 20, 3, 2)));
        }

        @DisplayName("(Rectangle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0, getS().getDistanceSquared(createRectangle(8.5, 8.5, 3, 2)));
        }

        @DisplayName("(Rectangle2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0, getS().getDistanceSquared(createRectangle(6, 10, 3, 2)));
        }

        @DisplayName("(Ellipse2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(45.70503, getS().getDistanceSquared(createEllipse(0, 0, 3, 2)));
        }

        @DisplayName("(Ellipse2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(9, getS().getDistanceSquared(createEllipse(-1, 12, 3, 2)));
        }

        @DisplayName("(Ellipse2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(7.6269, getS().getDistanceSquared(createEllipse(1, 20, 3, 2)));
        }

        @DisplayName("(Ellipse2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0, getS().getDistanceSquared(createEllipse(8.5, 8.5, 3, 2)));
        }

        @DisplayName("(Ellipse2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0, getS().getDistanceSquared(createEllipse(6, 10, 3, 2)));
        }

        @DisplayName("(RoundRectangle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(40.9926, getS().getDistanceSquared(createRoundRectangle(0, 0, 3, 2, .2, .1)));
        }

        @DisplayName("(RoundRectangle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(9, getS().getDistanceSquared(createRoundRectangle(-1, 12, 3, 2, .2, .1)));
        }

        @DisplayName("(RoundRectangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(5.45907, getS().getDistanceSquared(createRoundRectangle(1, 20, 3, 2, .2, .1)));
        }

        @DisplayName("(RoundRectangle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0, getS().getDistanceSquared(createRoundRectangle(8.5, 8.5, 3, 2, .2, .1)));
        }

        @DisplayName("(RoundRectangle2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0, getS().getDistanceSquared(createRoundRectangle(6, 10, 3, 2, .2, .1)));
        }

        @DisplayName("(Path2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(73.05999, getS().getDistanceSquared(createNonEmptyPath(0, 0)));
        }

        @DisplayName("(Path2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(4, getS().getDistanceSquared(createNonEmptyPath(2, 12)));
        }

        @DisplayName("(Path2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0, getS().getDistanceSquared(createNonEmptyPath(9.75, 14)));
        }

        @DisplayName("(Path2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0, getS().getDistanceSquared(createNonEmptyPath(7, 9)));
        }

        @DisplayName("(MultiShape2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void multishape_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(64.89556, getS().getDistanceSquared(createTestMultiShape(0, 0)));
        }

        @DisplayName("(MultiShape2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void multishape_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0, getS().getDistanceSquared(createTestMultiShape(2, 12)));
        }

        @DisplayName("(MultiShape2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void multishape_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0, getS().getDistanceSquared(createTestMultiShape(9.75, 14)));
        }

        @DisplayName("(MultiShape2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void multishape_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0, getS().getDistanceSquared(createTestMultiShape(7, 9)));
        }

        @DisplayName("(Parallelogram2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(52.36173, getS().getDistanceSquared(createTestParallelogram(0, 0)));
        }

        @DisplayName("(Parallelogram2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0.12433, getS().getDistanceSquared(createTestParallelogram(2, 12)));
        }

        @DisplayName("(Parallelogram2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0, getS().getDistanceSquared(createTestParallelogram(9.75, 14)));
        }

        @DisplayName("(Parallelogram2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0, getS().getDistanceSquared(createTestParallelogram(7, 9)));
        }

        @DisplayName("(OrientedRectangle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(54.40957, getS().getDistanceSquared(createTestOrientedRectangle(0, 0)));
        }

        @DisplayName("(OrientedRectangle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0.66778, getS().getDistanceSquared(createTestOrientedRectangle(2, 12)));
        }

        @DisplayName("(OrientedRectangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0, getS().getDistanceSquared(createTestOrientedRectangle(9.75, 14)));
        }

        @DisplayName("(OrientedRectangle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0, getS().getDistanceSquared(createTestOrientedRectangle(7, 9)));
        }

	}

	@DisplayName("getDistanceL1")
	@Nested
	public class GetDistanceL1 {

        @DisplayName("(Point2D) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(13.07915, getS().getDistanceL1(createPoint(0, 0)));
        }

        @DisplayName("(Point2D) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(18.07741, getS().getDistanceL1(createPoint(20, 0)));
        }

        @DisplayName("(Point2D) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(12.12984, getS().getDistanceL1(createPoint(20, 20)));
        }

        @DisplayName("(Point2D) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(7.09557, getS().getDistanceL1(createPoint(0, 20)));
        }

        @DisplayName("(Point2D) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        }

        @DisplayName("(Point2D) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(5, getS().getDistanceL1(createPoint(0, 11)));
        }

        @DisplayName("(Point2D) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        }

        @DisplayName("(Point2D) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        }

        @DisplayName("(Point2D) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(10, getS().getDistanceL1(createPoint(20, 11)));
        }

        @DisplayName("(Point2D) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(8, getS().getDistanceL1(createPoint(7, 0)));
        }

        @DisplayName("(Point2D) #11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(2, getS().getDistanceL1(createPoint(7, 20)));
        }

        @DisplayName("(Point2D) #12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(5, getS().getDistanceL1(createPoint(0, 8.2)));
        }

        @DisplayName("(Point2D) #13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(8, getS().getDistanceL1(createPoint(5.1, 0)));
        }

        @DisplayName("(Point2D) #14")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_14(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(0, getS().getDistanceL1(createPoint(7, 10)));
	    }

	}

	@DisplayName("getDistanceLinf")
	@Nested
	public class GetDistanceLinf {

        @DisplayName("(Point2D) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(8.00932, getS().getDistanceLinf(createPoint(0, 0)));
        }

        @DisplayName("(Point2D) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(10.04697, getS().getDistanceLinf(createPoint(20, 0)));
        }

        @DisplayName("(Point2D) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(10.007934, getS().getDistanceLinf(createPoint(20, 20)));
        }

        @DisplayName("(Point2D) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(5.02287, getS().getDistanceLinf(createPoint(0, 20)));
        }

        @DisplayName("(Point2D) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(5, getS().getDistanceLinf(createPoint(0, 11)));
        }

        @DisplayName("(Point2D) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(10, getS().getDistanceLinf(createPoint(20, 11)));
        }

        @DisplayName("(Point2D) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(8, getS().getDistanceLinf(createPoint(7, 0)));
        }

        @DisplayName("(Point2D) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(2, getS().getDistanceLinf(createPoint(7, 20)));
        }

        @DisplayName("(Point2D) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(5, getS().getDistanceLinf(createPoint(0, 8.2)));
        }

        @DisplayName("(Point2D) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        }

        @DisplayName("(Point2D) #11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(8, getS().getDistanceLinf(createPoint(5.1, 0)));
        }

        @DisplayName("(Point2D) #12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(0, getS().getDistanceLinf(createPoint(7, 10)));
	    }

	}

	@DisplayName("set(IT)")
	@Nested
	public class SetIT {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        getS().set((T) createRoundRectangle(10, 20, 30, 40, 1, 2));
	        assertEpsilonEquals(10, getS().getMinX());
	        assertEpsilonEquals(20, getS().getMinY());
	        assertEpsilonEquals(40, getS().getMaxX());
	        assertEpsilonEquals(60, getS().getMaxY());
	        assertEpsilonEquals(1, getS().getArcWidth());
	        assertEpsilonEquals(2, getS().getArcHeight());
	    }

	}

	@DisplayName("getPathIterator")
	@Nested
	public class GetPathIterator {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        PathIterator2afp pi = getS().getPathIterator();
	        assertElement(pi, PathElementType.MOVE_TO, 5.1, 8);
	        assertElement(pi, PathElementType.LINE_TO, 9.9, 8);
	        assertElement(pi, PathElementType.CURVE_TO, 9.95523, 8, 10, 8.08954, 10, 8.2);
	        assertElement(pi, PathElementType.LINE_TO, 10, 17.8);
	        assertElement(pi, PathElementType.CURVE_TO, 10, 17.91046, 9.95523, 18, 9.9, 18);
	        assertElement(pi, PathElementType.LINE_TO, 5.1, 18);
	        assertElement(pi, PathElementType.CURVE_TO, 5.04477, 18, 5, 17.91046, 5, 17.8);
	        assertElement(pi, PathElementType.LINE_TO, 5, 8.2);
	        assertElement(pi, PathElementType.CURVE_TO, 5, 8.08954, 5.04477, 8, 5.1, 8);
	        assertElement(pi, PathElementType.CLOSE, 5.1, 8);
	        assertNoElement(pi);
	    }

	}

	@DisplayName("getPathIterator(Transform2D)")
	@Nested
	public class GetPathIteratorTransform2D {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var pi = getS().getPathIterator(null);
	        assertElement(pi, PathElementType.MOVE_TO, 5.1, 8);
	        assertElement(pi, PathElementType.LINE_TO, 9.9, 8);
	        assertElement(pi, PathElementType.CURVE_TO, 9.95523, 8, 10, 8.08954, 10, 8.2);
	        assertElement(pi, PathElementType.LINE_TO, 10, 17.8);
	        assertElement(pi, PathElementType.CURVE_TO, 10, 17.91046, 9.95523, 18, 9.9, 18);
	        assertElement(pi, PathElementType.LINE_TO, 5.1, 18);
	        assertElement(pi, PathElementType.CURVE_TO, 5.04477, 18, 5, 17.91046, 5, 17.8);
	        assertElement(pi, PathElementType.LINE_TO, 5, 8.2);
	        assertElement(pi, PathElementType.CURVE_TO, 5, 8.08954, 5.04477, 8, 5.1, 8);
	        assertElement(pi, PathElementType.CLOSE, 5.1, 8);
	        assertNoElement(pi);
        }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var pi = getS().getPathIterator(new Transform2D());
	        assertElement(pi, PathElementType.MOVE_TO, 5.1, 8);
	        assertElement(pi, PathElementType.LINE_TO, 9.9, 8);
	        assertElement(pi, PathElementType.CURVE_TO, 9.95523, 8, 10, 8.08954, 10, 8.2);
	        assertElement(pi, PathElementType.LINE_TO, 10, 17.8);
	        assertElement(pi, PathElementType.CURVE_TO, 10, 17.91046, 9.95523, 18, 9.9, 18);
	        assertElement(pi, PathElementType.LINE_TO, 5.1, 18);
	        assertElement(pi, PathElementType.CURVE_TO, 5.04477, 18, 5, 17.91046, 5, 17.8);
	        assertElement(pi, PathElementType.LINE_TO, 5, 8.2);
	        assertElement(pi, PathElementType.CURVE_TO, 5, 8.08954, 5.04477, 8, 5.1, 8);
	        assertElement(pi, PathElementType.CLOSE, 5.1, 8);
	        assertNoElement(pi);
        }

        @DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        Transform2D tr = new Transform2D();
	        tr.setTranslation(10, -1);
	        var pi = getS().getPathIterator(tr);
	        assertElement(pi, PathElementType.MOVE_TO, 15.1, 7);
	        assertElement(pi, PathElementType.LINE_TO, 19.9, 7);
	        assertElement(pi, PathElementType.CURVE_TO, 19.95523, 7, 20, 7.08954, 20, 7.2);
	        assertElement(pi, PathElementType.LINE_TO, 20, 16.8);
	        assertElement(pi, PathElementType.CURVE_TO, 20, 16.91046, 19.95523, 17, 19.9, 17);
	        assertElement(pi, PathElementType.LINE_TO, 15.1, 17);
	        assertElement(pi, PathElementType.CURVE_TO, 15.04477, 17, 15, 16.91046, 15, 16.8);
	        assertElement(pi, PathElementType.LINE_TO, 15, 7.2);
	        assertElement(pi, PathElementType.CURVE_TO, 15, 7.08954, 15.04477, 7, 15.1, 7);
	        assertElement(pi, PathElementType.CLOSE, 15.1, 7);
	        assertNoElement(pi);
	    }

	}

	@DisplayName("createTransformedShape")
	@Nested
	public class CreateTransformedShape {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var pi = getS().createTransformedShape(null).getPathIterator();
	        assertElement(pi, PathElementType.MOVE_TO, 5.1, 8);
	        assertElement(pi, PathElementType.LINE_TO, 9.9, 8);
	        assertElement(pi, PathElementType.CURVE_TO, 9.95523, 8, 10, 8.08954, 10, 8.2);
	        assertElement(pi, PathElementType.LINE_TO, 10, 17.8);
	        assertElement(pi, PathElementType.CURVE_TO, 10, 17.91046, 9.95523, 18, 9.9, 18);
	        assertElement(pi, PathElementType.LINE_TO, 5.1, 18);
	        assertElement(pi, PathElementType.CURVE_TO, 5.04477, 18, 5, 17.91046, 5, 17.8);
	        assertElement(pi, PathElementType.LINE_TO, 5, 8.2);
	        assertElement(pi, PathElementType.CURVE_TO, 5, 8.08954, 5.04477, 8, 5.1, 8);
	        assertElement(pi, PathElementType.CLOSE, 5.1, 8);
	        assertNoElement(pi);
        }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var pi = getS().createTransformedShape(new Transform2D()).getPathIterator();
	        assertElement(pi, PathElementType.MOVE_TO, 5.1, 8);
	        assertElement(pi, PathElementType.LINE_TO, 9.9, 8);
	        assertElement(pi, PathElementType.CURVE_TO, 9.95523, 8, 10, 8.08954, 10, 8.2);
	        assertElement(pi, PathElementType.LINE_TO, 10, 17.8);
	        assertElement(pi, PathElementType.CURVE_TO, 10, 17.91046, 9.95523, 18, 9.9, 18);
	        assertElement(pi, PathElementType.LINE_TO, 5.1, 18);
	        assertElement(pi, PathElementType.CURVE_TO, 5.04477, 18, 5, 17.91046, 5, 17.8);
	        assertElement(pi, PathElementType.LINE_TO, 5, 8.2);
	        assertElement(pi, PathElementType.CURVE_TO, 5, 8.08954, 5.04477, 8, 5.1, 8);
	        assertElement(pi, PathElementType.CLOSE, 5.1, 8);
	        assertNoElement(pi);
        }

        @DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        Transform2D tr = new Transform2D();
	        tr.setTranslation(10, -1);
	        var pi = getS().createTransformedShape(tr).getPathIterator();
	        assertElement(pi, PathElementType.MOVE_TO, 15.1, 7);
	        assertElement(pi, PathElementType.LINE_TO, 19.9, 7);
	        assertElement(pi, PathElementType.CURVE_TO, 19.95523, 7, 20, 7.08954, 20, 7.2);
	        assertElement(pi, PathElementType.LINE_TO, 20, 16.8);
	        assertElement(pi, PathElementType.CURVE_TO, 20, 16.91046, 19.95523, 17, 19.9, 17);
	        assertElement(pi, PathElementType.LINE_TO, 15.1, 17);
	        assertElement(pi, PathElementType.CURVE_TO, 15.04477, 17, 15, 16.91046, 15, 16.8);
	        assertElement(pi, PathElementType.LINE_TO, 15, 7.2);
	        assertElement(pi, PathElementType.CURVE_TO, 15, 7.08954, 15.04477, 7, 15.1, 7);
	        assertElement(pi, PathElementType.CLOSE, 15.1, 7);
	        assertNoElement(pi);
	    }

	}

	@DisplayName("intersects")
	@Nested
	public class Intersects {

        @DisplayName("(Rectangle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().intersects(createRectangle(0, 0, 1, 1)));
        }

        @DisplayName("(Rectangle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().intersects(createRectangle(20, 20, 21, 21)));
        }

        @DisplayName("(Rectangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().intersects(createRectangle(0, 0, 7, 12)));
        }

        @DisplayName("(Rectangle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().intersects(createRectangle(0, 0, 7, 8)));
        }

        @DisplayName("(Rectangle2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().intersects(createRectangle(0, 0, 5.01, 8.01)));
        }

        @DisplayName("(Rectangle2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().intersects(createRectangle(0, 0, 5.05, 8.05)));
        }

        @DisplayName("(Rectangle2afp) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().intersects(createRectangle(0, 0, 5.05, 8.1)));
        }

        @DisplayName("(Rectangle2afp) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().intersects(createRectangle(0, 0, 5.05, 8.15)));
	    }

        @DisplayName("(Circle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().intersects(createCircle(7, 0, 1)));
	    }

        @DisplayName("(Circle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().intersects(createCircle(7, 20, 1)));
	    }

        @DisplayName("(Circle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().intersects(createCircle(0, 12, 1)));
	    }

        @DisplayName("(Circle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().intersects(createCircle(20, 12, 1)));
	    }

        @DisplayName("(Circle2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().intersects(createCircle(0, 0, 1)));
	    }

        @DisplayName("(Circle2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().intersects(createCircle(20, 0, 1)));
	    }

        @DisplayName("(Circle2afp) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().intersects(createCircle(20, 20, 1)));
	    }

        @DisplayName("(Circle2afp) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().intersects(createCircle(0, 20, 1)));
	    }

        @DisplayName("(Circle2afp) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().intersects(createCircle(4, 12, 1)));
	    }

        @DisplayName("(Circle2afp) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().intersects(createCircle(4.1, 12, 1)));
	    }

        @DisplayName("(Circle2afp) #11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().intersects(createCircle(6, 12, 1)));
	    }

        @DisplayName("(Circle2afp) #12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().intersects(createCircle(10.9, 12, 1)));
	    }

        @DisplayName("(Circle2afp) #13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().intersects(createCircle(11, 12, 1)));
	    }

        @DisplayName("(Circle2afp) #14")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_14(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().intersects(createCircle(7, 7, 1)));
	    }

        @DisplayName("(Circle2afp) #15")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_15(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().intersects(createCircle(7, 7.1, 1)));
	    }

        @DisplayName("(Circle2afp) #16")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_16(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().intersects(createCircle(7, 12, 1)));
	    }

        @DisplayName("(Circle2afp) #17")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_17(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().intersects(createCircle(7, 18.9, 1)));
	    }

        @DisplayName("(Circle2afp) #18")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_18(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().intersects(createCircle(7, 19, 1)));
	    }

        @DisplayName("(Circle2afp) #19")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_19(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().intersects(createCircle(4.32, 7.32, 1)));
	    }

        @DisplayName("(Circle2afp) #20")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_20(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().intersects(createCircle(4.4, 7.4, 1)));
	    }

        @DisplayName("(Circle2afp) #21")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_21(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().intersects(createCircle(4.75, 7.75, 1)));
	    }

        @DisplayName("(Circle2afp) #22")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_22(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().intersects(createCircle(4.19, 7.55, 1)));
	    }

        @DisplayName("(Ellipse2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().intersects(createEllipse(6, -.5, 2, 1)));
	    }

        @DisplayName("(Ellipse2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().intersects(createEllipse(6, 19.5, 2, 1)));
	    }

        @DisplayName("(Ellipse2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().intersects(createEllipse(-1, 11.5, 2, 1)));
	    }

        @DisplayName("(Ellipse2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().intersects(createEllipse(19, 11.5, 2, 1)));
	    }

        @DisplayName("(Ellipse2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().intersects(createEllipse(-1, -.5, 2, 1)));
	    }

        @DisplayName("(Ellipse2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().intersects(createEllipse(19, -.5, 2, 1)));
	    }

        @DisplayName("(Ellipse2afp) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().intersects(createEllipse(19, 19.5, 2, 1)));
	    }

        @DisplayName("(Ellipse2afp) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().intersects(createEllipse(-1, 19.5, 2, 1)));
	    }

        @DisplayName("(Ellipse2afp) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().intersects(createEllipse(3, 11.5, 2, 1)));
	    }

        @DisplayName("(Ellipse2afp) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().intersects(createEllipse(3.1, 11.5, 2, 1)));
	    }

        @DisplayName("(Ellipse2afp) #11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().intersects(createEllipse(5, 11.5, 2, 1)));
	    }

        @DisplayName("(Ellipse2afp) #12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().intersects(createEllipse(9.9, 11.5, 2, 1)));
	    }

        @DisplayName("(Ellipse2afp) #13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().intersects(createEllipse(10, 11.5, 2, 1)));
	    }

        @DisplayName("(Ellipse2afp) #14")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_14(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().intersects(createEllipse(6, 6.5, 2, 1)));
	    }

        @DisplayName("(Ellipse2afp) #15")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_15(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().intersects(createEllipse(6, 6.6, 2, 1)));
	    }

        @DisplayName("(Ellipse2afp) #16")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_16(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().intersects(createEllipse(6, 11.5, 2, 1)));
	    }

        @DisplayName("(Ellipse2afp) #17")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_17(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().intersects(createEllipse(6, 18.4, 2, 1)));
	    }

        @DisplayName("(Ellipse2afp) #18")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_18(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().intersects(createEllipse(6, 18.5, 2, 1)));
	    }

        @DisplayName("(Ellipse2afp) #19")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_19(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().intersects(createEllipse(3.32, 6.82, 2, 1)));
	    }

        @DisplayName("(Ellipse2afp) #20")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_20(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().intersects(createEllipse(3.4, 6.9, 2, 1)));
	    }

        @DisplayName("(Ellipse2afp) #21")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_21(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().intersects(createEllipse(3.75, 7.25, 2, 1)));
	    }

        @DisplayName("(Ellipse2afp) #22")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_22(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().intersects(createEllipse(3.19, 7.05, 2, 1)));
	    }

        @DisplayName("(Ellipse2afp) #23")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_23(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().intersects(createEllipse(3.08, 7.45, 2, 1)));
	    }

        @DisplayName("(Segment2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().intersects(createSegment(0, 0, 1, 1)));
	    }

        @DisplayName("(Segment2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().intersects(createSegment(20, 20, 21, 21)));
	    }

        @DisplayName("(Segment2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().intersects(createSegment(0, 0, 7, 12)));
	    }

        @DisplayName("(Segment2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().intersects(createSegment(0, 0, 7, 8)));
	    }

        @DisplayName("(Segment2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().intersects(createSegment(6, 7, 4.1, 9)));
	    }

        @DisplayName("(Segment2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().intersects(createSegment(6.1, 7, 4.1, 9)));
	    }

        @DisplayName("(Triangle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        Triangle2afp triangle = createTriangle(5, 8, -10, 1, -1, -2);
	        assertTrue(createRoundRectangle(0, 0, 1, 1, .2, .4).intersects(triangle));
	    }

        @DisplayName("(Triangle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        Triangle2afp triangle = createTriangle(5, 8, -10, 1, -1, -2);
	        assertTrue(createRoundRectangle(0, 2, 1, 1, .2, .4).intersects(triangle));
	    }

        @DisplayName("(Triangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        Triangle2afp triangle = createTriangle(5, 8, -10, 1, -1, -2);
	        assertTrue(createRoundRectangle(0, 3, 1, 1, .2, .4).intersects(triangle));
	    }

        @DisplayName("(Triangle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        Triangle2afp triangle = createTriangle(5, 8, -10, 1, -1, -2);
	    }

        @DisplayName("(Triangle2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        Triangle2afp triangle = createTriangle(5, 8, -10, 1, -1, -2);
	        assertTrue(createRoundRectangle(0, 4, 1, 1, .2, .4).intersects(triangle));
	    }

        @DisplayName("(Triangle2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        Triangle2afp triangle = createTriangle(5, 8, -10, 1, -1, -2);
	        assertTrue(createRoundRectangle(0, 5, 1, 1, .2, .4).intersects(triangle));
	    }

        @DisplayName("(Triangle2afp) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        Triangle2afp triangle = createTriangle(5, 8, -10, 1, -1, -2);
	        assertTrue(createRoundRectangle(0, 6, 1, 1, .2, .4).intersects(triangle));
	    }

        @DisplayName("(Triangle2afp) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        Triangle2afp triangle = createTriangle(5, 8, -10, 1, -1, -2);
	        assertTrue(createRoundRectangle(0, 6.05, 1, 1, .2, .4).intersects(triangle));
	    }

        @DisplayName("(Triangle2afp) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        Triangle2afp triangle = createTriangle(5, 8, -10, 1, -1, -2);
	        assertFalse(createRoundRectangle(0, 6.06, 1, 1, .2, .4).intersects(triangle));
	    }

        @DisplayName("(Triangle2afp) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        Triangle2afp triangle = createTriangle(5, 8, -10, 1, -1, -2);
	        assertFalse(createRoundRectangle(4.5, 8, 1, 1, .2, .4).intersects(triangle));
	    }

        @DisplayName("(Path2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var p = createPath();
	        p.moveTo(-20, -20);
	        p.lineTo(-20, 20);
	        p.lineTo(20, 20);
	        p.lineTo(20, -20);
	        assertFalse(getS().intersects(p));
	    }

        @DisplayName("(Path2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var p = createPath();
	        p.moveTo(-20, -20);
	        p.lineTo(-20, 20);
	        p.lineTo(20, 20);
	        p.lineTo(20, -20);
	        p.closePath();
	        assertTrue(getS().intersects(p));
	    }

        @DisplayName("(Path2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var p = createPath();
	        p.moveTo(-20, -20);
	        p.lineTo(5, 8);
	        p.lineTo(-20, 20);
	        assertFalse(getS().intersects(p));
	    }

        @DisplayName("(Path2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var p = createPath();
	        p.moveTo(-20, -20);
	        p.lineTo(5, 8);
	        p.lineTo(-20, 20);
	        p.closePath();
	        assertFalse(getS().intersects(p));
	    }

        @DisplayName("(Path2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var p = createPath();
	        p.moveTo(-20, -20);
	        p.lineTo(30, 20);
	        p.lineTo(-20, 20);
	        assertFalse(getS().intersects(p));
	    }

        @DisplayName("(Path2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var p = createPath();
	        p.moveTo(-20, -20);
	        p.lineTo(30, 20);
	        p.lineTo(-20, 20);
	        p.closePath();
	        assertTrue(getS().intersects(p));
	    }

        @DisplayName("(Path2afp) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var p = createPath();
	        p.moveTo(-20, -20);
	        p.lineTo(-20, 20);
	        p.lineTo(20, -20);
	        assertFalse(getS().intersects(p));
	    }

        @DisplayName("(Path2afp) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var p = createPath();
	        p.moveTo(-20, -20);
	        p.lineTo(-20, 20);
	        p.lineTo(20, -20);
	        p.closePath();
	        assertFalse(getS().intersects(p));
	    }

        @DisplayName("(Path2afp) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var p = createPath();
	        p.moveTo(-20, 20);
	        p.lineTo(10, 8);
	        p.lineTo(20, 18);
	        assertTrue(getS().intersects(p));
	    }

        @DisplayName("(Path2afp) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var p = createPath();
	        p.moveTo(-20, 20);
	        p.lineTo(10, 8);
	        p.lineTo(20, 18);
	        p.closePath();
	        assertTrue(getS().intersects(p));
	    }

        @DisplayName("(Path2afp) #11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var p = createPath();
	        p.moveTo(-20, 20);
	        p.lineTo(20, 18);
	        p.lineTo(10, 8);
	        assertFalse(getS().intersects(p));
	    }

        @DisplayName("(Path2afp) #12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var p = createPath();
	        p.moveTo(-20, 20);
	        p.lineTo(20, 18);
	        p.lineTo(10, 8);
	        p.closePath();
	        assertTrue(getS().intersects(p));
	    }

        @DisplayName("(PathIterator2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void pathiterator_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var p = createPath();
	        p.moveTo(-20, -20);
	        p.lineTo(-20, 20);
	        p.lineTo(20, 20);
	        p.lineTo(20, -20);
	        assertFalse(getS().intersects(p.getPathIterator()));
	    }

        @DisplayName("(PathIterator2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void pathiterator_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var p = createPath();
	        p.moveTo(-20, -20);
	        p.lineTo(-20, 20);
	        p.lineTo(20, 20);
	        p.lineTo(20, -20);
	        p.closePath();
	        assertTrue(getS().intersects(p.getPathIterator()));
	    }

        @DisplayName("(PathIterator2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void pathiterator_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var p = createPath();
	        p.moveTo(-20, -20);
	        p.lineTo(5, 8);
	        p.lineTo(-20, 20);
	        assertFalse(getS().intersects(p.getPathIterator()));
	    }

        @DisplayName("(PathIterator2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void pathiterator_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var p = createPath();
	        p.moveTo(-20, -20);
	        p.lineTo(5, 8);
	        p.lineTo(-20, 20);
	        p.closePath();
	        assertFalse(getS().intersects(p.getPathIterator()));
	    }

        @DisplayName("(PathIterator2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void pathiterator_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var p = createPath();
	        p.moveTo(-20, -20);
	        p.lineTo(30, 20);
	        p.lineTo(-20, 20);
	        assertFalse(getS().intersects(p.getPathIterator()));
	    }

        @DisplayName("(PathIterator2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void pathiterator_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var p = createPath();
	        p.moveTo(-20, -20);
	        p.lineTo(30, 20);
	        p.lineTo(-20, 20);
	        p.closePath();
	        assertTrue(getS().intersects(p.getPathIterator()));
	    }

        @DisplayName("(PathIterator2afp) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void pathiterator_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var p = createPath();
	        p.moveTo(-20, -20);
	        p.lineTo(-20, 20);
	        p.lineTo(20, -20);
	        assertFalse(getS().intersects(p.getPathIterator()));
	    }

        @DisplayName("(PathIterator2afp) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void pathiterator_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var p = createPath();
	        p.moveTo(-20, -20);
	        p.lineTo(-20, 20);
	        p.lineTo(20, -20);
	        p.closePath();
	        assertFalse(getS().intersects(p.getPathIterator()));
	    }

        @DisplayName("(PathIterator2afp) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void pathiterator_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var p = createPath();
	        p.moveTo(-20, 20);
	        p.lineTo(10, 8);
	        p.lineTo(20, 18);
	        assertTrue(getS().intersects(p.getPathIterator()));
	    }

        @DisplayName("(PathIterator2afp) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void pathiterator_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var p = createPath();
	        p.moveTo(-20, 20);
	        p.lineTo(10, 8);
	        p.lineTo(20, 18);
	        p.closePath();
	        assertTrue(getS().intersects(p.getPathIterator()));
	    }

        @DisplayName("(PathIterator2afp) #11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void pathiterator_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var p = createPath();
	        p.moveTo(-20, 20);
	        p.lineTo(20, 18);
	        p.lineTo(10, 8);
	        assertFalse(getS().intersects(p.getPathIterator()));
	    }

        @DisplayName("(PathIterator2afp) #12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void pathiterator_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var p = createPath();
	        p.moveTo(-20, 20);
	        p.lineTo(20, 18);
	        p.lineTo(10, 8);
	        p.closePath();
	        assertTrue(getS().intersects(p.getPathIterator()));
	    }

        @DisplayName("(OrientedRectangle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        OrientedRectangle2afp rectangle = createOrientedRectangle(
	                6, 9,
	                0.894427190999916, -0.447213595499958, 13.999990000000002,
	                12.999989999999997);
	        assertTrue(createRoundRectangle(0, 0, 2, 1, .1, .05).intersects(rectangle));
	    }

        @DisplayName("(OrientedRectangle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        OrientedRectangle2afp rectangle = createOrientedRectangle(
	                6, 9,
	                0.894427190999916, -0.447213595499958, 13.999990000000002,
	                12.999989999999997);
	        assertFalse(createRoundRectangle(-9, 15, 2, 1, .1, .05).intersects(rectangle));
	    }

        @DisplayName("(OrientedRectangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        OrientedRectangle2afp rectangle = createOrientedRectangle(
	                6, 9,
	                0.894427190999916, -0.447213595499958, 13.999990000000002,
	                12.999989999999997);
	        assertFalse(createRoundRectangle(-8.7, 15, 2, 1, .1, .05).intersects(rectangle));
	    }

        @DisplayName("(OrientedRectangle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        OrientedRectangle2afp rectangle = createOrientedRectangle(
	                6, 9,
	                0.894427190999916, -0.447213595499958, 13.999990000000002,
	                12.999989999999997);
	        assertFalse(createRoundRectangle(-8.7, 15, 2, 1, .1, .05).intersects(rectangle));
	    }

        @DisplayName("(OrientedRectangle2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        OrientedRectangle2afp rectangle = createOrientedRectangle(
	                6, 9,
	                0.894427190999916, -0.447213595499958, 13.999990000000002,
	                12.999989999999997);
	        assertFalse(createRoundRectangle(-8.65, 15, 2, 1, .1, .05).intersects(rectangle));
	    }

        @DisplayName("(OrientedRectangle2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        OrientedRectangle2afp rectangle = createOrientedRectangle(
	                6, 9,
	                0.894427190999916, -0.447213595499958, 13.999990000000002,
	                12.999989999999997);
	        assertFalse(createRoundRectangle(-8.64, 15, 2, 1, .1, .05).intersects(rectangle));
	    }

        @DisplayName("(OrientedRectangle2afp) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        OrientedRectangle2afp rectangle = createOrientedRectangle(
	                6, 9,
	                0.894427190999916, -0.447213595499958, 13.999990000000002,
	                12.999989999999997);
	        assertFalse(createRoundRectangle(-8.63, 15, 2, 1, .1, .05).intersects(rectangle));
	    }

        @DisplayName("(OrientedRectangle2afp) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        OrientedRectangle2afp rectangle = createOrientedRectangle(
	                6, 9,
	                0.894427190999916, -0.447213595499958, 13.999990000000002,
	                12.999989999999997);
	        assertTrue(createRoundRectangle(-8.62, 15, 2, 1, .1, .05).intersects(rectangle));
	    }

        @DisplayName("(OrientedRectangle2afp) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        OrientedRectangle2afp rectangle = createOrientedRectangle(
	                6, 9,
	                0.894427190999916, -0.447213595499958, 13.999990000000002,
	                12.999989999999997);
	        assertTrue(createRoundRectangle(-8, 15, 2, 1, .1, .05).intersects(rectangle));
	    }

        @DisplayName("(OrientedRectangle2afp) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        OrientedRectangle2afp rectangle = createOrientedRectangle(
	                6, 9,
	                0.894427190999916, -0.447213595499958, 13.999990000000002,
	                12.999989999999997);
	        assertFalse(createRoundRectangle(10, 25, 2, 1, .1, .05).intersects(rectangle));
	    }

        @DisplayName("(OrientedRectangle2afp) #11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        OrientedRectangle2afp rectangle = createOrientedRectangle(
	                6, 9,
	                0.894427190999916, -0.447213595499958, 13.999990000000002,
	                12.999989999999997);
	        assertFalse(createRoundRectangle(20, -5, 2, 1, .1, .05).intersects(rectangle));
	    }

        @DisplayName("(Parallelogram2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        Parallelogram2afp para = createParallelogram(
	                6, 9,
	                2.425356250363330e-01, 9.701425001453320e-01, 9.219544457292887,
	                -7.071067811865475e-01, 7.071067811865475e-01, 1.264911064067352e+01);
	        assertFalse(createRoundRectangle(0, 0, 1, 1, .1, .05).intersects(para));
	    }

        @DisplayName("(Parallelogram2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        Parallelogram2afp para = createParallelogram(
	                6, 9,
	                2.425356250363330e-01, 9.701425001453320e-01, 9.219544457292887,
	                -7.071067811865475e-01, 7.071067811865475e-01, 1.264911064067352e+01);
	        assertTrue(createRoundRectangle(0, 2, 1, 1, .1, .05).intersects(para));
	    }

        @DisplayName("(Parallelogram2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        Parallelogram2afp para = createParallelogram(
	                6, 9,
	                2.425356250363330e-01, 9.701425001453320e-01, 9.219544457292887,
	                -7.071067811865475e-01, 7.071067811865475e-01, 1.264911064067352e+01);
	        assertTrue(createRoundRectangle(-5.5, 8.5, 1, 1, .1, .05).intersects(para));
	    }

        @DisplayName("(Parallelogram2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        Parallelogram2afp para = createParallelogram(
	                6, 9,
	                2.425356250363330e-01, 9.701425001453320e-01, 9.219544457292887,
	                -7.071067811865475e-01, 7.071067811865475e-01, 1.264911064067352e+01);
	        assertFalse(createRoundRectangle(-6, 16, 1, 1, .1, .05).intersects(para));
	    }

        @DisplayName("(Parallelogram2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        Parallelogram2afp para = createParallelogram(
	                6, 9,
	                2.425356250363330e-01, 9.701425001453320e-01, 9.219544457292887,
	                -7.071067811865475e-01, 7.071067811865475e-01, 1.264911064067352e+01);
	        assertFalse(createRoundRectangle(146, 16, 1, 1, .1, .05).intersects(para));
	    }

        @DisplayName("(Parallelogram2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        Parallelogram2afp para = createParallelogram(
	                6, 9,
	                2.425356250363330e-01, 9.701425001453320e-01, 9.219544457292887,
	                -7.071067811865475e-01, 7.071067811865475e-01, 1.264911064067352e+01);
	        assertTrue(createRoundRectangle(12, 14, 1, 1, .1, .05).intersects(para));
	    }

        @DisplayName("(Parallelogram2afp) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        Parallelogram2afp para = createParallelogram(
	                6, 9,
	                2.425356250363330e-01, 9.701425001453320e-01, 9.219544457292887,
	                -7.071067811865475e-01, 7.071067811865475e-01, 1.264911064067352e+01);
	        assertTrue(createRoundRectangle(0, 8, 1, 1, .1, .05).intersects(para));
	    }

        @DisplayName("(Parallelogram2afp) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        Parallelogram2afp para = createParallelogram(
	                6, 9,
	                2.425356250363330e-01, 9.701425001453320e-01, 9.219544457292887,
	                -7.071067811865475e-01, 7.071067811865475e-01, 1.264911064067352e+01);
	        assertTrue(createRoundRectangle(10, -1, 1, 1, .1, .05).intersects(para));
	    }

        @DisplayName("(Parallelogram2afp) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        Parallelogram2afp para = createParallelogram(
	                6, 9,
	                2.425356250363330e-01, 9.701425001453320e-01, 9.219544457292887,
	                -7.071067811865475e-01, 7.071067811865475e-01, 1.264911064067352e+01);
	        assertTrue(createRoundRectangle(-15, -10, 35, 40, .1, .05).intersects(para));
	    }

        @DisplayName("(Parallelogram2afp) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        Parallelogram2afp para = createParallelogram(
	                6, 9,
	                2.425356250363330e-01, 9.701425001453320e-01, 9.219544457292887,
	                -7.071067811865475e-01, 7.071067811865475e-01, 1.264911064067352e+01);
	        assertFalse(createRoundRectangle(-4.79634, 14.50886, 1, 1, .1, .05).intersects(para));
	    }

        @DisplayName("(RoundRectangle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().intersects(createRoundRectangle(0, 0, 1, 1, .1, .2)));
	    }

        @DisplayName("(RoundRectangle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().intersects(createRoundRectangle(20, 20, 21, 21, .1, .2)));
	    }

        @DisplayName("(RoundRectangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().intersects(createRoundRectangle(0, 0, 7, 12, .1, .2)));
	    }

        @DisplayName("(RoundRectangle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().intersects(createRoundRectangle(0, 0, 7, 8, .1, .2)));
	    }

        @DisplayName("(RoundRectangle2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().intersects(createRoundRectangle(0, 0, 5.01, 8.01, .1, .2)));
	    }

        @DisplayName("(RoundRectangle2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().intersects(createRoundRectangle(0, 0, 5.05, 8.05, .1, .2)));
	    }

        @DisplayName("(RoundRectangle2afp) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().intersects(createRoundRectangle(0, 0, 5.05, 8.1, .1, .2)));
	    }

        @DisplayName("(RoundRectangle2afp) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().intersects(createRoundRectangle(0, 0, 5.05, 8.15, .1, .2)));
	    }

        @DisplayName("(Shape2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void shape_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertTrue(getS().intersects((Shape2D) createCircle(4.1, 12, 1)));
        }

        @DisplayName("(Shape2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void shape_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertTrue(getS().intersects((Shape2D) createEllipse(5, 11.5, 2, 1)));
        }

	}

	@DisplayName("inflate")
	@Nested
	public class Inflate {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        getS().inflate(1, 2, 3, 4);
	        assertEpsilonEquals(4, getS().getMinX());
	        assertEpsilonEquals(6, getS().getMinY());
	        assertEpsilonEquals(13, getS().getMaxX());
	        assertEpsilonEquals(22, getS().getMaxY());
	    }

	}

	@DisplayName("this += Vector2D")
	@Nested
	public class OperatorAddVector2D {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        getS().operator_add(createVector(123.456, 456.789));
	        assertEpsilonEquals(128.456, getS().getMinX());
	        assertEpsilonEquals(464.789, getS().getMinY());
	        assertEpsilonEquals(133.456, getS().getMaxX());
	        assertEpsilonEquals(474.789, getS().getMaxY());
	    }

	}

	@DisplayName("this + Vector2D")
	@Nested
	public class OperatorPlusVector2D {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        T shape = getS().operator_plus(createVector(123.456, 456.789));
	        assertEpsilonEquals(128.456, shape.getMinX());
	        assertEpsilonEquals(464.789, shape.getMinY());
	        assertEpsilonEquals(133.456, shape.getMaxX());
	        assertEpsilonEquals(474.789, shape.getMaxY());
	    }

	}

	@DisplayName("this -= Vector2D")
	@Nested
	public class OperatorRemoveVector2D {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        getS().operator_remove(createVector(123.456, 456.789));
	        assertEpsilonEquals(-118.456, getS().getMinX());
	        assertEpsilonEquals(-448.789, getS().getMinY());
	        assertEpsilonEquals(-113.456, getS().getMaxX());
	        assertEpsilonEquals(-438.789, getS().getMaxY());
	    }

	}

	@DisplayName("this - Vector2D")
	@Nested
	public class OperatorMinusVector2D {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        T shape = getS().operator_minus(createVector(123.456, 456.789));
	        assertEpsilonEquals(-118.456, shape.getMinX());
	        assertEpsilonEquals(-448.789, shape.getMinY());
	        assertEpsilonEquals(-113.456, shape.getMaxX());
	        assertEpsilonEquals(-438.789, shape.getMaxY());
	    }

	}
   
	@DisplayName("this * Transform2D")
	@Nested
	public class OperatorMultiplyTransform2D {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var pi = getS().operator_multiply(null).getPathIterator();
	        assertElement(pi, PathElementType.MOVE_TO, 5.1, 8);
	        assertElement(pi, PathElementType.LINE_TO, 9.9, 8);
	        assertElement(pi, PathElementType.CURVE_TO, 9.95523, 8, 10, 8.08954, 10, 8.2);
	        assertElement(pi, PathElementType.LINE_TO, 10, 17.8);
	        assertElement(pi, PathElementType.CURVE_TO, 10, 17.91046, 9.95523, 18, 9.9, 18);
	        assertElement(pi, PathElementType.LINE_TO, 5.1, 18);
	        assertElement(pi, PathElementType.CURVE_TO, 5.04477, 18, 5, 17.91046, 5, 17.8);
	        assertElement(pi, PathElementType.LINE_TO, 5, 8.2);
	        assertElement(pi, PathElementType.CURVE_TO, 5, 8.08954, 5.04477, 8, 5.1, 8);
	        assertElement(pi, PathElementType.CLOSE, 5.1, 8);
	        assertNoElement(pi);
        }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var pi = getS().operator_multiply(new Transform2D()).getPathIterator();
	        assertElement(pi, PathElementType.MOVE_TO, 5.1, 8);
	        assertElement(pi, PathElementType.LINE_TO, 9.9, 8);
	        assertElement(pi, PathElementType.CURVE_TO, 9.95523, 8, 10, 8.08954, 10, 8.2);
	        assertElement(pi, PathElementType.LINE_TO, 10, 17.8);
	        assertElement(pi, PathElementType.CURVE_TO, 10, 17.91046, 9.95523, 18, 9.9, 18);
	        assertElement(pi, PathElementType.LINE_TO, 5.1, 18);
	        assertElement(pi, PathElementType.CURVE_TO, 5.04477, 18, 5, 17.91046, 5, 17.8);
	        assertElement(pi, PathElementType.LINE_TO, 5, 8.2);
	        assertElement(pi, PathElementType.CURVE_TO, 5, 8.08954, 5.04477, 8, 5.1, 8);
	        assertElement(pi, PathElementType.CLOSE, 5.1, 8);
	        assertNoElement(pi);
        }

        @DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        Transform2D tr = new Transform2D();
	        tr.setTranslation(10, -1);
	        var pi = getS().operator_multiply(tr).getPathIterator();
	        assertElement(pi, PathElementType.MOVE_TO, 15.1, 7);
	        assertElement(pi, PathElementType.LINE_TO, 19.9, 7);
	        assertElement(pi, PathElementType.CURVE_TO, 19.95523, 7, 20, 7.08954, 20, 7.2);
	        assertElement(pi, PathElementType.LINE_TO, 20, 16.8);
	        assertElement(pi, PathElementType.CURVE_TO, 20, 16.91046, 19.95523, 17, 19.9, 17);
	        assertElement(pi, PathElementType.LINE_TO, 15.1, 17);
	        assertElement(pi, PathElementType.CURVE_TO, 15.04477, 17, 15, 16.91046, 15, 16.8);
	        assertElement(pi, PathElementType.LINE_TO, 15, 7.2);
	        assertElement(pi, PathElementType.CURVE_TO, 15, 7.08954, 15.04477, 7, 15.1, 7);
	        assertElement(pi, PathElementType.CLOSE, 15.1, 7);
	        assertNoElement(pi);
	    }

	}

	@DisplayName("this && Point2D")
	@Nested
	public class OperatorAndPoint2D {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().operator_and(createPoint(0, 0)));
        }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        	assertFalse(getS().operator_and(createPoint(20, 0)));
        }

        @DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().operator_and(createPoint(20, 20)));
        }

        @DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().operator_and(createPoint(0, 20)));
        }

        @DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().operator_and(createPoint(8, 13)));
        }

        @DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().operator_and(createPoint(5, 13)));
        }

        @DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().operator_and(createPoint(4.999, 13)));
        }

        @DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().operator_and(createPoint(5, 8)));
        }

        @DisplayName("#9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().operator_and(createPoint(5, 18)));
        }

        @DisplayName("#10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().operator_and(createPoint(15, 18)));
        }

        @DisplayName("#11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().operator_and(createPoint(15, 8)));
	    }

	}


	@DisplayName("this && Shape2D")
	@Nested
	public class OperatorAndShape2D {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().operator_and(createCircle(4.1, 12, 1)));
	    }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().operator_and(createEllipse(5, 11.5, 2, 1)));
	    }

	}

	@DisplayName("this .. Point2D")
	@Nested
	public class OperatorUpToPoint2D {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(9.47905, getS().operator_upTo(createPoint(0, 0)));
        }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(12.86194, getS().operator_upTo(createPoint(20, 0)));
        }

        @DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(10.23041, getS().operator_upTo(createPoint(20, 20)));
        }

        @DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(5.43372, getS().operator_upTo(createPoint(0, 20)));
        }

        @DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(5, getS().operator_upTo(createPoint(0, 11)));
        }

        @DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(10, getS().operator_upTo(createPoint(20, 11)));
        }

        @DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(8, getS().operator_upTo(createPoint(7, 0)));
        }

        @DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(2, getS().operator_upTo(createPoint(7, 20)));
        }

        @DisplayName("#9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(5, getS().operator_upTo(createPoint(0, 8.2)));
        }

        @DisplayName("#10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(8, getS().operator_upTo(createPoint(5.1, 0)));
        }

        @DisplayName("#11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(0, getS().operator_upTo(createPoint(7, 10)));
	    }

	}

}