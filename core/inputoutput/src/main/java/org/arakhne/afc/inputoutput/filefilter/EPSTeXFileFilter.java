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

package org.arakhne.afc.inputoutput.filefilter;

import org.arakhne.afc.vmutil.locale.Locale;

/** File filter for a TeX part of
 * a EPS figure combined with TeX macros.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class EPSTeXFileFilter extends AbstractFileFilter {

	/** Historical extension for TeX part of the EPS documents combined with TeX macros.
	 */
	public static final String EXTENSION_PSTEX_T = "pstex_t"; //$NON-NLS-1$

	/** Modern extension for TeX part of the EPS documents combined with TeX macros.
	 */
	public static final String EXTENSION_PS_TEX = "ps_tex"; //$NON-NLS-1$

	/** Construct.
	 */
	public EPSTeXFileFilter() {
		this(true);
	}

	/** Constructor.
	 * @param acceptDirectories is <code>true</code> to
	 *     permit to this file filter to accept directories;
	 *     <code>false</code> if the directories should not
	 *     match.
	 */
	public EPSTeXFileFilter(boolean acceptDirectories) {
		super(
				acceptDirectories,
				Locale.getString(EPSTeXFileFilter.class, "FILE_FILTER_NAME"), //$NON-NLS-1$
				EXTENSION_PSTEX_T, EXTENSION_PS_TEX);
	}

}
