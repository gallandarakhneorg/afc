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
import org.arakhne.afc.jasim.environment.model.world.MobileEntity;
import org.arakhne.afc.jasim.environment.time.Clock;

/**
 * This event describes the arrival and departures of entities
 * in a JaSIM simulation.
 *
 * @param <ME> is the type of the mobile entities.
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class EntityLifeEvent<ME extends MobileEntity<?>> extends EventObject {

	private static final long serialVersionUID = -2082082265218634015L;
	
	private final UUID sid;
	private final UUID id;
	private final Clock time;
	private final int entityCount;
	private final Iterable<ME> entities;
	
	/**
	 * @param source is the source of the event.
	 * @param sid is the identifier of the simulation run.
	 * @param id is the identifier of the simulation scenario.
	 * @param time is the stopping time.
	 * @param entityCount is the count of entity in the list of entities.
	 * @param entities is list of the entities which are the cause of the event.
	 */
	public EntityLifeEvent(SituatedEnvironment<?,?,ME,?> source, UUID sid, UUID id, Clock time, int entityCount, Iterable<ME> entities) {
		super(source);
		this.sid = sid;
		this.id = id;
		this.time = time;
		this.entityCount = entityCount;
		this.entities = entities;
	}
	
	/** Replies the situated environment which has fired this event.
	 * 
	 * @return the situated environment which has caused this event.
	 */
	@SuppressWarnings("unchecked")
	public SituatedEnvironment<?,?,ME,?> getEnvironment() {
		return (SituatedEnvironment<?,?,ME,?>)getSource();
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

	/** Replies the entities.
	 * 
	 * @return the entities.
	 */
	public Iterable<ME> getEntities() {
		return this.entities;
	}

	/** Replies the count of entities.
	 * 
	 * @return the count of entities.
	 */
	public int getEntityCount() {
		return this.entityCount;
	}

}
