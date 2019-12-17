/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

package org.arakhne.afc.io.dbase;

import org.arakhne.afc.vmutil.locale.Locale;

/**
 * Exception throws when the number of columns
 * to write in DBase file is too high.
 *
 * @author $Author: sgalland$
 * @author $Author: olamotte$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class TooManyDBaseColumnException extends DBaseFileException {

	private static final long serialVersionUID = -5669121136539530424L;

	/** Constructor.
	 *
	 * @param currentCount column number.
	 * @param maxCount the max number of columns.
	 */
	public TooManyDBaseColumnException(int currentCount, int maxCount) {
		super(Locale.getString("MESSAGE", //$NON-NLS-1$
				Integer.toString(currentCount), Integer.toString(maxCount)));
    }

}
