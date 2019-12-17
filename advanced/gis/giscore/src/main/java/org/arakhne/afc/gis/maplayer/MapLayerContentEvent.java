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

package org.arakhne.afc.gis.maplayer;

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * This interface representes an event on the layer content changes.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class MapLayerContentEvent extends ConsumableEvent {

	private static final long serialVersionUID = 5661900782832388216L;

	/** Constructor.
	 * @param layer the layer.
	 */
	public MapLayerContentEvent(MapLayer layer) {
		super(layer);
	}

	/** Replies the layer that changed.
	 *
	 * @return the layer that changed.
	 */
	@Pure
	public MapLayer getLayer() {
		return (MapLayer) getSource();
	}

	/** Replies if the change in the layer was marked as temporary.
	 * The usage of this information depends on the listener's behaviour.
	 *
	 * @return <code>true</code> if the change is temporary,
	 *     otherwise <code>false</code>
	 */
	@Pure
	public boolean isTemporaryChange() {
		return getLayer().isTemporaryLayer();
	}

}
