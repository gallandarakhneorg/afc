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
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

import org.arakhne.afc.math.tree.Tree;
import org.arakhne.afc.math.tree.TreeNode;

/**
 * This class is an iterator on a forest that replies the user data.
 * <p>
 * This iterator goes thru the trees in a broad-first order.
 * 
 * @param <D> is the type of the data inside the forest
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class DataBroadFirstForestIterator<D>
implements Iterator<D> {

	/** List of the node to treat.
	 */
	private final Queue<TreeNode<D,?>> availableNodes = new LinkedList<TreeNode<D,?>>();
	
	/** List of the data to replies.
	 */
	private final Queue<DataPair<D>> availableData = new LinkedList<DataPair<D>>();
	
	private DataPair<D> lastlyReplied = null;
	private boolean isStarted = false;

	/**
	 * @param iterator is the iterator on the trees.
	 */
	public DataBroadFirstForestIterator(Iterator<Tree<D,?>> iterator) {
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
			searchNext(root);
		}
		this.isStarted = true;
	}

	private void searchNext(TreeNode<D,?> parent) {
		TreeNode<D,?> prt = parent;
		while (prt!=null) {
			for(int i=0; i<prt.getUserDataCount(); ++i) {
				D d = prt.getUserDataAt(i);
				if (d!=null) {
					this.availableData.add(new DataPair<D>(d,prt));
				}
			}
			
			for(int i=0; i<prt.getChildCount(); ++i) {
				TreeNode<D,?> child = prt.getChildAt(i);
				if (child!=null) {
					this.availableNodes.offer(child);
				}
			}
			
			prt = null; // avoid loop
	
			if ((this.availableData.isEmpty())&&
				(!this.availableNodes.isEmpty())) {
				do {
					prt = this.availableNodes.poll();
				}
				while ((prt==null)&&(!this.availableNodes.isEmpty()));
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext() {
		if (!this.isStarted) startIterator();
		return (!this.availableData.isEmpty());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public D next() {
		if (!this.isStarted) startIterator();
		
		// If a data was found and not already replied
		// this function replies it.
		if (!this.availableData.isEmpty()) {
			this.lastlyReplied = this.availableData.poll();
			assert(this.lastlyReplied!=null && this.lastlyReplied.data!=null);
			
			if ((this.availableData.isEmpty())&&
				(!this.availableNodes.isEmpty())) {
				TreeNode<D,?> newNode = this.availableNodes.poll();
				if (newNode!=null) {
					searchNext(newNode);
				}
			}
			
			return this.lastlyReplied.data;
		}
		
		throw new NoSuchElementException();
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
		assert(pair.node!=null);
		pair.node.removeUserData(pair.data);
	}

	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class DataPair<D> {
		
		/** Data.
		 */
		public final D data;

		/** Node containing the data.
		 */
		public final TreeNode<D,?> node;
		
		/**
		 * @param data
		 * @param node
		 */
		public DataPair(D data, TreeNode<D,?> node) {
			this.data = data;
			this.node = node;
		}
		
	}

}
