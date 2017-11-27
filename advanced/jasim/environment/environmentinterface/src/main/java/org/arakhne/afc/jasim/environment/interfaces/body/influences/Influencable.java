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
package org.arakhne.afc.jasim.environment.interfaces.body.influences;

import java.util.UUID;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;

import fr.utbm.set.geom.object.Point1D;
import fr.utbm.set.geom.object.Point1D5;

/** This interface describes a influencable object.
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Influencable {
	
	/**
	 * Replies the identifier of this object.
	 * 
	 * @return the identifier of this object.
	 */
	public UUID getIdentifier();

	/**
	 * Returns the position of this body in its environment
	 * <p>
	 * The position is a 3D environment is a space point.
	 * 
	 * @return the position in the space.
	 */
	public Point3d getPosition3D();
	
	/**
	 * Returns the position of this body in its environment
	 * <p>
	 * The position is a 2.5D environment is a space point.
	 * 
	 * @return the position in space
	 */
	public Point3d getPosition2D5();

	/**
	 * Returns the position of this body in its environment
	 * <p>
	 * The position is a 2D environment is a plan point.
	 * 
	 * @return the position on a plane
	 */
	public Point2d getPosition2D();

	/**
	 * Returns the position of this body in its environment
	 * <p>
	 * The position is a 1.5D environment is a curviline position (x) and
	 * a shifting distance (y).
	 * 
	 * @return the curviline position (x) and a shifting distance (y)
	 */
	public Point1D5 getPosition1D5();

	/**
	 * Returns the position of this body in its environment
	 * <p>
	 * The position is a 1D environment is a curviline position.
	 * 
	 * @return the curviline position
	 */
	public Point1D getPosition1D();

}