/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.mapelement.MapPolyline;
import org.arakhne.afc.nodefx.ZoomableGraphicsContext;

/** Drawer of a map polyline.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
public class MapPolylineDrawer extends AbstractMapPolylineDrawer<MapPolyline> {

	@Override
	@Pure
	public Class<? extends MapPolyline> getPrimitiveType() {
		return MapPolyline.class;
	}

	@Override
	public void draw(ZoomableGraphicsContext gc, MapPolyline element) {
		definePath(gc, element);

		final Color color = gc.rgb(getDrawingColor(element));
		gc.setFill(null);
		gc.setStroke(color);
		if (element.isWidePolyline()) {
			gc.setLineWidthInMeters(element.getWidth());
		} else {
			gc.setLineWidthInPixels(1);
		}
		gc.setLineCap(StrokeLineCap.ROUND);
		gc.setLineJoin(StrokeLineJoin.ROUND);
		gc.stroke();
	}

}
