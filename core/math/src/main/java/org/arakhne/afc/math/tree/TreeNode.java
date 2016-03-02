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
import java.util.List;

/**
 * This is the generic implementation of a tree node.
 * 
 * @param <D> is the type of the data inside the tree
 * @param <N> is the type of the tree nodes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface TreeNode<D,N extends TreeNode<D,?>> extends IterableNode<N>, Comparable<N> {
	
	/** Replies the parent node or <code>null</code>.
	 * 
	 * @return the node that is containing this object.
	 */
	public N getParentNode();
	
	/** Set the n-th child in this node.
	 * 
	 * @param index is the index of the child to reply
	 * @param child is the new child node.
	 * @return <code>true</code> if it was set, otherwise <code>false</code>
	 * @throws IndexOutOfBoundsException if the given index was invalid
	 */
	public boolean setChildAt(int index, N child) throws IndexOutOfBoundsException;

	/** Replies the index of the specified child.
	 * 
	 * @param child is the node to search for.
	 * @return the index or <code>-1</code>.
	 */
	public int indexOf(N child);

	/** Replies the enumeration type that is defining
	 * the children of the nodes.
	 * 
	 * @return the enumeration type of the child partition, 
	 * or <code>null</code> if no such enumeration type is
	 * defined.
	 * @since 4.0
	 */
	public Class<? extends Enum<?>> getPartitionEnumeration();

	/** Replies the all user data associated to this node.
	 * 
	 * @param a is the array to fill if it has enough cells
	 * @return the parameter <var>a</var> if it has enough cells, otherwise another array.
	 */
	public D[] getAllUserData(D[] a);
	
	/** Replies the all user data associated to this node.
	 * 
	 * @return all the user data
	 */
	public List<D> getAllUserData();

	/** Replies the first user data associated to this node.
	 * 
	 * @return first user data
	 */
	public D getUserData();

	/** Replies the count of user data associated to this node.
	 * 
	 * @return the count of user data.
	 */
	public int getUserDataCount();

	/** Replies the user data associated to this node which
	 * is at the specified index.
	 * 
	 * @param index is the position of the data to reply
	 * @return the user data
	 * @throws IndexOutOfBoundsException when the given index in not valid
	 */
	public D getUserDataAt(int index) throws IndexOutOfBoundsException;

	/** Add a user data associated to this node.
	 *
	 * @param data
	 * @return <code>true</code> if the the list of user data has changed,
	 * otherwise <code>false</code>.
	 */
	public boolean addUserData(Collection<? extends D> data);

	/** Add a user data associated to this node.
	 *
	 * @param index is the position where to put the elements in the user data list.
	 * @param data
	 * @return <code>true</code> if the the list of user data has changed,
	 * otherwise <code>false</code>.
	 */
	public boolean addUserData(int index, Collection<? extends D> data);

	/** Add a user data associated to this node.
	 * 
	 * @param data
	 * @return <code>true</code> if the data was successfully added,
	 * otherwhise <code>false</code>
	 */
	public boolean addUserData(D data);

	/** Add a user data associated to this node.
	 * 
	 * @param index is the position where to insert the data.
	 * @param data
	 */
	public void addUserData(int index, D data);

	/** Remove a user data associated to this node.
	 * 
	 * @param data
	 * @return <code>true</code> if the data was successfully removed,
	 * otherwhise <code>false</code>
	 */
	public boolean removeUserData(Collection<D> data);

	/** Remove a user data associated to this node.
	 * 
	 * @param data
	 * @return <code>true</code> if the data was successfully removed,
	 * otherwhise <code>false</code>
	 */
	public boolean removeUserData(D data);

	/** Remove a user data associated to this node.
	 * 
	 * @param index is the position of the user data to remove.
	 * @return the removed data or <code>null</code> if the data
	 * was not removed.
	 */
	public D removeUserData(int index);

	/** Set the user data associated to this node.
	 * 
	 * @param data
	 * @return <code>true</code> if the data was successfully set,
	 * otherwhise <code>false</code>
	 */
	public boolean setUserData(Collection<D> data);

	/** Set the user data associated to this node.
	 * 
	 * @param data
	 * @return <code>true</code> if the data was successfully set,
	 * otherwhise <code>false</code>
	 */
	public boolean setUserData(D data);

	/** Set the user data associated to this node at the specified index.
	 * 
	 * @param index
	 * @param data
	 * @return <code>true</code> if the data was successfully set,
	 * otherwhise <code>false</code>
	 * @throws IndexOutOfBoundsException if the given index was invalid
	 */
	public boolean setUserDataAt(int index, D data) throws IndexOutOfBoundsException;

	/** Add a listener on the node events.
	 * 
	 * @param listener
	 */
	public void addTreeNodeListener(TreeNodeListener listener);

	/** Remove a listener on the node events.
	 * 
	 * @param listener
	 */
	public void removeTreeNodeListener(TreeNodeListener listener);

	/** Replies if this node is a leaf and has no user data.
	 * <p>
	 * This function is equivalent to call <code>{@link #isLeaf()} &amp;&amp;
	 * {@link #getUserDataCount()}==0</code>
	 * 
	 * @return <code>true</code> is this node is a leaf without user data,
	 * otherwise <code>false</code>
	 */
	public boolean isEmpty();

	/** Replies if this node is a root.
	 * 
	 * @return <code>true</code> is this node is the root,
	 * otherwise <code>false</code>
	 */
	public boolean isRoot();

	/** Replies if this node is a valid. The validity of a node depends of
	 * the node implementation.
	 * 
	 * @return <code>true</code> is this node is valid,
	 * otherwise <code>false</code>
	 */
	public boolean isValid();

	/** Clear the tree.
	 * <p>
	 * Caution: this method also destroyes the
	 * links between the child nodes inside the tree.
	 * If you want to unlink the first-level
	 * child node with
	 * this node but leave the rest of the tree
	 * unchanged, please call <code>setChildAt(i,null)</code>.
	 */
	public void clear();

	/** Remove all the user data associated to this node.
	 */
	public void removeAllUserData();
	
	/** Remove the specified node from this node if it is a child.
	 * 
	 * @param child is the child to remove.
	 * @return <code>true</code> if the node was removed, otherwise <code>false</code>
	 */
	public boolean removeChild(N child);
	
	/** Remove this node from its parent and remove the parent if
	 * it is becoming empty, and so one.
	 */
	public void removeDeeplyFromParent();

	/** Replies the count of nodes inside the tree for which the root
	 * is this node.
	 * 
	 * @return the count of node inside the subtree.
	 */
	public int getDeepNodeCount();
	
	/** Replies the count of user data inside the tree for which the root
	 * is this node.
	 * 
	 * @return the count of user data inside the subtree.
	 */
	public int getDeepUserDataCount();

	/** Replies the not-null child nodes of this node.
	 * <p>
	 * The given iterator never replies a null value for a child node. 
	 * 
	 * @return the children.
	 * @see #getChildren(Object[])
	 * @see #getChildren(Class)
	 */
	public Iterator<N> children();
	
	/** Replies the child nodes of this node.
	 * <p>
	 * This function may put <code>null</code> in the
	 * array cells if the current tree node has not a child
	 * at the corresponding index. 
	 * 
	 * @param type is the type of the children to reply.
	 * @return the children.
	 * @see #getChildren(Object[])
	 * @see #children()
	 */
	public N[] getChildren(Class<N> type);

	/** Replies the child nodes of this node.
	 * <p>
	 * This function may put <code>null</code> in the
	 * array cells if the current tree node has not a child
	 * at the corresponding index. 
	 * 
	 * @param array is the array to fill.
	 * @see #getChildren(Class)
	 * @see #children()
	 */
	public void getChildren(Object[] array);

	/** Replies the depth level of this node.
	 * Depth level <code>0</code> is the root node,
	 * <code>1</code> are the children of the root node, etc.
	 * 
	 * @return the height of the lowest leaf in the tree.
	 * @since 4.0
	 */
	public int getDepth();

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

	/** Move this node in the given new node.
	 * <p>
	 * If any child node is already present
	 * at the given position in the new parent node,
	 * the tree node may replace the existing node
	 * or insert the moving node according to its
	 * implementation.
	 * <p>
	 * This function is preferred to a sequence of calls
	 * to {@link #removeFromParent()} and {@link #setChildAt(int, TreeNode)}
	 * because it fires a limited set of events dedicated to the move
	 * the node.
	 * 
	 * @param newParent is the new parent for this node.
	 * @param index is the position of this node in the new parent.
	 * @return <code>true</code> on success, otherwise <code>false</code>.
	 * @since 4.0
	 */
	public boolean moveTo(N newParent, int index);

}