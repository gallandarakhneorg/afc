/* 
 * $Id$
 * 
 * Copyright (c) 2005-10, Multiagent Team,
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
 * tree.
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
public interface Tree<D,N extends TreeNode<D,?>> extends Iterable<N> {

	/** Replies the root of the tree.
	 * 
	 * @return the root of the tree.
	 */
	public N getRoot();
	
	/** Set the root of the tree.
	 * 
	 * @param newRoot is the new root node.
	 */
	public void setRoot(N newRoot);
	
	/** Clear the tree.
	 * <p>
	 * Caution: this method also destroyes the
	 * links between the nodes inside the tree.
	 * If you want to unlink the root node with
	 * this tree but leave the nodes' links
	 * unchanged, please call <code>setRoot(null)</code>.
	 */
	public void clear();
	
	/** Replies the count of nodes inside this tree.
	 * 
	 * @return the count of nodes inside the whole tree.
	 */
	public int getNodeCount();

	/** Replies the count of data inside this tree.
	 * 
	 * @return the count of user objects inside the whole tree.
	 */
	public int getUserDataCount();
	
	/** Replies if this tree contains user data.
	 * 
	 * @return <code>true</code> if the tree is empty (no user data),
	 * otherwise <code>false</code>
	 */
	public boolean isEmpty();

	/** Replies the minimal height of the tree.
	 * 
	 * @return the height of the uppest leaf in the tree.
	 */
	public int getMinHeight();

	/** Replies the maximal height of the tree.
	 * 
	 * @return the height of the lowest leaf in the tree.
	 */
	public int getMaxHeight();

	/** Replies the heights of all the leaf nodes.
	 * The order of the heights is given by a depth-first iteration.
	 * 
	 * @return the heights of the leaf nodes
	 */
	public int[] getHeights();

	/** Replies a depth-first iterator on nodes.
	 * 
	 * @param nodeOrder indicates how the data are treated by the iterator. 
	 * @return a depth first iterator on nodes
	 */
	public Iterator<N> depthFirstIterator(DepthFirstNodeOrder nodeOrder);

	/** Replies a depth-first iterator on nodes.
	 * 
	 * @return a depth-first iterator.
	 */
	public Iterator<N> depthFirstIterator();

	/** Replies a broad first iterator on nodes.
	 * 
	 * @return a broad first iterator.
	 */
	public Iterator<N> broadFirstIterator();

	/** Replies a prefixed depth first iterator on the tree.
	 * 
	 * @return a prefixed depth first iterator on data.
	 */
	public Iterator<D> dataDepthFirstIterator();

	/** Replies a depth first iterator on the tree.
	 * 
	 * @param nodeOrder indicates how the data are treated by the iterator. 
	 * @return an iterator on the data.
	 */
	public Iterator<D> dataDepthFirstIterator(DepthFirstNodeOrder nodeOrder);

	/** Replies the depth first iterator on the tree.
	 * 
	 * @param infixPosition is the index at which the parent
	 * will be treated according to its child set.
	 * @return the iterator on user data
	 * @see InfixDepthFirstTreeIterator
	 */
	public Iterator<D> dataDepthFirstIterator(int infixPosition);


	/** Replies the broad-first iterator on the tree.
	 * 
	 * @return the iterator on user data.
	 */
	public Iterator<D> dataBroadFirstIterator();

}