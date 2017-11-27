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
package fr.utbm.set.jasim.environment.model.ground;

import java.util.UUID;

import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;

import fr.utbm.set.geom.bounds.bounds2d.OrientedBoundingRectangle;
import fr.utbm.set.jasim.environment.semantics.GroundType;
import fr.utbm.set.unittest.AbstractRepeatedTestCase;

/** Unit test for OrientedIndoorGround
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class OrientedIndoorGroundTest extends AbstractRepeatedTestCase {

	/**
	 */
	protected UUID id;
	/**
	 */
	protected OrientedBoundingRectangle area;
	/**
	 */
	protected float floorHeight;
	/**
	 */
	protected GroundType defaultSemantic;
	/**
	 */
	protected OrientedIndoorGround ground;
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		this.id = UUID.randomUUID();
		double x = (this.RANDOM.nextDouble()-this.RANDOM.nextDouble())*1000.;
		double y = (this.RANDOM.nextDouble()-this.RANDOM.nextDouble())*1000.;
		double extentR = this.RANDOM.nextDouble()*1000.;
		double extentS = this.RANDOM.nextDouble()*1000.;
		Vector2d R = randomVector2D();
		Vector2d S = randomVector2D();
		R.normalize();
		S.normalize();
		this.area = new OrientedBoundingRectangle(
				new Point2d(x,y),
				new Vector2d[] {
					R,
					S,
				},
				new double[] {
					extentR,
					extentS
				});
		this.floorHeight = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat())*10f;
		this.defaultSemantic = GroundType.GROUNDTYPE_SINGLETON;
		this.ground = new OrientedIndoorGround(this.id, 
				this.area,
				this.floorHeight,
				this.defaultSemantic);
	}
	
	@Override
	public void tearDown() throws Exception {
		this.id = null;
		this.ground = null;
		this.area = null;
		this.defaultSemantic = null;
		super.tearDown();
	}

	/**
	 */
	public void testGetIdentifier() {
		assertEquals(this.id, this.ground.getIdentifier());
	}

	/**
	 */
	public void testGetMinX() {
		assertEpsilonEquals(this.area.getMinX(), this.ground.getMinX());
	}

	/**
	 */
	public void testGetMinY() {
		assertEpsilonEquals(this.area.getMinY(), this.ground.getMinY());
	}

	/**
	 */
	public void testGetMaxX() {
		assertEpsilonEquals(this.area.getMaxX(), this.ground.getMaxX());
	}

	/**
	 */
	public void testGetMaxY() {
		assertEpsilonEquals(this.area.getMaxY(), this.ground.getMaxY());
	}

	/**
	 */
	public void testGetSizeX() {
		assertEpsilonEquals(this.area.getSizeX(), this.ground.getSizeX());
	}
	
	/**
	 */
	public void testGetSizeY() {
		assertEpsilonEquals(this.area.getSizeY(), this.ground.getSizeY());
	}
	
	/**
	 */
	public void testGetHeightAt() {
		double _x = (this.RANDOM.nextDouble()-this.RANDOM.nextDouble())*2000.;
		double _y = (this.RANDOM.nextDouble()-this.RANDOM.nextDouble())*2000.;
		if (this.area.intersects(new Point2d(_x,_y))) {
			assertEpsilonEquals(this.floorHeight, this.ground.getHeightAt(_x, _y));
		}
		else {
			assertTrue(Double.isNaN(this.ground.getHeightAt(_x, _y)));
		}
	}
	
	/**
	 */
	public void testIsTraversable() {
		double _x = (this.RANDOM.nextDouble()-this.RANDOM.nextDouble())*2000.;
		double _y = (this.RANDOM.nextDouble()-this.RANDOM.nextDouble())*2000.;
		if (this.area.intersects(new Point2d(_x,_y))) {
			assertTrue(this.ground.isTraversable(_x, _y));
		}
		else {
			assertFalse(this.ground.isTraversable(_x, _y));
		}
	}

	/**
	 */
	public void testGetGroundType() {
		double _x = (this.RANDOM.nextDouble()-this.RANDOM.nextDouble())*2000.;
		double _y = (this.RANDOM.nextDouble()-this.RANDOM.nextDouble())*2000.;
		if (this.area.intersects(new Point2d(_x,_y))) {
			assertSame(this.defaultSemantic, this.ground.getGroundType(_x, _y));
		}
		else {
			assertNull(this.ground.getGroundType(_x, _y));
		}
	}

}