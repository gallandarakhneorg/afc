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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.base.PathElementType;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.base.d2.Point2D;
import org.arakhne.afc.math.geometry.base.d2.Shape2D;
import org.arakhne.afc.math.geometry.base.d2.Transform2D;
import org.arakhne.afc.math.geometry.base.d2.Tuple2D;
import org.arakhne.afc.math.geometry.base.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.afp.Circle2afp;
import org.arakhne.afc.math.geometry.d2.afp.MultiShape2afp;
import org.arakhne.afc.math.geometry.d2.afp.OrientedRectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Parallelogram2afp;
import org.arakhne.afc.math.geometry.d2.afp.Path2afp;
import org.arakhne.afc.math.geometry.d2.afp.PathIterator2afp;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Triangle2afp;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("all")
public abstract class AbstractParallelogram2afpTestCase<T extends Parallelogram2afp<?, T, ?, ?, ?, B>,
B extends Rectangle2afp<?, ?, ?, ?, ?, B>> extends AbstractShape2afpTestCase<T, B> {

    protected final double cx = 6;
    protected final double cy = 9;
    protected final double ux = 2.425356250363330e-01;   
    protected final double uy = 9.701425001453320e-01;
    protected final double e1 = 9.219544457292887;
    protected final double vx = -7.071067811865475e-01;
    protected final double vy = 7.071067811865475e-01;
    protected final double e2 = 1.264911064067352e+01;

    // Points' names are in the ggb diagram
    protected final double pEx = 12.7082;
    protected final double pEy = -8.88854;
    protected final double pFx = 17.18034;
    protected final double pFy = 9;
    protected final double pGx = -0.7082;
    protected final double pGy = 26.88854;
    protected final double pHx = -5.18034;
    protected final double pHy = 9;

    @Override
    protected final T createShape() {
        return (T) createParallelogram(cx, cy, ux, uy, e1, vx, vy, e2);
    }

    protected MultiShape2afp createTestMultiShape(double dx, double dy) {
        Circle2afp circle = createCircle(dx - 3, dy + 2, 2);
        Triangle2afp triangle = createTestTriangle(dx +1, dy - 1);
        MultiShape2afp multishape = createMultiShape();
        multishape.add(circle);
        multishape.add(triangle);
        return multishape;
    }

    protected Triangle2afp createTestTriangle(double dx, double dy) {
        return createTriangle(dx, dy, dx + 6, dy + 3, dx - 1, dy + 2.5);
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

    protected Path2afp<?, ?, ?, ?, ?, ?> createNonEmptyPath(double x, double y) {
        Path2afp<?, ?, ?, ?, ?, ?> path = createPath();
        path.moveTo(x, y);
        path.lineTo(x + 1, y + .5);
        path.lineTo(x, y + 1);
        return path;
    }

    @DisplayName("calculatesOrthogonalAxes")
	@Nested
	public class calculatesOrthogonalAxes {

        private final double obrux = 0.8944271909999159;
        private final double obruy = -0.4472135954999579;
        private final double obrvx = 0.4472135954999579;
        private final double obrvy = 0.8944271909999159;

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        List points = Arrays.asList(
	                createPoint(11.7082, -0.94427), createPoint(16.18034, 8),
	                createPoint(-1.7082, 16.94427), createPoint(-6.18034, 8));
	        Vector2D R, S;
	        R = createVector(Double.NaN, Double.NaN);
	        S = createVector(Double.NaN, Double.NaN);
	        Parallelogram2afp.calculatesOrthogonalAxes(points, R, S);
	        assertEpsilonEquals(obrux, R.getX());
	        assertEpsilonEquals(obruy, R.getY());
	        assertEpsilonEquals(obrvx, S.getX());
	        assertEpsilonEquals(obrvy, S.getY());
	    }

    }

    @DisplayName("findsVectorProjectionRAxisPoint")
	@Nested
	public class FindsVectorProjectionRAxisPoint {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(-e1, Parallelogram2afp.findsVectorProjectionRAxisPoint(ux, uy, vx, vy, pEx - cx, pEy - cy));
        }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(e1, Parallelogram2afp.findsVectorProjectionRAxisPoint(ux, uy, vx, vy, pFx - cx, pFy - cy));
        }

        @DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(e1, Parallelogram2afp.findsVectorProjectionRAxisPoint(ux, uy, vx, vy, pGx - cx, pGy - cy));
        }

        @DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(-e1, Parallelogram2afp.findsVectorProjectionRAxisPoint(ux, uy, vx, vy, pHx - cx, pHy - cy));
        }

        @DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(-12.36932, Parallelogram2afp.findsVectorProjectionRAxisPoint(ux, uy, vx, vy, -cx, -cy));
	    }

    }

    @DisplayName("findsVectorProjectionSAxisVector")
	@Nested
	public class FindsVectorProjectionSAxisVector {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(-e2, Parallelogram2afp.findsVectorProjectionSAxisVector(ux, uy, vx, vy, pEx - cx, pEy - cy));
        }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(-e2, Parallelogram2afp.findsVectorProjectionSAxisVector(ux, uy, vx, vy, pFx - cx, pFy - cy));
        }

        @DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(e2, Parallelogram2afp.findsVectorProjectionSAxisVector(ux, uy, vx, vy, pGx - cx, pGy - cy));
        }

        @DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(e2, Parallelogram2afp.findsVectorProjectionSAxisVector(ux, uy, vx, vy, pHx - cx, pHy - cy));
        }

        @DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(4.24264, Parallelogram2afp.findsVectorProjectionSAxisVector(ux, uy, vx, vy, -cx, -cy));
	    }

    }

    @DisplayName("calculatesCenterPointAxisExtents")
	@Nested
	public class CalculatesCenterPointAxisExtents {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        List points = Arrays.asList(
	                createPoint(pEx, pEy), createPoint(pGx, pGy),
	                createPoint(pFx, pFy), createPoint(pEx, pEy));
	        Vector2D R, S;
	        Point2D center;
	        Tuple2D extents;
	        R = createVector(ux, uy);
	        S = createVector(vx, vy);
	        center = createPoint(Double.NaN, Double.NaN);
	        extents = createVector(Double.NaN, Double.NaN);
	        Parallelogram2afp.calculatesCenterPointAxisExtents(points, R, S, center, extents);
	        assertEpsilonEquals(cx, center.getX());
	        assertEpsilonEquals(cy, center.getY());
	        assertEpsilonEquals(e1, extents.getX());
	        assertEpsilonEquals(e2, extents.getY());
	    }

    }

    @DisplayName("findsClosestPointPointParallelogram")
	@Nested
	public class FindsClosestPointPointParallelogram {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var closest = createPoint(Double.NaN, Double.NaN);
	        Parallelogram2afp.findsClosestPointPointParallelogram(
	                -20, 9,
	                cx, cy, ux, uy, e1, vx, vy, e2,
	                closest);
	        assertFpPointEquals(pHx, pHy, closest);
        }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var closest = createPoint(Double.NaN, Double.NaN);
	        Parallelogram2afp.findsClosestPointPointParallelogram(
	                0, 0,
	                cx, cy, ux, uy, e1, vx, vy, e2,
	                closest);
	        assertFpPointEquals(1.90983, 1.90983, closest);
        }

        @DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var closest = createPoint(Double.NaN, Double.NaN);
	        Parallelogram2afp.findsClosestPointPointParallelogram(
	                5, -10,
	                cx, cy, ux, uy, e1, vx, vy, e2,
	                closest);
	        assertFpPointEquals(9.40983, -5.59017, closest);
        }

        @DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var closest = createPoint(Double.NaN, Double.NaN);
	        Parallelogram2afp.findsClosestPointPointParallelogram(
	                14, -20,
	                cx, cy, ux, uy, e1, vx, vy, e2,
	                closest);
	        assertFpPointEquals(pEx, pEy, closest);
        }

        @DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var closest = createPoint(Double.NaN, Double.NaN);
	        Parallelogram2afp.findsClosestPointPointParallelogram(
	                -6, 15,
	                cx, cy, ux, uy, e1, vx, vy, e2,
	                closest);
	        assertFpPointEquals(-3.81679, 14.4542, closest);
        }

        @DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var closest = createPoint(Double.NaN, Double.NaN);
	        Parallelogram2afp.findsClosestPointPointParallelogram(
	                0, 10,
	                cx, cy, ux, uy, e1, vx, vy, e2,
	                closest);
	        assertFpPointEquals(0, 10, closest);
        }

        @DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var closest = createPoint(Double.NaN, Double.NaN);
	        Parallelogram2afp.findsClosestPointPointParallelogram(
	                10, 0,
	                cx, cy, ux, uy, e1, vx, vy, e2,
	                closest);
	        assertFpPointEquals(10, 0, closest);
        }

        @DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var closest = createPoint(Double.NaN, Double.NaN);
	        Parallelogram2afp.findsClosestPointPointParallelogram(
	                15, -4,
	                cx, cy, ux, uy, e1, vx, vy, e2,
	                closest);
	        assertFpPointEquals(13.99326, -3.74832, closest);
        }

        @DisplayName("#9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var closest = createPoint(Double.NaN, Double.NaN);
	        Parallelogram2afp.findsClosestPointPointParallelogram(
	                -5, 25,
	                cx, cy, ux, uy, e1, vx, vy, e2,
	                closest);
	        assertFpPointEquals(-1.40503, 24.10126, closest);
        }

        @DisplayName("#10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var closest = createPoint(Double.NaN, Double.NaN);
	        Parallelogram2afp.findsClosestPointPointParallelogram(
	                0, 20,
	                cx, cy, ux, uy, e1, vx, vy, e2,
	                closest);
	        assertFpPointEquals(0, 20, closest);
        }

        @DisplayName("#11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var closest = createPoint(Double.NaN, Double.NaN);
	        Parallelogram2afp.findsClosestPointPointParallelogram(
	                10, 10,
	                cx, cy, ux, uy, e1, vx, vy, e2,
	                closest);
	        assertFpPointEquals(10, 10, closest);
        }

        @DisplayName("#12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var closest = createPoint(Double.NaN, Double.NaN);
	        Parallelogram2afp.findsClosestPointPointParallelogram(
	                20, 0,
	                cx, cy, ux, uy, e1, vx, vy, e2,
	                closest);
	        assertFpPointEquals(15.22856, 1.19286, closest);
        }

        @DisplayName("#13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var closest = createPoint(Double.NaN, Double.NaN);
	        Parallelogram2afp.findsClosestPointPointParallelogram(
	                -3, 35,
	                cx, cy, ux, uy, e1, vx, vy, e2,
	                closest);
	        assertFpPointEquals(pGx, pGy, closest);
        }

        @DisplayName("#14")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_14(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var closest = createPoint(Double.NaN, Double.NaN);
	        Parallelogram2afp.findsClosestPointPointParallelogram(
	                5, 35,
	                cx, cy, ux, uy, e1, vx, vy, e2,
	                closest);
	        assertFpPointEquals(pGx, pGy, closest);
        }

        @DisplayName("#15")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_15(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var closest = createPoint(Double.NaN, Double.NaN);
	        Parallelogram2afp.findsClosestPointPointParallelogram(
	                20, 15,
	                cx, cy, ux, uy, e1, vx, vy, e2,
	                closest);
	        assertFpPointEquals(15.59017, 10.59017, closest);
        }

        @DisplayName("#16")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_16(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var closest = createPoint(Double.NaN, Double.NaN);
	        Parallelogram2afp.findsClosestPointPointParallelogram(
	                35, 10,
	                cx, cy, ux, uy, e1, vx, vy, e2,
	                closest);
	        assertFpPointEquals(pFx, pFy, closest);
        }

        @DisplayName("#17")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_17(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var closest = createPoint(Double.NaN, Double.NaN);
	        Parallelogram2afp.findsClosestPointPointParallelogram(
	                -8, 29,
	                cx, cy, ux, uy, e1, vx, vy, e2,
	                closest);
	        assertFpPointEquals(pGx, pGy, closest);
        }

        @DisplayName("#18")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_18(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var closest = createPoint(Double.NaN, Double.NaN);
	        Parallelogram2afp.findsClosestPointPointParallelogram(0, 0,
	                4.7, 15,
	                0.12403, 0.99228,
	                18.02776,
	                -0.44721, 0.89443,
	                20,
	                closest);
	        assertFpPointEquals(0.81573, 0.40786, closest);
        }

        @DisplayName("#19")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_19(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        // In triangle.ggb
	        var closest = createPoint(Double.NaN, Double.NaN);
	        Parallelogram2afp.findsClosestPointPointParallelogram(-5.2964, 3.19501,
	                -10, 7,
	                -0.9863939238321437, 0.1643989873053573,
	                1,
	                0.9998000599800071, 0.01999600119960014,
	                2,
	                closest);
	        assertFpPointEquals(-7.01401, 6.87559, closest);
        }

        @DisplayName("#20")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_20(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        // In triangle.ggb
	        var closest = createPoint(Double.NaN, Double.NaN);
	        Parallelogram2afp.findsClosestPointPointParallelogram(-1, -2,
	                0, -6,
	                -0.9863939238321437, 0.1643989873053573,
	                1,
	                0.9998000599800071, 0.01999600119960014,
	                2,
	                closest);
	        assertFpPointEquals(-0.92331, -5.83434, closest);
        }

        @DisplayName("#21")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_21(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        // In segment.ggb
	        var closest = createPoint(Double.NaN, Double.NaN);
	        Parallelogram2afp.findsClosestPointPointParallelogram(0, 0,
	                -10, -3,
	                -.8944271909999159, .4472135954999579,
	                2,
	                .5547001962252290, -.8320502943378436,
	                1,
	                closest);
	        assertFpPointEquals(-7.65645, -4.72648, closest);		
	    }

    }

    @DisplayName("findsFarthestPointPointParallelogram")
	@Nested
	public class FindsFarthestPointPointParallelogram {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var farthest = createPoint(Double.NaN, Double.NaN);
	        Parallelogram2afp.findsFarthestPointPointParallelogram(
	                -20, 9,
	                cx, cy, ux, uy, e1, vx, vy, e2,
	                farthest);
	        assertFpPointEquals(pEx, pEy, farthest);
        }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var farthest = createPoint(Double.NaN, Double.NaN);
	        Parallelogram2afp.findsFarthestPointPointParallelogram(
	                0, 0,
	                cx, cy, ux, uy, e1, vx, vy, e2,
	                farthest);
	        assertFpPointEquals(pGx, pGy, farthest);
        }

        @DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var farthest = createPoint(Double.NaN, Double.NaN);
	        Parallelogram2afp.findsFarthestPointPointParallelogram(
	                5, -10,
	                cx, cy, ux, uy, e1, vx, vy, e2,
	                farthest);
	        assertFpPointEquals(pGx, pGy, farthest);
        }

        @DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var farthest = createPoint(Double.NaN, Double.NaN);
	        Parallelogram2afp.findsFarthestPointPointParallelogram(
	                14, -20,
	                cx, cy, ux, uy, e1, vx, vy, e2,
	                farthest);
	        assertFpPointEquals(pGx, pGy, farthest);
        }

        @DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var farthest = createPoint(Double.NaN, Double.NaN);
	        Parallelogram2afp.findsFarthestPointPointParallelogram(
	                -6, 15,
	                cx, cy, ux, uy, e1, vx, vy, e2,
	                farthest);
	        assertFpPointEquals(pEx, pEy, farthest);
        }

        @DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var farthest = createPoint(Double.NaN, Double.NaN);
	        Parallelogram2afp.findsFarthestPointPointParallelogram(
	                0, 10,
	                cx, cy, ux, uy, e1, vx, vy, e2,
	                farthest);
	        assertFpPointEquals(pEx, pEy, farthest);
        }

        @DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var farthest = createPoint(Double.NaN, Double.NaN);
	        Parallelogram2afp.findsFarthestPointPointParallelogram(
	                10, 0,
	                cx, cy, ux, uy, e1, vx, vy, e2,
	                farthest);
	        assertFpPointEquals(pGx, pGy, farthest);
        }

        @DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var farthest = createPoint(Double.NaN, Double.NaN);
	        Parallelogram2afp.findsFarthestPointPointParallelogram(
	                15, -4,
	                cx, cy, ux, uy, e1, vx, vy, e2,
	                farthest);
	        assertFpPointEquals(pGx, pGy, farthest);
        }

        @DisplayName("#9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var farthest = createPoint(Double.NaN, Double.NaN);
	        Parallelogram2afp.findsFarthestPointPointParallelogram(
	                -5, 25,
	                cx, cy, ux, uy, e1, vx, vy, e2,
	                farthest);
	        assertFpPointEquals(pEx, pEy, farthest);
        }

        @DisplayName("#10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var farthest = createPoint(Double.NaN, Double.NaN);
	        Parallelogram2afp.findsFarthestPointPointParallelogram(
	                0, 20,
	                cx, cy, ux, uy, e1, vx, vy, e2,
	                farthest);
	        assertFpPointEquals(pEx, pEy, farthest);
        }

        @DisplayName("#11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var farthest = createPoint(Double.NaN, Double.NaN);
	        Parallelogram2afp.findsFarthestPointPointParallelogram(
	                10, 10,
	                cx, cy, ux, uy, e1, vx, vy, e2,
	                farthest);
	        assertFpPointEquals(pGx, pGy, farthest);
        }

        @DisplayName("#12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var farthest = createPoint(Double.NaN, Double.NaN);
	        Parallelogram2afp.findsFarthestPointPointParallelogram(
	                20, 0,
	                cx, cy, ux, uy, e1, vx, vy, e2,
	                farthest);
	        assertFpPointEquals(pGx, pGy, farthest);
        }

        @DisplayName("#13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var farthest = createPoint(Double.NaN, Double.NaN);
	        Parallelogram2afp.findsFarthestPointPointParallelogram(
	                -3, 35,
	                cx, cy, ux, uy, e1, vx, vy, e2,
	                farthest);
	        assertFpPointEquals(pEx, pEy, farthest);
        }

        @DisplayName("#14")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_14(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var farthest = createPoint(Double.NaN, Double.NaN);
	        Parallelogram2afp.findsFarthestPointPointParallelogram(
	                5, 35,
	                cx, cy, ux, uy, e1, vx, vy, e2,
	                farthest);
	        assertFpPointEquals(pEx, pEy, farthest);
        }

        @DisplayName("#15")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_15(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var farthest = createPoint(Double.NaN, Double.NaN);
	        Parallelogram2afp.findsFarthestPointPointParallelogram(
	                20, 15,
	                cx, cy, ux, uy, e1, vx, vy, e2,
	                farthest);
	        assertFpPointEquals(pHx, pHy, farthest);
        }

        @DisplayName("#16")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_16(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var farthest = createPoint(Double.NaN, Double.NaN);
	        Parallelogram2afp.findsFarthestPointPointParallelogram(
	                35, 10,
	                cx, cy, ux, uy, e1, vx, vy, e2,
	                farthest);
	        assertFpPointEquals(pHx, pHy, farthest);
        }

        @DisplayName("#17")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_17(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var farthest = createPoint(Double.NaN, Double.NaN);
	        Parallelogram2afp.findsFarthestPointPointParallelogram(
	                -8, 29,
	                cx, cy, ux, uy, e1, vx, vy, e2,
	                farthest);
	        assertFpPointEquals(pEx, pEy, farthest);
        }

        @DisplayName("#18")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_18(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var farthest = createPoint(Double.NaN, Double.NaN);
	        Parallelogram2afp.findsFarthestPointPointParallelogram(0, 0,
	                4.7, 15,
	                0.12403, 0.99228,
	                18.02776,
	                -0.44721, 0.89443,
	                20,
	                farthest);
	        assertFpPointEquals(-2.0082, 50.77719, farthest);
	    }

    }

    @DisplayName("containsParallelogramPoint")
	@Nested
	public class ContainsParallelogramPoint {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(Parallelogram2afp.containsParallelogramPoint(cx, cy, ux, uy, e1, vx, vy, e2,
	                0, 0));
        }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(Parallelogram2afp.containsParallelogramPoint(cx, cy, ux, uy, e1, vx, vy, e2,
	                -20, 0));
        }

        @DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.containsParallelogramPoint(cx, cy, ux, uy, e1, vx, vy, e2,
	                12, -4));
        }

        @DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.containsParallelogramPoint(cx, cy, ux, uy, e1, vx, vy, e2,
	                14, 0));
        }

        @DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(Parallelogram2afp.containsParallelogramPoint(cx, cy, ux, uy, e1, vx, vy, e2,
	                15, 0));
        }

        @DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(Parallelogram2afp.containsParallelogramPoint(cx, cy, ux, uy, e1, vx, vy, e2,
	                20, 8));
        }

        @DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.containsParallelogramPoint(cx, cy, ux, uy, e1, vx, vy, e2,
	                8, 16));
        }

        @DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(Parallelogram2afp.containsParallelogramPoint(cx, cy, ux, uy, e1, vx, vy, e2,
	                -4, 20));
        }

        @DisplayName("#9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(Parallelogram2afp.containsParallelogramPoint(cx, cy, ux, uy, e1, vx, vy, e2,
	                -5, 12));
        }

        @DisplayName("#10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.containsParallelogramPoint(cx, cy, ux, uy, e1, vx, vy, e2,
	                0, 6));
        }

        @DisplayName("#11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.containsParallelogramPoint(cx, cy, ux, uy, e1, vx, vy, e2,
	                0, 7));
        }

        @DisplayName("#12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.containsParallelogramPoint(cx, cy, ux, uy, e1, vx, vy, e2,
	                0, 8));
        }

        @DisplayName("#13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.containsParallelogramPoint(cx, cy, ux, uy, e1, vx, vy, e2,
	                0, 9));
        }

        @DisplayName("#14")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_14(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.containsParallelogramPoint(cx, cy, ux, uy, e1, vx, vy, e2,
	                0, 10));
        }

        @DisplayName("#15")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_15(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(Parallelogram2afp.containsParallelogramPoint(cx, cy, ux, uy, e1, vx, vy, e2,
	                0, 27));
        }

        @DisplayName("#16")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_16(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.containsParallelogramPoint(cx, cy, ux, uy, e1, vx, vy, e2,
	                cx, cy));
        }

        @DisplayName("#17")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_17(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.containsParallelogramPoint(cx, cy, ux, uy, e1, vx, vy, e2,
	                16, 8));
	    }

    }

    @DisplayName("containsParallelogramRectangle")
	@Nested
	public class ContainsParallelogramRectangle {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(Parallelogram2afp.containsParallelogramRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
	                0, 0, 1, 1));
        }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(Parallelogram2afp.containsParallelogramRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
	                0, 1, 1, 1));
        }

        @DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(Parallelogram2afp.containsParallelogramRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
	                0, 2, 1, 1));
        }

        @DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(Parallelogram2afp.containsParallelogramRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
	                0, 3, 1, 1));
        }

        @DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.containsParallelogramRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
	                0, 4, 1, 1));
        }

        @DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.containsParallelogramRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
	                0, 5, 1, 1));
        }

        @DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.containsParallelogramRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
	                0, 6, 1, 1));
        }
    }

    @DisplayName("intersectsParallelogramSegment")
	@Nested
	public class IntersectsParallelogramSegment {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(Parallelogram2afp.intersectsParallelogramSegment(cx, cy, ux, uy, e1, vx, vy, e2,
	                0, 0, 1, 1));
        }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.intersectsParallelogramSegment(cx, cy, ux, uy, e1, vx, vy, e2,
	                5, 5, 4, 6));
        }

        @DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.intersectsParallelogramSegment(cx, cy, ux, uy, e1, vx, vy, e2,
	                2, -2, 5, 0));
        }

        @DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(Parallelogram2afp.intersectsParallelogramSegment(cx, cy, ux, uy, e1, vx, vy, e2,
	                -20, -5, -10, 6));
        }

        @DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(Parallelogram2afp.intersectsParallelogramSegment(cx, cy, ux, uy, e1, vx, vy, e2,
	                -5, 0, -10, 16));
        }

        @DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.intersectsParallelogramSegment(cx, cy, ux, uy, e1, vx, vy, e2,
	                -10, 1, 10, 20));
        }

    }

    @DisplayName("intersectsParallelogramCircle")
	@Nested
	public class IntersectsParallelogramCircle {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(Parallelogram2afp.intersectsParallelogramCircle(cx, cy, ux, uy, e1, vx, vy, e2,
	                .5, .5, .5));
        }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(Parallelogram2afp.intersectsParallelogramCircle(cx, cy, ux, uy, e1, vx, vy, e2,
	                .5, 1.5, .5));
        }

        @DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(Parallelogram2afp.intersectsParallelogramCircle(cx, cy, ux, uy, e1, vx, vy, e2,
	                .5, 2.5, .5));
        }

        @DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.intersectsParallelogramCircle(cx, cy, ux, uy, e1, vx, vy, e2,
	                .5, 3.5, .5));
        }

        @DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.intersectsParallelogramCircle(cx, cy, ux, uy, e1, vx, vy, e2,
	                4.5, 3.5, .5));
        }

        @DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(Parallelogram2afp.intersectsParallelogramCircle(cx, cy, ux, uy, e1, vx, vy, e2,
	                10, -7, .5));
        }

        @DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(Parallelogram2afp.intersectsParallelogramCircle(cx, cy, ux, uy, e1, vx, vy, e2,
	                10.1, -7, .5));
        }

        @DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.intersectsParallelogramCircle(cx, cy, ux, uy, e1, vx, vy, e2,
	                10.2, -7, .5));
        }

        @DisplayName("#9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.intersectsParallelogramCircle(cx, cy, ux, uy, e1, vx, vy, e2,
	                10, -1, 5));
	    }

    }

    @DisplayName("intersectsParallelogramEllipse")
	@Nested
	public class IntersectsParallelogramEllipse {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(Parallelogram2afp.intersectsParallelogramEllipse(cx, cy, ux, uy, e1, vx, vy, e2,
	                0, 0, 2, 1));
        }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(Parallelogram2afp.intersectsParallelogramEllipse(cx, cy, ux, uy, e1, vx, vy, e2,
	                0, 1, 2, 1));
        }

        @DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.intersectsParallelogramEllipse(cx, cy, ux, uy, e1, vx, vy, e2,
	                0, 2, 2, 1));
        }

        @DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.intersectsParallelogramEllipse(cx, cy, ux, uy, e1, vx, vy, e2,
	                0, 3, 2, 1));
        }

        @DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.intersectsParallelogramEllipse(cx, cy, ux, uy, e1, vx, vy, e2,
	                0, 4, 2, 1));
        }

        @DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.intersectsParallelogramEllipse(cx, cy, ux, uy, e1, vx, vy, e2,
	                1, 3, 2, 1));
        }

        @DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.intersectsParallelogramEllipse(cx, cy, ux, uy, e1, vx, vy, e2,
	                5, 5, 2, 1));
        }

        @DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(Parallelogram2afp.intersectsParallelogramEllipse(cx, cy, ux, uy, e1, vx, vy, e2,
	                0.1, 1, 2, 1));
        }

        @DisplayName("#9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(Parallelogram2afp.intersectsParallelogramEllipse(cx, cy, ux, uy, e1, vx, vy, e2,
	                0.2, 1, 2, 1));
        }

        @DisplayName("#10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.intersectsParallelogramEllipse(cx, cy, ux, uy, e1, vx, vy, e2,
	                0.3, 1, 2, 1));
        }

        @DisplayName("#11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.intersectsParallelogramEllipse(cx, cy, ux, uy, e1, vx, vy, e2,
	                0.4, 1, 2, 1));
        }

        @DisplayName("#12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(Parallelogram2afp.intersectsParallelogramEllipse(cx, cy, ux, uy, e1, vx, vy, e2,
	                -7, 7.5, 2, 1));
	    }

    }

    @DisplayName("intersectsParallelogramTriangle")
	@Nested
	public class IntersectsParallelogramTriangle {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.intersectsParallelogramTriangle(cx, cy, ux, uy, e1, vx, vy, e2,
	                -5, 15, -3, 16, -8, 19));
        }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.intersectsParallelogramTriangle(cx, cy, ux, uy, e1, vx, vy, e2,
	                -5, 15, -8, 19, -3, 16));
        }

        @DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(Parallelogram2afp.intersectsParallelogramTriangle(cx, cy, ux, uy, e1, vx, vy, e2,
	                0, -5, 2, -4, -3, -1));
        }

        @DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(Parallelogram2afp.intersectsParallelogramTriangle(cx, cy, ux, uy, e1, vx, vy, e2,
	                0, -5, -3, -1, 2, -4));
        }

        @DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(Parallelogram2afp.intersectsParallelogramTriangle(cx, cy, ux, uy, e1, vx, vy, e2,
	                20, 0, 22, 1, 17, 4));
        }

        @DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(Parallelogram2afp.intersectsParallelogramTriangle(cx, cy, ux, uy, e1, vx, vy, e2,
	                20, 0, 17, 4, 22, 1));
        }

        @DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(Parallelogram2afp.intersectsParallelogramTriangle(cx, cy, ux, uy, e1, vx, vy, e2,
	                17.18034, 9, 19.18034, 10, 14.18034, 13));
        }

        @DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(Parallelogram2afp.intersectsParallelogramTriangle(cx, cy, ux, uy, e1, vx, vy, e2,
	                17.18034, 9, 14.18034, 13, 19.18034, 10));
        }

        @DisplayName("#9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.intersectsParallelogramTriangle(cx, cy, ux, uy, e1, vx, vy, e2,
	                0, 10, 2, 11, -3, 14));
        }

        @DisplayName("#10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.intersectsParallelogramTriangle(cx, cy, ux, uy, e1, vx, vy, e2,
	                0, 10, -3, 14, 2, 11));
        }

        @DisplayName("#11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.intersectsParallelogramTriangle(cx, cy, ux, uy, e1, vx, vy, e2,
	                0, 20, 2, 21, -3, 24));
        }

    }

    @DisplayName("intersectsParallelogramParallelogram")
	@Nested
	public class IntersectsParallelogramParallelogram {

        private final double ux2 = -0.9284766908852592;
        private final double uy2 = 0.3713906763541037;
        private final double et1 = 5;
        private final double vx2 = 0.3713906763541037;
        private final double vy2 = 0.9284766908852592;
        private final double et2 = 3;
        // P + (-0.9284766908852592,0.3713906763541037) * 5 + (0.3713906763541037,0.9284766908852592) * 3
        // P - (-0.9284766908852592,0.3713906763541037) * 5 + (0.3713906763541037,0.9284766908852592) * 3
        // P - (-0.9284766908852592,0.3713906763541037) * 5 - (0.3713906763541037,0.9284766908852592) * 3
        // P + (-0.9284766908852592,0.3713906763541037) * 5 - (0.3713906763541037,0.9284766908852592) * 3

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(Parallelogram2afp.intersectsParallelogramParallelogram(cx, cy, ux, uy, e1, vx, vy, e2,
	                -10, 0,
	                ux2, uy2, et1, vx2, vy2, et2));
        }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(Parallelogram2afp.intersectsParallelogramParallelogram(cx, cy, ux, uy, e1, vx, vy, e2,
	                -15, 25,
	                ux2, uy2, et1, vx2, vy2, et2));
        }

        @DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(Parallelogram2afp.intersectsParallelogramParallelogram(cx, cy, ux, uy, e1, vx, vy, e2,
	                2, -6,
	                ux2, uy2, et1, vx2, vy2, et2));
        }

        @DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(Parallelogram2afp.intersectsParallelogramParallelogram(cx, cy, ux, uy, e1, vx, vy, e2,
	                2, -5,
	                ux2, uy2, et1, vx2, vy2, et2));
        }

        @DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.intersectsParallelogramParallelogram(cx, cy, ux, uy, e1, vx, vy, e2,
	                2, -4,
	                ux2, uy2, et1, vx2, vy2, et2));
        }

        @DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.intersectsParallelogramParallelogram(cx, cy, ux, uy, e1, vx, vy, e2,
	                pEx, pEy,
	                ux2, uy2, et1, vx2, vy2, et2));
        }

        @DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.intersectsParallelogramParallelogram(cx, cy, ux, uy, e1, vx, vy, e2,
	                6, 6,
	                ux2, uy2, et1, vx2, vy2, et2));
        }

        @DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.intersectsParallelogramParallelogram(cx, cy, ux, uy, e1, vx, vy, e2,
	                6, 6,
	                ux2, uy2, 10 * et1, vx2, vy2, 10 * et2));
	    }

    }

    @DisplayName("intersectsParallelogramRectangle")
	@Nested
	public class IntersectsParallelogramRectangle {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(Parallelogram2afp.intersectsParallelogramRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
	                0, 0, 1, 1));
        }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.intersectsParallelogramRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
	                0, 2, 1, 1));
        }

        @DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.intersectsParallelogramRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
	                -5.5, 8.5, 1, 1));
        }

        @DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(Parallelogram2afp.intersectsParallelogramRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
	                -6, 16, 1, 1));
        }

        @DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(Parallelogram2afp.intersectsParallelogramRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
	                146, 16, 1, 1));
        }

        @DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.intersectsParallelogramRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
	                12, 14, 1, 1));
        }

        @DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.intersectsParallelogramRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
	                0, 8, 1, 1));
        }

        @DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.intersectsParallelogramRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
	                10, -1, 1, 1));
        }

        @DisplayName("#9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.intersectsParallelogramRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
	                -15, -10, 35, 40));
        }

        @DisplayName("#10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.intersectsParallelogramRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
	                -4.79634, 14.50886, 1, 1));
	    }

    }

    @DisplayName("intersectsParallelogramRoundRectangle")
	@Nested
	public class IntersectsParallelogramRoundRectangle {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(Parallelogram2afp.intersectsParallelogramRoundRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
	                0, 0, 1, 1, .1, .05));
        }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.intersectsParallelogramRoundRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
	                0, 2, 1, 1, .1, .05));
        }

        @DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.intersectsParallelogramRoundRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
	                -5.5, 8.5, 1, 1, .1, .05));
        }

        @DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(Parallelogram2afp.intersectsParallelogramRoundRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
	                -6, 16, 1, 1, .1, .05));
        }

        @DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(Parallelogram2afp.intersectsParallelogramRoundRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
	                146, 16, 1, 1, .1, .05));
        }

        @DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.intersectsParallelogramRoundRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
	                12, 14, 1, 1, .1, .05));
        }

        @DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.intersectsParallelogramRoundRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
	                0, 8, 1, 1, .1, .05));
        }

        @DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.intersectsParallelogramRoundRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
	                10, -1, 1, 1, .1, .05));
        }

        @DisplayName("#9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(Parallelogram2afp.intersectsParallelogramRoundRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
	                -15, -10, 35, 40, .1, .05));
        }

        @DisplayName("#10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(Parallelogram2afp.intersectsParallelogramRoundRectangle(cx, cy, ux, uy, e1, vx, vy, e2,
	                -4.79634, 14.50886, 1, 1, .1, .05));
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
	        assertEpsilonEquals(cx, clone.getCenterX());
	        assertEpsilonEquals(cy, clone.getCenterY());
	        assertEpsilonEquals(ux, clone.getFirstAxisX());
	        assertEpsilonEquals(uy, clone.getFirstAxisY());
	        assertEpsilonEquals(e1, clone.getFirstAxisExtent());
	        assertEpsilonEquals(vx, clone.getSecondAxisX());
	        assertEpsilonEquals(vy, clone.getSecondAxisY());
	        assertEpsilonEquals(e2, clone.getSecondAxisExtent());
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
	        assertFalse(getS().equals(createParallelogram(0, cy, ux, uy, e1, vx, vy, e2)));
        }

        @DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().equals(createParallelogram(cx, cy, ux, uy, e1, vx, vy, 20)));
        }

        @DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().equals(createSegment(5, 8, 6, 10)));
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
	        assertTrue(getS().equals(createParallelogram(cx, cy, ux, uy, e1, vx, vy, e2)));
        }

        @DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFalse(getS().equals((PathIterator2afp) null));
        }

        @DisplayName("#9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFalse(getS().equals(createParallelogram(0, cy, ux, uy, e1, vx, vy, e2).getPathIterator()));
        }

        @DisplayName("#10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFalse(getS().equals(createParallelogram(cx, cy, ux, uy, e1, vx, vy, 20).getPathIterator()));
        }

        @DisplayName("#11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFalse(getS().equals(createSegment(5, 8, 6, 10).getPathIterator()));
        }

        @DisplayName("#12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertTrue(getS().equals(getS().getPathIterator()));
        }

        @DisplayName("#13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertTrue(getS().equals(createParallelogram(cx, cy, ux, uy, e1, vx, vy, e2).getPathIterator()));
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
	        assertFalse(getS().equalsToPathIterator(null));
        }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().equalsToPathIterator(createParallelogram(0, cy, ux, uy, e1, vx, vy, e2).getPathIterator()));
        }

        @DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().equalsToPathIterator(createParallelogram(cx, cy, ux, uy, e1, vx, vy, 20).getPathIterator()));
        }

        @DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().equalsToPathIterator(createSegment(5, 8, 6, 10).getPathIterator()));
        }

        @DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().equalsToPathIterator(getS().getPathIterator()));
        }

        @DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().equalsToPathIterator(createParallelogram(cx, cy, ux, uy, e1, vx, vy, e2).getPathIterator()));
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
	        assertFalse(getS().equalsToShape((T) createParallelogram(0, cy, ux, uy, e1, vx, vy, e2)));
        }

        @DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().equalsToShape((T) createParallelogram(cx, cy, ux, uy, e1, vx, vy, 20)));
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
	        assertTrue(getS().equalsToShape((T) createParallelogram(cx, cy, ux, uy, e1, vx, vy, e2)));
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
	        assertEpsilonEquals(0, getS().getCenterX());
	        assertEpsilonEquals(0, getS().getCenterY());
	        assertEpsilonEquals(1, getS().getFirstAxisX());
	        assertEpsilonEquals(0, getS().getFirstAxisY());
	        assertEpsilonEquals(0, getS().getFirstAxisExtent());
	        assertEpsilonEquals(0, getS().getSecondAxisX());
	        assertEpsilonEquals(1, getS().getSecondAxisY());
	        assertEpsilonEquals(0, getS().getSecondAxisExtent());
	    }

    }

    @DisplayName("contains(double,double)")
	@Nested
	public class ContainsDoubleDouble {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().contains(0, 0));
        }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().contains(-20, 0));
        }

        @DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().contains(12, -4));
        }

        @DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().contains(14, 0));
        }

        @DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().contains(15, 0));
        }

        @DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().contains(20, 8));
        }

        @DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().contains(8, 16));
        }

        @DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().contains(-4, 20));
        }

        @DisplayName("#9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().contains(-5, 12));
        }

        @DisplayName("#10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().contains(0, 6));
        }

        @DisplayName("#11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().contains(0, 7));
        }

        @DisplayName("#12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().contains(0, 8));
        }

        @DisplayName("#13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().contains(0, 9));
        }

        @DisplayName("#14")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_14(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().contains(0, 10));
        }

        @DisplayName("#15")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_15(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().contains(0, 27));
        }

        @DisplayName("#16")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_16(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().contains(cx, cy));
        }

        @DisplayName("#17")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_17(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().contains(16, 8));
	    }

    }

    @DisplayName("contains(Point2D)")
	@Nested
	public class ContainsPoint2D {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().contains(createPoint(0, 0)));
        }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().contains(createPoint(-20, 0)));
        }

        @DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().contains(createPoint(12, -4)));
        }

        @DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().contains(createPoint(14, 0)));
        }

        @DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().contains(createPoint(15, 0)));
        }

        @DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().contains(createPoint(20, 8)));
        }

        @DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().contains(createPoint(8, 16)));
        }

        @DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().contains(createPoint(-4, 20)));
        }

        @DisplayName("#9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().contains(createPoint(-5, 12)));
        }

        @DisplayName("#10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().contains(createPoint(0, 6)));
        }

        @DisplayName("#11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().contains(createPoint(0, 7)));
        }

        @DisplayName("#12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().contains(createPoint(0, 8)));
        }

        @DisplayName("#13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().contains(createPoint(0, 9)));
        }

        @DisplayName("#14")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_14(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().contains(createPoint(0, 10)));
        }

        @DisplayName("#15")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_15(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().contains(createPoint(0, 27)));
        }

        @DisplayName("#16")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_16(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().contains(createPoint(cx, cy)));
        }

        @DisplayName("#17")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_17(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().contains(createPoint( 16, 8)));
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
	        var closest = getS().getClosestPointTo(createPoint(-20, 9));
	        assertEpsilonEquals(pHx, closest.getX());
	        assertEpsilonEquals(pHy, closest.getY());
        }

        @DisplayName("(Point2D) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var closest = getS().getClosestPointTo(createPoint(0, 0));
	        assertEpsilonEquals(1.90983, closest.getX());
	        assertEpsilonEquals(1.90983, closest.getY());
        }

        @DisplayName("(Point2D) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var closest = getS().getClosestPointTo(createPoint(5, -10));
	        assertEpsilonEquals(9.40983, closest.getX());
	        assertEpsilonEquals(-5.59017, closest.getY());
        }

        @DisplayName("(Point2D) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var closest = getS().getClosestPointTo(createPoint(14, -20));
	        assertEpsilonEquals(pEx, closest.getX());
	        assertEpsilonEquals(pEy, closest.getY());
        }

        @DisplayName("(Point2D) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var closest = getS().getClosestPointTo(createPoint(-6, 15));
	        assertEpsilonEquals(-3.81679, closest.getX());
	        assertEpsilonEquals(14.4542, closest.getY());
        }

        @DisplayName("(Point2D) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var closest = getS().getClosestPointTo(createPoint(0, 10));
	        assertEpsilonEquals(0, closest.getX());
	        assertEpsilonEquals(10, closest.getY());
        }

        @DisplayName("(Point2D) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var closest = getS().getClosestPointTo(createPoint(10, 0));
	        assertEpsilonEquals(10, closest.getX());
	        assertEpsilonEquals(0, closest.getY());
        }

        @DisplayName("(Point2D) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var closest = getS().getClosestPointTo(createPoint(15, -4));
	        assertEpsilonEquals(13.99326, closest.getX());
	        assertEpsilonEquals(-3.74832, closest.getY());
        }

        @DisplayName("(Point2D) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var closest = getS().getClosestPointTo(createPoint(-5, 25));
	        assertEpsilonEquals(-1.40503, closest.getX());
	        assertEpsilonEquals(24.10126, closest.getY());
        }

        @DisplayName("(Point2D) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var closest = getS().getClosestPointTo(createPoint(0, 20));
	        assertEpsilonEquals(0, closest.getX());
	        assertEpsilonEquals(20, closest.getY());
        }

        @DisplayName("(Point2D) #11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var closest = getS().getClosestPointTo(createPoint(10, 10));
	        assertEpsilonEquals(10, closest.getX());
	        assertEpsilonEquals(10, closest.getY());
        }

        @DisplayName("(Point2D) #12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var closest = getS().getClosestPointTo(createPoint(20, 0));
	        assertEpsilonEquals(15.22856, closest.getX());
	        assertEpsilonEquals(1.19286, closest.getY());
        }

        @DisplayName("(Point2D) #13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var closest = getS().getClosestPointTo(createPoint(-3, 35));
	        assertEpsilonEquals(pGx, closest.getX());
	        assertEpsilonEquals(pGy, closest.getY());
        }

        @DisplayName("(Point2D) #14")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_14(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var closest = getS().getClosestPointTo(createPoint(5, 35));
	        assertEpsilonEquals(pGx, closest.getX());
	        assertEpsilonEquals(pGy, closest.getY());
        }

        @DisplayName("(Point2D) #15")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_15(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var closest = getS().getClosestPointTo(createPoint(20, 15));
	        assertEpsilonEquals(15.59017, closest.getX());
	        assertEpsilonEquals(10.59017, closest.getY());
        }

        @DisplayName("(Point2D) #16")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_16(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var closest = getS().getClosestPointTo(createPoint(35, 10));
	        assertEpsilonEquals(pFx, closest.getX());
	        assertEpsilonEquals(pFy, closest.getY());
        }

        @DisplayName("(Point2D) #17")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_17(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var closest = getS().getClosestPointTo(createPoint(-8, 29));
	        assertEpsilonEquals(pGx, closest.getX());
	        assertEpsilonEquals(pGy, closest.getY());
	    }

        @DisplayName("(Circle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertFpPointEquals(0.90983, 2.90983, getS().getClosestPointTo(createCircle(0, 2, 1)));
        }

        @DisplayName("(Circle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertFpPointEquals(-5.18034, 9, getS().getClosestPointTo(createCircle(-12, 8, 1)));
        }

        @DisplayName("(Circle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertClosestPointInBothShapes(getS(), createCircle(16, 2, 1));
        }

        @DisplayName("(Circle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertClosestPointInBothShapes(getS(), createCircle(12, 10, 1));
	    }

        @DisplayName("(Segment2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertFpPointEquals(0.40983, 3.40983, getS().getClosestPointTo(createSegment(-2, 2, 0, 3)));
	    }

        @DisplayName("(Segment2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertFpPointEquals(-5.18034, 9, getS().getClosestPointTo(createSegment(-12, 8, -10, 9)));
	    }

        @DisplayName("(Segment2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertClosestPointInBothShapes(getS(), createSegment(15, 2, 17, 3));
	    }

        @DisplayName("(Segment2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertClosestPointInBothShapes(getS(), createSegment(12, 10, 14, 11));
        }

        @DisplayName("(Triangle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertFpPointEquals(3.40983, 0.40983, getS().getClosestPointTo(createTestTriangle(-5, -5)));
        }

        @DisplayName("(Triangle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertFpPointEquals(-5.18034, 9, getS().getClosestPointTo(createTestTriangle(-14, 5)));
        }

        @DisplayName("(Triangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertClosestPointInBothShapes(getS(), createTestTriangle(15, 2));
        }

        @DisplayName("(Triangle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertClosestPointInBothShapes(getS(), createTestTriangle(5, 5));
        }

        @DisplayName("(Rectangle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertFpPointEquals(2.40983, 1.40983, getS().getClosestPointTo(createRectangle(-5, -5, 2, 1)));
        }

        @DisplayName("(Rectangle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertFpPointEquals(-5.18034, 9, getS().getClosestPointTo(createRectangle(-14, 5, 2, 1)));
        }

        @DisplayName("(Rectangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertClosestPointInBothShapes(getS(), createRectangle(15, 2, 2, 1));
        }

        @DisplayName("(Rectangle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertClosestPointInBothShapes(getS(), createRectangle(5, 5, 2, 1));
        }

        @DisplayName("(Ellipse2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertFpPointEquals(2.52323, 1.29643, getS().getClosestPointTo(createEllipse(-5, -5, 2, 1)));
        }

        @DisplayName("(Ellipse2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertFpPointEquals(-5.18034, 9, getS().getClosestPointTo(createEllipse(-14, 5, 2, 1)));
        }

        @DisplayName("(Ellipse2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertClosestPointInBothShapes(getS(), createRectangle(15, 2, 2, 1));
        }

        @DisplayName("(Ellipse2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertClosestPointInBothShapes(getS(), createRectangle(5, 5, 2, 1));
        }

        @DisplayName("(RoundRectangle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertFpPointEquals(2.39519, 1.42447, getS().getClosestPointTo(createRoundRectangle(-5, -5, 2, 1, .2, .1)));
        }

        @DisplayName("(RoundRectangle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertFpPointEquals(-5.18034, 9, getS().getClosestPointTo(createRoundRectangle(-14, 5, 2, 1, .2, .1)));
        }

        @DisplayName("(RoundRectangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertClosestPointInBothShapes(getS(), createRoundRectangle(15, 2, 2, 1, .2, .1));
        }

        @DisplayName("(RoundRectangle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertClosestPointInBothShapes(getS(), createRoundRectangle(5, 5, 2, 1, .2, .1));
        }

        @DisplayName("(MultiShape2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void multishape_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertFpPointEquals(4.40983, -0.59017, getS().getClosestPointTo(createTestMultiShape(-5, -5)));
        }

        @DisplayName("(MultiShape2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void multishape_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertFpPointEquals(-5.18034, 9, getS().getClosestPointTo(createTestMultiShape(-18, 5)));
        }

        @DisplayName("(MultiShape2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void multishape_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertClosestPointInBothShapes(getS(), createTestMultiShape(15, 2));
        }

        @DisplayName("(MultiShape2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void multishape_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertClosestPointInBothShapes(getS(), createTestMultiShape(5, 5));
        }

        @DisplayName("(Path2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_1(CoordinateSystem2D cs) {
        	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFpPointEquals(2.15983, 1.65983, getS().getClosestPointTo(createNonEmptyPath(-5, -5)));
        }

        @DisplayName("(Path2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_2(CoordinateSystem2D cs) {
        	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFpPointEquals(-5.18034, 9, getS().getClosestPointTo(createNonEmptyPath(-18, 5)));
        }

        @DisplayName("(Path2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_3(CoordinateSystem2D cs) {
        	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertClosestPointInBothShapes(getS(), createNonEmptyPath(15, 2));
        }

        @DisplayName("(Path2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_4(CoordinateSystem2D cs) {
        	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertClosestPointInBothShapes(getS(), createNonEmptyPath(5, 5));
        }

        @DisplayName("(Parallelogram2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_1(CoordinateSystem2D cs) {
        	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFpPointEquals(2.63744, 1.18222, getS().getClosestPointTo(createTestParallelogram(-5, -5)));
        }

        @DisplayName("(Parallelogram2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_2(CoordinateSystem2D cs) {
        	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFpPointEquals(-5.18034, 9, getS().getClosestPointTo(createTestParallelogram(-18, 5)));
        }

        @DisplayName("(Parallelogram2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_3(CoordinateSystem2D cs) {
        	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertClosestPointInBothShapes(getS(), createTestParallelogram(15, 2));
        }

        @DisplayName("(Parallelogram2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_4(CoordinateSystem2D cs) {
        	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertClosestPointInBothShapes(getS(), createTestParallelogram(5, 5));
        }

        @DisplayName("(OrientedRectangle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_1(CoordinateSystem2D cs) {
        	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFpPointEquals(2.0311, 1.78856, getS().getClosestPointTo(createTestOrientedRectangle(-5, -5)));
        }

        @DisplayName("(OrientedRectangle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_2(CoordinateSystem2D cs) {
        	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFpPointEquals(-5.18034, 9, getS().getClosestPointTo(createTestOrientedRectangle(-18, 5)));
        }

        @DisplayName("(OrientedRectangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_3(CoordinateSystem2D cs) {
        	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertClosestPointInBothShapes(getS(), createTestOrientedRectangle(15, 2));
        }

        @DisplayName("(OrientedRectangle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_4(CoordinateSystem2D cs) {
        	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertClosestPointInBothShapes(getS(), createTestOrientedRectangle(5, 5));
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
	        var farthest = getS().getFarthestPointTo(createPoint(-20, 9));
	        assertEpsilonEquals(pEx, farthest.getX());
	        assertEpsilonEquals(pEy, farthest.getY());
        }

        @DisplayName("(Point2D) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        	var farthest = getS().getFarthestPointTo(createPoint(0, 0));
	        assertEpsilonEquals(pGx, farthest.getX());
	        assertEpsilonEquals(pGy, farthest.getY());
        }

        @DisplayName("(Point2D) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var farthest = getS().getFarthestPointTo(createPoint(5, -10));
	        assertEpsilonEquals(pGx, farthest.getX());
	        assertEpsilonEquals(pGy, farthest.getY());
        }

        @DisplayName("(Point2D) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var farthest = getS().getFarthestPointTo(createPoint(14, -20));
	        assertEpsilonEquals(pGx, farthest.getX());
	        assertEpsilonEquals(pGy, farthest.getY());
        }

        @DisplayName("(Point2D) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var farthest = getS().getFarthestPointTo(createPoint(-6, 15));
	        assertEpsilonEquals(pEx, farthest.getX());
	        assertEpsilonEquals(pEy, farthest.getY());
        }

        @DisplayName("(Point2D) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var farthest = getS().getFarthestPointTo(createPoint(0, 10));
	        assertEpsilonEquals(pEx, farthest.getX());
	        assertEpsilonEquals(pEy, farthest.getY());
        }

        @DisplayName("(Point2D) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var farthest = getS().getFarthestPointTo(createPoint(10, 0));
	        assertEpsilonEquals(pGx, farthest.getX());
	        assertEpsilonEquals(pGy, farthest.getY());
        }

        @DisplayName("(Point2D) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var farthest = getS().getFarthestPointTo(createPoint(15, -4));
	        assertEpsilonEquals(pGx, farthest.getX());
	        assertEpsilonEquals(pGy, farthest.getY());
        }

        @DisplayName("(Point2D) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var farthest = getS().getFarthestPointTo(createPoint(-5, 25));
	        assertEpsilonEquals(pEx, farthest.getX());
	        assertEpsilonEquals(pEy, farthest.getY());
        }

        @DisplayName("(Point2D) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var farthest = getS().getFarthestPointTo(createPoint(0, 20));
	        assertEpsilonEquals(pEx, farthest.getX());
	        assertEpsilonEquals(pEy, farthest.getY());
        }

        @DisplayName("(Point2D) #11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var farthest = getS().getFarthestPointTo(createPoint(10, 10));
	        assertEpsilonEquals(pGx, farthest.getX());
	        assertEpsilonEquals(pGy, farthest.getY());
        }

        @DisplayName("(Point2D) #12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var farthest = getS().getFarthestPointTo(createPoint(20, 0));
	        assertEpsilonEquals(pGx, farthest.getX());
	        assertEpsilonEquals(pGy, farthest.getY());
        }

        @DisplayName("(Point2D) #13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var farthest = getS().getFarthestPointTo(createPoint(-3, 35));
	        assertEpsilonEquals(pEx, farthest.getX());
	        assertEpsilonEquals(pEy, farthest.getY());
        }

        @DisplayName("(Point2D) #14")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_14(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var farthest = getS().getFarthestPointTo(createPoint(5, 35));
	        assertEpsilonEquals(pEx, farthest.getX());
	        assertEpsilonEquals(pEy, farthest.getY());
        }

        @DisplayName("(Point2D) #15")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_15(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var farthest = getS().getFarthestPointTo(createPoint(20, 15));
	        assertEpsilonEquals(pHx, farthest.getX());
	        assertEpsilonEquals(pHy, farthest.getY());
        }

        @DisplayName("(Point2D) #16")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_16(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var farthest = getS().getFarthestPointTo(createPoint(35, 10));
	        assertEpsilonEquals(pHx, farthest.getX());
	        assertEpsilonEquals(pHy, farthest.getY());
        }

        @DisplayName("(Point2D) #17")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_17(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var farthest = getS().getFarthestPointTo(createPoint(-8, 29));
	        assertEpsilonEquals(pEx, farthest.getX());
	        assertEpsilonEquals(pEy, farthest.getY());
	    }

    }

    @DisplayName("translate(double,double)")
	@Nested
	public class TranslateDoubleDouble {

        @DisplayName("(Point2D) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        getS().translate(123.456, 789.123);
	        assertEpsilonEquals(cx + 123.456, getS().getCenterX());
	        assertEpsilonEquals(cy + 789.123, getS().getCenterY());
	        assertEpsilonEquals(ux, getS().getFirstAxisX());
	        assertEpsilonEquals(uy, getS().getFirstAxisY());
	        assertEpsilonEquals(e1, getS().getFirstAxisExtent());
	        assertEpsilonEquals(vx, getS().getSecondAxisX());
	        assertEpsilonEquals(vy, getS().getSecondAxisY());
	        assertEpsilonEquals(e2, getS().getSecondAxisExtent());
	    }

    }

    @DisplayName("translate(Vector2D)")
	@Nested
	public class TranslateVector2D {

        @DisplayName("(Point2D) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        getS().translate(createVector(123.456, 789.123));
	        assertEpsilonEquals(cx + 123.456, getS().getCenterX());
	        assertEpsilonEquals(cy + 789.123, getS().getCenterY());
	        assertEpsilonEquals(ux, getS().getFirstAxisX());
	        assertEpsilonEquals(uy, getS().getFirstAxisY());
	        assertEpsilonEquals(e1, getS().getFirstAxisExtent());
	        assertEpsilonEquals(vx, getS().getSecondAxisX());
	        assertEpsilonEquals(vy, getS().getSecondAxisY());
	        assertEpsilonEquals(e2, getS().getSecondAxisExtent());
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
	        assertEpsilonEquals(14.81966, getS().getDistance(createPoint(-20, 9)));
        }

        @DisplayName("(Point2D) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        	assertEpsilonEquals(2.7009, getS().getDistance(createPoint(0, 0)));
        }

        @DisplayName("(Point2D) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(6.23644, getS().getDistance(createPoint(5, -10)));
        }

        @DisplayName("(Point2D) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(11.1863, getS().getDistance(createPoint(14, -20)));
        }

        @DisplayName("(Point2D) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(2.25040, getS().getDistance(createPoint(-6, 15)));
        }

        @DisplayName("(Point2D) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(0, getS().getDistance(createPoint(0, 10)));
        }

        @DisplayName("(Point2D) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(0, getS().getDistance(createPoint(10, 0)));
        }

        @DisplayName("(Point2D) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(1.03772, getS().getDistance(createPoint(15, -4)));
        }

        @DisplayName("(Point2D) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(3.70561, getS().getDistance(createPoint(-5, 25)));
        }

        @DisplayName("(Point2D) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(0, getS().getDistance(createPoint(0, 20)));
        }

        @DisplayName("(Point2D) #11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(0, getS().getDistance(createPoint(10, 10)));
        }

        @DisplayName("(Point2D) #12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(4.91829, getS().getDistance(createPoint(20, 0)));
        }

        @DisplayName("(Point2D) #13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(8.42901, getS().getDistance(createPoint(-3, 35)));
        }

        @DisplayName("(Point2D) #14")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_14(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(9.91864, getS().getDistance(createPoint(5, 35)));
        }

        @DisplayName("(Point2D) #15")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_15(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(6.23644, getS().getDistance(createPoint(20, 15)));
        }

        @DisplayName("(Point2D) #16")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_16(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(17.8477, getS().getDistance(createPoint(35, 10)));
        }

        @DisplayName("(Point2D) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_17(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(7.59135, getS().getDistance(createPoint(-8, 29)));
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
	        assertEpsilonEquals(219.62232, getS().getDistanceSquared(createPoint(-20, 9)));
        }

        @DisplayName("(Point2D) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(7.29486, getS().getDistanceSquared(createPoint(0, 0)));
        }

        @DisplayName("(Point2D) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(38.89318, getS().getDistanceSquared(createPoint(5, -10)));
        }

        @DisplayName("(Point2D) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(125.13319, getS().getDistanceSquared(createPoint(14, -20)));
        }

        @DisplayName("(Point2D) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(5.0643, getS().getDistanceSquared(createPoint(-6, 15)));
        }

        @DisplayName("(Point2D) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(0, getS().getDistanceSquared(createPoint(0, 10)));
        }

        @DisplayName("(Point2D) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(0, getS().getDistanceSquared(createPoint(10, 0)));
        }

        @DisplayName("(Point2D) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(1.07686, getS().getDistanceSquared(createPoint(15, -4)));
        }

        @DisplayName("(Point2D) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(13.73155, getS().getDistanceSquared(createPoint(-5, 25)));
        }

        @DisplayName("(Point2D) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(0, getS().getDistanceSquared(createPoint(0, 20)));
        }

        @DisplayName("(Point2D) #11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(0, getS().getDistanceSquared(createPoint(10, 10)));
        }

        @DisplayName("(Point2D) #12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(24.18958, getS().getDistanceSquared(createPoint(20, 0)));
        }

        @DisplayName("(Point2D) #13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(71.04805, getS().getDistanceSquared(createPoint(-3, 35)));
        }

        @DisplayName("(Point2D) #14")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_14(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(98.37931, getS().getDistanceSquared(createPoint(5, 35)));
        }

        @DisplayName("(Point2D) #15")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_15(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(38.89318, getS().getDistanceSquared(createPoint(20, 15)));
        }

        @DisplayName("(Point2D) #16")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_16(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(318.54029, getS().getDistanceSquared(createPoint(35, 10)));
        }

        @DisplayName("(Point2D) #17")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_17(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(57.62859, getS().getDistanceSquared(createPoint(-8, 29)));
	    }

        @DisplayName("(Circle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0.08219, getS().getDistanceSquared(createCircle(0, 2, 1)));
	    }

        @DisplayName("(Circle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(34.72259, getS().getDistanceSquared(createCircle(-12, 8, 1)));
	    }

        @DisplayName("(Circle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0, getS().getDistanceSquared(createCircle(16, 2, 1)));
	    }

        @DisplayName("(Circle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0, getS().getDistanceSquared(createCircle(12, 10, 1)));
        }

        @DisplayName("(Segment2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0.33592, getS().getDistanceSquared(createSegment(-2, 2, 0, 3)));
        }

        @DisplayName("(Segment2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(23.22912, getS().getDistanceSquared(createSegment(-12, 8, -10, 9)));
        }

        @DisplayName("(Segment2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0, getS().getDistanceSquared(createSegment(15, 2, 17, 3)));
        }

        @DisplayName("(Segment2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0, getS().getDistanceSquared(createSegment(12, 10, 14, 11)));
        }

        @DisplayName("(Triangle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(11.61456, getS().getDistanceSquared(createTestTriangle(-5, -5)));
        }

        @DisplayName("(Triangle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(8.95048, getS().getDistanceSquared(createTestTriangle(-14, 5)));
        }

        @DisplayName("(Triangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0, getS().getDistanceSquared(createTestTriangle(15, 2)));
        }

        @DisplayName("(Triangle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0, getS().getDistanceSquared(createTestTriangle(5, 5)));
        }

        @DisplayName("(Rectangle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(58.53252, getS().getDistanceSquared(createRectangle(-5, -5, 2, 1)));
        }

        @DisplayName("(Rectangle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(55.50776, getS().getDistanceSquared(createRectangle(-14, 5, 2, 1)));
        }

        @DisplayName("(Rectangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0, getS().getDistanceSquared(createRectangle(15, 2, 2, 1)));
        }

        @DisplayName("(Rectangle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0, getS().getDistanceSquared(createRectangle(5, 5, 2, 1)));
        }

        @DisplayName("(Ellipse2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(62.73969, getS().getDistanceSquared(createEllipse(-5, -5, 2, 1)));
        }

        @DisplayName("(Ellipse2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(58.33165, getS().getDistanceSquared(createEllipse(-14, 5, 2, 1)));
        }

        @DisplayName("(Ellipse2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0, getS().getDistanceSquared(createEllipse(15, 2, 2, 1)));
        }

        @DisplayName("(Ellipse2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0, getS().getDistanceSquared(createEllipse(5, 5, 2, 1)));
        }

        @DisplayName("(RoundRectangle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_1(CoordinateSystem2D cs) {
        	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(59.36398, getS().getDistanceSquared(createRoundRectangle(-5, -5, 2, 1, .2, .1)));
        }

        @DisplayName("(RoundRectangle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_2(CoordinateSystem2D cs) {
        	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(56.0487, getS().getDistanceSquared(createRoundRectangle(-14, 5, 2, 1, .2, .1)));
        }

        @DisplayName("(RoundRectangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_3(CoordinateSystem2D cs) {
        	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0, getS().getDistanceSquared(createRoundRectangle(15, 2, 2, 1, .2, .1)));
        }

        @DisplayName("(RoundRectangle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_4(CoordinateSystem2D cs) {
        	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0, getS().getDistanceSquared(createRoundRectangle(5, 5, 2, 1, .2, .1)));
        }

        @DisplayName("(MultiShape2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void multishape_1(CoordinateSystem2D cs) {
        	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(11.61456, getS().getDistanceSquared(createTestMultiShape(-5, -5)));
        }

        @DisplayName("(MultiShape2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void multishape_2(CoordinateSystem2D cs) {
        	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(37.86844, getS().getDistanceSquared(createTestMultiShape(-18, 5)));
        }

        @DisplayName("(MultiShape2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void multishape_3(CoordinateSystem2D cs) {
        	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0, getS().getDistanceSquared(createTestMultiShape(15, 2)));
        }

        @DisplayName("(MultiShape2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void multishape_4(CoordinateSystem2D cs) {
        	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0, getS().getDistanceSquared(createTestMultiShape(5, 5)));
        }

        @DisplayName("(Path2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_1(CoordinateSystem2D cs) {
        	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(75.88701, getS().getDistanceSquared(createNonEmptyPath(-5, -5)));
        }

        @DisplayName("(Path2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_2(CoordinateSystem2D cs) {
        	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(151.95437, getS().getDistanceSquared(createNonEmptyPath(-18, 5)));
        }

        @DisplayName("(Path2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_3(CoordinateSystem2D cs) {
        	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0, getS().getDistanceSquared(createNonEmptyPath(15, 2)));
        }

        @DisplayName("(Path2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_4(CoordinateSystem2D cs) {
        	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0, getS().getDistanceSquared(createNonEmptyPath(5, 5)));
        }

        @DisplayName("(Parallelogram2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_1(CoordinateSystem2D cs) {
        	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(49.8011, getS().getDistanceSquared(createTestParallelogram(-5, -5)));
        }

        @DisplayName("(Parallelogram2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_2(CoordinateSystem2D cs) {
        	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(111.35891, getS().getDistanceSquared(createTestParallelogram(-18, 5)));
        }

        @DisplayName("(Parallelogram2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_3(CoordinateSystem2D cs) {
        	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0, getS().getDistanceSquared(createTestParallelogram(15, 2)));
        }

        @DisplayName("(Parallelogram2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_4(CoordinateSystem2D cs) {
        	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0, getS().getDistanceSquared(createTestParallelogram(5, 5)));
        }
        
        @DisplayName("(OrientedRectangle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_1(CoordinateSystem2D cs) {
        	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(56.88921, getS().getDistanceSquared(createTestOrientedRectangle(-5, -5)));
        }
        
        @DisplayName("(OrientedRectangle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_2(CoordinateSystem2D cs) {
        	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(130.12055, getS().getDistanceSquared(createTestOrientedRectangle(-18, 5)));
        }
        
        @DisplayName("(OrientedRectangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_3(CoordinateSystem2D cs) {
        	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0, getS().getDistanceSquared(createTestOrientedRectangle(15, 2)));
        }
        
        @DisplayName("(OrientedRectangle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_4(CoordinateSystem2D cs) {
        	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertEpsilonEquals(0, getS().getDistanceSquared(createTestOrientedRectangle(5, 5)));
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
	        assertEpsilonEquals(14.81966, getS().getDistanceL1(createPoint(-20, 9)));
        }

        @DisplayName("(Point2D) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(3.81966, getS().getDistanceL1(createPoint(0, 0)));
        }

        @DisplayName("(Point2D) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(8.81966, getS().getDistanceL1(createPoint(5, -10)));
        }

        @DisplayName("(Point2D) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(12.40325, getS().getDistanceL1(createPoint(14, -20)));
        }

        @DisplayName("(Point2D) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(2.72901, getS().getDistanceL1(createPoint(-6, 15)));
        }

        @DisplayName("(Point2D) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(0, getS().getDistanceL1(createPoint(0, 10)));
        }

        @DisplayName("(Point2D) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(0, getS().getDistanceL1(createPoint(10, 0)));
        }

        @DisplayName("(Point2D) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(1.25842, getS().getDistanceL1(createPoint(15, -4)));
        }

        @DisplayName("(Point2D) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(4.49371, getS().getDistanceL1(createPoint(-5, 25)));
        }

        @DisplayName("(Point2D) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(0, getS().getDistanceL1(createPoint(0, 20)));
        }

        @DisplayName("(Point2D) #11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(0, getS().getDistanceL1(createPoint(10, 10)));
        }

        @DisplayName("(Point2D) #12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(5.9643, getS().getDistanceL1(createPoint(20, 0)));
        }

        @DisplayName("(Point2D) #13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(10.40326, getS().getDistanceL1(createPoint(-3, 35)));
        }

        @DisplayName("(Point2D) #14")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_14(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(13.81966, getS().getDistanceL1(createPoint(5, 35)));
        }

        @DisplayName("(Point2D) #15")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_15(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(8.81966, getS().getDistanceL1(createPoint(20, 15)));
        }

        @DisplayName("(Point2D) #16")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_16(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(18.81966, getS().getDistanceL1(createPoint(35, 10)));
        }

        @DisplayName("(Point2D) #17")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_17(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(9.40326, getS().getDistanceL1(createPoint(-8, 29)));
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
	        assertEpsilonEquals(14.81966, getS().getDistanceLinf(createPoint(-20, 9)));
        }

        @DisplayName("(Point2D) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        	assertEpsilonEquals(1.90983, getS().getDistanceLinf(createPoint(0, 0)));
        }

        @DisplayName("(Point2D) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(4.40983, getS().getDistanceLinf(createPoint(5, -10)));
        }

        @DisplayName("(Point2D) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(11.11146, getS().getDistanceLinf(createPoint(14, -20)));
        }

        @DisplayName("(Point2D) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(2.18321, getS().getDistanceLinf(createPoint(-6, 15)));
        }

        @DisplayName("(Point2D) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(0, getS().getDistanceLinf(createPoint(0, 10)));
        }

        @DisplayName("(Point2D) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(0, getS().getDistanceLinf(createPoint(10, 0)));
        }

        @DisplayName("(Point2D) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(1.00674, getS().getDistanceLinf(createPoint(15, -4)));
        }

        @DisplayName("(Point2D) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(3.59497, getS().getDistanceLinf(createPoint(-5, 25)));
        }

        @DisplayName("(Point2D) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(0, getS().getDistanceLinf(createPoint(0, 20)));
        }

        @DisplayName("(Point2D) #11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(0, getS().getDistanceLinf(createPoint(10, 10)));
        }

        @DisplayName("(Point2D) #12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(4.77144, getS().getDistanceLinf(createPoint(20, 0)));
        }

        @DisplayName("(Point2D) #13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(8.11146, getS().getDistanceLinf(createPoint(-3, 35)));
        }

        @DisplayName("(Point2D) #14")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_14(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(8.11146, getS().getDistanceLinf(createPoint(5, 35)));
        }

        @DisplayName("(Point2D) #15")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_15(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(4.40983, getS().getDistanceLinf(createPoint(20, 15)));
        }

        @DisplayName("(Point2D) #16")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_16(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(17.81966, getS().getDistanceLinf(createPoint(35, 10)));
        }

        @DisplayName("(Point2D) #17")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_17(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(7.2918, getS().getDistanceLinf(createPoint(-8, 29)));
	    }

    }

    @DisplayName("set(IT)")
	@Nested
	public class SetIT {

        @DisplayName("(Point2D) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        getS().set((T) createParallelogram(17, 20, 1, 0, 15, 0, 1, 14));
	        assertEpsilonEquals(17, getS().getCenterX());
	        assertEpsilonEquals(20, getS().getCenterY());
	        assertEpsilonEquals(1, getS().getFirstAxisX());
	        assertEpsilonEquals(0, getS().getFirstAxisY());
	        assertEpsilonEquals(15, getS().getFirstAxisExtent());
	        assertEpsilonEquals(0, getS().getSecondAxisX());
	        assertEpsilonEquals(1, getS().getSecondAxisY());
	        assertEpsilonEquals(14, getS().getSecondAxisExtent());
	    }

    }

    @DisplayName("getPathIterator")
	@Nested
	public class GetPathIterator {

        @DisplayName("(Point2D) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        PathIterator2afp pi = getS().getPathIterator();
	        assertElement(pi, PathElementType.MOVE_TO, pGx, pGy);
	        assertElement(pi, PathElementType.LINE_TO, pHx, pHy);
	        assertElement(pi, PathElementType.LINE_TO, pEx, pEy);
	        assertElement(pi, PathElementType.LINE_TO, pFx, pFy);
	        assertElement(pi, PathElementType.CLOSE, pGx, pGy);
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
	        PathIterator2afp pi = getS().getPathIterator(null);
	        assertElement(pi, PathElementType.MOVE_TO, pGx, pGy);
	        assertElement(pi, PathElementType.LINE_TO, pHx, pHy);
	        assertElement(pi, PathElementType.LINE_TO, pEx, pEy);
	        assertElement(pi, PathElementType.LINE_TO, pFx, pFy);
	        assertElement(pi, PathElementType.CLOSE, pGx, pGy);
	        assertNoElement(pi);
        }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var transform = new Transform2D();
	        var pi = getS().getPathIterator(transform);
	        assertElement(pi, PathElementType.MOVE_TO, pGx, pGy);
	        assertElement(pi, PathElementType.LINE_TO, pHx, pHy);
	        assertElement(pi, PathElementType.LINE_TO, pEx, pEy);
	        assertElement(pi, PathElementType.LINE_TO, pFx, pFy);
	        assertElement(pi, PathElementType.CLOSE, pGx, pGy);
	        assertNoElement(pi);
        }

        @DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var transform = new Transform2D();
	        transform.setTranslation(18,  -45);
	        var pi = getS().getPathIterator(transform);
	        assertElement(pi, PathElementType.MOVE_TO, pGx + 18, pGy - 45);
	        assertElement(pi, PathElementType.LINE_TO, pHx + 18, pHy - 45);
	        assertElement(pi, PathElementType.LINE_TO, pEx + 18, pEy - 45);
	        assertElement(pi, PathElementType.LINE_TO, pFx + 18, pFy - 45);
	        assertElement(pi, PathElementType.CLOSE, pGx + 18, pGy - 45);
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
	        PathIterator2afp pi = getS().createTransformedShape(null).getPathIterator();
	        assertElement(pi, PathElementType.MOVE_TO, pGx, pGy);
	        assertElement(pi, PathElementType.LINE_TO, pHx, pHy);
	        assertElement(pi, PathElementType.LINE_TO, pEx, pEy);
	        assertElement(pi, PathElementType.LINE_TO, pFx, pFy);
	        assertElement(pi, PathElementType.CLOSE, pGx, pGy);
	        assertNoElement(pi);
        }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var transform = new Transform2D();
	        var pi = getS().createTransformedShape(transform).getPathIterator();
	        assertElement(pi, PathElementType.MOVE_TO, pGx, pGy);
	        assertElement(pi, PathElementType.LINE_TO, pHx, pHy);
	        assertElement(pi, PathElementType.LINE_TO, pEx, pEy);
	        assertElement(pi, PathElementType.LINE_TO, pFx, pFy);
	        assertElement(pi, PathElementType.CLOSE, pGx, pGy);
	        assertNoElement(pi);
        }

        @DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        var transform = new Transform2D();
	        transform.setTranslation(18,  -45);
	        var pi = getS().createTransformedShape(transform).getPathIterator();
	        assertElement(pi, PathElementType.MOVE_TO, pGx + 18, pGy - 45);
	        assertElement(pi, PathElementType.LINE_TO, pHx + 18, pHy - 45);
	        assertElement(pi, PathElementType.LINE_TO, pEx + 18, pEy - 45);
	        assertElement(pi, PathElementType.LINE_TO, pFx + 18, pFy - 45);
	        assertElement(pi, PathElementType.CLOSE, pGx + 18, pGy - 45);
	        assertNoElement(pi);
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
	        assertEpsilonEquals(pHx, box.getMinX());
	        assertEpsilonEquals(pEy, box.getMinY());
	        assertEpsilonEquals(pFx, box.getMaxX());
	        assertEpsilonEquals(pGy, box.getMaxY());
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
	        assertEpsilonEquals(pHx, box.getMinX());
	        assertEpsilonEquals(pEy, box.getMinY());
	        assertEpsilonEquals(pFx, box.getMaxX());
	        assertEpsilonEquals(pGy, box.getMaxY());
	    }

    }

    @DisplayName("contains")
	@Nested
	public class Contains {

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
	        assertFalse(getS().contains(createRectangle(0, 1, 1, 1)));
        }

        @DisplayName("(Rectangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().contains(createRectangle(0, 2, 1, 1)));
        }

        @DisplayName("(Rectangle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(getS().contains(createRectangle(0, 3, 1, 1)));
        }

        @DisplayName("(Rectangle2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().contains(createRectangle(0, 4, 1, 1)));
        }

        @DisplayName("(Rectangle2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().contains(createRectangle(0, 5, 1, 1)));
        }

        @DisplayName("(Rectangle2afp) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertTrue(getS().contains(createRectangle(0, 6, 1, 1)));
	    }

        @DisplayName("(Circle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
    	@EnumSource(CoordinateSystem2D.class)
        public void shape_1(CoordinateSystem2D cs) {
        	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFalse(getS().contains(createCircle(0, 0, 1)));
	    }

        @DisplayName("(Circle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
    	@EnumSource(CoordinateSystem2D.class)
        public void shape_2(CoordinateSystem2D cs) {
        	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFalse(getS().contains(createCircle(0, 1, 1)));
	    }

        @DisplayName("(Circle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
    	@EnumSource(CoordinateSystem2D.class)
        public void shape_3(CoordinateSystem2D cs) {
        	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFalse(getS().contains(createCircle(0, 2, 1)));
	    }

        @DisplayName("(Circle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
    	@EnumSource(CoordinateSystem2D.class)
        public void shape_4(CoordinateSystem2D cs) {
        	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFalse(getS().contains(createCircle(0, 3, 1)));
	    }

        @DisplayName("(Circle2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
    	@EnumSource(CoordinateSystem2D.class)
        public void shape_5(CoordinateSystem2D cs) {
        	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFalse(getS().contains(createCircle(0, 4, 1)));
	    }

        @DisplayName("(Circle2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
    	@EnumSource(CoordinateSystem2D.class)
        public void shape_6(CoordinateSystem2D cs) {
        	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertFalse(getS().contains(createCircle(0, 5, 1)));
	    }

        @DisplayName("(Circle2afp) #7")
    	@ParameterizedTest(name = "{index} => {0}")
    	@EnumSource(CoordinateSystem2D.class)
        public void shape_7(CoordinateSystem2D cs) {
        	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertTrue(getS().contains(createCircle(0, 6, 1)));
        }

    }

    @DisplayName("rotate(double)")
	@Nested
	public class RotateDouble {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        getS().rotate(-MathConstants.DEMI_PI);
	        assertEpsilonEquals(6, getS().getCenterX());
	        assertEpsilonEquals(9, getS().getCenterY());
	        assertEpsilonEquals(9.701400000000000e-01, getS().getFirstAxisX());
	        assertEpsilonEquals(-2.425400000000000e-01, getS().getFirstAxisY());
	        assertEpsilonEquals(9.21954, getS().getFirstAxisExtent());
	        assertEpsilonEquals(7.071100000000000e-01, getS().getSecondAxisX());
	        assertEpsilonEquals(7.071100000000000e-01, getS().getSecondAxisY());
	        assertEpsilonEquals(12.64911, getS().getSecondAxisExtent());
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
	        Point2D c = getS().getCenter();
	        assertEpsilonEquals(6, c.getX());
	        assertEpsilonEquals(9, c.getY());
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
	        assertEpsilonEquals(6, getS().getCenterX());
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
	        assertEpsilonEquals(9, getS().getCenterY());
	    }

    }

    @DisplayName("setCenter(double,double)")
	@Nested
	public class SetCenterDoubleDouble {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        getS().setCenter(123.456, -789.123);
	        assertEpsilonEquals(123.456, getS().getCenterX());
	        assertEpsilonEquals(-789.123, getS().getCenterY());
	        assertEpsilonEquals(ux, getS().getFirstAxisX());
	        assertEpsilonEquals(uy, getS().getFirstAxisY());
	        assertEpsilonEquals(e1, getS().getFirstAxisExtent());
	        assertEpsilonEquals(vx, getS().getSecondAxisX());
	        assertEpsilonEquals(vy, getS().getSecondAxisY());
	        assertEpsilonEquals(e2, getS().getSecondAxisExtent());
	    }

    }

    @DisplayName("setCenter(Point2D)")
	@Nested
	public class SetCenterPoint2D {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        getS().setCenter(createPoint(123.456, -789.123));
	        assertEpsilonEquals(123.456, getS().getCenterX());
	        assertEpsilonEquals(-789.123, getS().getCenterY());
	        assertEpsilonEquals(ux, getS().getFirstAxisX());
	        assertEpsilonEquals(uy, getS().getFirstAxisY());
	        assertEpsilonEquals(e1, getS().getFirstAxisExtent());
	        assertEpsilonEquals(vx, getS().getSecondAxisX());
	        assertEpsilonEquals(vy, getS().getSecondAxisY());
	        assertEpsilonEquals(e2, getS().getSecondAxisExtent());
	    }

    }

    @DisplayName("setCenterX")
	@Nested
	public class SetCenterX {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        getS().setCenterX(123.456);
	        assertEpsilonEquals(123.456, getS().getCenterX());
	        assertEpsilonEquals(cy, getS().getCenterY());
	        assertEpsilonEquals(ux, getS().getFirstAxisX());
	        assertEpsilonEquals(uy, getS().getFirstAxisY());
	        assertEpsilonEquals(e1, getS().getFirstAxisExtent());
	        assertEpsilonEquals(vx, getS().getSecondAxisX());
	        assertEpsilonEquals(vy, getS().getSecondAxisY());
	        assertEpsilonEquals(e2, getS().getSecondAxisExtent());
	    }

    }

    @DisplayName("setCenterY")
	@Nested
	public class SetCenterY {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        getS().setCenterY(123.456);
	        assertEpsilonEquals(cx, getS().getCenterX());
	        assertEpsilonEquals(123.456, getS().getCenterY());
	        assertEpsilonEquals(ux, getS().getFirstAxisX());
	        assertEpsilonEquals(uy, getS().getFirstAxisY());
	        assertEpsilonEquals(e1, getS().getFirstAxisExtent());
	        assertEpsilonEquals(vx, getS().getSecondAxisX());
	        assertEpsilonEquals(vy, getS().getSecondAxisY());
	        assertEpsilonEquals(e2, getS().getSecondAxisExtent());
	    }

    }

    @DisplayName("getFirstAxis")
	@Nested
	public class GetFirstAxis {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        Vector2D v = getS().getFirstAxis();
	        assertEpsilonEquals(ux, v.getX());
	        assertEpsilonEquals(uy, v.getY());
	    }

    }

    @DisplayName("getFirstAxisX")
	@Nested
	public class GetFirstAxisX {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(ux, getS().getFirstAxisX());
	    }

    }

    @DisplayName("getFirstAxisY")
	@Nested
	public class GetFirstAxisY {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(uy, getS().getFirstAxisY());
	    }

    }

    @DisplayName("getSecondAxis")
	@Nested
	public class GetSecondAxis {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        Vector2D v = getS().getSecondAxis();
	        assertEpsilonEquals(vx, v.getX());
	        assertEpsilonEquals(vy, v.getY());
	    }

    }

    @DisplayName("getSecondAxisX")
	@Nested
	public class GetSecondAxisX {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(vx, getS().getSecondAxisX());
	    }

    }

    @DisplayName("getSecondAxisY")
	@Nested
	public class GetSecondAxisY {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(vy, getS().getSecondAxisY());
	    }

    }

    @DisplayName("getFirstAxisExtent")
	@Nested
	public class GetFirstAxisExtent {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(e1, getS().getFirstAxisExtent());
	    }

    }

    @DisplayName("setFirstAxisExtent")
	@Nested
	public class SetFirstAxisExtent {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        getS().setFirstAxisExtent(123.456);
	        assertEpsilonEquals(cx, getS().getCenterX());
	        assertEpsilonEquals(cy, getS().getCenterY());
	        assertEpsilonEquals(ux, getS().getFirstAxisX());
	        assertEpsilonEquals(uy, getS().getFirstAxisY());
	        assertEpsilonEquals(123.456, getS().getFirstAxisExtent());
	        assertEpsilonEquals(vx, getS().getSecondAxisX());
	        assertEpsilonEquals(vy, getS().getSecondAxisY());
	        assertEpsilonEquals(e2, getS().getSecondAxisExtent());
	    }

    }

    @DisplayName("getSecondAxisExtent")
	@Nested
	public class GetSecondAxisExtent {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertEpsilonEquals(e2, getS().getSecondAxisExtent());
	    }

    }

    @DisplayName("setSecondAxisExtent")
	@Nested
	public class SetSecondAxisExtent {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        getS().setSecondAxisExtent(123.456);
	        assertEpsilonEquals(cx, getS().getCenterX());
	        assertEpsilonEquals(cy, getS().getCenterY());
	        assertEpsilonEquals(ux, getS().getFirstAxisX());
	        assertEpsilonEquals(uy, getS().getFirstAxisY());
	        assertEpsilonEquals(e1, getS().getFirstAxisExtent());
	        assertEpsilonEquals(vx, getS().getSecondAxisX());
	        assertEpsilonEquals(vy, getS().getSecondAxisY());
	        assertEpsilonEquals(123.456, getS().getSecondAxisExtent());
	    }

    }

    @DisplayName("setFirstAxis(double,double)")
	@Nested
	public class SetFirstAxisDoubleDouble {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        Vector2D newU = createVector(123.456, 456.789).toUnitVector();
	        getS().setFirstAxis(newU.getX(), newU.getY());
	        assertEpsilonEquals(cx, getS().getCenterX());
	        assertEpsilonEquals(cy, getS().getCenterY());
	        assertEpsilonEquals(newU.getX(), getS().getFirstAxisX());
	        assertEpsilonEquals(newU.getY(), getS().getFirstAxisY());
	        assertEpsilonEquals(e1, getS().getFirstAxisExtent());
	        assertEpsilonEquals(vx, getS().getSecondAxisX());
	        assertEpsilonEquals(vy, getS().getSecondAxisY());
	        assertEpsilonEquals(e2, getS().getSecondAxisExtent());
	    }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertThrows(AssertionError.class, () -> getS().setFirstAxis(123.456, 456.789));
        }

    }

    @DisplayName("setFirstAxis(Vector2D)")
	@Nested
	public class SetFirstAxisVector2D {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        Vector2D newU = createVector(123.456, 456.789).toUnitVector();
	        getS().setFirstAxis(newU);
	        assertEpsilonEquals(cx, getS().getCenterX());
	        assertEpsilonEquals(cy, getS().getCenterY());
	        assertEpsilonEquals(newU.getX(), getS().getFirstAxisX());
	        assertEpsilonEquals(newU.getY(), getS().getFirstAxisY());
	        assertEpsilonEquals(e1, getS().getFirstAxisExtent());
	        assertEpsilonEquals(vx, getS().getSecondAxisX());
	        assertEpsilonEquals(vy, getS().getSecondAxisY());
	        assertEpsilonEquals(e2, getS().getSecondAxisExtent());
	    }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertThrows(AssertionError.class, () -> getS().setFirstAxis(createVector(123.456, 456.789)));
	    }

    }

    @DisplayName("setFirstAxis(Vector2D,double)")
	@Nested
	public class SetFirstAxisVector2DDouble {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        Vector2D newU = createVector(123.456, 456.789).toUnitVector();
	        getS().setFirstAxis(newU, 159.753);
	        assertEpsilonEquals(cx, getS().getCenterX());
	        assertEpsilonEquals(cy, getS().getCenterY());
	        assertEpsilonEquals(newU.getX(), getS().getFirstAxisX());
	        assertEpsilonEquals(newU.getY(), getS().getFirstAxisY());
	        assertEpsilonEquals(159.753, getS().getFirstAxisExtent());
	        assertEpsilonEquals(vx, getS().getSecondAxisX());
	        assertEpsilonEquals(vy, getS().getSecondAxisY());
	        assertEpsilonEquals(e2, getS().getSecondAxisExtent());
	    }

    }

    @DisplayName("setFirstAxis(double,double,double)")
	@Nested
	public class SetFirstAxisDoubleDoubleDouble {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        Vector2D newU = createVector(123.456, 456.789).toUnitVector();
	        getS().setFirstAxis(newU.getX(), newU.getY(), 159.753);
	        assertEpsilonEquals(cx, getS().getCenterX());
	        assertEpsilonEquals(cy, getS().getCenterY());
	        assertEpsilonEquals(newU.getX(), getS().getFirstAxisX());
	        assertEpsilonEquals(newU.getY(), getS().getFirstAxisY());
	        assertEpsilonEquals(159.753, getS().getFirstAxisExtent());
	        assertEpsilonEquals(vx, getS().getSecondAxisX());
	        assertEpsilonEquals(vy, getS().getSecondAxisY());
	        assertEpsilonEquals(e2, getS().getSecondAxisExtent());
	    }

    }

    @DisplayName("setSecondAxis(double,double)")
	@Nested
	public class SetSecondAxisDoubleDouble {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        Vector2D newV = createVector(123.456, 456.789).toUnitVector();
	        getS().setSecondAxis(newV.getX(), newV.getY());
	        assertEpsilonEquals(cx, getS().getCenterX());
	        assertEpsilonEquals(cy, getS().getCenterY());
	        assertEpsilonEquals(ux, getS().getFirstAxisX());
	        assertEpsilonEquals(uy, getS().getFirstAxisY());
	        assertEpsilonEquals(e1, getS().getFirstAxisExtent());
	        assertEpsilonEquals(newV.getX(), getS().getSecondAxisX());
	        assertEpsilonEquals(newV.getY(), getS().getSecondAxisY());
	        assertEpsilonEquals(e2, getS().getSecondAxisExtent());
	    }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertThrows(AssertionError.class, () -> getS().setSecondAxis(123.456, 456.789));
        }

    }

    @DisplayName("setSecondAxis(Vector2D)")
	@Nested
	public class SetSecondAxisVector2D {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        Vector2D newV = createVector(123.456, 456.789).toUnitVector();
	        getS().setSecondAxis(newV);
	        assertEpsilonEquals(cx, getS().getCenterX());
	        assertEpsilonEquals(cy, getS().getCenterY());
	        assertEpsilonEquals(ux, getS().getFirstAxisX());
	        assertEpsilonEquals(uy, getS().getFirstAxisY());
	        assertEpsilonEquals(e1, getS().getFirstAxisExtent());
	        assertEpsilonEquals(newV.getX(), getS().getSecondAxisX());
	        assertEpsilonEquals(newV.getY(), getS().getSecondAxisY());
	        assertEpsilonEquals(e2, getS().getSecondAxisExtent());
	    }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
            assertThrows(AssertionError.class, () -> getS().setSecondAxis(createVector(123.456, 456.789)));
        }

    }

    @DisplayName("setSecondAxis(Vector2D,double)")
	@Nested
	public class SetSecondAxisVector2DDouble {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        Vector2D newV = createVector(123.456, 456.789).toUnitVector();
	        getS().setSecondAxis(newV, 159.753);
	        assertEpsilonEquals(cx, getS().getCenterX());
	        assertEpsilonEquals(cy, getS().getCenterY());
	        assertEpsilonEquals(ux, getS().getFirstAxisX());
	        assertEpsilonEquals(uy, getS().getFirstAxisY());
	        assertEpsilonEquals(e1, getS().getFirstAxisExtent());
	        assertEpsilonEquals(newV.getX(), getS().getSecondAxisX());
	        assertEpsilonEquals(newV.getY(), getS().getSecondAxisY());
	        assertEpsilonEquals(159.753, getS().getSecondAxisExtent());
	    }

    }

    @DisplayName("setSecondAxis(double,double,double)")
	@Nested
	public class SetSecondAxisDoubleDoubleDouble {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        Vector2D newV = createVector(123.456, 456.789).toUnitVector();
	        getS().setSecondAxis(newV.getX(), newV.getY(), 159.753);
	        assertEpsilonEquals(cx, getS().getCenterX());
	        assertEpsilonEquals(cy, getS().getCenterY());
	        assertEpsilonEquals(ux, getS().getFirstAxisX());
	        assertEpsilonEquals(uy, getS().getFirstAxisY());
	        assertEpsilonEquals(e1, getS().getFirstAxisExtent());
	        assertEpsilonEquals(newV.getX(), getS().getSecondAxisX());
	        assertEpsilonEquals(newV.getY(), getS().getSecondAxisY());
	        assertEpsilonEquals(159.753, getS().getSecondAxisExtent());
	    }

    }

    @DisplayName("set(double,double,double,double,double,double,double,double)")
	@Nested
	public class SetDoubleDoubleDoubleDoubleDoubleDoubleDoubleDouble {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        Vector2D newU = createVector(-456.789, 159.753).toUnitVector();
	        Vector2D newV = createVector(123.456, 456.789).toUnitVector();
	        getS().set(-6, -4, newU.getX(), newU.getY(), 147.369, newV.getX(), newV.getY(), 159.753);
	        assertEpsilonEquals(-6, getS().getCenterX());
	        assertEpsilonEquals(-4, getS().getCenterY());
	        assertEpsilonEquals(newU.getX(), getS().getFirstAxisX());
	        assertEpsilonEquals(newU.getY(), getS().getFirstAxisY());
	        assertEpsilonEquals(147.369, getS().getFirstAxisExtent());
	        assertEpsilonEquals(newV.getX(), getS().getSecondAxisX());
	        assertEpsilonEquals(newV.getY(), getS().getSecondAxisY());
	        assertEpsilonEquals(159.753, getS().getSecondAxisExtent());
	    }

    }

    @DisplayName("set(Point2D,Vector2D,double,Vector2D,double)")
	@Nested
	public class SetPoint2DVector2DDoubleVector2DDouble {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        Vector2D newU = createVector(-456.789, 159.753).toUnitVector();
	        Vector2D newV = createVector(123.456, 456.789).toUnitVector();
	        getS().set(createPoint(-6, -4), newU, 147.369, newV, 159.753);
	        assertEpsilonEquals(-6, getS().getCenterX());
	        assertEpsilonEquals(-4, getS().getCenterY());
	        assertEpsilonEquals(newU.getX(), getS().getFirstAxisX());
	        assertEpsilonEquals(newU.getY(), getS().getFirstAxisY());
	        assertEpsilonEquals(147.369, getS().getFirstAxisExtent());
	        assertEpsilonEquals(newV.getX(), getS().getSecondAxisX());
	        assertEpsilonEquals(newV.getY(), getS().getSecondAxisY());
	        assertEpsilonEquals(159.753, getS().getSecondAxisExtent());
	    }

    }

    @DisplayName("setFromPointCloud")
	@Nested
	public class SetFromPointCloud {

        private final double obrux = 0.8944271909999159;
        private final double obruy = -0.4472135954999579;
        private final double obrvx = 0.4472135954999579;
        private final double obrvy = 0.8944271909999159;

        @DisplayName("(Iterable)")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        getS().setFromPointCloud((List) Arrays.asList(
	                createPoint(11.7082, -0.94427), createPoint(16.18034, 8),
	                createPoint(-1.7082, 16.94427), createPoint(-6.18034, 8)));
	        assertEpsilonEquals(5, getS().getCenterX());
	        assertEpsilonEquals(8, getS().getCenterY());
	        assertEpsilonEquals(obrux, getS().getFirstAxisX());
	        assertEpsilonEquals(obruy, getS().getFirstAxisY());
	        assertEpsilonEquals(10, getS().getFirstAxisExtent());
	        assertEpsilonEquals(obrvx, getS().getSecondAxisX());
	        assertEpsilonEquals(obrvy, getS().getSecondAxisY());
	        assertEpsilonEquals(5, getS().getSecondAxisExtent());
	    }

        @DisplayName("(Point2D[])")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            getS().setFromPointCloud(
                    createPoint(11.7082, -0.94427), createPoint(16.18034, 8),
                    createPoint(-1.7082, 16.94427), createPoint(-6.18034, 8));
            assertEpsilonEquals(5, getS().getCenterX());
            assertEpsilonEquals(8, getS().getCenterY());
            assertEpsilonEquals(obrux, getS().getFirstAxisX());
            assertEpsilonEquals(obruy, getS().getFirstAxisY());
            assertEpsilonEquals(10, getS().getFirstAxisExtent());
            assertEpsilonEquals(obrvx, getS().getSecondAxisX());
            assertEpsilonEquals(obrvy, getS().getSecondAxisY());
            assertEpsilonEquals(5, getS().getSecondAxisExtent());
        }

    }

    @DisplayName("intersects")
	@Nested
	public class Intersects {

        @DisplayName("(Circle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertFalse(getS().intersects(createCircle(.5, .5, .5)));
        }

        @DisplayName("(Circle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertFalse(getS().intersects(createCircle(.5, 1.5, .5)));
        }

        @DisplayName("(Circle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertFalse(getS().intersects(createCircle(.5, 2.5, .5)));
        }

        @DisplayName("(Circle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
        }

        @DisplayName("(Circle2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertTrue(getS().intersects(createCircle(.5, 3.5, .5)));
        }

        @DisplayName("(Circle2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertTrue(getS().intersects(createCircle(4.5, 3.5, .5)));
        }

        @DisplayName("(Circle2afp) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertFalse(getS().intersects(createCircle(10, -7, .5)));
        }

        @DisplayName("(Circle2afp) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertFalse(getS().intersects(createCircle(10.1, -7, .5)));
        }

        @DisplayName("(Circle2afp) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertTrue(getS().intersects(createCircle(10.2, -7, .5)));
        }

        @DisplayName("(Circle2afp) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertTrue(getS().intersects(createCircle(10, -1, 5)));
        }

        @DisplayName("(Ellipse2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertFalse(getS().intersects(createEllipse(0, 0, 2, 1)));
        }

        @DisplayName("(Ellipse2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertFalse(getS().intersects(createEllipse(0, 1, 2, 1)));
        }

        @DisplayName("(Ellipse2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertTrue(getS().intersects(createEllipse(0, 2, 2, 1)));
        }

        @DisplayName("(Ellipse2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertTrue(getS().intersects(createEllipse(0, 3, 2, 1)));
        }

        @DisplayName("(Ellipse2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertTrue(getS().intersects(createEllipse(0, 4, 2, 1)));
        }

        @DisplayName("(Ellipse2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertTrue(getS().intersects(createEllipse(1, 3, 2, 1)));
        }

        @DisplayName("(Ellipse2afp) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertTrue(getS().intersects(createEllipse(5, 5, 2, 1)));
        }

        @DisplayName("(Ellipse2afp) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertFalse(getS().intersects(createEllipse(0.1, 1, 2, 1)));
        }

        @DisplayName("(Ellipse2afp) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertFalse(getS().intersects(createEllipse(0.2, 1, 2, 1)));
        }

        @DisplayName("(Ellipse2afp) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertTrue(getS().intersects(createEllipse(0.3, 1, 2, 1)));
        }

        @DisplayName("(Ellipse2afp) #11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertTrue(getS().intersects(createEllipse(0.4, 1, 2, 1)));
        }

        @DisplayName("(Ellipse2afp) #12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertFalse(getS().intersects(createEllipse(-7, 7.5, 2, 1)));
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
            assertTrue(getS().intersects(createSegment(5, 5, 4, 6)));
        }

        @DisplayName("(Segment2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertTrue(getS().intersects(createSegment(2, -2, 5, 0)));
        }

        @DisplayName("(Segment2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertFalse(getS().intersects(createSegment(-20, -5, -10, 6)));
        }

        @DisplayName("(Segment2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertFalse(getS().intersects(createSegment(-5, 0, -10, 16)));
        }

        @DisplayName("(Segment2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertTrue(getS().intersects(createSegment(-10, 1, 10, 20)));
        }

        @DisplayName("(Path2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            Path2afp<?, ?, ?, ?, ?, B> path = createPath();
            path.moveTo(-15,  2);
            path.lineTo(6, -9);
            path.lineTo(19, -9);
            path.lineTo(20, 26);
            path.lineTo(-6, 30);
            assertFalse(getS().intersects(path));
        }

        @DisplayName("(Path2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            Path2afp<?, ?, ?, ?, ?, B> path = createPath();
            path.moveTo(-15,  2);
            path.lineTo(6, -9);
            path.lineTo(19, -9);
            path.lineTo(20, 26);
            path.lineTo(-6, 30);
            path.closePath();
            assertTrue(getS().intersects(path));
        }

        @DisplayName("(PathIterator2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void pathiterator_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            Path2afp<?, ?, ?, ?, ?, B> path = createPath();
            path.moveTo(-15,  2);
            path.lineTo(6, -9);
            path.lineTo(19, -9);
            path.lineTo(20, 26);
            path.lineTo(-6, 30);
            assertFalse(getS().intersects(path.getPathIterator()));
        }

        @DisplayName("(PathIterator2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void pathiterator_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            Path2afp<?, ?, ?, ?, ?, B> path = createPath();
            path.moveTo(-15,  2);
            path.lineTo(6, -9);
            path.lineTo(19, -9);
            path.lineTo(20, 26);
            path.lineTo(-6, 30);
            path.closePath();
            assertTrue(getS().intersects(path.getPathIterator()));
        }

        @DisplayName("(Triangle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertTrue(getS().intersects(createTriangle(-5, 15, -3, 16, -8, 19)));
        }

        @DisplayName("(Triangle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertTrue(getS().intersects(createTriangle(-5, 15, -8, 19, -3, 16)));
        }

        @DisplayName("(Triangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertFalse(getS().intersects(createTriangle(0, -5, 2, -4, -3, -1)));
        }

        @DisplayName("(Triangle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertFalse(getS().intersects(createTriangle(0, -5, -3, -1, 2, -4)));
        }

        @DisplayName("(Triangle2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertFalse(getS().intersects(createTriangle(20, 0, 22, 1, 17, 4)));
        }

        @DisplayName("(Triangle2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertFalse(getS().intersects(createTriangle(20, 0, 17, 4, 22, 1)));
        }

        @DisplayName("(Triangle2afp) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertFalse(getS().intersects(createTriangle(17.18034, 9, 19.18034, 10, 14.18034, 13)));
        }

        @DisplayName("(Triangle2afp) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertFalse(getS().intersects(createTriangle(17.18034, 9, 14.18034, 13, 19.18034, 10)));
        }

        @DisplayName("(Triangle2afp) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertTrue(getS().intersects(createTriangle(0, 10, 2, 11, -3, 14)));
        }

        @DisplayName("(Triangle2afp) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertTrue(getS().intersects(createTriangle(0, 10, -3, 14, 2, 11)));
        }

        @DisplayName("(Triangle2afp) #11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertTrue(getS().intersects(createTriangle(0, 20, 2, 21, -3, 24)));
        }

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
            assertTrue(getS().intersects(createRectangle(0, 2, 1, 1)));
        }

        @DisplayName("(Rectangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertTrue(getS().intersects(createRectangle(-5.5, 8.5, 1, 1)));
        }

        @DisplayName("(Rectangle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
        }

        @DisplayName("(Rectangle2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertFalse(getS().intersects(createRectangle(-6, 16, 1, 1)));
        }

        @DisplayName("(Rectangle2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertFalse(getS().intersects(createRectangle(146, 16, 1, 1)));
        }

        @DisplayName("(Rectangle2afp) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertTrue(getS().intersects(createRectangle(12, 14, 1, 1)));
        }

        @DisplayName("(Rectangle2afp) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertTrue(getS().intersects(createRectangle(0, 8, 1, 1)));
        }

        @DisplayName("(Rectangle2afp) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertTrue(getS().intersects(createRectangle(10, -1, 1, 1)));
        }

        @DisplayName("(Rectangle2afp) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertTrue(getS().intersects(createRectangle(-15, -10, 35, 40)));
        }

        @DisplayName("(Parallelogram2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            double ux2 = -0.9284766908852592;
            double uy2 = 0.3713906763541037;
            double et1 = 5;
            double vx2 = 0.3713906763541037;
            double vy2 = 0.9284766908852592;
            double et2 = 3;
            assertFalse(getS().intersects(createParallelogram(-10, 0,
                    ux2, uy2, et1, vx2, vy2, et2)));
        }

        @DisplayName("(Parallelogram2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            double ux2 = -0.9284766908852592;
            double uy2 = 0.3713906763541037;
            double et1 = 5;
            double vx2 = 0.3713906763541037;
            double vy2 = 0.9284766908852592;
            double et2 = 3;
            assertFalse(getS().intersects(createParallelogram(-15, 25,
                    ux2, uy2, et1, vx2, vy2, et2)));
        }

        @DisplayName("(Parallelogram2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            double ux2 = -0.9284766908852592;
            double uy2 = 0.3713906763541037;
            double et1 = 5;
            double vx2 = 0.3713906763541037;
            double vy2 = 0.9284766908852592;
            double et2 = 3;
            assertFalse(getS().intersects(createParallelogram(2, -6,
                    ux2, uy2, et1, vx2, vy2, et2)));
        }

        @DisplayName("(Parallelogram2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            double ux2 = -0.9284766908852592;
            double uy2 = 0.3713906763541037;
            double et1 = 5;
            double vx2 = 0.3713906763541037;
            double vy2 = 0.9284766908852592;
            double et2 = 3;
            assertFalse(getS().intersects(createParallelogram(2, -5,
                    ux2, uy2, et1, vx2, vy2, et2)));
        }

        @DisplayName("(Parallelogram2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            double ux2 = -0.9284766908852592;
            double uy2 = 0.3713906763541037;
            double et1 = 5;
            double vx2 = 0.3713906763541037;
            double vy2 = 0.9284766908852592;
            double et2 = 3;
            assertTrue(getS().intersects(createParallelogram(2, -4,
                    ux2, uy2, et1, vx2, vy2, et2)));
        }

        @DisplayName("(Parallelogram2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            double ux2 = -0.9284766908852592;
            double uy2 = 0.3713906763541037;
            double et1 = 5;
            double vx2 = 0.3713906763541037;
            double vy2 = 0.9284766908852592;
            double et2 = 3;
            assertTrue(getS().intersects(createParallelogram(pEx, pEy,
                    ux2, uy2, et1, vx2, vy2, et2)));
        }

        @DisplayName("(Parallelogram2afp) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            double ux2 = -0.9284766908852592;
            double uy2 = 0.3713906763541037;
            double et1 = 5;
            double vx2 = 0.3713906763541037;
            double vy2 = 0.9284766908852592;
            double et2 = 3;
            assertTrue(getS().intersects(createParallelogram(6, 6,
                    ux2, uy2, et1, vx2, vy2, et2)));
        }

        @DisplayName("(Parallelogram2afp) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            double ux2 = -0.9284766908852592;
            double uy2 = 0.3713906763541037;
            double et1 = 5;
            double vx2 = 0.3713906763541037;
            double vy2 = 0.9284766908852592;
            double et2 = 3;
            assertTrue(getS().intersects(createParallelogram(6, 6,
                    ux2, uy2, 10 * et1, vx2, vy2, 10 * et2)));
        }

        @DisplayName("(RoundRectangle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertFalse(getS().intersects(createRoundRectangle(0, 0, 1, 1, .1, .05)));
        }

        @DisplayName("(RoundRectangle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertTrue(getS().intersects(createRoundRectangle(0, 2, 1, 1, .1, .05)));
        }

        @DisplayName("(RoundRectangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertTrue(getS().intersects(createRoundRectangle(-5.5, 8.5, 1, 1, .1, .05)));
        }

        @DisplayName("(RoundRectangle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
        }

        @DisplayName("(RoundRectangle2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertFalse(getS().intersects(createRoundRectangle(-6, 16, 1, 1, .1, .05)));
        }

        @DisplayName("(RoundRectangle2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertFalse(getS().intersects(createRoundRectangle(146, 16, 1, 1, .1, .05)));
        }

        @DisplayName("(RoundRectangle2afp) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertTrue(getS().intersects(createRoundRectangle(12, 14, 1, 1, .1, .05)));
        }

        @DisplayName("(RoundRectangle2afp) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertTrue(getS().intersects(createRoundRectangle(0, 8, 1, 1, .1, .05)));
        }

        @DisplayName("(RoundRectangle2afp) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertTrue(getS().intersects(createRoundRectangle(10, -1, 1, 1, .1, .05)));
        }

        @DisplayName("(RoundRectangle2afp) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertTrue(getS().intersects(createRoundRectangle(-15, -10, 35, 40, .1, .05)));
        }

        @DisplayName("(RoundRectangle2afp) #11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertFalse(getS().intersects(createRoundRectangle(-4.79634, 14.50886, 1, 1, .1, .05)));
        }

        @DisplayName("(OrientedRectangle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectagnle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            OrientedRectangle2afp rectangle = createOrientedRectangle(
                    6, 9,
                    0.894427190999916, -0.447213595499958, 13.999990000000002,
                    12.999989999999997);
            double ux2 = 0.55914166827779;
            double uy2 = 0.829072128825671;
            double et1 = 10;
            double vx2 = -0.989660599000356;
            double vy2 = -0.143429072318889;
            double et2 = 15;
            assertFalse(createParallelogram(
                    -20, -20, ux2, uy2, et1, vx2, vy2, et2).intersects(rectangle));
        }

        @DisplayName("(OrientedRectangle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectagnle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            OrientedRectangle2afp rectangle = createOrientedRectangle(
                    6, 9,
                    0.894427190999916, -0.447213595499958, 13.999990000000002,
                    12.999989999999997);
            double ux2 = 0.55914166827779;
            double uy2 = 0.829072128825671;
            double et1 = 10;
            double vx2 = -0.989660599000356;
            double vy2 = -0.143429072318889;
            double et2 = 15;
            assertFalse(createParallelogram(
                    -40, 20, ux2, uy2, et1, vx2, vy2, et2).intersects(rectangle));
        }

        @DisplayName("(OrientedRectangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectagnle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            OrientedRectangle2afp rectangle = createOrientedRectangle(
                    6, 9,
                    0.894427190999916, -0.447213595499958, 13.999990000000002,
                    12.999989999999997);
            double ux2 = 0.55914166827779;
            double uy2 = 0.829072128825671;
            double et1 = 10;
            double vx2 = -0.989660599000356;
            double vy2 = -0.143429072318889;
            double et2 = 15;
            assertTrue(createParallelogram(
                    -20, -10, ux2, uy2, et1, vx2, vy2, et2).intersects(rectangle));
        }

        @DisplayName("(OrientedRectangle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectagnle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            OrientedRectangle2afp rectangle = createOrientedRectangle(
                    6, 9,
                    0.894427190999916, -0.447213595499958, 13.999990000000002,
                    12.999989999999997);
            double ux2 = 0.55914166827779;
            double uy2 = 0.829072128825671;
            double et1 = 10;
            double vx2 = -0.989660599000356;
            double vy2 = -0.143429072318889;
            double et2 = 15;
            assertTrue(createParallelogram(
                    10, -10, ux2, uy2, et1, vx2, vy2, et2).intersects(rectangle));
        }

        @DisplayName("(OrientedRectangle2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectagnle_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            OrientedRectangle2afp rectangle = createOrientedRectangle(
                    6, 9,
                    0.894427190999916, -0.447213595499958, 13.999990000000002,
                    12.999989999999997);
            double ux2 = 0.55914166827779;
            double uy2 = 0.829072128825671;
            double et1 = 10;
            double vx2 = -0.989660599000356;
            double vy2 = -0.143429072318889;
            double et2 = 15;
            assertTrue(createParallelogram(
                    5, 5, ux2, uy2, et1, vx2, vy2, et2).intersects(rectangle));
        }

        @DisplayName("(Shape2D) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void shape_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertTrue(getS().intersects((Shape2D) createCircle(.5, 3.5, .5)));
        }

        @DisplayName("(Shape2D) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void shape_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
            assertTrue(getS().intersects((Shape2D) createRectangle(12, 14, 1, 1)));
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
	        getS().operator_add(createVector(123.456, 789.123));
	        assertEpsilonEquals(cx + 123.456, getS().getCenterX());
	        assertEpsilonEquals(cy + 789.123, getS().getCenterY());
	        assertEpsilonEquals(ux, getS().getFirstAxisX());
	        assertEpsilonEquals(uy, getS().getFirstAxisY());
	        assertEpsilonEquals(e1, getS().getFirstAxisExtent());
	        assertEpsilonEquals(vx, getS().getSecondAxisX());
	        assertEpsilonEquals(vy, getS().getSecondAxisY());
	        assertEpsilonEquals(e2, getS().getSecondAxisExtent());
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
	        T shape = getS().operator_plus(createVector(123.456, 789.123));
	        assertEpsilonEquals(cx + 123.456, shape.getCenterX());
	        assertEpsilonEquals(cy + 789.123, shape.getCenterY());
	        assertEpsilonEquals(ux, shape.getFirstAxisX());
	        assertEpsilonEquals(uy, shape.getFirstAxisY());
	        assertEpsilonEquals(e1, shape.getFirstAxisExtent());
	        assertEpsilonEquals(vx, shape.getSecondAxisX());
	        assertEpsilonEquals(vy, shape.getSecondAxisY());
	        assertEpsilonEquals(e2, shape.getSecondAxisExtent());
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
	        getS().operator_remove(createVector(123.456, 789.123));
	        assertEpsilonEquals(cx - 123.456, getS().getCenterX());
	        assertEpsilonEquals(cy - 789.123, getS().getCenterY());
	        assertEpsilonEquals(ux, getS().getFirstAxisX());
	        assertEpsilonEquals(uy, getS().getFirstAxisY());
	        assertEpsilonEquals(e1, getS().getFirstAxisExtent());
	        assertEpsilonEquals(vx, getS().getSecondAxisX());
	        assertEpsilonEquals(vy, getS().getSecondAxisY());
	        assertEpsilonEquals(e2, getS().getSecondAxisExtent());
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
	        T shape = getS().operator_minus(createVector(123.456, 789.123));
	        assertEpsilonEquals(cx - 123.456, shape.getCenterX());
	        assertEpsilonEquals(cy - 789.123, shape.getCenterY());
	        assertEpsilonEquals(ux, shape.getFirstAxisX());
	        assertEpsilonEquals(uy, shape.getFirstAxisY());
	        assertEpsilonEquals(e1, shape.getFirstAxisExtent());
	        assertEpsilonEquals(vx, shape.getSecondAxisX());
	        assertEpsilonEquals(vy, shape.getSecondAxisY());
	        assertEpsilonEquals(e2, shape.getSecondAxisExtent());
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
	        PathIterator2afp pi = getS().operator_multiply(null).getPathIterator();
	        assertElement(pi, PathElementType.MOVE_TO, pGx, pGy);
	        assertElement(pi, PathElementType.LINE_TO, pHx, pHy);
	        assertElement(pi, PathElementType.LINE_TO, pEx, pEy);
	        assertElement(pi, PathElementType.LINE_TO, pFx, pFy);
	        assertElement(pi, PathElementType.CLOSE, pGx, pGy);
	        assertNoElement(pi);
        }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        var transform = new Transform2D();
	        var pi = getS().operator_multiply(transform).getPathIterator();
	        assertElement(pi, PathElementType.MOVE_TO, pGx, pGy);
	        assertElement(pi, PathElementType.LINE_TO, pHx, pHy);
	        assertElement(pi, PathElementType.LINE_TO, pEx, pEy);
	        assertElement(pi, PathElementType.LINE_TO, pFx, pFy);
	        assertElement(pi, PathElementType.CLOSE, pGx, pGy);
	        assertNoElement(pi);
        }

        @DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        var transform = new Transform2D();
	        transform.setTranslation(18,  -45);
	        var pi = getS().operator_multiply(transform).getPathIterator();
	        assertElement(pi, PathElementType.MOVE_TO, pGx + 18, pGy - 45);
	        assertElement(pi, PathElementType.LINE_TO, pHx + 18, pHy - 45);
	        assertElement(pi, PathElementType.LINE_TO, pEx + 18, pEy - 45);
	        assertElement(pi, PathElementType.LINE_TO, pFx + 18, pFy - 45);
	        assertElement(pi, PathElementType.CLOSE, pGx + 18, pGy - 45);
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
	        assertFalse(getS().operator_and(createPoint(-20, 0)));
        }

        @DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertTrue(getS().operator_and(createPoint(12, -4)));
        }

        @DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertTrue(getS().operator_and(createPoint(14, 0)));
        }

        @DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertFalse(getS().operator_and(createPoint(15, 0)));
        }

        @DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertFalse(getS().operator_and(createPoint(20, 8)));
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
	        assertTrue(getS().operator_and(createPoint(8, 16)));
        }

        @DisplayName("#9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertFalse(getS().operator_and(createPoint(-4, 20)));
        }

        @DisplayName("#10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
        }

        @DisplayName("#11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertFalse(getS().operator_and(createPoint(-5, 12)));
        }

        @DisplayName("#12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
        }

        @DisplayName("#13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertTrue(getS().operator_and(createPoint(0, 6)));
        }

        @DisplayName("#14")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_14(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertTrue(getS().operator_and(createPoint(0, 7)));
        }

        @DisplayName("#15")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_15(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertTrue(getS().operator_and(createPoint(0, 8)));
        }

        @DisplayName("#16")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_16(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertTrue(getS().operator_and(createPoint(0, 9)));
        }

        @DisplayName("#17")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_17(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertTrue(getS().operator_and(createPoint(0, 10)));
        }

        @DisplayName("#18")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_18(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertFalse(getS().operator_and(createPoint(0, 27)));
        }

        @DisplayName("#19")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_19(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertTrue(getS().operator_and(createPoint(cx, cy)));
        }

        @DisplayName("#20")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_20(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertTrue(getS().operator_and(createPoint( 16, 8)));
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
	        assertTrue(getS().operator_and(createCircle(.5, 3.5, .5)));
	    }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertTrue(getS().operator_and(createRectangle(12, 14, 1, 1)));
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
	        assertEpsilonEquals(14.81966, getS().operator_upTo(createPoint(-20, 9)));
        }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertEpsilonEquals(2.7009, getS().operator_upTo(createPoint(0, 0)));
        }

        @DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertEpsilonEquals(6.23644, getS().operator_upTo(createPoint(5, -10)));
        }

        @DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertEpsilonEquals(11.1863, getS().operator_upTo(createPoint(14, -20)));
        }

        @DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertEpsilonEquals(2.25040, getS().operator_upTo(createPoint(-6, 15)));
        }

        @DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertEpsilonEquals(0, getS().operator_upTo(createPoint(0, 10)));
        }

        @DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertEpsilonEquals(0, getS().operator_upTo(createPoint(10, 0)));
        }

        @DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertEpsilonEquals(1.03772, getS().operator_upTo(createPoint(15, -4)));
        }

        @DisplayName("#9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertEpsilonEquals(3.70561, getS().operator_upTo(createPoint(-5, 25)));
        }

        @DisplayName("#10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertEpsilonEquals(0, getS().operator_upTo(createPoint(0, 20)));
        }

        @DisplayName("#11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertEpsilonEquals(0, getS().operator_upTo(createPoint(10, 10)));
        }

        @DisplayName("#12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertEpsilonEquals(4.91829, getS().operator_upTo(createPoint(20, 0)));
        }

        @DisplayName("#13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertEpsilonEquals(8.42901, getS().operator_upTo(createPoint(-3, 35)));
        }

        @DisplayName("#14")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_14(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
        }

        @DisplayName("#15")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_15(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertEpsilonEquals(9.91864, getS().operator_upTo(createPoint(5, 35)));
        }

        @DisplayName("#16")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_16(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertEpsilonEquals(6.23644, getS().operator_upTo(createPoint(20, 15)));
        }

        @DisplayName("#17")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_17(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertEpsilonEquals(17.8477, getS().operator_upTo(createPoint(35, 10)));
        }

        @DisplayName("#18")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_18(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertEpsilonEquals(7.59135, getS().operator_upTo(createPoint(-8, 29)));
	    }

    }

    @DisplayName("isCCW")
	@Nested
	public class IsCCW {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertTrue(getS().isCCW());
        }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
        	assertTrue(createParallelogram(cx, cy, ux, uy, e1, vx, vy, e2).isCCW());
        }

        @DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertTrue(createParallelogram(
	                4.7, 15,
	                0.12403, 0.99228, 18.02776,
	                -0.44721, 0.89443, 20).isCCW());
        }

        @DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertTrue(createParallelogram(
	                -10, -3,
	                -.8944271909999159, .4472135954999579, 2,
	                .5547001962252290, -.8320502943378436, 1).isCCW());
        }

        @DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertFalse(createParallelogram(
	                -10, 7,
	                -0.9863939238321437, 0.1643989873053573, 1,
	                0.9998000599800071, 0.01999600119960014, 2).isCCW());
        }

        @DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);	
	        assertFalse(createParallelogram(
	                0, -6,
	                -0.9863939238321437, 0.1643989873053573, 1,
	                0.9998000599800071, 0.01999600119960014, 2).isCCW());
        }

    }

}