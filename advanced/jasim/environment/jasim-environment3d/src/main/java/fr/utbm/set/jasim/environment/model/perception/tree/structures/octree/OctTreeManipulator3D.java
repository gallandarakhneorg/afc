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

 import java.util.List;

import javax.vecmath.Point3d;

import fr.utbm.set.geom.bounds.bounds3d.AlignedBoundingBox;
import fr.utbm.set.geom.bounds.bounds3d.CombinableBounds3D;
import fr.utbm.set.jasim.environment.model.perception.tree.manipulators.AbstractTreeManipulator3D;
import fr.utbm.set.jasim.environment.model.perception.tree.partitions.OctyPartitionField;
import fr.utbm.set.jasim.environment.model.perception.tree.partitions.PartitionPolicy;
import fr.utbm.set.jasim.environment.model.world.MobileEntity3D;
import fr.utbm.set.tree.node.OctTreeNode.OctTreeZone;

/**
 * This is the generic implementation of a binary
 * space/plane partition tree containing bounds.
 * 
 * @param <DB> is the type of the bounds of the data.
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class OctTreeManipulator3D<DB extends CombinableBounds3D>
extends AbstractTreeManipulator3D<DB,
								  AlignedBoundingBox,
								  DynamicOctTreeNode3D<DB>,
								  DynamicOctTree3D<DB>,
								  OctyPartitionField> {

	/**
	 * @param splittingLevel is the count of entities over which a node must
	 * be splitted.
	 * @param partitionPolicy is the partition heuristic.
	 * @param universe are the bounds of the universe.
	 */
	public OctTreeManipulator3D(int splittingLevel, PartitionPolicy partitionPolicy, AlignedBoundingBox universe) {
		super(splittingLevel, 
				NonIcosepOctyPartitionFieldFactory.SINGLETON, 
				partitionPolicy,
				universe);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int classifies(DynamicOctTreeNode3D<DB> node, CombinableBounds3D location) {
		int i = OctTreeUtil.classifiesBox(
				node.getCutLineX(),
				node.getCutLineY(),
				node.getCutLineZ(),
				location.getLower(), location.getUpper());
		return (i<0) ? -1 : i;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected AlignedBoundingBox convertsDataBoundsToNodeBounds(DB dataBounds) {
		return new AlignedBoundingBox(dataBounds);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DynamicOctTreeNode3D<DB> createAndBindChildNode(
			DynamicOctTreeNode3D<DB> parent, int index,
			List<MobileEntity3D<DB>> entities,
			OctyPartitionField cutPlane) {
		Point3d cutCoord = cutPlane.getCutPlaneIntersection();
		DynamicOctTreeNode3D<DB> node = new DynamicOctTreeNode3D<DB>(
				getTree(),
				cutCoord.x,
				cutCoord.y,
				cutCoord.z,
				entities);
		if (parent!=null) parent.setChildAt(index, node);
		return node;
	}

	/** {@inheritDoc}
	 */
	@Override
	protected AlignedBoundingBox refineUniverse(DynamicOctTreeNode3D<DB> node, AlignedBoundingBox universe, int region) {
		return OctTreeUtil3D.getSubArea(
				universe, 
				OctTreeZone.values()[region], 
				node.getCutLineIntersectionPoint());
	}

}