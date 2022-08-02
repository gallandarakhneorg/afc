/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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

import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.tree.TreeNodeAddedEvent;
import org.arakhne.afc.math.tree.TreeNodeListener;
import org.arakhne.afc.math.tree.TreeNodeParentChangedEvent;
import org.arakhne.afc.math.tree.TreeNodeRemovedEvent;

/** This is the generic implementation of a tree
 * tree with a weak reference to its parent node.
 *
 * @param <D> is the type of the data inside the tree
 * @param <N> is the type of the tree nodes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public abstract class AbstractTreeNode<D, N extends AbstractTreeNode<D, N>> extends AbstractParentlessTreeNode<D, N> {

	private static final long serialVersionUID = -296917015483866693L;

	/** Parent of this node.
	 */
	private transient WeakReference<N> parent;

	/** Construct node.
	 * @param useLinkedList indicates if a linked list must be used to store the data.
	 *     If <code>false</code>, an ArrayList will be used.
	 */
	public AbstractTreeNode(boolean useLinkedList) {
		super(useLinkedList);
		this.parent = null;
	}

	/** Construct node.
	 * @param useLinkedList indicates if a linked list must be used to store the data.
	 *     If <code>false</code>, an ArrayList will be used.
	 * @param copyDataCollection indicates if the given data collection is copied
	 *     if <code>true</code> or the inner data collection will be the given
	 *     collection itself if <code>false</code>.
	 * @param data are the initial user data.
	 */
	public AbstractTreeNode(boolean useLinkedList, boolean copyDataCollection, List<D> data) {
		super(useLinkedList, copyDataCollection, data);
		this.parent = null;
	}

	/** Construct node.
	 * @param useLinkedList indicates if a linked list must be used to store the data.
	 *     If <code>false</code>, an ArrayList will be used.
	 * @param data are the initial user data.
	 */
	public AbstractTreeNode(boolean useLinkedList, Collection<D> data) {
		super(useLinkedList, data);
		this.parent = null;
	}

	/** Construct node.
	 * @param useLinkedList indicates if a linked list must be used to store the data.
	 *     If <code>false</code>, an ArrayList will be used.
	 * @param data are the initial user data.
	 */
	public AbstractTreeNode(boolean useLinkedList, D data) {
		super(useLinkedList, data);
		this.parent = null;
	}

	@Override
	@Pure
	public int getDepth() {
		final N p = getParentNode();
		if (p == null) {
			return 0;
		}
		return p.getDepth() + 1;
	}

	@Override
	public abstract boolean setChildAt(int index, N newChild) throws IndexOutOfBoundsException;

	@Override
	@Pure
	public final N getParentNode() {
		return this.parent == null ? null : this.parent.get();
	}

	/** Set the reference to the parent node.
	 *
	 * @param newParent is the new parent.
	 * @param fireEvent indicates if the event must be fired or not.
	 * @return <code>true</code> if an event must be fired due to
	 *     the action of this function, otherwise <code>false</code>.
	 * @since 4.0
	 */
	boolean setParentNodeReference(N newParent, boolean fireEvent) {
		final N oldParent = getParentNode();
		if (newParent == oldParent) {
			return false;
		}
		this.parent = (newParent == null) ? null : new WeakReference<>(newParent);
		if (!fireEvent) {
			return true;
		}
		firePropertyParentChanged(oldParent, newParent);
		if (oldParent != null) {
			oldParent.firePropertyParentChanged(toN(), oldParent, newParent);
		}
		return false;
	}

	@Override
	public N removeFromParent() {
		final N lparent = getParentNode();
		if (lparent != null) {
			final int index = lparent.indexOf(toN());
			if (index != -1) {
				lparent.setChildAt(index, null);
			}
		}
		return lparent;
	}

	@Override
	public void removeDeeplyFromParent() {
		N lparent = getParentNode();
		N child = toN();

		while (lparent != null && lparent.isEmpty()) {
			child = lparent;
			lparent = lparent.getParentNode();
		}

		if (lparent != null) {
			final int index = lparent.indexOf(child);
			if (index != -1) {
				lparent.setChildAt(index, null);
			}
		}
	}

	/** Fire the event for the node child sets.
	 *
	 * @param childIndex is the index of the child that was set. If the added node was root, this parameter is <code>0</code>.
	 * @param newChild is the child that was added.
	 */
	protected final void firePropertyChildAdded(int childIndex, N newChild) {
		firePropertyChildAdded(new TreeNodeAddedEvent(toN(), childIndex, newChild));
	}

	/** Fire the event for the node child sets.
	 *
	 * @param event the event.
	 */
	void firePropertyChildAdded(TreeNodeAddedEvent event) {
		if (this.nodeListeners != null) {
			for (final TreeNodeListener listener : this.nodeListeners) {
				if (listener != null) {
					listener.treeNodeChildAdded(event);
				}
			}
		}
		final N parentNode = getParentNode();
		assert parentNode != this;
		if (parentNode != null) {
			parentNode.firePropertyChildAdded(event);
		}
	}

	/** Fire the event for the removed node child.
	 *
	 * @param childIndex is the index of the child that was removed. If the added node was root, this parameter is <code>0</code>.
	 * @param oldChild is the child that was removed.
	 */
	protected final void firePropertyChildRemoved(int childIndex, N oldChild) {
		firePropertyChildRemoved(new TreeNodeRemovedEvent(toN(), childIndex, oldChild));
	}

	/** Fire the event for the removed node child.
	 *
	 * @param event the event.
	 */
	void firePropertyChildRemoved(TreeNodeRemovedEvent event) {
		if (this.nodeListeners != null) {
			for (final TreeNodeListener listener : this.nodeListeners) {
				if (listener != null) {
					listener.treeNodeChildRemoved(event);
				}
			}
		}
		final N parentNode = getParentNode();
		if (parentNode != null) {
			parentNode.firePropertyChildRemoved(event);
		}
	}

	/** Fire the event for the changes node parents.
	 *
	 * @param oldParent is the previous parent node
	 * @param newParent is the current parent node
	 */
	protected final void firePropertyParentChanged(N oldParent, N newParent) {
		firePropertyParentChanged(new TreeNodeParentChangedEvent(toN(), oldParent, newParent));
	}

	/** Fire the event for the changes node parents.
	 *
	 * @param node is the node for which the parent has changed.
	 * @param oldParent is the previous parent node
	 * @param newParent is the current parent node
	 */
	void firePropertyParentChanged(N node, N oldParent, N newParent) {
		firePropertyParentChanged(new TreeNodeParentChangedEvent(node, oldParent, newParent));
	}

	/** Fire the event for the changes node parents.
	 *
	 * @param event the event.
	 */
	void firePropertyParentChanged(TreeNodeParentChangedEvent event) {
		if (this.nodeListeners != null) {
			for (final TreeNodeListener listener : this.nodeListeners) {
				if (listener != null) {
					listener.treeNodeParentChanged(event);
				}
			}
		}
		final N parentNode = getParentNode();
		if (parentNode != null) {
			parentNode.firePropertyParentChanged(event);
		}
	}

	@Override
	@Pure
	public final boolean isRoot() {
		return this.parent == null;
	}

	@Override
	@SuppressWarnings("unchecked")
	@Pure
	public final N[] getChildren(Class<N> type) {
		final N[] array = (N[]) Array.newInstance(type, getChildCount());
		getChildren(array);
		return array;
	}

	@Override
	@Pure
	public final Iterator<N> children() {
		return new ChildIterator();
	}

	/** Move this node in the given new node.
	 *
	 * <p>If any child node is already present
	 * at the given position in the new parent node,
	 * the tree node may replace the existing node
	 * or insert the moving node according to its
	 * implementation.
	 *
	 * <p>This function is preferred to a sequence of calls
	 * to {@link #removeFromParent()} and {@link #setChildAt(int, AbstractTreeNode)}
	 * because it fires a limited set of events dedicated to the move
	 * the node.
	 *
	 * @param newParent is the new parent for this node.
	 * @param index is the position of this node in the new parent.
	 * @param isDynamicChildList indicates if the parent node has a dynamic list of children.
	 *     If <code>true</code> the given {@code index} is clamped to
	 *     avoid {@link IndexOutOfBoundsException} when inserting this node in the parent node.
	 *     If <code>false</code> an {@link IndexOutOfBoundsException} is thrown when
	 *     the given {@code index} is outside the range of children of the parent node.
	 * @return <code>true</code> on success, otherwise <code>false</code>.
	 */
	@SuppressWarnings("checkstyle:npathcomplexity")
	protected boolean moveTo(N newParent, int index, boolean isDynamicChildList) {
		if (newParent == null) {
			return false;
		}

		int newIndex = index;
		if (isDynamicChildList) {
			if (newIndex < 0) {
				newIndex = 0;
			} else if (newIndex > getChildCount()) {
				newIndex = getChildCount();
			}
		} else if (newIndex < 0 || newIndex >= getChildCount()) {
			return false;
		}

		final N oldParent = getParentNode();
		if (oldParent == newParent) {
			return false;
		}
		final N me = toN();

		// Remove from previous parent
		int oldIndex = -1;
		if (oldParent != null) {
			oldIndex = oldParent.indexOf(me);
			if (oldIndex >= 0) {
				oldParent.setChildAtWithoutEventFiring(oldIndex, null);
			}
		}

		// Remove the node at the target position
		if (!isDynamicChildList || newIndex < getChildCount()) {
			newParent.setChildAt(newIndex, null);
		}

		// Add inside the new parent
		newParent.setChildAtWithoutEventFiring(newIndex, me);

		// Set the parent reference
		final boolean fireEvent = setParentNodeReference(newParent, false);

		// Fire events
		if (oldIndex >= 0) {
			assert oldParent != null;
			oldParent.firePropertyChildRemoved(oldIndex, me);
		}

		if (newIndex >= 0) {
			newParent.firePropertyChildAdded(newIndex, me);
		}

		if (fireEvent) {
			firePropertyParentChanged(oldParent, newParent);
		}

		return true;
	}

	/** Invoked by the inner classes to set the child at the given index
	 * without firing the events. This function should never
	 * invoke {@link #setParentNodeReference(AbstractTreeNode, boolean)}.
	 *
	 * @param index is the position of the new child.
	 * @param child is the new child
	 * @throws IndexOutOfBoundsException if the index is invalid.
	 */
	protected abstract void setChildAtWithoutEventFiring(int index, N child) throws IndexOutOfBoundsException;

	/** Internal iterator.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	private class ChildIterator implements Iterator<N> {

		private int idx;

		private N next;

		/** Construct iterator.
		 */
		ChildIterator() {
			searchNext();
		}

		private void searchNext() {
			final int count = AbstractTreeNode.this.getChildCount();
			this.next = null;
			while (this.next == null && this.idx < count) {
				this.next = AbstractTreeNode.this.getChildAt(this.idx);
				++this.idx;
			}
		}

		@Override
		public boolean hasNext() {
			return this.next != null;
		}

		@Override
		public N next() {
			if (this.next == null) {
				throw new NoSuchElementException();
			}
			final N n = this.next;
			searchNext();
			return n;
		}

	}

}
