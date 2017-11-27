/* 
 * $Id$
 * 
 * Copyright (c) 2004-09, Multiagent Team - Systems and Transportation Laboratory (SeT)
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
package fr.utbm.set.jasim.agent.hotpoint.hotpoint1d;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

import fr.utbm.set.geom.object.Direction1D;
import fr.utbm.set.geom.object.Point1D;
import fr.utbm.set.geom.object.Segment1D;
import fr.utbm.set.jasim.agent.hotpoint.Segment1DStub;
import fr.utbm.set.unittest.AbstractExtendedTestCase;

/**
 * Unit test on WayPoint.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class WayPoint1DTest extends AbstractExtendedTestCase {

	/**
	 */
	protected double velocity;
	/**
	 */
	protected double time;
	/**
	 */
	protected WayPoint1D hotPoint;
	
	/**
	 */
	protected Segment1D segment;
	
	/**
	 */
	protected Point1D position;
	
	/**
	 */
	protected Vector2d tangent;
	/**
	 */
	protected boolean reversed;
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		this.segment = new Segment1DStub(randomPoint2D(),randomPoint2D());
		this.position = new Point1D(this.segment,
				this.RANDOM.nextDouble()*this.segment.getLength());
		this.tangent = new Vector2d();
		this.tangent.sub(this.segment.getLastPoint(), this.segment.getFirstPoint());
		this.tangent.normalize();
		this.reversed = this.RANDOM.nextBoolean();
		if (this.reversed) this.tangent.negate();
		this.time = this.RANDOM.nextDouble() * 1000;
		this.velocity = this.RANDOM.nextDouble() * 100;
		this.hotPoint = new WayPoint1D(this.position, 
				this.reversed ? Direction1D.REVERTED_DIRECTION : Direction1D.SEGMENT_DIRECTION,
						this.velocity, this.time);
	}
	
	@Override
	public void tearDown() throws Exception {
		this.hotPoint = null;
		this.position = null;
		this.tangent = null;
		this.segment = null;
		super.tearDown();
	}
	
	/**
	 */
	public void testGetTangent3D() {
		Vector3d p = this.hotPoint.getTangent3D();
		assertEpsilonEquals(this.tangent.x, p.x);
		assertEpsilonEquals(this.tangent.y, p.y);
		assertEpsilonEquals(0., p.z);
	}

	/**
	 */
	public void testGetTangent2D() {
		Vector2d p = this.hotPoint.getTangent2D();
		assertEpsilonEquals(this.tangent.x, p.x);
		assertEpsilonEquals(this.tangent.y, p.y);
	}

	/**
	 */
	public void testGetPosition3D() {
		Point3d p = this.hotPoint.getPosition3D();
		assertEpsilonEquals(this.position.toPoint2D().x, p.x);
		assertEpsilonEquals(this.position.toPoint2D().y, p.y);
		assertEpsilonEquals(0., p.z);
	}

	/**
	 */
	public void testGetPosition2D() {
		Point2d p = this.hotPoint.getPosition2D();
		assertEpsilonEquals(this.position.toPoint2D().x, p.x);
		assertEpsilonEquals(this.position.toPoint2D().y, p.y);
	}
	
	/**
	 */
	public void testGetTime() {
		assertEquals(this.time, this.hotPoint.getTime());
	}

	/**
	 */
	public void testGetVelocity() {
		assertEquals(this.velocity, this.hotPoint.getVelocity());
	}

}
