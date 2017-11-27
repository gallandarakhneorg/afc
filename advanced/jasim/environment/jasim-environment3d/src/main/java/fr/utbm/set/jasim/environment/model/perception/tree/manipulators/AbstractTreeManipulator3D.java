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
package fr.utbm.set.jasim.environment.model.perception.tree.manipulators;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.vecmath.AxisAngle4d;

import fr.utbm.set.geom.bounds.bounds3d.CombinableBounds3D;
import fr.utbm.set.geom.intersection.IntersectionType;
import fr.utbm.set.geom.transform.Transform3D;
import fr.utbm.set.jasim.environment.model.perception.tree.DynamicPerceptionTree;
import fr.utbm.set.jasim.environment.model.perception.tree.DynamicPerceptionTreeNode;
import fr.utbm.set.jasim.environment.model.perception.tree.partitions.PartitionField;
import fr.utbm.set.jasim.environment.model.perception.tree.partitions.PartitionFieldFactory;
import fr.utbm.set.jasim.environment.model.perception.tree.partitions.PartitionPolicy;
import fr.utbm.set.jasim.environment.model.world.MobileEntity3D;

/**
 * A tree manipulator is the abstract definition
 * of a modification operation on a tree or on
 * a tree node.
 * 
 * @param <MB> is the type of the bounds of the data inside this tree 
 * @param <NB> is the type of the bounds of the tree nodes 
 * @param <N> is the type of the nodes inside this tree
 * @param <T> is the type of the tree to manipulate
 * @param <P> is the type of the partition field supported by this manipulator.
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractTreeManipulator3D<MB extends CombinableBounds3D, 
												NB extends CombinableBounds3D, 
												N extends DynamicPerceptionTreeNode<MB,MobileEntity3D<MB>,NB,N>,
												T extends DynamicPerceptionTree<?,?,MobileEntity3D<MB>,?,N,T,?>,
												P extends PartitionField>
implements TreeManipulator3D<MB,N,T> {

	private WeakReference<T> tree;
	
	private final Map<N, List<MobileEntity3D<MB>>> additions = new TreeMap<N,List<MobileEntity3D<MB>>>();
	private final Map<N, List<MobileEntity3D<MB>>> deletions = new TreeMap<N,List<MobileEntity3D<MB>>>();
	
	/** Is the count of entities over which a node must
	 * be splitted.
	 */
	protected final int splittingLevel;
	
	/** Is the factory used to build partition fields.
	 */
	private final PartitionFieldFactory<P> partitionFieldFactory;
	
	/** Is the partition heuristic to use.
	 */
	private final PartitionPolicy partitionHeuristic;
	
	/** Universe bounds
	 */
	private transient final NB universeBounds;

	/**
	 * @param splittingLevel is the count of entities over which a node must
	 * be splitted.
	 * @param fieldFactory is the factory which permits to create new partition fields.
	 * @param partitionPolicy is the partition heuristic.
	 * @param universe is the bounds of the universe.
	 */
	public AbstractTreeManipulator3D(int splittingLevel, PartitionFieldFactory<P> fieldFactory, PartitionPolicy partitionPolicy, NB universe) {
		this.splittingLevel = splittingLevel;
		this.partitionFieldFactory = fieldFactory;
		this.partitionHeuristic = partitionPolicy;
		this.universeBounds = universe;
	}
	
	/** Set the tree on which this manipulator is supposed to work.
	 * 
	 * @param tree
	 */
	public void setTree(T tree) {
		this.tree = tree==null ? null : new WeakReference<T>(tree);
	}
	
	/** Compute the preferred partition field.
	 * 
	 * @param nodeIndex is the index of the tree node for which the partition field must be computed.
	 * @param universe is the bounds that must be cut by a cut plane.
	 * @param entities is the list of the entities to partition. 
	 * @return the cut plane or <code>null</code>
	 */
	protected final P computePartitionField(int nodeIndex, NB universe, Collection<MobileEntity3D<MB>> entities) {
		assert(this.partitionFieldFactory!=null);
		assert(this.partitionHeuristic!=null);
		try {
			return this.partitionHeuristic.computePartitionField(
					nodeIndex, universe, entities,
					this.partitionFieldFactory);
		}
		catch(ClassCastException _) {
			return null;
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public T getTree() {
		return this.tree.get();
	}
	
	/** Create a list dedicated to user data in perception tree nodes.
	 * 
	 * @return a new instance of list for tree node user data.
	 */
	protected List<MobileEntity3D<MB>> createUserDataList() {
		return new LinkedList<MobileEntity3D<MB>>();
	}
	
	/** Register the specified addition.
	 * 
	 * @param targetNode is the node which will received the
	 * <var>entity</var> as a new child.
	 * @param entity is the object to manipulate.
	 */
	protected void registerAddition(N targetNode, MobileEntity3D<MB> entity) {
		assert(targetNode!=null);
		List<MobileEntity3D<MB>> list = this.additions.get(targetNode);
		if (list==null) {
			list = createUserDataList();
			this.additions.put(targetNode, list);
		}
		list.add(entity);
	}
	
	/** Register the specified addition.
	 * 
	 * @param oldNode is the node from which the
	 * <var>entity</var> will be removed.
	 * @param entity is the object to manipulate.
	 */
	@SuppressWarnings("unchecked")
	protected void registerDeletion(DynamicPerceptionTreeNode<MB,?,?,?> oldNode, MobileEntity3D<MB> entity) {
		assert(oldNode!=null);
		try {
			List<MobileEntity3D<MB>> list = this.deletions.get(oldNode);
			if (list==null) {
				list = createUserDataList();
				this.deletions.put((N)oldNode, list);
			}
			list.add(entity);
		}
		catch(ClassCastException _) {
			// Ignore this error.
		}
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public final void transform(Transform3D transform, MobileEntity3D<MB> entity) {
		transform(transform, Collections.singleton(entity));
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public void transform(Transform3D transform, Collection<MobileEntity3D<MB>> entities) {
		for (MobileEntity3D<MB> entity : entities) {
			DynamicPerceptionTreeNode<MB,?,?,?> oldNode = entity.getNodeOwner();
			assert(oldNode!=null);
			entity.transform(transform);
			N newNode = searchTopMostNode(entity);
			if (oldNode!=newNode) {
				registerAddition(newNode,entity);
				registerDeletion(oldNode, entity);
			}
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public final void translate(double dx, double dy, double dz, MobileEntity3D<MB> entity) {
		translate(dx, dy, dz, Collections.singleton(entity));
	}

	/** {@inheritDoc}
	 */
	@Override
	public void translate(double dx, double dy, double dz, Collection<MobileEntity3D<MB>> entities) {
		for (MobileEntity3D<MB> entity : entities) {
			DynamicPerceptionTreeNode<MB,?,?,?> oldNode = entity.getNodeOwner();
			assert(oldNode!=null);
			entity.translate(dx,dy,dz);
			N newNode = searchTopMostNode(entity);
			if (oldNode!=newNode) {
				registerAddition(newNode,entity);
				registerDeletion(oldNode, entity);
			}
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public final void rotate(double ax, double ay, double az, double angle, MobileEntity3D<MB> entity) {
		rotate(ax, ay, az, angle, Collections.singleton(entity));
	}

	/** {@inheritDoc}
	 */
	@Override
	public void rotate(double ax, double ay, double az, double angle, Collection<MobileEntity3D<MB>> entities) {
		AxisAngle4d aa = new AxisAngle4d(ax,ay,az,angle);
		for (MobileEntity3D<MB> entity : entities) {
			DynamicPerceptionTreeNode<MB,?,?,?> oldNode = entity.getNodeOwner();
			assert(oldNode!=null);
			entity.rotate(aa);
			N newNode = searchTopMostNode(entity);
			if (oldNode!=newNode) {
				registerAddition(newNode,entity);
				registerDeletion(oldNode, entity);
			}
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public final void scale(double sx, double sy, double sz, MobileEntity3D<MB> entity) {
		scale(sx, sy, sz, Collections.singleton(entity));
	}

	/** {@inheritDoc}
	 */
	@Override
	public void scale(double sx, double sy, double sz, Collection<MobileEntity3D<MB>> entities) {
		for (MobileEntity3D<MB> entity : entities) {
			DynamicPerceptionTreeNode<MB,?,?,?> oldNode = entity.getNodeOwner();
			assert(oldNode!=null);
			entity.scale(sx,sy,sz);
			N newNode = searchTopMostNode(entity);
			if (oldNode!=newNode) {
				registerAddition(newNode,entity);
				registerDeletion(oldNode, entity);
			}
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public void reorderEntities(Collection<? extends MobileEntity3D<MB>> entities) {
		for (MobileEntity3D<MB> entity : entities) {
			DynamicPerceptionTreeNode<MB,?,?,?> oldNode = entity.getNodeOwner();
			assert(oldNode!=null);
			N newNode = searchTopMostNode(entity);
			assert(newNode!=null);
			if (oldNode!=newNode) {
				registerAddition(newNode,entity);
				registerDeletion(oldNode, entity);
			}
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public void commit() {

		N node;
		
		T ltree = getTree();
		assert(ltree!=null);
		if (ltree.isBoundRefreshing())
			ltree.setBoundRefreshing(false);
		
		List<MobileEntity3D<MB>> entities;

		// Remove the entities
		for (Entry<N,List<MobileEntity3D<MB>>> entry : this.deletions.entrySet()) {
			node = entry.getKey();
			entities = entry.getValue();
			node.removeUserData(entities);
			entities.clear();
		}

		// Add the entities
		for (Entry<N,List<MobileEntity3D<MB>>> entry : this.additions.entrySet()) {
			node = entry.getKey();
			entities = entry.getValue();
			
			if (node==null) {
				node = createAndBindChildNode(null, 0, entities, 
						computePartitionField(0, this.universeBounds, entities));
				ltree.setRoot(node);
			}
			
			for(MobileEntity3D<MB> entity : entities) {
				insertInside(node, entity);
			}
			
			entities.clear();
		}
		this.additions.clear();

		// Force the tree to refresh the bounds 
		ltree.setBoundRefreshing(true);

		// Remove unnecessary nodes
		for(N removeCandidate : this.deletions.keySet()) {
			if (removeCandidate!=null && removeCandidate.isEmpty())
				removeCandidate.removeDeeplyFromParent();
		}
		this.deletions.clear();
	}

	/** Search the top-most node that contains the specified entity.
	 * <p>
	 * This function does not create the child nodes.
	 * <p>
	 * This function assumes that the entity is already inside the tree.
	 * 
	 * @param entity is the entity for which as node must be find
	 * @return the best node that could contains the given entity.
	 * @see #insertInside(DynamicPerceptionTreeNode, MobileEntity3D)
	 */
	@SuppressWarnings("unchecked")
	protected N searchTopMostNode(MobileEntity3D<MB> entity) {
		N owner;
		try {
			owner = (N)entity.getNodeOwner();
		}
		catch(ClassCastException _) {
			owner = null;
		}
		
		T ltree = getTree();
		assert(ltree!=null);

		if (owner!=null) {
			// Search the top-most node that contains the entity box
			// Test if the entity box is outside the node box 
			CombinableBounds3D nbox;
			MB bbox;
			
			bbox = entity.getBounds();
			assert(bbox!=null);

			do {
				nbox = owner.getBounds();
				assert(nbox!=null);
				if (nbox.classifies(bbox)==IntersectionType.INSIDE) break;
				owner = owner.getParentNode();
			}
			while (owner!=null);
		}
		
		// Assume the root as the better choice if
		// no node was already found.
		if (owner==null) {
			owner = ltree.getRoot();
		}
		
		return owner;
	}

	/** Convert the given data bounds into a node bounds.
	 * 
	 * @param dataBounds
	 * @return the node bounds
	 */
	protected abstract NB convertsDataBoundsToNodeBounds(MB dataBounds);
	
	/** Create an instance of tree node inside the parent node.
	 * <p>
	 * This function is assumed to bind the parent and child nodes and
	 * to put the given data inside the node.
	 * 
	 * @param parent is the parent node in which the new node must be put. If <code>null</code>
	 * the new node is assumed to be the new root of the tree.
	 * @param index is the index of the child in its parent.
	 * @param entities are the entities to put in the node. The list is not copied.
	 * @param cutPlane is the cut plane to use for the new node.
	 * @return the created node.
	 */
	protected abstract N createAndBindChildNode(N parent, int index, List<MobileEntity3D<MB>> entities, P cutPlane);

	/** {@inheritDoc}
	 */
	@Override
	public void insert(MobileEntity3D<MB> data) {
		T ltree = getTree();
		assert(ltree!=null);
		N node = ltree.getRoot();
		registerAddition(node, data);
	}

	/**
	 * Insert the specified entity inside the given root node or inside
	 * one of its children.
	 * <p>
	 * This function maintains a correct split ratio.
	 *
	 * @param node is the root in which the data must be inserted
	 * @param data is the data to insert
	 * @return the node in which the data was inserted, or <code>null</code> if
	 * the given node is <code>null</code>.
	 */
	@SuppressWarnings("unchecked")
	protected N insertInside(N node, MobileEntity3D<MB> data) {
		assert(data!=null);
		MB bounds = data.getBounds();
		assert(bounds!=null);
		
		N currentNode = node;
		NB universe = this.universeBounds;

		if (currentNode==null) return null;

		N target = null;

		while (target==null) {
			if (currentNode.isLeaf()) {
				// The node is a leaf, insert the element in the
				// node or in a new child if split is required.
				if ((currentNode.getUserDataCount()+1)>this.splittingLevel) {
					// Split the node
					int classification;
					int count = currentNode.getChildCount();
					
					// Prepare the list of elements for each child
					MB dataLocation;
					List<MobileEntity3D<MB>>[] collections = new List[count+1];
					
					// compute the positions of the data (new data included)
					int branchCount = 0;
					for(MobileEntity3D<MB> oldEntity : currentNode.getAllUserData()) {
						dataLocation = oldEntity.getBounds();
						classification = classifies(currentNode, dataLocation)+1;
						if (collections[classification]==null) {
							collections[classification] = new LinkedList<MobileEntity3D<MB>>();
							++branchCount;
						}
						collections[classification].add(oldEntity);
					}
			
					classification = classifies(currentNode, bounds)+1;
					if (collections[classification]==null) {
						collections[classification] = createUserDataList();
						++branchCount;
					}
					collections[classification].add(data);
					
					if (branchCount==1) {
						// All the objects should be inside the same node.
						// This is a special case where the node could not be splitted
						target = currentNode;
						currentNode.addUserData(data);
					}
					else {
						
						currentNode.removeAllUserData();
						if (collections[0]!=null && !collections[0].isEmpty()) {
							currentNode.addUserData(collections[0]);
							collections[0].clear();
							collections[0] = null;
							if (classification==0) target = currentNode;
						}

						// save the data into the children
						for(int region=1; region<collections.length; ++region) {
							if ((collections[region]!=null)&&(!collections[region].isEmpty())) {
								
								N newChild = createAndBindChildNode(
										currentNode, region-1,
										collections[region],
										computePartitionField(
												region-1,
												universe,
												collections[region]));
								
								if (region==classification)
									target = newChild;
							}
							
							collections[region] = null;
						}
					}
				}
				else {
					// The current node could contain the data
					currentNode.addUserData(data);
					target = currentNode;
				}
			}
			else {
				// The node is not a leaf. Insert the element inside the subtree
				int region = classifies(currentNode, bounds);
				if (region>=0) {
					N child = currentNode.getChildAt(region);
					if (child==null) {
						// No child, create it
						List<MobileEntity3D<MB>> childData = createUserDataList();
						childData.add(data);
						child = createAndBindChildNode(
								currentNode, 
										region,
										childData,
										computePartitionField(
												region,
												universe, 
												Collections.singleton(data)));
						target = child;
					}
					else {
						// Refine universe bounds
						NB newUniverse = refineUniverse(currentNode, universe, region);
						if (newUniverse!=null)
							universe = newUniverse;
						// Insert into the sub tree
						currentNode = child;
					}
				}
				else {
					// Insert into the current node
					currentNode.addUserData(data);
					target = currentNode;
				}
			}
		}
		
		return target;
	}
	
	/** Invoked to obtain the bounds of a subpart of the given universe.
	 * 
	 * @param node is the node associated to the given universe.
	 * @param universe is the bounds to split.
	 * @param region is the index of the subpart to reply.
	 * @return the subbounds.
	 */
	protected abstract NB refineUniverse(N node, NB universe, int region);

	/** Replies the classificiation of the specified bounds into the
	 * supported nodes.
	 * 
	 * @param node is the node in which the object must be classified
	 * @param location is the bound of the object to classify.
	 * @return the index of the child in which the object should be located or <code>-1</code>
	 * if the object should be located in the given <var>node</var>.
	 */
	protected abstract int classifies(N node, CombinableBounds3D location);
	
	/** {@inheritDoc}
	 */
	@Override
	public void remove(MobileEntity3D<MB> data) {
		DynamicPerceptionTreeNode<MB,?,?,?> owner = data.getNodeOwner();
		if (owner!=null) {
			registerDeletion(owner, data);
		}
	}

}