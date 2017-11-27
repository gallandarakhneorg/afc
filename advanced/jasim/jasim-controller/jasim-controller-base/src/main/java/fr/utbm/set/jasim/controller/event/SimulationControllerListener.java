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

import java.util.EventListener;

/**
 * This interface describes an event listener on a simulation controller.
 * <p>
 * A simulation controller is an object which permits to launch, stop a simulation.
 * It is also able to dynamically add and remove entities from the simulation.
 * The SimulationController is the entry point and the bottleneck in which
 * all the actions of the simulation's user must pass. 
 *
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface SimulationControllerListener extends EventListener {

	/**
	 * Invoked when the simulation is paused.
	 * 
	 * @param event describes the event.
	 */
	public void simulationPaused(ControllerEvent event);
	
	/**
	 * Invoked when the simulation is started.
	 * 
	 * @param event describes the event.
	 */
	public void simulationStarted(ControllerEvent event);

	/**
	 * Invoked when the simulation has run one step.
	 * 
	 * @param event describes the event.
	 */
	public void simulationStepped(ControllerEvent event);

	/**
	 * Invoked when the simulation is stopped.
	 * 
	 * @param event describes the event.
	 */
	public void simulationStopped(ControllerEvent event);

	/**
	 * Invoked when the simulation execution delay was changed.
	 * 
	 * @param event describes the event.
	 */
	public void simulationExecutionDelayChange(ControllerEvent event);

}
