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
package org.arakhne.afc.jasim.environment.model.place;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import fr.utbm.set.collection.CollectionUtil;

import org.arakhne.afc.jasim.environment.model.actions.EnvironmentalActionCollector;
import org.arakhne.afc.jasim.environment.model.dynamics.DynamicsEngine;
import org.arakhne.afc.jasim.environment.model.ground.Ground;
import org.arakhne.afc.jasim.environment.model.influences.InfluenceCollector;
import org.arakhne.afc.jasim.environment.model.influences.InfluenceSolver;
import org.arakhne.afc.jasim.environment.model.perceptions.PerceptionGeneratorType;
import org.arakhne.afc.jasim.environment.model.world.EntityContainer;
import org.arakhne.afc.jasim.environment.model.world.MobileEntity;
import org.arakhne.afc.jasim.environment.model.world.WorldEntity;

/**
 * Describes a {@link Place place} in a situated environment during
 * its initialization stage.
 * 
 * @param <SE> is the type of static entity supported by this environment.
 * @param <ME> is the type of mobile entity supported by this environment.
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class DefaultPlaceDescription<SE extends WorldEntity<?>, ME extends MobileEntity<?>> implements PlaceDescription<SE,ME> {

	private final UUID id;
	private String name;
	private Ground groundObject = null;
	private final Map<Class<?>,Object> worldModel = new HashMap<Class<?>,Object>();
	private Collection<? extends SE> staticEntities = null;
	private Class<?> embeddedStaticEntities = null;
	private Collection<? extends ME> mobileEntities = null;
	private Class<?> embeddedMobileEntities = null;
	private Class<? extends InfluenceSolver<?,ME>> influenceSolverType = null;
	private Class<? extends DynamicsEngine> dynamicsEngineType = null;
	private Class<? extends InfluenceCollector> influenceCollectorType = null;
	private Class<? extends EnvironmentalActionCollector<?>> environmentalActionCollectorType = null;
	private PerceptionGeneratorType perceptionGeneratorType = PerceptionGeneratorType.getSystemDefault();

	/**
	 * @param id is the identifier of the place.
	 */
	public DefaultPlaceDescription(UUID id) {
		assert(id!=null);
		this.id = id;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public UUID getIdentifier() {
		return this.id;
	}

	/** {@inheritDoc}
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/** Set the name of the place.
	 * 
	 * @param name is the new name or <code>null</code> to unset.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PerceptionGeneratorType getPerceptionGeneratorType() {
		return this.perceptionGeneratorType;
	}

	/**
	 * Set the prefered type of perception generator for the place.
	 * 
	 * @param type is the prefered type of perception generator.
	 */
	public void setPerceptionGeneratorType(PerceptionGeneratorType type) {
		this.perceptionGeneratorType = type;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Ground getGround() {
		return this.groundObject;
	}

	/**
	 * Set the ground object for the place.
	 * 
	 * @param ground
	 */
	public void setGround(Ground ground) {
		this.groundObject = ground;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Iterator<? extends ME> getMobileEntities() {
		if (this.mobileEntities!=null)
			return this.mobileEntities.iterator();
		
		if (this.embeddedMobileEntities!=null) {
			Object model = getWorldModel(this.embeddedMobileEntities);
			if (model instanceof EntityContainer<?>) {
				return ((EntityContainer<? extends ME>)model).entityIterator();
			}
			else if (model instanceof Iterable<?>) {
				return ((Iterable<? extends ME>)model).iterator();
			}
		}
		
		return CollectionUtil.emptyIterator();
	}

	/** Set the mobile entities to spawn at start up.
	 * 
	 * @param entities
	 */
	public void setMobileEntities(Collection<? extends ME> entities) {
		this.mobileEntities = entities;
		this.embeddedMobileEntities = null;
	}

	/** Set the mobile entities to spawn at start up.
	 * 
	 * @param worldModelType is the type of world model which is able to
	 * replies the mobile entities.
	 */
	public void setMobileEntities(Class<?> worldModelType) {
		this.mobileEntities = null;
		this.embeddedMobileEntities = worldModelType;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Iterator<? extends SE> getStaticEntities() {
		if (this.staticEntities!=null)
			return this.staticEntities.iterator();
		
		if (this.embeddedStaticEntities!=null) {
			Object model = getWorldModel(this.embeddedStaticEntities);
			if (model instanceof EntityContainer<?>) {
				return ((EntityContainer<? extends SE>)model).entityIterator();
			}
			else if (model instanceof Iterable<?>) {
				return ((Iterable<? extends SE>)model).iterator();
			}
		}
		
		return CollectionUtil.emptyIterator();
	}

	/** Set the static entities to spawn at start up.
	 * 
	 * @param entities
	 */
	public void setStaticEntities(Collection<? extends SE> entities) {
		this.staticEntities = entities;
	}

	/** Set the static entities to spawn at start up.
	 * 
	 * @param worldModelType is the type of world model which is able to
	 * replies the static entities.
	 */
	public void setStaticEntities(Class<?> worldModelType) {
		this.staticEntities = null;
		this.embeddedStaticEntities = worldModelType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T getWorldModel(Class<T> type) {
		return type.cast(this.worldModel.get(type));
	}
	
	/**
	 * Set the world model.
	 * 
	 * @param <T>
	 * @param type
	 * @param worldModel
	 */
	public <T> void setWorldModel(Class<T> type, T worldModel) {
		this.worldModel.put(type,worldModel);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Class<? extends InfluenceSolver<?,ME>> getInfluenceSolverType() {
		return this.influenceSolverType;
	}

	/** Set the influence solver type used in this place.
	 * 
	 * @param type is the type of influence solver to use.
	 */
	public void setInfluenceSolverType(Class<? extends InfluenceSolver<?,ME>> type) {
		this.influenceSolverType = type;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Class<? extends DynamicsEngine> getDynamicsEngineType() {
		return this.dynamicsEngineType;
	}

	/** Set the dynamics engine type used in this place.
	 * 
	 * @param type is the type of dynamics engine to use.
	 */
	public void setDynamicsEngineType(Class<? extends DynamicsEngine> type) {
		this.dynamicsEngineType = type;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Class<? extends InfluenceCollector> getInfluenceCollectorType() {
		return this.influenceCollectorType;
	}

	/** Set the influence collector type used in this place.
	 * 
	 * @param type is the type of influence collector to use.
	 */
	public void setInfluenceCollectorType(Class<? extends InfluenceCollector> type) {
		this.influenceCollectorType = type;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Class<? extends EnvironmentalActionCollector<?>> getEnvironmentalActionCollectorType() {
		return this.environmentalActionCollectorType;
	}

	/** Replies the environmental action collector type used in this place.
	 * 
	 * @param type is the type of environmental action collector to use.
	 */
	public void setEnvironmentalActionCollectorType(Class<? extends EnvironmentalActionCollector<?>> type) {
		this.environmentalActionCollectorType = type;
	}
	
	/** Replies a string representation of this description.
	 * 
	 * @return a string representation of this description.
	 */
	@Override
	public String toString() {
		if (this.name==null || "".equals(this.name)) { //$NON-NLS-1$
			return this.id.toString();
		}
		return this.name;
	}

}
