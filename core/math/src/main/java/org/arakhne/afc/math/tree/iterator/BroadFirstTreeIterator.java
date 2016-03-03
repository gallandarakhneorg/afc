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
 * This class is an iterator on a tree.
 * <p>
 * The node A is treated <i>before</i> its children.
 * 
 * @param <N> is the type of the tree nodes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class BroadFirstTreeIterator<N extends TreeNode<?,N>>
extends AbstractBroadFirstTreeIterator<N,N>
implements NodeSelectionTreeIterator<N> {

	private static <N extends TreeNode<?,N>> N select(NodeSelector<N> selector, N node) {
		if ((node!=null) && ((selector==null) || (selector.nodeCouldBeTreatedByIterator(node))))
			return node;
		return null;
	}
	
	private NodeSelector<N> selector;

	/** Create an iterator on the given tree
	 * 
	 * @param tree is the tree to iterate.
	 */
	public BroadFirstTreeIterator(Tree<?,N> tree) {
		this(tree.getRoot(),null);
	}

	/** Create an iterator on the given tree
	 * 
	 * @param tree is the tree to iterate.
	 * @param selector1 permits to filter the node repliable by this iterator.
	 */
	public BroadFirstTreeIterator(Tree<?,N> tree, NodeSelector<N> selector1) {
		this(tree.getRoot(), selector1);
	}
	
	/** Create an iterator on the given node
	 * 
	 * @param node is the node to iterate.
	 */
	public BroadFirstTreeIterator(N node) {
		this(node,null);
	}

	/** Create an iterator on the given node
	 * 
	 * @param node is the node to iterate.
	 * @param selector1 permits to filter the node repliable by this iterator.
	 */
	public BroadFirstTreeIterator(N node, NodeSelector<N> selector1) {
		super(select(selector1, node));
		this.selector = selector1;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setNodeSelector(NodeSelector<N> selector1) {
		this.selector = selector1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected N toTraversableChild(N parent, N child, int childIndex, int notNullChildIndex) {
		assert(parent!=null && child!=null);
		if (this.selector==null || this.selector.nodeCouldBeTreatedByIterator(child))
			return child;
		return null;
	}
			
}
