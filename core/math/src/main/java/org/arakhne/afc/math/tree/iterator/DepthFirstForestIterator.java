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
import org.arakhne.afc.math.tree.TreeNode;

/**
 * This class is an iterator on a forest nodes.
 * 
 * @param <D> is the type of the data inside the forest
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class DepthFirstForestIterator<D>
implements Iterator<TreeNode<D,?>> {
	
	private final DepthFirstNodeOrder order;
	private final Iterator<Tree<D,?>> trees;
	private Iterator<? extends TreeNode<D,?>> treeIterator;
	private TreeNode<D,?> nextNode;
	
	/**
	 * @param order is the treatement order for data.
	 * @param iterator is the trees to iterate on.
	 */
	@SuppressWarnings("hiding")
	public DepthFirstForestIterator(DepthFirstNodeOrder order, Iterator<Tree<D,?>> iterator) {
		assert(order!=null);
		this.order = order;
		this.trees = iterator;
		this.treeIterator = null;
		searchNext();
	}
	
	private void searchNext() {
		Tree<D,?> tree;
		this.nextNode = null;
		while ((this.treeIterator==null || !this.treeIterator.hasNext())
			   && this.trees.hasNext()) {
			tree = this.trees.next();
			assert(tree!=null);
			this.treeIterator = tree.depthFirstIterator(this.order);
		}
		if (this.treeIterator!=null && this.treeIterator.hasNext()) {
			this.nextNode = this.treeIterator.next();
			assert(this.nextNode!=null);
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext() {
		return this.nextNode!=null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TreeNode<D,?> next() {
		TreeNode<D,?> d = this.nextNode;
		if (d==null) throw new NoSuchElementException();
		searchNext();
		return d;
	}
		
}
