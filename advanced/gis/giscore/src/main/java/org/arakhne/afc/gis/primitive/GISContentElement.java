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

/** GIS Element that known it is contained inside a GISContainer.
 *
 * @param <C> is the type of the container of this object.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public interface GISContentElement<C extends GISContainer<?>> extends GISElement {

	/** Sets the container of this MapElement.
	 *
	 * @param container the new container.
	 * @return the success state of the operation.
	 */
	boolean setContainer(C container);

	/** Replies the object which contains this MapElement.
	 *
	 * @return the container or <code>null</code>
	 */
	@Pure
	C getContainer();

	/** Replies the top-most object which contains this element.
	 *
	 * @return the top container or <code>null</code>
	 */
	@Pure
	Object getTopContainer();

}
