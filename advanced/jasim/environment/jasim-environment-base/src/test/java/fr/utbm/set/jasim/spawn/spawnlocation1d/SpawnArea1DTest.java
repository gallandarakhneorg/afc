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
package fr.utbm.set.jasim.spawn.spawnlocation1d;

import java.util.UUID;

import fr.utbm.set.geom.object.Direction1D;
import fr.utbm.set.geom.object.Point1D;
import fr.utbm.set.jasim.agent.hotpoint.Segment1DStub;
import fr.utbm.set.jasim.spawn.AbstractSpawnLocationTest;

/**
 * Unit test on SpawnArea.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class SpawnArea1DTest extends AbstractSpawnLocationTest<SpawnArea1D> {

	private Direction1D direction;
	private double width;
	private Point1D position;
	
	@Override
	public void setUp() throws Exception {
		this.direction = Direction1D.fromInteger(this.RANDOM.nextInt() - this.RANDOM.nextInt());
		this.position = randomPoint1D(new Segment1DStub(randomPoint2D(),randomPoint2D()));
		this.width = this.RANDOM.nextDouble() * (this.position.getSegment().getLength()-this.position.getCurvilineCoordinate());
		super.setUp();
	}
	
	@Override
	protected SpawnArea1D createSpawner(double startDate, double endDate) {
		return new SpawnArea1D(
				UUID.randomUUID(),
				randomString(),
				this.position, this.width, this.direction,
				startDate, endDate);
	}

	@Override
	public void tearDown() throws Exception {
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
	public void testGetSpawningAreaWidth() {
		assertEquals(this.width, this.point.getSpawningAreaWidth());
	}

	/**
	 */
	public void testGetReferencePoint() {
		assertEquals(this.position, this.point.getReferencePoint());
	}

}
