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

/** Provide the extension for printing the Bootique configuration.
 *
 * @provides io.bootique.BQModuleProvider
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 17.0
 */
open module org.arakhne.afc.bootique.bootique_printconfig {
	requires org.eclipse.xtext.xbase.lib;
	requires javax.inject;
	requires transitive bootique;
	requires com.google.guice;
	requires org.arakhne.afc.core.vmutils;
	requires com.fasterxml.jackson.core;
	requires com.fasterxml.jackson.databind;
	requires com.fasterxml.jackson.dataformat.xml;
	requires com.fasterxml.jackson.dataformat.yaml;
	requires jopt.simple;

	exports org.arakhne.afc.bootique.printconfig;
	exports org.arakhne.afc.bootique.printconfig.commands;
	exports org.arakhne.afc.bootique.printconfig.configs;
	exports org.arakhne.afc.bootique.printconfig.modules;

	provides io.bootique.BQModuleProvider
		with org.arakhne.afc.bootique.printconfig.PrintConfigModuleProvider;
}