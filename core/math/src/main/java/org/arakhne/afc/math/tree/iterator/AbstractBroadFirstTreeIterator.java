/* 
 * $Id$
 * 
 * Copyright (c) 2011, Multiagent Team,
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

import org.arakhne.afc.math.tree.IterableNode;


/**
 * This class is an iterator on a tree.
 * <p>
 * The node A is treated <i>before</i> its children.
 * 
 * @param <P> is the type of the parent nodes.
 * @param <C> is the type of the child nodes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public abstract class AbstractBroadFirstTreeIterator<P extends IterableNode<? extends C>, C extends IterableNode<?>>
implements Iterator<P> {

	private final Queue<P> availableNodes = new LinkedList<>();
	private P lastReplied = null;
	private BroadFirstIterationListener levelListener = null;
	
	/** Create an iterator on the given node
	 * 
	 * @param node is the node to iterate.
	 */
	public AbstractBroadFirstTreeIterator(P node) {
		if (node!=null) {
			this.availableNodes.offer(node);
			this.availableNodes.offer(null);
		}
	}
	
	/** Set the listener invoked when a level of the tree has been treated.
	 * 
	 * @param listener
	 */
	public void setBroadFirstIterationListener(BroadFirstIterationListener listener) {
		this.levelListener = listener;
	}
	
	/** Replies the listener invoked when a level of the tree has been treated.
	 * 
	 * @return the listener
	 */
	public BroadFirstIterationListener getBroadFirstIterationListener() {
		return this.levelListener;
	}

	/** Invoked when a row of tree nodes was completely replied by the iterator.
	 */
	protected void onBoardFirstIterationLevelFinished() {
		if (this.levelListener!=null)
			this.levelListener.onBoardFirstIterationLevelFinished();
	}
	
	/** Invoked after all the children of a node were memorized by the iterator.
	 * 
	 * @param node is the node for which all the child nodes have been memorized.
	 * @param childCount is the number of child nodes that are memorized by the iterator.
	 */
	protected void onAfterChildNodes(P node, int childCount) {
		//
	}

	/** Invoked before all the children of a node were memorized by the iterator.
	 * 
	 * @param node is the node for which all the child nodes will be memorized.
	 */
	protected void onBeforeChildNodes(P node) {
		//
	}

	/** Replies an object to type N which is corresponding to 
	 * the given child node retreived from the given parent node.
	 * 
	 * @param parent is the node from which the child node was retreived.
	 * @param child is the child node to test.
	 * @param childIndex is the position of the <var>child</var> node in its <var>parent</var>.
	 * <var>childIndex</var> is always greater or equal to <var>notNullChildIndex</var>.
	 * @param notNullChildIndex is the position of the <var>child</var> node in the
	 * list of the not-null nodes of its <var>parent</var>. <var>notNullChildIndex</var>
	 * is always lower or equal to <var>childIndex</var>.
	 * @return the traversable node, or <code>null</code> if the node is not traversable.
	 */
	protected abstract P toTraversableChild(P parent, C child, int childIndex, int notNullChildIndex);

	/** {@inheritDoc}
	 */
	@Override
	public boolean hasNext() {
		return !this.availableNodes.isEmpty();
	}

	/** {@inheritDoc}
	 */
	@Override
	public P next() {
		this.lastReplied = null;

		if (this.availableNodes.isEmpty()) throw new NoSuchElementException();

		P current = this.availableNodes.poll();
		
		if (current==null) throw new ConcurrentModificationException();
				
		// Add the children of the polled element
		onBeforeChildNodes(current);

		int childCount = 0;
		int notNullIndex = 0;
		for(int i=0; i<current.getChildCount(); ++i) {
			try {
				C child = current.getChildAt(i);
				if (child!=null) {
					assert(notNullIndex<=i);
					P tn = toTraversableChild(current, child, i, notNullIndex);
					if (tn!=null) {
						this.availableNodes.offer(tn);
						++childCount;
					}
					++notNullIndex;
				}
			}
			catch(IndexOutOfBoundsException e) {
				throw new ConcurrentModificationException();
			}
		}
		
		onAfterChildNodes(current, childCount);
		
		if (this.availableNodes.isEmpty()) {
			onBoardFirstIterationLevelFinished();
			this.levelListener = null;
		}
		else if (this.availableNodes.peek()==null) {
			this.availableNodes.poll();
			if (!this.availableNodes.isEmpty())
				this.availableNodes.offer(null);
			onBoardFirstIterationLevelFinished();
		}

		this.lastReplied = current;
		return current;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void remove() {
		P lr = this.lastReplied;
		this.lastReplied = null;
		if (lr==null) throw new NoSuchElementException();
		lr.removeFromParent();
	}
	
}
