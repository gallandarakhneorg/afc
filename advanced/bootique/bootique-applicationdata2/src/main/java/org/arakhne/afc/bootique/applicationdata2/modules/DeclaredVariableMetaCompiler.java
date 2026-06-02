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

package org.arakhne.afc.bootique.applicationdata2.modules;

import io.bootique.env.DeclaredVariable;
import io.bootique.meta.config.ConfigMetadataNode;
import io.bootique.meta.config.ConfigValueMetadata;
import io.bootique.meta.module.ModulesMetadata;

/** Module for the compiler application metadata version 2.
 *
 * <p>This file is copied from the Bootique's original file and adapted in order to change the visibility of the class.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 18.0
 */
public final class DeclaredVariableMetaCompiler {

	private DeclaredVariableMetaCompiler() {
		//
	}

	/** Compile the given variable.
	 *
	 * @param var the variable to compile.
	 * @param modulesMetadata the metadata for the module
	 * @return the variable metadata.
	 */
	public static ConfigValueMetadata compile(DeclaredVariable var, ModulesMetadata modulesMetadata) {
		for (final var mm : modulesMetadata.getModules()) {
			final var cmn = mm.findConfig(var.getConfigPath());
			if (cmn.isPresent()) {
				return compileMetadata(var, cmn.get());
			}
		}
		return compileUnboundMetadata(var);
	}

	private static ConfigValueMetadata compileUnboundMetadata(DeclaredVariable variable) {
		return ConfigValueMetadata
				.builder(variable.getName())
				.unbound()
				.description(variable.getDescription() != null ? variable.getDescription() : null)
				.build();
	}

	private static ConfigValueMetadata compileMetadata(DeclaredVariable variable, ConfigMetadataNode configMetadata) {
		return ConfigValueMetadata
				.builder(variable.getName())
				.description(variable.getDescription() != null ? variable.getDescription() : configMetadata.getDescription())
				.type(configMetadata.getType())
				.build();
	}

}
