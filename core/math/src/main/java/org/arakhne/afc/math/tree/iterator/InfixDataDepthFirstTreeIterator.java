/* 
 * $Id$
 * 
 * Copyright (c) 2005-11, Multiagent Team,
 * Laboratoire Systemes et Transports,
 * Universite de Technologie de Belfort-Montbeliard.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Laboratoire Systemes et Transports
 * of the Universite de Technologie de Belfort-Montbeliard ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 *
 * http://www.multiagent.fr/
 */
package org.arakhne.afc.math.tree.iterator;

import org.arakhne.afc.math.tree.Tree;
import org.arakhne.afc.math.tree.TreeNode;


/**
 * This class is an infixed depth-first iterator on a tree data.
 * <p>
 * This iterator has an infix position which
 * describes when to treat the parent node inside
 * the set of child treatments.
 * Significant values of this position are:
 * <table>
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
 * <p>
 * By default this iterator assumes an infix index that corresponds
 * to the middle of each child set (ie, <code>getChildCount()/2</code>).
 * 
 * @param <D> is the type of the data inside the tree
 * @param <N> is the type of the tree nodes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class InfixDataDepthFirstTreeIterator<D,N extends TreeNode<D,N>>
extends AbstractDataTreeIterator<D,N> {

	/**
	 * @param tree is the tree to iterate.
	 */
	public InfixDataDepthFirstTreeIterator(Tree<D,N> tree) {
		this(tree.getRoot(),null,null);
	}

	/**
	 * @param tree is the tree to iterate.
	 * @param nodeSelector permits to filter the nodes replied/traversed by this iterator.
	 */
	public InfixDataDepthFirstTreeIterator(Tree<D,N> tree, NodeSelector<N> nodeSelector) {
		this(tree.getRoot(),nodeSelector,null);
	}

	/**
	 * @param tree is the tree to iterate.
	 * @param dataSelector permits to filter the user data replied by this iterator.
	 */
	public InfixDataDepthFirstTreeIterator(Tree<D,N> tree, DataSelector<D> dataSelector) {
		this(tree.getRoot(),null,dataSelector);
	}

	/**
	 * @param tree is the tree to iterate.
	 * @param nodeSelector permits to filter the nodes replied/traversed by this iterator.
	 * @param dataSelector permits to filter the user data replied by this iterator.
	 */
	public InfixDataDepthFirstTreeIterator(Tree<D,N> tree, NodeSelector<N> nodeSelector, DataSelector<D> dataSelector) {
		this(tree.getRoot(), nodeSelector, dataSelector);
	}
	
	/**
	 * @param tree is the tree to iterate.
	 * @param infixPosition is the index of the child which is assumed to be at the infixed position.
	 */
	public InfixDataDepthFirstTreeIterator(Tree<D,N> tree, int infixPosition) {
		this(tree.getRoot(),infixPosition,null,null);
	}

	/**
	 * @param tree is the tree to iterate.
	 * @param infixPosition is the index of the child which is assumed to be at the infixed position.
	 * @param nodeSelector permits to filter the nodes replied/traversed by this iterator.
	 */
	public InfixDataDepthFirstTreeIterator(Tree<D,N> tree, int infixPosition, NodeSelector<N> nodeSelector) {
		this(tree.getRoot(),infixPosition,nodeSelector,null);
	}

	/**
	 * @param tree is the tree to iterate.
	 * @param infixPosition is the index of the child which is assumed to be at the infixed position.
	 * @param dataSelector permits to filter the user data replied by this iterator.
	 */
	public InfixDataDepthFirstTreeIterator(Tree<D,N> tree, int infixPosition, DataSelector<D> dataSelector) {
		this(tree.getRoot(),infixPosition,null,dataSelector);
	}

	/**
	 * @param tree is the tree to iterate.
	 * @param infixPosition is the index of the child which is assumed to be at the infixed position.
	 * @param nodeSelector permits to filter the nodes replied/traversed by this iterator.
	 * @param dataSelector permits to filter the user data replied by this iterator.
	 */
	public InfixDataDepthFirstTreeIterator(Tree<D,N> tree, int infixPosition, NodeSelector<N> nodeSelector, DataSelector<D> dataSelector) {
		this(tree.getRoot(), infixPosition, nodeSelector, dataSelector);
	}

	/**
	 * @param node is the node to iterate.
	 */
	public InfixDataDepthFirstTreeIterator(N node) {
		this(node,null,null);
	}

	/**
	 * @param node is the node to iterate.
	 * @param nodeSelector permits to filter the nodes replied/traversed by this iterator.
	 */
	public InfixDataDepthFirstTreeIterator(N node, NodeSelector<N> nodeSelector) {
		this(node,nodeSelector,null);
	}

	/**
	 * @param node is the node to iterate.
	 * @param dataSelector permits to filter the user data replied by this iterator.
	 */
	public InfixDataDepthFirstTreeIterator(N node, DataSelector<D> dataSelector) {
		this(node,null,dataSelector);
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
		this(node,infixPosition,null,null);
	}

	/**
	 * @param node is the node to iterate.
	 * @param infixPosition is the index of the child which is assumed to be at the infixed position.
	 * @param nodeSelector permits to filter the nodes replied/traversed by this iterator.
	 */
	public InfixDataDepthFirstTreeIterator(N node, int infixPosition, NodeSelector<N> nodeSelector) {
		this(node,infixPosition,nodeSelector,null);
	}

	/**
	 * @param node is the node to iterate.
	 * @param infixPosition is the index of the child which is assumed to be at the infixed position.
	 * @param dataSelector permits to filter the user data replied by this iterator.
	 */
	public InfixDataDepthFirstTreeIterator(N node, int infixPosition, DataSelector<D> dataSelector) {
		this(node,infixPosition,null,dataSelector);
	}

	/**
	 * @param node is the node to iterate.
	 * @param infixPosition is the index of the child which is assumed to be at the infixed position.
	 * @param nodeSelector permits to filter the nodes replied/traversed by this iterator.
	 * @param dataSelector permits to filter the user data replied by this iterator.
	 */
	public InfixDataDepthFirstTreeIterator(N node, int infixPosition, NodeSelector<N> nodeSelector, DataSelector<D> dataSelector) {
		super(new InfixDepthFirstTreeIterator<>(node, infixPosition, nodeSelector), dataSelector);
	}

}
