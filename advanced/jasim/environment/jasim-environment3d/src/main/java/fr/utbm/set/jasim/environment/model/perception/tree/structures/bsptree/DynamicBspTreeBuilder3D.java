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

import fr.utbm.set.collection.IntegerList;
import fr.utbm.set.geom.bounds.bounds3d.AlignedBoundingBox;
import fr.utbm.set.geom.bounds.bounds3d.CombinableBounds3D;
import fr.utbm.set.jasim.environment.model.perception.tree.partitions.BinaryPartitionField;
import fr.utbm.set.jasim.environment.model.perception.tree.partitions.PartitionPolicy;
import fr.utbm.set.jasim.environment.model.world.MobileEntity3D;
import fr.utbm.set.tree.node.BinaryTreeNode.BinaryTreeZone;

/**
 * A tree builder is a class that create a tree
 * according to a set of objects and a set
 * of contraints and heuristics.
 * 
 * @param <DB> is the type of the bounds of the data.
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class DynamicBspTreeBuilder3D<DB extends CombinableBounds3D> 
extends AbstractBspTreeBuilder<CombinableBounds3D,
							   DB,
							   MobileEntity3D<DB>,
							   AlignedBoundingBox,
							   DynamicBspTreeNode3D<DB>,
							   DynamicBspTree3D<DB>> {

	/**
	 * @param splittingCount is the maximal count of elements per node, if possible.
	 * @param partitionHeuristic is the heuristic to use to compute the partitions.
	 */
	public DynamicBspTreeBuilder3D(int splittingCount, PartitionPolicy partitionHeuristic) {
		super(splittingCount, partitionHeuristic);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected AlignedBoundingBox createEmptyNodeBounds() {
		return new AlignedBoundingBox();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DynamicBspTree3D<DB> createEmptyTree(AlignedBoundingBox universeBounds) {
		return new DynamicBspTree3D<DB>(new BspTreeManipulator3D<DB>(
				getSplittingCount(),
				getPartitionHeuristic(),
				universeBounds));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DynamicBspTreeNode3D<DB> createTreeNode(
			int index,
			AlignedBoundingBox box, IntegerList indexes,
			BinaryPartitionField cutPlane) {
		assert(cutPlane!=null);
		DynamicBspTreeNode3D<DB> node = new DynamicBspTreeNode3D<DB>(
				this.builtTree,
				cutPlane.getCutCoordinate(),
				cutPlane.getCutCoordinateIndex());
		node.addUserData(extractData(indexes));
		return node;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int getChildNodeCount() {
		return 2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected AlignedBoundingBox getSubArea(int childIndex,
			AlignedBoundingBox sourceArea, BinaryPartitionField plane,
			IntegerList elementIndexes) {
		BinaryTreeZone zone = BinaryTreeZone.fromInteger(childIndex);
		return BspTreeUtil3D.getSubArea(sourceArea, zone, plane);
	}
	
}
