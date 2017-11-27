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
 * This event describes the applied environmental actions in a JaSIM simulation.
 *
 * @param <EA> is the type of the environmental actions.
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class EntityActionEvent<EA extends EnvironmentalAction> extends EventObject {

	private static final long serialVersionUID = -8602610179485919349L;
	
	private final UUID sid;
	private final UUID id;
	private final Clock time;
	private final int actionCount;
	private final Iterable<EA> actions;
	
	/**
	 * @param source is the source of the event.
	 * @param sid is the identifier of the simulation run.
	 * @param id is the identifier of the simulation scenario.
	 * @param time is the stopping time.
	 * @param actionCount is the count of applied actions.
	 * @param actions are the applied actions.
	 */
	public EntityActionEvent(SituatedEnvironment<EA,?,?,?> source, UUID sid, UUID id, Clock time, int actionCount, Iterable<EA> actions) {
		super(source);
		this.sid = sid;
		this.id = id;
		this.time = time;
		this.actionCount = actionCount;
		this.actions = actions;
	}
	
	/** Replies the situated environment which has fired this event.
	 * 
	 * @return the situated environment which has caused this event.
	 */
	@SuppressWarnings("unchecked")
	public SituatedEnvironment<EA,?,?,?> getEnvironment() {
		return (SituatedEnvironment<EA,?,?,?>)getSource();
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

	/** Replies the applied actions.
	 * 
	 * @return the applied actions.
	 */
	public Iterable<EA> getAppliedActions() {
		return this.actions;
	}

	/** Replies the count of applied actions.
	 * 
	 * @return the count of applied actions.
	 */
	public int getAppliedActionCount() {
		return this.actionCount;
	}

}
