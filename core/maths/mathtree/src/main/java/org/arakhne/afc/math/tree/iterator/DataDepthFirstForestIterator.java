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
import java.util.NoSuchElementException;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.tree.Tree;

/**
 * This class is an iterator on a forest data.
 *
 * @param <D> is the type of the data inside the forest
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class DataDepthFirstForestIterator<D>
		implements Iterator<D> {

	private final int infixOrder;

	private final DepthFirstNodeOrder order;

	private final Iterator<Tree<D, ?>> trees;

	private Iterator<D> treeIterator;

	private DataPair<D> nextData;

	private DataPair<D> lastlyReplied;

	/** Constructor.
	 * @param order is the treatement order for data.
	 * @param iterator is the trees to iterate on.
	 */
	public DataDepthFirstForestIterator(DepthFirstNodeOrder order, Iterator<Tree<D, ?>> iterator) {
		this.infixOrder = -1;
		this.order = order;
		this.trees = iterator;
		this.treeIterator = null;
		searchNext();
	}

	/** Constructor.
	 * @param infixOrder1 is the order of the nodes when to treat data according to an infixed iteration.
	 * @param iterator is the trees to iterate on.
	 */
	public DataDepthFirstForestIterator(int infixOrder1, Iterator<Tree<D, ?>> iterator) {
		this.infixOrder = infixOrder1;
		this.order = null;
		this.trees = iterator;
		this.treeIterator = null;
		searchNext();
	}

	private void searchNext() {
		Tree<D, ?> tree;
		this.nextData = null;
		while ((this.treeIterator == null || !this.treeIterator.hasNext())
			   && this.trees.hasNext()) {
			tree = this.trees.next();
			assert tree != null;
			if (this.order == null) {
				this.treeIterator = tree.dataDepthFirstIterator(this.infixOrder);
			} else {
				this.treeIterator = tree.dataDepthFirstIterator(this.order);
			}
		}
		if (this.treeIterator != null && this.treeIterator.hasNext()) {
			this.nextData = new DataPair<>(this.treeIterator.next(), this.treeIterator);
			assert this.nextData != null;
		}
	}

	@Override
	public void remove() {
		final DataPair<D> pair = this.lastlyReplied;
		this.lastlyReplied = null;
		if (pair == null) {
			throw new NoSuchElementException();
		}
		assert pair.getData() != null;
		assert pair.getIterator() != null;
		pair.getIterator().remove();
	}

	@Pure
	@Override
	public boolean hasNext() {
		return this.nextData != null;
	}

	@Override
	public D next() {
		this.lastlyReplied = this.nextData;
		if (this.lastlyReplied == null || this.lastlyReplied.getData() == null) {
			throw new NoSuchElementException();
		}
		searchNext();
		return this.lastlyReplied.getData();
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

		/** Iterator that has replied the data.
		 */
		private final Iterator<D> iterator;

		/** Construct the data pair.
		 *
		 * @param data the data.
		 * @param iterator the iterator.
		 */
		DataPair(D data, Iterator<D> iterator) {
			this.data = data;
			this.iterator = iterator;
		}

		/** Replies the data.
		 *
		 * @return the data.
		 */
		public D getData() {
			return this.data;
		}

		/** Replies the iterator.
		 *
		 * @return the iterator.
		 */
		public Iterator<D> getIterator() {
			return this.iterator;
		}

	}

}
