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

import java.awt.geom.Rectangle2D;
import java.util.UUID;

import javax.vecmath.Point2d;

import fr.utbm.set.geom.GeometryUtil;
import fr.utbm.set.geom.object.EuclidianPoint2D;
import fr.utbm.set.geom.object.EuclidianPoint3D;
import fr.utbm.set.jasim.spawn.AbstractSpawnLocationTest;
import fr.utbm.set.math.MathConstants;

/**
 * Unit test on SpawnArea.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class SpawnArea3DTest extends AbstractSpawnLocationTest<SpawnArea3D> {

	private double startAngle;
	private double endAngle;
	private Point2d p1, p2;
	private Rectangle2D position;
	
	@Override
	public void setUp() throws Exception {
		this.startAngle = (this.RANDOM.nextDouble() - this.RANDOM.nextDouble()) * 2. * Math.PI;
		this.endAngle = this.startAngle + this.RANDOM.nextDouble() * 2. * Math.PI + 0.0001;
		this.p1 = randomPoint2D();
		this.p2 = randomPoint2D();
		this.position = new Rectangle2D.Double(
				Math.min(this.p1.x, this.p2.x),
				Math.min(this.p1.y, this.p2.y),
				Math.abs(this.p1.x-this.p2.x),
				Math.abs(this.p1.y-this.p2.y));
		super.setUp();
	}
	
	@Override
	protected SpawnArea3D createSpawner(double startDate, double endDate) {
		return new SpawnArea3D(
				UUID.randomUUID(),
				randomString(),
				new EuclidianPoint2D(this.position.getMinX(),this.position.getMinY()),
				this.position.getWidth(), this.position.getHeight(),
				this.startAngle, this.endAngle, startDate, endDate);
	}

	@Override
	public void tearDown() throws Exception {
		this.position = null;
		this.p1 = this.p2 = null;
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
		assertEpsilonEquals(this.position.getMinX(), actual.getCoordinate(0));
		assertEpsilonEquals(this.position.getMinY(), actual.getCoordinate(1));
	}

}
