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

import org.arakhne.afc.math.tree.TreeNode;


/**
 * This is the generic implementation of a
 * tree for which each node has four children.
 * <p>
 * <h3>moveTo</h3>
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
 */
public abstract class QuadTreeNode<D,N extends QuadTreeNode<D,N>> extends AbstractTreeNode<D,N> {	
	
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
	
	/**
	 * @param data are initial user data
	 */
	public QuadTreeNode(Collection<D> data) {
		super(DEFAULT_LINK_LIST_USE, data);
		this.nNorthWest = null;
		this.nNorthEast = null;
		this.nSouthWest = null;
		this.nSouthEast = null;
	}
	
	/**
	 * @param data are initial user data
	 */
	public QuadTreeNode(D data) {
		this(DEFAULT_LINK_LIST_USE, data);
	}

	/**
	 * @param useLinkedList indicates if a linked list must be used to store the data.
	 * If <code>false</code>, an ArrayList will be used.
	 */
	public QuadTreeNode(boolean useLinkedList) {
		super(useLinkedList);
		this.nNorthWest = null;
		this.nNorthEast = null;
		this.nSouthWest = null;
		this.nSouthEast = null;
	}
	
	/**
	 * @param useLinkedList indicates if a linked list must be used to store the data.
	 * If <code>false</code>, an ArrayList will be used.
	 * @param copyDataCollection indicates if the given data collection is copied
	 * if <code>true</code> or the inner data collection will be the given
	 * collection itself if <code>false</code>.
	 * @param data are initial user data
	 */
	public QuadTreeNode(boolean useLinkedList, boolean copyDataCollection, List<D> data) {
		super(useLinkedList, copyDataCollection, data);
		this.nNorthWest = null;
		this.nNorthEast = null;
		this.nSouthWest = null;
		this.nSouthEast = null;
	}

	/**
	 * @param useLinkedList indicates if a linked list must be used to store the data.
	 * If <code>false</code>, an ArrayList will be used.
	 * @param data are initial user data
	 */
	public QuadTreeNode(boolean useLinkedList, D data) {
		super(useLinkedList, data);
		this.nNorthWest = null;
		this.nNorthEast = null;
		this.nSouthWest = null;
		this.nSouthEast = null;
	}

