/* 
 * $Id$
 * 
 * Copyright (c) 2006-09, Multiagent Team - Systems and Transportation Laboratory (SeT)
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Systems and Transportation Laboratory ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 *
 * http://www.multiagent.fr/
 */
package fr.utbm.set.jasim.environment.model.perception.tree.structures.bsptree;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import fr.utbm.set.geom.bounds.CombinableBounds;
import fr.utbm.set.jasim.environment.model.perception.tree.DynamicPerceptionTreeNode;
import fr.utbm.set.jasim.environment.model.perception.tree.PerceptionNodeListener;
import fr.utbm.set.jasim.environment.model.perception.tree.PerceptionTreeNode;
import fr.utbm.set.jasim.environment.model.world.TreeDataMobileEntity;
import fr.utbm.set.jasim.environment.model.world.WorldEntity;
import fr.utbm.set.tree.node.IcosepBinaryTreeNode;

/**
 * This is the generic implementation of a
 * tree for which each node has two children plus
 * one icosep child.
 * 
 * @param <CB> is the type of the bounds common to <var>&lt;DB&gt;</var> and <var>&lt;NB&gt;</var>.
 * @param <DB> is the type of the bounds of the data.
 * @param <D> is the type of the user data inside this tree.
 * @param <NB> is the type of the bounds of the nodes.
 * @param <N> is the type of the nodes in the tree.
 * @param <T> is the type of the tree.
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
abstract class AbstractIcosepBspTreeNode<
						CB extends CombinableBounds<? super CB,?,?,?>,
						DB extends CB,
						D extends WorldEntity<DB>,
						NB extends CB,
						N extends AbstractIcosepBspTreeNode<CB,DB,D,NB,N,T>,
						T extends AbstractIcosepBspTree<CB,DB,D,NB,N,T>>
extends IcosepBinaryTreeNode<D,N>
implements PerceptionTreeNode<D,NB,N> {

	private static final long serialVersionUID = -3361575991435178588L;

	private transient NB boundingBox;
	
	private transient WeakReference<T> tree;
	
	private transient List<PerceptionNodeListener<N>> listeners = null;
	
	private final double cutValue;
	private final int cutCoordinateIndex;
	
	/**
	 * @param tree is the tree in which this node is located.
	 * @param cutValue is the coordinate of the line that cut the plane. 
	 * @param cutCoordinateIndex is the index of the <var>cutValue</var> in an euclidian point. 
	 */
	public AbstractIcosepBspTreeNode(T tree, double cutValue, int cutCoordinateIndex) {
		this(tree, cutValue, cutCoordinateIndex, null);
	}

	/**
	 * @param tree is the tree in which this node is located.
	 * @param cutValue is the coordinate of the line that cut the plane. 
	 * @param cutCoordinateIndex is the index of the <var>cutValue</var> in an euclidian point. 
	 * @param data is the user data associated to this node. The list is not copied 
	 */
	public AbstractIcosepBspTreeNode(T tree, double cutValue, int cutCoordinateIndex, List<D> data) {
		super(true, false, data);
		this.cutValue = cutValue;
		this.cutCoordinateIndex = cutCoordinateIndex;
		this.tree = new WeakReference<T>(tree);
		if ((data!=null)&&
			(!data.isEmpty())) {
			updateBoundsAndNodeOwners(true,true);
		}
	}
	
	/** Create a not initialized node bounds.
	 *
	 * @return a no- initialized bounds
	 */
	protected abstract NB createEmptyNodeBounds();

	/** Replies if this node is a valid. The validity of a node depends of
	 * the node implementation.
	 * 
	 * @return <code>true</code> is this node is valid,
	 * otherwise <code>false</code>
	 */
	@Override
	public boolean isValid() {
		if (this.boundingBox!=null && this.boundingBox.isInit()) return !isEmpty();
		return isEmpty();
	}

	/** Set the tree associated to this node.
	 * 
	 * @param tree is the new tree
	 */
	void setTree(T tree) {
		this.tree = new WeakReference<T>(tree);
	}

	/** Set the tree associated to this node.
	 * 
	 * @param tree is the new tree
	 */
	void setTreeRecursively(T tree) {
		this.tree = new WeakReference<T>(tree);
		N child;
		for(int idx=0; idx<getChildCount(); ++idx) {
			child = getChildAt(idx);
			if (child!=null) {
				child.setTreeRecursively(tree);
			}
		}
	}

	/** Replies the tree associated to this node.
	 * 
	 * @return the tree
	 */
	protected T getTree() {
		return this.tree==null ? null : this.tree.get();
	}

	/** {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public final NB getBounds() {
		if (this.boundingBox==null ||
			!this.boundingBox.isInit())
				updateBoundsAndNodeOwners(false,false);
		if (this.boundingBox!=null) {
			return (NB)this.boundingBox.clone();
		}
		return null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public final void invalidateBounds() {
		this.boundingBox = null;
		Iterator<N> children = children();
		N child;
		while (children.hasNext()) {
			child = children.next();
			if (child!=null) child.invalidateBounds();
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean setUserData(Collection<D> data) {
		if (super.setUserData(data)) {
			// Update the bounding box
			updateBoundsAndNodeOwners(true,true);
			return true;
		}
		return false;
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean setUserDataAt(int index, D data) throws IndexOutOfBoundsException {
		if (super.setUserDataAt(index,data)) {
			// Update the bounding box
			updateBoundsAndNodeOwners(true,true);
			return true;
		}
		return false;
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean addUserData(Collection<? extends D> data) {
		if (super.addUserData(data)) {
			// Update the bounding box
			updateBoundsAndNodeOwners(true,true);
			return true;
		}
		return false;
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean removeUserData(Collection<D> data) {
		if (super.removeUserData(data)) {
			// Update the bounding box
			updateBoundsAndNodeOwners(true,false);
			return true;
		}
		return false;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public void removeAllUserData() {
		super.removeAllUserData();
		// Update the bounding box
		updateBoundsAndNodeOwners(true, false);
	}	

	/** Set the up right child of this node.
	 */
	@Override
	public boolean setLeftChild(N newChild) {
		if (super.setLeftChild(newChild)) {
			// Update the bounding box
			updateBoundsAndNodeOwners(true, false);
			return true;
		}
		return false;
	}

	/** Set the up left child of this node.
	 */
	@Override
	public boolean setRightChild(N newChild) {
		if (super.setRightChild(newChild)) {
			// Update the bounding box
			updateBoundsAndNodeOwners(true, false);
			return true;
		}
		return false;
	}

	/** Set the up right child of this node.
	 */
	@Override
	public boolean setIcosepChild(N newChild) {
		if (super.setIcosepChild(newChild)) {
			// Update the bounding box
			updateBoundsAndNodeOwners(true,false);
			return true;
		}
		return false;
	}

	/** {@inheritDoc}
	 */
	@Override
	public final void updateBounds(boolean updateParents) {
		updateBoundsAndNodeOwners(updateParents, false);
	}

	/** Update the bounds of this node and of the parent nodes.
	 * 
	 * @param updateParents msut be <code>true</code> to allows to update also
	 * the parent nodes.
	 * @param updateNodeOwner indicates if the node owner links must be updated also.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void updateBoundsAndNodeOwners(boolean updateParents, boolean updateNodeOwner) {
		T ltree = this.tree.get();
		
		Object nodeOwner;

		if ((ltree!=null)&&
			(!ltree.isBoundRefreshing())) {
			ltree.saveNodeForRefreshing(this);
			
			if (updateNodeOwner) {
				D entity;
				for(int i=0; i<getUserDataCount(); ++i) {
					entity = getUserDataAt(i);
					if (entity instanceof TreeDataMobileEntity
							&& this instanceof DynamicPerceptionTreeNode) {
						nodeOwner = ((TreeDataMobileEntity)entity).getNodeOwner();
						if (nodeOwner!=this)
							((TreeDataMobileEntity)entity).setNodeOwner((DynamicPerceptionTreeNode)this);
					}
				}
			}

			return;
		}
		
		// Compute the bounds of the eight children
		NB bb = createEmptyNodeBounds();
		NB cb;
		final int count = getChildCount();
		for(int i=0; i<count; ++i) {
			N child = getChildAt(i);
			if (child!=null) {
				cb = child.getBounds();
				if (cb!=null) bb.combine(child.getBounds());
			}
		}
		
		// Add the local objects
		for(int i=0; i<getUserDataCount(); ++i) {
			D entity = getUserDataAt(i);
			if (entity!=null) {
				DB entityBounds = entity.getBounds(); 
				if (entityBounds!=null) bb.combine(entityBounds);
				if (updateNodeOwner 
						&& entity instanceof TreeDataMobileEntity
						&& this instanceof DynamicPerceptionTreeNode) {
					nodeOwner = ((TreeDataMobileEntity)entity).getNodeOwner();
					if (nodeOwner!=this)
						((TreeDataMobileEntity)entity).setNodeOwner((DynamicPerceptionTreeNode)this);
				}
			}
		}

		// Apply the bound change only if necessary
		if ((this.boundingBox==null && bb.isInit())
			||
			(this.boundingBox!=null && !this.boundingBox.isInit() && bb.isInit())
			||
			(this.boundingBox!=null && this.boundingBox.isInit() && !bb.equals(this.boundingBox))) {
			this.boundingBox = bb;
			fireBoundUpdated((N)this);
			
			if (updateParents) {
				// Call recursively the parent's function
				N parent = getParentNode();
				if (parent!=null) parent.updateBounds(true);
			}
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public void addPerceptionNodeListener(PerceptionNodeListener<N> listener) {
		if (this.listeners==null)
			this.listeners = new ArrayList<PerceptionNodeListener<N>>();
		this.listeners.add(listener);
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public void removePerceptionNodeListener(PerceptionNodeListener<N> listener) {
		if (this.listeners==null) return;
		this.listeners.remove(listener);
		if (this.listeners.isEmpty())
			this.listeners = null;
	}

	/** Notify listeners about bound changes.
	 * 
	 * @param node is the node which has its bounds changed.
	 */
	protected void fireBoundUpdated(N node) {
		if (this.listeners==null) return;
		for(PerceptionNodeListener<N> listener : this.listeners) {
			if (listener!=null) {
				listener.boundsUpdated(node);
			}
		}
	}
	
	/** Replies the coordinate of the cut line.
	 * <p>
	 * The index of the replied coordinate in an
	 * euclidian point is given by
	 * {@link #getCutCoordinateIndex()}.
	 * 
	 * @return the coordinate of the cut line
	 * @see #getCutCoordinateIndex()
	 */
	public double getCutCoordinate() {
		return this.cutValue;
	}

	/** Replies the index of the cut-line coordinate
	 * in an euclidian point.
	 * The coordinate is replied by {@link #getCutCoordinate()}.
	 * 
	 * @return the index of the coordinate replied by
	 * {@link #getCutCoordinate()}
	 * @see #getCutCoordinate()
	 */
	public int getCutCoordinateIndex() {
		return this.cutCoordinateIndex;
	}
	
	/** Replies the coordinate that corresponds to the specified cut line.
	 * 
	 * @param i is always equals to the value replied by {@link #getCutCoordinateIndex()}.
	 * @return the coordinate of the cut line.
	 * @see #getCutCoordinateIndex()
	 * @see #getCutCoordinate()
	 */
	public double getCutLine(int i) {
		if (i==getCutCoordinateIndex())
			return getCutCoordinate();
		throw new IllegalArgumentException();
	}

}