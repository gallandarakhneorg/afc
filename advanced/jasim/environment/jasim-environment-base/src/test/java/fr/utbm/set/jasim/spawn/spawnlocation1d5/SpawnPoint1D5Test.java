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
package fr.utbm.set.jasim.spawn.spawnlocation1d5;

import java.util.UUID;

import fr.utbm.set.geom.object.Direction1D;
import fr.utbm.set.geom.object.Point1D5;
import fr.utbm.set.jasim.agent.hotpoint.Segment1DStub;
import fr.utbm.set.jasim.spawn.AbstractSpawnLocationTest;

/**
 * Unit test on SpawnPoint.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class SpawnPoint1D5Test extends AbstractSpawnLocationTest<SpawnPoint1D5> {

	private Direction1D direction;
	private Point1D5 position;	
	
	@Override
	public void setUp() throws Exception {
		this.direction = Direction1D.fromInteger(this.RANDOM.nextInt() - this.RANDOM.nextInt());
		this.position = randomPoint1D5(new Segment1DStub(randomPoint2D(),randomPoint2D()));
		super.setUp();
	}
	
	@Override
	protected SpawnPoint1D5 createSpawner(double startDate, double endDate) {
		return new SpawnPoint1D5(UUID.randomUUID(),
				randomString(),
				this.position, this.direction, startDate, endDate);
	}

	@Override
	public void tearDown() throws Exception {
		this.direction = null;
		this.position = null;
		super.tearDown();
	}

	/**
	 */
	public void testGetSpawningDirection() {
		assertEquals(this.direction, this.point.getReferenceDirection());
	}

	/**
	 */
	public void testGetReferencePoint() {
		assertEquals(this.position, this.point.getReferencePoint());
	}

}
