/* 
 * $Id$
 * 
 * Copyright (c) 2004-08, Multiagent Team - Systems and Transportation Laboratory (SeT)
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
package fr.utbm.set.jasim.agent.hotpoint;

import java.io.Serializable;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;

import fr.utbm.set.geom.object.Point1D;
import fr.utbm.set.geom.object.Point1D5;

/**
 * A hot point is a situated point used by agents to do something.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class HotPoint implements Serializable {
	
	private static final long serialVersionUID = 3468556513679021292L;
	
	/** Is the time a which the point should be used.
	 */
	private final double time;

	/**
	 * @param desiredTime is the desired time at which this point must be used by the agent.
	 */
	public HotPoint(double desiredTime) {
		this.time = desiredTime;
	}

	/**
	 */
	public HotPoint() {
		this(Double.NaN);
	}
	
	/** Replies the position of this point.
	 * 
	 * @return the position in 3D.
	 */
	public abstract Point3d getPosition3D();

	/** Replies the position of this point.
	 * 
	 * @return the position in 1D.
	 */
	public abstract Point1D getPosition1D();

	/** Replies the position of this point.
	 * 
	 * @return the position in 1.5D.
	 */
	public abstract Point1D5 getPosition1D5();

	/** Replies the position of this point.
	 * 
	 * @return the position in 2D.
	 */
	public abstract Point2d getPosition2D();
	
	/** Replies the desired time at which this point must be used.
	 * 
	 * @return the time, or {@link Double#NaN} if none.
	 */
	public double getTime() {
		return this.time;
	}

}
