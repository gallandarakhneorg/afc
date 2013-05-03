/* 
 * $Id$
 * 
 * Copyright (C) 2002 Stephane GALLAND, Madhi HANNOUN, Marc BAUMGARTNER.
 * Copyright (C) 2005-10 Stephane GALLAND.
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

package org.arakhne.afc.ui.swing.undo;

import javax.swing.undo.CompoundEdit;

import org.arakhne.afc.ui.undo.Undoable;

/** Abstract implementation of an undoable edit that is also
 * directly callable to run/redo the action.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class UndoableGroupSwing extends CompoundEdit implements Undoable {

	private static final long serialVersionUID = -5363974010663738749L;
	
	private final String label;
	
	/**
	 * @param label
     */
    public UndoableGroupSwing(String label) {
        super();
        this.label = label;
    }
    
    @Override
    public String getPresentationName() {
    	return this.label;
    }

}
