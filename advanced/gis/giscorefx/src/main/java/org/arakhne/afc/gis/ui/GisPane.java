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

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.mapelement.GISElementContainer;
import org.arakhne.afc.gis.mapelement.MapElement;
import org.arakhne.afc.nodefx.DocumentDrawer;
import org.arakhne.afc.nodefx.ZoomablePane;
import org.arakhne.afc.util.InformedIterable;

/** Resizeable pane for rendering GIS primitives.
 *
 * <p>The GIS elements are displayed within this {@code GisCanvas} by the {@link DocumentDrawer drawers}
 * that are declared as services.
 *
 * <p>The {@code GisCanvas} provides a tool for displaying GIS elements. It does not provide
 * advanced UI components (scroll bars, etc.) and interaction means (mouse support, etc.).
 *
 * @param <T> the type of the map elements.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
public class GisPane<T extends MapElement> extends ZoomablePane<T, GISElementContainer<T>> {

	/** Constructor. The renderer is detected with the type replied by
	 * {@link InformedIterable#getElementType()} on the model.
	 *
	 * @param model the source of the elements.
	 */
	public GisPane(GISElementContainer<T> model) {
		this(new GisCanvas<>(model));
	}

	/** Constructor.
	 *
	 * @param model the source of the elements.
	 * @param drawer the drawer.
	 */
	public GisPane(GISElementContainer<T> model, DocumentDrawer<T, GISElementContainer<T>> drawer) {
		this(new GisCanvas<>(model, drawer));
	}

	/** Constructor with the canvas.
	 *
	 * @param canvas the pre-created canvas with the model to display inside.
	 */
	public GisPane(GisCanvas<T> canvas) {
		super(canvas);
	}

	/** Replies the document canvas within this pane.
	 *
	 * @return the document canvas.
	 * @since 15.0
	 */
	@Pure
	public GisCanvas<T> getGisCanvas() {
		return (GisCanvas<T>) getDocumentCanvas();
	}

}
