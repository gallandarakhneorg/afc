/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.tree.IterableNode;

/** This class is an iterator on a tree.
 *
 * <p>The node A is treated <i>before</i> its children.
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

	private P lastReplied;

	private BroadFirstIterationListener levelListener;

	/** Create an iterator on the given node.
	 *
	 * @param node is the node to iterate.
	 */
	public AbstractBroadFirstTreeIterator(P node) {
		if (node != null) {
			this.availableNodes.offer(node);
			this.availableNodes.offer(null);
		}
	}

	/** Set the listener invoked when a level of the tree has been treated.
	 *
	 * @param listener the listener.
	 */
	public void setBroadFirstIterationListener(BroadFirstIterationListener listener) {
		this.levelListener = listener;
	}

	/** Replies the listener invoked when a level of the tree has been treated.
	 *
	 * @return the listener
	 */
	@Pure
	public BroadFirstIterationListener getBroadFirstIterationListener() {
		return this.levelListener;
	}

	/** Invoked when a row of tree nodes was completely replied by the iterator.
	 */
	protected void onBoardFirstIterationLevelFinished() {
		if (this.levelListener != null) {
			this.levelListener.onBoardFirstIterationLevelFinished();
		}
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
	 * @param childIndex is the position of the {@code child} node in its {@code parent}.
	 * {@code childIndex} is always greater or equal to {@code notNullChildIndex}.
	 * @param notNullChildIndex is the position of the {@code child} node in the
	 *     list of the not-null nodes of its {@code parent}. {@code notNullChildIndex}
	 *     is always lower or equal to {@code childIndex}.
	 * @return the traversable node, or <code>null</code> if the node is not traversable.
	 */
	@Pure
	protected abstract P toTraversableChild(P parent, C child, int childIndex, int notNullChildIndex);

	@Pure
	@Override
	public boolean hasNext() {
		return !this.availableNodes.isEmpty();
	}

	@Override
	public P next() {
		this.lastReplied = null;

		if (this.availableNodes.isEmpty()) {
			throw new NoSuchElementException();
		}

		final P current = this.availableNodes.poll();

		if (current == null) {
			throw new ConcurrentModificationException();
		}

		// Add the children of the polled element
		onBeforeChildNodes(current);

		int childCount = 0;
		int notNullIndex = 0;
		final int nb = current.getChildCount();
		for (int i = 0; i < nb; ++i) {
			try {
				final C child = current.getChildAt(i);
				if (child != null) {
					assert notNullIndex <= i;
					final P tn = toTraversableChild(current, child, i, notNullIndex);
					if (tn != null) {
						this.availableNodes.offer(tn);
						++childCount;
					}
					++notNullIndex;
				}
			} catch (IndexOutOfBoundsException e) {
				throw new ConcurrentModificationException();
			}
		}

		onAfterChildNodes(current, childCount);

		if (this.availableNodes.isEmpty()) {
			onBoardFirstIterationLevelFinished();
			this.levelListener = null;
		} else if (this.availableNodes.peek() == null) {
			this.availableNodes.poll();
			if (!this.availableNodes.isEmpty()) {
				this.availableNodes.offer(null);
			}
			onBoardFirstIterationLevelFinished();
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
