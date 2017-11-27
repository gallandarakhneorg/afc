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

import fr.utbm.set.geom.bounds.bounds3d.AlignedBoundingBox;
import fr.utbm.set.geom.bounds.bounds3d.CombinableBounds3D;
import fr.utbm.set.jasim.environment.model.world.MobileEntity3D;

/**
 * This is the generic implementation of a oct-tree
 * containing bounds.
 * 
 * @param <DB> is the type of the bounds of the data.
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class DynamicIcosepOctTree3D<DB extends CombinableBounds3D>
extends DynamicIcosepOctTree<CombinableBounds3D,
							 DB,
							 MobileEntity3D<DB>,
							 AlignedBoundingBox,
							 DynamicIcosepOctTreeNode3D<DB>,
							 DynamicIcosepOctTree3D<DB>,
							 IcosepOctTreeManipulator3D<DB>> {

	private static final long serialVersionUID = -8594530040053766966L;

	/** Empty tree.
	 * 
	 * @param manipulator is the manipulator to use.
	 */
	public DynamicIcosepOctTree3D(IcosepOctTreeManipulator3D<DB> manipulator) {
		super(manipulator);
		manipulator.setTree(this);
	}

}