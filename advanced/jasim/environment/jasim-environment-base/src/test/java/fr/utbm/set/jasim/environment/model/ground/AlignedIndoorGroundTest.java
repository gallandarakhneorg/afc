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

import fr.utbm.set.jasim.environment.semantics.GroundType;
import fr.utbm.set.unittest.AbstractRepeatedTestCase;

/** Unit test for AlignedIndoorGround
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class AlignedIndoorGroundTest extends AbstractRepeatedTestCase {

	/**
	 */
	protected UUID id;
	/**
	 */
	protected double x;
	/**
	 */
	protected double y;
	/**
	 */
	protected double width;
	/**
	 */
	protected double height;
	/**
	 */
	protected double floorHeight;
	/**
	 */
	protected GroundType defaultSemantic;
	/**
	 */
	protected AlignedIndoorGround ground;
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		this.id = UUID.randomUUID();
		this.x = (this.RANDOM.nextDouble()-this.RANDOM.nextDouble())*1000.;
		this.y = (this.RANDOM.nextDouble()-this.RANDOM.nextDouble())*1000.;
		this.width = this.RANDOM.nextDouble()*1000.;
		this.height = this.RANDOM.nextDouble()*1000.;
		this.floorHeight = (this.RANDOM.nextDouble()-this.RANDOM.nextDouble())*10.;
		this.defaultSemantic = GroundType.GROUNDTYPE_SINGLETON;
		this.ground = new AlignedIndoorGround(this.id, 
				this.x, this.y,
				this.width, this.height,
				this.floorHeight,
				this.defaultSemantic);
	}
	
	@Override
	public void tearDown() throws Exception {
		this.id = null;
		this.ground = null;
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
		assertEpsilonEquals(this.x, this.ground.getMinX());
	}

	/**
	 */
	public void testGetMinY() {
		assertEpsilonEquals(this.y, this.ground.getMinY());
	}

	/**
	 */
	public void testGetMaxX() {
		assertEpsilonEquals(this.x+this.width, this.ground.getMaxX());
	}

	/**
	 */
	public void testGetMaxY() {
		assertEpsilonEquals(this.y+this.height, this.ground.getMaxY());
	}

	/**
	 */
	public void testGetSizeX() {
		assertEpsilonEquals(this.width, this.ground.getSizeX());
	}
	
	/**
	 */
	public void testGetSizeY() {
		assertEpsilonEquals(this.height, this.ground.getSizeY());
	}
	
	/**
	 */
	public void testGetHeightAt() {
		double _x = (this.RANDOM.nextDouble()-this.RANDOM.nextDouble())*2000.;
		double _y = (this.RANDOM.nextDouble()-this.RANDOM.nextDouble())*2000.;
		if (_x>=this.x && _x<=(this.x+this.width)
			&&
			_y>=this.y && _y<=(this.y+this.height)) {
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
		if (_x>=this.x && _x<=(this.x+this.width)
			&&
			_y>=this.y && _y<=(this.y+this.height)) {
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
		if (_x>=this.x && _x<=(this.x+this.width)
			&&
			_y>=this.y && _y<=(this.y+this.height)) {
			assertSame(this.defaultSemantic, this.ground.getGroundType(_x, _y));
		}
		else {
			assertNull(this.ground.getGroundType(_x, _y));
		}
	}

}