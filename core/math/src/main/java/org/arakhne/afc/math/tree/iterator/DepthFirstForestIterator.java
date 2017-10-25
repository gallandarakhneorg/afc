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

package org.arakhne.afc.math.tree.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.tree.Tree;
import org.arakhne.afc.math.tree.TreeNode;

/**
 * This class is an iterator on a forest nodes.
 *
 * @param <D> is the type of the data inside the forest
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class DepthFirstForestIterator<D>
		implements Iterator<TreeNode<D, ?>> {

	private final DepthFirstNodeOrder order;

	private final Iterator<Tree<D, ?>> trees;

	private Iterator<? extends TreeNode<D, ?>> treeIterator;

	private TreeNode<D, ?> nextNode;

	/**
	 * @param order is the treatement order for data.
	 * @param iterator is the trees to iterate on.
	 */
	public DepthFirstForestIterator(DepthFirstNodeOrder order, Iterator<Tree<D, ?>> iterator) {
		assert order != null;
		this.order = order;
		this.trees = iterator;
		this.treeIterator = null;
		searchNext();
	}

	private void searchNext() {
		Tree<D, ?> tree;
		this.nextNode = null;
		while ((this.treeIterator == null || !this.treeIterator.hasNext())
			   && this.trees.hasNext()) {
			tree = this.trees.next();
			assert tree != null;
			this.treeIterator = tree.depthFirstIterator(this.order);
		}
		if (this.treeIterator != null && this.treeIterator.hasNext()) {
			this.nextNode = this.treeIterator.next();
			assert this.nextNode != null;
		}
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	@Pure
	@Override
	public boolean hasNext() {
		return this.nextNode != null;
	}

	@Override
	public TreeNode<D, ?> next() {
		final TreeNode<D, ?> d = this.nextNode;
		if (d == null) {
			throw new NoSuchElementException();
		}
		searchNext();
		return d;
	}

}
