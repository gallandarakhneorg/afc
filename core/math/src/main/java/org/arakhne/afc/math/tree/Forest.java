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

import java.util.Collection;
import java.util.Iterator;

import org.arakhne.afc.math.tree.iterator.DepthFirstNodeOrder;
import org.arakhne.afc.math.tree.iterator.InfixDepthFirstTreeIterator;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * This is the generic definition of a
 * forest of trees.
 * <p>
 * A forest of trees is a collection of trees.
 * 
 * @param <D> is the type of the data inside the forest
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface Forest<D> extends Collection<Tree<D,?>> {
	
	/** Replies the minimal height of the forest.
	 * 
	 * @return the height of the uppest leaf in the forest.
	 */
	@Pure
	public int getMinHeight();

	/** Replies the maximal height of the forest.
	 * 
	 * @return the height of the lowest leaf in the forest.
	 */
	@Pure
	public int getMaxHeight();

	/** Replies the heights of all the leaf nodes.
	 * The order of the heights is given by a depth-first iteration.
	 * 
	 * @return the heights of the leaf nodes
	 */
	@Pure
	public int[] getHeights();

	/** Replies a depth-first iterator on nodes.
	 * 
	 * @param nodeOrder indicates how the data are treated by the iterator. 
	 * @return a depth first iterator on nodes
	 */
	@Pure
	public Iterator<TreeNode<D,?>> depthFirstIterator(DepthFirstNodeOrder nodeOrder);

	/** Replies a depth-first iterator on nodes.
	 * 
	 * @return a depth-first iterator.
	 */
	@Pure
	public Iterator<TreeNode<D,?>> depthFirstIterator();

	/** Replies a broad first iterator on nodes.
	 * 
	 * @return a broad first iterator.
	 */
	@Pure
	public Iterator<TreeNode<D,?>> broadFirstIterator();

	/** Replies a prefixed depth first iterator on the tree.
	 * 
	 * @return a prefixed depth first iterator on data.
	 */
	@Pure
	public Iterator<D> dataDepthFirstIterator();

	/** Replies a depth first iterator on the tree.
	 * 
	 * @param nodeOrder indicates how the data are treated by the iterator. 
	 * @return an iterator on the data.
	 */
	@Pure
	public Iterator<D> dataDepthFirstIterator(DepthFirstNodeOrder nodeOrder);

	/** Replies the depth first iterator on the tree.
	 * 
	 * @param infixPosition is the index at which the parent
	 * will be treated according to its child set.
	 * @return the iterator on user data
	 * @see InfixDepthFirstTreeIterator
	 */
	@Pure
	public Iterator<D> dataDepthFirstIterator(int infixPosition);


	/** Replies the broad-first iterator on the tree.
	 * 
	 * @return the iterator on user data.
	 */
	@Pure
	public Iterator<D> dataBroadFirstIterator();

}