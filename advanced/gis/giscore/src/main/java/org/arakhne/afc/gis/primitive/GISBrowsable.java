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

package org.arakhne.afc.gis.primitive;

import org.eclipse.xtext.xbase.lib.Pure;

/** Describe a GIS element that could be displayed inside a <code>GISDisplayer</code>.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public interface GISBrowsable extends GISPrimitive {

	/** Set the name of the element.
	 *
	 * @param name the new name.
	 */
	void setName(String name);

	/** Set the color of this element.
	 *
	 * @param color the new color.
	 */
	void setColor(int color);

	/** Replies if this element is assumed to be visible inside a displayer.
	 * This flag should not be used to adapt the visibility of the element
	 * inside the browser.
	 *
	 * @return <code>true</code> if this element is visible, otherwise <code>false</code>
	 */
	@Pure
	boolean isVisible();

	/** Set if this element is assumed to be visible inside a displayer.
	 * This flag should not be used to adapt the visibility of the element
	 * inside the browser.
	 *
	 * @param isVisible must be <code>true</code> if this element is visible, otherwise <code>false</code>
	 */
	void setVisible(boolean isVisible);

	/** Set if this element is assumed to be visible inside a displayer.
	 * This flag should not be used to adapt the visibility of the element
	 * inside the browser.
	 *
	 * @param isVisible must be <code>true</code> if this element is visible, otherwise <code>false</code>
	 * @param setChildrenVisibility indicates if the children visibility should be also changed in turn.
	 */
	void setVisible(boolean isVisible, boolean setChildrenVisibility);

	/** Replies the name of the parent.
	 *
	 * @return the name of the parent.
	 */
	@Pure
	String getParentName();

}
