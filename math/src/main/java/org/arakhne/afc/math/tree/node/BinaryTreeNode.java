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


/**
 * This is the generic implementation of a binary
 * tree.
 * <p>
 * <h3>moveTo</h3>
 * According to its definition in 
 * {@link TreeNode#moveTo(TreeNode, int)}, the binary
 * tree node implementation of <code>moveTo</code>
 * replaces any existing node at the position given as
 * parameter of <code>moveTo</code>..
 * 
 * @param <D> is the type of the data inside the tree
 * @param <N> is the type of the tree nodes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class BinaryTreeNode<D,N extends BinaryTreeNode<D,N>> extends AbstractTreeNode<D,N> {

	private static final long serialVersionUID = -3061156557458672703L;
	
	private N left;
	private N right;
	
	/**
	 * Empty node
	 */
	public BinaryTreeNode() {
		this(DEFAULT_LINK_LIST_USE);
	}
	
	/**
	 * @param data are the initial data.
	 */
	public BinaryTreeNode(Collection<D> data) {
		super(DEFAULT_LINK_LIST_USE,data);
		this.left = null;
		this.right = null;
	}
	
	/**
	 * @param data are the initial data.
	 */
	public BinaryTreeNode(D data) {
		this(DEFAULT_LINK_LIST_USE,data);
	}

	/**
	 * @param useLinkedList indicates if a linked list must be used to store the data.
	 * If <code>false</code>, an ArrayList will be used.
	 */
	public BinaryTreeNode(boolean useLinkedList) {
		super(useLinkedList);
		this.left = null;
		this.right = null;
	}
	
	/**
	 * @param useLinkedList indicates if a linked list must be used to store the data.
	 * If <code>false</code>, an ArrayList will be used.
	 * @param copyDataCollection indicates if the given data collection is copied
	 * if <code>true</code> or the inner data collection will be the given
	 * collection itself if <code>false</code>.
	 * @param data are the initial data.
	 */
	public BinaryTreeNode(boolean useLinkedList, boolean copyDataCollection, List<D> data) {
		super(useLinkedList,copyDataCollection,data);
		this.left = null;
		this.right = null;
	}

	/**
	 * @param useLinkedList indicates if a linked list must be used to store the data.
	 * If <code>false</code>, an ArrayList will be used.
	 * @param data are the initial data.
	 */
	public BinaryTreeNode(boolean useLinkedList, D data) {
		super(useLinkedList,data);
		this.left = null;
		this.right = null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Class<? extends Enum<?>> getPartitionEnumeration() {
		return BinaryTreeZone.class;
	}

	/** Invoked when this object must be deserialized.
	 * 
	 * @param in is the input stream.
	 * @throws IOException in case of input stream access error.
	 * @throws ClassNotFoundException if some class was not found.
	 */
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		if (this.left!=null) this.left.setParentNodeReference(toN(), false);
		if (this.right!=null) this.right.setParentNodeReference(toN(), false);
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
		if (this.left!=null) {
			N child = this.left;
			setLeftChild(null);
			child.clear();
		}
		if (this.right!=null) {
			N child = this.right;
			setRightChild(null);
			child.clear();
		}
		removeAllUserData();
	}

	/** Replies count of children in this node.
	 * 
	 * @return always 2.
	 */
	@Override
	public int getChildCount() {
		return 2;
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
	public N getChildAt(int index) throws IndexOutOfBoundsException {
		switch(index) {
		case 0:
			return this.left;
		case 1:
			return this.right;
		default:
			throw new IndexOutOfBoundsException(index+">= 2"); //$NON-NLS-1$
		}
	}

	/** Replies the child node at the specified position
	 * 
	 * @param index is the position of the child to reply.
	 * @return the child or <code>null</code>
	 */
	public final N getChildAt(BinaryTreeZone index) {
		switch(index) {
		case LEFT:
			return this.left;
		case RIGHT:
			return this.right;
		default:
			throw new IndexOutOfBoundsException();
		}
	}

	/** Set the left child of this node.
	 * 
	 * @param newChild is the new left child
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	public boolean setLeftChild(N newChild) {
		N oldChild = this.left;
		if (oldChild==newChild) return false;
				
		if (oldChild!=null) {
			oldChild.setParentNodeReference(null, true);
			--this.notNullChildCount;
			firePropertyChildRemoved(0, oldChild);
		}
		
		if (newChild!=null) {
			N oldParent = newChild.getParentNode();
			if (oldParent!=this) {
				newChild.removeFromParent();
			}
		}

		this.left = newChild;
		
		if (newChild!=null) {
			++this.notNullChildCount;
			newChild.setParentNodeReference(toN(), true);
			firePropertyChildAdded(0, newChild);
		}
		
		return true;
	}

	/** Set the left child of this node.
	 * 
	 * @return the left child or <code>null</code> if it does not exist
	 */
	public final N getLeftChild() {
		return this.left;
	}

	/** Set the right child of this node.
	 * 
	 * @param newChild is the new left child
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	public boolean setRightChild(N newChild) {
		N oldChild = this.right;
		if (oldChild==newChild) return false;
				
		if (oldChild!=null) {
			oldChild.setParentNodeReference(null, true);
			--this.notNullChildCount;
			firePropertyChildRemoved(1, oldChild);
		}
		
		if (newChild!=null) {
			N oldParent = newChild.getParentNode();
			if (oldParent!=this) {
				newChild.removeFromParent();
			}
		}

		this.right = newChild;

		if (newChild!=null) {
			newChild.setParentNodeReference(toN(), true);
			++this.notNullChildCount;
			firePropertyChildAdded(1, newChild);
		}
		
		return true;
	}

	/** Set the right child of this node.
	 * 
	 * @return the right child or <code>null</code> if it does not exist
	 */
	public final N getRightChild() {
		return this.right;
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isLeaf() {
		return this.left==null && this.right==null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean setChildAt(int index, N newChild) throws IndexOutOfBoundsException {
		switch(index) {
		case 0:
			return setLeftChild(newChild);
		case 1:
			return setRightChild(newChild);
		default:
			throw new IndexOutOfBoundsException(index+">= 2"); //$NON-NLS-1$
		}
	}
	
	/** {@inheritDoc}
	 */
	@Override
	protected void setChildAtWithoutEventFiring(int index, N newChild) throws IndexOutOfBoundsException {
		switch(index) {
		case 0:
			if (this.left!=null) --this.notNullChildCount;
			this.left = newChild;
			if (this.left!=null) ++this.notNullChildCount;
			break;
		case 1:
			if (this.right!=null) --this.notNullChildCount;
			this.right = newChild;
			if (this.right!=null) ++this.notNullChildCount;
			break;
		default:
			throw new IndexOutOfBoundsException(index+">= 2"); //$NON-NLS-1$
		}
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public boolean moveTo(N newParent, int index) {
		return moveTo(newParent, index, false);
	}

	/** Set the child for the specified zone.
	 * 
	 * @param zone is the zone to set
	 * @param newChild is the child to insert
	 * @return <code>true</code> if the child was added, otherwise <code>false</code>
	 */
	public final boolean setChildAt(BinaryTreeZone zone, N newChild) {
		switch(zone) {
		case LEFT:
			return setLeftChild(newChild);
		case RIGHT:
			return setRightChild(newChild);
		default:
			throw new IndexOutOfBoundsException();
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean removeChild(N child) {
		if (child!=null) {
			if (child==this.left) {
				return setLeftChild(null);
			}
			else if (child==this.right) {
				return setRightChild(null);
			}
		}
		return false;
	}

	/** {@inheritDoc}
	 */
	@Override
	public int indexOf(N child) {
		if (child==this.left) return 0;
		if (child==this.right) return 1;
		return -1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void getChildren(Object[] array) {
		if (array!=null) {
			if (array.length>0) {
				array[0] = this.left;
			}
			if (array.length>1) {
				array[1] = this.right;
			}
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public int getMinHeight() {
		return 1+Math.min(
				this.left!=null ? this.left.getMinHeight() : 0,
				this.right!=null ? this.right.getMinHeight() : 0);
	}

	/** {@inheritDoc}
	 */
	@Override
	public int getMaxHeight() {
		return 1+Math.max(
				this.left!=null ? this.left.getMaxHeight() : 0,
				this.right!=null ? this.right.getMaxHeight() : 0);
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
			heights.add(currentHeight);
		}
		else {
			if (this.left!=null) this.left.getHeights(currentHeight+1, heights);
			if (this.right!=null) this.right.getHeights(currentHeight+1, heights);
		}
	}

	/**
	 * This is the generic implementation of a ternary
	 * tree.
	 * 
	 * @param <D> is the type of the data inside the tree
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static class DefaultBinaryTreeNode<D> extends BinaryTreeNode<D,DefaultBinaryTreeNode<D>> {
		
		private static final long serialVersionUID = -1756893035646038303L;

		/**
		 * Empty node.
		 */
		public DefaultBinaryTreeNode() {
			super();
		}
		
		/**
		 * @param data are the initial user data.
		 * 
		 */
		public DefaultBinaryTreeNode(Collection<D> data) {
			super(data);
		}
		
		/**
		 * @param data are the initial user data.
		 * 
		 */
		public DefaultBinaryTreeNode(D data) {
			super(data);
		}

	} /* class DefaultBinaryTreeNode */

	/**
	 * This is the generic implementation of a
	 * tree for which each node has two children.
	 * 
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static enum BinaryTreeZone {
		/** This is the index of the child that correspond to
		 * the voxel at front/left/up position.
		 */
		LEFT,
		
		/** This is the index of the child that correspond to
		 * the voxel at back/right/down position.
		 */
		RIGHT;
		
		/** Replies the zone corresponding to the given index.
		 * The index is the same as the ordinal value of the
		 * enumeration. If the given index does not correspond
		 * to an ordinal value, <code>null</code> is replied.
		 * 
		 * @param index
		 * @return the zone or <code>null</code>
		 */
		public static BinaryTreeZone fromInteger(int index) {
			if (index<0) return null;
			BinaryTreeZone[] nodes = values();
			if (index>=nodes.length) return null;
			return nodes[index];
		}
		
	}

}