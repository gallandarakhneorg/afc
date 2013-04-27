/* 
 * $Id$
 * 
 * Copyright (C) 2013 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
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


/** Manager of undoable actions.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface UndoManager {

	/** Add listener on the changes in the undo list.
	 * 
	 * @param l
	 */
	public void addUndoListener(UndoListener l);
	
	/** Remove listener on the changes in the undo list.
	 * 
	 * @param l
	 */
	public void removeUndoListener(UndoListener l);

	/**
     * Undoes the appropriate actions.
     * @see #canUndo()
     */
    public void undo();

    /**
     * Returns <code>true</code> if actions may be undone.
     * 
     * @return <code>true</code> if there are actions to be undone.
     */
    public boolean canUndo();

    /**
     * Redoes the appropriate actions.
     * @see #canRedo()
     */
    public void redo();

    /**
     * Returns <code>true</code> if actions may be redone.
     * 
     * @return <code>true</code> if there are actions to be redone.
     */
    public boolean canRedo();

    /**
     * Returns a description of the undoable form of this edit.
     *
     * @return a description of the undoable form of this edit
     */
    public String getUndoPresentationName();
    /**
     * Returns a description of the redoable form of this edit.
     * 
     * @return a description of the redoable form of this edit
     */
    public String getRedoPresentationName();

    /**
     * Adds an <code>Undoable</code> to this
     * <code>UndoManager</code>, if it's possible.  This removes all
     * edits from the index of the next edit to the end of the edits
     * list.  If <code>end</code> has been invoked the edit is not added
     * and <code>false</code> is returned.  If <code>end</code> hasn't
     * been invoked this returns <code>true</code>.
     *
     * @param action is the action to be added
     * @return true if <code>action</code> can be incorporated into this
     *              edit
     */
    public boolean add(Undoable action);

    /**
     * Sets the maximum number of action this <code>UndoManager</code>
     * holds. A value less than 0 indicates the number of edits is not
     * limited.
     *
     * @param l the new limit
     */
    public void setLimit(int l);

    /**
     * Replies the maximum number of action this <code>UndoManager</code>
     * holds. A value less than 0 indicates the number of edits is not
     * limited.
     *
     * @return the limit.
     */
    public int getLimit();

    /**
     * Empties the undo manager sending each edit a <code>die</code> message
     * in the process.
     *
     * @see Undoable#die()
     */
    public void discardAll();
    	
}