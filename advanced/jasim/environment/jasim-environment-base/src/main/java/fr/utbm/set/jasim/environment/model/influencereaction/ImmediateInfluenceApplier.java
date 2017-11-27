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
package fr.utbm.set.jasim.environment.model.influencereaction;

import java.lang.ref.WeakReference;

import fr.utbm.set.collection.SizedIterable;
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influencable;
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influence;
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influencer;
import fr.utbm.set.jasim.environment.interfaces.internalevents.EnvironmentalAction;
import fr.utbm.set.jasim.environment.model.actions.EnvironmentalActionCollector;
import fr.utbm.set.jasim.environment.model.ground.Ground;
import fr.utbm.set.jasim.environment.model.influences.InfluenceCollector;
import fr.utbm.set.jasim.environment.model.influences.InfluenceSolver;
import fr.utbm.set.jasim.environment.model.world.DynamicEntityManager;
import fr.utbm.set.jasim.environment.model.world.MobileEntity;
import fr.utbm.set.jasim.environment.time.TimeManager;

/**
 * This solver is filtering the influences into environmental actions
 * without any check according to the other influences.
 * <p>
 * This solver is also an influence collector.
 * When an influence is collected, this solver
 * translate the arriving influence into an environmental action
 * without check for a conflict with the other potential influences.
 * The action is immediately put in the action collector.
 * 
 * @param <EA> is the type of supported actions.
 * @param <ME> is the type of supported entity manager.
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class ImmediateInfluenceApplier<EA extends EnvironmentalAction,
												ME extends MobileEntity<?>>
implements InfluenceSolver<EA,ME>, InfluenceCollector {

	private WeakReference<TimeManager> timeManager = null;
	private WeakReference<EnvironmentalActionCollector<EA>> actionCollector = null;
	private WeakReference<DynamicEntityManager<ME>> entityManager = null;
	private WeakReference<Ground> ground = null;
	
	/**
	 * Create a solver with link to a dispatcher nor a collector.
	 */
	public ImmediateInfluenceApplier() {
		//
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setActionCollector(EnvironmentalActionCollector<EA> collector) {
		this.actionCollector = new WeakReference<EnvironmentalActionCollector<EA>>(collector);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setGround(Ground ground) {
		this.ground = new WeakReference<Ground>(ground);
	}

	/**
	 * Replies the ground.
	 * 
	 * @return the ground
	 */
	public Ground getGround() {
		return this.ground==null ? null : this.ground.get();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTimeManager(TimeManager timeManager) {
		this.timeManager = new WeakReference<TimeManager>(timeManager);
	}

	/**
	 * Replies the current time manager.
	 * 
	 * @return the current time manager.
	 */
	public TimeManager getTimeManager() {
		return this.timeManager==null ? null : this.timeManager.get();
	}

	@Override
	public void setInfluenceCollector(InfluenceCollector collector) {
		// I'm the influence collector
	}

	@Override
	public void setDynamicEntityManager(DynamicEntityManager<ME> entityManager) {
		this.entityManager = new WeakReference<DynamicEntityManager<ME>>(entityManager);
	}

	/**
	 * Replies the manager for the dnymaic entities.
	 * 
	 * @return the dynamic entity manager.
	 */
	public DynamicEntityManager<ME> getDynamicEntityManager() {
		return this.entityManager==null ? null : this.entityManager.get();
	}

	@Override
	public void run() {
		// Nothing to solve
	}

	@Override
	public SizedIterable<? extends Influence> consumeStandardInfluences() {
		return null;
	}

	@Override
	public SizedIterable<? extends Influence> consumePopulationInfluences() {
		return null;
	}

	@Override
	public void registerInfluence(Influencer defaultInfluencer,
			Influencable defaultInfluencedObject, Influence... influences) {
		EnvironmentalActionCollector<EA> collector =
					this.actionCollector==null ? null : this.actionCollector.get();
		DynamicEntityManager<ME> manager =
			this.entityManager==null ? null : this.entityManager.get();
		if (collector!=null && manager!=null) {
			for(Influence inf : influences) {
				if (InfluenceSolverUtil.isPopulationChangeInfluence(inf)) {
					InfluenceSolverUtil.applyPopulationChangeInfluence(manager, inf);
				}
				else {
					EA action = filterStandardInfluence(
							defaultInfluencer,
							defaultInfluencedObject,
							inf);
					if (action!=null) collector.addAction(action);
				}
			}
		}
	}

	/** Invoked to filter a standard influence.
	 * <p>
	 * Don't forget to invoke {@link Influencer#setLastInfluenceStatus(fr.utbm.set.jasim.environment.interfaces.body.influences.InfluenceApplicationStatus)}
	 * in the overridden functions.
	 * 
	 * @param defaultInfluencer is the default influence emitter.
	 * @param defaultInfluencedObject is the default influenced object.
	 * @param influence is the influence to filter.
	 * @return the corresponding action
	 */
	protected abstract EA filterStandardInfluence(
			Influencer defaultInfluencer,
			Influencable defaultInfluencedObject,
			Influence influence);

}
