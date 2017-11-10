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

package org.arakhne.afc.gis.ui;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.mapelement.MapPolygon;
import org.arakhne.afc.math.geometry.d2.afp.RectangularShape2afp;

/** Renderer of a map polyline.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class MapPolygonRenderer extends AbstractMapPolygonRenderer<MapPolygon> {

	@Override
	@Pure
	public Node drawShape(MapPolygon element, RectangularShape2afp<?, ?, ?, ?, ?, ?> bounds) {
		final Path fxPath = createPath(element, bounds);
		final Color color = GisFxTools.rgb(element.getColor());
		fxPath.setFill(color);
		fxPath.setStroke(color);
		return fxPath;
	}

	@Override
	@Pure
	public Class<? extends MapPolygon> getElementType() {
		return MapPolygon.class;
	}

}
