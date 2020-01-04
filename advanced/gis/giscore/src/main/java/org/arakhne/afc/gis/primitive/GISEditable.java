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

package org.arakhne.afc.gis.primitive;

import org.eclipse.xtext.xbase.lib.Pure;

/** Describe a GIS primitive that could be edited.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public interface GISEditable extends GISPrimitive {

	/** Replies the name of the element.
	 *
	 * @return the name of the element.
	 */
	@Pure
	String getName();

	/** Set the name of the element.
	 *
	 * @param name the new name.
	 */
	void setName(String name);

	/** Replies the color stored inside this element.
	 *
	 * @return the color of this element, or <code>null</code>.
	 */
	@Pure
	Integer getRawColor();

	/**
	 * Replies the color of this element or the color of the container.
	 *
	 * @return the color.
	 * @see #getRawColor()
	 */
	@Pure
	int getColor();

	/**
	 * Set the color of this element.
	 *
	 * @param color the new color
	 */
	void setColor(int color);

	/** Add listener.
	 *
	 * @param listener the listener.
	 */
	void addGISEditableChangeListener(GISEditableChangeListener listener);

	/** Remove listener.
	 *
	 * @param listener the listener.
	 */
	void removeGISEditableChangeListener(GISEditableChangeListener listener);

	/** Replies if this editable object want to be seen as a reed-only object.
	 *
	 * <p>Even if this object replies <code>true</code>, it is possible to change
	 * its attributes. The value replied by this function is just a desire
	 * from this object. It could be used by the GUI to allow edition or not
	 * from a graphical component.
	 *
	 * @return <code>true</code> if this object ant to be assumed as read-only,
	 *     otherwise <code>false</code>
	 */
	@Pure
	boolean isReadOnlyObject();

}
