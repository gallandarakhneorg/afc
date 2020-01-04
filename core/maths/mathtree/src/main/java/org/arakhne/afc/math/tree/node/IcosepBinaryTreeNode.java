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

package org.arakhne.afc.math.tree.node;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Collection;
import java.util.List;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.tree.IcosepTreeNodeContainer;

/** This is the generic implementation of a
 * tree for which each node has two children and
 * that implements the Icosep heuristic.
 *
 * <p>The icosep heuristic implies that all objects
 * that intersect the split lines will be stored inside
 * an addition child that contains only the corresponding
 * objects.
 *
 * @param <D> is the type of the data inside the tree
 * @param <N> is the type of the tree nodes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public abstract class IcosepBinaryTreeNode<D, N extends IcosepBinaryTreeNode<D, N>>
		extends BinaryTreeNode<D, N> implements IcosepTreeNodeContainer<N> {

	private static final long serialVersionUID = -5640709107014467219L;

	private N nicosep;

	/**
	 * Empty node.
	 */
	public IcosepBinaryTreeNode() {
		super();
		this.nicosep = null;
	}

	/** Construct node.
	 * @param data are the initial user data
	 */
	public IcosepBinaryTreeNode(Collection<D> data) {
		super(data);
		this.nicosep = null;
	}

	/** Construct node.
	 * @param data are the initial user data
	 */
	public IcosepBinaryTreeNode(D data) {
		super(data);
		this.nicosep = null;
	}

	/** Construct node.
	 * @param useLinkedList indicates if a linked list must be used to store the data.
	 *     If <code>false</code>, an ArrayList will be used.
	 */
	public IcosepBinaryTreeNode(boolean useLinkedList) {
		super(useLinkedList);
		this.nicosep = null;
	}

	/** Construct node.
	 * @param useLinkedList indicates if a linked list must be used to store the data.
	 *     If <code>false</code>, an ArrayList will be used.
	 * @param copyDataCollection indicates if the given data collection is copied
	 *     if <code>true</code> or the inner data collection will be the given
	 *     collection itself if <code>false</code>.
	 * @param data are the initial user data
	 */
	public IcosepBinaryTreeNode(boolean useLinkedList, boolean copyDataCollection, List<D> data) {
		super(useLinkedList, copyDataCollection, data);
		this.nicosep = null;
	}

	/** Construct node.
	 * @param useLinkedList indicates if a linked list must be used to store the data.
	 *     If <code>false</code>, an ArrayList will be used.
	 * @param data are the initial user data
	 */
	public IcosepBinaryTreeNode(boolean useLinkedList, D data) {
		super(useLinkedList, data);
		this.nicosep = null;
	}

	@Pure
	@Override
	public Class<? extends Enum<?>> getPartitionEnumeration() {
		return IcosepBinaryTreeZone.class;
	}

	/** Invoked when this object must be deserialized.
	 *
	 * @param in is the input stream.
	 * @throws IOException in case of input stream access error.
	 * @throws ClassNotFoundException if some class was not found.
	 */
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		if (this.nicosep != null) {
			this.nicosep.setParentNodeReference(toN(), false);
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
		super.clear();
		if (this.nicosep != null) {
			final N child = this.nicosep;
			setIcosepChild(null);
			child.clear();
		}
	}

	@Pure
	@Override
	public int getChildCount() {
		return super.getChildCount() + 1;
	}

	@Pure
	@Override
	public N getChildAt(int index) throws IndexOutOfBoundsException {
		if (index == IcosepBinaryTreeZone.ICOSEP.ordinal()) {
			return this.nicosep;
		}
		return super.getChildAt(index);
	}

	/** Replies the node that is corresponding to the given zone.
	 *
	 * @param zone the zone.
	 * @return the child node for the given zone, or <code>null</code>
	 */
	@Pure
	public N getChildAt(IcosepBinaryTreeZone zone) {
		if (zone == IcosepBinaryTreeZone.ICOSEP) {
			return this.nicosep;
		}
		return getChildAt(zone.toBinaryTreeZone());
	}

	@Override
	public boolean setIcosepChild(N newChild) {
		final N oldChild = this.nicosep;
		if (oldChild == newChild) {
			return false;
		}

		if (oldChild != null) {
			oldChild.setParentNodeReference(null, false);
			--this.notNullChildCount;
			firePropertyChildRemoved(IcosepBinaryTreeZone.ICOSEP.ordinal(), oldChild);
		}

		if (newChild != null) {
			final N oldParent = newChild.getParentNode();
			if (oldParent != this) {
				newChild.removeFromParent();
			}
		}

		this.nicosep = newChild;

		if (newChild != null) {
			newChild.setParentNodeReference(toN(), true);
			++this.notNullChildCount;
			firePropertyChildAdded(IcosepBinaryTreeZone.ICOSEP.ordinal(), newChild);
		}

		return true;
	}

	@Pure
	@Override
	public N getIcosepChild() {
		return this.nicosep;
	}

	@Pure
	@Override
	public boolean isLeaf() {
		return super.isLeaf() && this.nicosep == null;
	}

	@Override
	public boolean setChildAt(int index, N newChild) throws IndexOutOfBoundsException {
		if (index == IcosepBinaryTreeZone.ICOSEP.ordinal()) {
			return setIcosepChild(newChild);
		}
		return super.setChildAt(index, newChild);
	}

	@Override
	protected void setChildAtWithoutEventFiring(int index, N newChild) throws IndexOutOfBoundsException {
		if (index == IcosepBinaryTreeZone.ICOSEP.ordinal()) {
			if (this.nicosep != null) {
				--this.notNullChildCount;
			}
			this.nicosep = newChild;
			if (this.nicosep != null) {
				++this.notNullChildCount;
			}
		} else {
			super.setChildAtWithoutEventFiring(index, newChild);
		}
	}

	@Override
	public boolean removeChild(N child) {
		if (child != null) {
			if (child == this.nicosep) {
				return setIcosepChild(null);
			}
			return super.removeChild(child);
		}
		return false;
	}

	@Pure
	@Override
	public int indexOf(N child) {
		if (child == this.nicosep) {
			return IcosepBinaryTreeZone.ICOSEP.ordinal();
		}
		return super.indexOf(child);
	}

	@Override
	public void getChildren(Object[] array) {
		if (array != null) {
			final IcosepBinaryTreeZone[] zones = IcosepBinaryTreeZone.values();
			for (int i = 0; i < zones.length && i < array.length; ++i) {
				array[i] = getChildAt(zones[i]);
			}
		}
	}

	@Pure
	@Override
	public int getMinHeight() {
		return Math.min(
				super.getMinHeight(),
				1 + (this.nicosep != null ? this.nicosep.getMinHeight() : 0));
	}

	@Pure
	@Override
	public int getMaxHeight() {
		return Math.max(
				super.getMaxHeight(),
				1 + (this.nicosep != null ? this.nicosep.getMaxHeight() : 0));
	}

	@Override
	protected void getHeights(int currentHeight, List<Integer> heights) {
		super.getHeights(currentHeight, heights);
		if (this.nicosep != null) {
			this.nicosep.getHeights(currentHeight + 1, heights);
		}
	}

	/**
	 * This is the generic implementation of a
	 * tree for which each node has two children.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	public enum IcosepBinaryTreeZone {
		/** This is the index of the child that correspond to
		 * the voxel at front/left/up position.
		 */
		LEFT,

		/** This is the index of the child that correspond to
		 * the voxel at back/right/down position.
		 */
		RIGHT,

		/** intersection zone.
		 */
		ICOSEP;

		/** Replies the octtree zone that is corresponding to this zone.
		 *
		 * @return a quadtree zone
		 */
		public BinaryTreeZone toBinaryTreeZone() {
			return BinaryTreeZone.values()[ordinal()];
		}

		/** Replies the zone corresponding to the given index.
		 * The index is the same as the ordinal value of the
		 * enumeration. If the given index does not correspond
		 * to an ordinal value, <code>null</code> is replied.
		 *
		 * @param index the index.
		 * @return the zone or <code>null</code>
		 */
		@Pure
		public static IcosepBinaryTreeZone fromInteger(int index) {
			if (index < 0) {
				return null;
			}
			final IcosepBinaryTreeZone[] nodes = values();
			if (index >= nodes.length) {
				return null;
			}
			return nodes[index];
		}

	}

	/**
	 * This is the generic implementation of a quad
	 * tree with icosep heuristic.
	 *
	 * @param <D> is the type of the data inside the tree
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	public static class DefaultIcosepBinaryTreeNode<D> extends IcosepBinaryTreeNode<D, DefaultIcosepBinaryTreeNode<D>> {

		private static final long serialVersionUID = -8047453795982146718L;

		/**
		 * Empty node.
		 */
		public DefaultIcosepBinaryTreeNode() {
			super();
		}

		/** Construct a node.
		 * @param data are the initial user data
		 */
		public DefaultIcosepBinaryTreeNode(Collection<D> data) {
			super(data);
		}

		/** Construct a node.
		 * @param data are the initial user data
		 */
		public DefaultIcosepBinaryTreeNode(D data) {
			super(data);
		}

	}

}
