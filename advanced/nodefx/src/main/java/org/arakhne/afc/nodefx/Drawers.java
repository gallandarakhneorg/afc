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

package org.arakhne.afc.nodefx;

import java.util.Iterator;
import java.util.ServiceLoader;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.afp.BoundedElement2afp;
import org.arakhne.afc.util.InformedIterable;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** Utility class for {@code ZoomableDrawer}.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
public final class Drawers {

	@SuppressWarnings("rawtypes")
	private static ServiceLoader services;

	private Drawers() {
		//
	}

	/** Replies all the registered document drawers.
	 *
	 * @return the drawers.
	 */
	@SuppressWarnings("unchecked")
	@Pure
	public static Iterator<Drawer<?>> getAllDrawers() {
		if (services == null) {
			services = ServiceLoader.load(Drawer.class);
		}
		return services.iterator();
	}

	/** Replies the registered document drawer that is supporting the given type.
	 *
	 * @param <T> the type of the elements to drawer.
	 * @param type the type of the elements to drawer.
	 * @return the drawer, or {@code null} if none.
	 */
	@SuppressWarnings("unchecked")
	@Pure
	public static <T> Drawer<T> getDrawersFor(Class<? extends T> type) {
		assert type != null : AssertMessages.notNullParameter();
		final Iterator<Drawer<?>> iterator = getAllDrawers();
		Drawer<T> defaultChoice = null;
		while (iterator.hasNext()) {
			final Drawer<?> drawer = iterator.next();
			final Class<?> elementType = drawer.getElementType();
			if (elementType.equals(type)) {
				return (Drawer<T>) drawer;
			}
			if (elementType.isAssignableFrom(type)
				&& (defaultChoice == null
					|| defaultChoice.getElementType().isAssignableFrom(elementType))) {
				defaultChoice = (Drawer<T>) drawer;
			}
		}
		return defaultChoice;
	}

	/** Replies the registered document Drawer that is supporting the given type.
	 *
	 * @param <T> the type of the elements to drawer.
	 * @param <DT> the type of the document.
	 * @param type the type of the elements to drawer.
	 * @return the drawer, or {@code null} if none.
	 */
	@SuppressWarnings("unchecked")
	@Pure
	public static <T, DT extends InformedIterable<? super T> & BoundedElement2afp<?>> DocumentDrawer<T, DT> getDocumentDrawerFor(
			Class<?> type) {
		assert type != null : AssertMessages.notNullParameter();
		final Iterator<Drawer<?>> iterator = getAllDrawers();
		DocumentDrawer<T, DT> defaultChoice = null;
		while (iterator.hasNext()) {
			final Drawer<?> drawer = iterator.next();
			if (drawer instanceof DocumentDrawer<?, ?>) {
				final DocumentDrawer<?, ?> containerRenderer = (DocumentDrawer<?, ?>) drawer;
				final Class<?> elementType = containerRenderer.getContainedElementType();
				if (elementType.equals(type)) {
					return (DocumentDrawer<T, DT>) drawer;
				}
				if (elementType.isAssignableFrom(type)
					&& (defaultChoice == null
						|| defaultChoice.getElementType().isAssignableFrom(elementType))) {
					defaultChoice = (DocumentDrawer<T, DT>) drawer;
				}
			}
		}
		return defaultChoice;
	}

}
