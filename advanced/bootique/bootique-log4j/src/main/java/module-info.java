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

/** Provide the bootique extension for log4j.
 *
 * @provides io.bootique.BQModuleProvider
 * @provides io.bootique.config.PolymorphicConfiguration
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 17.0
 */
open module org.arakhne.afc.bootique.bootique_log4j {
	requires org.eclipse.xtext.xbase.lib;
	requires transitive bootique;
	requires transitive bootique.di;
	requires javax.inject;
	requires transitive log4j;
	requires org.slf4j;
	requires jul.to.slf4j;
	requires com.fasterxml.jackson.annotation;
	requires com.google.common;
	requires transitive java.logging;
	requires org.arakhne.afc.core.vmutils;
	requires org.arakhne.afc.slf4j.slf4j_log4j;
	requires org.arakhne.afc.bootique.bootique_variables;

	exports org.arakhne.afc.bootique.log4j;
	exports org.arakhne.afc.bootique.log4j.configs;
	exports org.arakhne.afc.bootique.log4j.modules;

	provides io.bootique.BQModuleProvider
		with org.arakhne.afc.bootique.log4j.Log4jIntegrationModuleProvider;
	provides io.bootique.config.PolymorphicConfiguration
		with org.arakhne.afc.bootique.log4j.configs.ConsoleAppenderConfig,
		org.arakhne.afc.bootique.log4j.configs.FileAppenderConfig;

}