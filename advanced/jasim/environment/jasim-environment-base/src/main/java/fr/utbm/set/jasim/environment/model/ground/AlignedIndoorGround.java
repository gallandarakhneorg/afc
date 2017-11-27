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

/** This class representes an indoor ground which has bounds
 * aligned on world axes.
 *
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class AlignedIndoorGround extends AbstractGround {

	/**
	 * @param id is the identifier of this ground.
	 * @param x is the lowest point of the ground (minimum x)
	 * @param y is the lowest point of the ground (minimum y)
	 * @param width is the size along ox axis of the ground 
	 * @param height is the size along oy axis of the ground
	 * @param floorHeight is the height of the floor for this indoor ground.
	 * @param defaultSemantic is the default semantic associated to the ground.
	 */
	public AlignedIndoorGround(UUID id, double x, double y, double width, double height, double floorHeight, GroundType defaultSemantic) {
		super(id,x, y, floorHeight, x+width, y+height, floorHeight, defaultSemantic);
	}
	
	/**
	 * @param id is the identifier of this ground.
	 * @param x is the lowest point of the ground (minimum x)
	 * @param y is the lowest point of the ground (minimum y)
	 * @param width is the size along ox axis of the ground 
	 * @param height is the size along oy axis of the ground
	 * @param defaultSemantic is the default semantic associated to the ground.
	 */
	public AlignedIndoorGround(UUID id, double x, double y, double width, double height, GroundType defaultSemantic) {
		this(id,x,y,width, height, 0., defaultSemantic);
	}

	/**
	 * @param id is the identifier of this ground.
	 * @param x is the lowest point of the ground (minimum x)
	 * @param y is the lowest point of the ground (minimum y)
	 * @param width is the size along ox axis of the ground 
	 * @param height is the size along oy axis of the ground
	 * @param floorHeight is the height of the floor for this indoor ground.
	 */
	public AlignedIndoorGround(UUID id, double x, double y, double width, double height, double floorHeight) {
		this(id,x,y,width, height, floorHeight, null);
	}

	/**
	 * Create a new indoor ground with a floor height of zero.
	 * 
	 * @param id is the identifier of this ground.
	 * @param x is the lowest point of the ground (minimum x)
	 * @param y is the lowest point of the ground (minimum y)
	 * @param width is the size along ox axis of the ground 
	 * @param height is the size along oy axis of the ground
	 */
	public AlignedIndoorGround(UUID id, double x, double y, double width, double height) {
		this(id,x,y,width, height, 0., null);
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getHeightAt(double x, double y) {
		double mx = getMinX();
		double my = getMinY();
		double ax = getMaxX();
		double ay = getMaxY();
		if (x<mx || y<my || x>ax || y>ay)
			return Double.NaN;
		return this.getMinZ();
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public boolean isTraversable(double x, double y) {
		double mx = getMinX();
		double my = getMinY();
		double ax = getMaxX();
		double ay = getMaxY();
		return (x>=mx && y>=my && x<=ax && y<=ay);
	}

}