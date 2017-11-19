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

package org.arakhne.afc.gis.ui.drawers;

import java.util.Iterator;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.mapelement.GISElementContainer;
import org.arakhne.afc.gis.mapelement.MapElement;
import org.arakhne.afc.gis.ui.GisDrawer;
import org.arakhne.afc.gis.ui.GisGraphicsContext;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** Drawer of a map element container.
 *
 * @param <T> the type of the elements to draw.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
public class GisContainerDrawer<T extends MapElement> implements GisDrawer<GISElementContainer<T>> {

	private final GisDrawer<? super T> drawer;

	/** Constructor based on a {@link StaticMapElementDrawer} drawer.
	 */
	public GisContainerDrawer() {
		this(new StaticMapElementDrawer());
	}

	/** Constructor based on the given drawer.
	 *
	 * @param drawer the element drawer.
	 */
	public GisContainerDrawer(GisDrawer<? super T> drawer) {
		assert drawer != null : AssertMessages.notNullParameter();
		this.drawer = drawer;
	}

	/** Replies the element drawer.
	 *
	 * @return the element drawer.
	 */
	public GisDrawer<? super T> getElementDrawer() {
		return this.drawer;
	}

	@Override
	@SuppressWarnings("checkstyle:magicnumber")
	public void draw(GisGraphicsContext gc, GISElementContainer<T> element) {
		final Iterator<T> iterator = element.iterator(gc.getVisibleArea());
		final GisDrawer<? super T> drawer = getElementDrawer();
		while (iterator.hasNext()) {
			final T mapelement = iterator.next();
			if (isDrawable(gc, mapelement)) {
				drawer.draw(gc, mapelement);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	@Pure
	public Class<? extends GISElementContainer<T>> getElementType() {
		return (Class<? extends GISElementContainer<T>>) GISElementContainer.class;
	}

	/** Replies the type of the elements inside this container.
	 *
	 * @return the type of the elements inside this container.
	 */
	@SuppressWarnings("unchecked")
	@Pure
	public Class<? extends MapElement> getContainedElementType() {
		return (Class<? extends MapElement>) getElementDrawer().getElementType();
	}

	/** Replies if the given element could be drawn.
	 *
	 * @param gc the current graphics context.
	 * @param mapelement the element to draw.
	 * @return {@code true} to draw the element. {@code false} to hide the element.
	 */
	@SuppressWarnings("checkstyle:magicnumber")
	public boolean isDrawable(GisGraphicsContext gc, T mapelement) {
		return gc.consumeBudget();
	}

}
