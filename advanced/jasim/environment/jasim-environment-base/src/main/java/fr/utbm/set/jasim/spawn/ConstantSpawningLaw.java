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

import java.util.Map;
import java.util.concurrent.TimeUnit;

import fr.utbm.set.jasim.environment.time.Clock;

/**
 * Spawn entities in the environment according to a constant amount.
 * <p>
 * The constant amount is the amount of entities for each hour passed inside the simulation.
 * <p>
 * This law support the parameter {@code value}, which is the amount of
 * entities to spawn per hour. 
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class ConstantSpawningLaw extends SpawningLaw {

	private final double amountPerHour;
	private double memorizedAmount;
	
	/**
	 * @param amountPerHour is the amount of entities per hour.
	 */
	public ConstantSpawningLaw(double amountPerHour) {
		this.amountPerHour = amountPerHour;
		this.memorizedAmount = 0.;
	}

	/**
	 * Create a spawning. If the parameter {@code value}
	 * is not inside the map this spawning will use the
	 * default value of {@code 0}.
	 * 
	 * @param parameters is a map of the parameter's values
	 */
	public ConstantSpawningLaw(Map<String,String> parameters) {
		double amount = 0.;
		String val = parameters.get("value"); //$NON-NLS-1$
		if (val!=null) {
			try {
				amount = Double.parseDouble(val);
			}
			catch(Throwable _) {
				//
			}
		}
		this.amountPerHour = amount;
		this.memorizedAmount = 0.;
	}

	/** {@inheritDoc}
	 */
	@Override
	public int getSpawnableAmount(Clock simulationTime) {
		double stepDuration = simulationTime.getSimulationStepDuration(TimeUnit.HOURS);
		this.memorizedAmount += stepDuration * this.amountPerHour;
		int amount = (int)this.memorizedAmount;
		this.memorizedAmount -= amount;
		return amount;
	}

}
