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
 * tree for which each node has eight children.
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
public abstract class OctTreeNode<D,N extends OctTreeNode<D,N>> extends AbstractTreeNode<D,N> {

	private static final long serialVersionUID = -6184547894654511018L;
	
	private N child1;
	private N child2;
	private N child3;
	private N child4;
	private N child5;
	private N child6;
	private N child7;
	private N child8;

	/**
	 * Empty node.
	 */
	public OctTreeNode() {
		this(DEFAULT_LINK_LIST_USE);
	}
	
	/**
	 * @param data are the initial user data
	 */
	public OctTreeNode(Collection<D> data) {
		super(DEFAULT_LINK_LIST_USE, data);
		this.child1 = null;
		this.child2 = null;
		this.child3 = null;
		this.child4 = null;
		this.child5 = null;
		this.child6 = null;
		this.child7 = null;
		this.child8 = null;
	}
	
	/**
	 * @param data are the initial user data
	 */
	public OctTreeNode(D data) {
		this(DEFAULT_LINK_LIST_USE, data);
	}

	/**
	 * @param useLinkedList indicates if a linked list must be used to store the data.
	 * If <code>false</code>, an ArrayList will be used.
	 */
	public OctTreeNode(boolean useLinkedList) {
		super(useLinkedList);
		this.child1 = null;
		this.child2 = null;
		this.child3 = null;
		this.child4 = null;
		this.child5 = null;
		this.child6 = null;
		this.child7 = null;
		this.child8 = null;
	}
	
	/**
	 * @param useLinkedList indicates if a linked list must be used to store the data.
	 * If <code>false</code>, an ArrayList will be used.
	 * @param copyDataCollection indicates if the given data collection is copied
	 * if <code>true</code> or the inner data collection will be the given
	 * collection itself if <code>false</code>.
	 * @param data are the initial user data
	 */
	public OctTreeNode(boolean useLinkedList, boolean copyDataCollection, List<D> data) {
		super(useLinkedList, copyDataCollection, data);
		this.child1 = null;
		this.child2 = null;
		this.child3 = null;
		this.child4 = null;
		this.child5 = null;
		this.child6 = null;
		this.child7 = null;
		this.child8 = null;
	}

