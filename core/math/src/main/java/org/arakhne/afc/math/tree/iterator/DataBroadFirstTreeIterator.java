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
 * This class is an iterator on a tree that replies the user data.
 * <p>
 * This iterator go thru the tree in a broad-first order.
 * 
 * @param <D> is the type of the data inside the tree
 * @param <N> is the type of the tree nodes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class DataBroadFirstTreeIterator<D,N extends TreeNode<D,N>>
extends AbstractDataTreeIterator<D,N> {

	/**
	 * @param tree is the tree to iterate
	 */
	public DataBroadFirstTreeIterator(Tree<D,N> tree) {
		this(tree.getRoot(),null,null);
	}

	/**
	 * @param tree is the tree to iterate
	 * @param nodeSelector permits to filter the node repliable by this iterator.
	 */
	public DataBroadFirstTreeIterator(Tree<D,N> tree, NodeSelector<N> nodeSelector) {
		this(tree.getRoot(),nodeSelector,null);
	}

	/**
	 * @param tree is the tree to iterate
	 * @param dataSelector permits to filter the user data repliable by this iterator.
	 */
	public DataBroadFirstTreeIterator(Tree<D,N> tree, DataSelector<D> dataSelector) {
		this(tree.getRoot(), null, dataSelector);
	}

	/**
	 * @param tree is the tree to iterate
	 * @param nodeSelector permits to filter the node repliable by this iterator.
	 * @param dataSelector permits to filter the user data repliable by this iterator.
	 */
	public DataBroadFirstTreeIterator(Tree<D,N> tree, NodeSelector<N> nodeSelector, DataSelector<D> dataSelector) {
		this(tree.getRoot(), nodeSelector, dataSelector);
	}
	
	/**
	 * @param node is the node to iterate
	 */
	public DataBroadFirstTreeIterator(N node) {
		this(node,null,null);
	}

	/**
	 * @param node is the node to iterate
	 * @param nodeSelector permits to filter the node repliable by this iterator.
	 */
	public DataBroadFirstTreeIterator(N node, NodeSelector<N> nodeSelector) {
		this(node,nodeSelector,null);
	}

	/**
	 * @param node is the node to iterate
	 * @param dataSelector permits to filter the user data repliable by this iterator.
	 */
	public DataBroadFirstTreeIterator(N node, DataSelector<D> dataSelector) {
		this(node,null,dataSelector);
	}

	/**
	 * @param node is the node to iterate
	 * @param nodeSelector permits to filter the node repliable by this iterator.
	 * @param dataSelector permits to filter the user data repliable by this iterator.
	 */
	public DataBroadFirstTreeIterator(N node, NodeSelector<N> nodeSelector, DataSelector<D> dataSelector) {
		this(new BroadFirstTreeIterator<>(node, nodeSelector), dataSelector);
	}

	/**
	 * @param nodeIterator1 is the node iterator.
	 * @param dataSelector permits to filter the user data repliable by this iterator.
	 */
	private DataBroadFirstTreeIterator(BroadFirstTreeIterator<N> nodeIterator1, DataSelector<D> dataSelector) {
		super(nodeIterator1, dataSelector);
	}
	
}
