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


/** Manager of undoable actions.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
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