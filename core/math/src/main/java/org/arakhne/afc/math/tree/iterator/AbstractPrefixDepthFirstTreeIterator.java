/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
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

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

import org.eclipse.xtext.xbase.lib.Pure;

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

	private boolean isStarted;

	private P lastReplied;

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
	 *     otherwise.
	 */
	@Pure
	protected abstract boolean isTraversableParent(P parent);

	private void startIterator() {
		final P root = this.availableNodes.pop();
		if ((root != null)
				&& (isTraversableParent(root))) {
			this.availableNodes.push(root);
		}
		this.isStarted = true;
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

		for (int i = current.getChildCount() - 1; i >= 0; --i) {
			final C child = current.getChildAt(i);
			if (child != null) {
				final P cn = toTraversableChild(current, child);
				if (cn != null) {
					this.availableNodes.push(cn);
				}
			}
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
