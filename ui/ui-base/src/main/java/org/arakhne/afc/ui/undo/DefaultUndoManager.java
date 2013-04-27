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

import java.util.Iterator;
import java.util.LinkedList;

import org.arakhne.afc.util.ListenerCollection;
import org.arakhne.vmutil.locale.Locale;

/** Manager of undoable actions.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class DefaultUndoManager implements UndoManager {

	private final ListenerCollection<UndoListener> listeners = new ListenerCollection<UndoListener>();
	private final LinkedList<Undoable> undoes = new LinkedList<Undoable>(); 
	private final LinkedList<Undoable> redoes = new LinkedList<Undoable>(); 
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
