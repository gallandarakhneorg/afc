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

package org.arakhne.afc.math.tree;

import java.util.Iterator;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.tree.iterator.DepthFirstNodeOrder;
import org.arakhne.afc.math.tree.iterator.InfixDepthFirstTreeIterator;

/**
 * This is the generic implementation of a
 * tree service that permits to iterator
 * with a depth-first approach.
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
public interface DepthFirstIterableTree<D, N extends TreeNode<D, ?>> extends Tree<D, N> {

	@Override
	@Pure
	Iterator<N> depthFirstIterator(DepthFirstNodeOrder nodeOrder);

	/** Replies the infixes depth first iterator on the tree.
	 *
	 * @param infixPosition is the index at which the parent
	 *     will be treated according to its child set.
	 * @return the iterator on nodes
	 * @see InfixDepthFirstTreeIterator
	 */
	@Pure
	Iterator<N> depthFirstIterator(int infixPosition);

	@Override
	@Pure
	Iterator<N> depthFirstIterator();

	@Override
	@Pure
	Iterator<D> dataDepthFirstIterator();

	/** Replies the depth first iterator on the tree.
	 *
	 * @return the iterator on nodes
	 */
	@Pure
	Iterable<N> toDepthFirstIterable();

	/** Replies the depth first iterator on the tree.
	 *
	 * @param nodeOrder is the order in which the parent node
	 *     will be treated in comparison to its children.
	 * @return the iterator on nodes
	 */
	@Pure
	Iterable<N> toDepthFirstIterable(DepthFirstNodeOrder nodeOrder);

	/** Replies the infixed depth first iterator on the tree.
	 *
	 * @param infixPosition is the index at which the parent
	 *     will be treated according to its child set.
	 * @return the iterator on nodes
	 * @see InfixDepthFirstTreeIterator
	 */
	@Pure
	Iterable<N> toDepthFirstIterable(int infixPosition);

	/** Replies the depth first iterator on the tree.
	 *
	 * @return the iterator on user data
	 */
	@Pure
	Iterable<D> toDataDepthFirstIterable();

	/** Replies the depth first iterator on the tree.
	 *
	 * @param nodeOrder is the order in which the parent node
	 *     will be treated in comparison to its children.
	 * @return the iterator on user data
	 */
	@Pure
	Iterable<D> toDataDepthFirstIterable(DepthFirstNodeOrder nodeOrder);

	/** Replies the infixed depth first iterator on the tree.
	 *
	 * @param infixPosition is the index at which the parent
	 *     will be treated according to its child set.
	 * @return the iterator on user data
	 * @see InfixDepthFirstTreeIterator
	 */
	@Pure
	Iterable<D> toDataDepthFirstIterable(int infixPosition);

}
