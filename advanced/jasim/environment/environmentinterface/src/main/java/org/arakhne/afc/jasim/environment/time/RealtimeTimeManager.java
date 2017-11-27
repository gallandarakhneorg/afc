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
 * <li>one loop duration is computed according to its processing duration.</li>
 * <li>it is a real time manager.</li>
 * </ul>
 * 
 * FIXME: Reimplement the Realtime time manager because it is completed heratic
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class RealtimeTimeManager extends AbstractClock implements TimeManager {

	/** Listeners
	 */
	private final Collection<TimeManagerListener> listeners = new LinkedList<TimeManagerListener>();
	
	private boolean started = false;
	private long time0;
	private long loopCount;
	private long lastStepDuration;
	private long timeN;

	/**
	 * @param defaultTimeUnit is the time unit used by this manager.
	 */
	public RealtimeTimeManager(TimeUnit defaultTimeUnit) {
		super(defaultTimeUnit);
	}

	/**
	 * Constructs a time manager based on the second time unit.
	 */
	public RealtimeTimeManager() {
		this(TimeUnit.SECONDS);
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

	/** {@inheritDoc}
	 */
	@Override
	public void onEnvironmentBehaviourStarted() {
		if (!this.started) {
			this.loopCount = 0;
			this.time0 = System.nanoTime();
			this.timeN = 0;
			this.lastStepDuration = 0;
			this.started = true;
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public void onEnvironmentBehaviourFinished() {
		this.timeN = System.nanoTime() - this.time0;
		this.loopCount ++;
		this.lastStepDuration = this.timeN / this.loopCount;
		fireTimeChanged();
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getSimulationStepDuration(TimeUnit desired_unit) {
		return MeasureUnitUtil.convert(this.lastStepDuration, TimeUnit.NANOSECONDS, desired_unit);
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
		return MeasureUnitUtil.convert(this.timeN, TimeUnit.NANOSECONDS, desired_unit);
	}

}