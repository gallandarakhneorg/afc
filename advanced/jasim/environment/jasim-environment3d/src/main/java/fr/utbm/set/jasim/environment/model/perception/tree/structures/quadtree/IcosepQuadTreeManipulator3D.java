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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.vecmath.Point2d;

import fr.utbm.set.geom.bounds.bounds3d.AlignedBoundingBox;
import fr.utbm.set.geom.bounds.bounds3d.CombinableBounds3D;
import fr.utbm.set.jasim.environment.model.perception.tree.manipulators.AbstractTreeManipulator3D;
import fr.utbm.set.jasim.environment.model.perception.tree.partitions.PartitionPolicy;
import fr.utbm.set.jasim.environment.model.perception.tree.partitions.QuadryPartitionField;
import fr.utbm.set.jasim.environment.model.world.MobileEntity3D;
import fr.utbm.set.tree.node.IcosepQuadTreeNode.IcosepQuadTreeZone;

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
public class IcosepQuadTreeManipulator3D<DB extends CombinableBounds3D>
extends AbstractTreeManipulator3D<DB,
								  AlignedBoundingBox,
								  DynamicIcosepQuadTreeNode3D<DB>,
								  DynamicIcosepQuadTree3D<DB>,
								  QuadryPartitionField> {

	/**
	 * @param splittingLevel is the count of entities over which a node must
	 * be splitted.
	 * @param partitionPolicy is the partition heuristic.
	 * @param universe are the bounds of the universe.
	 */
	public IcosepQuadTreeManipulator3D(int splittingLevel, PartitionPolicy partitionPolicy, AlignedBoundingBox universe) {
		super(splittingLevel, 
				IcosepQuadryPartitionFieldFactory.SINGLETON, 
				partitionPolicy,
				universe);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int classifies(DynamicIcosepQuadTreeNode3D<DB> node, CombinableBounds3D location) {
		int i = QuadTreeUtil.classifiesBoxWithIcosep(
				node.getCutLineX(),
				node.getCutLineY(),
				location.getLower(), location.getUpper());
		return (i<0) ? -1 : i;
	}

	/** {@inheritDoc}
	 */
	@Override
	protected AlignedBoundingBox refineUniverse(DynamicIcosepQuadTreeNode3D<DB> node, AlignedBoundingBox universe, int region) {
		return QuadTreeUtil3D.getSubArea(
				universe, 
				IcosepQuadTreeZone.values()[region], 
				node.getCutLineIntersectionPoint());
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
	protected DynamicIcosepQuadTreeNode3D<DB> createAndBindChildNode(
			DynamicIcosepQuadTreeNode3D<DB> parent, int index,
			List<MobileEntity3D<DB>> entities,
			QuadryPartitionField cutPlane) {
		assert(entities instanceof ArrayList<?> || entities instanceof LinkedList<?>);
		Point2d cutCoord = cutPlane.getCutPlaneIntersection();
		DynamicIcosepQuadTreeNode3D<DB> node = new DynamicIcosepQuadTreeNode3D<DB>(
				getTree(),
				cutCoord.x,
				cutCoord.y,
				entities);
		if (parent!=null) parent.setChildAt(index, node);
		return node;
	}

}