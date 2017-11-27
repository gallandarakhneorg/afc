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
package fr.utbm.set.jasim.environment.model.perception.percepts;

import fr.utbm.set.geom.bounds.bounds3d.CombinableBounds3D;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perception;
import fr.utbm.set.jasim.environment.model.perception.frustum.CullingResult;
import fr.utbm.set.jasim.environment.model.world.Entity3D;
import fr.utbm.set.jasim.environment.model.world.MobileEntity3D;

/**
 * This class describes a set of perceptions and their respective classifications
 * for a specific frustum.
 * 
 * @param <SB> is the bounds of the static entities.
 * @param <MB> is the bounds of the mobile entities.
 * @param <P> is the type of the perceptions from the agent point of view.
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class PerceptionList3D<SB extends CombinableBounds3D,
									   MB extends CombinableBounds3D,
									   P extends Perception>
extends PerceptionList<Entity3D<? extends CombinableBounds3D>,
					   Entity3D<SB>,
					   MobileEntity3D<MB>,
					   CullingResult<? extends Entity3D<? extends CombinableBounds3D>>,
					   P> {

	/**
	 */
	public PerceptionList3D() {
		//
	}
	
}