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

package org.arakhne.afc.math.geometry.d1.tests.afp;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.arakhne.afc.math.geometry.base.d1.Point1D;
import org.arakhne.afc.math.geometry.base.d1.Segment1D;
import org.arakhne.afc.math.geometry.base.d1.Shape1D;
import org.arakhne.afc.math.geometry.d1.afp.Rectangle1afp;
import org.arakhne.afc.math.geometry.d1.afp.RectangularShape1afp;
import org.arakhne.afc.math.geometry.d1.general.Shape1DType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("all")
public abstract class AbstractRectangularShape1afpTestCase<
			SH extends RectangularShape1afp<?, ? super SH, ?, ?, ? super SG, ?>,
			SG extends Segment1D<?, ?>,
			B extends Rectangle1afp<?, ?, ?, ?, ?, ?>>
		extends AbstractShape1afpTestCase<SH, SG, B> {

	@Override
    protected final SG createSegment() {
		return createSegment(125.3569, 14587.659, 442.74158, 12473.93215);
    }
	
	/** Create a segment.
	 *
	 * @param x1 the x coordinate of the first segment point.
	 * @param y1 the y coordinate of the first segment point.
	 * @param x2 the x coordinate of the second segment point.
	 * @param y2 the y coordinate of the second segment point.
	 * @return the box
	 */
	protected abstract SG createSegment(double x1, double y1, double x2, double y2);

	@Override
    protected final SH createShape() {
		return createShape(1.235, -3.459, 10.254, 14.963);
    }
	
	/** Create a rectangular shape.
	 *
	 * @param x the x coordinate of the minimal point.
	 * @param y the y coordinate of the minimal point.
	 * @param width the width of the box.
	 * @param width the height of the box.
	 * @return the box
	 */
	protected abstract SH createShape(double x, double y, double width, double height);

	@DisplayName("clear")
	@Nested
	public class Clear {
		
		@DisplayName("#1")
		@Test
		public void test_1() {
			getSH().clear();
			assertSame(getSG(), getSH().getSegment());
			assertEpsilonEquals(0, getSH().getMinX());
			assertEpsilonEquals(0, getSH().getMinY());
			assertEpsilonEquals(0, getSH().getMaxX());
			assertEpsilonEquals(0, getSH().getMaxY());
		}

	}
	
	@DisplayName("getCenter")
	@Nested
	public class GetCenter {
		
		@DisplayName("#1")
		@Test
		public void test_1() {
			var actual = getSH().getCenter();
			assertEpsilonEquals(createPoint(6.362, 4.0225), actual);
		}

	}

	@DisplayName("getCenterX")
	@Nested
	public class GetCenterX {
		
		@DisplayName("#1")
		@Test
		public void test_1() {
			assertEpsilonEquals(6.362, getSH().getCenterX());
		}

	}

	@DisplayName("getCenterY")
	@Nested
	public class GetCenterY {
		
		@DisplayName("#1")
		@Test
		public void test_1() {
			assertEpsilonEquals(4.0225, getSH().getCenterY());
		}

	}

	@DisplayName("getWidth")
	@Nested
	public class GetWidth {
		
		@DisplayName("#1")
		@Test
		public void test_1() {
			assertEpsilonEquals(10.254, getSH().getWidth());
		}

	}

	@DisplayName("getHeight")
	@Nested
	public class GetHeight {
		
		@DisplayName("#1")
		@Test
		public void test_1() {
			assertEpsilonEquals(14.963, getSH().getHeight());
		}

	}

	@DisplayName("getMinX")
	@Nested
	public class GetMinX {
		
		@DisplayName("#1")
		@Test
		public void test_1() {
			assertEpsilonEquals(1.235, getSH().getMinX());
		}

	}

	@DisplayName("getMaxX")
	@Nested
	public class GetMaxX {
		
		@DisplayName("#1")
		@Test
		public void test_1() {
			assertEpsilonEquals(11.489, getSH().getMaxX());
		}

	}

	@DisplayName("getMinY")
	@Nested
	public class GetMinY {
		
		@DisplayName("#1")
		@Test
		public void test_1() {
			assertEpsilonEquals(-3.459, getSH().getMinY());
		}

	}

	@DisplayName("getMaxY")
	@Nested
	public class GetMaxY {
		
		@DisplayName("#1")
		@Test
		public void test_1() {
			assertEpsilonEquals(11.504, getSH().getMaxY());
		}

	}

	@DisplayName("inflate")
	@Nested
	public class Inflate {
		
		@DisplayName("#1")
		@Test
		public void test_1() {
			getSH().inflate(1, 1, 1, 1);
			assertEpsilonEquals(0.235, getSH().getMinX());
			assertEpsilonEquals(-4.459, getSH().getMinY());
			assertEpsilonEquals(12.489, getSH().getMaxX());
			assertEpsilonEquals(12.504, getSH().getMaxY());
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
	        getSH().inflate(0, 0, 0, 0);
	        assertEpsilonEquals(1.235, getSH().getMinX());
	        assertEpsilonEquals(-3.459, getSH().getMinY());
	        assertEpsilonEquals(11.489, getSH().getMaxX());
	        assertEpsilonEquals(11.504, getSH().getMaxY());
	    }

		@DisplayName("#3")
		@Test
		public void test_3() {
	        getSH().inflate(-1, -1, -1, -1);
	        assertEpsilonEquals(2.235, getSH().getMinX());
	        assertEpsilonEquals(-2.459, getSH().getMinY());
	        assertEpsilonEquals(10.489, getSH().getMaxX());
	        assertEpsilonEquals(10.504, getSH().getMaxY());
	    }

		@DisplayName("#4")
		@Test
		public void test_4() {
	        getSH().inflate(2, 0, 3, 0);
	        assertEpsilonEquals(-0.765, getSH().getMinX());
	        assertEpsilonEquals(-3.459, getSH().getMinY());
	        assertEpsilonEquals(14.489, getSH().getMaxX());
	        assertEpsilonEquals(11.504, getSH().getMaxY());
	    }

		@DisplayName("#5")
		@Test
		public void test_5() {
	        getSH().inflate(0, 2, 0, 3);
	        assertEpsilonEquals(1.235, getSH().getMinX());
	        assertEpsilonEquals(-5.459, getSH().getMinY());
	        assertEpsilonEquals(11.489, getSH().getMaxX());
	        assertEpsilonEquals(14.504, getSH().getMaxY());
	    }

		@DisplayName("#6")
		@Test
		public void test_6() {
	        getSH().inflate(1.5, -2.5, 0.5, 3.5);
	        assertEpsilonEquals(-0.265, getSH().getMinX());
	        assertEpsilonEquals(-0.959, getSH().getMinY());
	        assertEpsilonEquals(11.989, getSH().getMaxX());
	        assertEpsilonEquals(15.004, getSH().getMaxY());
	    }

		@DisplayName("#7")
		@Test
		public void test_7() {
	        getSH().inflate(10, 20, 30, 40);
	        assertEpsilonEquals(-8.765, getSH().getMinX());
	        assertEpsilonEquals(-23.459, getSH().getMinY());
	        assertEpsilonEquals(41.489, getSH().getMaxX());
	        assertEpsilonEquals(51.504, getSH().getMaxY());
	    }

		@DisplayName("#8")
		@Test
		public void test_8() {
	        getSH().inflate(-10, -20, -30, -40);
	        assertEpsilonEquals(-18.511, getSH().getMinX());
	        assertEpsilonEquals(-28.496, getSH().getMinY());
	        assertEpsilonEquals(11.235, getSH().getMaxX());
	        assertEpsilonEquals(16.541, getSH().getMaxY());
	    }

		@DisplayName("#9")
		@Test
		public void test_9() {
	        getSH().inflate(0.123, 0.456, 0.789, 0.987);
	        assertEpsilonEquals(1.112, getSH().getMinX());
	        assertEpsilonEquals(-3.915, getSH().getMinY());
	        assertEpsilonEquals(12.278, getSH().getMaxX());
	        assertEpsilonEquals(12.491, getSH().getMaxY());
	    }

		@DisplayName("#10")
		@Test
		public void test_10() {
	        getSH().inflate(1e-12, 2e-12, 3e-12, 4e-12);
	        assertEpsilonEquals(1.235 - 1e-12, getSH().getMinX());
	        assertEpsilonEquals(-3.459 - 2e-12, getSH().getMinY());
	        assertEpsilonEquals(11.489 + 3e-12, getSH().getMaxX());
	        assertEpsilonEquals(11.504 + 4e-12, getSH().getMaxY());
	    }

		@DisplayName("#11")
		@Test
		public void test_11() {
	        getSH().inflate(1, 2, 3, 4);
	        getSH().inflate(5, 6, 7, 8);
	        assertEpsilonEquals(1.235 - 6, getSH().getMinX());
	        assertEpsilonEquals(-4.765, getSH().getMinX());
	        assertEpsilonEquals(-3.459 - (2+6), getSH().getMinY());
	        assertEpsilonEquals(11.489 + (3+7), getSH().getMaxX());
	        assertEpsilonEquals(11.504 + (4+8), getSH().getMaxY());
	    }

	}

	@DisplayName("isEmpty")
	@Nested
	public class IsEmpty {
		
		@DisplayName("#1")
		@Test
		public void test_1() {
			assertFalse(getSH().isEmpty());
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			getSH().clear();
			assertTrue(getSH().isEmpty());
		}

	}

	@DisplayName("set")
	@Nested
	public class Set {
		
		@DisplayName("(double,double,double,double) #1")
		@Test
		public void point_1() {
			getSH().set(456.145, 96.4586, 47.69, 93.412);
			assertSame(getSG(), getSH().getSegment());
			assertEpsilonEquals(456.145, getSH().getMinX());
			assertEpsilonEquals(96.4586, getSH().getMinY());
			assertEpsilonEquals(503.835, getSH().getMaxX());
			assertEpsilonEquals(189.8706, getSH().getMaxY());
		}

		@DisplayName("(double,double,double,double) #2")
		@Test
		public void point_2() {
	        getSH().set(10.0, 20.0, 30.0, 40.0);
	        assertSame(getSG(), getSH().getSegment());
	        assertEpsilonEquals(10.0, getSH().getMinX());
	        assertEpsilonEquals(20.0, getSH().getMinY());
	        assertEpsilonEquals(40.0, getSH().getMaxX());
	        assertEpsilonEquals(60.0, getSH().getMaxY());
	    }

		@DisplayName("(double,double,double,double) #3")
		@Test
		public void point_3() {
	        getSH().set(5.0, 7.0, 0.0, 0.0);
	        assertSame(getSG(), getSH().getSegment());
	        assertEpsilonEquals(5.0, getSH().getMinX());
	        assertEpsilonEquals(7.0, getSH().getMinY());
	        assertEpsilonEquals(5.0, getSH().getMaxX());
	        assertEpsilonEquals(7.0, getSH().getMaxY());
	    }

		@DisplayName("(double,double,double,double) #4")
		@Test
		public void point_4() {
	        getSH().set(-100.5, -200.75, 50.25, 60.5);
	        assertSame(getSG(), getSH().getSegment());
	        assertEpsilonEquals(-100.5, getSH().getMinX());
	        assertEpsilonEquals(-200.75, getSH().getMinY());
	        assertEpsilonEquals(-50.25, getSH().getMaxX());
	        assertEpsilonEquals(-140.25, getSH().getMaxY());
	    }

		@DisplayName("(double,double,double,double) #5")
		@Test
		public void point_5() {
	        getSH().set(100.0, 200.0, 30.0, 40.0);
	        assertSame(getSG(), getSH().getSegment());
	        assertEpsilonEquals(100.0, getSH().getMinX());
	        assertEpsilonEquals(200.0, getSH().getMinY());
	        assertEpsilonEquals(130.0, getSH().getMaxX());
	        assertEpsilonEquals(240.0, getSH().getMaxY());
	    }

		@DisplayName("(double,double,double,double) #6")
		@Test
		public void point_6() {
	        getSH().set(-50.0, 30.0, 100.0, 200.0);
	        assertSame(getSG(), getSH().getSegment());
	        assertEpsilonEquals(-50.0, getSH().getMinX());
	        assertEpsilonEquals(30.0, getSH().getMinY());
	        assertEpsilonEquals(50.0, getSH().getMaxX());
	        assertEpsilonEquals(230.0, getSH().getMaxY());
	    }

		@DisplayName("(double,double,double,double) #7")
		@Test
		public void point_7() {
	        double x = 1.5e9;
	        double y = 2.5e9;
	        double w = 3.2e8;
	        double h = 4.1e8;
	        getSH().set(x, y, w, h);
	        assertSame(getSG(), getSH().getSegment());
	        assertEpsilonEquals(x, getSH().getMinX());
	        assertEpsilonEquals(y, getSH().getMinY());
	        assertEpsilonEquals(x + w, getSH().getMaxX());
	        assertEpsilonEquals(y + h, getSH().getMaxY());
	    }

		@DisplayName("(double,double,double,double) #8")
		@Test
		public void point_8() {
	        double x = -1.5e9;
	        double y = -2.5e9;
	        double w = 3.2e8;
	        double h = 4.1e8;
	        getSH().set(x, y, w, h);
	        assertSame(getSG(), getSH().getSegment());
	        assertEpsilonEquals(x, getSH().getMinX());
	        assertEpsilonEquals(y, getSH().getMinY());
	        assertEpsilonEquals(x + w, getSH().getMaxX());
	        assertEpsilonEquals(y + h, getSH().getMaxY());
	    }

		@DisplayName("(double,double,double,double) #9")
		@Test
		public void point_9() {
	        getSH().set(0.0001, 0.0002, 0.0003, 0.0004);
	        assertSame(getSG(), getSH().getSegment());
	        assertEpsilonEquals(0.0001, getSH().getMinX());
	        assertEpsilonEquals(0.0002, getSH().getMinY());
	        assertEpsilonEquals(0.0004, getSH().getMaxX());
	        assertEpsilonEquals(0.0006, getSH().getMaxY());
	    }

		@DisplayName("(double,double,double,double) #10")
		@Test
		public void point_10() {
	        double x = 1.23456789e-10;
	        double y = 2.3456789e-10;
	        double w = 3.456789e-11;
	        double h = 4.56789e-11;
	        getSH().set(x, y, w, h);
	        assertSame(getSG(), getSH().getSegment());
	        assertEpsilonEquals(x, getSH().getMinX());
	        assertEpsilonEquals(y, getSH().getMinY());
	        assertEpsilonEquals(x + w, getSH().getMaxX());
	        assertEpsilonEquals(y + h, getSH().getMaxY());
	    }

		@DisplayName("(double,double,double,double) #11")
		@Test
		public void point_11() {
	        double x = 123.456;
	        double y = 789.012;
	        double w = 345.678;
	        double h = 901.234;
	        getSH().set(x, y, w, h);
	        assertSame(getSG(), getSH().getSegment());
	        assertEpsilonEquals(x, getSH().getMinX());
	        assertEpsilonEquals(y, getSH().getMinY());
	        assertEpsilonEquals(x + w, getSH().getMaxX());
	        assertEpsilonEquals(y + h, getSH().getMaxY());
	    }

		@DisplayName("(double,double,double,double) #12")
		@Test
		public void point_12() {
	        getSH().set(10, 20, 30, 40);
	        assertSame(getSG(), getSH().getSegment());
	        assertEpsilonEquals(10, getSH().getMinX());
	        assertEpsilonEquals(20, getSH().getMinY());
	        assertEpsilonEquals(40, getSH().getMaxX());
	        assertEpsilonEquals(60, getSH().getMaxY());
	    }

		@DisplayName("(double,double,double,double) #13")
		@Test
		public void point_13() {
	        getSH().set(100, 200, 5, 6);
	        assertSame(getSG(), getSH().getSegment());
	        assertEpsilonEquals(100, getSH().getMinX());
	        assertEpsilonEquals(200, getSH().getMinY());
	        assertEpsilonEquals(105, getSH().getMaxX());
	        assertEpsilonEquals(206, getSH().getMaxY());
	    }

		@DisplayName("(double,double,double,double) #14")
		@Test
		public void point_14() {
	        getSH().set(-50, -60, 15, 25);
	        assertSame(getSG(), getSH().getSegment());
	        assertEpsilonEquals(-50, getSH().getMinX());
	        assertEpsilonEquals(-60, getSH().getMinY());
	        assertEpsilonEquals(-35, getSH().getMaxX());
	        assertEpsilonEquals(-35, getSH().getMaxY());
	    }

		@DisplayName("(Point2D,Point2D) #15")
		@Test
		public void point_15() {
			getSH().set(factory.createPoint2d(456.145, 96.4586), factory.createPoint2d(503.835, 189.8706));
			assertSame(getSG(), getSH().getSegment());
			assertEpsilonEquals(456.145, getSH().getMinX());
			assertEpsilonEquals(96.4586, getSH().getMinY());
			assertEpsilonEquals(503.835, getSH().getMaxX());
			assertEpsilonEquals(189.8706, getSH().getMaxY());
		}

		@DisplayName("(Point2D,Point2D) #16")
		@Test
		public void point_16() {
	        getSH().set(factory.createPoint2d(10.0, 20.0), factory.createPoint2d(40.0, 60.0));
	        assertSame(getSG(), getSH().getSegment());
	        assertEpsilonEquals(10.0, getSH().getMinX());
	        assertEpsilonEquals(20.0, getSH().getMinY());
	        assertEpsilonEquals(40.0, getSH().getMaxX());
	        assertEpsilonEquals(60.0, getSH().getMaxY());
	    }

		@DisplayName("(Point2D,Point2D) #17")
		@Test
		public void point_17() {
	        getSH().set(factory.createPoint2d(5.0, 7.0), factory.createPoint2d(5.0, 7.0));
	        assertSame(getSG(), getSH().getSegment());
	        assertEpsilonEquals(5.0, getSH().getMinX());
	        assertEpsilonEquals(7.0, getSH().getMinY());
	        assertEpsilonEquals(5.0, getSH().getMaxX());
	        assertEpsilonEquals(7.0, getSH().getMaxY());
	    }

		@DisplayName("(Point2D,Point2D) #18")
		@Test
		public void point_18() {
	        getSH().set(factory.createPoint2d(-100.5, -200.75), factory.createPoint2d(-50.25, -140.25));
	        assertSame(getSG(), getSH().getSegment());
	        assertEpsilonEquals(-100.5, getSH().getMinX());
	        assertEpsilonEquals(-200.75, getSH().getMinY());
	        assertEpsilonEquals(-50.25, getSH().getMaxX());
	        assertEpsilonEquals(-140.25, getSH().getMaxY());
	    }

		@DisplayName("(Point2D,Point2D) #19")
		@Test
		public void point_19() {
	        getSH().set(factory.createPoint2d(100.0, 200.0), factory.createPoint2d(130., 240.));
	        assertSame(getSG(), getSH().getSegment());
	        assertEpsilonEquals(100.0, getSH().getMinX());
	        assertEpsilonEquals(200.0, getSH().getMinY());
	        assertEpsilonEquals(130.0, getSH().getMaxX());
	        assertEpsilonEquals(240.0, getSH().getMaxY());
	    }

		@DisplayName("(Point2D,Point2D) #20")
		@Test
		public void point_20() {
	        getSH().set(factory.createPoint2d(-50.0, 30.0), factory.createPoint2d(50.0, 230.0));
	        assertSame(getSG(), getSH().getSegment());
	        assertEpsilonEquals(-50.0, getSH().getMinX());
	        assertEpsilonEquals(30.0, getSH().getMinY());
	        assertEpsilonEquals(50.0, getSH().getMaxX());
	        assertEpsilonEquals(230.0, getSH().getMaxY());
	    }

		@DisplayName("(Point2D,Point2D) #21")
		@Test
		public void point_21() {
	        double x = 1.5e9;
	        double y = 2.5e9;
	        double w = 3.2e8;
	        double h = 4.1e8;
	        getSH().set(factory.createPoint2d(x, y), factory.createPoint2d(x + w, y + h));
	        assertSame(getSG(), getSH().getSegment());
	        assertEpsilonEquals(x, getSH().getMinX());
	        assertEpsilonEquals(y, getSH().getMinY());
	        assertEpsilonEquals(x + w, getSH().getMaxX());
	        assertEpsilonEquals(y + h, getSH().getMaxY());
	    }

		@DisplayName("(Point2D,Point2D) #22")
		@Test
		public void point_22() {
	        double x = -1.5e9;
	        double y = -2.5e9;
	        double w = 3.2e8;
	        double h = 4.1e8;
	        getSH().set(factory.createPoint2d(x, y), factory.createPoint2d(x + w, y + h));
	        assertSame(getSG(), getSH().getSegment());
	        assertEpsilonEquals(x, getSH().getMinX());
	        assertEpsilonEquals(y, getSH().getMinY());
	        assertEpsilonEquals(x + w, getSH().getMaxX());
	        assertEpsilonEquals(y + h, getSH().getMaxY());
	    }

		@DisplayName("(Point2D,Point2D) #23")
		@Test
		public void point_23() {
	        getSH().set(factory.createPoint2d(0.0001, 0.0002), factory.createPoint2d(0.0004, 0.0006));
	        assertSame(getSG(), getSH().getSegment());
	        assertEpsilonEquals(0.0001, getSH().getMinX());
	        assertEpsilonEquals(0.0002, getSH().getMinY());
	        assertEpsilonEquals(0.0004, getSH().getMaxX());
	        assertEpsilonEquals(0.0006, getSH().getMaxY());
	    }

		@DisplayName("(Point2D,Point2D) #24")
		@Test
		public void point_24() {
	        double x = 1.23456789e-10;
	        double y = 2.3456789e-10;
	        double w = 3.456789e-11;
	        double h = 4.56789e-11;
	        getSH().set(factory.createPoint2d(x, y), factory.createPoint2d(x + w, y + h));
	        assertSame(getSG(), getSH().getSegment());
	        assertEpsilonEquals(x, getSH().getMinX());
	        assertEpsilonEquals(y, getSH().getMinY());
	        assertEpsilonEquals(x + w, getSH().getMaxX());
	        assertEpsilonEquals(y + h, getSH().getMaxY());
	    }

		@DisplayName("(Point2D,Point2D) #25")
		@Test
		public void point_25() {
	        double x = 123.456;
	        double y = 789.012;
	        double w = 345.678;
	        double h = 901.234;
	        getSH().set(factory.createPoint2d(x, y), factory.createPoint2d(x + w, y + h));
	        assertSame(getSG(), getSH().getSegment());
	        assertEpsilonEquals(x, getSH().getMinX());
	        assertEpsilonEquals(y, getSH().getMinY());
	        assertEpsilonEquals(x + w, getSH().getMaxX());
	        assertEpsilonEquals(y + h, getSH().getMaxY());
	    }

		@DisplayName("(Point2D,Point2D) #26")
		@Test
		public void point_26() {
	        getSH().set(factory.createPoint2d(10, 20), factory.createPoint2d(40, 60));
	        assertSame(getSG(), getSH().getSegment());
	        assertEpsilonEquals(10, getSH().getMinX());
	        assertEpsilonEquals(20, getSH().getMinY());
	        assertEpsilonEquals(40, getSH().getMaxX());
	        assertEpsilonEquals(60, getSH().getMaxY());
	    }

		@DisplayName("(Point2D,Point2D) #27")
		@Test
		public void point_27() {
	        getSH().set(factory.createPoint2d(100, 200), factory.createPoint2d(105, 206));
	        assertSame(getSG(), getSH().getSegment());
	        assertEpsilonEquals(100, getSH().getMinX());
	        assertEpsilonEquals(200, getSH().getMinY());
	        assertEpsilonEquals(105, getSH().getMaxX());
	        assertEpsilonEquals(206, getSH().getMaxY());
	    }

		@DisplayName("(Point2D,Point2D) #28")
		@Test
		public void point_28() {
	        getSH().set(factory.createPoint2d(-50, -60), factory.createPoint2d(-35, -35));
	        assertSame(getSG(), getSH().getSegment());
	        assertEpsilonEquals(-50, getSH().getMinX());
	        assertEpsilonEquals(-60, getSH().getMinY());
	        assertEpsilonEquals(-35, getSH().getMaxX());
	        assertEpsilonEquals(-35, getSH().getMaxY());
	    }

	}

	@DisplayName("setFromCenter")
	@Nested
	public class SetFromCenter {

		@DisplayName("(double,double,double,double) #1")
		@Test
		public void point_1() {
			getSH().setFromCenter(0,  0, 1, 1);
	        assertEpsilonEquals(-1., getSH().getMinX());
	        assertEpsilonEquals(-1., getSH().getMinY());
	        assertEpsilonEquals(1., getSH().getMaxX());
	        assertEpsilonEquals(1., getSH().getMaxY());
		}

		@DisplayName("(double,double,double,double) #2")
		@Test
		public void point_2() {
	        getSH().setFromCenter(0, 0, 2, 3);
	        assertEpsilonEquals(-2.0, getSH().getMinX());
	        assertEpsilonEquals(-3.0, getSH().getMinY());
	        assertEpsilonEquals(2.0, getSH().getMaxX());
	        assertEpsilonEquals(3.0, getSH().getMaxY());
	    }

		@DisplayName("(double,double,double,double) #3")
		@Test
		public void point_3() {
	        getSH().setFromCenter(5, 5, 3, 4);
	        assertEpsilonEquals(3.0, getSH().getMinX());
	        assertEpsilonEquals(4.0, getSH().getMinY());
	        assertEpsilonEquals(7.0, getSH().getMaxX());
	        assertEpsilonEquals(6.0, getSH().getMaxY());
	    }

		@DisplayName("(double,double,double,double) #4")
		@Test
		public void point_4() {
	        getSH().setFromCenter(-5, -5, -3, -4);
	        assertEpsilonEquals(-7.0, getSH().getMinX());
	        assertEpsilonEquals(-6.0, getSH().getMinY());
	        assertEpsilonEquals(-3.0, getSH().getMaxX());
	        assertEpsilonEquals(-4.0, getSH().getMaxY());
	    }

		@DisplayName("(double,double,double,double) #5")
		@Test
		public void point_5() {
	        getSH().setFromCenter(0, 0, 0, 0);
	        assertEpsilonEquals(0.0, getSH().getMinX());
	        assertEpsilonEquals(0.0, getSH().getMinY());
	        assertEpsilonEquals(0.0, getSH().getMaxX());
	        assertEpsilonEquals(0.0, getSH().getMaxY());
	    }

		@DisplayName("(double,double,double,double) #6")
		@Test
		public void point_6() {
	        getSH().setFromCenter(2.5, 3.7, 4.2, 5.1);
	        assertEpsilonEquals(0.8, getSH().getMinX());
	        assertEpsilonEquals(2.3, getSH().getMinY());
	        assertEpsilonEquals(4.2, getSH().getMaxX());
	        assertEpsilonEquals(5.1, getSH().getMaxY());
	    }

		@DisplayName("(double,double,double,double) #7")
		@Test
		public void point_7() {
	        getSH().setFromCenter(10, -20, 15, -25);
	        assertEpsilonEquals(5.0, getSH().getMinX());
	        assertEpsilonEquals(-25.0, getSH().getMinY());
	        assertEpsilonEquals(15.0, getSH().getMaxX());
	        assertEpsilonEquals(-15.0, getSH().getMaxY());
	    }

		@DisplayName("(double,double,double,double) #8")
		@Test
		public void point_8() {
	        getSH().setFromCenter(-1.5, 2.5, 0.5, 4.5);
	        assertEpsilonEquals(-3.5, getSH().getMinX());
	        assertEpsilonEquals(0.5, getSH().getMinY());
	        assertEpsilonEquals(0.5, getSH().getMaxX());
	        assertEpsilonEquals(4.5, getSH().getMaxY());
	    }

		@DisplayName("(double,double,double,double) #9")
		@Test
		public void point_9() {
	        double centerX = 1e9;
	        double centerY = 2e9;
	        double cornerX = centerX + 3e8;
	        double cornerY = centerY + 4e8;
	        getSH().setFromCenter(centerX, centerY, cornerX, cornerY);
	        assertEpsilonEquals(7e8, getSH().getMinX());
	        assertEpsilonEquals(1.6e9, getSH().getMinY());
	        assertEpsilonEquals(1.3e9, getSH().getMaxX());
	        assertEpsilonEquals(2.4e9, getSH().getMaxY());
	    }

		@DisplayName("(double,double,double,double) #10")
		@Test
		public void point_10() {
	        double centerX = -1e9;
	        double centerY = -2e9;
	        double cornerX = centerX + 3e8;
	        double cornerY = centerY + 4e8;
	        getSH().setFromCenter(centerX, centerY, cornerX, cornerY);
	        assertEpsilonEquals(-1.3e9, getSH().getMinX());
	        assertEpsilonEquals(-2.4e9, getSH().getMinY());
	        assertEpsilonEquals(-7e8, getSH().getMaxX());
	        assertEpsilonEquals(-1.6e9, getSH().getMaxY());
	    }

		@DisplayName("(double,double,double,double) #11")
		@Test
		public void point_11() {
	        double centerX = 1.23456789;
	        double centerY = 2.3456789;
	        double dx = 1e-6;
	        double dy = 2e-6;
	        getSH().setFromCenter(centerX, centerY, centerX + dx, centerY + dy);
	        assertEpsilonEquals(centerX - dx, getSH().getMinX());
	        assertEpsilonEquals(centerY - dy, getSH().getMinY());
	        assertEpsilonEquals(centerX + dx, getSH().getMaxX());
	        assertEpsilonEquals(centerY + dy, getSH().getMaxY());
	    }

		@DisplayName("(Point2D,Point2D) #12")
		@Test
		public void point_12() {
			getSH().setFromCenter(factory.createPoint2d(0, 0), factory.createPoint2d(1, 1));
	        assertEpsilonEquals(-1., getSH().getMinX());
	        assertEpsilonEquals(-1., getSH().getMinY());
	        assertEpsilonEquals(1., getSH().getMaxX());
	        assertEpsilonEquals(1., getSH().getMaxY());
		}

		@DisplayName("(Point2D,Point2D) #13")
		@Test
		public void point_13() {
	        getSH().setFromCenter(factory.createPoint2d(0, 0), factory.createPoint2d(2, 3));
	        assertEpsilonEquals(-2.0, getSH().getMinX());
	        assertEpsilonEquals(-3.0, getSH().getMinY());
	        assertEpsilonEquals(2.0, getSH().getMaxX());
	        assertEpsilonEquals(3.0, getSH().getMaxY());
	    }

		@DisplayName("(Point2D,Point2D) #14")
		@Test
		public void point_14() {
	        getSH().setFromCenter(factory.createPoint2d(5, 5), factory.createPoint2d(3, 4));
	        assertEpsilonEquals(3.0, getSH().getMinX());
	        assertEpsilonEquals(4.0, getSH().getMinY());
	        assertEpsilonEquals(7.0, getSH().getMaxX());
	        assertEpsilonEquals(6.0, getSH().getMaxY());
	    }

		@DisplayName("(Point2D,Point2D) #15")
		@Test
		public void point_15() {
	        getSH().setFromCenter(factory.createPoint2d(-5, -5), factory.createPoint2d(-3, -4));
	        assertEpsilonEquals(-7.0, getSH().getMinX());
	        assertEpsilonEquals(-6.0, getSH().getMinY());
	        assertEpsilonEquals(-3.0, getSH().getMaxX());
	        assertEpsilonEquals(-4.0, getSH().getMaxY());
	    }

		@DisplayName("(Point2D,Point2D) #16")
		@Test
		public void point_16() {
	        getSH().setFromCenter(factory.createPoint2d(0, 0), factory.createPoint2d(0, 0));
	        assertEpsilonEquals(0.0, getSH().getMinX());
	        assertEpsilonEquals(0.0, getSH().getMinY());
	        assertEpsilonEquals(0.0, getSH().getMaxX());
	        assertEpsilonEquals(0.0, getSH().getMaxY());
	    }

		@DisplayName("(Point2D,Point2D) #17")
		@Test
		public void point_17() {
	        getSH().setFromCenter(factory.createPoint2d(2.5, 3.7), factory.createPoint2d(4.2, 5.1));
	        assertEpsilonEquals(0.8, getSH().getMinX());
	        assertEpsilonEquals(2.3, getSH().getMinY());
	        assertEpsilonEquals(4.2, getSH().getMaxX());
	        assertEpsilonEquals(5.1, getSH().getMaxY());
	    }

		@DisplayName("(Point2D,Point2D) #18")
		@Test
		public void point_18() {
	        getSH().setFromCenter(factory.createPoint2d(10, -20), factory.createPoint2d(15, -25));
	        assertEpsilonEquals(5.0, getSH().getMinX());
	        assertEpsilonEquals(-25.0, getSH().getMinY());
	        assertEpsilonEquals(15.0, getSH().getMaxX());
	        assertEpsilonEquals(-15.0, getSH().getMaxY());
	    }

		@DisplayName("(Point2D,Point2D) #19")
		@Test
		public void point_19() {
	        getSH().setFromCenter(factory.createPoint2d(-1.5, 2.5), factory.createPoint2d(0.5, 4.5));
	        assertEpsilonEquals(-3.5, getSH().getMinX());
	        assertEpsilonEquals(0.5, getSH().getMinY());
	        assertEpsilonEquals(0.5, getSH().getMaxX());
	        assertEpsilonEquals(4.5, getSH().getMaxY());
	    }

		@DisplayName("(Point2D,Point2D) #20")
		@Test
		public void point_20() {
	        double centerX = 1e9;
	        double centerY = 2e9;
	        double cornerX = centerX + 3e8;
	        double cornerY = centerY + 4e8;
	        getSH().setFromCenter(factory.createPoint2d(centerX, centerY), factory.createPoint2d(cornerX, cornerY));
	        assertEpsilonEquals(7e8, getSH().getMinX());
	        assertEpsilonEquals(1.6e9, getSH().getMinY());
	        assertEpsilonEquals(1.3e9, getSH().getMaxX());
	        assertEpsilonEquals(2.4e9, getSH().getMaxY());
	    }

		@DisplayName("(Point2D,Point2D) #21")
		@Test
		public void point_21() {
	        double centerX = -1e9;
	        double centerY = -2e9;
	        double cornerX = centerX + 3e8;
	        double cornerY = centerY + 4e8;
	        getSH().setFromCenter(factory.createPoint2d(centerX, centerY), factory.createPoint2d(cornerX, cornerY));
	        assertEpsilonEquals(-1.3e9, getSH().getMinX());
	        assertEpsilonEquals(-2.4e9, getSH().getMinY());
	        assertEpsilonEquals(-7e8, getSH().getMaxX());
	        assertEpsilonEquals(-1.6e9, getSH().getMaxY());
	    }

		@DisplayName("(Point2D,Point2D) #22")
		@Test
		public void point_22() {
	        double centerX = 1.23456789;
	        double centerY = 2.3456789;
	        double dx = 1e-6;
	        double dy = 2e-6;
	        getSH().setFromCenter(factory.createPoint2d(centerX, centerY), factory.createPoint2d(centerX + dx, centerY + dy));
	        assertEpsilonEquals(centerX - dx, getSH().getMinX());
	        assertEpsilonEquals(centerY - dy, getSH().getMinY());
	        assertEpsilonEquals(centerX + dx, getSH().getMaxX());
	        assertEpsilonEquals(centerY + dy, getSH().getMaxY());
	    }

		@DisplayName("(Point1D,Point1D) #23")
		@Test
		public void point_23() {
			getSH().setFromCenter(createPoint(0, 0), createPoint(1, 1));
	        assertEpsilonEquals(-1., getSH().getMinX());
	        assertEpsilonEquals(-1., getSH().getMinY());
	        assertEpsilonEquals(1., getSH().getMaxX());
	        assertEpsilonEquals(1., getSH().getMaxY());
		}

		@DisplayName("(Point1D,Point1D) #24")
		@Test
		public void point_24() {
	        getSH().setFromCenter(createPoint(0, 0), createPoint(2, 3));
	        assertEpsilonEquals(-2.0, getSH().getMinX());
	        assertEpsilonEquals(-3.0, getSH().getMinY());
	        assertEpsilonEquals(2.0, getSH().getMaxX());
	        assertEpsilonEquals(3.0, getSH().getMaxY());
	    }

		@DisplayName("(Point1D,Point1D) #25")
		@Test
		public void point_25() {
	        getSH().setFromCenter(createPoint(5, 5), createPoint(3, 4));
	        assertEpsilonEquals(3.0, getSH().getMinX());
	        assertEpsilonEquals(4.0, getSH().getMinY());
	        assertEpsilonEquals(7.0, getSH().getMaxX());
	        assertEpsilonEquals(6.0, getSH().getMaxY());
	    }

		@DisplayName("(Point1D,Point1D) #26")
		@Test
		public void point_26() {
	        getSH().setFromCenter(createPoint(-5, -5), createPoint(-3, -4));
	        assertEpsilonEquals(-7.0, getSH().getMinX());
	        assertEpsilonEquals(-6.0, getSH().getMinY());
	        assertEpsilonEquals(-3.0, getSH().getMaxX());
	        assertEpsilonEquals(-4.0, getSH().getMaxY());
	    }

		@DisplayName("(Point1D,Point1D) #27")
		@Test
		public void point_27() {
	        getSH().setFromCenter(createPoint(0, 0), createPoint(0, 0));
	        assertEpsilonEquals(0.0, getSH().getMinX());
	        assertEpsilonEquals(0.0, getSH().getMinY());
	        assertEpsilonEquals(0.0, getSH().getMaxX());
	        assertEpsilonEquals(0.0, getSH().getMaxY());
	    }

		@DisplayName("(Point1D,Point1D) #28")
		@Test
		public void point_28() {
	        getSH().setFromCenter(createPoint(2.5, 3.7), createPoint(4.2, 5.1));
	        assertEpsilonEquals(0.8, getSH().getMinX());
	        assertEpsilonEquals(2.3, getSH().getMinY());
	        assertEpsilonEquals(4.2, getSH().getMaxX());
	        assertEpsilonEquals(5.1, getSH().getMaxY());
	    }

		@DisplayName("(Point1D,Point1D) #29")
		@Test
		public void point_29() {
	        getSH().setFromCenter(createPoint(10, -20), createPoint(15, -25));
	        assertEpsilonEquals(5.0, getSH().getMinX());
	        assertEpsilonEquals(-25.0, getSH().getMinY());
	        assertEpsilonEquals(15.0, getSH().getMaxX());
	        assertEpsilonEquals(-15.0, getSH().getMaxY());
	    }

		@DisplayName("(Point1D,Point1D) #30")
		@Test
		public void point_30() {
	        getSH().setFromCenter(createPoint(-1.5, 2.5), createPoint(0.5, 4.5));
	        assertEpsilonEquals(-3.5, getSH().getMinX());
	        assertEpsilonEquals(0.5, getSH().getMinY());
	        assertEpsilonEquals(0.5, getSH().getMaxX());
	        assertEpsilonEquals(4.5, getSH().getMaxY());
	    }

		@DisplayName("(Point1D,Point1D) #31")
		@Test
		public void point_31() {
	        double centerX = 1e9;
	        double centerY = 2e9;
	        double cornerX = centerX + 3e8;
	        double cornerY = centerY + 4e8;
	        getSH().setFromCenter(createPoint(centerX, centerY), createPoint(cornerX, cornerY));
	        assertEpsilonEquals(7e8, getSH().getMinX());
	        assertEpsilonEquals(1.6e9, getSH().getMinY());
	        assertEpsilonEquals(1.3e9, getSH().getMaxX());
	        assertEpsilonEquals(2.4e9, getSH().getMaxY());
	    }

		@DisplayName("(Point1D,Point1D) #32")
		@Test
		public void point_32() {
	        double centerX = -1e9;
	        double centerY = -2e9;
	        double cornerX = centerX + 3e8;
	        double cornerY = centerY + 4e8;
	        getSH().setFromCenter(createPoint(centerX, centerY), createPoint(cornerX, cornerY));
	        assertEpsilonEquals(-1.3e9, getSH().getMinX());
	        assertEpsilonEquals(-2.4e9, getSH().getMinY());
	        assertEpsilonEquals(-7e8, getSH().getMaxX());
	        assertEpsilonEquals(-1.6e9, getSH().getMaxY());
	    }

		@DisplayName("(Point1D,Point1D) #33")
		@Test
		public void point_33() {
	        double centerX = 1.23456789;
	        double centerY = 2.3456789;
	        double dx = 1e-6;
	        double dy = 2e-6;
	        getSH().setFromCenter(createPoint(centerX, centerY), createPoint(centerX + dx, centerY + dy));
	        assertEpsilonEquals(centerX - dx, getSH().getMinX());
	        assertEpsilonEquals(centerY - dy, getSH().getMinY());
	        assertEpsilonEquals(centerX + dx, getSH().getMaxX());
	        assertEpsilonEquals(centerY + dy, getSH().getMaxY());
	    }

	}

	@DisplayName("setFromCorners")
	@Nested
	public class SetFromCorners {
		
		@DisplayName("(double,double,double,double) #1")
		@Test
		public void point_1() {
			getSH().setFromCorners(0,  0, 1, 1);
	        assertEpsilonEquals(0., getSH().getMinX());
	        assertEpsilonEquals(0., getSH().getMinY());
	        assertEpsilonEquals(1., getSH().getMaxX());
	        assertEpsilonEquals(1., getSH().getMaxY());
		}

		@DisplayName("(double,double,double,double) #2")
		@Test
		public void point_2() {
	        getSH().setFromCorners(0, 0, 2, 3);
	        assertEpsilonEquals(0., getSH().getMinX());
	        assertEpsilonEquals(0., getSH().getMinY());
	        assertEpsilonEquals(2.0, getSH().getMaxX());
	        assertEpsilonEquals(3.0, getSH().getMaxY());
	    }

		@DisplayName("(double,double,double,double) #3")
		@Test
		public void point_3() {
	        getSH().setFromCorners(5, 5, 3, 4);
	        assertEpsilonEquals(3.0, getSH().getMinX());
	        assertEpsilonEquals(4.0, getSH().getMinY());
	        assertEpsilonEquals(5.0, getSH().getMaxX());
	        assertEpsilonEquals(5.0, getSH().getMaxY());
	    }

		@DisplayName("(double,double,double,double) #4")
		@Test
		public void point_4() {
	        getSH().setFromCorners(-5, -5, -3, -4);
	        assertEpsilonEquals(-5.0, getSH().getMinX());
	        assertEpsilonEquals(-5.0, getSH().getMinY());
	        assertEpsilonEquals(-3.0, getSH().getMaxX());
	        assertEpsilonEquals(-4.0, getSH().getMaxY());
	    }

		@DisplayName("(double,double,double,double) #5")
		@Test
		public void point_5() {
	        getSH().setFromCorners(0, 0, 0, 0);
	        assertEpsilonEquals(0.0, getSH().getMinX());
	        assertEpsilonEquals(0.0, getSH().getMinY());
	        assertEpsilonEquals(0.0, getSH().getMaxX());
	        assertEpsilonEquals(0.0, getSH().getMaxY());
	    }

		@DisplayName("(double,double,double,double) #6")
		@Test
		public void point_6() {
	        getSH().setFromCorners(2.5, 3.7, 4.2, 5.1);
	        assertEpsilonEquals(2.5, getSH().getMinX());
	        assertEpsilonEquals(3.7, getSH().getMinY());
	        assertEpsilonEquals(4.2, getSH().getMaxX());
	        assertEpsilonEquals(5.1, getSH().getMaxY());
	    }

		@DisplayName("(double,double,double,double) #7")
		@Test
		public void point_7() {
	        getSH().setFromCorners(10, -20, 15, -25);
	        assertEpsilonEquals(10.0, getSH().getMinX());
	        assertEpsilonEquals(-25.0, getSH().getMinY());
	        assertEpsilonEquals(15.0, getSH().getMaxX());
	        assertEpsilonEquals(-20.0, getSH().getMaxY());
	    }

		@DisplayName("(double,double,double,double) #8")
		@Test
		public void point_8() {
	        getSH().setFromCorners(-1.5, 2.5, 0.5, 4.5);
	        assertEpsilonEquals(-1.5, getSH().getMinX());
	        assertEpsilonEquals(2.5, getSH().getMinY());
	        assertEpsilonEquals(0.5, getSH().getMaxX());
	        assertEpsilonEquals(4.5, getSH().getMaxY());
	    }

		@DisplayName("(double,double,double,double) #9")
		@Test
		public void point_9() {
	        double cornerX1 = 1e9;
	        double cornerY1 = 2e9;
	        double cornerX = cornerX1 + 3e8;
	        double cornerY = cornerY1 + 4e8;
	        getSH().setFromCorners(cornerX1, cornerY1, cornerX, cornerY);
	        assertEpsilonEquals(cornerX1, getSH().getMinX());
	        assertEpsilonEquals(cornerY1, getSH().getMinY());
	        assertEpsilonEquals(cornerX, getSH().getMaxX());
	        assertEpsilonEquals(cornerY, getSH().getMaxY());
	    }

		@DisplayName("(double,double,double,double) #10")
		@Test
		public void point_10() {
	        double cornerX1 = -1e9;
	        double cornerY1 = -2e9;
	        double cornerX = cornerX1 + 3e8;
	        double cornerY = cornerY1 + 4e8;
	        getSH().setFromCorners(cornerX1, cornerY1, cornerX, cornerY);
	        assertEpsilonEquals(cornerX1, getSH().getMinX());
	        assertEpsilonEquals(cornerY1, getSH().getMinY());
	        assertEpsilonEquals(cornerX, getSH().getMaxX());
	        assertEpsilonEquals(cornerY, getSH().getMaxY());
	    }

		@DisplayName("(double,double,double,double) #11")
		@Test
		public void point_11() {
	        double cornerX = 1.23456789;
	        double cornerY = 2.3456789;
	        double dx = 1e-6;
	        double dy = 2e-6;
	        getSH().setFromCorners(cornerX, cornerY, cornerX + dx, cornerY + dy);
	        assertEpsilonEquals(cornerX - dx, getSH().getMinX());
	        assertEpsilonEquals(cornerY - dy, getSH().getMinY());
	        assertEpsilonEquals(cornerX + dx, getSH().getMaxX());
	        assertEpsilonEquals(cornerY + dy, getSH().getMaxY());
	    }

		@DisplayName("(Point2D,Point2D) #12")
		@Test
		public void point_12() {
			getSH().setFromCorners(factory.createPoint2d(0, 0), factory.createPoint2d(1, 1));
	        assertEpsilonEquals(0., getSH().getMinX());
	        assertEpsilonEquals(0., getSH().getMinY());
	        assertEpsilonEquals(1., getSH().getMaxX());
	        assertEpsilonEquals(1., getSH().getMaxY());
		}

		@DisplayName("(Point2D,Point2D) #13")
		@Test
		public void point_13() {
	        getSH().setFromCorners(factory.createPoint2d(0, 0), factory.createPoint2d(2, 3));
	        assertEpsilonEquals(0., getSH().getMinX());
	        assertEpsilonEquals(0., getSH().getMinY());
	        assertEpsilonEquals(2.0, getSH().getMaxX());
	        assertEpsilonEquals(3.0, getSH().getMaxY());
	    }

		@DisplayName("(Point2D,Point2D) #14")
		@Test
		public void point_14() {
	        getSH().setFromCorners(factory.createPoint2d(5, 5), factory.createPoint2d(3, 4));
	        assertEpsilonEquals(3.0, getSH().getMinX());
	        assertEpsilonEquals(4.0, getSH().getMinY());
	        assertEpsilonEquals(5.0, getSH().getMaxX());
	        assertEpsilonEquals(5.0, getSH().getMaxY());
	    }

		@DisplayName("(Point2D,Point2D) #15")
		@Test
		public void point_15() {
	        getSH().setFromCorners(factory.createPoint2d(-5, -5), factory.createPoint2d(-3, -4));
	        assertEpsilonEquals(-5.0, getSH().getMinX());
	        assertEpsilonEquals(-5.0, getSH().getMinY());
	        assertEpsilonEquals(-3.0, getSH().getMaxX());
	        assertEpsilonEquals(-4.0, getSH().getMaxY());
	    }

		@DisplayName("(Point2D,Point2D) #16")
		@Test
		public void point_16() {
	        getSH().setFromCorners(factory.createPoint2d(0, 0), factory.createPoint2d(0, 0));
	        assertEpsilonEquals(0.0, getSH().getMinX());
	        assertEpsilonEquals(0.0, getSH().getMinY());
	        assertEpsilonEquals(0.0, getSH().getMaxX());
	        assertEpsilonEquals(0.0, getSH().getMaxY());
	    }

		@DisplayName("(Point2D,Point2D) #17")
		@Test
		public void point_17() {
	        getSH().setFromCorners(factory.createPoint2d(2.5, 3.7), factory.createPoint2d(4.2, 5.1));
	        assertEpsilonEquals(2.5, getSH().getMinX());
	        assertEpsilonEquals(3.7, getSH().getMinY());
	        assertEpsilonEquals(4.2, getSH().getMaxX());
	        assertEpsilonEquals(5.1, getSH().getMaxY());
	    }

		@DisplayName("(Point2D,Point2D) #18")
		@Test
		public void point_18() {
	        getSH().setFromCorners(factory.createPoint2d(10, -20), factory.createPoint2d(15, -25));
	        assertEpsilonEquals(10.0, getSH().getMinX());
	        assertEpsilonEquals(-25.0, getSH().getMinY());
	        assertEpsilonEquals(15.0, getSH().getMaxX());
	        assertEpsilonEquals(-20.0, getSH().getMaxY());
	    }

		@DisplayName("(Point2D,Point2D) #19")
		@Test
		public void point_19() {
	        getSH().setFromCorners(factory.createPoint2d(-1.5, 2.5), factory.createPoint2d(0.5, 4.5));
	        assertEpsilonEquals(-1.5, getSH().getMinX());
	        assertEpsilonEquals(2.5, getSH().getMinY());
	        assertEpsilonEquals(0.5, getSH().getMaxX());
	        assertEpsilonEquals(4.5, getSH().getMaxY());
	    }

		@DisplayName("(Point2D,Point2D) #20")
		@Test
		public void point_20() {
	        double cornerX1 = 1e9;
	        double cornerY1 = 2e9;
	        double cornerX = cornerX1 + 3e8;
	        double cornerY = cornerY1 + 4e8;
	        getSH().setFromCorners(factory.createPoint2d(cornerX1, cornerY1), factory.createPoint2d(cornerX, cornerY));
	        assertEpsilonEquals(cornerX1, getSH().getMinX());
	        assertEpsilonEquals(cornerY1, getSH().getMinY());
	        assertEpsilonEquals(cornerX, getSH().getMaxX());
	        assertEpsilonEquals(cornerY, getSH().getMaxY());
	    }

		@DisplayName("(Point2D,Point2D) #21")
		@Test
		public void point_21() {
	        double cornerX1 = -1e9;
	        double cornerY1 = -2e9;
	        double cornerX = cornerX1 + 3e8;
	        double cornerY = cornerY1 + 4e8;
	        getSH().setFromCorners(factory.createPoint2d(cornerX1, cornerY1), factory.createPoint2d(cornerX, cornerY));
	        assertEpsilonEquals(cornerX1, getSH().getMinX());
	        assertEpsilonEquals(cornerY1, getSH().getMinY());
	        assertEpsilonEquals(cornerX, getSH().getMaxX());
	        assertEpsilonEquals(cornerY, getSH().getMaxY());
	    }

		@DisplayName("(Point2D,Point2D) #22")
		@Test
		public void point_22() {
	        double cornerX = 1.23456789;
	        double cornerY = 2.3456789;
	        double dx = 1e-6;
	        double dy = 2e-6;
	        getSH().setFromCorners(factory.createPoint2d(cornerX, cornerY), factory.createPoint2d(cornerX + dx, cornerY + dy));
	        assertEpsilonEquals(cornerX - dx, getSH().getMinX());
	        assertEpsilonEquals(cornerY - dy, getSH().getMinY());
	        assertEpsilonEquals(cornerX + dx, getSH().getMaxX());
	        assertEpsilonEquals(cornerY + dy, getSH().getMaxY());
	    }

		@DisplayName("(Point1D,Point1D) #23")
		@Test
		public void point_23() {
			getSH().setFromCorners(createPoint(0, 0), createPoint(1, 1));
	        assertEpsilonEquals(0., getSH().getMinX());
	        assertEpsilonEquals(0., getSH().getMinY());
	        assertEpsilonEquals(1., getSH().getMaxX());
	        assertEpsilonEquals(1., getSH().getMaxY());
		}

		@DisplayName("(Point1D,Point1D) #24")
		@Test
		public void point_24() {
	        getSH().setFromCorners(createPoint(0, 0), createPoint(2, 3));
	        assertEpsilonEquals(0., getSH().getMinX());
	        assertEpsilonEquals(0., getSH().getMinY());
	        assertEpsilonEquals(2.0, getSH().getMaxX());
	        assertEpsilonEquals(3.0, getSH().getMaxY());
	    }

		@DisplayName("(Point1D,Point1D) #25")
		@Test
		public void point_25() {
	        getSH().setFromCorners(createPoint(5, 5), createPoint(3, 4));
	        assertEpsilonEquals(3.0, getSH().getMinX());
	        assertEpsilonEquals(4.0, getSH().getMinY());
	        assertEpsilonEquals(5.0, getSH().getMaxX());
	        assertEpsilonEquals(5.0, getSH().getMaxY());
	    }

		@DisplayName("(Point1D,Point1D) #26")
		@Test
		public void point_26() {
	        getSH().setFromCorners(createPoint(-5, -5), createPoint(-3, -4));
	        assertEpsilonEquals(-5.0, getSH().getMinX());
	        assertEpsilonEquals(-5.0, getSH().getMinY());
	        assertEpsilonEquals(-3.0, getSH().getMaxX());
	        assertEpsilonEquals(-4.0, getSH().getMaxY());
	    }

		@DisplayName("(Point1D,Point1D) #27")
		@Test
		public void point_27() {
	        getSH().setFromCorners(createPoint(0, 0), createPoint(0, 0));
	        assertEpsilonEquals(0.0, getSH().getMinX());
	        assertEpsilonEquals(0.0, getSH().getMinY());
	        assertEpsilonEquals(0.0, getSH().getMaxX());
	        assertEpsilonEquals(0.0, getSH().getMaxY());
	    }

		@DisplayName("(Point1D,Point1D) #28")
		@Test
		public void point_28() {
	        getSH().setFromCorners(createPoint(2.5, 3.7), createPoint(4.2, 5.1));
	        assertEpsilonEquals(2.5, getSH().getMinX());
	        assertEpsilonEquals(3.7, getSH().getMinY());
	        assertEpsilonEquals(4.2, getSH().getMaxX());
	        assertEpsilonEquals(5.1, getSH().getMaxY());
	    }

		@DisplayName("(Point1D,Point1D) #29")
		@Test
		public void point_29() {
	        getSH().setFromCorners(createPoint(10, -20), createPoint(15, -25));
	        assertEpsilonEquals(10.0, getSH().getMinX());
	        assertEpsilonEquals(-25.0, getSH().getMinY());
	        assertEpsilonEquals(15.0, getSH().getMaxX());
	        assertEpsilonEquals(-20.0, getSH().getMaxY());
	    }

		@DisplayName("(Point1D,Point1D) #30")
		@Test
		public void point_30() {
	        getSH().setFromCorners(createPoint(-1.5, 2.5), createPoint(0.5, 4.5));
	        assertEpsilonEquals(-1.5, getSH().getMinX());
	        assertEpsilonEquals(2.5, getSH().getMinY());
	        assertEpsilonEquals(0.5, getSH().getMaxX());
	        assertEpsilonEquals(4.5, getSH().getMaxY());
	    }

		@DisplayName("(Point1D,Point1D) #31")
		@Test
		public void point_31() {
	        double cornerX1 = 1e9;
	        double cornerY1 = 2e9;
	        double cornerX = cornerX1 + 3e8;
	        double cornerY = cornerY1 + 4e8;
	        getSH().setFromCorners(createPoint(cornerX1, cornerY1), createPoint(cornerX, cornerY));
	        assertEpsilonEquals(cornerX1, getSH().getMinX());
	        assertEpsilonEquals(cornerY1, getSH().getMinY());
	        assertEpsilonEquals(cornerX, getSH().getMaxX());
	        assertEpsilonEquals(cornerY, getSH().getMaxY());
	    }

		@DisplayName("(Point1D,Point1D) #32")
		@Test
		public void point_32() {
	        double cornerX1 = -1e9;
	        double cornerY1 = -2e9;
	        double cornerX = cornerX1 + 3e8;
	        double cornerY = cornerY1 + 4e8;
	        getSH().setFromCorners(createPoint(cornerX1, cornerY1), createPoint(cornerX, cornerY));
	        assertEpsilonEquals(cornerX1, getSH().getMinX());
	        assertEpsilonEquals(cornerY1, getSH().getMinY());
	        assertEpsilonEquals(cornerX, getSH().getMaxX());
	        assertEpsilonEquals(cornerY, getSH().getMaxY());
	    }

		@DisplayName("(Point1D,Point1D) #33")
		@Test
		public void point_33() {
	        double cornerX = 1.23456789;
	        double cornerY = 2.3456789;
	        double dx = 1e-6;
	        double dy = 2e-6;
	        getSH().setFromCorners(createPoint(cornerX, cornerY), createPoint(cornerX + dx, cornerY + dy));
	        assertEpsilonEquals(cornerX - dx, getSH().getMinX());
	        assertEpsilonEquals(cornerY - dy, getSH().getMinY());
	        assertEpsilonEquals(cornerX + dx, getSH().getMaxX());
	        assertEpsilonEquals(cornerY + dy, getSH().getMaxY());
	    }

	}

	@DisplayName("setWidth")
	@Nested
	public class SetWidth {

		@DisplayName("#1")
		@Test
		public void test_1() {
	        getSH().setWidth(100.);
	        assertEpsilonEquals(1.235, getSH().getMinX());
	        assertEpsilonEquals(-3.459, getSH().getMinY());
	        assertEpsilonEquals(101.235, getSH().getMaxX());
	        assertEpsilonEquals(11.504, getSH().getMaxY());
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
	        getSH().setWidth(0.0);
	        assertEpsilonEquals(1.235, getSH().getMinX());
	        assertEpsilonEquals(-3.459, getSH().getMinY());
	        assertEpsilonEquals(1.235, getSH().getMaxX());
	        assertEpsilonEquals(11.504, getSH().getMaxY());
	    }

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertThrows(AssertionError.class, () -> {
				getSH().setWidth(-20.0);
			});
	    }

		@DisplayName("#4")
		@Test
		public void test_4() {
	        getSH().setWidth(10.254);
	        assertEpsilonEquals(1.235, getSH().getMinX());
	        assertEpsilonEquals(-3.459, getSH().getMinY());
	        assertEpsilonEquals(11.489, getSH().getMaxX());
	        assertEpsilonEquals(11.504, getSH().getMaxY());
	    }

		@DisplayName("#5")
		@Test
		public void test_5() {
	        getSH().setWidth(1.5e9);
	        assertEpsilonEquals(1.235, getSH().getMinX());
	        assertEpsilonEquals(-3.459, getSH().getMinY());
	        assertEpsilonEquals(1500000001.235, getSH().getMaxX());
	        assertEpsilonEquals(11.504, getSH().getMaxY());
	    }

		@DisplayName("#6")
		@Test
		public void test_6() {
			assertThrows(AssertionError.class, () -> {
		        getSH().setWidth(-1.2e9);
			});
	    }

		@DisplayName("#7")
		@Test
		public void test_7() {
	        getSH().setWidth(0.0001);
	        assertEpsilonEquals(1.235, getSH().getMinX());
	        assertEpsilonEquals(-3.459, getSH().getMinY());
	        assertEpsilonEquals(1.2351, getSH().getMaxX());
	        assertEpsilonEquals(11.504, getSH().getMaxY());
	    }

		@DisplayName("#8")
		@Test
		public void test_8() {
	        getSH().setWidth(3.141592653589793);
	        assertEpsilonEquals(1.235, getSH().getMinX());
	        assertEpsilonEquals(-3.459, getSH().getMinY());
	        assertEpsilonEquals(4.376592654, getSH().getMaxX());
	        assertEpsilonEquals(11.504, getSH().getMaxY());
	    }

		@DisplayName("#9")
		@Test
		public void test_9() {
	        double tiny = Double.MIN_VALUE;
	        getSH().setWidth(tiny);
	        assertEpsilonEquals(1.235, getSH().getMinX());
	        assertEpsilonEquals(-3.459, getSH().getMinY());
	        assertEpsilonEquals(1.235 + Double.MIN_VALUE, getSH().getMaxX());
	        assertEpsilonEquals(11.504, getSH().getMaxY());
	    }

		@DisplayName("#10")
		@Test
		public void test_10() {
	        double width = 1e-12;
	        getSH().setWidth(width);
	        assertEpsilonEquals(1.235, getSH().getMinX());
	        assertEpsilonEquals(-3.459, getSH().getMinY());
	        assertEpsilonEquals(1.235, getSH().getMaxX());
	        assertEpsilonEquals(11.504, getSH().getMaxY());
	    }

	}

	@DisplayName("setHeight")
	@Nested
	public class SetHeight {

		@DisplayName("#1")
		@Test
		public void test_1() {
	        getSH().setHeight(100.);
	        assertEpsilonEquals(1.235, getSH().getMinX());
	        assertEpsilonEquals(-3.459, getSH().getMinY());
	        assertEpsilonEquals(11.489, getSH().getMaxX());
	        assertEpsilonEquals(96.541, getSH().getMaxY());
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
	        getSH().setHeight(0.0);
	        assertEpsilonEquals(1.235, getSH().getMinX());
	        assertEpsilonEquals(-3.459, getSH().getMinY());
	        assertEpsilonEquals(11.489, getSH().getMaxX());
	        assertEpsilonEquals(-3.459, getSH().getMaxY());
	    }

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertThrows(AssertionError.class, () -> {
		        getSH().setHeight(-20.0);
			});
	    }

		@DisplayName("#4")
		@Test
		public void test_4() {
	        getSH().setHeight(14.963);
	        assertEpsilonEquals(1.235, getSH().getMinX());
	        assertEpsilonEquals(-3.459, getSH().getMinY());
	        assertEpsilonEquals(11.489, getSH().getMaxX());
	        assertEpsilonEquals(11.504, getSH().getMaxY());
	    }

		@DisplayName("#5")
		@Test
		public void test_5() {
	        getSH().setHeight(1.5e9);
	        assertEpsilonEquals(1.235, getSH().getMinX());
	        assertEpsilonEquals(-3.459, getSH().getMinY());
	        assertEpsilonEquals(11.489, getSH().getMaxX());
	        assertEpsilonEquals(1499999996.541, getSH().getMaxY());
	    }

		@DisplayName("#6")
		@Test
		public void test_6() {
	        assertThrows(AssertionError.class, () -> {
	        	getSH().setHeight(-1.2e9);
	        });
	    }

		@DisplayName("#7")
		@Test
		public void test_7() {
	        getSH().setHeight(0.0001);
	        assertEpsilonEquals(1.235, getSH().getMinX());
	        assertEpsilonEquals(-3.459, getSH().getMinY());
	        assertEpsilonEquals(11.489, getSH().getMaxX());
	        assertEpsilonEquals(-3.4589, getSH().getMaxY());
	    }

		@DisplayName("#8")
		@Test
		public void test_8() {
	        double height = 2.718281828459045;
	        getSH().setHeight(height);
	        assertEpsilonEquals(1.235, getSH().getMinX());
	        assertEpsilonEquals(-3.459, getSH().getMinY());
	        assertEpsilonEquals(11.489, getSH().getMaxX());
	        assertEpsilonEquals(-0.740718172, getSH().getMaxY());
	    }

		@DisplayName("#9")
		@Test
		public void test_9() {
	        double tiny = Double.MIN_VALUE;
	        getSH().setHeight(tiny);
	        assertEpsilonEquals(1.235, getSH().getMinX());
	        assertEpsilonEquals(-3.459, getSH().getMinY());
	        assertEpsilonEquals(11.489, getSH().getMaxX());
	        assertEpsilonEquals(-3.459, getSH().getMaxY());
	    }

		@DisplayName("#10")
		@Test
		public void test_10() {
	        double height = 1e-12;
	        getSH().setHeight(height);
	        assertEpsilonEquals(1.235, getSH().getMinX());
	        assertEpsilonEquals(-3.459, getSH().getMinY());
	        assertEpsilonEquals(11.489, getSH().getMaxX());
	        assertEpsilonEquals(-3.459, getSH().getMaxY());
	    }

	}

	@DisplayName("setMinX")
	@Nested
	public class SetMinX {
		
		@DisplayName("#1")
		@Test
		public void test_1() {
			getSH().setMinX(0.);
			assertEpsilonEquals(0., getSH().getMinX());
			assertEpsilonEquals(-3.459, getSH().getMinY());
			assertEpsilonEquals(11.489, getSH().getMaxX());
			assertEpsilonEquals(11.504, getSH().getMaxY());
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
	        getSH().setMinX(-5.0);
	        assertEpsilonEquals(-5.0, getSH().getMinX());
	        assertEpsilonEquals(-3.459, getSH().getMinY());
	        assertEpsilonEquals(11.489, getSH().getMaxX());
	        assertEpsilonEquals(11.504, getSH().getMaxY());
	    }

		@DisplayName("#3")
		@Test
		public void test_3() {
	        getSH().setMinX(12.0);
	        assertEpsilonEquals(12., getSH().getMinX());
	        assertEpsilonEquals(-3.459, getSH().getMinY());
	        assertEpsilonEquals(12., getSH().getMaxX());
	        assertEpsilonEquals(11.504, getSH().getMaxY());
	    }

		@DisplayName("#4")
		@Test
		public void test_4() {
	        double originalMinX = getSH().getMinX();
	        getSH().setMinX(originalMinX);
	        assertEpsilonEquals(1.235, getSH().getMinX());
	        assertEpsilonEquals(-3.459, getSH().getMinY());
	        assertEpsilonEquals(11.489, getSH().getMaxX());
	        assertEpsilonEquals(11.504, getSH().getMaxY());
	    }

		@DisplayName("#5")
		@Test
		public void test_5() {
	        getSH().setMinX(1.5e9);
	        assertEpsilonEquals(1.5e9, getSH().getMinX());
	        assertEpsilonEquals(-3.459, getSH().getMinY());
	        assertEpsilonEquals(1.5e9, getSH().getMaxX());
	        assertEpsilonEquals(11.504, getSH().getMaxY());
	    }

		@DisplayName("#6")
		@Test
		public void test_6() {
	        getSH().setMinX(1e-10);
	        assertEpsilonEquals(1e-10, getSH().getMinX());
	        assertEpsilonEquals(-3.459, getSH().getMinY());
	        assertEpsilonEquals(11.489, getSH().getMaxX());
	        assertEpsilonEquals(11.504, getSH().getMaxY());
	    }

		@DisplayName("#7")
		@Test
		public void test_7() {
	        getSH().setMinX(-1.2e9);
	        assertEpsilonEquals(-1.2e9, getSH().getMinX());
	        assertEpsilonEquals(-3.459, getSH().getMinY());
	        assertEpsilonEquals(11.489, getSH().getMaxX());
	        assertEpsilonEquals(11.504, getSH().getMaxY());
	    }

		@DisplayName("#8")
		@Test
		public void test_8() {
	        getSH().setMinX(3.141592653589793);
	        assertEpsilonEquals(3.141592653589793, getSH().getMinX());
	        assertEpsilonEquals(-3.459, getSH().getMinY());
	        assertEpsilonEquals(11.489, getSH().getMaxX());
	        assertEpsilonEquals(11.504, getSH().getMaxY());
	    }

		@DisplayName("#9")
		@Test
		public void test_9() {
	        double newMinX = 100.0;
	        getSH().setMinX(newMinX);
	        assertEpsilonEquals(newMinX, getSH().getMinX());
	        assertEpsilonEquals(-3.459, getSH().getMinY());
	        assertEpsilonEquals(newMinX, getSH().getMaxX());
	        assertEpsilonEquals(11.504, getSH().getMaxY());
	    }

		@DisplayName("#10")
		@Test
		public void test_10() {
	        double tiny = Double.MIN_VALUE; // ~4.9e-324
	        getSH().setMinX(tiny);
	        assertEpsilonEquals(tiny, getSH().getMinX());
	        assertEpsilonEquals(-3.459, getSH().getMinY());
	        assertEpsilonEquals(11.489, getSH().getMaxX());
	        assertEpsilonEquals(11.504, getSH().getMaxY());
	    }

	}

	@DisplayName("setMaxX")
	@Nested
	public class SetMaxX {
		
		@DisplayName("#1")
		@Test
		public void test_1() {
			getSH().setMaxX(32.);
			assertEpsilonEquals(1.235, getSH().getMinX());
			assertEpsilonEquals(-3.459, getSH().getMinY());
			assertEpsilonEquals(32., getSH().getMaxX());
			assertEpsilonEquals(11.504, getSH().getMaxY());
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
	        getSH().setMaxX(0.0);
	        assertEpsilonEquals(0., getSH().getMinX());
	        assertEpsilonEquals(-3.459, getSH().getMinY());
	        assertEpsilonEquals(0., getSH().getMaxX());
	        assertEpsilonEquals(11.504, getSH().getMaxY());
	    }

		@DisplayName("#3")
		@Test
		public void test_3() {
	        getSH().setMaxX(-5.0);
	        assertEpsilonEquals(-5., getSH().getMinX());
	        assertEpsilonEquals(-3.459, getSH().getMinY());
	        assertEpsilonEquals(-5., getSH().getMaxX());
	        assertEpsilonEquals(11.504, getSH().getMaxY());
	    }

		@DisplayName("#4")
		@Test
		public void test_4() {
	        double originalMaxX = getSH().getMaxX(); // should be 11.489
	        getSH().setMaxX(originalMaxX);
	        assertEpsilonEquals(1.235, getSH().getMinX());
	        assertEpsilonEquals(-3.459, getSH().getMinY());
	        assertEpsilonEquals(11.489, getSH().getMaxX());
	        assertEpsilonEquals(11.504, getSH().getMaxY());
	    }

		@DisplayName("#5")
		@Test
		public void test_5() {
	        getSH().setMaxX(1.5e9);
	        assertEpsilonEquals(1.235, getSH().getMinX());
	        assertEpsilonEquals(-3.459, getSH().getMinY());
	        assertEpsilonEquals(1.5e9, getSH().getMaxX());
	        assertEpsilonEquals(11.504, getSH().getMaxY());
	    }

		@DisplayName("#6")
		@Test
		public void test_6() {
	        getSH().setMaxX(-1.2e9);
	        assertEpsilonEquals(-1.2e9, getSH().getMinX());
	        assertEpsilonEquals(-3.459, getSH().getMinY());
	        assertEpsilonEquals(-1.2e9, getSH().getMaxX());
	        assertEpsilonEquals(11.504, getSH().getMaxY());
	    }

		@DisplayName("#7")
		@Test
		public void test_7() {
	        getSH().setMaxX(3.141592653589793);
	        assertEpsilonEquals(1.235, getSH().getMinX());
	        assertEpsilonEquals(-3.459, getSH().getMinY());
	        assertEpsilonEquals(3.141592653589793, getSH().getMaxX());
	        assertEpsilonEquals(11.504, getSH().getMaxY());
	    }

		@DisplayName("#8")
		@Test
		public void test_8() {
	        double tiny = Double.MIN_VALUE;
	        getSH().setMaxX(tiny);
	        assertEpsilonEquals(tiny, getSH().getMinX());
	        assertEpsilonEquals(-3.459, getSH().getMinY());
	        assertEpsilonEquals(tiny, getSH().getMaxX());
	        assertEpsilonEquals(11.504, getSH().getMaxY());
	    }

		@DisplayName("#9")
		@Test
		public void test_9() {
	        double newMaxX = 1.235 + 1e-12;
	        getSH().setMaxX(newMaxX);
	        assertEpsilonEquals(1.235, getSH().getMinX());
	        assertEpsilonEquals(-3.459, getSH().getMinY());
	        assertEpsilonEquals(newMaxX, getSH().getMaxX());
	        assertEpsilonEquals(11.504, getSH().getMaxY());
	    }

		@DisplayName("#10")
		@Test
		public void test_10() {
	        getSH().setMaxX(1.235); // equal to minX
	        assertEpsilonEquals(1.235, getSH().getMinX());
	        assertEpsilonEquals(-3.459, getSH().getMinY());
	        assertEpsilonEquals(1.235, getSH().getMaxX());
	        assertEpsilonEquals(11.504, getSH().getMaxY());
	    }

	}

	@DisplayName("setMinY")
	@Nested
	public class SetMinY {
		
		@DisplayName("#1")
		@Test
		public void test_1() {
	        getSH().setMinY(-10.);
	        assertEpsilonEquals(1.235, getSH().getMinX());
	        assertEpsilonEquals(-10., getSH().getMinY());
	        assertEpsilonEquals(11.489, getSH().getMaxX());
	        assertEpsilonEquals(11.504, getSH().getMaxY());
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
	        getSH().setMinY(15.0);
	        assertEpsilonEquals(1.235, getSH().getMinX());
	        assertEpsilonEquals(15., getSH().getMinY());
	        assertEpsilonEquals(11.489, getSH().getMaxX());
	        assertEpsilonEquals(15., getSH().getMaxY());
	    }

		@DisplayName("#3")
		@Test
		public void test_3() {
	        getSH().setMinY(0.0);
	        assertEpsilonEquals(1.235, getSH().getMinX());
	        assertEpsilonEquals(0.0, getSH().getMinY());
	        assertEpsilonEquals(11.489, getSH().getMaxX());
	        assertEpsilonEquals(11.504, getSH().getMaxY());
	    }

		@DisplayName("#4")
		@Test
		public void test_4() {
	        double originalMinY = getSH().getMinY();
	        getSH().setMinY(originalMinY);
	        assertEpsilonEquals(1.235, getSH().getMinX());
	        assertEpsilonEquals(-3.459, getSH().getMinY());
	        assertEpsilonEquals(11.489, getSH().getMaxX());
	        assertEpsilonEquals(11.504, getSH().getMaxY());
	    }

		@DisplayName("#5")
		@Test
		public void test_5() {
	        getSH().setMinY(1.5e9);
	        assertEpsilonEquals(1.235, getSH().getMinX());
	        assertEpsilonEquals(1.5e9, getSH().getMinY());
	        assertEpsilonEquals(11.489, getSH().getMaxX());
	        assertEpsilonEquals(1.5e9, getSH().getMaxY());
	    }

		@DisplayName("#6")
		@Test
		public void test_6() {
	        getSH().setMinY(-1.2e9);
	        assertEpsilonEquals(1.235, getSH().getMinX());
	        assertEpsilonEquals(-1.2e9, getSH().getMinY());
	        assertEpsilonEquals(11.489, getSH().getMaxX());
	        assertEpsilonEquals(11.504, getSH().getMaxY());
	    }

		@DisplayName("#7")
		@Test
		public void test_7() {
	        getSH().setMinY(2.718281828459045);
	        assertEpsilonEquals(1.235, getSH().getMinX());
	        assertEpsilonEquals(2.718281828459045, getSH().getMinY());
	        assertEpsilonEquals(11.489, getSH().getMaxX());
	        assertEpsilonEquals(11.504, getSH().getMaxY());
	    }

		@DisplayName("#8")
		@Test
		public void test_8() {
	        double tiny = Double.MIN_VALUE;
	        getSH().setMinY(tiny);
	        assertEpsilonEquals(1.235, getSH().getMinX());
	        assertEpsilonEquals(tiny, getSH().getMinY());
	        assertEpsilonEquals(11.489, getSH().getMaxX());
	        assertEpsilonEquals(11.504, getSH().getMaxY());
	    }

		@DisplayName("#9")
		@Test
		public void test_9() {
	        double newMinY = 11.504 - 1e-12;
	        getSH().setMinY(newMinY);
	        assertEpsilonEquals(1.235, getSH().getMinX());
	        assertEpsilonEquals(newMinY, getSH().getMinY());
	        assertEpsilonEquals(11.489, getSH().getMaxX());
	        assertEpsilonEquals(11.504, getSH().getMaxY());
	    }

		@DisplayName("#10")
		@Test
		public void test_10() {
	        getSH().setMinY(11.504);
	        assertEpsilonEquals(1.235, getSH().getMinX());
	        assertEpsilonEquals(11.504, getSH().getMinY());
	        assertEpsilonEquals(11.489, getSH().getMaxX());
	        assertEpsilonEquals(11.504, getSH().getMaxY());
	    }

		@DisplayName("#11")
		@Test
		public void test_11() {
	        getSH().setMinY(20.0);
	        assertEpsilonEquals(1.235, getSH().getMinX());
	        assertEpsilonEquals(20., getSH().getMinY());
	        assertEpsilonEquals(11.489, getSH().getMaxX());
	        assertEpsilonEquals(20., getSH().getMaxY());
	    }

	}

	@DisplayName("setMaxY")
	@Nested
	public class SetMaxY {

		@DisplayName("#1")
		@Test
		public void test_1() {
	        getSH().setMaxY(20.);
	        assertEpsilonEquals(1.235, getSH().getMinX());
	        assertEpsilonEquals(-3.459, getSH().getMinY());
	        assertEpsilonEquals(11.489, getSH().getMaxX());
	        assertEpsilonEquals(20., getSH().getMaxY());
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
	        getSH().setMaxY(0.0);
	        assertEpsilonEquals(1.235, getSH().getMinX());
	        assertEpsilonEquals(-3.459, getSH().getMinY());
	        assertEpsilonEquals(11.489, getSH().getMaxX());
	        assertEpsilonEquals(0., getSH().getMaxY());
	    }

		@DisplayName("#3")
		@Test
		public void test_3() {
	        getSH().setMaxY(-10.0);
	        assertEpsilonEquals(1.235, getSH().getMinX());
	        assertEpsilonEquals(-10., getSH().getMinY());
	        assertEpsilonEquals(11.489, getSH().getMaxX());
	        assertEpsilonEquals(-10., getSH().getMaxY());
	    }

		@DisplayName("#4")
		@Test
		public void test_4() {
	        double originalMaxY = getSH().getMaxY();
	        getSH().setMaxY(originalMaxY);
	        assertEpsilonEquals(1.235, getSH().getMinX());
	        assertEpsilonEquals(-3.459, getSH().getMinY());
	        assertEpsilonEquals(11.489, getSH().getMaxX());
	        assertEpsilonEquals(11.504, getSH().getMaxY());
	    }

		@DisplayName("#5")
		@Test
		public void test_5() {
	        getSH().setMaxY(1.5e9);
	        assertEpsilonEquals(1.235, getSH().getMinX());
	        assertEpsilonEquals(-3.459, getSH().getMinY());
	        assertEpsilonEquals(11.489, getSH().getMaxX());
	        assertEpsilonEquals(1.5e9, getSH().getMaxY());
	    }

		@DisplayName("#6")
		@Test
		public void test_6() {
	        getSH().setMaxY(-1.2e9);
	        assertEpsilonEquals(1.235, getSH().getMinX());
	        assertEpsilonEquals(-1.2e9, getSH().getMinY());
	        assertEpsilonEquals(11.489, getSH().getMaxX());
	        assertEpsilonEquals(-1.2e9, getSH().getMaxY());
	    }

		@DisplayName("#7")
		@Test
		public void test_7() {
	        getSH().setMaxY(2.718281828459045);
	        assertEpsilonEquals(1.235, getSH().getMinX());
	        assertEpsilonEquals(-3.459, getSH().getMinY());
	        assertEpsilonEquals(11.489, getSH().getMaxX());
	        assertEpsilonEquals(2.718281828459045, getSH().getMaxY());
	    }

		@DisplayName("#8")
		@Test
		public void test_8() {
	        double tiny = Double.MIN_VALUE;
	        getSH().setMaxY(tiny);
	        assertEpsilonEquals(1.235, getSH().getMinX());
	        assertEpsilonEquals(-3.459, getSH().getMinY());
	        assertEpsilonEquals(11.489, getSH().getMaxX());
	        assertEpsilonEquals(tiny, getSH().getMaxY());
	    }

		@DisplayName("#9")
		@Test
		public void test_9() {
	        double newMaxY = -3.459 + 1e-12;
	        getSH().setMaxY(newMaxY);
	        assertEpsilonEquals(1.235, getSH().getMinX());
	        assertEpsilonEquals(-3.459, getSH().getMinY());
	        assertEpsilonEquals(11.489, getSH().getMaxX());
	        assertEpsilonEquals(newMaxY, getSH().getMaxY());
	    }

		@DisplayName("#10")
		@Test
		public void test_10() {
	        getSH().setMaxY(-3.459);
	        assertEpsilonEquals(1.235, getSH().getMinX());
	        assertEpsilonEquals(-3.459, getSH().getMinY());
	        assertEpsilonEquals(11.489, getSH().getMaxX());
	        assertEpsilonEquals(-3.459, getSH().getMaxY());
	    }

		@DisplayName("#11")
		@Test
		public void test_11() {
	        getSH().setMaxY(-5.0);
	        assertEpsilonEquals(1.235, getSH().getMinX());
	        assertEpsilonEquals(-5., getSH().getMinY());
	        assertEpsilonEquals(11.489, getSH().getMaxX());
	        assertEpsilonEquals(-5., getSH().getMaxY());
	    }

	}

	@DisplayName("toBoundingBox(B)")
	@Nested
	public class ToBoundingBoxB {
		
		private B box;

		@BeforeEach
		public void setUp() {
			box = factory.createBox(getSG(), 0, 0, 0, 0);
		}
		
		@DisplayName("#1")
		@Test
		public void test_1() {
			getSH().toBoundingBox(box);
			assertEpsilonEquals(1.235, getSH().getMinX());
			assertEpsilonEquals(-3.459, getSH().getMinY());
			assertEpsilonEquals(11.489, getSH().getMaxX());
			assertEpsilonEquals(11.504, getSH().getMaxY());
		}

	}

	@DisplayName("contains")
	@Nested
	public class Contains {

		@DisplayName("(Point1D) #1")
		@Test
		public void point_1() {
			assertFalse(getSH().contains(createPoint(0, 0)));
		}
		
		@DisplayName("(Point1D) #2")
		@Test
		public void point_2() {
	        assertFalse(getSH().contains(createPoint(12.0, 0.0)));
	    }

		@DisplayName("(Point1D) #3")
		@Test
		public void point_3() {
	        assertFalse(getSH().contains(createPoint(2.0, -4.0)));
	    }

		@DisplayName("(Point1D) #4")
		@Test
		public void point_4() {
	        assertFalse(getSH().contains(createPoint(2.0, 12.0)));
	    }

		@DisplayName("(Point1D) #5")
		@Test
		public void point_5() {
	        assertFalse(getSH().contains(createPoint(0.0, 12.0)));
	    }

		@DisplayName("(Point1D) #6")
		@Test
		public void point_6() {
	        assertTrue(getSH().contains(createPoint(6.362, 4.0225)));
	    }

		@DisplayName("(Point1D) #7")
		@Test
		public void point_7() {
	        assertTrue(getSH().contains(createPoint(2.0, 0.0)));
	    }

		@DisplayName("(Point1D) #8")
		@Test
		public void point_8() {
	        assertTrue(getSH().contains(createPoint(1.235, 0.0)));
	    }

		@DisplayName("(Point1D) #9")
		@Test
		public void point_9() {
	        assertTrue(getSH().contains(createPoint(11.488, 0.0)));
	    }

		@DisplayName("(Point1D) #10")
		@Test
		public void point_10() {
	        assertTrue(getSH().contains(createPoint(2.0, -3.459)));
	    }

		@DisplayName("(Point1D) #11")
		@Test
		public void point_11() {
	        assertTrue(getSH().contains(createPoint(2.0, 11.504)));
	    }

		@DisplayName("(Point1D) #12")
		@Test
		public void point_12() {
	        assertTrue(getSH().contains(createPoint(1.235, -3.459)));
	    }

		@DisplayName("(Point1D) #13")
		@Test
		public void point_13() {
	        assertTrue(getSH().contains(createPoint(11.488, 11.504)));
	    }

		@DisplayName("(Point1D) #14")
		@Test
		public void point_14() {
	        assertFalse(getSH().contains(createPoint(11.49, 0.0)));
	    }
	    
	    @DisplayName("(Segment1afp,double,double) #1")
		@Test
		public void segmentdoubledouble_1() {
			assertFalse(getSH().contains(getSG(), 0, 0));
		}
		
	    @DisplayName("(Segment1afp,double,double) #2")
		@Test
		public void segmentdoubledouble_2() {
	        assertFalse(getSH().contains(getSG(), 12.0, 0.0));
	    }

	    @DisplayName("(Segment1afp,double,double) #3")
		@Test
		public void segmentdoubledouble_3() {
	        assertFalse(getSH().contains(getSG(), 2.0, -4.0));
	    }

	    @DisplayName("(Segment1afp,double,double) #4")
		@Test
		public void segmentdoubledouble_4() {
	        assertFalse(getSH().contains(getSG(), 2.0, 12.0));
	    }

	    @DisplayName("(Segment1afp,double,double) #5")
		@Test
		public void segmentdoubledouble_5() {
	        assertFalse(getSH().contains(getSG(), 0.0, 12.0));
	    }

	    @DisplayName("(Segment1afp,double,double) #6")
		@Test
		public void segmentdoubledouble_6() {
	        assertTrue(getSH().contains(getSG(), 6.362, 4.0225));
	    }

	    @DisplayName("(Segment1afp,double,double) #7")
		@Test
		public void segmentdoubledouble_7() {
	        assertTrue(getSH().contains(getSG(), 2.0, 0.0));
	    }

	    @DisplayName("(Segment1afp,double,double) #8")
		@Test
		public void segmentdoubledouble_8() {
	        assertTrue(getSH().contains(getSG(), 1.235, 0.0));
	    }

	    @DisplayName("(Segment1afp,double,double) #9")
		@Test
		public void segmentdoubledouble_9() {
	        assertTrue(getSH().contains(getSG(), 11.488, 0.0));
	    }

	    @DisplayName("(Segment1afp,double,double) #10")
		@Test
		public void segmentdoubledouble_10() {
	        assertTrue(getSH().contains(getSG(), 2.0, -3.459));
	    }

	    @DisplayName("(Segment1afp,double,double) #11")
		@Test
		public void segmentdoubledouble_11() {
	        assertTrue(getSH().contains(getSG(), 2.0, 11.504));
	    }

	    @DisplayName("(Segment1afp,double,double) #12")
		@Test
		public void segmentdoubledouble_12() {
	        assertTrue(getSH().contains(getSG(), 1.235, -3.459));
	    }

	    @DisplayName("(Segment1afp,double,double) #13")
		@Test
		public void segmentdoubledouble_13() {
	        assertTrue(getSH().contains(getSG(), 11.488, 11.504));
	    }

	    @DisplayName("(Segment1afp,double,double) #14")
		@Test
		public void segmentdoubledouble_14() {
	        assertFalse(getSH().contains(getSG(), 11.49, 0.0));
	    }

		@DisplayName("(Rectangle1afp) #1")
		@Test
		public void rectangle_1() {
	        assertFalse(getSH().contains(createBox(0, 0, 1, 1)));
		}

		@DisplayName("(Rectangle1afp) #2")
		@Test
	    public void rectangle_2() {
	        assertTrue(getSH().contains(createBox(2, -2, 1, 1)));
	    }

		@DisplayName("(Rectangle1afp) #3")
		@Test
	    public void rectangle_3() {
	        assertFalse(getSH().contains(createBox(-5, 0, 1, 1)));
	    }

		@DisplayName("(Rectangle1afp) #4")
		@Test
	    public void rectangle_4() {
	        assertFalse(getSH().contains(createBox(12, 0, 1, 1)));
	    }

		@DisplayName("(Rectangle1afp) #5")
		@Test
	    public void rectangle_5() {
	        assertFalse(getSH().contains(createBox(2, -5, 1, 1)));
	    }

		@DisplayName("(Rectangle1afp) #6")
		@Test
	    public void rectangle_6() {
	        assertFalse(getSH().contains(createBox(2, 12, 1, 1)));
	    }

		@DisplayName("(Rectangle1afp) #7")
		@Test
	    public void rectangle_7() {
	        assertFalse(getSH().contains(createBox(1, 0, 2, 2)));
	    }

		@DisplayName("(Rectangle1afp) #8")
		@Test
	    public void rectangle_8() {
	        assertTrue(getSH().contains(createBox(1.235, -3.459, 10.254, 14.963)));
	    }

		@DisplayName("(Rectangle1afp) #9")
		@Test
	    public void rectangle_9() {
	        assertTrue(getSH().contains(createBox(1.235, 0, 1, 1)));
	    }

		@DisplayName("(Rectangle1afp) #10")
		@Test
	    public void rectangle_10() {
	        assertTrue(getSH().contains(createBox(2, 10.504, 1, 1)));
	    }

		@DisplayName("(Rectangle1afp) #11")
		@Test
	    public void rectangle_11() {
	        assertFalse(getSH().contains(createBox(1.0, 0, 1, 1)));
	    }

		@DisplayName("(Rectangle1afp) #12")
		@Test
	    public void rectangle_12() {
	        assertTrue(getSH().contains(createBox(5, 5, 0, 0)));
	    }

	    @DisplayName("(Shape1D) #1")
		@Test
		public void shape_1() {
	        assertFalse(getSH().contains((Shape1D) createBox(2, -5, 1, 1)));
		}

	    @DisplayName("(Shape1D) #2")
		@Test
		public void shape_2() {
	    	assertFalse(getSH().contains((Shape1D) createBox(0, 0, 1, 1)));
	    }

	    @DisplayName("(Shape1D) #3")
		@Test
		public void shape_3() {
	        assertTrue(getSH().contains((Shape1D) createBox(1.235, -3.459, 10.254, 14.963)));
	    }

	}

	@DisplayName("getClosestPointTo")
	@Nested
	public class GetClosestPointTo {
		
		@DisplayName("(Point1D) #1")
		@Test
		public void point_1() {
			assertThrows(AssertionError.class, () -> {
				getSH().getClosestPointTo((Point1D) null);
			});
		}

		@DisplayName("(Point1D) #2")
		@Test
		public void point_2() {
			var s = getSH().getClosestPointTo(createPoint(0, 0));
			assertSame(getSG(), s.getSegment());
			assertEpsilonEquals(factory.createPoint2d(1.235, 0), s);
		}

		@DisplayName("(Point1D) #3")
		@Test
		public void point_3() {
			Point1D p = createPoint(5.0, 0.0);
			var result = getSH().getClosestPointTo(p);
			assertSame(getSG(), result.getSegment());
			assertEpsilonEquals(5.0, result.getX());
			assertEpsilonEquals(0.0, result.getY());
		}

		@DisplayName("(Point1D) #4")
		@Test
		public void point_4() {
			Point1D p = createPoint(1.235, 0.0);
			var result = getSH().getClosestPointTo(p);
			assertSame(getSG(), result.getSegment());
			assertEpsilonEquals(1.235, result.getX());
			assertEpsilonEquals(0.0, result.getY());
		}

		@DisplayName("(Point1D) #5")
		@Test
		public void point_5() {
			Point1D p = createPoint(11.489, 0.0);
			var result = getSH().getClosestPointTo(p);
			assertSame(getSG(), result.getSegment());
			assertEpsilonEquals(11.489, result.getX());
			assertEpsilonEquals(0.0, result.getY());
		}

		@DisplayName("(Point1D) #6")
		@Test
		public void point_6() {
			Point1D p = createPoint(5.0, -3.459);
			var result = getSH().getClosestPointTo(p);
			assertSame(getSG(), result.getSegment());
			assertEpsilonEquals(5.0, result.getX());
			assertEpsilonEquals(-3.459, result.getY());
		}

		@DisplayName("(Point1D) #7")
		@Test
		public void point_7() {
			Point1D p = createPoint(5.0, 11.504);
			var result = getSH().getClosestPointTo(p);
			assertSame(getSG(), result.getSegment());
			assertEpsilonEquals(5.0, result.getX());
			assertEpsilonEquals(11.504, result.getY());
		}

		@DisplayName("(Point1D) #8")
		@Test
		public void point_8() {
			Point1D p = createPoint(0.0, 2.0);
			var result = getSH().getClosestPointTo(p);
			assertSame(getSG(), result.getSegment());
			assertEpsilonEquals(1.235, result.getX());
			assertEpsilonEquals(2.0, result.getY());
		}

		@DisplayName("(Point1D) #9")
		@Test
		public void point_9() {
			Point1D p = createPoint(12.0, 2.0);
			var result = getSH().getClosestPointTo(p);
			assertSame(getSG(), result.getSegment());
			assertEpsilonEquals(11.489, result.getX());
			assertEpsilonEquals(2.0, result.getY());
		}

		@DisplayName("(Point1D) #10")
		@Test
		public void point_10() {
			Point1D p = createPoint(5.0, -4.0);
			var result = getSH().getClosestPointTo(p);
			assertSame(getSG(), result.getSegment());
			assertEpsilonEquals(5.0, result.getX());
			assertEpsilonEquals(-3.459, result.getY());
		}

		@DisplayName("(Point1D) #11")
		@Test
		public void point_11() {
			Point1D p = createPoint(5.0, 12.0);
			var result = getSH().getClosestPointTo(p);
			assertSame(getSG(), result.getSegment());
			assertEpsilonEquals(5.0, result.getX());
			assertEpsilonEquals(11.504, result.getY());
		}

		@DisplayName("(Point1D) #12")
		@Test
		public void point_12() {
			Point1D p = createPoint(0.0, -4.0);
			var result = getSH().getClosestPointTo(p);
			assertSame(getSG(), result.getSegment());
			assertEpsilonEquals(1.235, result.getX());   // minX
			assertEpsilonEquals(-3.459, result.getY());  // minY
		}

		@DisplayName("(Point1D) #13")
		@Test
		public void point_13() {
			Point1D p = createPoint(12.0, -4.0);
			var result = getSH().getClosestPointTo(p);
			assertSame(getSG(), result.getSegment());
			assertEpsilonEquals(11.489, result.getX());  // maxX
			assertEpsilonEquals(-3.459, result.getY());  // minY
		}

		@DisplayName("(Point1D) #14")
		@Test
		public void point_14() {
			Point1D p = createPoint(0.0, 12.0);
			var result = getSH().getClosestPointTo(p);
			assertSame(getSG(), result.getSegment());
			assertEpsilonEquals(1.235, result.getX());   // minX
			assertEpsilonEquals(11.504, result.getY());  // maxY
		}

		@DisplayName("(Point1D) #15")
		@Test
		public void point_15() {
			Point1D p = createPoint(12.0, 12.0);
			var result = getSH().getClosestPointTo(p);
			assertSame(getSG(), result.getSegment());
			assertEpsilonEquals(11.489, result.getX());  // maxX
			assertEpsilonEquals(11.504, result.getY());  // maxY
		}

		@DisplayName("(Point1D) #16")
		@Test
		public void point_16() {
			// Far top-right
			Point1D p = createPoint(1e6, 1e6);
			var result = getSH().getClosestPointTo(p);
			assertSame(getSG(), result.getSegment());
			assertEpsilonEquals(11.489, result.getX());
			assertEpsilonEquals(11.504, result.getY());
		}

		@DisplayName("(Rectangle1afp) #1")
		@Test
		public void rectangle_1() {
			assertThrows(AssertionError.class, () -> {
				getSH().getClosestPointTo((Rectangle1afp) null);
			});
		}

		@DisplayName("(Rectangle1afp) #2")
		@Test
		public void rectangle_2() {
			var b = factory.createBox(getSG(), 0, 0, 1, 1);
			var result = getSH().getClosestPointTo(b);
			assertSame(getSG(), result.getSegment());
			assertEpsilonEquals(createPoint(1.235, .5), result);
		}
		
		@DisplayName("(Rectangle1afp) #3")
		@Test
		public void rectangle_3() {
			var b = factory.createBox(getSG(), 5, -2, 10, 8);
			var result = getSH().getClosestPointTo(b);
			assertSame(getSG(), result.getSegment());
			assertEpsilonEquals(createPoint(10., 2.), result);
		}

		@DisplayName("(Rectangle1afp) #4")
		@Test
		public void rectangle_4() {
			var b = factory.createBox(getSG(), 2, -2, 5, 6);
			var result = getSH().getClosestPointTo(b);
			assertSame(getSG(), result.getSegment());
			assertEpsilonEquals(createPoint(4.5, 1.), result);
		}

		@DisplayName("(Rectangle1afp) #5")
		@Test
		public void rectangle_5() {
			var b = factory.createBox(getSG(), 0, -5, 20, 25);
			var result = getSH().getClosestPointTo(b);
			assertSame(getSG(), result.getSegment());
			assertEpsilonEquals(createPoint(10., 7.5), result);
		}

		@DisplayName("(Rectangle1afp) #6")
		@Test
		public void rectangle_6() {
			var b = factory.createBox(getSG(), 1.235 - 3, 0, 3, 5);
			var result = getSH().getClosestPointTo(b);
			assertSame(getSG(), result.getSegment());
			assertEpsilonEquals(createPoint(1.235, 2.5), result);
		}

		@DisplayName("(Rectangle1afp) #7")
		@Test
		public void rectangle_7() {
			var b = factory.createBox(getSG(), 11.489, 0, 4, 6);
			var result = getSH().getClosestPointTo(b);
			assertSame(getSG(), result.getSegment());
			assertEpsilonEquals(createPoint(11.489, 3.), result);
		}

		@DisplayName("(Rectangle1afp) #8")
		@Test
		public void rectangle_8() {
			var b = factory.createBox(getSG(), 0, 0, 0.5, 1);
			var result = getSH().getClosestPointTo(b);
			assertSame(getSG(), result.getSegment());
			assertEpsilonEquals(createPoint(1.235, .5), result);
		}

		@DisplayName("(Rectangle1afp) #9")
		@Test
		public void rectangle_9() {
			var b = factory.createBox(getSG(), 12, 0, 0.5, 1);
			var result = getSH().getClosestPointTo(b);
			assertSame(getSG(), result.getSegment());
			assertEpsilonEquals(createPoint(11.489, .5), result);
		}

		@DisplayName("(Rectangle1afp) #10")
		@Test
		public void rectangle_10() {
			var b = factory.createBox(getSG(), 5, -4.5, 1, 0.5);
			var result = getSH().getClosestPointTo(b);
			assertSame(getSG(), result.getSegment());
			assertEpsilonEquals(createPoint(5.5, -3.459), result);
		}

		@DisplayName("(Rectangle1afp) #11")
		@Test
		public void rectangle_11() {
			var b = factory.createBox(getSG(), 5, 12, 1, 0.5);
			var result = getSH().getClosestPointTo(b);
			assertSame(getSG(), result.getSegment());
			assertEpsilonEquals(createPoint(5.5, 11.504), result);
		}

		@DisplayName("(Rectangle1afp) #12")
		@Test
		public void rectangle_12() {
			var b = factory.createBox(getSG(), 0, -5, 0.5, 0.5);
			var result = getSH().getClosestPointTo(b);
			assertSame(getSG(), result.getSegment());
			assertEpsilonEquals(createPoint(1.235, -3.459), result);
		}

		@DisplayName("(Rectangle1afp) #13")
		@Test
		public void rectangle_13() {
			var b = factory.createBox(getSG(), 12, 12, 0.5, 0.5);
			var result = getSH().getClosestPointTo(b);
			assertSame(getSG(), result.getSegment());
			assertEpsilonEquals(createPoint(11.489, 11.504), result);
		}

		@DisplayName("(Rectangle1afp) #14")
		@Test
		public void rectangle_14() {
			var b = factory.createBox(getSG(), 5, 0, 0, 0);
			var result = getSH().getClosestPointTo(b);
			assertSame(getSG(), result.getSegment());
			assertEpsilonEquals(createPoint(5., 0.), result);
		}

		@DisplayName("(Rectangle1afp) #15")
		@Test
		public void rectangle_15() {
			var b = factory.createBox(getSG(), 0, 2, 0, 0);
			var result = getSH().getClosestPointTo(b);
			assertSame(getSG(), result.getSegment());
			assertEpsilonEquals(createPoint(1.235, 2.), result);
		}

		@DisplayName("(Rectangle1afp) #16")
		@Test
		public void rectangle_16() {
			var b = factory.createBox(getSG(), 5, 12, 0, 0);
			var result = getSH().getClosestPointTo(b);
			assertSame(getSG(), result.getSegment());
			assertEpsilonEquals(createPoint(5., 11.504), result);
		}

		@DisplayName("(Shape1D) #1")
		@Test
		public void shape_1() {
			Shape1D b = factory.createBox(getSG(), 0, 2, 0, 0);
			var result = getSH().getClosestPointTo(b);
			assertSame(getSG(), result.getSegment());
			assertEpsilonEquals(createPoint(1.235, 2.), result);
		}

		@DisplayName("(Shape1D) #2")
		@Test
		public void shape_2() {
			Shape1D b = factory.createBox(getSG(), 5, 12, 1, 0.5);
			var result = getSH().getClosestPointTo(b);
			assertSame(getSG(), result.getSegment());
			assertEpsilonEquals(createPoint(5.5, 11.504), result);
		}

	}

	@DisplayName("getFarthestPointTo")
	@Nested
	public class GetFarthestPointTo {

		@DisplayName("(Point1D) #1")
		@Test
		public void point_1() {
			assertThrows(AssertionError.class, () -> {
				getSH().getFarthestPointTo((Point1D) null);
			});
		}

		@DisplayName("(Point1D) #2")
		@Test
		public void point_2() {
			var s = getSH().getFarthestPointTo(createPoint(0, 0));
			assertSame(getSG(), s.getSegment());
			assertEpsilonEquals(createPoint(11.489, 11.504), s);
		}

		@DisplayName("(Point1D) #3")
		@Test
		public void point_3() {
			Point1D p = createPoint(5.0, 0.0);
			var result = getSH().getFarthestPointTo(p);
			assertSame(getSG(), result.getSegment());
			assertEpsilonEquals(createPoint(11.489, 11.504), result);
		}

		@DisplayName("(Point1D) #4")
		@Test
		public void point_4() {
			Point1D p = createPoint(1.235, 0.0);
			var result = getSH().getFarthestPointTo(p);
			assertSame(getSG(), result.getSegment());
			assertEpsilonEquals(createPoint(11.489, 11.504), result);
		}

		@DisplayName("(Point1D) #5")
		@Test
		public void point_5() {
			Point1D p = createPoint(11.489, 0.0);
			var result = getSH().getFarthestPointTo(p);
			assertSame(getSG(), result.getSegment());
			assertEpsilonEquals(createPoint(1.235, 11.504), result);
		}

		@DisplayName("(Point1D) #6")
		@Test
		public void point_6() {
			Point1D p = createPoint(5.0, -3.459);
			var result = getSH().getFarthestPointTo(p);
			assertSame(getSG(), result.getSegment());
			assertEpsilonEquals(createPoint(11.489, 11.504), result);
		}

		@DisplayName("(Point1D) #7")
		@Test
		public void point_7() {
			Point1D p = createPoint(5.0, 11.504);
			var result = getSH().getFarthestPointTo(p);
			assertSame(getSG(), result.getSegment());
			assertEpsilonEquals(createPoint(11.489, -3.459), result);
		}

		@DisplayName("(Point1D) #8")
		@Test
		public void point_8() {
			Point1D p = createPoint(0.0, 2.0);
			var result = getSH().getFarthestPointTo(p);
			assertSame(getSG(), result.getSegment());
			assertEpsilonEquals(createPoint(11.489, 11.504), result);
		}

		@DisplayName("(Point1D) #9")
		@Test
		public void point_9() {
			Point1D p = createPoint(12.0, 2.0);
			var result = getSH().getFarthestPointTo(p);
			assertSame(getSG(), result.getSegment());
			assertEpsilonEquals(createPoint(1.235, 11.504), result);
		}

		@DisplayName("(Point1D) #10")
		@Test
		public void point_10() {
			Point1D p = createPoint(5.0, -4.0);
			var result = getSH().getFarthestPointTo(p);
			assertSame(getSG(), result.getSegment());
			assertEpsilonEquals(createPoint(11.489, 11.504), result);
		}

		@DisplayName("(Point1D) #11")
		@Test
		public void point_11() {
			Point1D p = createPoint(5.0, 12.0);
			var result = getSH().getFarthestPointTo(p);
			assertSame(getSG(), result.getSegment());
			assertEpsilonEquals(createPoint(11.489, -3.459), result);
		}

		@DisplayName("(Point1D) #12")
		@Test
		public void point_12() {
			Point1D p = createPoint(0.0, -4.0);
			var result = getSH().getFarthestPointTo(p);
			assertSame(getSG(), result.getSegment());
			assertEpsilonEquals(createPoint(11.489, 11.504), result);
		}

		@DisplayName("(Point1D) #13")
		@Test
		public void point_13() {
			Point1D p = createPoint(12.0, -4.0);
			var result = getSH().getFarthestPointTo(p);
			assertSame(getSG(), result.getSegment());
			assertEpsilonEquals(createPoint(1.235, 11.504), result);
		}

		@DisplayName("(Point1D) #14")
		@Test
		public void point_14() {
			Point1D p = createPoint(0.0, 12.0);
			var result = getSH().getFarthestPointTo(p);
			assertSame(getSG(), result.getSegment());
			assertEpsilonEquals(createPoint(11.489, -3.459), result);
		}

		@DisplayName("(Point1D) #15")
		@Test
		public void point_15() {
			Point1D p = createPoint(12.0, 12.0);
			var result = getSH().getFarthestPointTo(p);
			assertSame(getSG(), result.getSegment());
			assertEpsilonEquals(createPoint(1.235, -3.459), result);
		}

		@DisplayName("(Point1D) #16")
		@Test
		public void point_16() {
			// Far top-right
			Point1D p = createPoint(1e6, 1e6);
			var result = getSH().getFarthestPointTo(p);
			assertSame(getSG(), result.getSegment());
			assertEpsilonEquals(createPoint(1.235, -3.459), result);
		}

	}

	@DisplayName("getDistance")
	@Nested
	public class GetDistance {
		
		@DisplayName("(Point1D) #1")
		@Test
		public void point_1() {
			assertEpsilonEquals(1, getSH().getDistance(createPoint(0.235, -3.459)));
		}

		@DisplayName("(Point1D) #2")
		@Test
		public void point_2() {
			double x = 5.0;
			double y = 0.0;
			assertEpsilonEquals(0.0, getSH().getDistance(createPoint(x, y)));
		}

		@DisplayName("(Point1D) #3")
		@Test
		public void point_3() {
			double x = 1.235;
			double y = 0.0;
			assertEpsilonEquals(0.0, getSH().getDistance(createPoint(x, y)));
		}

		@DisplayName("(Point1D) #4")
		@Test
		public void point_4() {
			double x = 11.489;
			double y = 0.0;
			assertEpsilonEquals(0.0, getSH().getDistance(createPoint(x, y)));
		}

		@DisplayName("(Point1D) #5")
		@Test
		public void point_5() {
			double x = 5.0;
			double y = -3.459;
			assertEpsilonEquals(0.0, getSH().getDistance(createPoint(x, y)));
		}

		@DisplayName("(Point1D) #6")
		@Test
		public void point_6() {
			double x = 5.0;
			double y = 11.504;
			assertEpsilonEquals(0.0, getSH().getDistance(createPoint(x, y)));
		}

		@DisplayName("(Point1D) #7")
		@Test
		public void point_7() {
			double x = 0.0;
			double y = 0.0;
			double expected = 1.235 - x; // 1.235
			assertEpsilonEquals(expected, getSH().getDistance(createPoint(x, y)));
		}

		@DisplayName("(Point1D) #8")
		@Test
		public void point_8() {
			double x = 12.0;
			double y = 0.0;
			double expected = x - 11.489; // 0.511
			assertEpsilonEquals(expected, getSH().getDistance(createPoint(x, y)));
		}

		@DisplayName("(Point1D) #9")
		@Test
		public void point_9() {
			double x = 5.0;
			double y = -4.0;
			double expected = -3.459 - y; // 0.541
			assertEpsilonEquals(expected, getSH().getDistance(createPoint(x, y)));
		}

		@DisplayName("(Point1D) #10")
		@Test
		public void point_10() {
			double x = 5.0;
			double y = 12.0;
			double expected = y - 11.504; // 0.496
			assertEpsilonEquals(expected, getSH().getDistance(createPoint(x, y)));
		}

		@DisplayName("(Point1D) #11")
		@Test
		public void point_11() {
			double x = 0.0;
			double y = -4.0;
			double expected = Math.hypot(1.235 - x, -3.459 - y); // distance to corner (minX,minY)
			assertEpsilonEquals(expected, getSH().getDistance(createPoint(x, y)));
		}

		@DisplayName("(Point1D) #12")
		@Test
		public void point_12() {
			double x = 12.0;
			double y = 12.0;
			double expected = Math.hypot(x - 11.489, y - 11.504);
			assertEpsilonEquals(expected, getSH().getDistance(createPoint(x, y)));
		}

		@DisplayName("(Point1D) #13")
		@Test
		public void point_13() {
			double x = 1e6;
			double y = 1e6;
			// distance to top-right corner (11.489, 11.504)
			double expected = Math.hypot(x - 11.489, y - 11.504);
			assertEpsilonEquals(expected, getSH().getDistance(createPoint(x, y)));
		}

		@DisplayName("(Rectangle1afp) #1")
		@Test
		public void rectangle_1() {
			//Reference shape is: minx=1.235, miny=-3.459, width=10.254, height=14.963, maxx=11.489, maxy=11.504
			assertEpsilonEquals(1, getSH().getDistance(factory.createBox(getSG(), 0.235, -3.459, 0, 0)));
		}

		@DisplayName("(Rectangle1afp) #2")
		@Test
		public void rectangle_2() {
	        double minX = 5.0, minY = -2.0, width = 10.0, height = 8.0;
	        assertEpsilonEquals(0.0, getSH().getDistance(factory.createBox(getSG(), minX, minY, width, height)));
	    }

		@DisplayName("(Rectangle1afp) #3")
		@Test
		public void rectangle_3() {
	        double minX = 2.0, minY = -2.0, width = 5.0, height = 6.0;
	        assertEpsilonEquals(0.0, getSH().getDistance(factory.createBox(getSG(), minX, minY, width, height)));
	    }

		@DisplayName("(Rectangle1afp) #4")
		@Test
		public void rectangle_4() {
	        double minX = 0.0, minY = -5.0, width = 20.0, height = 25.0;
	        assertEpsilonEquals(0.0, getSH().getDistance(factory.createBox(getSG(), minX, minY, width, height)));
	    }

		@DisplayName("(Rectangle1afp) #5")
		@Test
		public void rectangle_5() {
	        double minX = 1.235 - 3.0, minY = 0.0, width = 3.0, height = 5.0;
	        assertEpsilonEquals(0.0, getSH().getDistance(factory.createBox(getSG(), minX, minY, width, height)));
	    }

		@DisplayName("(Rectangle1afp) #6")
		@Test
		public void rectangle_6() {
	        double minX = 11.489, minY = 0.0, width = 4.0, height = 6.0;
	        assertEpsilonEquals(0.0, getSH().getDistance(factory.createBox(getSG(), minX, minY, width, height)));
	    }

		@DisplayName("(Rectangle1afp) #7")
		@Test
		public void rectangle_7() {
	        double minX = 0.0, minY = 0.0, width = 0.5, height = 1.0;
	        assertEpsilonEquals(0.735, getSH().getDistance(factory.createBox(getSG(), minX, minY, width, height)));
	    }

		@DisplayName("(Rectangle1afp) #8")
		@Test
		public void rectangle_8() {
	        double minX = 12.0, minY = 0.0, width = 0.5, height = 1.0;
	        assertEpsilonEquals(0.511, getSH().getDistance(factory.createBox(getSG(), minX, minY, width, height)));
	    }

		@DisplayName("(Rectangle1afp) #9")
		@Test
		public void rectangle_9() {
	        double minX = 5.0, minY = -4.5, width = 1.0, height = 0.5;
	        assertEpsilonEquals(0.541, getSH().getDistance(factory.createBox(getSG(), minX, minY, width, height)));
	    }

		@DisplayName("(Rectangle1afp) #10")
		@Test
		public void rectangle_10() {
	        double minX = 5.0, minY = 12.0, width = 1.0, height = 0.5;
	        assertEpsilonEquals(0.496, getSH().getDistance(factory.createBox(getSG(), minX, minY, width, height)));
	    }

		@DisplayName("(Rectangle1afp) #11")
		@Test
		public void rectangle_11() {
	        double minX = 0.0, minY = -5.0, width = 0.5, height = 0.5;
	        assertEpsilonEquals(1.274325704049, getSH().getDistance(factory.createBox(getSG(), minX, minY, width, height)));
	    }

		@DisplayName("(Rectangle1afp) #12")
		@Test
		public void rectangle_12() {
	        double minX = 12.0, minY = 12.0, width = 0.5, height = 0.5;
	        double dx = minX - 11.489;
	        double dy = minY - 11.504;
	        double expected = Math.hypot(dx, dy);
	        assertEpsilonEquals(expected, getSH().getDistance(factory.createBox(getSG(), minX, minY, width, height)));
	    }

		@DisplayName("(Rectangle1afp) #13")
		@Test
		public void rectangle_13() {
	        assertEpsilonEquals(0.0, getSH().getDistance(factory.createBox(getSG(), 5.0, 0.0, 0, 0)));
	    }

		@DisplayName("(Rectangle1afp) #14")
		@Test
		public void rectangle_14() {
	        double x = 5.0, y = 12.0;
	        double expected = y - 11.504;
	        assertEpsilonEquals(expected, getSH().getDistance(factory.createBox(getSG(), x, y, 0, 0)));
	    }

	    @DisplayName("(Shape1D) #1")
		@Test
		public void shape_1() {
	        double minX = 12.0, minY = 12.0, width = 0.5, height = 0.5;
	        double dx = minX - 11.489;
	        double dy = minY - 11.504;
	        double expected = Math.hypot(dx, dy);
	        Shape1D shp = factory.createBox(getSG(), minX, minY, width, height);
	        assertEpsilonEquals(expected, getSH().getDistance(shp));
	    }

	}

	@DisplayName("getDistanceL1")
	@Nested
	public class GetDistanceL1 {

		@DisplayName("(Point1D) #1")
		@Test
		public void point_1() {
			assertEpsilonEquals(1, getSH().getDistanceL1(createPoint(0.235, -3.459)));
		}

		@DisplayName("(Point1D) #2")
		@Test
		public void point_2() {
			double x = 5.0;
			double y = 0.0;
			assertEpsilonEquals(0.0, getSH().getDistanceL1(createPoint(x, y)));
		}

		@DisplayName("(Point1D) #3")
		@Test
		public void point_3() {
			double x = 1.235;
			double y = 0.0;
			assertEpsilonEquals(0.0, getSH().getDistanceL1(createPoint(x, y)));
		}

		@DisplayName("(Point1D) #4")
		@Test
		public void point_4() {
			double x = 11.489;
			double y = 0.0;
			assertEpsilonEquals(0.0, getSH().getDistanceL1(createPoint(x, y)));
		}

		@DisplayName("(Point1D) #5")
		@Test
		public void point_5() {
			double x = 5.0;
			double y = -3.459;
			assertEpsilonEquals(0.0, getSH().getDistanceL1(createPoint(x, y)));
		}

		@DisplayName("(Point1D) #6")
		@Test
		public void point_6() {
			double x = 5.0;
			double y = 11.504;
			assertEpsilonEquals(0.0, getSH().getDistanceL1(createPoint(x, y)));
		}

		@DisplayName("(Point1D) #7")
		@Test
		public void point_7() {
			double x = 0.0;
			double y = 0.0;
			double expected = 1.235 - x; // 1.235
			assertEpsilonEquals(expected, getSH().getDistanceL1(createPoint(x, y)));
		}

		@DisplayName("(Point1D) #8")
		@Test
		public void point_8() {
			double x = 12.0;
			double y = 0.0;
			double expected = x - 11.489; // 0.511
			assertEpsilonEquals(expected, getSH().getDistanceL1(createPoint(x, y)));
		}

		@DisplayName("(Point1D) #9")
		@Test
		public void point_9() {
			double x = 5.0;
			double y = -4.0;
			double expected = -3.459 - y; // 0.541
			assertEpsilonEquals(expected, getSH().getDistanceL1(createPoint(x, y)));
		}

		@DisplayName("(Point1D) #10")
		@Test
		public void point_10() {
			double x = 5.0;
			double y = 12.0;
			double expected = y - 11.504; // 0.496
			assertEpsilonEquals(expected, getSH().getDistanceL1(createPoint(x, y)));
		}

		@DisplayName("(Point1D) #11")
		@Test
		public void point_11() {
			double x = 0.0;
			double y = -4.0;
			assertEpsilonEquals(1.776, getSH().getDistanceL1(createPoint(x, y)));
		}

		@DisplayName("(Point1D) #12")
		@Test
		public void point_12() {
			double x = 12.0;
			double y = 12.0;
			assertEpsilonEquals(1.007, getSH().getDistanceL1(createPoint(x, y)));
		}

		@DisplayName("(Point1D) #13")
		@Test
		public void point_13() {
			double x = 1e6;
			double y = 1e6;
			assertEpsilonEquals(1999977.007, getSH().getDistanceL1(createPoint(x, y)));
		}

	}

	@DisplayName("getDistanceLinf")
	@Nested
	public class GetDistanceLinf {
		
		@DisplayName("(Point1D) #1")
		@Test
		public void point_1() {
			assertEpsilonEquals(1, getSH().getDistanceLinf(createPoint(0.235, -3.459)));
		}

		@DisplayName("(Point1D) #2")
		@Test
		public void point_2() {
			double x = 5.0;
			double y = 0.0;
			assertEpsilonEquals(0.0, getSH().getDistanceLinf(createPoint(x, y)));
		}

		@DisplayName("(Point1D) #3")
		@Test
		public void point_3() {
			double x = 1.235;
			double y = 0.0;
			assertEpsilonEquals(0.0, getSH().getDistanceLinf(createPoint(x, y)));
		}

		@DisplayName("(Point1D) #4")
		@Test
		public void point_4() {
			double x = 11.489;
			double y = 0.0;
			assertEpsilonEquals(0.0, getSH().getDistanceLinf(createPoint(x, y)));
		}

		@DisplayName("(Point1D) #5")
		@Test
		public void point_5() {
			double x = 5.0;
			double y = -3.459;
			assertEpsilonEquals(0.0, getSH().getDistanceLinf(createPoint(x, y)));
		}

		@DisplayName("(Point1D) #6")
		@Test
		public void point_6() {
			double x = 5.0;
			double y = 11.504;
			assertEpsilonEquals(0.0, getSH().getDistanceLinf(createPoint(x, y)));
		}

		@DisplayName("(Point1D) #7")
		@Test
		public void point_7() {
			double x = 0.0;
			double y = 0.0;
			double expected = 1.235 - x; // 1.235
			assertEpsilonEquals(expected, getSH().getDistanceLinf(createPoint(x, y)));
		}

		@DisplayName("(Point1D) #8")
		@Test
		public void point_8() {
			double x = 12.0;
			double y = 0.0;
			double expected = x - 11.489; // 0.511
			assertEpsilonEquals(expected, getSH().getDistanceLinf(createPoint(x, y)));
		}

		@DisplayName("(Point1D) #9")
		@Test
		public void point_9() {
			double x = 5.0;
			double y = -4.0;
			double expected = -3.459 - y; // 0.541
			assertEpsilonEquals(expected, getSH().getDistanceLinf(createPoint(x, y)));
		}

		@DisplayName("(Point1D) #10")
		@Test
		public void point_10() {
			double x = 5.0;
			double y = 12.0;
			double expected = y - 11.504; // 0.496
			assertEpsilonEquals(expected, getSH().getDistanceLinf(createPoint(x, y)));
		}

		@DisplayName("(Point1D) #11")
		@Test
		public void point_11() {
			double x = 0.0;
			double y = -4.0;
			assertEpsilonEquals(1.235, getSH().getDistanceLinf(createPoint(x, y)));
		}

		@DisplayName("(Point1D) #12")
		@Test
		public void point_12() {
			double x = 12.0;
			double y = 12.0;
			assertEpsilonEquals(0.511, getSH().getDistanceLinf(createPoint(x, y)));
		}

		@DisplayName("(Point1D) #13")
		@Test
		public void point_13() {
			double x = 1e6;
			double y = 1e6;
			assertEpsilonEquals(999988.511, getSH().getDistanceLinf(createPoint(x, y)));
		}

	}

	@DisplayName("getDistanceSquared")
	@Nested
	public class GetDistanceSquared {
		
		@DisplayName("(Point1D) #1")
		@Test
		public void point_1() {
			assertEpsilonEquals(1., getSH().getDistanceSquared(createPoint(0.235, -3.459)));
		}

		@DisplayName("(Point1D) #2")
		@Test
		public void point_2() {
			double x = 5.0;
			double y = 0.0;
			assertEpsilonEquals(0., getSH().getDistanceSquared(createPoint(x, y)));
		}

		@DisplayName("(Point1D) #3")
		@Test
		public void point_3() {
			double x = 1.235;
			double y = 0.0;
			assertEpsilonEquals(0., getSH().getDistanceSquared(createPoint(x, y)));
		}

		@DisplayName("(Point1D) #4")
		@Test
		public void point_4() {
			double x = 11.489;
			double y = 0.0;
			assertEpsilonEquals(0., getSH().getDistanceSquared(createPoint(x, y)));
		}

		@DisplayName("(Point1D) #5")
		@Test
		public void point_5() {
			double x = 5.0;
			double y = -3.459;
			assertEpsilonEquals(0., getSH().getDistanceSquared(createPoint(x, y)));
		}

		@DisplayName("(Point1D) #6")
		@Test
		public void point_6() {
			double x = 5.0;
			double y = 11.504;
			assertEpsilonEquals(0., getSH().getDistanceSquared(createPoint(x, y)));
		}

		@DisplayName("(Point1D) #7")
		@Test
		public void point_7() {
			double x = 0.0;
			double y = 0.0;
			assertEpsilonEquals(1.525225, getSH().getDistanceSquared(createPoint(x, y)));
		}

		@DisplayName("(Point1D) #8")
		@Test
		public void point_8() {
			double x = 12.0;
			double y = 0.0;
			assertEpsilonEquals(0.261121, getSH().getDistanceSquared(createPoint(x, y)));
		}

		@DisplayName("(Point1D) #9")
		@Test
		public void point_9() {
			double x = 5.0;
			double y = -4.0;
			assertEpsilonEquals(0.292681, getSH().getDistanceSquared(createPoint(x, y)));
		}

		@DisplayName("(Point1D) #10")
		@Test
		public void point_10() {
			double x = 5.0;
			double y = 12.0;
			assertEpsilonEquals(0.246016, getSH().getDistanceSquared(createPoint(x, y)));
		}

		@DisplayName("(Point1D) #11")
		@Test
		public void point_11() {
			double x = 0.0;
			double y = -4.0;
			assertEpsilonEquals(1.817906, getSH().getDistanceSquared(createPoint(x, y)));
		}

		@DisplayName("(Point1D) #12")
		@Test
		public void point_12() {
			double x = 12.0;
			double y = 12.0;
			assertEpsilonEquals(0.507137, getSH().getDistanceSquared(createPoint(x, y)));
		}

		@DisplayName("(Point1D) #13")
		@Test
		public void point_13() {
			double x = 1e6;
			double y = 1e6;
			assertEpsilonEquals(1.9999540142643394E12, getSH().getDistanceSquared(createPoint(x, y)));
		}

		@DisplayName("(Rectangle1afp) #1")
		@Test
		public void rectangle_1() {
			assertEpsilonEquals(1, getSH().getDistanceSquared(factory.createBox(getSG(), 0.235, -3.459, 0, 0)));
		}

		@DisplayName("(Rectangle1afp) #2")
		@Test
		public void rectangle_2() {
	        double minX = 5.0, minY = -2.0, width = 10.0, height = 8.0;
	        assertEpsilonEquals(0., getSH().getDistanceSquared(factory.createBox(getSG(), minX, minY, width, height)));
	    }

		@DisplayName("(Rectangle1afp) #3")
		@Test
		public void rectangle_3() {
	        double minX = 2.0, minY = -2.0, width = 5.0, height = 6.0;
	        assertEpsilonEquals(0., getSH().getDistanceSquared(factory.createBox(getSG(), minX, minY, width, height)));
	    }

		@DisplayName("(Rectangle1afp) #4")
		@Test
		public void rectangle_4() {
	        double minX = 0.0, minY = -5.0, width = 20.0, height = 25.0;
	        assertEpsilonEquals(0., getSH().getDistanceSquared(factory.createBox(getSG(), minX, minY, width, height)));
	    }

		@DisplayName("(Rectangle1afp) #5")
		@Test
		public void rectangle_5() {
	        double minX = 1.235 - 3.0, minY = 0.0, width = 3.0, height = 5.0;
	        assertEpsilonEquals(0., getSH().getDistanceSquared(factory.createBox(getSG(), minX, minY, width, height)));
	    }

		@DisplayName("(Rectangle1afp) #6")
		@Test
		public void rectangle_6() {
	        double minX = 11.489, minY = 0.0, width = 4.0, height = 6.0;
	        assertEpsilonEquals(0., getSH().getDistanceSquared(factory.createBox(getSG(), minX, minY, width, height)));
	    }

		@DisplayName("(Rectangle1afp) #7")
		@Test
		public void rectangle_7() {
	        double minX = 0.0, minY = 0.0, width = 0.5, height = 1.0;
	        assertEpsilonEquals(0.540225, getSH().getDistanceSquared(factory.createBox(getSG(), minX, minY, width, height)));
	    }

		@DisplayName("(Rectangle1afp) #8")
		@Test
		public void rectangle_8() {
	        double minX = 12.0, minY = 0.0, width = 0.5, height = 1.0;
	        assertEpsilonEquals(0.261121, getSH().getDistanceSquared(factory.createBox(getSG(), minX, minY, width, height)));
	    }

		@DisplayName("(Rectangle1afp) #9")
		@Test
		public void rectangle_9() {
	        double minX = 5.0, minY = -4.5, width = 1.0, height = 0.5;
	        assertEpsilonEquals(0.292681, getSH().getDistanceSquared(factory.createBox(getSG(), minX, minY, width, height)));
	    }

		@DisplayName("(Rectangle1afp) #10")
		@Test
		public void rectangle_10() {
	        double minX = 5.0, minY = 12.0, width = 1.0, height = 0.5;
	        assertEpsilonEquals(0.246016, getSH().getDistanceSquared(factory.createBox(getSG(), minX, minY, width, height)));
	    }

		@DisplayName("(Rectangle1afp) #11")
		@Test
		public void rectangle_11() {
	        double minX = 0.0, minY = -5.0, width = 0.5, height = 0.5;
	        assertEpsilonEquals(1.623906, getSH().getDistanceSquared(factory.createBox(getSG(), minX, minY, width, height)));
	    }

		@DisplayName("(Rectangle1afp) #12")
		@Test
		public void rectangle_12() {
	        double minX = 12.0, minY = 12.0, width = 0.5, height = 0.5;
	        assertEpsilonEquals(0.507137, getSH().getDistanceSquared(factory.createBox(getSG(), minX, minY, width, height)));
	    }

		@DisplayName("(Rectangle1afp) #13")
		@Test
		public void rectangle_13() {
	        assertEpsilonEquals(0., getSH().getDistanceSquared(factory.createBox(getSG(), 5.0, 0.0, 0, 0)));
	    }

		@DisplayName("(Rectangle1afp) #14")
		@Test
		public void rectangle_14() {
	        double x = 5.0, y = 12.0;
	        assertEpsilonEquals(0.246016, getSH().getDistanceSquared(factory.createBox(getSG(), x, y, 0, 0)));
	    }

	    @DisplayName("(Shape1D) #1")
		@Test
		public void shape_1() {
	        double minX = 12.0, minY = 12.0, width = 0.5, height = 0.5;
	        Shape1D shp = factory.createBox(getSG(), minX, minY, width, height);
	        assertEpsilonEquals(0.507137, getSH().getDistanceSquared(shp));
	    }

	}

	@DisplayName("getType")
	@Nested
	public class GetType {
		
		@DisplayName("()")
		@Test
		public void noparam() {
			assertSame(Shape1DType.RECTANGLE, getSH().getType());
		}

		@DisplayName("(Class)")
		@Test
		public void type() {
			assertSame(Shape1DType.RECTANGLE, getSH().getType(Shape1DType.class));
		}

	}

	@DisplayName("intersects")
	@Nested
	public class Intersects {
		
		@DisplayName("(Rectangle1afp) #1")
		@Test
		public void rectangle_1() {
	        assertFalse(getSH().intersects(createBox(0, 0, 1, 1)));
		}

		@DisplayName("(Rectangle1afp) #2")
		@Test
	    public void rectangle_2() {
	        assertTrue(getSH().intersects(createBox(2, -2, 1, 1)));
	    }

		@DisplayName("(Rectangle1afp) #3")
		@Test
	    public void rectangle_3() {
	        assertFalse(getSH().intersects(createBox(-5, 0, 1, 1)));
	    }

		@DisplayName("(Rectangle1afp) #4")
		@Test
	    public void rectangle_4() {
	        assertFalse(getSH().intersects(createBox(12, 0, 1, 1)));
	    }

		@DisplayName("(Rectangle1afp) #5")
		@Test
	    public void rectangle_5() {
	        assertFalse(getSH().intersects(createBox(2, -5, 1, 1)));
	    }

		@DisplayName("(Rectangle1afp) #6")
		@Test
	    public void rectangle_6() {
	        assertFalse(getSH().intersects(createBox(2, 12, 1, 1)));
	    }

		@DisplayName("(Rectangle1afp) #7")
		@Test
	    public void rectangle_7() {
	        assertTrue(getSH().intersects(createBox(1, 0, 2, 2)));
	    }

		@DisplayName("(Rectangle1afp) #8")
		@Test
	    public void rectangle_8() {
	        assertTrue(getSH().intersects(createBox(1.235, -3.459, 10.254, 14.963)));
	    }

		@DisplayName("(Rectangle1afp) #9")
		@Test
	    public void rectangle_9() {
	        assertTrue(getSH().intersects(createBox(1.235, 0, 1, 1)));
	    }

		@DisplayName("(Rectangle1afp) #10")
		@Test
	    public void rectangle_10() {
	        assertTrue(getSH().contains(createBox(2, 10.504, 1, 1)));
	    }

		@DisplayName("(Rectangle1afp) #11")
		@Test
	    public void rectangle_11() {
	        assertTrue(getSH().intersects(createBox(1.0, 0, 1, 1)));
	    }

		@DisplayName("(Rectangle1afp) #12")
		@Test
	    public void rectangle_12() {
	        assertTrue(getSH().intersects(createBox(5, 5, 0, 0)));
	    }

	    @DisplayName("(Shape1D) #1")
		@Test
		public void shape_1() {
	        assertFalse(getSH().intersects((Shape1D) createBox(2, -5, 1, 1)));
		}

	    @DisplayName("(Shape1D) #2")
		@Test
		public void shape_2() {
	    	assertFalse(getSH().intersects((Shape1D) createBox(0, 0, 1, 1)));
	    }

	    @DisplayName("(Shape1D) #3")
		@Test
		public void shape_3() {
	        assertTrue(getSH().intersects((Shape1D) createBox(1.235, -3.459, 10.254, 14.963)));
	    }

	}


	@DisplayName("toBoundingBox")
	@Nested
	public class ToBoundingBox {
		
		@DisplayName("#1")
		@Test
		public void test_1() {
			var box = getSH().toBoundingBox();
			assertEpsilonEquals(1.235, getSH().getMinX());
			assertEpsilonEquals(-3.459, getSH().getMinY());
			assertEpsilonEquals(11.489, getSH().getMaxX());
			assertEpsilonEquals(11.504, getSH().getMaxY());
		}

	}

	@DisplayName("translate")
	@Nested
	public class Translate {

		@DisplayName("(double,double) #1")
		@Test
		public void doubledouble_1() {
			getSH().translate(0, 0);
			assertEpsilonEquals(1.235, getSH().getMinX());
			assertEpsilonEquals(-3.459, getSH().getMinY());
			assertEpsilonEquals(11.489, getSH().getMaxX());
			assertEpsilonEquals(11.504, getSH().getMaxY());
		}

		@DisplayName("(double,double) #2")
		@Test
		public void doubledouble_2() {
	        getSH().translate(5.0, 3.0);
	        assertEpsilonEquals(6.235, getSH().getMinX());
	        assertEpsilonEquals(-0.459, getSH().getMinY());
	        assertEpsilonEquals(16.489, getSH().getMaxX());
	        assertEpsilonEquals(14.504, getSH().getMaxY());
	    }

		@DisplayName("(double,double) #3")
		@Test
		public void doubledouble_3() {
	        getSH().translate(-2.0, -4.0);
	        assertEpsilonEquals(-0.765, getSH().getMinX());
	        assertEpsilonEquals(-7.459, getSH().getMinY());
	        assertEpsilonEquals(9.489, getSH().getMaxX());
	        assertEpsilonEquals(7.504, getSH().getMaxY());
	    }

		@DisplayName("(double,double) #4")
		@Test
		public void doubledouble_4() {
	        getSH().translate(3.5, -1.2);
	        assertEpsilonEquals(4.735, getSH().getMinX());
	        assertEpsilonEquals(-4.659, getSH().getMinY());
	        assertEpsilonEquals(14.989, getSH().getMaxX());
	        assertEpsilonEquals(10.304, getSH().getMaxY());
	    }

		@DisplayName("(double,double) #5")
		@Test
		public void doubledouble_5() {
	        getSH().translate(0.123, -0.456);
	        assertEpsilonEquals(1.358, getSH().getMinX());
	        assertEpsilonEquals(-3.915, getSH().getMinY());
	        assertEpsilonEquals(11.612, getSH().getMaxX());
	        assertEpsilonEquals(11.048, getSH().getMaxY());
	    }

		@DisplayName("(double,double) #6")
		@Test
		public void doubledouble_6() {
	        getSH().translate(1e6, 2e6);
	        assertEpsilonEquals(1.235 + 1e6, getSH().getMinX());
	        assertEpsilonEquals(-3.459 + 2e6, getSH().getMinY());
	        assertEpsilonEquals(11.489 + 1e6, getSH().getMaxX());
	        assertEpsilonEquals(11.504 + 2e6, getSH().getMaxY());
	    }

		@DisplayName("(double,double) #7")
		@Test
		public void doubledouble_7() {
	        getSH().translate(-1e6, -2e6);
	        assertEpsilonEquals(1.235 - 1e6, getSH().getMinX());
	        assertEpsilonEquals(-3.459 - 2e6, getSH().getMinY());
	        assertEpsilonEquals(11.489 - 1e6, getSH().getMaxX());
	        assertEpsilonEquals(11.504 - 2e6, getSH().getMaxY());
	    }

		@DisplayName("(double,double) #8")
		@Test
		public void doubledouble_8() {
	        getSH().translate(0.0, 10.0);
	        assertEpsilonEquals(1.235, getSH().getMinX());
	        assertEpsilonEquals(6.541, getSH().getMinY());
	        assertEpsilonEquals(11.489, getSH().getMaxX());
	        assertEpsilonEquals(21.504, getSH().getMaxY());
	    }

		@DisplayName("(double,double) #9")
		@Test
		public void doubledouble_9() {
	        getSH().translate(15.0, 0.0);
	        assertEpsilonEquals(16.235, getSH().getMinX());
	        assertEpsilonEquals(-3.459, getSH().getMinY());
	        assertEpsilonEquals(26.489, getSH().getMaxX());
	        assertEpsilonEquals(11.504, getSH().getMaxY());
	    }

		@DisplayName("(double,double) #10")
		@Test
		public void doubledouble_10() {
	        getSH().translate(1e-12, -1e-12);
	        assertEpsilonEquals(1.235 + 1e-12, getSH().getMinX());
	        assertEpsilonEquals(-3.459 - 1e-12, getSH().getMinY());
	        assertEpsilonEquals(11.489 + 1e-12, getSH().getMaxX());
	        assertEpsilonEquals(11.504 - 1e-12, getSH().getMaxY());
	    }

		@DisplayName("(double,double) #11")
		@Test
		public void doubledouble_11() {
	        getSH().translate(2.0, 3.0);
	        getSH().translate(4.0, -1.0);
	        // Net translation: (6.0, 2.0)
	        assertEpsilonEquals(7.235, getSH().getMinX());
	        assertEpsilonEquals(-1.459, getSH().getMinY());
	        assertEpsilonEquals(17.489, getSH().getMaxX());
	        assertEpsilonEquals(13.504, getSH().getMaxY());
	    }

		@DisplayName("(Vector1D) #1")
		@Test
		public void vector_1() {
			getSH().translate(factory.createVector(getSG(), 0, 0));
			assertEpsilonEquals(1.235, getSH().getMinX());
			assertEpsilonEquals(-3.459, getSH().getMinY());
			assertEpsilonEquals(11.489, getSH().getMaxX());
			assertEpsilonEquals(11.504, getSH().getMaxY());
		}

		@DisplayName("(Vector1D) #2")
		@Test
		public void vector_2() {
	        getSH().translate(factory.createVector(getSG(), 5.0, 3.0));
	        assertEpsilonEquals(6.235, getSH().getMinX());
	        assertEpsilonEquals(-0.459, getSH().getMinY());
	        assertEpsilonEquals(16.489, getSH().getMaxX());
	        assertEpsilonEquals(14.504, getSH().getMaxY());
	    }

		@DisplayName("(Vector1D) #3")
		@Test
		public void vector_3() {
	        getSH().translate(factory.createVector(getSG(), -2.0, -4.0));
	        assertEpsilonEquals(-0.765, getSH().getMinX());
	        assertEpsilonEquals(-7.459, getSH().getMinY());
	        assertEpsilonEquals(9.489, getSH().getMaxX());
	        assertEpsilonEquals(7.504, getSH().getMaxY());
	    }

		@DisplayName("(Vector1D) #4")
		@Test
		public void vector_4() {
	        getSH().translate(factory.createVector(getSG(), 3.5, -1.2));
	        assertEpsilonEquals(4.735, getSH().getMinX());
	        assertEpsilonEquals(-4.659, getSH().getMinY());
	        assertEpsilonEquals(14.989, getSH().getMaxX());
	        assertEpsilonEquals(10.304, getSH().getMaxY());
	    }

		@DisplayName("(Vector1D) #5")
		@Test
		public void vector_5() {
	        getSH().translate(factory.createVector(getSG(), 0.123, -0.456));
	        assertEpsilonEquals(1.358, getSH().getMinX());
	        assertEpsilonEquals(-3.915, getSH().getMinY());
	        assertEpsilonEquals(11.612, getSH().getMaxX());
	        assertEpsilonEquals(11.048, getSH().getMaxY());
	    }

		@DisplayName("(Vector1D) #6")
		@Test
		public void vector_6() {
	        getSH().translate(factory.createVector(getSG(), 1e6, 2e6));
	        assertEpsilonEquals(1.235 + 1e6, getSH().getMinX());
	        assertEpsilonEquals(-3.459 + 2e6, getSH().getMinY());
	        assertEpsilonEquals(11.489 + 1e6, getSH().getMaxX());
	        assertEpsilonEquals(11.504 + 2e6, getSH().getMaxY());
	    }

		@DisplayName("(Vector1D) #7")
		@Test
		public void vector_7() {
	        getSH().translate(factory.createVector(getSG(), -1e6, -2e6));
	        assertEpsilonEquals(1.235 - 1e6, getSH().getMinX());
	        assertEpsilonEquals(-3.459 - 2e6, getSH().getMinY());
	        assertEpsilonEquals(11.489 - 1e6, getSH().getMaxX());
	        assertEpsilonEquals(11.504 - 2e6, getSH().getMaxY());
	    }

		@DisplayName("(Vector1D) #8")
		@Test
		public void vector_8() {
	        getSH().translate(factory.createVector(getSG(), 0.0, 10.0));
	        assertEpsilonEquals(1.235, getSH().getMinX());
	        assertEpsilonEquals(6.541, getSH().getMinY());
	        assertEpsilonEquals(11.489, getSH().getMaxX());
	        assertEpsilonEquals(21.504, getSH().getMaxY());
	    }

		@DisplayName("(Vector1D) #9")
		@Test
		public void vector_9() {
	        getSH().translate(factory.createVector(getSG(), 15.0, 0.0));
	        assertEpsilonEquals(16.235, getSH().getMinX());
	        assertEpsilonEquals(-3.459, getSH().getMinY());
	        assertEpsilonEquals(26.489, getSH().getMaxX());
	        assertEpsilonEquals(11.504, getSH().getMaxY());
	    }

		@DisplayName("(Vector1D) #10")
		@Test
		public void vector_10() {
	        getSH().translate(factory.createVector(getSG(), 1e-12, -1e-12));
	        assertEpsilonEquals(1.235 + 1e-12, getSH().getMinX());
	        assertEpsilonEquals(-3.459 - 1e-12, getSH().getMinY());
	        assertEpsilonEquals(11.489 + 1e-12, getSH().getMaxX());
	        assertEpsilonEquals(11.504 - 1e-12, getSH().getMaxY());
	    }

		@DisplayName("(Vector1D) #11")
		@Test
		public void vector_11() {
	        getSH().translate(factory.createVector(getSG(), 2.0, 3.0));
	        getSH().translate(factory.createVector(getSG(), 4.0, -1.0));
	        // Net translation: (6.0, 2.0)
	        assertEpsilonEquals(7.235, getSH().getMinX());
	        assertEpsilonEquals(-1.459, getSH().getMinY());
	        assertEpsilonEquals(17.489, getSH().getMaxX());
	        assertEpsilonEquals(13.504, getSH().getMaxY());
	    }

	}

}
