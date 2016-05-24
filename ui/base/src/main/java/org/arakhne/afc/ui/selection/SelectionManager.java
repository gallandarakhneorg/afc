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

package org.arakhne.afc.ui.selection ;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

import org.arakhne.afc.util.ListenerCollection;

/** Abstracxt implementation of a selection manager.
 *
 * @param <OBJ> is the type of the objects inside this manager.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
public abstract class SelectionManager<OBJ extends Selectable> implements Set<OBJ> {

	private final ListenerCollection<SelectionListener> listeners = new ListenerCollection<>();

	private final Class<OBJ> elementType;
	
	/** Create a new SelectionManager.
	 * 
	 * @param elementType is the type of the elements inside this selection manager.
	 */
	public SelectionManager(Class<OBJ> elementType) {
		this.elementType = elementType;
	}
	
	/** Update the system's selection.
	 */
	protected void updateSystemSelection() {
		//
	}

	/** Reset any internal bufferized value.
	 */
	protected void resetInternalBuffers() {
		//
	}

	/** Add the given object inside the inner storage data structure.
	 * 
	 * @param object
	 * @return <code>true</code> if the object was added; <code>false</code>
	 * if not added.
	 */
	protected abstract boolean addInStorage(OBJ object);

	/** Remove the given object from the inner storage data structure.
	 * 
	 * @param object
	 * @return <code>true</code> if the object was removed; <code>false</code>
	 * if not removed.
	 */
	protected abstract boolean removeFromStorage(OBJ object);

	/** Clear the storage
	 * 
	 * @return the removed objects.
	 */
	protected abstract Collection<OBJ> clearStorage();

	/** Replies an iterator on the storage.
	 * 
	 * @return the iterator.
	 */
	protected abstract Iterator<OBJ> getIteratorOnStorage();

	/** Invoked when the given object was removed from the inner storage.
	 * 
	 * @param object
	 */
	protected void onRemovedObject(OBJ object) {
		//
	}

	/** Invoked when the given object was added into the inner storage.
	 * 
	 * @param object
	 */
	protected void onAddedObject(OBJ object) {
		//
	}

	/** Add selection listener.
	 * 
	 * @param listener
	 */
	public final void addSelectionListener(SelectionListener listener) {
		this.listeners.add(SelectionListener.class, listener);
	}

	/** Remove selection listener.
	 * 
	 * @param listener
	 */
	public final void removeSelectionListener(SelectionListener listener) {
		this.listeners.remove(SelectionListener.class, listener);
	}

	/** Notifies the listeners about the selection of a selectable object.
	 * 
	 * @param selectableObject is the selected object.
	 * @param isAdjusting indicates if the event to fire is the last inside
	 * a sequence of events. If <code>true</code> the event to fire must
	 * be followed by other selection events that are produces by the
	 * same action on the selection manager. If <code>false</code>, there
	 * is no following selection event for the same action on the selection
	 * manager.
	 */
	protected final void fireSelected(OBJ selectableObject, boolean isAdjusting) {
		SelectionEvent event = new SelectionEvent(this, selectableObject, false, isAdjusting);
		for(SelectionListener listener : this.listeners.getListeners(SelectionListener.class)) {
			listener.selectionChanged(event);
		}
	}

	/** Notifies the listeners about the deselection of a selectable object.
	 * 
	 * @param selectableObject is the unselected object.
	 * @param isAdjusting indicates if the event to fire is the last inside
	 * a sequence of events. If <code>true</code> the event to fire must
	 * be followed by other selection events that are produces by the
	 * same action on the selection manager. If <code>false</code>, there
	 * is no following selection event for the same action on the selection
	 * manager.
	 */
	protected final void fireUnselected(OBJ selectableObject, boolean isAdjusting) {
		SelectionEvent event = new SelectionEvent(this, selectableObject, true, isAdjusting);
		for(SelectionListener listener : this.listeners.getListeners(SelectionListener.class)) {
			listener.selectionChanged(event);
		}
	}

	/** Toggle the selection of figures.
	 *
	 * @param selectableObject are the figures to toggle.
	 * @return <code>true</code> if the selection of a selectable object
	 * has changed; <code>false</code> if no object has changed
	 * of selection state.
	 */
	@SuppressWarnings("unchecked")
	public final boolean toggle(OBJ... selectableObject) {
		return toggle(Arrays.asList(selectableObject));
	}
	
	/** Toggle the selection of figures.
	 *
	 * @param selectableObjects are the figures to toggle.
	 * @return <code>true</code> if the selection of a selectable object
	 * has changed; <code>false</code> if no object has changed
	 * of selection state.
	 */
	public synchronized final boolean toggle(Collection<? extends OBJ> selectableObjects) {
		boolean changed = false;
		OBJ selected = null;
		OBJ unselected = null;
		for(OBJ f : selectableObjects) {
			if (removeFromStorage(f)) {
				if (selected!=null) {
					fireSelected(selected, true);
					selected = null;
				}
				else if (unselected!=null) {
					fireUnselected(unselected, true);
					unselected = null;
				}
				onRemovedObject(f);
				changed = true;
				resetInternalBuffers();
				unselected = f;
			}
			else if (f.isSelectable() && addInStorage(f)) {
				if (selected!=null) {
					fireSelected(selected, true);
					selected = null;
				}
				else if (unselected!=null) {
					fireUnselected(unselected, true);
					unselected = null;
				}
				onAddedObject(f);
				changed = true;
				resetInternalBuffers();
				selected = f;
			}
		}
		if (selected!=null) {
			fireSelected(selected, false);
		}
		else if (unselected!=null) {
			fireUnselected(unselected, false);
		}
		if (changed) {
			updateSystemSelection();
		}
		return changed;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized final boolean add(OBJ e) {
		if (e.isSelectable() && addInStorage(e)) {
			onAddedObject(e);
			resetInternalBuffers();
			fireSelected(e, false);
			updateSystemSelection();
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized final boolean remove(Object o) {
		if (o!=null && this.elementType.isInstance(o)) {
			OBJ obj = this.elementType.cast(o);
			if (removeFromStorage(obj)) {
				onRemovedObject(obj);
				resetInternalBuffers();
				fireUnselected(obj, false);
				updateSystemSelection();
				return true;
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized final boolean addAll(Collection<? extends OBJ> c) {
		boolean changed = false;
		if (c!=null) {
			OBJ selected = null;
			for(OBJ obj : c) {
				if (obj.isSelectable() && addInStorage(obj)) {
					if (selected!=null) {
						fireSelected(selected, true);
					}
					onAddedObject(obj);
					resetInternalBuffers();
					changed = true;
					selected = obj;
				}
			}
			if (selected!=null) {
				fireSelected(selected, false);
			}
		}
		if (changed) {
			updateSystemSelection();
		}
		return changed;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized final boolean retainAll(Collection<?> c) {
		boolean changed = false;
		if (c==null || c.isEmpty()) {
			changed = !isEmpty();
			clear();
		}
		else {
			OBJ unselected = null;
			Iterator<OBJ> iterator = getIteratorOnStorage();
			OBJ selObject;
			while (iterator.hasNext()) {
				selObject = iterator.next();
				if (!c.contains(selObject)) {
					if (unselected!=null) {
						fireUnselected(unselected, true);
					}
					iterator.remove();
					onRemovedObject(selObject);
					changed = true;
					resetInternalBuffers();
					unselected = selObject;
				}
			}
			if (unselected!=null) {
				fireUnselected(unselected, false);
			}
		}
		if (changed) {
			updateSystemSelection();
		}
		return changed;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized final boolean removeAll(Collection<?> c) {
		boolean changed = false;
		if (c!=null) {
			OBJ unselected = null;
			for(Object o : c) {
				if (o!=null && this.elementType.isInstance(o)) {
					OBJ obj = this.elementType.cast(o);
					if (removeFromStorage(obj)) {
						if (unselected!=null) {
							fireUnselected(unselected, true);
						}
						onRemovedObject(obj);
						resetInternalBuffers();
						changed = true;
						unselected = obj;
					}
				}
			}
			if (unselected!=null) {
				fireUnselected(unselected, false);
			}
		}
		if (changed) {
			updateSystemSelection();
		}
		return changed;
	}

	/**
	 * Select the specified selectable object and deselect all the previously
	 * selected objects.
	 * 
	 * @param e is the selectable object to select.
	 * @return <code>true</code> if the selection has changed;
	 * otherwise <code>false</code>.
	 */
	@SuppressWarnings("unchecked")
	public final boolean setSelection(OBJ... e) {
		return setSelection(Arrays.asList(e));
	}

	/**
	 * Select the specified selectable object and deselect all the previously
	 * selected objects.
	 * 
	 * @param e is the selectable object to select.
	 * @return <code>true</code> if the selection has changed;
	 * otherwise <code>false</code>.
	 */
	public synchronized final boolean setSelection(Collection<? extends OBJ> e) {
		boolean changed = false;

		Set<OBJ> alreadySelected = new TreeSet<>();
		OBJ unselected = null;
		OBJ selected = null;

		Iterator<OBJ> iterator = getIteratorOnStorage();
		OBJ selObject;
		while (iterator.hasNext()) {
			selObject = iterator.next();
			if (!e.contains(selObject)) {
				if (unselected!=null) {
					fireUnselected(unselected, true);
				}
				iterator.remove();
				onRemovedObject(selObject);
				changed = true;
				resetInternalBuffers();
				unselected = selObject;
			}
			else {
				alreadySelected.add(selObject);
			}
		}

		for(OBJ fig : e) {
			if (fig.isSelectable() && 
					!alreadySelected.contains(fig)
					&& addInStorage(fig)) {
				if (selected!=null) {
					fireSelected(selected, true);
				}
				else if (unselected!=null) {
					fireUnselected(unselected, true);
					unselected = null;
				}
				onAddedObject(fig);
				changed = true;
				resetInternalBuffers();
				selected = fig;
			}
		}
		
		if (selected!=null) {
			fireSelected(selected, false);
		}
		else if (unselected!=null) {
			fireUnselected(unselected, false);
		}

		if (changed) {
			updateSystemSelection();
		}

		return changed;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized final Iterator<OBJ> iterator() {
		return new SelectionIterator(getIteratorOnStorage());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized final void clear() {
		Collection<OBJ> elements = clearStorage();
		if (!elements.isEmpty()) {
			resetInternalBuffers();
			updateSystemSelection();
			Iterator<OBJ> iterator = elements.iterator();
			OBJ obj;
			while (iterator.hasNext()) {
				obj = iterator.next();
				onRemovedObject(obj);
				resetInternalBuffers();
				fireUnselected(obj, iterator.hasNext());
			}
		}
	}

	/** 
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class SelectionIterator implements Iterator<OBJ> {

		private final Iterator<OBJ> iterator;

		private OBJ lastObject = null;

		public SelectionIterator(Iterator<OBJ> iterator) {
			this.iterator = iterator;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNext() {
			synchronized(SelectionManager.this) {
				return this.iterator.hasNext();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public OBJ next() {
			synchronized(SelectionManager.this) {
				this.lastObject = this.iterator.next();
			}
			return this.lastObject;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void remove() {
			OBJ obj = this.lastObject;
			this.lastObject = null;
			if (obj==null) throw new NoSuchElementException();
			synchronized(SelectionManager.this) {
				this.iterator.remove();
			}
			onRemovedObject(obj);
			resetInternalBuffers();
			fireUnselected(obj, false);
			updateSystemSelection();
		}

	} // class SelectionIterator

}
