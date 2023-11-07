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

package org.arakhne.afc.math.matrix;

/** Exception for all the singular matrices.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class SingularMatrixException extends RuntimeException {

	private static final long serialVersionUID = 2834240107372614319L;

	/** Construct the exception.
	 */
	public SingularMatrixException() {
		//
	}

	/** Construct the exception.
	 *
	 * @param message the message.
	 */
	public SingularMatrixException(String message) {
		super(message);
	}

	/** Construct the exception.
	 *
	 * @param cause the cause of the error.
	 */
	public SingularMatrixException(Throwable cause) {
		super(cause);
	}

	/** Construct the exception.
	 *
	 * @param message the message.
	 * @param cause the cause of the error.
	 */
	public SingularMatrixException(String message, Throwable cause) {
		super(message, cause);
	}

}
