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

package org.arakhne.afc.nodefx;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.afp.BoundedElement2afp;
import org.arakhne.afc.util.InformedIterable;

/** Drawer of a document.
 *
 * @param <T> the type of the elements to draw.
 * @param <DT> the type of the document.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
public interface DocumentDrawer<T, DT extends InformedIterable<? super T> & BoundedElement2afp<?>> extends Drawer<DT> {

	/** Replies the element drawer.
	 *
	 * @return the element drawer.
	 */
	@Pure
	Drawer<? super T> getElementDrawer();

	/** Replies the type of the elements inside this container.
	 *
	 * @return the type of the elements inside this container.
	 */
	@Pure
	Class<? extends T> getContainedElementType();

	/** Replies if the given element could be drawn.
	 *
	 * @param gc the current graphics context.
	 * @param mapelement the element to draw.
	 * @return {@code true} to draw the element. {@code false} to hide the element.
	 */
	boolean isDrawable(ZoomableGraphicsContext gc, T mapelement);

}
