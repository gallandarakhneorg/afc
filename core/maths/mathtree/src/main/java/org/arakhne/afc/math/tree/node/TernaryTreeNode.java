/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Collection;
import java.util.List;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.tree.TreeNode;

/**
 * This is the generic implementation of a ternary
 * tree.
 *
 * <p><h3>moveTo</h3>
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
 * @since 13.0
 */
public abstract class TernaryTreeNode<D, N extends TernaryTreeNode<D, N>> extends AbstractTreeNode<D, N> {

	private static final long serialVersionUID = -5699134081962229144L;

	private N left;

	private N middle;

	private N right;

	/**
	 * Empty node.
	 */
	public TernaryTreeNode() {
		this(DEFAULT_LINK_LIST_USE);
	}

	/** Construct a node.
	 *
	 * @param data is the initial user data
	 */
	public TernaryTreeNode(Collection<D> data) {
		super(DEFAULT_LINK_LIST_USE, data);
		this.left = null;
		this.middle = null;
		this.right = null;
	}

	/** Construct a node.
	 *
	 * @param data is the initial user data
	 */
	public TernaryTreeNode(D data) {
		this(DEFAULT_LINK_LIST_USE, data);
	}

	/** Constructor.
	 * @param useLinkedList indicates if a linked list must be used to store the data.
	 *     If <code>false</code>, an ArrayList will be used.
	 */
	public TernaryTreeNode(boolean useLinkedList) {
		super(useLinkedList);
		this.left = null;
		this.middle = null;
		this.right = null;
	}

	/** Constructor.
	 * @param useLinkedList indicates if a linked list must be used to store the data.
	 *     If <code>false</code>, an ArrayList will be used.
	 * @param copyDataCollection indicates if the given data collection is copied
	 *     if <code>true</code> or the inner data collection will be the given
	 *     collection itself if <code>false</code>.
	 * @param data is the initial user data
	 */
	public TernaryTreeNode(boolean useLinkedList, boolean copyDataCollection, List<D> data) {
		super(useLinkedList, copyDataCollection, data);
		this.left = null;
		this.middle = null;
		this.right = null;
	}

	/** Constructor.
	 * @param useLinkedList indicates if a linked list must be used to store the data.
	 *     If <code>false</code>, an ArrayList will be used.
	 * @param data is the initial user data
	 */
	public TernaryTreeNode(boolean useLinkedList, D data) {
		super(useLinkedList, data);
		this.left = null;
		this.middle = null;
		this.right = null;
	}

