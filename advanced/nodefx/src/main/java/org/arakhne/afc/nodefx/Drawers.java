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

import org.arakhne.afc.references.SoftValueTreeMap;
import org.arakhne.afc.vmutil.ClassComparator;
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

	private static SoftValueTreeMap<Class<?>, Drawer<?>> buffer = new SoftValueTreeMap<>(ClassComparator.SINGLETON);

	private Drawers() {
		//
	}

	/** Reload the drawers' definition.
	 */
	public static void reload() {
		buffer.clear();
		services.reload();
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

	/** Replies the first registered document drawer that is supporting the given type.
	 *
	 * <p>If multiple drawers handles the given type, the ones handling the top most type
	 * within the type hierarchy are selected. If there is more than one drawer selected,
	 * the one that is the lowest into the class hierarchy is chosen. In this case, there is no warranty about the
	 * order of the drawers; and the replied drawer may be any of the selected drawers.
	 * The only assumption that could be done on the order of the drawers is:
	 * if only one Jar library provides drawers' implementation, then the order of
	 * the drawers is the same as the order of the types declared within the service's file.
	 *
	 * @param <T> the type of the elements to drawer.
	 * @param type the type of the elements to drawer.
	 * @return the drawer, or {@code null} if none.
	 */
	@SuppressWarnings("unchecked")
	@Pure
	public static <T> Drawer<T> getDrawerFor(Class<? extends T> type) {
		assert type != null : AssertMessages.notNullParameter();
		final Drawer<?> bufferedType = buffer.get(type);
		Drawer<T> defaultChoice = null;
		if (bufferedType != null) {
			defaultChoice = (Drawer<T>) bufferedType;
		} else {
			final Iterator<Drawer<?>> iterator = getAllDrawers();
			while (iterator.hasNext()) {
				final Drawer<?> drawer = iterator.next();
				final Class<?> drawerType = drawer.getPrimitiveType();
				if (drawerType.equals(type)) {
					if (defaultChoice == null
						|| !defaultChoice.getPrimitiveType().equals(type)
						|| defaultChoice.getClass().isAssignableFrom(drawer.getClass())) {
						defaultChoice = (Drawer<T>) drawer;
					}
				} else  if (drawerType.isAssignableFrom(type)
					&& (defaultChoice == null
						|| drawerType.isAssignableFrom(defaultChoice.getPrimitiveType()))) {
					defaultChoice = (Drawer<T>) drawer;
				}
			}
			if (defaultChoice != null) {
				buffer.put(type, defaultChoice);
			}
		}
		return defaultChoice;
	}

	/** Replies the first registered document drawer that is supporting the given instance.
	 *
	 * @param <T> the type of the element.
	 * @param instance the element to drawer.
	 * @return the drawer, or {@code null} if none.
	 * @since 16.0
	 */
	@Pure
	@SuppressWarnings("unchecked")
	public static <T> Drawer<? super T> getDrawerFor(T instance) {
		if (instance != null) {
			if (instance instanceof DrawerReference) {
				final DrawerReference<T> drawable = (DrawerReference<T>) instance;
				Drawer<? super T> drawer = drawable.getDrawer();
				if (drawer != null) {
					return drawer;
				}
				drawer = getDrawerFor(instance.getClass());
				drawable.setDrawer(drawer);
				return drawer;
			}
			return getDrawerFor(instance.getClass());
		}
		return null;
	}

}
