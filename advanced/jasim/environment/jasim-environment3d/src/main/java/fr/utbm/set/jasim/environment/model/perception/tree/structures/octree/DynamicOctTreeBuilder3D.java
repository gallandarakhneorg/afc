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
package fr.utbm.set.jasim.environment.model.perception.tree.structures.octree;

import javax.vecmath.Point3d;

import fr.utbm.set.collection.IntegerList;
import fr.utbm.set.geom.bounds.bounds3d.AlignedBoundingBox;
import fr.utbm.set.geom.bounds.bounds3d.CombinableBounds3D;
import fr.utbm.set.jasim.environment.model.perception.tree.partitions.OctyPartitionField;
import fr.utbm.set.jasim.environment.model.perception.tree.partitions.PartitionPolicy;
import fr.utbm.set.jasim.environment.model.world.MobileEntity3D;
import fr.utbm.set.tree.node.OctTreeNode.OctTreeZone;

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
public class DynamicOctTreeBuilder3D<DB extends CombinableBounds3D> 
extends AbstractOctTreeBuilder<CombinableBounds3D,
							   DB,
							   MobileEntity3D<DB>,
							   AlignedBoundingBox,
							   DynamicOctTreeNode3D<DB>,
							   DynamicOctTree3D<DB>> {

	/**
	 * @param splittingCount is the maximal count of elements per node, if possible.
	 * @param partitionHeuristic is the heuristic to use to compute the partitions.
	 */
	public DynamicOctTreeBuilder3D(int splittingCount, PartitionPolicy partitionHeuristic) {
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
	protected DynamicOctTree3D<DB> createEmptyTree(AlignedBoundingBox universeBounds) {
		return new DynamicOctTree3D<DB>(new OctTreeManipulator3D<DB>(
				getSplittingCount(),
				getPartitionHeuristic(),
				universeBounds));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DynamicOctTreeNode3D<DB> createTreeNode(
			int index,
			AlignedBoundingBox box, IntegerList indexes,
			OctyPartitionField cutPlane) {
		Point3d cutCoord = cutPlane.getCutPlaneIntersection();
		DynamicOctTreeNode3D<DB> node = new DynamicOctTreeNode3D<DB>(
				this.builtTree,
				cutCoord.x,
				cutCoord.y,
				cutCoord.z);
		node.addUserData(extractData(indexes));
		return node;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int getChildNodeCount() {
		return 8;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected AlignedBoundingBox getSubArea(int childIndex,
			AlignedBoundingBox sourceArea, OctyPartitionField plane,
			IntegerList elementIndexes) {
		OctTreeZone zone = OctTreeZone.fromInteger(childIndex);
		return OctTreeUtil3D.getSubArea(sourceArea, zone, plane);
	}
	
}
