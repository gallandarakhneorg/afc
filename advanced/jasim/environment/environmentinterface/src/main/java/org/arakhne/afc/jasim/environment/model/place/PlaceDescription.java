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

import java.util.Iterator;
import java.util.UUID;

import org.arakhne.afc.jasim.environment.model.actions.EnvironmentalActionCollector;
import org.arakhne.afc.jasim.environment.model.dynamics.DynamicsEngine;
import org.arakhne.afc.jasim.environment.model.ground.Ground;
import org.arakhne.afc.jasim.environment.model.influences.InfluenceCollector;
import org.arakhne.afc.jasim.environment.model.influences.InfluenceSolver;
import org.arakhne.afc.jasim.environment.model.perceptions.PerceptionGeneratorType;
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
public interface PlaceDescription<SE extends WorldEntity<?>,ME extends MobileEntity<?>> {

	/** Replies the identifier of the place.
	 * 
	 * @return an identifier
	 */
	public UUID getIdentifier();
	
	/** Replies the name of the place.
	 * 
	 * @return a name or <code>null</code>
	 */
	public String getName();

	/** Replies the static entities.
	 * 
	 * @return the static entities.
	 */
	public Iterator<? extends SE> getStaticEntities();

	/** Replies the mobile entities.
	 * 
	 * @return the mobile entities.
	 */
	public Iterator<? extends ME>  getMobileEntities();

	/** Replies the ground attached to this place.
	 * 
	 * @return the ground.
	 */
	public Ground getGround();

	/** Replies the world model.
	 * 
	 * @param <T> is the type of world model to reply.
	 * @param type is the type of world model to reply.
	 * @return a world model or <code>null</code>
	 */
	public <T> T getWorldModel(Class<T> type);

	/** Replies the influence solver type used in this place.
	 * 
	 * @return a type of influence solver
	 */
	public Class<? extends InfluenceSolver<?,ME>> getInfluenceSolverType();

	/** Replies the dynamics engine type used in this place.
	 * 
	 * @return a type of dynamics engine
	 */
	public Class<? extends DynamicsEngine> getDynamicsEngineType();

	/** Replies the influence collector type used in this place.
	 * 
	 * @return a type of influence collector
	 */
	public Class<? extends InfluenceCollector> getInfluenceCollectorType();

	/** Replies the environmental action collector type used in this place.
	 * 
	 * @return a type of environmental action collector
	 */
	public Class<? extends EnvironmentalActionCollector<?>> getEnvironmentalActionCollectorType();

	/** Replies the prefered type of perception generator to use in the place.
	 * 
	 * @return the prefered type of perception generator.
	 */
	public PerceptionGeneratorType getPerceptionGeneratorType();
	
}
