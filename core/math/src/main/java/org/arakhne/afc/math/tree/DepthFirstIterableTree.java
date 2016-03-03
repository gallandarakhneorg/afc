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
package org.arakhne.afc.math.tree;

import java.util.Iterator;

import org.arakhne.afc.math.tree.iterator.DepthFirstNodeOrder;
import org.arakhne.afc.math.tree.iterator.InfixDepthFirstTreeIterator;


/**
 * This is the generic implementation of a
 * tree service that permits to iterator
 * with a depth-first approach.
 * <p>
 * This is the public interface for a tree which
 * is independent of the tree implementation.
 * 
 * @param <D> is the type of the data inside the tree
 * @param <N> is the type of the tree nodes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface DepthFirstIterableTree<D,N extends TreeNode<D,?>> extends Tree<D,N> {

	/** Replies the depth first iterator on the tree.
	 * 
	 * @param nodeOrder is the order in which the parent node
	 * will be treated in comparison to its children.
	 * @return the iterator on nodes
	 */
	@Override
	public Iterator<N> depthFirstIterator(DepthFirstNodeOrder nodeOrder);

	/** Replies the infixes depth first iterator on the tree.
	 * 
	 * @param infixPosition is the index at which the parent
	 * will be treated according to its child set.
	 * @return the iterator on nodes
	 * @see InfixDepthFirstTreeIterator
	 */
	public Iterator<N> depthFirstIterator(int infixPosition);

	/** Replies the depth first iterator on the tree.
	 * 
	 * @return the iterator on nodes
	 */
	@Override
	public Iterator<N> depthFirstIterator();

	/** Replies the depth first iterator on the tree.
	 * 
	 * @return the iterator on user data
	 */
	@Override
	public Iterator<D> dataDepthFirstIterator();

	/** Replies the depth first iterator on the tree.
	 * 
	 * @return the iterator on nodes
	 */
	public Iterable<N> toDepthFirstIterable();

	/** Replies the depth first iterator on the tree.
	 * 
	 * @param nodeOrder is the order in which the parent node
	 * will be treated in comparison to its children.
	 * @return the iterator on nodes
	 */
	public Iterable<N> toDepthFirstIterable(DepthFirstNodeOrder nodeOrder);

	/** Replies the infixed depth first iterator on the tree.
	 * 
	 * @param infixPosition is the index at which the parent
	 * will be treated according to its child set.
	 * @return the iterator on nodes
	 * @see InfixDepthFirstTreeIterator
	 */
	public Iterable<N> toDepthFirstIterable(int infixPosition);

	/** Replies the depth first iterator on the tree.
	 * 
	 * @return the iterator on user data
	 */
	public Iterable<D> toDataDepthFirstIterable();

	/** Replies the depth first iterator on the tree.
	 * 
	 * @param nodeOrder is the order in which the parent node
	 * will be treated in comparison to its children.
	 * @return the iterator on user data
	 */
	public Iterable<D> toDataDepthFirstIterable(DepthFirstNodeOrder nodeOrder);

	/** Replies the infixed depth first iterator on the tree.
	 * 
	 * @param infixPosition is the index at which the parent
	 * will be treated according to its child set.
	 * @return the iterator on user data
	 * @see InfixDepthFirstTreeIterator
	 */
	public Iterable<D> toDataDepthFirstIterable(int infixPosition);

}