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

package org.arakhne.afc.gis.ui.drawers;

import java.util.Iterator;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.mapelement.GISElementContainer;
import org.arakhne.afc.gis.mapelement.MapElement;
import org.arakhne.afc.nodefx.Drawer;
import org.arakhne.afc.nodefx.DrawerReference;
import org.arakhne.afc.nodefx.Drawers;
import org.arakhne.afc.nodefx.ZoomableGraphicsContext;

/** Drawer of a map element container.
 *
 * @param <T> the type of the elements to draw.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
public class GisElementContainerDrawer<T extends MapElement> implements Drawer<GISElementContainer<T>>, DrawerReference<T> {

	private Drawer<? super T> drawer;

	/** Constructor.
	 */
	public GisElementContainerDrawer() {
		this(null);
	}

	/** Constructor based on the given drawer.
	 *
	 * @param drawer the element drawer.
	 */
	public GisElementContainerDrawer(Drawer<? super T> drawer) {
		this.drawer = drawer;
	}

	@Override
	public Drawer<? super T> getDrawer() {
		return this.drawer;
	}

	@Override
	public void setDrawer(Drawer<? super T> drawer) {
		this.drawer = drawer;
	}

	@Override
	public final void draw(ZoomableGraphicsContext gc, GISElementContainer<T> primitive) {
		draw(gc, primitive, getDrawer());
	}

	/** Draw the primitive with the given drawer.
	 *
	 * @param gc the graphics context to draw with.
	 * @param primitive the primitive to draw.
	 * @param drawer the drawer, or {@code null} to use the default.
	 * @return the used drawer.
	 */
	protected Drawer<? super T> draw(ZoomableGraphicsContext gc, GISElementContainer<T> primitive, Drawer<? super T> drawer) {
		return drawPrimitives(gc, primitive, drawer);
	}

	/** Draw the primitives.
	 *
	 * <p>This function calls the drawers on each primitive inside the given container. It is not expected to be
	 * overridden into the sub-classes.
	 *
	 * @param gc the graphics context to draw with.
	 * @param primitive the primitive to draw.
	 * @param drawer the drawer, or {@code null} to use the default.
	 * @return the used drawer.
	 * @since 16.0
	 */
	protected Drawer<? super T> drawPrimitives(ZoomableGraphicsContext gc, GISElementContainer<T> primitive, Drawer<? super T> drawer) {
		Drawer<? super T> drw = drawer;
		final Iterator<T> iterator = primitive.iterator(gc.getVisibleArea());
		while (iterator.hasNext()) {
			final T mapelement = iterator.next();
			if (drw == null) {
				drw = Drawers.getDrawerFor(mapelement);
				if (drw != null) {
					gc.save();
					drw.draw(gc, mapelement);
					gc.restore();
				}
			} else {
				gc.save();
				drw.draw(gc, mapelement);
				gc.restore();
			}
		}
		return drw;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Pure
	public Class<? extends GISElementContainer<T>> getPrimitiveType() {
		return (Class<? extends GISElementContainer<T>>) GISElementContainer.class;
	}

}
