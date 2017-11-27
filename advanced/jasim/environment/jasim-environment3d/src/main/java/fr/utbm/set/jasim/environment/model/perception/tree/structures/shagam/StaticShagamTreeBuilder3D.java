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
package fr.utbm.set.jasim.environment.model.perception.tree.structures.shagam;

import javax.vecmath.Point3d;

import fr.utbm.set.collection.IntegerList;
import fr.utbm.set.geom.bounds.bounds3d.AlignedBoundingBox;
import fr.utbm.set.geom.bounds.bounds3d.CombinableBounds3D;
import fr.utbm.set.jasim.environment.model.perception.tree.partitions.OctyPartitionField;
import fr.utbm.set.jasim.environment.model.perception.tree.partitions.PartitionPolicy;
import fr.utbm.set.jasim.environment.model.world.Entity3D;

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
public class StaticShagamTreeBuilder3D<DB extends CombinableBounds3D> 
extends AbstractShagamTreeBuilder<CombinableBounds3D,
							   DB,
							   Entity3D<DB>,
							   AlignedBoundingBox,
							   StaticShagamTreeNode3D<DB>,
							   StaticShagamTree3D<DB>> {

	/**
	 * @param splittingCount is the maximal count of elements per node, if possible.
	 * @param partitionHeuristic is the heuristic to use to compute the partitions.
	 */
	public StaticShagamTreeBuilder3D(int splittingCount, PartitionPolicy partitionHeuristic) {
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
	protected StaticShagamTree3D<DB> createEmptyTree(AlignedBoundingBox universBounds) {
		return new StaticShagamTree3D<DB>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected StaticShagamTreeNode3D<DB> createTreeNode(
			int index,
			AlignedBoundingBox box, IntegerList indexes,
			OctyPartitionField cutPlane) {
		Point3d cutCoord = cutPlane.getCutPlaneIntersection();
		StaticShagamTreeNode3D<DB> node = new StaticShagamTreeNode3D<DB>(
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
		return 9;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected AlignedBoundingBox getSubArea(int childIndex,
			AlignedBoundingBox sourceArea, OctyPartitionField plane,
			IntegerList elementIndexes) {
		ShagamTreeZone zone = ShagamTreeZone.fromInteger(childIndex);
		return ShagamTreeUtil3D.getSubArea(sourceArea, zone, plane);
	}
	
}
