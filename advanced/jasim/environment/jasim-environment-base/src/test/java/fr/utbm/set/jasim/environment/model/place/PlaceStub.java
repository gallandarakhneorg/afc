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
package fr.utbm.set.jasim.environment.model.place;

import java.util.UUID;

import fr.utbm.set.jasim.environment.interfaces.internalevents.EnvironmentalAction;
import fr.utbm.set.jasim.environment.model.actions.EnvironmentalActionCollector;
import fr.utbm.set.jasim.environment.model.dynamics.DynamicsEngine;
import fr.utbm.set.jasim.environment.model.ground.Ground;
import fr.utbm.set.jasim.environment.model.influences.InfluenceCollector;
import fr.utbm.set.jasim.environment.model.influences.InfluenceSolver;
import fr.utbm.set.jasim.environment.model.perceptions.PerceptionGenerator;
import fr.utbm.set.jasim.environment.model.perceptions.PerceptionGeneratorType;
import fr.utbm.set.jasim.environment.model.world.MobileEntity;
import fr.utbm.set.jasim.environment.model.world.WorldEntity;
import fr.utbm.set.jasim.environment.model.world.WorldModelContainer;
import fr.utbm.set.jasim.environment.model.world.WorldModelActuator;
import fr.utbm.set.jasim.environment.time.Clock;

/** This interface representes the place in the world.
 * A place contains several objects (in the corresponding
 * perception tree), its own dynamics, its own ground, and
 * its own probe manager.
 * Each place could contains a set of portals to be connected to
 * other places.
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class PlaceStub implements Place<EnvironmentalAction, WorldEntity<?>, MobileEntity<?>> {

	private final UUID id = UUID.randomUUID();
	
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
		return null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public int getPortalCount() {
		return 0;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Portal<EnvironmentalAction, WorldEntity<?>, MobileEntity<?>> getPortalAt(int index) {
		return null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public DynamicsEngine getDynamicsEngine() {
		return null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public EnvironmentalActionCollector<EnvironmentalAction> getEnvironmentalActionCollector() {
		return null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Ground getGround() {
		return null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public InfluenceCollector getInfluenceCollector() {
		return null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public InfluenceSolver<EnvironmentalAction,MobileEntity<?>> getInfluenceSolver() {
		return null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public PerceptionGenerator getPerceptionGenerator() {
		return null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public WorldModelContainer<WorldEntity<?>, MobileEntity<?>> getWorldModel() {
		return null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public WorldModelActuator<EnvironmentalAction, MobileEntity<?>> getWorldModelUpdater() {
		return null;
	}

	@Override
	public Clock getSimulationClock() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PerceptionGeneratorType getPerceptionGeneratorType() {
		return PerceptionGeneratorType.LOCAL_SEQUENTIAL_TOPDOWN;
	}

}