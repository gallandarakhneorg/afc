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

package org.arakhne.maven.plugins.tagreplacer;

/**
 * List of the macros strings.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public final class Macros {

	/** &#36;ArtifactId&#36;.
	 */
	public static final String MACRO_ARTIFACTID = "ArtifactId"; //$NON-NLS-1$

	/** &#36;Author: id&#36;.
	 */
	public static final String MACRO_AUTHOR = "Author"; //$NON-NLS-1$

	/** &#36;Date&#36;.
	 */
	public static final String MACRO_DATE = "Date"; //$NON-NLS-1$

	/** &#36;Filename&#36;.
	 */
	public static final String MACRO_FILENAME = "Filename"; //$NON-NLS-1$

	/** &#36;FullVersion&#36;.
	 */
	public static final String MACRO_FULLVERSION = "FullVersion"; //$NON-NLS-1$

	/** &#36;GroupId&#36;.
	 */
	public static final String MACRO_GROUPID = "GroupId"; //$NON-NLS-1$

	/** &#36;Id&#36;.
	 */
	public static final String MACRO_ID = "Id"; //$NON-NLS-1$

	/** &#36;Name&#36;.
	 */
	public static final String MACRO_NAME = "Name"; //$NON-NLS-1$

	/** &#36;Organization&#36;.
	 */
	public static final String MACRO_ORGANIZATION = "Organization"; //$NON-NLS-1$

	/** &#36;Revision&#36;.
	 */
	public static final String MACRO_REVISION = "Revision"; //$NON-NLS-1$

	/** &#36;Version&#36;.
	 */
	public static final String MACRO_VERSION = "Version"; //$NON-NLS-1$

	/** &#36;Website&#36;.
	 */
	public static final String MACRO_WEBSITE = "Website"; //$NON-NLS-1$

	/** &#36;Year&#36;.
	 *
	 * @since 2.3
	 */
	public static final String MACRO_YEAR = "Year"; //$NON-NLS-1$

	/** &#36;InceptionYear&#36;.
	 *
	 * @since 2.3
	 */
	public static final String MACRO_INCEPTIONYEAR = "InceptionYear"; //$NON-NLS-1$

	/** &#36;Prop: name&#36;.
	 *
	 * @since 2.3
	 */
	public static final String MACRO_PROP = "Prop"; //$NON-NLS-1$

	private Macros() {
		//
	}

}
