/* 
 * $Id$
 * 
 * Copyright (C) 2012 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
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
