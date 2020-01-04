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

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.tree.Tree;
import org.arakhne.afc.math.tree.TreeNode;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/**
 * This class is an iterator on a forest that replies the tree nodes.
 *
 * <p>This iterator goes thru the trees in a broad-first order.
 *
 * @param <D> is the type of the data inside the forest
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class BroadFirstForestIterator<D>
		implements Iterator<TreeNode<D, ?>> {

	/** List of the node to treat.
	 */
	private final Queue<TreeNode<D, ?>> availableNodes = new LinkedList<>();

	private boolean isStarted;

	/** Constructor.
	 * @param iterator is the iterator on the trees.
	 */
	public BroadFirstForestIterator(Iterator<Tree<D, ?>> iterator) {
		assert iterator != null : AssertMessages.notNullParameter();
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
			this.availableNodes.offer(root);
		}
		this.isStarted = true;
	}

	@Pure
	@Override
	public boolean hasNext() {
		if (!this.isStarted) {
			startIterator();
		}
		return !this.availableNodes.isEmpty();
	}

	@Override
	public TreeNode<D, ?> next() {
		if (!this.isStarted) {
			startIterator();
		}
		if (this.availableNodes.isEmpty()) {
			throw new NoSuchElementException();
		}

		final TreeNode<D, ?> current = this.availableNodes.poll();

		if (current == null) {
			throw new ConcurrentModificationException();
		}

		// Add the children of the polled element
		final int childCount = current.getChildCount();
		for (int i = 0; i < childCount; ++i) {
			try {
				final TreeNode<D, ?> child = current.getChildAt(i);
				if (child != null) {
					this.availableNodes.offer(child);
				}
			} catch (IndexOutOfBoundsException e) {
				throw new ConcurrentModificationException(e);
			}
		}

		return current;
	}

}
