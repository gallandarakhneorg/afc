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

import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

import fr.utbm.set.geom.object.Direction1D;

/**
 * A way point is a situated point on which an agent is supposed to pass.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class WayPoint extends HotPoint {

	/**
	 */
	private static final long serialVersionUID = -3132298263844375509L;
	
	/** Is the velocity at which the point should be reached.
	 */
	private final double velocity;

	/**
	 * @param desiredTime is the desired time at which this point must be used by the agent.
	 * @param velocity is the desired velocity that the agent should have
	 * when it reach this waypoint.
	 */
	public WayPoint(double desiredTime, double velocity) {
		super(desiredTime);
		this.velocity = velocity;
	}
	
	/**
	 * @param velocity is the desired velocity that the agent should have
	 * when it reach this waypoint.
	 */
	public WayPoint(double velocity) {
		super();
		this.velocity = velocity;
	}
	
	/** Replies the tangent of the agent movement when it reach
	 * this waypoint.
	 * 
	 * @return the position in 3D.
	 */
	public abstract Vector3d getTangent3D();

	/** Replies the tangent of the agent movement when it reach
	 * this waypoint.
	 * 
	 * @return the position in 2D.
	 */
	public abstract Vector2d getTangent2D();
	
	/** Replies the tangent of the agent movement when it reach
	 * this waypoint.
	 * 
	 * @return the position in 1D or 1.5D.
	 */
	public abstract Direction1D getTangent1D();

	/** Replies the desired velocity that the agent should have
	 * when it reach this waypoint..
	 * 
	 * @return the desired speed, or {@link Double#NaN} if none.
	 */
	public double getVelocity() {
		return this.velocity;
	}

}
