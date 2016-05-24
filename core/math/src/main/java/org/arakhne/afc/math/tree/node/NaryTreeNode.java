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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.tree.TreeNode;


/**
 * This is the generic implementation of a n-ary
 * tree. This node has not a constant count of children.
 * Indeed when a child node was removed, the count of children
 * is also changed. It also means that a child node is never
 * <code>null</code>. If you want a generic implementation
 * of a tree node which has always the same count of children,
 * please see {@link ConstantNaryTreeNode}.
 *
 * <p><h3>moveTo</h3>
 * According to its definition in
 * {@link TreeNode#moveTo(TreeNode, int)}, the binary
 * tree node implementation of <code>moveTo</code>
 * does not replace any existing node at the position given as
 * parameter of <code>moveTo</code>. In place <code>moveTo</code>
 * insert the moving node in the new parent.
 *
 * @param <D> is the type of the data inside the tree
 * @param <N> is the type of the tree nodes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 * @see ConstantNaryTreeNode
 */
public abstract class NaryTreeNode<D, N extends NaryTreeNode<D, N>> extends AbstractTreeNode<D, N> {

	private static final long serialVersionUID = -1313340976961548532L;

	private List<N> children;

	/**
	 * Empty node.
	 */
	public NaryTreeNode() {
		this(DEFAULT_LINK_LIST_USE);
	}

	/**
	 * @param data are the initial user data.
	 */
	public NaryTreeNode(Collection<D> data) {
		super(DEFAULT_LINK_LIST_USE, data);
		this.children = null;
	}

	/**
	 * @param data are the initial user data.
	 */
	public NaryTreeNode(D data) {
		this(DEFAULT_LINK_LIST_USE, data);
	}

	/**
	 * @param useLinkedList indicates if a linked list must be used to store the data.
	 *     If <code>false</code>, an ArrayList will be used.
	 */
	public NaryTreeNode(boolean useLinkedList) {
		super(useLinkedList);
		this.children = null;
	}

	/**
	 * @param useLinkedList indicates if a linked list must be used to store the data.
	 *     If <code>false</code>, an ArrayList will be used.
	 * @param copyDataCollection indicates if the given data collection is copied
	 *     if <code>true</code> or the inner data collection will be the given
	 *     collection itself if <code>false</code>.
	 * @param data are the initial user data
	 */
	public NaryTreeNode(boolean useLinkedList, boolean copyDataCollection, List<D> data) {
		super(useLinkedList, copyDataCollection, data);
		this.children = null;
	}

	/**
	 * @param useLinkedList indicates if a linked list must be used to store the data.
	 *     If <code>false</code>, an ArrayList will be used.
	 * @param data are the initial user data
	 */
	public NaryTreeNode(boolean useLinkedList, D data) {
		super(useLinkedList, data);
		this.children = null;
	}

	@Pure
	@Override
	public Class<? extends Enum<?>> getPartitionEnumeration() {
		return null;
	}

	private List<N> newInternalList(int size) {
		return new ArrayList<>(size);
	}

	/** Invoked when this object must be deserialized.
	 *
	 * @param in is the input stream.
	 * @throws IOException in case of input stream access error.
	 * @throws ClassNotFoundException if some class was not found.
	 */
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		if (this.children != null) {
			final N me = toN();
			for (final NaryTreeNode<D, N> child : this.children) {
				if (child != null) {
					child.setParentNodeReference(me, false);
				}
			}
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
		removeAllUserData();
		if (this.children != null) {
			final List<N> nodes = new ArrayList<>(this.children);
			while (!this.children.isEmpty()) {
				setChildAt(0, null);
			}
			for (final N child : nodes) {
				child.clear();
			}
			nodes.clear();
		}
	}

	@Pure
	@Override
	public final int getChildCount() {
		return (this.children == null) ? 0 : this.children.size();
	}

	@Pure
	@Override
	public int getNotNullChildCount() {
		return this.notNullChildCount;
	}

	@Pure
	@Override
	public final int indexOf(N child) {
		int i = 0;
		if (this.children != null) {
			for (final N cchild : this.children) {
				if (cchild == child) {
					return i;
				}
				++i;
			}
		}
		return -1;
	}

	@Pure
	@Override
	public final N getChildAt(int index) throws IndexOutOfBoundsException {
		if (this.children != null) {
			return this.children.get(index);
		}
		throw new IndexOutOfBoundsException(index + ">= 3"); //$NON-NLS-1$
	}

	/** Move this node in the given new parent node.
	 *
	 * <p>This function adds this node at the end of the list
	 * of the children of the new parent node.
	 *
	 * <p>This function is preferred to a sequence of calls
	 * to {@link #removeFromParent()} and {@link #setChildAt(int, NaryTreeNode)}
	 * because it fires a limited set of events dedicated to the move
	 * the node.
	 *
	 * @param newParent is the new parent for this node.
	 * @return <code>true</code> on success, otherwise <code>false</code>.
	 */
	public boolean moveTo(N newParent) {
		if (newParent == null) {
			return false;
		}
		return moveTo(newParent, newParent.getChildCount());
	}

	@Override
	public boolean moveTo(N newParent, int index) {
		return moveTo(newParent, index, true);
	}

