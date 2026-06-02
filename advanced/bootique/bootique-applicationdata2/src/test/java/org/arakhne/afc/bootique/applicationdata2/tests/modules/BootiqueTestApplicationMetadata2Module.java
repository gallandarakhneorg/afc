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

package org.arakhne.afc.bootique.applicationdata2.tests.modules;

import io.bootique.BQModule;
import io.bootique.ModuleCrate;
import io.bootique.di.Binder;
import org.arakhne.afc.bootique.applicationdata2.annotations.ApplicationDescription2;
import org.arakhne.afc.bootique.applicationdata2.annotations.DefaultApplicationName;

/** Module for the command for testing bootique.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
class BootiqueTestApplicationMetadata2Module implements BQModule {

	@Override
    public ModuleCrate crate() {
        return ModuleCrate.of(this)
                .description("The test application metadata 2 module.") //$NON-NLS-1$
                .build();
    }

	@Override
	public void configure(Binder binder) {
		binder.<String>bind(String.class, DefaultApplicationName.class).toInstance("my-application-name"); //$NON-NLS-1$
		binder.<String>bind(String.class, ApplicationDescription2.class).toInstance("A regular description of the application"); //$NON-NLS-1$
	}

}
