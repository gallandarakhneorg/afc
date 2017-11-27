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
package fr.utbm.set.jasim.environment.model.perception.tree.builders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.arakhne.afc.progress.Progression;

import fr.utbm.set.collection.IntegerList;
import fr.utbm.set.geom.bounds.CombinableBounds;
import fr.utbm.set.jasim.environment.model.perception.tree.PerceptionTree;
import fr.utbm.set.jasim.environment.model.perception.tree.PerceptionTreeNode;
import fr.utbm.set.jasim.environment.model.perception.tree.partitions.PartitionField;
import fr.utbm.set.jasim.environment.model.perception.tree.partitions.PartitionFieldFactory;
import fr.utbm.set.jasim.environment.model.perception.tree.partitions.PartitionPolicy;
import fr.utbm.set.jasim.environment.model.world.WorldEntity;
import fr.utbm.set.tree.builder.TreeBuilderException;
import fr.utbm.set.tree.builder.TreeBuilderException.EmptyBoundingBoxException;

/**
 * A tree builder is a class that create a tree
 * according to a set of objects and a set
 * of contraints and heuristics. The objects
 * have to be axis-aligned.
 *
 * @param <CB> is the common type to the generic parameters {@code DB} and {@code NB}.
 * @param <T> is the type of the tree to build.
 * @param <DB> is the type of bounds for data.
 * @param <D> is the type of the user data inside this tree.
 * @param <NB> is the type of the node bounds inside this tree.
 * @param <N> is the type of the tree nodes.
 * @param <P> is the type of plane to use to build the tree.
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @author $Author: jdemange$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractPerceptionTreeBuilder<
						CB extends CombinableBounds<? super CB,?,?,?>,
						DB extends CB,
						D extends WorldEntity<DB>, 
						T extends PerceptionTree<CB,DB,D,NB,N>,
						NB extends CB,
						N extends PerceptionTreeNode<D,NB,N>,
						P extends PartitionField> 
implements PerceptionTreeBuilder<D,T> {

	private PartitionFieldFactory<P> partitionFieldFactory = null;
	private PartitionPolicy partitionHeuristic;
	private int splittingCount;

	private volatile boolean started = false;

	/** List of icosep node indexes.
	 */
	private final IntegerList icosepNodeIndexes;

	/** List of entities to insert inside a tree.
	 */
	protected List<D> entities;
	
	/** Is the instance of the tree under building.
	 */
	protected T builtTree;

	/**
	 * @param splittingCount is the maximal count of elements per node, if possible.
	 * @param partitionHeuristic is the heuristic to use to compute the partitions.
	 */
	public AbstractPerceptionTreeBuilder(int splittingCount, PartitionPolicy partitionHeuristic) {
		this.splittingCount = splittingCount;
		this.partitionHeuristic = partitionHeuristic;
		this.icosepNodeIndexes = null;
	}

	/**
	 * @param splittingCount is the maximal count of elements per node, if possible.
	 * @param partitionHeuristic is the heuristic to use to compute the partitions.
	 * @param icosepNodeSplitted must be <code>true</code> to decomposed icosep nodes, and
	 * <code>false</code> to incompose the icosep nodes. 
	 */
	public AbstractPerceptionTreeBuilder(int splittingCount, PartitionPolicy partitionHeuristic, boolean icosepNodeSplitted) {
		this.splittingCount = splittingCount;
		this.partitionHeuristic = partitionHeuristic;
		
		if (icosepNodeSplitted) {
			this.icosepNodeIndexes = new IntegerList();
			fillWithIcosepNodeIndexes(this.icosepNodeIndexes);
		}
		else {
			this.icosepNodeIndexes = null;
		}
	}
	
	/** Replies used partition heuristic.
	 * 
	 * @return the partition heuristic
	 */
	public PartitionPolicy getPartitionHeuristic() {
		return this.partitionHeuristic;
	}

	/** Fill the given list with the indexes of the icosep nodes.
	 * 
	 * @param list is the list to fill with the icosep node indexes
	 * if they exist.
	 */
	protected void fillWithIcosepNodeIndexes(List<Integer> list) {
		//
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setSplittingCount(int count) {
		if (count<=0) throw new IllegalArgumentException();
		this.splittingCount = count;
	}

	/** {@inheritDoc}
	 */
	@Override
	public int getSplittingCount() {
		return this.splittingCount;
	}

	/** Set the partition heuristic to use.
	 * 
	 * @param partitionHeuristic
	 */
	public void setPartitionHeuristic(PartitionPolicy partitionHeuristic) {
		if (partitionHeuristic==null) throw new IllegalArgumentException();
		this.partitionHeuristic = partitionHeuristic;
	}

	/** Set the partition field factory to use.
	 * 
	 * @param partitionFieldFactory
	 */
	public void setPartitionFieldFactory(PartitionFieldFactory<P> partitionFieldFactory) {
		if (partitionFieldFactory==null) throw new IllegalArgumentException();
		this.partitionFieldFactory = partitionFieldFactory;
	}

	/** Replies if the icoseptre nodes are splitted.
	 * 
	 * @return <code>true</code> if the icosep nodes are splitted.
	 */
	public boolean isIcosepNodeDecomposed() {
		return this.icosepNodeIndexes==null;
	}

	/** Replies the bounds of the given entities.
	 * 
	 * @param indexes is the list of entity's indexes to insert inside the tree.
	 * @return the bounding box of the entities pointed by the given indexes.
	 */
	protected NB computeBounds(IntegerList... indexes) {
		NB box = createEmptyNodeBounds();
		for(IntegerList list : indexes) {
			for (int i : list) {
				D d = this.entities.get(i);
				if (d!=null) {
					DB b = d.getBounds();
					if (b!=null) box.combine(b);
				}
			}
		}
		return box;
	}

	/** Replies the AABB of the given entities.
	 * 
	 * @param worldEntities is the list of entities for which the bounds must be computed.
	 * @return the bounding box of the given entities.
	 */
	protected NB computeBounds(Collection<? extends D> worldEntities) {
		NB box = createEmptyNodeBounds();
		for (D d : worldEntities) {
			if (d!=null) {
				DB b = d.getBounds();
				if (b!=null) box.combine(b);
			}
		}
		return box;
	}
	
	/** Create an empty bounding box.
	 * 
	 * @return an empty bounding box.
	 */
	protected abstract NB createEmptyNodeBounds();

	/** Extract the data that corresponds to the specified interval.
	 * 
	 * @param indexes is the list of entity's indexes to insert inside the tree.
	 * @return the list of entities that is corresponding to the given indexes.
	 */
	protected Collection<D> extractData(IntegerList... indexes) {
		ArrayList<D> tab = new ArrayList<D>();
		int count = 0;
		for(IntegerList list : indexes) {
			count += list.size();
			tab.ensureCapacity(count);
			for(int idx : list) {
				if (idx>=0 && idx<this.entities.size())
					tab.add(this.entities.get(idx));
			}
		}
		return tab;
	}
	
	/** Create and reply an empty instance of tree.
	 * 
	 * @param universeBounds are the bounds of the universe.
	 * @return an empty instance of tree.
	 */
	protected abstract T createEmptyTree(NB universeBounds);
	
	/** Create and reply an empty tree node.
	 * 
	 * @param index indicates the future index of the new child in its parent.
	 * @param box is the box which encloses the new node.
	 * @param indexes are the indexes of the data to put in the node.
	 * @param cutPlane is the cut plane. 
	 * @return an empty tree node.
	 */
	protected abstract N createTreeNode(int index, NB box, IntegerList indexes, P cutPlane);

	/** {@inheritDoc}
	 */
	@Override
	public final T buildTree(List<? extends D> worldEntities) throws TreeBuilderException {
		return buildTree(worldEntities, null, null);
	}

	/** {@inheritDoc}
	 */
	@Override
	public final T buildTree(List<? extends D> worldEntities, Progression pm) throws TreeBuilderException {
		return buildTree(worldEntities, null, pm);
	}

	/** Builds a tree.
	 * 
	 * @param worldEntities is the set of world's entities from which the tree must be built.
	 * @param pm is the task progression indicator, may be <code>null</code>
	 * @return the built tree.
	 * @throws TreeBuilderException
	 */
	public final T buildTree(Iterator<? extends D> worldEntities, Progression pm) throws TreeBuilderException {
		return buildTree(worldEntities, null, pm);
	}

	/** Builds a tree.
	 * 
	 * @param worldEntities is the set of world's entities from which the tree must be built.
	 * @return the built tree.
	 * @throws TreeBuilderException
	 */
	public final T buildTree(Iterator<? extends D> worldEntities) throws TreeBuilderException {
		return buildTree(worldEntities, null, null);
	}

	/** Builds a tree.
	 * 
	 * @param worldEntities is the set of world's entities from which the tree must be built.
	 * @param worldBounds are the bounds of the world to use, or <code>null</code>.
	 * @return the built tree.
	 * @throws TreeBuilderException
	 */
	public final T buildTree(Iterator<? extends D> worldEntities, NB worldBounds) throws TreeBuilderException {
		return buildTree(worldEntities, worldBounds, null);
	}

	/** Builds a tree.
	 * 
	 * @param worldEntities is the set of world's entities from which the tree must be built.
	 * @param worldBounds are the bounds of the world to use, or <code>null</code>.
	 * @param pm is the task progression indicator, may be <code>null</code>
	 * @return the built tree.
	 * @throws TreeBuilderException
	 */
	public T buildTree(Iterator<? extends D> worldEntities, NB worldBounds, Progression pm) throws TreeBuilderException {
		if (this.started) throw new TreeBuilderException.BuilderAlreadyStartedException();
		this.started = true;
		
		this.entities = new ArrayList<D>();
		while (worldEntities.hasNext()) {
			this.entities.add(worldEntities.next());
		}
		
		return buildTree(worldBounds, pm);
	}

	/** Builds a tree.
	 * 
	 * @param worldEntities is the set of world's entities from which the tree must be built.
	 * @param worldBounds are the bounds of the world to use, or <code>null</code>.
	 * @return the built tree.
	 * @throws TreeBuilderException
	 */
	public final T buildTree(List<? extends D> worldEntities, NB worldBounds) throws TreeBuilderException {
		return buildTree(worldEntities, worldBounds, null);
	}

	/** Builds a tree.
	 * 
	 * @param worldEntities is the set of world's entities from which the tree must be built.
	 * @param worldBounds are the bounds of the world to use, or <code>null</code>.
	 * @param pm is the task progression indicator, may be <code>null</code>
	 * @return the built tree.
	 * @throws TreeBuilderException
	 */
	@SuppressWarnings("unchecked")
	public T buildTree(List<? extends D> worldEntities, NB worldBounds, Progression pm) throws TreeBuilderException {
		if (this.started) throw new TreeBuilderException.BuilderAlreadyStartedException();
		this.started = true;
		
		this.entities =(List<D>)worldEntities;
		
		return buildTree(worldBounds, pm);
	}

	/** Builds a tree.
	 * 
	 * @param worldBounds are the bounds of the world to use, or <code>null</code>.
	 * @param pm is the task progression indicator, may be <code>null</code>
	 * @return the built tree.
	 * @throws TreeBuilderException
	 */
	private T buildTree(NB worldBounds, Progression pm) throws TreeBuilderException {
		//
		// Compute the AABB of the entities
		//
		int nodeCountEstimation = this.entities.size()/this.splittingCount;
		if (pm!=null) pm.setProperties(0, 0, this.entities.size()+nodeCountEstimation+2, false);
		
		NB box;
		if (worldBounds==null)
			box = computeBounds(this.entities);
		else
			box = worldBounds;
		
		if (box==null) throw new EmptyBoundingBoxException();
		
		if (pm!=null) pm.setValue(this.entities.size());

		this.builtTree = createEmptyTree(box);
		if (pm!=null) pm.setValue(this.entities.size()+1);
		
		// Compute the tree (recursively)
		Progression subTask = null;
		if (pm!=null) subTask = pm.subTask(nodeCountEstimation);
		//expandsTreeOnTheBottom_recursiveform(-1, box, new IntegerList(0, this.entities.size()-1), subTask);
		expandsTreeOnTheBottom(box, new IntegerList(0, this.entities.size()-1), subTask);
		if (subTask!=null) subTask.setValue(subTask.getMaximum());

		// Clean bounds
		this.builtTree.invalidateBounds();
		
		// cleaning up
		this.entities = null;
		T tree = this.builtTree;
		this.builtTree = null;
		this.started = false;
		
		if (pm!=null) pm.setValue(pm.getMaximum());
		
		return tree;
	}
	
	/** Compute the preferred partition field.
	 * 
	 * @param nodeIndex is the index of the tree node for which the partition field must be computed.
	 * @param universe is the bounds that must be cut by a cut plane
	 * @param indexes is the list of entity's indexes to insert inside the tree.
	 * @param notSelectable is the list of not-selectable planes 
	 * @return the cut plane or <code>null</code>
	 */
	protected final P computePartitionField(int nodeIndex, NB universe, IntegerList indexes, Collection<P> notSelectable) {
		PartitionPolicy heuristic = getPartitionHeuristic();
		assert(heuristic!=null);
		if (this.partitionFieldFactory==null)
			throw new IllegalStateException("you must invoke setPartitionFieldFactory() before building"); //$NON-NLS-1$
		return heuristic.computePartitionField(
				nodeIndex, universe, this.entities, indexes,
				notSelectable, this.partitionFieldFactory);
	}
		
	/** Replies the subarea that corresponds to the given information.
	 * 
	 * @param childIndex is the index of the subarea to compute.
	 * @param sourceArea is the area that must be splitted.
	 * @param plane is the cut plane.
	 * @param elementIndexes is the list of the indexes of the elements in the subarea.
	 * @return the bounds of the area, never <code>null</code>
	 */
	protected abstract NB getSubArea(int childIndex, NB sourceArea, P plane, IntegerList elementIndexes);
	
	/** Replies the count of child nodes for each node.
	 * 
	 * @return the count of child nodes for each node.
	 */
	protected abstract int getChildNodeCount();
	
	/** Iterative built of the tree.
	 * 
	 * @param childIndex indicates the future index of the new child in its parent.
	 * @param universe1 is bounding box of the universe
	 * @param indexes1 is the list of entity's indexes to insert inside the tree.
	 * @param pm is a task progression indicator.
	 * @throws TreeBuilderException
	 */
	private void expandsTreeOnTheBottom(NB universe1, IntegerList indexes1, Progression pm) 
	throws TreeBuilderException {
		final List<Candidate> openList = new LinkedList<Candidate>();
		Candidate candidate;
		P selectedPlane;
		IntegerList currentNodeIndexes = new IntegerList();
		IntegerList[] indexTabs = new IntegerList[getChildNodeCount()];
		List<P> selectedPlanes = new ArrayList<P>(3);
		int callCount, cnt, child;
		int treated = 0;

		for(int idx=0; idx<indexTabs.length; ++idx)
			indexTabs[idx] = new IntegerList();

		candidate = new Candidate(null, -1, universe1, indexes1);
		
		if (pm!=null) pm.setProperties(0, 0, 1, false);
		
		while (candidate!=null) {
			
			if (candidate.hasBounds() && candidate.hasIndexes()) {
				
				if ((candidate.getIndexes().size()<=this.splittingCount)
					||((this.icosepNodeIndexes!=null)&&(this.icosepNodeIndexes.contains(candidate.CHILD_INDEX)))) {
					
					// Leaf node
					
					candidate.createNode();
					
				}
				else {
					
					// Not leaf node
					
					callCount = 0;
			
					selectedPlane = null;
					currentNodeIndexes.clear();
					
					selectedPlanes.clear();
					
					do {
						selectedPlane = computePartitionField(
								candidate.CHILD_INDEX, candidate.getBounds(), candidate.getIndexes(), selectedPlanes);
						if (selectedPlane==null) break;
						
						//
						// Build the three groups
						//
						currentNodeIndexes.clear();
						for(int idx=0; idx<indexTabs.length; ++idx)
							indexTabs[idx].clear();
						
						for(int idxObj : candidate.getIndexes()) {
							CombinableBounds<?,?,?,?> box = this.entities.get(idxObj).getBounds();
							child = selectedPlane.classifies(candidate.CHILD_INDEX, box);
							if (child>=0) indexTabs[child].add(idxObj);
							else currentNodeIndexes.add(idxObj);
						}
		
						cnt = currentNodeIndexes.isEmpty() ? 0 : 1;
						for(int idx=0; idx<indexTabs.length; ++idx)
							cnt += indexTabs[idx].isEmpty() ? 0 : 1;
						
						if (cnt==1) {
							selectedPlanes.add(selectedPlane);
							selectedPlane = null;
						}
		
					}
					while (selectedPlane==null);

					// Create the current node with its data content
					if (selectedPlane!=null) {
						// A cutting plane was computed.
						//
						candidate.setEntityIndexes(currentNodeIndexes);
						currentNodeIndexes.clear();
						
						// Put child nodes in open list
						//
						for(int idx=0; idx<indexTabs.length; ++idx) {
							if (!indexTabs[idx].isEmpty()) {
								openList.add(new Candidate(
										candidate,
										idx,
										getSubArea(idx,candidate.getBounds(),selectedPlane,indexTabs[idx]),
										indexTabs[idx]));
								indexTabs[idx].clear();
							}
						}
					}
					else {
						// No cutting plane computed.
						// All the data may be in a root node.
						
						// Compute the default partition field
						N theNode = candidate.createNode();
			
						//Only one child has all the objects.
						N nNode = onUnclassifiableObjects(callCount++, candidate.CHILD_INDEX, theNode, indexTabs);
						if (nNode!=null) candidate.setNode(nNode);
		
						if (pm!=null) pm.setValue(pm.getValue()+1);
						
					}
					
				}
				
			}

			treated ++;
			if (pm!=null) pm.setProperties(treated, 0, openList.size()+treated, false);

			candidate = openList.isEmpty() ? null : openList.remove(0);
		}

		if (pm!=null) pm.setValue(pm.getMaximum());
	}
	
	/* Recursive built of the tree.
	 * 
	 * @param childIndex indicates the future index of the new child in its parent.
	 * @param universe is bounding box of the universe
	 * @param indexes is the list of entity's indexes to insert inside the tree.
	 * @param pm is a task progression indicator.
	 * @return the root node.
	 * @throws TreeBuilderException
	 */
	/*private N expandsTreeOnTheBottom_recursiveform(int childIndex, NB universe, IntegerList indexes, Progression pm) 
	throws TreeBuilderException {
		N theNode = null;

		// Does the index represent a no empty set of objects
		// or the node is an indecomposable node
		if (universe==null || indexes==null || indexes.isEmpty()) return null;
		if ((indexes.size()<=this.splittingCount)||
			((this.icosepNodeIndexes!=null)&&(this.icosepNodeIndexes.contains(childIndex)))) {
			P cutPlane = computePartitionField(
					childIndex, universe, 
					indexes, Collections.<P>emptyList());
			assert(cutPlane!=null);
			theNode = createTreeNode(
					childIndex, 
					universe, 
					indexes, 
					cutPlane);
			if (pm!=null) pm.setValue(pm.getValue()+1);
		}
		else {
		
			// Build the current node
			int callCount = 0;
			List<P> selectedPlanes = new ArrayList<P>(3);
	
			while (theNode==null) {
				//
				// Select the cut plane
				//
				IntegerList currentNodeIndexes = new IntegerList();
				IntegerList[] indexTabs = new IntegerList[getChildNodeCount()];
				for(int idx=0; idx<indexTabs.length; ++idx)
					indexTabs[idx] = new IntegerList();
		
				P selectedPlane = null;
		
				selectedPlanes.clear();
			
				do {
					selectedPlane = computePartitionField(
							childIndex, universe, indexes, selectedPlanes);
					if (selectedPlane==null) break;
					
					//
					// Build the three groups
					//
					currentNodeIndexes.clear();
					for(int idx=0; idx<indexTabs.length; ++idx)
						indexTabs[idx].clear();
					
					for(int idxObj : indexes) {
						CombinableBounds<?,?,?,?> box = this.entities.get(idxObj).getBounds();
						int child = selectedPlane.classifies(childIndex, box);
						if (child>=0) indexTabs[child].add(idxObj);
						else currentNodeIndexes.add(idxObj);
					}
	
					int cnt = currentNodeIndexes.isEmpty() ? 0 : 1;
					for(int idx=0; idx<indexTabs.length; ++idx)
						cnt += indexTabs[idx].isEmpty() ? 0 : 1;
					
					if (cnt==1) {
						selectedPlanes.add(selectedPlane);
						selectedPlane = null;
					}
	
				}
				while (selectedPlane==null);
	
				// Create the current node with its data content
				if (selectedPlane!=null) {
					// A cutting plane was computed.
					// Create the splitted node.
					theNode = createTreeNode(
							childIndex, 
							universe, 
							currentNodeIndexes, 
							selectedPlane);
					currentNodeIndexes.clear();
					currentNodeIndexes = null;
					//
					// Build the subtrees
					//
					for(int idx=0; idx<indexTabs.length; ++idx) {
						if (!indexTabs[idx].isEmpty()) {
							N group = expandsTreeOnTheBottom_recursiveform(idx, getSubArea(idx,universe,selectedPlane,indexTabs[idx]), indexTabs[idx], pm);
							indexTabs[idx].clear();
							indexTabs[idx] = null;
							
							if (group!=null)
								theNode.setChildAt(idx, group);
						}
					}				
				}
				else {
					// No cutting plane computed.
					// All the data may be in a root node.
					
					// Compute the default partition field
					P cutPlane = computePartitionField(
									childIndex, universe, 
									indexes, Collections.<P>emptyList());
					assert(cutPlane!=null);
					
					// Create the root node
					theNode = createTreeNode(
							childIndex, 
							universe, 
							indexes, 
							cutPlane);
		
					//Only one child has all the objects.
					N nNode = onUnclassifiableObjects(callCount++, childIndex, theNode, indexTabs);
					if (nNode!=null) theNode = nNode;
	
					if (pm!=null) pm.setValue(pm.getValue()+1);
					
				}
			}
		}
		
		return theNode;
	}*/
	
	/** Invoked when all the objects could not be classified into child nodes.
	 * 
	 * @param callCount is the count of calls of this function on the given node.
	 * @param index is the in-parent position of the node in which the objects could not be classified
	 * @param node is the node in which the objects could not be classified
	 * @param objects is the list of objects' indexes that ar enot classifiable.
	 * @return a node which must be used in place of the given <var>node</var>, or <code>null</code>
	 * to use the given node.
	 */
	protected N onUnclassifiableObjects(int callCount, int index, N node, IntegerList... objects) {
		return null;
	}
		
	/**
	 * Candidate for the tree.
	 *
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class Candidate {
		
		private N node;
		
		/** Parent candidate.
		 */
		public final Candidate PARENT;
		
		/** Bounds of the candidate.
		 */
		private NB bounds;
		
		/** Objects covered by this candidate.
		 */
		private IntegerList entityIndexes;
		
		/** Child index.
		 */
		public final int CHILD_INDEX;
		
		/**
		 * @param parent
		 * @param childIndex
		 * @param candidateBounds
		 * @param indexes
		 */
		public Candidate(Candidate parent, int childIndex, NB candidateBounds, IntegerList indexes) {
			this.PARENT = parent;
			this.bounds = candidateBounds;
			this.entityIndexes = new IntegerList(indexes);
			this.CHILD_INDEX = childIndex;
			this.node = null;
		}
		
		/** Force the indexes of the entities in this node.
		 * 
		 * @param indexes
		 */
		public void setEntityIndexes(IntegerList indexes) {
			if (this.entityIndexes==null) this.entityIndexes = new IntegerList();
			this.entityIndexes.set(indexes);
		}

		/** Create a tree node and bind it to the tree.
		 * 
		 * @return the node
		 */
		public N createNode() {
			P cutPlane = computePartitionField(
					this.CHILD_INDEX, this.bounds, 
					this.entityIndexes, Collections.<P>emptyList());
			return createNode(cutPlane);
		}
		
		/** Create a tree node and bind it to the tree.
		 * 
		 * @param cutPlane
		 * @return the node
		 */
		public N createNode(P cutPlane) {
			assert(cutPlane!=null);
			setNode(createTreeNode(
					this.CHILD_INDEX, this.bounds, 
					this.entityIndexes, cutPlane));
			this.entityIndexes.clear();
			this.entityIndexes = null;
			this.bounds = null;
			return this.node;
		}

		/** Set the node attached to this candidate.
		 * 
		 * @param newNode
		 */
		public void setNode(N newNode) {
			this.node = newNode;
			if (this.PARENT!=null) {
				this.PARENT.setChildAt(this.CHILD_INDEX, this.node);
			}
			else {
				AbstractPerceptionTreeBuilder.this.builtTree.setRoot(this.node);
			}
		}
		
		private void setChildAt(int childIndex, N child) {
			if (this.node==null) {
				createNode();
			}
			this.node.setChildAt(childIndex, child);
		}
		
		/** Replies the bounds.
		 * 
		 * @return the bounds
		 */
		public NB getBounds() {
			assert(this.bounds!=null);
			return this.bounds;
		}
		
		/** Replies if this candidate has bounds.
		 * 
		 * @return <code>true</code> if this candidate contains bounds, otherwise <code>false</code>
		 */
		public boolean hasBounds() {
			return (this.bounds!=null);
		}

		/** Replies the entity indexes in this node.
		 * 
		 * @return the entity indexes
		 */
		public IntegerList getIndexes() {
			assert(this.entityIndexes!=null);
			return this.entityIndexes;
		}
		
		/** Replies if this candidate has entity indexes.
		 * 
		 * @return <code>true</code> if this candidate contains entity indexes, otherwise <code>false</code>
		 */
		public boolean hasIndexes() {
			return (this.entityIndexes!=null && !this.entityIndexes.isEmpty());
		}

	}

}