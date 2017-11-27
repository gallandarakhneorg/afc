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
package fr.utbm.set.jasim.spawn;

import java.util.concurrent.TimeUnit;

import fr.utbm.set.jasim.environment.time.Clock;
import fr.utbm.set.math.MathException;
import fr.utbm.set.math.stochastic.StochasticLaw;

/**
 * Spawn entities in the environment according to a stochastic law.
 * <p>
 * The stochastic law describes the probability to spawn an amount of entity
 * for each hour passed inside the simulation.
 * The abscise of the law describes the amount of entity to spawn per hour, and 
 * the ordinate of the law describes the probability to spawn the given amount.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class StochasticSpawningLaw extends SpawningLaw {

	/** Stochastic law for generation.
	 */
	private final StochasticLaw generationLaw;
	
	private double memorizedAmount;
	
	/**
	 * @param generationLaw is the stochastic law to use for generating the entities. It describes how many
	 */
	public StochasticSpawningLaw(StochasticLaw generationLaw) {
		assert(generationLaw!=null);
		this.generationLaw = generationLaw;
		this.memorizedAmount = 0.;
	}

	/** {@inheritDoc}
	 */
	@Override
	public int getSpawnableAmount(Clock simulationTime) {
		try {
			double amountPerHour = this.generationLaw.generateRandomValue();
			double stepDuration = simulationTime.getSimulationStepDuration(TimeUnit.HOURS);
			this.memorizedAmount += stepDuration * amountPerHour;
			int amount = (int)this.memorizedAmount;
			this.memorizedAmount -= amount;
			return amount;
		}
		catch (MathException e) {
			// Ignore this exception
		}
		return 0;
	}

}
