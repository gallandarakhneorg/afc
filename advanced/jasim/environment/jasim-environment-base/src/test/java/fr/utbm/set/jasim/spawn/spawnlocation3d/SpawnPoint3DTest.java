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
package fr.utbm.set.jasim.spawn.spawnlocation3d;

import java.util.UUID;

import fr.utbm.set.geom.GeometryUtil;
import fr.utbm.set.geom.object.EuclidianPoint2D;
import fr.utbm.set.geom.object.EuclidianPoint3D;
import fr.utbm.set.jasim.spawn.AbstractSpawnLocationTest;
import fr.utbm.set.math.MathConstants;

/**
 * Unit test on SpawnPoint.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class SpawnPoint3DTest extends AbstractSpawnLocationTest<SpawnPoint3D> {

	private double startAngle;
	private double endAngle;
	private EuclidianPoint2D position;
	
	@Override
	public void setUp() throws Exception {
		this.startAngle = (this.RANDOM.nextDouble() - this.RANDOM.nextDouble()) * 2. * Math.PI;
		this.endAngle = this.startAngle + this.RANDOM.nextDouble() * 2. * Math.PI + 0.0001;
		this.position = new EuclidianPoint2D(randomPoint2D());
		super.setUp();
	}
	
	@Override
	protected SpawnPoint3D createSpawner(double startDate, double endDate) {
		return new SpawnPoint3D(UUID.randomUUID(),
				randomString(),
				this.position, this.startAngle, this.endAngle, startDate, endDate);
	}

	@Override
	public void tearDown() throws Exception {
		this.position = null;
		super.tearDown();
	}

	/**
	 */
	public void testGetReferenceDirection() {
		DirectionConstraint3D constraint = this.point.getReferenceDirection();
		double s = GeometryUtil.clampRadian0To2PI(this.startAngle);
		assertEpsilonEquals(s, constraint.startAngle);
		double e = this.endAngle;
		while (e<s) {
			e += MathConstants.TWO_PI;
		}
		assertEpsilonEquals(e, constraint.endAngle);
	}
	
	/**
	 */
	public void testGetReferencePoint() {
		EuclidianPoint3D actual = this.point.getReferencePoint();
		assertNotNull(actual);
		assertEpsilonEquals(this.position.getCoordinate(0), actual.getCoordinate(0));
		assertEpsilonEquals(this.position.getCoordinate(1), actual.getCoordinate(1));
	}

}