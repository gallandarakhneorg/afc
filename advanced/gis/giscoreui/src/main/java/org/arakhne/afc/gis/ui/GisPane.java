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
import javafx.scene.layout.Pane;

import org.arakhne.afc.gis.mapelement.GISElementContainer;
import org.arakhne.afc.gis.mapelement.MapElement;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** This component renders GIS primitives.
 *
 * @param <T> the type of the map elements.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class GisPane<T extends MapElement> extends Pane {

	private final GISElementContainer<T> model;

	private final GisContainerRenderer<T> renderer;

	/** Constructor. The renderer is detected with the type replied by
	 * {@link GISElementContainer#getElementType()} on the model.
	 *
	 * @param model the source of the elements.
	 */
	public GisPane(GISElementContainer<T> model) {
		this(model, GisElementRenderer.getContainerRenderersFor(model.getElementType()));
	}

	/** Constructor.
	 *
	 * @param model the source of the elements.
	 * @param renderer the renderers.
	 */
	public GisPane(GISElementContainer<T> model, GisContainerRenderer<T> renderer) {
		assert model != null : AssertMessages.notNullParameter(0);
		assert renderer != null : AssertMessages.notNullParameter(1);
		this.model = model;
		this.renderer = renderer;
		draw();
	}

	/** Draw the elements.
	 */
	@SuppressWarnings("all")
	protected void draw() {
		Rectangle2d bounds = this.model.getBoundingBox();
		final Node node = this.renderer.drawShape(this.model, bounds);
		if (node != null) {
			getChildren().add(node);
		}
	}

}
