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

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.io.StringBufferInputStream;
import java.net.URL;

import io.bootique.Bootique;
import io.bootique.command.CommandOutcome;
import io.bootique.log.DefaultBootLogger;
import org.arakhne.afc.vmutil.FileSystem;
import org.arakhne.afc.vmutil.Resources;
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

	private URL testConfig;

	private File configFile;
	
	@BeforeEach
	public void setUp() {
		this.testConfig = Resources.getResource("org/arakhne/afc/bootique/printconfig/tests/configs/testconfig.yml");
		assertNotNull(this.testConfig);
		this.configFile = FileSystem.convertURLToFile(this.testConfig);
		assertNotNull(this.configFile);
	}

	protected CommandOutcome runBootique(ByteArrayOutputStream out, String... args) {
		var bt = Bootique.app(args)
				.autoLoadModules()
				.module(new BootiqueTestConfigModule());
		if (out != null) {
			var output = new PrintStream(out);
			bt = bt.bootLogger(new DefaultBootLogger(false, output, output));
		}
		return bt.exec();
	}

	@Test
	@DisplayName("print in YAML")
	public void printCommand_yaml() {
		var output = new ByteArrayOutputStream();
		var result = runBootique(output, "-c", this.configFile.getAbsolutePath(), "-C");
		assertNotNull(result);
		assertEquals(0, result.getExitCode());
		var outputContent = output.toString();
		assertEquals("testconfig:\n  configVariable: \"a string value\"\n\n", outputContent);
	}

	@Test
	@DisplayName("print in JSON")
	public void printCommand_json() {
		var output = new ByteArrayOutputStream();
		var result = runBootique(output, "-c", this.configFile.getAbsolutePath(), "-C", "--json");
		assertNotNull(result);
		assertEquals(0, result.getExitCode());
		var outputContent = output.toString();
		assertEquals("{\n"
				+ "  \"testconfig\" : {\n"
				+ "    \"configVariable\" : \"a string value\"\n"
				+ "  }\n"
				+ "}\n"
				+ "", outputContent);
	}

	@Test
	@DisplayName("print in XML")
	public void printCommand_xml() {
		var output = new ByteArrayOutputStream();
		var result = runBootique(output, "-c", this.configFile.getAbsolutePath(), "-C", "--xml");
		assertNotNull(result);
		assertEquals(0, result.getExitCode());
		var outputContent = output.toString();
		assertEquals("<configuration>\n"
				+ "  <testconfig>\n"
				+ "    <configVariable>a string value</configVariable>\n"
				+ "  </testconfig>\n"
				+ "</configuration>\n"
				+ "\n"
				+ "", outputContent);
	}

}
