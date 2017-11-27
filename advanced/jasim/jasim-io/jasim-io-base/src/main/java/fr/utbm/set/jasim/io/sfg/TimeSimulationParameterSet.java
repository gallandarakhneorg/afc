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
package fr.utbm.set.jasim.io.sfg;

import java.util.concurrent.TimeUnit;

/**
 * Set of parameters to initialize a time manager.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class TimeSimulationParameterSet extends AbstractParameter {

	private final TimeParameterType type;
	private final TimeUnit unit;
	private final double timeStep;
	
	/**
	 * @param dimension must be one of <code>1</code>, <code>1.5</code>, <code>2</code>, <code>2.5</code>, 
	 * or <code>3</code>.
	 * @param type
	 * @param unit
	 * @param timeStep
	 */
	TimeSimulationParameterSet(float dimension, TimeParameterType type, TimeUnit unit, double timeStep) {
		super(dimension);
		this.type = type;
		this.unit = unit;
		this.timeStep = timeStep;
	}
	
	/**
	 * @param dimension must be one of <code>1</code>, <code>1.5</code>, <code>2</code>, <code>2.5</code>, 
	 * or <code>3</code>.
	 */
	TimeSimulationParameterSet(float dimension) {
		this(dimension, TimeParameterType.REALTIME, TimeUnit.SECONDS, 1.);
	}

	/** Replies the type of time manager.
	 * 
	 * @return the type.
	 */
	public TimeParameterType getType() {
		return this.type;
	}
	
	/** Replies the unit in the time manager.
	 * 
	 * @return the unit.
	 */
	public TimeUnit getTimeUnit() {
		return this.unit;
	}

	/** Replies the time step in the time manager.
	 * 
	 * @return the time step.
	 */
	public double getTimeStep() {
		return this.timeStep;
	}

	/**
	 * Set of parameters to initialize a time manager.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public enum TimeParameterType {

		/** Realtime manager.
		 */
		REALTIME,
		
		/** Step based manager.
		 */
		STEP_BY_STEP;
		
	}
	
}
