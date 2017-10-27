/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

package org.arakhne.afc.gis.primitive;

import java.util.Comparator;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.collection.AttributeCollection;

/** Element of a GIS application.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public interface GISElement extends GISPrimitive, AttributeCollection {

	/** A name-base comparator of attribute containers.
	 *
	 * <p>The returned comparator use the function
	 * {@link #getName()} to make the comparison.
	 */
	Comparator<GISElement> NAME_COMPARATOR = (GISElement o1, GISElement o2) -> {
		if (o1 == o2) {
			return 0;
		}
		if (o1 == null) {
			return -1;
		}
		if (o2 == null) {
			return 1;
		}
		final String n1 = o1.getName();
		final String n2 = o2.getName();
		if (n1 == n2) {
			return 0;
		}
		if (n1 == null) {
			return -1;
		}
		if (n2 == null) {
			return 1;
		}
		return n1.compareTo(n2);
	};

	/** Attribute: name of the element.
	 */
	String ATTR_NAME = "name"; //$NON-NLS-1$

	@Override
	@Pure
	GISElement clone();

	/** Copy all the attributes of the specified container.
	 *
	 * @param container is the object from which the attributes must be extracted.
	 * @return count of copied attributes
	 */
	int copyAttributes(GISElement container);

	/** Replies a string suitable for hashtables.
	 *
	 * @return a string suitable for hashtables.
	 */
	@Pure
	String hashKey();

	/** Replies the name of the element.
	 *
	 * @return the name
	 */
	@Pure
	String getName();

	/** Set the name of the element.
	 *
	 * @param name the new name.
	 */
	void setName(String name);

}
