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

package org.arakhne.afc.math.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.tree.iterator.BroadFirstForestIterator;
import org.arakhne.afc.math.tree.iterator.DataBroadFirstForestIterator;
import org.arakhne.afc.math.tree.iterator.DataDepthFirstForestIterator;
import org.arakhne.afc.math.tree.iterator.DepthFirstForestIterator;
import org.arakhne.afc.math.tree.iterator.DepthFirstNodeOrder;


/**
 * This is the generic implementation of a
 * forest of trees.
 *
 * <p>A forest of trees is a collection of trees.
 *
 * @param <D> is the type of the data inside the forest
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public abstract class AbstractForest<D> implements Forest<D> {

	/**
	 * Indicates if the forests are using a linked list
	 * by default to store the trees.
	 */
	public static final boolean USE_LINKED_LIST = false;

	/** List of trees.
	 */
	private final List<Tree<D, ?>> trees;

	private Collection<ForestListener> listeners;

	/**
	 * @param internalList is the internal list to use.
	 */
	protected AbstractForest(List<Tree<D, ?>> internalList) {
		assert internalList != null;
		this.trees = internalList;
	}

	/**
	 * @param internalList is the internal list to use.
	 * @param trees is the trees to put inside the forest.
	 */
	protected AbstractForest(List<Tree<D, ?>> internalList, Collection<? extends Tree<D, ?>> trees) {
		assert internalList != null;
		assert trees != null;
		this.trees = internalList;
		this.trees.addAll(trees);
	}

	@Override
	@Pure
	public final int getMinHeight() {
		if (this.trees.isEmpty()) {
			return 0;
		}
		int height;
		int min = Integer.MAX_VALUE;
		for (final Tree<D, ?> tree : this.trees) {
			height = tree.getMinHeight();
			if (height < min) {
				min = height;
			}
		}
		return min;
	}

	@Override
	@Pure
	public final int getMaxHeight() {
		if (this.trees.isEmpty()) {
			return 0;
		}
		int height;
		int max = 0;
		for (final Tree<D, ?> tree : this.trees) {
			height = tree.getMaxHeight();
			if (height > max) {
				max = height;
			}
		}
		return max;
	}

	@Override
	@Pure
	public final int[] getHeights() {
		int[] array = new int[0];
		for (final Tree<D, ?> tree : this.trees) {
			final int[] a = tree.getHeights();
			if (a != null && a.length > 0) {
				final int[] b = new int[array.length + a.length];
				System.arraycopy(array, 0, b, 0, array.length);
				System.arraycopy(a, 0, b, array.length, a.length);
				array = b;
			}
		}
		return array;
	}

	@Override
	@Pure
	public final Iterator<TreeNode<D, ?>> depthFirstIterator(DepthFirstNodeOrder nodeOrder) {
		return new DepthFirstForestIterator<>(
				nodeOrder,
				this.trees.iterator());
	}

	@Override
	@Pure
	public final Iterator<TreeNode<D, ?>> depthFirstIterator() {
		return new DepthFirstForestIterator<>(
				DepthFirstNodeOrder.PREFIX,
				this.trees.iterator());
	}

	@Override
	@Pure
	public final Iterator<TreeNode<D, ?>> broadFirstIterator() {
		return new BroadFirstForestIterator<>(this.trees.iterator());
	}

	@Override
	@Pure
	public final Iterator<D> dataDepthFirstIterator() {
		return new DataDepthFirstForestIterator<>(
				DepthFirstNodeOrder.PREFIX,
				this.trees.iterator());
	}

	@Override
	@Pure
	public final Iterator<D> dataDepthFirstIterator(DepthFirstNodeOrder nodeOrder) {
		return new DataDepthFirstForestIterator<>(
				nodeOrder,
				this.trees.iterator());
	}

	@Override
	@Pure
	public final Iterator<D> dataDepthFirstIterator(int infixPosition) {
		if (infixPosition <= 0) {
			return new DataDepthFirstForestIterator<>(
					DepthFirstNodeOrder.PREFIX,
					this.trees.iterator());
		}
		return new DataDepthFirstForestIterator<>(
				infixPosition,
				this.trees.iterator());
	}

	@Override
	@Pure
	public final Iterator<D> dataBroadFirstIterator() {
		return new DataBroadFirstForestIterator<>(this.trees.iterator());
	}

	@Override
	public boolean add(Tree<D, ?> tree) {
		if (this.trees.add(tree)) {
			fireTreeAddition(tree);
			return true;
		}
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends Tree<D, ?>> newTrees) {
		if (newTrees.isEmpty()) {
			return false;
		}
		boolean allAdded = true;
		for (final Tree<D, ?> tree : newTrees) {
			if (!add(tree)) {
				allAdded = false;
			}
		}
		return allAdded;
	}

	@Override
	public void clear() {
		for (final Tree<D, ?> tree : this.trees) {
			fireTreeRemoval(tree);
		}
		this.trees.clear();
	}

	@Override
	@Pure
	public boolean contains(Object tree) {
		return this.trees.contains(tree);
	}

	@Override
	@Pure
	public boolean containsAll(Collection<?> treeCollection) {
		return this.trees.containsAll(treeCollection);
	}

	@Override
	@Pure
	public boolean isEmpty() {
		if (this.trees.isEmpty()) {
			return true;
		}
		for (final Tree<D, ?> tree : this.trees) {
			if (!tree.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	@Pure
	public Iterator<Tree<D, ?>> iterator() {
		return new ForestIterator(this.trees.iterator());
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(Object obj) {
		final Tree<D, ?> tree;
		try {
			tree = (Tree<D, ?>) obj;
		} catch (AssertionError e) {
			throw e;
		} catch (Throwable e) {
			return false;
		}
		if (this.trees.remove(obj)) {
			fireTreeRemoval(tree);
			return true;
		}
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> tree) {
		if (tree.isEmpty()) {
			return false;
		}
		boolean allRemoved = true;
		for (final Object o : tree) {
			if (!remove(o)) {
				allRemoved = false;
			}
		}
		return allRemoved;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean retainAll(Collection<?> tree) {
		final List<Tree<D, ?>> retained = new LinkedList<>();
		boolean changed = false;
		for (final Object o : tree) {
			if (this.trees.remove(o)) {
				retained.add((Tree<D, ?>) o);
				changed = true;
			}
		}
		for (final Tree<D, ?> t : this.trees) {
			fireTreeRemoval(t);
			changed = true;
		}
		this.trees.clear();
		this.trees.addAll(retained);
		return changed;
	}

	@Override
	@Pure
	public int size() {
		return this.trees.size();
	}

	@Override
	@Pure
	public Object[] toArray() {
		return this.trees.toArray();
	}

	@Override
	public <TT> TT[] toArray(TT[] array) {
		return this.trees.toArray(array);
	}

	/** Add forest listener.
	 *
	 * @param listener the listener.
	 */
	public synchronized void addForestListener(ForestListener listener) {
		if (this.listeners == null) {
			this.listeners = new ArrayList<>();
		}
		this.listeners.add(listener);
	}

	/** Remove forest listener.
	 *
	 * @param listener the listener.
	 */
	public synchronized void removeForestListener(ForestListener listener) {
		if (this.listeners != null) {
			this.listeners.remove(listener);
			if (this.listeners.isEmpty()) {
				this.listeners = null;
			}
		}
	}

	/** Fire the addition event.
	 *
	 * @param tree is the new tree in the forest.
	 */
	protected synchronized void fireTreeAddition(Tree<D, ?> tree) {
		if (this.listeners != null) {
			final ForestListener[] list = new ForestListener[this.listeners.size()];
			this.listeners.toArray(list);
			final ForestEvent event = new ForestEvent(this, null, tree);
			for (final ForestListener listener : list) {
				listener.forestChanged(event);
			}
		}
	}

	/** Fire the removal event.
	 *
	 * @param tree is the old tree in the forest.
	 */
	protected synchronized void fireTreeRemoval(Tree<D, ?> tree) {
		if (this.listeners != null) {
			final ForestListener[] list = new ForestListener[this.listeners.size()];
			this.listeners.toArray(list);
			final ForestEvent event = new ForestEvent(this, tree, null);
			for (final ForestListener listener : list) {
				listener.forestChanged(event);
			}
		}
	}

	/** Iterator on trees in a  forest.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	private class ForestIterator implements Iterator<Tree<D, ?>> {

		private final Iterator<Tree<D, ?>> iterator;

		private Tree<D, ?> lastReplied;

		/** Construct the iterator.
		 *
		 * @param iterator1 the initial iterator.
		 */
		ForestIterator(Iterator<Tree<D, ?>> iterator1) {
			this.iterator = iterator1;
		}

		@Override
		public boolean hasNext() {
			return this.iterator.hasNext();
		}

		@Override
		public Tree<D, ?> next() {
			final Tree<D, ?> t = this.iterator.next();
			this.lastReplied = t;
			return t;
		}

		@Override
		public void remove() {
			this.iterator.remove();
			if (this.lastReplied != null) {
				fireTreeRemoval(this.lastReplied);
				this.lastReplied = null;
			}
		}

	}

}
