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

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

import org.arakhne.afc.math.tree.Tree;
import org.arakhne.afc.math.tree.TreeNode;

/**
 * This class is an iterator on a forest that replies the tree nodes.
 * <p>
 * This iterator goes thru the trees in a broad-first order.
 * 
 * @param <D> is the type of the data inside the forest
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class BroadFirstForestIterator<D>
implements Iterator<TreeNode<D,?>> {

	/** List of the node to treat.
	 */
	private final Queue<TreeNode<D,?>> availableNodes = new LinkedList<TreeNode<D,?>>();
	
	private boolean isStarted = false;

	/**
	 * @param iterator is the iterator on the trees.
	 */
	public BroadFirstForestIterator(Iterator<Tree<D,?>> iterator) {
		assert(iterator!=null);
		Tree<D,?> tree;
		TreeNode<D,?> node;
		while (iterator.hasNext()) {
			tree = iterator.next();
			node = tree.getRoot();
			if (node!=null) this.availableNodes.offer(node);
		}
	}
	
	private void startIterator() {
		TreeNode<D,?> root = this.availableNodes.poll();
		if (root!=null) {
			this.availableNodes.offer(root);
		}
		this.isStarted = true;
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
		if (!this.isStarted) startIterator();
		return !this.availableNodes.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TreeNode<D,?> next() {
		if (!this.isStarted) startIterator();
		if (this.availableNodes.isEmpty()) throw new NoSuchElementException();
		
		TreeNode<D,?> current = this.availableNodes.poll();
		
		if (current==null) throw new ConcurrentModificationException();

		// Add the children of the polled element
		for(int i=0; i<current.getChildCount(); ++i) {
			try {
				TreeNode<D,?> child = current.getChildAt(i);
				if (child!=null)
					this.availableNodes.offer(child);
			}
			catch(IndexOutOfBoundsException _) {
				throw new ConcurrentModificationException();
			}
		}
		
		return current;
	}
	
}
