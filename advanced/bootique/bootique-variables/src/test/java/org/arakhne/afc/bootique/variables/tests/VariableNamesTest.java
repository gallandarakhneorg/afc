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

package org.arakhne.afc.bootique.variables.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.arakhne.afc.bootique.variables.VariableNames;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for constants and utilities for variable naming.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class VariableNamesTest {

	@Test
	@DisplayName("toPropertyName(null)")
	public void toPropertyName_null() {
		assertNull(VariableNames.toPropertyName(null));
	}

	@Test
	@DisplayName("toPropertyName(\"\")")
	public void toPropertyName_empty() {
		assertNull(VariableNames.toPropertyName(""));
	}

	@Test
	@DisplayName("toPropertyName(\"abcd\")")
	public void toPropertyName_abcd() {
		assertEquals("bq.abcd", VariableNames.toPropertyName("abcd"));
	}

	@Test
	@DisplayName("toEnvironmentVariableName(null)")
	public void toEnvironmentVariableName_null() {
		assertNull(VariableNames.toEnvironmentVariableName(null));
	}

	@Test
	@DisplayName("toEnvironmentVariableName(\"\")")
	public void toEnvironmentVariableName_empty() {
		assertNull(VariableNames.toEnvironmentVariableName(""));
	}

	@Test
	@DisplayName("toEnvironmentVariableName(\"abcd\")")
	public void toEnvironmentVariableName_abcd() {
		assertEquals("ABCD", VariableNames.toEnvironmentVariableName("abcd"));
	}

	@Test
	@DisplayName("toEnvironmentVariableName(\"ab_cd\")")
	public void toEnvironmentVariableName_ab_cd() {
		assertEquals("AB_CD", VariableNames.toEnvironmentVariableName("ab_cd"));
	}

	@Test
	@DisplayName("basename(null)")
	public void basename_null() {
		assertNull(VariableNames.basename(null));
	}

	@Test
	@DisplayName("basename(\"\")")
	public void basename_empty() {
		assertEquals("", VariableNames.basename(""));
	}

	@Test
	@DisplayName("basename(\"abcd.efgh.ijkl\")")
	public void basename_abcd() {
		assertEquals("ijkl", VariableNames.basename("abcd.efgh.ijkl"));
	}

}
