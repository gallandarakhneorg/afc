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

package org.arakhne.afc.gis.ui.drawers;

import java.util.Iterator;

import org.arakhne.afc.gis.maplayer.GISLayerContainer;
import org.arakhne.afc.gis.maplayer.MapLayer;
import org.arakhne.afc.nodefx.Drawer;
import org.arakhne.afc.nodefx.DrawerReference;
import org.arakhne.afc.nodefx.Drawers;
import org.arakhne.afc.nodefx.ZoomableGraphicsContext;

/** Drawer of a map layer containers.
 *
 * @param <T> the type of the elements to draw.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
@SuppressWarnings("rawtypes")
public class GisLayerContainerDrawer<T extends MapLayer> implements Drawer<GISLayerContainer>, DrawerReference<T> {

	private Drawer<? super T> drawer;

	/** Constructor.
	 */
	public GisLayerContainerDrawer() {
		this(null);
	}

	/** Constructor based on the given drawer.
	 *
	 * @param drawer the element drawer.
	 */
	public GisLayerContainerDrawer(Drawer<? super T> drawer) {
		this.drawer = drawer;
	}

	@Override
	public Drawer<? super T> getDrawer() {
		return this.drawer;
	}

	@Override
	public void setDrawer(Drawer<? super T> drawer) {
		this.drawer = drawer;
	}

	@Override
	public Class<? extends GISLayerContainer> getPrimitiveType() {
		return GISLayerContainer.class;
	}

	@Override
	public void draw(ZoomableGraphicsContext gc, GISLayerContainer primitive) {
		@SuppressWarnings("unchecked")
		final Iterator<T> iterator = primitive.getBottomUpIterator();
		final Drawer<? super T> drw = getDrawer();
		if (drw != null) {
			while (iterator.hasNext()) {
				final T layer = iterator.next();
				if (layer.isVisible()) {
					gc.save();
					drw.draw(gc, layer);
					gc.restore();
				}
			}
		} else {
			while (iterator.hasNext()) {
				final T layer = iterator.next();
				if (layer.isVisible()) {
					final Drawer<? super T> drawer = Drawers.getDrawerFor(layer);
					if (drawer != null) {
						gc.save();
						drawer.draw(gc, layer);
						gc.restore();
					}
				}
			}
		}
	}

}
