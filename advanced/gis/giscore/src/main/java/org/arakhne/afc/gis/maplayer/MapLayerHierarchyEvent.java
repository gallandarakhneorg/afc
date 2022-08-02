/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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

import java.util.Collections;
import java.util.List;

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * This interface representes an event on the hierarchy change inside
 * a set of layers.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class MapLayerHierarchyEvent extends ConsumableEvent {

	private static final long serialVersionUID = 3172814968867800834L;

	private final GISLayerContainer<?> parentLayer;

	private final List<? extends MapLayer> childLayers;

	private final Type type;

	private final int index;

	private final boolean temporaryChange;

	/** Constructor.
	 * @param parentLayer the parent layer.
	 * @param childLayer the child layer.
	 * @param type the type of event.
	 * @param index the layer's index.
	 * @param temporaryChange indicates if the change is temporary in time.
	 *     The usage of this information depends on the listener's behaviour.
	 */
	public MapLayerHierarchyEvent(GISLayerContainer<?> parentLayer, MapLayer childLayer, Type type,
			int index, boolean temporaryChange) {
		super(parentLayer);
		this.parentLayer = parentLayer;
		this.childLayers = Collections.singletonList(childLayer);
		this.type = type;
		this.index = index;
		this.temporaryChange = temporaryChange;
	}

	/** Constructor.
	 * @param parentLayer the parent layer.
	 * @param childLayers the child layers.
	 * @param type the type of event.
	 * @param index the layer's index.
	 * @param temporaryChange indicates if the change is temporary in time.
	 *     The usage of this information depends on the listener's behaviour.
	 */
	public MapLayerHierarchyEvent(GISLayerContainer<?> parentLayer, List<? extends MapLayer> childLayers, Type type,
			int index, boolean temporaryChange) {
		super(parentLayer);
		this.parentLayer = parentLayer;
		this.childLayers = childLayers;
		this.type = type;
		this.index = index;
		this.temporaryChange = temporaryChange;
	}

	/** Replies the index of the layer in its parent.
	 *
	 * @return the index of the layer in its parent.
	 */
	@Pure
	public int getIndex() {
		return this.index;
	}

	/** Replies the type of event.
	 *
	 * @return the type of event.
	 */
	@Pure
	public Type getType() {
		return this.type;
	}

	/** Replies the parent layer.
	 *
	 * @return the parent layer.
	 */
	@Pure
	public GISLayerContainer<?> getParent() {
		return this.parentLayer;
	}

	/** Replies the child layer.
	 *
	 * @return the child layer.
	 */
	@Pure
	public MapLayer getChild() {
		return this.childLayers.get(0);
	}


	/** Replies the child layers.
	 *
	 * @return the child layers.
	 */
	@Pure
	public List<? extends MapLayer> getChildren() {
		return Collections.unmodifiableList(this.childLayers);
	}

	/** Replies if the change in the layer was marked as temporary.
	 * The usage of this information depends on the listener's behaviour.
	 *
	 * @return <code>true</code> if the change is temporary,
	 *     otherwise <code>false</code>
	 */
	@Pure
	public boolean isTemporaryChange() {
		return this.temporaryChange;
	}

	/**
	 * This interface representes the types of hierarchy events.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	public enum Type {
		// Events from the viewpoint of the layers that were removed/added
		/**
		 * The layer was removed from its container. This type of event
		 * is received by the removed layer.
		 */
		REMOVED_FROM_PARENT,
		/**
		 * The layer was added into its container. This type of event
		 * is received by the removed layer.
		 */
		ADDED_INTO_PARENT,
		// Events from the viewpoint of the layers that receive the action
		/**
		 * The layer was added into its container. This type of event
		 * is received by the parent of the new layer.
		 */
		ADD_CHILD,
		/**
		 * The layer was removed from its container. This type of event
		 * is received by the parent of the removed layer.
		 */
		REMOVE_CHILD,
		/**
		 * All the layers are removed from a container. This type of event
		 * is received by the parent of the removed layers.
		 */
		REMOVE_ALL_CHILDREN,
		/**
		 * The order of the children has changed: a child layer has moving up.
		 * This type of event is received by the parent of the moveed layer.
		 */
		MOVE_CHILD_UP,
		/**
		 * The order of the children has changed: a child layer has moving down.
		 * This type of event is received by the parent of the moveed layer.
		 */
		MOVE_CHILD_DOWN,

		/**
		 * The order of the children has changed: child layers have moving.
		 * This type of event is received by the parent of the moved layer.
		 */
		MOVE_CHILDREN;
	}

}
