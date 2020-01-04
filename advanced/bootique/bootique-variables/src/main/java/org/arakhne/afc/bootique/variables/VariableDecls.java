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

package org.arakhne.afc.bootique.variables;

import com.google.inject.Binder;
import io.bootique.BQCoreModuleExtender;

/**
 * Constants and utilities for variable declaration.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
public class VariableDecls {

	private final BQCoreModuleExtender extender;

	/** Constructor.
	 *
	 * @param extender the Bootique extender.
	 */
	protected VariableDecls(BQCoreModuleExtender extender) {
		this.extender = extender;
	}

	/** Create an extended from the given binder.
	 *
	 * @param binder the injection binder.
	 * @return the variable declarator.
	 */
	public static VariableDecls extend(Binder binder) {
		return new VariableDecls(io.bootique.BQCoreModule.extend(binder));
	}

	/** Declare an environment variable which is linked to the given Bootique variable, and has its name defined
	 * from the name of the Bootique variable.
	 *
	 * @param bootiqueVariable the name of the bootique variable.
	 * @return the Bootique extender.
	 */
	public VariableDecls declareVar(String bootiqueVariable) {
		this.extender.declareVar(bootiqueVariable, VariableNames.toEnvironmentVariableName(bootiqueVariable));
		return this;
	}

}
