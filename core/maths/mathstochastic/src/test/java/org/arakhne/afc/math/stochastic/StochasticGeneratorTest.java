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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Random;

import org.arakhne.afc.testtools.AbstractTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

@DisplayName("StochasticGenerator")
@SuppressWarnings("all")
public class StochasticGeneratorTest extends AbstractTestCase {

	@DisplayName("generateRandomValue")
	@Nested
	public class GenerateRandomValue {

		@DisplayName("#1")
		@Test
		public void test_1() throws Exception {
			var law = mock(StochasticLaw.class);
			StochasticGenerator.generateRandomValue(law);
			var arg = ArgumentCaptor.forClass(Random.class);
			verify(law).inverseF(arg.capture());
			assertNotNull(arg.getValue());
		}
	}

	@DisplayName("noiseValue")
	@Nested
	public class NoiseValue {

		@DisplayName("#1")
		@Test
		public void test_1() throws Exception {
			var law = mock(StochasticLaw.class);
			when(law.f(anyDouble())).thenReturn(753.159);
			var actual = StochasticGenerator.noiseValue(123.456, law);
			assertNotEquals(753.159, actual);
		}
	}

}
