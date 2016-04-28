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

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * This is the generic implementation of a
 * tree service that permits to iterate
 * with a broad-first approach.
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
public interface BroadFirstIterableTree<D,N extends TreeNode<D,?>> extends Tree<D,N> {

	@Override
	@Pure
	public Iterator<N> broadFirstIterator();

	@Override
	@Pure
	public Iterator<D> dataBroadFirstIterator();

	/** Replies the broad-first iterator on the tree.
	 * 
	 * @return the iterator on nodes.
	 */
	@Pure
	public Iterable<N> toBroadFirstIterable();

	/** Replies the broad-first iterator on the tree.
	 * 
	 * @return the iterator on user data.
	 */
	@Pure
	public Iterable<D> toDataBroadFirstIterable();
	
}