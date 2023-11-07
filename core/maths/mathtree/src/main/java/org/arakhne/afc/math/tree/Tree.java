/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

package org.arakhne.afc.math.tree;

import java.util.Iterator;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.tree.iterator.DepthFirstNodeOrder;
import org.arakhne.afc.math.tree.iterator.InfixDepthFirstTreeIterator;

/**
 * This is the generic implementation of a
 * tree.
 *
 * <p>This is the public interface for a tree which
 * is independent of the tree implementation.
 *
 * @param <D> is the type of the data inside the tree
 * @param <N> is the type of the tree nodes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface Tree<D, N extends TreeNode<D, ?>> extends Iterable<N> {

	/** Replies the root of the tree.
	 *
	 * @return the root of the tree.
	 */
	@Pure
	N getRoot();

	/** Set the root of the tree.
	 *
	 * @param newRoot is the new root node.
	 */
	void setRoot(N newRoot);

	/** Clear the tree.
	 *
	 * <p>Caution: this method also destroyes the
	 * links between the nodes inside the tree.
	 * If you want to unlink the root node with
	 * this tree but leave the nodes' links
	 * unchanged, please call {@code setRoot(null)}.
	 */
	void clear();

	/** Replies the count of nodes inside this tree.
	 *
	 * @return the count of nodes inside the whole tree.
	 */
	@Pure
	int getNodeCount();

	/** Replies the count of data inside this tree.
	 *
	 * @return the count of user objects inside the whole tree.
	 */
	@Pure
	int getUserDataCount();

	/** Replies if this tree contains user data.
	 *
	 * @return {@code true} if the tree is empty (no user data),
	 *     otherwise {@code false}
	 */
	@Pure
	boolean isEmpty();

	/** Replies the minimal height of the tree.
	 *
	 * @return the height of the uppest leaf in the tree.
	 */
	@Pure
	int getMinHeight();

	/** Replies the maximal height of the tree.
	 *
	 * @return the height of the lowest leaf in the tree.
	 */
	@Pure
	int getMaxHeight();

	/** Replies the heights of all the leaf nodes.
	 * The order of the heights is given by a depth-first iteration.
	 *
	 * @return the heights of the leaf nodes
	 */
	@Pure
	int[] getHeights();

	/** Replies a depth-first iterator on nodes.
	 *
	 * @param nodeOrder indicates how the data are treated by the iterator.
	 * @return a depth first iterator on nodes
	 */
	@Pure
	Iterator<N> depthFirstIterator(DepthFirstNodeOrder nodeOrder);

	/** Replies a depth-first iterator on nodes.
	 *
	 * @return a depth-first iterator.
	 */
	@Pure
	Iterator<N> depthFirstIterator();

	/** Replies a breadth first iterator on nodes.
	 *
	 * @return a breadth first iterator.
	 * @since 18.0 (replacement for 13.0 and higher).
	 */
	@Pure
	Iterator<N> breadthFirstIterator();

	/** Replies a breadth first iterator on nodes.
	 *
	 * @return a broad first iterator.
	 * @deprecated since 18.0, see {@link #breadthFirstIterator}.
	 */
	@Pure
	@Deprecated(since = "18.0", forRemoval = true)
	default Iterator<N> broadFirstIterator() {
		return breadthFirstIterator();
	}

	/** Replies a prefixed depth first iterator on the tree.
	 *
	 * @return a prefixed depth first iterator on data.
	 */
	@Pure
	Iterator<D> dataDepthFirstIterator();

	/** Replies a depth first iterator on the tree.
	 *
	 * @param nodeOrder indicates how the data are treated by the iterator.
	 * @return an iterator on the data.
	 */
	@Pure
	Iterator<D> dataDepthFirstIterator(DepthFirstNodeOrder nodeOrder);

	/** Replies the depth first iterator on the tree.
	 *
	 * @param infixPosition is the index at which the parent
	 *     will be treated according to its child set.
	 * @return the iterator on user data
	 * @see InfixDepthFirstTreeIterator
	 */
	@Pure
	Iterator<D> dataDepthFirstIterator(int infixPosition);


	/** Replies the breadth-first iterator on the tree.
	 *
	 * @return the iterator on user data.
	 * @since 18.0 (replacement for 13.0 and higher).
	 */
	@Pure
	Iterator<D> dataBreadthFirstIterator();

	/** Replies the broad-first iterator on the tree.
	 *
	 * @return the iterator on user data.
	 * @deprecated since 18.0, see {@link #dataBreadthFirstIterator()}.
	 */
	@Pure
	@Deprecated(since = "18.0", forRemoval = true)
	default Iterator<D> dataBroadFirstIterator() {
		return dataBreadthFirstIterator();
	}

}
