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
import fr.utbm.set.jasim.environment.model.world.Entity3D;
import fr.utbm.set.tree.node.QuadTreeNode.QuadTreeZone;

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
public class StaticQuadTreeBuilder3D<DB extends CombinableBounds3D> 
extends AbstractQuadTreeBuilder<CombinableBounds3D,
							   DB,
							   Entity3D<DB>,
							   AlignedBoundingBox,
							   StaticQuadTreeNode3D<DB>,
							   StaticQuadTree3D<DB>> {

	/**
	 * @param splittingCount is the maximal count of elements per node, if possible.
	 * @param partitionHeuristic is the heuristic to use to compute the partitions.
	 */
	public StaticQuadTreeBuilder3D(int splittingCount, PartitionPolicy partitionHeuristic) {
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
	protected StaticQuadTree3D<DB> createEmptyTree(AlignedBoundingBox universBounds) {
		return new StaticQuadTree3D<DB>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected StaticQuadTreeNode3D<DB> createTreeNode(
			int index,
			AlignedBoundingBox box, IntegerList indexes,
			QuadryPartitionField cutPlane) {
		Point2d cutCoord = cutPlane.getCutPlaneIntersection();
		StaticQuadTreeNode3D<DB> node = new StaticQuadTreeNode3D<DB>(
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
		return 4;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected AlignedBoundingBox getSubArea(int childIndex,
			AlignedBoundingBox sourceArea, QuadryPartitionField plane,
			IntegerList elementIndexes) {
		QuadTreeZone zone = QuadTreeZone.fromInteger(childIndex);
		return QuadTreeUtil3D.getSubArea(sourceArea, zone, plane);
	}
	
}
