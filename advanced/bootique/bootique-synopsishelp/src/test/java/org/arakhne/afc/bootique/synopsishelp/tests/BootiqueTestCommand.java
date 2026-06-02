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

package org.arakhne.afc.bootique.synopsishelp.tests;

import io.bootique.cli.Cli;
import io.bootique.command.CommandOutcome;
import io.bootique.command.CommandWithMetadata;
import io.bootique.meta.application.CommandMetadata;
import io.bootique.meta.application.OptionMetadata;

/** Command for testing bootique.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
class BootiqueTestCommand extends CommandWithMetadata {

	/** Message used for valid execution.
	 */
	public static final int VALID_EXITCODE = 159;

	/** Message used for valid execution.
	 */
	public static final String VALID_MESSAGE = "this is a valid test"; //$NON-NLS-1$
	
	/** Constructor.
	 */
	public BootiqueTestCommand() {
		super(CommandMetadata.builder("runtest") //$NON-NLS-1$
                .shortName('R')
                .description("Executes the test") //$NON-NLS-1$
                .addOption(OptionMetadata.builder().name("opt").description("fake option").shortName('o').build()) //$NON-NLS-1$ //$NON-NLS-2$
                .build());
	}

    @Override
    public CommandOutcome run(Cli cli) {
        return CommandOutcome.failed(VALID_EXITCODE, VALID_MESSAGE);
    }
}
