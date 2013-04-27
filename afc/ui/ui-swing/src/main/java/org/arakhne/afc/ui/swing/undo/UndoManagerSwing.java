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
package org.arakhne.afc.ui.swing.undo;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

import org.arakhne.afc.ui.undo.UndoListener;
import org.arakhne.afc.ui.undo.Undoable;
import org.arakhne.afc.util.ListenerCollection;


/** Implementation of a {@link org.arakhne.afc.ui.undo.UndoManager} based on the
 * standard Swing {@link UndoManager}.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class UndoManagerSwing extends UndoManager implements org.arakhne.afc.ui.undo.UndoManager {

	private static final long serialVersionUID = 4936381853383834466L;

	private final ListenerCollection<UndoListener> listeners = new ListenerCollection<UndoListener>();

	/**
	 */
	public UndoManagerSwing() {
		super();
	}

	@Override
	public void addUndoListener(UndoListener l) {
		this.listeners.add(UndoListener.class, l);
	}

	@Override
	public void removeUndoListener(UndoListener l) {
		this.listeners.remove(UndoListener.class, l);
	}

	/**
	 * Notifies listeners.
	 */
	protected void fireChange() {
		for(UndoListener listener : this.listeners.getListeners(UndoListener.class)) {
			listener.undoListChanged(this);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void redoTo(UndoableEdit edit) throws CannotRedoException {
		super.redoTo(edit);
		fireChange();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void undoTo(UndoableEdit edit) throws CannotUndoException {
		super.undoTo(edit);
		fireChange();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void undo() throws CannotUndoException {
		boolean inProgress = isInProgress();
		super.undo();
		if(inProgress) fireChange();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void redo() throws CannotRedoException {
		boolean inProgress = isInProgress();
		super.redo();
		if(!inProgress) fireChange();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized boolean addEdit(UndoableEdit anEdit) {
		if (anEdit==null) return false;
		boolean retVal = super.addEdit(anEdit);
		if (retVal) fireChange();
		return retVal;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void discardAllEdits() {
		boolean fire = !this.edits.isEmpty();
		super.discardAllEdits();
		if (fire) fireChange();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void end() {
		super.end();
		fireChange();
	}

	@Override
	public final boolean add(Undoable action) {
		if (action==null) return false;
		if (action instanceof UndoableEdit) {
			return addEdit((UndoableEdit)action);
		}
		return addEdit(new UndoableWrapper(action));
	}

	@Override
	public final void discardAll() {
		discardAllEdits();
	}

	/**
	 * @author $Author: galland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class UndoableWrapper extends AbstractCallableUndoableEdit {
		
		private static final long serialVersionUID = -8903679029751177352L;
		
		private final Undoable undoable;
		
		/**
		 * @param undoable
		 */
		public UndoableWrapper(Undoable undoable) {
			this.undoable = undoable;
		}
		
		@Override
		public void doEdit() {
			this.undoable.redo();
		}

		@Override
		public void undoEdit() {
			this.undoable.undo();
		}
		
		@Override
		public String getPresentationName() {
			return this.undoable.getPresentationName();
		}
		
		@Override
		public void die() {
			this.undoable.die();
			super.die();
		}
		
	}

}
