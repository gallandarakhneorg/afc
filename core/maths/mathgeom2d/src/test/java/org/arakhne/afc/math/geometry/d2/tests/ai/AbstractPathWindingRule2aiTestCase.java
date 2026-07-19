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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.arakhne.afc.math.geometry.base.PathWindingRule;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.base.d2.Point2D;
import org.arakhne.afc.math.geometry.base.d2.Vector2D;
import org.arakhne.afc.math.geometry.base.tests.AbstractMathTestCase;
import org.arakhne.afc.math.geometry.d2.ai.Path2ai;
import org.arakhne.afc.math.geometry.d2.ai.Rectangle2ai;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("all")
public abstract class AbstractPathWindingRule2aiTestCase<P extends Point2D<? super P, ? super V>,
V extends Vector2D<? super V, ? super P>,
B extends Rectangle2ai<?, ?, ?, P, V, B>> extends AbstractMathTestCase {

	/** Is the shape to test.
	 */
	protected Path2ai<?, ?, ?, P, V, B> shape;

	/** Shape factory.
	 */
	protected TestShapeFactory<P, V, B> factory;

	protected abstract TestShapeFactory<P, V, B> createFactory();

	/** Replies the tested shape.
	 *
	 * @return the shape.
	 */
	protected Path2ai<?, ?, ?, P, V, B> getS() {
		return this.shape;
	};

	@BeforeEach
	@ParameterizedTest
	@EnumSource(PathWindingRule.class)
	public void setUp(PathWindingRule rule) throws Exception {
		this.factory = createFactory();
		this.shape = this.factory.createPath(rule);
		getS().moveTo(1, -3);
		getS().lineTo(4, -4);
		getS().lineTo(6, -2);
		getS().lineTo(7, 2);
		getS().lineTo(5, 5);
		getS().lineTo(4, 1);
		getS().lineTo(5, 0);
		getS().lineTo(12, -1);
		getS().lineTo(13, -4);
		getS().lineTo(10, -5);
		getS().lineTo(4, -2);
		getS().lineTo(1, 2);
		getS().lineTo(3, 4);
		getS().lineTo(9, 4);
		getS().lineTo(9, -3);
		getS().lineTo(4, -6);
		getS().closePath();
	}

	@AfterEach
	public void tearDown() throws Exception {
		this.shape = null;
		this.factory = null;
	}

	@DisplayName("contains")
	@Nested
	public class Contains {

		@DisplayName("(double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_1(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(getS().getWindingRule() == PathWindingRule.EVEN_ODD);
			assertFalse(getS().contains(0, 0));
		}

		@DisplayName("(double,double) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_2(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(getS().getWindingRule() == PathWindingRule.EVEN_ODD);
			assertFalse(getS().contains(-4, 10));
		}

		@DisplayName("(double,double) #3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_3(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(getS().getWindingRule() == PathWindingRule.EVEN_ODD);
			assertFalse(getS().contains(2, -2));
		}

		@DisplayName("(double,double) #4")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_4(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(getS().getWindingRule() == PathWindingRule.EVEN_ODD);
			assertFalse(getS().contains(10, 0));
		}

		@DisplayName("(double,double) #5")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_5(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(getS().getWindingRule() == PathWindingRule.EVEN_ODD);
			assertFalse(getS().contains(8, -5));
		}

		@DisplayName("(double,double) #6")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_6(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(getS().getWindingRule() == PathWindingRule.NON_ZERO);
			assertFalse(getS().contains(0, 0));
		}

		@DisplayName("(double,double) #7")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_7(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(getS().getWindingRule() == PathWindingRule.NON_ZERO);
			assertFalse(getS().contains(-4, 10));
		}

		@DisplayName("(double,double) #8")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_8(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(getS().getWindingRule() == PathWindingRule.NON_ZERO);
			assertFalse(getS().contains(2, -2));
		}

		@DisplayName("(double,double) #9")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_9(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(getS().getWindingRule() == PathWindingRule.NON_ZERO);
			assertFalse(getS().contains(10, 0));
		}

		@DisplayName("(double,double) #10")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_10(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(getS().getWindingRule() == PathWindingRule.NON_ZERO);
			assertFalse(getS().contains(8, -5));
		}

		@DisplayName("(double,double) #11")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_11(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(getS().getWindingRule() == PathWindingRule.EVEN_ODD);
			assertFalse(getS().contains(6, 2));
		}

		@DisplayName("(double,double) #12")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_12(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(getS().getWindingRule() == PathWindingRule.EVEN_ODD);
			assertFalse(getS().contains(5, 2));
		}

		@DisplayName("(double,double) #13")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_13(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(getS().getWindingRule() == PathWindingRule.NON_ZERO);
			assertFalse(getS().contains(6, 2));
		}

		@DisplayName("(double,double) #14")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_14(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(getS().getWindingRule() == PathWindingRule.NON_ZERO);
			assertFalse(getS().contains(5, 2));
		}

		@DisplayName("(double,double) #15")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_15(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(getS().getWindingRule() == PathWindingRule.EVEN_ODD);
			assertTrue(getS().contains(3, 2));
		}

		@DisplayName("(double,double) #16")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_16(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(getS().getWindingRule() == PathWindingRule.EVEN_ODD);
			assertTrue(getS().contains(5, 4));
		}

		@DisplayName("(double,double) #17")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_17(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(getS().getWindingRule() == PathWindingRule.EVEN_ODD);
			assertTrue(getS().contains(8, 3));
		}

		@DisplayName("(double,double) #18")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_18(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(getS().getWindingRule() == PathWindingRule.EVEN_ODD);
			assertTrue(getS().contains(11, -3));
		}

		@DisplayName("(double,double) #19")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_19(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(getS().getWindingRule() == PathWindingRule.EVEN_ODD);
			assertTrue(getS().contains(5, -4));
		}

		@DisplayName("(double,double) #20")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_20(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(getS().getWindingRule() == PathWindingRule.NON_ZERO);
			assertTrue(getS().contains(3, 2));
		}

		@DisplayName("(double,double) #21")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_21(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(getS().getWindingRule() == PathWindingRule.NON_ZERO);
			assertTrue(getS().contains(5, 4));
		}

		@DisplayName("(double,double) #22")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_22(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(getS().getWindingRule() == PathWindingRule.NON_ZERO);
			assertTrue(getS().contains(8, 3));
		}

		@DisplayName("(double,double) #23")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_23(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(getS().getWindingRule() == PathWindingRule.NON_ZERO);
			assertTrue(getS().contains(11, -3));
		}

		@DisplayName("(double,double) #24")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_24(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(getS().getWindingRule() == PathWindingRule.NON_ZERO);
			assertTrue(getS().contains(5, -4));
		}

		@DisplayName("(double,double) #25")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_25(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(getS().getWindingRule() == PathWindingRule.EVEN_ODD);
			assertFalse(getS().contains(7, -1));
		}

		@DisplayName("(double,double) #26")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_26(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(getS().getWindingRule() == PathWindingRule.EVEN_ODD);
			assertFalse(getS().contains(8, -2));
		}

		@DisplayName("(double,double) #27")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_27(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(getS().getWindingRule() == PathWindingRule.EVEN_ODD);
			assertFalse(getS().contains(6, -2));
		}

		@DisplayName("(double,double) #28")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_28(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(getS().getWindingRule() == PathWindingRule.NON_ZERO);
			assertTrue(getS().contains(7, -1));
		}

		@DisplayName("(double,double) #29")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_29(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(getS().getWindingRule() == PathWindingRule.NON_ZERO);
			assertTrue(getS().contains(8, -2));
		}

		@DisplayName("(double,double) #30")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem2D.class)
		public void point_30(CoordinateSystem2D cs) {
			CoordinateSystem2D.setDefaultCoordinateSystem(cs);
			assumeTrue(getS().getWindingRule() == PathWindingRule.NON_ZERO);
			assertTrue(getS().contains(6, -2));
		}

	}

}