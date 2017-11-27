/*
 * 
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
package fr.utbm.set.jasim.controller.event;

/**
 * Enum describing the various possible types of a SimulationTimeControlEvent.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public enum SimulationTimeControlEventType {
	/**
	 * Start Simulation
	 */
	START,
	/**
	 * Execute one step in the simulation 
	 */
	ONESTEP,
	/**
	 * Pause the execution of the simulation
	 */
	PAUSE,
	/**
	 * Stop the simulation
	 */
	STOP,
	/**
	 * Simulation execution delay change
	 */
	EXECUTION_DELAY_CHANGE;
}
