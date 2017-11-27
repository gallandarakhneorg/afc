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
package fr.utbm.set.jasim.environment.model.influencereaction;

import fr.utbm.set.jasim.environment.interfaces.body.influences.Influence;
import fr.utbm.set.jasim.environment.interfaces.body.influences.KillInfluence;
import fr.utbm.set.jasim.environment.model.world.DynamicEntityManager;
import fr.utbm.set.jasim.environment.model.world.MobileEntity;

/**
 * Several utilities for influence solvers.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class InfluenceSolverUtil {

	/** Replies if the given influence is an
	 * influence which changes the population size.
	 * 
	 * @param influence
	 * @return <code>true</code> if the given influence changes the population
	 * size, otherwise <code>false</code>.
	 */
	public static boolean isPopulationChangeInfluence(Influence influence) {
		return influence instanceof KillInfluence;
	}

	/** Applies the given population-change influence.
	 * 
	 * @param <ME> is the type of the supported mobile entities.
	 * @param entityManager is the manager to use to apply influences.
	 * @param influence is the influence to apply
	 * @return <code>true</code> if the influence was applied, otherwise
	 * <code>false</code>
	 */
	@SuppressWarnings("unchecked")
	public static <ME extends MobileEntity<?>> boolean applyPopulationChangeInfluence(DynamicEntityManager<ME> entityManager, Influence influence) {
		if (influence instanceof KillInfluence) {
			try {
				ME objectToKill = (ME)influence.getInfluencedObject();
				entityManager.unregisterMobileEntity(objectToKill);
				return true;
			}
			catch(ClassCastException _) {
				//
			}
		}
		return false;
	}

}
