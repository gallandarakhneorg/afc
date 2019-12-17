/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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
 * This is the generic implementation of a
 * tree for which each node has five children.
 *
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
 * @since 13.0
 */
public abstract class PentaTreeNode<D, N extends PentaTreeNode<D, N>> extends AbstractTreeNode<D, N> {

	private static final long serialVersionUID = -6999579785485666574L;

	private N child1;

	private N child2;

	private N child3;

	private N child4;

	private N child5;

	/**
	 * Empty node.
	 */
	public PentaTreeNode() {
		this(DEFAULT_LINK_LIST_USE);
	}

	/** Construct a node.
	 * @param data are initial user data
	 */
	public PentaTreeNode(Collection<D> data) {
		super(DEFAULT_LINK_LIST_USE, data);
		this.child1 = null;
		this.child2 = null;
		this.child3 = null;
		this.child4 = null;
		this.child5 = null;
	}

	/** Construct a node.
	 * @param data are initial user data
	 */
	public PentaTreeNode(D data) {
		this(DEFAULT_LINK_LIST_USE, data);
	}

	/** Constructor.
	 * @param useLinkedList indicates if a linked list must be used to store the data.
	 *     If <code>false</code>, an ArrayList will be used.
	 */
	public PentaTreeNode(boolean useLinkedList) {
		super(useLinkedList);
		this.child1 = null;
		this.child2 = null;
		this.child3 = null;
		this.child4 = null;
		this.child5 = null;
	}

	/** Constructor.
	 * @param useLinkedList indicates if a linked list must be used to store the data.
	 *     If <code>false</code>, an ArrayList will be used.
	 * @param copyDataCollection indicates if the given data collection is copied
	 *     if <code>true</code> or the inner data collection will be the given
	 *     collection itself if <code>false</code>.
	 * @param data are initial user data
	 */
	public PentaTreeNode(boolean useLinkedList, boolean copyDataCollection, List<D> data) {
		super(useLinkedList, copyDataCollection, data);
		this.child1 = null;
		this.child2 = null;
		this.child3 = null;
		this.child4 = null;
		this.child5 = null;
	}

