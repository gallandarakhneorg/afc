/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.arakhne.afc.math.tree;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * This is the generic implementation of a tree node.
 *
 * @param <D> is the type of the data inside the tree
 * @param <N> is the type of the tree nodes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface TreeNode<D, N extends TreeNode<D, ?>> extends IterableNode<N>, Comparable<N> {

	/** Replies the parent node or <code>null</code>.
	 *
	 * @return the node that is containing this object.
	 */
	@Pure
	N getParentNode();

	/** Set the n-th child in this node.
	 *
	 * @param index is the index of the child to reply
	 * @param child is the new child node.
	 * @return <code>true</code> if it was set, otherwise <code>false</code>
	 * @throws IndexOutOfBoundsException if the given index was invalid
	 */
	boolean setChildAt(int index, N child) throws IndexOutOfBoundsException;

	/** Replies the index of the specified child.
	 *
	 * @param child is the node to search for.
	 * @return the index or <code>-1</code>.
	 */
	@Pure
	int indexOf(N child);

	/** Replies the enumeration type that is defining
	 * the children of the nodes.
	 *
	 * @return the enumeration type of the child partition,
	 *     or <code>null</code> if no such enumeration type is
	 *     defined.
	 */
	@Pure
	Class<? extends Enum<?>> getPartitionEnumeration();

	/** Replies the all user data associated to this node.
	 *
	 * @param array is the array to fill if it has enough cells
	 * @return the parameter {@code a} if it has enough cells, otherwise another array.
	 */
	D[] getAllUserData(D[] array);

	/** Replies the all user data associated to this node.
	 *
	 * @return all the user data
	 */
	@Pure
	List<D> getAllUserData();

	/** Replies the first user data associated to this node.
	 *
	 * @return first user data
	 */
	@Pure
	D getUserData();

	/** Replies the count of user data associated to this node.
	 *
	 * @return the count of user data.
	 */
	@Pure
	int getUserDataCount();

	/** Replies the user data associated to this node which
	 * is at the specified index.
	 *
	 * @param index is the position of the data to reply
	 * @return the user data
	 * @throws IndexOutOfBoundsException when the given index in not valid
	 */
	@Pure
	D getUserDataAt(int index) throws IndexOutOfBoundsException;

	/** Add a user data associated to this node.
	 *
	 * @param data the data to add.
	 * @return <code>true</code> if the the list of user data has changed,
	 *     otherwise <code>false</code>.
	 */
	boolean addUserData(Collection<? extends D> data);

	/** Add a user data associated to this node.
	 *
	 * @param index is the position where to put the elements in the user data list.
	 * @param data the data to add.
	 * @return <code>true</code> if the the list of user data has changed,
	 *     otherwise <code>false</code>.
	 */
	boolean addUserData(int index, Collection<? extends D> data);

	/** Add a user data associated to this node.
	 *
	 * @param data the data to add.
	 * @return <code>true</code> if the data was successfully added,
	 *     otherwhise <code>false</code>
	 */
	boolean addUserData(D data);

	/** Add a user data associated to this node.
	 *
	 * @param index is the position where to insert the data.
	 * @param data the data to add.
	 */
	void addUserData(int index, D data);

	/** Remove a user data associated to this node.
	 *
	 * @param data the data to remove.
	 * @return <code>true</code> if the data was successfully removed,
	 *     otherwhise <code>false</code>
	 */
	boolean removeUserData(Collection<D> data);

	/** Remove a user data associated to this node.
	 *
	 * @param data the data to remove.
	 * @return <code>true</code> if the data was successfully removed,
	 *     otherwhise <code>false</code>
	 */
	boolean removeUserData(D data);

	/** Remove a user data associated to this node.
	 *
	 * @param index is the position of the user data to remove.
	 * @return the removed data or <code>null</code> if the data
	 *     was not removed.
	 */
	D removeUserData(int index);

	/** Set the user data associated to this node.
	 *
	 * @param data the data to put inside.
	 * @return <code>true</code> if the data was successfully set,
	 *     otherwhise <code>false</code>
	 */
	boolean setUserData(Collection<D> data);

	/** Set the user data associated to this node.
	 *
	 * @param data the data to put inside.
	 * @return <code>true</code> if the data was successfully set,
	 *     otherwhise <code>false</code>
	 */
	boolean setUserData(D data);

	/** Set the user data associated to this node at the specified index.
	 *
	 * @param index the index at which the data must be set.
	 * @param data the data to put inside.
	 * @return <code>true</code> if the data was successfully set,
	 *     otherwhise <code>false</code>
	 * @throws IndexOutOfBoundsException if the given index was invalid
	 */
	boolean setUserDataAt(int index, D data) throws IndexOutOfBoundsException;

	/** Add a listener on the node events.
	 *
	 * @param listener the listener.
	 */
	void addTreeNodeListener(TreeNodeListener listener);

	/** Remove a listener on the node events.
	 *
	 * @param listener the listener.
	 */
	void removeTreeNodeListener(TreeNodeListener listener);

	/** Replies if this node is a leaf and has no user data.
	 *
	 * <p>This function is equivalent to call <code>{@link #isLeaf()} &amp;&amp;
	 * {@link #getUserDataCount()}==0</code>
	 *
	 * @return <code>true</code> is this node is a leaf without user data,
	 *     otherwise <code>false</code>
	 */
	@Pure
	boolean isEmpty();

	/** Replies if this node is a root.
	 *
	 * @return <code>true</code> is this node is the root,
	 *     otherwise <code>false</code>
	 */
	@Pure
	boolean isRoot();

	/** Replies if this node is a valid. The validity of a node depends of
	 * the node implementation.
	 *
	 * @return <code>true</code> is this node is valid,
	 *     otherwise <code>false</code>
	 */
	@Pure
	boolean isValid();

	/** Clear the tree.
	 *
	 * <p>Caution: this method also destroyes the
	 * links between the child nodes inside the tree.
	 * If you want to unlink the first-level
	 * child node with
	 * this node but leave the rest of the tree
	 * unchanged, please call <code>setChildAt(i,null)</code>.
	 */
	void clear();

	/** Remove all the user data associated to this node.
	 */
	void removeAllUserData();

	/** Remove the specified node from this node if it is a child.
	 *
	 * @param child is the child to remove.
	 * @return <code>true</code> if the node was removed, otherwise <code>false</code>
	 */
	boolean removeChild(N child);

	/** Remove this node from its parent and remove the parent if
	 * it is becoming empty, and so one.
	 */
	void removeDeeplyFromParent();

	/** Replies the count of nodes inside the tree for which the root
	 * is this node.
	 *
	 * @return the count of node inside the subtree.
	 */
	@Pure
	int getDeepNodeCount();

	/** Replies the count of user data inside the tree for which the root
	 * is this node.
	 *
	 * @return the count of user data inside the subtree.
	 */
	@Pure
	int getDeepUserDataCount();

	/** Replies the not-null child nodes of this node.
	 *
	 * <p>The given iterator never replies a null value for a child node.
	 *
	 * @return the children.
	 * @see #getChildren(Object[])
	 * @see #getChildren(Class)
	 */
	@Pure
	Iterator<N> children();

	/** Replies the child nodes of this node.
	 *
	 * <p>This function may put <code>null</code> in the
	 * array cells if the current tree node has not a child
	 * at the corresponding index.
	 *
	 * @param type is the type of the children to reply.
	 * @return the children.
	 * @see #getChildren(Object[])
	 * @see #children()
	 */
	@Pure
	N[] getChildren(Class<N> type);

	/** Replies the child nodes of this node.
	 *
	 * <p>This function may put <code>null</code> in the
	 * array cells if the current tree node has not a child
	 * at the corresponding index.
	 *
	 * @param array is the array to fill.
	 * @see #getChildren(Class)
	 * @see #children()
	 */
	void getChildren(Object[] array);

	/** Replies the depth level of this node.
	 * Depth level <code>0</code> is the root node,
	 * <code>1</code> are the children of the root node, etc.
	 *
	 * @return the height of the lowest leaf in the tree.
	 */
	@Pure
	int getDepth();

	/** Replies the minimal height of the tree.
	 *
	 * @return the height of the uppest leaf in the tree.
	 */
	@Pure
	int getMinHeight();

	/** Replies the maximal height of the tree.
	 *
	 * @return the height of the lowest leaf in the tree.
	 */
	@Pure
	int getMaxHeight();

	/** Replies the heights of all the leaf nodes.
	 * The order of the heights is given by a depth-first iteration.
	 *
	 * @return the heights of the leaf nodes
	 */
	@Pure
	int[] getHeights();

	/** Move this node in the given new node.
	 *
	 * <p>If any child node is already present
	 * at the given position in the new parent node,
	 * the tree node may replace the existing node
	 * or insert the moving node according to its
	 * implementation.
	 *
	 * <p>This function is preferred to a sequence of calls
	 * to {@link #removeFromParent()} and {@link #setChildAt(int, TreeNode)}
	 * because it fires a limited set of events dedicated to the move
	 * the node.
	 *
	 * @param newParent is the new parent for this node.
	 * @param index is the position of this node in the new parent.
	 * @return <code>true</code> on success, otherwise <code>false</code>.
	 */
	boolean moveTo(N newParent, int index);

}
