/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

/** Provide the bootique extension for creating a user-friendly synopsis.
 *
 * @provides io.bootique.BQModuleProvider
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 17.0
 */
open module org.arakhne.afc.bootique.bootique_synopsishelp {
	requires transitive bootique;
	requires transitive bootique.di;
	requires org.arakhne.afc.core.vmutils;
	requires javax.inject;

	exports org.arakhne.afc.bootique.synopsishelp;
	exports org.arakhne.afc.bootique.synopsishelp.annotations;
	exports org.arakhne.afc.bootique.synopsishelp.help;
	exports org.arakhne.afc.bootique.synopsishelp.modules;

	provides io.bootique.BQModuleProvider
		with org.arakhne.afc.bootique.synopsishelp.SynopsisHelpModuleProvider;
}