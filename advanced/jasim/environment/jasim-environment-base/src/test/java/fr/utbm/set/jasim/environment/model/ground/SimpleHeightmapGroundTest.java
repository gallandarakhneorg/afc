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
import fr.utbm.set.jasim.environment.semantics.Semantic;
import fr.utbm.set.math.MathUtil;
import fr.utbm.set.unittest.AbstractRepeatedTestCase;

/** Unit test for SimpleHeightmapGround
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class SimpleHeightmapGroundTest extends AbstractRepeatedTestCase {

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
	protected double z;
	/**
	 */
	protected double sx;
	/**
	 */
	protected double sy;
	/**
	 */
	protected double sz;
	/**
	 */
	protected byte groundZero;
	/**
	 */
	protected byte[][] heights;
	/**
	 */
	protected GroundType defaultSemantic;
	/**
	 */
	protected SimpleHeightmapGround ground;
	/**
	 */
	protected int heightSize;
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		this.id = UUID.randomUUID();
		this.x = (this.RANDOM.nextDouble()-this.RANDOM.nextDouble())*1000.;
		this.y = (this.RANDOM.nextDouble()-this.RANDOM.nextDouble())*1000.;
		this.z = (this.RANDOM.nextDouble()-this.RANDOM.nextDouble())*1000.;
		this.sx = this.RANDOM.nextDouble()*1000.;
		this.sy = this.RANDOM.nextDouble()*1000.;
		this.sz = this.RANDOM.nextDouble()*500.;
		this.groundZero = (byte)this.RANDOM.nextInt(); 
		this.defaultSemantic = GroundType.GROUNDTYPE_SINGLETON;
		
		int sizex = Math.max(2, this.RANDOM.nextInt(100));
		int sizey = Math.max(2, this.RANDOM.nextInt(100));
		this.heights = new byte[sizex][sizey];
		byte delta;
		this.heights[0][0] = (byte)this.RANDOM.nextInt();
		byte min, max;
		min = max = this.heights[0][0];
		for(int _x=1; _x<sizex; ++_x) {
			delta = (byte)((this.RANDOM.nextDouble() - this.RANDOM.nextDouble()) * 50);
			this.heights[_x][0] = (byte)(this.heights[_x-1][0] + delta);
			if (this.heights[_x][0]<min) min = this.heights[_x][0];
			if (this.heights[_x][0]>max) max = this.heights[_x][0];
		}
		for(int _y=1; _y<sizey; ++_y) {
			delta = (byte)((this.RANDOM.nextDouble() - this.RANDOM.nextDouble()) * 50);
			this.heights[0][_y] = (byte)(this.heights[0][_y-1] + delta);
			if (this.heights[0][_y]<min) min = this.heights[0][_y];
			if (this.heights[0][_y]>max) max = this.heights[0][_y];
		}
		for(int _x=1; _x<sizex; ++_x) {
			for(int _y=1; _y<sizey; ++_y) {
				delta = (byte)((this.RANDOM.nextDouble() - this.RANDOM.nextDouble()) * 50);
				this.heights[_x][_y] = (byte)(this.heights[_x-1][_y-1] + delta);
				if (this.heights[_x][_y]<min) min = this.heights[_x][_y];
				if (this.heights[_x][_y]>max) max = this.heights[_x][_y];
			}
		}
		
		this.heightSize = max - min;
		
		this.ground = new SimpleHeightmapGround(
				this.id,
				this.x, this.y, this.z,
				this.x+this.sx, this.y+this.sy, this.z+this.sz,
				this.groundZero,
				this.heights,
				this.defaultSemantic);
	}

	/**
	 */
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
		assertEquals(this.x, this.ground.getMinX());
	}

	/**
	 */
	public void testGetMinY() {
		assertEquals(this.y, this.ground.getMinY());
	}

	/**
	 */
	public void testGetMaxX() {
		assertEquals(this.x+this.sx, this.ground.getMaxX());
	}

	/**
	 */
	public void testGetMaxY() {
		assertEquals(this.y+this.sy, this.ground.getMaxY());
	}

	/**
	 */
	public void testGetSizeX() {
		assertEpsilonEquals(this.sx, this.ground.getSizeX());
	}
	
	/**
	 */
	public void testGetSizeY() {
		assertEpsilonEquals(this.sy, this.ground.getSizeY());
	}
	
	/**
	 */
	public void testGetByteHeightValueAt() {
		int ix = this.RANDOM.nextInt(this.heights.length-1);
		int iy = this.RANDOM.nextInt(this.heights[0].length-1);
		assertEquals(this.heights[ix][iy], this.ground.getByteHeightValueAt(iy, ix));
	}
	
	/**
	 */
	public void testGetDoubleHeightValueAt() {
		int ix = this.RANDOM.nextInt(this.heights.length-1);
		int iy = this.RANDOM.nextInt(this.heights[0].length-1);
		double int2double = this.sz / this.heightSize;
		double height = (this.heights[ix][iy]+128)*int2double+this.z;
		assertEpsilonEquals(height, this.ground.getDoubleHeightValueAt(iy, ix));
	}
	
	/**
	 */
	public void testGetCellSizeX() {
		assertEpsilonEquals(this.sx / this.heights.length, this.ground.getCellSizeX());
	}
	
	/**
	 */
	public void testGetCellSizeY() {
		assertEpsilonEquals(this.sy / this.heights[0].length, this.ground.getCellSizeY());
	}

	/**
	 */
	public void testGetHeightAt_Inside() {
		int ix = this.RANDOM.nextInt(this.heights.length-1);
		int iy = this.RANDOM.nextInt(this.heights[0].length-1);

		double int2double = this.sz / this.heightSize;
		
		int byteHeight11 = this.heights[ix][iy]+128;
		int byteHeight12 = this.heights[ix][iy+1]+128;
		int byteHeight21 = this.heights[ix+1][iy]+128;
		int byteHeight22 = this.heights[ix+1][iy+1]+128;
		
		double z11 = byteHeight11*int2double + this.z;
		double z12 = byteHeight12*int2double + this.z;
		double z21 = byteHeight21*int2double + this.z;
		double z22 = byteHeight22*int2double + this.z;
		
		double cellSizeX = this.sx / this.heights.length;
		double cellSizeY = this.sy / this.heights[0].length;
		
		double _x = this.x + (ix+Math.random()) * cellSizeX;
		double _y = this.y + (iy+Math.random()) * cellSizeY;
		
		double denom = cellSizeX*cellSizeY;		
		double x11 = this.x + ix * cellSizeX;
		double y11 = this.y + iy * cellSizeY;
		double x22 = this.x + (ix+1) * cellSizeX;
		double y22 = this.y + (iy+1) * cellSizeY;
		double dx1 = _x - x11;
		double dx2 = x22 - _x;
		double dy1 = _y - y11;
		double dy2 = y22 - _y;
		
		double expectedZ =
			   z11/denom * dx2*dy2
			+  z21/denom * dx1*dy2
			+  z12/denom * dx2*dy1
			+  z22/denom * dx1*dy1;
		
		assertNumber(expectedZ);
		
		double computedZ = this.ground.getHeightAt(_x, _y);
		
		if (  byteHeight11>=(this.groundZero+128)
			&&byteHeight12>=(this.groundZero+128)
			&&byteHeight21>=(this.groundZero+128)
			&&byteHeight22>=(this.groundZero+128)) {
			// Traversable
			assertNumber(computedZ);

			assertTrue(computedZ >= MathUtil.min(z11,z12,z21,z22));
			assertTrue(computedZ <= MathUtil.max(z11,z12,z21,z22));
					
			assertEpsilonEquals(expectedZ, computedZ);
		}
		else {
			// Not traversable
			assertNaN(computedZ);
		}
	}
	
	/**
	 */
	public void testGetHeightAt_Outside() {
		double outsideX = this.x - this.RANDOM.nextDouble() - this.RANDOM.nextDouble();
		double outsideY = this.y + this.sy + this.RANDOM.nextDouble() + this.RANDOM.nextDouble();
		assertNaN(this.ground.getHeightAt(outsideX, outsideY));
	}

	/**
	 */
	public void testIsTraversable() {
		boolean desiredTraversable = false;

		int ix = (this.RANDOM.nextInt(100)-this.RANDOM.nextInt(100));
		int iy = (this.RANDOM.nextInt(100)-this.RANDOM.nextInt(100));
		
		double cellx = this.sx / (this.heights.length-1);
		double celly = this.sy / (this.heights[0].length-1);
		
		double _x = this.x + ix * cellx + this.RANDOM.nextDouble()*cellx;
		double _y = this.y + iy * celly + this.RANDOM.nextDouble()*celly;

		boolean computedTraversable = this.ground.isTraversable(_x, _y);			
		
		if (ix>=0 && ix<(this.heights.length-1) && iy>=0 && iy<(this.heights[ix].length-1)) {

			if (this.heights[ix][iy]>=this.groundZero
			    &&this.heights[ix+1][iy]>=this.groundZero
			    &&this.heights[ix][iy+1]>=this.groundZero
			    &&this.heights[ix+1][iy+1]>=this.groundZero) {
				desiredTraversable = true;
			}
		}

		assertEquals(desiredTraversable, computedTraversable);
	}

	/**
	 */
	public void testGetGroundType() {
		Semantic desiredSemantic = null;

		int ix = (this.RANDOM.nextInt(100)-this.RANDOM.nextInt(100));
		int iy = (this.RANDOM.nextInt(100)-this.RANDOM.nextInt(100));
		
		double cellx = this.sx / (this.heights.length-1);
		double celly = this.sy / (this.heights[0].length-1);
		
		double _x = this.x + ix * cellx + this.RANDOM.nextDouble()*cellx;
		double _y = this.y + iy * celly + this.RANDOM.nextDouble()*celly;

		Semantic computedSemantic = this.ground.getGroundType(_x, _y);			
		
		if (ix>=0 && ix<(this.heights.length-1) && iy>=0 && iy<(this.heights[ix].length-1)) {

			if (this.heights[ix][iy]>=this.groundZero
			    && this.heights[ix+1][iy]>=this.groundZero
			    && this.heights[ix][iy+1]>=this.groundZero
			    && this.heights[ix+1][iy+1]>=this.groundZero) {
				desiredSemantic = this.defaultSemantic;
			}
		}

		if (desiredSemantic==null)
			assertNull(computedSemantic);
		else
			assertSame(desiredSemantic, computedSemantic);
	}

}