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

import fr.utbm.set.geom.bounds.Bounds;
import fr.utbm.set.geom.intersection.IntersectionType;
import fr.utbm.set.jasim.environment.interfaces.body.frustums.Frustum;
import fr.utbm.set.jasim.environment.model.perception.tree.DynamicPerceptionTree;
import fr.utbm.set.jasim.environment.model.perception.tree.DynamicPerceptionTreeNode;
import fr.utbm.set.jasim.environment.model.world.TreeDataMobileEntity;
import fr.utbm.set.tree.iterator.DataSelectionTreeIterator;
import fr.utbm.set.tree.iterator.DataSelector;
import fr.utbm.set.tree.iterator.NodeSelector;
import fr.utbm.set.tree.iterator.PrefixDataDepthFirstTreeIterator;

/**
 * This class is an bottom-up iterator on a tree for frustum culling.
 * <p>
 * The iterator starts from the node inside which the original entity is located.
 * Then it traverses the nodes from child to parent node until the bounidng box
 * of the node is completely enclosing the perception bounds. Then the iterator
 * is traversing the nodes from parent to child to determine what are the
 * entities intersecting the perception bounds.
 * 
 * @param <CB> is the type of the bounds supported by this iterator (including the
 * entity bounds and the node bounds).
 * @param <DB> is the type of the bounds supported by the entities.
 * @param <D> is the type of entity supported by the iterator.
 * @param <NB> is the type of the bounds supported by the tree nodes.
 * @param <N> is the type of the tree nodes.
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class BottomUpFrustumCullingIterator<CB extends Bounds<?,?,?>,
											DB extends CB,
											D extends TreeDataMobileEntity<DB>,
											NB extends CB,
											N extends DynamicPerceptionTreeNode<DB,D,NB,N>>
implements DataSelectionTreeIterator<D,N> {
	
	private final Frustum<? super CB,?,?> frustum;

	private DataSelectionTreeIterator<D,N> topDownIterator = null;
	
	/**
	 * @param tree is the raw data structure.
	 * @param originalEntity is the perceiver.
	 * @param frustum is the perception frustum.
	 */
	@SuppressWarnings("unchecked")
	public BottomUpFrustumCullingIterator(DynamicPerceptionTree<CB,?,D,NB,N,?,?> tree, D originalEntity, Frustum<? super CB,?,?> frustum) {
		this.frustum = frustum;
		
		N topNode = (N)originalEntity.getNodeOwner();
		
		topNode = searchTopNode(topNode);
		if (topNode==null) topNode = tree.getRoot();
		
		this.topDownIterator = new PrefixDataDepthFirstTreeIterator<D,N>(topNode);
	}
	
	private N searchTopNode(N node) {
		N n = node;
		while (n!=null && this.frustum.classifies(n.getBounds())==IntersectionType.INSIDE) {
			n = n.getParentNode();
		}
		return n;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext() {
		return this.topDownIterator.hasNext();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public D next() {
		return this.topDownIterator.next();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove() {
		//
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDataSelector(DataSelector<D> selector) {
		if (this.topDownIterator!=null) {
			this.topDownIterator.setDataSelector(selector);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setNodeSelector(NodeSelector<N> selector) {
		if (this.topDownIterator!=null) {
			this.topDownIterator.setNodeSelector(selector);
		}
	}
	
}
