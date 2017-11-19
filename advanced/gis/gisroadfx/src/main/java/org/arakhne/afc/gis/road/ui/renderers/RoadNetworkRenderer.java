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

package org.arakhne.afc.gis.road.ui.renderers;

/** Render of a map polyline.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class RoadNetworkRenderer /*extends GisContainerRenderer<RoadPolyline>*/ {

	/** Constructor.
	 */
	public RoadNetworkRenderer() {
		//this(GisElementRenderer.getRenderersFor(RoadPolyline.class));
	}

	/** Constructor.
	 *
	 * @param renderer the element renderer.
	 */
	/*public RoadNetworkRenderer(GisElementRenderer<RoadPolyline> renderer) {
		super(renderer);
	}

	@Override
	public Node drawShape(GISElementContainer<RoadPolyline> model, RectangularShape2afp<?, ?, ?, ?, ?, ?> bounds) {
		final Node networkShape = super.drawShape(model, bounds);
		final DropShadow borders = new DropShadow(2, Color.BLACK);
		borders.setBlurType(BlurType.ONE_PASS_BOX);
		networkShape.setEffect(borders);
		return networkShape;
	}*/

}
