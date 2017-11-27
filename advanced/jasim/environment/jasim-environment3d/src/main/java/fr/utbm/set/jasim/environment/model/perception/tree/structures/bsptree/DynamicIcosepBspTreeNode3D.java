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

import java.util.List;

import fr.utbm.set.geom.bounds.bounds3d.AlignedBoundingBox;
import fr.utbm.set.geom.bounds.bounds3d.CombinableBounds3D;
import fr.utbm.set.jasim.environment.model.world.MobileEntity3D;

/**
 * This is the generic implementation of a
 * tree for which each node has four children plus
 * one icosep child.
 * 
 * @param <DB> is the type of the bounds of the data.
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class DynamicIcosepBspTreeNode3D<DB extends CombinableBounds3D>
extends DynamicIcosepBspTreeNode<CombinableBounds3D,
								 DB,
								 MobileEntity3D<DB>,
								 AlignedBoundingBox,
								 DynamicIcosepBspTreeNode3D<DB>,
								 DynamicIcosepBspTree3D<DB>,
								 IcosepBspTreeManipulator3D<DB>> {

	private static final long serialVersionUID = 9164977177633786192L;

	/**
	 * @param tree is the tree in which this node is located.
	 * @param cutValue is the coordinate of the line that cut the plane. 
	 * @param cutCoordinateIndex is the index of the <var>cutValue</var> in an euclidian point. 
	 */
	public DynamicIcosepBspTreeNode3D(DynamicIcosepBspTree3D<DB> tree, double cutValue, int cutCoordinateIndex) {
		super(tree, cutValue, cutCoordinateIndex);
	}

	/**
	 * @param tree is the tree in which this node is located.
	 * @param cutValue is the coordinate of the line that cut the plane. 
	 * @param cutCoordinateIndex is the index of the <var>cutValue</var> in an euclidian point.
	 * @param entities are the entities to insert inside the node. The list is not copied. 
	 */
	public DynamicIcosepBspTreeNode3D(DynamicIcosepBspTree3D<DB> tree, double cutValue, int cutCoordinateIndex, List<MobileEntity3D<DB>> entities) {
		super(tree, cutValue, cutCoordinateIndex, entities);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected AlignedBoundingBox createEmptyNodeBounds() {
		return new AlignedBoundingBox();
	}

}