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

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import fr.utbm.set.math.MeasureUnitUtil;

/** This interface manages the time for a environment.
 * <p>
 * It has the following properties:
 * <ul>
 * <li>the time unit is the one given at the constructor,</li>
 * <li>it is based on the concept of loop: each time environment has finished to run its behaviour,
 * the loop index will be increased,</li>
 * <li>one loop duration is given by the time step duration. By default, the time step is 1.</li>
 * <li>it is not a real time manager.</li>
 * </ul>
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class ConstantTimeManager extends AbstractClock implements TimeManager {
	
	/** Listeners
	 */
	private final Collection<TimeManagerListener> listeners = new LinkedList<TimeManagerListener>();
	
	/** Index of the current loop.
	 */
	private long currentLoop;
	
	/** Time step.
	 */
	private final double timeStep;
	
	/**
	 * @param timeUnit is the time unit used to measure one loop duration.
	 * @param timeStep is the amonut of time between two steps.
	 */
	public ConstantTimeManager(TimeUnit timeUnit, double timeStep) {
		super(timeUnit);
		assert(timeStep>0);
		this.timeStep = timeStep;
		this.currentLoop = 0;
	}

	/**
	 * @param timeUnit is the time unit used to measure one loop duration.
	 */
	public ConstantTimeManager(TimeUnit timeUnit) {
		this(timeUnit,1.);
	}
	
	/**
	 * @param timeStep is the amonut of time between two steps.
	 */
	public ConstantTimeManager(double timeStep) {
		this(TimeUnit.SECONDS,timeStep);
	}

	/**
	 * Constructs a manager with SECONDS as default time unit.
	 */
	public ConstantTimeManager() {
		this(TimeUnit.SECONDS,1.);
	}

	/** {@inheritDoc}
	 */
	@Override
	public TimeUnit getTimeUnit() {
		return getClockTimeUnit();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void addTimeManagerListener(TimeManagerListener listener) {
		this.listeners.add(listener);
	}

	/** {@inheritDoc}
	 */
	@Override
	public void removeTimeManagerListener(TimeManagerListener listener) {
		this.listeners.remove(listener);
	}

	/**
	 * Notify listeners that the time has changed.
	 */
	protected void fireTimeChanged() {
		TimeManagerEvent event = new TimeManagerEvent(this);
		for(TimeManagerListener listener : this.listeners) {
			listener.onSimulationClockChanged(event);
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public Clock getClock() {
		return this;
	}
	
	/** Replies the amount of time between two steps.
	 *
	 * @return the amount of time between two steps.
	 */
	public final double getTimeStep() {
		return this.timeStep;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void onEnvironmentBehaviourStarted() {
		//
	}

	/** {@inheritDoc}
	 */
	@Override
	public void onEnvironmentBehaviourFinished() {
		++this.currentLoop;
		fireTimeChanged();
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public double getSimulationStepDuration(TimeUnit desired_unit) {
		return this.currentLoop<=0 ? 0. : MeasureUnitUtil.convert(this.timeStep, this.defaultTimeUnit, desired_unit);
	}

	/** {@inheritDoc}
	 */
	@Override
	public long getSimulationStepCount() {
		return this.currentLoop;
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getSimulationTime(TimeUnit desired_unit) {
		return MeasureUnitUtil.convert(this.currentLoop*this.timeStep, this.defaultTimeUnit, desired_unit);
	}

}