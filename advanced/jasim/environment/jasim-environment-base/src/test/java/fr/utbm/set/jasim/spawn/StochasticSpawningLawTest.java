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

import fr.utbm.set.jasim.environment.time.Clock;
import fr.utbm.set.jasim.environment.time.ConstantTimeManager;
import fr.utbm.set.math.stochastic.LinearStochasticLaw;
import fr.utbm.set.unittest.AbstractTestCase;

/**
 * Unit test for StochasticSpawningLaw.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class StochasticSpawningLawTest extends AbstractTestCase {

	private LinearStochasticLaw law;
	private StochasticSpawningLaw spawnLaw;
	private Clock clock;
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		this.law = new LinearStochasticLaw(0., 100.);
		this.clock = new ConstantTimeManager();
		this.spawnLaw = new StochasticSpawningLaw(this.law);
	}
	
	@Override
	public void tearDown() throws Exception {
		this.spawnLaw = null;
		this.law = null;
		this.clock = null;
		super.tearDown();
	}

	/**
	 */
	public void testGetSpawnableAmountClock() {
		int curve[] = new int[101];
		
		for(int i=0; i<curve.length; ++i)
			curve[i] = 0;
		
		int testCount = 50000;
		int count;
		
		for(int i=0; i<testCount; ++i) {
			count = this.spawnLaw.getSpawnableAmount(this.clock);
			curve[count]++;
		}
		
		count = 0;
		for(int i=0; i<curve.length; ++i) {
			count += curve[i];
		}
		
		assertEquals(testCount, count);

		for(int i=1; i<curve.length; ++i) {
			assertTrue(curve[i]+"<="+curve[i-1], curve[i]<=curve[i-1]); //$NON-NLS-1$
		}
	}

}
