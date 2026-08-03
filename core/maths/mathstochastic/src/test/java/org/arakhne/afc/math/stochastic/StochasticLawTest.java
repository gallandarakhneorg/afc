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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.arakhne.afc.testtools.AbstractTestCase;
import org.arakhne.afc.vmutil.json.JsonBuffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("StochasticLaw")
@SuppressWarnings("all")
public class StochasticLawTest extends AbstractTestCase {

	private StochasticLaw law;

	@BeforeEach
	public void setUp() {
		law = new MyStubStochasticLaw();
	}

	@DisplayName("getLawName")
	@Nested
	public class GetLawName {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertEquals("MyStub", law.getLawName());
		}
	}

	@DisplayName("paramDouble")
	@Nested
	public class ParamDouble {

		private Map<String, String> params;

		@BeforeEach
		public void setUp() {
			params = new HashMap<>();
			params.put("p1", "48.4");
		}
		
		@DisplayName("(String,double,Map) #1")
		@Test
		public void stringdoublemap_1() {
			assertEquals(48.4, law.paramDouble("p1", 1.35, params));
		}
		
		@DisplayName("(String,double,Map) #2")
		@Test
		public void stringdoublemap_2() {
			assertEquals(1.35, law.paramDouble("p2", 1.35, params));
		}
		
		@DisplayName("(String,Function,Map) #1")
		@Test
		public void stringfunctionmap_1() {
			assertEquals(48.4, law.paramDouble("p1", () -> 1.35, params));
		}
		
		@DisplayName("(String,Function,Map) #2")
		@Test
		public void stringfunctionmap_2() {
			assertEquals(1.35, law.paramDouble("p2", () -> 1.35, params));
		}
		
		@DisplayName("(String,Map) #1")
		@Test
		public void stringmap_1() throws Exception {
			assertEquals(48.4, law.paramDouble("p1", params));
		}
		
		@DisplayName("(String,Map) #2")
		@Test
		public void stringmap_2() {
			assertThrows(LawParameterNotFoundException.class, () -> law.paramDouble("p2", params));
		}
	}


	@DisplayName("paramBoolean")
	@Nested
	public class ParamBoolean {

		private Map<String, String> params;

		@BeforeEach
		public void setUp() {
			params = new HashMap<>();
			params.put("p1", "true");
		}
		
		@DisplayName("(String,double,Map) #1")
		@Test
		public void stringdoublemap_1() {
			assertTrue(law.paramBoolean("p1", false, params));
		}
		
		@DisplayName("(String,double,Map) #2")
		@Test
		public void stringdoublemap_2() {
			assertTrue(law.paramBoolean("p2", true, params));
		}
		
		@DisplayName("(String,Function,Map) #1")
		@Test
		public void stringfunctionmap_1() {
			assertTrue(law.paramBoolean("p1", () -> false, params));
		}
		
		@DisplayName("(String,Function,Map) #2")
		@Test
		public void stringfunctionmap_2() {
			assertTrue(law.paramBoolean("p2", () -> true, params));
		}
		
		@DisplayName("(String,Map) #1")
		@Test
		public void stringmap_1() throws Exception {
			assertTrue(law.paramBoolean("p1", params));
		}
		
		@DisplayName("(String,Map) #2")
		@Test
		public void stringmap_2() {
			assertThrows(LawParameterNotFoundException.class, () -> law.paramBoolean("p2", params));
		}
	}

	private static class MyStubStochasticLaw extends StochasticLaw {

		@Override
		public double f(double x) throws MathException {
			throw new UnsupportedOperationException();
		}

		@Override
		public MathFunctionRange[] getRange() {
			return new MathFunctionRange[0];
		}

		@Override
		public void toJson(JsonBuffer buffer) {
		}

		@Override
		public double inverseF(double u) throws MathException {
			return u;
		}
		
	}

}
