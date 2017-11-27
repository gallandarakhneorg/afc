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

import java.util.EventObject;
import java.util.UUID;

import org.arakhne.afc.jasim.environment.model.SituatedEnvironment;
import org.arakhne.afc.jasim.environment.time.Clock;

/**
 * This event describes the stop of a JaSIM simulation.
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class SimulationStopEvent extends EventObject {

	private static final long serialVersionUID = 695463960872644181L;
	
	private final UUID sid;
	private final UUID id;
	private final Clock time;
	
	/**
	 * @param source is the source of the event.
	 * @param sid is the identifier of the simulation run.
	 * @param id is the identifier of the simulation scenario.
	 * @param time is the stopping time.
	 */
	public SimulationStopEvent(SituatedEnvironment<?,?,?,?> source, UUID sid, UUID id, Clock time) {
		super(source);
		this.sid = sid;
		this.id = id;
		this.time = time;
	}
	
	/** Replies the situated environment which has fired this event.
	 * 
	 * @return the situated environment which has caused this event.
	 */
	public SituatedEnvironment<?,?,?,?> getEnvironment() {
		return (SituatedEnvironment<?,?,?,?>)getSource();
	}

	/** Replies the identifier of the simulation scenario.
	 * 
	 * @return the scenario identifier.
	 */
	public UUID getScenarioIdentifier() {
		return this.id;
	}

	/** Replies the identifier of the simulation run.
	 * 
	 * @return the simulation run identifier.
	 */
	public UUID getSimulationRunIdentifier() {
		return this.sid;
	}

	/** Replies the time at which the simulation has stopped.
	 * 
	 * @return the stopping time.
	 */
	public Clock getTime() {
		return this.time;
	}

}
