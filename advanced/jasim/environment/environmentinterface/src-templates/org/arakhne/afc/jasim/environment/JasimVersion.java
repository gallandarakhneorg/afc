/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

package org.arakhne.afc.jasim.environment;

/**
 * Describes the version of the JaSIM libraries.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
@SuppressWarnings("all")
public final class JasimVersion {

	/** The version number of the current release of the JaSIM libraries.
	 */
	public static final String JASIM_RELEASE_VERSION = "@jasimreleaseversion@"; //$NON-NLS-1$

	/** Flag that indicates if the current JaSIM libraries is a stable release.
	 *
	 * <p>A stable release is a library that will be not more compiled and generated.
	 */
	public static final boolean IS_STABLE = @jasimisstableversion@;

	private JasimVersion() {
		//
	}

}
