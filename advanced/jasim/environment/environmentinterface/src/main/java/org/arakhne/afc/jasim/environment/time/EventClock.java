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

package org.arakhne.afc.jasim.environment.time;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import fr.utbm.set.math.MeasureUnitUtil;

/** This class permits to implement a simulation clock for events.
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class EventClock extends AbstractClock implements Serializable {

	private static final long serialVersionUID = -694736428832983939L;
	
	private final double time;
	private final double duration;
	private final long loopCount;
	
	/**
	 * @param clockToCopy is the clock to copy.
	 */
	public EventClock(Clock clockToCopy) {
		super(clockToCopy.getClockTimeUnit());
		this.time = clockToCopy.getSimulationTime();
		this.duration = clockToCopy.getSimulationStepDuration();
		this.loopCount = clockToCopy.getSimulationStepCount();
	}
	
	/**
	 */
	public EventClock() {
		super(TimeUnit.SECONDS);
		this.time = Double.NaN;
		this.duration = Double.NaN;
		this.loopCount = Long.MIN_VALUE;
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getSimulationStepDuration(TimeUnit desired_unit) {
		return MeasureUnitUtil.convert(this.duration, this.defaultTimeUnit, desired_unit);
	}

	/** {@inheritDoc}
	 */
	@Override
	public long getSimulationStepCount() {
		return this.loopCount;
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getSimulationTime(TimeUnit desired_unit) {
		return MeasureUnitUtil.convert(this.time, this.defaultTimeUnit, desired_unit);
	}

}