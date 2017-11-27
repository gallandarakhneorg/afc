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

import javax.vecmath.Point2d;

import fr.utbm.set.collection.IntegerList;
import fr.utbm.set.geom.bounds.bounds3d.AlignedBoundingBox;
import fr.utbm.set.geom.bounds.bounds3d.CombinableBounds3D;
import fr.utbm.set.jasim.environment.model.perception.tree.partitions.PartitionPolicy;
import fr.utbm.set.jasim.environment.model.perception.tree.partitions.QuadryPartitionField;
import fr.utbm.set.jasim.environment.model.world.MobileEntity3D;
import fr.utbm.set.tree.node.IcosepQuadTreeNode.IcosepQuadTreeZone;

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
public class DynamicIcosepQuadTreeBuilder3D<DB extends CombinableBounds3D> 
extends AbstractIcosepQuadTreeBuilder<CombinableBounds3D,
							   DB,
							   MobileEntity3D<DB>,
							   AlignedBoundingBox,
							   DynamicIcosepQuadTreeNode3D<DB>,
							   DynamicIcosepQuadTree3D<DB>> {

	/**
	 * @param splittingCount is the maximal count of elements per node, if possible.
	 * @param partitionHeuristic is the heuristic to use to compute the partitions.
	 */
	public DynamicIcosepQuadTreeBuilder3D(int splittingCount, PartitionPolicy partitionHeuristic) {
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
	protected DynamicIcosepQuadTree3D<DB> createEmptyTree(AlignedBoundingBox universeBounds) {
		return new DynamicIcosepQuadTree3D<DB>(new IcosepQuadTreeManipulator3D<DB>(
				getSplittingCount(),
				getPartitionHeuristic(),
				universeBounds));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DynamicIcosepQuadTreeNode3D<DB> createTreeNode(
			int index,
			AlignedBoundingBox box, IntegerList indexes,
			QuadryPartitionField cutPlane) {
		Point2d cutCoord = cutPlane.getCutPlaneIntersection();
		DynamicIcosepQuadTreeNode3D<DB> node = new DynamicIcosepQuadTreeNode3D<DB>(
				this.builtTree,
				cutCoord.x,
				cutCoord.y);
		node.addUserData(extractData(indexes));
		return node;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int getChildNodeCount() {
		return 5;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected AlignedBoundingBox getSubArea(int childIndex,
			AlignedBoundingBox sourceArea, QuadryPartitionField plane,
			IntegerList elementIndexes) {
		IcosepQuadTreeZone zone = IcosepQuadTreeZone.fromInteger(childIndex);
		AlignedBoundingBox box = QuadTreeUtil3D.getSubArea(sourceArea, zone, plane);
		if (box==null) {
			box = computeBounds(elementIndexes);
		}
		return box;
	}
	
}
