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

import java.util.List;

import fr.utbm.set.geom.bounds.bounds3d.AlignedBoundingBox;
import fr.utbm.set.geom.bounds.bounds3d.CombinableBounds3D;
import fr.utbm.set.jasim.environment.model.world.Entity3D;

/**
 * This is the generic implementation of a
 * tree for which each node has two children.
 * 
 * @param <DB> is the type of the bounds of the data.
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class StaticBspTreeNode3D<DB extends CombinableBounds3D>
extends StaticBspTreeNode<CombinableBounds3D,
						  DB,
						  Entity3D<DB>,
						  AlignedBoundingBox,
						  StaticBspTreeNode3D<DB>,
						  StaticBspTree3D<DB>> {

	private static final long serialVersionUID = -5879953801524414725L;

	/**
	 * @param tree is the tree in which this node is located.
	 * @param cutValue is the coordinate of the line that cut the plane. 
	 * @param cutCoordinateIndex is the index of the <var>cutValue</var> in an euclidian point. 
	 */
	public StaticBspTreeNode3D(StaticBspTree3D<DB> tree, double cutValue, int cutCoordinateIndex) {
		super(tree, cutValue, cutCoordinateIndex);
	}

	/**
	 * @param tree is the tree in which this node is located.
	 * @param cutValue is the coordinate of the line that cut the plane. 
	 * @param cutCoordinateIndex is the index of the <var>cutValue</var> in an euclidian point. 
	 * @param data is the user data associated to this node. The list is not copied. 
	 */
	public StaticBspTreeNode3D(StaticBspTree3D<DB> tree, double cutValue, int cutCoordinateIndex, List<Entity3D<DB>> data) {
		super(tree, cutValue, cutCoordinateIndex, data);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected AlignedBoundingBox createEmptyNodeBounds() {
		return new AlignedBoundingBox();
	}

}