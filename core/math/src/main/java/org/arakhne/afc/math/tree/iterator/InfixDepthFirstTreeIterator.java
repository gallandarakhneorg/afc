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
import org.eclipse.xtext.xbase.lib.Pure;


/**
 * This class is an infixed depth-first iterator on a tree.
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
 * @param <N> is the type of the tree nodes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class InfixDepthFirstTreeIterator<N extends TreeNode<?,N>>
extends AbstractInfixDepthFirstTreeIterator<N,N>
implements NodeSelectionTreeIterator<N> {
	
	private NodeSelector<N> selector;
	
	/**
	 * @param tree is the tree to iterate.
	 */
	public InfixDepthFirstTreeIterator(Tree<?,N> tree) {
		this(tree.getRoot(), -1, null);
	}

	/**
	 * @param tree is the tree to iterate.
	 * @param selector permits to filter the nodes replied/traversed by this iterator.
	 */
	public InfixDepthFirstTreeIterator(Tree<?,N> tree, NodeSelector<N> selector) {
		this(tree.getRoot(), -1, selector);
	}
	
	/**
	 * @param tree is the tree to iterate.
	 * @param infixPosition is the index of the child which is assumed to be at the infixed position.
	 */
	public InfixDepthFirstTreeIterator(Tree<?,N> tree, int infixPosition) {
		this(tree.getRoot(), infixPosition, null);
	}

	/**
	 * @param tree is the tree to iterate.
	 * @param infixPosition is the index of the child which is assumed to be at the infixed position.
	 * @param selector permits to filter the nodes replied/traversed by this iterator.
	 */
	public InfixDepthFirstTreeIterator(Tree<?,N> tree, int infixPosition, NodeSelector<N> selector) {
		this(tree.getRoot(), infixPosition, selector);
	}

	/**
	 * @param node is the node to iterate.
	 */
	public InfixDepthFirstTreeIterator(N node) {
		this(node,null);
	}

	/**
	 * @param node is the node to iterate.
	 * @param selector permits to filter the nodes replied/traversed by this iterator.
	 */
	public InfixDepthFirstTreeIterator(N node, NodeSelector<N> selector) {
		super(node);
		this.selector = selector;
	}
	
	/**
	 * @param node is the node to iterate.
	 * @param infixPosition is the index of the child which is assumed to be at the infixed position.
	 */
	public InfixDepthFirstTreeIterator(N node, int infixPosition) {
		this(node,infixPosition,null);
	}

	/**
	 * @param node is the node to iterate.
	 * @param infixPosition is the index of the child which is assumed to be at the infixed position.
	 * @param selector permits to filter the nodes replied/traversed by this iterator.
	 */
	public InfixDepthFirstTreeIterator(N node, int infixPosition, NodeSelector<N> selector) {
		super(node, infixPosition);
		this.selector = selector;
	}

	@Override
	public void setNodeSelector(NodeSelector<N> selector) {
		this.selector = selector;
	}

	@Pure
	@Override
	protected boolean isTraversableParent(N parent) {
		return (parent!=null && (this.selector==null || this.selector.nodeCouldBeTreatedByIterator(parent)));
	}

	@Pure
	@Override
	protected N toTraversableChild(N parent, N child) {
		assert(child!=null & parent!=null);
		if (this.selector==null || this.selector.nodeCouldBeTreatedByIterator(child)) {
			return child;
		}
		return null;
	}
		
}
