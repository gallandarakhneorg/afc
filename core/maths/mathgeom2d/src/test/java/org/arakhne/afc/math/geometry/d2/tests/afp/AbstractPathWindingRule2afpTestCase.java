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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("all")
public abstract class AbstractPathWindingRule2afpTestCase<P extends Point2D<? super P, ? super V>,
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

		@DisplayName("EVEN_ODD #1")
		@Test
	    public void evenodd_1() {
			createTestShape(PathWindingRule.EVEN_ODD);
			assertFalse(shape.contains(0, 0));
		}

		@DisplayName("NON_ZERO #1")
		@Test
	    public void nonzero_1() {
			createTestShape(PathWindingRule.NON_ZERO);
			assertFalse(shape.contains(0, 0));
		}

		@DisplayName("EVEN_ODD #2")
		@Test
	    public void evenodd_2() {
			createTestShape(PathWindingRule.EVEN_ODD);
			assertFalse(shape.contains(-4, 10));
		}

		@DisplayName("NON_ZERO #2")
		@Test
	    public void nonzero_2() {
			createTestShape(PathWindingRule.NON_ZERO);
			assertFalse(shape.contains(-4, 10));
		}

		@DisplayName("EVEN_ODD #3")
		@Test
	    public void evenodd_3() {
			createTestShape(PathWindingRule.EVEN_ODD);
			assertFalse(shape.contains(2, -2));
		}

		@DisplayName("NON_ZERO #3")
		@Test
	    public void nonzero_3() {
			createTestShape(PathWindingRule.NON_ZERO);
			assertFalse(shape.contains(2, -2));
		}

		@DisplayName("EVEN_ODD #4")
		@Test
	    public void evenodd_4() {
			createTestShape(PathWindingRule.EVEN_ODD);
			assertFalse(shape.contains(10, 0));
		}

		@DisplayName("NON_ZERO #4")
		@Test
	    public void nonzero_4() {
			createTestShape(PathWindingRule.NON_ZERO);
			assertFalse(shape.contains(10, 0));
		}

		@DisplayName("EVEN_ODD #5")
		@Test
	    public void evenodd_5() {
			createTestShape(PathWindingRule.EVEN_ODD);
			assertFalse(shape.contains(8, -5));
		}

		@DisplayName("NON_ZERO #5")
		@Test
	    public void nonzero_5() {
			createTestShape(PathWindingRule.NON_ZERO);
			assertFalse(shape.contains(8, -5));
		}

		@DisplayName("EVEN_ODD #6")
		@Test
	    public void evenodd_6() {
			createTestShape(PathWindingRule.EVEN_ODD);
			assertTrue(shape.contains(2, 2));
		}

		@DisplayName("NON_ZERO #6")
		@Test
	    public void nonzero_6() {
			createTestShape(PathWindingRule.NON_ZERO);
			assertTrue(shape.contains(2, 2));
		}

		@DisplayName("EVEN_ODD #7")
		@Test
	    public void evenodd_7() {
			createTestShape(PathWindingRule.EVEN_ODD);
			assertTrue(shape.contains(8, 2));
		}

		@DisplayName("NON_ZERO #7")
		@Test
	    public void nonzero_7() {
			createTestShape(PathWindingRule.NON_ZERO);
			assertTrue(shape.contains(8, 2));
		}

		@DisplayName("EVEN_ODD #8")
		@Test
	    public void evenodd_8() {
			createTestShape(PathWindingRule.EVEN_ODD);
			assertTrue(shape.contains(10, -2));
		}

		@DisplayName("NON_ZERO #8")
		@Test
	    public void nonzero_8() {
			createTestShape(PathWindingRule.NON_ZERO);
			assertTrue(shape.contains(10, -2));
		}

		@DisplayName("EVEN_ODD #9")
		@Test
	    public void evenodd_9() {
			createTestShape(PathWindingRule.EVEN_ODD);
			assertTrue(shape.contains(6, -4));
		}

		@DisplayName("NON_ZERO #9")
		@Test
	    public void nonzero_9() {
			createTestShape(PathWindingRule.NON_ZERO);
			assertTrue(shape.contains(6, -4));
		}

		@DisplayName("EVEN_ODD #10")
		@Test
	    public void evenodd_10() {
			createTestShape(PathWindingRule.EVEN_ODD);
			assertFalse(shape.contains(5, 2.5));
		}

		@DisplayName("NON_ZERO #10")
		@Test
	    public void nonzero_10() {
			createTestShape(PathWindingRule.NON_ZERO);
			assertFalse(shape.contains(5, 2.5));
		}

		@DisplayName("EVEN_ODD #11")
		@Test
	    public void evenodd_11() {
			createTestShape(PathWindingRule.EVEN_ODD);
			assertFalse(shape.contains(8, -2.5));
		}

		@DisplayName("NON_ZERO #11")
		@Test
	    public void nonzero_11() {
			createTestShape(PathWindingRule.NON_ZERO);
			assertTrue(shape.contains(8, -2.5));
		}

		@DisplayName("EVEN_ODD #12")
		@Test
	    public void evenodd_12() {
			createTestShape(PathWindingRule.EVEN_ODD);
			assertTrue(shape.contains(5, 4.2));
		}

		@DisplayName("NON_ZERO #12")
		@Test
	    public void nonzero_12() {
			createTestShape(PathWindingRule.NON_ZERO);
			assertTrue(shape.contains(5, 4.2));
		}

    }

}