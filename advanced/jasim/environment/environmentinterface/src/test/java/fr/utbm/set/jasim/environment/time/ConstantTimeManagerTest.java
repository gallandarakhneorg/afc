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

import java.util.concurrent.TimeUnit;

import fr.utbm.set.unittest.AbstractTestCase;

import org.arakhne.afc.jasim.environment.time.Clock;
import org.arakhne.afc.jasim.environment.time.ConstantTimeManager;
import org.arakhne.afc.jasim.environment.time.TimeManagerEvent;
import org.arakhne.afc.jasim.environment.time.TimeManagerListener;

/**
 * Unit test for AbstractClock classes.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class ConstantTimeManagerTest extends AbstractTestCase {

	/**
	 */
	public void testGetClock() {
		ConstantTimeManager manager;
		Clock clock;
		double reference;
		for(TimeUnit unit : TimeUnit.values()) {
			manager = new ConstantTimeManager(unit);

			int count = this.RANDOM.nextInt(50)+10;
			
			for(int idx=0; idx<count; ++idx) {
			
				manager.onEnvironmentBehaviourStarted();
				manager.onEnvironmentBehaviourFinished();
			
				clock = manager.getClock();
				
				reference = idx+1.;				
				assertEpsilonEquals(reference,clock.getSimulationTime());
			}
		}
	}

	/**
	 */
	public void testStartEnvironmentLoop() {
		ConstantTimeManager manager = new ConstantTimeManager(TimeUnit.SECONDS);
		Clock clock;
		int count = this.RANDOM.nextInt(50)+10;
		
		for(int idx=0; idx<count; ++idx) {
			manager.onEnvironmentBehaviourStarted();
			clock = manager.getClock();
			assertEquals(0.,clock.getSimulationTime());
		}
	}

	/**
	 */
	public void testEndEnvironmentLoop() {
		ConstantTimeManager manager = new ConstantTimeManager(TimeUnit.SECONDS);
		Clock clock;
		int count = this.RANDOM.nextInt(50)+10;
		
		for(int idx=0; idx<count; ++idx) {
			manager.onEnvironmentBehaviourFinished();
			clock = manager.getClock();
			assertEquals(idx+1.,clock.getSimulationTime());
		}
	}
	
	/**
	 */
	public void testGetSimulationStepDuration() {
		ConstantTimeManager manager = new ConstantTimeManager(TimeUnit.SECONDS);
		Clock clock;
		int count = this.RANDOM.nextInt(50)+10;
		
		for(int idx=0; idx<count; ++idx) {
		
			manager.onEnvironmentBehaviourStarted();
			try {
				Thread.sleep(this.RANDOM.nextInt(25));
			}
			catch (InterruptedException _) {
				//
			}
			manager.onEnvironmentBehaviourFinished();
		
			clock = manager.getClock();
			
			assertEquals(1.,clock.getSimulationStepDuration());
		}
	}

	/**
	 */
	public void testTimeManagerListener() {
		ConstantTimeManager manager = new ConstantTimeManager(TimeUnit.SECONDS);
		Clock clock;
		int count = this.RANDOM.nextInt(50)+10;
		
		Listener listener = new Listener();
		manager.addTimeManagerListener(listener);
			
		for(int idx=0; idx<count; ++idx) {
		
			manager.onEnvironmentBehaviourStarted();
			try {
				Thread.sleep(25);
			}
			catch (InterruptedException _) {
				//
			}
			manager.onEnvironmentBehaviourFinished();
		
			clock = manager.getClock();
			
			assertEquals(clock.getSimulationTime(),listener.time);
			assertEquals(clock.getSimulationStepDuration(),listener.duration);
		}
	}
	
	private class Listener implements TimeManagerListener {

		public double time = Double.NaN;
		public double duration = Double.NaN;
		
		public Listener() {
			//
		}
		
		/** {@inheritDoc}
		 */
		@Override
		public void onSimulationClockChanged(TimeManagerEvent event) {
			this.time = event.getClock().getSimulationTime();
			this.duration = event.getClock().getSimulationStepDuration();
		}
		
	}
	
}
