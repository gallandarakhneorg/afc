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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.regex.Pattern;

import io.bootique.BQCoreModuleExtender;
import io.bootique.di.Binder;
import org.arakhne.afc.bootique.variables.VariableDecls;
import org.arakhne.afc.bootique.variables.VariableNames;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

/**
 * Tests for constants and utilities for variable naming.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class VariableDeclsTest {

	@Test
	@DisplayName("extend")
	public void extend() {
		var binder = mock(Binder.class);
		var decls = VariableDecls.extend(binder);
		assertNotNull(decls);
	}

	@Test
	@DisplayName("declareVar")
	public void declareVar() {
		var extender = mock(BQCoreModuleExtender.class);
		var decls0 = new VariableDecls(extender);

		var decls1 = decls0.declareVar("abcd.efgh.ijkl");

		assertSame(decls0, decls1);
		var configPath = ArgumentCaptor.forClass(String.class);
		var name = ArgumentCaptor.forClass(String.class);
		verify(extender).declareVar(configPath.capture(), name.capture());
		assertEquals("abcd.efgh.ijkl", configPath.getValue());
		assertEquals("ABCD_EFGH_IJKL", name.getValue());
	}

}
