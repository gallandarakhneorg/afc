/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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

package org.arakhne.afc.bootique.log4j;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;

import com.google.inject.Module;
import io.bootique.BQModule;
import io.bootique.BQModuleProvider;

import org.arakhne.afc.bootique.log4j.configs.Log4jIntegrationConfig;
import org.arakhne.afc.bootique.log4j.modules.Log4jIntegrationModule;
import org.arakhne.afc.vmutil.locale.Locale;

/** Provider of a Bootique module for log4j.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
public class Log4jIntegrationModuleProvider implements BQModuleProvider {

	@Override
	public Module module() {
		return new Log4jIntegrationModule();
	}

	@Override
	public Map<String, Type> configs() {
		return Collections.singletonMap(Log4jIntegrationConfig.PREFIX, Log4jIntegrationConfig.class);
	}

	@Override
	public BQModule.Builder moduleBuilder() {
		return BQModule
				.builder(module())
				.overrides(overrides())
				.providerName(name())
				.configs(configs())
				.description(Locale.getString("MODULE_DESCRIPTION")); //$NON-NLS-1$
	}

}
