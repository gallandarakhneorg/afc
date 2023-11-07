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

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.tree.Tree;
import org.arakhne.afc.math.tree.TreeNode;

/** This class is an iterator on a tree.
 *
 * <p>The node A is treated <i>before</i> its children.
 *
 * @param <N> is the type of the tree nodes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 18.0 (replacement for 13.0 and higher)
 */
public class BreadthFirstTreeIterator<N extends TreeNode<?, N>>
		extends AbstractBreadthFirstTreeIterator<N, N>
		implements NodeSelectionTreeIterator<N> {

	private NodeSelector<N> selector;

	/** Create an iterator on the given tree.
	 *
	 * @param tree is the tree to iterate.
	 */
	public BreadthFirstTreeIterator(Tree<?, N> tree) {
		this(tree.getRoot(), null);
	}

	/** Create an iterator on the given tree.
	 *
	 * @param tree is the tree to iterate.
	 * @param selector permits to filter the node that could be returned by this iterator.
	 */
	public BreadthFirstTreeIterator(Tree<?, N> tree, NodeSelector<N> selector) {
		this(tree.getRoot(), selector);
	}

	/** Create an iterator on the given node.
	 *
	 * @param node is the node to iterate.
	 */
	public BreadthFirstTreeIterator(N node) {
		this(node, null);
	}

	/** Create an iterator on the given node.
	 *
	 * @param node is the node to iterate.
	 * @param selector permits to filter the node that could be returned by this iterator.
	 */
	public BreadthFirstTreeIterator(N node, NodeSelector<N> selector) {
		super(select(selector, node));
		this.selector = selector;
	}

	private static <N extends TreeNode<?, N>> N select(NodeSelector<N> selector, N node) {
		if ((node != null) && ((selector == null) || (selector.nodeCouldBeTreatedByIterator(node)))) {
			return node;
		}
		return null;
	}

	@Override
	public void setNodeSelector(NodeSelector<N> selector1) {
		this.selector = selector1;
	}

	@Pure
	@Override
	protected N toTraversableChild(N parent, N child, int childIndex, int notNullChildIndex) {
		assert parent != null && child != null;
		if (this.selector == null || this.selector.nodeCouldBeTreatedByIterator(child)) {
			return child;
		}
		return null;
	}

}
