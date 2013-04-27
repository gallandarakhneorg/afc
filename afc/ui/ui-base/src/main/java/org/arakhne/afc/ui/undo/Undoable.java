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

import java.io.Serializable;



/** Action that can be undoed.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Undoable extends Serializable {

	/** Redo the action.
	 */
	public void redo();

	/** Undo the action.
	 */
	public void undo();

	/** Remove any buffered data.
	 * This function is invoked by the undo manager
	 * when this action is removed from the manager.
	 */
	public void die();

    /**
     * This default implementation returns "". Used by
     * <code>getUndoPresentationName</code> and
     * <code>getRedoPresentationName</code> to
     * construct the strings they return. Subclasses should override to
     * return an appropriate description of the operation this edit
     * represents.
     *
     * @return the empty string ""
     * @see     UndoManager#getUndoPresentationName()
     * @see     UndoManager#getRedoPresentationName()
     */
    public String getPresentationName();

}
