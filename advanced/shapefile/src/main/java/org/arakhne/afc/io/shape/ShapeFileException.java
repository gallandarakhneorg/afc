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

package org.arakhne.afc.io.shape;

import java.io.IOException;

/**
 * Something wrong appends on ESRI Shape file.

 * @author $Author: sgalland$
 * @author $Author: olamotte$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class ShapeFileException extends IOException {

	private static final long serialVersionUID = -4964675492711258793L;

	/** Constructor.
	 */
	public ShapeFileException() {
		//
	}

	/** Constructor.
	 *
	 * @param message the error message.
	 */
	public ShapeFileException(String message) {
		super(message);
	}

	/** Constructor.
	 *
	 * @param cause the cause of the error.
	 */
	public ShapeFileException(Throwable cause) {
		super(cause);
	}

}
