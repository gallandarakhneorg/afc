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

import fr.utbm.set.geom.object.Point1D;
import fr.utbm.set.geom.object.Point1D5;
import fr.utbm.set.geom.object.Segment1D;
import fr.utbm.set.unittest.AbstractTestCase;

/**
 * Unit test on SpawnPoint.
 * 
 * @param <SP>
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractSpawnLocationTest<SP extends AbstractSpawnLocation<?,?,?>> extends AbstractTestCase {

	private double startDate;
	private double endDate;
	/**
	 */
	protected SP point;
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		this.startDate = 0;
		this.endDate = this.startDate + this.RANDOM.nextDouble()*1000 +1;
		
		this.point = createSpawner(this.startDate, this.endDate);
	}
	
	/** Random an 1D point.
	 *
	 * @param segment
	 * @return a random point.
	 */
	protected Point1D randomPoint1D(Segment1D segment) {
		return new Point1D(segment,
				this.RANDOM.nextDouble() * segment.getLength());
	}
	
	/** Random an 1.5D point.
	 * 
	 * @param segment
	 * @return a random point.
	 */
	protected Point1D5 randomPoint1D5(Segment1D segment) {
		return new Point1D5(
				segment,
				this.RANDOM.nextDouble() * segment.getLength(),
				(this.RANDOM.nextDouble()-this.RANDOM.nextDouble()) * 10);
	}

	/**
	 * @param _startDate
	 * @param _endDate
	 * @return a spawner
	 */
	protected abstract SP createSpawner(double _startDate, double _endDate);
	
	@Override
	public void tearDown() throws Exception {
		this.point = null;
		super.tearDown();
	}

	/**
	 */
	public void testGetStartDate() {
		assertEquals(this.startDate, this.point.getStartDate());
	}
	
	/**
	 */
	public void testGetEndDate() {
		assertEquals(this.endDate, this.point.getEndDate());
	}

	/**
	 */
	/*public void testSpawn() {
		int testCount = 50000;

		double s = GeometryUtil.ensure2PIRadian(this.startAngle);
		double e = GeometryUtil.ensure2PIRadian(this.endAngle);
		while (e<s) e += 2.*Math.PI;
		
		assertEquals(this.point.getSpawningQuadrant()[0], s);
		assertEquals(this.point.getSpawningQuadrant()[1], e);
		
		s = Math.toDegrees(s);
		e = Math.toDegrees(e);

		ConstantTimeManager time = new ConstantTimeManager();
		time.startEnvironmentLoop();
		time.endEnvironmentLoop();
		TestingSpawner spawner = new TestingSpawner();
		this.point.addSpawningLaw(spawner, new StochasticSpawningLaw(new UniformStochasticLaw(0,100)));
		
		int cnt = 0;
		for(int i=0; i<testCount; ++i) {
			if (this.point.spawn(time)) {
				++cnt;
				assertEquals(this.position, spawner.lastPoint);
				
				double a = Math.toDegrees(GeometryUtil.angle(1, 0, spawner.lastDirection.x, spawner.lastDirection.y));
				assertTrue(a+">="+s,a>=s); //$NON-NLS-1$
				assertTrue(a+"<="+e,a<=e); //$NON-NLS-1$
			}
			assertEquals(cnt, spawner.count);
		}
	}*/
	
}
