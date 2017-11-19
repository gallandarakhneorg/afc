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

import javafx.scene.paint.Color;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.mapelement.MapPoint;
import org.arakhne.afc.gis.ui.GisGraphicsContext;

/** Drawer of a map point.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
public class MapPointDrawer extends AbstractMapPointDrawer<MapPoint> {

	@Override
	@Pure
	public Class<? extends MapPoint> getElementType() {
		return MapPoint.class;
	}

	@Override
	public void draw(GisGraphicsContext gc, MapPoint element) {
		final Color color = gc.rgb(element.getColor());
		gc.setFill(color);
		gc.setStroke(color.darker());
		if (element.isDoubleFramed()) {
			defineBigRectangle(gc, element);
			gc.fill();
			gc.stroke();
			defineSmallRectangle(gc, element);
			gc.stroke();
		} else {
			defineSmallRectangle(gc, element);
			gc.fill();
			gc.stroke();
		}
	}

}
