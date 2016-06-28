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

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.tree.Tree;
import org.arakhne.afc.math.tree.TreeNode;


/**
 * This class is an prefixed iterator on a tree.
 * It treats the parent node before going inside the child nodes.
 *
 * @param <N> is the type of the tree nodes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class PrefixDepthFirstTreeIterator<N extends TreeNode<?, N>>
		extends AbstractPrefixDepthFirstTreeIterator<N, N>
		implements NodeSelectionTreeIterator<N> {

	private NodeSelector<N> selector;

	/**
	 * @param tree is the tree to iterate.
	 */
	public PrefixDepthFirstTreeIterator(Tree<?, N> tree) {
		this(tree.getRoot(), null);
	}

	/**
	 * @param tree is the tree to iterate.
	 * @param selector permits to filter the nodes replied/traversed by this iterator.
	 */
	public PrefixDepthFirstTreeIterator(Tree<?, N> tree, NodeSelector<N> selector) {
		this(tree.getRoot(), selector);
	}

	/**
	 * @param node is the node to iterate.
	 */
	public PrefixDepthFirstTreeIterator(N node) {
		this(node, null);
	}

	/**
	 * @param node is the node to iterate.
	 * @param selector permits to filter the nodes replied/traversed by this iterator.
	 */
	public PrefixDepthFirstTreeIterator(N node, NodeSelector<N> selector) {
		super(node);
		this.selector = selector;
	}

	@Override
	public void setNodeSelector(NodeSelector<N> selector) {
		this.selector = selector;
	}

	@Override
	@Pure
	protected boolean isTraversableParent(N parent) {
		return parent != null && (this.selector == null || this.selector.nodeCouldBeTreatedByIterator(parent));
	}

	@Override
	@Pure
	protected N toTraversableChild(N parent, N child) {
		assert child != null && parent != null;
		if (this.selector == null || this.selector.nodeCouldBeTreatedByIterator(child)) {
			return child;
		}
		return null;
	}

}
