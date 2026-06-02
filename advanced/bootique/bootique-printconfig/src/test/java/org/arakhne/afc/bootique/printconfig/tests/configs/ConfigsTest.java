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

package org.arakhne.afc.bootique.printconfig.tests.configs;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;

import org.arakhne.afc.bootique.printconfig.configs.Configs;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for constants and utilities for configurations.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 18.0
 */
@SuppressWarnings("all")
public class ConfigsTest {

	@Test
	@DisplayName("defineScalar")
	public void defineScalar() throws Exception {
		var map = new HashMap<String, Object>();
		var value = mock(Object.class);
		Configs.defineScalar(map, "abcd.efgh.ijkl", value);
		var a = map.get("abcd");
		assertInstanceOf(Map.class, a);
		var b = ((Map<String, Object>) a).get("efgh");
		assertInstanceOf(Map.class, b);
		assertSame(value, ((Map<String, Object>) b).get("ijkl"));
	}

}
