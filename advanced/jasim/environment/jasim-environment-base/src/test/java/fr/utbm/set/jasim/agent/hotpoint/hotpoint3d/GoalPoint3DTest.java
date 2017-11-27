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
package fr.utbm.set.jasim.agent.hotpoint.hotpoint3d;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

import fr.utbm.set.unittest.AbstractExtendedTestCase;

/**
 * Unit test on GoalPoint.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class GoalPoint3DTest extends AbstractExtendedTestCase {

	/**
	 */
	protected double time;
	/**
	 */
	protected GoalPoint3D hotPoint;
	
	/**
	 */
	protected double x;
	/**
	 */
	protected double y;
	/**
	 */
	protected double z;
	/**
	 */
	protected double tx;
	/**
	 */
	protected double ty;
	/**
	 */
	protected double tz;
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		this.x = this.RANDOM.nextDouble() * 1000;
		this.y = this.RANDOM.nextDouble() * 1000;
		this.z = this.RANDOM.nextDouble() * 1000;
		this.tx = this.RANDOM.nextDouble();
		this.ty = this.RANDOM.nextDouble();
		this.tz = this.RANDOM.nextDouble();
		this.time = this.RANDOM.nextDouble() * 1000;
		this.hotPoint = new GoalPoint3D(this.x,this.y,this.z,
				this.tx,this.ty,this.tz,this.time);
	}
	
	@Override
	public void tearDown() throws Exception {
		this.hotPoint = null;
		super.tearDown();
	}
	
	/**
	 */
	public void testGetTangent3D() {
		Vector3d p = this.hotPoint.getTangent3D();
		assertEquals(this.tx, p.x);
		assertEquals(this.ty, p.y);
		assertEquals(this.tz, p.z);
	}

	/**
	 */
	public void testGetTangent2D() {
		Vector2d p = this.hotPoint.getTangent2D();
		assertEquals(this.tx, p.x);
		assertEquals(this.ty, p.y);
	}

	/**
	 */
	public void testGetPosition3D() {
		Point3d p = this.hotPoint.getPosition3D();
		assertEquals(this.x, p.x);
		assertEquals(this.y, p.y);
		assertEquals(this.z, p.z);
	}

	/**
	 */
	public void testGetPosition2D() {
		Point2d p = this.hotPoint.getPosition2D();
		assertEquals(this.x, p.x);
		assertEquals(this.y, p.y);
	}
	
	/**
	 */
	public void testGetTime() {
		assertEquals(this.time, this.hotPoint.getTime());
	}

}
