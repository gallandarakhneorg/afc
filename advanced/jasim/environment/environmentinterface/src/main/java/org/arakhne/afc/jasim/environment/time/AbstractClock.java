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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/** This class permits to implement a simulation clock.
*
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
*/
public abstract class AbstractClock implements Clock {
	
	/**
	 * Is the default time unit of this clock.
	 */
	protected final TimeUnit defaultTimeUnit;
	
	private Date startUp = new Date();
	
	/**
	 * @param defaultTimeUnit is the default time unit of this clock.
	 */
	public AbstractClock(TimeUnit defaultTimeUnit) {
		this.defaultTimeUnit = defaultTimeUnit;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date getOperatingSystemStartUpTime() {
		return this.startUp;
	}

	/** {@inheritDoc}
	 */
	@Override
	public TimeUnit getClockTimeUnit() {
		return this.defaultTimeUnit;
	}

	/** {@inheritDoc}
	 */
	@Override
	public final double getSimulationTime() {
		return getSimulationTime(this.defaultTimeUnit);
	}

	/** {@inheritDoc}
	 */
	@Override
	public final double getSimulationStepDuration() {
		return getSimulationStepDuration(this.defaultTimeUnit);
	}

	/** {@inheritDoc}
	 */
	@Override
	public final double perSecond(double value) {
		return getSimulationStepDuration(TimeUnit.SECONDS) * value;
	}

	/** {@inheritDoc}
	 */
	@Override
	public final double toSecond(double value) {
		return value / getSimulationStepDuration(TimeUnit.SECONDS);
	}

	/** {@inheritDoc}
	 */
	@Override
	public final double perTimeUnit(double value, TimeUnit desiredUnit) {
		return getSimulationStepDuration(desiredUnit) * value;
	}

	/** {@inheritDoc}
	 */
	@Override
	public final double toTimeUnit(double value, TimeUnit desiredUnit) {
		return value / getSimulationStepDuration(desiredUnit);
	}

	/** {@inheritDoc}
	 */
	@Override
	public final double perSecond(int value) {
		return getSimulationStepDuration(TimeUnit.SECONDS) * value;
	}

	/** {@inheritDoc}
	 */
	@Override
	public final double toSecond(int value) {
		return value / getSimulationStepDuration(TimeUnit.SECONDS);
	}

	/** {@inheritDoc}
	 */
	@Override
	public final double perTimeUnit(int value, TimeUnit desiredUnit) {
		return getSimulationStepDuration(desiredUnit) * value;
	}

	/** {@inheritDoc}
	 */
	@Override
	public final double toTimeUnit(int value, TimeUnit desiredUnit) {
		return value / getSimulationStepDuration(desiredUnit);
	}

	/** {@inheritDoc}
	 */
	@Override
	public final double perSecond(short value) {
		return getSimulationStepDuration(TimeUnit.SECONDS) * value;		
	}

	/** {@inheritDoc}
	 */
	@Override
	public final double toSecond(short value) {
		return value / getSimulationStepDuration(TimeUnit.SECONDS);
	}

	/** {@inheritDoc}
	 */
	@Override
	public final double perTimeUnit(short value, TimeUnit desiredUnit) {
		return getSimulationStepDuration(desiredUnit) * value;		
	}

	/** {@inheritDoc}
	 */
	@Override
	public final double toTimeUnit(short value, TimeUnit desiredUnit) {
		return value / getSimulationStepDuration(desiredUnit);
	}

	/** {@inheritDoc}
	 */
	@Override
	public final double perSecond(long value) {
		return getSimulationStepDuration(TimeUnit.SECONDS) * value;		
	}

	/** {@inheritDoc}
	 */
	@Override
	public final double toSecond(long value) {
		return value / getSimulationStepDuration(TimeUnit.SECONDS);
	}

	/** {@inheritDoc}
	 */
	@Override
	public final double perTimeUnit(long value, TimeUnit desiredUnit) {
		return getSimulationStepDuration(desiredUnit) * value;		
	}

	/** {@inheritDoc}
	 */
	@Override
	public final double toTimeUnit(long value, TimeUnit desiredUnit) {
		return value / getSimulationStepDuration(desiredUnit);
	}

	/** {@inheritDoc}
	 */
	@Override
	public final double perSecond(float value) {
		return getSimulationStepDuration(TimeUnit.SECONDS) * value;		
	}

	/** {@inheritDoc}
	 */
	@Override
	public final double toSecond(float value) {
		return value / getSimulationStepDuration(TimeUnit.SECONDS);
	}

	/** {@inheritDoc}
	 */
	@Override
	public final double perTimeUnit(float value, TimeUnit desiredUnit) {
		return getSimulationStepDuration(desiredUnit) * value;		
	}

	/** {@inheritDoc}
	 */
	@Override
	public final double toTimeUnit(float value, TimeUnit desiredUnit) {
		return value / getSimulationStepDuration(desiredUnit);
	}

	/** {@inheritDoc}
	 */
	@Override
	public final double perSecond(byte value) {
		return getSimulationStepDuration(TimeUnit.SECONDS) * value;
	}

	/** {@inheritDoc}
	 */
	@Override
	public final double toSecond(byte value) {
		return value / getSimulationStepDuration(TimeUnit.SECONDS);
	}

	/** {@inheritDoc}
	 */
	@Override
	public final double perTimeUnit(byte value, TimeUnit desiredUnit) {
		return getSimulationStepDuration(desiredUnit) * value;
	}

	/** {@inheritDoc}
	 */
	@Override
	public final double toTimeUnit(byte value, TimeUnit desiredUnit) {
		return value / getSimulationStepDuration(desiredUnit);
	}

	/** {@inheritDoc}
	 */
	@Override
	public final <N extends Number> BigDecimal perSecond(N value) {
		return perTimeUnit(value, TimeUnit.SECONDS);
	}

	/** {@inheritDoc}
	 */
	@Override
	public final <N extends Number> BigDecimal toSecond(N value) {
		return toTimeUnit(value, TimeUnit.SECONDS);
	}

	/** {@inheritDoc}
	 */
	@Override
	public final <N extends Number> BigDecimal perTimeUnit(N value, TimeUnit desiredUnit) {
		assert(value!=null);
		if (value instanceof BigInteger) {
			BigDecimal bigValue = new BigDecimal((BigInteger)value);
			BigDecimal stepDuration = new BigDecimal(getSimulationStepDuration(desiredUnit));
			return stepDuration.multiply(bigValue);
		}
		else if (value instanceof BigDecimal) {
			BigDecimal stepDuration = new BigDecimal(getSimulationStepDuration(desiredUnit));
			return stepDuration.multiply((BigDecimal)value);
		}
		else {
			return new BigDecimal(perTimeUnit(value.doubleValue(), desiredUnit));
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public final <N extends Number> BigDecimal toTimeUnit(N value, TimeUnit desiredUnit) {
		assert(value!=null);
		if (value instanceof BigInteger) {
			BigDecimal bigValue = new BigDecimal((BigInteger)value);
			BigDecimal stepDuration = new BigDecimal(getSimulationStepDuration(desiredUnit));
			return bigValue.divide(stepDuration);
		}
		else if (value instanceof BigDecimal) {
			BigDecimal stepDuration = new BigDecimal(getSimulationStepDuration(desiredUnit));
			return ((BigDecimal)value).divide(stepDuration);
		}
		else {
			return new BigDecimal(toTimeUnit(value.doubleValue(), desiredUnit));
		}
	}

}