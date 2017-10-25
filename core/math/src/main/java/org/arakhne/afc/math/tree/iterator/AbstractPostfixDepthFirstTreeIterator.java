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

package org.arakhne.afc.math.tree.iterator;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.tree.IterableNode;


/**
 * This class is an postfixed iterator on a tree.
 * It treats the parent node after going inside the child nodes.
 *
 * @param <P> is the type of the parent nodes.
 * @param <C> is the type of the child nodes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public abstract class AbstractPostfixDepthFirstTreeIterator<P extends IterableNode<? extends C>, C extends IterableNode<?>>
		implements Iterator<P> {

	/** List of the node to treat.
	 */
	private final Deque<P> availableNodes = new ArrayDeque<>();

	/** List of expanded nodes.
	 */
	private final List<P> expandedNodes = new ArrayList<>();

	private boolean isStarted;

	private P lastReplied;

	/**
	 * @param node is the node to iterate.
	 */
	public AbstractPostfixDepthFirstTreeIterator(P node) {
		this.availableNodes.push(node);
	}

	/** Replies an object to type N which is corresponding to
	 * the given child node retreived from the given parent node.
	 *
	 * @param parent is the node from which the child node was retreived.
	 * @param child is the child node to test.
	 * @return the traversable node, or <code>null</code> if the node is not traversable.
	 */
	@Pure
	protected abstract P toTraversableChild(P parent, C child);

	/** Replies if an object to type N which is corresponding to
	 * the given child node retreived from the given parent node.
	 *
	 * @param parent is the node from which the child node was retreived.
	 * @return <code>true</code> if the given node is traversable, <code>false</code>
	 *     otherwise.
	 */
	@Pure
	protected abstract boolean isTraversableParent(P parent);

	private void startIterator() {
		final P root = this.availableNodes.pop();
		if ((root != null)
				&& (isTraversableParent(root))) {
			this.availableNodes.push(root);
			fillNodeList(root);
		}
		this.isStarted = true;
	}

	/** fill the list of the available nodes from the
	 * specified parent node.
	 *
	 * @param parent is the node from which the tree must be explored.
	 */
	private void fillNodeList(P parent) {
		P prt = parent;
		if ((prt != null) && (prt.isLeaf())) {
			return;
		}
		while (prt != null) {
			if (!prt.isLeaf()) {
				if (!this.expandedNodes.contains(prt)) {
					this.expandedNodes.add(prt);
					for (int i = prt.getChildCount() - 1; i >= 0; --i) {
						final C child = prt.getChildAt(i);
						if (child != null) {
							final P cn = toTraversableChild(prt, child);
							if (cn != null) {
								this.availableNodes.push(cn);
							}
						}
					}
				} else {
					return;
				}
			}
			prt = this.availableNodes.isEmpty() ? null : this.availableNodes.peek();
			if ((prt != null) && (prt.isLeaf())) {
				return;
			}
		}
	}

	@Pure
	@Override
	public boolean hasNext() {
		if (!this.isStarted) {
			startIterator();
		}
		return !this.availableNodes.isEmpty();
	}

	@Override
	public P next() {
		this.lastReplied = null;
		if (!this.isStarted) {
			startIterator();
		}
		if (this.availableNodes.isEmpty()) {
			throw new NoSuchElementException();
		}

		final P current = this.availableNodes.pop();
		if (current == null) {
			throw new ConcurrentModificationException();
		}

		if (!this.availableNodes.isEmpty()) {
			fillNodeList(this.availableNodes.peek());
		}

		if (!current.isLeaf()) {
			this.expandedNodes.remove(current);
		}

		this.lastReplied = current;
		return current;
	}

	@Override
	public void remove() {
		final P lr = this.lastReplied;
		this.lastReplied = null;
		if (lr == null) {
			throw new NoSuchElementException();
		}
		lr.removeFromParent();
	}

}
