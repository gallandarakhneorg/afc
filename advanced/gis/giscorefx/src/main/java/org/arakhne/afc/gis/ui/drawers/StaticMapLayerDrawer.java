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

import org.arakhne.afc.gis.maplayer.MapLayer;
import org.arakhne.afc.nodefx.Drawer;
import org.arakhne.afc.nodefx.Drawers;
import org.arakhne.afc.nodefx.ZoomableGraphicsContext;

/** Drawer that draws any map layer for which a drawer is known.
 *
 * <p>The drawer is determined a single time when the first map layer is drawn.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
public class StaticMapLayerDrawer implements Drawer<MapLayer> {

	private Class<? extends MapLayer> type = MapLayer.class;

	private Drawer<? super MapLayer> drawer;

	@Override
	public Class<? extends MapLayer> getElementType() {
		return this.type;
	}

	@Override
	public void draw(ZoomableGraphicsContext gc, MapLayer element) {
		Drawer<? super MapLayer> drawer = this.drawer;
		if (drawer == null) {
			drawer = Drawers.getDrawerFor(element.getClass());
			if (drawer != null) {
				this.type = element.getClass();
				this.drawer = drawer;
			}
		}
		if (drawer != null) {
			drawer.draw(gc, element);
		}
	}

}
