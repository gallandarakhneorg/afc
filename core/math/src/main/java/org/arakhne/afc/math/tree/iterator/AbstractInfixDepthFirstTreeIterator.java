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

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

import org.arakhne.afc.math.tree.IterableNode;

/**
 * This class is an infixed depth-first iterator on a tree.
 * <p>
 * This iterator has an infix position which
 * describes when to treat the parent node inside
 * the set of child treatments.
 * Significant values of this position are:
 * <table>
 * <tr><td><code>0</code></td><td>the parent is
 * treated before the child at index <code>0</code>.
 * This is equivalent to a prefixed depth-first iterator.
 * We recommend to use {@link PrefixDepthFirstTreeIterator}
 * insteed of this iterator class.</td></tr>
 * <tr><td>between <code>1</code> and <code>getChildCount()-1</code></td><td>the parent is
 * treated before the child at the specified index.</td></tr>
 * <tr><td><code>getChildCount()</code></td><td>the parent is
 * treated after the last child.
 * This is equivalent to a postfixed depth-first iterator.
 * We recommend to use {@link PostfixDepthFirstTreeIterator}
 * insteed of this iterator class.</td></tr>
 * </table>
 * <p>
 * By default this iterator assumes an infix index that corresponds
 * to the middle of each child set (ie, <code>getChildCount()/2</code>).
 * 
 * @param <P> is the type of the parent nodes.
 * @param <C> is the type of the child nodes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public abstract class AbstractInfixDepthFirstTreeIterator<P extends IterableNode<? extends C>, C extends IterableNode<?>>
implements Iterator<P> {

	/** List of the node to treat.
	 */
	private final Stack<P> availableNodes = new Stack<>();
	
	/** List of expanded nodes.
	 */
	private final List<P> expandedNodes = new ArrayList<>();
	
	/** Is the position at which the parent node must be treated.
	 */
	private int infixPosition;
	
	private boolean isStarted = false;
	private P lastReplied = null;
	
	/**
	 * @param node is the node to iterate.
	 */
	public AbstractInfixDepthFirstTreeIterator(P node) {
		this.infixPosition = -1;
		this.availableNodes.push(node);
	}
	
	/**
	 * @param node is the node to iterate.
	 * @param infixPosition is the index of the child which is assumed to be at the infixed position.
	 */
	public AbstractInfixDepthFirstTreeIterator(P node, int infixPosition) {
		this.infixPosition = infixPosition;
		this.availableNodes.push(node);
	}

	/** Replies an object to type N which is corresponding to 
	 * the given child node retreived from the given parent node.
	 * 
	 * @param parent is the node from which the child node was retreived.
	 * @param child is the child node to test.
	 * @return the traversable node, or <code>null</code> if the node is not traversable.
	 */
	protected abstract P toTraversableChild(P parent, C child);

	/** Replies if an object to type N which is corresponding to 
	 * the given child node retreived from the given parent node.
	 * 
	 * @param parent is the node from which the child node was retreived.
	 * @return <code>true</code> if the given node is traversable, <code>false</code>
	 * otherwise.
	 */
	protected abstract boolean isTraversableParent(P parent);

	private void startIterator() {
		P root = this.availableNodes.pop();
		if ((root!=null)&&
			(isTraversableParent(root))) {
			if (this.infixPosition==-1)
				this.infixPosition = root.getChildCount()/2;
			fillNodeList(root);
		}
		else if (this.infixPosition==-1)
			this.infixPosition = 0;
		this.isStarted = true;
	}

	/** fill the list of the available nodes from the
	 * specified parent node.
	 * 
	 * @param parent is the node from which the tree must be explored.
	 */
	private void fillNodeList(P parent) {
		P prt = parent;
		if ((prt!=null)&&(prt.isLeaf())) {
			if (isTraversableParent(prt))
				this.availableNodes.push(prt);
			return;
		}
		
		while (prt!=null) {
			if (!prt.isLeaf()) {
				if (!this.expandedNodes.contains(prt)) {
					this.expandedNodes.add(prt);
					for(int i=prt.getChildCount()-1; i>=this.infixPosition; --i) {
						C child = prt.getChildAt(i);
						if (child!=null) {
							P cn = toTraversableChild(prt, child);
							if (cn!=null) this.availableNodes.push(cn);
						}
					}
					
					this.availableNodes.push(prt);
					
					for(int i=this.infixPosition-1; i>=0; --i) {
						C child = prt.getChildAt(i);
						if (child!=null) {
							P cn = toTraversableChild(prt, child);
							if (cn!=null) this.availableNodes.push(cn);
						}
					}
				}
				else {
					if (isTraversableParent(prt))
						this.availableNodes.push(prt);
					return;
				}
			}
			prt = this.availableNodes.isEmpty() ? null : this.availableNodes.peek();
			if ((prt!=null)&&(prt.isLeaf())) {
				return;
			}
			if (!this.availableNodes.isEmpty()) this.availableNodes.pop();
		}
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public boolean hasNext() {
		if (!this.isStarted) startIterator();
		return !this.availableNodes.isEmpty();
	}

	/** {@inheritDoc}
	 */
	@Override
	public P next() {
		this.lastReplied = null;
		if (!this.isStarted) startIterator();
		if (this.availableNodes.isEmpty()) throw new NoSuchElementException();
		
		P current = this.availableNodes.pop();
		if (current==null)
			throw new ConcurrentModificationException();
		
		if (!this.availableNodes.isEmpty()) {
			fillNodeList(this.availableNodes.pop());
		}

		if (!current.isLeaf()) this.expandedNodes.remove(current);
		
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