	@Override
	@SuppressWarnings("checkstyle:npathcomplexity")
	public boolean setChildAt(int index, N newChild) throws IndexOutOfBoundsException {
		final int count = (this.children == null) ? 0 : this.children.size();

		if (index < 0 || index >= count) {
			throw new IndexOutOfBoundsException();
		}

		final N oldChild = (index < count) ? this.children.get(index) : null;
		if (oldChild == newChild) {
			return false;
		}

		if (oldChild != null) {
			oldChild.setParentNodeReference(null, true);
			--this.notNullChildCount;
			firePropertyChildRemoved(index, oldChild);
		}

		if (newChild != null) {
			final N oldParent = newChild.getParentNode();
			if (oldParent != this) {
				newChild.removeFromParent();
			}
		}

		if (index < count) {
			// set the element
			if (newChild != null) {
				this.children.set(index, newChild);
			} else {
				this.children.remove(index);
			}
		} else if (newChild != null) {
			// Resize the array
			if (this.children == null) {
				this.children = newInternalList(index + 1);
			}
			this.children.add(newChild);
		}

		if (newChild != null) {
			newChild.setParentNodeReference(toN(), true);
			++this.notNullChildCount;
			firePropertyChildAdded(index, newChild);
		}

		return true;
	}

	@Override
	protected void setChildAtWithoutEventFiring(int index, N newChild) throws IndexOutOfBoundsException {
		final int count = (this.children == null) ? 0 : this.children.size();
		int insertionIndex = index;
		if (insertionIndex < 0) {
			insertionIndex = 0;
		}

		if (newChild != null) {
			if (insertionIndex > count) {
				insertionIndex = count;
			}

			if (this.children == null) {
				this.children = newInternalList(count + 1);
			}
			this.children.add(insertionIndex, newChild);
			++this.notNullChildCount;
		} else {
			if (insertionIndex >= count) {
				insertionIndex = count - 1;
			}
			final N oldValue = this.children.remove(insertionIndex);
			if (this.children.isEmpty()) {
				this.children = null;
			}
			if (oldValue != null) {
				--this.notNullChildCount;
			}
		}
	}

	@Override
	public final boolean removeChild(N child) {
		if (child != null && this.children != null) {
			final int index = this.children.indexOf(child);
			if (index >= 0 && index < this.children.size()) {
				this.children.remove(index);
				--this.notNullChildCount;
				child.setParentNodeReference(null, true);
				firePropertyChildRemoved(index, child);
				return true;
			}
		}
		return false;
	}

	/** Add a child node at the end of the children list.
	 *
	 * @param child is the new child to insert.
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	public final boolean addChild(N child) {
		if (child != null) {
			return addChild(getChildCount(), child);
		}
		return false;
	}

	/** Add a child node at the specified index.
	 *
	 * @param index is the insertion index.
	 * @param newChild is the new child to insert.
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	public final boolean addChild(int index, N newChild) {
		if (newChild == null) {
			return false;
		}

		final int count = (this.children == null) ? 0 : this.children.size();

		final N oldParent = newChild.getParentNode();
		if (oldParent != this && oldParent != null) {
			newChild.removeFromParent();
		}

		final int insertionIndex;

		if (this.children == null) {
			this.children = newInternalList(index + 1);
		}

		if (index < count) {
			// set the element
			insertionIndex = Math.max(index, 0);
			this.children.add(insertionIndex, newChild);
		} else {
			// Resize the array
			insertionIndex = this.children.size();
			this.children.add(newChild);
		}

		++this.notNullChildCount;

		newChild.setParentNodeReference(toN(), true);
		firePropertyChildAdded(insertionIndex, newChild);

		return true;
	}

	@Pure
	@Override
	public final boolean isLeaf() {
		return (this.children == null) || this.children.isEmpty();
	}

	@Override
	public void getChildren(Object[] array) {
		if (array != null && this.children != null) {
			this.children.toArray(array);
		}
	}

	@Pure
	@Override
	public int getMinHeight() {
		int min = Integer.MAX_VALUE;
		boolean set = false;
		if (this.children != null) {
			for (final N child : this.children) {
				if (child != null) {
					if (set) {
						min = Math.min(min, child.getMinHeight());
					} else {
						min = child.getMinHeight();
						set = true;
					}
				}
			}
		}
		return 1 + (set ? min : 0);
	}

	@Pure
	@Override
	public int getMaxHeight() {
		int max = Integer.MIN_VALUE;
		boolean set = false;
		if (this.children != null) {
			for (final N child : this.children) {
				if (child != null) {
					if (set) {
						max = Math.max(max, child.getMaxHeight());
					} else {
						max = child.getMaxHeight();
						set = true;
					}
				}
			}
		}
		return 1 + (set ? max : 0);
	}

	@Override
	protected void getHeights(int currentHeight, List<Integer> heights) {
		if (isLeaf()) {
			heights.add(new Integer(currentHeight));
		} else {
			for (final N child : this.children) {
				if (child != null) {
					child.getHeights(currentHeight + 1, heights);
				}
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
	 * @since 13.0
	 */
	public static class DefaultNaryTreeNode<D> extends NaryTreeNode<D, DefaultNaryTreeNode<D>> {

		private static final long serialVersionUID = -6775932201459338929L;

		/**
		 * Empty node.
		 */
		public DefaultNaryTreeNode() {
			super();
		}

		/** Construct a node.
		 * @param data are the initial user data
		 */
		public DefaultNaryTreeNode(Collection<D> data) {
			super(data);
		}

		/** Construct a node.
		 * @param data are the initial user data
		 */
		public DefaultNaryTreeNode(D data) {
			super(data);
		}

	}

}
