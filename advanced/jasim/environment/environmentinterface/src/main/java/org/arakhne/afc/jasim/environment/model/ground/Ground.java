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

import java.util.UUID;

import org.arakhne.afc.jasim.environment.semantics.GroundType;

/** This interface representes the ground in a environment.
 *
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Ground {

	/** Replies the identifier of the ground.
	 * 
	 * @return the identifier.
	 */
	public UUID getIdentifier();

	/** Replies the bounds of the ground.
	 * 
	 * @return the minimal x.
	 */
	public double getMinX();

	/** Replies the bounds of the ground.
	 * 
	 * @return the maximal x.
	 */
	public double getMaxX();

	/** Replies the bounds of the ground.
	 * 
	 * @return the minimal y.
	 */
	public double getMinY();

	/** Replies the bounds of the ground.
	 * 
	 * @return the maximal y.
	 */
	public double getMaxY();

	/** Replies the bounds of the ground.
	 * 
	 * @return the minimal z.
	 */
	public double getMinZ();

	/** Replies the bounds of the ground.
	 * 
	 * @return the maximal z.
	 */
	public double getMaxZ();

	/** Replies the size of the ground.
	 * 
	 * @return the size.
	 */
	public double getSizeX();
	
	/** Replies the size of the ground.
	 * 
	 * @return the size.
	 */
	public double getSizeY();
	
	/** Replies the size of the ground.
	 * 
	 * @return the size.
	 */
	public double getSizeZ();

	/** Replies the height of the ground at the specified point.
	 * 
	 * @param x
	 * @param y
	 * @return the height or {@link Double#NaN} if the point is not traversable.
	 */
	public double getHeightAt(double x, double y);
	
	/** Replies if the given point exists on the ground, ie
	 * if the ground is traversable at this point.
	 * 
	 * @param x
	 * @param y
	 * @return <code>true</code> if the ground is traversable
	 * at the given point, otherwise <code>false</code>.
	 */
	public boolean isTraversable(double x, double y);

	/** Replies the type of the ground at the given point.
	 * 
	 * @param x
	 * @param y
	 * @return the type of ground at the given point.
	 */
	public GroundType getGroundType(double x, double y);

}