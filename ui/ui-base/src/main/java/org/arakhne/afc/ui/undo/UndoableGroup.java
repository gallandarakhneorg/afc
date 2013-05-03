/* 
 * $Id$
 * 
 * Copyright (C) 2013 Stephane GALLAND.
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
package org.arakhne.afc.ui.undo ;

import java.util.ArrayList;

/** A group of undoable actions.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class UndoableGroup implements Undoable {

	private static final long serialVersionUID = 515680393060970792L;
	
	private final ArrayList<Undoable> undoables = new ArrayList<Undoable>();
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
