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
package fr.utbm.set.jasim.environment.model.world;

import fr.utbm.set.geom.bounds.Bounds;
import fr.utbm.set.jasim.environment.model.perception.tree.DynamicPerceptionTreeNode;

/** This interface representes an object in a 1D, 2D or 3D space which could be
 * stored inside a tree.
 *
 * @param <B> is the type of the bounding box associated to this entity.
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface TreeDataMobileEntity<B extends Bounds<?,?,?>> extends MobileEntity<B> {

	/** Replies the tree node which is containing this object.
	 * 
	 * @return the tree node containing this object.
	 */
	public DynamicPerceptionTreeNode<B,?,?,?> getNodeOwner();
	
	/** Set the tree node which is containing this object.
	 * 
	 * @param owner is the tree node reference.
	 */
	public void setNodeOwner(DynamicPerceptionTreeNode<B,?,?,?> owner);

}