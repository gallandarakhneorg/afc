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

/** This class representes an indoor ground.
 *
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see AlignedIndoorGround
 */
@Deprecated
public class IndoorGround extends AlignedIndoorGround {

	/**
	 * @param id is the identifier of this ground.
	 * @param x is the lowest point of the ground (minimum x)
	 * @param y is the lowest point of the ground (minimum y)
	 * @param width is the size along ox axis of the ground 
	 * @param height is the size along oy axis of the ground
	 * @param floorHeight is the height of the floor for this indoor ground.
	 * @param defaultSemantic is the default semantic associated to the ground.
	 */
	public IndoorGround(UUID id, double x, double y, double width, double height, double floorHeight, GroundType defaultSemantic) {
		super(id,x, y, width, height, floorHeight, defaultSemantic);
	}
	
	/**
	 * @param id is the identifier of this ground.
	 * @param x is the lowest point of the ground (minimum x)
	 * @param y is the lowest point of the ground (minimum y)
	 * @param width is the size along ox axis of the ground 
	 * @param height is the size along oy axis of the ground
	 * @param defaultSemantic is the default semantic associated to the ground.
	 */
	public IndoorGround(UUID id, double x, double y, double width, double height, GroundType defaultSemantic) {
		super(id,x,y,width, height, defaultSemantic);
	}

	/**
	 * @param id is the identifier of this ground.
	 * @param x is the lowest point of the ground (minimum x)
	 * @param y is the lowest point of the ground (minimum y)
	 * @param width is the size along ox axis of the ground 
	 * @param height is the size along oy axis of the ground
	 * @param floorHeight is the height of the floor for this indoor ground.
	 */
	public IndoorGround(UUID id, double x, double y, double width, double height, double floorHeight) {
		super(id,x,y,width, height, floorHeight);
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
	public IndoorGround(UUID id, double x, double y, double width, double height) {
		super(id,x,y,width, height);
	}

}