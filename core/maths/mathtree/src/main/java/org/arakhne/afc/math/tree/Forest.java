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

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.tree.iterator.DepthFirstNodeOrder;
import org.arakhne.afc.math.tree.iterator.InfixDepthFirstTreeIterator;

/**
 * This is the generic definition of a
 * forest of trees.
 *
 * <p>A forest of trees is a collection of trees.
 *
 * @param <D> is the type of the data inside the forest
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
interface Forest<D> extends Collection<Tree<D, ?>> {

	/** Replies the minimal height of the forest.
	 *
	 * @return the height of the uppest leaf in the forest.
	 */
	@Pure
	int getMinHeight();

	/** Replies the maximal height of the forest.
	 *
	 * @return the height of the lowest leaf in the forest.
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
	Iterator<TreeNode<D, ?>> depthFirstIterator(DepthFirstNodeOrder nodeOrder);

	/** Replies a depth-first iterator on nodes.
	 *
	 * @return a depth-first iterator.
	 */
	@Pure
	Iterator<TreeNode<D, ?>> depthFirstIterator();

	/** Replies a breadth first iterator on nodes.
	 *
	 * @return a breadth first iterator.
	 * @since 18.0 (replacement for 13.0 and higher)
	 */
	@Pure
	Iterator<TreeNode<D, ?>> breadthFirstIterator();

	/** Replies a broad first iterator on nodes.
	 *
	 * @return a broad first iterator.
	 * @deprecated since 18.0, see {@link #breadthFirstIterator()}.
	 */
	@Pure
	@Deprecated(since = "18.0", forRemoval = true)
	default Iterator<TreeNode<D, ?>> broadFirstIterator() {
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
	 * @since 18.0 (replacement for 13.0 and higher)
	 */
	@Pure
	Iterator<D> dataBreadthFirstIterator();

	/** Replies the broad-first iterator on the tree.
	 *
	 * @return the iterator on user data.
	 * @deprecated since 18.0, see {@link #dataBreadthFirstIterator()}
	 */
	@Pure
	@Deprecated(since = "18.0", forRemoval = true)
	default Iterator<D> dataBroadFirstIterator() {
		return dataBreadthFirstIterator();
	}

}
