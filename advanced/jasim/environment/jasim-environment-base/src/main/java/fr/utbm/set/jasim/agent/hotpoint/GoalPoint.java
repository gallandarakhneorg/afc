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
 * A goal point is a situated point on which an agent is supposed to reach and stay.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class GoalPoint extends HotPoint {

	private static final long serialVersionUID = 5418873795130473838L;

	/**
	 * @param desiredTime is the desired time at which this point must be used by the agent.
	 */
	public GoalPoint(double desiredTime) {
		super(desiredTime);
	}
	
	/**
	 */
	public GoalPoint() {
		super();
	}
	
	/** Replies the tangent of the agent movement when it reach
	 * this goal.
	 * 
	 * @return the tangent in 1D or 1.5D.
	 */
	public abstract Direction1D getTangent1D();

	/** Replies the tangent of the agent movement when it reach
	 * this goal.
	 * 
	 * @return the position in 3D.
	 */
	public abstract Vector3d getTangent3D();

	/** Replies the tangent of the agent movement when it reach
	 * this goal.
	 * 
	 * @return the position in 2D.
	 */
	public abstract Vector2d getTangent2D();
	
}
