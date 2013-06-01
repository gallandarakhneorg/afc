/* 
 * $Id$
 * 
 * Copyright (c) 2011, Multiagent Team,
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

/**
 * This interface is used to specify the base functions
 * of a tree node which is usable by an tree iterator.
 * 
 * @param <N> is the type of the tree nodes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public interface IterableNode<N extends IterableNode<?>> {
	
	/** Replies count of children in this node.
	 * <p>
	 * The number of children is greater or equal
	 * to the value replied by {@link #getNotNullChildCount()}.
	 * 
	 * @return the count of children.
	 * @see #getNotNullChildCount()
	 */
	public int getChildCount();

	/** Replies count of not-null children in this node.
	 * <p>
	 * The number of not-null children is lower or equal
	 * to the value replied by {@link #getChildCount()}.
	 * 
	 * @return the count of not-null children.
	 * @see #getChildCount()
	 */
	public int getNotNullChildCount();

	/** Replies the n-th child in this node.
	 * 
	 * @param index is the index of the child to reply
	 * @return the child node.
	 * @throws IndexOutOfBoundsException if the given index was invalid
	 */
	public N getChildAt(int index) throws IndexOutOfBoundsException;
	
	/** Replies if this node is a leaf.
	 * 
	 * @return <code>true</code> is this node is a leaf,
	 * otherwise <code>false</code>
	 */
	public boolean isLeaf();

	/** Remove this node from its parent.
	 * 
	 * @return the parent node from which the node was removed.
	 */
	public N removeFromParent();

}