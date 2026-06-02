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

import io.bootique.BQModule;
import io.bootique.ModuleCrate;
import io.bootique.config.ConfigurationFactory;
import io.bootique.di.Binder;
import io.bootique.di.Injector;
import io.bootique.di.Provides;
import jakarta.inject.Singleton;

/** Module for the command for testing bootique.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
class BootiqueTestConfigModule implements BQModule {

	@Override
    public ModuleCrate crate() {
        return ModuleCrate.of(this)
                .description("The test command module.") //$NON-NLS-1$
                .config(TestConfig.PREFIX, TestConfig.class)
                .build();
    }

	@Override
	public void configure(Binder binder) {
		//
	}

	/** Replies the instance of the test configuration.
	 *
	 * @param configFactory accessor to the bootique factory.
	 * @param injector the current injector.
	 * @return the configuration accessor.
	 */
	@SuppressWarnings("static-method")
	@Provides
	@Singleton
	public TestConfig getTestConfig(ConfigurationFactory configFactory, Injector injector) {
		final var config = TestConfig.getConfiguration(configFactory);
		injector.injectMembers(config);
		return config;
	}

}
