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

package org.arakhne.afc.inputoutput.xml;

import org.w3c.dom.Element;

/**
 * Matches the attribute "name" with the given value.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class XMLNameConstraint implements XMLConstraint {

	private final String value;

	/** Constructor.
	 * @param value is the value to match.
	 */
	public XMLNameConstraint(String value) {
		this.value = value;
	}

	@Override
	public boolean isValidElement(Element element) {
		final String v = XMLUtil.getAttributeValueWithDefault(element, null, XMLUtil.ATTR_NAME);
		return v == this.value || (v != null && v.equals(this.value));
	}

}

