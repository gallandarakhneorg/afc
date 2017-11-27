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

import java.util.List;

import fr.utbm.set.geom.bounds.bounds3d.AlignedBoundingBox;
import fr.utbm.set.geom.bounds.bounds3d.CombinableBounds3D;
import fr.utbm.set.jasim.environment.model.world.MobileEntity3D;

/**
 * This is the generic implementation of a
 * tree for which each node has four children.
 * 
 * @param <DB> is the type of the bounds of the data.
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class DynamicQuadTreeNode3D<DB extends CombinableBounds3D>
extends DynamicQuadTreeNode<CombinableBounds3D,
							DB,
							MobileEntity3D<DB>,
							AlignedBoundingBox,
							DynamicQuadTreeNode3D<DB>,
							DynamicQuadTree3D<DB>,
							QuadTreeManipulator3D<DB>> {

	private static final long serialVersionUID = -5666033641233475891L;

	/**
	 * @param tree is the tree in which this node is located.
	 * @param cutX is the coordinate of the vertical line that cut the plane. 
	 * @param cutY is the coordinate of the horizontal line that cut the plane. 
	 */
	public DynamicQuadTreeNode3D(DynamicQuadTree3D<DB> tree, double cutX, double cutY) {
		super(tree, cutX, cutY);
	}

	/**
	 * @param tree is the tree in which this node is located.
	 * @param cutX is the coordinate of the vertical line that cut the plane. 
	 * @param cutY is the coordinate of the horizontal line that cut the plane.
	 * @param data is the user data associated to this node. The list is not copied.
	 */
	public DynamicQuadTreeNode3D(DynamicQuadTree3D<DB> tree, double cutX, double cutY, List<MobileEntity3D<DB>> data) {
		super(tree, cutX, cutY, data);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected AlignedBoundingBox createEmptyNodeBounds() {
		return new AlignedBoundingBox();
	}

}