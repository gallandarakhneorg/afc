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

import static org.arakhne.afc.math.geometry.base.GeomConstants.SHAPE_INTERSECTS;
import static org.arakhne.afc.math.geometry.base.GeomConstants.SPLINE_APPROXIMATION_RATIO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.Iterator;

import org.arakhne.afc.math.geometry.base.CrossingComputationType;
import org.arakhne.afc.math.geometry.base.PathElementType;
import org.arakhne.afc.math.geometry.base.PathWindingRule;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.base.d2.Path2D.ArcType;
import org.arakhne.afc.math.geometry.base.d2.Point2D;
import org.arakhne.afc.math.geometry.base.d2.Shape2D;
import org.arakhne.afc.math.geometry.base.d2.Transform2D;
import org.arakhne.afc.math.geometry.base.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.afp.BasicPathShadow2afp;
import org.arakhne.afc.math.geometry.d2.afp.Circle2afp;
import org.arakhne.afc.math.geometry.d2.afp.MultiShape2afp;
import org.arakhne.afc.math.geometry.d2.afp.OrientedRectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Parallelogram2afp;
import org.arakhne.afc.math.geometry.d2.afp.Path2afp;
import org.arakhne.afc.math.geometry.d2.afp.PathElement2afp;
import org.arakhne.afc.math.geometry.d2.afp.PathIterator2afp;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Segment2afp;
import org.arakhne.afc.math.geometry.d2.afp.Triangle2afp;
import org.arakhne.afc.math.geometry.d2.ai.PathIterator2ai;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("all")
public abstract class AbstractPath2afpTestCase<T extends Path2afp<?, T, ?, ?, ?, B>, B extends Rectangle2afp<?, ?, ?, ?, ?, B>>
extends AbstractShape2afpTestCase<T, B> {

	@Override
	protected T createShape() {
		T path = (T) createPath();
		path.moveTo(0, 0);
		path.lineTo(1, 1);
		path.quadTo(3, 0, 4, 3);
		path.curveTo(5, -1, 6, 5, 7, -5);
		return path;
	}

	protected BasicPathShadow2afp createShadow(int x1, int y1, int x2, int y2) {
		T path = (T) createPath();
		path.moveTo(x1, y1);
		path.lineTo(x2, y2);
		return new BasicPathShadow2afp(path);
	}

	protected Triangle2afp createTestTriangle(double dx, double dy) {
		return createTriangle(dx, dy, dx + 3, dy + 1, dx + 1, dy + 1.5);
	}

	protected MultiShape2afp createTestMultiShape(double x, double y) {
		MultiShape2afp multishape = createMultiShape();
		multishape.add(createCircle(x - 5, y + 4, 1));
		multishape.add(createSegment(x + 4, y + 2, x + 8, y - 1));
		return multishape;
	}

	protected OrientedRectangle2afp createTestOrientedRectangle(double x, double y) {
		Vector2D u = createVector(.5, .75).toUnitVector();
		return createOrientedRectangle(x, y, u.getX(), u.getY(), 2, 1);
	}

	protected Parallelogram2afp createTestParallelogram(double x, double y) {
		Vector2D u = createVector(.5, .75).toUnitVector();
		Vector2D v = createVector(-2, .5).toUnitVector();
		return createParallelogram(x, y, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1);
	}

	@DisplayName("findsVectorProjectionRAxisPoint")
	@Nested
	public class FindsVectorProjectionRAxisPoint {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorCircleShadow(0, getS().getPathIterator(), -2, -2, 2, null));
        }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorCircleShadow(0, getS().getPathIterator(), 2, -2, 2, null));
        }

        @DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorCircleShadow(0, getS().getPathIterator(), 2.5, -1.5, 2, null));
        }

        @DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Path2afp.calculatesCrossingsPathIteratorCircleShadow(0, getS().getPathIterator(), 10, 0, 2, null));
        }

        @DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorCircleShadow(0, getS().getPathIterator(), 4, 0, 0.5, null));
        }

        @DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorCircleShadow(0, getS().getPathIterator(), 2.5, 1, 0.5, null));
		}

    }

    @DisplayName("calculatesCrossingsPathIteratorCircleShadow")
	@Nested
	public class CalculatesCrossingsPathIteratorCircleShadow {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorCircleShadow(0, getS().getPathIterator(), -2, -2, 2, CrossingComputationType.STANDARD));
        }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorCircleShadow(0, getS().getPathIterator(), 2, -2, 2, CrossingComputationType.STANDARD));
        }

        @DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorCircleShadow(0, getS().getPathIterator(), 2.5, -1.5, 2, CrossingComputationType.STANDARD));
        }

        @DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Path2afp.calculatesCrossingsPathIteratorCircleShadow(0, getS().getPathIterator(), 10, 0, 2, CrossingComputationType.STANDARD));
        }

        @DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorCircleShadow(0, getS().getPathIterator(), 4, 0, 0.5, CrossingComputationType.STANDARD));
        }

        @DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorCircleShadow(0, getS().getPathIterator(), 2.5, 1, 0.5, CrossingComputationType.STANDARD));
		}

        @DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Path2afp.calculatesCrossingsPathIteratorCircleShadow(0, getS().getPathIterator(), -2, -2, 2, CrossingComputationType.AUTO_CLOSE));
        }

        @DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorCircleShadow(0, getS().getPathIterator(), 2, -2, 2, CrossingComputationType.AUTO_CLOSE));
        }

        @DisplayName("#9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorCircleShadow(0, getS().getPathIterator(), 2.5, -1.5, 2, CrossingComputationType.AUTO_CLOSE));
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
			assertEquals(0, Path2afp.calculatesCrossingsPathIteratorCircleShadow(0, getS().getPathIterator(), 10, 0, 2, CrossingComputationType.AUTO_CLOSE));
        }

        @DisplayName("#12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorCircleShadow(0, getS().getPathIterator(), 4, 0, 0.5, CrossingComputationType.AUTO_CLOSE));
        }

        @DisplayName("#13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorCircleShadow(0, getS().getPathIterator(), 2.5, 1, 0.5, CrossingComputationType.AUTO_CLOSE));
        }

        @DisplayName("#14")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_15(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorCircleShadow(0, getS().getPathIterator(), -2, -2, 2, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
        }

        @DisplayName("#16")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_16(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorCircleShadow(0, getS().getPathIterator(), 2, -2, 2, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
        }

        @DisplayName("#17")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_17(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorCircleShadow(0, getS().getPathIterator(), 2.5, -1.5, 2, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
        }

        @DisplayName("#18")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_18(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorCircleShadow(0, getS().getPathIterator(), 10, 0, 2, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
        }

        @DisplayName("#19")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_19(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorCircleShadow(0, getS().getPathIterator(), 4, 0, 0.5, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
        }

        @DisplayName("#20")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_20(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorCircleShadow(0, getS().getPathIterator(), 2.5, 1, 0.5, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
        }

        @DisplayName("#21")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_21(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		Path2afp path = createPath();
    		path.moveTo(-5.180339887498949, 9);
    		path.lineTo(12.70820393249937, -8.888543819998318);
    		assertEquals(-2,
    				Path2afp.calculatesCrossingsPathIteratorCircleShadow(
    						0,
    						(PathIterator2afp) path.getPathIterator(),
    						0, 2, 1,
    						CrossingComputationType.STANDARD));
    	}

    }

    @DisplayName("calculatesCrossingsPathIteratorEllipseShadow")
	@Nested
	public class CalculatesCrossingsPathIteratorEllipseShadow {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorEllipseShadow(0, getS().getPathIterator(), 1, -1.5, 2, 1, null));
        }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Path2afp.calculatesCrossingsPathIteratorEllipseShadow(0, getS().getPathIterator(), 1, 1, 2, 1, null));
        }

        @DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorEllipseShadow(0, getS().getPathIterator(), 4.5, -1, 2, 1, null));
        }

        @DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-1, Path2afp.calculatesCrossingsPathIteratorEllipseShadow(0, getS().getPathIterator(), 0, -5.5, 2, 1, null));
        }

        @DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorEllipseShadow(0, getS().getPathIterator(), 1, -1.5, 2, 1, CrossingComputationType.STANDARD));
        }

        @DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Path2afp.calculatesCrossingsPathIteratorEllipseShadow(0, getS().getPathIterator(), 1, 1, 2, 1, CrossingComputationType.STANDARD));
        }

        @DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorEllipseShadow(0, getS().getPathIterator(), 4.5, -1, 2, 1, CrossingComputationType.STANDARD));
        }

        @DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-1, Path2afp.calculatesCrossingsPathIteratorEllipseShadow(0, getS().getPathIterator(), 0, -5.5, 2, 1, CrossingComputationType.STANDARD));
        }

        @DisplayName("#9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorEllipseShadow(0, getS().getPathIterator(), 1, -1.5, 2, 1, CrossingComputationType.AUTO_CLOSE));
        }

        @DisplayName("#10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Path2afp.calculatesCrossingsPathIteratorEllipseShadow(0, getS().getPathIterator(), 1, 1, 2, 1, CrossingComputationType.AUTO_CLOSE));
        }

        @DisplayName("#11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorEllipseShadow(0, getS().getPathIterator(), 4.5, -1, 2, 1, CrossingComputationType.AUTO_CLOSE));
        }

        @DisplayName("#12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Path2afp.calculatesCrossingsPathIteratorEllipseShadow(0, getS().getPathIterator(), 0, -5.5, 2, 1, CrossingComputationType.AUTO_CLOSE));
        }

        @DisplayName("#13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Path2afp.calculatesCrossingsPathIteratorEllipseShadow(0, getS().getPathIterator(), 1, -1.5, 2, 1, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
        }

        @DisplayName("#14")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_14(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Path2afp.calculatesCrossingsPathIteratorEllipseShadow(0, getS().getPathIterator(), 1, 1, 2, 1, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
        }

        @DisplayName("#15")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_15(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorEllipseShadow(0, getS().getPathIterator(), 4.5, -1, 2, 1, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
        }

        @DisplayName("#16")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_16(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Path2afp.calculatesCrossingsPathIteratorEllipseShadow(0, getS().getPathIterator(), 0, -5.5, 2, 1, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		}

    }

    @DisplayName("calculatesCrossingsPathIteratorPathShadow")
	@Nested
	public class CalculatesCrossingsPathIteratorPathShadow {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorPathShadow(0, getS().getPathIterator(),
					createShadow(1, -1, 4, -3), null));
        }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorPathShadow(0, getS().getPathIterator(),
					createShadow(1, -1, 5, -3), null));
        }

        @DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorPathShadow(0, getS().getPathIterator(),
					createShadow(1, -1, 4, 1), null));
        }

        @DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorPathShadow(0, getS().getPathIterator(),
					createShadow(5, 2, 4, 1), null));
        }

        @DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorPathShadow(0, getS().getPathIterator(),
    				createShadow(1, -1, 4, -3), CrossingComputationType.STANDARD));
        }

        @DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorPathShadow(0, getS().getPathIterator(),
    				createShadow(1, -1, 5, -3), CrossingComputationType.STANDARD));
        }

        @DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorPathShadow(0, getS().getPathIterator(),
    				createShadow(1, -1, 4, 1), CrossingComputationType.STANDARD));
        }

        @DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorPathShadow(0, getS().getPathIterator(),
    				createShadow(5, 2, 4, 1), CrossingComputationType.STANDARD));
        }

        @DisplayName("#9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorPathShadow(0, getS().getPathIterator(),
    				createShadow(1, -1, 4, -3), CrossingComputationType.AUTO_CLOSE));
        }

        @DisplayName("#10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorPathShadow(0, getS().getPathIterator(),
    				createShadow(1, -1, 5, -3), CrossingComputationType.AUTO_CLOSE));
        }

        @DisplayName("#11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorPathShadow(0, getS().getPathIterator(),
    				createShadow(1, -1, 4, 1), CrossingComputationType.AUTO_CLOSE));
        }

        @DisplayName("#12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorPathShadow(0, getS().getPathIterator(),
    				createShadow(5, 2, 4, 1), CrossingComputationType.AUTO_CLOSE));
        }

        @DisplayName("#13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorPathShadow(0, getS().getPathIterator(),
    				createShadow(1, -1, 4, -3), CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
        }

        @DisplayName("#14")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_14(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorPathShadow(0, getS().getPathIterator(),
    				createShadow(1, -1, 5, -3), CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
        }

        @DisplayName("#15")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_15(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorPathShadow(0, getS().getPathIterator(),
    				createShadow(1, -1, 4, 1), CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
        }

        @DisplayName("#16")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_16(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorPathShadow(0, getS().getPathIterator(),
    				createShadow(5, 2, 4, 1), CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
        }

        @DisplayName("#17")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_17(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		Path2afp path = createPath();
    		path.moveTo(-5.180339887498949, 9);
    		path.lineTo(12.70820393249937, -8.888543819998318);
    		Circle2afp circle = createCircle(0, 2, 1);
    		assertEquals(-2,
    				Path2afp.calculatesCrossingsPathIteratorPathShadow(
    						0,
    						(PathIterator2afp) path.getPathIterator(),
    						new BasicPathShadow2afp((PathIterator2afp) circle.getPathIterator(), circle.toBoundingBox()),
    						CrossingComputationType.STANDARD));
        }

    }

    @DisplayName("calculatesCrossingsPathIteratorPointShadow")
	@Nested
	public class CalculatesCrossingsPathIteratorPointShadow {

    	@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(-1, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, getS().getPathIterator(), 1, -0.5, CrossingComputationType.STANDARD));
    	}

    	@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(-1, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, getS().getPathIterator(), 0, -0.5, CrossingComputationType.STANDARD));
    	}

    	@DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, getS().getPathIterator(), 7, 1, CrossingComputationType.STANDARD));
    	}

    	@DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, getS().getPathIterator(), 2, 2, CrossingComputationType.STANDARD));
    	}

    	@DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, getS().getPathIterator(), 5, 2, CrossingComputationType.STANDARD));
    	}

    	@DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, getS().getPathIterator(), 0, 4, CrossingComputationType.STANDARD));
    	}

    	@DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(-1, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, getS().getPathIterator(), 3, 0, CrossingComputationType.STANDARD));
    	}

    	@DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, getS().getPathIterator(), 1, 1, CrossingComputationType.STANDARD));
    	}

    	@DisplayName("#9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(-1, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, getS().getPathIterator(), 1, -0.5, CrossingComputationType.AUTO_CLOSE));
    	}

    	@DisplayName("#10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, getS().getPathIterator(), 0, -0.5, CrossingComputationType.AUTO_CLOSE));
    	}

    	@DisplayName("#11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, getS().getPathIterator(), 7, 1, CrossingComputationType.AUTO_CLOSE));
    	}

    	@DisplayName("#12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, getS().getPathIterator(), 2, 2, CrossingComputationType.AUTO_CLOSE));
    	}

    	@DisplayName("#13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, getS().getPathIterator(), 5, 2, CrossingComputationType.AUTO_CLOSE));
    	}

    	@DisplayName("#14")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_14(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, getS().getPathIterator(), 0, 4, CrossingComputationType.AUTO_CLOSE));
    	}

    	@DisplayName("#15")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_15(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(-1, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, getS().getPathIterator(), 3, 0, CrossingComputationType.AUTO_CLOSE));
    	}

    	@DisplayName("#16")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_16(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, getS().getPathIterator(), 1, 1, CrossingComputationType.AUTO_CLOSE));
    	}

    	@DisplayName("#18")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_18(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(-1, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, getS().getPathIterator(), 1, -0.5, null));
        }

        @DisplayName("#19")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_19(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(-1, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, getS().getPathIterator(), 0, -0.5, null));
        }

        @DisplayName("#20")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_20(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, getS().getPathIterator(), 7, 1, null));
        }

        @DisplayName("#21")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_21(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, getS().getPathIterator(), 2, 2, null));
        }

        @DisplayName("#22")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_22(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, getS().getPathIterator(), 5, 2, null));
        }

        @DisplayName("#23")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_23(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, getS().getPathIterator(), 0, 4, null));
        }

        @DisplayName("#24")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_24(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(-1, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, getS().getPathIterator(), 3, 0, null));
        }

        @DisplayName("#25")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_25(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, getS().getPathIterator(), 1, 1, null));
        }

        @DisplayName("#26")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_26(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, getS().getPathIterator(), 1, -0.5, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
        }

        @DisplayName("#27")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_27(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, getS().getPathIterator(), 0, -0.5, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
        }

        @DisplayName("#28")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_28(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, getS().getPathIterator(), 7, 1, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
        }

        @DisplayName("#29")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_29(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, getS().getPathIterator(), 2, 2, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
        }

        @DisplayName("#30")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_30(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        }

        @DisplayName("#31")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_31(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        }

        @DisplayName("#32")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_32(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, getS().getPathIterator(), 5, 2, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
        }

        @DisplayName("#33")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_33(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, getS().getPathIterator(), 0, 4, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
        }

        @DisplayName("#34")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_34(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, getS().getPathIterator(), 3, 0, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
        }

        @DisplayName("#35")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_35(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, getS().getPathIterator(), 1, 1, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
    	}

    }

    @DisplayName("calculatesCrossingsPathIteratorRectangleShadow")
	@Nested
	public class CalculatesCrossingsPathIteratorRectangleShadow {

    	@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorRectangleShadow(0, getS().getPathIterator(), 1, -2, 3, -1, null));
    	}

    	@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorRectangleShadow(0, getS().getPathIterator(), 1.5, 1.5, 3.5, 2.5, null));
    	}

    	@DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Path2afp.calculatesCrossingsPathIteratorRectangleShadow(0, getS().getPathIterator(), 7, 3, 9, 4, null));
    	}

    	@DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-1, Path2afp.calculatesCrossingsPathIteratorRectangleShadow(0, getS().getPathIterator(), -4, -0.5, -2, 0.5, null));
    	}

    	@DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorRectangleShadow(0, getS().getPathIterator(), 1, -2, 3, -1, CrossingComputationType.STANDARD));
    	}

    	@DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorRectangleShadow(0, getS().getPathIterator(), 1.5, 1.5, 3.5, 2.5, CrossingComputationType.STANDARD));
    	}

    	@DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorRectangleShadow(0, getS().getPathIterator(), 7, 3, 9, 4, CrossingComputationType.STANDARD));
    	}

    	@DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(-1, Path2afp.calculatesCrossingsPathIteratorRectangleShadow(0, getS().getPathIterator(), -4, -0.5, -2, 0.5, CrossingComputationType.STANDARD));
    	}

    	@DisplayName("#9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorRectangleShadow(0, getS().getPathIterator(), 1, -2, 3, -1, CrossingComputationType.AUTO_CLOSE));
    	}

    	@DisplayName("#10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorRectangleShadow(0, getS().getPathIterator(), 1.5, 1.5, 3.5, 2.5, CrossingComputationType.AUTO_CLOSE));
    	}

    	@DisplayName("#11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorRectangleShadow(0, getS().getPathIterator(), 7, 3, 9, 4, CrossingComputationType.AUTO_CLOSE));
    	}

    	@DisplayName("#12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorRectangleShadow(0, getS().getPathIterator(), -4, -0.5, -2, 0.5, CrossingComputationType.AUTO_CLOSE));
    	}

    	@DisplayName("#13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorRectangleShadow(0, getS().getPathIterator(), 1, -2, 3, -1, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
    	}

    	@DisplayName("#14")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_14(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorRectangleShadow(0, getS().getPathIterator(), 1.5, 1.5, 3.5, 2.5, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
    	}

    	@DisplayName("#15")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_15(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorRectangleShadow(0, getS().getPathIterator(), 7, 3, 9, 4, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
    	}

    	@DisplayName("#16")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_16(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorRectangleShadow(0, getS().getPathIterator(), -4, -0.5, -2, 0.5, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
    	}

    }

    @DisplayName("calculatesCrossingsPathIteratorRoundRectangleShadow")
	@Nested
	public class CalculatesCrossingsPathIteratorRoundRectangleShadow {

    	@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorRoundRectangleShadow(0, getS().getPathIterator(), 1, -2, 3, -1, .2, .1, null));
    	}

    	@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorRoundRectangleShadow(0, getS().getPathIterator(), 1.5, 1.5, 3.5, 2.5, .2, .1, null));
    	}

    	@DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Path2afp.calculatesCrossingsPathIteratorRoundRectangleShadow(0, getS().getPathIterator(), 7, 3, 9, 4, .2, .1, null));
    	}

    	@DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-1, Path2afp.calculatesCrossingsPathIteratorRoundRectangleShadow(0, getS().getPathIterator(), -4, -0.5, -2, 0.5, .1, .2, null));
    	}

    	@DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorRoundRectangleShadow(0, getS().getPathIterator(), 1, -2, 3, -1, .2, .1, CrossingComputationType.STANDARD));
    	}

    	@DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorRoundRectangleShadow(0, getS().getPathIterator(), 1.5, 1.5, 3.5, 2.5, .2, .1, CrossingComputationType.STANDARD));
    	}

    	@DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Path2afp.calculatesCrossingsPathIteratorRoundRectangleShadow(0, getS().getPathIterator(), 7, 3, 9, 4, .2, .1, CrossingComputationType.STANDARD));
    	}

    	@DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-1, Path2afp.calculatesCrossingsPathIteratorRoundRectangleShadow(0, getS().getPathIterator(), -4, -0.5, -2, 0.5, .2, .1, CrossingComputationType.STANDARD));
    	}

    	@DisplayName("#9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorRoundRectangleShadow(0, getS().getPathIterator(), 1, -2, 3, -1, .2, .1, CrossingComputationType.AUTO_CLOSE));
    	}

    	@DisplayName("#10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorRoundRectangleShadow(0, getS().getPathIterator(), 1.5, 1.5, 3.5, 2.5, .2, .1, CrossingComputationType.AUTO_CLOSE));
    	}

    	@DisplayName("#11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Path2afp.calculatesCrossingsPathIteratorRoundRectangleShadow(0, getS().getPathIterator(), 7, 3, 9, 4, .2, .1, CrossingComputationType.AUTO_CLOSE));
    	}

    	@DisplayName("#12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Path2afp.calculatesCrossingsPathIteratorRoundRectangleShadow(0, getS().getPathIterator(), -4, -0.5, -2, 0.5, .2, .1, CrossingComputationType.AUTO_CLOSE));
    	}

    	@DisplayName("#13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Path2afp.calculatesCrossingsPathIteratorRoundRectangleShadow(0, getS().getPathIterator(), 1, -2, 3, -1, .2, .1, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
    	}

    	@DisplayName("#14")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_14(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorRoundRectangleShadow(0, getS().getPathIterator(), 1.5, 1.5, 3.5, 2.5, .2, .1, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
    	}

    	@DisplayName("#15")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_15(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Path2afp.calculatesCrossingsPathIteratorRoundRectangleShadow(0, getS().getPathIterator(), 7, 3, 9, 4, .2, .1, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
    	}

    	@DisplayName("#16")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_16(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Path2afp.calculatesCrossingsPathIteratorRoundRectangleShadow(0, getS().getPathIterator(), -4, -0.5, -2, 0.5, .2, .1, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		}

    }

    @DisplayName("CalculatesCrossingsPathIteratorSegmentShadow")
	@Nested
	public class CalculatesCrossingsPathIteratorSegmentShadow {

    	@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorSegmentShadow(0, getS().getPathIterator(), 1, -1, 2, -3, null));
    	}

    	@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-1, Path2afp.calculatesCrossingsPathIteratorSegmentShadow(0, getS().getPathIterator(), 1, -6, 2, -3, null));
    	}

    	@DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorSegmentShadow(0, getS().getPathIterator(), 4, 0, 2, -3, null));
    	}

    	@DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorSegmentShadow(0, getS().getPathIterator(), 4, 0, 5, 3, null));
    	}

    	@DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorSegmentShadow(0, getS().getPathIterator(), 1, -1, 2, -3, CrossingComputationType.STANDARD));
    	}

    	@DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-1, Path2afp.calculatesCrossingsPathIteratorSegmentShadow(0, getS().getPathIterator(), 1, -6, 2, -3, CrossingComputationType.STANDARD));
    	}

    	@DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorSegmentShadow(0, getS().getPathIterator(), 4, 0, 2, -3, CrossingComputationType.STANDARD));
    	}

    	@DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorSegmentShadow(0, getS().getPathIterator(), 4, 0, 5, 3, CrossingComputationType.STANDARD));
    	}

    	@DisplayName("#9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Path2afp.calculatesCrossingsPathIteratorSegmentShadow(0, getS().getPathIterator(), 1, -1, 2, -3, CrossingComputationType.AUTO_CLOSE));
    	}

    	@DisplayName("#10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Path2afp.calculatesCrossingsPathIteratorSegmentShadow(0, getS().getPathIterator(), 1, -6, 2, -3, CrossingComputationType.AUTO_CLOSE));
    	}

    	@DisplayName("#11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorSegmentShadow(0, getS().getPathIterator(), 4, 0, 2, -3, CrossingComputationType.AUTO_CLOSE));
    	}

    	@DisplayName("#12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorSegmentShadow(0, getS().getPathIterator(), 4, 0, 5, 3, CrossingComputationType.AUTO_CLOSE));
    	}

    	@DisplayName("#13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Path2afp.calculatesCrossingsPathIteratorSegmentShadow(0, getS().getPathIterator(), 1, -1, 2, -3, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
    	}

    	@DisplayName("#14")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_14(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Path2afp.calculatesCrossingsPathIteratorSegmentShadow(0, getS().getPathIterator(), 1, -6, 2, -3, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
    	}

    	@DisplayName("#15")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_15(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    	}

    	@DisplayName("#16")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_16(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Path2afp.calculatesCrossingsPathIteratorSegmentShadow(0, getS().getPathIterator(), 4, 0, 2, -3, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
    	}

    	@DisplayName("#17")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_17(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorSegmentShadow(0, getS().getPathIterator(), 4, 0, 5, 3, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		}

    }

    @DisplayName("calculatesCrossingsPathIteratorTriangleShadow")
	@Nested
	public class CalculatesCrossingsPathIteratorTriangleShadow {

    	@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorTriangleShadow(0, getS().getPathIterator(),
					1, -1, 4, 0, 2, .5, null));
    	}

    	@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, Path2afp.calculatesCrossingsPathIteratorTriangleShadow(0, getS().getPathIterator(),
					9, 1, 12, 2, 10, 1.5, null));
    	}

    	@DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorTriangleShadow(0, getS().getPathIterator(),
					5, 0, 8, 1, 6, .5, null));
    	}

    	@DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorTriangleShadow(0, getS().getPathIterator(),
					-1, -4, 2, -3, 0, -2.5, null));
    	}

    	@DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(-1, Path2afp.calculatesCrossingsPathIteratorTriangleShadow(0, getS().getPathIterator(),
					3, -6, 6, -5, 4, -4.5, null));
    	}

    	@DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorTriangleShadow(0, getS().getPathIterator(),
    				1, -1, 4, 0, 2, .5, CrossingComputationType.STANDARD));
    	}

    	@DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorTriangleShadow(0, getS().getPathIterator(),
    				9, 1, 12, 2, 10, 1.5, CrossingComputationType.STANDARD));
    	}

    	@DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorTriangleShadow(0, getS().getPathIterator(),
    				5, 0, 8, 1, 6, .5, CrossingComputationType.STANDARD));
    	}

    	@DisplayName("#9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorTriangleShadow(0, getS().getPathIterator(),
    				-1, -4, 2, -3, 0, -2.5, CrossingComputationType.STANDARD));
    	}

    	@DisplayName("#10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(-1, Path2afp.calculatesCrossingsPathIteratorTriangleShadow(0, getS().getPathIterator(),
    				3, -6, 6, -5, 4, -4.5, CrossingComputationType.STANDARD));
    	}

    	@DisplayName("#11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorTriangleShadow(0, getS().getPathIterator(),
    				1, -1, 4, 0, 2, .5, CrossingComputationType.AUTO_CLOSE));
    	}

    	@DisplayName("#12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorTriangleShadow(0, getS().getPathIterator(),
    				9, 1, 12, 2, 10, 1.5, CrossingComputationType.AUTO_CLOSE));
    	}

    	@DisplayName("#13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorTriangleShadow(0, getS().getPathIterator(),
    				5, 0, 8, 1, 6, .5, CrossingComputationType.AUTO_CLOSE));
    	}

    	@DisplayName("#14")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_14(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorTriangleShadow(0, getS().getPathIterator(),
    				-1, -4, 2, -3, 0, -2.5, CrossingComputationType.AUTO_CLOSE));
    	}

    	@DisplayName("#15")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_15(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorTriangleShadow(0, getS().getPathIterator(),
    				3, -6, 6, -5, 4, -4.5, CrossingComputationType.AUTO_CLOSE));
    	}

    	@DisplayName("#16")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_16(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorTriangleShadow(0, getS().getPathIterator(),
    				1, -1, 4, 0, 2, .5, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
    	}

    	@DisplayName("#17")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_17(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorTriangleShadow(0, getS().getPathIterator(),
    				9, 1, 12, 2, 10, 1.5, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
    	}

    	@DisplayName("#18")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_18(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorTriangleShadow(0, getS().getPathIterator(),
    				5, 0, 8, 1, 6, .5, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
    	}

    	@DisplayName("#19")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_19(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorTriangleShadow(0, getS().getPathIterator(),
    				-1, -4, 2, -3, 0, -2.5, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
    	}

    	@DisplayName("#20")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_20(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorTriangleShadow(0, getS().getPathIterator(),
    				3, -6, 6, -5, 4, -4.5, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
    	}

    }

    @DisplayName("calculatesControlPointBoundingBox")
	@Nested
	public class CalculatesControlPointBoundingBox {

    	@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			B box = createRectangle(0, 0, 0, 0);
			Path2afp.calculatesControlPointBoundingBox(getS().getPathIterator(), box);
			assertEpsilonEquals(0, box.getMinX());
			assertEpsilonEquals(-5, box.getMinY());
			assertEpsilonEquals(7, box.getMaxX());
			assertEpsilonEquals(5, box.getMaxY());
		}

    }

    @DisplayName("calculatesDrawableElementBoundingBox")
	@Nested
	public class CalculatesDrawableElementBoundingBox {

    	@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			B box = createRectangle(0, 0, 0, 0);
			Path2afp.calculatesDrawableElementBoundingBox(getS().getPathIterator(), box);
			assertEpsilonEquals(0, box.getMinX());
			assertEpsilonEquals(-5, box.getMinY());
			assertEpsilonEquals(7, box.getMaxX());
			assertEpsilonEquals(3, box.getMaxY());
    	}

    }

    @DisplayName("containsPoint")
	@Nested
	public class ContainsPoint {

    	@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Path2afp.containsPoint(getS().getPathIterator(), -5, 1));
    	}

    	@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Path2afp.containsPoint(getS().getPathIterator(), 3, 6));
    	}

    	@DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Path2afp.containsPoint(getS().getPathIterator(), 3, -10));
    	}

    	@DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Path2afp.containsPoint(getS().getPathIterator(), 11, 1));
    	}

    	@DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Path2afp.containsPoint(getS().getPathIterator(), 4, 1));
    	}

    	@DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Path2afp.containsPoint(getS().getPathIterator(), 4, 3));
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
			getS().closePath();
			assertFalse(Path2afp.containsPoint(getS().getPathIterator(), -5, 1));
    	}

    	@DisplayName("#9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(Path2afp.containsPoint(getS().getPathIterator(), 3, 6));
    	}

    	@DisplayName("#10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(Path2afp.containsPoint(getS().getPathIterator(), 3, -10));
    	}

    	@DisplayName("#11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(Path2afp.containsPoint(getS().getPathIterator(), 11, 1));
    	}

    	@DisplayName("#12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(Path2afp.containsPoint(getS().getPathIterator(), 4, 1));
    	}

    	@DisplayName("#13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(Path2afp.containsPoint(getS().getPathIterator(), 4, 3));
		}

    }

    @DisplayName("containsRectangle")
	@Nested
	public class ContainsRectangle {

    	@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Path2afp.containsRectangle(getS().getPathIterator(), -5, 1, 2, 1));
    	}

    	@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Path2afp.containsRectangle(getS().getPathIterator(), 3, 6, 2, 1));
    	}

    	@DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Path2afp.containsRectangle(getS().getPathIterator(), 3, -10, 2, 1));
    	}

    	@DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Path2afp.containsRectangle(getS().getPathIterator(), 11, 1, 2, 1));
    	}

    	@DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Path2afp.containsRectangle(getS().getPathIterator(), 3, 1, 2, 1));
    	}

    	@DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Path2afp.containsRectangle(getS().getPathIterator(), 4, 3, 2, 1));
    	}

    	@DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(Path2afp.containsRectangle(getS().getPathIterator(), -5, 1, 2, 1));
    	}

    	@DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(Path2afp.containsRectangle(getS().getPathIterator(), 3, 6, 2, 1));
    	}

    	@DisplayName("#9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(Path2afp.containsRectangle(getS().getPathIterator(), 3, -10, 2, 1));
    	}

    	@DisplayName("#10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(Path2afp.containsRectangle(getS().getPathIterator(), 11, 1, 2, 1));
    	}

    	@DisplayName("#11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(Path2afp.containsRectangle(getS().getPathIterator(), 3, 0, 2, 1));
    	}

    	@DisplayName("#12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(Path2afp.containsRectangle(getS().getPathIterator(), 4, 3, 2, 1));
		}

    }

    @DisplayName("findsClosestPointPathIteratorPoint")
	@Nested
	public class FindsClosestPointPathIteratorPoint {

    	private Point2D result;

    	@BeforeEach
    	public void setUp() {
    		result = createPoint(Double.NaN, Double.NaN);
    	}
    	
    	@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2afp.findsClosestPointPathIteratorPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), -2, 1, result);
			assertEpsilonEquals(0, result.getX());
			assertEpsilonEquals(0, result.getY());
    	}
    	
    	@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2afp.findsClosestPointPathIteratorPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 0, result);
			assertEpsilonEquals(.5, result.getX());
			assertEpsilonEquals(.5, result.getY());
    	}
    	
    	@DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2afp.findsClosestPointPathIteratorPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 3, 0, result);
			assertEpsilonEquals(2.56307, result.getX());
			assertEpsilonEquals(0.91027, result.getY());
    	}
    	
    	@DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2afp.findsClosestPointPathIteratorPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, -4, result);
			assertEpsilonEquals(0, result.getX());
			assertEpsilonEquals(0, result.getY());
    	}
    	
    	@DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		Path2afp.findsClosestPointPathIteratorPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), -2, 1, result);
    		assertEpsilonEquals(0, result.getX());
    		assertEpsilonEquals(0, result.getY());
    	}
    	
    	@DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		Path2afp.findsClosestPointPathIteratorPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 0, result);
    		assertEpsilonEquals(1, result.getX());
    		assertEpsilonEquals(0, result.getY());
    	}
    	
    	@DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		Path2afp.findsClosestPointPathIteratorPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 3, 0, result);
    		assertEpsilonEquals(3, result.getX());
    		assertEpsilonEquals(0, result.getY());
    	}
    	
    	@DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		Path2afp.findsClosestPointPathIteratorPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, -4, result);
    		assertEpsilonEquals(2.55405, result.getX());
    		assertEpsilonEquals(-1.82432, result.getY());
    	}

    }

    @DisplayName("findsFarthestPointPathIteratorPoint")
	@Nested
	public class FindsFarthestPointPathIteratorPoint {

    	private Point2D result;

    	@BeforeEach
    	public void setUp() {
    		result = createPoint(Double.NaN, Double.NaN);
    	}
    	
    	@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2afp.findsFarthestPointPathIteratorPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), -2, 1, result);
			assertEpsilonEquals(7, result.getX());
			assertEpsilonEquals(-5, result.getY());
    	}
    	
    	@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2afp.findsFarthestPointPathIteratorPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 0, result);
			assertEpsilonEquals(7, result.getX());
			assertEpsilonEquals(-5, result.getY());
    	}
    	
    	@DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2afp.findsFarthestPointPathIteratorPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 3, 0, result);
			assertEpsilonEquals(7, result.getX());
			assertEpsilonEquals(-5, result.getY());
    	}
    	
    	@DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2afp.findsFarthestPointPathIteratorPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, -4, result);
			assertEpsilonEquals(4, result.getX());
			assertEpsilonEquals(3, result.getY());
    	}
    	
    	@DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			Path2afp.findsFarthestPointPathIteratorPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), -2, 1, result);
			assertEpsilonEquals(7, result.getX());
			assertEpsilonEquals(-5, result.getY());
    	}
    	
    	@DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			Path2afp.findsFarthestPointPathIteratorPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 0, result);
			assertEpsilonEquals(7, result.getX());
			assertEpsilonEquals(-5, result.getY());
    	}
    	
    	@DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			Path2afp.findsFarthestPointPathIteratorPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 3, 0, result);
			assertEpsilonEquals(7, result.getX());
			assertEpsilonEquals(-5, result.getY());
    	}
    	
    	@DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			Path2afp.findsFarthestPointPathIteratorPoint(getS().getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, -4, result);
			assertEpsilonEquals(4, result.getX());
			assertEpsilonEquals(3, result.getY());
		}

    }

    @DisplayName("intersectsPathIteratorRectangle")
	@Nested
	public class IntersectsPathIteratorRectangle {

    	@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Path2afp.intersectsPathIteratorRectangle(getS().getPathIterator(), 1, -2, 2, 1));
    	}

    	@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Path2afp.intersectsPathIteratorRectangle(getS().getPathIterator(), 1.5, 1.5, 2, 1));
    	}

    	@DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Path2afp.intersectsPathIteratorRectangle(getS().getPathIterator(), 7, 3, 2, 1));
    	}

    	@DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Path2afp.intersectsPathIteratorRectangle(getS().getPathIterator(), -4, -0.5, 2, 1));
    	}

    	@DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		assertTrue(Path2afp.intersectsPathIteratorRectangle(getS().getPathIterator(), 1.5, 1.5, 2, 1));
    	}

    	@DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		assertFalse(Path2afp.intersectsPathIteratorRectangle(getS().getPathIterator(), 7, 3, 2, 1));
    	}

    	@DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		assertFalse(Path2afp.intersectsPathIteratorRectangle(getS().getPathIterator(), -4, -0.5, 2, 1));
    	}

    	@DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		assertTrue(Path2afp.intersectsPathIteratorRectangle(getS().getPathIterator(), 1, -2, 2, 1));
    	}

    }

    @DisplayName("calculatesPathLength")
	@Nested
	public class CalculatesPathLength {

    	@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	assertEpsilonEquals(14.71628, Path2afp.calculatesPathLength(getS().getPathIterator()));
    	}

    	@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		assertEpsilonEquals(23.31861, Path2afp.calculatesPathLength(getS().getPathIterator()));
    	}

    }

    @DisplayName("add(Iterator)")
	@Nested
	public class AddIterator {

    	@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2afp<?, ?, ?, ?, ?, B> p2 = createPath();
			p2.moveTo(7, -5);
			p2.lineTo(4, 6);
			p2.lineTo(0, 8);
			p2.lineTo(5, -3);
			p2.closePath();
	
			Iterator<? extends PathElement2afp> iterator = p2.getPathIterator();
			iterator.next();
			getS().add(iterator);
	
			PathIterator2afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, 1);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
			assertElement(pi, PathElementType.LINE_TO, 4, 6);
			assertElement(pi, PathElementType.LINE_TO, 0, 8);
			assertElement(pi, PathElementType.LINE_TO, 5, -3);
			assertElement(pi, PathElementType.CLOSE, 0, 0);
			assertNoElement(pi);
		}

    	@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
    	@EnumSource(CoordinateSystem2D.class)
    	public void addIterator_closeAfter(CoordinateSystem2D cs) {
        	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		Path2afp<?, ?, ?, ?, ?, B> p2 = createPath();
    		p2.moveTo(7, -5);
    		p2.lineTo(4, 6);
    		p2.lineTo(0, 8);
    		p2.lineTo(5, -3);
    		p2.closePath();

    		Iterator<? extends PathElement2afp> iterator = p2.getPathIterator();
    		iterator.next();
    		getS().add(iterator);

    		getS().closePath();

    		PathIterator2afp pi = getS().getPathIterator();
    		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
    		assertElement(pi, PathElementType.LINE_TO, 1, 1);
    		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
    		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
    		assertElement(pi, PathElementType.LINE_TO, 4, 6);
    		assertElement(pi, PathElementType.LINE_TO, 0, 8);
    		assertElement(pi, PathElementType.LINE_TO, 5, -3);
    		assertElement(pi, PathElementType.CLOSE, 0, 0);
    		assertNoElement(pi);
    	}

    	@DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
    	@EnumSource(CoordinateSystem2D.class)
    	public void addIterator_closeBefore(CoordinateSystem2D cs) {
        	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();

    		Path2afp<?, ?, ?, ?, ?, B> p2 = createPath();
    		p2.moveTo(7, -5);
    		p2.lineTo(4, 6);
    		p2.lineTo(0, 8);
    		p2.lineTo(5, -3);
    		p2.closePath();

    		Iterator<? extends PathElement2afp> iterator = p2.getPathIterator();
    		iterator.next();
    		getS().add(iterator);

    		PathIterator2afp pi = getS().getPathIterator();
    		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
    		assertElement(pi, PathElementType.LINE_TO, 1, 1);
    		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
    		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
    		assertElement(pi, PathElementType.CLOSE, 0, 0);
    		assertElement(pi, PathElementType.LINE_TO, 4, 6);
    		assertElement(pi, PathElementType.LINE_TO, 0, 8);
    		assertElement(pi, PathElementType.LINE_TO, 5, -3);
    		assertElement(pi, PathElementType.CLOSE, 0, 0);
    		assertNoElement(pi);
    	}

    }

    @DisplayName("curveTo(double,double,double,double,double,double)")
	@Nested
	public class curveToDoubleDoubleDoubleDoubleDoubleDouble {

    	@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertThrows(IllegalStateException.class, () -> {
				Path2afp<?, ?, ?, ?, ?, ?> tmpShape = createPath();
				tmpShape.curveTo(15, 145, 50, 20, 0, 0);
			});
		}
		
    	@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().curveTo(123.456, 456.789, 789.123, 159.753, 456.852, 963.789);
			PathIterator2afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, 1);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
			assertElement(pi, PathElementType.CURVE_TO, 123.456,  456.789, 789.123, 159.753, 456.852, 963.789);
			assertNoElement(pi);
		}

    }
	
    @DisplayName("curveTo(Point2D,Point2D,Point2D)")
	@Nested
	public class curveToPoint2DPoint2DPoint2D {

    	@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertThrows(IllegalStateException.class, () -> {
				Path2afp<?, ?, ?, ?, ?, ?> tmpShape = createPath();
				tmpShape.curveTo(createPoint(15, 145), createPoint(50, 20), createPoint(0, 0));
			});
		}
	
    	@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void curveToPoint2DPoint2DPoint2D(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().curveTo(createPoint(123.456, 456.789), createPoint(789.123, 159.753), createPoint(456.852, 963.789));
			PathIterator2afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, 1);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
			assertElement(pi, PathElementType.CURVE_TO, 123.456,  456.789, 789.123, 159.753, 456.852, 963.789);
			assertNoElement(pi);
		}

    }

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void arcToDoubleDoubleDoubleDoubleDoubleDoubleArcType_01_arcOnly(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		getS().arcTo(5, 5, 20, 10, 0, 1, ArcType.ARC_ONLY);
		PathIterator2afp pi = getS().getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CURVE_TO, 5.89543, 0.52285, 11.71573, 7.23858, 20, 10);
		assertNoElement(pi);
	}

    @DisplayName("arcTo(double,double,double,double,double,double,ArcType)")
	@Nested
	public class ArcToDoubleDoubleDoubleDoubleDoubleDoubleArcType {

    	@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().arcTo(5, 5, 20, 10, 0, 1, ArcType.LINE_THEN_ARC);
			PathIterator2afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, 1);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
			assertElement(pi, PathElementType.CURVE_TO, 5.89543, 0.52285, 11.71573, 7.23858, 20, 10);
			assertNoElement(pi);
		}
	
    	@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void arcToDoubleDoubleDoubleDoubleDoubleDoubleArcType_01_moveTo(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().arcTo(5, 5, 20, 10, 0, 1, ArcType.MOVE_THEN_ARC);
			PathIterator2afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, 1);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
			assertElement(pi, PathElementType.CURVE_TO, 5.89543, 0.52285, 11.71573, 7.23858, 20, 10);
			assertNoElement(pi);
		}
	
    	@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void arcToDoubleDoubleDoubleDoubleDoubleDoubleArcType_0251_arcOnly(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().arcTo(5, 5, 20, 10, .25, 1, ArcType.ARC_ONLY);
			PathIterator2afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, 1);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
			assertElement(pi, PathElementType.CURVE_TO, 8.95958, 3.63357, 13.7868, 7.92893, 20, 10);
			assertNoElement(pi);
		}
	
    	@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void arcToDoubleDoubleDoubleDoubleDoubleDoubleArcType_0251_lineTo(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().arcTo(5, 5, 20, 10, .25, 1, ArcType.LINE_THEN_ARC);
			PathIterator2afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, 1);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
			assertElement(pi, PathElementType.LINE_TO, 7.40028, -0.71462);
			assertElement(pi, PathElementType.CURVE_TO, 8.95958, 3.63357, 13.7868, 7.92893, 20, 10);
			assertNoElement(pi);
		}
	
    	@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void arcToDoubleDoubleDoubleDoubleDoubleDoubleArcType_0251_moveTo(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().arcTo(5, 5, 20, 10, .25, 1, ArcType.MOVE_THEN_ARC);
			PathIterator2afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, 1);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
			assertElement(pi, PathElementType.MOVE_TO, 7.40028, -0.71462);
			assertElement(pi, PathElementType.CURVE_TO, 8.95958, 3.63357, 13.7868, 7.92893, 20, 10);
			assertNoElement(pi);
		}

    }
    
    @DisplayName("arcTo(Point2D,Point2D,Point2D,ArcType)")
	@Nested
	public class ArcToPoint2DPoint2DPoint2DArcType {

    	@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().arcTo(createPoint(5, 5), createPoint(20, 10), 0, 1, ArcType.ARC_ONLY);
			PathIterator2afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, 1);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
			assertElement(pi, PathElementType.CURVE_TO, 5.89543, 0.52285, 11.71573, 7.23858, 20, 10);
			assertNoElement(pi);
		}

    }

    @DisplayName("arcTo(double,double,double,double)")
	@Nested
	public class ArcToDoubleDoubleDoubleDouble {

    	@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().arcTo(5, 5, 20, 10);
			PathIterator2afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, 1);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
			assertElement(pi, PathElementType.CURVE_TO, 5.89543, 0.52285, 11.71573, 7.23858, 20, 10);
			assertNoElement(pi);
		}

    }

    @DisplayName("arcTo(Point2D,Point2D)")
	@Nested
	public class ArcToPoint2DPoint2D {

    	@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().arcTo(createPoint(5, 5), createPoint(20, 10));
			PathIterator2afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, 1);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
			assertElement(pi, PathElementType.CURVE_TO, 5.89543, 0.52285, 11.71573, 7.23858, 20, 10);
			assertNoElement(pi);
		}

    }

    @DisplayName("getCoordAt")
	@Nested
	public class GetCoordAt {

    	@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getCoordAt(0)==0);
    	}

    	@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getCoordAt(1)==0);
    	}

    	@DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getCoordAt(2)==1);
    	}

    	@DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getCoordAt(3)==1);
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
			assertTrue(getS().getCoordAt(4)==3);
    	}

    	@DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getCoordAt(5)==0);
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
			assertTrue(getS().getCoordAt(6)==4);
    	}

    	@DisplayName("#10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getCoordAt(7)==3);
    	}

    	@DisplayName("#11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getCoordAt(8)==5);
    	}

    	@DisplayName("#12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getCoordAt(9)==-1);
    	}

    	@DisplayName("#13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
			assertTrue(getS().getCoordAt(10)==6);
    	}

    	@DisplayName("#16")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_16(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getCoordAt(11)==5);
    	}

    	@DisplayName("#17")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_17(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getCoordAt(12)==7);
    	}

    	@DisplayName("#18")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_18(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getCoordAt(13)==-5);
		}

    }

    @DisplayName("getPathIterator(double)")
	@Nested
	public class GetPathIteratorDouble {

    	@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			PathIterator2afp pi = getS().getPathIterator(SPLINE_APPROXIMATION_RATIO);
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, 1);
			assertElement(pi, PathElementType.LINE_TO, 1.484375, 0.8125);
			assertElement(pi, PathElementType.LINE_TO, 1.9375, 0.75);
			assertElement(pi, PathElementType.LINE_TO, 2.359375, 0.8125);
			assertElement(pi, PathElementType.LINE_TO, 2.75, 1.0);
			assertElement(pi, PathElementType.LINE_TO, 3.109375, 1.3125);
			assertElement(pi, PathElementType.LINE_TO, 3.4375, 1.75);
			assertElement(pi, PathElementType.LINE_TO, 4.0, 3.0);
			assertElement(pi, PathElementType.LINE_TO, 4.0234375, 2.90807);
			assertElement(pi, PathElementType.LINE_TO, 4.046875, 2.819725);
			assertElement(pi, PathElementType.LINE_TO, 4.070313, 2.734895);
			assertElement(pi, PathElementType.LINE_TO, 4.09375, 2.6535034);
			assertElement(pi, PathElementType.LINE_TO, 4.11719, 2.5754766);
			assertElement(pi, PathElementType.LINE_TO, 4.14063, 2.5007401);
			assertElement(pi, PathElementType.LINE_TO, 4.16406, 2.4292192);
			assertElement(pi, PathElementType.LINE_TO, 4.1875, 2.3608398);
			assertElement(pi, PathElementType.LINE_TO, 4.234375, 2.233208);
			assertElement(pi, PathElementType.LINE_TO, 4.28125, 2.117249);
			assertElement(pi, PathElementType.LINE_TO, 4.328125, 2.012367);
			assertElement(pi, PathElementType.LINE_TO, 4.375, 1.917969);
			assertElement(pi, PathElementType.LINE_TO, 4.421875, 1.833458);
			assertElement(pi, PathElementType.LINE_TO, 4.46875, 1.758239);
			assertElement(pi, PathElementType.LINE_TO, 4.515625, 1.691719);
			assertElement(pi, PathElementType.LINE_TO, 4.5625, 1.6333);
			assertElement(pi, PathElementType.LINE_TO, 4.65625, 1.538391);
			assertElement(pi, PathElementType.LINE_TO, 4.75, 1.46875);
			assertElement(pi, PathElementType.LINE_TO, 4.84375, 1.419617);
			assertElement(pi, PathElementType.LINE_TO, 4.9375, 1.38623);
			assertElement(pi, PathElementType.LINE_TO, 5.03125, 1.363831);
			assertElement(pi, PathElementType.LINE_TO, 5.125, 1.347656);
			assertElement(pi, PathElementType.LINE_TO, 5.21875, 1.332947);
			assertElement(pi, PathElementType.LINE_TO, 5.3125, 1.314941);
			assertElement(pi, PathElementType.LINE_TO, 5.40625, 1.288879);
			assertElement(pi, PathElementType.LINE_TO, 5.5, 1.25);
			assertElement(pi, PathElementType.LINE_TO, 5.59375, 1.193542);
			assertElement(pi, PathElementType.LINE_TO, 5.6875, 1.114746);
			assertElement(pi, PathElementType.LINE_TO, 5.78125, 1.00885);
			assertElement(pi, PathElementType.LINE_TO, 5.828125, 0.944252);
			assertElement(pi, PathElementType.LINE_TO, 5.875, 0.871094);
			assertElement(pi, PathElementType.LINE_TO, 5.921875, 0.788780);
			assertElement(pi, PathElementType.LINE_TO, 5.96875, 0.696716);
			assertElement(pi, PathElementType.LINE_TO, 6.015625, 0.594307);
			assertElement(pi, PathElementType.LINE_TO, 6.0625, 0.480957);
			assertElement(pi, PathElementType.LINE_TO, 6.109375, 0.356071);
			assertElement(pi, PathElementType.LINE_TO, 6.15625, 0.219055);
			assertElement(pi, PathElementType.LINE_TO, 6.179688, 0.145812);
			assertElement(pi, PathElementType.LINE_TO, 6.203125, 0.069313);
			assertElement(pi, PathElementType.LINE_TO, 6.226563, -0.010516);
			assertElement(pi, PathElementType.LINE_TO, 6.25, -0.09375);
			assertElement(pi, PathElementType.LINE_TO, 6.273438, -0.180463);
			assertElement(pi, PathElementType.LINE_TO, 6.296875, -0.270729);
			assertElement(pi, PathElementType.LINE_TO, 6.320313, -0.364623);
			assertElement(pi, PathElementType.LINE_TO, 6.34375, -0.462219);
			assertElement(pi, PathElementType.LINE_TO, 6.36719, -0.563592);
			assertElement(pi, PathElementType.LINE_TO, 6.39063, -0.668816);
			assertElement(pi, PathElementType.LINE_TO, 6.41406, -0.7779646);
			assertElement(pi, PathElementType.LINE_TO, 6.4375, -0.891113);
			assertElement(pi, PathElementType.LINE_TO, 6.460938, -1.008336);
			assertElement(pi, PathElementType.LINE_TO, 6.484375, -1.129707);
			assertElement(pi, PathElementType.LINE_TO, 6.507813, -1.255301);
			assertElement(pi, PathElementType.LINE_TO, 6.53125, -1.385193);
			assertElement(pi, PathElementType.LINE_TO, 6.55469, -1.519456);
			assertElement(pi, PathElementType.LINE_TO, 6.57813, -1.658165);
			assertElement(pi, PathElementType.LINE_TO, 6.60156, -1.801394);
			assertElement(pi, PathElementType.LINE_TO, 6.625, -1.949219);
			assertElement(pi, PathElementType.LINE_TO, 6.63672, -2.024877);
			assertElement(pi, PathElementType.LINE_TO, 6.648438, -2.101712);
			assertElement(pi, PathElementType.LINE_TO, 6.660156, -2.179733);
			assertElement(pi, PathElementType.LINE_TO, 6.671875, -2.258949);
			assertElement(pi, PathElementType.LINE_TO, 6.683594, -2.33937);
			assertElement(pi, PathElementType.LINE_TO, 6.695313, -2.421);
			assertElement(pi, PathElementType.LINE_TO, 6.707031, -2.503862);
			assertElement(pi, PathElementType.LINE_TO, 6.71875, -2.587952);
			assertElement(pi, PathElementType.LINE_TO, 6.730469, -2.673283);
			assertElement(pi, PathElementType.LINE_TO, 6.742188, -2.759866);
			assertElement(pi, PathElementType.LINE_TO, 6.753906, -2.847709);
			assertElement(pi, PathElementType.LINE_TO, 6.765625, -2.936821);
			assertElement(pi, PathElementType.LINE_TO, 6.777344, -3.027212);
			assertElement(pi, PathElementType.LINE_TO, 6.789063, -3.118892);
			assertElement(pi, PathElementType.LINE_TO, 6.800781, -3.211869);
			assertElement(pi, PathElementType.LINE_TO, 6.8125, -3.306152);
			assertElement(pi, PathElementType.LINE_TO, 6.824219, -3.401752);
			assertElement(pi, PathElementType.LINE_TO, 6.835938, -3.498677);
			assertElement(pi, PathElementType.LINE_TO, 6.847656, -3.596937);
			assertElement(pi, PathElementType.LINE_TO, 6.859375, -3.696541);
			assertElement(pi, PathElementType.LINE_TO, 6.871094, -3.797498);
			assertElement(pi, PathElementType.LINE_TO, 6.882813, -3.899817);
			assertElement(pi, PathElementType.LINE_TO, 6.894531, -4.003509);
			assertElement(pi, PathElementType.LINE_TO, 6.90625, -4.108582);
			assertElement(pi, PathElementType.LINE_TO, 6.917969, -4.215045);
			assertElement(pi, PathElementType.LINE_TO, 6.929688, -4.322907);
			assertElement(pi, PathElementType.LINE_TO, 6.941406, -4.432179);
			assertElement(pi, PathElementType.LINE_TO, 6.953125, -4.54287);
			assertElement(pi, PathElementType.LINE_TO, 6.964844, -4.654987);
			assertElement(pi, PathElementType.LINE_TO, 6.976563, -4.768542);
			assertElement(pi, PathElementType.LINE_TO, 6.988281, -4.883543);
			assertElement(pi, PathElementType.LINE_TO, 7, -5);
			assertNoElement(pi);
		}

    	@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
    	@EnumSource(CoordinateSystem2D.class)
    	public void getPathIteratorDouble_close(CoordinateSystem2D cs) {
        	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		PathIterator2afp pi = getS().getPathIterator(SPLINE_APPROXIMATION_RATIO);
    		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
    		assertElement(pi, PathElementType.LINE_TO, 1, 1);
    		assertElement(pi, PathElementType.LINE_TO, 1.484375, 0.8125);
    		assertElement(pi, PathElementType.LINE_TO, 1.9375, 0.75);
    		assertElement(pi, PathElementType.LINE_TO, 2.359375, 0.8125);
    		assertElement(pi, PathElementType.LINE_TO, 2.75, 1.0);
    		assertElement(pi, PathElementType.LINE_TO, 3.109375, 1.3125);
    		assertElement(pi, PathElementType.LINE_TO, 3.4375, 1.75);
    		assertElement(pi, PathElementType.LINE_TO, 4.0, 3.0);
    		assertElement(pi, PathElementType.LINE_TO, 4.0234375, 2.90807);
    		assertElement(pi, PathElementType.LINE_TO, 4.046875, 2.819725);
    		assertElement(pi, PathElementType.LINE_TO, 4.070313, 2.734895);
    		assertElement(pi, PathElementType.LINE_TO, 4.09375, 2.6535034);
    		assertElement(pi, PathElementType.LINE_TO, 4.11719, 2.5754766);
    		assertElement(pi, PathElementType.LINE_TO, 4.14063, 2.5007401);
    		assertElement(pi, PathElementType.LINE_TO, 4.16406, 2.4292192);
    		assertElement(pi, PathElementType.LINE_TO, 4.1875, 2.3608398);
    		assertElement(pi, PathElementType.LINE_TO, 4.234375, 2.233208);
    		assertElement(pi, PathElementType.LINE_TO, 4.28125, 2.117249);
    		assertElement(pi, PathElementType.LINE_TO, 4.328125, 2.012367);
    		assertElement(pi, PathElementType.LINE_TO, 4.375, 1.917969);
    		assertElement(pi, PathElementType.LINE_TO, 4.421875, 1.833458);
    		assertElement(pi, PathElementType.LINE_TO, 4.46875, 1.758239);
    		assertElement(pi, PathElementType.LINE_TO, 4.515625, 1.691719);
    		assertElement(pi, PathElementType.LINE_TO, 4.5625, 1.6333);
    		assertElement(pi, PathElementType.LINE_TO, 4.65625, 1.538391);
    		assertElement(pi, PathElementType.LINE_TO, 4.75, 1.46875);
    		assertElement(pi, PathElementType.LINE_TO, 4.84375, 1.419617);
    		assertElement(pi, PathElementType.LINE_TO, 4.9375, 1.38623);
    		assertElement(pi, PathElementType.LINE_TO, 5.03125, 1.363831);
    		assertElement(pi, PathElementType.LINE_TO, 5.125, 1.347656);
    		assertElement(pi, PathElementType.LINE_TO, 5.21875, 1.332947);
    		assertElement(pi, PathElementType.LINE_TO, 5.3125, 1.314941);
    		assertElement(pi, PathElementType.LINE_TO, 5.40625, 1.288879);
    		assertElement(pi, PathElementType.LINE_TO, 5.5, 1.25);
    		assertElement(pi, PathElementType.LINE_TO, 5.59375, 1.193542);
    		assertElement(pi, PathElementType.LINE_TO, 5.6875, 1.114746);
    		assertElement(pi, PathElementType.LINE_TO, 5.78125, 1.00885);
    		assertElement(pi, PathElementType.LINE_TO, 5.828125, 0.944252);
    		assertElement(pi, PathElementType.LINE_TO, 5.875, 0.871094);
    		assertElement(pi, PathElementType.LINE_TO, 5.921875, 0.788780);
    		assertElement(pi, PathElementType.LINE_TO, 5.96875, 0.696716);
    		assertElement(pi, PathElementType.LINE_TO, 6.015625, 0.594307);
    		assertElement(pi, PathElementType.LINE_TO, 6.0625, 0.480957);
    		assertElement(pi, PathElementType.LINE_TO, 6.109375, 0.356071);
    		assertElement(pi, PathElementType.LINE_TO, 6.15625, 0.219055);
    		assertElement(pi, PathElementType.LINE_TO, 6.179688, 0.145812);
    		assertElement(pi, PathElementType.LINE_TO, 6.203125, 0.069313);
    		assertElement(pi, PathElementType.LINE_TO, 6.226563, -0.010516);
    		assertElement(pi, PathElementType.LINE_TO, 6.25, -0.09375);
    		assertElement(pi, PathElementType.LINE_TO, 6.273438, -0.180463);
    		assertElement(pi, PathElementType.LINE_TO, 6.296875, -0.270729);
    		assertElement(pi, PathElementType.LINE_TO, 6.320313, -0.364623);
    		assertElement(pi, PathElementType.LINE_TO, 6.34375, -0.462219);
    		assertElement(pi, PathElementType.LINE_TO, 6.36719, -0.563592);
    		assertElement(pi, PathElementType.LINE_TO, 6.39063, -0.668816);
    		assertElement(pi, PathElementType.LINE_TO, 6.41406, -0.7779646);
    		assertElement(pi, PathElementType.LINE_TO, 6.4375, -0.891113);
    		assertElement(pi, PathElementType.LINE_TO, 6.460938, -1.008336);
    		assertElement(pi, PathElementType.LINE_TO, 6.484375, -1.129707);
    		assertElement(pi, PathElementType.LINE_TO, 6.507813, -1.255301);
    		assertElement(pi, PathElementType.LINE_TO, 6.53125, -1.385193);
    		assertElement(pi, PathElementType.LINE_TO, 6.55469, -1.519456);
    		assertElement(pi, PathElementType.LINE_TO, 6.57813, -1.658165);
    		assertElement(pi, PathElementType.LINE_TO, 6.60156, -1.801394);
    		assertElement(pi, PathElementType.LINE_TO, 6.625, -1.949219);
    		assertElement(pi, PathElementType.LINE_TO, 6.63672, -2.024877);
    		assertElement(pi, PathElementType.LINE_TO, 6.648438, -2.101712);
    		assertElement(pi, PathElementType.LINE_TO, 6.660156, -2.179733);
    		assertElement(pi, PathElementType.LINE_TO, 6.671875, -2.258949);
    		assertElement(pi, PathElementType.LINE_TO, 6.683594, -2.33937);
    		assertElement(pi, PathElementType.LINE_TO, 6.695313, -2.421);
    		assertElement(pi, PathElementType.LINE_TO, 6.707031, -2.503862);
    		assertElement(pi, PathElementType.LINE_TO, 6.71875, -2.587952);
    		assertElement(pi, PathElementType.LINE_TO, 6.730469, -2.673283);
    		assertElement(pi, PathElementType.LINE_TO, 6.742188, -2.759866);
    		assertElement(pi, PathElementType.LINE_TO, 6.753906, -2.847709);
    		assertElement(pi, PathElementType.LINE_TO, 6.765625, -2.936821);
    		assertElement(pi, PathElementType.LINE_TO, 6.777344, -3.027212);
    		assertElement(pi, PathElementType.LINE_TO, 6.789063, -3.118892);
    		assertElement(pi, PathElementType.LINE_TO, 6.800781, -3.211869);
    		assertElement(pi, PathElementType.LINE_TO, 6.8125, -3.306152);
    		assertElement(pi, PathElementType.LINE_TO, 6.824219, -3.401752);
    		assertElement(pi, PathElementType.LINE_TO, 6.835938, -3.498677);
    		assertElement(pi, PathElementType.LINE_TO, 6.847656, -3.596937);
    		assertElement(pi, PathElementType.LINE_TO, 6.859375, -3.696541);
    		assertElement(pi, PathElementType.LINE_TO, 6.871094, -3.797498);
    		assertElement(pi, PathElementType.LINE_TO, 6.882813, -3.899817);
    		assertElement(pi, PathElementType.LINE_TO, 6.894531, -4.003509);
    		assertElement(pi, PathElementType.LINE_TO, 6.90625, -4.108582);
    		assertElement(pi, PathElementType.LINE_TO, 6.917969, -4.215045);
    		assertElement(pi, PathElementType.LINE_TO, 6.929688, -4.322907);
    		assertElement(pi, PathElementType.LINE_TO, 6.941406, -4.432179);
    		assertElement(pi, PathElementType.LINE_TO, 6.953125, -4.54287);
    		assertElement(pi, PathElementType.LINE_TO, 6.964844, -4.654987);
    		assertElement(pi, PathElementType.LINE_TO, 6.976563, -4.768542);
    		assertElement(pi, PathElementType.LINE_TO, 6.988281, -4.883543);
    		assertElement(pi, PathElementType.LINE_TO, 7, -5);
    		assertElement(pi, PathElementType.CLOSE, 0, 0);
    		assertNoElement(pi);
    	}

    }

    @DisplayName("getPathIterator(Transform2D,double)")
	@Nested
	public class GetPathIteratorTransform2DDouble {

    	@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			PathIterator2afp pi = getS().getPathIterator(null, SPLINE_APPROXIMATION_RATIO);
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, 1);
			assertElement(pi, PathElementType.LINE_TO, 1.484375, 0.8125);
			assertElement(pi, PathElementType.LINE_TO, 1.9375, 0.75);
			assertElement(pi, PathElementType.LINE_TO, 2.359375, 0.8125);
			assertElement(pi, PathElementType.LINE_TO, 2.75, 1.0);
			assertElement(pi, PathElementType.LINE_TO, 3.109375, 1.3125);
			assertElement(pi, PathElementType.LINE_TO, 3.4375, 1.75);
			assertElement(pi, PathElementType.LINE_TO, 4.0, 3.0);
			assertElement(pi, PathElementType.LINE_TO, 4.0234375, 2.90807);
			assertElement(pi, PathElementType.LINE_TO, 4.046875, 2.819725);
			assertElement(pi, PathElementType.LINE_TO, 4.070313, 2.734895);
			assertElement(pi, PathElementType.LINE_TO, 4.09375, 2.6535034);
			assertElement(pi, PathElementType.LINE_TO, 4.11719, 2.5754766);
			assertElement(pi, PathElementType.LINE_TO, 4.14063, 2.5007401);
			assertElement(pi, PathElementType.LINE_TO, 4.16406, 2.4292192);
			assertElement(pi, PathElementType.LINE_TO, 4.1875, 2.3608398);
			assertElement(pi, PathElementType.LINE_TO, 4.234375, 2.233208);
			assertElement(pi, PathElementType.LINE_TO, 4.28125, 2.117249);
			assertElement(pi, PathElementType.LINE_TO, 4.328125, 2.012367);
			assertElement(pi, PathElementType.LINE_TO, 4.375, 1.917969);
			assertElement(pi, PathElementType.LINE_TO, 4.421875, 1.833458);
			assertElement(pi, PathElementType.LINE_TO, 4.46875, 1.758239);
			assertElement(pi, PathElementType.LINE_TO, 4.515625, 1.691719);
			assertElement(pi, PathElementType.LINE_TO, 4.5625, 1.6333);
			assertElement(pi, PathElementType.LINE_TO, 4.65625, 1.538391);
			assertElement(pi, PathElementType.LINE_TO, 4.75, 1.46875);
			assertElement(pi, PathElementType.LINE_TO, 4.84375, 1.419617);
			assertElement(pi, PathElementType.LINE_TO, 4.9375, 1.38623);
			assertElement(pi, PathElementType.LINE_TO, 5.03125, 1.363831);
			assertElement(pi, PathElementType.LINE_TO, 5.125, 1.347656);
			assertElement(pi, PathElementType.LINE_TO, 5.21875, 1.332947);
			assertElement(pi, PathElementType.LINE_TO, 5.3125, 1.314941);
			assertElement(pi, PathElementType.LINE_TO, 5.40625, 1.288879);
			assertElement(pi, PathElementType.LINE_TO, 5.5, 1.25);
			assertElement(pi, PathElementType.LINE_TO, 5.59375, 1.193542);
			assertElement(pi, PathElementType.LINE_TO, 5.6875, 1.114746);
			assertElement(pi, PathElementType.LINE_TO, 5.78125, 1.00885);
			assertElement(pi, PathElementType.LINE_TO, 5.828125, 0.944252);
			assertElement(pi, PathElementType.LINE_TO, 5.875, 0.871094);
			assertElement(pi, PathElementType.LINE_TO, 5.921875, 0.788780);
			assertElement(pi, PathElementType.LINE_TO, 5.96875, 0.696716);
			assertElement(pi, PathElementType.LINE_TO, 6.015625, 0.594307);
			assertElement(pi, PathElementType.LINE_TO, 6.0625, 0.480957);
			assertElement(pi, PathElementType.LINE_TO, 6.109375, 0.356071);
			assertElement(pi, PathElementType.LINE_TO, 6.15625, 0.219055);
			assertElement(pi, PathElementType.LINE_TO, 6.179688, 0.145812);
			assertElement(pi, PathElementType.LINE_TO, 6.203125, 0.069313);
			assertElement(pi, PathElementType.LINE_TO, 6.226563, -0.010516);
			assertElement(pi, PathElementType.LINE_TO, 6.25, -0.09375);
			assertElement(pi, PathElementType.LINE_TO, 6.273438, -0.180463);
			assertElement(pi, PathElementType.LINE_TO, 6.296875, -0.270729);
			assertElement(pi, PathElementType.LINE_TO, 6.320313, -0.364623);
			assertElement(pi, PathElementType.LINE_TO, 6.34375, -0.462219);
			assertElement(pi, PathElementType.LINE_TO, 6.36719, -0.563592);
			assertElement(pi, PathElementType.LINE_TO, 6.39063, -0.668816);
			assertElement(pi, PathElementType.LINE_TO, 6.41406, -0.7779646);
			assertElement(pi, PathElementType.LINE_TO, 6.4375, -0.891113);
			assertElement(pi, PathElementType.LINE_TO, 6.460938, -1.008336);
			assertElement(pi, PathElementType.LINE_TO, 6.484375, -1.129707);
			assertElement(pi, PathElementType.LINE_TO, 6.507813, -1.255301);
			assertElement(pi, PathElementType.LINE_TO, 6.53125, -1.385193);
			assertElement(pi, PathElementType.LINE_TO, 6.55469, -1.519456);
			assertElement(pi, PathElementType.LINE_TO, 6.57813, -1.658165);
			assertElement(pi, PathElementType.LINE_TO, 6.60156, -1.801394);
			assertElement(pi, PathElementType.LINE_TO, 6.625, -1.949219);
			assertElement(pi, PathElementType.LINE_TO, 6.63672, -2.024877);
			assertElement(pi, PathElementType.LINE_TO, 6.648438, -2.101712);
			assertElement(pi, PathElementType.LINE_TO, 6.660156, -2.179733);
			assertElement(pi, PathElementType.LINE_TO, 6.671875, -2.258949);
			assertElement(pi, PathElementType.LINE_TO, 6.683594, -2.33937);
			assertElement(pi, PathElementType.LINE_TO, 6.695313, -2.421);
			assertElement(pi, PathElementType.LINE_TO, 6.707031, -2.503862);
			assertElement(pi, PathElementType.LINE_TO, 6.71875, -2.587952);
			assertElement(pi, PathElementType.LINE_TO, 6.730469, -2.673283);
			assertElement(pi, PathElementType.LINE_TO, 6.742188, -2.759866);
			assertElement(pi, PathElementType.LINE_TO, 6.753906, -2.847709);
			assertElement(pi, PathElementType.LINE_TO, 6.765625, -2.936821);
			assertElement(pi, PathElementType.LINE_TO, 6.777344, -3.027212);
			assertElement(pi, PathElementType.LINE_TO, 6.789063, -3.118892);
			assertElement(pi, PathElementType.LINE_TO, 6.800781, -3.211869);
			assertElement(pi, PathElementType.LINE_TO, 6.8125, -3.306152);
			assertElement(pi, PathElementType.LINE_TO, 6.824219, -3.401752);
			assertElement(pi, PathElementType.LINE_TO, 6.835938, -3.498677);
			assertElement(pi, PathElementType.LINE_TO, 6.847656, -3.596937);
			assertElement(pi, PathElementType.LINE_TO, 6.859375, -3.696541);
			assertElement(pi, PathElementType.LINE_TO, 6.871094, -3.797498);
			assertElement(pi, PathElementType.LINE_TO, 6.882813, -3.899817);
			assertElement(pi, PathElementType.LINE_TO, 6.894531, -4.003509);
			assertElement(pi, PathElementType.LINE_TO, 6.90625, -4.108582);
			assertElement(pi, PathElementType.LINE_TO, 6.917969, -4.215045);
			assertElement(pi, PathElementType.LINE_TO, 6.929688, -4.322907);
			assertElement(pi, PathElementType.LINE_TO, 6.941406, -4.432179);
			assertElement(pi, PathElementType.LINE_TO, 6.953125, -4.54287);
			assertElement(pi, PathElementType.LINE_TO, 6.964844, -4.654987);
			assertElement(pi, PathElementType.LINE_TO, 6.976563, -4.768542);
			assertElement(pi, PathElementType.LINE_TO, 6.988281, -4.883543);
			assertElement(pi, PathElementType.LINE_TO, 7, -5);
			assertNoElement(pi);
		}
	
    	@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void getPathIteratorTransform2DDouble_identity(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			PathIterator2afp pi = getS().getPathIterator(new Transform2D(), SPLINE_APPROXIMATION_RATIO);
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, 1);
			assertElement(pi, PathElementType.LINE_TO, 1.484375, 0.8125);
			assertElement(pi, PathElementType.LINE_TO, 1.9375, 0.75);
			assertElement(pi, PathElementType.LINE_TO, 2.359375, 0.8125);
			assertElement(pi, PathElementType.LINE_TO, 2.75, 1.0);
			assertElement(pi, PathElementType.LINE_TO, 3.109375, 1.3125);
			assertElement(pi, PathElementType.LINE_TO, 3.4375, 1.75);
			assertElement(pi, PathElementType.LINE_TO, 4.0, 3.0);
			assertElement(pi, PathElementType.LINE_TO, 4.0234375, 2.90807);
			assertElement(pi, PathElementType.LINE_TO, 4.046875, 2.819725);
			assertElement(pi, PathElementType.LINE_TO, 4.070313, 2.734895);
			assertElement(pi, PathElementType.LINE_TO, 4.09375, 2.6535034);
			assertElement(pi, PathElementType.LINE_TO, 4.11719, 2.5754766);
			assertElement(pi, PathElementType.LINE_TO, 4.14063, 2.5007401);
			assertElement(pi, PathElementType.LINE_TO, 4.16406, 2.4292192);
			assertElement(pi, PathElementType.LINE_TO, 4.1875, 2.3608398);
			assertElement(pi, PathElementType.LINE_TO, 4.234375, 2.233208);
			assertElement(pi, PathElementType.LINE_TO, 4.28125, 2.117249);
			assertElement(pi, PathElementType.LINE_TO, 4.328125, 2.012367);
			assertElement(pi, PathElementType.LINE_TO, 4.375, 1.917969);
			assertElement(pi, PathElementType.LINE_TO, 4.421875, 1.833458);
			assertElement(pi, PathElementType.LINE_TO, 4.46875, 1.758239);
			assertElement(pi, PathElementType.LINE_TO, 4.515625, 1.691719);
			assertElement(pi, PathElementType.LINE_TO, 4.5625, 1.6333);
			assertElement(pi, PathElementType.LINE_TO, 4.65625, 1.538391);
			assertElement(pi, PathElementType.LINE_TO, 4.75, 1.46875);
			assertElement(pi, PathElementType.LINE_TO, 4.84375, 1.419617);
			assertElement(pi, PathElementType.LINE_TO, 4.9375, 1.38623);
			assertElement(pi, PathElementType.LINE_TO, 5.03125, 1.363831);
			assertElement(pi, PathElementType.LINE_TO, 5.125, 1.347656);
			assertElement(pi, PathElementType.LINE_TO, 5.21875, 1.332947);
			assertElement(pi, PathElementType.LINE_TO, 5.3125, 1.314941);
			assertElement(pi, PathElementType.LINE_TO, 5.40625, 1.288879);
			assertElement(pi, PathElementType.LINE_TO, 5.5, 1.25);
			assertElement(pi, PathElementType.LINE_TO, 5.59375, 1.193542);
			assertElement(pi, PathElementType.LINE_TO, 5.6875, 1.114746);
			assertElement(pi, PathElementType.LINE_TO, 5.78125, 1.00885);
			assertElement(pi, PathElementType.LINE_TO, 5.828125, 0.944252);
			assertElement(pi, PathElementType.LINE_TO, 5.875, 0.871094);
			assertElement(pi, PathElementType.LINE_TO, 5.921875, 0.788780);
			assertElement(pi, PathElementType.LINE_TO, 5.96875, 0.696716);
			assertElement(pi, PathElementType.LINE_TO, 6.015625, 0.594307);
			assertElement(pi, PathElementType.LINE_TO, 6.0625, 0.480957);
			assertElement(pi, PathElementType.LINE_TO, 6.109375, 0.356071);
			assertElement(pi, PathElementType.LINE_TO, 6.15625, 0.219055);
			assertElement(pi, PathElementType.LINE_TO, 6.179688, 0.145812);
			assertElement(pi, PathElementType.LINE_TO, 6.203125, 0.069313);
			assertElement(pi, PathElementType.LINE_TO, 6.226563, -0.010516);
			assertElement(pi, PathElementType.LINE_TO, 6.25, -0.09375);
			assertElement(pi, PathElementType.LINE_TO, 6.273438, -0.180463);
			assertElement(pi, PathElementType.LINE_TO, 6.296875, -0.270729);
			assertElement(pi, PathElementType.LINE_TO, 6.320313, -0.364623);
			assertElement(pi, PathElementType.LINE_TO, 6.34375, -0.462219);
			assertElement(pi, PathElementType.LINE_TO, 6.36719, -0.563592);
			assertElement(pi, PathElementType.LINE_TO, 6.39063, -0.668816);
			assertElement(pi, PathElementType.LINE_TO, 6.41406, -0.7779646);
			assertElement(pi, PathElementType.LINE_TO, 6.4375, -0.891113);
			assertElement(pi, PathElementType.LINE_TO, 6.460938, -1.008336);
			assertElement(pi, PathElementType.LINE_TO, 6.484375, -1.129707);
			assertElement(pi, PathElementType.LINE_TO, 6.507813, -1.255301);
			assertElement(pi, PathElementType.LINE_TO, 6.53125, -1.385193);
			assertElement(pi, PathElementType.LINE_TO, 6.55469, -1.519456);
			assertElement(pi, PathElementType.LINE_TO, 6.57813, -1.658165);
			assertElement(pi, PathElementType.LINE_TO, 6.60156, -1.801394);
			assertElement(pi, PathElementType.LINE_TO, 6.625, -1.949219);
			assertElement(pi, PathElementType.LINE_TO, 6.63672, -2.024877);
			assertElement(pi, PathElementType.LINE_TO, 6.648438, -2.101712);
			assertElement(pi, PathElementType.LINE_TO, 6.660156, -2.179733);
			assertElement(pi, PathElementType.LINE_TO, 6.671875, -2.258949);
			assertElement(pi, PathElementType.LINE_TO, 6.683594, -2.33937);
			assertElement(pi, PathElementType.LINE_TO, 6.695313, -2.421);
			assertElement(pi, PathElementType.LINE_TO, 6.707031, -2.503862);
			assertElement(pi, PathElementType.LINE_TO, 6.71875, -2.587952);
			assertElement(pi, PathElementType.LINE_TO, 6.730469, -2.673283);
			assertElement(pi, PathElementType.LINE_TO, 6.742188, -2.759866);
			assertElement(pi, PathElementType.LINE_TO, 6.753906, -2.847709);
			assertElement(pi, PathElementType.LINE_TO, 6.765625, -2.936821);
			assertElement(pi, PathElementType.LINE_TO, 6.777344, -3.027212);
			assertElement(pi, PathElementType.LINE_TO, 6.789063, -3.118892);
			assertElement(pi, PathElementType.LINE_TO, 6.800781, -3.211869);
			assertElement(pi, PathElementType.LINE_TO, 6.8125, -3.306152);
			assertElement(pi, PathElementType.LINE_TO, 6.824219, -3.401752);
			assertElement(pi, PathElementType.LINE_TO, 6.835938, -3.498677);
			assertElement(pi, PathElementType.LINE_TO, 6.847656, -3.596937);
			assertElement(pi, PathElementType.LINE_TO, 6.859375, -3.696541);
			assertElement(pi, PathElementType.LINE_TO, 6.871094, -3.797498);
			assertElement(pi, PathElementType.LINE_TO, 6.882813, -3.899817);
			assertElement(pi, PathElementType.LINE_TO, 6.894531, -4.003509);
			assertElement(pi, PathElementType.LINE_TO, 6.90625, -4.108582);
			assertElement(pi, PathElementType.LINE_TO, 6.917969, -4.215045);
			assertElement(pi, PathElementType.LINE_TO, 6.929688, -4.322907);
			assertElement(pi, PathElementType.LINE_TO, 6.941406, -4.432179);
			assertElement(pi, PathElementType.LINE_TO, 6.953125, -4.54287);
			assertElement(pi, PathElementType.LINE_TO, 6.964844, -4.654987);
			assertElement(pi, PathElementType.LINE_TO, 6.976563, -4.768542);
			assertElement(pi, PathElementType.LINE_TO, 6.988281, -4.883543);
			assertElement(pi, PathElementType.LINE_TO, 7, -5);
			assertNoElement(pi);
		}
	
    	@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void getPathIteratorTransform2DDouble_translation(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Transform2D transform = new Transform2D();
			transform.setTranslation(10, 10);
			PathIterator2afp pi = getS().getPathIterator(transform, SPLINE_APPROXIMATION_RATIO);
			assertElement(pi, PathElementType.MOVE_TO, 10, 10);
			assertElement(pi, PathElementType.LINE_TO, 11, 11);
			assertElement(pi, PathElementType.LINE_TO, 11.484375, 10.8125);
			assertElement(pi, PathElementType.LINE_TO, 11.9375, 10.75);
			assertElement(pi, PathElementType.LINE_TO, 12.359375, 10.8125);
			assertElement(pi, PathElementType.LINE_TO, 12.75, 11.0);
			assertElement(pi, PathElementType.LINE_TO, 13.109375, 11.3125);
			assertElement(pi, PathElementType.LINE_TO, 13.4375, 11.75);
			assertElement(pi, PathElementType.LINE_TO, 14.0, 13.0);
			assertElement(pi, PathElementType.LINE_TO, 14.0234375, 12.90807);
			assertElement(pi, PathElementType.LINE_TO, 14.046875, 12.819725);
			assertElement(pi, PathElementType.LINE_TO, 14.070313, 12.734895);
			assertElement(pi, PathElementType.LINE_TO, 14.09375, 12.6535034);
			assertElement(pi, PathElementType.LINE_TO, 14.11719, 12.5754766);
			assertElement(pi, PathElementType.LINE_TO, 14.14063, 12.5007401);
			assertElement(pi, PathElementType.LINE_TO, 14.16406, 12.4292192);
			assertElement(pi, PathElementType.LINE_TO, 14.1875, 12.3608398);
			assertElement(pi, PathElementType.LINE_TO, 14.234375, 12.233208);
			assertElement(pi, PathElementType.LINE_TO, 14.28125, 12.117249);
			assertElement(pi, PathElementType.LINE_TO, 14.328125, 12.012367);
			assertElement(pi, PathElementType.LINE_TO, 14.375, 11.917969);
			assertElement(pi, PathElementType.LINE_TO, 14.421875, 11.833458);
			assertElement(pi, PathElementType.LINE_TO, 14.46875, 11.758239);
			assertElement(pi, PathElementType.LINE_TO, 14.515625, 11.691719);
			assertElement(pi, PathElementType.LINE_TO, 14.5625, 11.6333);
			assertElement(pi, PathElementType.LINE_TO, 14.65625, 11.538391);
			assertElement(pi, PathElementType.LINE_TO, 14.75, 11.46875);
			assertElement(pi, PathElementType.LINE_TO, 14.84375, 11.419617);
			assertElement(pi, PathElementType.LINE_TO, 14.9375, 11.38623);
			assertElement(pi, PathElementType.LINE_TO, 15.03125, 11.363831);
			assertElement(pi, PathElementType.LINE_TO, 15.125, 11.347656);
			assertElement(pi, PathElementType.LINE_TO, 15.21875, 11.332947);
			assertElement(pi, PathElementType.LINE_TO, 15.3125, 11.314941);
			assertElement(pi, PathElementType.LINE_TO, 15.40625, 11.288879);
			assertElement(pi, PathElementType.LINE_TO, 15.5, 11.25);
			assertElement(pi, PathElementType.LINE_TO, 15.59375, 11.193542);
			assertElement(pi, PathElementType.LINE_TO, 15.6875, 11.114746);
			assertElement(pi, PathElementType.LINE_TO, 15.78125, 11.00885);
			assertElement(pi, PathElementType.LINE_TO, 15.828125, 10.944252);
			assertElement(pi, PathElementType.LINE_TO, 15.875, 10.871094);
			assertElement(pi, PathElementType.LINE_TO, 15.921875, 10.788780);
			assertElement(pi, PathElementType.LINE_TO, 15.96875, 10.696716);
			assertElement(pi, PathElementType.LINE_TO, 16.015625, 10.594307);
			assertElement(pi, PathElementType.LINE_TO, 16.0625, 10.480957);
			assertElement(pi, PathElementType.LINE_TO, 16.109375, 10.356071);
			assertElement(pi, PathElementType.LINE_TO, 16.15625, 10.219055);
			assertElement(pi, PathElementType.LINE_TO, 16.179688, 10.145812);
			assertElement(pi, PathElementType.LINE_TO, 16.203125, 10.069313);
			assertElement(pi, PathElementType.LINE_TO, 16.226563, 10-0.010516);
			assertElement(pi, PathElementType.LINE_TO, 16.25, 10-0.09375);
			assertElement(pi, PathElementType.LINE_TO, 16.273438, 10-0.180463);
			assertElement(pi, PathElementType.LINE_TO, 16.296875, 10-0.270729);
			assertElement(pi, PathElementType.LINE_TO, 16.320313, 10-0.364623);
			assertElement(pi, PathElementType.LINE_TO, 16.34375, 10-0.462219);
			assertElement(pi, PathElementType.LINE_TO, 16.36719, 10-0.563592);
			assertElement(pi, PathElementType.LINE_TO, 16.39063, 10-0.668816);
			assertElement(pi, PathElementType.LINE_TO, 16.41406, 10-0.7779646);
			assertElement(pi, PathElementType.LINE_TO, 16.4375, 10-0.891113);
			assertElement(pi, PathElementType.LINE_TO, 16.460938, 10-1.008336);
			assertElement(pi, PathElementType.LINE_TO, 16.484375, 10-1.129707);
			assertElement(pi, PathElementType.LINE_TO, 16.507813, 10-1.255301);
			assertElement(pi, PathElementType.LINE_TO, 16.53125, 10-1.385193);
			assertElement(pi, PathElementType.LINE_TO, 16.55469, 10-1.519456);
			assertElement(pi, PathElementType.LINE_TO, 16.57813, 10-1.658165);
			assertElement(pi, PathElementType.LINE_TO, 16.60156, 10-1.801394);
			assertElement(pi, PathElementType.LINE_TO, 16.625, 10-1.949219);
			assertElement(pi, PathElementType.LINE_TO, 16.63672, 10-2.024877);
			assertElement(pi, PathElementType.LINE_TO, 16.648438, 10-2.101712);
			assertElement(pi, PathElementType.LINE_TO, 16.660156, 10-2.179733);
			assertElement(pi, PathElementType.LINE_TO, 16.671875, 10-2.258949);
			assertElement(pi, PathElementType.LINE_TO, 16.683594, 10-2.33937);
			assertElement(pi, PathElementType.LINE_TO, 16.695313, 10-2.421);
			assertElement(pi, PathElementType.LINE_TO, 16.707031, 10-2.503862);
			assertElement(pi, PathElementType.LINE_TO, 16.71875, 10-2.587952);
			assertElement(pi, PathElementType.LINE_TO, 16.730469, 10-2.673283);
			assertElement(pi, PathElementType.LINE_TO, 16.742188, 10-2.759866);
			assertElement(pi, PathElementType.LINE_TO, 16.753906, 10-2.847709);
			assertElement(pi, PathElementType.LINE_TO, 16.765625, 10-2.936821);
			assertElement(pi, PathElementType.LINE_TO, 16.777344, 10-3.027212);
			assertElement(pi, PathElementType.LINE_TO, 16.789063, 10-3.118892);
			assertElement(pi, PathElementType.LINE_TO, 16.800781, 10-3.211869);
			assertElement(pi, PathElementType.LINE_TO, 16.8125, 10-3.306152);
			assertElement(pi, PathElementType.LINE_TO, 16.824219, 10-3.401752);
			assertElement(pi, PathElementType.LINE_TO, 16.835938, 10-3.498677);
			assertElement(pi, PathElementType.LINE_TO, 16.847656, 10-3.596937);
			assertElement(pi, PathElementType.LINE_TO, 16.859375, 10-3.696541);
			assertElement(pi, PathElementType.LINE_TO, 16.871094, 10-3.797498);
			assertElement(pi, PathElementType.LINE_TO, 16.882813, 10-3.899817);
			assertElement(pi, PathElementType.LINE_TO, 16.894531, 10-4.003509);
			assertElement(pi, PathElementType.LINE_TO, 16.90625, 10-4.108582);
			assertElement(pi, PathElementType.LINE_TO, 16.917969, 10-4.215045);
			assertElement(pi, PathElementType.LINE_TO, 16.929688, 10-4.322907);
			assertElement(pi, PathElementType.LINE_TO, 16.941406, 10-4.432179);
			assertElement(pi, PathElementType.LINE_TO, 16.953125, 10-4.54287);
			assertElement(pi, PathElementType.LINE_TO, 16.964844, 10-4.654987);
			assertElement(pi, PathElementType.LINE_TO, 16.976563, 10-4.768542);
			assertElement(pi, PathElementType.LINE_TO, 16.988281, 10-4.883543);
			assertElement(pi, PathElementType.LINE_TO, 17, 5);
			assertNoElement(pi);
		}

    }

    @DisplayName("getLength")
	@Nested
	public class GetLength {

    	@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(14.71628, getS().getLength());
		}

    }

    @DisplayName("getLengthSquared")
	@Nested
	public class GetLengthSquared {

    	@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(216.56892, getS().getLengthSquared());
		}

    }

    @DisplayName("lineTo")
	@Nested
	public class LineTo {

    	@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertThrows(IllegalStateException.class, () -> {
				Path2afp<?, ?, ?, ?, ?, ?> tmpShape = createPath();
				tmpShape.lineTo(15, 145);
			});
		}

    	@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().lineTo(123.456, 456.789);
			PathIterator2afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, 1);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
			assertElement(pi, PathElementType.LINE_TO, 123.456, 456.789);
			assertNoElement(pi);
		}

    	@DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertThrows(IllegalStateException.class, () -> {
				Path2afp<?, ?, ?, ?, ?, ?> tmpShape = createPath();
				tmpShape.lineTo(createPoint(15, 145));
			});
		}
	
    	@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void lineToPoint2D(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().lineTo(createPoint(123.456, 456.789));
			PathIterator2afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, 1);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
			assertElement(pi, PathElementType.LINE_TO, 123.456, 456.789);
			assertNoElement(pi);
		}

    }

    @DisplayName("moveTo")
	@Nested
	public class MoveTo {

    	@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().moveTo(123.456, 456.789);
			PathIterator2afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, 1);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
			assertElement(pi, PathElementType.MOVE_TO, 123.456, 456.789);
			assertNoElement(pi);
		}
	
    	@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void moveToPoint2D(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().moveTo(createPoint(123.456, 456.789));
			PathIterator2afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, 1);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
			assertElement(pi, PathElementType.MOVE_TO, 123.456, 456.789);
			assertNoElement(pi);
		}

    }

    @DisplayName("quadTo")
	@Nested
	public class QuadTo {

    	@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertThrows(IllegalStateException.class, () -> {
				Path2afp<?, ?, ?, ?, ?, ?> tmpShape = createPath();
				tmpShape.quadTo(15, 145, 50, 20);
			});
		}
	
    	@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void quadToDoubleDoubleDoubleDouble(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().quadTo(123.456, 456.789, 789.123, 159.753);
			PathIterator2afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, 1);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
			assertElement(pi, PathElementType.QUAD_TO, 123.456,  456.789, 789.123, 159.753);
			assertNoElement(pi);
		}
		
    	@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void quadToPoint2DPoint2D_noMoveTo(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertThrows(IllegalStateException.class, () -> {
				Path2afp<?, ?, ?, ?, ?, ?> tmpShape = createPath();
				tmpShape.quadTo(createPoint(15, 145), createPoint(50, 20));
			});
		}
	
    	@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void quadToPoint2DPoint2D(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().quadTo(createPoint(123.456, 456.789), createPoint(789.123, 159.753));
			PathIterator2afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, 1);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
			assertElement(pi, PathElementType.QUAD_TO, 123.456,  456.789, 789.123, 159.753);
			assertNoElement(pi);
		}

    }

    @DisplayName("remove(double,double)")
	@Nested
	public class RemoveDoubleDouble {

    	@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().remove(5, -1));
			assertTrue(getS().getCurrentPoint().equals(createPoint(4, 3)));
			PathIterator2afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, 1);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertNoElement(pi);
    	}

    	@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().remove(1, 1));
			PathIterator2afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
			assertNoElement(pi);
    	}

    	@DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().remove(35, 35));
			PathIterator2afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, 1);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
			assertNoElement(pi);
		}

    }

    @DisplayName("setLastPoint")
	@Nested
	public class SetLastPoint {

    	@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getCurrentPoint().equals(createPoint(7, -5)));
			getS().setLastPoint(2, 2);
			assertTrue(getS().getCurrentPoint().equals(createPoint(2, 2)));
			PathIterator2afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, 1);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 2, 2);
			assertNoElement(pi);
		}
	
    	@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().getCurrentPoint().equals(createPoint(7, -5)));
			getS().setLastPoint(createPoint(2, 2));
			assertTrue(getS().getCurrentPoint().equals(createPoint(2, 2)));
			PathIterator2afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, 1);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 2, 2);
			assertNoElement(pi);
		}

    }

    @DisplayName("toCollection")
	@Nested
	public class ToCollection {

    	@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Collection<? extends Point2D> collection = getS().toCollection();
			assertEquals(7, collection.size());
			Iterator<? extends Point2D> iterator = collection.iterator();
			assertEpsilonEquals(createPoint(0, 0), iterator.next());
			assertEpsilonEquals(createPoint(1, 1), iterator.next());
			assertEpsilonEquals(createPoint(3, 0), iterator.next());
			assertEpsilonEquals(createPoint(4, 3), iterator.next());
			assertEpsilonEquals(createPoint(5, -1), iterator.next());
			assertEpsilonEquals(createPoint(6, 5), iterator.next());
			assertEpsilonEquals(createPoint(7, -5), iterator.next());
			assertFalse(iterator.hasNext());
		}

    }

    @DisplayName("transform(Transform2D)")
	@Nested
	public class TransformTransform2D {

    	@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Point2D p1 = randomPoint2d();
			Point2D p2 = randomPoint2d();
			Point2D p3 = randomPoint2d();
			Point2D p4 = randomPoint2d();
			Point2D p5 = randomPoint2d();
			Point2D p6 = randomPoint2d();
			Point2D p7 = randomPoint2d();
			
			Path2afp path = createPath();
			path.moveTo(p1.getX(),p1.getY());
			path.lineTo(p2.getX(),p2.getY());
			path.quadTo(p3.getX(),p3.getY(),p4.getX(),p4.getY());
			path.curveTo(p5.getX(),p5.getY(), p6.getX(),p6.getY(), p7.getX(),p7.getY());
			path.closePath();
			
			Transform2D trans = new Transform2D(randomMatrix3d());
			
			trans.transform(p1);
			trans.transform(p2);
			trans.transform(p3);
			trans.transform(p4);
			trans.transform(p5);
			trans.transform(p6);
			trans.transform(p7);
			
			Path2afp pathTrans = createPath();
			pathTrans.moveTo(p1.getX(),p1.getY());
			pathTrans.lineTo(p2.getX(),p2.getY());
			pathTrans.quadTo(p3.getX(),p3.getY(),p4.getX(),p4.getY());
			pathTrans.curveTo(p5.getX(),p5.getY(), p6.getX(),p6.getY(), p7.getX(),p7.getY());
			pathTrans.closePath();
			
			path.transform(trans);
			
			assertTrue(path.equalsToPathIterator(pathTrans.getPathIterator()));
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
			Path2afp clone = getS().clone();
			PathIterator2afp pi = (PathIterator2afp) clone.getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, 1);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
			assertNoElement(pi);
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
			assertFalse(getS().equals(createPath()));
    	}

    	@DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equals(createSegment(5, 8, 5, 10)));
    	}

    	@DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().equals(getS()));
    	}

    	@DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().equals(getS().clone()));
		}

    	@DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equals(createPath().getPathIterator()));
    	}

    	@DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equals(createSegment(5, 8, 5, 10).getPathIterator()));
    	}

    	@DisplayName("#9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().equals(getS().getPathIterator()));
    	}

    	@DisplayName("#10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().equals(getS().clone().getPathIterator()));
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
    		assertFalse(getS().equalsToPathIterator(createPath().getPathIterator()));
    	}

    	@DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertFalse(getS().equalsToPathIterator(createSegment(5, 8, 5, 10).getPathIterator()));
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
    		assertTrue(getS().equalsToPathIterator(getS().clone().getPathIterator()));
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
			assertFalse(getS().equalsToShape((T) createPath()));
    	}

    	@DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().equalsToShape(getS()));
    	}

    	@DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().equalsToShape(getS().clone()));
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
			PathIterator2afp pi = getS().getPathIterator();
			assertNoElement(pi);
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
			assertFalse(getS().contains(-5, 1));
    	}

    	@DisplayName("(double,double) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(3, 6));
    	}

    	@DisplayName("(double,double) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(3, -10));
    	}

    	@DisplayName("(double,double) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(11, 1));
    	}

    	@DisplayName("(double,double) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(4, 1));
    	}

    	@DisplayName("(double,double) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(4, 3));
    	}

    	@DisplayName("(double,double) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().contains(-5, 1));
    	}

    	@DisplayName("(double,double) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().contains(3, 6));
    	}

    	@DisplayName("(double,double) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().contains(3, -10));
    	}

    	@DisplayName("(double,double) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().contains(11, 1));
    	}

    	@DisplayName("(double,double) #11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().contains(4, 1));
    	}

    	@DisplayName("(double,double) #12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().contains(4, 3));
    	}

    	@DisplayName("(Point3D) #13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertFalse(getS().contains(createPoint(-5, 1)));
    	}

    	@DisplayName("(Point3D) #14")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_14(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertFalse(getS().contains(createPoint(3, 6)));
    	}

    	@DisplayName("(Point3D) #15")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_15(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertFalse(getS().contains(createPoint(3, -10)));
    	}

    	@DisplayName("(Point3D) #16")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_16(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertFalse(getS().contains(createPoint(11, 1)));
    	}

    	@DisplayName("(Point3D) #17")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_17(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertFalse(getS().contains(createPoint(4, 1)));
    	}

    	@DisplayName("(Point3D) #18")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_18(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertTrue(getS().contains(createPoint(4, 3)));
    	}

    	@DisplayName("(Point3D) #19")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_19(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		assertFalse(getS().contains(createPoint(-5, 1)));
    	}

    	@DisplayName("(Point3D) #20")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_20(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		assertFalse(getS().contains(createPoint(3, 6)));
    	}

    	@DisplayName("(Point3D) #21")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_21(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		assertFalse(getS().contains(createPoint(3, -10)));
    	}

    	@DisplayName("(Point3D) #22")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_22(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		assertFalse(getS().contains(createPoint(11, 1)));
    	}

    	@DisplayName("(Point3D) #23")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_23(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		assertTrue(getS().contains(createPoint(4, 1)));
    	}

    	@DisplayName("(Point3D) #24")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_24(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		assertTrue(getS().contains(createPoint(4, 3)));
    	}

		@DisplayName("(Rectangle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(-5, 1, 2, 1)));
    	}

		@DisplayName("(Rectangle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(3, 6, 2, 1)));
    	}

		@DisplayName("(Rectangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(3, -10, 2, 1)));
    	}

		@DisplayName("(Rectangle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(11, 1, 2, 1)));
    	}

		@DisplayName("(Rectangle2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    	}

		@DisplayName("(Rectangle2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    	}

		@DisplayName("(Rectangle2afp) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(3, 1, 2, 1)));
    	}

		@DisplayName("(Rectangle2afp) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(4, 3, 2, 1)));
    	}

		@DisplayName("(Rectangle2afp) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().contains(createRectangle(-5, 1, 2, 1)));
    	}

		@DisplayName("(Rectangle2afp) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().contains(createRectangle(3, 6, 2, 1)));
    	}

		@DisplayName("(Rectangle2afp) #11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().contains(createRectangle(3, -10, 2, 1)));
    	}

		@DisplayName("(Rectangle2afp) #12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().contains(createRectangle(11, 1, 2, 1)));
    	}

		@DisplayName("(Rectangle2afp) #13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
    	}

		@DisplayName("(Rectangle2afp) #14")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_14(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().contains(createRectangle(3, 0, 2, 1)));
    	}

		@DisplayName("(Rectangle2afp) #15")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_15(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().contains(createRectangle(4, 3, 2, 1)));
		}
    }

    @DisplayName("contains(Shape2afp)")
	@Nested
	public class ContainsShape {

		private Path2afp path1;
		
		private Path2afp path2;
		
		private Path2afp path3;

		private Segment2afp segment1;
		
		private Path2afp path4;
		
		private Path2afp path5;
		
		private Circle2afp circle1;

		private Path2afp path6;
		
		private Circle2afp circle2;
		
		private Path2afp path7;

		private Path2afp path00;

		private Segment2afp segment00;

		@BeforeEach
    	public void setUp() {
			path1 = createPath();
			path1.moveTo(204.0, 193.5);
			path1.lineTo(204.0, 85.5);
			path1.lineTo(268.0, 85.5);
			path1.lineTo(268.0, 149.5);
			path1.lineTo(388.0, 149.5);
			path1.lineTo(388.0, 193.5);
			path1.closePath();
			
			path2 = createPath();
			path2.moveTo(288.0, 145.5);
			path2.lineTo(388.0, 145.5);
			path2.lineTo(388.0, 93.5);
			path2.lineTo(288.0, 93.5);
			path2.closePath();
			
			path3 = createPath();
			path3.moveTo(292.0, 185.5);
			path3.lineTo(340.0, 185.5);
			path3.lineTo(340.0, 153.5);
			path3.lineTo(292.0, 153.5);
	
			segment1 = createSegment(372.0, 169.5, 372.0, 255.2);
			
			path4 = createPath();
			path4.moveTo(14.0, 285.5);
			path4.lineTo(74.0, 285.5);
			path4.lineTo(74.0, 139.5);
			path4.lineTo(180.0, 139.5);
			path4.lineTo(180.0, 285.5);
			path4.lineTo(224.0, 285.5);
			path4.lineTo(224.0, 139.5);
			path4.lineTo(390.0, 139.5);
			path4.lineTo(390.0, 49.5);
			path4.lineTo(14.0, 49.5);
			path4.closePath();
			
			path5 = createPath();
			path5.moveTo(228.0, 239.5);
			path5.lineTo(224.0, 239.5);
			path5.lineTo(224.0, 257.5);
			path5.lineTo(228.0, 257.5);
			path5.closePath();
			
			circle1 = createCircle(324.0, 93.5, 5.66);
	
			path6 = createPath();
			path6.moveTo(286.0, 131.5);
			path6.lineTo(286.0, 111.5);
			path6.arcTo(282.0, 111.5, 2.0, 2.0, 0.0, true, false);
			path6.lineTo(282.0, 131.5);
			path6.arcTo(286.0, 131.5, 2.0, 2.0, 0.0, true, false);
			path6.closePath();
			
			circle2 = createCircle(284.0, 133.5, 0.725);
			
			path7 = createPath();
			path7.moveTo(227.5, 239.5);
			path7.lineTo(223.5, 239.5);
			path7.lineTo(223.5, 257.5);
			path7.lineTo(227.5, 257.5);
			path7.closePath();

			path00 = createPath();
			path00.moveTo(168.0, 145.0);
			path00.lineTo(200.0, 200.0);
			path00.lineTo(200.0, 129.0);
			path00.lineTo(168.0, 129.0);
			path00.closePath();

			segment00 = createSegment(420.0, 297.0, 420.0, 0.0);
		}
    	
		@DisplayName("(Shape2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void shape_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(path1.contains(path2));
    	}
    	
		@DisplayName("(Shape2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void shape_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(path2.contains(path1));
    	}
    	
		@DisplayName("(Shape2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void shape_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(path1.contains(path3));
    	}
    	
		@DisplayName("(Shape2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void shape_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(path3.contains(path1));
    	}
    	
		@DisplayName("(Shape2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void shape_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(path1.contains(segment1));
    	}
    	
		@DisplayName("(Shape2afp) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void shape_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(segment1.contains(path1));
    	}
    	
		@DisplayName("(Shape2afp) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void shape_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(path4.contains(path5));
    	}
    	
		@DisplayName("(Shape2afp) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void shape_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(path5.contains(path4));
    	}
    	
		@DisplayName("(Shape2afp) #12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void shape_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(path4.contains(path7));
    	}
    	
		@DisplayName("(Shape2afp) #13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void shape_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(path7.contains(path4));
    	}
    	
		@DisplayName("(Shape2afp) #15")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void shape_15(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(path4.contains(circle1));
    	}
    	
		@DisplayName("(Shape2afp) #16")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void shape_16(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(circle1.contains(path4));
    	}
    	
		@DisplayName("(Shape2afp) #18")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void shape_18(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(path6.contains(circle2));
    	}
    	
		@DisplayName("(Shape2afp) #19")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void shape_19(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(circle2.contains(path6));
    	}
    	
		@DisplayName("(Shape2afp) #20")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void shape_20(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	        assertFalse(path00.contains(segment00));
    	}
    	
		@DisplayName("(Shape2afp) #21")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void shape_21(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(segment00.contains(path00));
		}

    }

    @DisplayName("getClosestPointTo")
	@Nested
	public class GetClosestPointTo {

		private Point2D result;

		@DisplayName("(Point2D) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createPoint(-2, 1));
			assertEpsilonEquals(0, result.getX());
			assertEpsilonEquals(0, result.getY());
		}

		@DisplayName("(Point2D) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createPoint(1, 0));
			assertEpsilonEquals(.5, result.getX());
			assertEpsilonEquals(.5, result.getY());
		}

		@DisplayName("(Point2D) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createPoint(3, 0));
			assertEpsilonEquals(2.56307, result.getX());
			assertEpsilonEquals(0.91027, result.getY());
		}

		@DisplayName("(Point2D) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getClosestPointTo(createPoint(1, -4));
			assertEpsilonEquals(0, result.getX());
			assertEpsilonEquals(0, result.getY());
		}

		@DisplayName("(Circle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createCircle(-5, 2, 1)));
		}

		@DisplayName("(Circle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5.6875, 1.11475, getS().getClosestPointTo(createCircle(10, 5, 1)));
		}

		@DisplayName("(Circle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(7, -5, getS().getClosestPointTo(createCircle(2, -10, 1)));
		}

		@DisplayName("(Circle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createCircle(2, 0, 1));
		}

		@DisplayName("(Circle2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(6.01612, 0.59312, getS().getClosestPointTo(createCircle(7, 1, 1)));
		}

		@DisplayName("(Segment2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createSegment(-6, -2, -5, -.5)));
		}

		@DisplayName("(Segment2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(2.0349, 0.76443, getS().getClosestPointTo(createSegment(0, 2, 2, 1)));
		}

		@DisplayName("(Segment2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createSegment(2, 0, 7, 1));
		}

		@DisplayName("(Triangle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createTestTriangle(-6, -2)));
		}

		@DisplayName("(Triangle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(6.01612, 0.59312, getS().getClosestPointTo(createTestTriangle(7, 1)));
		}

		@DisplayName("(Triangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createTestTriangle(4, 0));
		}

		@DisplayName("(Ellipse2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(1, 1, getS().getClosestPointTo(createEllipse(-4, 6, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5.92188, 0.78878, getS().getClosestPointTo(createEllipse(7, 1, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(2.88045, 1.11344, getS().getClosestPointTo(createEllipse(3, 0, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createEllipse(2, 0, 2, 1));
		}

		@DisplayName("(Rectangle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(1, 1, getS().getClosestPointTo(createRectangle(-4, 6, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(6.01612, 0.59312, getS().getClosestPointTo(createRectangle(7, 1, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(2.89236, 1.12379, getS().getClosestPointTo(createRectangle(3, 0, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRectangle(2, 0, 2, 1));
		}

		@DisplayName("(RoundRectangle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(1, 1, getS().getClosestPointTo(createRoundRectangle(-4, 6, 2, 1, .2, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5.99476, 0.63988, getS().getClosestPointTo(createRoundRectangle(7, 1, 2, 1, .2, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(2.91121, 1.14018, getS().getClosestPointTo(createRoundRectangle(3, 0, 2, 1, .2, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRoundRectangle(2, 0, 2, 1, .2, .1));
		}

		@DisplayName("(MultiShape2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void multishape_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(4, 3, getS().getClosestPointTo(createTestMultiShape(-4, 6)));
		}

		@DisplayName("(MultiShape2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void multishape_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(4, 3, getS().getClosestPointTo(createTestMultiShape(7, 1)));
		}

		@DisplayName("(MultiShape2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void multishape_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5.78125, 1.00885, getS().getClosestPointTo(createTestMultiShape(3, 0)));
		}

		@DisplayName("(OrientedRectangle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createTestOrientedRectangle(-4, 6)));
		}

		@DisplayName("(OrientedRectangle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5.59375, 1.19354, getS().getClosestPointTo(createTestOrientedRectangle(8, 3)));
		}

		@DisplayName("(OrientedRectangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createTestOrientedRectangle(3, 0));
		}

		@DisplayName("(Paralelogram2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(0, 0, getS().getClosestPointTo(createTestParallelogram(-4, 6)));
		}

		@DisplayName("(Paralelogram2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5.59375, 1.19354, getS().getClosestPointTo(createTestParallelogram(8, 3)));
		}

		@DisplayName("(Paralelogram2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createTestParallelogram(3, 0));
		}

		@DisplayName("(Path2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.EVEN_ODD);
			path.moveTo(0, 2);
			path.lineTo(2, 1);
			path.lineTo(0, 4);
			path.closePath();
			assertFpPointEquals(2.0349, 0.76443, getS().getClosestPointTo(path));
		}

		@DisplayName("(Path2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.EVEN_ODD);
			path.moveTo(-2, 2);
			path.lineTo(7, 5);
			path.lineTo(8, -6);
			path.lineTo(0, -4);
			path.closePath();
			assertClosestPointInBothShapes(getS(), path);
		}

		@DisplayName("(Path2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.EVEN_ODD);
			path.moveTo(-2, 2);
			path.lineTo(7, 5);
			path.lineTo(8, -6);
			path.lineTo(0, -1);
			path.lineTo(-1, 2);
			path.lineTo(5, 4);
			path.lineTo(7, 1);
			path.lineTo(7.5, -5.5);
			path.lineTo(0, -4);
			path.closePath();
			assertFpPointEquals(0, 0, getS().getClosestPointTo(path));
		}

    }

    @DisplayName("getFarthestPointTo")
	@Nested
	public class GetFarthestPointTo {

		private Point2D result;

		@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getFarthestPointTo(createPoint(-2, 1));
			assertEpsilonEquals(7, result.getX());
			assertEpsilonEquals(-5, result.getY());
		}

		@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getFarthestPointTo(createPoint(1, 0));
			assertEpsilonEquals(7, result.getX());
			assertEpsilonEquals(-5, result.getY());
		}

		@DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getFarthestPointTo(createPoint(3, 0));
			assertEpsilonEquals(7, result.getX());
			assertEpsilonEquals(-5, result.getY());
		}

		@DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			result = getS().getFarthestPointTo(createPoint(1, -4));
			assertEpsilonEquals(4, result.getX());
			assertEpsilonEquals(3, result.getY());
		}

    }

    @DisplayName("getDistance")
	@Nested
	public class GetDistance {

		@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2.23607, getS().getDistance(createPoint(-2, 1)));
		}

		@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.70711, getS().getDistance(createPoint(1, 0)));
		}

		@DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.00970, getS().getDistance(createPoint(3, 0)));
		}

		@DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4.12311, getS().getDistance(createPoint(1, -4)));
		}

		@DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertEpsilonEquals(2.23606, getS().getDistance(createPoint(-2, 1)));
		}

		@DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertEpsilonEquals(0, getS().getDistance(createPoint(1, 0)));
		}

		@DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertEpsilonEquals(0, getS().getDistance(createPoint(3, 0)));
		}

		@DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertEpsilonEquals(2.6737, getS().getDistance(createPoint(1, -4)));
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
			assertEpsilonEquals(5, getS().getDistanceSquared(createPoint(-2, 1)));
		}

		@DisplayName("(Point2D) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.5, getS().getDistanceSquared(createPoint(1, 0)));
		}

		@DisplayName("(Point2D) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.0195, getS().getDistanceSquared(createPoint(3, 0)));
		}

		@DisplayName("(Point2D) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(17, getS().getDistanceSquared(createPoint(1, -4)));
		}

		@DisplayName("(Point2D) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertEpsilonEquals(5, getS().getDistanceSquared(createPoint(-2, 1)));
		}

		@DisplayName("(Point2D) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertEpsilonEquals(0, getS().getDistanceSquared(createPoint(1, 0)));
		}

		@DisplayName("(Point2D) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertEpsilonEquals(0, getS().getDistanceSquared(createPoint(3, 0)));
		}

		@DisplayName("(Point2D) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertEpsilonEquals(7.14865, getS().getDistanceSquared(createPoint(1, -4)));
		}

		@DisplayName("(Circle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(19.22967, getS().getDistanceSquared(createCircle(-5, 2, 1)));
		}

		@DisplayName("(Circle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(23.08372, getS().getDistanceSquared(createCircle(10, 5, 1)));
		}

		@DisplayName("(Circle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(36.85786, getS().getDistanceSquared(createCircle(2, -10, 1)));
		}

		@DisplayName("(Circle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createCircle(2, 0, 1)));
		}

		@DisplayName("(Circle2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.0041857, getS().getDistanceSquared(createCircle(7, 1, 1)));
		}

		@DisplayName("(Segment2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(25.25, getS().getDistanceSquared(createSegment(-6, -2, -5, -.5)));
		}

		@DisplayName("(Segment2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.05671, getS().getDistanceSquared(createSegment(0, 2, 2, 1)));
		}

		@DisplayName("(Segment2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createSegment(2, 0, 7, 1)));
		}

		@DisplayName("(Triangele2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(10, getS().getDistanceSquared(createTestTriangle(-6, -2)));
		}

		@DisplayName("(Triangele2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.13358, getS().getDistanceSquared(createTestTriangle(7, 1)));
		}

		@DisplayName("(Triangele2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createTestTriangle(4, 0)));
		}

		@DisplayName("(Ellipse2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(37.2745, getS().getDistanceSquared(createEllipse(-4, 6, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.57546, getS().getDistanceSquared(createEllipse(7, 1, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.19513, getS().getDistanceSquared(createEllipse(3, 0, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createEllipse(2, 0, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(34, getS().getDistanceSquared(createRectangle(-4, 6, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.13358, getS().getDistanceSquared(createRectangle(7, 1, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.026911, getS().getDistanceSquared(createRectangle(3, 0, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRectangle(2, 0, 2, 1)));
		}

		@DisplayName("(RoundRectangle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(34.64138, getS().getDistanceSquared(createRoundRectangle(-4, 6, 2, 1, .2, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.21231, getS().getDistanceSquared(createRoundRectangle(7, 1, 2, 1, .2, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.049032, getS().getDistanceSquared(createRoundRectangle(3, 0, 2, 1, .2, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRoundRectangle(2, 0, 2, 1, .2, .1)));
		}

		@DisplayName("(MultiShape2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void multishape_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4, getS().getDistanceSquared(createTestMultiShape(-4, 6)));
		}

		@DisplayName("(MultiShape2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void multishape_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3.34315, getS().getDistanceSquared(createTestMultiShape(7, 1)));
		}

		@DisplayName("(MultiShape2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void multishape_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2.46773, getS().getDistanceSquared(createTestMultiShape(3, 0)));
		}

		@DisplayName("(OrientedRectangle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(32.59319, getS().getDistanceSquared(createTestOrientedRectangle(-4, 6)));
		}

		@DisplayName("(OrientedRectangle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.70193, getS().getDistanceSquared(createTestOrientedRectangle(8, 3)));
		}

		@DisplayName("(OrientedRectangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createTestOrientedRectangle(3, 0)));
		}

		@DisplayName("(Parallelogram2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(33.88908, getS().getDistanceSquared(createTestParallelogram(-4, 6)));
		}

		@DisplayName("(Parallelogram2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.25487, getS().getDistanceSquared(createTestParallelogram(8, 3)));
		}

		@DisplayName("(Parallelogram2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createTestParallelogram(3, 0)));
		}

		@DisplayName("(Path2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.EVEN_ODD);
			path.moveTo(0, 2);
			path.lineTo(2, 1);
			path.lineTo(0, 4);
			path.closePath();
			assertEpsilonEquals(0.05671, getS().getDistanceSquared(path));
		}

		@DisplayName("(Path2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.EVEN_ODD);
			path.moveTo(-2, 2);
			path.lineTo(7, 5);
			path.lineTo(8, -6);
			path.lineTo(0, -4);
			path.closePath();
			assertEpsilonEquals(0, getS().getDistanceSquared(path));
		}

		@DisplayName("(Path2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var path = createPath(PathWindingRule.EVEN_ODD);
			path.moveTo(-2, 2);
			path.lineTo(7, 5);
			path.lineTo(8, -6);
			path.lineTo(0, -1);
			path.lineTo(-1, 2);
			path.lineTo(5, 4);
			path.lineTo(7, 1);
			path.lineTo(7.5, -5.5);
			path.lineTo(0, -4);
			path.closePath();
			assertEpsilonEquals(0.1, getS().getDistanceSquared(path));
		}

    }

    @DisplayName("getDistanceL1")
	@Nested
	public class GetDistanceL1 {

		@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3, getS().getDistanceL1(createPoint(-2, 1)));
		}

		@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, getS().getDistanceL1(createPoint(1, 0)));
		}

		@DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.3472, getS().getDistanceL1(createPoint(3, 0)));
		}

		@DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5, getS().getDistanceL1(createPoint(1, -4)));
		}

		@DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertEpsilonEquals(3, getS().getDistanceL1(createPoint(-2, 1)));
		}

		@DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertEpsilonEquals(0, getS().getDistanceL1(createPoint(1, 0)));
		}

		@DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertEpsilonEquals(0, getS().getDistanceL1(createPoint(3, 0)));
		}

		@DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertEpsilonEquals(3.72973, getS().getDistanceL1(createPoint(1, -4)));
		}

    }

    @DisplayName("getDistanceLinf")
	@Nested
	public class GetDistanceLinf {

		@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2, getS().getDistanceLinf(createPoint(-2, 1)));
		}

		@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.5, getS().getDistanceLinf(createPoint(1, 0)));
		}

		@DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.91027, getS().getDistanceLinf(createPoint(3, 0)));
		}

		@DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4, getS().getDistanceLinf(createPoint(1, -4)));
		}

		@DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertEpsilonEquals(2, getS().getDistanceLinf(createPoint(-2, 1)));
		}

		@DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertEpsilonEquals(0, getS().getDistanceLinf(createPoint(1, 0)));
		}

		@DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertEpsilonEquals(0, getS().getDistanceLinf(createPoint(3, 0)));
		}

		@DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertEpsilonEquals(2.17568, getS().getDistanceLinf(createPoint(1, -4)));
		}

    }

    @DisplayName("set(IT)")
	@Nested
	public class SeTIT {

		@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set((T) createPath());
			PathIterator2afp pi = getS().getPathIterator();
			assertNoElement(pi);
		}

		@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Path2afp path = createPath();
			path.moveTo(123.456, 456.789);
			path.lineTo(789.123, 159.753);
			getS().set(path);
			var pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 123.456, 456.789);
			assertElement(pi, PathElementType.LINE_TO, 789.123, 159.753);
			assertNoElement(pi);
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
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, 1);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
			assertNoElement(pi);
		}

		@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			var pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, 1);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
			assertElement(pi, PathElementType.CLOSE, 0, 0);
			assertNoElement(pi);
		}

    }

    @DisplayName("getPathIterator(Transform2D)")
	@Nested
	public class GetPathIteratorTransform2D {

		private Transform2D transform;

		@BeforeEach
		public void setUp() {
			transform = new Transform2D();
		}

		@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var pi = getS().getPathIterator(null);
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, 1);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
			assertNoElement(pi);
		}

		@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			transform.setIdentity();
			var pi = getS().getPathIterator(transform);
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, 1);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
			assertNoElement(pi);
		}

		@DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			transform.setTranslation(14, -5);
			var pi = getS().getPathIterator(transform);
			assertElement(pi, PathElementType.MOVE_TO, 14, -5);
			assertElement(pi, PathElementType.LINE_TO, 15, -4);
			assertElement(pi, PathElementType.QUAD_TO, 17, -5, 18, -2);
			assertElement(pi, PathElementType.CURVE_TO, 19, -6, 20, 0, 21, -10);
			assertNoElement(pi);
		}

		@DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			var pi = getS().getPathIterator(null);
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, 1);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
			assertElement(pi, PathElementType.CLOSE, 0, 0);
			assertNoElement(pi);
		}

		@DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			transform.setIdentity();
			var pi = getS().getPathIterator(transform);
			assertElement(pi, PathElementType.MOVE_TO, 0, 0);
			assertElement(pi, PathElementType.LINE_TO, 1, 1);
			assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
			assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
			assertElement(pi, PathElementType.CLOSE, 0, 0);
			assertNoElement(pi);
		}

		@DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			transform.setTranslation(14, -5);
			var pi = getS().getPathIterator(transform);
			assertElement(pi, PathElementType.MOVE_TO, 14, -5);
			assertElement(pi, PathElementType.LINE_TO, 15, -4);
			assertElement(pi, PathElementType.QUAD_TO, 17, -5, 18, -2);
			assertElement(pi, PathElementType.CURVE_TO, 19, -6, 20, 0, 21, -10);
			assertElement(pi, PathElementType.CLOSE, 14, -5);
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
			Point2D p1 = randomPoint2d();
			Point2D p2 = randomPoint2d();
			Point2D p3 = randomPoint2d();
			Point2D p4 = randomPoint2d();
			Point2D p5 = randomPoint2d();
			Point2D p6 = randomPoint2d();
			Point2D p7 = randomPoint2d();
			
			Path2afp path = createPath();
			path.moveTo(p1.getX(),p1.getY());
			path.lineTo(p2.getX(),p2.getY());
			path.quadTo(p3.getX(),p3.getY(),p4.getX(),p4.getY());
			path.curveTo(p5.getX(),p5.getY(), p6.getX(),p6.getY(), p7.getX(),p7.getY());
			path.closePath();
	
			Transform2D trans = new Transform2D(randomMatrix3d());
			Path2afp transformedShape = (Path2afp) path.createTransformedShape(trans);
			path.transform(trans);		
		
			assertTrue(path.equalsToShape(transformedShape));
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
			double dx = getRandom().nextDouble()*20;
			double dy = getRandom().nextDouble()*20;
			
			Path2afp p2 = createPath();
			p2.moveTo(dx, dy);
			p2.lineTo(1 + dx, 1 + dy);
			p2.quadTo(3 + dx, 0 + dy, 4 + dx, 3 + dy);
			p2.curveTo(5 + dx, -1 + dy, 6 + dx, 5 + dy, 7 + dx, -5 + dy);
			
			getS().translate(dx, dy);
			
			assertTrue(getS().equals(p2));		
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
			double dx = getRandom().nextDouble()*20;
			double dy = getRandom().nextDouble()*20;
			
			getS().translate(dx, dy);
			
			PathIterator2afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, dx, dy);
			assertElement(pi, PathElementType.LINE_TO, dx + 1, dy + 1);
			assertElement(pi, PathElementType.QUAD_TO, dx + 3, dy, dx + 4, dy + 3);
			assertElement(pi, PathElementType.CURVE_TO, dx + 5, dy - 1, dx + 6, dy + 5, dx + 7, dy - 5);
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
			assertEpsilonEquals(0, box.getMinX());
			assertEpsilonEquals(-5, box.getMinY());
			assertEpsilonEquals(7, box.getMaxX());
			assertEpsilonEquals(3, box.getMaxY());
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
			assertEpsilonEquals(0, box.getMinX());
			assertEpsilonEquals(-5, box.getMinY());
			assertEpsilonEquals(7, box.getMaxX());
			assertEpsilonEquals(3, box.getMaxY());
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
    		assertFalse(getS().intersects(createRectangle(1, -2, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertTrue(getS().intersects(createRectangle(1.5, 1.5, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertFalse(getS().intersects(createRectangle(7, 3, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertFalse(getS().intersects(createRectangle(-4, -0.5, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		assertTrue(getS().intersects(createRectangle(1, -2, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		assertTrue(getS().intersects(createRectangle(1.5, 1.5, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		assertFalse(getS().intersects(createRectangle(7, 3, 2, 1)));
		}

		@DisplayName("(Rectangle2afp) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		assertFalse(getS().intersects(createRectangle(-4, -0.5, 2, 1)));
		}

		@DisplayName("(Circle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertFalse(getS().intersects(createCircle(-2, -2, 2)));
		}

		@DisplayName("(Circle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertFalse(getS().intersects(createCircle(2, -2, 2)));
		}

		@DisplayName("(Circle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertFalse(getS().intersects(createCircle(2.5, -1.5, 2)));
		}

		@DisplayName("(Circle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertFalse(getS().intersects(createCircle(10, 0, 2)));
		}

		@DisplayName("(Circle2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertFalse(getS().intersects(createCircle(4, 0, 0.5)));
		}

		@DisplayName("(Circle2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertTrue(getS().intersects(createCircle(2.5, 1, 0.5)));
		}

		@DisplayName("(Circle2afp) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		assertFalse(getS().intersects(createCircle(-2, -2, 2)));
		}

		@DisplayName("(Circle2afp) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		assertTrue(getS().intersects(createCircle(2, -2, 2)));
		}

		@DisplayName("(Circle2afp) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		assertTrue(getS().intersects(createCircle(2.5, -1.5, 2)));
		}

		@DisplayName("(Circle2afp) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		assertFalse(getS().intersects(createCircle(10, 0, 2)));
		}

		@DisplayName("(Circle2afp) #11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		assertTrue(getS().intersects(createCircle(4, 0, 0.5)));
		}

		@DisplayName("(Circle2afp) #12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		assertTrue(getS().intersects(createCircle(2.5, 1, 0.5)));
		}

		@DisplayName("(Triangle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertFalse(getS().intersects(createTriangle(1, -1, 4, 0, 2, .5)));
		}

		@DisplayName("(Triangle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertFalse(getS().intersects(createTriangle(9, 1, 12, 2, 10, 1.5)));
		}

		@DisplayName("(Triangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertTrue(getS().intersects(createTriangle(5, 0, 8, 1, 6, .5)));
		}

		@DisplayName("(Triangle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertFalse(getS().intersects(createTriangle(-1, -4, 2, -3, 0, -2.5)));
		}

		@DisplayName("(Triangle2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertFalse(getS().intersects(createTriangle(3, -6, 6, -5, 4, -4.5)));
		}

		@DisplayName("(Triangle2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		assertTrue(getS().intersects(createTriangle(1, -1, 4, 0, 2, .5)));
		}

		@DisplayName("(Triangle2afp) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		assertFalse(getS().intersects(createTriangle(9, 1, 12, 2, 10, 1.5)));
		}

		@DisplayName("(Triangle2afp) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		assertTrue(getS().intersects(createTriangle(5, 0, 8, 1, 6, .5)));
		}

		@DisplayName("(Triangle2afp) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		assertFalse(getS().intersects(createTriangle(-1, -4, 2, -3, 0, -2.5)));
		}

		@DisplayName("(Triangle2afp) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		assertFalse(getS().intersects(createTriangle(3, -6, 6, -5, 4, -4.5)));
		}

		@DisplayName("(Ellipse2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertFalse(getS().intersects(createEllipse(1, -1.5, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertFalse(getS().intersects(createEllipse(1, 1, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertTrue(getS().intersects(createEllipse(4.5, -1, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertFalse(getS().intersects(createEllipse(0, -5.5, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		assertTrue(getS().intersects(createEllipse(1, -1.5, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		assertFalse(getS().intersects(createEllipse(1, 1, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		assertTrue(getS().intersects(createEllipse(4.5, -1, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		assertFalse(getS().intersects(createEllipse(0, -5.5, 2, 1)));
		}

		@DisplayName("(Segment2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertFalse(getS().intersects(createSegment(1, -1, 2, -3)));
		}

		@DisplayName("(Segment2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertFalse(getS().intersects(createSegment(1, -6, 2, -3)));
		}

		@DisplayName("(Segment2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertFalse(getS().intersects(createSegment(4, 0, 2, -3)));
		}

		@DisplayName("(Segment2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertTrue(getS().intersects(createSegment(4, 0, 5, 3)));
		}

		@DisplayName("(Segment2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		assertFalse(getS().intersects(createSegment(1, -1, 2, -3)));
		}

		@DisplayName("(Segment2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		assertFalse(getS().intersects(createSegment(1, -6, 2, -3)));
		}

		@DisplayName("(Segment2afp) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		assertTrue(getS().intersects(createSegment(4, 0, 2, -3)));
		}

		@DisplayName("(Segment2afp) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		assertTrue(getS().intersects(createSegment(4, 0, 5, 3)));
		}

		@DisplayName("(Path2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertFalse(getS().intersects(createPolyline(1, -1, 4, -3)));
		}

		@DisplayName("(Path2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertFalse(getS().intersects(createPolyline(1, -1, 5, -3)));
		}

		@DisplayName("(Path2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertFalse(getS().intersects(createPolyline(1, -1, 4, 1)));
		}

		@DisplayName("(Path2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertTrue(getS().intersects(createPolyline(5, 2, 4, 1)));
		}

		@DisplayName("(Path2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		assertFalse(getS().intersects(createPolyline(1, -1, 4, -3)));
		}

		@DisplayName("(Path2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		assertTrue(getS().intersects(createPolyline(1, -1, 5, -3)));
		}

		@DisplayName("(Path2afp) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		assertTrue(getS().intersects(createPolyline(1, -1, 4, 1)));
		}

		@DisplayName("(Path2afp) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		assertTrue(getS().intersects(createPolyline(5, 2, 4, 1)));
		}

		@DisplayName("(PathIterator2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void pathiterator_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertFalse(getS().intersects(createPolyline(1, -1, 4, -3).getPathIterator()));
		}

		@DisplayName("(PathIterator2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void pathiterator_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertFalse(getS().intersects(createPolyline(1, -1, 5, -3).getPathIterator()));
		}

		@DisplayName("(PathIterator2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void pathiterator_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertFalse(getS().intersects(createPolyline(1, -1, 4, 1).getPathIterator()));
		}

		@DisplayName("(PathIterator2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void pathiterator_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertTrue(getS().intersects(createPolyline(5, 2, 4, 1).getPathIterator()));
		}

		@DisplayName("(PathIterator2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void pathiterator_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		assertFalse(getS().intersects(createPolyline(1, -1, 4, -3).getPathIterator()));
		}

		@DisplayName("(PathIterator2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void pathiterator_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		assertTrue(getS().intersects(createPolyline(1, -1, 5, -3).getPathIterator()));
		}

		@DisplayName("(PathIterator2afp) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void pathiterator_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
		}

		@DisplayName("(PathIterator2afp) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void pathiterator_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		assertTrue(getS().intersects(createPolyline(1, -1, 4, 1).getPathIterator()));
    		assertTrue(getS().intersects(createPolyline(5, 2, 4, 1).getPathIterator()));
		}

		@DisplayName("(OrientedRectangle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertFalse(getS().intersects(createOrientedRectangle(0, -5, 0.5547, 0.83205, 2, 1)));
		}

		@DisplayName("(OrientedRectangle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertFalse(getS().intersects(createOrientedRectangle(4, -1, 0.5547, 0.83205, 2, 1)));
		}

		@DisplayName("(OrientedRectangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertFalse(getS().intersects(createOrientedRectangle(6, 5, 0.5547, 0.83205, 2, 1)));
		}

		@DisplayName("(OrientedRectangle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertTrue(getS().intersects(createOrientedRectangle(7, 2, 0.5547, 0.83205, 2, 1)));
		}

		@DisplayName("(OrientedRectangle2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertTrue(getS().intersects(createOrientedRectangle(-1, -1, 0.5547, 0.83205, 2, 1)));
		}

		@DisplayName("(Parallelogram2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertFalse(getS().intersects(createParallelogram(0, -5, 0.5547, 0.83205, 2, -0.83205, 0.5547, 1)));
		}

		@DisplayName("(Parallelogram2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertFalse(getS().intersects(createParallelogram(4, -1, 0.5547, 0.83205, 2, -0.83205, 0.5547, 1)));
		}

		@DisplayName("(Parallelogram2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertFalse(getS().intersects(createParallelogram(6, 5, 0.5547, 0.83205, 2, -0.83205, 0.5547, 1)));
		}

		@DisplayName("(Parallelogram2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertTrue(getS().intersects(createParallelogram(7, 2, 0.5547, 0.83205, 2, -0.83205, 0.5547, 1)));
		}

		@DisplayName("(Parallelogram2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertTrue(getS().intersects(createParallelogram(-1, -1, 0.5547, 0.83205, 2, -0.83205, 0.5547, 1)));
		}

		@DisplayName("(RoundRectangle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertFalse(getS().intersects(createRoundRectangle(1, -2, 2, 1, .2, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertTrue(getS().intersects(createRoundRectangle(1.5, 1.5, 2, 1, .2, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertFalse(getS().intersects(createRoundRectangle(7, 3, 2, 1, .2, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertFalse(getS().intersects(createRoundRectangle(-4, -0.5, 2, 1, .2, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		assertTrue(getS().intersects(createRoundRectangle(1, -2, 2, 1, .2, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		assertTrue(getS().intersects(createRoundRectangle(1.5, 1.5, 2, 1, .2, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		assertFalse(getS().intersects(createRoundRectangle(7, 3, 2, 1, .2, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		getS().closePath();
    		assertFalse(getS().intersects(createRoundRectangle(-4, -0.5, 2, 1, .2, .1)));
		}

		@DisplayName("(Shape2D) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void shape_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertTrue(getS().intersects((Shape2D) createSegment(4, 0, 5, 3)));
		}

		@DisplayName("(Shape2D) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void shape_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
    		assertTrue(getS().intersects((Shape2D)createRectangle(1.5, 1.5, 2, 1)));
    	}

    }

    @DisplayName("intersects(Shape2afp)")
	@Nested
	public class IntersectsShape {

		private Path2afp path1;
		
		private Path2afp path2;
		
		private Path2afp path3;

		private Segment2afp segment1;
		
		private Path2afp path4;
		
		private Path2afp path5;
		
		private Circle2afp circle1;

		private Path2afp path6;
		
		private Circle2afp circle2;
		
		private Path2afp path7;

		private Path2afp path00;

		private Segment2afp segment00;

		@BeforeEach
    	public void setUp() {
			path1 = createPath();
			path1.moveTo(204.0, 193.5);
			path1.lineTo(204.0, 85.5);
			path1.lineTo(268.0, 85.5);
			path1.lineTo(268.0, 149.5);
			path1.lineTo(388.0, 149.5);
			path1.lineTo(388.0, 193.5);
			path1.closePath();
			
			path2 = createPath();
			path2.moveTo(288.0, 145.5);
			path2.lineTo(388.0, 145.5);
			path2.lineTo(388.0, 93.5);
			path2.lineTo(288.0, 93.5);
			path2.closePath();
			
			path3 = createPath();
			path3.moveTo(292.0, 185.5);
			path3.lineTo(340.0, 185.5);
			path3.lineTo(340.0, 153.5);
			path3.lineTo(292.0, 153.5);
	
			segment1 = createSegment(372.0, 169.5, 372.0, 255.2);
			
			path4 = createPath();
			path4.moveTo(14.0, 285.5);
			path4.lineTo(74.0, 285.5);
			path4.lineTo(74.0, 139.5);
			path4.lineTo(180.0, 139.5);
			path4.lineTo(180.0, 285.5);
			path4.lineTo(224.0, 285.5);
			path4.lineTo(224.0, 139.5);
			path4.lineTo(390.0, 139.5);
			path4.lineTo(390.0, 49.5);
			path4.lineTo(14.0, 49.5);
			path4.closePath();
			
			path5 = createPath();
			path5.moveTo(228.0, 239.5);
			path5.lineTo(224.0, 239.5);
			path5.lineTo(224.0, 257.5);
			path5.lineTo(228.0, 257.5);
			path5.closePath();
			
			circle1 = createCircle(324.0, 93.5, 5.66);
	
			path6 = createPath();
			path6.moveTo(286.0, 131.5);
			path6.lineTo(286.0, 111.5);
			path6.arcTo(282.0, 111.5, 2.0, 2.0, 0.0, true, false);
			path6.lineTo(282.0, 131.5);
			path6.arcTo(286.0, 131.5, 2.0, 2.0, 0.0, true, false);
			path6.closePath();
			
			circle2 = createCircle(284.0, 133.5, 0.725);
			
			path7 = createPath();
			path7.moveTo(227.5, 239.5);
			path7.lineTo(223.5, 239.5);
			path7.lineTo(223.5, 257.5);
			path7.lineTo(227.5, 257.5);
			path7.closePath();

			path00 = createPath();
			path00.moveTo(168.0, 145.0);
			path00.lineTo(200.0, 200.0);
			path00.lineTo(200.0, 129.0);
			path00.lineTo(168.0, 129.0);
			path00.closePath();

			segment00 = createSegment(420.0, 297.0, 420.0, 0.0);
		}
    	
		@DisplayName("(Shape2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void shape_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(path1.intersects(segment1));
    	}

		@DisplayName("(Shape2afp) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void shape_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(path4.intersects(path5));
    	}

		@DisplayName("(Shape2afp) #11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void shape_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(path4.intersects(path7));
    	}
    	
		@DisplayName("(Shape2afp) #14")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void shape_14(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(path4.intersects(circle1));
    	}
    	
		@DisplayName("(Shape2afp) #17")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void shape_17(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(path6.intersects(circle2));
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
			double dx = getRandom().nextDouble()*20;
			double dy = getRandom().nextDouble()*20;
			
			getS().operator_add(createVector(dx, dy));
			
			PathIterator2afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, dx, dy);
			assertElement(pi, PathElementType.LINE_TO, dx + 1, dy + 1);
			assertElement(pi, PathElementType.QUAD_TO, dx + 3, dy, dx + 4, dy + 3);
			assertElement(pi, PathElementType.CURVE_TO, dx + 5, dy - 1, dx + 6, dy + 5, dx + 7, dy - 5);
			assertNoElement(pi);
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
			double dx = getRandom().nextDouble()*20;
			double dy = getRandom().nextDouble()*20;
			
			T shape = getS().operator_plus(createVector(dx, dy));
			
			PathIterator2afp pi = shape.getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, dx, dy);
			assertElement(pi, PathElementType.LINE_TO, dx + 1, dy + 1);
			assertElement(pi, PathElementType.QUAD_TO, dx + 3, dy, dx + 4, dy + 3);
			assertElement(pi, PathElementType.CURVE_TO, dx + 5, dy - 1, dx + 6, dy + 5, dx + 7, dy - 5);
			assertNoElement(pi);
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
			double dx = getRandom().nextDouble()*20;
			double dy = getRandom().nextDouble()*20;
			
			getS().operator_remove(createVector(dx, dy));
			
			PathIterator2afp pi = getS().getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, -dx, -dy);
			assertElement(pi, PathElementType.LINE_TO, -dx + 1, -dy + 1);
			assertElement(pi, PathElementType.QUAD_TO, -dx + 3, -dy, -dx + 4, -dy + 3);
			assertElement(pi, PathElementType.CURVE_TO, -dx + 5, -dy - 1, -dx + 6, -dy + 5, -dx + 7, -dy - 5);
			assertNoElement(pi);
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
			double dx = getRandom().nextDouble()*20;
			double dy = getRandom().nextDouble()*20;
			
			T shape = getS().operator_minus(createVector(dx, dy));
			
			PathIterator2afp pi = shape.getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, -dx, -dy);
			assertElement(pi, PathElementType.LINE_TO, -dx + 1, -dy + 1);
			assertElement(pi, PathElementType.QUAD_TO, -dx + 3, -dy, -dx + 4, -dy + 3);
			assertElement(pi, PathElementType.CURVE_TO, -dx + 5, -dy - 1, -dx + 6, -dy + 5, -dx + 7, -dy - 5);
			assertNoElement(pi);
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
			Point2D p1 = randomPoint2d();
			Point2D p2 = randomPoint2d();
			Point2D p3 = randomPoint2d();
			Point2D p4 = randomPoint2d();
			Point2D p5 = randomPoint2d();
			Point2D p6 = randomPoint2d();
			Point2D p7 = randomPoint2d();
			
			Path2afp path = createPath();
			path.moveTo(p1.getX(),p1.getY());
			path.lineTo(p2.getX(),p2.getY());
			path.quadTo(p3.getX(),p3.getY(),p4.getX(),p4.getY());
			path.curveTo(p5.getX(),p5.getY(), p6.getX(),p6.getY(), p7.getX(),p7.getY());
			path.closePath();
	
			Transform2D trans = new Transform2D(randomMatrix3d());
			Path2afp transformedShape = (Path2afp) path.operator_multiply(trans);
			path.transform(trans);		
		
			assertTrue(path.equalsToShape(transformedShape));
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
			assertFalse(getS().operator_and(createPoint(-5, 1)));
		}

		@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createPoint(3, 6)));
		}

		@DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createPoint(3, -10)));
		}

		@DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createPoint(11, 1)));
		}

		@DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().operator_and(createPoint(4, 1)));
		}

		@DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().operator_and(createPoint(4, 3)));
		}

		@DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().operator_and(createPoint(-5, 1)));
		}

		@DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().operator_and(createPoint(3, 6)));
		}

		@DisplayName("#9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().operator_and(createPoint(3, -10)));
		}

		@DisplayName("#10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertFalse(getS().operator_and(createPoint(11, 1)));
		}

		@DisplayName("#11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().operator_and(createPoint(4, 1)));
		}

		@DisplayName("#12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertTrue(getS().operator_and(createPoint(4, 3)));
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
	    	assertTrue(getS().operator_and(createSegment(4, 0, 5, 3)));
		}

		@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	assertTrue(getS().operator_and(createRectangle(1.5, 1.5, 2, 1)));
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
			assertEpsilonEquals(2.23607, getS().operator_upTo(createPoint(-2, 1)));
		}

		@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.70711, getS().operator_upTo(createPoint(1, 0)));
		}

		@DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.00970, getS().operator_upTo(createPoint(3, 0)));
		}

		@DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4.12311, getS().operator_upTo(createPoint(1, -4)));
		}

		@DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();			
			assertEpsilonEquals(2.23606, getS().operator_upTo(createPoint(-2, 1)));
		}

		@DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertEpsilonEquals(0, getS().operator_upTo(createPoint(1, 0)));
		}

		@DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertEpsilonEquals(0, getS().operator_upTo(createPoint(3, 0)));
		}

		@DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().closePath();
			assertEpsilonEquals(2.6737, getS().operator_upTo(createPoint(1, -4)));
		}

    }

    @DisplayName("isCurved")
	@Nested
	public class IsCurved {
    	
		@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().isCurved());
		}
		
		@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	getS().clear();
			assertFalse(getS().isCurved());
		}
		
		@DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	getS().clear();
			getS().moveTo(1, 2);
			assertFalse(getS().isCurved());
		}
		
		@DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			assertFalse(getS().isCurved());
		}
		
		@DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			getS().lineTo(5, 6);
			assertFalse(getS().isCurved());
		}
		
		@DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			getS().lineTo(5, 6);
			getS().closePath();
			assertFalse(getS().isCurved());
		}
		
		@DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			getS().lineTo(3, 4);
			getS().lineTo(5, 6);
			getS().quadTo(7, 8, 9, 10);
			assertTrue(getS().isCurved());
		}

    }
	
    @DisplayName("isMultiParts")
	@Nested
	public class IsMultiParts {
    	
		@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	assertFalse(getS().isMultiParts());
		}
		
		@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			assertFalse(getS().isMultiParts());
		}
		
		@DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			assertFalse(getS().isMultiParts());
		}
		
		@DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			assertFalse(getS().isMultiParts());
		}
		
		@DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			getS().lineTo(5, 6);
			assertFalse(getS().isMultiParts());
		}
		
		@DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			getS().lineTo(5, 6);
			getS().closePath();
			assertFalse(getS().isMultiParts());
		}
		
		@DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().lineTo(3, 4);
			getS().moveTo(3, 4);
			getS().lineTo(5, 6);
			getS().quadTo(7, 8, 9, 10);
			assertTrue(getS().isMultiParts());
		}

	}
	
    @DisplayName("isPolygon")
	@Nested
	public class IsPolygon {

    	@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().isPolygon());
    	}

    	@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			assertFalse(getS().isPolygon());
    	}

    	@DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			assertFalse(getS().isPolygon());
    	}

    	@DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			assertFalse(getS().isPolygon());
    	}

    	@DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			getS().lineTo(5, 6);
			assertFalse(getS().isPolygon());
    	}

    	@DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			getS().lineTo(5, 6);
			getS().closePath();
			assertTrue(getS().isPolygon());
    	}

    	@DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			assertFalse(getS().isPolygon());
    	}
	
    	@DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			assertFalse(getS().isPolygon());
    	}

    	@DisplayName("#9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			getS().lineTo(3, 4);
			assertFalse(getS().isPolygon());
    	}

    	@DisplayName("#10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			getS().lineTo(3, 4);
			getS().closePath();
			assertTrue(getS().isPolygon());
    	}

    	@DisplayName("#11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			assertFalse(getS().isPolygon());
    	}

    	@DisplayName("#12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			assertFalse(getS().isPolygon());
    	}

    	@DisplayName("#13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			getS().lineTo(3, 4);
			assertFalse(getS().isPolygon());
    	}

    	@DisplayName("#14")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_14(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			getS().lineTo(3, 4);
			getS().lineTo(5, 6);
			assertFalse(getS().isPolygon());
    	}

    	@DisplayName("#15")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_15(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			getS().lineTo(3, 4);
			getS().lineTo(5, 6);
			getS().curveTo(7, 8, 9, 10, 11, 12);
			assertFalse(getS().isPolygon());
    	}

    	@DisplayName("#16")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_16(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			getS().lineTo(3, 4);
			getS().lineTo(5, 6);
			getS().curveTo(7, 8, 9, 10, 11, 12);
			getS().closePath();
			assertTrue(getS().isPolygon());
    	}

    	@DisplayName("#17")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_17(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			getS().lineTo(3, 4);
			getS().lineTo(5, 6);
			getS().quadTo(7, 8, 9, 10);
			assertFalse(getS().isPolygon());
    	}

    	@DisplayName("#18")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_18(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			getS().lineTo(3, 4);
			getS().lineTo(5, 6);
			getS().quadTo(7, 8, 9, 10);
			getS().closePath();
			assertTrue(getS().isPolygon());
    	}

    }
	
    @DisplayName("isPolyline")
	@Nested
	public class IsPolyline {

    	@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().isPolyline());
    	}

    	@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			assertFalse(getS().isPolyline());
    	}

    	@DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			assertFalse(getS().isPolyline());
    	}

    	@DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			assertFalse(getS().isPolyline());
    	}

    	@DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			getS().lineTo(5, 6);
			assertTrue(getS().isPolyline());
    	}

    	@DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			getS().lineTo(5, 6);
			getS().closePath();
			assertFalse(getS().isPolyline());
    	}

    	@DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			getS().lineTo(3, 4);
			getS().lineTo(5, 6);
			getS().curveTo(7, 8, 9, 10, 11, 12);
			assertFalse(getS().isPolyline());
    	}	

    	@DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			getS().lineTo(3, 4);
			getS().lineTo(5, 6);
			getS().curveTo(7, 8, 9, 10, 11, 12);
			getS().closePath();
			assertFalse(getS().isPolyline());
    	}	

    	@DisplayName("#9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			getS().lineTo(3, 4);
			getS().lineTo(5, 6);
			getS().quadTo(7, 8, 9, 10);
			assertFalse(getS().isPolyline());
    	}	

    	@DisplayName("#10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().clear();
			getS().moveTo(1, 2);
			getS().moveTo(3, 4);
			getS().lineTo(3, 4);
			getS().lineTo(5, 6);
			getS().quadTo(7, 8, 9, 10);
			getS().closePath();
			assertFalse(getS().isPolyline());
		}

    }
	
    @DisplayName("findsClosestPointPathIteratorPathIterator")
	@Nested
	public class FindsClosestPointPathIteratorPathIterator {

		Point2D result;
		Path2afp path;

		@BeforeEach
		public void sertUp() {
			result = createPoint(Double.NaN, Double.NaN);
		}

		@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			path = createPath();
			path.moveTo(0, 2);
			path.lineTo(2, 1);
			path.lineTo(0, 4);
			assertTrue(Path2afp.findsClosestPointPathIteratorPathIterator(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					(PathIterator2afp) path.getPathIterator(), result));
			assertFpPointEquals(2.0349, 0.76443, result);
		}

		@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			path = createPath();
			path.moveTo(-2, 2);
			path.lineTo(7, 5);
			path.lineTo(8, -6);
			path.lineTo(0, -4);
			assertTrue(Path2afp.findsClosestPointPathIteratorPathIterator(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					(PathIterator2afp) path.getPathIterator(), result));
			assertFpPointEquals(7, -5, result);
		}

		@DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			path = createPath();
			path.moveTo(-2, 2);
			path.lineTo(7, 5);
			path.lineTo(8, -6);
			path.lineTo(0, -1);
			path.lineTo(-1, 2);
			path.lineTo(5, 4);
			path.lineTo(7, 1);
			path.lineTo(7.5, -5.5);
			path.lineTo(0, -4);
			assertTrue(Path2afp.findsClosestPointPathIteratorPathIterator(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					(PathIterator2afp) path.getPathIterator(), result));
			assertFpPointEquals(0, 0, result);
		}

		@DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			path = createPath(PathWindingRule.EVEN_ODD);
			path.moveTo(0, 2);
			path.lineTo(2, 1);
			path.lineTo(0, 4);
			path.closePath();
			assertTrue(Path2afp.findsClosestPointPathIteratorPathIterator(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					(PathIterator2afp) path.getPathIterator(), result));
			assertFpPointEquals(2.0349, 0.76443, result);
		}

		@DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			path = createPath(PathWindingRule.EVEN_ODD);
			path.moveTo(-2, 2);
			path.lineTo(7, 5);
			path.lineTo(8, -6);
			path.lineTo(0, -4);
			path.closePath();
			assertTrue(Path2afp.findsClosestPointPathIteratorPathIterator(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					(PathIterator2afp) path.getPathIterator(), result));
			assertFpPointEquals(7, -5, result);
		}

		@DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			path = createPath(PathWindingRule.EVEN_ODD);
			path.moveTo(-2, 2);
			path.lineTo(7, 5);
			path.lineTo(8, -6);
			path.lineTo(0, -1);
			path.lineTo(-1, 2);
			path.lineTo(5, 4);
			path.lineTo(7, 1);
			path.lineTo(7.5, -5.5);
			path.lineTo(0, -4);
			path.closePath();
			assertTrue(Path2afp.findsClosestPointPathIteratorPathIterator(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					(PathIterator2afp) path.getPathIterator(), result));
			assertFpPointEquals(0, 0, result);
		}

		@DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			path = createPath(PathWindingRule.NON_ZERO);
			path.moveTo(0, 2);
			path.lineTo(2, 1);
			path.lineTo(0, 4);
			path.closePath();
			assertTrue(Path2afp.findsClosestPointPathIteratorPathIterator(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					(PathIterator2afp) path.getPathIterator(), result));
			assertFpPointEquals(2.0349, 0.76443, result);
		}

		@DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			path = createPath(PathWindingRule.NON_ZERO);
			path.moveTo(-2, 2);
			path.lineTo(7, 5);
			path.lineTo(8, -6);
			path.lineTo(0, -4);
			path.closePath();
			assertTrue(Path2afp.findsClosestPointPathIteratorPathIterator(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					(PathIterator2afp) path.getPathIterator(), result));
			assertFpPointEquals(7, -5, result);
		}

		@DisplayName("#9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			path = createPath(PathWindingRule.NON_ZERO);
			path.moveTo(-2, 2);
			path.lineTo(7, 5);
			path.lineTo(8, -6);
			path.lineTo(0, -1);
			path.lineTo(-1, 2);
			path.lineTo(5, 4);
			path.lineTo(7, 1);
			path.lineTo(7.5, -5.5);
			path.lineTo(0, -4);
			path.closePath();
			assertTrue(Path2afp.findsClosestPointPathIteratorPathIterator(
					getS().getPathIterator(SPLINE_APPROXIMATION_RATIO),
					(PathIterator2afp) path.getPathIterator(), result));
			assertFpPointEquals(0, 0, result);
		}

		@DisplayName("#10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			path = createPath();
			path.moveTo(0, 2);
			path.lineTo(2, 1);
			path.lineTo(0, 4);
			assertTrue(Path2afp.findsClosestPointPathIteratorPathIterator(
					path.getPathIterator(SPLINE_APPROXIMATION_RATIO),
					getS().getPathIterator(),
					result));
			assertFpPointEquals(2, 1, result);
		}

		@DisplayName("#11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			path = createPath();
			path.moveTo(-2, 2);
			path.lineTo(7, 5);
			path.lineTo(8, -6);
			path.lineTo(0, -4);
			assertTrue(Path2afp.findsClosestPointPathIteratorPathIterator(
					path.getPathIterator(SPLINE_APPROXIMATION_RATIO),
					getS().getPathIterator(),
					result));
			assertFpPointEquals(6.82353, -5.70588, result);
		}

		@DisplayName("#12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			path = createPath();
			path.moveTo(-2, 2);
			path.lineTo(7, 5);
			path.lineTo(8, -6);
			path.lineTo(0, -1);
			path.lineTo(-1, 2);
			path.lineTo(5, 4);
			path.lineTo(7, 1);
			path.lineTo(7.5, -5.5);
			path.lineTo(0, -4);
			assertTrue(Path2afp.findsClosestPointPathIteratorPathIterator(
					path.getPathIterator(SPLINE_APPROXIMATION_RATIO),
					getS().getPathIterator(),
					result));
			assertFpPointEquals(-.3, -.1, result);
		}

		@DisplayName("#13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			path = createPath(PathWindingRule.EVEN_ODD);
			path.moveTo(0, 2);
			path.lineTo(2, 1);
			path.lineTo(0, 4);
			path.closePath();
			assertTrue(Path2afp.findsClosestPointPathIteratorPathIterator(
					path.getPathIterator(SPLINE_APPROXIMATION_RATIO),
					getS().getPathIterator(),
					result));
			assertFpPointEquals(2, 1, result);
		}

		@DisplayName("#14")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_14(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			path = createPath(PathWindingRule.EVEN_ODD);
			path.moveTo(-2, 2);
			path.lineTo(7, 5);
			path.lineTo(8, -6);
			path.lineTo(0, -4);
			path.closePath();
			assertTrue(Path2afp.findsClosestPointPathIteratorPathIterator(
					path.getPathIterator(SPLINE_APPROXIMATION_RATIO),
					getS().getPathIterator(),
					result));
			assertFpPointEquals(7, -5, result);
		}

		@DisplayName("#15")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_15(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			path = createPath(PathWindingRule.EVEN_ODD);
			path.moveTo(-2, 2);
			path.lineTo(7, 5);
			path.lineTo(8, -6);
			path.lineTo(0, -1);
			path.lineTo(-1, 2);
			path.lineTo(5, 4);
			path.lineTo(7, 1);
			path.lineTo(7.5, -5.5);
			path.lineTo(0, -4);
			path.closePath();
			assertTrue(Path2afp.findsClosestPointPathIteratorPathIterator(
					path.getPathIterator(SPLINE_APPROXIMATION_RATIO),
					getS().getPathIterator(),
					result));
			assertFpPointEquals(-.3, -.1, result);
		}

		@DisplayName("#16")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_16(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			path = createPath(PathWindingRule.NON_ZERO);
			path.moveTo(0, 2);
			path.lineTo(2, 1);
			path.lineTo(0, 4);
			path.closePath();
			assertTrue(Path2afp.findsClosestPointPathIteratorPathIterator(
					path.getPathIterator(SPLINE_APPROXIMATION_RATIO),
					getS().getPathIterator(),
					result));
			assertFpPointEquals(2, 1, result);
		}

		@DisplayName("#17")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_17(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			path = createPath(PathWindingRule.NON_ZERO);
			path.moveTo(-2, 2);
			path.lineTo(7, 5);
			path.lineTo(8, -6);
			path.lineTo(0, -4);
			path.closePath();
			assertTrue(Path2afp.findsClosestPointPathIteratorPathIterator(
					path.getPathIterator(SPLINE_APPROXIMATION_RATIO),
					getS().getPathIterator(),
					result));
			assertFpPointEquals(7, -5, result);
		}

		@DisplayName("#18")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_18(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			path = createPath(PathWindingRule.NON_ZERO);
			path.moveTo(-2, 2);
			path.lineTo(7, 5);
			path.lineTo(8, -6);
			path.lineTo(0, -1);
			path.lineTo(-1, 2);
			path.lineTo(5, 4);
			path.lineTo(7, 1);
			path.lineTo(7.5, -5.5);
			path.lineTo(0, -4);
			path.closePath();
			assertTrue(Path2afp.findsClosestPointPathIteratorPathIterator(
					path.getPathIterator(SPLINE_APPROXIMATION_RATIO),
					getS().getPathIterator(),
					result));
			assertFpPointEquals(0, 0, result);
		}

    }
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getCurrentX(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(7, getS().getCurrentX());
		getS().lineTo(154, 485);
		assertEpsilonEquals(154, getS().getCurrentX());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getCurrentY(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(-5, getS().getCurrentY());
		getS().lineTo(154, 485);
		assertEpsilonEquals(485, getS().getCurrentY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getCurrentPoint(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFpPointEquals(7, -5, getS().getCurrentPoint());
		getS().lineTo(154, 485);
		assertFpPointEquals(154, 485, getS().getCurrentPoint());
	}

}