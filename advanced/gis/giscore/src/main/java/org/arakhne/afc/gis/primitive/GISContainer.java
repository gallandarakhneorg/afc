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

import org.arakhne.afc.math.geometry.d2.afp.BoundedElement2afp;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.util.InformedIterable;

/** Container of GIS elements.
 *
 * @param <T> is the type of the containing layers.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public interface GISContainer<T extends GISElement> extends InformedIterable<T>, BoundedElement2afp<Rectangle2d> {

	/** Replies the name of the container.
	 *
	 * @return the name of the container.
	 */
	@Pure
	String getName();

	/** Clear the current bounding box to force the computation of it at
	 * the next call to {@link #getBoundingBox()}.
	 */
	void resetBoundingBox();

	/** Replies the count of map elements inside this container.
	 *
	 * @return the count of map elements inside this container.
	 */
	@Pure
	int size();

	/** Replies the color of this container that could be used
	 * as default color by the inside elements.
	 *
	 * @return the container color.
	 */
	@Pure
	int getColor();

	/** Bind the given listener to this container.
	 *
	 * @param listener the listener.
	 * @since 16.0
	 */
	void bindChangeListener(ChangeListener listener);

}
