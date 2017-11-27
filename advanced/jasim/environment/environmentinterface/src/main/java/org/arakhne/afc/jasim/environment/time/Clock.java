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
import java.util.Date;
import java.util.concurrent.TimeUnit;

/** This interface describes a simulation clock.
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Clock {
	
	/** Replies operating system time which is corresponding to time 0 in simulation.
	 * 
	 * @return the operating system time at startup.
	 */
	public Date getOperatingSystemStartUpTime();

	/** Replies the default time unit used by this clock.
	 * 
	 * @return the default time unit.
	 */
	public TimeUnit getClockTimeUnit();

	/** Replies the current date of simulation.
	 * <p>
	 * The value semantics depends on the implementation of the {@link TimeManager}
	 * used by the environment.
	 * 
	 * @param desired_unit indicates the unit to use
	 * @return the simulation's time espressed in the specified unit.
	 */
	public double getSimulationTime(TimeUnit desired_unit);

	/** Replies the current date of simulation.
	 * <p>
	 * The value semantics depends on the implementation of the {@link TimeManager}
	 * used by the environment.
	 * The unit also depends on the environment's implementation.
	 * 
	 * @return the simulation's time espressed in the default time unit.
	 */
	public double getSimulationTime();

	/** Replies the duration of one simulation loop.
	 * <p>
	 * The loop's semantics depends on the implementation of the {@link TimeManager}
	 * used by the environment.
	 * <p>
	 * The returned value could evolve during the simulation process.
	 * 
	 * @param desired_unit indicates the unit to use
	 * @return the simulation's step time.
	 */
	public double getSimulationStepDuration(TimeUnit desired_unit);

	/** Replies the duration of one simulation loop.
	 * <p>
	 * The loop's semantics depends on the implementation of the {@link TimeManager}
	 * used by the environment.
	 * <p>
	 * The returned value could evolve during the simulation process.
	 * 
	 * @return the simulation's step time in the default time unit.
	 */
	public double getSimulationStepDuration();

	/** Replies the count of simulation loops which were run.
	 * <p>
	 * The loop's semantics depends on the implementation of the {@link TimeManager}
	 * used by the environment.
	 * 
	 * @return the number of run simulation steps.
	 * @since 2.0
	 */
	public long getSimulationStepCount();

	/** Compute a value expressed in something per time unit into
	 * something per simulation loop.
	 * <p>
	 * The returned value corresponds to the quantity of <var>value</var>
	 * to apply at each simulation step if the entire <var>value</var>
	 * must be applied during one second.
	 * <p>
	 * perSecond(v) = getSimulationStepDuration(TimeUnit.SECONDS) * v
	 * 
	 * @param value is the value to smooth on one second
	 * @return the quantity of <var>value</var> that corresponds to a simulation step.
	 */
	public double perSecond(double value);

	/** Compute a value expressed in something per simulation loop into
	 * something per time unit.
	 * <p>
	 * The returned value corresponds to the quantity of <var>value</var>
	 * to apply each second if the entire <var>value</var>
	 * is be applied during one simulation loop.
	 * <p>
	 * toSecond(v) = v / getSimulationStepDuration(TimeUnit.SECONDS)
	 * 
	 * @param value is the value to smooth on one simulation loop
	 * @return the quantity of <var>value</var> that corresponds to a second.
	 */
	public double toSecond(double value);

	/** Compute a value expressed in something per time unit into
	 * something per simulation loop.
	 * <p>
	 * The returned value corresponds to the quantity of <var>value</var>
	 * to apply at each simulation step if the entire <var>value</var>
	 * must be applied during one given unit time.
	 * <p>
	 * perTimeUnit(v) = SECONDS.convert(getSimulationStepDuration(TimeUnit.SECONDS), desiredUnit) * v
	 * 
	 * @param value is the value to smooth on one second
	 * @param desiredUnit is the unit to use.
	 * @return the quantity of <var>value</var> that corresponds to a simulation step.
	 */
	public double perTimeUnit(double value, TimeUnit desiredUnit);

	/** Compute a value expressed in something per simulation loop into
	 * something per time unit.
	 * <p>
	 * The returned value corresponds to the quantity of <var>value</var>
	 * to apply at each time unit if the entire <var>value</var>
	 * is applied during one simulation loop.
	 * <p>
	 * toTimeUnit(v) = v / SECONDS.convert(getSimulationStepDuration(TimeUnit.SECONDS), desiredUnit)
	 * 
	 * @param value is the value to smooth on one simulation loop
	 * @param desiredUnit is the unit to use.
	 * @return the quantity of <var>value</var> that corresponds to a time unit.
	 */
	public double toTimeUnit(double value, TimeUnit desiredUnit);

	/** Compute a value expressed in something per time unit into
	 * something per simulation loop.
	 * <p>
	 * The returned value corresponds to the quantity of <var>value</var>
	 * to apply at each simulation step if the entire <var>value</var>
	 * must be applied during one second.
	 * <p>
	 * perSecond(v) = getSimulationStepDuration(TimeUnit.SECONDS) * v
	 * 
	 * @param value is the value to smooth on one second
	 * @return the quantity of <var>value</var> that corresponds to a simulation step.
	 */
	public double perSecond(int value);

	/** Compute a value expressed in something per simulation loop into
	 * something per time unit.
	 * <p>
	 * The returned value corresponds to the quantity of <var>value</var>
	 * to apply each second if the entire <var>value</var>
	 * is be applied during one simulation loop.
	 * <p>
	 * toSecond(v) = v / getSimulationStepDuration(TimeUnit.SECONDS)
	 * 
	 * @param value is the value to smooth on one simulation loop
	 * @return the quantity of <var>value</var> that corresponds to a second.
	 */
	public double toSecond(int value);

	/** Compute a value expressed in something per time unit into
	 * something per simulation loop.
	 * <p>
	 * The returned value corresponds to the quantity of <var>value</var>
	 * to apply at each simulation step if the entire <var>value</var>
	 * must be applied during one given unit time.
	 * <p>
	 * perTimeUnit(v) = SECONDS.convert(getSimulationStepDuration(SECONDS), desiredUnit) * v
	 * 
	 * @param value is the value to smooth on one second
	 * @param desiredUnit is the unit to use.
	 * @return the quantity of <var>value</var> that corresponds to a simulation step.
	 */
	public double perTimeUnit(int value, TimeUnit desiredUnit);

	/** Compute a value expressed in something per simulation loop into
	 * something per time unit.
	 * <p>
	 * The returned value corresponds to the quantity of <var>value</var>
	 * to apply at each time unit if the entire <var>value</var>
	 * is applied during one simulation loop.
	 * <p>
	 * toTimeUnit(v) = v / SECONDS.convert(getSimulationStepDuration(TimeUnit.SECONDS), desiredUnit)
	 * 
	 * @param value is the value to smooth on one simulation loop
	 * @param desiredUnit is the unit to use.
	 * @return the quantity of <var>value</var> that corresponds to a time unit.
	 */
	public double toTimeUnit(int value, TimeUnit desiredUnit);

	/** Compute a value expressed in something per time unit into
	 * something per simulation loop.
	 * <p>
	 * The returned value corresponds to the quantity of <var>value</var>
	 * to apply at each simulation step if the entire <var>value</var>
	 * must be applied during one second.
	 * <p>
	 * perSecond(v) = getSimulationStepDuration(SECONDS) * v
	 * 
	 * @param value is the value to smooth on one second
	 * @return the quantity of <var>value</var> that corresponds to a simulation step.
	 */
	public double perSecond(short value);

	/** Compute a value expressed in something per simulation loop into
	 * something per time unit.
	 * <p>
	 * The returned value corresponds to the quantity of <var>value</var>
	 * to apply each second if the entire <var>value</var>
	 * is be applied during one simulation loop.
	 * <p>
	 * toSecond(v) = v / getSimulationStepDuration(TimeUnit.SECONDS)
	 * 
	 * @param value is the value to smooth on one simulation loop
	 * @return the quantity of <var>value</var> that corresponds to a second.
	 */
	public double toSecond(short value);

	/** Compute a value expressed in something per time unit into
	 * something per simulation loop.
	 * <p>
	 * The returned value corresponds to the quantity of <var>value</var>
	 * to apply at each simulation step if the entire <var>value</var>
	 * must be applied during one given unit time.
	 * <p>
	 * perTimeUnit(v) = SECONDS.convert(getSimulationStepDuration(SECONDS), desiredUnit) * v
	 * 
	 * @param value is the value to smooth on one second
	 * @param desiredUnit is the unit to use.
	 * @return the quantity of <var>value</var> that corresponds to a simulation step.
	 */
	public double perTimeUnit(short value, TimeUnit desiredUnit);

	/** Compute a value expressed in something per simulation loop into
	 * something per time unit.
	 * <p>
	 * The returned value corresponds to the quantity of <var>value</var>
	 * to apply at each time unit if the entire <var>value</var>
	 * is applied during one simulation loop.
	 * <p>
	 * toTimeUnit(v) = v / SECONDS.convert(getSimulationStepDuration(TimeUnit.SECONDS), desiredUnit)
	 * 
	 * @param value is the value to smooth on one simulation loop
	 * @param desiredUnit is the unit to use.
	 * @return the quantity of <var>value</var> that corresponds to a time unit.
	 */
	public double toTimeUnit(short value, TimeUnit desiredUnit);

	/** Compute a value expressed in something per time unit into
	 * something per simulation loop.
	 * <p>
	 * The returned value corresponds to the quantity of <var>value</var>
	 * to apply at each simulation step if the entire <var>value</var>
	 * must be applied during one second.
	 * <p>
	 * perSecond(v) = getSimulationStepDuration(SECONDS) * v
	 * 
	 * @param value is the value to smooth on one second
	 * @return the quantity of <var>value</var> that corresponds to a simulation step.
	 */
	public double perSecond(long value);

	/** Compute a value expressed in something per simulation loop into
	 * something per time unit.
	 * <p>
	 * The returned value corresponds to the quantity of <var>value</var>
	 * to apply each second if the entire <var>value</var>
	 * is be applied during one simulation loop.
	 * <p>
	 * toSecond(v) = v / getSimulationStepDuration(TimeUnit.SECONDS)
	 * 
	 * @param value is the value to smooth on one simulation loop
	 * @return the quantity of <var>value</var> that corresponds to a second.
	 */
	public double toSecond(long value);

	/** Compute a value expressed in something per time unit into
	 * something per simulation loop.
	 * <p>
	 * The returned value corresponds to the quantity of <var>value</var>
	 * to apply at each simulation step if the entire <var>value</var>
	 * must be applied during one given unit time.
	 * <p>
	 * perTimeUnit(v) = SECONDS.convert(getSimulationStepDuration(SECONDS), desiredUnit) * v
	 * 
	 * @param value is the value to smooth on one second
	 * @param desiredUnit is the unit to use.
	 * @return the quantity of <var>value</var> that corresponds to a simulation step.
	 */
	public double perTimeUnit(long value, TimeUnit desiredUnit);

	/** Compute a value expressed in something per simulation loop into
	 * something per time unit.
	 * <p>
	 * The returned value corresponds to the quantity of <var>value</var>
	 * to apply at each time unit if the entire <var>value</var>
	 * is applied during one simulation loop.
	 * <p>
	 * toTimeUnit(v) = v / SECONDS.convert(getSimulationStepDuration(TimeUnit.SECONDS), desiredUnit)
	 * 
	 * @param value is the value to smooth on one simulation loop
	 * @param desiredUnit is the unit to use.
	 * @return the quantity of <var>value</var> that corresponds to a time unit.
	 */
	public double toTimeUnit(long value, TimeUnit desiredUnit);

	/** Compute a value expressed in something per time unit into
	 * something per simulation loop.
	 * <p>
	 * The returned value corresponds to the quantity of <var>value</var>
	 * to apply at each simulation step if the entire <var>value</var>
	 * must be applied during one second.
	 * <p>
	 * perSecond(v) = getSimulationStepDuration(SECONDS) * v
	 * 
	 * @param value is the value to smooth on one second
	 * @return the quantity of <var>value</var> that corresponds to a simulation step.
	 */
	public double perSecond(float value);

	/** Compute a value expressed in something per simulation loop into
	 * something per time unit.
	 * <p>
	 * The returned value corresponds to the quantity of <var>value</var>
	 * to apply each second if the entire <var>value</var>
	 * is be applied during one simulation loop.
	 * <p>
	 * toSecond(v) = v / getSimulationStepDuration(TimeUnit.SECONDS)
	 * 
	 * @param value is the value to smooth on one simulation loop
	 * @return the quantity of <var>value</var> that corresponds to a second.
	 */
	public double toSecond(float value);

	/** Compute a value expressed in something per time unit into
	 * something per simulation loop.
	 * <p>
	 * The returned value corresponds to the quantity of <var>value</var>
	 * to apply at each simulation step if the entire <var>value</var>
	 * must be applied during one given unit time.
	 * <p>
	 * perTimeUnit(v) = SECONDS.convert(getSimulationStepDuration(SECONDS), desiredUnit) * v
	 * 
	 * @param value is the value to smooth on one second
	 * @param desiredUnit is the unit to use.
	 * @return the quantity of <var>value</var> that corresponds to a simulation step.
	 */
	public double perTimeUnit(float value, TimeUnit desiredUnit);

	/** Compute a value expressed in something per simulation loop into
	 * something per time unit.
	 * <p>
	 * The returned value corresponds to the quantity of <var>value</var>
	 * to apply at each time unit if the entire <var>value</var>
	 * is applied during one simulation loop.
	 * <p>
	 * toTimeUnit(v) = v / SECONDS.convert(getSimulationStepDuration(TimeUnit.SECONDS), desiredUnit)
	 * 
	 * @param value is the value to smooth on one simulation loop
	 * @param desiredUnit is the unit to use.
	 * @return the quantity of <var>value</var> that corresponds to a time unit.
	 */
	public double toTimeUnit(float value, TimeUnit desiredUnit);

	/** Compute a value expressed in something per time unit into
	 * something per simulation loop.
	 * <p>
	 * The returned value corresponds to the quantity of <var>value</var>
	 * to apply at each simulation step if the entire <var>value</var>
	 * must be applied during one second.
	 * <p>
	 * perSecond(v) = getSimulationStepDuration(SECONDS) * v
	 * 
	 * @param value is the value to smooth on one second
	 * @return the quantity of <var>value</var> that corresponds to a simulation step.
	 */
	public double perSecond(byte value);

	/** Compute a value expressed in something per simulation loop into
	 * something per time unit.
	 * <p>
	 * The returned value corresponds to the quantity of <var>value</var>
	 * to apply each second if the entire <var>value</var>
	 * is be applied during one simulation loop.
	 * <p>
	 * toSecond(v) = v / getSimulationStepDuration(TimeUnit.SECONDS)
	 * 
	 * @param value is the value to smooth on one simulation loop
	 * @return the quantity of <var>value</var> that corresponds to a second.
	 */
	public double toSecond(byte value);

	/** Compute a value expressed in something per time unit into
	 * something per simulation loop.
	 * <p>
	 * The returned value corresponds to the quantity of <var>value</var>
	 * to apply at each simulation step if the entire <var>value</var>
	 * must be applied during one given unit time.
	 * <p>
	 * perTimeUnit(v) = SECONDS.convert(getSimulationStepDuration(SECONDS), desiredUnit) * v
	 * 
	 * @param value is the value to smooth on one second
	 * @param desiredUnit is the unit to use.
	 * @return the quantity of <var>value</var> that corresponds to a simulation step.
	 */
	public double perTimeUnit(byte value, TimeUnit desiredUnit);

	/** Compute a value expressed in something per simulation loop into
	 * something per time unit.
	 * <p>
	 * The returned value corresponds to the quantity of <var>value</var>
	 * to apply at each time unit if the entire <var>value</var>
	 * is applied during one simulation loop.
	 * <p>
	 * toTimeUnit(v) = v / SECONDS.convert(getSimulationStepDuration(TimeUnit.SECONDS), desiredUnit)
	 * 
	 * @param value is the value to smooth on one simulation loop
	 * @param desiredUnit is the unit to use.
	 * @return the quantity of <var>value</var> that corresponds to a time unit.
	 */
	public double toTimeUnit(byte value, TimeUnit desiredUnit);

	/** Compute a value expressed in something per time unit into
	 * something per simulation loop.
	 * <p>
	 * The returned value corresponds to the quantity of <var>value</var>
	 * to apply at each simulation step if the entire <var>value</var>
	 * must be applied during one second.
	 * <p>
	 * perSecond(v) = getSimulationStepDuration(SECONDS) * v
	 * 
	 * @param <N> is the type of the number to convert.
	 * @param value is the value to smooth on one second
	 * @return the quantity of <var>value</var> that corresponds to a simulation step.
	 */
	public <N extends Number> BigDecimal perSecond(N value);

	/** Compute a value expressed in something per simulation loop into
	 * something per time unit.
	 * <p>
	 * The returned value corresponds to the quantity of <var>value</var>
	 * to apply each second if the entire <var>value</var>
	 * is be applied during one simulation loop.
	 * <p>
	 * toSecond(v) = v / getSimulationStepDuration(TimeUnit.SECONDS)
	 * 
	 * @param <N> is the type of the number to convert.
	 * @param value is the value to smooth on one simulation loop
	 * @return the quantity of <var>value</var> that corresponds to a second.
	 */
	public <N extends Number> BigDecimal toSecond(N value);

	/** Compute a value expressed in something per time unit into
	 * something per simulation loop.
	 * <p>
	 * The returned value corresponds to the quantity of <var>value</var>
	 * to apply at each simulation step if the entire <var>value</var>
	 * must be applied during one given unit time.
	 * <p>
	 * perTimeUnit(v) = SECONDS.convert(getSimulationStepDuration(SECONDS), desiredUnit) * v
	 *
	 * @param <N> is the type of the number to convert.
	 * @param value is the value to smooth on one second
	 * @param desiredUnit is the unit to use.
	 * @return the quantity of <var>value</var> that corresponds to a simulation step.
	 */
	public <N extends Number> BigDecimal perTimeUnit(N value, TimeUnit desiredUnit);

	/** Compute a value expressed in something per simulation loop into
	 * something per time unit.
	 * <p>
	 * The returned value corresponds to the quantity of <var>value</var>
	 * to apply at each time unit if the entire <var>value</var>
	 * is applied during one simulation loop.
	 * <p>
	 * toTimeUnit(v) = v / SECONDS.convert(getSimulationStepDuration(TimeUnit.SECONDS), desiredUnit)
	 * 
	 * @param <N> is the type of the number to convert.
	 * @param value is the value to smooth on one simulation loop
	 * @param desiredUnit is the unit to use.
	 * @return the quantity of <var>value</var> that corresponds to a time unit.
	 */
	public <N extends Number> BigDecimal toTimeUnit(N value, TimeUnit desiredUnit);

}