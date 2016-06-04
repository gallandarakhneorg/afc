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

package org.arakhne.afc.math.tree.node;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.tree.TreeDataEvent;
import org.arakhne.afc.math.tree.TreeNode;
import org.arakhne.afc.math.tree.TreeNodeListener;


/**
 * This is the very generic implementation of a tree node which
 * does not made any implementation choice about how this
 * node is related to its parent node.
 *
 * @param <D> is the type of the data inside the tree
 * @param <N> is the type of the tree nodes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public abstract class AbstractParentlessTreeNode<D, N extends AbstractParentlessTreeNode<D, N>>
		implements TreeNode<D, N>, Serializable {

	/** By default, does tree nodes use a linked list or not.
	 */
	public static final boolean DEFAULT_LINK_LIST_USE = false;

	/** By default, does the user data are copied or not.
	 */
	public static final boolean DEFAULT_COPY_USER_DATA = true;

	private static final long serialVersionUID = 2525868013637426514L;

	/** List of the event listeners on the tree.
	 */
	protected transient List<TreeNodeListener> nodeListeners;

	/** Temporary count of child nodes that are not null.
	 *
	 * @see #getNotNullChildCount()
	 */
	protected int notNullChildCount;

	/** Associated user data.
	 */
	private List<D> data;

	/** Indicates if a linked list must be used to store the data.
	 * If <code>false</code>, an ArrayList will be used.
	 */
	private final boolean linkedList;

	/** Construct node.
	 * @param useLinkedList indicates if a linked list must be used to store the data.
	 *     If <code>false</code>, an ArrayList will be used.
	 */
	public AbstractParentlessTreeNode(boolean useLinkedList) {
		this.linkedList = useLinkedList;
		if (this.linkedList) {
			this.data = new LinkedList<>();
		} else {
			this.data = new ArrayList<>();
		}
	}

	/** Construct node.
	 * @param useLinkedList indicates if a linked list must be used to store the data.
	 *     If <code>false</code>, an ArrayList will be used.
	 * @param copyDataCollection indicates if the given data collection is copied
	 *     if <code>true</code> or the inner data collection will be the given
	 *     collection itself if <code>false</code>.
	 * @param idata are the initial user data
	 */
	public AbstractParentlessTreeNode(boolean useLinkedList, boolean copyDataCollection, List<D> idata) {
		this.linkedList = useLinkedList;
		if (idata != null) {
			if (!copyDataCollection) {
				this.data = idata;
			} else {
				if (this.linkedList) {
					this.data = new LinkedList<>(idata);
				} else {
					this.data = new ArrayList<>(idata);
				}
			}
		} else {
			if (this.linkedList) {
				this.data = new LinkedList<>();
			} else {
				this.data = new ArrayList<>();
			}
		}
	}

	/** Construct node.
	 * @param useLinkedList indicates if a linked list must be used to store the data.
	 *     If <code>false</code>, an ArrayList will be used.
	 * @param idata are the initial user data
	 */
	public AbstractParentlessTreeNode(boolean useLinkedList, Collection<D> idata) {
		this.linkedList = useLinkedList;
		if (idata != null) {
			if (this.linkedList) {
				this.data = new LinkedList<>(idata);
			} else {
				this.data = new ArrayList<>(idata);
			}
		} else {
			if (this.linkedList) {
				this.data = new LinkedList<>();
			} else {
				this.data = new ArrayList<>();
			}
		}
	}

	/** Construct node.
	 * @param useLinkedList indicates if a linked list must be used to store the data.
	 *     If <code>false</code>, an ArrayList will be used.
	 * @param idata are the initial user data
	 */
	public AbstractParentlessTreeNode(boolean useLinkedList, D idata) {
		this.linkedList = useLinkedList;
		if (idata != null) {
			if (this.linkedList) {
				this.data = new LinkedList<>();
			} else {
				this.data = new ArrayList<>();
			}
			this.data.add(idata);
		} else {
			if (this.linkedList) {
				this.data = new LinkedList<>();
			} else {
				this.data = new ArrayList<>();
			}
		}
	}

	/** Replies the minimal value from the integer array.
	 *
	 * @param array the array.
	 * @return the minimum value.
	 * @deprecated see {@link MathUtil#min(int...)}
	 */
	@Deprecated
	protected static int minInteger(int... array) {
		int min = array[0];
		for (int i = 0; i < array.length; ++i) {
			if (min > array[i]) {
				min = array[i];
			}
		}
		return min;
	}

	/** Replies the maximal value from the integer array.
	 *
	 * @param array the array.
	 * @return the maximum value.
	 * @deprecated see {@link MathUtil#max(int...)}
	 */
	@Deprecated
	protected static int maxInteger(int... array) {
		int max = array[0];
		for (int i = 0; i < array.length; ++i) {
			if (max < array[i]) {
				max = array[i];
			}
		}
		return max;
	}

	/** Cast this node to N.
	 *
	 *  @return <code>this</code>
	 */
	@SuppressWarnings("unchecked")
	@Pure
	public N toN() {
		return (N) this;
	}

	@Override
	@Pure
	public boolean isValid() {
		return true;
	}

	@Override
	@Pure
	public boolean isEmpty() {
		return isLeaf() && (getUserDataCount() == 0);
	}

	@Override
	@Pure
	public String toString() {
		final StringBuilder buffer = new StringBuilder();
		buffer.append('[');
		if (this.data != null) {
			for (final D data : this.data) {
				if (data == null) {
					buffer.append("null"); //$NON-NLS-1$
				} else {
					buffer.append(data.toString());
				}
			}
		}
		buffer.append(']');
		return buffer.toString();
	}

	@Override
	public D[] getAllUserData(D[] array) {
		if (this.data == null) {
			return null;
		}
		return this.data.toArray(array);
	}

	@Override
	@Pure
	public List<D> getAllUserData() {
		return new DataCollection();
	}

	/** Replies the internal data structure which is storing user datas.
	 *
	 * <p>Use with caution because any change applied outside
	 * {@link AbstractParentlessTreeNode} will not be taken into
	 * account (no event...).
	 *
	 * @return the internal data structure
	 */
	@Pure
	protected final List<D> getInternalDataStructureForUserData() {
		return this.data;
	}

	@Override
	@Pure
	public D getUserData() {
		if ((this.data == null) || (this.data.size() == 0)) {
			return null;
		}
		return this.data.get(0);
	}

	@Override
	@Pure
	public int getUserDataCount() {
		return (this.data == null) ? 0 : this.data.size();
	}

	@Override
	@Pure
	public D getUserDataAt(int index) throws IndexOutOfBoundsException {
		if ((this.data == null) || (index < 0) || (index >= this.data.size())) {
			throw new IndexOutOfBoundsException();
		}
		return this.data.get(index);
	}

	@Override
	public boolean addUserData(Collection<? extends D> data) {
		if ((data == null) || (data.size() == 0)) {
			return false;
		}

		if (this.data == null) {
			if (this.linkedList) {
				this.data = new LinkedList<>(data);
			} else {
				this.data = new ArrayList<>(data);
			}
		} else if (!this.data.addAll(data)) {
			return false;
		}

		firePropertyDataChanged(null, data);

		return true;
	}

	@Override
	public final boolean addUserData(int index, Collection<? extends D> data) {
		if ((data == null) || (data.size() == 0)) {
			return false;
		}

		if (this.data == null) {
			if (this.linkedList) {
				this.data = new LinkedList<>();
			} else {
				this.data = new ArrayList<>();
			}
		}

		if (this.data.addAll(index, data)) {
			firePropertyDataChanged(null, data);
			return true;
		}
		return false;
	}

	@Override
	public final boolean addUserData(D data) {
		return addUserData(Collections.singleton(data));
	}

	@Override
	public final void addUserData(int index, D data) {
		addUserData(index, Collections.singleton(data));
	}

	@Override
	public boolean removeUserData(Collection<D> data) {
		if ((data == null) || (data.size() == 0) || (this.data == null)) {
			return false;
		}
		if (this.data.removeAll(data)) {
			if (this.data.size() == 0) {
				this.data = null;
			}
			firePropertyDataChanged(data, null);
			return true;
		}
		return false;
	}

	@Override
	public final D removeUserData(int index) {
		if (this.data == null) {
			throw new IndexOutOfBoundsException();
		}
		final D removedElement = this.data.remove(index);
		if (removedElement != null) {
			if (this.data.size() == 0) {
				this.data = null;
			}
			firePropertyDataChanged(Collections.singleton(removedElement), null);
		}
		return removedElement;
	}

	@Override
	public final boolean removeUserData(D data1) {
		return removeUserData(Collections.singleton(data1));
	}

	@Override
	public void removeAllUserData() {
		if (this.data != null) {
			final List<D> oldData = this.data;
			this.data = null;
			firePropertyDataChanged(oldData, null);
		}
	}

	@Override
	public boolean setUserData(Collection<D> data) {
		if ((data == null) && (this.data == null)) {
			return false;
		}

		final List<D> oldData = this.data;

		if ((data == null) || (data.size() == 0)) {
			this.data = null;
		} else {
			this.data = new ArrayList<>(data);
		}

		firePropertyDataChanged(oldData, data);

		return true;
	}

	@Override
	public final boolean setUserData(D data1) {
		return setUserData(Collections.singleton(data1));
	}

	@Override
	public boolean setUserDataAt(int index, D data) throws IndexOutOfBoundsException {
		final int count = (this.data == null) ? 0 : this.data.size();

		if ((index < 0) || (index > count)) {
			throw new IndexOutOfBoundsException();
		}

		if (data != null) {
			D oldData = null;
			if (index < count) {
				oldData = this.data.get(index);
				this.data.set(index, data);
			} else {
				if (this.data == null) {
					if (this.linkedList) {
						this.data = new LinkedList<>();
					} else {
						this.data = new ArrayList<>();
					}
				}
				this.data.add(data);
			}
			firePropertyDataChanged(oldData == null ? null : Collections.singleton(oldData), Collections.singleton(data));
			return true;
		} else if (index < count) {
			final D oldData = this.data.get(index);
			this.data.remove(index);
			firePropertyDataChanged(oldData == null ? null : Collections.singleton(oldData), null);
			return true;
		}

		return false;
	}

	/** Fire the event for the changes of the user data associated to a node.
	 *
	 * @param oldData is the list of old values
	 * @param newData is the list of new values
	 */
	protected final void firePropertyDataChanged(Collection<? extends D> oldData, Collection<? extends D> newData) {
		firePropertyDataChanged(new TreeDataEvent(this, oldData, newData, this.data));
	}

	/** Fire the event for the changes of the user data associated to a node.
	 *
	 * @param delta is the difference between the size of the data list before change
	 *     and size after change.
	 */
	protected final void firePropertyDataChanged(int delta) {
		firePropertyDataChanged(new TreeDataEvent(this, delta, this.data));
	}

	/** Fire the event for the changes of the user data associated to a node.
	 *
	 * @param event is the event ot fire.
	 */
	void firePropertyDataChanged(TreeDataEvent event) {
		if (this.nodeListeners != null) {
			for (final TreeNodeListener listener : this.nodeListeners) {
				if (listener != null) {
					listener.treeNodeDataChanged(event);
				}
			}
		}
		final N parent = getParentNode();
		if (parent != null) {
			parent.firePropertyDataChanged(event);
		}
	}

	@Override
	public final void addTreeNodeListener(TreeNodeListener listener) {
		if (this.nodeListeners == null) {
			this.nodeListeners = new ArrayList<>();
		}
		this.nodeListeners.add(listener);
	}

	@Override
	public final void removeTreeNodeListener(TreeNodeListener listener) {
		if (this.nodeListeners == null) {
			return;
		}
		this.nodeListeners.remove(listener);
		if (this.nodeListeners.isEmpty()) {
			this.nodeListeners = null;
		}
	}

	@Override
	@Pure
    public int compareTo(N obj) {
    	return obj.hashCode() - hashCode();
    }

	@Override
	@Pure
	public int getDeepNodeCount() {
		int count = 1;
		final int childCount = getChildCount();
		for (int index = 0; index < childCount; ++index) {
			final N child = getChildAt(index);
			if (child != null) {
				count += child.getDeepNodeCount();
			}
		}
		return count;
	}

	@Override
	@Pure
	public int getDeepUserDataCount() {
		int count = getUserDataCount();
		final int childCount = getChildCount();
		for (int index = 0; index < childCount; ++index) {
			final N child = getChildAt(index);
			if (child != null) {
				count += child.getDeepUserDataCount();
			}
		}
		return count;
	}

	@Override
	@Pure
	public final int[] getHeights() {
		final List<Integer> list = new ArrayList<>();
		getHeights(1, list);
		final int[] heights = new int[list.size()];
		int idx = 0;
		for (final Integer intObject : list) {
			heights[idx] = intObject.intValue();
			++idx;
		}
		list.clear();
		return heights;
	}

	/** Replies the heights of all the leaf nodes.
	 * The order of the heights is given by a depth-first iteration.
	 *
	 * @param currentHeight is the current height of this node.
	 * @param heights is the list of heights to fill
	 */
	protected abstract void getHeights(int currentHeight, List<Integer> heights);

	/**
	 * This collection permits to store the data of a tree node
	 * and notify the tree node when the collection changed.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	private class DataCollection implements List<D>, Serializable {

		private static final long serialVersionUID = -8019401625554108057L;

		private final List<D> backgroundList;

		/** Construct collection.
		 * @param list the original list.
		 */
		DataCollection(List<D> list) {
			this.backgroundList = list;
		}

		/** Construct collection.
		 */
		DataCollection() {
			this(null);
		}

		@Override
		public boolean add(D element) {
			if (this.backgroundList == null) {
				return addUserData(element);
			}
			if (this.backgroundList.add(element)) {
				firePropertyDataChanged(null, Collections.singleton(element));
				return true;
			}
			return false;
		}

		@Override
		public void add(int index, D element) {
			if (this.backgroundList == null) {
				addUserData(index, element);
			} else {
				this.backgroundList.add(index, element);
				firePropertyDataChanged(null, Collections.singleton(element));
			}
		}

		@Override
		public boolean addAll(Collection<? extends D> collection) {
			if (this.backgroundList == null) {
				return addUserData(collection);
			}
			if (this.backgroundList.addAll(collection)) {
				firePropertyDataChanged(null, collection);
				return true;
			}
			return false;
		}

		@Override
		public boolean addAll(int index, Collection<? extends D> collection) {
			if (this.backgroundList == null) {
				return addUserData(index, collection);
			}
			if (this.backgroundList.addAll(index, collection)) {
				firePropertyDataChanged(null, collection);
				return true;
			}
			return false;
		}

		@Override
		public void clear() {
			if (this.backgroundList == null) {
				removeAllUserData();
			} else if (!this.backgroundList.isEmpty()) {
				final List<D> removed = new ArrayList<>(this.backgroundList);
				this.backgroundList.clear();
				firePropertyDataChanged(removed, null);
			}
		}

		@Override
		public boolean contains(Object obj) {
			if (this.backgroundList == null) {
				final List<D> backList = getInternalDataStructureForUserData();
				if (backList == null) {
					return false;
				}
				return backList.contains(obj);
			}
			return this.backgroundList.contains(obj);
		}

		@Override
		public boolean containsAll(Collection<?> collection) {
			if (this.backgroundList == null) {
				final List<D> backList = getInternalDataStructureForUserData();
				if (backList == null) {
					return false;
				}
				return backList.containsAll(collection);
			}
			return this.backgroundList.containsAll(collection);
		}

		@Override
		public D get(int index) {
			if (this.backgroundList == null) {
				return getUserDataAt(index);
			}
			return this.backgroundList.get(index);
		}

		@Override
		public int indexOf(Object obj) {
			if (this.backgroundList == null) {
				final List<D> backList = getInternalDataStructureForUserData();
				if (backList == null) {
					return -1;
				}
				return backList.indexOf(obj);
			}
			return this.backgroundList.indexOf(obj);
		}

		@Override
		public boolean isEmpty() {
			if (this.backgroundList == null) {
				final List<D> backList = getInternalDataStructureForUserData();
				if (backList == null) {
					return true;
				}
				return backList.isEmpty();
			}
			return this.backgroundList.isEmpty();
		}

		@Override
		public int lastIndexOf(Object obj) {
			if (this.backgroundList == null) {
				final List<D> backList = getInternalDataStructureForUserData();
				if (backList == null) {
					return -1;
				}
				return backList.lastIndexOf(obj);
			}
			return this.backgroundList.lastIndexOf(obj);
		}

		@Override
		@SuppressWarnings("unchecked")
		public boolean remove(Object obj) {
			if (this.backgroundList == null) {
				try {
					return removeUserData((D) obj);
				} catch (ClassCastException e) {
					return false;
				}
			}
			if (this.backgroundList.remove(obj)) {
				firePropertyDataChanged(Collections.singleton((D) obj), null);
				return true;
			}
			return false;
		}

		@Override
		public D remove(int index) {
			if (this.backgroundList == null) {
				return removeUserData(index);
			}
			final D oldElement = this.backgroundList.remove(index);
			if (oldElement != null) {
				firePropertyDataChanged(Collections.singleton(oldElement), null);
			}
			return oldElement;
		}

		@Override
		public boolean removeAll(Collection<?> collection) {
			boolean changed = false;
			for (final Object o : collection) {
				changed = remove(o) | changed;
			}
			return changed;
		}

		@Override
		public int size() {
			if (this.backgroundList == null) {
				return getUserDataCount();
			}
			return this.backgroundList.size();
		}

		@Override
		public Object[] toArray() {
			if (this.backgroundList == null) {
				final List<D> backList = getInternalDataStructureForUserData();
				if (backList == null) {
					return new Object[0];
				}
				return backList.toArray();
			}
			return this.backgroundList.toArray();
		}

		@Override
		public <T> T[] toArray(T[] array) {
			if (this.backgroundList == null) {
				final List<D> backList = getInternalDataStructureForUserData();
				if (backList != null) {
					return backList.toArray(array);
				}
				return array;
			}
			return this.backgroundList.toArray(array);
		}

		@Override
		public D set(int index, D element) {
			if (this.backgroundList == null) {
				setUserDataAt(index, element);
			}
			final D oldElement = this.backgroundList.set(index, element);
			firePropertyDataChanged(Collections.singleton(oldElement), Collections.singleton(element));
			return oldElement;
		}

		@Override
		public boolean retainAll(Collection<?> collection) {
			final List<D> removed = new ArrayList<>();
			final Iterator<D> iterator;
			if (this.backgroundList == null) {
				final List<D> backList = getInternalDataStructureForUserData();
				if (backList == null) {
					iterator = Collections.<D>emptyList().iterator();
				} else {
					iterator = backList.iterator();
				}
			} else {
				iterator = this.backgroundList.iterator();
			}
			D data1;
			while (iterator.hasNext()) {
				data1 = iterator.next();
				if (!collection.contains(data1)) {
					iterator.remove();
					removed.add(data1);
				}
			}
			if (!removed.isEmpty()) {
				firePropertyDataChanged(removed, null);
				return true;
			}
			return false;
		}

		@Override
		public Iterator<D> iterator() {
			return listIterator();
		}

		@Override
		public ListIterator<D> listIterator() {
			if (this.backgroundList == null) {
				final List<D> backList = getInternalDataStructureForUserData();
				if (backList == null) {
					return Collections.<D>emptyList().listIterator();
				}
				return new DataIterator(backList.listIterator());
			}
			return new DataIterator(this.backgroundList.listIterator());
		}

		@Override
		public ListIterator<D> listIterator(int index) {
			return subList(index, size()).listIterator();
		}

		@Override
		public List<D> subList(int fromIndex, int toIndex) {
			return new DataCollection(this.backgroundList.subList(fromIndex, toIndex));
		}

	}

	/**
	 * This iterator permits to iterate on the data of a tree node
	 * and notify the tree node when the collection changed.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	private class DataIterator implements ListIterator<D> {

		private final ListIterator<D> backgroundIterator;

		private D removableElement;

		/** Construct iterator.
		 * @param iterator the original iterator.
		 */
		DataIterator(ListIterator<D> iterator) {
			this.backgroundIterator = iterator;
		}

		@Override
		public boolean hasNext() {
			return this.backgroundIterator.hasNext();
		}

		@Override
		public D next() {
			this.removableElement = this.backgroundIterator.next();
			return this.removableElement;
		}

		@Override
		public boolean hasPrevious() {
			return this.backgroundIterator.hasPrevious();
		}

		@Override
		public int nextIndex() {
			return this.backgroundIterator.nextIndex();
		}

		@Override
		public D previous() {
			this.removableElement = this.backgroundIterator.previous();
			return this.removableElement;
		}

		@Override
		public int previousIndex() {
			return this.backgroundIterator.previousIndex();
		}

		@Override
		public void set(D element) {
			final D re = this.removableElement;
			this.removableElement = null;
			if (re == null) {
				throw new NoSuchElementException();
			}
			this.backgroundIterator.set(element);
			firePropertyDataChanged(Collections.singleton(re), Collections.singleton(element));
		}

		@Override
		public void remove() {
			final D re = this.removableElement;
			this.removableElement = null;
			if (re == null) {
				throw new NoSuchElementException();
			}
			this.backgroundIterator.remove();
			firePropertyDataChanged(Collections.singleton(re), null);
		}

		@Override
		public void add(D element) {
			this.backgroundIterator.add(element);
			firePropertyDataChanged(null, Collections.singleton(element));
		}

	}

}
