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
 * This class is an postfixed iterator on a tree.
 * It treats the parent node after going inside the child nodes.
 * 
 * @param <N> is the type of the tree nodes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class PostfixDepthFirstTreeIterator<N extends TreeNode<?,N>>
extends AbstractPostfixDepthFirstTreeIterator<N,N>
implements NodeSelectionTreeIterator<N> {

	private NodeSelector<N> selector;

	/**
	 * @param tree is the tree to iterate.
	 */
	public PostfixDepthFirstTreeIterator(Tree<?,N> tree) {
		this(tree.getRoot(), null);
	}

	/**
	 * @param tree is the tree to iterate.
	 * @param selector permits to filter the nodes replied/traversed by this iterator.
	 */
	@SuppressWarnings("hiding")
	public PostfixDepthFirstTreeIterator(Tree<?,N> tree, NodeSelector<N> selector) {
		this(tree.getRoot(), selector);
	}
	
	/**
	 * @param node is the node to iterate.
	 */
	public PostfixDepthFirstTreeIterator(N node) {
		this(node,null);
	}

	/**
	 * @param node is the node to iterate.
	 * @param selector permits to filter the nodes replied/traversed by this iterator.
	 */
	@SuppressWarnings("hiding")
	public PostfixDepthFirstTreeIterator(N node, NodeSelector<N> selector) {
		super(node);
		this.selector = selector;
	}

	/** {@inheritDoc}
	 */
	@SuppressWarnings("hiding")
	@Override
	public void setNodeSelector(NodeSelector<N> selector) {
		this.selector = selector;
	}
		
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean isTraversableParent(N parent) {
		return (parent!=null && (this.selector==null || this.selector.nodeCouldBeTreatedByIterator(parent)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected N toTraversableChild(N parent, N child) {
		assert(child!=null & parent!=null);
		if (this.selector==null || this.selector.nodeCouldBeTreatedByIterator(child)) {
			return child;
		}
		return null;
	}

}
