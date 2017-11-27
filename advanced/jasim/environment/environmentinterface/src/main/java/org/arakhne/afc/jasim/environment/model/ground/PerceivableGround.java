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
package org.arakhne.afc.jasim.environment.model.ground;

import javax.vecmath.Vector2d;


/** This interface representes the ground in a environment that is perceivable.
 * <p>
 * Each not-traversable area of the ground changes the potential field of the map.
 * This change is described by a vector which could be:
 * <ul>
 * <li><i>repulsive</i>: the direction of the vector permits to escape from the not-traversable area, and
 * the length of the vector is invertely proportional to the distance to the not-traversable area; or</li>
 * <li><i>attrative</i>: the direction of the vector permits to escape from the not-traversable area, and
 * the length of the vector is invertely proportional to the distance to the not-traversable area.</li>
 * </ul>
 *
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface PerceivableGround extends Ground {

	/** Replies the maximal weight of the repulsive/attractive forces.
	 * 
	 *  @return the maximal force weight
	 */
	public double getMaxForceWeight();

	/** Replies the repulsion vector stored for the specified cell that describe
	 * the inverse direction to the nearest
	 * 
	 * @param x is the coordinate of the cell
	 * @param y is the coordinate of the cell
	 * @return the vector.
	 */
	public Vector2d getRepulsion(double x, double y);
	
	/** Replies the attraction vector stored for the specified cell that describe
	 * the inverse direction to the nearest
	 * 
	 * @param x is the coordinate of the cell
	 * @param y is the coordinate of the cell
	 * @return the vector.
	 */
	public Vector2d getAttraction(double x, double y);

	
}