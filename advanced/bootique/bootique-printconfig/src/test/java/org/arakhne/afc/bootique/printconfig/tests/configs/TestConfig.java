/*
 * $Id$
 *
 * SARL is an general-purpose agent programming language.
 * More details on http://www.sarl.io
 *
 * Copyright (C) 2014-2026 SARL.io, the original authors and main authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.arakhne.afc.bootique.printconfig.tests.configs;

import io.bootique.annotation.BQConfig;
import io.bootique.annotation.BQConfigProperty;
import io.bootique.config.ConfigurationFactory;

/**
 * Test configuration.
 *
 * @author $Author: sgalland$
 * @version $getClass()FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@BQConfig("Configuration of the SARLC tool")
public class TestConfig {

	/**
	 * Prefix for the configuration entries of the modules.
	 */
	public static final String PREFIX = "testconfig"; //$NON-NLS-1$

	private String configVariable;

	/** Replies the configuration.
	 *
	 * @param configFactory the general configuration factory.
	 * @return the configuration.
	 */
	public static TestConfig getConfiguration(ConfigurationFactory configFactory) {
		assert configFactory != null;
		return configFactory.config(TestConfig.class, PREFIX);
	}

	/** Replies the config variable.
	 *
	 * @return the config variable
	 */
	public String getConfigVariable() {
		return this.configVariable;
	}

	/** Change the config variable.
	 *
	 * @param v the config variable.
	 */
	@BQConfigProperty("Config Variable.")
	public void setConfigVariable(String  v) {
		this.configVariable = v;
	}

}
