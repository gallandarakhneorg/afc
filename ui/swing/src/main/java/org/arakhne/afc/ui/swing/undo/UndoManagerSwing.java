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
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
public class UndoManagerSwing extends UndoManager implements org.arakhne.afc.ui.undo.UndoManager {

	private static final long serialVersionUID = 4936381853383834466L;

	private final ListenerCollection<UndoListener> listeners = new ListenerCollection<>();

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
	 * @author $Author: sgalland$
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
