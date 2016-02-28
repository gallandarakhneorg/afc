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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Collection;
import java.util.List;

import org.arakhne.afc.math.tree.TreeNode;


/**
 * This is the generic implementation of a n-ary
 * tree. This node has a constant count of children.
 * Indeed when a child node was removed, the count of children
 * is not changed. It also means that a child node could be
 * <code>null</code>. If you want a generic implementation
 * of a tree node which has a dynamic count count of children,
 * please see {@link NaryTreeNode}.
 * <p>
 * <h3>moveTo</h3>
 * According to its definition in 
 * {@link TreeNode#moveTo(TreeNode, int)}, the binary
 * tree node implementation of <code>moveTo</code>
 * replaces any existing node at the position given as
 * parameter of <code>moveTo</code>..
 * 
 * @see NaryTreeNode
 * @param <D> is the type of the data inside the tree
 * @param <N> is the type of the tree nodes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class ConstantNaryTreeNode<D,N extends ConstantNaryTreeNode<D,N>> extends AbstractTreeNode<D,N> {

	private static final long serialVersionUID = -3499092312746430238L;
	
	private final N[] children;

	/**
	 * Empty node.
	 * 
	 * @param childCount is the constant count of child
	 */
	public ConstantNaryTreeNode(int childCount) {
		this(childCount, DEFAULT_LINK_LIST_USE);
	}
	
	/**
	 * @param childCount is the constant count of child
	 * @param data are the initial user data.
	 */
	public ConstantNaryTreeNode(int childCount, Collection<D> data) {
		super(DEFAULT_LINK_LIST_USE, data);
		this.children = newArray(childCount);
	}
	
	/**
	 * @param childCount is the constant count of child
	 * @param data are the initial user data.
	 */
	public ConstantNaryTreeNode(int childCount, D data) {
		this(childCount, DEFAULT_LINK_LIST_USE, data);
	}

	/**
	 * @param childCount is the constant count of child
	 * @param useLinkedList indicates if a linked list must be used to store the data.
	 * If <code>false</code>, an ArrayList will be used.
	 */
	public ConstantNaryTreeNode(int childCount, boolean useLinkedList) {
		super(useLinkedList);
		this.children = newArray(childCount);
	}
	
	/**
	 * @param childCount is the constant count of child
	 * @param useLinkedList indicates if a linked list must be used to store the data.
	 * If <code>false</code>, an ArrayList will be used.
	 * @param copyDataCollection indicates if the given data collection is copied
	 * if <code>true</code> or the inner data collection will be the given
	 * collection itself if <code>false</code>.
	 * @param data are the initial user data
	 */
	public ConstantNaryTreeNode(int childCount, boolean useLinkedList, boolean copyDataCollection, List<D> data) {
		super(useLinkedList, copyDataCollection, data);
		this.children = newArray(childCount);
	}

	/**
	 * @param childCount is the constant count of child
	 * @param useLinkedList indicates if a linked list must be used to store the data.
	 * If <code>false</code>, an ArrayList will be used.
	 * @param data are the initial user data
	 */
	public ConstantNaryTreeNode(int childCount, boolean useLinkedList, D data) {
		super(useLinkedList, data);
		this.children = newArray(childCount);
	}

	@SuppressWarnings("unchecked")
	private static <N> N[] newArray(int size) {
		return (N[])new ConstantNaryTreeNode[size];
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public Class<? extends Enum<?>> getPartitionEnumeration() {
		return null;
	}

	/** Invoked when this object must be deserialized.
	 * 
	 * @param in is the input stream.
	 * @throws IOException in case of input stream access error.
	 * @throws ClassNotFoundException if some class was not found.
	 */
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		if (this.children!=null) {
			for(N child : this.children) {
				if (child!=null) child.setParentNodeReference(toN(), false);
			}
		}
	}

	/** Clear the tree.
	 * <p>
	 * Caution: this method also destroyes the
	 * links between the child nodes inside the tree.
	 * If you want to unlink the first-level
	 * child node with
	 * this node but leave the rest of the tree
	 * unchanged, please call <code>setChildAt(i,null)</code>.
	 */
	@Override
	public void clear() {
		removeAllUserData();
		if (this.children!=null) {
			N child;
			for(int i=0; i<this.children.length; ++i) {
				child = this.children[i];
				if (child!=null) {
					setChildAt(i, null);
					child.clear();
				}
			}
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public final int getChildCount() {
		return this.children.length;
	}

	/** {@inheritDoc}
	 */
	@Override
	public int getNotNullChildCount() {
		return this.notNullChildCount;
	}

	/** {@inheritDoc}
	 */
	@Override
	public final int indexOf(N child) {
		int i=0;
		for(N cchild : this.children) {
			if (cchild==child) {
				return i;
			}
			++i;
		}
		return -1;
	}

	/** {@inheritDoc}
	 */
	@Override
	public final N getChildAt(int index) throws IndexOutOfBoundsException {
		return this.children[index];
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean moveTo(N newParent, int index) {
		return moveTo(newParent, index, false);
	}

	/** Set the child at the given index in this node.
	 * 
	 * @param index is the index of the new child between <code>0</code>
	 * and <code>getChildCount()</code> (inclusive).
	 * @param newChild is the child to insert.
	 */
	@Override
	public boolean setChildAt(int index, N newChild) throws IndexOutOfBoundsException {
		N oldChild = (index<this.children.length) ? this.children[index] : null;
		if (oldChild==newChild) return false;

		if (oldChild!=null) {
			oldChild.setParentNodeReference(null, true);
			--this.notNullChildCount;
			firePropertyChildRemoved(index, oldChild);
		}
		
		if (newChild!=null) {
			N oldParent = newChild.getParentNode();
			if (oldParent!=this) {
				newChild.removeFromParent();
			}
		}

		// set the element
		this.children[index] = newChild;
		
		if (newChild!=null) {
			newChild.setParentNodeReference(toN(), true);		
			++this.notNullChildCount;
			firePropertyChildAdded(index, newChild);
		}
		
		return true;
	}

	/** {@inheritDoc}
	 */
	@Override
	protected void setChildAtWithoutEventFiring(int index, N newChild) throws IndexOutOfBoundsException {
		if (this.children[index]!=null) --this.notNullChildCount;
		this.children[index] = newChild;
		if (this.children[index]!=null) ++this.notNullChildCount;
	}

	/** {@inheritDoc}
	 */
	@Override
	public final boolean removeChild(N child) {
		if (child!=null && this.children!=null) {
			int index = indexOf(child);
			if (index>=0 && index<this.children.length) {
				this.children[index] = null;
				--this.notNullChildCount;
				child.setParentNodeReference(null, true);
				firePropertyChildRemoved(index, child);
				return true;
			}
		}
		return false;
	}

	/** {@inheritDoc}
	 */
	@Override
	public final boolean isLeaf() {
		for(N child : this.children) {
			if (child!=null) return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void getChildren(Object[] array) {
		if (array!=null) {
			System.arraycopy(this.children, 0, array, 0, Math.min(this.children.length,array.length));
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public int getMinHeight() {
		int min = Integer.MAX_VALUE;
		boolean set = false;
		for(N child : this.children) {
			if (child!=null) {
				if (set) {
					min = Math.min(min, child.getMinHeight());
				}
				else {
					min = child.getMinHeight();
					set = true;
				}
			}
		}
		return 1+(set ? min : 0);
	}

	/** {@inheritDoc}
	 */
	@Override
	public int getMaxHeight() {
		int max = Integer.MIN_VALUE;
		boolean set = false;
		for(N child : this.children) {
			if (child!=null) {
				if (set) {
					max = Math.max(max, child.getMaxHeight());
				}
				else {
					max = child.getMaxHeight();
					set = true;
				}
			}
		}
		return 1+(set ? max : 0);
	}

	/** Replies the heights of all the leaf nodes.
	 * The order of the heights is given by a depth-first iteration.
	 * 
	 * @param currentHeight is the current height of this node.
	 * @param heights is the list of heights to fill
	 */
	@Override
	protected void getHeights(int currentHeight, List<Integer> heights) {
		if (isLeaf()) {
			heights.add(new Integer(currentHeight));
		}
		else {
			for(N child : this.children) {
				if (child!=null) child.getHeights(currentHeight+1, heights);
			}
		}
	}

	/**
	 * This is the generic implementation of a n-ary
	 * tree.
	 * 
	 * @param <D> is the type of the data inside the tree
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static class DefaultConstantNaryTreeNode<D> extends ConstantNaryTreeNode<D,DefaultConstantNaryTreeNode<D>> {
		
		private static final long serialVersionUID = -5185295672252424553L;

		/**
		 * Empty node.
		 * 
		 * @param childCount is the constant count of child
		 */
		public DefaultConstantNaryTreeNode(int childCount) {
			super(childCount);
		}
		
		/**
		 * @param childCount is the constant count of child
		 * @param data are the initial user data
		 */
		public DefaultConstantNaryTreeNode(int childCount, Collection<D> data) {
			super(childCount);
		}
		
		/**
		 * @param childCount is the constant count of child
		 * @param data are the initial user data
		 */
		public DefaultConstantNaryTreeNode(int childCount, D data) {
			super(childCount);
		}

	} /* class DefaultConstantNaryTreeNode */

}