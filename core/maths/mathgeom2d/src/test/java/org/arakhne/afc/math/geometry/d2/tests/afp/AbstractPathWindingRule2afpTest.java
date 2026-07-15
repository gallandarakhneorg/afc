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
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.arakhne.afc.math.geometry.base.PathWindingRule;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.base.d2.Point2D;
import org.arakhne.afc.math.geometry.base.d2.Vector2D;
import org.arakhne.afc.math.geometry.base.tests.AbstractMathTestCase;
import org.arakhne.afc.math.geometry.d2.afp.Path2afp;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("all")
public abstract class AbstractPathWindingRule2afpTest<P extends Point2D<? super P, ? super V>,
		V extends Vector2D<? super V, ? super P>,
		B extends Rectangle2afp<?, ?, ?, P, V, B>> extends AbstractMathTestCase {
	
	/** Is the shape to test.
	 */
	protected Path2afp<?, ?, ?, P, V, B> shape;
	
	/** Shape factory.
	 */
	protected TestShapeFactory<P, V, B> factory;

	protected abstract TestShapeFactory<P, V, B> createFactory();

	@BeforeEach
	public void setUp() throws Exception {
		factory = createFactory();
	}
	
	protected void createTestShape(PathWindingRule rule) {
		shape = factory.createPath(rule);
		shape.moveTo(1, -3);
		shape.lineTo(4, -4);
		shape.lineTo(6, -2);
		shape.lineTo(7, 2);
		shape.lineTo(5, 5);
		shape.lineTo(4, 1);
		shape.lineTo(5, 0);
		shape.lineTo(12, -1);
		shape.lineTo(13, -4);
		shape.lineTo(10, -5);
		shape.lineTo(4, -2);
		shape.lineTo(1, 2);
		shape.lineTo(3, 4);
		shape.lineTo(9, 4);
		shape.lineTo(9, -3);
		shape.lineTo(4, -6);
		shape.closePath();
	}
	
	@AfterEach
	public void tearDown() throws Exception {
		shape = null;
		factory = null;
	}

    @DisplayName("contains")
	@Nested
	public class Contains {

		@DisplayName("(double,double) #1")
		@ParameterizedTest
		@EnumSource(PathWindingRule.class)
	    public void test_1(PathWindingRule rule) {
			createTestShape(rule);
			assumeTrue(shape.getWindingRule() == PathWindingRule.EVEN_ODD);
			assertFalse(shape.contains(0, 0));
		}

		@DisplayName("(double,double) #2")
		@ParameterizedTest
		@EnumSource(PathWindingRule.class)
	    public void test_2(PathWindingRule rule) {
			createTestShape(rule);
			assumeTrue(shape.getWindingRule() == PathWindingRule.EVEN_ODD);
			assertFalse(shape.contains(-4, 10));
		}

		@DisplayName("(double,double) #3")
		@ParameterizedTest
		@EnumSource(PathWindingRule.class)
	    public void test_3(PathWindingRule rule) {
			createTestShape(rule);
			assumeTrue(shape.getWindingRule() == PathWindingRule.EVEN_ODD);
			assertFalse(shape.contains(2, -2));
		}

		@DisplayName("(double,double) #4")
		@ParameterizedTest
		@EnumSource(PathWindingRule.class)
	    public void test_4(PathWindingRule rule) {
			createTestShape(rule);
			assumeTrue(shape.getWindingRule() == PathWindingRule.EVEN_ODD);
			assertFalse(shape.contains(10, 0));
		}

		@DisplayName("(double,double) #5")
		@ParameterizedTest
		@EnumSource(PathWindingRule.class)
	    public void test_5(PathWindingRule rule) {
			createTestShape(rule);
			assumeTrue(shape.getWindingRule() == PathWindingRule.EVEN_ODD);
		}

		@DisplayName("(double,double) #6")
		@ParameterizedTest
		@EnumSource(PathWindingRule.class)
	    public void test_6(PathWindingRule rule) {
			createTestShape(rule);
			assumeTrue(shape.getWindingRule() == PathWindingRule.EVEN_ODD);
			assertFalse(shape.contains(8, -5));
	    }
   
		@DisplayName("(double,double) #7")
		@ParameterizedTest
		@EnumSource(PathWindingRule.class)
	    public void test_7(PathWindingRule rule) {
			createTestShape(rule);
			assumeTrue(shape.getWindingRule() == PathWindingRule.NON_ZERO);
	    }
		   
		@DisplayName("(double,double) #8")
		@ParameterizedTest
		@EnumSource(PathWindingRule.class)
	    public void test_8(PathWindingRule rule) {
			createTestShape(rule);
			assumeTrue(shape.getWindingRule() == PathWindingRule.NON_ZERO);
			assertFalse(shape.contains(0, 0));
	    }
		   
		@DisplayName("(double,double) #9")
		@ParameterizedTest
		@EnumSource(PathWindingRule.class)
	    public void test_9(PathWindingRule rule) {
			createTestShape(rule);
			assumeTrue(shape.getWindingRule() == PathWindingRule.NON_ZERO);
			assertFalse(shape.contains(-4, 10));
	    }
		   
		@DisplayName("(double,double) #10")
		@ParameterizedTest
		@EnumSource(PathWindingRule.class)
	    public void test_10(PathWindingRule rule) {
			createTestShape(rule);
			assumeTrue(shape.getWindingRule() == PathWindingRule.NON_ZERO);
	    }
		   
		@DisplayName("(double,double) #11")
		@ParameterizedTest
		@EnumSource(PathWindingRule.class)
	    public void test_11(PathWindingRule rule) {
			createTestShape(rule);
			assumeTrue(shape.getWindingRule() == PathWindingRule.NON_ZERO);
			assertFalse(shape.contains(2, -2));
	    }
		   
		@DisplayName("(double,double) #12")
		@ParameterizedTest
		@EnumSource(PathWindingRule.class)
	    public void test_12(PathWindingRule rule) {
			createTestShape(rule);
			assumeTrue(shape.getWindingRule() == PathWindingRule.NON_ZERO);
			assertFalse(shape.contains(10, 0));
	    }
		   
		@DisplayName("(double,double) #13")
		@ParameterizedTest
		@EnumSource(PathWindingRule.class)
	    public void test_13(PathWindingRule rule) {
			createTestShape(rule);
			assumeTrue(shape.getWindingRule() == PathWindingRule.NON_ZERO);
			assertFalse(shape.contains(8, -5));
	    }
		   
		@DisplayName("(double,double) #14")
		@ParameterizedTest
		@EnumSource(PathWindingRule.class)
	    public void test_14(PathWindingRule rule) {
			createTestShape(rule);
			assumeTrue(shape.getWindingRule() == PathWindingRule.NON_ZERO);
			assertFalse(shape.contains(6, 2));
	    }
		   
		@DisplayName("(double,double) #15")
		@ParameterizedTest
		@EnumSource(PathWindingRule.class)
	    public void test_15(PathWindingRule rule) {
			createTestShape(rule);
			assumeTrue(shape.getWindingRule() == PathWindingRule.NON_ZERO);
			assertFalse(shape.contains(5, 2));
	    }
		   
		@DisplayName("(double,double) #16")
		@ParameterizedTest
		@EnumSource(PathWindingRule.class)
	    public void test_16(PathWindingRule rule) {
			createTestShape(rule);
			assumeTrue(shape.getWindingRule() == PathWindingRule.NON_ZERO);
			assertFalse(shape.contains(6, 2));
	    }
		   
		@DisplayName("(double,double) #17")
		@ParameterizedTest
		@EnumSource(PathWindingRule.class)
	    public void test_17(PathWindingRule rule) {
			createTestShape(rule);
			assumeTrue(shape.getWindingRule() == PathWindingRule.NON_ZERO);
			assertFalse(shape.contains(5, 2));
	    }
		   
		@DisplayName("(double,double) #18")
		@ParameterizedTest
		@EnumSource(PathWindingRule.class)
	    public void test_18(PathWindingRule rule) {
			createTestShape(rule);
			assumeTrue(shape.getWindingRule() == PathWindingRule.NON_ZERO);
			assertTrue(shape.contains(3, 2));
	    }
		   
		@DisplayName("(double,double) #19")
		@ParameterizedTest
		@EnumSource(PathWindingRule.class)
	    public void test_19(PathWindingRule rule) {
			createTestShape(rule);
			assumeTrue(shape.getWindingRule() == PathWindingRule.NON_ZERO);
			assertTrue(shape.contains(5, 4.1));
	    }
		   
		@DisplayName("(double,double) #20")
		@ParameterizedTest
		@EnumSource(PathWindingRule.class)
	    public void test_20(PathWindingRule rule) {
			createTestShape(rule);
			assumeTrue(shape.getWindingRule() == PathWindingRule.NON_ZERO);
			assertTrue(shape.contains(8, 3));
	    }
		   
		@DisplayName("(double,double) #21")
		@ParameterizedTest
		@EnumSource(PathWindingRule.class)
	    public void test_21(PathWindingRule rule) {
			createTestShape(rule);
			assumeTrue(shape.getWindingRule() == PathWindingRule.NON_ZERO);
			assertTrue(shape.contains(11, -3));
	    }
		   
		@DisplayName("(double,double) #22")
		@ParameterizedTest
		@EnumSource(PathWindingRule.class)
	    public void test_22(PathWindingRule rule) {
			createTestShape(rule);
			assumeTrue(shape.getWindingRule() == PathWindingRule.NON_ZERO);
			assertTrue(shape.contains(5, -4));
	    }
		   
		@DisplayName("(double,double) #23")
		@ParameterizedTest
		@EnumSource(PathWindingRule.class)
	    public void test_23(PathWindingRule rule) {
			createTestShape(rule);
			assumeTrue(shape.getWindingRule() == PathWindingRule.NON_ZERO);
			assertTrue(shape.contains(3, 2));
	    }
		   
		@DisplayName("(double,double) #24")
		@ParameterizedTest
		@EnumSource(PathWindingRule.class)
	    public void test_24(PathWindingRule rule) {
			createTestShape(rule);
			assumeTrue(shape.getWindingRule() == PathWindingRule.NON_ZERO);
			assertTrue(shape.contains(5, 4.1));
	    }
		   
		@DisplayName("(double,double) #25")
		@ParameterizedTest
		@EnumSource(PathWindingRule.class)
	    public void test_25(PathWindingRule rule) {
			createTestShape(rule);
			assumeTrue(shape.getWindingRule() == PathWindingRule.NON_ZERO);
			assertTrue(shape.contains(8, 3));
	    }
		   
		@DisplayName("(double,double) #26")
		@ParameterizedTest
		@EnumSource(PathWindingRule.class)
	    public void test_26(PathWindingRule rule) {
			createTestShape(rule);
			assumeTrue(shape.getWindingRule() == PathWindingRule.NON_ZERO);
			assertTrue(shape.contains(11, -3));
	    }
		   
		@DisplayName("(double,double) #27")
		@ParameterizedTest
		@EnumSource(PathWindingRule.class)
	    public void test_27(PathWindingRule rule) {
			createTestShape(rule);
			assumeTrue(shape.getWindingRule() == PathWindingRule.NON_ZERO);
			assertTrue(shape.contains(5, -4));
	    }
		   
		@DisplayName("(double,double) #28")
		@ParameterizedTest
		@EnumSource(PathWindingRule.class)
	    public void test_28(PathWindingRule rule) {
			createTestShape(rule);
			assumeTrue(shape.getWindingRule() == PathWindingRule.NON_ZERO);
			assertFalse(shape.contains(7, -1));
	    }
		   
		@DisplayName("(double,double) #29")
		@ParameterizedTest
		@EnumSource(PathWindingRule.class)
	    public void test_29(PathWindingRule rule) {
			createTestShape(rule);
			assumeTrue(shape.getWindingRule() == PathWindingRule.NON_ZERO);
			assertFalse(shape.contains(8, -2));
	    }
		   
		@DisplayName("(double,double) #30")
		@ParameterizedTest
		@EnumSource(PathWindingRule.class)
	    public void test_30(PathWindingRule rule) {
			createTestShape(rule);
			assumeTrue(shape.getWindingRule() == PathWindingRule.NON_ZERO);
			assertFalse(shape.contains(6, -2.5));
	    }
		   
		@DisplayName("(double,double) #31")
		@ParameterizedTest
		@EnumSource(PathWindingRule.class)
	    public void test_31(PathWindingRule rule) {
			createTestShape(rule);
			assumeTrue(shape.getWindingRule() == PathWindingRule.NON_ZERO);
			assertTrue(shape.contains(7, -1));
	    }
		   
		@DisplayName("(double,double) #32")
		@ParameterizedTest
		@EnumSource(PathWindingRule.class)
	    public void test_32(PathWindingRule rule) {
			createTestShape(rule);
			assumeTrue(shape.getWindingRule() == PathWindingRule.NON_ZERO);
			assertTrue(shape.contains(8, -2));
	    }
		   
		@DisplayName("(double,double) #33")
		@ParameterizedTest
		@EnumSource(PathWindingRule.class)
	    public void test_33(PathWindingRule rule) {
			createTestShape(rule);
			assumeTrue(shape.getWindingRule() == PathWindingRule.NON_ZERO);
			assertTrue(shape.contains(6, -2.5));
	    }

    }

}