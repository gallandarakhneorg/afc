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

import org.arakhne.afc.gis.maplayer.GISLayerContainer;
import org.arakhne.afc.gis.maplayer.MapLayer;
import org.arakhne.afc.nodefx.DocumentDrawer;
import org.arakhne.afc.nodefx.Drawer;
import org.arakhne.afc.nodefx.ZoomableGraphicsContext;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** Drawer of a map layer containers.
 *
 * @param <T> the type of the elements to draw.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
public class GisLayerContainerDrawer<T extends MapLayer> implements DocumentDrawer<T, GISLayerContainer<T>> {

	private final Drawer<? super T> drawer;

	/** Constructor based on a {@link StaticMapElementDrawer} drawer.
	 */
	public GisLayerContainerDrawer() {
		this(new StaticMapLayerDrawer());
	}

	/** Constructor based on the given drawer.
	 *
	 * @param drawer the element drawer.
	 */
	public GisLayerContainerDrawer(Drawer<? super T> drawer) {
		assert drawer != null : AssertMessages.notNullParameter();
		this.drawer = drawer;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends GISLayerContainer<T>> getElementType() {
		return (Class<? extends GISLayerContainer<T>>) GISLayerContainer.class;
	}

	@Override
	public Drawer<? super T> getElementDrawer() {
		return this.drawer;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends T> getContainedElementType() {
		return (Class<? extends T>) getElementDrawer().getElementType();
	}

	@Override
	public void draw(ZoomableGraphicsContext gc, GISLayerContainer<T> element) {
		final Iterator<T> iterator = element.getBottomUpIterator();
		while (iterator.hasNext()) {
			final T layer = iterator.next();
			if (isDrawable(gc, layer)) {
				gc.save();
				getElementDrawer().draw(gc, layer);
				gc.restore();
			}
		}
	}

	@Override
	public boolean isDrawable(ZoomableGraphicsContext gc, T maplayer) {
		return maplayer.isVisible();
	}

}
