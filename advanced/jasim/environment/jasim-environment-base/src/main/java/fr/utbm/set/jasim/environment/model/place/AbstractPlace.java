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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import fr.utbm.set.jasim.environment.interfaces.internalevents.EnvironmentalAction;
import fr.utbm.set.jasim.environment.model.SituatedEnvironment;
import fr.utbm.set.jasim.environment.model.actions.EnvironmentalActionCollector;
import fr.utbm.set.jasim.environment.model.dynamics.DynamicsEngine;
import fr.utbm.set.jasim.environment.model.ground.Ground;
import fr.utbm.set.jasim.environment.model.influences.InfluenceCollector;
import fr.utbm.set.jasim.environment.model.influences.InfluenceSolver;
import fr.utbm.set.jasim.environment.model.perceptions.PerceptionGenerator;
import fr.utbm.set.jasim.environment.model.world.MobileEntity;
import fr.utbm.set.jasim.environment.model.world.WorldEntity;
import fr.utbm.set.jasim.environment.time.Clock;

/** This class representes the place in the world.
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
public abstract class AbstractPlace<EA extends EnvironmentalAction, SE extends WorldEntity<?>, ME extends MobileEntity<?>>
implements Place<EA,SE,ME> {

	private final WeakReference<SituatedEnvironment<EA,SE,ME,?>> situatedEnvironment;
	private final UUID id;

	private String name = null;
	private DynamicsEngine dynamicsEngine = null;
	private EnvironmentalActionCollector<EA> actionCollector;
	private Ground ground;
	private InfluenceCollector influenceCollector;
	private InfluenceSolver<EA,ME> influenceSolver;
	private PerceptionGenerator perceptionGenerator;
	
	private List<Portal<EA,SE,ME>> portals = new ArrayList<Portal<EA,SE,ME>>();
	
	/**
	 * @param environment is the environment which is containing this place.
	 * @param placeId is the identifier of the place.
	 */
	public AbstractPlace(SituatedEnvironment<EA,SE,ME,?> environment, UUID placeId) {
		this.situatedEnvironment = new WeakReference<SituatedEnvironment<EA,SE,ME,?>>(environment);
		this.id = placeId;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Clock getSimulationClock() {
		return this.situatedEnvironment.get().getSimulationClock();
	}

	/**
	 * Replies the situated environment in which this place is lying.
	 * 
	 * @return the situated enviromnent or <code>null</code>.
	 */
	public SituatedEnvironment<EA,SE,ME,?> getEnvironment() {
		return this.situatedEnvironment.get();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UUID getIdentifier() {
		return this.id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * Set the name of the place.
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
	public DynamicsEngine getDynamicsEngine() {
		return this.dynamicsEngine;
	}
	
	/** Set the dynamics engine of this place.
	 * 
	 * @param engine
	 */
	public void setDynamicsEngine(DynamicsEngine engine) {
		this.dynamicsEngine = engine;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EnvironmentalActionCollector<EA> getEnvironmentalActionCollector() {
		return this.actionCollector;
	}

	/**
	 * Set the action collector used by this place.
	 * 
	 * @param collector
	 */
	public void setEnvironmentalActionCollector(EnvironmentalActionCollector<EA> collector) {
		this.actionCollector = collector;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Ground getGround() {
		return this.ground;
	}

	/**
	 * Set the ground of this place.
	 * 
	 * @param ground
	 */
	public void setGround(Ground ground) {
		this.ground = ground;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InfluenceCollector getInfluenceCollector() {
		return this.influenceCollector;
	}

	/**
	 * Set the influence collector used by this place.
	 * 
	 * @param collector
	 */
	public void setInfluenceCollector(InfluenceCollector collector) {
		this.influenceCollector = collector;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InfluenceSolver<EA,ME> getInfluenceSolver() {
		return this.influenceSolver;
	}

	/**
	 * Set the influence solver used by this place.
	 * 
	 * @param solver 
	 */
	public void setInfluenceSolver(InfluenceSolver<EA,ME> solver) {
		this.influenceSolver = solver;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PerceptionGenerator getPerceptionGenerator() {
		return this.perceptionGenerator;
	}

	/**
	 * Set the perception generator used by this place.
	 * 
	 * @param generator
	 */
	public void setPerceptionGenerator(PerceptionGenerator generator) {
		this.perceptionGenerator = generator;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Portal<EA, SE, ME> getPortalAt(int index) {
		return this.portals.get(index);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getPortalCount() {
		return this.portals.size();
	}
	
	/** Add a portal in this place.
	 * 
	 * @param portal
	 * @param position
	 * @return the success
	 */
	public boolean addPortal(DefaultPortal<EA,SE,ME> portal, PortalPosition position) {
		if (portal.attachPlace(this, position)) {
			if (this.portals.add(portal)) {
				return true;
			}
			portal.detachPlace(this);
		}
		return false;
	}
	
}