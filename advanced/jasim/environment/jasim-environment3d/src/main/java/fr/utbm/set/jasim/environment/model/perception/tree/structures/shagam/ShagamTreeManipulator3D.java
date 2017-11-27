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

import java.util.List;

import javax.vecmath.Point3d;

import fr.utbm.set.geom.bounds.bounds3d.AlignedBoundingBox;
import fr.utbm.set.geom.bounds.bounds3d.CombinableBounds3D;
import fr.utbm.set.jasim.environment.model.perception.tree.manipulators.AbstractTreeManipulator3D;
import fr.utbm.set.jasim.environment.model.perception.tree.partitions.OctyPartitionField;
import fr.utbm.set.jasim.environment.model.perception.tree.partitions.PartitionPolicy;
import fr.utbm.set.jasim.environment.model.world.MobileEntity3D;

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
public class ShagamTreeManipulator3D<DB extends CombinableBounds3D>
extends AbstractTreeManipulator3D<DB,
								  AlignedBoundingBox,
								  DynamicShagamTreeNode3D<DB>,
								  DynamicShagamTree3D<DB>,
								  OctyPartitionField> {

	/**
	 * @param splittingLevel is the count of entities over which a node must
	 * be splitted.
	 * @param partitionPolicy is the partition heuristic.
	 * @param universe are the bounds of the universe.
	 */
	public ShagamTreeManipulator3D(int splittingLevel, PartitionPolicy partitionPolicy, AlignedBoundingBox universe) {
		super(splittingLevel, 
				new ShagamOctyPartitionFieldFactory(), 
				partitionPolicy,
				universe);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int classifies(DynamicShagamTreeNode3D<DB> node, CombinableBounds3D location) {
		int i = ShagamTreeUtil.classifiesBox(
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
	protected DynamicShagamTreeNode3D<DB> createAndBindChildNode(
			DynamicShagamTreeNode3D<DB> parent, int index,
			List<MobileEntity3D<DB>> entities,
			OctyPartitionField cutPlane) {
		Point3d cutCoord = cutPlane.getCutPlaneIntersection();
		DynamicShagamTreeNode3D<DB> node = new DynamicShagamTreeNode3D<DB>(
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
	protected AlignedBoundingBox refineUniverse(DynamicShagamTreeNode3D<DB> node, AlignedBoundingBox universe, int region) {
		return ShagamTreeUtil3D.getSubArea(
				universe, 
				ShagamTreeZone.values()[region], 
				node.getCutLineIntersectionPoint());
	}

}