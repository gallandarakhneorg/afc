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

import org.arakhne.afc.math.tree.Tree;
import org.arakhne.afc.math.tree.TreeNode;


/**
 * This class is an infixed depth-first iterator on a tree data.
 *
 * <p>This iterator has an infix position which
 * describes when to treat the parent node inside
 * the set of child treatments.
 * Significant values of this position are:
 * <table border="1" width="100%" summary="Values of the parents' position">
 * <tr><td><code>0</code></td><td>the parent is
 * treated before the child at index <code>0</code>.
 * This is equivalent to a prefixed depth-first iterator.
 * We recommend to use {@link PrefixDepthFirstTreeIterator}
 * insteed of this iterator class.</td></tr>
 * <tr><td>between <code>1</code> and <code>getChildCount()-1</code></td><td>the parent is
 * treated before the child at the specified index.</td></tr>
 * <tr><td><code>getChildCount()</code></td><td>the parent is
 * treated after the last child.
 * This is equivalent to a postfixed depth-first iterator.
 * We recommend to use {@link PostfixDepthFirstTreeIterator}
 * insteed of this iterator class.</td></tr>
 * </table>
 *
 * <p>By default this iterator assumes an infix index that corresponds
 * to the middle of each child set (ie, <code>getChildCount()/2</code>).
 *
 * @param <D> is the type of the data inside the tree
 * @param <N> is the type of the tree nodes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class InfixDataDepthFirstTreeIterator<D, N extends TreeNode<D, N>>
		extends AbstractDataTreeIterator<D, N> {

	/**
	 * @param tree is the tree to iterate.
	 */
	public InfixDataDepthFirstTreeIterator(Tree<D, N> tree) {
		this(tree.getRoot(), null, null);
	}

	/**
	 * @param tree is the tree to iterate.
	 * @param nodeSelector permits to filter the nodes replied/traversed by this iterator.
	 */
	public InfixDataDepthFirstTreeIterator(Tree<D, N> tree, NodeSelector<N> nodeSelector) {
		this(tree.getRoot(), nodeSelector, null);
	}

	/**
	 * @param tree is the tree to iterate.
	 * @param dataSelector permits to filter the user data replied by this iterator.
	 */
	public InfixDataDepthFirstTreeIterator(Tree<D, N> tree, DataSelector<D> dataSelector) {
		this(tree.getRoot(), null, dataSelector);
	}

	/**
	 * @param tree is the tree to iterate.
	 * @param nodeSelector permits to filter the nodes replied/traversed by this iterator.
	 * @param dataSelector permits to filter the user data replied by this iterator.
	 */
	public InfixDataDepthFirstTreeIterator(Tree<D, N> tree, NodeSelector<N> nodeSelector, DataSelector<D> dataSelector) {
		this(tree.getRoot(), nodeSelector, dataSelector);
	}

	/**
	 * @param tree is the tree to iterate.
	 * @param infixPosition is the index of the child which is assumed to be at the infixed position.
	 */
	public InfixDataDepthFirstTreeIterator(Tree<D, N> tree, int infixPosition) {
		this(tree.getRoot(), infixPosition, null, null);
	}

	/**
	 * @param tree is the tree to iterate.
	 * @param infixPosition is the index of the child which is assumed to be at the infixed position.
	 * @param nodeSelector permits to filter the nodes replied/traversed by this iterator.
	 */
	public InfixDataDepthFirstTreeIterator(Tree<D, N> tree, int infixPosition, NodeSelector<N> nodeSelector) {
		this(tree.getRoot(), infixPosition, nodeSelector, null);
	}

	/**
	 * @param tree is the tree to iterate.
	 * @param infixPosition is the index of the child which is assumed to be at the infixed position.
	 * @param dataSelector permits to filter the user data replied by this iterator.
	 */
	public InfixDataDepthFirstTreeIterator(Tree<D, N> tree, int infixPosition, DataSelector<D> dataSelector) {
		this(tree.getRoot(), infixPosition, null, dataSelector);
	}

	/**
	 * @param tree is the tree to iterate.
	 * @param infixPosition is the index of the child which is assumed to be at the infixed position.
	 * @param nodeSelector permits to filter the nodes replied/traversed by this iterator.
	 * @param dataSelector permits to filter the user data replied by this iterator.
	 */
	public InfixDataDepthFirstTreeIterator(Tree<D, N> tree, int infixPosition, NodeSelector<N> nodeSelector,
			DataSelector<D> dataSelector) {
		this(tree.getRoot(), infixPosition, nodeSelector, dataSelector);
	}

	/**
	 * @param node is the node to iterate.
	 */
	public InfixDataDepthFirstTreeIterator(N node) {
		this(node, null, null);
	}

	/**
	 * @param node is the node to iterate.
	 * @param nodeSelector permits to filter the nodes replied/traversed by this iterator.
	 */
	public InfixDataDepthFirstTreeIterator(N node, NodeSelector<N> nodeSelector) {
		this(node, nodeSelector, null);
	}

	/**
	 * @param node is the node to iterate.
	 * @param dataSelector permits to filter the user data replied by this iterator.
	 */
	public InfixDataDepthFirstTreeIterator(N node, DataSelector<D> dataSelector) {
		this(node, null, dataSelector);
	}

	/**
	 * @param node is the node to iterate.
	 * @param nodeSelector permits to filter the nodes replied/traversed by this iterator.
	 * @param dataSelector permits to filter the user data replied by this iterator.
	 */
	public InfixDataDepthFirstTreeIterator(N node, NodeSelector<N> nodeSelector, DataSelector<D> dataSelector) {
		super(new InfixDepthFirstTreeIterator<>(node, nodeSelector), dataSelector);
	}

	/**
	 * @param node is the node to iterate.
	 * @param infixPosition is the index of the child which is assumed to be at the infixed position.
	 */
	public InfixDataDepthFirstTreeIterator(N node, int infixPosition) {
		this(node, infixPosition, null, null);
	}

	/**
	 * @param node is the node to iterate.
	 * @param infixPosition is the index of the child which is assumed to be at the infixed position.
	 * @param nodeSelector permits to filter the nodes replied/traversed by this iterator.
	 */
	public InfixDataDepthFirstTreeIterator(N node, int infixPosition, NodeSelector<N> nodeSelector) {
		this(node, infixPosition, nodeSelector, null);
	}

	/**
	 * @param node is the node to iterate.
	 * @param infixPosition is the index of the child which is assumed to be at the infixed position.
	 * @param dataSelector permits to filter the user data replied by this iterator.
	 */
	public InfixDataDepthFirstTreeIterator(N node, int infixPosition, DataSelector<D> dataSelector) {
		this(node, infixPosition, null, dataSelector);
	}

	/**
	 * @param node is the node to iterate.
	 * @param infixPosition is the index of the child which is assumed to be at the infixed position.
	 * @param nodeSelector permits to filter the nodes replied/traversed by this iterator.
	 * @param dataSelector permits to filter the user data replied by this iterator.
	 */
	public InfixDataDepthFirstTreeIterator(N node, int infixPosition, NodeSelector<N> nodeSelector,
			DataSelector<D> dataSelector) {
		super(new InfixDepthFirstTreeIterator<>(node, infixPosition, nodeSelector), dataSelector);
	}

}
