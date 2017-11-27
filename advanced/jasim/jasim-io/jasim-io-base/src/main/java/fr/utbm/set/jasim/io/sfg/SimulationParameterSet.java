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
package fr.utbm.set.jasim.io.sfg;


import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Abstract class for configuration parameter entries.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class SimulationParameterSet extends AbstractParameter {

	private final UUID simulationId;
	private String simulationName;
	private Date simulationDate;
	private String simulationAuthors;
	private String simulationVersion;
	private String simulationDescription;
	private TimeSimulationParameterSet timeManager;
	private final Map<UUID,EnvironmentSimulationParameterSet> environment = new HashMap<UUID,EnvironmentSimulationParameterSet>();
	private final Map<UUID, SpawnerSimulationParameterSet> spawners = new HashMap<UUID, SpawnerSimulationParameterSet>();
	
	/**
	 * @param simulationIdentifier is the unique identifier of the simulation.
	 */
	SimulationParameterSet(UUID simulationIdentifier) {
		this.simulationId = simulationIdentifier;
	}
	
	/** Replies the simulation identifier (aka. the simulation name).
	 * 
	 * @return the identifier, never <code>null</code>
	 */
	public UUID getSimulationId() {
		return this.simulationId;
	}
	
	/** Replies the simulation name.
	 * 
	 * @return the name or <code>null</code> if never set.
	 */
	public String getSimulationName() {
		return this.simulationName;
	}

	/** Set the simulation name.
	 * 
	 * @param name is the name or <code>null</code> to unset it.
	 */
	public void setSimulationName(String name) {
		this.simulationName = name;
	}

	/** Replies the simulation date.
	 * 
	 * @return the date or <code>null</code> if never set.
	 */
	public Date getSimulationDate() {
		return this.simulationDate;
	}

	/** Set the simulation date.
	 * 
	 * @param date is the date or <code>null</code> to unset it.
	 */
	public void setSimulationDate(Date date) {
		this.simulationDate = date;
	}

	/** Replies the authors of this simulation.
	 * 
	 * @return the authors or <code>null</code> if never set.
	 */
	public String getSimulationAuthors() {
		return this.simulationAuthors;
	}

	/** Set the authors of this simulation.
	 * 
	 * @param authors are the authors or <code>null</code> to unset them.
	 */
	public void setSimulationAuthors(String authors) {
		this.simulationAuthors = authors;
	}

	/** Replies the version number of this simulation.
	 * 
	 * @return the version number or <code>null</code> if never set.
	 */
	public String getSimulationVersion() {
		return this.simulationVersion;
	}

	/** Set the version number of this simulation.
	 * 
	 * @param number is the version number or <code>null</code> to unset it.
	 */
	public void setSimulationVersion(String number) {
		this.simulationVersion = number;
	}

	/** Replies the description of this simulation.
	 * 
	 * @return the description or <code>null</code> if never set.
	 */
	public String getSimulationDescription() {
		return this.simulationDescription;
	}

	/** Set the description of this simulation.
	 * 
	 * @param description is the description or <code>null</code> to unset it.
	 */
	public void setSimulationDescription(String description) {
		this.simulationDescription = description;
	}
	
	/** Replies the parameters for the place environment.
	 *
	 * @param placeName is the name of the place.
	 * @return the parameter set, never <code>null</code>.
	 */
	public EnvironmentSimulationParameterSet getEnvironmentParameters(UUID placeName) {
		return getEnvironmentParameters(placeName, true);
	}

	/** Replies the parameters for the place environment.
	 *
	 * @return the parameter set, never <code>null</code>.
	 */
	public Iterable<EnvironmentSimulationParameterSet> getEnvironmentParameters() {
		return this.environment.values();
	}

	/** Replies the count of parameters for the place environment.
	 *
	 * @return the count of parameters.
	 */
	public int getEnvironmentParameterCount() {
		return this.environment.size();
	}

	/** Replies the parameters for the place environment.
	 *
	 * @param placeName is the name of the place.
	 * @param create indicates if a set must be created if it does not exists.
	 * @return the parameter set or <code>null</code>.
	 */
	public EnvironmentSimulationParameterSet getEnvironmentParameters(UUID placeName, boolean create) {
		EnvironmentSimulationParameterSet env = this.environment.get(placeName);
		if (env==null) {
			if (!create) return null;
			env = new EnvironmentSimulationParameterSet(getEnvironmentDimension(), placeName);
			this.environment.put(placeName, env);
		}
		return env;
	}

	/** Set the parameters for the time manager.
	 *
	 * @param timeManager is the parameter set.
	 */
	void setTimeManager(TimeSimulationParameterSet timeManager) {
		this.timeManager = timeManager;
	}

	/** Replies the parameters for the time manager.
	 *
	 * @return the parameter set.
	 */
	public TimeSimulationParameterSet getTimeManagerParameters() {
		if (this.timeManager==null)
			this.timeManager = new TimeSimulationParameterSet(getEnvironmentDimension());
		return this.timeManager;
	}

	/** Replies the parameters for the spawner with the given name.
	 *
	 * @param id is the id of the spawner.
	 * @return the parameter set.
	 */
	public SpawnerSimulationParameterSet getSpawnerParameters(UUID id) {
		SpawnerSimulationParameterSet theset = this.spawners.get(id);
		if (theset==null) {
			theset = new SpawnerSimulationParameterSet(getEnvironmentDimension(), id);
			this.spawners.put(id,theset);
		}
		return theset;
	}

	/** Replies the list of spawner's identifiers.
	 *
	 * @return the spawner's identifiers.
	 */
	public Collection<UUID> getSpawnerIdentifiers() {
		return Collections.unmodifiableCollection(this.spawners.keySet());
	}

}
