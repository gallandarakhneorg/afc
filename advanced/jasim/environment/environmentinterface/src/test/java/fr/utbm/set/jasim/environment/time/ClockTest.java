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
package fr.utbm.set.jasim.environment.time;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import fr.utbm.set.math.MeasureUnitUtil;
import fr.utbm.set.unittest.AbstractTestCase;

/**
 * Unit test for AbstractClock classes.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class ClockTest extends AbstractTestCase {

	private ClockStub clock;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.clock = new ClockStub(this.RANDOM.nextDouble()*500, this.RANDOM.nextDouble()*5);
	}

	@Override
	protected void tearDown() throws Exception {
		this.clock = null;
		super.tearDown();
	}

	/**
	 */
	public void testGetSimulationStepDurationTimeUnit() {
		TimeUnit[] values = TimeUnit.values();
		for(TimeUnit unit : values) {
			double attemptedValue = MeasureUnitUtil.convert(this.clock.duration, TimeUnit.SECONDS, unit);
			double currentValue = this.clock.getSimulationStepDuration(unit);
			assertEquals(attemptedValue,currentValue);
		}
	}

	/**
	 */
	public void testGetSimulationTimeTimeUnit() {
		TimeUnit[] values = TimeUnit.values();
		for(TimeUnit unit : values) {
			double attemptedValue = MeasureUnitUtil.convert(this.clock.currentTime, TimeUnit.SECONDS, unit);
			double currentValue = this.clock.getSimulationTime(unit);
			assertEquals(attemptedValue,currentValue);
		}
	}
	
	/**
	 */
	public void testPerSecondDouble() {
		double totalValue = this.RANDOM.nextDouble() * this.RANDOM.nextInt(1000);
		double perSecondAmount = this.clock.perSecond(totalValue);
		double reference = this.clock.duration * totalValue;
		assertEquals(reference,perSecondAmount);
	}

	/**
	 */
	public void testPerSecondInt() {
		int totalValue = this.RANDOM.nextInt(1000);
		double perSecondAmount = this.clock.perSecond(totalValue);
		double reference = this.clock.duration * totalValue;
		assertEquals(reference,perSecondAmount);
	}

	/**
	 */
	public void testPerSecondShort() {
		short totalValue = (short)this.RANDOM.nextInt(1000);
		double perSecondAmount = this.clock.perSecond(totalValue);
		double reference = this.clock.duration * totalValue;
		assertEquals(reference,perSecondAmount);
	}

	/**
	 */
	public void testPerSecondLong() {
		long totalValue = this.RANDOM.nextInt(1000);
		double perSecondAmount = this.clock.perSecond(totalValue);
		double reference = this.clock.duration * totalValue;
		assertEquals(reference,perSecondAmount);
	}

	/**
	 */
	public void testPerSecondFloat() {
		float totalValue = this.RANDOM.nextFloat() * this.RANDOM.nextInt(1000);
		double perSecondAmount = this.clock.perSecond(totalValue);
		double reference = this.clock.duration * totalValue;
		assertEquals(reference,perSecondAmount);
	}

	/**
	 */
	public void testPerSecondByte() {
		byte totalValue = (byte)this.RANDOM.nextInt(100);
		double perSecondAmount = this.clock.perSecond(totalValue);
		double reference = this.clock.duration * totalValue;
		assertEquals(reference,perSecondAmount);
	}

	/**
	 */
	public void testPerSecondNumber_Byte() {
		Byte totalValue = new Byte((byte)this.RANDOM.nextInt(100));
		BigDecimal perSecondAmount = this.clock.perSecond(totalValue);
		double reference = this.clock.duration * totalValue.doubleValue();
		assertEpsilonEquals(new BigDecimal(reference),perSecondAmount);
	}

	/**
	 */
	public void testPerSecondNumber_Short() {
		Short totalValue = new Short((short)this.RANDOM.nextInt(1000));
		BigDecimal perSecondAmount = this.clock.perSecond(totalValue);
		double reference = this.clock.duration * totalValue.doubleValue();
		assertEpsilonEquals(new BigDecimal(reference),perSecondAmount);
	}

	/**
	 */
	public void testPerSecondNumber_Integer() {
		Integer totalValue = new Integer(this.RANDOM.nextInt(1000));
		BigDecimal perSecondAmount = this.clock.perSecond(totalValue);
		double reference = this.clock.duration * totalValue.doubleValue();
		assertEpsilonEquals(new BigDecimal(reference),perSecondAmount);
	}

	/**
	 */
	public void testPerSecondNumber_Long() {
		Long totalValue = new Long(this.RANDOM.nextInt(1000));
		BigDecimal perSecondAmount = this.clock.perSecond(totalValue);
		double reference = this.clock.duration * totalValue.doubleValue();
		assertEpsilonEquals(new BigDecimal(reference),perSecondAmount);
	}

	/**
	 */
	public void testPerSecondNumber_Float() {
		Float totalValue = new Float(this.RANDOM.nextFloat() * this.RANDOM.nextInt(1000));
		BigDecimal perSecondAmount = this.clock.perSecond(totalValue);
		double reference = this.clock.duration * totalValue.doubleValue();
		assertEpsilonEquals(new BigDecimal(reference),perSecondAmount);
	}

	/**
	 */
	public void testPerSecondNumber_Double() {
		Double totalValue = new Double(this.RANDOM.nextDouble() * this.RANDOM.nextInt(1000));
		BigDecimal perSecondAmount = this.clock.perSecond(totalValue);
		double reference = this.clock.duration * totalValue.doubleValue();
		assertEpsilonEquals(new BigDecimal(reference),perSecondAmount);
	}

	/**
	 */
	public void testPerSecondNumber_BigInteger() {
		BigInteger totalValue = new BigInteger(Integer.toString(this.RANDOM.nextInt(1000)));
		BigDecimal perSecondAmount = this.clock.perSecond(totalValue);
		double reference = this.clock.duration * totalValue.doubleValue();
		assertEpsilonEquals(new BigDecimal(reference),perSecondAmount);
	}

	/**
	 */
	public void testPerSecondNumber_BigDecimal() {
		BigDecimal totalValue = new BigDecimal(Double.toString(this.RANDOM.nextDouble()*this.RANDOM.nextInt(1000)));
		BigDecimal perSecondAmount = this.clock.perSecond(totalValue);
		double reference = this.clock.duration * totalValue.doubleValue();
		assertEpsilonEquals(new BigDecimal(reference),perSecondAmount);
	}

	/**
	 */
	public void testPerTimeUnitByteTimeUnit() {
		for(TimeUnit desiredUnit : TimeUnit.values()) {
			byte totalValue = (byte)this.RANDOM.nextInt(100);
			double perUnitAmount = this.clock.perTimeUnit(totalValue, desiredUnit);
			double reference = MeasureUnitUtil.convert(
										this.clock.duration,
										TimeUnit.SECONDS,
										desiredUnit) * totalValue;
			assertEquals(reference,perUnitAmount);
		}
	}

	/**
	 */
	public void testPerTimeUnitShortTimeUnit() {
		for(TimeUnit desiredUnit : TimeUnit.values()) {
			short totalValue = (short)this.RANDOM.nextInt(100);
			double perUnitAmount = this.clock.perTimeUnit(totalValue, desiredUnit);
			double reference = MeasureUnitUtil.convert(
										this.clock.duration,
										TimeUnit.SECONDS,
										desiredUnit) * totalValue;
			assertEquals(reference,perUnitAmount);
		}
	}

	/**
	 */
	public void testPerTimeUnitIntegerTimeUnit() {
		for(TimeUnit desiredUnit : TimeUnit.values()) {
			int totalValue = this.RANDOM.nextInt(100);
			double perUnitAmount = this.clock.perTimeUnit(totalValue, desiredUnit);
			double reference = MeasureUnitUtil.convert(
										this.clock.duration,
										TimeUnit.SECONDS,
										desiredUnit) * totalValue;
			assertEquals(reference,perUnitAmount);
		}
	}

	/**
	 */
	public void testPerTimeUnitLongTimeUnit() {
		for(TimeUnit desiredUnit : TimeUnit.values()) {
			long totalValue = this.RANDOM.nextInt(100);
			double perUnitAmount = this.clock.perTimeUnit(totalValue, desiredUnit);
			double reference = MeasureUnitUtil.convert(
										this.clock.duration,
										TimeUnit.SECONDS,
										desiredUnit) * totalValue;
			assertEquals(reference,perUnitAmount);
		}
	}

	/**
	 */
	public void testPerTimeUnitFloatTimeUnit() {
		for(TimeUnit desiredUnit : TimeUnit.values()) {
			float totalValue = this.RANDOM.nextFloat();
			double perUnitAmount = this.clock.perTimeUnit(totalValue, desiredUnit);
			double reference = MeasureUnitUtil.convert(
										this.clock.duration,
										TimeUnit.SECONDS,
										desiredUnit) * totalValue;
			assertEquals(reference,perUnitAmount);
		}
	}

	/**
	 */
	public void testPerTimeUnitDoubleTimeUnit() {
		for(TimeUnit desiredUnit : TimeUnit.values()) {
			double totalValue = this.RANDOM.nextDouble();
			double perUnitAmount = this.clock.perTimeUnit(totalValue, desiredUnit);
			double reference = MeasureUnitUtil.convert(
										this.clock.duration,
										TimeUnit.SECONDS,
										desiredUnit) * totalValue;
			assertEquals(reference,perUnitAmount);
		}
	}

	private BigDecimal computePerTimeReference(TimeUnit desiredUnit, double value) {
		double stepDuration = MeasureUnitUtil.convert(this.clock.duration, TimeUnit.SECONDS, desiredUnit);
		return new BigDecimal(stepDuration * value);
	}
	
	private BigDecimal computePerTimeReferenceB(TimeUnit desiredUnit, double value) {
		double stepDuration = MeasureUnitUtil.convert(this.clock.duration, TimeUnit.SECONDS, desiredUnit);
		BigDecimal bigDuration = new BigDecimal(stepDuration);
		BigDecimal bigValue = new BigDecimal(value);
		return bigDuration.multiply(bigValue);
	}

	/**
	 */
	public void testPerTimeUnitNumberTimeUnit_Byte() {
		for(TimeUnit desiredUnit : TimeUnit.values()) {
			Byte value = new Byte((byte)this.RANDOM.nextInt(100));
			BigDecimal perUnitAmount = this.clock.perTimeUnit(value, desiredUnit);
			assertEpsilonEquals(computePerTimeReference(desiredUnit, value.doubleValue()), perUnitAmount);
		}
	}

	/**
	 */
	public void testPerTimeUnitNumberTimeUnit_Short() {
		for(TimeUnit desiredUnit : TimeUnit.values()) {
			Short value = new Short((short)this.RANDOM.nextInt(100));
			BigDecimal perUnitAmount = this.clock.perTimeUnit(value, desiredUnit);
			assertEpsilonEquals(computePerTimeReference(desiredUnit, value.doubleValue()), perUnitAmount);
		}
	}

	/**
	 */
	public void testPerTimeUnitNumberTimeUnit_Integer() {
		for(TimeUnit desiredUnit : TimeUnit.values()) {
			Integer value = new Integer(this.RANDOM.nextInt(100));
			BigDecimal perUnitAmount = this.clock.perTimeUnit(value, desiredUnit);
			assertEpsilonEquals(computePerTimeReference(desiredUnit, value.doubleValue()), perUnitAmount);
		}
	}

	/**
	 */
	public void testPerTimeUnitNumberTimeUnit_Long() {
		for(TimeUnit desiredUnit : TimeUnit.values()) {
			Long value = new Long(this.RANDOM.nextInt(100));
			BigDecimal perUnitAmount = this.clock.perTimeUnit(value, desiredUnit);
			assertEpsilonEquals(computePerTimeReference(desiredUnit, value.doubleValue()), perUnitAmount);
		}
	}

	/**
	 */
	public void testPerTimeUnitNumberTimeUnit_Float() {
		for(TimeUnit desiredUnit : TimeUnit.values()) {
			Float value = new Float(this.RANDOM.nextInt(100));
			BigDecimal perUnitAmount = this.clock.perTimeUnit(value, desiredUnit);
			assertEpsilonEquals(computePerTimeReference(desiredUnit, value.doubleValue()), perUnitAmount);
		}
	}

	/**
	 */
	public void testPerTimeUnitNumberTimeUnit_Double() {
		for(TimeUnit desiredUnit : TimeUnit.values()) {
			Double value = new Double(this.RANDOM.nextInt(100));
			BigDecimal perUnitAmount = this.clock.perTimeUnit(value, desiredUnit);
			assertEpsilonEquals(computePerTimeReference(desiredUnit, value.doubleValue()), perUnitAmount);
		}
	}

	/**
	 */
	public void testPerTimeUnitNumberTimeUnit_AtomicInteger() {
		for(TimeUnit desiredUnit : TimeUnit.values()) {
			AtomicInteger value = new AtomicInteger(this.RANDOM.nextInt(100));
			BigDecimal perUnitAmount = this.clock.perTimeUnit(value, desiredUnit);
			assertEpsilonEquals(computePerTimeReference(desiredUnit, value.doubleValue()), perUnitAmount);
		}
	}

	/**
	 */
	public void testPerTimeUnitNumberTimeUnit_AtomicLong() {
		for(TimeUnit desiredUnit : TimeUnit.values()) {
			AtomicLong value = new AtomicLong(this.RANDOM.nextInt(100));
			BigDecimal perUnitAmount = this.clock.perTimeUnit(value, desiredUnit);
			assertEpsilonEquals(computePerTimeReference(desiredUnit, value.doubleValue()), perUnitAmount);
		}
	}

	/**
	 */
	public void testPerTimeUnitNumberTimeUnit_BigInteger() {
		for(TimeUnit desiredUnit : TimeUnit.values()) {
			BigInteger value = new BigInteger(Integer.toString(this.RANDOM.nextInt(100)));
			BigDecimal perUnitAmount = this.clock.perTimeUnit(value, desiredUnit);
			assertEpsilonEquals(computePerTimeReferenceB(desiredUnit, value.doubleValue()), perUnitAmount);
		}
	}

	/**
	 */
	public void testPerTimeUnitNumberTimeUnit_BigDecimal() {
		for(TimeUnit desiredUnit : TimeUnit.values()) {
			BigDecimal value = new BigDecimal(Integer.toString(this.RANDOM.nextInt(100)));
			BigDecimal perUnitAmount = this.clock.perTimeUnit(value, desiredUnit);
			assertEpsilonEquals(computePerTimeReferenceB(desiredUnit, value.doubleValue()), perUnitAmount);
		}
	}

}
