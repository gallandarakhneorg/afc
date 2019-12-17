/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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
import java.util.concurrent.atomic.AtomicBoolean;

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

	private static ServiceLoader<Drawer<?>> services;

	private static Iterable<Drawer<?>> iterable;

	private static SoftValueTreeMap<Class<?>, Drawer<?>> buffer = new SoftValueTreeMap<>(ClassComparator.SINGLETON);

	private static final AtomicBoolean LOCK = new AtomicBoolean();

	private Drawers() {
		//
	}

	private static boolean getLock() {
		if (LOCK.getAndSet(true)) {
			throw new IllegalStateException(Drawers.class.getName()
					+ " is not reentrant. Please do not call it from drawer's constructor."); //$NON-NLS-1$
		}
		return true;
	}

	private static boolean releaseLock() {
		LOCK.set(false);
		return true;
	}

	/** Reload the drawers' definition.
	 */
	public static synchronized void reload() {
		buffer.clear();
		getDrawerProvidingService().reload();
	}

	/** Replies all the registered document drawers.
	 *
	 * @return the drawers.
	 */
	@Pure
	public static synchronized Iterator<Drawer<?>> getAllDrawers() {
		if (iterable == null) {
			iterable = getDrawerProvidingService();
		}
		return iterable.iterator();
	}

	/** Change the collection of drawers.
	 *
	 * @param drawers the collection of the drawers.
	 * @since 17.0
	 */
	public static synchronized void setBackedDrawers(Iterable<Drawer<?>> drawers) {
		iterable = drawers;
	}

	/** Replies the service that provides the drawers.
	 *
	 * @return the drawers' providing service.
	 * @since 17.0
	 */
	@SuppressWarnings("unchecked")
	@Pure
	public static synchronized ServiceLoader<Drawer<?>> getDrawerProvidingService() {
		if (services == null) {
			@SuppressWarnings("rawtypes")
			final ServiceLoader serv = ServiceLoader.load(Drawer.class);
			services = serv;
		}
		return services;
	}

	/** Replies the first registered document drawer that is supporting the given type.
	 *
	 * <p>If multiple drawers handles the given type, the ones handling the bottom most type
	 * within the type hierarchy are selected.
	 * In the case there is multiple drawers exactly handling the type, there is no warranty about the
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
	public static synchronized <T> Drawer<T> getDrawerFor(Class<? extends T> type) {
		assert type != null : AssertMessages.notNullParameter();
		try {
			assert getLock();
			final Drawer<?> bufferedType = buffer.get(type);
			Drawer<T> choice = null;
			if (bufferedType != null) {
				choice = (Drawer<T>) bufferedType;
			} else {
				final Iterator<Drawer<?>> iterator = getAllDrawers();
				Drawer<?> exactMatcherDrawer = null;
				Drawer<?> superMatcherDrawer = null;
				while (iterator.hasNext()) {
					final Drawer<?> drawer = iterator.next();
					final Class<?> drawerType = drawer.getPrimitiveType();
					if (exactMatcherDrawer != null) {
						// Drawer that is exactly matching the element type is already found.
						// If the new drawer is a subtype of the previous one, the new drawer is used.
						if (drawerType.equals(type)
							&& exactMatcherDrawer.getClass().isAssignableFrom(drawer.getClass())) {
							exactMatcherDrawer = drawer;
						}
					} else if (drawerType.equals(type)) {
						// First time a drawer that is exactly matching the element type was found.
						exactMatcherDrawer = drawer;
					} else if (drawerType.isAssignableFrom(type)) {
						// The new drawer is able to support the element type because it could
						// draw elements with a super type of the expected element type.
						// The best choice is the drawer that supports the lowest type into the type hierarchy.
						if (superMatcherDrawer == null
							|| (superMatcherDrawer.getPrimitiveType().isAssignableFrom(drawerType))) {
							superMatcherDrawer = drawer;
						}
					}
				}
				if (exactMatcherDrawer != null) {
					choice = (Drawer<T>) exactMatcherDrawer;
				} else if (superMatcherDrawer != null) {
					choice = (Drawer<T>) superMatcherDrawer;
				}
				// Memorize for further use.
				if (choice != null) {
					buffer.put(type, choice);
				}
			}
			return choice;
		} finally {
			assert releaseLock();
		}
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
	public static synchronized <T> Drawer<? super T> getDrawerFor(T instance) {
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
