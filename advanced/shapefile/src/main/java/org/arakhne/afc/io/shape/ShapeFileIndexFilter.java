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

package org.arakhne.afc.io.shape;

import org.arakhne.afc.inputoutput.filefilter.AbstractFileFilter;
import org.arakhne.afc.vmutil.locale.Locale;

/**
 * This class permits to filter the files to show only
 * the SHAPE files. It could be used by a {@code javax.swing.JFileChooser}.
 *
 * @author $Author: sgalland$
 * @author $Author: olamotte$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class ShapeFileIndexFilter extends AbstractFileFilter {

	/** Extension of the SHX files.
	 */
	public static final String EXTENSION_SHX = "shx"; //$NON-NLS-1$

	/** Constructor.
	 */
	public ShapeFileIndexFilter() {
		this(true);
	}

	/** Constructor.
	 * @param acceptDirectories is {@code true} to
	 *     permit to this file filter to accept directories;
	 *     {@code false} if the directories should not
	 *     match.
	 */
	public ShapeFileIndexFilter(boolean acceptDirectories) {
		super(acceptDirectories,
				Locale.getString("DESCRIPTION_SHX"), //$NON-NLS-1$
				EXTENSION_SHX);
	}

}
