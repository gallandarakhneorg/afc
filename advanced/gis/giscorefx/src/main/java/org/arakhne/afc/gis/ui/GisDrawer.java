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

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.mapelement.MapElement;
import org.arakhne.afc.gis.ui.drawers.GisContainerDrawer;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** Draw of a map element.
 *
 * @param <T> the type of the element to draw.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
public interface GisDrawer<T> {

	/** Replies all the registered GIS drawers.
	 *
	 * @return the drawers.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Pure
	static Iterator<GisDrawer<?>> getAllDrawers() {
		final ServiceLoader services = ServiceLoader.load(GisDrawer.class);
		return services.iterator();
	}

	/** Replies the registered GIS drawer  that is supporting the given type.
	 *
	 * @param <T> the type of the elements to drawer.
	 * @param type the type of the elements to drawer.
	 * @return the drawer, or {@code null} if none.
	 */
	@SuppressWarnings("unchecked")
	@Pure
	static <T> GisDrawer<T> getDrawersFor(Class<? extends T> type) {
		assert type != null : AssertMessages.notNullParameter();
		final Iterator<GisDrawer<?>> iterator = getAllDrawers();
		GisDrawer<T> defaultChoice = null;
		while (iterator.hasNext()) {
			final GisDrawer<?> drawer = iterator.next();
			final Class<?> elementType = drawer.getElementType();
			if (elementType.equals(type)) {
				return (GisDrawer<T>) drawer;
			}
			if (elementType.isAssignableFrom(type)
				&& (defaultChoice == null
					|| defaultChoice.getElementType().isAssignableFrom(elementType))) {
				defaultChoice = (GisDrawer<T>) drawer;
			}
		}
		return defaultChoice;
	}

	/** Replies the registered GIS Drawer that is supporting the given type.
	 *
	 * @param <T> the type of the elements to drawer.
	 * @param type the type of the elements to drawer.
	 * @return the drawer, or {@code null} if none.
	 */
	@SuppressWarnings("unchecked")
	@Pure
	static <T extends MapElement> GisContainerDrawer<T> getContainerDrawerFor(Class<? extends T> type) {
		assert type != null : AssertMessages.notNullParameter();
		final Iterator<GisDrawer<?>> iterator = getAllDrawers();
		GisContainerDrawer<T> defaultChoice = null;
		while (iterator.hasNext()) {
			final GisDrawer<?> drawer = iterator.next();
			if (drawer instanceof GisContainerDrawer<?>) {
				final GisContainerDrawer<?> containerRenderer = (GisContainerDrawer<?>) drawer;
				final Class<?> elementType = containerRenderer.getContainedElementType();
				if (elementType.equals(type)) {
					return (GisContainerDrawer<T>) drawer;
				}
				if (elementType.isAssignableFrom(type)
					&& (defaultChoice == null
						|| defaultChoice.getElementType().isAssignableFrom(elementType))) {
					defaultChoice = (GisContainerDrawer<T>) drawer;
				}
			}
		}
		return defaultChoice;
	}

	/** Draw the given map element.
	 *
	 * @param gc the graphics context that must be used for drawing.
	 * @param element the map element.
	 */
	void draw(GisGraphicsContext gc, T element);

	/** Replies the type of the components that could be drawn by this drawer.
	 *
	 * @return the type of the elements.
	 */
	@Pure
	Class<? extends T> getElementType();

}
