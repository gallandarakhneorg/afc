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

package org.arakhne.afc.inputoutput.xml;

import org.w3c.dom.Element;

/**
 * Matches the attribute "id" with the given value.
 *
 * @param <E> is the type of the attribute value to test.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public abstract class XMLValueConstraint<E> implements XMLConstraint {

	private final String name;

	private final E value;

	/** Constructor.
	 * @param name is the name of the attribute to test with the given value.
	 * @param value is the value to match.
	 */
	public XMLValueConstraint(String name, E value) {
		this.name = name;
		this.value = value;
	}

	/** Invoked to convert the string into the value to test.
	 *
	 * @param stringValue the value to convert.
	 * @return the value to test.
	 */
	protected abstract E convertValue(String stringValue);

	@Override
	public boolean isValidElement(Element element) {
		final String sv = XMLUtil.getAttributeValueWithDefault(element, null, this.name);
		final E v = (sv == null) ? null : convertValue(sv);
		return v == this.value || (v != null && v.equals(this.value));
	}

}
