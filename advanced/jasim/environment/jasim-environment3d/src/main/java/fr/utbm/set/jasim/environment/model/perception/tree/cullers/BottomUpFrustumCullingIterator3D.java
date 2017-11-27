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

import fr.utbm.set.geom.bounds.bounds3d.CombinableBounds3D;
import fr.utbm.set.jasim.environment.model.perception.frustum.Frustum3D;
import fr.utbm.set.jasim.environment.model.perception.tree.DynamicPerceptionTree;
import fr.utbm.set.jasim.environment.model.perception.tree.DynamicPerceptionTreeNode;
import fr.utbm.set.jasim.environment.model.world.MobileEntity3D;

/**
 * This class is an bottom-up iterator on a tree for frustum culling.
 * <p>
 * The iterator starts from the node inside which the original entity is located.
 * Then it traverses the nodes from child to parent node until the bounidng box
 * of the node is completely enclosing the perception bounds. Then the iterator
 * is traversing the nodes from parent to child to determine what are the
 * entities intersecting the perception bounds.
 * 
 * @param <DB> is the type of the bounds supported by the entities.
 * @param <D> is the type of entity supported by the iterator.
 * @param <NB> is the type of the bounds supported by the tree nodes.
 * @param <N> is the type of the tree nodes.
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class BottomUpFrustumCullingIterator3D<DB extends CombinableBounds3D,
											  D extends MobileEntity3D<DB>,
											  NB extends CombinableBounds3D,
											  N extends DynamicPerceptionTreeNode<DB,D,NB,N>>
extends BottomUpFrustumCullingIterator<CombinableBounds3D,DB,D,NB,N> {

	/**
	 * @param tree
	 * @param originalEntity
	 * @param frustum
	 */
	public BottomUpFrustumCullingIterator3D(
			DynamicPerceptionTree<CombinableBounds3D, DB, D, NB, N, ?, ?> tree,
			D originalEntity, Frustum3D frustum) {
		super(tree, originalEntity, frustum);
	}
	
	
}
