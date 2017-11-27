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

import java.util.EventObject;

import fr.utbm.set.jasim.controller.SimulationController;
import fr.utbm.set.jasim.environment.time.Clock;
import fr.utbm.set.jasim.environment.time.EventClock;

/**
 * This class describes an event in a simulation controller.
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
public class ControllerEvent extends EventObject {

	private static final long serialVersionUID = 224719775320363108L;

	/**
	 * The type of this SimulationTimeControlEvent
	 * @see SimulationTimeControlEventType
	 */
	private final SimulationTimeControlEventType controlEventType;
	
	/** Simulation time.
	 */
	private final Clock simulationTime;
	
	/** User data
	 */
	private final long userData;
	
	
	/**
	 * @param source is the source of the event.
	 * @param type is the type of the event.
	 * @param simulationTime is the time at which the event occured
	 */
	public ControllerEvent(SimulationController<?,?,?> source, SimulationTimeControlEventType type, Clock simulationTime) {
		this(source, type, simulationTime, 0);
	}
	
	/**
	 * @param source is the source of the event.
	 * @param type is the type of the event.
	 * @param simulationTime is the time at which the event occured
	 * @param userData is a user data associated to this event.
	 */
	public ControllerEvent(SimulationController<?,?,?> source, SimulationTimeControlEventType type, Clock simulationTime, long userData) {
		super(source);
		this.controlEventType = type;
		this.simulationTime = new EventClock(simulationTime);
		this.userData = userData;
	}

	/**
	 * Returns the type of this SimulationTimeControlEvent
	 * @return The type of this SimulationTimeControlEvent
	 */
	public SimulationTimeControlEventType getControlEventType() {
		return this.controlEventType;
	}
	
	/** Replies the simulation time at which this event occured.
	 *
	 * @return the clock.
	 */
	public Clock getSimulationTime() {
		return this.simulationTime;
	}

	/** Replies the user data associated to this event.
	 *
	 * @return the user data.
	 */
	public long getUserData() {
		return this.userData;
	}

}
