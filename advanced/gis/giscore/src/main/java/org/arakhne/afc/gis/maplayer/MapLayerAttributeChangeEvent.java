/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

import org.arakhne.afc.attrs.attr.Attribute;
import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.attrs.collection.AttributeChangeEvent;
import org.arakhne.afc.attrs.collection.AttributeChangeEvent.Type;

/**
 * This interface representes an event on the layer attribute changes.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class MapLayerAttributeChangeEvent extends ConsumableEvent {

	private static final long serialVersionUID = -1392350428889831180L;

	private final AttributeChangeEvent originalEvent;

	/** Constructor.
	 * @param source the event source.
	 * @param event the event.
	 */
	public MapLayerAttributeChangeEvent(Object source, AttributeChangeEvent event) {
		super(source);
		this.originalEvent = event;
	}

	/** Replies the layer that changed.
	 *
	 * @return the layer that changed.
	 */
	@Pure
	public MapLayer getLayer() {
		return (MapLayer) getSource();
	}

	/** Replies the original event that describes the attribute change.
	 *
	 * @return the original event.
	 */
	@Pure
	public AttributeChangeEvent getEvent() {
		return this.originalEvent;
	}

	/** Replies the name of the changed attributes.
	 *
	 * @return the attribute name.
	 */
	@Pure
	public String getName() {
		return this.originalEvent.getName();
	}

	/** Replies the old name of the changed attributes.
	 *
	 * @return the old attribute name.
	 */
	@Pure
	public String getOldName() {
		return this.originalEvent.getOldName();
	}

	/** Replies the old value of the attribute.
	 *
	 * @return the attribute value or {@code null}
	 */
	@Pure
	public AttributeValue getOldValue() {
		return this.originalEvent.getOldValue();
	}

	/** Replies the new value of the attribute.
	 *
	 * @return the attribute value, never {@code null}
	 */
	@Pure
	public AttributeValue getValue() {
		return this.originalEvent.getValue();
	}

	/** Replies the changed attribute.
	 *
	 * @return the attribute, never {@code null}
	 */
	@Pure
	public Attribute getAttribute() {
		return this.originalEvent.getAttribute();
	}

	/** Replies the type of event.
	 *
	 * @return the type of event.
	 */
	@Pure
	public Type getType() {
		return this.originalEvent.getType();
	}

	/** Replies if the change in the layer was marked as temporary.
	 * The usage of this information depends on the listener's behaviour.
	 *
	 * @return {@code true} if the change is temporary,
	 *     otherwise {@code false}
	 */
	@Pure
	public boolean isTemporaryChange() {
		final Object src = getSource();
		if (src instanceof MapLayer) {
			return ((MapLayer) src).isTemporaryLayer();
		}
		return false;
	}

}
