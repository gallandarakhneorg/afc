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

package org.arakhne.afc.vmutil;

import static org.arakhne.afc.testtools.XbaseInlineTestUtil.assertInlineParameterUsage;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Array;
import java.lang.reflect.Method;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("ReflectionUtil")
@SuppressWarnings("all")
public class ReflectionUtilTest {

	@DisplayName("matchesParameters")
	@Nested
	public class MatchesParameters {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertTrue(ReflectionUtil.matchesParameters(
					new Class<?>[0],
					new Object[0]));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertFalse(ReflectionUtil.matchesParameters(
					new Class<?>[0],
					new Object[] { 1 }));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertFalse(ReflectionUtil.matchesParameters(
					new Class<?>[] { Double.class },
					new Object[0]));
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			assertFalse(ReflectionUtil.matchesParameters(
					new Class<?>[] { Double.class },
					new Object[] { 'c' }));
		}

		@DisplayName("#5")
		@Test
		public void test_5() {
			assertTrue(ReflectionUtil.matchesParameters(
					new Class<?>[] { Double.class },
					new Object[] { 3. }));
		}

		@DisplayName("#6")
		@Test
		public void test_6() {
			assertFalse(ReflectionUtil.matchesParameters(
					new Class<?>[] { Double.class },
					new Object[] { 4f }));
		}

		@DisplayName("#7")
		@Test
		public void test_7() {
			assertFalse(ReflectionUtil.matchesParameters(
					new Class<?>[] { Double.class },
					new Object[] { 1 }));
		}

		@DisplayName("#8")
		@Test
		public void test_8() {
			assertFalse(ReflectionUtil.matchesParameters(
					new Class<?>[] { Double.class, String.class },
					new Object[0]));
		}

		@DisplayName("#9")
		@Test
		public void test_9() {
			assertFalse(ReflectionUtil.matchesParameters(
					new Class<?>[] { Double.class, String.class },
					new Object[] { 'c' }));
		}

		@DisplayName("#10")
		@Test
		public void test_10() {
			assertFalse(ReflectionUtil.matchesParameters(
					new Class<?>[] { Double.class, String.class },
					new Object[] { 3. }));
		}

		@DisplayName("#11")
		@Test
		public void test_11() {
			assertFalse(ReflectionUtil.matchesParameters(
					new Class<?>[] { Double.class, String.class },
					new Object[] { 4. }));
		}

		@DisplayName("#12")
		@Test
		public void test_12() {
			assertFalse(ReflectionUtil.matchesParameters(
					new Class<?>[] { Double.class, String.class },
					new Object[] { 1 }));
		}

		@DisplayName("#13")
		@Test
		public void test_13() {
			assertFalse(ReflectionUtil.matchesParameters(
					new Class<?>[] { Double.class, String.class },
					new Object[] { 'c', "a" }));  //$NON-NLS-1$
		}

		@DisplayName("#14")
		@Test
		public void test_14() {
			assertTrue(ReflectionUtil.matchesParameters(
					new Class<?>[] { Double.class, String.class },
					new Object[] { 3., "a" }));  //$NON-NLS-1$
		}

		@DisplayName("#15")
		@Test
		public void test_15() {
			assertTrue(ReflectionUtil.matchesParameters(
					new Class<?>[] { Double.class, String.class },
					new Object[] { 4., "a" }));  //$NON-NLS-1$
		}

		@DisplayName("#16")
		@Test
		public void test_16() {
			assertFalse(ReflectionUtil.matchesParameters(
					new Class<?>[] { Double.class, String.class },
					new Object[] { 1, "a" }));  //$NON-NLS-1$
		}

		@DisplayName("#17")
		@Test
		public void test_17() {
			assertFalse(ReflectionUtil.matchesParameters(
					new Class<?>[] { Double.class, String.class },
					new Object[] { 'c', true }));
		}

		@DisplayName("#18")
		@Test
		public void test_18() {
			assertFalse(ReflectionUtil.matchesParameters(
					new Class<?>[] { Double.class, String.class },
					new Object[] { 3., true }));
		}

		@DisplayName("#19")
		@Test
		public void test_19() {
			assertFalse(ReflectionUtil.matchesParameters(
					new Class<?>[] { Double.class, String.class },
					new Object[] { 4., true }));
		}

		@DisplayName("#20")
		@Test
		public void test_20() {
			assertFalse(ReflectionUtil.matchesParameters(
					new Class<?>[] { Double.class, String.class },
					new Object[] { 1, true }));
		}

		@DisplayName("#21")
		@Test
		public void test_21() {
			assertFalse(ReflectionUtil.matchesParameters(
					new Class<?>[] { Double.class, String.class, Array.class },
					new Object[0]));
		}

		@DisplayName("#22")
		@Test
		public void test_22() {
			assertFalse(ReflectionUtil.matchesParameters(
					new Class<?>[] { Double.class, String.class, Array.class },
					new Object[] { 1. }));
		}

		@DisplayName("#23")
		@Test
		public void test_23() {
			assertFalse(ReflectionUtil.matchesParameters(
					new Class<?>[] { Double.class, String.class, Array.class },
					new Object[] { 1., "a" }));  //$NON-NLS-1$
		}

		@DisplayName("#24")
		@Test
		public void test_24() {
			assertFalse(ReflectionUtil.matchesParameters(
					new Class<?>[] { Double.class, String.class, Array.class },
					new Object[] { 1., "a", null }));  //$NON-NLS-1$
		}

		@DisplayName("#25")
		@Test
		public void test_25() {
			assertFalse(ReflectionUtil.matchesParameters(
					new Class<?>[] { Double.class, String.class, Array.class },
					new Object[] { 1., "a", new int[0] }));  //$NON-NLS-1$
		}

		@DisplayName("#26")
		@Test
		public void test_26() {
			assertTrue(ReflectionUtil.matchesParameters(
					new Class<?>[] { Double.class, String.class, int[].class },
					new Object[] { 1., "a", new int[0] }));  //$NON-NLS-1$
		}

		@DisplayName("#27")
		@Test
		public void test_27() {
			assertTrue(ReflectionUtil.matchesParameters(
					new Class<?>[] { Number.class, String.class, int[].class },
					new Object[] { 1., "a", new int[0] }));  //$NON-NLS-1$
		}

		@DisplayName("#28")
		@Test
		public void test_28() {
			assertInlineParameterUsage(ReflectionUtil.class, "matchesParameters", Method.class, Object[].class); //$NON-NLS-1$
		}
	}
	
	@DisplayName("forName")
	@Nested
	public class ForName {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertInlineParameterUsage(ReflectionUtil.class, "forName", String.class, ClassLoader.class); //$NON-NLS-1$
		}
	}
	
	@DisplayName("getPackageClasses")
	@Nested
	public class GetPackageClasses {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertInlineParameterUsage(ReflectionUtil.class, "getPackageClasses", Package.class); //$NON-NLS-1$
		}
	}

}
