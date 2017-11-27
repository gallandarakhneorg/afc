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
package org.arakhne.afc.jasim.environment.model.place;

import java.util.UUID;

import org.arakhne.afc.jasim.environment.interfaces.internalevents.EnvironmentalAction;
import org.arakhne.afc.jasim.environment.model.actions.EnvironmentalActionCollector;
import org.arakhne.afc.jasim.environment.model.dynamics.DynamicsEngine;
import org.arakhne.afc.jasim.environment.model.ground.Ground;
import org.arakhne.afc.jasim.environment.model.influences.InfluenceCollector;
import org.arakhne.afc.jasim.environment.model.influences.InfluenceSolver;
import org.arakhne.afc.jasim.environment.model.perceptions.PerceptionGenerator;
import org.arakhne.afc.jasim.environment.model.perceptions.PerceptionGeneratorType;
import org.arakhne.afc.jasim.environment.model.world.MobileEntity;
import org.arakhne.afc.jasim.environment.model.world.WorldEntity;
import org.arakhne.afc.jasim.environment.model.world.WorldModelActuator;
import org.arakhne.afc.jasim.environment.model.world.WorldModelContainer;
import org.arakhne.afc.jasim.environment.time.Clock;

/** This interface representes the place in the world.
 * A place contains several objects (in the corresponding
 * perception tree), its own dynamics, its own ground, and
 * its own probe manager.
 * Each place could contains a set of portals to be connected to
 * other places.
 *
 * @param <EA> is the type of the environmental actions supported by this place.
 * @param <SE> is the type of the immobile entities supported by this environment.
 * @param <ME> is the type of the mobile entities supported by this environment.
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Place<EA extends EnvironmentalAction, SE extends WorldEntity<?>, ME extends MobileEntity<?>> {

	/** Replies the clock in the simulation.
	 * 
	 * @return the clock.
	 */
	public Clock getSimulationClock();

	/** Replies the identifier of the place.
	 * 
	 * @return the identifier.
	 */
	public UUID getIdentifier();

	/** Replies the name of the place.
	 * 
	 * @return the name or <code>null</code>.
	 */
	public String getName();

	/** Replies the count of portals in the place.
	 * 
	 * @return the count of portals.
	 */
	public int getPortalCount();

	/** Replies the portal at the given index.
	 * 
	 * @param index is the index of the portal.
	 * @return the portal at the given index.
	 */
	public Portal<EA,SE,ME> getPortalAt(int index);

	//-------------------------------------------
	// Components of the place
	//-------------------------------------------
	
	/** Replies the endogeneous dynamics engine used by this place.
	 * 
	 * @return the endogeneous dynamics engine.
	 */
	public DynamicsEngine getDynamicsEngine();

	/** Replies the collector of environmental actions used by this place.
	 * 
	 * @return the environmental action collector
	 */
	public EnvironmentalActionCollector<EA> getEnvironmentalActionCollector();

	/** Replies the ground model.
	 * 
	 * @return the ground model.
	 */
	public Ground getGround();

	/** Replies the collector of influences.
	 * 
	 * @return the influence collector.
	 */
	public InfluenceCollector getInfluenceCollector();

	/** Replies the solver of influence conflicts.
	 * 
	 * @return the influence solver.
	 */
	public InfluenceSolver<EA,ME> getInfluenceSolver();

	/** Replies the generator of entity perceptions.
	 * 
	 * @return the perception generator.
	 */
	public PerceptionGenerator getPerceptionGenerator();

	/** Replies the type of the generator of entity perceptions.
	 * 
	 * @return the type of the perception generator.
	 */
	public PerceptionGeneratorType getPerceptionGeneratorType();

	/** Replies the data-structure which is containing all the
	 * world entities.
	 * 
	 * @return the world model container.
	 */
	public WorldModelContainer<SE,ME> getWorldModel();

	/** Replies the updater able to modify the world model.
	 * 
	 * @return the world model updater.
	 */
	public WorldModelActuator<EA,ME> getWorldModelUpdater();

}