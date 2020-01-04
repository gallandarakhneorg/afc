/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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

import org.eclipse.xtext.xbase.lib.Pure;

/** Drawer that defers to another drawer dynamically.
 * This drawer is implemented in order to be used inside the
 * constructors of a Drawer when it needs another drawer.
 *
 * @param <T> the type of the primitives to draw.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 16.0
 */
public final class DeferredDrawer<T> implements Drawer<T> {

	private final Class<? extends T> type;

	private Drawer<T> drawer;

	/** Constructor.
	 *
	 * @param type the type of elements to be drawn.
	 */
	private DeferredDrawer(Class<? extends T> type) {
		this.type = type;
	}

	/** Constructor.
	 *
	 * @param <T> the type of elements to be drawn.
	 * @param type the type of elements to be drawn.
	 * @return the drawer.
	 */
	public static <T> Drawer<T> of(Class<? extends T> type) {
		return new DeferredDrawer<>(type);
	}

	@Override
	public void draw(ZoomableGraphicsContext gc, T primitive) {
		Drawer<T> drw = this.drawer;
		if (drw == null) {
			drw = Drawers.getDrawerFor(this.type);
			this.drawer = drw;
		}
		if (drw != null) {
			drw.draw(gc, primitive);
		}
	}

	@Override
	@Pure
	public Class<? extends T> getPrimitiveType() {
		return this.type;
	}

}
