/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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

package org.arakhne.afc.util;

import org.eclipse.xtext.xbase.lib.Pure;

/** Exception thrown when an object does not support any
 * natural order.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class UnsupportedNaturalOrderException extends RuntimeException {

	private static final long serialVersionUID = 6506767532507115987L;

	private final Object element;

	/** Constructor.
	 * @param element is the element which does not provide a natural order.
	 */
	public UnsupportedNaturalOrderException(Object element) {
		super();
		this.element = element;
	}

	/** Replies the element which does not provide a natural order.
	 *
	 * @return the element which does not provide a natural order.
	 */
	@Pure
	public Object getElement() {
		return this.element;
	}

}
