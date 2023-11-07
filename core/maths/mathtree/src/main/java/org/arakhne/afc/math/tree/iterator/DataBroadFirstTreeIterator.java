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

package org.arakhne.afc.math.tree.iterator;

import org.arakhne.afc.math.tree.Tree;
import org.arakhne.afc.math.tree.TreeNode;

/** This class is an iterator on a tree that replies the user data.
 *
 * <p>This iterator go thru the tree in a broad-first order.
 *
 * @param <D> is the type of the data inside the tree
 * @param <N> is the type of the tree nodes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 * @deprecated since 18.0, see {link DataBreadthFirstTreeIterator}
 */
@Deprecated(since = "18.0", forRemoval = true)
public class DataBroadFirstTreeIterator<D, N extends TreeNode<D, N>>
		extends DataBreadthFirstTreeIterator<D, N> {

	/** Construct the iterator.
	 * @param tree is the tree to iterate
	 */
	public DataBroadFirstTreeIterator(Tree<D, N> tree) {
		super(tree);
	}

	/** Construct the iterator.
	 * @param tree is the tree to iterate
	 * @param nodeSelector permits to filter the node repliable by this iterator.
	 */
	public DataBroadFirstTreeIterator(Tree<D, N> tree, NodeSelector<N> nodeSelector) {
		super(tree, nodeSelector);
	}

	/** Construct the iterator.
	 * @param tree is the tree to iterate
	 * @param dataSelector permits to filter the user data repliable by this iterator.
	 */
	public DataBroadFirstTreeIterator(Tree<D, N> tree, DataSelector<D> dataSelector) {
		super(tree, dataSelector);
	}

	/** Construct the iterator.
	 * @param tree is the tree to iterate
	 * @param nodeSelector permits to filter the node repliable by this iterator.
	 * @param dataSelector permits to filter the user data repliable by this iterator.
	 */
	public DataBroadFirstTreeIterator(Tree<D, N> tree, NodeSelector<N> nodeSelector, DataSelector<D> dataSelector) {
		super(tree, nodeSelector, dataSelector);
	}

	/** Construct the iterator.
	 * @param node is the node to iterate
	 */
	public DataBroadFirstTreeIterator(N node) {
		super(node);
	}

	/** Construct the iterator.
	 * @param node is the node to iterate
	 * @param nodeSelector permits to filter the node repliable by this iterator.
	 */
	public DataBroadFirstTreeIterator(N node, NodeSelector<N> nodeSelector) {
		super(node, nodeSelector);
	}

	/** Construct the iterator.
	 * @param node is the node to iterate
	 * @param dataSelector permits to filter the user data repliable by this iterator.
	 */
	public DataBroadFirstTreeIterator(N node, DataSelector<D> dataSelector) {
		super(node, dataSelector);
	}

	/** Construct the iterator.
	 * @param node is the node to iterate
	 * @param nodeSelector permits to filter the node repliable by this iterator.
	 * @param dataSelector permits to filter the user data repliable by this iterator.
	 */
	public DataBroadFirstTreeIterator(N node, NodeSelector<N> nodeSelector, DataSelector<D> dataSelector) {
		super(node, nodeSelector, dataSelector);
	}

}
