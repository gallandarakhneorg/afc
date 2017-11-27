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

import java.util.concurrent.TimeUnit;

import fr.utbm.set.jasim.environment.time.AbstractClock;
import fr.utbm.set.math.MeasureUnitUtil;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
final class ClockStub extends AbstractClock {

	/**
	 */
	double currentTime;
	/**
	 */
	double duration;
	
	/**
	 * @param time
	 * @param duration
	 */
	public ClockStub(double time, double duration) {
		super(TimeUnit.SECONDS);
		this.currentTime = time;
		this.duration = duration;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public double getSimulationStepDuration(TimeUnit desired_unit) {
		return MeasureUnitUtil.convert(this.duration, TimeUnit.SECONDS, desired_unit);
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getSimulationTime(TimeUnit desired_unit) {
		return MeasureUnitUtil.convert(this.currentTime, TimeUnit.SECONDS, desired_unit);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getSimulationStepCount() {
		return 0;
	}
	
}
