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

package org.arakhne.afc.math.stochastic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Stream;

import org.arakhne.afc.testtools.AbstractTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("MathFunctionRange")
@SuppressWarnings("all")
public class MathFunctionRangeTest extends AbstractTestCase {

	protected static final Random RANDOM = new Random();
	
	protected static Stream<Arguments> providesArguments() throws Exception {
		final var arguments = new ArrayList<Arguments>();
        int count = RANDOM.nextInt(50) + 50;
		for (int i = 0; i < count; ++i) {
        	var value0 = RANDOM.nextDouble(100);
        	var value1 = RANDOM.nextDouble(100);
        	if (value0 <= value1) {
        		arguments.add(Arguments.of(value0, value1));
        	} else {
        		arguments.add(Arguments.of(value1, value0));
        	}
		}
		return arguments.stream();
	}
	
	@DisplayName("createDiscreteSet")
	@Nested
	public class CreateDiscreteSet {

		private MathFunctionRange[] ranges;

		@BeforeEach
		public void setUp() {
			ranges = MathFunctionRange.createDiscreteSet(1, 45, 3, 7.25, 4, 6, 5, 9, 10);
		}

		@DisplayName("#1")
		@Test
		public void test_1() {
			var range = ranges[0];
			assertNotNull(range);
			assertEquals(1., range.getMin());
			assertEquals(1., range.getMax());
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			var range = ranges[1];
			assertNotNull(range);
			assertEquals(45., range.getMin());
			assertEquals(45., range.getMax());
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			var range = ranges[2];
			assertNotNull(range);
			assertEquals(3., range.getMin());
			assertEquals(3., range.getMax());
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			var range = ranges[3];
			assertNotNull(range);
			assertEquals(7.25, range.getMin());
			assertEquals(7.25, range.getMax());
		}

		@DisplayName("#5")
		@Test
		public void test_5() {
			var range = ranges[4];
			assertNotNull(range);
			assertEquals(4., range.getMin());
			assertEquals(4., range.getMax());
		}

		@DisplayName("#6")
		@Test
		public void test_6() {
			var range = ranges[5];
			assertNotNull(range);
			assertEquals(6., range.getMin());
			assertEquals(6., range.getMax());
		}

		@DisplayName("#7")
		@Test
		public void test_7() {
			var range = ranges[6];
			assertNotNull(range);
			assertEquals(5., range.getMin());
			assertEquals(5., range.getMax());
		}

		@DisplayName("#8")
		@Test
		public void test_8() {
			var range = ranges[7];
			assertNotNull(range);
			assertEquals(9., range.getMin());
			assertEquals(9., range.getMax());
		}

		@DisplayName("#9")
		@Test
		public void test_9() {
			var range = ranges[8];
			assertNotNull(range);
			assertEquals(10., range.getMin());
			assertEquals(10., range.getMax());
		}
	}

	@DisplayName("createSet")
	@Nested
	public class CreateSet {

		private MathFunctionRange[] ranges;

		@BeforeEach
		public void setUp() {
			ranges = MathFunctionRange.createSet(1, 45, 3, 7.25, 4, 6, 5, 9, 10);
		}

		@DisplayName("#1")
		@Test
		public void test_1() {
			var range = ranges[0];
			assertNotNull(range);
			assertEquals(1., range.getMin());
			assertEquals(45., range.getMax());
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			var range = ranges[1];
			assertNotNull(range);
			assertEquals(3., range.getMin());
			assertEquals(7.25, range.getMax());
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			var range = ranges[2];
			assertNotNull(range);
			assertEquals(4., range.getMin());
			assertEquals(6., range.getMax());
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			var range = ranges[3];
			assertNotNull(range);
			assertEquals(5., range.getMin());
			assertEquals(9., range.getMax());
		}

		@DisplayName("#5")
		@Test
		public void test_5() {
			assertEquals(4, ranges.length);
		}
	}

	@DisplayName("createInfinitySet")
	@Nested
	public class CreateInfinitySet {

		@DisplayName("#1")
		@Test
		public void test_1() {
			var ranges = MathFunctionRange.createInfinitySet();
			assertNotNull(ranges);
			assertEquals(1, ranges.length);
			var range = ranges[0];
			assertEquals(Double.NEGATIVE_INFINITY, range.getMin());
			assertEquals(Double.POSITIVE_INFINITY, range.getMax());
		}
	}

	@DisplayName("getMin")
	@Nested
	public class GetMin {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.stochastic.MathFunctionRangeTest#providesArguments")
		public void test_1(Double v1, Double v2) {
			var range = new MathFunctionRange(v1, v2);
			assertEquals(v1, range.getMin());
		}
	}

	@DisplayName("getMax")
	@Nested
	public class GetMax {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.stochastic.MathFunctionRangeTest#providesArguments")
		public void test_1(Double v1, Double v2) {
			var range = new MathFunctionRange(v1, v2);
			assertEquals(v2, range.getMax());
		}
	}

	@DisplayName("isMinValueIncluded")
	@Nested
	public class IsMinValueIncluded {

		@DisplayName("(true, true)")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.stochastic.MathFunctionRangeTest#providesArguments")
		public void truetrue_1(Double v1, Double v2) {
			var range = new MathFunctionRange(v1, true, v2, true);
			assertTrue(range.isMinValueIncluded());
		}

		@DisplayName("(true, false)")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.stochastic.MathFunctionRangeTest#providesArguments")
		public void truefalse_1(Double v1, Double v2) {
			var range = new MathFunctionRange(v1, true, v2, false);
			assertTrue(range.isMinValueIncluded());
		}

		@DisplayName("(false, false)")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.stochastic.MathFunctionRangeTest#providesArguments")
		public void falsefalse_1(Double v1, Double v2) {
			var range = new MathFunctionRange(v1, false, v2, false);
			assertFalse(range.isMinValueIncluded());
		}

		@DisplayName("(false, true)")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.stochastic.MathFunctionRangeTest#providesArguments")
		public void falsetrue_1(Double v1, Double v2) {
			var range = new MathFunctionRange(v1, false, v2, true);
			assertFalse(range.isMinValueIncluded());
		}
	}

	@DisplayName("isMaxValueIncluded")
	@Nested
	public class IsMaxValueIncluded {

		@DisplayName("(true, true)")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.stochastic.MathFunctionRangeTest#providesArguments")
		public void truetrue_1(Double v1, Double v2) {
			var range = new MathFunctionRange(v1, true, v2, true);
			assertTrue(range.isMaxValueIncluded());
		}

		@DisplayName("(true, false)")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.stochastic.MathFunctionRangeTest#providesArguments")
		public void truefalse_1(Double v1, Double v2) {
			var range = new MathFunctionRange(v1, true, v2, false);
			assertFalse(range.isMaxValueIncluded());
		}

		@DisplayName("(false, false)")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.stochastic.MathFunctionRangeTest#providesArguments")
		public void falsefalse_1(Double v1, Double v2) {
			var range = new MathFunctionRange(v1, false, v2, false);
			assertFalse(range.isMaxValueIncluded());
		}

		@DisplayName("(false, true)")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.math.stochastic.MathFunctionRangeTest#providesArguments")
		public void falsetrue_1(Double v1, Double v2) {
			var range = new MathFunctionRange(v1, false, v2, true);
			assertTrue(range.isMaxValueIncluded());
		}
	}

}
