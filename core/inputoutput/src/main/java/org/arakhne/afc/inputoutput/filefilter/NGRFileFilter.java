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

package org.arakhne.afc.inputoutput.filefilter;

import org.arakhne.afc.vmutil.locale.Locale;

/** File filter for the NetEditor graphs.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 16.0
 */
public class NGRFileFilter extends AbstractFileFilter {

	/** NGR extension.
	 */
	public static final String EXTENSION_NGR = "ngr"; //$NON-NLS-1$

	/** NED extension.
	 */
	private static final String EXTENSION_NED = "ned"; //$NON-NLS-1$

	/** Construct.
	 */
	public NGRFileFilter() {
		this(true);
	}

	/** Constructor.
	 * @param acceptDirectories is {@code true} to
	 *     permit to this file filter to accept directories;
	 *     {@code false} if the directories should not
	 *     match.
	 */
	public NGRFileFilter(boolean acceptDirectories) {
		super(
				acceptDirectories,
				Locale.getString(NGRFileFilter.class, "FILE_FILTER_NAME"), //$NON-NLS-1$
				EXTENSION_NGR, EXTENSION_NED);
	}

}