	/**
	 * @param useLinkedList indicates if a linked list must be used to store the data.
	 * If <code>false</code>, an ArrayList will be used.
	 * @param data are the initial user data
	 */
	public OctTreeNode(boolean useLinkedList, D data) {
		super(useLinkedList, data);
		this.child1 = null;
		this.child2 = null;
		this.child3 = null;
		this.child4 = null;
		this.child5 = null;
		this.child6 = null;
		this.child7 = null;
		this.child8 = null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Class<? extends Enum<?>> getPartitionEnumeration() {
		return OctTreeZone.class;
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
		if (this.child1!=null) this.child1.setParentNodeReference(me, false);
		if (this.child2!=null) this.child2.setParentNodeReference(me, false);
		if (this.child3!=null) this.child3.setParentNodeReference(me, false);
		if (this.child4!=null) this.child4.setParentNodeReference(me, false);
		if (this.child5!=null) this.child5.setParentNodeReference(me, false);
		if (this.child6!=null) this.child6.setParentNodeReference(me, false);
		if (this.child7!=null) this.child7.setParentNodeReference(me, false);
		if (this.child8!=null) this.child8.setParentNodeReference(me, false);
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
		if (this.child1!=null) {
			child = this.child1;
			setChildAt(0, null);
			child.clear();
		}
		if (this.child2!=null) {
			child = this.child2;
			setChildAt(1, null);
			child.clear();
		}
		if (this.child3!=null) {
			child = this.child3;
			setChildAt(2, null);
			child.clear();
		}
		if (this.child4!=null) {
			child = this.child4;
			setChildAt(3, null);
			child.clear();
		}
		if (this.child5!=null) {
			child = this.child5;
			setChildAt(4, null);
			child.clear();
		}
		if (this.child6!=null) {
			child = this.child6;
			setChildAt(5, null);
			child.clear();
		}
		if (this.child7!=null) {
			child = this.child7;
			setChildAt(6, null);
			child.clear();
		}
		if (this.child8!=null) {
			child = this.child8;
			setChildAt(7, null);
			child.clear();
		}
		removeAllUserData();
	}

	/** {@inheritDoc}
	 * 
	 * @return always 8
	 */
	@Override
	public int getChildCount() {
		return 8;
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
	public int indexOf(N child) {
		if (child==this.child1) return 0;
		if (child==this.child2) return 1;
		if (child==this.child3) return 2;
		if (child==this.child4) return 3;
		if (child==this.child5) return 4;
		if (child==this.child6) return 5;
		if (child==this.child7) return 6;
		if (child==this.child8) return 7;
		return -1;
	}

	/** Replies the zone of the specified child.
	 *
	 * @param child
	 * @return the zone or <code>null</code>.
	 */
	public final OctTreeZone zoneOf(N child) {
		int idx = indexOf(child);
		OctTreeZone[] zones = OctTreeZone.values();
		if (idx<0 || idx>=zones.length) {
			return zones[idx];
		}
		return null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public N getChildAt(int index) throws IndexOutOfBoundsException {
		switch(index) {
		case 0:
			return this.child1;
		case 1:
			return this.child2;
		case 2:
			return this.child3;
		case 3:
			return this.child4;
		case 4:
			return this.child5;
		case 5:
			return this.child6;
		case 6:
			return this.child7;
		case 7:
			return this.child8;
		default:
		}
		throw new IndexOutOfBoundsException(index+">= 8"); //$NON-NLS-1$
	}

	/** Replies count of children in this node.
	 * 
	 * @param zone is the position of the node to reply
	 * @return the node at the given index
	 */
	public final N getChildAt(OctTreeZone zone) {
		if (zone!=null)
			return getChildAt(zone.ordinal());
		return null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean moveTo(N newParent, int index) {
		return moveTo(newParent, index, false);
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean setChildAt(int index, N newChild) throws IndexOutOfBoundsException {
		switch(index) {
		case 0:
			return setChild1(newChild);
		case 1:
			return setChild2(newChild);
		case 2:
			return setChild3(newChild);
		case 3:
			return setChild4(newChild);
		case 4:
			return setChild5(newChild);
		case 5:
			return setChild6(newChild);
		case 6:
			return setChild7(newChild);
		case 7:
			return setChild8(newChild);
		default:
		}
		throw new IndexOutOfBoundsException(index+">= 8"); //$NON-NLS-1$
	}

	/** {@inheritDoc}
	 */
	@Override
	protected void setChildAtWithoutEventFiring(int index, N newChild) throws IndexOutOfBoundsException {
		switch(index) {
		case 0:
			if (this.child1!=null) --this.notNullChildCount;
			this.child1 = newChild;
			if (this.child1!=null) ++this.notNullChildCount;
			break;
		case 1:
			if (this.child2!=null) --this.notNullChildCount;
			this.child2 = newChild;
			if (this.child2!=null) ++this.notNullChildCount;
			break;
		case 2:
			if (this.child3!=null) --this.notNullChildCount;
			this.child3 = newChild;
			if (this.child3!=null) ++this.notNullChildCount;
			break;
		case 3:
			if (this.child4!=null) --this.notNullChildCount;
			this.child4 = newChild;
			if (this.child4!=null) ++this.notNullChildCount;
			break;
		case 4:
			if (this.child5!=null) --this.notNullChildCount;
			this.child5 = newChild;
			if (this.child5!=null) ++this.notNullChildCount;
			break;
		case 5:
			if (this.child6!=null) --this.notNullChildCount;
			this.child6 = newChild;
			if (this.child6!=null) ++this.notNullChildCount;
			break;
		case 6:
			if (this.child7!=null) --this.notNullChildCount;
			this.child7 = newChild;
			if (this.child7!=null) ++this.notNullChildCount;
			break;
		case 7:
			if (this.child8!=null) --this.notNullChildCount;
			this.child8 = newChild;
			if (this.child8!=null) ++this.notNullChildCount;
			break;
		default:
			throw new IndexOutOfBoundsException();
		}
	}

	/** Move this node in the given new node.
	 * <p>
	 * This function is preferred to a sequence of calls
	 * to {@link #removeFromParent()} and {@link #setChildAt(int, OctTreeNode)}
	 * because it fires a limited set of events dedicated to the move
	 * the node.
	 * 
	 * @param newParent is the new parent for this node.
	 * @param zone is the position of this node in the new parent.
	 * @return <code>true</code> on success, otherwise <code>false</code>.
	 * @since 4.0
	 */
	public boolean moveTo(N newParent, OctTreeZone zone) {
		if (zone!=null) {
			return moveTo(newParent, zone.ordinal());
		}
		return false;
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean removeChild(N child) {
		if (child!=null) {
			if (child==this.child1) {
				return setChild1(null);
			}
			else if (child==this.child2) {
				return setChild2(null);
			}
			else if (child==this.child3) {
				return setChild3(null);
			}
			else if (child==this.child4) {
				return setChild4(null);
			}
			else if (child==this.child5) {
				return setChild5(null);
			}
			else if (child==this.child6) {
				return setChild6(null);
			}
			else if (child==this.child7) {
				return setChild7(null);
			}
			else if (child==this.child8) {
				return setChild8(null);
			}
		}
		return false;
	}

	/** Set the child 1.
	 * 
	 * @param newChild is the new child
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	private boolean setChild1(N newChild) {
		if (this.child1==newChild) return false;

		if (this.child1!=null) {
			this.child1.setParentNodeReference(null, true);
			--this.notNullChildCount;
			firePropertyChildRemoved(0, this.child1);
		}

		if (newChild!=null) {
			N oldParent = newChild.getParentNode();
			if (oldParent!=this) {
				newChild.removeFromParent();
			}
		}

		this.child1 = newChild;
		
		if (newChild!=null) {
			newChild.setParentNodeReference(toN(), true);
			++this.notNullChildCount;
			firePropertyChildAdded(0, newChild);
		}
		
		return true;
		
	}

	/** Set the child 2.
	 * 
	 * @param newChild is the new child
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	private boolean setChild2(N newChild) {
		if (this.child2==newChild) return false;

		if (this.child2!=null) {
			this.child2.setParentNodeReference(null, true);
			--this.notNullChildCount;
			firePropertyChildRemoved(1, this.child2);
		}

		if (newChild!=null) {
			N oldParent = newChild.getParentNode();
			if (oldParent!=this) {
				newChild.removeFromParent();
			}
		}

		this.child2 = newChild;
		
		if (newChild!=null) {
			newChild.setParentNodeReference(toN(), true);
			++this.notNullChildCount;
			firePropertyChildAdded(1, newChild);
		}
		
		return true;
		
	}

	/** Set the child 3.
	 * 
	 * @param newChild is the new child
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	private boolean setChild3(N newChild) {
		if (this.child3==newChild) return false;

		if (this.child3!=null) {
			this.child3.setParentNodeReference(null, true);
			--this.notNullChildCount;
			firePropertyChildRemoved(2, this.child3);
		}

		if (newChild!=null) {
			N oldParent = newChild.getParentNode();
			if (oldParent!=this) {
				newChild.removeFromParent();
			}
		}

		this.child3 = newChild;
		
		if (newChild!=null) {
			newChild.setParentNodeReference(toN(), true);
			++this.notNullChildCount;
			firePropertyChildAdded(2, newChild);
		}
		
		return true;
		
	}

	/** Set the child 4.
	 * 
	 * @param newChild is the new child
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	private boolean setChild4(N newChild) {
		if (this.child4==newChild) return false;

		if (this.child4!=null) {
			this.child4.setParentNodeReference(null, true);
			--this.notNullChildCount;
			firePropertyChildRemoved(3, this.child4);
		}

		if (newChild!=null) {
			N oldParent = newChild.getParentNode();
			if (oldParent!=this) {
				newChild.removeFromParent();
			}
		}

		this.child4 = newChild;
		
		if (newChild!=null) {
			newChild.setParentNodeReference(toN(), true);
			++this.notNullChildCount;
			firePropertyChildAdded(3, newChild);
		}
		
		return true;
		
	}

	/** Set the child 5.
	 * 
	 * @param newChild is the new child
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	private boolean setChild5(N newChild) {
		if (this.child5==newChild) return false;

		if (this.child5!=null) {
			this.child5.setParentNodeReference(null, true);
			--this.notNullChildCount;
			firePropertyChildRemoved(4, this.child5);
		}

		if (newChild!=null) {
			N oldParent = newChild.getParentNode();
			if (oldParent!=this) {
				newChild.removeFromParent();
			}
		}

		this.child5 = newChild;
		
		if (newChild!=null) {
			newChild.setParentNodeReference(toN(), true);
			++this.notNullChildCount;
			firePropertyChildAdded(4, newChild);
		}
		
		return true;
		
	}

	/** Set the child 6.
	 * 
	 * @param newChild is the new child
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	private boolean setChild6(N newChild) {
		if (this.child6==newChild) return false;

		if (this.child6!=null) {
			this.child6.setParentNodeReference(null, true);
			--this.notNullChildCount;
			firePropertyChildRemoved(5, this.child5);
		}

		if (newChild!=null) {
			N oldParent = newChild.getParentNode();
			if (oldParent!=this) {
				newChild.removeFromParent();
			}
		}

		this.child6 = newChild;
		
		if (newChild!=null) {
			newChild.setParentNodeReference(toN(), true);
			++this.notNullChildCount;
			firePropertyChildAdded(5, newChild);
		}
		
		return true;
		
	}

	/** Set the child 7.
	 * 
	 * @param newChild is the new child
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	private boolean setChild7(N newChild) {
		if (this.child7==newChild) return false;

		if (this.child7!=null) {
			this.child7.setParentNodeReference(null, true);
			--this.notNullChildCount;
			firePropertyChildRemoved(6, this.child7);
		}

		if (newChild!=null) {
			N oldParent = newChild.getParentNode();
			if (oldParent!=this) {
				newChild.removeFromParent();
			}
		}

		this.child7 = newChild;
		
		if (newChild!=null) {
			newChild.setParentNodeReference(toN(), true);
			++this.notNullChildCount;
			firePropertyChildAdded(6, newChild);
		}
		
		return true;
		
	}

	/** Set the child 8.
	 * 
	 * @param newChild is the new child
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	private boolean setChild8(N newChild) {
		if (this.child8==newChild) return false;

		if (this.child8!=null) {
			this.child8.setParentNodeReference(null, true);
			--this.notNullChildCount;
			firePropertyChildRemoved(7, this.child8);
		}

		if (newChild!=null) {
			N oldParent = newChild.getParentNode();
			if (oldParent!=this) {
				newChild.removeFromParent();
			}
		}

		this.child8 = newChild;
		
		if (newChild!=null) {
			newChild.setParentNodeReference(toN(), true);
			++this.notNullChildCount;
			firePropertyChildAdded(7, newChild);
		}
		
		return true;
		
	}

	/** Set the child at the specified zone.
	 * 
	 * @param zone is the zone where to put the child
	 * @param newChild is the new child
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	public final boolean setChildAt(OctTreeZone zone, N newChild) {
		return setChildAt(zone.ordinal(), newChild);
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isLeaf() {
		return this.child1==null && this.child2==null && this.child3==null && this.child4==null
		 && this.child5==null  && this.child6==null  && this.child7==null  && this.child8==null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void getChildren(Object[] array) {
		if (array!=null) {
			if (array.length>0) {
				array[0] = this.child1;
			}
			if (array.length>1) {
				array[1] = this.child2;
			}
			if (array.length>2) {
				array[2] = this.child3;
			}
			if (array.length>3) {
				array[3] = this.child4;
			}
			if (array.length>4) {
				array[4] = this.child5;
			}
			if (array.length>5) {
				array[5] = this.child6;
			}
			if (array.length>6) {
				array[6] = this.child7;
			}
			if (array.length>7) {
				array[7] = this.child8;
			}
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public int getMinHeight() {
		return 1+minInteger(
				this.child1!=null ? this.child1.getMinHeight() : 0,
				this.child2!=null ? this.child2.getMinHeight() : 0,
				this.child3!=null ? this.child3.getMinHeight() : 0,
				this.child4!=null ? this.child4.getMinHeight() : 0,
				this.child5!=null ? this.child5.getMinHeight() : 0,
				this.child6!=null ? this.child6.getMinHeight() : 0,
				this.child7!=null ? this.child7.getMinHeight() : 0,
				this.child8!=null ? this.child8.getMinHeight() : 0);
	}

	/** {@inheritDoc}
	 */
	@Override
	public int getMaxHeight() {
		return 1+maxInteger(
				this.child1!=null ? this.child1.getMaxHeight() : 0,
				this.child2!=null ? this.child2.getMaxHeight() : 0,
				this.child3!=null ? this.child3.getMaxHeight() : 0,
				this.child4!=null ? this.child4.getMaxHeight() : 0,
				this.child5!=null ? this.child5.getMaxHeight() : 0,
				this.child6!=null ? this.child6.getMaxHeight() : 0,
				this.child7!=null ? this.child7.getMaxHeight() : 0,
				this.child8!=null ? this.child8.getMaxHeight() : 0);
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
			if (this.child1!=null) this.child1.getHeights(currentHeight+1, heights);
			if (this.child2!=null) this.child2.getHeights(currentHeight+1, heights);
			if (this.child3!=null) this.child3.getHeights(currentHeight+1, heights);
			if (this.child4!=null) this.child4.getHeights(currentHeight+1, heights);
			if (this.child5!=null) this.child5.getHeights(currentHeight+1, heights);
			if (this.child6!=null) this.child6.getHeights(currentHeight+1, heights);
			if (this.child7!=null) this.child7.getHeights(currentHeight+1, heights);
			if (this.child8!=null) this.child8.getHeights(currentHeight+1, heights);
		}
	}

	/**
	 * This is the generic implementation of a
	 * tree for which each node has eight children.
	 * 
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static enum OctTreeZone {
		/** This is the index of the child that correspond to
		 * the voxel at north-west and front position.
		 */
		NORTH_WEST_FRONT,
		
		/** This is the index of the child that correspond to
		 * the voxel at north-west and back position.
		 */
		NORTH_WEST_BACK,

		/** This is the index of the child that correspond to
		 * the voxel at north-east and front position.
		 */
		NORTH_EAST_FRONT,
		
		/** This is the index of the child that correspond to
		 * the voxel at north-east and back position.
		 */
		NORTH_EAST_BACK,

		/** This is the index of the child that correspond to
		 * the voxel at south-west and front position.
		 */
		SOUTH_WEST_FRONT,
		
		/** This is the index of the child that correspond to
		 * the voxel at south-west and back position.
		 */
		SOUTH_WEST_BACK,

		/** This is the index of the child that correspond to
		 * the voxel at south-east and front position.
		 */
		SOUTH_EAST_FRONT,
		
		/** This is the index of the child that correspond to
		 * the voxel at south-east and back position.
		 */
		SOUTH_EAST_BACK;
		
		/** Replies the zone corresponding to the given index.
		 * The index is the same as the ordinal value of the
		 * enumeration. If the given index does not correspond
		 * to an ordinal value, <code>null</code> is replied.
		 * 
		 * @param index
		 * @return the zone or <code>null</code>
		 */
		public static OctTreeZone fromInteger(int index) {
			if (index<0) return null;
			OctTreeZone[] nodes = values();
			if (index>=nodes.length) return null;
			return nodes[index];
		}
		
	}

	/**
	 * This is the generic implementation of a oct
	 * tree.
	 * 
	 * @param <D> is the type of the data inside the tree
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static class DefaultOctTreeNode<D> extends OctTreeNode<D,DefaultOctTreeNode<D>> {

		private static final long serialVersionUID = 3732643480212763103L;

		/**
		 * Empty node.
		 */
		public DefaultOctTreeNode() {
			super();
		}
		
		/**
		 * @param data are the initial user data
		 */
		public DefaultOctTreeNode(Collection<D> data) {
			super(data);
		}
		
		/**
		 * @param data are the initial user data
		 */
		public DefaultOctTreeNode(D data) {
			super(data);
		}

	} /* class DefaultOctTreeNode */

}