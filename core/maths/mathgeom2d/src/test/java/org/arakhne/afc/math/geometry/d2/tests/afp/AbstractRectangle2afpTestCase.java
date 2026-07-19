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

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.base.PathElementType;
import org.arakhne.afc.math.geometry.base.PathWindingRule;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.base.d2.Point2D;
import org.arakhne.afc.math.geometry.base.d2.Shape2D;
import org.arakhne.afc.math.geometry.base.d2.Transform2D;
import org.arakhne.afc.math.geometry.base.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.afp.MultiShape2afp;
import org.arakhne.afc.math.geometry.d2.afp.OrientedRectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Parallelogram2afp;
import org.arakhne.afc.math.geometry.d2.afp.Path2afp;
import org.arakhne.afc.math.geometry.d2.afp.PathIterator2afp;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Triangle2afp;
import org.arakhne.afc.math.geometry.d2.ai.PathIterator2ai;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("all")
public abstract class AbstractRectangle2afpTestCase<T extends Rectangle2afp<?, T, ?, ?, ?, B>,
		B extends Rectangle2afp<?, ?, ?, ?, ?, B>> extends AbstractRectangularShape2afpTestCase<T, B> {

	@Override
	protected final T createShape() {
		return (T) createRectangle(5, 8, 5, 10);
	}

	protected MultiShape2afp createTestMultiShape(double x, double y) {
		MultiShape2afp multishape = createMultiShape();
		multishape.add(createCircle(x - 5, y + 4, 1));
		multishape.add(createSegment(x + 4, y + 2, x + 8, y - 1));
		return multishape;
	}

	protected Triangle2afp createTestTriangle(double dx,  double dy) {
		return createTriangle(dx, dy, dx + 3, dy + 3, dx - 1, dy + 1);
	}

	protected Path2afp createSimpleTestPath(double dx, double dy, boolean close) {
		Path2afp path = createPath();
		path.moveTo(dx + 8, dy + 4);
		path.lineTo(dx - 2, dy + 10);
		path.lineTo(dx + 6, dy + 26);
		path.lineTo(dx + 18, dy + 18);
		if (close) {
			path.closePath();
		}
		return path;
	}

	protected Path2afp createComplexTestPath(double dx, double dy, boolean close, PathWindingRule rule) {
		Path2afp path = createPath(rule);
		path.moveTo(dx, dy);
		path.lineTo(dx - 12, dy + 8);
		path.lineTo(dx - 8, dy + 18);
		path.lineTo(dx + 4, dy + 18);
		path.lineTo(dx - 2, dy);
		path.lineTo(dx - 16, dy + 8);
		path.lineTo(dx - 8, dy + 24);
		path.lineTo(dx + 6, dy + 20);
		if (close) {
			path.closePath();
		}
		return path;
	}

	protected Parallelogram2afp createTestParallelogram(double cx, double cy) {
		Vector2D u = createVector(5, 1).toUnitVector();
		Vector2D v = createVector(4, -6).toUnitVector();
		return createParallelogram(cx, cy, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1);
	}
	
	protected OrientedRectangle2afp createTestOrientedRectangle(double cx, double cy) {
		Vector2D u = createVector(5, 1).toUnitVector();
		return createOrientedRectangle(cx, cy, u.getX(), u.getY(), 2, 1);
	}

	@DisplayName("intersectsRectangleRectangle")
	@Nested
	public class IntersectsRectangleRectangle {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2afp.intersectsRectangleRectangle(5, 8, 10, 18, 0, 0, 1, 1));
        }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2afp.intersectsRectangleRectangle(0, 0, 1, 1, 5, 8, 10, 18));
        }

        @DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2afp.intersectsRectangleRectangle(5, 8, 10, 18, 0, 20, 1, 22));
        }

        @DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2afp.intersectsRectangleRectangle(0, 20, 1, 22, 5, 8, 10, 18));
        }

        @DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2afp.intersectsRectangleRectangle(5, 8, 10, 18, 0, 0, 5, 100));
        }

        @DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2afp.intersectsRectangleRectangle(0, 0, 5, 100, 5, 8, 10, 18));
        }

        @DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Rectangle2afp.intersectsRectangleRectangle(5, 8, 10, 18, 0, 0, 5.1, 100));
        }

        @DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Rectangle2afp.intersectsRectangleRectangle(0, 0, 5.1, 100, 5, 8, 10, 18));
        }

        @DisplayName("#9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Rectangle2afp.intersectsRectangleRectangle(5, 8, 10, 18, 6, 9, 9.5, 15));
        }

        @DisplayName("#10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Rectangle2afp.intersectsRectangleRectangle(6, 9, 9.5, 15, 5, 8, 10, 18));
        }

        @DisplayName("#11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Rectangle2afp.intersectsRectangleRectangle(5, 8, 10, 18, 6, 9, 9.5, 15));
        }

        @DisplayName("#12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Rectangle2afp.intersectsRectangleRectangle(6, 9, 9.5, 15, 5, 8, 10, 18));
		}

	}

	@DisplayName("intersectsRectangleLine")
	@Nested
	public class IntersectsRectangleLine {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Rectangle2afp.intersectsRectangleLine(10, 12, 40, 37, 20, 45, 43, 15));
        }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Rectangle2afp.intersectsRectangleLine(10, 12, 40, 37, 20, 55, 43, 15));
        }

        @DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Rectangle2afp.intersectsRectangleLine(10, 12, 40, 37, 20, 0, 43, 15));
        }

        @DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Rectangle2afp.intersectsRectangleLine(10, 12, 40, 37, 0, 45, 43, 15));
        }

        @DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Rectangle2afp.intersectsRectangleLine(10, 12, 40, 37, 20, 45, 60, 15));
        }

        @DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2afp.intersectsRectangleLine(10, 12, 40, 37, 5, 45, 30, 55));
        }

        @DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2afp.intersectsRectangleLine(10, 12, 40, 37, 40, 55, 60, 15));
        }

        @DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2afp.intersectsRectangleLine(10, 12, 40, 37, 40, 0, 60, 40));
        }

        @DisplayName("#9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Rectangle2afp.intersectsRectangleLine(10, 12, 40, 37, 0, 40, 20, 0));
        }

        @DisplayName("#10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Rectangle2afp.intersectsRectangleLine(10, 12, 40, 37, 0, 45, 100, 15));
        }

        @DisplayName("#11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Rectangle2afp.intersectsRectangleLine(10, 12, 40, 37, 20, 100, 43, 0));
        }

        @DisplayName("#12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2afp.intersectsRectangleLine(10, 12, 40, 37, 20, 100, 43, 101));
        }

        @DisplayName("#13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2afp.intersectsRectangleLine(10, 12, 40, 37, 100, 45, 102, 15));
        }

        @DisplayName("#14")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_14(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2afp.intersectsRectangleLine(10, 12, 40, 37, 20, 0, 43, -2));
        }

        @DisplayName("#15")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_15(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2afp.intersectsRectangleLine(10, 12, 50, 49, -100, 45, -48, 15));
        }

        @DisplayName("#16")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_16(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2afp.intersectsRectangleLine(10, 12, 40, 37, -100, 60, -98, 61));
        }

        @DisplayName("#17")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_17(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Rectangle2afp.intersectsRectangleLine(10, 12, 40, 37, 0, 30, 9, 21));
		}

	}

	@DisplayName("intersectsRectangleSegment")
	@Nested
	public class IntersectsRectangleSegment {

        @DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Rectangle2afp.intersectsRectangleSegment(10, 12, 40, 37, 20, 45, 43, 15));
        }

        @DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Rectangle2afp.intersectsRectangleSegment(10, 12, 40, 37, 20, 55, 43, 15));
        }

        @DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Rectangle2afp.intersectsRectangleSegment(10, 12, 40, 37, 20, 0, 43, 15));
        }

        @DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Rectangle2afp.intersectsRectangleSegment(10, 12, 40, 37, 0, 45, 43, 15));
        }

        @DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Rectangle2afp.intersectsRectangleSegment(10, 12, 40, 37, 20, 45, 60, 15));
        }

        @DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2afp.intersectsRectangleSegment(10, 12, 40, 37, 5, 45, 30, 55));
        }

        @DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2afp.intersectsRectangleSegment(10, 12, 40, 37, 40, 55, 60, 15));
        }

        @DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2afp.intersectsRectangleSegment(10, 12, 40, 37, 40, 0, 60, 40));
        }

        @DisplayName("#9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Rectangle2afp.intersectsRectangleSegment(10, 12, 40, 37, 0, 40, 20, 0));
        }

        @DisplayName("#10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Rectangle2afp.intersectsRectangleSegment(10, 12, 40, 37, 0, 45, 100, 15));
        }

        @DisplayName("#11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Rectangle2afp.intersectsRectangleSegment(10, 12, 40, 37, 20, 100, 43, 0));
        }

        @DisplayName("#12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2afp.intersectsRectangleSegment(10, 12, 40, 37, 20, 100, 43, 101));
        }

        @DisplayName("#13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2afp.intersectsRectangleSegment(10, 12, 40, 37, 100, 45, 102, 15));
        }

        @DisplayName("#14")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_14(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2afp.intersectsRectangleSegment(10, 12, 40, 37, 20, 0, 43, -2));
        }

        @DisplayName("#15")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_15(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2afp.intersectsRectangleSegment(10, 12, 50, 49, -100, 45, -48, 15));
        }

        @DisplayName("#16")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_16(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2afp.intersectsRectangleSegment(10, 12, 40, 37, -100, 60, -98, 61));
        }

        @DisplayName("#17")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_17(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2afp.intersectsRectangleSegment(10, 12, 40, 37, 0, 30, 9, 21));
		}

	}

	@DisplayName("reducesCohenSutherlandZoneRectangleSegment")
	@Nested
	public class ReducesCohenSutherlandZoneRectangleSegment {

		private Point2D p1;
		private Point2D p2;

		@BeforeEach
		public void setUp() {
			p1 = createPoint(0, 0);
			p2 = createPoint(0, 0);
		}

		@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0,
					Rectangle2afp.reducesCohenSutherlandZoneRectangleSegment(10, 12, 40, 37, 20, 45, 43, 15,
					MathUtil.getCohenSutherlandCode(20, 45, 0, 12, 40, 37),
					MathUtil.getCohenSutherlandCode(43, 15, 0, 12, 40, 37),
					p1, p2));
			assertFpPointEquals(26.13333, 37, p1);
			assertFpPointEquals(40, 18.91304, p2);
		}

		@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEquals(0, 
					Rectangle2afp.reducesCohenSutherlandZoneRectangleSegment(10, 12, 40, 37, 20, 55, 43, 15,
					MathUtil.getCohenSutherlandCode(20, 55, 0, 12, 40, 37),
					MathUtil.getCohenSutherlandCode(43, 15, 0, 12, 40, 37),
					p1, p2));
			assertFpPointEquals(30.35, 37, p1);
			assertFpPointEquals(40, 20.21739, p2);
        }

	}

	@DisplayName("containsRectangleRectangle")
	@Nested
	public class ContainsRectangleRectangle {

		@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2afp.containsRectangleRectangle(5, 8, 10, 18, 0, 0, 1, 1));
		}

		@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2afp.containsRectangleRectangle(0, 0, 1, 1, 5, 8, 10, 18));
		}

		@DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2afp.containsRectangleRectangle(5, 8, 10, 18, 0, 20, 1, 22));
		}

		@DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2afp.containsRectangleRectangle(0, 20, 1, 22, 5, 8, 10, 18));
		}

		@DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2afp.containsRectangleRectangle(5, 8, 10, 18, 0, 0, 5, 100));
		}

		@DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2afp.containsRectangleRectangle(0, 0, 5, 100, 5, 8, 10, 18));
		}

		@DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2afp.containsRectangleRectangle(5, 8, 10, 18, 0, 0, 5.1, 100));
		}

		@DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2afp.containsRectangleRectangle(0, 0, 5.1, 100, 5, 8, 10, 18));
		}

		@DisplayName("#9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Rectangle2afp.containsRectangleRectangle(5, 8, 10, 18, 6, 9, 9.5, 15));
		}

		@DisplayName("#10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2afp.containsRectangleRectangle(6, 9, 9.5, 15, 5, 8, 10, 18));
		}

	}

	@DisplayName("containsRectanglePoint")
	@Nested
	public class ContainsRectanglePoint {

		@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2afp.containsRectanglePoint(10, 12, 40, 37, 20, 45));
		}

		@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2afp.containsRectanglePoint(10, 12, 40, 37, 20, 55));
		}

		@DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2afp.containsRectanglePoint(10, 12, 40, 37, 20, 0));
		}

		@DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2afp.containsRectanglePoint(10, 12, 40, 37, 0, 45));
		}

		@DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2afp.containsRectanglePoint(10, 12, 40, 37, 5, 45));
		}

		@DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		}

		@DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2afp.containsRectanglePoint(10, 12, 40, 37, 40, 55));
		}

		@DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2afp.containsRectanglePoint(10, 12, 40, 37, 40, 0));
		}

		@DisplayName("#9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2afp.containsRectanglePoint(10, 12, 40, 37, 0, 40));
		}

		@DisplayName("#10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2afp.containsRectanglePoint(10, 12, 40, 37, 20, 100));
		}

		@DisplayName("#11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2afp.containsRectanglePoint(10, 12, 40, 37, 100, 45));
		}

		@DisplayName("#12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2afp.containsRectanglePoint(10, 12, 50, 49, -100, 45));
		}

		@DisplayName("#13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(Rectangle2afp.containsRectanglePoint(10, 12, 40, 37, -100, 60));
		}

		@DisplayName("#14")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_14(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Rectangle2afp.containsRectanglePoint(10, 12, 40, 37, 10, 12));
		}

		@DisplayName("#15")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_15(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Rectangle2afp.containsRectanglePoint(10, 12, 40, 37, 40, 12));
		}

		@DisplayName("#16")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_16(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Rectangle2afp.containsRectanglePoint(10, 12, 40, 37, 40, 37));
		}

		@DisplayName("#17")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_17(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Rectangle2afp.containsRectanglePoint(10, 12, 40, 37, 10, 37));
		}

		@DisplayName("#18")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_18(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(Rectangle2afp.containsRectanglePoint(10, 12, 40, 37, 35, 24));
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
			assertFalse(getS().equals(createRectangle(0, 8, 5, 12)));
		}

		@DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equals(createRectangle(5, 8, 5, 0)));
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
			assertTrue(getS().equals(createRectangle(5, 8, 5, 10)));
		}

		@DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equals(createRectangle(0, 8, 5, 12).getPathIterator()));
		}

		@DisplayName("#9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equals(createRectangle(5, 8, 5, 0).getPathIterator()));
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
			assertTrue(getS().equals(createRectangle(5, 8, 5, 10).getPathIterator()));
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
			assertFalse(getS().equalsToPathIterator(createRectangle(0, 8, 5, 12).getPathIterator()));
		}

		@DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equalsToPathIterator(createRectangle(5, 8, 5, 0).getPathIterator()));
		}

		@DisplayName("#4")
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
			assertTrue(getS().equalsToPathIterator(createRectangle(5, 8, 5, 10).getPathIterator()));
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
			assertFalse(getS().equalsToShape((T) createRectangle(0, 8, 5, 12)));
		}

		@DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equalsToShape((T) createRectangle(5, 8, 5, 0)));
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
			assertTrue(getS().equalsToShape((T) createRectangle(5, 8, 5, 10)));
		}

	}

	@DisplayName("add(Point2D)")
	@Nested
	public class AddPoint2D {

		@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().add(createPoint(123.456, 456.789));
			assertEpsilonEquals(5, getS().getMinX());
			assertEpsilonEquals(8, getS().getMinY());
			assertEpsilonEquals(123.456, getS().getMaxX());
			assertEpsilonEquals(456.789, getS().getMaxY());
		}

		@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().add(createPoint(123.456, 456.789));
			getS().add(createPoint(-123.456, 456.789));
			assertEpsilonEquals(-123.456, getS().getMinX());
			assertEpsilonEquals(8, getS().getMinY());
			assertEpsilonEquals(123.456, getS().getMaxX());
			assertEpsilonEquals(456.789, getS().getMaxY());
		}

		@DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().add(createPoint(123.456, 456.789));
			getS().add(createPoint(-123.456, 456.789));
			getS().add(createPoint(-123.456, -456.789));
			assertEpsilonEquals(-123.456, getS().getMinX());
			assertEpsilonEquals(-456.789, getS().getMinY());
			assertEpsilonEquals(123.456, getS().getMaxX());
			assertEpsilonEquals(456.789, getS().getMaxY());
		}

	}

	@DisplayName("add(double,double)")
	@Nested
	public class AddDoubleDouble {

		@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().add(123.456, 456.789);
			assertEpsilonEquals(5, getS().getMinX());
			assertEpsilonEquals(8, getS().getMinY());
			assertEpsilonEquals(123.456, getS().getMaxX());
			assertEpsilonEquals(456.789, getS().getMaxY());
		}

		@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().add(123.456, 456.789);
			getS().add(-123.456, 456.789);
			assertEpsilonEquals(-123.456, getS().getMinX());
			assertEpsilonEquals(8, getS().getMinY());
			assertEpsilonEquals(123.456, getS().getMaxX());
			assertEpsilonEquals(456.789, getS().getMaxY());
		}

		@DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().add(123.456, 456.789);
			getS().add(-123.456, 456.789);
			getS().add(-123.456, -456.789);
			assertEpsilonEquals(-123.456, getS().getMinX());
			assertEpsilonEquals(-456.789, getS().getMinY());
			assertEpsilonEquals(123.456, getS().getMaxX());
			assertEpsilonEquals(456.789, getS().getMaxY());
		}

	}
	
	@DisplayName("setUnion")
	@Nested
	public class SetUnion {

		@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().setUnion(createRectangle(0, 0, 12, 1));
			assertEpsilonEquals(0, getS().getMinX());
			assertEpsilonEquals(0, getS().getMinY());
			assertEpsilonEquals(12, getS().getMaxX());
			assertEpsilonEquals(18, getS().getMaxY());
		}

	}

	@DisplayName("createUnion")
	@Nested
	public class CreateUnion {

		@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			B union = getS().createUnion(createRectangle(0, 0, 12, 1));
			assertNotSame(getS(), union);
			assertEpsilonEquals(0, union.getMinX());
			assertEpsilonEquals(0, union.getMinY());
			assertEpsilonEquals(12, union.getMaxX());
			assertEpsilonEquals(18, union.getMaxY());
			assertEpsilonEquals(5, getS().getMinX());
			assertEpsilonEquals(8, getS().getMinY());
			assertEpsilonEquals(10, getS().getMaxX());
			assertEpsilonEquals(18, getS().getMaxY());
		}

	}

	@DisplayName("setIntersection")
	@Nested
	public class SetIntersection {

		@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().setIntersection(createRectangle(0, 0, 12, 1));
			assertTrue(getS().isEmpty());
		}

		@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().setIntersection(createRectangle(0, 0, 7, 10));
			assertEpsilonEquals(5, getS().getMinX());
			assertEpsilonEquals(8, getS().getMinY());
			assertEpsilonEquals(7, getS().getMaxX());
			assertEpsilonEquals(10, getS().getMaxY());
		}

	}

	@DisplayName("createIntersection")
	@Nested
	public class CreateIntersection {

		@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			B box = getS().createIntersection(createRectangle(0, 0, 12, 1));
			assertNotSame(getS(), box);
			assertTrue(box.isEmpty());
			assertEpsilonEquals(5, getS().getMinX());
			assertEpsilonEquals(8, getS().getMinY());
			assertEpsilonEquals(10, getS().getMaxX());
			assertEpsilonEquals(18, getS().getMaxY());
		}
	
		@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			B box = getS().createIntersection(createRectangle(0, 0, 7, 10));
			assertNotSame(getS(), box);
			assertEpsilonEquals(5, box.getMinX());
			assertEpsilonEquals(8, box.getMinY());
			assertEpsilonEquals(7, box.getMaxX());
			assertEpsilonEquals(10, box.getMaxY());
			assertEpsilonEquals(5, getS().getMinX());
			assertEpsilonEquals(8, getS().getMinY());
			assertEpsilonEquals(10, getS().getMaxX());
			assertEpsilonEquals(18, getS().getMaxY());
		}

	}

	@DisplayName("avoidCollisionWith")
	@Nested
	public class AvoidCollisionWith {

		@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			B r = createRectangle(0, 0, 7, 10);
			assertTrue(getS().intersects(r));
			assertTrue(r.intersects(getS()));
	
			Vector2D v = createVector(Double.NaN, Double.NaN);
			getS().avoidCollisionWith(r, v);
			
			assertEpsilonEquals(2, v.getX());
			assertEpsilonEquals(0, v.getY());
			assertEpsilonEquals(7, getS().getMinX());
			assertEpsilonEquals(8, getS().getMinY());
			assertEpsilonEquals(12, getS().getMaxX());
			assertEpsilonEquals(18, getS().getMaxY());
			assertFalse(getS().intersects(r));
			assertFalse(r.intersects(getS()));
		}

		@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			B r = createRectangle(0, 0, 7, 10);
			assertTrue(getS().intersects(r));
			assertTrue(r.intersects(getS()));

			Vector2D v = createVector(Double.NaN, Double.NaN);
			getS().avoidCollisionWith(r, null, v);
			
			assertEpsilonEquals(2, v.getX());
			assertEpsilonEquals(0, v.getY());
			assertEpsilonEquals(7, getS().getMinX());
			assertEpsilonEquals(8, getS().getMinY());
			assertEpsilonEquals(12, getS().getMaxX());
			assertEpsilonEquals(18, getS().getMaxY());
			assertFalse(getS().intersects(r));
			assertFalse(r.intersects(getS()));
		}

		@DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			B r = createRectangle(0, 0, 7, 10);
			assertTrue(getS().intersects(r));
			assertTrue(r.intersects(getS()));

			Vector2D v1 = createVector(0, 0);
			Vector2D v2 = createVector(Double.NaN, Double.NaN);
			getS().avoidCollisionWith(r, v1, v2);
			
			assertEpsilonEquals(2, v2.getX());
			assertEpsilonEquals(0, v2.getY());
			assertEpsilonEquals(7, getS().getMinX());
			assertEpsilonEquals(8, getS().getMinY());
			assertEpsilonEquals(12, getS().getMaxX());
			assertEpsilonEquals(18, getS().getMaxY());
			assertFalse(getS().intersects(r));
			assertFalse(r.intersects(getS()));
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void avoidCollisionWithRectangle2afpVector2DVector2D_givenDisplacement(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			B r = createRectangle(0, 0, 7, 10);
			assertTrue(getS().intersects(r));
			assertTrue(r.intersects(getS()));

			Vector2D v1 = createVector(-4, 4);
			Vector2D v2 = createVector(Double.NaN, Double.NaN);
			getS().avoidCollisionWith(r, v1, v2);
			
			assertEpsilonEquals(-2, v1.getX());
			assertEpsilonEquals(2, v1.getY());
			assertEpsilonEquals(-2, v2.getX());
			assertEpsilonEquals(2, v2.getY());
			assertEpsilonEquals(3, getS().getMinX());
			assertEpsilonEquals(10, getS().getMinY());
			assertEpsilonEquals(8, getS().getMaxX());
			assertEpsilonEquals(20, getS().getMaxY());
			assertFalse(getS().intersects(r));
			assertFalse(r.intersects(getS()));
		}

	}

	@DisplayName("findsClosestPointRectanglePoint")
	@Nested
	public class FindsClosestPointRectanglePoint {

		@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPoint(Double.NaN, Double.NaN);
			Rectangle2afp.findsClosestPointRectanglePoint(5, 8, 10, 18, 0, 0, p);
			assertFpPointEquals(5, 8, p);
		}

		@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPoint(Double.NaN, Double.NaN);
			Rectangle2afp.findsClosestPointRectanglePoint(5, 8, 10, 18, 100, 0, p);
			assertFpPointEquals(10, 8, p);
		}

		@DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
			var p = createPoint(Double.NaN, Double.NaN);
			Rectangle2afp.findsClosestPointRectanglePoint(5, 8, 10, 18, 100, 100, p);
			assertFpPointEquals(10, 18, p);
		}

		@DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
			var p = createPoint(Double.NaN, Double.NaN);
			Rectangle2afp.findsClosestPointRectanglePoint(5, 8, 10, 18, 0, 100, p);
			assertFpPointEquals(5, 18, p);
		}

		@DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
			var p = createPoint(Double.NaN, Double.NaN);
			Rectangle2afp.findsClosestPointRectanglePoint(5, 8, 10, 18, 0, 10, p);
			assertFpPointEquals(5, 10, p);
		}

		@DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
			var p = createPoint(Double.NaN, Double.NaN);
			Rectangle2afp.findsClosestPointRectanglePoint(5, 8, 10, 18, 7, 0, p);
			assertFpPointEquals(7, 8, p);
		}

		@DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
			var p = createPoint(Double.NaN, Double.NaN);
			Rectangle2afp.findsClosestPointRectanglePoint(5, 8, 10, 18, 154, 17, p);
			assertFpPointEquals(10, 17, p);
		}

		@DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
			var p = createPoint(Double.NaN, Double.NaN);
			Rectangle2afp.findsClosestPointRectanglePoint(5, 8, 10, 18, 9, 154, p);
			assertFpPointEquals(9, 18, p);
		}

		@DisplayName("#9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_9(CoordinateSystem2D cs) {
			var p = createPoint(Double.NaN, Double.NaN);
			Rectangle2afp.findsClosestPointRectanglePoint(5, 8, 10, 18, 8, 18, p);
			assertFpPointEquals(8, 18, p);
		}

		@DisplayName("#10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_10(CoordinateSystem2D cs) {
			var p = createPoint(Double.NaN, Double.NaN);
			Rectangle2afp.findsClosestPointRectanglePoint(5, 8, 10, 18, 7, 12, p);
			assertFpPointEquals(7, 12, p);
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
			assertEpsilonEquals(5, p.getX());
			assertEpsilonEquals(8, p.getY());
		}

		@DisplayName("(Point2D) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var p = getS().getClosestPointTo(createPoint(100, 0));
			assertEpsilonEquals(10, p.getX());
			assertEpsilonEquals(8, p.getY());
		}

		@DisplayName("(Point2D) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var p = getS().getClosestPointTo(createPoint(100, 100));
			assertEpsilonEquals(10, p.getX());
			assertEpsilonEquals(18, p.getY());
		}

		@DisplayName("(Point2D) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var p = getS().getClosestPointTo(createPoint(0, 100));
			assertEpsilonEquals(5, p.getX());
			assertEpsilonEquals(18, p.getY());
		}

		@DisplayName("(Point2D) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var p = getS().getClosestPointTo(createPoint(0, 10));
			assertEpsilonEquals(5, p.getX());
			assertEpsilonEquals(10, p.getY());
		}

		@DisplayName("(Point2D) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var p = getS().getClosestPointTo(createPoint(7, 0));
			assertEpsilonEquals(7, p.getX());
			assertEpsilonEquals(8, p.getY());
		}

		@DisplayName("(Point2D) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var p = getS().getClosestPointTo(createPoint(154, 17));
			assertEpsilonEquals(10, p.getX());
			assertEpsilonEquals(17, p.getY());
		}

		@DisplayName("(Point2D) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var p = getS().getClosestPointTo(createPoint(9, 154));
			assertEpsilonEquals(9, p.getX());
			assertEpsilonEquals(18, p.getY());
		}

		@DisplayName("(Point2D) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var p = getS().getClosestPointTo(createPoint(8, 18));
			assertEpsilonEquals(8, p.getX());
			assertEpsilonEquals(18, p.getY());
		}

		@DisplayName("(Point2D) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var p = getS().getClosestPointTo(createPoint(7, 12));
			assertEpsilonEquals(7, p.getX());
			assertEpsilonEquals(12, p.getY());
		}

		@DisplayName("(Circle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 8, getS().getClosestPointTo(createCircle(0, 0, 1)));
		}

		@DisplayName("(Circle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 14, getS().getClosestPointTo(createCircle(-2, 14, 1)));
		}

		@DisplayName("(Circle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 18, getS().getClosestPointTo(createCircle(-1, 21, 1)));
		}

		@DisplayName("(Circle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(7, 18, getS().getClosestPointTo(createCircle(7, 21, 1)));
		}

		@DisplayName("(Circle2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createCircle(9, 13, 1));
		}

		@DisplayName("(Circle2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(8, 8, getS().getClosestPointTo(createCircle(8, 4, 1)));
		}

		@DisplayName("(Circle2afp) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(10, 8, getS().getClosestPointTo(createCircle(20, 0, 1)));
		}

		@DisplayName("(Circle2afp) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(10, 14, getS().getClosestPointTo(createCircle(19, 14, 1)));
		}

		@DisplayName("(Circle2afp) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(10, 18, getS().getClosestPointTo(createCircle(18, 21, 1)));
		}

		@DisplayName("(Circle2afp) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createCircle(5, 18, 1));
		}

		@DisplayName("(Circle2afp) #11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 8, getS().getClosestPointTo(createCircle(4.3, 7, 1)));
		}

		@DisplayName("(Rectangle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 8, getS().getClosestPointTo(createRectangle(0, 0, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 12.5, getS().getClosestPointTo(createRectangle(0, 12, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 18, getS().getClosestPointTo(createRectangle(0, 21, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(7.5, 8, getS().getClosestPointTo(createRectangle(7, 0, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRectangle(8, 12, 1, 1));
		}

		@DisplayName("(Rectangle2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(9.5, 18, getS().getClosestPointTo(createRectangle(9, 21, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(10, 8, getS().getClosestPointTo(createRectangle(15, 0, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(10, 12.5, getS().getClosestPointTo(createRectangle(16, 12, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(10, 18, getS().getClosestPointTo(createRectangle(17, 21, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(6, 12.5, getS().getClosestPointTo(createRectangle(1, 12, 10, 1)));
		}

		@DisplayName("(Rectangle2afp) #11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRectangle(1, 12, 10, 20));
		}

		@DisplayName("(Rectangle2afp) #12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRectangle(8, 0, 1, 20));
		}

		@DisplayName("(Rectangle2afp) #13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRectangle(8, 0, 10, 20));
		}

		@DisplayName("(Rectangle2afp) #14")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_14(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRectangle(6, 10, 1, 1));
		}

		@DisplayName("(Rectangle2afp) #15")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_15(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRectangle(4, 12, 1, 1));
		}

		@DisplayName("(Rectangle2afp) #16")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_16(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRectangle(4.1, 12, 1, 1));
		}

		@DisplayName("(Rectangle2afp) #17")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_17(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRectangle(4.2, 12, 1, 1));
		}

		@DisplayName("(Rectangle2afp) #18")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_18(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRectangle(4.3, 12, 1, 1));
		}

		@DisplayName("(Rectangle2afp) #19")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_19(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRectangle(4.4, 12, 1, 1));
		}

		@DisplayName("(Rectangle2afp) #20")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_20(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRectangle(4.5, 12, 1, 1));
		}

		@DisplayName("(Rectangle2afp) #21")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_21(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRectangle(4.6, 12, 1, 1));
		}

		@DisplayName("(Rectangle2afp) #22")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_22(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRectangle(4.7, 12, 1, 1));
		}

		@DisplayName("(Rectangle2afp) #23")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_23(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes( getS(), createRectangle(4.8, 12, 1, 1));
		}

		@DisplayName("(Rectangle2afp) #24")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_24(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRectangle(4.9, 12, 1, 1));
		}

		@DisplayName("(Rectangle2afp) #25")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_25(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRectangle(5, 12, 1, 1));
		}

		@DisplayName("(Rectangle2afp) #26")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_26(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRectangle(5.1, 12, 1, 1));
		}

		@DisplayName("(Segment2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 8, getS().getClosestPointTo(createSegment(0, 0, 2, 1)));
		}

		@DisplayName("(Segment2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 8, getS().getClosestPointTo(createSegment(0, 0, 1, 2)));
		}

		@DisplayName("(Segment2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 14, getS().getClosestPointTo(createSegment(1, 10, 4, 14)));
		}

		@DisplayName("(Segment2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createSegment(1, 10, 6, 14));
		}

		@DisplayName("(Segment2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createSegment(1, 10, 18, 14));
		}

		@DisplayName("(Segment2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createSegment(7, 10, 18, 14));
		}

		@DisplayName("(Segment2afp) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(10, 10, getS().getClosestPointTo(createSegment(17, 10, 18, 14)));
		}

		@DisplayName("(Segment2afp) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(9, 8, getS().getClosestPointTo(createSegment(6, 0, 9, 6)));
		}

		@DisplayName("(Segment2afp) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createSegment(6, 0, 9, 16));
		}

		@DisplayName("(Segment2afp) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createSegment(6, 0, 9, 21));
		}

		@DisplayName("(Segment2afp) #11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createSegment(6, 10, 9, 21));
		}

		@DisplayName("(Segment2afp) #12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(6, 18, getS().getClosestPointTo(createSegment(6, 19, 9, 21)));
		}

		@DisplayName("(Segment2afp) #13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createSegment(8, 20, 14, 8));
		}

		@DisplayName("(MultiShape2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void multihape_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 8, getS().getClosestPointTo(createTestMultiShape(-10, 7)));
		}

		@DisplayName("(MultiShape2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void multihape_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 8, getS().getClosestPointTo(createTestMultiShape(0, -4)));
		}

		@DisplayName("(MultiShape2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void multihape_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createTestMultiShape(4, 6));
		}

		@DisplayName("(MultiShape2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void multihape_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 10, getS().getClosestPointTo(createTestMultiShape(8, 6)));
		}

		@DisplayName("(MultiShape2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void multihape_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createTestMultiShape(9, 6));
		}
		
		@DisplayName("(Triangle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 8, getS().getClosestPointTo(createTestTriangle(0, 4)));
		}
		
		@DisplayName("(Triangle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(10, 8, getS().getClosestPointTo(createTestTriangle(10, 6)));
		}
		
		@DisplayName("(Triangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createTestTriangle(4, 16));
		}
		
		@DisplayName("(Triangle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(6, 18, getS().getClosestPointTo(createTestTriangle(6, 19)));
		}
		
		@DisplayName("(Triangle2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createTestTriangle(10.5, 17.1));
		}

		@DisplayName("(Path2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 8, getS().getClosestPointTo(createSimpleTestPath(0, 0, false)));
		}

		@DisplayName("(Path2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createSimpleTestPath(0, 0, true));
		}

		@DisplayName("(Path2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createSimpleTestPath(0, 10, false));
		}

		@DisplayName("(Path2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createSimpleTestPath(0, 10, true));
		}

		@DisplayName("(Path2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(10, 12, getS().getClosestPointTo(createSimpleTestPath(14, 2, false)));
		}

		@DisplayName("(Path2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(10, 12, getS().getClosestPointTo(createSimpleTestPath(14, 2, true)));
		}

		@DisplayName("(Path2afp) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 8, getS().getClosestPointTo(createComplexTestPath(12, 2, false, PathWindingRule.EVEN_ODD)));
		}

		@DisplayName("(Path2afp) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 8, getS().getClosestPointTo(createComplexTestPath(12, 2, true, PathWindingRule.EVEN_ODD)));
		}

		@DisplayName("(Path2afp) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 18, getS().getClosestPointTo(createComplexTestPath(3, 8, false, PathWindingRule.EVEN_ODD)));
		}

		@DisplayName("(Path2afp) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createComplexTestPath(3, 8, true, PathWindingRule.EVEN_ODD));
		}

		@DisplayName("(Path2afp) #11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 18, getS().getClosestPointTo(createComplexTestPath(-2, 8, false, PathWindingRule.EVEN_ODD)));
		}

		@DisplayName("(Path2afp) #12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 18, getS().getClosestPointTo(createComplexTestPath(-2, 8, true, PathWindingRule.EVEN_ODD)));
		}

		@DisplayName("(Path2afp) #13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 8, getS().getClosestPointTo(createComplexTestPath(12, 2, false, PathWindingRule.NON_ZERO)));
		}

		@DisplayName("(Path2afp) #14")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_14(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createComplexTestPath(12, 2, true, PathWindingRule.NON_ZERO));
		}

		@DisplayName("(Path2afp) #15")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_15(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 18, getS().getClosestPointTo(createComplexTestPath(3, 8, false, PathWindingRule.NON_ZERO)));
		}

		@DisplayName("(Path2afp) #16")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_16(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createComplexTestPath(3, 8, true, PathWindingRule.NON_ZERO));
		}

		@DisplayName("(Path2afp) #17")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_17(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 18, getS().getClosestPointTo(createComplexTestPath(-2, 8, false, PathWindingRule.NON_ZERO)));
		}

		@DisplayName("(Path2afp) #18")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_18(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 18, getS().getClosestPointTo(createComplexTestPath(-2, 8, true, PathWindingRule.NON_ZERO)));
		}

		@DisplayName("(Ellipse2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 8, getS().getClosestPointTo(createEllipse(0, 0, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 12.5, getS().getClosestPointTo(createEllipse(2, 12, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createEllipse(6, 16, 2, 1));
		}

		@DisplayName("(Ellipse2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(10, 8, getS().getClosestPointTo(createEllipse(9.897519745562938, 7.003543789189412, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createEllipse(9.5, 9.5, 2, 1));
		}

		@DisplayName("(RoundRectangle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 8, getS().getClosestPointTo(createRoundRectangle(0, 0, 1, 1, .1, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 12.5, getS().getClosestPointTo(createRoundRectangle(0, 12, 1, 1, .1, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 18, getS().getClosestPointTo(createRoundRectangle(0, 21, 1, 1, .1, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(7.5, 8, getS().getClosestPointTo(createRoundRectangle(7, 0, 1, 1, .1, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRoundRectangle(8, 12, 1, 1, .1, .1));
		}

		@DisplayName("(RoundRectangle2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(9.5, 18, getS().getClosestPointTo(createRoundRectangle(9, 21, 1, 1, .1, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(10, 8, getS().getClosestPointTo(createRoundRectangle(15, 0, 1, 1, .1, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(10, 12.5, getS().getClosestPointTo(createRoundRectangle(16, 12, 1, 1, .1, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(10, 18, getS().getClosestPointTo(createRoundRectangle(17, 21, 1, 1, .1, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(6, 12.5, getS().getClosestPointTo(createRoundRectangle(1, 12, 10, 1, .1, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRoundRectangle(1, 12, 10, 20, .1, .1));
		}

		@DisplayName("(RoundRectangle2afp) #12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRoundRectangle(8, 0, 1, 20, .1, .1));
		}

		@DisplayName("(RoundRectangle2afp) #13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRoundRectangle(8, 0, 10, 20, .1, .1));
		}

		@DisplayName("(RoundRectangle2afp) #14")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_14(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRoundRectangle(6, 10, 1, 1, .1, .1));
		}

		@DisplayName("(RoundRectangle2afp) #15")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_15(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRoundRectangle(4, 12, 1, 1, .1, .1));
		}

		@DisplayName("(RoundRectangle2afp) #16")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_16(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRoundRectangle(4.1, 12, 1, 1, .1, .1));
		}

		@DisplayName("(RoundRectangle2afp) #17")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_17(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRoundRectangle(4.2, 12, 1, 1, .1, .1));
		}

		@DisplayName("(RoundRectangle2afp) #18")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_18(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRoundRectangle(4.3, 12, 1, 1, .1, .1));
		}

		@DisplayName("(RoundRectangle2afp) #19")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_19(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRoundRectangle(4.4, 12, 1, 1, .1, .1));
		}

		@DisplayName("(RoundRectangle2afp) #20")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_20(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRoundRectangle(4.5, 12, 1, 1, .1, .1));
		}

		@DisplayName("(RoundRectangle2afp) #21")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_21(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRoundRectangle(4.6, 12, 1, 1, .1, .1));
		}

		@DisplayName("(RoundRectangle2afp) #22")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_22(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRoundRectangle(4.7, 12, 1, 1, .1, .1));
		}

		@DisplayName("(RoundRectangle2afp) #23")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_23(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRoundRectangle(4.8, 12, 1, 1, .1, .1));
		}

		@DisplayName("(RoundRectangle2afp) #24")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_24(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRoundRectangle(4.9, 12, 1, 1, .1, .1));
		}

		@DisplayName("(RoundRectangle2afp) #25")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_25(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRoundRectangle(5, 12, 1, 1, .1, .1));
		}

		@DisplayName("(RoundRectangle2afp) #26")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_26(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createRoundRectangle(5.1, 12, 1, 1, .1, .1));
		}

		@DisplayName("(Parallelogram2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 8, getS().getClosestPointTo(createTestParallelogram(0, 0)));
		}

		@DisplayName("(Parallelogram2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 9.56018, getS().getClosestPointTo(createTestParallelogram(2, 10)));
		}

		@DisplayName("(Parallelogram2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(10, 18, getS().getClosestPointTo(createTestParallelogram(14, 18)));
		}

		@DisplayName("(Parallelogram2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createTestParallelogram(12, 14));
		}

		@DisplayName("(Parallelogram2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(10, 8, getS().getClosestPointTo(createTestParallelogram(14, 2)));
		}

		@DisplayName("(OrientedRectangle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 8, getS().getClosestPointTo(createTestOrientedRectangle(0, 0)));
		}

		@DisplayName("(OrientedRectangle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 9.41165, getS().getClosestPointTo(createTestOrientedRectangle(2, 10)));
		}

		@DisplayName("(OrientedRectangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createTestOrientedRectangle(12, 8));
		}

		@DisplayName("(OrientedRectangle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertClosestPointInBothShapes(getS(), createTestOrientedRectangle(8, 12));
		}

		@DisplayName("(OrientedRectangle2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 18, getS().getClosestPointTo(createTestOrientedRectangle(6, 20)));
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
			assertEpsilonEquals(10, p.getX());
			assertEpsilonEquals(18, p.getY());
		}

		@DisplayName("(Point2D) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var p = getS().getFarthestPointTo(createPoint(100, 0));
			assertEpsilonEquals(5, p.getX());
			assertEpsilonEquals(18, p.getY());
		}

		@DisplayName("(Point2D) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var p = getS().getFarthestPointTo(createPoint(100, 100));
			assertEpsilonEquals(5, p.getX());
			assertEpsilonEquals(8, p.getY());
		}

		@DisplayName("(Point2D) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var p = getS().getFarthestPointTo(createPoint(0, 100));
			assertEpsilonEquals(10, p.getX());
			assertEpsilonEquals(8, p.getY());
		}

		@DisplayName("(Point2D) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var p = getS().getFarthestPointTo(createPoint(0, 10));
			assertEpsilonEquals(10, p.getX());
			assertEpsilonEquals(18, p.getY());
		}

		@DisplayName("(Point2D) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var p = getS().getFarthestPointTo(createPoint(7, 0));
			assertEpsilonEquals(10, p.getX());
			assertEpsilonEquals(18, p.getY());
		}

		@DisplayName("(Point2D) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var p = getS().getFarthestPointTo(createPoint(154, 17));
			assertEpsilonEquals(5, p.getX());
			assertEpsilonEquals(8, p.getY());
		}

		@DisplayName("(Point2D) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var p = getS().getFarthestPointTo(createPoint(9, 154));
			assertEpsilonEquals(5, p.getX());
			assertEpsilonEquals(8, p.getY());
		}

		@DisplayName("(Point2D) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var p = getS().getFarthestPointTo(createPoint(8, 18));
			assertEpsilonEquals(5, p.getX());
			assertEpsilonEquals(8, p.getY());
		}

		@DisplayName("(Point2D) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var p = getS().getFarthestPointTo(createPoint(7, 12));
			assertEpsilonEquals(10, p.getX());
			assertEpsilonEquals(18, p.getY());
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
			assertEpsilonEquals(9.43398, getS().getDistance(createPoint(0, 0)));
		}

		@DisplayName("(Point2D) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(90.35486, getS().getDistance(createPoint(100, 0)));
		}

		@DisplayName("(Point2D) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(121.75385, getS().getDistance(createPoint(100, 100)));
		}

		@DisplayName("(Point2D) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(82.1523, getS().getDistance(createPoint(0, 100)));
		}

		@DisplayName("(Point2D) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5, getS().getDistance(createPoint(0, 10)));
		}

		@DisplayName("(Point2D) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(8, getS().getDistance(createPoint(7, 0)));
		}

		@DisplayName("(Point2D) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(144, getS().getDistance(createPoint(154, 17)));
		}

		@DisplayName("(Point2D) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(136, getS().getDistance(createPoint(9, 154)));
		}

		@DisplayName("(Point2D) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistance(createPoint(8, 18)));
		}

		@DisplayName("(Point2D) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistance(createPoint(7, 12)));
		}

	}

	@DisplayName("calculatesDistanceSquaredRectanglePoint")
	@Nested
	public class CalculatesDistanceSquaredRectanglePoint {

		@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(88.99998, Rectangle2afp.calculatesDistanceSquaredRectanglePoint(5, 8, 10, 18, 0, 0));
		}

		@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(8164, Rectangle2afp.calculatesDistanceSquaredRectanglePoint(5, 8, 10, 18, 100, 0));
		}

		@DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(14823.99999, Rectangle2afp.calculatesDistanceSquaredRectanglePoint(5, 8, 10, 18, 100, 100));
		}

		@DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(6749, Rectangle2afp.calculatesDistanceSquaredRectanglePoint(5, 8, 10, 18, 0, 100));
		}

		@DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(25, Rectangle2afp.calculatesDistanceSquaredRectanglePoint(5, 8, 10, 18, 0, 10));
		}

		@DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(64, Rectangle2afp.calculatesDistanceSquaredRectanglePoint(5, 8, 10, 18, 7, 0));
		}

		@DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(20736, Rectangle2afp.calculatesDistanceSquaredRectanglePoint(5, 8, 10, 18, 154, 17));
		}

		@DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(18496, Rectangle2afp.calculatesDistanceSquaredRectanglePoint(5, 8, 10, 18, 9, 154));
		}

		@DisplayName("#9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, Rectangle2afp.calculatesDistanceSquaredRectanglePoint(5, 8, 10, 18, 8, 18));
		}

		@DisplayName("#10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, Rectangle2afp.calculatesDistanceSquaredRectanglePoint(5, 8, 10, 18, 7, 12));
		}

	}

	@DisplayName("getDistanceSquared")
	@Nested
	public class getDistanceSquared {

		@DisplayName("(Point2D) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(88.99998, getS().getDistanceSquared(createPoint(0, 0)));
		}

		@DisplayName("(Point2D) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(8164, getS().getDistanceSquared(createPoint(100, 0)));
		}

		@DisplayName("(Point2D) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(14823.99999, getS().getDistanceSquared(createPoint(100, 100)));
		}

		@DisplayName("(Point2D) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(6749, getS().getDistanceSquared(createPoint(0, 100)));
		}

		@DisplayName("(Point2D) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(25, getS().getDistanceSquared(createPoint(0, 10)));
		}

		@DisplayName("(Point2D) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(64, getS().getDistanceSquared(createPoint(7, 0)));
		}

		@DisplayName("(Point2D) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(20736, getS().getDistanceSquared(createPoint(154, 17)));
		}

		@DisplayName("(Point2D) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(18496, getS().getDistanceSquared(createPoint(9, 154)));
		}

		@DisplayName("(Point2D) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createPoint(8, 18)));
		}

		@DisplayName("(Point2D) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createPoint(7, 12)));
		}

		@DisplayName("(Circle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(71.13204, getS().getDistanceSquared(createCircle(0, 0, 1)));
		}

		@DisplayName("(Circle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(36, getS().getDistanceSquared(createCircle(-2, 14, 1)));
		}

		@DisplayName("(Circle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(32.58359, getS().getDistanceSquared(createCircle(-1, 21, 1)));
		}

		@DisplayName("(Circle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4, getS().getDistanceSquared(createCircle(7, 21, 1)));
		}

		@DisplayName("(Circle2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createCircle(9, 13, 1)));
		}

		@DisplayName("(Circle2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(9, getS().getDistanceSquared(createCircle(8, 4, 1)));
		}

		@DisplayName("(Circle2afp) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(139.3875, getS().getDistanceSquared(createCircle(20, 0, 1)));
		}

		@DisplayName("(Circle2afp) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(64, getS().getDistanceSquared(createCircle(19, 14, 1)));
		}

		@DisplayName("(Circle2afp) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(56.91199, getS().getDistanceSquared(createCircle(18, 21, 1)));
		}

		@DisplayName("(Circle2afp) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createCircle(5, 18, 1)));
		}

		@DisplayName("(Circle2afp) #11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.04869, getS().getDistanceSquared(createCircle(4.3, 7, 1)));
		}

		@DisplayName("(Rectangle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(65, getS().getDistanceSquared(createRectangle(0, 0, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(16, getS().getDistanceSquared(createRectangle(0, 12, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(25, getS().getDistanceSquared(createRectangle(0, 21, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(49, getS().getDistanceSquared(createRectangle(7, 0, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRectangle(8, 12, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(9, getS().getDistanceSquared(createRectangle(9, 21, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(74, getS().getDistanceSquared(createRectangle(15, 0, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(36, getS().getDistanceSquared(createRectangle(16, 12, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(58, getS().getDistanceSquared(createRectangle(17, 21, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRectangle(1, 12, 10, 1)));
		}

		@DisplayName("(Rectangle2afp) #11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRectangle(1, 12, 10, 20)));
		}

		@DisplayName("(Rectangle2afp) #12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRectangle(8, 0, 1, 20)));
		}

		@DisplayName("(Rectangle2afp) #13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRectangle(8, 0, 10, 20)));
		}

		@DisplayName("(Rectangle2afp) #14")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_14(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRectangle(6, 10, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #15")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_15(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRectangle(4, 12, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #16")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_16(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRectangle(4.1, 12, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #17")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_17(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRectangle(4.2, 12, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #18")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_18(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRectangle(4.3, 12, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #19")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_19(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRectangle(4.4, 12, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #20")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_20(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRectangle(4.5, 12, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #21")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_21(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRectangle(4.6, 12, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #22")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_22(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRectangle(4.7, 12, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #23")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_23(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRectangle(4.8, 12, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #24")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_24(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRectangle(4.9, 12, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #25")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_25(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRectangle(5, 12, 1, 1)));
		}

		@DisplayName("(Rectangle2afp) #26")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_26(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRectangle(5.1, 12, 1, 1)));
		}

		@DisplayName("(Segment2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(58, getS().getDistanceSquared(createSegment(0, 0, 2, 1)));
		}

		@DisplayName("(Segment2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(52, getS().getDistanceSquared(createSegment(0, 0, 1, 2)));
		}

		@DisplayName("(Segment2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, getS().getDistanceSquared(createSegment(1, 10, 4, 14)));
		}

		@DisplayName("(Segment2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createSegment(1, 10, 6, 14)));
		}

		@DisplayName("(Segment2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createSegment(1, 10, 18, 14)));
		}

		@DisplayName("(Segment2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createSegment(7, 10, 18, 14)));
		}

		@DisplayName("(Segment2afp) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(49, getS().getDistanceSquared(createSegment(17, 10, 18, 14)));
		}

		@DisplayName("(Segment2afp) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4, getS().getDistanceSquared(createSegment(6, 0, 9, 6)));
		}

		@DisplayName("(Segment2afp) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createSegment(6, 0, 9, 16)));
		}

		@DisplayName("(Segment2afp) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createSegment(6, 0, 9, 21)));
		}

		@DisplayName("(Segment2afp) #11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createSegment(6, 10, 9, 21)));
		}

		@DisplayName("(Segment2afp) #12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, getS().getDistanceSquared(createSegment(6, 19, 9, 21)));
		}

		@DisplayName("(Segment2afp) #13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createSegment(8, 20, 14, 8)));
		}

		@DisplayName("(MultiShape2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void multishape_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(53, getS().getDistanceSquared(createTestMultiShape(-10, 7)));
		}

		@DisplayName("(MultiShape2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void multishape_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(101, getS().getDistanceSquared(createTestMultiShape(0, -4)));
		}

		@DisplayName("(MultiShape2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void multishape_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createTestMultiShape(4, 6)));
		}

		@DisplayName("(MultiShape2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void multishape_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, getS().getDistanceSquared(createTestMultiShape(8, 6)));
		}

		@DisplayName("(MultiShape2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void multishape_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createTestMultiShape(9, 6)));
		}

		@DisplayName("(Triangle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5, getS().getDistanceSquared(createTestTriangle(0, 4)));
		}

		@DisplayName("(Triangle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.2, getS().getDistanceSquared(createTestTriangle(10, 6)));
		}

		@DisplayName("(Triangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createTestTriangle(4, 16)));
		}

		@DisplayName("(Triangle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, getS().getDistanceSquared(createTestTriangle(6, 19)));
		}

		@DisplayName("(Triangle2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createTestTriangle(10.5, 17.1)));
		}

		@DisplayName("(Path2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3.55882, getS().getDistanceSquared(createSimpleTestPath(0, 0, false)));
		}

		@DisplayName("(Path2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createSimpleTestPath(0, 0, true)));
		}

		@DisplayName("(Path2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createSimpleTestPath(0, 10, false)));
		}

		@DisplayName("(Path2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createSimpleTestPath(0, 10, true)));
		}

		@DisplayName("(Path2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4, getS().getDistanceSquared(createSimpleTestPath(14, 2, false)));
		}

		@DisplayName("(Path2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(4, getS().getDistanceSquared(createSimpleTestPath(14, 2, true)));
		}

		@DisplayName("(Path2afp) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.23077, getS().getDistanceSquared(createComplexTestPath(12, 2, false, PathWindingRule.EVEN_ODD)));
		}

		@DisplayName("(Path2afp) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.23077, getS().getDistanceSquared(createComplexTestPath(12, 2, true, PathWindingRule.EVEN_ODD)));
		}

		@DisplayName("(Path2afp) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.4, getS().getDistanceSquared(createComplexTestPath(3, 8, false, PathWindingRule.EVEN_ODD)));
		}

		@DisplayName("(Path2afp) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createComplexTestPath(3, 8, true, PathWindingRule.EVEN_ODD)));
		}

		@DisplayName("(Path2afp) #11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(28.9, getS().getDistanceSquared(createComplexTestPath(-2, 8, false, PathWindingRule.EVEN_ODD)));
		}

		@DisplayName("(Path2afp) #12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(14.6789, getS().getDistanceSquared(createComplexTestPath(-2, 8, true, PathWindingRule.EVEN_ODD)));
		}

		@DisplayName("(Path2afp) #13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1.23077, getS().getDistanceSquared(createComplexTestPath(12, 2, false, PathWindingRule.NON_ZERO)));
		}

		@DisplayName("(Path2afp) #14")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_14(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createComplexTestPath(12, 2, true, PathWindingRule.NON_ZERO)));
		}

		@DisplayName("(Path2afp) #15")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_15(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(.4, getS().getDistanceSquared(createComplexTestPath(3, 8, false, PathWindingRule.NON_ZERO)));
		}

		@DisplayName("(Path2afp) #16")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_16(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createComplexTestPath(3, 8, true, PathWindingRule.NON_ZERO)));
		}

		@DisplayName("(Path2afp) #17")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_17(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(28.9, getS().getDistanceSquared(createComplexTestPath(-2, 8, false, PathWindingRule.NON_ZERO)));
		}

		@DisplayName("(Path2afp) #18")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_18(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(14.6789, getS().getDistanceSquared(createComplexTestPath(-2, 8, true, PathWindingRule.NON_ZERO)));
		}

		@DisplayName("(Ellipse2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(61.90769, getS().getDistanceSquared(createEllipse(0, 0, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, getS().getDistanceSquared(createEllipse(2, 12, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createEllipse(6, 16, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.047502, getS().getDistanceSquared(createEllipse(9.897519745562938, 7.003543789189412, 2, 1)));
		}

		@DisplayName("(Ellipse2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createEllipse(9.5, 9.5, 2, 1)));
		}

		@DisplayName("(RoundRectangle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(65.59024, getS().getDistanceSquared(createRoundRectangle(0, 0, 1, 1, .1, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(16, getS().getDistanceSquared(createRoundRectangle(0, 12, 1, 1, .1, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(25.40199, getS().getDistanceSquared(createRoundRectangle(0, 21, 1, 1, .1, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(49, getS().getDistanceSquared(createRoundRectangle(7, 0, 1, 1, .1, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRoundRectangle(8, 12, 1, 1, .1, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(9, getS().getDistanceSquared(createRoundRectangle(9, 21, 1, 1, .1, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(74.68163, getS().getDistanceSquared(createRoundRectangle(15, 0, 1, 1, .1, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(36, getS().getDistanceSquared(createRoundRectangle(16, 12, 1, 1, .1, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(58.48055, getS().getDistanceSquared(createRoundRectangle(17, 21, 1, 1, .1, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRoundRectangle(1, 12, 10, 1, .1, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRoundRectangle(1, 12, 10, 20, .1, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRoundRectangle(8, 0, 1, 20, .1, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRoundRectangle(8, 0, 10, 20, .1, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #14")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_14(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRoundRectangle(6, 10, 1, 1, .1, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #15")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_15(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRoundRectangle(4, 12, 1, 1, .1, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #16")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_16(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRoundRectangle(4.1, 12, 1, 1, .1, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #17")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_17(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRoundRectangle(4.2, 12, 1, 1, .1, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #18")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_18(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRoundRectangle(4.3, 12, 1, 1, .1, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #19")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_19(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRoundRectangle(4.4, 12, 1, 1, .1, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #20")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_20(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRoundRectangle(4.5, 12, 1, 1, .1, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #21")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_21(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRoundRectangle(4.6, 12, 1, 1, .1, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #22")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_22(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRoundRectangle(4.7, 12, 1, 1, .1, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #23")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_23(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRoundRectangle(4.8, 12, 1, 1, .1, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #24")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_24(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRoundRectangle(4.9, 12, 1, 1, .1, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #25")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_25(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRoundRectangle(5, 12, 1, 1, .1, .1)));
		}

		@DisplayName("(RoundRectangle2afp) #26")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_26(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createRoundRectangle(5.1, 12, 1, 1, .1, .1)));
		}

		@DisplayName("(Parallelogram2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(58.82387, getS().getDistanceSquared(createTestParallelogram(0, 0)));
		}

		@DisplayName("(Parallelogram2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.23439, getS().getDistanceSquared(createTestParallelogram(2, 10)));
		}

		@DisplayName("(Parallelogram2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2.39611, getS().getDistanceSquared(createTestParallelogram(14, 18)));
		}

		@DisplayName("(Parallelogram2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createTestParallelogram(12, 14)));
		}

		@DisplayName("(Parallelogram2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(33.11829, getS().getDistanceSquared(createTestParallelogram(14, 2)));
		}

		@DisplayName("(OrientedRectangle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(54.38454, getS().getDistanceSquared(createTestOrientedRectangle(0, 0)));
		}

		@DisplayName("(OrientedRectangle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.71018, getS().getDistanceSquared(createTestOrientedRectangle(2, 10)));
		}

		@DisplayName("(OrientedRectangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createTestOrientedRectangle(12, 8)));
		}

		@DisplayName("(OrientedRectangle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceSquared(createTestOrientedRectangle(8, 12)));
		}

		@DisplayName("(OrientedRectangle2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0.58529, getS().getDistanceSquared(createTestOrientedRectangle(6, 20)));
		}

	}

	@DisplayName("getDistanceL1")
	@Nested
	public class getDistanceL1 {

		@DisplayName("(Point2D) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(13, getS().getDistanceL1(createPoint(0, 0)));
		}

		@DisplayName("(Point2D) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(98, getS().getDistanceL1(createPoint(100, 0)));
		}

		@DisplayName("(Point2D) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(172, getS().getDistanceL1(createPoint(100, 100)));
		}

		@DisplayName("(Point2D) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(87, getS().getDistanceL1(createPoint(0, 100)));
		}

		@DisplayName("(Point2D) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5, getS().getDistanceL1(createPoint(0, 10)));
		}

		@DisplayName("(Point2D) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(8, getS().getDistanceL1(createPoint(7, 0)));
		}

		@DisplayName("(Point2D) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(144, getS().getDistanceL1(createPoint(154, 17)));
		}

		@DisplayName("(Point2D) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(136, getS().getDistanceL1(createPoint(9, 154)));
		}

		@DisplayName("(Point2D) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceL1(createPoint(8, 18)));
		}

		@DisplayName("(Point2D) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceL1(createPoint(7, 12)));
		}

	}

	@DisplayName("getDistanceLinf")
	@Nested
	public class getDistanceLinf {

		@DisplayName("(Point2D) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(8, getS().getDistanceLinf(createPoint(0, 0)));
		}

		@DisplayName("(Point2D) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(90, getS().getDistanceLinf(createPoint(100, 0)));
		}

		@DisplayName("(Point2D) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(90, getS().getDistanceLinf(createPoint(100, 100)));
		}

		@DisplayName("(Point2D) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(82, getS().getDistanceLinf(createPoint(0, 100)));
		}

		@DisplayName("(Point2D) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5, getS().getDistanceLinf(createPoint(0, 10)));
		}

		@DisplayName("(Point2D) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(8, getS().getDistanceLinf(createPoint(7, 0)));
		}

		@DisplayName("(Point2D) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(144, getS().getDistanceLinf(createPoint(154, 17)));
		}

		@DisplayName("(Point2D) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(136, getS().getDistanceLinf(createPoint(9, 154)));
		}

		@DisplayName("(Point2D) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceLinf(createPoint(8, 18)));
		}

		@DisplayName("(Point2D) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().getDistanceLinf(createPoint(7, 12)));
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
			getS().set((T) createRectangle(123.456, 456.789, 789.123, 159.753));
			assertEpsilonEquals(123.456, getS().getMinX());
			assertEpsilonEquals(456.789, getS().getMinY());
			assertEpsilonEquals(912.579, getS().getMaxX());
			assertEpsilonEquals(616.542, getS().getMaxY());
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
			assertElement(pi, PathElementType.MOVE_TO, 5, 8);
			assertElement(pi, PathElementType.LINE_TO, 10, 8);
			assertElement(pi, PathElementType.LINE_TO, 10, 18);
			assertElement(pi, PathElementType.LINE_TO, 5, 18);
			assertElement(pi, PathElementType.CLOSE, 5, 8);
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
			assertElement(pi, PathElementType.MOVE_TO, 5, 8);
			assertElement(pi, PathElementType.LINE_TO, 10, 8);
			assertElement(pi, PathElementType.LINE_TO, 10, 18);
			assertElement(pi, PathElementType.LINE_TO, 5, 18);
			assertElement(pi, PathElementType.CLOSE, 5, 8);
			assertNoElement(pi);
		}

		@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var pi = getS().getPathIterator(new Transform2D());
			assertElement(pi, PathElementType.MOVE_TO, 5, 8);
			assertElement(pi, PathElementType.LINE_TO, 10, 8);
			assertElement(pi, PathElementType.LINE_TO, 10, 18);
			assertElement(pi, PathElementType.LINE_TO, 5, 18);
			assertElement(pi, PathElementType.CLOSE, 5, 8);
			assertNoElement(pi);
		}

		@DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Transform2D tr;
			tr = new Transform2D();
			tr.setTranslation(123.456, 456.789);
			var pi = getS().getPathIterator(tr);
			assertElement(pi, PathElementType.MOVE_TO, 128.456, 464.789);
			assertElement(pi, PathElementType.LINE_TO, 133.456, 464.789);
			assertElement(pi, PathElementType.LINE_TO, 133.456, 474.789);
			assertElement(pi, PathElementType.LINE_TO, 128.456, 474.789);
			assertElement(pi, PathElementType.CLOSE, 128.456, 464.789);
			assertNoElement(pi);
		}

	}

	@DisplayName("createTransformedShape(Transform2D)")
	@Nested
	public class CreateTransformedShape {

		@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Transform2D tr;
			tr = new Transform2D();
			tr.setTranslation(123.456, 456.789);
			PathIterator2afp pi = getS().createTransformedShape(tr).getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 128.456, 464.789);
			assertElement(pi, PathElementType.LINE_TO, 133.456, 464.789);
			assertElement(pi, PathElementType.LINE_TO, 133.456, 474.789);
			assertElement(pi, PathElementType.LINE_TO, 128.456, 474.789);
			assertElement(pi, PathElementType.CLOSE, 128.456, 464.789);
			assertNoElement(pi);
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
			assertFalse(createRectangle(0, 0, 1, 1).contains(getS()));
		}

		@DisplayName("(Rectangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(0, 20, 1, 2)));
		}

		@DisplayName("(Rectangle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(createRectangle(0, 20, 1, 2).contains(getS()));
		}

		@DisplayName("(Rectangle2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(0, 0, 5, 100)));
		}

		@DisplayName("(Rectangle2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(createRectangle(0, 0, 5, 100).contains(getS()));
		}

		@DisplayName("(Rectangle2afp) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createRectangle(0, 0, 5.1, 100)));
		}

		@DisplayName("(Rectangle2afp) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(createRectangle(0, 0, 5.1, 100).contains(getS()));
		}

		@DisplayName("(Rectangle2afp) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createRectangle(6, 9, .5, 9)));
		}

		@DisplayName("(Rectangle2afp) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(createRectangle(6, 9, .5, 9).contains(getS()));
		}

		@DisplayName("(Shape2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void shape_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createCircle(0, 0, 1)));
		}

		@DisplayName("(Shape2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void shape_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createCircle(0, 20, 1)));
		}

		@DisplayName("(Shape2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void shape_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createCircle(0, 0, 5)));
		}

		@DisplayName("(Shape2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void shape_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().contains(createCircle(0, 0, 5.1)));
		}

		@DisplayName("(Shape2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void shape_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().contains(createCircle(6, 9, .5)));
		}

		@DisplayName("(double,double) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertFalse(getS().contains(20, 45));
		}

		@DisplayName("(double,double) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertFalse(getS().contains(20, 55));
		}

		@DisplayName("(double,double) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertFalse(getS().contains(20, 0));
		}

		@DisplayName("(double,double) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertFalse(getS().contains(0, 45));
		}

		@DisplayName("(double,double) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertFalse(getS().contains(5, 45));
		}

		@DisplayName("(double,double) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertFalse(getS().contains(40, 55));
		}

		@DisplayName("(double,double) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertFalse(getS().contains(40, 0));
		}

		@DisplayName("(double,double) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertFalse(getS().contains(0, 40));
		}

		@DisplayName("(double,double) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertFalse(getS().contains(20, 100));
		}

		@DisplayName("(double,double) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertFalse(getS().contains(100, 45));
		}

		@DisplayName("(double,double) #11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertFalse(getS().contains(-100, 45));
		}

		@DisplayName("(double,double) #12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertFalse(getS().contains(-100, 60));
		}

		@DisplayName("(double,double) #13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertTrue(getS().contains(10, 12));
		}

		@DisplayName("(double,double) #14")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_14(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertTrue(getS().contains(40, 12));
		}

		@DisplayName("(double,double) #15")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_15(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertTrue(getS().contains(40, 37));
		}

		@DisplayName("(double,double) #16")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_16(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertTrue(getS().contains(10, 37));
		}

		@DisplayName("(double,double) #17")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_17(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertTrue(getS().contains(35, 24));
		}

		@DisplayName("(Point2D) #18")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_18(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertFalse(getS().contains(createPoint(20, 45)));
		}

		@DisplayName("(Point2D) #19")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_19(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertFalse(getS().contains(createPoint(20, 55)));
		}

		@DisplayName("(Point2D) #20")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_20(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertFalse(getS().contains(createPoint(20, 0)));
		}

		@DisplayName("(Point2D) #21")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_21(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertFalse(getS().contains(createPoint(0, 45)));
		}

		@DisplayName("(Point2D) #22")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_22(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertFalse(getS().contains(createPoint(5, 45)));
		}

		@DisplayName("(Point2D) #23")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_23(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertFalse(getS().contains(createPoint(40, 55)));
		}

		@DisplayName("(Point2D) #24")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_24(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertFalse(getS().contains(createPoint(40, 0)));
		}

		@DisplayName("(Point2D) #25")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_25(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertFalse(getS().contains(createPoint(0, 40)));
		}

		@DisplayName("(Point2D) #26")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_26(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertFalse(getS().contains(createPoint(20, 100)));
		}

		@DisplayName("(Point2D) #27")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_27(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertFalse(getS().contains(createPoint(100, 45)));
		}

		@DisplayName("(Point2D) #28")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_28(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertFalse(getS().contains(createPoint(-100, 45)));
		}

		@DisplayName("(Point2D) #29")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_29(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertFalse(getS().contains(createPoint(-100, 60)));
		}

		@DisplayName("(Point2D) #30")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_30(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertTrue(getS().contains(createPoint(10, 12)));
		}

		@DisplayName("(Point2D) #31")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_31(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertTrue(getS().contains(createPoint(40, 12)));
		}

		@DisplayName("(Point2D) #32")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_32(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertTrue(getS().contains(createPoint(40, 37)));
		}

		@DisplayName("(Point2D) #33")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_33(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertTrue(getS().contains(createPoint(10, 37)));
		}

		@DisplayName("(Point2D) #34")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void point_(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertTrue(getS().contains(createPoint(35, 24)));
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
			assertFalse(createRectangle(0, 0, 1, 1).intersects(getS()));
		}

		@DisplayName("(Rectangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createRectangle(0, 20, 1, 2)));
		}

		@DisplayName("(Rectangle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(createRectangle(0, 20, 1, 2).intersects(getS()));
		}

		@DisplayName("(Rectangle2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createRectangle(0, 0, 5, 100)));
		}

		@DisplayName("(Rectangle2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(createRectangle(0, 0, 5, 100).intersects(getS()));
		}

		@DisplayName("(Rectangle2afp) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createRectangle(0, 0, 5.1, 100)));
		}

		@DisplayName("(Rectangle2afp) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(createRectangle(0, 0, 5.1, 100).intersects(getS()));
		}

		@DisplayName("(Rectangle2afp) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createRectangle(6, 9, .5, 9)));
		}

		@DisplayName("(Rectangle2afp) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(createRectangle(6, 9, .5, 9).intersects(getS()));
		}

		@DisplayName("(Rectangle2afp) #11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createRectangle(0, 0, 5.1, 8.1)));
		}

		@DisplayName("(Rectangle2afp) #12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void rectangle_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(createRectangle(0, 0, 5.1, 8.1).intersects(getS()));
		}

		@DisplayName("(Triangle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Triangle2afp triangle = createTriangle(5, 8, -10, 1, -1, -2);
			assertFalse(createRectangle(-6, -2, 1, 1).intersects(triangle));
		}

		@DisplayName("(Triangle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Triangle2afp triangle = createTriangle(5, 8, -10, 1, -1, -2);
			assertFalse(createRectangle(-6, 6, 1, 1).intersects(triangle));
		}

		@DisplayName("(Triangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Triangle2afp triangle = createTriangle(5, 8, -10, 1, -1, -2);
			assertFalse(createRectangle(6, 6, 1, 1).intersects(triangle));
		}

		@DisplayName("(Triangle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Triangle2afp triangle = createTriangle(5, 8, -10, 1, -1, -2);
			assertFalse(createRectangle(-16, 0, 1, 1).intersects(triangle));
		}

		@DisplayName("(Triangle2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Triangle2afp triangle = createTriangle(5, 8, -10, 1, -1, -2);
			assertFalse(createRectangle(12, 12, 1, 1).intersects(triangle));
		}

		@DisplayName("(Triangle2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Triangle2afp triangle = createTriangle(5, 8, -10, 1, -1, -2);
			assertFalse(createRectangle(0, -6, 1, 1).intersects(triangle));
		}

		@DisplayName("(Triangle2afp) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Triangle2afp triangle = createTriangle(5, 8, -10, 1, -1, -2);
			assertTrue(createRectangle(-4, 2, 1, 1).intersects(triangle));
		}

		@DisplayName("(Triangle2afp) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Triangle2afp triangle = createTriangle(5, 8, -10, 1, -1, -2);
			assertTrue(createRectangle(-4, 4, 1, 1).intersects(triangle));
		}

		@DisplayName("(Triangle2afp) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Triangle2afp triangle = createTriangle(5, 8, -10, 1, -1, -2);
			assertTrue(createRectangle(0, 6, 1, 1).intersects(triangle));
		}

		@DisplayName("(Triangle2afp) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Triangle2afp triangle = createTriangle(5, 8, -10, 1, -1, -2);
			assertTrue(createRectangle(2, 4, 1, 1).intersects(triangle));
		}

		@DisplayName("(Triangle2afp) #11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void triangle_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Triangle2afp triangle = createTriangle(5, 8, -10, 1, -1, -2);
			assertFalse(createRectangle(5, 8, 1, 1).intersects(triangle));
		}

		@DisplayName("(Circle2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(createRectangle(0, 0, .5, .5).intersects(createCircle(0, 0, 1)));
		}

		@DisplayName("(Circle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(createRectangle(0, 0, 1, 1).intersects(createCircle(0, 0, 1)));
		}

		@DisplayName("(Circle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(createRectangle(0, 0, .5, 1).intersects(createCircle(0, 0, 1)));
		}

		@DisplayName("(Circle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void circle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(createRectangle(0, 0, 1, 1).intersects(createCircle(2, 0, 1)));
		}

		@DisplayName("(Ellipse2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(createRectangle(0, 0, 1, 1).intersects(createEllipse(0, 0, 1, 1)));
		}

		@DisplayName("(Ellipse2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(createRectangle(-5, -5, 1, 1).intersects(createEllipse(0, 0, 1, 1)));
		}

		@DisplayName("(Ellipse2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(createRectangle(.5, .5, 5, 5).intersects(createEllipse(0, 0, 1, 1)));
		}

		@DisplayName("(Ellipse2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(createRectangle(.5, .5, 5, .6).intersects(createEllipse(0, 0, 1, 1)));
		}

		@DisplayName("(Ellipse2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(createRectangle(-5, -5, 5, 5).intersects(createEllipse(0, 0, 1, 1)));
		}

		@DisplayName("(Ellipse2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(createRectangle(-9, -9, 4, 4).intersects(createEllipse(0, 0, 1, 1)));
		}

		@DisplayName("(Ellipse2afp) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(createRectangle(-5, -9, 4, 4).intersects(createEllipse(0, 0, 1, 1)));
		}

		@DisplayName("(Ellipse2afp) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(createRectangle(5, -5, 6, 5).intersects(createEllipse(0, 0, 1, 1)));
		}

		@DisplayName("(Ellipse2afp) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(createRectangle(-5, -5, 0, 0).intersects(createEllipse(0, 0, 1, 1)));
		}

		@DisplayName("(Ellipse2afp) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(createRectangle(-5, -5, .1, .1).intersects(createEllipse(0, 0, 1, 1)));
		}

		@DisplayName("(Ellipse2afp) #11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void ellipse_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(createRectangle(.25, .25, .5, .5).intersects(createEllipse(0, 0, 1, 1)));
		}

		@DisplayName("(Segment2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(createRectangle(10, 12, 40, 37).intersects(createSegment(20, 45, 43, 15)));
		}

		@DisplayName("(Segment2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(createRectangle(10, 12, 40, 37).intersects(createSegment(20, 55, 43, 15)));
		}

		@DisplayName("(Segment2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(createRectangle(10, 12, 40, 37).intersects(createSegment(20, 0, 43, 15)));
		}

		@DisplayName("(Segment2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(createRectangle(10, 12, 40, 37).intersects(createSegment(0, 45, 43, 15)));
		}

		@DisplayName("(Segment2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(createRectangle(10, 12, 40, 37).intersects(createSegment(20, 45, 60, 15)));
		}

		@DisplayName("(Segment2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(createRectangle(10, 12, 40, 37).intersects(createSegment(5, 45, 30, 55)));
		}

		@DisplayName("(Segment2afp) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(createRectangle(10, 12, 40, 37).intersects(createSegment(40, 55, 60, 15)));
		}

		@DisplayName("(Segment2afp) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(createRectangle(10, 12, 40, 37).intersects(createSegment(40, 0, 60, 40)));
		}

		@DisplayName("(Segment2afp) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(createRectangle(10, 12, 40, 37).intersects(createSegment(0, 40, 20, 0)));
		}

		@DisplayName("(Segment2afp) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(createRectangle(10, 12, 40, 37).intersects(createSegment(0, 45, 100, 15)));
		}

		@DisplayName("(Segment2afp) #11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(createRectangle(10, 12, 40, 37).intersects(createSegment(20, 100, 43, 0)));
		}

		@DisplayName("(Segment2afp) #12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(createRectangle(10, 12, 40, 37).intersects(createSegment(20, 100, 43, 101)));
		}

		@DisplayName("(Segment2afp) #13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(createRectangle(10, 12, 40, 37).intersects(createSegment(100, 45, 102, 15)));
		}

		@DisplayName("(Segment2afp) #14")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_14(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(createRectangle(10, 12, 40, 37).intersects(createSegment(20, 0, 43, -2)));
		}

		@DisplayName("(Segment2afp) #15")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_15(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(createRectangle(10, 12, 50, 49).intersects(createSegment(-100, 45, -48, 15)));
		}

		@DisplayName("(Segment2afp) #16")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void segment_16(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(createRectangle(10, 12, 40, 37).intersects(createSegment(-100, 60, -98, 61)));
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
			p.lineTo(20, 20);
			p.lineTo(-20, 20);
			assertTrue(getS().intersects(p));
		}

		@DisplayName("(Path2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void path_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-20, -20);
			p.lineTo(20, 20);
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
	    public void path_12(CoordinateSystem2D cs) {
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
			p.lineTo(20, 20);
			p.lineTo(-20, 20);
			assertTrue(getS().intersects(p.getPathIterator()));
		}

		@DisplayName("(PathIterator2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void pathiterator_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var p = createPath();
			p.moveTo(-20, -20);
			p.lineTo(20, 20);
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
			OrientedRectangle2afp obr = createOrientedRectangle(
					6, 9,
					0.894427190999916, -0.447213595499958, 13.999990000000002,
					12.999989999999997);
			assertFalse(createRectangle(0, -5, 2, 1).intersects(obr));
		}

		@DisplayName("(OrientedRectangle2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			OrientedRectangle2afp obr = createOrientedRectangle(
					6, 9,
					0.894427190999916, -0.447213595499958, 13.999990000000002,
					12.999989999999997);
			assertTrue(createRectangle(0, -4.5, 2, 1).intersects(obr));
		}

		@DisplayName("(OrientedRectangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			OrientedRectangle2afp obr = createOrientedRectangle(
					6, 9,
					0.894427190999916, -0.447213595499958, 13.999990000000002,
					12.999989999999997);
			assertTrue(createRectangle(0, -4, 2, 1).intersects(obr));
		}

		@DisplayName("(OrientedRectangle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			OrientedRectangle2afp obr = createOrientedRectangle(
					6, 9,
					0.894427190999916, -0.447213595499958, 13.999990000000002,
					12.999989999999997);
			assertTrue(createRectangle(4, 4, 2, 1).intersects(obr));
		}

		@DisplayName("(OrientedRectangle2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			OrientedRectangle2afp obr = createOrientedRectangle(
					6, 9,
					0.894427190999916, -0.447213595499958, 13.999990000000002,
					12.999989999999997);
			assertFalse(createRectangle(20, -2, 2, 1).intersects(obr));
		}

		@DisplayName("(OrientedRectangle2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void orientedrectangle_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			OrientedRectangle2afp obr = createOrientedRectangle(
					6, 9,
					0.894427190999916, -0.447213595499958, 13.999990000000002,
					12.999989999999997);
			assertTrue(createRectangle(-15, -10, 50, 50).intersects(obr));
		}

		@DisplayName("(Parallelogram2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Parallelogram2afp para = createParallelogram(
					6, 9, 2.425356250363330e-01, 9.701425001453320e-01, 9.219544457292887,
					-7.071067811865475e-01, 7.071067811865475e-01, 1.264911064067352e+01);
			assertFalse(createRectangle(0, 0, 1, 1).intersects(para));
		}

		@DisplayName("(Parallelogram2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Parallelogram2afp para = createParallelogram(
					6, 9, 2.425356250363330e-01, 9.701425001453320e-01, 9.219544457292887,
					-7.071067811865475e-01, 7.071067811865475e-01, 1.264911064067352e+01);
			assertTrue(createRectangle(0, 2, 1, 1).intersects(para));
		}

		@DisplayName("(Parallelogram2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Parallelogram2afp para = createParallelogram(
					6, 9, 2.425356250363330e-01, 9.701425001453320e-01, 9.219544457292887,
					-7.071067811865475e-01, 7.071067811865475e-01, 1.264911064067352e+01);
			assertTrue(createRectangle(-5.5, 8.5, 1, 1).intersects(para));
		}

		@DisplayName("(Parallelogram2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Parallelogram2afp para = createParallelogram(
					6, 9, 2.425356250363330e-01, 9.701425001453320e-01, 9.219544457292887,
					-7.071067811865475e-01, 7.071067811865475e-01, 1.264911064067352e+01);
			assertFalse(createRectangle(-6, 16, 1, 1).intersects(para));
		}

		@DisplayName("(Parallelogram2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Parallelogram2afp para = createParallelogram(
					6, 9, 2.425356250363330e-01, 9.701425001453320e-01, 9.219544457292887,
					-7.071067811865475e-01, 7.071067811865475e-01, 1.264911064067352e+01);
			assertFalse(createRectangle(146, 16, 1, 1).intersects(para));
		}

		@DisplayName("(Parallelogram2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Parallelogram2afp para = createParallelogram(
					6, 9, 2.425356250363330e-01, 9.701425001453320e-01, 9.219544457292887,
					-7.071067811865475e-01, 7.071067811865475e-01, 1.264911064067352e+01);
			assertTrue(createRectangle(12, 14, 1, 1).intersects(para));
		}

		@DisplayName("(Parallelogram2afp) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Parallelogram2afp para = createParallelogram(
					6, 9, 2.425356250363330e-01, 9.701425001453320e-01, 9.219544457292887,
					-7.071067811865475e-01, 7.071067811865475e-01, 1.264911064067352e+01);
			assertTrue(createRectangle(0, 8, 1, 1).intersects(para));
		}

		@DisplayName("(Parallelogram2afp) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Parallelogram2afp para = createParallelogram(
					6, 9, 2.425356250363330e-01, 9.701425001453320e-01, 9.219544457292887,
					-7.071067811865475e-01, 7.071067811865475e-01, 1.264911064067352e+01);
			assertTrue(createRectangle(10, -1, 1, 1).intersects(para));
		}

		@DisplayName("(Parallelogram2afp) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void parallelogram_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Parallelogram2afp para = createParallelogram(
					6, 9, 2.425356250363330e-01, 9.701425001453320e-01, 9.219544457292887,
					-7.071067811865475e-01, 7.071067811865475e-01, 1.264911064067352e+01);
			assertTrue(createRectangle(-15, -10, 35, 40).intersects(para));
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
			assertFalse(createRoundRectangle(0, 0, 1, 1, .1, .2).intersects(getS()));
		}

		@DisplayName("(RoundRectangle2afp) #3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createRoundRectangle(0, 20, 1, 2, .1, .2)));
		}

		@DisplayName("(RoundRectangle2afp) #4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(createRoundRectangle(0, 20, 1, 2, .1, .2).intersects(getS()));
		}

		@DisplayName("(RoundRectangle2afp) #5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createRoundRectangle(0, 0, 5, 100, .1, .2)));
		}

		@DisplayName("(RoundRectangle2afp) #6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(createRoundRectangle(0, 0, 5, 100, .1, .2).intersects(getS()));
		}

		@DisplayName("(RoundRectangle2afp) #7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createRoundRectangle(0, 0, 5.1, 100, .1, .2)));
		}

		@DisplayName("(RoundRectangle2afp) #8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(createRoundRectangle(0, 0, 5.1, 100, .1, .2).intersects(getS()));
		}

		@DisplayName("(RoundRectangle2afp) #9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createRoundRectangle(6, 9, .5, 9, .1, .2)));
		}

		@DisplayName("(RoundRectangle2afp) #10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(createRoundRectangle(6, 9, .5, 9, .1, .2).intersects(getS()));
		}

		@DisplayName("(RoundRectangle2afp) #11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(getS().intersects(createRoundRectangle(0, 0, 5.1, 8.1, .1, .2)));
		}

		@DisplayName("(RoundRectangle2afp) #12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(createRoundRectangle(0, 0, 5.1, 8.1, .1, .2).intersects(getS()));
		}

		@DisplayName("(RoundRectangle2afp) #13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().intersects(createRoundRectangle(0, 0, 5.01, 8.01, .1, .2)));
		}

		@DisplayName("(RoundRectangle2afp) #14")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void roundrectangle_14(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFalse(createRoundRectangle(0, 0, 5.01, 8.01, .1, .2).intersects(getS()));
		}

		@DisplayName("(Shape2afp) #1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void shape_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(createRectangle(0, 0, 5.1, 100).intersects((Shape2D) getS()));
		}

		@DisplayName("(Shape2afp) #2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void shape_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(createRectangle(.25, .25, .5, .5).intersects((Shape2D) createEllipse(0, 0, 1, 1)));
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
			Transform2D tr;
			tr = new Transform2D();
			tr.setTranslation(123.456, 456.789);
			PathIterator2afp pi = getS().operator_multiply(tr).getPathIterator();
			assertElement(pi, PathElementType.MOVE_TO, 128.456, 464.789);
			assertElement(pi, PathElementType.LINE_TO, 133.456, 464.789);
			assertElement(pi, PathElementType.LINE_TO, 133.456, 474.789);
			assertElement(pi, PathElementType.LINE_TO, 128.456, 474.789);
			assertElement(pi, PathElementType.CLOSE, 128.456, 464.789);
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
			getS().set(10, 12, 30, 25);
			assertFalse(getS().operator_and(createPoint(20, 45)));
		}

		@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertFalse(getS().operator_and(createPoint(20, 55)));
		}

		@DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertFalse(getS().operator_and(createPoint(20, 0)));
		}

		@DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertFalse(getS().operator_and(createPoint(0, 45)));
		}

		@DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertFalse(getS().operator_and(createPoint(5, 45)));
		}

		@DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertFalse(getS().operator_and(createPoint(40, 55)));
		}

		@DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertFalse(getS().operator_and(createPoint(40, 0)));
		}

		@DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertFalse(getS().operator_and(createPoint(0, 40)));
		}

		@DisplayName("#9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertFalse(getS().operator_and(createPoint(20, 100)));
		}

		@DisplayName("#10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertFalse(getS().operator_and(createPoint(100, 45)));
		}

		@DisplayName("#11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertFalse(getS().operator_and(createPoint(-100, 45)));
		}

		@DisplayName("#12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertFalse(getS().operator_and(createPoint(-100, 60)));
		}

		@DisplayName("#13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertTrue(getS().operator_and(createPoint(10, 12)));
		}

		@DisplayName("#14")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_14(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertTrue(getS().operator_and(createPoint(40, 12)));
		}

		@DisplayName("#15")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_15(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertTrue(getS().operator_and(createPoint(40, 37)));
		}

		@DisplayName("#16")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_16(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertTrue(getS().operator_and(createPoint(10, 37)));
		}

		@DisplayName("#17")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_17(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().set(10, 12, 30, 25);
			assertTrue(getS().operator_and(createPoint(35, 24)));
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
	    	assertTrue(createRectangle(0, 0, 5.1, 100).operator_and(getS()));
		}

		@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	assertTrue(createRectangle(.25, .25, .5, .5).operator_and(createEllipse(0, 0, 1, 1)));
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
			assertEpsilonEquals(9.43398, getS().operator_upTo(createPoint(0, 0)));
		}

		@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(90.35486, getS().operator_upTo(createPoint(100, 0)));
		}

		@DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(121.75385, getS().operator_upTo(createPoint(100, 100)));
		}

		@DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(82.1523, getS().operator_upTo(createPoint(0, 100)));
		}

		@DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5, getS().operator_upTo(createPoint(0, 10)));
		}

		@DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(8, getS().operator_upTo(createPoint(7, 0)));
		}

		@DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(144, getS().operator_upTo(createPoint(154, 17)));
		}

		@DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(136, getS().operator_upTo(createPoint(9, 154)));
		}

		@DisplayName("#9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().operator_upTo(createPoint(8, 18)));
		}

		@DisplayName("#10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(0, getS().operator_upTo(createPoint(7, 12)));
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
			Point2D p = getS().getCenter();
			assertNotNull(p);
			assertEpsilonEquals(7.5, p.getX());
			assertEpsilonEquals(13, p.getY());
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

	@DisplayName("setCenter(double,double)")
	@Nested
	public class SetCenterDoubleDouble {

		@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			getS().setCenter(145,  -47);
			assertEpsilonEquals(142.5, getS().getMinX());
			assertEpsilonEquals(-52, getS().getMinY());
			assertEpsilonEquals(147.5, getS().getMaxX());
			assertEpsilonEquals(-42, getS().getMaxY());
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
			getS().setCenterX(145);
			assertEpsilonEquals(142.5, getS().getMinX());
			assertEpsilonEquals(8, getS().getMinY());
			assertEpsilonEquals(147.5, getS().getMaxX());
			assertEpsilonEquals(18, getS().getMaxY());
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
			getS().setCenterY(-47);
			assertEpsilonEquals(5, getS().getMinX());
			assertEpsilonEquals(-52, getS().getMinY());
			assertEpsilonEquals(10, getS().getMaxX());
			assertEpsilonEquals(-42, getS().getMaxY());
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
			assertNotNull(v);
			assertEpsilonEquals(1, v.getX());
			assertEpsilonEquals(0, v.getY());
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
			assertEpsilonEquals(1, getS().getFirstAxisX());
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
			assertEpsilonEquals(0, getS().getFirstAxisY());
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
			assertNotNull(v);
			assertEpsilonEquals(0, v.getX());
			assertEpsilonEquals(1, v.getY());
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
			assertEpsilonEquals(0, getS().getSecondAxisX());
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
			assertEpsilonEquals(1, getS().getSecondAxisY());
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
			assertEpsilonEquals(2.5, getS().getFirstAxisExtent());
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
			getS().setFirstAxisExtent(124);
			assertEpsilonEquals(-116.5, getS().getMinX());
			assertEpsilonEquals(8, getS().getMinY());
			assertEpsilonEquals(131.5, getS().getMaxX());
			assertEpsilonEquals(18, getS().getMaxY());
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
			assertEpsilonEquals(5, getS().getSecondAxisExtent());
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
			getS().setSecondAxisExtent(124);
			assertEpsilonEquals(5, getS().getMinX());
			assertEpsilonEquals(-111, getS().getMinY());
			assertEpsilonEquals(10, getS().getMaxX());
			assertEpsilonEquals(137, getS().getMaxY());
		}

	}

	@DisplayName("setFirstAxis")
	@Nested
	public class SetFirstAxis {

		@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D v = createVector(1,  1).toUnitVector();
			getS().setFirstAxis(v.getX(), v.getY(), 5);
			assertEpsilonEquals(3.96447, getS().getMinX());
			assertEpsilonEquals(9.46446, getS().getMinY());
			assertEpsilonEquals(11.03553, getS().getMaxX());
			assertEpsilonEquals(16.53553, getS().getMaxY());
		}

	}

	@DisplayName("setSecondAxis")
	@Nested
	public class SetSecondAxis {

		@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Vector2D v = createVector(-1,  1).toUnitVector();
			getS().setSecondAxis(v.getX(), v.getY(), 6);
			assertEpsilonEquals(3.25736, getS().getMinX());
			assertEpsilonEquals(8.75736, getS().getMinY());
			assertEpsilonEquals(11.74264, getS().getMaxX());
			assertEpsilonEquals(17.24264, getS().getMaxY());
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
			getS().set(1, 2, -1, 0, 5, 6);
			assertEpsilonEquals(-4, getS().getMinX());
			assertEpsilonEquals(-4, getS().getMinY());
			assertEpsilonEquals(6, getS().getMaxX());
			assertEpsilonEquals(8, getS().getMaxY());
		}

		@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	Vector2D v = createVector(1,  1).toUnitVector();
			getS().set(1, 2, v.getX(), v.getY(), 5, 6);
			assertEpsilonEquals(-3.24264, getS().getMinX());
			assertEpsilonEquals(-2.24264, getS().getMinY());
			assertEpsilonEquals(5.24264, getS().getMaxX());
			assertEpsilonEquals(6.24264, getS().getMaxY());
		}

	}

	@DisplayName("isCWW")
	@Nested
	public class IsCWW {

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
			assertTrue(createRectangle(
					4.7, 15,
					18.02776, 20).isCCW());
		}

		@DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(createRectangle(
					-10, -3,
					2, 1).isCCW());
		}

		@DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertTrue(createRectangle(
					-10, 7,
					1, 2).isCCW());
		}

	}

	@DisplayName("findsClosestPointRectangleRectangle")
	@Nested
	public class FindsClosestPointRectangleRectangle {

		@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var result = createPoint(Double.NaN, Double.NaN);
			Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 0, 0, 1, 1, result);
			assertFpPointEquals(5, 8, result);
		}

		@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			var result = createPoint(Double.NaN, Double.NaN);
			Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 0, 12, 1, 13, result);
			assertFpPointEquals(5, 12.5, result);
		}

		@DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var result = createPoint(Double.NaN, Double.NaN);
			Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 0, 21, 1, 22, result);
			assertFpPointEquals(5, 18, result);
		}

		@DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var result = createPoint(Double.NaN, Double.NaN);
			Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 7, 0, 8, 1, result);
			assertFpPointEquals(7.5, 8, result);
		}

		@DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var result = createPoint(Double.NaN, Double.NaN);
			Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 8, 12, 9, 13, result);
			assertFpPointEquals(8.5, 12.5, result);
		}

		@DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var result = createPoint(Double.NaN, Double.NaN);
			Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 9, 21, 10, 22, result);
			assertFpPointEquals(9.5, 18, result);
		}

		@DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var result = createPoint(Double.NaN, Double.NaN);
			Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 15, 0, 16, 1, result);
			assertFpPointEquals(10, 8, result);
		}

		@DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var result = createPoint(Double.NaN, Double.NaN);
			Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 16, 12, 17, 13, result);
			assertFpPointEquals(10, 12.5, result);
		}

		@DisplayName("#9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var result = createPoint(Double.NaN, Double.NaN);
			Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 17, 21, 18, 22, result);
			assertFpPointEquals(10, 18, result);
		}

		@DisplayName("#10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var result = createPoint(Double.NaN, Double.NaN);
			Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 1, 12, 11, 13, result);
			assertFpPointEquals(6, 12.5, result);
		}

		@DisplayName("#11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var result = createPoint(Double.NaN, Double.NaN);
			Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 1, 12, 11, 32, result);
			assertFpPointEquals(6, 18, result);
		}

		@DisplayName("#12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var result = createPoint(Double.NaN, Double.NaN);
			Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 8, 0, 9, 20, result);
			assertFpPointEquals(8.5, 10, result);
		}

		@DisplayName("#13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var result = createPoint(Double.NaN, Double.NaN);
			Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 8, 0, 18, 20, result);
			assertFpPointEquals(10, 10, result);
		}

		@DisplayName("#14")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_14(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var result = createPoint(Double.NaN, Double.NaN);
			Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 6, 10, 7, 11, result);
			assertFpPointEquals(6.5, 10.5, result);
		}

		@DisplayName("#15")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_15(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var result = createPoint(Double.NaN, Double.NaN);
			Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 4, 12, 5, 13, result);
			assertFpPointEquals(5, 12.5, result);
		}

		@DisplayName("#16")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_16(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var result = createPoint(Double.NaN, Double.NaN);
			Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 4.1, 12, 5.1, 13, result);
			assertFpPointEquals(5, 12.5, result);
		}

		@DisplayName("#17")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_17(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var result = createPoint(Double.NaN, Double.NaN);
			Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 4.2, 12, 5.2, 13, result);
			assertFpPointEquals(5, 12.5, result);
		}

		@DisplayName("#18")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_18(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var result = createPoint(Double.NaN, Double.NaN);
			Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 4.3, 12, 5.3, 13, result);
			assertFpPointEquals(5, 12.5, result);
		}

		@DisplayName("#20")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_20(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var result = createPoint(Double.NaN, Double.NaN);
			Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 4.4, 12, 5.4, 13, result);
			assertFpPointEquals(5, 12.5, result);
		}

		@DisplayName("#21")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_21(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var result = createPoint(Double.NaN, Double.NaN);
			Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 4.5, 12, 5.5, 13, result);
			assertFpPointEquals(5, 12.5, result);
		}

		@DisplayName("#22")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_22(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var result = createPoint(Double.NaN, Double.NaN);
			Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 4.6, 12, 5.6, 13, result);
			assertFpPointEquals(5.1, 12.5, result);
		}

		@DisplayName("#23")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_23(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var result = createPoint(Double.NaN, Double.NaN);
			Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 4.7, 12, 5.7, 13, result);
			assertFpPointEquals(5.2, 12.5, result);
		}

		@DisplayName("#24")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_24(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var result = createPoint(Double.NaN, Double.NaN);
			Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 4.8, 12, 5.8, 13, result);
			assertFpPointEquals(5.3, 12.5, result);
		}

		@DisplayName("#25")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_25(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var result = createPoint(Double.NaN, Double.NaN);
			Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 4.9, 12, 5.9, 13, result);
			assertFpPointEquals(5.4, 12.5, result);
		}

		@DisplayName("#26")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_26(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var result = createPoint(Double.NaN, Double.NaN);
			Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 5, 12, 6, 13, result);
			assertFpPointEquals(5.5, 12.5, result);
		}

		@DisplayName("#27")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_27(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
	    	var result = createPoint(Double.NaN, Double.NaN);
			Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 5.1, 12, 6.1, 13, result);
			assertFpPointEquals(5.6, 12.5, result);
		}

	}

	@DisplayName("findsClosestPointRectangleSegment")
	@Nested
	public class FindsClosestPointRectangleSegment {

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
			Rectangle2afp.findsClosestPointRectangleSegment(5, 8, 10, 18, 0, 0, 2, 1, result);
			assertFpPointEquals(5, 8, result);
		}

		@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Rectangle2afp.findsClosestPointRectangleSegment(5, 8, 10, 18, 0, 0, 1, 2, result);
			assertFpPointEquals(5, 8, result);
		}

		@DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Rectangle2afp.findsClosestPointRectangleSegment(5, 8, 10, 18, 1, 10, 4, 14, result);
			assertFpPointEquals(5, 14, result);
		}

		@DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Rectangle2afp.findsClosestPointRectangleSegment(5, 8, 10, 18, 1, 10, 6, 14, result);
			assertFpPointEquals(5, 13.2, result);
		}

		@DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Rectangle2afp.findsClosestPointRectangleSegment(5, 8, 10, 18, 1, 10, 18, 14, result);
			assertFpPointEquals(5, 10.94118, result);
		}

		@DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Rectangle2afp.findsClosestPointRectangleSegment(5, 8, 10, 18, 7, 10, 18, 14, result);
			assertFpPointEquals(7, 10, result);
		}

		@DisplayName("#7")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_7(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Rectangle2afp.findsClosestPointRectangleSegment(5, 8, 10, 18, 17, 10, 18, 14, result);
			assertFpPointEquals(10, 10, result);
		}

		@DisplayName("#8")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_8(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Rectangle2afp.findsClosestPointRectangleSegment(5, 8, 10, 18, 6, 0, 9, 6, result);
			assertFpPointEquals(9, 8, result);
		}

		@DisplayName("#9")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_9(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Rectangle2afp.findsClosestPointRectangleSegment(5, 8, 10, 18, 6, 0, 9, 16, result);
			assertFpPointEquals(7.5, 8, result);
		}

		@DisplayName("#10")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_10(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Rectangle2afp.findsClosestPointRectangleSegment(5, 8, 10, 18, 6, 0, 9, 21, result);
			assertFpPointEquals(7.14286, 8, result);
		}

		@DisplayName("#11")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_11(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Rectangle2afp.findsClosestPointRectangleSegment(5, 8, 10, 18, 6, 10, 9, 21, result);
			assertFpPointEquals(6, 10, result);
		}

		@DisplayName("#12")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_12(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Rectangle2afp.findsClosestPointRectangleSegment(5, 8, 10, 18, 6, 19, 9, 21, result);
			assertFpPointEquals(6, 18, result);
		}

		@DisplayName("#13")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_13(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Rectangle2afp.findsClosestPointRectangleSegment(5, 8, 10, 18, 8, 20, 14, 8, result);
			assertFpPointEquals(9, 18, result);
		}

		@DisplayName("#14")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_14(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Rectangle2afp.findsClosestPointRectangleSegment(5, 8, 10, 18, 10.593538844843389, 12.775717435385788, 9.484138452392932, 14.439818024061475, result);
			assertFpPointEquals(10, 13.66603, result);
		}

	}

	@DisplayName("findsClosestPointRectangleParallelogram")
	@Nested
	public class FindsClosestPointRectangleParallelogram {

		private Point2D<?, ?> runFindsClosestPointRectangleParallelogram(double cx, double cy) {
			Parallelogram2afp p = createTestParallelogram(cx, cy);
			Point2D<?, ?> result = createPoint(Double.NaN, Double.NaN);
			Rectangle2afp.findsClosestPointRectangleParallelogram(5, 8, 10, 18,
					p.getCenterX(), p.getCenterY(), p.getFirstAxisX(), p.getFirstAxisY(), p.getFirstAxisExtent(),
					p.getSecondAxisX(), p.getSecondAxisY(), p.getSecondAxisExtent(), result);
			return result;
		}

		@DisplayName("#1")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_1(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 8, runFindsClosestPointRectangleParallelogram(0, 0));
		}

		@DisplayName("#2")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_2(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(5, 9.56018, runFindsClosestPointRectangleParallelogram(2, 10));
		}

		@DisplayName("#3")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_3(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(10, 18, runFindsClosestPointRectangleParallelogram(14, 18));
		}

		@DisplayName("#4")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_4(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(9.48414, 14.43982, runFindsClosestPointRectangleParallelogram(12, 14));
		}

		@DisplayName("#5")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_5(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assertFpPointEquals(10, 8, runFindsClosestPointRectangleParallelogram(14, 2));
		}

		@DisplayName("#6")
    	@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
	    public void test_6(CoordinateSystem2D cs) {
	    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			Point2D<?, ?> result = createPoint(Double.NaN, Double.NaN);
			Rectangle2afp.findsClosestPointRectangleParallelogram(
					5, 8, 7, 9,
					9, 5,
					-0.624695047554424, 0.780868809443031, 2,
					0.894427190999917, -0.447213595499958, 1,
					result);
			assertFpPointEquals(6.85618, 8, result);
		}

	}

}