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

package org.arakhne.afc.bootique.synopsishelp.tests;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringBufferInputStream;

import io.bootique.Bootique;
import io.bootique.command.CommandOutcome;
import io.bootique.log.DefaultBootLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Provider of a Bootique module for synopsis help generator.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class BootiqueTestApp {

	@BeforeEach
	public void setUp() {
	}

	protected CommandOutcome runBootique(boolean bindTestCommand, boolean bindModules, ByteArrayOutputStream out, String... args) {
		var bt = Bootique.app(args);
		if (bindModules) {
			bt = bt.autoLoadModules();
			bt = bt.module(new BootiqueTestSynopsisHelpModule());
		}
		if (bindTestCommand) {
			bt = bt.module(new BootiqueTestCommandModule());
		}
		if (out != null) {
			var output = new PrintStream(out);
			bt = bt.bootLogger(new DefaultBootLogger(false, output, output));
		}
		return bt.exec();
	}

	@Test
	@DisplayName("Testing command")
	public void testingCommand() {
		var result = runBootique(true, false, null, "-R");
		assertNotNull(result);
		assertEquals(BootiqueTestCommand.VALID_EXITCODE, result.getExitCode());
		assertEquals(BootiqueTestCommand.VALID_MESSAGE, result.getMessage());
	}

	@Test
	@DisplayName("Exported command")
	public void exportedCommand() {
		var output = new ByteArrayOutputStream();
		var result = runBootique(false, true, output, "--help");
		assertNotNull(result);
		assertEquals(0, result.getExitCode());
		var outputContent = output.toString();
		assertTrue(outputContent.contains("org.eclipse.jdt.internal.junit.runner.RemoteTestRunner [OPTIONS]... additional arguments"));
		assertTrue(outputContent.contains("A detailed description of the application"));
	}

}
