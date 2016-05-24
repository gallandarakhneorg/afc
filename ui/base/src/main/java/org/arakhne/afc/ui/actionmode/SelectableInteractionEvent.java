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

package org.arakhne.afc.ui.actionmode ;

import java.util.Collection;
import java.util.Collections;
import java.util.EventObject;

import org.arakhne.afc.ui.event.KeyEvent;
import org.arakhne.afc.ui.selection.Selectable;

/** Describes the interaction event.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
public class SelectableInteractionEvent extends EventObject {

	private static final long serialVersionUID = -1039544916556548342L;

	private final Collection<? extends Selectable> figures;
	private boolean consumed = false;
	private final ActionPointerEvent pointerEvent;
	private final KeyEvent keyEvent;
	private final boolean isEditable;
	
	/**
	 * @param manager
	 * @param figure
	 * @param isEditable
	 */
	public SelectableInteractionEvent(ActionModeManager<?,?,?> manager, Collection<? extends Selectable> figure, boolean isEditable) {
		super(manager);
		this.figures = figure==null ? Collections.<Selectable>emptyList() : figure;
		this.pointerEvent = null;
		this.keyEvent = null;
		this.isEditable = isEditable;
	}

	/**
	 * @param manager
	 * @param figure
	 * @param pointerEvent
	 * @param isEditable
	 */
	public SelectableInteractionEvent(ActionModeManager<?,?,?> manager, Collection<? extends Selectable> figure, ActionPointerEvent pointerEvent, boolean isEditable) {
		super(manager);
		this.figures = figure==null ? Collections.<Selectable>emptyList() : figure;
		this.pointerEvent = pointerEvent;
		this.keyEvent = null;
		this.isEditable = isEditable;
	}

	/**
	 * @param manager
	 * @param figure
	 * @param keyEvent
	 * @param isEditable
	 */
	public SelectableInteractionEvent(ActionModeManager<?,?,?> manager, Collection<? extends Selectable> figure, KeyEvent keyEvent, boolean isEditable) {
		super(manager);
		this.figures = figure==null ? Collections.<Selectable>emptyList() : figure;
		this.pointerEvent = null;
		this.keyEvent = keyEvent;
		this.isEditable = isEditable;
	}

	/** Replies if the editor on which the interaction occured enables the edition
	 * of the figures.
	 * 
	 * @return <code>true</code> if the viewer is editable; otherwise <code>false</code>.
	 */
	public boolean isEditable() {
		return this.isEditable;
	}
	
	/**
	 * Replies the figure on which the event occured.
	 * 
	 * @return the figures, never <code>null</code>.
	 * @see #getSelectable()
	 */
	public Collection<? extends Selectable> getSelectables() {
		return Collections.unmodifiableCollection(this.figures);
	}
	
	/**
	 * Replies one of the figures on which the event occured.
	 * The method of selection depends on the implementation.
	 * There is no warranty that the replied figure is at 
	 * a given position in the collection replied by {@link #getSelectables()}.
	 * 
	 * @return a figure, never <code>null</code>.
	 * @see #getSelectables()
	 */
	public Selectable getSelectable() {
		return this.figures.iterator().next();
	}

	/** Replies the pointer event which is the cause of 
	 * this event.
	 * 
	 * @return the pointer event or <code>null</code> if
	 * there is no pointer event causing this interaction event.
	 */
	public ActionPointerEvent getPointerEvent() {
		return this.pointerEvent;
	}

	/** Replies the key event which is the cause of 
	 * this event.
	 * 
	 * @return the key event or <code>null</code> if
	 * there is no key event causing this interaction event.
	 */
	public KeyEvent getKeyEvent() {
		return this.keyEvent;
	}
	
	/**
     * Consumes this event so that it will not be processed
     * in the default manner by the source which originated it.
     */
    public void consume() {
        this.consumed = true;
        if (this.pointerEvent!=null)
        	this.pointerEvent.consume();
        if (this.keyEvent!=null)
        	this.keyEvent.consume();
    }

    /**
     * Returns whether or not this event has been consumed.
     * @return <code>true</code> if the event has been marked as consumed;
     * otherwise <code>false</code>.
     */
    public boolean isConsumed() {
        return this.consumed;
    }
    
}
