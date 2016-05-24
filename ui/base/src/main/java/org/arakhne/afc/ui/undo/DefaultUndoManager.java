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

import java.util.Iterator;
import java.util.LinkedList;

import org.arakhne.afc.util.ListenerCollection;
import org.arakhne.afc.vmutil.locale.Locale;

/** Manager of undoable actions.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
public class DefaultUndoManager implements UndoManager {

	private final ListenerCollection<UndoListener> listeners = new ListenerCollection<>();
	private final LinkedList<Undoable> undoes = new LinkedList<>(); 
	private final LinkedList<Undoable> redoes = new LinkedList<>(); 
	private int limit = 100;
	
	/**
	 */
	public DefaultUndoManager() {
		//
	}

	@Override
	public synchronized final void setLimit(int l) {
		if (l!=this.limit) {
			this.limit = l;
		}
	}

	@Override
	public synchronized final int getLimit() {
		return this.limit;
	}


	@Override
	public void addUndoListener(UndoListener l) {
		this.listeners.add(UndoListener.class,l);
	}

	@Override
	public void removeUndoListener(UndoListener l) {
		this.listeners.remove(UndoListener.class,l);
	}

	/**
	 * Notifies listeners.
	 */
	protected void fireChange() {
		for(UndoListener listener : this.listeners.getListeners(UndoListener.class)) {
			listener.undoListChanged(this);
		}
	}
	
	@Override
	public boolean add(Undoable action) {
		synchronized(this) {
			if (action==null || this.limit==0) return false;
			Iterator<Undoable> iterator;
			Undoable u;
			
			// Clear the list of the redoable actions
			iterator = this.redoes.iterator();
			while (iterator.hasNext()) {
				u = iterator.next();
				u.die();
				iterator.remove();
			}
	
			// Remove the too old undoable actions
			if (this.limit>0) {
				while (this.undoes.size()>=this.limit) {
					u = this.undoes.removeFirst();
					u.die();
				}
			}
			
			// Add the new action.
			this.undoes.addLast(action);
			if (action instanceof UndoableGroup) {
				((UndoableGroup)action).end();
			}
		}
		
		fireChange();

		return true;
	}

	@Override
	public synchronized void undo() {
		synchronized(this) {
			if (this.undoes.isEmpty())
				throw new IllegalStateException();
			Undoable u = this.undoes.removeLast();
			u.undo();
			this.redoes.addFirst(u);
		}
		fireChange();
	}

	@Override
	public synchronized boolean canUndo() {
		return !this.undoes.isEmpty();
	}

	@Override
	public void redo() {
		synchronized(this) {
			if (this.redoes.isEmpty())
				throw new IllegalStateException();
			Undoable u = this.redoes.removeFirst();
			u.redo();
			this.undoes.addLast(u);
		}
		fireChange();
	}

	@Override
	public synchronized boolean canRedo() {
		return !this.redoes.isEmpty();
	}

	@Override
	public synchronized void discardAll() {
		boolean changed = false;
		synchronized(this) {
			Iterator<Undoable> iterator;
			Undoable u;
			
			iterator = this.redoes.iterator();
			while (iterator.hasNext()) {
				u = iterator.next();
				u.die();
				iterator.remove();
				changed = true;
			}

			iterator = this.undoes.iterator();
			while (iterator.hasNext()) {
				u = iterator.next();
				u.die();
				iterator.remove();
				changed = false;
			}
		}
		
		if (changed) {
			fireChange();
		}
	}
	
	@Override
	public synchronized String getUndoPresentationName() {
		if (canUndo())
			return Locale.getString("UNDO", this.undoes.getLast().getPresentationName()); //$NON-NLS-1$
		return Locale.getString("NO_UNDO"); //$NON-NLS-1$
	}

	@Override
	public synchronized String getRedoPresentationName() {
		if (canRedo())
			return Locale.getString("REDO", this.redoes.getFirst().getPresentationName()); //$NON-NLS-1$
		return Locale.getString("NO_REDO"); //$NON-NLS-1$
	}

}
