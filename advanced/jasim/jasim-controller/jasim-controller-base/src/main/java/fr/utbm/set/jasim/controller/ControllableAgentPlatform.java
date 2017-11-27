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
package fr.utbm.set.jasim.controller;

import java.util.Queue;
import java.util.UUID;
import java.util.logging.Logger;

import fr.utbm.set.geom.bounds.Bounds;
import fr.utbm.set.jasim.agent.SituatedAgent;
import fr.utbm.set.jasim.agent.spawn.SituatedAgentSpawner;
import fr.utbm.set.jasim.controller.event.ControllerEvent;
import fr.utbm.set.jasim.environment.interfaces.body.factory.BodyDescription;
import fr.utbm.set.jasim.environment.interfaces.body.factory.FrustumDescription;
import fr.utbm.set.jasim.environment.model.SituatedEnvironment;
import fr.utbm.set.jasim.environment.model.world.MobileEntity;
import fr.utbm.set.jasim.environment.time.TimeManager;

/**
 * This interface describes an object which is able to bind an agent platform
 * to a simulation controller. 
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface ControllableAgentPlatform {

	/** Replies the logger used by the agent platform.
	 * 
	 * @return a logger, never <code>null</code>
	 */
	public Logger getLogger();
	
	/** Stop the binded agent platform.
	 */
	public void killPlatform();
	
	/** Bind and start the agent platform.
	 * 
	 * @param environment is the environment which must be used during simulation.
	 * @param eventQueue is a reference to the event queue which will be used by the controller to store the simulation control events.
	 * @param timeManager is a reference to the time manager used by JaSIM. 
	 */
	public void startPlatform(
			SituatedEnvironment<?,?,?,?> environment,
			Queue<ControllerEvent> eventQueue,
			TimeManager timeManager);

	/** Launch the given agent.
	 * 
	 * @param agent is the agent to launch.
	 * @param activationParameters is the set of parameters to pass to activation function.
	 */
	public void launchAgent(SituatedAgent<?,?,?> agent, Object... activationParameters);
	
	/** Create a spawner without budget.
	 * 
	 * @param <B> is the type of the bounds supported by the simulated environment
	 * @param <ME> is the type of the mobile entities supported by the simulation
	 * @param agent is the type of agent to spawn.
	 * @param body is the type of the body to spawn.
	 * @param frustums are the types of frustums to spawn.
	 * @param environment is the environment in which the spawner is located.
	 * @param placeId is the place identifier.
	 * @return an instance of spawner, never <code>null</code>
	 */
	public <B extends Bounds<?,?,?>, ME extends MobileEntity<B>> SituatedAgentSpawner<B,ME> createSpawner(
			Class<? extends SituatedAgent<?,?,?>> agent, 
			BodyDescription body, 
			Iterable<FrustumDescription> frustums,
			SituatedEnvironment<?,?,ME,?> environment,
			UUID placeId);

	/** Create a spawner without budget.
	 * 
	 * @param <B> is the type of the bounds supported by the simulated environment
	 * @param <ME> is the type of the mobile entities supported by the simulation
	 * @param agent is the type of agent to spawn.
	 * @param body is the type of the body to spawn.
	 * @param frustums are the types of frustums to spawn.
	 * @param environment is the environment in which the spawner is located.
	 * @param placeId is the place identifier.
	 * @param budget is the available budget.
	 * @return an instance of spawner, never <code>null</code>
	 */
	public <B extends Bounds<?,?,?>, ME extends MobileEntity<B>> SituatedAgentSpawner<B,ME> createSpawner(
			Class<? extends SituatedAgent<?,?,?>> agent, 
			BodyDescription body, 
			Iterable<FrustumDescription> frustums,
			SituatedEnvironment<?,?,ME,?> environment,
			UUID placeId,
			long budget);
	
	/** Bind the given spawner with the platform.
	 * 
	 * @param spawner is the spawner to bind.
	 */
	public void bindSpawner(SituatedAgentSpawner<?,? extends MobileEntity<?>> spawner);

	/** Unbind the given spawner with the platform.
	 * 
	 * @param spawner is the spawner to bind.
	 */
	public void unbindSpawner(SituatedAgentSpawner<?,? extends MobileEntity<?>> spawner);

}
