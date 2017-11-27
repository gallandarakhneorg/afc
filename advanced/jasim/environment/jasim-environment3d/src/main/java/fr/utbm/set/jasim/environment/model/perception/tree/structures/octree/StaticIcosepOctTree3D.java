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
import fr.utbm.set.jasim.environment.model.world.Entity3D;

/**
 * This is the generic implementation of a quad tree
 * containing bounds.
 * 
 * @param <DB> is the type of the bounds of the data.
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class StaticIcosepOctTree3D<DB extends CombinableBounds3D>
extends StaticIcosepOctTree<CombinableBounds3D,
							DB,
							Entity3D<DB>,
							AlignedBoundingBox,
							StaticIcosepOctTreeNode3D<DB>,
							StaticIcosepOctTree3D<DB>> {

	private static final long serialVersionUID = -7055526174971584198L;

	/** Empty tree.
	 */
	public StaticIcosepOctTree3D() {
		//
	}

}