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

package org.arakhne.afc.gis.maplayer;

import java.util.EventObject;

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * This interface representes an event on the layer attribute changes.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class ConsumableEvent extends EventObject {

	private static final long serialVersionUID = -7450003442407718943L;

	private boolean isConsumed;

	private final boolean disappearWhenConsumed;

	/**
	 * Constructs a prototypical Event.
	 *
	 * @param    source    The object on which the Event initially occurred.
	 * @exception  IllegalArgumentException  if source is null.
	 */
	public ConsumableEvent(Object source) {
		super(source);
		this.disappearWhenConsumed = false;
	}

	/**
	 * Constructs a prototypical Event.
	 *
	 * @param    source    The object on which the Event initially occurred.
	 * @param disappearWhenConsumed indicates if the event is supposed to disappear when it was consumed.
	 * @throws IllegalArgumentException  if source is null.
	 */
	public ConsumableEvent(Object source, boolean disappearWhenConsumed) {
		super(source);
		this.disappearWhenConsumed = disappearWhenConsumed;
	}

	/** Replies if this object was already marked as consumed.
	 *
	 * @return <code>true</code> if the event was consumed,
	 *     otherwise <code>false</code>
	 */
	@Pure
	public boolean isConsumed() {
		return this.isConsumed;
	}

	/** Mark this event as consumed.
	 */
	public void consume() {
		this.isConsumed = true;
	}

	/** Replies if this event should disappear when it was consumed.
	 *
	 * <p>The use of this flag depends on the event forwarder implementations.
	 *
	 * @return <code>true</code> if the event is supposed to diseappear after its consumption,
	 *     otherwise <code>false</code>
	 */
	@Pure
	public boolean isDisappearingWhenConsumed() {
		return this.disappearWhenConsumed;
	}

}
