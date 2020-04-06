/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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

package org.arakhne.afc.math.tree.iterator;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.tree.Tree;
import org.arakhne.afc.math.tree.TreeNode;

/**
 * This class is an iterator on a forest that replies the user data.
 *
 * <p>This iterator goes thru the trees in a breadth-first order.
 *
 * @param <D> is the type of the data inside the forest
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 18.0 (replacement for 13.0 and higher)
 */
public class DataBreadthFirstForestIterator<D> implements Iterator<D> {

	/** List of the node to treat.
	 */
	private final Queue<TreeNode<D, ?>> availableNodes = new LinkedList<>();

	/** List of the data to replies.
	 */
	private final Queue<DataPair<D>> availableData = new LinkedList<>();

	private DataPair<D> lastlyReplied;

	private boolean isStarted;

	/** Constructor.
	 * @param iterator is the iterator on the trees.
	 */
	public DataBreadthFirstForestIterator(Iterator<Tree<D, ?>> iterator) {
		assert iterator != null;
		Tree<D, ?> tree;
		TreeNode<D, ?> node;
		while (iterator.hasNext()) {
			tree = iterator.next();
			node = tree.getRoot();
			if (node != null) {
				this.availableNodes.offer(node);
			}
		}
	}

	private void startIterator() {
		final TreeNode<D, ?> root = this.availableNodes.poll();
		if (root != null) {
			searchNext(root);
		}
		this.isStarted = true;
	}

	private void searchNext(TreeNode<D, ?> parent) {
		TreeNode<D, ?> prt = parent;
		while (prt != null) {
			final int count = prt.getUserDataCount();
			for (int i = 0; i < count; ++i) {
				final D d = prt.getUserDataAt(i);
				if (d != null) {
					this.availableData.add(new DataPair<>(d, prt));
				}
			}

			final int childCount = prt.getChildCount();
			for (int i = 0; i < childCount; ++i) {
				final TreeNode<D, ?> child = prt.getChildAt(i);
				if (child != null) {
					this.availableNodes.offer(child);
				}
			}

			// avoid loop
			prt = null;

			if ((this.availableData.isEmpty())
					&& (!this.availableNodes.isEmpty())) {
				do {
					prt = this.availableNodes.poll();
				}
				while ((prt == null) && (!this.availableNodes.isEmpty()));
			}
		}
	}

	@Pure
	@Override
	public boolean hasNext() {
		if (!this.isStarted) {
			startIterator();
		}
		return !this.availableData.isEmpty();
	}

	@Override
	public D next() {
		if (!this.isStarted) {
			startIterator();
		}

		// If a data was found and not already replied
		// this function replies it.
		if (!this.availableData.isEmpty()) {
			this.lastlyReplied = this.availableData.poll();
			assert this.lastlyReplied != null && this.lastlyReplied.getData() != null;

			if ((this.availableData.isEmpty())
					&& (!this.availableNodes.isEmpty())) {
				final TreeNode<D, ?> newNode = this.availableNodes.poll();
				if (newNode != null) {
					searchNext(newNode);
				}
			}

			return this.lastlyReplied.getData();
		}

		throw new NoSuchElementException();
	}

	@Override
	public void remove() {
		final DataPair<D> pair = this.lastlyReplied;
		this.lastlyReplied = null;
		if (pair == null) {
			throw new NoSuchElementException();
		}
		assert pair.getData() != null;
		assert pair.getNode() != null;
		pair.getNode().removeUserData(pair.getData());
	}

	/** Internal data storage.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	private static class DataPair<D> {

		/** Data.
		 */
		private final D data;

		/** Node containing the data.
		 */
		private final TreeNode<D, ?> node;

		/** Construct the pair.
		 * @param data the data.
		 * @param node the node.
		 */
		DataPair(D data, TreeNode<D, ?> node) {
			this.data = data;
			this.node = node;
		}

		/** Replies the data.
		 *
		 * @return the data.
		 */
		public D getData() {
			return this.data;
		}

		/** Replies the node.
		 *
		 * @return the node.
		 */
		public TreeNode<D, ?> getNode() {
			return this.node;
		}

	}

}
