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
 * This class is an prefixed iterator on a tree data.
 * It treats the node's data before going inside the child nodes.
 * 
 * @param <D> is the type of the data inside the tree
 * @param <N> is the type of the tree nodes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class PrefixDataDepthFirstTreeIterator<D,N extends TreeNode<D,N>>
extends AbstractDataTreeIterator<D,N> {

	/**
	 * @param tree is the tree to iterate.
	 */
	public PrefixDataDepthFirstTreeIterator(Tree<D,N> tree) {
		this(tree.getRoot(), null, null);
	}

	/**
	 * @param tree is the tree to iterate.
	 * @param nodeSelector permits to filter the nodes replied/traversed by this iterator.
	 */
	public PrefixDataDepthFirstTreeIterator(Tree<D,N> tree, NodeSelector<N> nodeSelector) {
		this(tree.getRoot(), nodeSelector, null);
	}

	/**
	 * @param tree is the tree to iterate.
	 * @param dataSelector permits to filter the user data replied by this iterator.
	 */
	public PrefixDataDepthFirstTreeIterator(Tree<D,N> tree, DataSelector<D> dataSelector) {
		this(tree.getRoot(), null, dataSelector);
	}

	/**
	 * @param tree is the tree to iterate.
	 * @param nodeSelector permits to filter the nodes replied/traversed by this iterator.
	 * @param dataSelector permits to filter the user data replied by this iterator.
	 */
	public PrefixDataDepthFirstTreeIterator(Tree<D,N> tree, NodeSelector<N> nodeSelector, DataSelector<D> dataSelector) {
		this(tree.getRoot(), nodeSelector, dataSelector);
	}
	
	/**
	 * @param node is the node to iterate.
	 */
	public PrefixDataDepthFirstTreeIterator(N node) {
		this(node,null,null);
	}

	/**
	 * @param node is the node to iterate.
	 * @param nodeSelector permits to filter the nodes replied/traversed by this iterator.
	 */
	public PrefixDataDepthFirstTreeIterator(N node, NodeSelector<N> nodeSelector) {
		this(node,nodeSelector,null);
	}

	/**
	 * @param node is the node to iterate.
	 * @param dataSelector permits to filter the user data replied by this iterator.
	 */
	public PrefixDataDepthFirstTreeIterator(N node, DataSelector<D> dataSelector) {
		this(node,null,dataSelector);
	}

	/**
	 * @param node is the node to iterate.
	 * @param nodeSelector permits to filter the nodes replied/traversed by this iterator.
	 * @param dataSelector permits to filter the user data replied by this iterator.
	 */
	public PrefixDataDepthFirstTreeIterator(N node, NodeSelector<N> nodeSelector, DataSelector<D> dataSelector) {
		super(new PrefixDepthFirstTreeIterator<N>(node, nodeSelector), dataSelector);
	}

}
