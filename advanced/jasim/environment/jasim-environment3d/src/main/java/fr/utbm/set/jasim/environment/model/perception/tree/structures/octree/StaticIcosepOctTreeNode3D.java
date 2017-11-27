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

import fr.utbm.set.geom.bounds.bounds3d.AlignedBoundingBox;
import fr.utbm.set.geom.bounds.bounds3d.CombinableBounds3D;
import fr.utbm.set.jasim.environment.model.world.Entity3D;

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
public class StaticIcosepOctTreeNode3D<DB extends CombinableBounds3D>
extends StaticIcosepOctTreeNode<CombinableBounds3D,
								DB,
								Entity3D<DB>,
								AlignedBoundingBox,
								StaticIcosepOctTreeNode3D<DB>,
								StaticIcosepOctTree3D<DB>> {

	private static final long serialVersionUID = 5429509097505892131L;

	/**
	 * @param tree is the tree in which this node is located.
	 * @param cutX is the coordinate of the plane's intersection point. 
	 * @param cutY is the coordinate of the plane's intersection point. 
	 * @param cutZ is the coordinate of the plane's intersection point. 
	 */
	public StaticIcosepOctTreeNode3D(StaticIcosepOctTree3D<DB> tree, double cutX, double cutY, double cutZ) {
		super(tree, cutX, cutY, cutZ);
	}

	/**
	 * @param tree is the tree in which this node is located.
	 * @param cutX is the coordinate of the plane's intersection point. 
	 * @param cutY is the coordinate of the plane's intersection point. 
	 * @param cutZ is the coordinate of the plane's intersection point. 
	 * @param data is the user data associated to this node. The list is not copied.
	 */
	public StaticIcosepOctTreeNode3D(StaticIcosepOctTree3D<DB> tree, double cutX, double cutY, double cutZ, List<Entity3D<DB>> data) {
		super(tree, cutX, cutY, cutZ, data);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected AlignedBoundingBox createEmptyNodeBounds() {
		return new AlignedBoundingBox();
	}

}