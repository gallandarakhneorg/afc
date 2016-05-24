/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
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

package org.arakhne.afc.ui.selection ;

import java.util.EventObject;

/** Event in selection manager.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
public class SelectionEvent extends EventObject {

	private static final long serialVersionUID = 2414246708227737737L;
	
	private final Selectable object;
	private final boolean isRemoved;
	private final boolean isLastEvent;
	
	/**
	 * @param source
	 * @param object
	 * @param removed
	 * @param isAdjusting indicates if the event to fire is the last inside
	 * a sequence of events. If <code>true</code> the event to fire must
	 * be followed by other selection events that are produces by the
	 * same action on the selection manager. If <code>false</code>, there
	 * is no following selection event for the same action on the selection
	 * manager.
	 */
	public SelectionEvent(SelectionManager<?> source, Selectable object, boolean removed, boolean isAdjusting) {
		super(source);
		this.object = object;
		this.isRemoved = removed;
		this.isLastEvent = !isAdjusting;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SelectionManager<?> getSource() {
		return (SelectionManager<?>)super.getSource();
	}
	
	/** Replies the selectable object concerned by the selection event.
	 * 
	 * @return the figure.
	 */
	public Selectable getSelectable() {
		return this.object;
	}
	
	/** Replies if the selectable object has just been selected.
	 * 
	 * @return <code>true</code> if the selectable object state passes from
	 * unselected to selected.
	 */
	public boolean isSelected() {
		return !this.isRemoved;
	}

	/** Replies if the selectable object has just been unselected.
	 * 
	 * @return <code>true</code> if the selectable object state passes from
	 * selected to unselected.
	 */
	public boolean isUnselected() {
		return this.isRemoved;
	}
	
	/** Replies if this event is the last event in a
	 * sequence of events that are fire by a single
	 * action on the selection manager.
	 * <p>
	 * This flag may be used to update some external
	 * objects (eg, UI) at the end of a sequence
	 * of selection changes.
	 * 
	 * @return <code>true</code> if this event is the
	 * last of a sequence of events; <code>false</code>
	 * if this event is a part of a sequence of selection
	 * events, but not the last one.
	 */
	public boolean isLastEvent() {
		return this.isLastEvent;
	}

}