	/** {@inheritDoc}
	 */
	@Override
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
		N me = toN();
		if (this.nNorthEast!=null) this.nNorthEast.setParentNodeReference(me, false);
		if (this.nNorthWest!=null) this.nNorthWest.setParentNodeReference(me, false);
		if (this.nSouthEast!=null) this.nSouthEast.setParentNodeReference(me, false);
		if (this.nSouthWest!=null) this.nSouthWest.setParentNodeReference(me, false);
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
		N child;
		if (this.nNorthWest!=null) {
			child = this.nNorthWest;
			setFirstChild(null);
			child.clear();
		}
		if (this.nNorthEast!=null) {
			child = this.nNorthEast;
			setSecondChild(null);
			child.clear();
		}
		if (this.nSouthWest!=null) {
			child = this.nSouthWest;
			setThirdChild(null);
			child.clear();
		}
		if (this.nSouthEast!=null) {
			child = this.nSouthEast;
			setFourthChild(null);
			child.clear();
		}
		removeAllUserData();
	}

	/** Replies count of children in this node.
	 * 
	 * @return always 4
	 */
	@Override
	public int getChildCount() {
		return 4;
	}

	/** {@inheritDoc}
	 */
	@Override
	public int getNotNullChildCount() {
		return this.notNullChildCount;
	}

	/** {@inheritDoc}
	 */
	@Override
	public N getChildAt(int index) throws IndexOutOfBoundsException {
		QuadTreeZone[] zones = QuadTreeZone.values();
		if (index>=0 && index<zones.length)
			return getChildAt(zones[index]);
		throw new IndexOutOfBoundsException(index+" >= 4"); //$NON-NLS-1$
	}

	/** Replies the node that is corresponding to the given zone
	 * 
	 * @param zone
	 * @return the child node for the given zone, or <code>null</code> 
	 */
	public N getChildAt(QuadTreeZone zone) {
		switch(zone) {
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
		N oldChild = this.nNorthWest;
		if (oldChild==newChild) return false;
		
		if (oldChild!=null) {
			oldChild.setParentNodeReference(null, true);
			--this.notNullChildCount;
			firePropertyChildRemoved(0, oldChild);
		}

		if (newChild!=null) {
			N oldParent = newChild.getParentNode();
			if (oldParent!=this) {
				newChild.removeFromParent();
			}
		}

		this.nNorthWest = newChild;
		
		if (newChild!=null) {
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
	public final N getFirstChild() {
		return this.nNorthWest;
	}

	/** Set the second child of this node.
	 * 
	 * @param newChild is the new child for the second zone
	 * @return <code>true</code> on success, otherwhise <code>false</code>
	 */
	public boolean setSecondChild(N newChild) {
		N oldChild = this.nNorthEast;
		if (oldChild==newChild) return false;
		
		if (oldChild!=null) {
			oldChild.setParentNodeReference(null, true);
			--this.notNullChildCount;
			firePropertyChildRemoved(1, oldChild);
		}

		if (newChild!=null) {
			N oldParent = newChild.getParentNode();
			if (oldParent!=this) {
				newChild.removeFromParent();
			}
		}

		this.nNorthEast = newChild;
		
		if (newChild!=null) {
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
	public final N getSecondChild() {
		return this.nNorthEast;
	}

	/** Set the third child of this node.
	 * 
	 * @param newChild is the new child for the third zone
	 * @return <code>true</code> on success, otherwhise <code>false</code>
	 */
	public boolean setThirdChild(N newChild) {
		N oldChild = this.nSouthWest;
		if (oldChild==newChild) return false;
		
		if (oldChild!=null) {
			oldChild.setParentNodeReference(null, true);
			--this.notNullChildCount;
			firePropertyChildRemoved(2, oldChild);
		}

		if (newChild!=null) {
			N oldParent = newChild.getParentNode();
			if (oldParent!=this) {
				newChild.removeFromParent();
			}
		}

		this.nSouthWest = newChild;
		
		if (newChild!=null) {
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
	public final N getThirdChild() {
		return this.nSouthWest;
	}

	/** Set the Fourth child of this node.
	 * 
	 * @param newChild is the new child for the fourth zone
	 * @return <code>true</code> on success, otherwhise <code>false</code>
	 */
	public boolean setFourthChild(N newChild) {
		N oldChild = this.nSouthEast;
		if (oldChild==newChild) return false;
		
		if (oldChild!=null) {
			oldChild.setParentNodeReference(null, true);
			--this.notNullChildCount;
			firePropertyChildRemoved(3, oldChild);
		}

		if (newChild!=null) {
			N oldParent = newChild.getParentNode();
			if (oldParent!=this) {
				newChild.removeFromParent();
			}
		}

		this.nSouthEast = newChild;
		
		if (newChild!=null) {
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
	public final N getFourthChild() {
		return this.nSouthEast;
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isLeaf() {
		return this.nNorthEast==null && this.nNorthWest==null && this.nSouthEast==null && this.nSouthWest==null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean moveTo(N newParent, int index) {
		return moveTo(newParent, index, false);
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean setChildAt(int index, N newChild) throws IndexOutOfBoundsException {
		QuadTreeZone[] zones = QuadTreeZone.values();
		if (index>=0 && index<zones.length)
			return setChildAt(zones[index], newChild);
		throw new IndexOutOfBoundsException(index+" >= 4"); //$NON-NLS-1$
	}

	/** {@inheritDoc}
	 */
	@Override
	protected void setChildAtWithoutEventFiring(int index, N newChild) throws IndexOutOfBoundsException {
		switch(QuadTreeZone.values()[index]) {
		case NORTH_EAST:
			if (this.nNorthEast!=null) --this.notNullChildCount;
			this.nNorthEast = newChild;
			if (this.nNorthEast!=null) ++this.notNullChildCount;
			break;
		case NORTH_WEST:
			if (this.nNorthWest!=null) --this.notNullChildCount;
			this.nNorthWest = newChild;
			if (this.nNorthWest!=null) ++this.notNullChildCount;
			break;
		case SOUTH_EAST:
			if (this.nSouthEast!=null) --this.notNullChildCount;
			this.nSouthEast = newChild;
			if (this.nSouthEast!=null) ++this.notNullChildCount;
			break;
		case SOUTH_WEST:
			if (this.nSouthWest!=null) --this.notNullChildCount;
			this.nSouthWest = newChild;
			if (this.nSouthWest!=null) ++this.notNullChildCount;
			break;
		default:
			throw new IndexOutOfBoundsException();
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean removeChild(N child) {
		if (child!=null) {
			if (child==this.nNorthWest) {
				return setFirstChild(null);
			}
			else if (child==this.nNorthEast) {
				return setSecondChild(null);
			}
			else if (child==this.nSouthWest) {
				return setThirdChild(null);
			}
			else if (child==this.nSouthEast) {
				return setFourthChild(null);
			}
		}
		return false;
	}

	/** Move this node in the given new node.
	 * <p>
	 * This function is preferred to a sequence of calls
	 * to {@link #removeFromParent()} and {@link #setChildAt(int, QuadTreeNode)}
	 * because it fires a limited set of events dedicated to the move
	 * the node.
	 * 
	 * @param newParent is the new parent for this node.
	 * @param zone is the position of this node in the new parent.
	 * @return <code>true</code> on success, otherwise <code>false</code>.
	 * @since 4.0
	 */
	public boolean moveTo(N newParent, QuadTreeZone zone) {
		return moveTo(newParent, zone.ordinal());
	}

	/** Set the child at the specified zone.
	 * 
	 * @param zone is the zone to set
	 * @param newChild is the new node for the given zone
	 * @return <code>true</code> on success, <code>false</code> otherwise
	 */
	public boolean setChildAt(QuadTreeZone zone, N newChild) {
		switch(zone) {
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

	/** {@inheritDoc}
	 */
	@Override
	public int indexOf(N child) {
		QuadTreeZone zone = zoneOf(child);
		if (zone==null) return -1;
		return zone.ordinal();
	}

	/** Replies the zone of the specified child.
	 *
	 * @param child
	 * @return the index or <code>null</code>.
	 */
	public QuadTreeZone zoneOf(N child) {
		if (child==this.nNorthWest) return QuadTreeZone.NORTH_WEST;
		if (child==this.nNorthEast) return QuadTreeZone.NORTH_EAST;
		if (child==this.nSouthWest) return QuadTreeZone.SOUTH_WEST;
		if (child==this.nSouthEast) return QuadTreeZone.SOUTH_EAST;
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void getChildren(Object[] array) {
		if (array!=null) {
			QuadTreeZone[] zones = QuadTreeZone.values();
			for(int i=0; i<zones.length && i<array.length; ++i) {
				array[i] = getChildAt(zones[i]);
			}
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public int getMinHeight() {
		return 1+minInteger(
				this.nNorthWest!=null ? this.nNorthWest.getMinHeight() : 0,
				this.nNorthEast!=null ? this.nNorthEast.getMinHeight() : 0,
				this.nSouthWest!=null ? this.nSouthWest.getMinHeight() : 0,
				this.nSouthEast!=null ? this.nSouthEast.getMinHeight() : 0);
	}

	/** {@inheritDoc}
	 */
	@Override
	public int getMaxHeight() {
		return 1+maxInteger(
				this.nNorthWest!=null ? this.nNorthWest.getMaxHeight() : 0,
				this.nNorthEast!=null ? this.nNorthEast.getMaxHeight() : 0,
				this.nSouthWest!=null ? this.nSouthWest.getMaxHeight() : 0,
				this.nSouthEast!=null ? this.nSouthEast.getMaxHeight() : 0);
	}

	/** Replies the heights of all the leaf nodes.
	 * The order of the heights is given by a depth-first iteration.
	 * 
	 * @param currentHeight is the current height of this node.
	 * @param heights is the list of heights to fill
	 */
	@Override
	protected void getHeights(int currentHeight, List<Integer> heights) {
		if (isLeaf()) {
			heights.add(new Integer(currentHeight));
		}
		else {
			if (this.nNorthWest!=null) this.nNorthWest.getHeights(currentHeight+1, heights);
			if (this.nNorthEast!=null) this.nNorthEast.getHeights(currentHeight+1, heights);
			if (this.nSouthWest!=null) this.nSouthWest.getHeights(currentHeight+1, heights);
			if (this.nSouthEast!=null) this.nSouthEast.getHeights(currentHeight+1, heights);
		}
	}

	/**
	 * Available zones of a quad tree.
	 * 
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static enum QuadTreeZone {
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
		 * @param index
		 * @return the zone or <code>null</code>
		 */
		public static QuadTreeZone fromInteger(int index) {
			if (index<0) return null;
			QuadTreeZone[] nodes = values();
			if (index>=nodes.length) return null;
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
	 */
	public static class DefaultQuadTreeNode<D> extends QuadTreeNode<D,DefaultQuadTreeNode<D>> {

		private static final long serialVersionUID = -3283371007433469124L;

		/**
		 */
		public DefaultQuadTreeNode() {
			super();
		}

		/**
		 * @param data are the initial user data
		 */
		public DefaultQuadTreeNode(Collection<D> data) {
			super(data);
		}
		
		/**
		 * @param data are the initial user data
		 */
		public DefaultQuadTreeNode(D data) {
			super(data);
		}

	} /* class DefaultQuadTreeNode */
	
}