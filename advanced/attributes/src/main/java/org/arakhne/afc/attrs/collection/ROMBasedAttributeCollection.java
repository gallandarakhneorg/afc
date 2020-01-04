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

package org.arakhne.afc.attrs.collection;

import java.util.Collection;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.attr.Attribute;

/**
 * This interface representes a provider of attributes that is
 * partly based on data stored on a ROM (read-only memory).
 *
 * <p>The changed values are stored inside the memory and never
 * written back into the ROM.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface ROMBasedAttributeCollection extends AttributeCollection {

	/** Replies the list of attributes that have
	 * changed.
	 *
	 * @return the list of the names of the attributes which are stored inside the memory buffer.
	 */
	@Pure
	Collection<String> getAllBufferedAttributeNames();

	/** Replies the list of attributes that have
	 * changed.
	 *
	 * @return the list of attributes stored inside the memory buffer.
	 */
	@Pure
	Collection<Attribute> getAllBufferedAttributes();

	/** Replies if the specified attribute name corresponds to
	 * a buffered attribute value.
	 *
	 * @param attributeName the name.
	 * @return <code>true</code> if an attribute with the given name
	 *     is stored inside the memory buffer, otherwise <code>false</code>
	 */
	@Pure
	boolean isBufferedAttribute(String attributeName);

	/** Replies the count of buffered attributes.
	 *
	 * @return the count of buffered attributes.
	 */
	@Pure
	int getBufferedAttributeCount();

}
