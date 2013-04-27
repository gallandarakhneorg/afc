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

package org.arakhne.afc.ui.swing.undo;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;

import org.arakhne.afc.ui.undo.Undoable;

/** Abstract implementation of an undoable edit that is also
 * directly callable to run/redo the action.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractCallableUndoableEdit extends AbstractUndoableEdit implements Undoable {

	private static final long serialVersionUID = -1330907958341724667L;

	/**
	 */
	public AbstractCallableUndoableEdit() {
		//
	}
	
	@Override
	public final void redo() throws CannotRedoException {
		super.redo();
		doEdit();
	}

	@Override
	public final void undo() throws CannotRedoException {
		super.undo();
		undoEdit();
	}
	
	/** Do the edition.
	 * This function is automatically invoked by {@link #redo()}.
	 * In opposite to {@link #redo()}, this function does not
	 * throw an exception when the edit was never done before.
	 */
	public abstract void doEdit();

	/** Undo the edition.
	 * This function is automatically invoked by {@link #undo()}.
	 * In opposite to {@link #undo()}, this function does not
	 * throw an exception when the edit was never done before.
	 */
	public abstract void undoEdit();

}
