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

import java.net.URL;

import fr.utbm.set.jasim.environment.model.ground.Ground;

/** This class representes a ground stored as a height map.
 * <p>
 * A height map is a matrix of integers between -128 and 127.
 * The matrix discretizes the ground area into regular cells.
 * The height of a point (x,y) is computed with a bilinear interpolation
 * with the four corners of the cell in which (x,y) lies.
 * <p>
 * A point is traversable if its coordinates (x,y) are on the ground
 * (ie, between the min and max values given at the constructor) AND
 * if the cell of (x,y) is traversable.
 * A cell is traversable if one of the corner as a height greater or equals
 * to the ground zero level (given as constructor's parameter).  
 *
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface HeightmapGround extends Ground {

	/** Replies the name of the image file that permits to create this ground.
	 * 
	 * @return the filename
	 */
	public URL getImageFile();
	
	/** Set the name of the image file that permits to create this ground.
	 * 
	 * @param filename
	 */
	public void setImageFile(URL filename);

	/** {@inheritDoc}
	 */
	@Override
	public double getMinZ();

	/** {@inheritDoc}
	 */
	@Override
	public double getMaxZ();
	
	/** Replies the raw height under which the map is not traversable.
	 * 
	 * @return the raw height under which the map is not traversable.
	 */
	public byte getGroundZero();
	
}