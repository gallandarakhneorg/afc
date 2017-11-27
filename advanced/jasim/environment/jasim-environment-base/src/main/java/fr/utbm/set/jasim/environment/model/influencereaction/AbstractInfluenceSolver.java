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
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influence;
import fr.utbm.set.jasim.environment.interfaces.internalevents.EnvironmentalAction;
import fr.utbm.set.jasim.environment.model.actions.EnvironmentalActionCollector;
import fr.utbm.set.jasim.environment.model.ground.Ground;
import fr.utbm.set.jasim.environment.model.influences.InfluenceCollector;
import fr.utbm.set.jasim.environment.model.influences.InfluenceSolver;
import fr.utbm.set.jasim.environment.model.world.DynamicEntityManager;
import fr.utbm.set.jasim.environment.model.world.MobileEntity;
import fr.utbm.set.jasim.environment.time.Clock;
import fr.utbm.set.jasim.environment.time.EventClock;
import fr.utbm.set.jasim.environment.time.TimeManager;

/**
 * This solver provides a common implementation to all solver classes.
 * 
 * @param <EA> is the type of supported actions
 * @param <ME> is the type of supported mobile entities
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractInfluenceSolver<EA extends EnvironmentalAction,
											  ME extends MobileEntity<?>>
implements InfluenceSolver<EA,ME> {

	private transient WeakReference<EnvironmentalActionCollector<EA>> actionCollector;
	private transient WeakReference<DynamicEntityManager<ME>> entityManager;
	private transient WeakReference<InfluenceCollector> influenceCollector;
	private transient WeakReference<Ground> ground;
	private transient WeakReference<TimeManager> timeManager;
	
	/**
	 * Create a solver with link to a dispatcher nor a collector.
	 */
	public AbstractInfluenceSolver() {
		//
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public void setActionCollector(EnvironmentalActionCollector<EA> collector) {
		this.actionCollector = new WeakReference<EnvironmentalActionCollector<EA>>(collector);
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setInfluenceCollector(InfluenceCollector collector) {
		this.influenceCollector = new WeakReference<InfluenceCollector>(collector);
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setDynamicEntityManager(DynamicEntityManager<ME> entityManager) {
		this.entityManager = new WeakReference<DynamicEntityManager<ME>>(entityManager);
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setGround(Ground ground) {
		this.ground = new WeakReference<Ground>(ground);
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setTimeManager(TimeManager timeManager) {
		this.timeManager = new WeakReference<TimeManager>(timeManager);
	}

	/** {@inheritDoc}
	 */
	@Override
	public final void run() {
		InfluenceCollector collector = (this.influenceCollector==null) ? null : this.influenceCollector.get();
		if (collector==null) return;
		
		SizedIterable<? extends Influence> standardInfluences = collector.consumeStandardInfluences();
		SizedIterable<? extends Influence> populationInfluences = collector.consumeStandardInfluences();
		
		TimeManager timeManagerInstance = (this.timeManager==null) ? null : this.timeManager.get();
		Clock clock = timeManagerInstance==null ? new EventClock() : new EventClock(timeManagerInstance.getClock());

		if (standardInfluences!=null && standardInfluences.size()>0) {
			EnvironmentalActionCollector<EA> dispatcher = (this.actionCollector==null) ? null : this.actionCollector.get();
			if (dispatcher!=null) {
				solveStandardInfluences(
						(this.ground==null) ? null : this.ground.get(),
						standardInfluences,
						dispatcher,
						clock);
			}
		}

		if (populationInfluences!=null && populationInfluences.size()>0) {
			DynamicEntityManager<ME> manager = (this.entityManager==null) ? null : this.entityManager.get();
			if (manager!=null) {
				solvePopulationInfluences(
						(this.ground==null) ? null : this.ground.get(),
						populationInfluences,
						manager,
						clock);
			}
		}
	}

	/** Applies the population changes.
	 * 
	 * @param groundInstance is the ground to use to solve the influences (if none, this value is <code>null</code>).
	 * @param influences are the collected influences to treat.
	 * @param entityManagerInstance is object that update the population.
	 * @param simulationTimeManager is the time manager used by this solver. It could be <code>null</code>.
	 */
	protected void solvePopulationInfluences(Ground groundInstance, Iterable<? extends Influence> influences, DynamicEntityManager<ME> entityManagerInstance, Clock simulationTimeManager) {
		for(Influence inf : influences) {
			InfluenceSolverUtil.applyPopulationChangeInfluence(entityManagerInstance, inf);
		}
	}

	/** Solve the conflicts and compute actions.
	 * 
	 * @param groundInstance is the ground to use to solve the influences (if none, this value is <code>null</code>).
	 * @param influences are the collected influences to treat.
	 * @param actionCollectorInstance is object that collect actions.
	 * @param simulationTimeManager is the time manager used by this solver. It could be <code>null</code>.
	 */
	protected abstract void solveStandardInfluences(Ground groundInstance, Iterable<? extends Influence> influences, EnvironmentalActionCollector<EA> actionCollectorInstance, Clock simulationTimeManager);

}
