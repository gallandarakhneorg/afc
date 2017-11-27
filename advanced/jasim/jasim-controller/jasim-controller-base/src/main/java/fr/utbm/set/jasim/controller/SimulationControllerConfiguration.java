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

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

import fr.utbm.set.jasim.agent.SituatedAgent;
import fr.utbm.set.jasim.io.sfg.EnvironmentSimulationParameterSet;
import fr.utbm.set.jasim.io.sfg.SimulationParameterSet;
import fr.utbm.set.jasim.io.sfg.SpawnerSimulationParameterSet;
import fr.utbm.set.jasim.environment.model.SituatedEnvironmentDescription;
import fr.utbm.set.jasim.environment.model.place.PlaceDescription;
import fr.utbm.set.jasim.environment.model.world.MobileEntity;
import fr.utbm.set.jasim.environment.model.world.WorldEntity;
import fr.utbm.set.jasim.environment.semantics.Semantic;

/**
 * A configuration description for the simulation controller. 
 *
 * @param <SE> is the type of the static entities supported by the simulation
 * @param <ME> is the type of the mobile entities supported by the simulation
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public final class SimulationControllerConfiguration<SE extends WorldEntity<?>, ME extends MobileEntity<?>> {

	private final SituatedEnvironmentDescription<SE,ME> situatedEnvironment;
	private final SimulationParameterSet simulation;

	/**
	 * @param environment
	 * @param simulation
	 */
	SimulationControllerConfiguration(SituatedEnvironmentDescription<SE,ME> environment, SimulationParameterSet simulation) {
		this.situatedEnvironment = environment;
		this.simulation = simulation;
	}

	/**
	 * @param environment describes the situated environment.
	 */
	public SimulationControllerConfiguration(SituatedEnvironmentDescription<SE,ME> environment) {
		assert(environment!=null);
		this.situatedEnvironment = environment;
		this.simulation = null;
	}

	/** Replies the description of the situated environment.
	 * 
	 * @return the situated environment description.
	 */
	public SituatedEnvironmentDescription<SE,ME> getSituatedEnvironmentDescription() {
		return this.situatedEnvironment;
	}

	/** Replies the identifier of the simulation scenario.
	 * 
	 * @return the scenario id
	 */
	public UUID getIdentifier() {
		return this.situatedEnvironment.getIdentifier();
	}

	/** Replies the date of creation of the scenario.
	 * 
	 * @return the scenario date
	 */
	public Date getDate() {
		return this.situatedEnvironment.getDate();
	}

	/** Replies the authors of the scenario.
	 * 
	 * @return the scenario authors
	 */
	public String getAuthors() {
		return this.situatedEnvironment.getAuthors();
	}

	/** Replies the version of the scenario.
	 * 
	 * @return the scenario version
	 */
	public String getVersion() {
		return this.situatedEnvironment.getVersion();
	}

	/** Replies the description of the scenario.
	 * 
	 * @return the scenario description
	 */
	public String getDescription() {
		return this.situatedEnvironment.getDescription();
	}
	
	/** Replies the spawner identifiers.
	 * 
	 * @return  the spawner identifiers.
	 */
	public Iterable<UUID> getSpawnerIdentifiers() {
		if (this.simulation==null) return Collections.emptyList();
		return Collections.unmodifiableCollection(this.simulation.getSpawnerIdentifiers());
	}
	
	/** Replies the collection of the spawner's parameters.
	 * 
	 * @return the collection of the spawner's parameters.
	 */
	public Iterable<SpawnerSimulationParameterSet> getSpawners() {
		if (this.simulation==null) return Collections.emptyList();
		return new Iterable<SpawnerSimulationParameterSet>() {
			@SuppressWarnings("synthetic-access")
			@Override
			public Iterator<SpawnerSimulationParameterSet> iterator() {
				return new SpawnerIterator(SimulationControllerConfiguration.this.simulation.getSpawnerIdentifiers().iterator());
			}
			
		};
	}

	/** Replies the default type of the agents which will be
	 * instanced at startup from the initial world model.
	 * 
	 * @param placeId is the place identifier.
	 * @return the default agent type.
	 */
	public Class<? extends SituatedAgent<?,?,?>> getDefaultAgentType(UUID placeId) {
		if (this.simulation==null) return null;
		EnvironmentSimulationParameterSet params = this.simulation.getEnvironmentParameters(placeId);
		return params.getDefaultDynamicEnvironmentAgentMapping();
	}
	
	/** Replies the type of agents associated to semantic or body types.
	 * 
	 * @param placeId is the place identifier.
	 * @return the agent type mapping.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<Class<?>, Class<? extends SituatedAgent<?,?,?>>> getBodyAgentMap(UUID placeId) {
		if (this.simulation==null) return null;
		EnvironmentSimulationParameterSet params = this.simulation.getEnvironmentParameters(placeId);
		Map m = params.getDynamicEnvironmentAgentMapping(); 
		return m;
	}
	
	/** Replies the collection of semantic types associated to a type of agent..
	 * 
	 * @param placeId is the place identifier.
	 * @return the semantic list.
	 */
	public Map<Class<? extends SituatedAgent<?,?,?>>, Collection<Class<? extends Semantic>>> getAgentSemanticMap(UUID placeId) {
		if (this.simulation==null) return null;
		EnvironmentSimulationParameterSet params = this.simulation.getEnvironmentParameters(placeId);
		return params.getDynamicEnvironmentAgentSemantics();
	}

	/** Replies the collection of the place's parameters.
	 * 
	 * @return the collection of the place's  parameters.
	 */
	public Iterable<PlaceDescription<SE,ME>> getPlaces() {
		return this.situatedEnvironment.getPlaces();
	}

	/**
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class SpawnerIterator implements Iterator<SpawnerSimulationParameterSet> {

		private final Iterator<UUID> idIterator;
		SpawnerSimulationParameterSet next;
		
		public SpawnerIterator(Iterator<UUID> iter) {
			this.idIterator = iter;
			searchNext();
		}
		
		@SuppressWarnings("synthetic-access")
		private void searchNext() {
			SpawnerSimulationParameterSet n = null;
			while (n==null && this.idIterator.hasNext()) {
				UUID id = this.idIterator.next();
				n = SimulationControllerConfiguration.this.simulation.getSpawnerParameters(id);
			}
			this.next = n;
		}

		@Override
		public boolean hasNext() {
			return this.next!=null;
		}

		@Override
		public SpawnerSimulationParameterSet next() {
			SpawnerSimulationParameterSet n = this.next;
			if (n==null) throw new NoSuchElementException();
			searchNext();
			return n;
		}

		@Override
		public void remove() {
			//
		}
		
	}

}