	/** Constructor.
	 * @param useLinkedList indicates if a linked list must be used to store the data.
	 *     If <code>false</code>, an ArrayList will be used.
	 * @param data are initial user data
	 */
	public PentaTreeNode(boolean useLinkedList, D data) {
		super(useLinkedList, data);
		this.child1 = null;
		this.child2 = null;
		this.child3 = null;
		this.child4 = null;
		this.child5 = null;
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
		if (this.child1 != null) {
			this.child1.setParentNodeReference(me, false);
		}
		if (this.child2 != null) {
			this.child2.setParentNodeReference(me, false);
		}
		if (this.child3 != null) {
			this.child3.setParentNodeReference(me, false);
		}
		if (this.child4 != null) {
			this.child4.setParentNodeReference(me, false);
		}
		if (this.child5 != null) {
			this.child5.setParentNodeReference(me, false);
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
	@SuppressWarnings("checkstyle:magicnumber")
	public void clear() {
		if (this.child1 != null) {
			final N child = this.child1;
			setChildAt(0, null);
			child.clear();
		}
		if (this.child2 != null) {
			final N child = this.child2;
			setChildAt(1, null);
			child.clear();
		}
		if (this.child3 != null) {
			final N child = this.child3;
			setChildAt(2, null);
			child.clear();
		}
		if (this.child4 != null) {
			final N child = this.child4;
			setChildAt(3, null);
			child.clear();
		}
		if (this.child5 != null) {
			final N child = this.child5;
			setChildAt(4, null);
			child.clear();
		}
		removeAllUserData();
	}

	@Pure
	@Override
	@SuppressWarnings("checkstyle:magicnumber")
	public int getChildCount() {
		return 5;
	}

	@Pure
	@Override
	public int getNotNullChildCount() {
		return this.notNullChildCount;
	}

	@Pure
	@Override
	@SuppressWarnings("checkstyle:magicnumber")
	public N getChildAt(int index) throws IndexOutOfBoundsException {
		switch (index) {
		case 0:
			return this.child1;
		case 1:
			return this.child2;
		case 2:
			return this.child3;
		case 3:
			return this.child4;
		case 4:
			return this.child5;
		default:
		}
		throw new IndexOutOfBoundsException();
	}

	@Pure
	@Override
	public boolean isLeaf() {
		return this.child1 == null && this.child2 == null
				&& this.child3 == null && this.child4 == null
				&& this.child5 == null;
	}

	/** Set the child 1.
	 *
	 * @param newChild is the new child
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	private boolean setChild1(N newChild) {
		if (this.child1 == newChild) {
			return false;
		}

		if (this.child1 != null) {
			this.child1.setParentNodeReference(null, true);
			--this.notNullChildCount;
			firePropertyChildRemoved(0, this.child1);
		}

		if (newChild != null) {
			final N oldParent = newChild.getParentNode();
			if (oldParent != this) {
				newChild.removeFromParent();
			}
		}

		this.child1 = newChild;

		if (newChild != null) {
			newChild.setParentNodeReference(toN(), true);
			++this.notNullChildCount;
			firePropertyChildAdded(0, newChild);
		}

		return true;
	}

	/** Set the child 2.
	 *
	 * @param newChild is the new child
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	private boolean setChild2(N newChild) {
		if (this.child2 == newChild) {
			return false;
		}

		if (this.child2 != null) {
			this.child2.setParentNodeReference(null, true);
			--this.notNullChildCount;
			firePropertyChildRemoved(1, this.child2);
		}

		if (newChild != null) {
			final N oldParent = newChild.getParentNode();
			if (oldParent != this) {
				newChild.removeFromParent();
			}
		}

		this.child2 = newChild;

		if (newChild != null) {
			newChild.setParentNodeReference(toN(), true);
			++this.notNullChildCount;
			firePropertyChildAdded(1, newChild);
		}

		return true;
	}

	/** Set the child 3.
	 *
	 * @param newChild is the new child
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	private boolean setChild3(N newChild) {
		if (this.child3 == newChild) {
			return false;
		}

		if (this.child3 != null) {
			this.child3.setParentNodeReference(null, true);
			--this.notNullChildCount;
			firePropertyChildRemoved(2, this.child3);
		}

		if (newChild != null) {
			final N oldParent = newChild.getParentNode();
			if (oldParent != this) {
				newChild.removeFromParent();
			}
		}

		this.child3 = newChild;

		if (newChild != null) {
			newChild.setParentNodeReference(toN(), true);
			++this.notNullChildCount;
			firePropertyChildAdded(2, newChild);
		}

		return true;
	}

	/** Set the child 4.
	 *
	 * @param newChild is the new child
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	private boolean setChild4(N newChild) {
		if (this.child4 == newChild) {
			return false;
		}

		if (this.child4 != null) {
			this.child4.setParentNodeReference(null, true);
			--this.notNullChildCount;
			firePropertyChildRemoved(3, this.child4);
		}

		if (newChild != null) {
			final N oldParent = newChild.getParentNode();
			if (oldParent != this) {
				newChild.removeFromParent();
			}
		}

		this.child4 = newChild;

		if (newChild != null) {
			newChild.setParentNodeReference(toN(), true);
			++this.notNullChildCount;
			firePropertyChildAdded(3, newChild);
		}

		return true;
	}

	/** Set the child 5.
	 *
	 * @param newChild is the new child
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	@SuppressWarnings("checkstyle:magicnumber")
		private boolean setChild5(N newChild) {
		if (this.child5 == newChild) {
			return false;
		}

		if (this.child5 != null) {
			this.child5.setParentNodeReference(null, true);
			--this.notNullChildCount;
			firePropertyChildRemoved(4, this.child5);
		}

		if (newChild != null) {
			final N oldParent = newChild.getParentNode();
			if (oldParent != this) {
				newChild.removeFromParent();
			}
		}

		this.child5 = newChild;

		if (newChild != null) {
			newChild.setParentNodeReference(toN(), true);
			++this.notNullChildCount;
			firePropertyChildAdded(4, newChild);
		}

		return true;
	}

	@Override
	public boolean moveTo(N newParent, int index) {
		return moveTo(newParent, index, false);
	}

	@Override
	@SuppressWarnings("checkstyle:magicnumber")
		public boolean setChildAt(int index, N newChild) throws IndexOutOfBoundsException {
		switch (index) {
		case 0:
			return setChild1(newChild);
		case 1:
			return setChild2(newChild);
		case 2:
			return setChild3(newChild);
		case 3:
			return setChild4(newChild);
		case 4:
			return setChild5(newChild);
		default:
		}
		throw new IndexOutOfBoundsException();
	}

	@Override
	@SuppressWarnings({"checkstyle:magicnumber", "checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity"})
	protected void setChildAtWithoutEventFiring(int index, N newChild) throws IndexOutOfBoundsException {
		switch (index) {
		case 0:
			if (this.child1 != null) {
				--this.notNullChildCount;
			}
			this.child1 = newChild;
			if (this.child1 != null) {
				++this.notNullChildCount;
			}
			break;
		case 1:
			if (this.child2 != null) {
				--this.notNullChildCount;
			}
			this.child2 = newChild;
			if (this.child2 != null) {
				++this.notNullChildCount;
			}
			break;
		case 2:
			if (this.child3 != null) {
				--this.notNullChildCount;
			}
			this.child3 = newChild;
			if (this.child3 != null) {
				++this.notNullChildCount;
			}
			break;
		case 3:
			if (this.child4 != null) {
				--this.notNullChildCount;
			}
			this.child4 = newChild;
			if (this.child4 != null) {
				++this.notNullChildCount;
			}
			break;
		case 4:
			if (this.child5 != null) {
				--this.notNullChildCount;
			}
			this.child5 = newChild;
			if (this.child5 != null) {
				++this.notNullChildCount;
			}
			break;
		default:
			throw new IndexOutOfBoundsException();
		}
	}

	@Override
	@SuppressWarnings("checkstyle:magicnumber")
	public boolean removeChild(N child) {
		if (child != null) {
			if (child == this.child1) {
				return setChildAt(0, null);
			} else if (child == this.child2) {
				return setChildAt(1, null);
			} else if (child == this.child3) {
				return setChildAt(2, null);
			} else if (child == this.child4) {
				return setChildAt(3, null);
			} else if (child == this.child5) {
				return setChildAt(4, null);
			}
		}
		return false;
	}

	@Pure
	@Override
	@SuppressWarnings("checkstyle:magicnumber")
	public int indexOf(N child) {
		if (child == this.child1) {
			return 0;
		}
		if (child == this.child2) {
			return 1;
		}
		if (child == this.child3) {
			return 2;
		}
		if (child == this.child4) {
			return 3;
		}
		if (child == this.child5) {
			return 4;
		}
		return -1;
	}

	@Override
	@SuppressWarnings("checkstyle:magicnumber")
	public void getChildren(Object[] array) {
		if (array != null) {
			if (array.length > 0) {
				array[0] = this.child1;
			}
			if (array.length > 1) {
				array[1] = this.child2;
			}
			if (array.length > 2) {
				array[2] = this.child3;
			}
			if (array.length > 3) {
				array[3] = this.child4;
			}
			if (array.length > 4) {
				array[4] = this.child5;
			}
		}
	}

	@Pure
	@Override
	public int getMinHeight() {
		return 1 + MathUtil.min(
				this.child1 != null ? this.child1.getMinHeight() : 0,
				this.child2 != null ? this.child2.getMinHeight() : 0,
				this.child3 != null ? this.child3.getMinHeight() : 0,
				this.child4 != null ? this.child4.getMinHeight() : 0,
				this.child5 != null ? this.child5.getMinHeight() : 0);
	}

	@Override
	@Pure
	public int getMaxHeight() {
		return 1 + MathUtil.max(
				this.child1 != null ? this.child1.getMaxHeight() : 0,
				this.child2 != null ? this.child2.getMaxHeight() : 0,
				this.child3 != null ? this.child3.getMaxHeight() : 0,
				this.child4 != null ? this.child4.getMaxHeight() : 0,
				this.child5 != null ? this.child5.getMaxHeight() : 0);
	}

	@Override
	protected void getHeights(int currentHeight, List<Integer> heights) {
		if (isLeaf()) {
			heights.add(new Integer(currentHeight));
		} else {
			if (this.child1 != null) {
				this.child1.getHeights(currentHeight + 1, heights);
			}
			if (this.child2 != null) {
				this.child2.getHeights(currentHeight + 1, heights);
			}
			if (this.child3 != null) {
				this.child3.getHeights(currentHeight + 1, heights);
			}
			if (this.child4 != null) {
				this.child4.getHeights(currentHeight + 1, heights);
			}
			if (this.child5 != null) {
				this.child5.getHeights(currentHeight + 1, heights);
			}
		}
	}

	/**
	 * This is the generic implementation of a quad
	 * tree.
	 *
	 * @param <D> is the type of the data inside the tree
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	public static class DefaultPentaTreeNode<D> extends PentaTreeNode<D, DefaultPentaTreeNode<D>> {

		private static final long serialVersionUID = -8015622788720736037L;

		/** Construct a node.
		 */
		public DefaultPentaTreeNode() {
			super();
		}

		/** Construct a node.
		 * @param data are the initial user data
		 */
		public DefaultPentaTreeNode(Collection<D> data) {
			super(data);
		}

		/** Construct a node.
		 * @param data are the initial user data
		 */
		public DefaultPentaTreeNode(D data) {
			super(data);
		}

	}

}
