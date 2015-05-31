/* 
 * $Id$
 * 
 * Copyright (c) 2005-10, Multiagent Team,
 * Laboratoire Systemes et Transports,
 * Universite de Technologie de Belfort-Montbeliard.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Laboratoire Systemes et Transports
 * of the Universite de Technologie de Belfort-Montbeliard ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 *
 * http://www.multiagent.fr/
 */
package org.arakhne.afc.math.tree.node;

import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.arakhne.afc.math.tree.TreeNode;
import org.arakhne.afc.math.tree.TreeNodeAddedEvent;
import org.arakhne.afc.math.tree.TreeNodeListener;
import org.arakhne.afc.math.tree.TreeNodeParentChangedEvent;
import org.arakhne.afc.math.tree.TreeNodeRemovedEvent;


/**
 * This is the generic implementation of a tree
 * tree with a weak reference to its parent node.
 * 
 * @param <D> is the type of the data inside the tree
 * @param <N> is the type of the tree nodes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractTreeNode<D,N extends AbstractTreeNode<D,N>> extends AbstractParentlessTreeNode<D,N> {

	private static final long serialVersionUID = -296917015483866693L;
	
	/** Parent of this node.
	 */
	private transient WeakReference<N> parent;

	/**
	 * @param useLinkedList indicates if a linked list must be used to store the data.
	 * If <code>false</code>, an ArrayList will be used.
	 */
	public AbstractTreeNode(boolean useLinkedList) {
		super(useLinkedList);
		this.parent = null;
	}

	/**
	 * @param useLinkedList indicates if a linked list must be used to store the data.
	 * If <code>false</code>, an ArrayList will be used.
	 * @param copyDataCollection indicates if the given data collection is copied
	 * if <code>true</code> or the inner data collection will be the given
	 * collection itself if <code>false</code>.
	 * @param data are the initial user data.
	 */
	public AbstractTreeNode(boolean useLinkedList, boolean copyDataCollection, List<D> data) {
		super(useLinkedList,copyDataCollection,data);
		this.parent = null;
	}
	
	/**
	 * @param useLinkedList indicates if a linked list must be used to store the data.
	 * If <code>false</code>, an ArrayList will be used.
	 * @param data are the initial user data.
	 */
	public AbstractTreeNode(boolean useLinkedList, Collection<D> data) {
		super(useLinkedList,data);
		this.parent = null;
	}

	/**
	 * @param useLinkedList indicates if a linked list must be used to store the data.
	 * If <code>false</code>, an ArrayList will be used.
	 * @param data are the initial user data.
	 */
	public AbstractTreeNode(boolean useLinkedList, D data) {
		super(useLinkedList,data);
		this.parent = null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public int getDepth() {
		N p = getParentNode();
		if (p==null) return 0;
		return p.getDepth()+1;
	}

	/** {@inheritDoc}
	 */
	@Override
	public abstract boolean setChildAt(int index, N newChild) throws IndexOutOfBoundsException;
	
	/** {@inheritDoc}
	 */
	@Override
	public final N getParentNode() {
		return this.parent==null ? null : this.parent.get();
	}

	/** Set the reference to the parent node.
	 * 
	 * @param newParent is the new parent.
	 * @param fireEvent indicates if the event must be fired or not.
	 * @return <code>true</code> if an event must be fired due to
	 * the action of this function, otherwise <code>false</code>.
	 * @since 4.0
	 */
	boolean setParentNodeReference(N newParent, boolean fireEvent) {
		N oldParent = getParentNode();
		if (newParent==oldParent) return false;
		this.parent = (newParent==null) ? null : new WeakReference<>(newParent);
		if (!fireEvent) return true;
		firePropertyParentChanged(oldParent, newParent);
		if (oldParent!=null)
			oldParent.firePropertyParentChanged(toN(), oldParent, newParent);
		return false;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	@Deprecated
	public final void setParentNode(N newParent) {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public N removeFromParent() {
		N lparent = getParentNode();
		if (lparent!=null) {
			int index = lparent.indexOf(toN());
			if (index!=-1) {
				lparent.setChildAt(index, null);
			}			
		}
		return lparent;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void removeDeeplyFromParent() {
		N lparent = getParentNode();
		N child = toN();

		while (lparent!=null && lparent.isEmpty()) {
			child = lparent;
			lparent = lparent.getParentNode();
		}

		if (lparent!=null) {
			int index = lparent.indexOf(child);
			if (index!=-1) {
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
		firePropertyChildAdded(new TreeNodeAddedEvent(toN(),childIndex, newChild));		
	}

	/** Fire the event for the node child sets.
	 * 
	 * @param event
	 */
	void firePropertyChildAdded(TreeNodeAddedEvent event) {
		if (this.nodeListeners!=null) {
			for (TreeNodeListener listener : this.nodeListeners) {
				if (listener!=null) {
					listener.treeNodeChildAdded(event);
				}
			}
		}
		N parentNode = getParentNode();
		assert(parentNode!=this);
		if (parentNode!=null) {
			parentNode.firePropertyChildAdded(event);
		}
	}

	/** Fire the event for the removed node child.
	 * 
	 * @param childIndex is the index of the child that was removed. If the added node was root, this parameter is <code>0</code>.
	 * @param oldChild is the child that was removed.
	 */
	protected final void firePropertyChildRemoved(int childIndex, N oldChild) {
		firePropertyChildRemoved(new TreeNodeRemovedEvent(toN(),childIndex, oldChild));		
	}

	/** Fire the event for the removed node child.
	 * 
	 * @param event
	 */
	void firePropertyChildRemoved(TreeNodeRemovedEvent event) {
		if (this.nodeListeners!=null) {
			for (TreeNodeListener listener : this.nodeListeners) {
				if (listener!=null) {
					listener.treeNodeChildRemoved(event);
				}
			}
		}
		N parentNode = getParentNode();
		if (parentNode!=null) parentNode.firePropertyChildRemoved(event);
	}

	/** Fire the event for the changes node parents.
	 * 
	 * @param oldParent is the previous parent node
	 * @param newParent is the current parent node
	 */
	protected final void firePropertyParentChanged(N oldParent, N newParent) {
		firePropertyParentChanged(new TreeNodeParentChangedEvent(toN(),oldParent,newParent));		
	}

	/** Fire the event for the changes node parents.
	 * 
	 * @param node is the node for which the parent has changed.
	 * @param oldParent is the previous parent node
	 * @param newParent is the current parent node
	 */
	void firePropertyParentChanged(N node, N oldParent, N newParent) {
		firePropertyParentChanged(new TreeNodeParentChangedEvent(node,oldParent,newParent));		
	}

	/** Fire the event for the changes node parents.
	 * 
	 * @param event
	 */
	void firePropertyParentChanged(TreeNodeParentChangedEvent event) {
		if (this.nodeListeners!=null) {
			for (TreeNodeListener listener : this.nodeListeners) {
				if (listener!=null) {
					listener.treeNodeParentChanged(event);
				}
			}
		}
		N parentNode = getParentNode();
		if (parentNode!=null) parentNode.firePropertyParentChanged(event);
	}

	/** {@inheritDoc}
	 */
	@Override
	public final boolean isRoot() {
		return this.parent == null;
	}

	/** {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public final N[] getChildren(Class<N> type) {
		N[] array = (N[])Array.newInstance(type, getChildCount());
		getChildren(array);
		return array;
	}

	/** {@inheritDoc}
	 */
	@Override
	public final Iterator<N> children() {
		return new ChildIterator();
	}
	
	/** Move this node in the given new node.
	 * <p>
	 * If any child node is already present
	 * at the given position in the new parent node,
	 * the tree node may replace the existing node
	 * or insert the moving node according to its
	 * implementation.
	 * <p>
	 * This function is preferred to a sequence of calls
	 * to {@link #removeFromParent()} and {@link #setChildAt(int, TreeNode)}
	 * because it fires a limited set of events dedicated to the move
	 * the node.
	 * 
	 * @param newParent is the new parent for this node.
	 * @param index is the position of this node in the new parent.
	 * @param isDynamicChildList indicates if the parent node has a dynamic list of children.
	 * If <code>true</code> the given <var>index</var> is clamped to
	 * avoid {@link IndexOutOfBoundsException} when inserting this node in the parent node.
	 * If <code>false</code> an {@link IndexOutOfBoundsException} is thrown when
	 * the given <var>index</var> is outside the range of children of the parent node.
	 * @return <code>true</code> on success, otherwise <code>false</code>.
	 * @since 4.0
	 */
	protected boolean moveTo(N newParent, int index, boolean isDynamicChildList) {
		if (newParent==null) return false;
		
		int newIndex = index;
		if (isDynamicChildList) {
			if (newIndex<0) newIndex = 0;
			else if (newIndex>getChildCount()) newIndex = getChildCount();
			
		}
		else if (newIndex<0 || newIndex>=getChildCount()) return false;
		
		N oldParent = getParentNode();
		if (oldParent==newParent) return false;
		N me = toN();
		
		// Remove from previous parent
		int oldIndex = -1;
		if (oldParent!=null) {
			oldIndex = oldParent.indexOf(me);
			if (oldIndex>=0)
				oldParent.setChildAtWithoutEventFiring(oldIndex, null);
		}
		
		// Remove the node at the target position
		if (!isDynamicChildList || newIndex<getChildCount()) {
			newParent.setChildAt(newIndex, null);
		}

		// Add inside the new parent
		newParent.setChildAtWithoutEventFiring(newIndex, me);
		
		// Set the parent reference
		boolean fireEvent = setParentNodeReference(newParent, false);
		
		// Fire events
		if (oldIndex>=0) {
			assert(oldParent!=null);
			oldParent.firePropertyChildRemoved(oldIndex, me);
		}
		
		if (newIndex>=0) {
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
	 * @throws IndexOutOfBoundsException
	 */
	protected abstract void setChildAtWithoutEventFiring(int index, N child) throws IndexOutOfBoundsException;

	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class ChildIterator implements Iterator<N> {

		private int idx = 0;
		private N next;
		
		/**
		 */
		public ChildIterator() {
			searchNext();
		}
		
		private void searchNext() {
			int count = AbstractTreeNode.this.getChildCount();
			this.next = null;
			while (this.next==null && this.idx<count) {
				this.next = AbstractTreeNode.this.getChildAt(this.idx);
				++this.idx;
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNext() {
			return this.next!=null;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public N next() {
			if (this.next==null) throw new NoSuchElementException();
			N n = this.next;
			searchNext();
			return n;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void remove() {
			//
		}
		
	}
	
}