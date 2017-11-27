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
package fr.utbm.set.jasim.spawn;

import fr.utbm.set.jasim.environment.time.Clock;

/**
 * Spawn entities in the environment.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class SpawningLaw {

	/**
	 */
	public SpawningLaw() {
		//
	}

	/** Replies the count of entities to spawn for the given
	 * simulation time.
	 * 
	 * @param simulationTime is the current simulation time usable to spawn entities.
	 * @return the count of entities to spawn.
	 */
	public abstract int getSpawnableAmount(Clock simulationTime);
	
}
