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

package org.arakhne.afc.ui.undo ;

import java.util.ArrayList;

/** A group of undoable actions.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
public class UndoableGroup implements Undoable {

	private static final long serialVersionUID = 515680393060970792L;
	
	private final ArrayList<Undoable> undoables = new ArrayList<>();
	private final String presentationName;
	private boolean died = false;
	private boolean finalized = false;
	
	/**
	 * @param presentationName
	 */
	public UndoableGroup(String presentationName) {
		this.presentationName = presentationName;
	}
	
    /**
     * Adds an <code>Undoable</code> to this
     * <code>UndoManager</code>, if it's possible.
     * You must call {@link #end()} after all the
     * actions where added.
     *
     * @param action is the action to be added
     */
    public void add(Undoable action) {
    	if (action!=null) this.undoables.add(action);
    }
    
    /** Replies if this group of undoable actions is empty or not.
     * 
     * @return <code>true</code> if there is no action in the group.
     */
    public boolean isEmpty() {
    	return this.undoables.isEmpty();
    }

    /** Replies the number of undoable actions in the group.
     * 
     * @return the number of undoable actions in the group.
     */
    public int size() {
    	return this.undoables.size();
    }

    /**
     * Finalize the additions in the group.
     */
    public void end() {
    	if (!this.finalized) {
	    	this.undoables.trimToSize();
	    	this.finalized = true;
	    	for(Undoable u : this.undoables) {
	    		if (u instanceof UndoableGroup) {
	    			((UndoableGroup)u).end();
	    		}
	    	}
    	}
    }
    
	@Override
	public void undo() {
		if (!this.finalized) throw new IllegalStateException("you must invoked end() before calling undo()"); //$NON-NLS-1$
		if (this.died || this.undoables.isEmpty()) throw new IllegalStateException();
		Undoable u;
		for(int i=this.undoables.size()-1; i>=0; --i) {
			u = this.undoables.get(i);
			u.undo();
		}
	}

	@Override
	public void redo() {
		if (!this.finalized) throw new IllegalStateException("you must invoked end() before calling redo()"); //$NON-NLS-1$
		if (this.died || this.undoables.isEmpty()) throw new IllegalStateException();
		for(Undoable u : this.undoables) {
			u.redo();
		}
	}

	@Override
	public void die() {
		this.died = true;
		Undoable u;
		for(int i=this.undoables.size()-1; i>=0; --i) {
			u = this.undoables.get(i);
			u.die();
		}
		this.undoables.clear();
	}

	@Override
	public String getPresentationName() {
		return this.presentationName;
	}
	
}
