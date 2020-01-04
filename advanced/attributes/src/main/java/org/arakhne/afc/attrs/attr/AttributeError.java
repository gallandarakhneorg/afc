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

package org.arakhne.afc.attrs.attr;

/**
 * This exception is generated each time something wrong append
 * with attributes.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class AttributeError extends RuntimeException {

	private static final long serialVersionUID = 3139198792703646207L;

	/** Construct the exception.
	 */
	public AttributeError() {
		//
	}

	/** Construct the exception.
	 * @param name is the name of the attribute on which something wrong appended.
	 */
	public AttributeError(String name) {
		super(name);
	}

	/** Construct the exception.
	 * @param cause is the exception to forward.
	 */
	public AttributeError(Throwable cause) {
		super(cause);
	}

}
