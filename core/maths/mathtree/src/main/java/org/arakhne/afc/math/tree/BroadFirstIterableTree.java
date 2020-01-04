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

/**
 * This is the generic implementation of a
 * tree service that permits to iterate
 * with a broad-first approach.
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
public interface BroadFirstIterableTree<D, N extends TreeNode<D, ?>> extends Tree<D, N> {

	@Override
	@Pure
	Iterator<N> broadFirstIterator();

	@Override
	@Pure
	Iterator<D> dataBroadFirstIterator();

	/** Replies the broad-first iterator on the tree.
	 *
	 * @return the iterator on nodes.
	 */
	@Pure
	Iterable<N> toBroadFirstIterable();

	/** Replies the broad-first iterator on the tree.
	 *
	 * @return the iterator on user data.
	 */
	@Pure
	Iterable<D> toDataBroadFirstIterable();

}
