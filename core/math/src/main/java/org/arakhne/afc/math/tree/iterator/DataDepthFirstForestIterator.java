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
package org.arakhne.afc.math.tree.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

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
	private final Iterator<Tree<D,?>> trees;
	private Iterator<D> treeIterator;
	private DataPair<D> nextData;
	private DataPair<D> lastlyReplied = null;
	
	/**
	 * @param order is the treatement order for data.
	 * @param iterator is the trees to iterate on.
	 */
	public DataDepthFirstForestIterator(DepthFirstNodeOrder order, Iterator<Tree<D,?>> iterator) {
		this.infixOrder = -1;
		this.order = order;
		this.trees = iterator;
		this.treeIterator = null;
		searchNext();
	}
	
	/**
	 * @param infixOrder1 is the order of the nodes when to treat data according to an infixed iteration.
	 * @param iterator is the trees to iterate on.
	 */
	public DataDepthFirstForestIterator(int infixOrder1, Iterator<Tree<D,?>> iterator) {
		this.infixOrder = infixOrder1;
		this.order = null;
		this.trees = iterator;
		this.treeIterator = null;
		searchNext();
	}

	private void searchNext() {
		Tree<D,?> tree;
		this.nextData = null;
		while ((this.treeIterator==null || !this.treeIterator.hasNext())
			   && this.trees.hasNext()) {
			tree = this.trees.next();
			assert(tree!=null);
			if (this.order==null)
				this.treeIterator = tree.dataDepthFirstIterator(this.infixOrder);
			else
				this.treeIterator = tree.dataDepthFirstIterator(this.order);
		}
		if (this.treeIterator!=null && this.treeIterator.hasNext()) {
			this.nextData = new DataPair<>(this.treeIterator.next(), this.treeIterator);
			assert(this.nextData!=null);
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public void remove() {
		DataPair<D> pair = this.lastlyReplied;
		this.lastlyReplied = null;
		if (pair==null)
			throw new NoSuchElementException();
		assert(pair.data!=null);
		assert(pair.iterator!=null);
		pair.iterator.remove();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext() {
		return this.nextData!=null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public D next() {
		this.lastlyReplied = this.nextData;
		if (this.lastlyReplied==null || this.lastlyReplied.data==null)
			throw new NoSuchElementException();
		searchNext();
		return this.lastlyReplied.data;
	}
		
	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	private static class DataPair<D> {
		
		/** Data.
		 */
		public final D data;

		/** Iterator that has replied the data.
		 */
		public final Iterator<D> iterator;
		
		/**
		 * @param data
		 * @param iterator
		 */
		public DataPair(D data, Iterator<D> iterator) {
			this.data = data;
			this.iterator = iterator;
		}
		
	}

}
