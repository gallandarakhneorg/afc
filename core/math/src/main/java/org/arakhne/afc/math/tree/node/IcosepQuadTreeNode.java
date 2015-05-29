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
package org.arakhne.afc.math.tree.node;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Collection;
import java.util.List;

import org.arakhne.afc.math.tree.IcosepTreeNodeContainer;


/**
 * This is the generic implementation of a
 * tree for which each node has four children and
 * that implements the Icosep heuristic.
 * <p>
 * The icosep heuristic implies that all objects
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
 */
public abstract class IcosepQuadTreeNode<D,N extends IcosepQuadTreeNode<D,N>>
extends QuadTreeNode<D,N> 
implements IcosepTreeNodeContainer<N> {

	private static final long serialVersionUID = -5009670937278314109L;
	
	private N nIcosep;

	/** 
	 * Empty node.
	 */
	public IcosepQuadTreeNode() {
		super();
		this.nIcosep = null;
	}
	
	/**
	 * @param data are the initial user data
	 */
	public IcosepQuadTreeNode(Collection<D> data) {
		super(data);
		this.nIcosep = null;
	}
	
	/**
	 * @param data are the initial user data
	 */
	public IcosepQuadTreeNode(D data) {
		super(data);
		this.nIcosep = null;
	}

	/**
	 * @param useLinkedList indicates if a linked list must be used to store the data.
	 * If <code>false</code>, an ArrayList will be used.
	 */
	public IcosepQuadTreeNode(boolean useLinkedList) {
		super(useLinkedList);
		this.nIcosep = null;
	}

	/**
	 * @param useLinkedList indicates if a linked list must be used to store the data.
	 * If <code>false</code>, an ArrayList will be used.
	 * @param copyDataCollection indicates if the given data collection is copied
	 * if <code>true</code> or the inner data collection will be the given
	 * collection itself if <code>false</code>.
	 * @param data are the initial user data
	 */
	public IcosepQuadTreeNode(boolean useLinkedList, boolean copyDataCollection, List<D> data) {
		super(useLinkedList, copyDataCollection, data);
		this.nIcosep = null;
	}

	/**
	 * @param useLinkedList indicates if a linked list must be used to store the data.
	 * If <code>false</code>, an ArrayList will be used.
	 * @param data are the initial user data
	 */
	public IcosepQuadTreeNode(boolean useLinkedList, D data) {
		super(useLinkedList, data);
		this.nIcosep = null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Class<? extends Enum<?>> getPartitionEnumeration() {
		return IcosepQuadTreeZone.class;
	}

	/** Invoked when this object must be deserialized.
	 * 
	 * @param in is the input stream.
	 * @throws IOException in case of input stream access error.
	 * @throws ClassNotFoundException if some class was not found.
	 */
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		if (this.nIcosep!=null) this.nIcosep.setParentNodeReference(toN(), false);
	}
	
	/** Clear the tree.
	 * <p>
	 * Caution: this method also destroyes the
	 * links between the child nodes inside the tree.
	 * If you want to unlink the first-level
	 * child node with
	 * this node but leave the rest of the tree
	 * unchanged, please call <code>setChildAt(i,null)</code>.
	 */
	@Override
	public void clear() {
		super.clear();
		if (this.nIcosep!=null) {
			N child = this.nIcosep;
			setIcosepChild(null);
			child.clear();
		}
	}

	/** Replies count of children in this node.
	 * 
	 * @return always 5
	 */
	@Override
	public int getChildCount() {
		return super.getChildCount()+1;
	}

	/** Replies count of children in this node.
	 */
	@Override
	public N getChildAt(int index) throws IndexOutOfBoundsException {
		if (index==IcosepQuadTreeZone.ICOSEP.ordinal())
			return this.nIcosep;
		return super.getChildAt(index);
	}

	/** Replies the node that is corresponding to the given zone
	 * 
	 * @param zone
	 * @return the child node for the given zone, or <code>null</code> 
	 */
	public N getChildAt(IcosepQuadTreeZone zone) {
		if (zone==IcosepQuadTreeZone.ICOSEP) {
			return this.nIcosep;
		}
		return getChildAt(zone.toQuadTreeZone());
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean setIcosepChild(N newChild) {
		N oldChild = this.nIcosep;
		if (oldChild==newChild) return false;
		
		if (oldChild!=null) {
			oldChild.setParentNodeReference(null, true);
			--this.notNullChildCount;
			firePropertyChildRemoved(IcosepQuadTreeZone.ICOSEP.ordinal(), oldChild);
		}

		if (newChild!=null) {
			N oldParent = newChild.getParentNode();
			if (oldParent!=this) {
				newChild.removeFromParent();
			}
		}

		this.nIcosep = newChild;
		
		if (newChild!=null) {
			newChild.setParentNodeReference(toN(), true);
			++this.notNullChildCount;
			firePropertyChildAdded(IcosepQuadTreeZone.ICOSEP.ordinal(), newChild);
		}
		
		return true;
	}

	/** {@inheritDoc}
	 */
	@Override
	public N getIcosepChild() {
		return this.nIcosep;
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isLeaf() {
		return super.isLeaf() && this.nIcosep==null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean setChildAt(int index, N newChild) throws IndexOutOfBoundsException {
		if (index==IcosepQuadTreeZone.ICOSEP.ordinal())
			return setIcosepChild(newChild);
		return super.setChildAt(index, newChild);
	}

	/** {@inheritDoc}
	 */
	@Override
	protected void setChildAtWithoutEventFiring(int index, N newChild) throws IndexOutOfBoundsException {
		if (index==IcosepQuadTreeZone.ICOSEP.ordinal()) {
			if (this.nIcosep!=null) --this.notNullChildCount;
			this.nIcosep = newChild;
			if (this.nIcosep!=null) ++this.notNullChildCount;
		}
		else {
			super.setChildAtWithoutEventFiring(index, newChild);
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean removeChild(N child) {
		if (child!=null) {
			if (child==this.nIcosep) {
				return setIcosepChild(null);
			}
			return super.removeChild(child);
		}
		return false;
	}

	/** Replies the index of the specified child.
	 * 
	 * @return the index or <code>-1</code>.
	 */
	@Override
	public int indexOf(N child) {
		if (child==this.nIcosep) return IcosepQuadTreeZone.ICOSEP.ordinal();
		return super.indexOf(child);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void getChildren(Object[] array) {
		if (array!=null) {
			IcosepQuadTreeZone[] zones = IcosepQuadTreeZone.values();
			for(int i=0; i<zones.length && i<array.length; ++i) {
				array[i] = getChildAt(zones[i]);
			}
		}
	}

	/** Replies the minimal height of the tree.
	 * 
	 * @return the height of the uppest leaf in the tree.
	 */
	@Override
	public int getMinHeight() {
		return Math.min(
				super.getMinHeight(),
				1+(this.nIcosep!=null ? this.nIcosep.getMinHeight() : 0));
	}

	/** Replies the maximal height of the tree.
	 * 
	 * @return the height of the lowest leaf in the tree.
	 */
	@Override
	public int getMaxHeight() {
		return Math.max(
				super.getMaxHeight(),
				1+(this.nIcosep!=null ? this.nIcosep.getMaxHeight() : 0));
	}

	/** Replies the heights of all the leaf nodes.
	 * The order of the heights is given by a depth-first iteration.
	 * 
	 * @param currentHeight is the current height of this node.
	 * @param heights is the list of heights to fill
	 */
	@Override
	protected void getHeights(int currentHeight, List<Integer> heights) {
		super.getHeights(currentHeight, heights);
		if (this.nIcosep!=null) this.nIcosep.getHeights(currentHeight+1, heights);
	}

	/**
	 * Available zones of a quad tree.
	 * 
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static enum IcosepQuadTreeZone {
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
		SOUTH_EAST,
		/** intersection zone
		 */
		ICOSEP;
		
		/** Replies the quadtree zone that is corresponding to this zone.
		 * 
		 * @return a quadtree zone
		 */
		public QuadTreeZone toQuadTreeZone() {
			return QuadTreeZone.values()[ordinal()];
		}
		
		/** Replies the zone corresponding to the given index.
		 * The index is the same as the ordinal value of the
		 * enumeration. If the given index does not correspond
		 * to an ordinal value, <code>null</code> is replied.
		 * 
		 * @param index
		 * @return the zone or <code>null</code>
		 */
		public static IcosepQuadTreeZone fromInteger(int index) {
			if (index<0) return null;
			IcosepQuadTreeZone[] nodes = values();
			if (index>=nodes.length) return null;
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
	 */
	public static class DefaultIcosepQuadTreeNode<D> extends IcosepQuadTreeNode<D,DefaultIcosepQuadTreeNode<D>> {

		private static final long serialVersionUID = -8047453795982146718L;

		/**
		 * Empty node.
		 */
		public DefaultIcosepQuadTreeNode() {
			super();
		}
		
		/**
		 * @param data are the initial user data
		 */
		public DefaultIcosepQuadTreeNode(Collection<D> data) {
			super(data);
		}
		
		/**
		 * @param data are the initial user data
		 */
		public DefaultIcosepQuadTreeNode(D data) {
			super(data);
		}

	} /* class DefaultIcosepQuadTreeNode */

}