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

import java.util.Collections;
import java.util.Date;
import java.util.EventObject;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import fr.utbm.set.collection.CollectionUtil;

import org.arakhne.afc.jasim.environment.model.SituatedEnvironment;
import org.arakhne.afc.jasim.environment.model.place.PlaceDescription;
import org.arakhne.afc.jasim.environment.model.world.MobileEntity;
import org.arakhne.afc.jasim.environment.model.world.WorldEntity;

/**
 * This event describes the initialization of a JaSIM simulation.
 *
 * @param <SE> is the type of the static entities.
 * @param <ME> is the type of the mobile entities.
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class SimulationInitEvent<SE extends WorldEntity<?>, ME extends MobileEntity<?>> extends EventObject {

	private static final long serialVersionUID = -4142941629720592914L;
	
	private final UUID sid;
	private final UUID id;
	private final String name;
	private final Date date;
	private final String authors;
	private final String version;
	private final String description;
	private final TimeUnit timeUnit;
	private final int placeCount;
	private final Iterable<PlaceDescription<SE,ME>> places;
	
	/**
	 * @param source is the source of the event.
	 * @param sid is the identifier of the simulation run.
	 * @param id is the identifier of the simulation scenario.
	 * @param name is the name of the simulation scenario.
	 * @param date is the date of the simulation scenario.
	 * @param authors are the authors of the simulation scneario.
	 * @param version is the version of the simulation scenario.
	 * @param description is the description of the simulation scenario.
	 * @param timeUnit is the time unit used by the simulator.
	 * @param placeCount is the count of places.
	 * @param places are the descriptions of the places.
	 */
	public SimulationInitEvent(SituatedEnvironment<?,SE,ME,?> source, UUID sid, UUID id, String name, Date date, String authors, String version, String description, TimeUnit timeUnit, int placeCount, Iterable<PlaceDescription<SE,ME>> places) {
		super(source);
		this.sid = sid;
		this.id = id;
		this.name = name;
		this.date = date;
		this.authors = authors;
		this.version = version;
		this.description = description;
		this.timeUnit = timeUnit;
		this.placeCount = placeCount;
		this.places = places;
	}
	
	/** Replies the situated environment which has fired this event.
	 * 
	 * @return the situated environment which has caused this event.
	 */
	@SuppressWarnings("unchecked")
	public SituatedEnvironment<?,SE,ME,?> getEnvironment() {
		return (SituatedEnvironment<?,SE,ME,?>)getSource();
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

	/** Replies the name of the simulation scenario.
	 * 
	 * @return the scenario name.
	 */
	public String getScenarioName() {
		return this.name;
	}

	/** Replies the authors of the simulation scenario.
	 * 
	 * @return the scenario authors.
	 */
	public String getScenarioAuthors() {
		return this.authors;
	}

	/** Replies the date of the simulation scenario.
	 * 
	 * @return the scenario date.
	 */
	public Date getScenarioDate() {
		return this.date;
	}

	/** Replies the description of the simulation scenario.
	 * 
	 * @return the scenario description.
	 */
	public String getScenarioDescription() {
		return this.description;
	}

	/** Replies the version of the simulation scenario.
	 * 
	 * @return the scenario version.
	 */
	public String getScenarioVersion() {
		return this.version;
	}

	/** Replies the time unit in the simulation.
	 * 
	 * @return the simulation time unit.
	 */
	public TimeUnit getSimulationTimeUnit() {
		return this.timeUnit;
	}

	/** Replies the places in the simulation scenario.
	 * 
	 * @return the places.
	 */
	public Iterable<PlaceDescription<SE,ME>> getPlaces() {
		if (this.places==null) return Collections.emptyList();
		return CollectionUtil.unmodifiableIterable(this.places);
	}

	/** Replies the count of places in the simulation scenario.
	 * 
	 * @return the count of places.
	 */
	public int getPlaceCount() {
		if (this.places==null) return 0;
		return this.placeCount;
	}

}
