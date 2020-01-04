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

package org.arakhne.afc.bootique.log4j.modules;

import static io.bootique.BQCoreModule.extend;
import static org.arakhne.afc.bootique.log4j.configs.Log4jIntegrationConfig.LEVEL;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.bootique.config.ConfigurationFactory;
import io.bootique.meta.application.OptionMetadata;
import org.apache.log4j.Logger;
import org.slf4j.bridge.SLF4JBridgeHandler;

import org.arakhne.afc.bootique.log4j.configs.Level;
import org.arakhne.afc.bootique.log4j.configs.Log4jIntegrationConfig;
import org.arakhne.afc.bootique.variables.VariableDecls;
import org.arakhne.afc.vmutil.locale.Locale;

/** Module for log4j.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
public class Log4jIntegrationModule extends AbstractModule {

	private static final String LOG_CLI = "log"; //$NON-NLS-1$

	@Override
	protected void configure() {
		// Binding a dummy class to trigger eager init of Log4j as
		// @Provides below can not be invoked eagerly.
		binder().bind(LogInitTrigger.class).asEagerSingleton();

		VariableDecls.extend(binder()).declareVar(LEVEL);
		extend(binder()).addOption(OptionMetadata.builder(
				LOG_CLI,
				Locale.getString("LOG_OPT", Level.getLabels())) //$NON-NLS-1$
				.valueRequired(Locale.getString("LEVEL")) //$NON-NLS-1$
				.build())
			.mapConfigPath(LOG_CLI, LEVEL);
	}


	/** Replies the instance of the log4j integration configuration..
	 *
	 * @param configFactory accessor to the bootique factory.
	 * @param injector the current injector.
	 * @return the configuration accessor.
	 */
	@SuppressWarnings("static-method")
	@Provides
	@Singleton
	public Log4jIntegrationConfig getLog4jIntegrationConfig(ConfigurationFactory configFactory, Injector injector) {
		final Log4jIntegrationConfig config = Log4jIntegrationConfig.getConfiguration(configFactory);
		injector.injectMembers(config);
		return config;
	}

	/** Provide the root logger.
	 *
	 * @param configFactory the factory of configurations.
	 * @param config the logger configuration.
	 * @return the root logger.
	 */
	@SuppressWarnings("static-method")
	@Singleton
	@Provides
	public Logger provideRootLogger(ConfigurationFactory configFactory, Provider<Log4jIntegrationConfig> config) {
		final Logger root = Logger.getRootLogger();
		// Reroute JUL
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
		final Log4jIntegrationConfig cfg = config.get();
		if (!cfg.getUseLog4jConfig()) {
			cfg.configureLogger(root);
		}
		return root;
	}

	/** Logger init trigger.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 15.0
	 */
	static class LogInitTrigger {

		/** Constructor.
		 *
		 * @param rootLogger the root logger.
		 */
		@Inject
		LogInitTrigger(Logger rootLogger) {
			//
		}
	}

}
