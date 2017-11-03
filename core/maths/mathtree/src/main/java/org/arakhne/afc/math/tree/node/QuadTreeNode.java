/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

package org.arakhne.afc.math.tree.node;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Collection;
import java.util.List;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.tree.TreeNode;


/**
 * This is the generic implementation of a
 * tree for which each node has four children.
 *
 * <p><h3>moveTo</h3>
 * According to its definition in
 * {@link TreeNode#moveTo(TreeNode, int)}, the binary
 * tree node implementation of <code>moveTo</code>
 * replaces any existing node at the position given as
 * parameter of <code>moveTo</code>..
 *
 * @param <D> is the type of the data inside the tree
 * @param <N> is the type of the tree nodes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public abstract class QuadTreeNode<D, N extends QuadTreeNode<D, N>> extends AbstractTreeNode<D, N> {

	private static final long serialVersionUID = 5760376281112333537L;

	private N nNorthWest;

	private N nNorthEast;

	private N nSouthWest;

	private N nSouthEast;

	/**
	 * Empty node.
	 */
	public QuadTreeNode() {
		this(DEFAULT_LINK_LIST_USE);
	}

	/** Construct a node.
	 *
	 * @param data are initial user data
	 */
	public QuadTreeNode(Collection<D> data) {
		super(DEFAULT_LINK_LIST_USE, data);
		this.nNorthWest = null;
		this.nNorthEast = null;
		this.nSouthWest = null;
		this.nSouthEast = null;
	}

	/** Construct a node.
	 *
	 * @param data are initial user data
	 */
	public QuadTreeNode(D data) {
		this(DEFAULT_LINK_LIST_USE, data);
	}

	/** Constructor.
	 * @param useLinkedList indicates if a linked list must be used to store the data.
	 *     If <code>false</code>, an ArrayList will be used.
	 */
	public QuadTreeNode(boolean useLinkedList) {
		super(useLinkedList);
		this.nNorthWest = null;
		this.nNorthEast = null;
		this.nSouthWest = null;
		this.nSouthEast = null;
	}

	/** Constructor.
	 * @param useLinkedList indicates if a linked list must be used to store the data.
	 *     If <code>false</code>, an ArrayList will be used.
	 * @param copyDataCollection indicates if the given data collection is copied
	 *     if <code>true</code> or the inner data collection will be the given
	 *     collection itself if <code>false</code>.
	 * @param data are initial user data
	 */
	public QuadTreeNode(boolean useLinkedList, boolean copyDataCollection, List<D> data) {
		super(useLinkedList, copyDataCollection, data);
		this.nNorthWest = null;
		this.nNorthEast = null;
		this.nSouthWest = null;
		this.nSouthEast = null;
	}

	/** Constructor.
	 * @param useLinkedList indicates if a linked list must be used to store the data.
	 *     If <code>false</code>, an ArrayList will be used.
	 * @param data are initial user data
	 */
	public QuadTreeNode(boolean useLinkedList, D data) {
		super(useLinkedList, data);
		this.nNorthWest = null;
		this.nNorthEast = null;
		this.nSouthWest = null;
		this.nSouthEast = null;
	}

	@Override
	@Pure
	public Class<? extends Enum<?>> getPartitionEnumeration() {
		return QuadTreeZone.class;
	}

	/** Invoked when this object must be deserialized.
	 *
	 * @param in is the input stream.
	 * @throws IOException in case of input stream access error.
	 * @throws ClassNotFoundException if some class was not found.
	 */
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		final N me = toN();
		if (this.nNorthEast != null) {
			this.nNorthEast.setParentNodeReference(me, false);
		}
		if (this.nNorthWest != null) {
			this.nNorthWest.setParentNodeReference(me, false);
		}
		if (this.nSouthEast != null) {
			this.nSouthEast.setParentNodeReference(me, false);
		}
		if (this.nSouthWest != null) {
			this.nSouthWest.setParentNodeReference(me, false);
		}
	}

	/** Clear the tree.
	 *
	 * <p>Caution: this method also destroyes the
	 * links between the child nodes inside the tree.
	 * If you want to unlink the first-level
	 * child node with
	 * this node but leave the rest of the tree
	 * unchanged, please call <code>setChildAt(i,null)</code>.
	 */
	@Override
	public void clear() {
		if (this.nNorthWest != null) {
			final N child = this.nNorthWest;
			setFirstChild(null);
			child.clear();
		}
		if (this.nNorthEast != null) {
			final N child = this.nNorthEast;
			setSecondChild(null);
			child.clear();
		}
		if (this.nSouthWest != null) {
			final N child = this.nSouthWest;
			setThirdChild(null);
			child.clear();
		}
		if (this.nSouthEast != null) {
			final N child = this.nSouthEast;
			setFourthChild(null);
			child.clear();
		}
		removeAllUserData();
	}

	@Override
	@Pure
	@SuppressWarnings("checkstyle:magicnumber")
	public int getChildCount() {
		return 4;
	}

	@Override
	@Pure
	public int getNotNullChildCount() {
		return this.notNullChildCount;
	}

	@Override
	@Pure
	public N getChildAt(int index) throws IndexOutOfBoundsException {
		final QuadTreeZone[] zones = QuadTreeZone.values();
		if (index >= 0 && index < zones.length) {
			return getChildAt(zones[index]);
		}
		throw new IndexOutOfBoundsException();
	}

	/** Replies the node that is corresponding to the given zone.
	 *
	 * @param zone the zone.
	 * @return the child node for the given zone, or <code>null</code>.
	 */
	@Pure
	public N getChildAt(QuadTreeZone zone) {
		switch (zone) {
		case NORTH_WEST:
			return this.nNorthWest;
		case NORTH_EAST:
			return this.nNorthEast;
		case SOUTH_WEST:
			return this.nSouthWest;
		case SOUTH_EAST:
			return this.nSouthEast;
		default:
		}
		return null;
	}

	/** Set the first child of this node.
	 *
	 * @param newChild is the new child for the first zone
	 * @return <code>true</code> on success, otherwhise <code>false</code>
	 */
	public boolean setFirstChild(N newChild) {
		final N oldChild = this.nNorthWest;
		if (oldChild == newChild) {
			return false;
		}

		if (oldChild != null) {
			oldChild.setParentNodeReference(null, true);
			--this.notNullChildCount;
			firePropertyChildRemoved(0, oldChild);
		}

		if (newChild != null) {
			final N oldParent = newChild.getParentNode();
			if (oldParent != this) {
				newChild.removeFromParent();
			}
		}

		this.nNorthWest = newChild;

		if (newChild != null) {
			newChild.setParentNodeReference(toN(), true);
			++this.notNullChildCount;
			firePropertyChildAdded(0, newChild);
		}

		return true;
	}

	/** Get the first child of this node.
	 *
	 * @return the child for the first zone
	 */
	@Pure
	public final N getFirstChild() {
		return this.nNorthWest;
	}

	/** Set the second child of this node.
	 *
	 * @param newChild is the new child for the second zone
	 * @return <code>true</code> on success, otherwhise <code>false</code>
	 */
	public boolean setSecondChild(N newChild) {
		final N oldChild = this.nNorthEast;
		if (oldChild == newChild) {
			return false;
		}

		if (oldChild != null) {
			oldChild.setParentNodeReference(null, true);
			--this.notNullChildCount;
			firePropertyChildRemoved(1, oldChild);
		}

		if (newChild != null) {
			final N oldParent = newChild.getParentNode();
			if (oldParent != this) {
				newChild.removeFromParent();
			}
		}

		this.nNorthEast = newChild;

		if (newChild != null) {
			newChild.setParentNodeReference(toN(), true);
			++this.notNullChildCount;
			firePropertyChildAdded(1, newChild);
		}

		return true;
	}

	/** Get the second child of this node.
	 *
	 * @return the child for the second zone
	 */
	@Pure
	public final N getSecondChild() {
		return this.nNorthEast;
	}

	/** Set the third child of this node.
	 *
	 * @param newChild is the new child for the third zone
	 * @return <code>true</code> on success, otherwhise <code>false</code>
	 */
	public boolean setThirdChild(N newChild) {
		final N oldChild = this.nSouthWest;
		if (oldChild == newChild) {
			return false;
		}

		if (oldChild != null) {
			oldChild.setParentNodeReference(null, true);
			--this.notNullChildCount;
			firePropertyChildRemoved(2, oldChild);
		}

		if (newChild != null) {
			final N oldParent = newChild.getParentNode();
			if (oldParent != this) {
				newChild.removeFromParent();
			}
		}

		this.nSouthWest = newChild;

		if (newChild != null) {
			newChild.setParentNodeReference(toN(), true);
			++this.notNullChildCount;
			firePropertyChildAdded(2, newChild);
		}

		return true;
	}

	/** Get the third child of this node.
	 *
	 * @return the child for the third zone
	 */
	@Pure
	public final N getThirdChild() {
		return this.nSouthWest;
	}

	/** Set the Fourth child of this node.
	 *
	 * @param newChild is the new child for the fourth zone
	 * @return <code>true</code> on success, otherwhise <code>false</code>
	 */
	public boolean setFourthChild(N newChild) {
		final N oldChild = this.nSouthEast;
		if (oldChild == newChild) {
			return false;
		}

		if (oldChild != null) {
			oldChild.setParentNodeReference(null, true);
			--this.notNullChildCount;
			firePropertyChildRemoved(3, oldChild);
		}

		if (newChild != null) {
			final N oldParent = newChild.getParentNode();
			if (oldParent != this) {
				newChild.removeFromParent();
			}
		}

		this.nSouthEast = newChild;

		if (newChild != null) {
			newChild.setParentNodeReference(toN(), true);
			++this.notNullChildCount;
			firePropertyChildAdded(3, newChild);
		}

		return true;
	}

	/** Get the fourth child of this node.
	 *
	 * @return the child for the fourth zone
	 */
	@Pure
	public final N getFourthChild() {
		return this.nSouthEast;
	}

	@Override
	@Pure
	public boolean isLeaf() {
		return this.nNorthEast == null && this.nNorthWest == null && this.nSouthEast == null && this.nSouthWest == null;
	}

	@Override
	public boolean moveTo(N newParent, int index) {
		return moveTo(newParent, index, false);
	}

	/** Move this node in the given new node.
	 *
	 * <p>This function is preferred to a sequence of calls
	 * to {@link #removeFromParent()} and {@link #setChildAt(int, QuadTreeNode)}
	 * because it fires a limited set of events dedicated to the move
	 * the node.
	 *
	 * @param newParent is the new parent for this node.
	 * @param zone is the position of this node in the new parent.
	 * @return <code>true</code> on success, otherwise <code>false</code>.
	 */
	public boolean moveTo(N newParent, QuadTreeZone zone) {
		return moveTo(newParent, zone.ordinal());
	}

	@Override
	public boolean setChildAt(int index, N newChild) throws IndexOutOfBoundsException {
		final QuadTreeZone[] zones = QuadTreeZone.values();
		if (index >= 0 && index < zones.length) {
			return setChildAt(zones[index], newChild);
		}
		throw new IndexOutOfBoundsException();
	}

	/** Set the child at the specified zone.
	 *
	 * @param zone is the zone to set
	 * @param newChild is the new node for the given zone
	 * @return <code>true</code> on success, <code>false</code> otherwise
	 */
	public boolean setChildAt(QuadTreeZone zone, N newChild) {
		switch (zone) {
		case NORTH_WEST:
			return setFirstChild(newChild);
		case NORTH_EAST:
			return setSecondChild(newChild);
		case SOUTH_WEST:
			return setThirdChild(newChild);
		case SOUTH_EAST:
			return setFourthChild(newChild);
		default:
		}
		return false;
	}

	@Override
	@SuppressWarnings("checkstyle:npathcomplexity")
	protected void setChildAtWithoutEventFiring(int index, N newChild) throws IndexOutOfBoundsException {
		switch (QuadTreeZone.values()[index]) {
		case NORTH_EAST:
			if (this.nNorthEast != null) {
				--this.notNullChildCount;
			}
			this.nNorthEast = newChild;
			if (this.nNorthEast != null) {
				++this.notNullChildCount;
			}
			break;
		case NORTH_WEST:
			if (this.nNorthWest != null) {
				--this.notNullChildCount;
			}
			this.nNorthWest = newChild;
			if (this.nNorthWest != null) {
				++this.notNullChildCount;
			}
			break;
		case SOUTH_EAST:
			if (this.nSouthEast != null) {
				--this.notNullChildCount;
			}
			this.nSouthEast = newChild;
			if (this.nSouthEast != null) {
				++this.notNullChildCount;
			}
			break;
		case SOUTH_WEST:
			if (this.nSouthWest != null) {
				--this.notNullChildCount;
			}
			this.nSouthWest = newChild;
			if (this.nSouthWest != null) {
				++this.notNullChildCount;
			}
			break;
		default:
			throw new IndexOutOfBoundsException();
		}
	}

	@Override
	public boolean removeChild(N child) {
		if (child != null) {
			if (child == this.nNorthWest) {
				return setFirstChild(null);
			} else if (child == this.nNorthEast) {
				return setSecondChild(null);
			} else if (child == this.nSouthWest) {
				return setThirdChild(null);
			} else if (child == this.nSouthEast) {
				return setFourthChild(null);
			}
		}
		return false;
	}

	@Override
	@Pure
	public int indexOf(N child) {
		final QuadTreeZone zone = zoneOf(child);
		if (zone == null) {
			return -1;
		}
		return zone.ordinal();
	}

	/** Replies the zone of the specified child.
	 *
	 * @param child the child.
	 * @return the index or <code>null</code>.
	 */
	@Pure
	public QuadTreeZone zoneOf(N child) {
		if (child == this.nNorthWest) {
			return QuadTreeZone.NORTH_WEST;
		}
		if (child == this.nNorthEast) {
			return QuadTreeZone.NORTH_EAST;
		}
		if (child == this.nSouthWest) {
			return QuadTreeZone.SOUTH_WEST;
		}
		if (child == this.nSouthEast) {
			return QuadTreeZone.SOUTH_EAST;
		}
		return null;
	}

	@Override
	public void getChildren(Object[] array) {
		if (array != null) {
			final QuadTreeZone[] zones = QuadTreeZone.values();
			for (int i = 0; i < zones.length && i < array.length; ++i) {
				array[i] = getChildAt(zones[i]);
			}
		}
	}

	@Override
	@Pure
	public int getMinHeight() {
		return 1 + MathUtil.min(
				this.nNorthWest != null ? this.nNorthWest.getMinHeight() : 0,
				this.nNorthEast != null ? this.nNorthEast.getMinHeight() : 0,
				this.nSouthWest != null ? this.nSouthWest.getMinHeight() : 0,
				this.nSouthEast != null ? this.nSouthEast.getMinHeight() : 0);
	}

	@Override
	@Pure
	public int getMaxHeight() {
		return 1 + MathUtil.max(
				this.nNorthWest != null ? this.nNorthWest.getMaxHeight() : 0,
				this.nNorthEast != null ? this.nNorthEast.getMaxHeight() : 0,
				this.nSouthWest != null ? this.nSouthWest.getMaxHeight() : 0,
				this.nSouthEast != null ? this.nSouthEast.getMaxHeight() : 0);
	}

	@Override
	protected void getHeights(int currentHeight, List<Integer> heights) {
		if (isLeaf()) {
			heights.add(new Integer(currentHeight));
		} else {
			if (this.nNorthWest != null) {
				this.nNorthWest.getHeights(currentHeight + 1, heights);
			}
			if (this.nNorthEast != null) {
				this.nNorthEast.getHeights(currentHeight + 1, heights);
			}
			if (this.nSouthWest != null) {
				this.nSouthWest.getHeights(currentHeight + 1, heights);
			}
			if (this.nSouthEast != null) {
				this.nSouthEast.getHeights(currentHeight + 1, heights);
			}
		}
	}

	/**
	 * Available zones of a quad tree.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	public enum QuadTreeZone {
		/** upper left zone.
		 */
		NORTH_WEST,
		/** upper right zone.
		 */
		NORTH_EAST,
		/** lower left zone.
		 */
		SOUTH_WEST,
		/** lower right zone.
		 */
		SOUTH_EAST;

		/** Replies the zone corresponding to the given index.
		 * The index is the same as the ordinal value of the
		 * enumeration. If the given index does not correspond
		 * to an ordinal value, <code>null</code> is replied.
		 *
		 * @param index the index.
		 * @return the zone or <code>null</code>
		 */
		public static QuadTreeZone fromInteger(int index) {
			if (index < 0) {
				return null;
			}
			final QuadTreeZone[] nodes = values();
			if (index >= nodes.length) {
				return null;
			}
			return nodes[index];
		}

	}

	/**
	 * This is the generic implementation of a quad
	 * tree.
	 *
	 * @param <D> is the type of the data inside the tree
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	public static class DefaultQuadTreeNode<D> extends QuadTreeNode<D, DefaultQuadTreeNode<D>> {

		private static final long serialVersionUID = -3283371007433469124L;

		/** Construct a node.
		 */
		public DefaultQuadTreeNode() {
			super();
		}

		/** Construct a node.
		 * @param data are the initial user data
		 */
		public DefaultQuadTreeNode(Collection<D> data) {
			super(data);
		}

		/** Construct a node.
		 * @param data are the initial user data
		 */
		public DefaultQuadTreeNode(D data) {
			super(data);
		}

	}

}
