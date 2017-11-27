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
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perception1D5;
import fr.utbm.set.jasim.environment.model.perception.frustum.CullingResult;
import fr.utbm.set.jasim.environment.model.world.Entity3D;

/**
 * This class describes a set of perceptions and their respective classifications
 * for a specific frustum.
 * 
 * @param <SB> is the bounds of the static entities.
 * @param <MB> is the bounds of the mobile entities.
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class PerceptionList3D1D5<SB extends CombinableBounds3D, MB extends CombinableBounds3D>
extends PerceptionList3D<SB,MB,Perception1D5> {
	
	/**
	 */
	public PerceptionList3D1D5() {
		//
	}

	@Override
	protected Perception1D5 createPerception(CullingResult<? extends Entity3D<? extends CombinableBounds3D>> result) {
		return null;
	}

	@Override
	public boolean isSupportedPerceptionType(Class<? extends Perception> type) {
		return type.isAssignableFrom(Perception1D5.class);
	}

}
