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

import javafx.scene.Group;
import javafx.scene.Node;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.mapelement.GISElementContainer;
import org.arakhne.afc.gis.mapelement.MapElement;
import org.arakhne.afc.math.geometry.d2.afp.RectangularShape2afp;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** Render of a map element container.
 *
 * @param <T> the type of the elements to render.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class GisContainerRenderer<T extends MapElement> extends AbstractGisElementRenderer<GISElementContainer<T>> {

	private final GisElementRenderer<T> renderer;

	/** Constructor.
	 *
	 * @param renderer the element renderer.
	 */
	public GisContainerRenderer(GisElementRenderer<T> renderer) {
		assert renderer != null : AssertMessages.notNullParameter();
		this.renderer = renderer;
	}

	/** Replies the element renderer.
	 *
	 * @return the element renderer.
	 */
	public GisElementRenderer<T> getElementRenderer() {
		return this.renderer;
	}

	@Override
	@Pure
	public Node drawShape(GISElementContainer<T> model, RectangularShape2afp<?, ?, ?, ?, ?, ?> bounds) {
		final Group group = new Group();
		final Iterator<T> iterator = model.iterator();
		final GisElementRenderer<T> renderer = getElementRenderer();
		while (iterator.hasNext()) {
			final T element = iterator.next();
			final Node node = renderer.drawShape(element, bounds);
			if (node != null) {
				group.getChildren().add(node);
			}
		}
		return group;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Pure
	public Class<? extends GISElementContainer<T>> getElementType() {
		return (Class<? extends GISElementContainer<T>>) GISElementContainer.class;
	}

	/** Replies the type of the elements inside this container.
	 *
	 * @return the type of the elements inside this container.
	 */
	@Pure
	public Class<? extends T> getContainedElementType() {
		return getElementRenderer().getElementType();
	}

}
