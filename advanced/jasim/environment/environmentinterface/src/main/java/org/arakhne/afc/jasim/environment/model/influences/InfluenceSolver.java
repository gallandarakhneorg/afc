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
package org.arakhne.afc.jasim.environment.model.influences;

import org.arakhne.afc.jasim.environment.interfaces.internalevents.EnvironmentalAction;
import org.arakhne.afc.jasim.environment.model.actions.EnvironmentalActionCollector;
import org.arakhne.afc.jasim.environment.model.ground.Ground;
import org.arakhne.afc.jasim.environment.model.world.DynamicEntityManager;
import org.arakhne.afc.jasim.environment.model.world.MobileEntity;
import org.arakhne.afc.jasim.environment.time.TimeManager;

/**
 * This interface describes an influence solver which permits to compute actions
 * according to influences.
 * 
 * @param <EA> is the type of supported actions
 * @param <ME> is the type of supported mobile entities
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * 
 */
public interface InfluenceSolver<EA extends EnvironmentalAction,
								 ME extends MobileEntity<?>>
extends Runnable {

	/**
	 * Set the influence collection that must be used by this solver
	 * to retreive the available influences.
	 * 
	 * @param collector is the new collector.
	 */
	public void setInfluenceCollector(InfluenceCollector collector);

	/**
	 * Set the action dispatcher that must be used by this solver
	 * when actions must be send to the other modules of the
	 * environment.
	 * 
	 * @param collector collects the actions from this solver.
	 */
	public void setActionCollector(EnvironmentalActionCollector<EA> collector);

	/**
	 * Set the manager of the dynamic entities that must be used by this solver
	 * to apply the available population influences.
	 * 
	 * @param entityManager is the entity manager to use.
	 */
	public void setDynamicEntityManager(DynamicEntityManager<ME> entityManager);

	/**
	 * Set the ground object that must be used by this solver
	 * to compute (re)actions.
	 * 
	 * @param ground is the ground.
	 */
	public void setGround(Ground ground);

	/**
	 * Set the time manager used by this solver.
	 * 
	 * @param timeManager is the manager of simulation time.
	 */
	public void setTimeManager(TimeManager timeManager);

}
