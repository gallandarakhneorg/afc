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

package fr.utbm.set.jasim.environment.model.perception.tree.cullers;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import fr.utbm.set.geom.bounds.Bounds;
import fr.utbm.set.geom.intersection.IntersectionType;
import fr.utbm.set.jasim.environment.interfaces.body.frustums.Frustum;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.InterestFilter;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.PhysicalPerceptionAlterator;
import fr.utbm.set.jasim.environment.model.perception.frustum.CullingResult;
import fr.utbm.set.jasim.environment.model.perception.tree.PerceptionTree;
import fr.utbm.set.jasim.environment.model.perception.tree.PerceptionTreeNode;
import fr.utbm.set.jasim.environment.model.world.WorldEntity;
import fr.utbm.set.tree.iterator.DataSelectionTreeIterator;
import fr.utbm.set.tree.iterator.DataSelector;
import fr.utbm.set.tree.iterator.NodeSelector;
import fr.utbm.set.tree.iterator.PrefixDataDepthFirstTreeIterator;

/**
 * This class is an iterator on a tree based on frustum culling.
 * 
 * @param <CB> is the type of the bounds supported by this culler (including the
 * entity bounds and the node bounds).
 * @param <DB> is the type of the bounds supported by the entities.
 * @param <D> is the type of entity supported by the culler
 * @param <NB> is the type of the bounds supported by the tree nodes.
 * @param <N> is the type of the tree nodes.
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class FrustumCuller<CB extends Bounds<?,?,?>,
						   DB extends CB,
						   D extends WorldEntity<DB>,
						   NB extends CB,
						   N extends PerceptionTreeNode<D,NB,N>> 
implements Iterator<CullingResult<D>> {

	/** Frustum used to cull.
	 */
	final Frustum<? super CB,?,?> frustum;
	
	private final DataSelectionTreeIterator<D,N> iterator;

	private final List<IntersectionType> classifications = new ArrayList<IntersectionType>();

	/**
	 * @param iterator is an iterator on the raw data structure.
	 * @param frustum is the frustum to use to classify and cull the objects
	 * @param physicalAlterator is the physical alterator used to alter the object classifications
	 * @param interestFilter is describing the agent interests.
	 */
	public FrustumCuller(DataSelectionTreeIterator<D,N> iterator, Frustum<? super CB,?,?> frustum, PhysicalPerceptionAlterator physicalAlterator, InterestFilter interestFilter) {
		this.iterator = iterator;
		this.frustum = frustum;
		initializeSelectors(physicalAlterator, interestFilter);
	}

	/**
	 * @param tree is the raw data structure.
	 * @param frustum is the frustum to use to classify and cull the objects
	 * @param physicalAlterator is the physical alterator used to alter the object classifications
	 * @param interestFilter is describing the agent interests.
	 */
	public FrustumCuller(PerceptionTree<CB,?,D,?,N> tree, Frustum<? super CB,?,?> frustum, PhysicalPerceptionAlterator physicalAlterator, InterestFilter interestFilter) {
		this.iterator = new PrefixDataDepthFirstTreeIterator<D,N>(tree);
		this.frustum = frustum;
		initializeSelectors(physicalAlterator, interestFilter);
	}

	private void initializeSelectors(PhysicalPerceptionAlterator physicalAlterator, InterestFilter interestFilter) {
		Selector selector = new Selector(physicalAlterator, interestFilter);
		this.iterator.setNodeSelector(selector);
		this.iterator.setDataSelector(selector);
	}

	/**
	 * Invoked when a data and its classification according to the frustum
	 * must be saved for future use (eg. replying them to the culler's caller).
	 * 
	 * @param data is the data to store
	 * @param classification is the position of the given data against to the frustum of this culler.
	 */
	void registerClassification(D data, IntersectionType classification) {
		this.classifications.add(classification);
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean hasNext() {
		return this.iterator != null ? this.iterator.hasNext() : false;
	}

	/** {@inheritDoc}
	 */
	@Override
	public CullingResult<D> next() {
		if (this.iterator == null)
			throw new NoSuchElementException();
		D data = this.iterator.next();
		IntersectionType type = this.classifications.remove(0);
		return new CullingResult<D>(this.frustum.getIdentifier(), type, data);
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public void remove() {
		if (this.iterator == null)
			throw new ConcurrentModificationException();
		this.iterator.remove();
	}

	/**
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class Selector implements NodeSelector<N>, DataSelector<D> {

		private final PhysicalPerceptionAlterator physicalAlterator;
		private final InterestFilter interestFilter;

		/**
		 * @param physicalAlterator
		 * @param interestFilter
		 */
		public Selector(PhysicalPerceptionAlterator physicalAlterator, InterestFilter interestFilter) {
			this.physicalAlterator = physicalAlterator;
			this.interestFilter = interestFilter;
		}
		
		/** {@inheritDoc}
		 */
		@Override
		public boolean nodeCouldBeTreatedByIterator(N node) {
			CB bounds = node.getBounds();
			return (bounds!=null && FrustumCuller.this.frustum.intersects(bounds));
		}

		/** {@inheritDoc}
		 */
		@Override
		public boolean dataCouldBeRepliedByIterator(D data) {
			CB bounds = data.getBounds();
			assert(bounds!=null);
			IntersectionType type = FrustumCuller.this.frustum.classifies(bounds);
			if (type==IntersectionType.OUTSIDE) return false;
			
			UUID frustumId = FrustumCuller.this.frustum.getIdentifier();
			
			if (this.physicalAlterator!=null) {
				type = this.physicalAlterator.alters(bounds, data, type, frustumId);
				if (type==IntersectionType.OUTSIDE) return false;
			}

			if (this.interestFilter!=null
				&& !this.interestFilter.filter(bounds, data, type, frustumId))
				return false;
			
			registerClassification(data, type);
			return true;
		}

	}
	
}
