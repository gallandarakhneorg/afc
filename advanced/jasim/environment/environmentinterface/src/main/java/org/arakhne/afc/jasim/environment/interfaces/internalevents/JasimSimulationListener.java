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
package org.arakhne.afc.jasim.environment.interfaces.internalevents;

import java.util.EventListener;

import org.arakhne.afc.jasim.environment.model.world.MobileEntity;
import org.arakhne.afc.jasim.environment.model.world.WorldEntity;
import org.arakhne.afc.jasim.environment.time.Clock;

/**
 * This interface describes an object able to receive changes
 * from a JaSIM environment.
 *
 * @param <EA> is the type of the environmental actions
 * @param <SE> is the type of the static entities.
 * @param <ME> is the type of the mobile entities.
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface JasimSimulationListener<EA extends EnvironmentalAction, SE extends WorldEntity<?>, ME extends MobileEntity<?>> extends EventListener {
	
	/**
	 * Invoked when the environment should be initialized.
	 * 
	 * @param event
	 */
	public void simulationInitiated(SimulationInitEvent<SE,ME> event);
	
	/**
	 * Invoked when the environment has stop its execution.
	 * 
	 * @param event
	 */
	public void simulationStopped(SimulationStopEvent event);

	/**
	 * Invoked when a mobile entity arrived in the simulation.
	 * 
	 * @param event
	 */
	public void entitiesArrived(EntityLifeEvent<ME> event);

	/**
	 * Invoked when a mobile entity has diseppeared from the simulation.
	 * 
	 * @param event
	 */
	public void entitiesDisappeared(EntityLifeEvent<ME> event);

	/**
	 * Invoked when actions must be applied.
	 * 
	 * @param event
	 */
	public void entityActionsApplied(EntityActionEvent<EA> event);

	/**
	 * Invoked when no action was perform during a simulation step.
	 * 
	 * @param clock is the simulation clock
	 */
	public void simulationIddle(Clock clock);

}
