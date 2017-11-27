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
package fr.utbm.set.jasim.environment.model.perception.tree.structures.quadtree;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.vecmath.Point2d;

import fr.utbm.set.geom.bounds.CombinableBounds;
import fr.utbm.set.jasim.environment.model.perception.tree.DynamicPerceptionTreeNode;
import fr.utbm.set.jasim.environment.model.perception.tree.PerceptionNodeListener;
import fr.utbm.set.jasim.environment.model.perception.tree.PerceptionTreeNode;
import fr.utbm.set.jasim.environment.model.world.TreeDataMobileEntity;
import fr.utbm.set.jasim.environment.model.world.WorldEntity;
import fr.utbm.set.tree.node.QuadTreeNode;

/**
 * This is the generic implementation of a
 * tree for which each node has four children.
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
abstract class AbstractQuadTreeNode<
					CB extends CombinableBounds<? super CB,?,?,?>,
					DB extends CB,
					D extends WorldEntity<DB>,
					NB extends CB,
					N extends AbstractQuadTreeNode<CB,DB,D,NB,N,T>,
					T extends AbstractQuadTree<CB,DB,D,NB,N,T>>
extends QuadTreeNode<D,N>
implements PerceptionTreeNode<D,NB,N> {

	private static final long serialVersionUID = -2207667897266396831L;

	private transient NB boundingBox;
	
	private transient WeakReference<T> tree;
	
	private transient List<PerceptionNodeListener<N>> listeners = null;
	
	private final double cutX;
	private final double cutY;
	
	/**
	 * @param tree is the tree in which this node is located.
	 * @param cutX is the coordinate of the vertical line that cut the plane. 
	 * @param cutY is the coordinate of the horizontal line that cut the plane. 
	 */
	public AbstractQuadTreeNode(T tree, double cutX, double cutY) {
		this(tree, cutX, cutY, null);
	}

	/**
	 * @param tree is the tree in which this node is located.
	 * @param cutX is the coordinate of the vertical line that cut the plane. 
	 * @param cutY is the coordinate of the horizontal line that cut the plane.
	 * @param data is the user data associated to this node. The list is not copied. 
	 */
	public AbstractQuadTreeNode(T tree, double cutX, double cutY, List<D> data) {
		super(true, false, data);
		this.cutX = cutX;
		this.cutY = cutY;
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
			updateBoundsAndNodeOwners(true, false);
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
	
	/** {@inheritDoc}
	 */
	@Override
	public boolean setFirstChild(N newChild) {
		if (super.setFirstChild(newChild)) {
			// Update the bounding box
			updateBoundsAndNodeOwners(true, false);
			return true;
		}
		return false;
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean setSecondChild(N newChild) {
		if (super.setSecondChild(newChild)) {
			// Update the bounding box
			updateBoundsAndNodeOwners(true, false);
			return true;
		}
		return false;
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean setThirdChild(N newChild) {
		if (super.setThirdChild(newChild)) {
			// Update the bounding box
			updateBoundsAndNodeOwners(true, false);
			return true;
		}
		return false;
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean setFourthChild(N newChild) {
		if (super.setFourthChild(newChild)) {
			// Update the bounding box
			updateBoundsAndNodeOwners(true, false);
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
	
	/** Replies the X coordinate of the vertical cut line.
	 * 
	 * @return the X-coordinate ofthe cut line
	 * @see #getCutLineY()
	 * @see #getCutLine(int)
	 */
	public double getCutLineX() {
		return this.cutX;
	}

	/** Replies the Y coordinate of the horizontal cut line.
	 * 
	 * @return the X-coordinate ofthe cut line
	 * @see #getCutLineX()
	 * @see #getCutLine(int)
	 */
	public double getCutLineY() {
		return this.cutY;
	}
	
	/** Replies the coordinate that corresponds to the specified cut line.
	 * 
	 * @param i is {@code 0} for the vertical line, {@code 1} for the horizontal.
	 * @return the coordinate of the cut line that corresponds to the given parameter.
	 * @see #getCutLineX()
	 * @see #getCutLineY()
	 */
	public double getCutLine(int i) {
		switch(i) {
		case 0:
			return getCutLineX();
		case 1:
			return getCutLineY();
		default:
			throw new IllegalArgumentException();
		}
	}

	/** Replies the (X,Y) coordinates of the cut lines.
	 * 
	 * @return the X- and Y-coordinates of the cut lines.
	 * @see #getCutLineX()
	 * @see #getCutLineY()
	 * @see #getCutLine(int)
	 */
	public Point2d getCutLineIntersectionPoint() {
		return new Point2d(this.cutX, this.cutY);
	}

}