	@Pure
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
		final N me = toN();
		if (this.left != null) {
			this.left.setParentNodeReference(me, false);
		}
		if (this.middle != null) {
			this.middle.setParentNodeReference(me, false);
		}
		if (this.right != null) {
			this.right.setParentNodeReference(me, false);
		}
	}

	/** Clear the tree.
	 *
	 * <p>Caution: this method also destroyes the
	 * links between the child nodes inside the tree.
	 * If you want to unlink the first-level
	 * child node with
	 * this node but leave the rest of the tree
	 * unchanged, please call <code>setChildAt(i,null)</code>.
	 */
	@Override
	public void clear() {
		if (this.left != null) {
			final N child = this.left;
			setLeftChild(null);
			child.clear();
		}
		if (this.middle != null) {
			final N child = this.middle;
			setMiddleChild(null);
			child.clear();
		}
		if (this.right != null) {
			final N child = this.right;
			setRightChild(null);
			child.clear();
		}
		removeAllUserData();
	}

	@Pure
	@Override
	public final int getChildCount() {
		return 3;
	}

	@Pure
	@Override
	public int getNotNullChildCount() {
		return this.notNullChildCount;
	}

	@Pure
	@Override
	public final N getChildAt(int index) throws IndexOutOfBoundsException {
		switch (index) {
		case 0:
			return this.left;
		case 1:
			return this.middle;
		case 2:
			return this.right;
		default:
			throw new IndexOutOfBoundsException();
		}
	}

	/** Set the left child of this node.
	 *
	 * @param newChild the new child.
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	public boolean setLeftChild(N newChild) {
		final N oldChild = this.left;
		if (oldChild == newChild) {
			return false;
		}

		if (oldChild != null) {
			oldChild.setParentNodeReference(null, true);
			--this.notNullChildCount;
			firePropertyChildRemoved(0, oldChild);
		}

		if (newChild != null) {
			final N oldParent = newChild.getParentNode();
			if (oldParent != this) {
				newChild.removeFromParent();
			}
		}

		this.left = newChild;

		if (newChild != null) {
			newChild.setParentNodeReference(toN(), true);
			++this.notNullChildCount;
			firePropertyChildAdded(0, newChild);
		}

		return true;
	}

	/** Set the left child of this node.
	 *
	 * @return the left child.
	 */
	@Pure
	public final N getLeftChild() {
		return this.left;
	}

	/** Set the middle child of this node.
	 *
	 * @param newChild the new child.
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	public boolean setMiddleChild(N newChild) {
		final N oldChild = this.middle;
		if (oldChild == newChild) {
			return false;
		}

		if (oldChild != null) {
			oldChild.setParentNodeReference(null, true);
			--this.notNullChildCount;
			firePropertyChildRemoved(1, oldChild);
		}

		if (newChild != null) {
			final N oldParent = newChild.getParentNode();
			if (oldParent != this) {
				newChild.removeFromParent();
			}
		}

		this.middle = newChild;

		if (newChild != null) {
			newChild.setParentNodeReference(toN(), true);
			++this.notNullChildCount;
			firePropertyChildAdded(1, newChild);
		}

		return true;
	}

	/** Set the middle child of this node.
	 *
	 * @return the middle child.
	 */
	@Pure
	public final N getMiddleChild() {
		return this.middle;
	}

	/** Set the right child of this node.
	 *
	 * @param newChild the new child.
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	public boolean setRightChild(N newChild) {
		final N oldChild = this.right;
		if (oldChild == newChild) {
			return false;
		}

		if (oldChild != null) {
			oldChild.setParentNodeReference(null, true);
			--this.notNullChildCount;
			firePropertyChildRemoved(2, oldChild);
		}

		if (newChild != null) {
			final N oldParent = newChild.getParentNode();
			if (oldParent != this) {
				newChild.removeFromParent();
			}
		}

		this.right = newChild;

		if (newChild != null) {
			newChild.setParentNodeReference(toN(), true);
			++this.notNullChildCount;
			firePropertyChildAdded(2, newChild);
		}

		return true;
	}

	/** Set the right child of this node.
	 *
	 * @return the right child.
	 */
	@Pure
	public final N getRightChild() {
		return this.right;
	}

	@Pure
	@Override
	public final boolean isLeaf() {
		return this.left == null && this.right == null && this.middle == null;
	}

	@Override
	public boolean moveTo(N newParent, int index) {
		return moveTo(newParent, index, false);
	}

	@Override
	public final boolean setChildAt(int index, N newChild) throws IndexOutOfBoundsException {
		switch (index) {
		case 0:
			return setLeftChild(newChild);
		case 1:
			return setMiddleChild(newChild);
		case 2:
			return setRightChild(newChild);
		default:
			throw new IndexOutOfBoundsException();
		}
	}

	@Override
	protected void setChildAtWithoutEventFiring(int index, N newChild) throws IndexOutOfBoundsException {
		switch (index) {
		case 0:
			if (this.left != null) {
				--this.notNullChildCount;
			}
			this.left = newChild;
			if (this.left != null) {
				++this.notNullChildCount;
			}
			break;
		case 1:
			if (this.middle != null) {
				--this.notNullChildCount;
			}
			this.middle = newChild;
			if (this.middle != null) {
				++this.notNullChildCount;
			}
			break;
		case 2:
			if (this.right != null) {
				--this.notNullChildCount;
			}
			this.right = newChild;
			if (this.right != null) {
				++this.notNullChildCount;
			}
			break;
		default:
			throw new IndexOutOfBoundsException();
		}
	}

	@Override
	public boolean removeChild(N child) {
		if (child != null) {
			if (child == this.left) {
				return setLeftChild(null);
			} else if (child == this.right) {
				return setRightChild(null);
			} else if (child == this.middle) {
				return setMiddleChild(null);
			}
		}
		return false;
	}

	@Pure
	@Override
	public final int indexOf(N child) {
		if (child == this.left) {
			return 0;
		}
		if (child == this.middle) {
			return 1;
		}
		if (child == this.right) {
			return 2;
		}
		return -1;
	}

	/** Returns true if the specified node is effectively a child of this node, false otherwise.
	 *
	 * @param potentialChild - the node to test
	 * @return a boolean, true if the specified node is effectively a child of this node, false otherwise
	 */
	@Pure
	public boolean hasChild(N potentialChild) {
		if ((this.left == potentialChild) || (this.middle == potentialChild) || (this.right == potentialChild)) {
			return true;
		}
		return false;
	}

	@Override
	public void getChildren(Object[] array) {
		if (array != null) {
			if (array.length > 0) {
				array[0] = this.left;
			}
			if (array.length > 1) {
				array[1] = this.middle;
			}
			if (array.length > 2) {
				array[2] = this.right;
			}
		}
	}

	@Pure
	@Override
	public int getMinHeight() {
		return 1 + MathUtil.min(
				this.left != null ? this.left.getMinHeight() : 0,
				this.middle != null ? this.middle.getMinHeight() : 0,
				this.right != null ? this.right.getMinHeight() : 0);
	}

	@Pure
	@Override
	public int getMaxHeight() {
		return 1 + MathUtil.max(
				this.left != null ? this.left.getMaxHeight() : 0,
				this.middle != null ? this.middle.getMaxHeight() : 0,
				this.right != null ? this.right.getMaxHeight() : 0);
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
		} else {
			if (this.left != null) {
				this.left.getHeights(currentHeight + 1, heights);
			}
			if (this.middle != null) {
				this.middle.getHeights(currentHeight + 1, heights);
			}
			if (this.right != null) {
				this.right.getHeights(currentHeight + 1, heights);
			}
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
	 * @since 13.0
	 */
	public static class DefaultTernaryTreeNode<D> extends TernaryTreeNode<D, DefaultTernaryTreeNode<D>> {

		private static final long serialVersionUID = 8673470473666658484L;

		/**
		 * Empty node.
		 */
		public DefaultTernaryTreeNode() {
			super();
		}

		/** Constructor.
		 * @param data are the initial user data.
		 */
		public DefaultTernaryTreeNode(Collection<D> data) {
			super(data);
		}

		/** Constructor.
		 * @param data are the initial user data.
		 */
		public DefaultTernaryTreeNode(D data) {
			super(data);
		}

	}

}
