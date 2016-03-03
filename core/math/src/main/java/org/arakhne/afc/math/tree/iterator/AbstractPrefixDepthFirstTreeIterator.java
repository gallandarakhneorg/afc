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
import java.util.NoSuchElementException;
import java.util.Stack;

import org.arakhne.afc.math.tree.IterableNode;


/**
 * This class is an prefixed iterator on a tree.
 * It treats the parent node before going inside the child nodes.
 * 
 * @param <P> is the type of the parent nodes.
 * @param <C> is the type of the child nodes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public abstract class AbstractPrefixDepthFirstTreeIterator<P extends IterableNode<? extends C>, C extends IterableNode<?>>
implements Iterator<P> {

	private final Stack<P> availableNodes = new Stack<>();	
	private boolean isStarted = false;
	private P lastReplied = null;
	
	/**
	 * @param node is the node to iterate.
	 */
	public AbstractPrefixDepthFirstTreeIterator(P node) {
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
			this.availableNodes.push(root);
		}
		this.isStarted = true;
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
		if (current==null) throw new ConcurrentModificationException();
		
		for(int i=current.getChildCount()-1; i>=0; --i) {
			C child = current.getChildAt(i);
			if (child!=null) {
				P cn = toTraversableChild(current, child);
				if (cn!=null) this.availableNodes.push(cn);
			}
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
