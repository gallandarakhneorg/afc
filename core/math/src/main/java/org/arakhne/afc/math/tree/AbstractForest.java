/* 
 * $Id$
 * 
 * Copyright (c) 2005-11, Multiagent Team,
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
package org.arakhne.afc.math.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.arakhne.afc.math.tree.iterator.BroadFirstForestIterator;
import org.arakhne.afc.math.tree.iterator.DataBroadFirstForestIterator;
import org.arakhne.afc.math.tree.iterator.DataDepthFirstForestIterator;
import org.arakhne.afc.math.tree.iterator.DepthFirstForestIterator;
import org.arakhne.afc.math.tree.iterator.DepthFirstNodeOrder;


/**
 * This is the generic implementation of a
 * forest of trees.
 * <p>
 * A forest of trees is a collection of trees.
 * 
 * @param <D> is the type of the data inside the forest
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.0
 */
public abstract class AbstractForest<D>
implements Forest<D> {
	
	/**
	 * Indicates if the forests are using a linked list
	 * by default to store the trees.
	 */
	public static final boolean USE_LINKED_LIST = false;
	
	/** List of trees.
	 */
	private final List<Tree<D,?>> trees;
	
	private Collection<ForestListener> listeners = null;
	
	/**
	 * @param internalList is the internal list to use.
	 */
	protected AbstractForest(List<Tree<D,?>> internalList) {
		assert(internalList!=null);
		this.trees = internalList;
	}
	
	/**
	 * @param internalList is the internal list to use.
	 * @param trees is the trees to put inside the forest.
	 */
	protected AbstractForest(List<Tree<D,?>> internalList, Collection<? extends Tree<D,?>> trees) {
		assert(internalList!=null);
		assert(trees!=null);
		this.trees = internalList;
		this.trees.addAll(trees);
	}

	/** {@inheritDoc}
	 */
	@Override
	public final int getMinHeight() {
		if (this.trees.isEmpty()) return 0;
		int m, min = Integer.MAX_VALUE;
		for(Tree<D,?> tree : this.trees) {
			m = tree.getMinHeight();
			if (m<min) min = m;
		}
		return min;
	}

	/** {@inheritDoc}
	 */
	@Override
	public final int getMaxHeight() {
		if (this.trees.isEmpty()) return 0;
		int m, max = 0;
		for(Tree<D,?> tree : this.trees) {
			m = tree.getMaxHeight();
			if (m>max) max = m;
		}
		return max;
	}

	/** {@inheritDoc}
	 */
	@Override
	public final int[] getHeights() {
		int[] array = new int[0];
		int[] a, b;
		for(Tree<D,?> tree : this.trees) {
			a = tree.getHeights();
			if (a!=null && a.length>0) {
				b = new int[array.length+a.length];
				System.arraycopy(array, 0, b, 0, array.length);
				System.arraycopy(a, 0, b, array.length, a.length);
				array = b;
			}
		}
		return array;
	}

	/** {@inheritDoc}
	 */
	@Override
	public final Iterator<TreeNode<D,?>> depthFirstIterator(DepthFirstNodeOrder nodeOrder) {
		return new DepthFirstForestIterator<D>(
				nodeOrder,
				this.trees.iterator());
	}

	/** {@inheritDoc}
	 */
	@Override
	public final Iterator<TreeNode<D,?>> depthFirstIterator() {
		return new DepthFirstForestIterator<D>(
				DepthFirstNodeOrder.PREFIX,
				this.trees.iterator());
	}

	/** {@inheritDoc}
	 */
	@Override
	public final Iterator<TreeNode<D,?>> broadFirstIterator() {
		return new BroadFirstForestIterator<D>(this.trees.iterator());
	}

	/** {@inheritDoc}
	 */
	@Override
	public final Iterator<D> dataDepthFirstIterator() {
		return new DataDepthFirstForestIterator<D>(
				DepthFirstNodeOrder.PREFIX,
				this.trees.iterator());
	}

	/** {@inheritDoc}
	 */
	@Override
	public final Iterator<D> dataDepthFirstIterator(DepthFirstNodeOrder nodeOrder) {
		return new DataDepthFirstForestIterator<D>(
				nodeOrder,
				this.trees.iterator());
	}

	/** {@inheritDoc}
	 */
	@Override
	public final Iterator<D> dataDepthFirstIterator(int infixPosition) {
		if (infixPosition<=0)
			return new DataDepthFirstForestIterator<D>(
					DepthFirstNodeOrder.PREFIX,
					this.trees.iterator());
		return new DataDepthFirstForestIterator<D>(
				infixPosition,
				this.trees.iterator());
	}

	/** {@inheritDoc}
	 */
	@Override
	public final Iterator<D> dataBroadFirstIterator() {
		return new DataBroadFirstForestIterator<D>(this.trees.iterator());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean add(Tree<D,?> tree) {
		if (this.trees.add(tree)) {
			fireTreeAddition(tree);
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addAll(Collection<? extends Tree<D,?>> newTrees) {
		if (newTrees.isEmpty()) return false;
		boolean allAdded = true;
		for(Tree<D,?> tree : newTrees) {
			if (!add(tree)) allAdded = false;
		}
		return allAdded;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		for(Tree<D,?> tree : this.trees) {
			fireTreeRemoval(tree);
		}
		this.trees.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean contains(Object tree) {
		return this.trees.contains(tree);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsAll(Collection<?> treeCollection) {
		return this.trees.containsAll(treeCollection);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		if (this.trees.isEmpty()) return true;
		for(Tree<D,?> tree : this.trees) {
			if (!tree.isEmpty())
				return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<Tree<D,?>> iterator() {
		return new ForestIterator(this.trees.iterator());
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(Object tree) {
		Tree<D,?> t;
		try {
			t = (Tree<D,?>)tree;
		}
		catch(AssertionError e) {
			throw e;
		}
		catch(Throwable _) {
			return false;
		}
		if (this.trees.remove(tree)) {
			fireTreeRemoval(t);
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeAll(Collection<?> tree) {
		if (tree.isEmpty()) return false;
		boolean allRemoved = true;
		for(Object o : tree) {
			if (!remove(o)) allRemoved = false;
		}
		return allRemoved;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean retainAll(Collection<?> tree) {
		List<Tree<D,?>> retained = new LinkedList<Tree<D,?>>();
		boolean changed = false;
		for(Object o : tree) {
			if (this.trees.remove(o)) {
				retained.add((Tree<D,?>)o);
				changed = true;
			}
		}
		for(Tree<D,?> t : this.trees) {
			fireTreeRemoval(t);
			changed = true;
		}
		this.trees.clear();
		this.trees.addAll(retained);
		return changed;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return this.trees.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		return this.trees.toArray();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <TT> TT[] toArray(TT[] array) {
		return this.trees.toArray(array);
	}
	
	/** Add forest listener.
	 * 
	 * @param listener
	 */
	public synchronized void addForestListener(ForestListener listener) {
		if (this.listeners==null)
			this.listeners = new ArrayList<ForestListener>();
		this.listeners.add(listener);
	}
	
	/** Remove forest listener.
	 * 
	 * @param listener
	 */
	public synchronized void removeForestListener(ForestListener listener) {
		if (this.listeners!=null) {
			this.listeners.remove(listener);
			if (this.listeners.isEmpty())
				this.listeners = null;
		}
	}
	
	/** Fire the addition event.
	 * 
	 * @param tree is the new tree in the forest.
	 */
	protected synchronized void fireTreeAddition(Tree<D,?> tree) {
		if (this.listeners!=null) {
			ForestListener[] list = new ForestListener[this.listeners.size()];
			this.listeners.toArray(list);
			ForestEvent event = new ForestEvent(this,null,tree);
			for(ForestListener listener : list) {
				listener.forestChanged(event);
			}
		}
	}

	/** Fire the removal event.
	 * 
	 * @param tree is the old tree in the forest.
	 */
	protected synchronized void fireTreeRemoval(Tree<D,?> tree) {
		if (this.listeners!=null) {
			ForestListener[] list = new ForestListener[this.listeners.size()];
			this.listeners.toArray(list);
			ForestEvent event = new ForestEvent(this,tree,null);
			for(ForestListener listener : list) {
				listener.forestChanged(event);
			}
		}
	}

	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 3.0
	 */
	private class ForestIterator implements Iterator<Tree<D,?>> {
		
		private final Iterator<Tree<D,?>> iterator;
		
		private Tree<D,?> lastReplied = null;
		
		/**
		 * @param iterator
		 */
		public ForestIterator(Iterator<Tree<D,?>> iterator) {
			this.iterator = iterator;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNext() {
			return this.iterator.hasNext();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Tree<D,?> next() {
			Tree<D,?> t = this.iterator.next();
			this.lastReplied = t;
			return t;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void remove() {
			this.iterator.remove();
			if (this.lastReplied!=null) {
				fireTreeRemoval(this.lastReplied);
				this.lastReplied = null;
			}
		}

	}
	
}