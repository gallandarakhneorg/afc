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

import java.util.Iterator;
import java.util.ServiceLoader;

import javafx.scene.Node;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.mapelement.MapElement;
import org.arakhne.afc.math.geometry.d2.afp.RectangularShape2afp;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** Renderer of a map element.
 *
 * @param <T> the type of the element to render.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public interface GisElementRenderer<T> {

	/** Replies all the registered GIS Renderers.
	 *
	 * @return the renderers.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Pure
	static Iterator<GisElementRenderer<?>> getAllRenderers() {
		final ServiceLoader services = ServiceLoader.load(GisElementRenderer.class);
		return services.iterator();
	}

	/** Replies the registered GIS Renderer that is supporting the given type.
	 *
	 * @param <T> the type of the elements to renderer.
	 * @param type the type of the elements to renderer.
	 * @return the renderer, or {@code null} if none.
	 */
	@SuppressWarnings("unchecked")
	@Pure
	static <T> GisElementRenderer<T> getRenderersFor(Class<? extends T> type) {
		assert type != null : AssertMessages.notNullParameter();
		final Iterator<GisElementRenderer<?>> iterator = getAllRenderers();
		GisElementRenderer<T> defaultChoice = null;
		while (iterator.hasNext()) {
			final GisElementRenderer<?> renderer = iterator.next();
			final Class<?> elementType = renderer.getElementType();
			if (elementType.equals(type)) {
				return (GisElementRenderer<T>) renderer;
			}
			if (elementType.isAssignableFrom(type)
				&& (defaultChoice == null
					|| defaultChoice.getElementType().isAssignableFrom(elementType))) {
				defaultChoice = (GisElementRenderer<T>) renderer;
			}
		}
		return defaultChoice;
	}

	/** Replies the registered GIS Renderer that is supporting the given type.
	 *
	 * @param <T> the type of the elements to renderer.
	 * @param type the type of the elements to renderer.
	 * @return the renderer, or {@code null} if none.
	 */
	@SuppressWarnings("unchecked")
	@Pure
	static <T extends MapElement> GisContainerRenderer<T> getContainerRenderersFor(Class<? extends T> type) {
		assert type != null : AssertMessages.notNullParameter();
		final Iterator<GisElementRenderer<?>> iterator = getAllRenderers();
		GisContainerRenderer<T> defaultChoice = null;
		while (iterator.hasNext()) {
			final GisElementRenderer<?> renderer = iterator.next();
			if (renderer instanceof GisContainerRenderer<?>) {
				final GisContainerRenderer<?> containerRenderer = (GisContainerRenderer<?>) renderer;
				final Class<?> elementType = containerRenderer.getContainedElementType();
				if (elementType.equals(type)) {
					return (GisContainerRenderer<T>) renderer;
				}
				if (elementType.isAssignableFrom(type)
					&& (defaultChoice == null
						|| defaultChoice.getElementType().isAssignableFrom(elementType))) {
					defaultChoice = (GisContainerRenderer<T>) renderer;
				}
			}
		}
		return defaultChoice;
	}

	/** Draw the given map element.
	 *
	 * @param element the map element.
	 * @param bounds the bounds of the GIS area to be displayed.
	 * @return the JavaFX shape.
	 */
	@Pure
	Node drawShape(T element, RectangularShape2afp<?, ?, ?, ?, ?, ?> bounds);

	/** Replies the type of the components that could be drawn by this renderer.
	 *
	 * @return the type of the elements.
	 */
	@Pure
	Class<? extends T> getElementType();

}
