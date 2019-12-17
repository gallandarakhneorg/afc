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

package org.arakhne.afc.math.tree;

import java.io.IOException;
import java.io.ObjectInputStream;

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * This is the generic implementation of a
 * tree based on linked lists.
 *
 * <p>This tree assumes that the nodes are linked with there
 * references.
 *
 * @param <D> is the type of the data inside the tree
 * @param <N> is the type of the tree nodes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class LinkedTree<D, N extends TreeNode<D, N>> extends AbstractTree<D, N> {

	private static final long serialVersionUID = 8805713324128349425L;

	/** Root of the tree.
	 */
	private N root;

	/** Count of nodes.
	 */
	private int nodeCount;

	/** Count of user data.
	 */
	private int dataCount;

	/** Event listener.
	 */
	private transient Listener listener;

	/**
	 * Create an emtpy tree.
	 */
	public LinkedTree() {
		this(null);
	}

	/** Create a tree with the given node.
	 *
	 * @param root1 is the root.
	 */
	public LinkedTree(N root1) {
		init(root1);
	}

	private void init(N rootNode) {
		this.listener = new Listener();
		setRoot(rootNode);
	}

	/** Invoked when this object must be deserialized.
	 *
	 * @param in is the input stream.
	 * @throws IOException in case of input stream access error.
	 * @throws ClassNotFoundException if some class was not found.
	 */
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		if (this.root != null) {
			this.root.removeFromParent();
			this.root.addTreeNodeListener(this.listener);
		}
	}

	@Override
	@Pure
	public N getRoot() {
		return this.root;
	}

	@Override
	public void setRoot(N newRoot) {
		if (newRoot == this.root) {
			return;
		}
		if (this.root != null) {
			this.root.removeTreeNodeListener(this.listener);
		}
		this.root = newRoot;
		if (this.root != null) {
			this.root.removeFromParent();
			this.nodeCount = this.root.getDeepNodeCount();
			this.dataCount = this.root.getDeepUserDataCount();
			this.root.addTreeNodeListener(this.listener);
		} else {
			this.nodeCount = 0;
			this.dataCount = 0;
		}
	}

	/** Clear the tree.
	 *
	 * <p>Caution: this method also destroyes the
	 * links between the nodes inside the tree.
	 * If you want to unlink the root node with
	 * this tree but leave the nodes' links
	 * unchanged, please call <code>setRoot(null)</code>.
	 */
	@Override
	public void clear() {
		if (this.root != null) {
			this.root.clear();
			setRoot(null);
		}
	}

	@Override
	@Pure
	public int getNodeCount() {
		return this.nodeCount;
	}

	@Override
	@Pure
	public int getUserDataCount() {
		return this.dataCount;
	}

	/** Force the computation of the user data count.
	 *
	 * @return the count of data.
	 */
	public int computeUserDataCount() {
		if (this.root != null) {
			this.dataCount = this.root.getDeepUserDataCount();
		} else {
			this.dataCount = 0;
		}
		return this.dataCount;
	}

	@Override
	@Pure
	public int getMinHeight() {
		return (this.root != null) ? this.root.getMinHeight() : 0;
	}

	@Override
	@Pure
	public int getMaxHeight() {
		return (this.root != null) ? this.root.getMaxHeight() : 0;
	}

	@Override
	@Pure
	public int[] getHeights() {
		return (this.root != null) ? this.root.getHeights() : new int[] {0};
	}

	/**
	 * This is the generic implementation of a
	 * tree based on linked lists.
	 *
	 * <p>This tree assumes that the nodes are linked with there
	 * references.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	private class Listener implements TreeNodeListener {

		/** Construct a listener.
		 */
		Listener() {
			//
		}

		@Override
		public void treeNodeChildAdded(TreeNodeAddedEvent event) {
			final TreeNode<?, ?> childNode = event.getChild();
			LinkedTree.this.nodeCount += childNode.getDeepNodeCount();
			LinkedTree.this.dataCount += childNode.getDeepUserDataCount();
		}

		@Override
		public void treeNodeChildRemoved(TreeNodeRemovedEvent event) {
			final TreeNode<?, ?> childNode = event.getChild();
			LinkedTree.this.nodeCount -= childNode.getDeepNodeCount();
			LinkedTree.this.dataCount -= childNode.getDeepUserDataCount();
		}

		@Override
		public void treeNodeDataChanged(TreeDataEvent event) {
			final int oldCount = event.getRemovedValueCount();
			final int newCount = event.getAddedValueCount();
			if (oldCount < newCount) {
				LinkedTree.this.dataCount += newCount - oldCount;
			} else if (oldCount > newCount) {
				LinkedTree.this.dataCount -= oldCount - newCount;
			}
		}

		@Override
		public void treeNodeParentChanged(TreeNodeParentChangedEvent event) {
			//
		}

	}

}
