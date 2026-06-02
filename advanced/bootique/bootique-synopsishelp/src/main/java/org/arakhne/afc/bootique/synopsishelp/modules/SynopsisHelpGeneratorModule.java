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

package org.arakhne.afc.bootique.synopsishelp.modules;

import io.bootique.BQCoreModule;
import io.bootique.BQModule;
import io.bootique.ModuleCrate;
import io.bootique.di.Binder;
import io.bootique.di.Injector;
import io.bootique.di.Key;
import io.bootique.di.Provides;
import io.bootique.help.HelpGenerator;
import io.bootique.meta.application.ApplicationMetadata;
import io.bootique.terminal.Terminal;
import jakarta.inject.Singleton;
import org.arakhne.afc.bootique.synopsishelp.annotations.ApplicationArgumentSynopsis;
import org.arakhne.afc.bootique.synopsishelp.annotations.ApplicationDetailedDescription;
import org.arakhne.afc.bootique.synopsishelp.help.SynopsisHelpGenerator;
import org.arakhne.afc.vmutil.locale.Locale;

/** Module for creating the help generator with synopsis.
 *
 * <p>The generator may have a specific parameters:
 * <ul>
 * <li><code>argumentSynopsis</code>: the synopsis of the arguments. If {@code null}, the default description is used.
 *     If it is an empty string, no argument description is displayed.</li>
 * <li><code>detailedDescription</code>: the detailed description of the application.</li>
 * </ul>
 *
 * <p>These parameters could be overridden if you are using an injector.
 * The annotation {@code ApplicationArgumentSynopsis} is used for redefining {@code argumentSynopsis}.
 * The annotation {@code ApplicationDetailedDescription} is used for redefining {@code detailedDescription}.
 * The following is an example of defining in an injector module:
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
public class SynopsisHelpGeneratorModule implements BQModule {

	private static final int TTY_MIN_COLUMNS = 40;

	private static final int TTY_DEFAULT_COLUMNS = 80;

	@Override
    public ModuleCrate crate() {
        return ModuleCrate.of(this)
                .description(Locale.getString("MODULE_DESCRIPTION")) //$NON-NLS-1$
                .overrides(BQCoreModule.class)
                .build();
    }

	@Override
	public void configure(Binder binder) {
		//
	}

	/** Provide the help generator with synopsis.
	 *
	 * @param metadata the application metadata.
	 * @param injector the injector.
	 * @param terminal the terminal description.
	 * @return the help generator.
	 */
	@SuppressWarnings("static-method")
	@Provides
	@Singleton
	public HelpGenerator provideHelpGenerator(
			ApplicationMetadata metadata, Injector injector, Terminal terminal) {
		var maxColumns = terminal.getColumns();
		if (maxColumns < TTY_MIN_COLUMNS) {
			maxColumns = TTY_DEFAULT_COLUMNS;
		}
		String argumentSynopsis;
		try {
			argumentSynopsis = injector.getInstance(Key.get(String.class, ApplicationArgumentSynopsis.class));
		} catch (Exception exception) {
			argumentSynopsis = null;
		}
		String detailedDescription;
		try {
			detailedDescription = injector.getInstance(Key.get(String.class, ApplicationDetailedDescription.class));
		} catch (Exception exception) {
			detailedDescription = null;
		}
		return new SynopsisHelpGenerator(metadata, argumentSynopsis, detailedDescription, maxColumns);
	}

}
